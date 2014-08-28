package com.ejushang.steward.logisticscenter.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.SessionUtils;
import com.ejushang.steward.logisticscenter.domain.LogisticsPrintInfo;
import com.ejushang.steward.logisticscenter.util.NumGeneratorUtil;
import com.ejushang.steward.ordercenter.domain.Invoice;
import com.ejushang.steward.ordercenter.service.InvoiceService;
import com.ejushang.steward.ordercenter.service.OrderService;
import com.ejushang.steward.ordercenter.util.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * User: Blomer
 * Date: 14-4-8
 * Time: 下午2:09
 */
@Service
public class LogisticsPrintInfoService {
    private static final Logger log = LoggerFactory.getLogger(LogisticsPrintInfoService.class);

    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoiceService invoiceService;

    /**
     * 查询所有的物流公司信息
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<LogisticsPrintInfo> list(Page page) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoServicez中的list方法,参数page[%s]", page.toString()));
        }
        //仓库管理人员只能设置自己的物流单
        StringBuilder hql = new StringBuilder("select lpi from LogisticsPrintInfo lpi where 1=1 ");
        Employee employee = SessionUtils.getEmployee();                                        // sort by lpi.id
        List<Object> params = new ArrayList<Object>();
        if (employee.isRepositoryEmployee()) {
            hql.append(" and lpi.repositoryId = (select rc.repositoryId from RepositoryCharger rc where rc.chargerId=?)");
            params.add(employee.getId());
        }
        //noinspection unchecked
        return generalDAO.query(hql.toString(), page, params.toArray());
    }

    /**
     * 根据Id查询细节
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public LogisticsPrintInfo getById(Integer id) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoService中的getById方法,参数id[%d]", id));
        }
        return generalDAO.get(LogisticsPrintInfo.class, id);
    }

    /**
     * 根据name查询细节
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public LogisticsPrintInfo getByName(String name) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoService中的getByName方法,参数name[%s]", name));
        }
//        Search search = new Search(LogisticsPrintInfo.class);
//        search.addFilterEqual("name", name);
        Employee employee = SessionUtils.getEmployee();
//        if (employee.isRepositoryEmployee()) {
//            search.addFilterEqual("repository.chargePersonId", employee.getId());
//        } else {
//            search.addFilterNull("repositoryId");
//        }
//        String hql1= "select lpi from LogisticsPrintInfo lpi " +
//                "where lpi.name=:name and lpi.repositoryId = " +
//                "(select rc.repositoryId from RepositoryCharger rc where rc.chargerId=:cid)";
        StringBuilder hql = new StringBuilder("select lpi from LogisticsPrintInfo lpi where 1=1");
        List<Object> params = new ArrayList<Object>();
        if (StringUtils.isEmpty(name)) {
            throw new StewardBusinessException("物流名称不能为空");
        }
        hql.append(" and lpi.name= ? ");
        params.add(name);
        if (employee.isRepositoryEmployee()) {
            hql.append(" and lpi.repositoryId = (select rc.repositoryId from RepositoryCharger rc where rc.chargerId= ? )");
            params.add(employee.getId());
        }else{
            hql.append("and lpi.repositoryId is null");
        }

        Query query = generalDAO.getSession().createQuery(hql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return (LogisticsPrintInfo) query.uniqueResult();

//        return (LogisticsPrintInfo) generalDAO.searchUnique(search);


    }

    /**
     * 增加或更新物流
     *
     * @param logisticsPrintInfo
     * @return
     */
    @Transactional
    public void save(LogisticsPrintInfo logisticsPrintInfo) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoService中的save方法,参数logisticsPrintInfo[%s]", logisticsPrintInfo.toString()));
        }
        if (this.isExist(logisticsPrintInfo.getId(), logisticsPrintInfo.getName(), logisticsPrintInfo.getRepositoryId())) {
            log.error(String.format("该仓库的物流公司[%s]已经存在！", logisticsPrintInfo.getName()));
            throw new StewardBusinessException(String.format("该仓库的物流公司[%s]已经存在！", logisticsPrintInfo.getName()));
        }
        generalDAO.saveOrUpdate(logisticsPrintInfo);
    }

    /**
     * 判读有没有重复的物流公司
     *
     * @param id
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    private Boolean isExist(Integer id, String name, Integer repoId) {
        if (log.isInfoEnabled()) {
            log.info(String.format("LogisticsPrintInfoService中的isDoubleExist方法,参数id[%d],name[%s]", id, name));
        }
        Search search = new Search(LogisticsPrintInfo.class);
        search.addFilterEqual("name", name);
        if (repoId != null) {
            search.addFilterEqual("repositoryId", repoId);
        } else {
            search.addFilterNull("repositoryId");
        }
        if (id != null) {
            search.addFilterNotEqual("id", id);
        }
        int count = generalDAO.count(search);
        return count > 0;
    }


    /**
     * 物流联想号生成
     *
     * @param orderIds     订单ID数组
     * @param shippingComp 物流公司
     * @param intNo        基数
     * @param isCover      是否覆盖
     */
    @Transactional
    public void updateOrderShipping(Integer orderIds[], String shippingComp, String intNo, String isCover) {
        if (log.isInfoEnabled()) {
            log.info("OrderService中的updateOrderShipping方法，参数orderIds:" + Arrays.toString(orderIds) + ",参数ShippingComp:" + shippingComp + ",参数intNo:" + intNo + ",参数isCover:" + isCover);
        }
        LogisticsPrintInfo logisticsPrint = getByName(shippingComp);
        if (logisticsPrint == null) {
            throw new StewardBusinessException("没有存在的快递公司");
        }
        List<String> numList = NumGeneratorUtil.getShippingNums(logisticsPrint, intNo, orderIds.length);
        assertShippingExist(orderIds, numList, isCover);
        for (int i = 0; i < orderIds.length; i++) {
            Invoice invoice = invoiceService.findInvoiceByOrderId(orderIds[i]);
            if (invoice == null) {
                throw new StewardBusinessException(String.format("订单[id=%d]没有收货信息", orderIds[i]));
            }
            if (!invoice.getShippingComp().equals(shippingComp)) {
                throw new StewardBusinessException("只能批量联想同一个物流公司的订单编号");
            }
            String destShippingNo = numList.get(i);
            if (destShippingNo.equals(invoice.getShippingNo())) {
                continue;
            }
            if (!StringUtils.isBlank(isCover) || StringUtils.isBlank(invoice.getShippingNo())) {
                invoice.setShippingNo(destShippingNo);
                invoiceService.save(invoice);
            }
        }
    }

    /**
     * 判断物流单号是否已经存在
     *
     * @param orderIds
     * @param numList
     * @param isCover
     */
    private void assertShippingExist(Integer orderIds[], List<String> numList, String isCover) {
        List<Invoice> invoices;
        if (StringUtils.isNotBlank(isCover)) {
            invoices = invoiceService.findExistShippingNoInvoiceByNotInOrderIds(numList, orderIds);

        } else {
            invoices = invoiceService.findExistShippingNoInvoice(numList);
        }
        if (!invoices.isEmpty()) {
            String errorMsg = Joiner.join(invoices, ",", new Joiner.Operator<Invoice>() {
                @Override
                public String convert(Invoice invoice) {
                    return invoice.getShippingNo();
                }
            });
            throw new StewardBusinessException(String.format("操作失败，物流单号[%s]已经存在", errorMsg));
        }
    }

    /**
     * 获取物流信息 通过名称和仓库id
     *
     * @param name
     * @param repositoryId
     * @return
     */
    public LogisticsPrintInfo getByNameAndRepositoryId(String name, Integer repositoryId) {
        Search search = new Search(LogisticsPrintInfo.class).addFilterEqual("name", name);
        if (repositoryId == null) {
            search.addFilterNull("repositoryId");
        } else {
            search.addFilterEqual("repositoryId", repositoryId);
        }
        return (LogisticsPrintInfo) generalDAO.searchUnique(search);
    }
}
