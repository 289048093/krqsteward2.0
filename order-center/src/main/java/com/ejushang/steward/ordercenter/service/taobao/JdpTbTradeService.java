package com.ejushang.steward.ordercenter.service.taobao;

import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.constant.ShopType;
import com.ejushang.steward.ordercenter.domain.Shop;
import com.ejushang.steward.ordercenter.domain.taobao.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User:moon
 * Date: 14-4-17
 * Time: 下午7:20
 */
@Service
@Transactional
public class JdpTbTradeService {

    private static final Logger log = LoggerFactory.getLogger(JdpTbTradeService.class);
    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private JdbcTemplate jdbcTemplateTb;

    private static final String BLANK_SEQ = " ";
    private static final String AND_SEQ = "and";
    private static final String QM_SEQ = "?";
    private static final String EQUAL_SIGN = "=";
    private static final String LIKE_SIGN = "=";
    private static final String IN_SIGN = "in";
    private static final String GE_THAN_SIGN = ">=";
    private static final String LE_THAN_SIGN = "<=";
    private static final String G_THAN_SIGN = ">";
    private static final String L_THAN_SIGN = "<";
    private static final String END_SEQ = ";";


    /**
     * 构造查询条件片段
     * @param fieldName
     * @param sign
     * @return
     */
    private String structConditionSeg(String fieldName,String sign){
        return BLANK_SEQ + fieldName + sign + QM_SEQ;
    }
    @Transactional(readOnly = true)
    public List<Shop> findShopByShopQuery(Shop shop){
        String tableName = "t_shop";
        String resultFields = "id as id,shop_auth_id as shopAuthId,out_shop_id as outShopId,cat_id as catId,uid as uid," +
                "nick as nick,title as title,description as description,bulletin as bulletin,pic_path as picPath," +
                "item_score as itemScore,service_score as serviceScore,delivery_score as deliveryScore,de_express as deExpress," +
                "enable_msg as enableMsg,msg_temp as msgTemp,msg_sign as msgSign,platform_type as platformType,shop_type as shopType," +
                "create_time as createTime,update_time as updateTime,operator_id as operatorId";
        String queryString = "select "+resultFields+" from " + tableName;
        String where = "where";
        List<String> queryConditionList = new ArrayList<String>();
        List<Object> argList = new ArrayList<Object>();

        if(shop != null){
            if(shop.getId() != null){
                queryConditionList.add(structConditionSeg("id", EQUAL_SIGN));
                argList.add(shop.getId());
            }
            if(StringUtils.isNotBlank(shop.getTitle())){
                queryConditionList.add(structConditionSeg("title", EQUAL_SIGN));
                argList.add(shop.getTitle());
            }
            queryConditionList.add(structConditionSeg("create_time", GE_THAN_SIGN));
            argList.add(new Timestamp(EJSDateUtils.parseDate("2013-01-21 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR).getTime()));
            queryConditionList.add(structConditionSeg("create_time", LE_THAN_SIGN));
            argList.add(new Timestamp(EJSDateUtils.parseDate("2014-05-22 00:00:00", EJSDateUtils.DateFormatType.DATE_FORMAT_STR).getTime()));
        }

        StringBuffer querySqlBuffer = new StringBuffer(queryString);
        if(argList.size() > 0){
            querySqlBuffer.append(BLANK_SEQ).append(where);
            for(int i = 0; i < queryConditionList.size(); i++){
                if(i == 0){
                    querySqlBuffer.append(queryConditionList.get(i));
                }else{
                    querySqlBuffer.append(BLANK_SEQ).append(AND_SEQ).append(queryConditionList.get(i));
                }
            }
        }

        querySqlBuffer.append(END_SEQ);

        List list = jdbcTemplateTb.queryForList(querySqlBuffer.toString(),argList.toArray());
        List<Shop> shopList = new ArrayList<Shop>();
        Shop shopNew = null;
        for(Object obj : list){
            shopNew = new Shop();
            shopNew.setId((Integer) ((Map) obj).get("id"));
            shopNew.setShopAuthId((Integer) ((Map) obj).get("shopAuthId"));
            shopNew.setOutShopId((String) ((Map) obj).get("outShopId"));
            shopNew.setCatId((String) ((Map) obj).get("catId"));
            shopNew.setUid((String) ((Map) obj).get("uid"));
            shopNew.setNick((String) ((Map) obj).get("nick"));
            shopNew.setTitle((String) ((Map) obj).get("title"));
            shopNew.setDescription(((String) ((Map)obj).get("description")));
            shopNew.setBulletin((String) ((Map) obj).get("bulletin"));
            shopNew.setPicPath((String) ((Map) obj).get("picPath"));
            shopNew.setItemScore((String) ((Map) obj).get("itemScore"));
            shopNew.setServiceScore((String) ((Map) obj).get("serviceScore"));
            shopNew.setDeliveryScore((String) ((Map) obj).get("deliveryScore"));
            shopNew.setDeExpress((String) ((Map) obj).get("deExpress"));
            shopNew.setEnableMsg((Boolean) ((Map) obj).get("enableMsg"));
            shopNew.setMsgTemp((String) ((Map) obj).get("msgTemp"));
            shopNew.setMsgSign((String) ((Map) obj).get("msgSign"));
            shopNew.setPlatformType(PlatformType.valueOf((String) ((Map) obj).get("platformType")));
            shopNew.setShopType(ShopType.valueOf((String) ((Map) obj).get("shopType")));
            shopNew.setCreateTime((Date) ((Map) obj).get("createTime"));
            shopNew.setUpdateTime((Date) ((Map) obj).get("updateTime"));
            shopNew.setOperatorId((Integer) ((Map) obj).get("operatorId"));
            shopList.add(shopNew);
        }

        return shopList;
    }

    @Transactional(readOnly = true)
    public List<JdpTbTrade> findJdpTbTradeByJdpTbTradeQuery(JdpTbTradeQuery jdpTbTradeQuery) {

        String tableName = "jdp_tb_trade";
        String queryString = "select * from " + tableName;
        String where = "where";
        List<String> queryConditionList = new ArrayList<String>();
        List<Object> argList = new ArrayList<Object>();

        if(jdpTbTradeQuery!=null){
            if(jdpTbTradeQuery.getTid()!=null){
                queryConditionList.add(structConditionSeg("tid", EQUAL_SIGN));
                argList.add(jdpTbTradeQuery.getTid());
            }
            if(StringUtils.isNotBlank(jdpTbTradeQuery.getStatus())){
                StringBuffer inConditionBuffer = new StringBuffer();
                inConditionBuffer.append(BLANK_SEQ).append("status").append(BLANK_SEQ).append("in(");
                String[] stateArr = jdpTbTradeQuery.getStatus().split(",");
                for(int i = 0; i<stateArr.length; i++){
                    if(i == 0) {
                        inConditionBuffer.append(QM_SEQ);
                    }
                    else{
                        inConditionBuffer.append(",").append(QM_SEQ);
                    }
                    argList.add(stateArr[i]);
                }
                inConditionBuffer.append(")");
                queryConditionList.add(inConditionBuffer.toString());
            }
            if(StringUtils.isNotBlank(jdpTbTradeQuery.getType())){
                queryConditionList.add(structConditionSeg("type", EQUAL_SIGN));
                argList.add(jdpTbTradeQuery.getType());
            }
            if(StringUtils.isNotBlank(jdpTbTradeQuery.getSellerNick())){
                queryConditionList.add(structConditionSeg("seller_nick", EQUAL_SIGN));
                argList.add(jdpTbTradeQuery.getSellerNick());
            }
            if(StringUtils.isNotBlank(jdpTbTradeQuery.getBuyerNick())){
                queryConditionList.add(structConditionSeg("buyer_nick", EQUAL_SIGN));
                argList.add(jdpTbTradeQuery.getBuyerNick());
            }
            if(jdpTbTradeQuery.getCreated()!=null){
                queryConditionList.add(structConditionSeg("created", EQUAL_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getCreated()));
            }
            if(jdpTbTradeQuery.getStartCreated()!=null){
                queryConditionList.add(structConditionSeg("created", GE_THAN_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getStartCreated()));
            }
            if(jdpTbTradeQuery.getEndCreated()!=null){
                queryConditionList.add(structConditionSeg("created", LE_THAN_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getEndCreated()));
            }
            if(jdpTbTradeQuery.getModified()!=null){
                queryConditionList.add(structConditionSeg("modified", EQUAL_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getModified()));
            }
            if(jdpTbTradeQuery.getJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", EQUAL_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getJdpCreated()));
            }
            if(jdpTbTradeQuery.getStartJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", GE_THAN_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getStartJdpCreated()));
            }
            if(jdpTbTradeQuery.getEndJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", LE_THAN_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getEndJdpCreated()));
            }
            if(jdpTbTradeQuery.getJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", EQUAL_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getJdpModified()));
            }
            if(jdpTbTradeQuery.getStartJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", GE_THAN_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getStartJdpModified()));
            }
            if(jdpTbTradeQuery.getEndJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", LE_THAN_SIGN));
                argList.add(convertUtilDate2Timestamp(jdpTbTradeQuery.getEndJdpModified()));
            }
            if(StringUtils.isNotBlank(jdpTbTradeQuery.getJdpHashcode())){
                queryConditionList.add(structConditionSeg("jdp_hashcode", EQUAL_SIGN));
                argList.add(jdpTbTradeQuery.getJdpHashcode());
            }
            if(StringUtils.isNotBlank(jdpTbTradeQuery.getJdpResponse())){
                queryConditionList.add(structConditionSeg("jdp_response", EQUAL_SIGN));
                argList.add(jdpTbTradeQuery.getJdpResponse());
            }
        }

        StringBuffer querySqlBuffer = new StringBuffer(queryString);
        if(argList.size() > 0){
            querySqlBuffer.append(BLANK_SEQ).append(where);
            for(int i = 0; i < queryConditionList.size(); i++){
                if(i == 0){
                    querySqlBuffer.append(queryConditionList.get(i));
                }else{
                    querySqlBuffer.append(BLANK_SEQ).append(AND_SEQ).append(queryConditionList.get(i));
                }
            }
        }
        querySqlBuffer.append(END_SEQ);

        if(log.isInfoEnabled()) {
            StringBuffer argsSb = new StringBuffer("天猫聚石塔订单抓取,sql参数:");
            for(Object obj : argList){
                if(obj instanceof Date){
                    Date date = (Date) obj;
                    argsSb.append(EJSDateUtils.formatDate(date, EJSDateUtils.DateFormatType.DATE_FORMAT_STR)).append(",");
                }
                else {
                    argsSb.append(obj).append(",");
                }
            }
            log.info("天猫聚石塔订单抓取：sql={},参数值:{}",querySqlBuffer.toString(),argsSb.toString());
        }

        List list = jdbcTemplateTb.queryForList(querySqlBuffer.toString(),argList.toArray());

        List<JdpTbTrade> jdpTbTradeList = new ArrayList<JdpTbTrade>();
        JdpTbTrade jdpTbTrade = null;
        for(Object obj : list) {
            jdpTbTrade = new JdpTbTrade();
            jdpTbTrade.setTid((Long) ((Map) obj).get("tid"));
            jdpTbTrade.setStatus((String) ((Map) obj).get("status"));
            jdpTbTrade.setType((String) ((Map) obj).get("type"));
            jdpTbTrade.setSellerNick((String) ((Map) obj).get("seller_nick"));
            jdpTbTrade.setBuyerNick((String) ((Map) obj).get("buyer_nick"));
            jdpTbTrade.setCreated((Date) ((Map) obj).get("created"));
            jdpTbTrade.setModified((Date) ((Map) obj).get("modified"));
            jdpTbTrade.setJdpCreated((Date) ((Map) obj).get("jdp_created"));
            jdpTbTrade.setJdpModified((Date) ((Map) obj).get("jdp_modified"));
            jdpTbTrade.setJdpHashcode((String) ((Map) obj).get("jdp_hashcode"));
            jdpTbTrade.setJdpResponse((String) ((Map) obj).get("jdp_response"));

            jdpTbTradeList.add(jdpTbTrade);
        }
        return jdpTbTradeList;
    }

    private Timestamp convertUtilDate2Timestamp(Date date){
        if(date == null){
            return null;
        }

        return new Timestamp(date.getTime());
    }

    @Transactional(readOnly = true)
    public List<JdpTmRefund> findJdpTmRefundByJdpTmRefundQuery(JdpTmRefundQuery jdpTmRefundQuery) {

        String tableName = "jdp_tm_refund";
        String queryString = "select * from " + tableName;
        String where = "where";
        List<String> queryConditionList = new ArrayList<String>();
        List<Object> argList = new ArrayList<Object>();

        if(jdpTmRefundQuery!=null){
            if(StringUtils.isNotBlank(jdpTmRefundQuery.getSellerNick())){
                queryConditionList.add(structConditionSeg("seller_nick", EQUAL_SIGN));
                argList.add(jdpTmRefundQuery.getSellerNick());
            }
            if(StringUtils.isNotBlank(jdpTmRefundQuery.getStatus())){
                StringBuffer inConditionBuffer = new StringBuffer();
                inConditionBuffer.append(BLANK_SEQ).append("status").append(BLANK_SEQ).append("in(");
                String[] stateArr = jdpTmRefundQuery.getStatus().split(",");
                for(int i = 0; i<stateArr.length; i++){
                    if(i == 0) {
                        inConditionBuffer.append(QM_SEQ);
                    }
                    else{
                        inConditionBuffer.append(",").append(QM_SEQ);
                    }
                    argList.add(stateArr[i]);
                }
                inConditionBuffer.append(")");
                queryConditionList.add(inConditionBuffer.toString());
            }
            if(jdpTmRefundQuery.getStartJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", GE_THAN_SIGN));
                argList.add(jdpTmRefundQuery.getStartJdpCreated());
            }
            if(jdpTmRefundQuery.getEndJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", LE_THAN_SIGN));
                argList.add(jdpTmRefundQuery.getEndJdpCreated());
            }
            if(jdpTmRefundQuery.getStartJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", GE_THAN_SIGN));
                argList.add(jdpTmRefundQuery.getStartJdpModified());
            }
            if(jdpTmRefundQuery.getEndJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", LE_THAN_SIGN));
                argList.add(jdpTmRefundQuery.getEndJdpModified());
            }
        }

        StringBuffer querySqlBuffer = new StringBuffer(queryString);
        if(argList.size() > 0){
            querySqlBuffer.append(BLANK_SEQ).append(where);
            for(int i = 0; i < queryConditionList.size(); i++){
                if(i == 0){
                    querySqlBuffer.append(queryConditionList.get(i));
                }else{
                    querySqlBuffer.append(BLANK_SEQ).append(AND_SEQ).append(queryConditionList.get(i));
                }
            }
        }
        querySqlBuffer.append(END_SEQ);

        if(log.isInfoEnabled()){
            log.info("天猫聚石塔抓取退款单：sql={}",querySqlBuffer.toString());
        }

        List list = jdbcTemplateTb.queryForList(querySqlBuffer.toString(),argList.toArray());

        List<JdpTmRefund> jdpTmRefundList = new ArrayList<JdpTmRefund>();
        JdpTmRefund jdpTmRefund = null;
        for(Object obj : list) {
            jdpTmRefund = new JdpTmRefund();
            jdpTmRefund.setRefundId((Long) ((Map) obj).get("refund_id"));
            jdpTmRefund.setStatus((String) ((Map) obj).get("status"));
            jdpTmRefund.setRefundPhase((String) ((Map) obj).get("refund_phase"));
            jdpTmRefund.setSellerNick((String) ((Map) obj).get("seller_nick"));
            jdpTmRefund.setBuyerNick((String) ((Map) obj).get("buyer_nick"));
            jdpTmRefund.setTid((Long) ((Map) obj).get("tid"));
            jdpTmRefund.setOid((Long) ((Map) obj).get("oid"));
            jdpTmRefund.setCreated((Date) ((Map) obj).get("created"));
            jdpTmRefund.setModified((Date) ((Map) obj).get("modified"));
            jdpTmRefund.setJdpCreated((Date) ((Map) obj).get("jdp_created"));
            jdpTmRefund.setJdpModified((Date) ((Map) obj).get("jdp_modified"));
            jdpTmRefund.setJdpHashcode((String) ((Map) obj).get("jdp_hashcode"));
            jdpTmRefund.setJdpResponse((String) ((Map) obj).get("jdp_response"));

            jdpTmRefundList.add(jdpTmRefund);
        }
        return jdpTmRefundList;
    }

    @Transactional(readOnly = true)
    public List<JdpTmReturn> findJdpTmReturnByJdpTmReturnQuery(JdpTmReturnQuery jdpTmReturnQuery) {

        String tableName = "jdp_tm_return";
        String queryString = "select * from " + tableName;
        String where = "where";
        List<String> queryConditionList = new ArrayList<String>();
        List<Object> argList = new ArrayList<Object>();

        if(jdpTmReturnQuery!=null){
            if(StringUtils.isNotBlank(jdpTmReturnQuery.getSid())){
                queryConditionList.add(structConditionSeg("sid", EQUAL_SIGN));
                argList.add(jdpTmReturnQuery.getSid());
            }
            if(StringUtils.isNotBlank(jdpTmReturnQuery.getStatus())){
                StringBuffer inConditionBuffer = new StringBuffer();
                inConditionBuffer.append(BLANK_SEQ).append("status").append(BLANK_SEQ).append("in(");
                String[] stateArr = jdpTmReturnQuery.getStatus().split(",");
                for(int i = 0; i<stateArr.length; i++){
                    if(i == 0) {
                        inConditionBuffer.append(QM_SEQ);
                    }
                    else{
                        inConditionBuffer.append(",").append(QM_SEQ);
                    }
                    argList.add(stateArr[i]);
                }
                inConditionBuffer.append(")");
                queryConditionList.add(inConditionBuffer.toString());
            }
            if(jdpTmReturnQuery.getStartJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", GE_THAN_SIGN));
                argList.add(jdpTmReturnQuery.getStartJdpCreated());
            }
            if(jdpTmReturnQuery.getEndJdpCreated()!=null){
                queryConditionList.add(structConditionSeg("jdp_created", LE_THAN_SIGN));
                argList.add(jdpTmReturnQuery.getEndJdpCreated());
            }
            if(jdpTmReturnQuery.getStartJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", GE_THAN_SIGN));
                argList.add(jdpTmReturnQuery.getStartJdpModified());
            }
            if(jdpTmReturnQuery.getEndJdpModified()!=null){
                queryConditionList.add(structConditionSeg("jdp_modified", LE_THAN_SIGN));
                argList.add(jdpTmReturnQuery.getEndJdpModified());
            }
        }

        StringBuffer querySqlBuffer = new StringBuffer(queryString);
        if(argList.size() > 0){
            querySqlBuffer.append(BLANK_SEQ).append(where);
            for(int i = 0; i < queryConditionList.size(); i++){
                if(i == 0){
                    querySqlBuffer.append(queryConditionList.get(i));
                }else{
                    querySqlBuffer.append(BLANK_SEQ).append(AND_SEQ).append(queryConditionList.get(i));
                }
            }
        }
        querySqlBuffer.append(END_SEQ);

        if(log.isInfoEnabled()){
            log.info("天猫聚石塔抓取退货单,sql={}",querySqlBuffer.toString());
        }

        List list = jdbcTemplateTb.queryForList(querySqlBuffer.toString(),argList.toArray());

        List<JdpTmReturn> jdpTmReturnList = new ArrayList<JdpTmReturn>();
        JdpTmReturn jdpTmReturn = null;
        for(Object obj : list) {
            jdpTmReturn = new JdpTmReturn();
            jdpTmReturn.setRefundId((Long) ((Map) obj).get("refund_id"));
            jdpTmReturn.setStatus((String) ((Map) obj).get("status"));
            jdpTmReturn.setStatus((String) ((Map) obj).get("sid"));
            jdpTmReturn.setRefundPhase((String) ((Map) obj).get("refund_phase"));
            jdpTmReturn.setTid((Long) ((Map) obj).get("tid"));
            jdpTmReturn.setOid((Long) ((Map) obj).get("oid"));
            jdpTmReturn.setCreated((Date) ((Map) obj).get("created"));
            jdpTmReturn.setModified((Date) ((Map) obj).get("modified"));
            jdpTmReturn.setJdpCreated((Date) ((Map) obj).get("jdp_created"));
            jdpTmReturn.setJdpModified((Date) ((Map) obj).get("jdp_modified"));
            jdpTmReturn.setJdpHashcode((String) ((Map) obj).get("jdp_hashcode"));
            jdpTmReturn.setJdpResponse((String) ((Map) obj).get("jdp_response"));

            jdpTmReturnList.add(jdpTmReturn);
        }
        return jdpTmReturnList;
    }

    @Transactional(readOnly = true)
    public List<Shop> findAllShop() {
        Search search=new Search(Shop.class);
        search.addSortDesc("createTime");
        return generalDAO.search(search);
    }
}
