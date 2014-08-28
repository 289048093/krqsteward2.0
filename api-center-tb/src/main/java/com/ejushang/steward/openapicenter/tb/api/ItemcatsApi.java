package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.SellerAuthorize;
import com.taobao.api.request.ItemcatsAuthorizeGetRequest;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.request.ItempropsGetRequest;
import com.taobao.api.request.ItempropvaluesGetRequest;
import com.taobao.api.response.ItemcatsAuthorizeGetResponse;
import com.taobao.api.response.ItemcatsGetResponse;
import com.taobao.api.response.ItempropsGetResponse;
import com.taobao.api.response.ItempropvaluesGetResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 类目API<br/>
 * 提供了标准类目，类目属性和类目属性值的查询功能<br/>
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午3:31
 */
public class ItemcatsApi {
    private TaobaoClient client;
    private String sessionKey;
    public ItemcatsApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.itemcats.authorize.get 查询商家被授权品牌列表和类目列表<br/>
     * @param fields 需要返回的字段。目前支持有： brand.vid, brand.name, item_cat.cid, item_cat.name, item_cat.status,
     *               item_cat.sort_order,item_cat.parent_cid,item_cat.is_parent, xinpin_item_cat.cid,
     *               xinpin_item_cat.name, xinpin_item_cat.status, xinpin_item_cat.sort_order,
     *               xinpin_item_cat.parent_cid, xinpin_item_cat.is_parent
     * @return
     */
    public SellerAuthorize getSellerAuthorize(String fields) throws ApiException {
        ItemcatsAuthorizeGetRequest req=new ItemcatsAuthorizeGetRequest();
        req.setFields(fields);
        ItemcatsAuthorizeGetResponse response = null;
        response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSellerAuthorize();
    }

    /**
     * taobao.itemcats.get 获取后台供卖家发布商品的标准商品类目<br/>
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     *    <ul>
     *      <li> fields 需要返回的字段列表，见ItemCat，默认返回：cid,parent_cid,name,is_parent。</li>
     *      <li> parent_cid 父商品类目 id，0表示根节点, 传输该参数返回所有子类目。 (cids、parent_cid至少传一个)。</li>
     *      <li> cids 商品所属类目ID列表，用半角逗号(,)分隔 例如:(18957,19562,) (cids、parent_cid至少传一个)。</li>
     *    </ul>
     *
     * @return
     */
    public Map<String,Object> getItemCats(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> itemCatsMap = new HashMap<String, Object>();

        ItemcatsGetRequest req=new ItemcatsGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemcatsGetResponse response = null;
        response = client.execute(req,sessionKey);
        // 获取最后更新的时间
        itemCatsMap.put(ConstantTaoBao.LAST_MODIFIED,response.getLastModified());
        // 属性值,根据fields传入的参数返回相应的结果
        itemCatsMap.put(ConstantTaoBao.ITEM_CATS,response.getItemCats());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return itemCatsMap;
    }

    /**
     * taobao.itemprops.get 获取标准商品类目属性
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     */
    public Map<String,Object> getItemprops(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> itempropsMap = new HashMap<String, Object>();

        ItempropsGetRequest req=new ItempropsGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        ItempropsGetResponse response = null;
        response = client.execute(req,sessionKey);
        itempropsMap.put(ConstantTaoBao.LAST_MODIFIED,response.getLastModified());
        itempropsMap.put(ConstantTaoBao.ITEM_PROPS,response.getItemProps());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return itempropsMap;
    }

    /**
     * taobao.itempropvalues.get 获取标准类目属性值
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     */
    public Map<String,Object> getPropValues(Map<String,Object> argsMap) throws Exception {
        Map<String,Object> propValuesMap = new HashMap<String, Object>();
        ItempropvaluesGetRequest req=new ItempropvaluesGetRequest ();
        ReflectUtils.executeMethods(req,argsMap);
        ItempropvaluesGetResponse response = null;
        response = client.execute(req,sessionKey);
        propValuesMap.put(ConstantTaoBao.LAST_MODIFIED,response.getLastModified());
        propValuesMap.put(ConstantTaoBao.PROP_VALUES,response.getPropValues());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return propValuesMap;
    }
}
