package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.domain.Shop;
import com.taobao.api.domain.ShopCat;
import com.taobao.api.request.*;
import com.taobao.api.response.*;

import java.util.List;
import java.util.Map;

/**
 * 淘宝店铺API<br/>
 * 提供了店铺查询，店铺自定义类目的查询和更新。<br/>
 * User: Baron.Zhang
 * Date: 13-12-13
 * Time: 下午2:23
 */
public class TbShopApi {
    private TaobaoClient client;
    private String sessionKey;
    public TbShopApi(String sessionKey){
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.sellercats.list.add 添加卖家自定义类目<br/>
     * 此API添加卖家店铺内自定义类目 父类目parent_cid值等于0：表示此类目为店铺下的一级类目，值不等于0：表示此类目有父类目<br/>
     * 注：因为缓存的关系,添加的新类目需8个小时后才可以在淘宝页面上正常显示，但是不影响在该类目下商品发布<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public SellerCat addSellercatsList(Map<String,Object> argsMap) throws Exception {
        SellercatsListAddRequest req=new SellercatsListAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        SellercatsListAddResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSellerCat();
    }

    /**
     * taobao.sellercats.list.get 获取前台展示的店铺内卖家自定义商品类目<br/>
     * 此API获取当前卖家店铺在淘宝前端被展示的浏览导航类目（面向买家）<br/>
     * @param nick 卖家昵称
     * @return
     */
    public List<SellerCat> getSellercatsList(String nick) throws ApiException {
        SellercatsListGetRequest req=new SellercatsListGetRequest();
        req.setNick(nick);
        SellercatsListGetResponse response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSellerCats();
    }

    /**
     * taobao.sellercats.list.update 更新卖家自定义类目<br/>
     * 此API更新卖家店铺内自定义类目<br/>
     * 注：因为缓存的关系，添加的新类目需8个小时后才可以在淘宝页面上正常显示，但是不影响在该类目下商品发布<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public SellerCat updateSellercatsList(Map<String,Object> argsMap) throws Exception {
        SellercatsListUpdateRequest req=new SellercatsListUpdateRequest();
        ReflectUtils.executeMethods(req,argsMap);
        SellercatsListUpdateResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSellerCat();
    }

    /**
     * taobao.shop.get 获取卖家店铺的基本信息<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public ShopGetResponse shopGet(Map<String,Object> argsMap) throws Exception {
        ShopGetRequest req=new ShopGetRequest();
        ReflectUtils.executeMethods(req,argsMap);
        ShopGetResponse response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response;
    }

    /**
     * taobao.shop.remainshowcase.get 获取卖家店铺剩余橱窗数量<br/>
     * 获取卖家店铺剩余橱窗数量，已用橱窗数量，总橱窗数量<br/>
     * @return
     */
    public Shop getShopRemainshowcase() throws ApiException {
        ShopRemainshowcaseGetRequest req=new ShopRemainshowcaseGetRequest();
        ShopRemainshowcaseGetResponse response = client.execute(req , sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getShop();
    }

    /**
     * taobao.shop.update 更新店铺基本信息<br/>
     * 目前只支持标题、公告和描述的更新<br/>
     * @param argsMap
     * @return
     * @throws Exception
     */
    public ShopUpdateResponse shopUpdate(Map<String,Object> argsMap) throws Exception {
        ShopUpdateRequest req=new ShopUpdateRequest();
        ReflectUtils.executeMethods(req,argsMap);
        ShopUpdateResponse response = client.execute(req , sessionKey);
        return response;
    }

    /**
     * taobao.shopcats.list.get 获取前台展示的店铺类目<br/>
     * 获取淘宝面向买家的浏览导航类目（跟后台卖家商品管理的类目有差异）<br/>
     * @param fields
     * @return
     */
    public List<ShopCat> getShopcatsList(String fields) throws ApiException {
        ShopcatsListGetRequest req=new ShopcatsListGetRequest();
        req.setFields(fields);
        ShopcatsListGetResponse response = client.execute(req,sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getShopCats();
    }

}
