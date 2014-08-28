package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TmcMessage;
import com.taobao.api.domain.TmcUser;
import com.taobao.api.request.*;
import com.taobao.api.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 淘宝消息服务API<br/>
 * 提供了用户基本信息查询功能<br/>
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:23
 */
public class TbTmcApi {
    private TaobaoClient client;
    private String sessionKey;
    public TbTmcApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.tmc.group.add 为已开通用户添加用户分组<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> addTmcGroup(Map<String,Object> argsMap) throws Exception {
        TmcGroupAddRequest req=new TmcGroupAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmcGroupAddResponse response = client.execute(req);
        TaoBaoLogUtil.logTaoBaoApi(response);

        Map<String,Object> tmcGroupMap = new HashMap<String, Object>();
        tmcGroupMap.put(ConstantTaoBao.GROUP_NAME,response.getGroupName());
        tmcGroupMap.put(ConstantTaoBao.CREATED,response.getCreated());

        return tmcGroupMap;
    }

    /**
     * taobao.tmc.group.delete 删除指定的分组或分组下的用户<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Boolean deleteTmcGroup(Map<String,Object> argsMap) throws Exception {
        TmcGroupDeleteRequest req=new TmcGroupDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmcGroupDeleteResponse response = client.execute(req);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getIsSuccess();
    }

    /**
     * taobao.tmc.message.produce 发布单条消息<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Boolean produceTmcMessage(Map<String,Object> argsMap) throws Exception {
        TmcMessageProduceRequest req=new TmcMessageProduceRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmcMessageProduceResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);

        return response.getIsSuccess();
    }

    /**
     * taobao.tmc.messages.confirm 确认消费消息的状态<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public Boolean confirmTmcMessage(Map<String,Object> argsMap) throws Exception {
        TmcMessagesConfirmRequest req=new TmcMessagesConfirmRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmcMessagesConfirmResponse response = client.execute(req);
        TaoBaoLogUtil.logTaoBaoApi(response);

        return response.getIsSuccess();
    }

    /**
     * taobao.tmc.messages.consume 消费多条消息<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public List<TmcMessage> consumeTmcMessage(Map<String,Object> argsMap) throws Exception {
        TmcMessagesConsumeRequest req=new TmcMessagesConsumeRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TmcMessagesConsumeResponse response = client.execute(req);
        TaoBaoLogUtil.logTaoBaoApi(response);

        return response.getMessages();
    }

    /**
     * taobao.tmc.user.cancel 取消用户的消息服务<br/>
     * @param nick
     * @return
     * @throws com.taobao.api.ApiException
     */
    public Boolean cancelTmcUser(String nick) throws ApiException {
        TmcUserCancelRequest req=new TmcUserCancelRequest();
        req.setNick(nick);
        TmcUserCancelResponse response = client.execute(req);
        TaoBaoLogUtil.logTaoBaoApi(response);

        return response.getIsSuccess();
    }

    /**
     * taobao.tmc.user.get 获取用户已开通消息<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public TmcUser getTmcUser(Map<String,Object> argsMap) throws Exception {
        TmcUserGetRequest req=new TmcUserGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        TmcUserGetResponse response = client.execute(req);
        TaoBaoLogUtil.logTaoBaoApi(response);

        return response.getTmcUser();
    }

    /**
     * taobao.tmc.user.permit 为已授权的用户开通消息服务<br/>
     * @param topics
     * @return
     * @throws com.taobao.api.ApiException
     */
    public TmcUserPermitResponse tmcUserPermit(String topics) throws ApiException {
        TmcUserPermitRequest req=new TmcUserPermitRequest();
        req.setTopics(topics);
        TmcUserPermitResponse response = client.execute(req , sessionKey);
        return response;
    }

}
