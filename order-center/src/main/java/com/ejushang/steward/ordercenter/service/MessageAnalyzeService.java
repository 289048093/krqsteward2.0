package com.ejushang.steward.ordercenter.service;


import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.grabber.Message;
import com.ejushang.steward.ordercenter.domain.OriginalOrder;
import com.ejushang.steward.ordercenter.domain.OriginalRefund;
import com.ejushang.steward.ordercenter.domain.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * User: liubin
 * Date: 14-4-14
 * Time: 上午9:19
 */
@Service
@Transactional
public class MessageAnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(MessageAnalyzeService.class);

    @Autowired
    private GeneralDAO generalDAO;


    @Autowired
    private OrderAnalyzeService orderAnalyzeService;

    @Autowired
    private RefundAnalyzeService refundAnalyzeService;

    @Autowired
    private OriginalOrderService originalOrderService;

    @Autowired
    private OriginalRefundService originalRefundService;


    @Autowired
    private StorageService storageService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void handleMessage(boolean firstTime, List<Message> messageList) {
        if(messageList.isEmpty()) {
            log.info("队列中数据为空");
        } else {
            log.info("从队列中读取到{}条数据", messageList.size());
        }
        List<OriginalOrder> originalOrders = new ArrayList<OriginalOrder>();
        List<OriginalRefund> originalRefunds = new ArrayList<OriginalRefund>();
        for(Message message : messageList) {
            switch (message.getMessageType()) {
                case ORDER: {
                    originalOrders.addAll((List<OriginalOrder>) message.getMessages());
                    break;

                } case REFUND: {
                    originalRefunds.addAll((List<OriginalRefund>) message.getMessages());
                    break;

                } case ORDER_ID: {
                    List<Integer> originalOrderIds = (List<Integer>) message.getMessages();
                    log.info("读取到解析原始订单请求的id集合,size:" + originalOrderIds.size());
                    List<OriginalOrder> tryAnalyzeOriginalOrders = new ArrayList<OriginalOrder>(originalOrderIds.size());
                    for(Integer originalOrderId : originalOrderIds) {
                        OriginalOrder originalOrder = originalOrderService.get(originalOrderId);
                        if(originalOrder != null) {
                            tryAnalyzeOriginalOrders.add(originalOrder);
                        }
                    }
                    log.info("根据解析原始订单请求的id集合读取到原始订单对象集合,size:" + tryAnalyzeOriginalOrders.size());
                    if(!tryAnalyzeOriginalOrders.isEmpty()) {
                        originalOrders.addAll(tryAnalyzeOriginalOrders);
                    }
                    break;

                } default: {
                    break;
                }
            }
        }


        if(firstTime) {
            log.info("启动后第一次运行消息分析程序");
            //判断是第一次运行程序,读取OriginalOrder表和OriginalRefund表中processed为false的记录
            //防止出现因断电导致队列中的数据丢失的情况
            List<OriginalOrder> unprocessedOriginalOrders = originalOrderService.findUnprocessedOriginalOrders();
            log.info("从数据库读取到还未解析的原始订单, size:" + unprocessedOriginalOrders.size());
            //将数据库中的与队列中的合并,id相同的即忽略
            Set<Integer> idSet = new HashSet<Integer>();
            for(OriginalOrder originalOrder : originalOrders) {
                idSet.add(originalOrder.getId());
            }
            for(OriginalOrder originalOrder : unprocessedOriginalOrders) {
                if(!idSet.contains(originalOrder.getId())) {
                    originalOrders.add(originalOrder);
                }
            }

            List<OriginalRefund> unprocessedOriginalRefunds = originalRefundService.findUnprocessedOriginalRefunds();
            log.info("从数据库读取到还未解析的原始退款, size:" + unprocessedOriginalRefunds.size());
            //将数据库中的与队列中的合并,id相同的即忽略
            idSet = new HashSet<Integer>();
            for(OriginalRefund originalRefund : originalRefunds) {
                idSet.add(originalRefund.getId());
            }
            for(OriginalRefund originalRefund : unprocessedOriginalRefunds) {
                if(!idSet.contains(originalRefund.getId())) {
                    originalRefunds.add(originalRefund);
                }
            }
        }

        if(!originalOrders.isEmpty()) {
            analyzeOriginalOrders(originalOrders);
        }

        if(!originalRefunds.isEmpty()) {
            analyzeOriginalRefunds(originalRefunds);
        }
    }

    /**
     * 分析原始订单列表
     * @param originalOrders
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyzeOriginalOrders(List<OriginalOrder> originalOrders) {

        log.info("开始分析原始订单, size:" + originalOrders.size());

        /**
         * 处理步骤:
         * 1.查看集合里有没有已处理或已废弃的原始订单,如果有的话删除这些原始订单
         * 2.对原始订单按照修改时间升序排列
         * 3.进行去重, 如果原始订单id相同, 从队列里排除掉靠前的原始订单.
         * 4.判断原始订单是对系统内的订单进行新增还是更新.
         * 5.如果是新增,则进行对应订单新增的操作(拆单).并且根据原始订单的状态设置新增的订单和订单项状态
         * 6.如果是更新,根据原始订单的状态修改订单和订单项状态,并且更新备注信息
         * 7.处理完毕,批量更新原始订单的processed为true
         *
         */

        //删除不需要处理的原始订单
        removeProcessedOrDiscardOriginalOrders(originalOrders);
        if(originalOrders.isEmpty()) return;

        //按照修改时间升序排列
        Collections.sort(originalOrders, new Comparator<OriginalOrder>() {
            @Override
            public int compare(OriginalOrder o1, OriginalOrder o2) {
                return o1.getModifiedTime().compareTo(o2.getModifiedTime());
            }
        });

        //查询系统所有产品以及其对应的库存集合
        Map<Integer, List<Storage>> productStorageCache = storageService.findProductsWithStorage(null);
        log.info("查询出的拥有库存的产品数量: " + productStorageCache.size());
        if(productStorageCache.isEmpty()) {
            log.error("查询不到任何产品的库存信息,请查看数据库数据是否正确");
            return;
        }
        //记录所有的id
        Set<Integer> allOriginalOrderIdSet = new HashSet<Integer>(originalOrders.size());
        for(OriginalOrder originalOrder : originalOrders) {
            allOriginalOrderIdSet.add(originalOrder.getId());
        }

        //从集合中删除重复的
        removeDuplicateOriginalOrders(originalOrders);

        //处理失败的id集合
        Set<Integer> processFailedOriginalOrderIdSet = new HashSet<Integer>();
        //遍历原始订单,根据外部订单号判断该订单属于更新还是新增
        for(Iterator<OriginalOrder> iterator = originalOrders.iterator(); iterator.hasNext();) {
            OriginalOrder originalOrder = iterator.next();
            String errorMsg = null;
            try {

                orderAnalyzeService.analyze(originalOrder, productStorageCache);

            } catch (StewardBusinessException e) {
                //处理失败
                errorMsg = String.format("订单分析失败,originalOrderId[%d]:%s", originalOrder.getId(), e.getMessage());
                log.error(errorMsg, e.getMessage());
                processFailedOriginalOrderIdSet.add(originalOrder.getId());
            } catch (Throwable e) {
                //处理失败
                errorMsg = String.format("进行订单分析的时候发生未知错误,originalOrderId[%d]:%s", originalOrder.getId(), e.getMessage());
                log.error(errorMsg, e);
                processFailedOriginalOrderIdSet.add(originalOrder.getId());
            }

            //记录解析日志
            originalOrderService.createAnalyzeLog(originalOrder, errorMsg == null, errorMsg == null ? "解析成功" : errorMsg);
        }

        if(log.isInfoEnabled()) {
            log.info("订单分析处理结束,总数量[{}], 处理成功数量[{}], 处理失败数量[{}], 跳过重复数量[{}]", new Object[]{allOriginalOrderIdSet.size(),
                    originalOrders.size() - processFailedOriginalOrderIdSet.size(), processFailedOriginalOrderIdSet.size(), allOriginalOrderIdSet.size() - originalOrders.size()});
        }

        //所有ID集合 - 处理失败集合 = 处理成功集合(包括重复的)
        allOriginalOrderIdSet.removeAll(processFailedOriginalOrderIdSet);

        //修改原始订单为已处理
        int[] ids = convertSetToIntArray(allOriginalOrderIdSet);
        updateProcessed(ids, "t_original_order");

    }

    /**
     * 删除不需要处理的原始订单
     * @param originalOrders
     */
    private void removeProcessedOrDiscardOriginalOrders(List<OriginalOrder> originalOrders) {
        for(Iterator<OriginalOrder> iterator = originalOrders.iterator(); iterator.hasNext();) {
            OriginalOrder originalOrder = iterator.next();
            if(originalOrder.getProcessed() || originalOrder.getDiscard()) {
                iterator.remove();
            }
        }
    }

    /**
     * 删除重复的原始订单,即同一主键的原始订单只会在集合中出现一次,保留时间靠后的
     * @param originalOrders
     */
    private void removeDuplicateOriginalOrders(List<OriginalOrder> originalOrders) {
        log.info("现在开始删除原始订单列表中重复的原始订单,删除前的队列数量, size:" + originalOrders.size());
        //降序遍历原始订单列表,如果原始订单编号出现重复,删除时间靠前的原始订单
        Set<Integer> originalOrderIdSet = new HashSet<Integer>();
        for(ListIterator<OriginalOrder> iterator = originalOrders.listIterator(originalOrders.size()); iterator.hasPrevious();) {
            OriginalOrder originalOrder = iterator.previous();
            if(originalOrderIdSet.contains(originalOrder.getId())) {
                iterator.remove();
            } else {
                originalOrderIdSet.add(originalOrder.getId());
            }
        }
        log.info("删除后的队列数量, size:" + originalOrders.size());
    }



    /**
     * 分析原始退款列表
     * @param originalRefunds
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyzeOriginalRefunds(List<OriginalRefund> originalRefunds) {

        log.info("开始分析原始退款, size:" + originalRefunds.size());

        //按照修改时间升序排列
        Collections.sort(originalRefunds, new Comparator<OriginalRefund>() {
            @Override
            public int compare(OriginalRefund o1, OriginalRefund o2) {
                return o1.getModified().compareTo(o2.getModified());
            }
        });

        /**
         * 处理步骤:
         * 1.首先去重, 如果原始退款id相同, 从队列里排除掉靠前的原始退款.
         * 2.判断是京东还是淘宝的退款(京东只有售前)
         * 3.判断是新增还是更新
         * 4.判断是售前退款还是售后(京东只有售前)
         * 5.如果是天猫并且是新增,根据退款中的子订单号查订单项,如果查询为空,则查预收款记录.然后根据订单项或预收款生成退款记录
         * 6.如果是京东并且是新增,根据退款中的交易号查订单,如果查询为空,则查预收款记录.然后根据订单项和预收款生成退款记录
         * 7.如果是更新,则根据原始退款信息修改退款记录的信息.
         * 8.新增和修改退款记录的时候,根据退款记录的类型,状态,阶段对退款记录进行生效操作
         * 9.处理完毕,批量更新原始退款的processed为true
         *
         */

        //记录所有的id
        Set<Integer> allOriginalRefundIdSet = new HashSet<Integer>(originalRefunds.size());
        for(OriginalRefund originalRefund : originalRefunds) {
            allOriginalRefundIdSet.add(originalRefund.getId());
        }

        //从集合中删除重复的
        removeDuplicateOriginalRefunds(originalRefunds);

        //处理失败的id集合
        Set<Integer> processFailedOriginalRefundIdSet = new HashSet<Integer>();
        //遍历原始订单,根据外部订单号判断该订单属于更新还是新增
        for(Iterator<OriginalRefund> iterator = originalRefunds.iterator(); iterator.hasNext();) {
            OriginalRefund originalRefund = iterator.next();
            try {

                refundAnalyzeService.analyze(originalRefund);

            } catch (StewardBusinessException e) {
                //处理失败
                String errorMsg = String.format("原始退款分析失败,originalRefundId[%d]:%s", originalRefund.getId(), e.getMessage());
                log.error(errorMsg, e.getMessage());
                processFailedOriginalRefundIdSet.add(originalRefund.getId());
            } catch (Throwable e) {
                //处理失败
                String errorMsg = String.format("进行原始退款分析的时候发生未知错误,originalRefundId[%d]:%s", originalRefund.getId(), e.getMessage());
                log.error(errorMsg, e);
                processFailedOriginalRefundIdSet.add(originalRefund.getId());
            }
        }

        if(log.isInfoEnabled()) {
            log.info("原始退款分析处理结束,总数量[{}], 处理成功数量[{}], 处理失败数量[{}], 跳过重复数量[{}]", new Object[]{allOriginalRefundIdSet.size(),
                    originalRefunds.size() - processFailedOriginalRefundIdSet.size(), processFailedOriginalRefundIdSet.size(), allOriginalRefundIdSet.size() - originalRefunds.size()});
        }

        //所有ID集合 - 处理失败集合 = 处理成功集合(包括重复的)
        allOriginalRefundIdSet.removeAll(processFailedOriginalRefundIdSet);

        //修改原始退款为已处理
        int[] ids = convertSetToIntArray(allOriginalRefundIdSet);
        updateProcessed(ids, "t_original_refund");
    }

    /**
     * 删除重复的原始退款,即同一主键的原始退款只会在集合中出现一次,保留时间靠后的
     * @param originalRefunds
     */
    private void removeDuplicateOriginalRefunds(List<OriginalRefund> originalRefunds) {
        log.info("现在开始删除原始退款列表中重复的原始退款,删除前的队列数量, size:" + originalRefunds.size());
        //降序遍历原始订单列表,如果原始订单编号出现重复,删除时间靠前的原始订单
        Set<Integer> originalRefundIdSet = new HashSet<Integer>();
        for(ListIterator<OriginalRefund> iterator = originalRefunds.listIterator(originalRefunds.size()); iterator.hasPrevious();) {
            OriginalRefund originalRefund = iterator.previous();
            if(originalRefundIdSet.contains(originalRefund.getId())) {
                iterator.remove();
            } else {
                originalRefundIdSet.add(originalRefund.getId());
            }
        }
        log.info("删除后的队列数量, size:" + originalRefunds.size());
    }

    /**
     * Set集合转成数组
     * @param set
     * @return
     */
    private int[] convertSetToIntArray(Set<Integer> set) {
        int[] results = new int[set.size()];
        int i=0;
        for(Integer id : set) {
            results[i++] = id;
        }
        return results;
    }

    /**
     * 修改原始订单的discard字段
     * @param ids
     */
    public void updateOriginalOrderDiscard(int[] ids, final boolean discard) {
        int count = batchUpdateProcessed(ids, new BatchUpdateProcessedTask() {
            @Override
            public int run(int[] ids) {
                StringBuilder sql = new StringBuilder("update t_original_order set discard = " + String.valueOf(discard) + " where id in (");
                for (int i = 0; i < ids.length; i++) {
                    sql.append(ids[i]).append(",");
                }
                sql.replace(sql.length()-1, sql.length(), ")");
                return generalDAO.getSession().createSQLQuery(sql.toString()).executeUpdate();
            }
        });

        if(count != ids.length) {
            log.error(String.format("批量更新原始订单的discard字段的时候出错,传进来的id数量[%d],修改成功的数量[%d], id列表[%s]", ids.length, count, Arrays.toString(ids)));
        }
    }


    /**
     * 修改为已处理
     * @param ids
     */
    public void updateProcessed(int[] ids, final String tableName) {
        int count = batchUpdateProcessed(ids, new BatchUpdateProcessedTask() {
            @Override
            public int run(int[] ids) {
                StringBuilder sql = new StringBuilder("update " + tableName + " set processed = true where id in (");
                for (int i = 0; i < ids.length; i++) {
                    sql.append(ids[i]).append(",");
                }
                sql.replace(sql.length()-1, sql.length(), ")");
                return generalDAO.getSession().createSQLQuery(sql.toString()).executeUpdate();
            }
        });

        if(count != ids.length) {
            log.error(String.format("批量更新%s为已处理的时候出错,传进来的id数量[%d],修改成功的数量[%d], id列表[%s]", tableName, ids.length, count, Arrays.toString(ids)));
        }
    }


    private int batchUpdateProcessed(int[] ids, BatchUpdateProcessedTask task) {
        if(ids == null || ids.length == 0) return 0;
        //一次执行sql更新的最大数量不超过20
        int maxLength = 20;
        if(ids.length <= maxLength) {
            return task.run(ids);
        }
        int count = 0;
        for(int i=0; i<=(ids.length / maxLength); i++) {
            int start = i * maxLength;
            int end = Math.min(start + maxLength, ids.length);
            if(start == end) continue;
            count += task.run(Arrays.copyOfRange(ids, start, end));
        }
        return count;
    }

    private static interface BatchUpdateProcessedTask {

        int run(int[] ids);

    }

}


