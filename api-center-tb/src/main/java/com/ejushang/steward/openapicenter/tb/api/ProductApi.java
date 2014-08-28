package com.ejushang.steward.openapicenter.tb.api;

import com.ejushang.steward.common.util.ReflectUtils;
import com.ejushang.steward.openapicenter.tb.constant.ConstantTaoBao;
import com.ejushang.steward.openapicenter.tb.util.TaoBaoLogUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.*;
import com.taobao.api.request.*;
import com.taobao.api.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 淘宝商品API
 * User: 龙清华
 * Date: 13-12-16
 * Time: 下午4:30
 */
public class ProductApi {
    private TaobaoClient client;
    private String sessionKey;

    public ProductApi(String sessionKey) {
        client = new DefaultTaobaoClient(ConstantTaoBao.TB_API_URL, ConstantTaoBao.TB_APP_KEY, ConstantTaoBao.TB_APP_SECRET);
        this.sessionKey = sessionKey;
    }

    /**
     * taobao.aftersale.get 查询用户售后服务模板
     * 仅返回标题和id
     *
     * @return
     */
    public List<AfterSale> getAfterSale() throws Exception {
        AftersaleGetRequest req = new AftersaleGetRequest();
        AftersaleGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAfterSales();
    }

    /**
     * taobao.item.update 更新商品信息
     *
     * @param args 商品的num_iid为必须参数
     * @return 商品结构里的num_iid，modified
     * @throws Exception
     */
    public ItemUpdateResponse itemUpdate(Map<String, Object> args) throws Exception {
        ItemUpdateRequest req = new ItemUpdateRequest();
        ReflectUtils.executeMethods(req, args);
        return client.execute(req, sessionKey);
    }

    /**
     * taobao.item.add 添加一个商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品结构, 仅有numIid和created返回
     */
    public Item addItem(Map<String, Object> argsMap) throws Exception {
        ItemAddRequest req = new ItemAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.anchor.get 获取可用宝贝描述规范化模块
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 返回宝贝模版的数量
     *         和
     *         宝贝描述规范化可使用打标模块的锚点信息
     */
    public Map<String, Object> getItemAnchor(Map<String, Object> argsMap) throws Exception {
        ItemAnchorGetRequest req = new ItemAnchorGetRequest();
        Map<String, Object> temp = new HashMap<String, Object>();
        ReflectUtils.executeMethods(req, argsMap);
        ItemAnchorGetResponse response = client.execute(req, sessionKey);
        temp.put(ConstantTaoBao.TOTAL_RESULTS, response.getTotalResults());
        temp.put(ConstantTaoBao.ANCHOR_MODULES, response.getAnchorModules());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return temp;
    }

    /**
     * taobao.item.get 获得单条商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品的相关信息
     */
    public Item getItem(Map<String, Object> argsMap) throws Exception {
        ItemGetRequest req = new ItemGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.delete 删除单条商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 被删除商品的相关信息
     */
    public Item deleteItem(Map<String, Object> argsMap) throws Exception {
        ItemDeleteRequest req = new ItemDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.add.rules.get 商品发布规则信息获取接口
     */
    public String getAddRules(Map<String, Object> argsMap) throws Exception {
        ItemAddRulesGetRequest req = new ItemAddRulesGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemAddRulesGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getAddRules();
    }

    /**
     * taobao.item.bseller.add 助理发布商城商品
     * <p/>
     * 淘宝助理提供的发布商城商品接口，在发布时 先查询是否有这个产品，有则将商品绑定到该产品上发布；
     * 如果没有这个产品，自动帮用户新建产品，再将商品绑定到该产品上发布。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 新发布的商品信息
     * @throws Exception
     */
    public Item addItemBseller(Map<String, Object> argsMap) throws Exception {
        ItemBsellerAddRequest req = new ItemBsellerAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemBsellerAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.cseller.add 助理发布集市商品
     * 提供的发布集市商品接口，接口参数除了包括taobao.item.vip.add的参数外
     * 新增一个sub_pic_paths参数，为图片空间的url，本方法会在发布商品同时
     * 将这些图片作为副图关联到新商品。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 新发布的商品信息
     * @throws Exception
     */
    public Item addItemCseller(Map<String, Object> argsMap) throws Exception {
        ItemCsellerAddRequest req = new ItemCsellerAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemCsellerAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.ebook.serial.add 添加一个网络原创电子书商品
     * 调用该接口 会创建一个二级类目为网络文学原创电子书商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 新发布的商品信息
     * @throws Exception
     */
    public Item addItemEbookSerial(Map<String, Object> argsMap) throws Exception {
        ItemEbookSerialAddRequest req = new ItemEbookSerialAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemEbookSerialAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.ebook.serial.update 更新连载电子书
     * 调用该接口 更新连载电子书信息
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 新发布的商品信息
     * @throws Exception
     */
    public Item updateItemEbookSerial(Map<String, Object> argsMap) throws Exception {
        ItemEbookSerialUpdateRequest req = new ItemEbookSerialUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemEbookSerialUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.img.delete 删除商品图片
     * 删除itemimg_id 所指定的商品图片
     * 传入的num_iid所对应的商品必须属于当前会话的用户
     * itemimg_id对应的图片需要属于num_iid对应的商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品图片结构
     * @throws Exception
     */
    public ItemImg deleteItemImg(Map<String, Object> argsMap) throws Exception {
        ItemImgDeleteRequest req = new ItemImgDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemImgDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItemImg();
    }

    /**
     * taobao.item.img.upload 添加商品图片
     * 添加一张商品图片到num_iid指定的商品中
     * 传入的num_iid所对应的商品必须属于当前会话的用户
     * 如果更新图片需要设置itemimg_id
     * 且该itemimg_id的图片记录需要属于传入的num_iid对应的商品
     * 如果新增图片则不用设置 商品图片有数量和大小上的限制
     * 根据卖家享有的服务（如：卖家订购了多图服务等）
     * 商品图片数量限制不同
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品图片结构
     * @throws Exception
     */
    public ItemImg uploadItemImg(Map<String, Object> argsMap) throws Exception {
        ItemImgUploadRequest req = new ItemImgUploadRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemImgUploadResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItemImg();
    }

    /**
     * taobao.item.joint.img 商品关联子图
     * 关联一张商品图片到num_iid指定的商品中
     * 传入的num_iid所对应的商品必须属于当前会话的用户
     * 商品图片关联在卖家身份和图片来源上的限制
     * 卖家要是B卖家或订购了多图服务才能关联图片
     * 并且图片要来自于卖家自己的图片空间才行
     * 商品图片数量有限制
     * 不管是上传的图片还是关联的图片
     * 他们的总数不能超过一定限额
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品图片信息
     * @throws Exception
     */
    public ItemImg itemJointImg(Map<String, Object> argsMap) throws Exception {
        ItemJointImgRequest req = new ItemJointImgRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemJointImgResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItemImg();
    }

    /**
     * taobao.item.joint.propimg 商品关联属性图
     * 关联一张商品属性图片到num_iid指定的商品中
     * 传入的num_iid所对应的商品必须属于当前会话的用户
     * 图片的属性必须要是颜色的属性，这个在前台显示的时候需要和sku进行关联的
     * 商品图片关联在卖家身份和图片来源上的限制，卖家要是B卖家或订购了多图服务才能关联图片
     * 并且图片要来自于卖家自己的图片空间才行,商品图片数量有限制。
     * 不管是上传的图片还是关联的图片，他们的总数不能超过一定限额
     * 最多不能超过24张（每个颜色属性都有一张）
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品图片信息
     * @throws Exception
     */
    public PropImg itemJointPropimg(Map<String, Object> argsMap) throws Exception {
        ItemJointPropimgRequest req = new ItemJointPropimgRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemJointPropimgResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getPropImg();
    }

    /**
     * taobao.item.price.update 更新商品价格
     * <p/>
     * 更新商品价格
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return 商品图片信息
     * @throws Exception
     */
    public Item updateItemPrice(Map<String, Object> argsMap) throws Exception {
        ItemPriceUpdateRequest req = new ItemPriceUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemPriceUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.propimg.delete 删除商品属性图片
     * <p/>
     * 删除propimg_id 所指定的商品属性图片 传入的num_iid所对应的商品必须属于当前会话的用户
     * propimg_id对应的属性图片需要属于num_iid对应的商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return prop_img 属性图片结构
     * @throws Exception
     */
    public PropImg deleteItemPropimg(Map<String, Object> argsMap) throws Exception {
        ItemPropimgDeleteRequest req = new ItemPropimgDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemPropimgDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getPropImg();
    }

    /**
     * taobao.item.propimg.upload 添加或修改商品属性图片
     * <p/>
     * 添加一张商品属性图片到num_iid指定的商品中 传入的num_iid所对应的商品必须属于当前会话的用户
     * 图片的属性必须要是颜色的属性，这个在前台显示的时候需要和sku进行关联的 商品属性图片只有享有服务的卖家
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return prop_img 属性图片结构
     * @throws Exception
     */
    public PropImg uploadItemPropimg(Map<String, Object> argsMap) throws Exception {
        ItemPropimgUploadRequest req = new ItemPropimgUploadRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemPropimgUploadResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getPropImg();
    }

    /**
     * taobao.item.quantity.update 宝贝/SKU库存修改
     * <p/>
     * 提供按照全量或增量形式修改宝贝/SKU库存的功能
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return Item 商品属性
     * @throws Exception
     */
    public Item updateItemQuantity(Map<String, Object> argsMap) throws Exception {
        ItemQuantityUpdateRequest req = new ItemQuantityUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemQuantityUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.recommend.add 橱窗推荐一个商品
     * <p/>
     * 将当前用户指定商品设置为橱窗推荐状态 橱窗推荐需要用户有剩余橱窗位才可以顺利执行
     * 这个Item所属卖家从传入的session中获取，需要session绑定 需要判断橱窗推荐是否已满
     * 橱窗推荐已满停止调用橱窗推荐接口，2010年1月底开放查询剩余橱窗推荐数后可以按数量橱窗推荐商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return Item 商品属性
     * @throws Exception
     */
    public Item addItemRecommend(Map<String, Object> argsMap) throws Exception {
        ItemRecommendAddRequest req = new ItemRecommendAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemRecommendAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }


    /**
     * taobao.item.recommend.delete 取消橱窗推荐一个商品
     * 取消当前用户指定商品的橱窗推荐状态 这个Item所属卖家从传入的session中获取，需要session绑定
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return Item 商品属性
     * @throws Exception
     */
    public Item deleteItemRecommend(Map<String, Object> argsMap) throws Exception {
        ItemRecommendDeleteRequest req = new ItemRecommendDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemRecommendDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }


    /**
     * taobao.item.sku.add 添加SKU
     * <p/>
     * 新增一个sku到num_iid指定的商品中 传入的iid所对应的商品必须属于当前会话的用户
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return sku码
     * @throws Exception
     */
    public Sku addItemSku(Map<String, Object> argsMap) throws Exception {
        ItemSkuAddRequest req = new ItemSkuAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemSkuAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSku();
    }

    /**
     * taobao.item.sku.delete 删除SKU
     * <p/>
     * 删除一个sku的数据 需要删除的sku通过属性properties进行匹配查找
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return sku码
     * @throws Exception
     */
    public Sku deleteItemSku(Map<String, Object> argsMap) throws Exception {
        ItemSkuDeleteRequest req = new ItemSkuDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemSkuDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSku();
    }

    /**
     * taobao.item.sku.get 获取SKU
     * <p/>
     * 获取sku_id所对应的sku数据 sku_id对应的sku要属于传入的nick对应的卖家
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return sku码
     * @throws Exception
     */
    public Sku getItemSku(Map<String, Object> argsMap) throws Exception {
        ItemSkuGetRequest req = new ItemSkuGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemSkuGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSku();
    }

    /**
     * taobao.item.sku.price.update 更新商品SKU的价格
     * <p/>
     * 更新商品SKU的价格
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return sku码
     * @throws Exception
     */
    public Sku updateItemSkuPrice(Map<String, Object> argsMap) throws Exception {
        ItemSkuPriceUpdateRequest req = new ItemSkuPriceUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemSkuPriceUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSku();
    }

    /**
     * taobao.item.sku.update 更新SKU信息
     * <p/>
     * 更新一个sku的数据 *需要更新的sku通过属性properties进行匹配查找
     * 商品的数量和价格必须大于等于0 *sku记录会更新到指定的num_iid对应的商品中
     * num_iid对应的商品必须属于当前的会话用户
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return sku码
     * @throws Exception
     */
    public Sku updateItemSku(Map<String, Object> argsMap) throws Exception {
        ItemSkuUpdateRequest req = new ItemSkuUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemSkuUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSku();
    }

    /**
     * taobao.item.skus.get 根据商品ID列表获取SKU信息
     * <p/>
     * 获取多个商品下的所有sku
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return sku集合
     * @throws Exception
     */
    public List<Sku> getItemSkus(Map<String, Object> argsMap) throws Exception {
        ItemSkusGetRequest req = new ItemSkusGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemSkusGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSkus();
    }

    /**
     * taobao.item.templates.get 获取用户宝贝详情页模板名称
     * <p/>
     * 查询当前登录用户的店铺的宝贝详情页的模板名称
     *
     * @return item_template_list
     * @throws Exception
     */
    public List<ItemTemplate> getItemTemplates() throws Exception {
        ItemTemplatesGetRequest req = new ItemTemplatesGetRequest();
        ItemTemplatesGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItemTemplateList();
    }


    /**
     * taobao.item.update 更新商品信息
     * <p/>
     * 根据传入的num_iid更新对应的商品的数据 传入的num_iid所对应的商品必须属于当前会话的用户,商品的属性和sku的属性有包含的关系
     * 商品的价格要位于sku的价格区间之中（例如，sku价格有5元、10元两种，那么商品的价格就需要大于等于5元
     * 小于等于10元，否则更新商品会失败）
     * 商品的类目和商品的价格、sku的价格都有一定的相关性（具体的关系要通过类目属性查询接口获得）
     * 当关键属性值更新为“其他”的时候，需要输入input_pids和input_str商品才能更新成功。
     * 该接口不支持产品属性修改。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return item_template_list
     * @throws Exception
     */
    public Item updateItem(Map<String, Object> argsMap) throws Exception {
        ItemUpdateRequest req = new ItemUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.update.delisting 商品下架
     * <p/>
     * 单个商品下架 * 输入的num_iid必须属于当前会话用户
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return Item商品属性
     * @throws Exception
     */
    public Item delistingItemUpdate(Map<String, Object> argsMap) throws Exception {
        ItemUpdateDelistingRequest req = new ItemUpdateDelistingRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemUpdateDelistingResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItem();
    }

    /**
     * taobao.item.update.listing 一口价商品上架
     * <p/>
     * * 单个商品上架 * 输入的num_iid必须属于当前会话用户
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ItemUpdateListingResponse itemUpdateListing(Map<String, Object> argsMap) throws Exception {
        ItemUpdateListingRequest req = new ItemUpdateListingRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemUpdateListingResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response;
    }

    /**
     * taobao.item.update.delisting 商品下架
     *
     * @param numIid taobao平台商品id
     * @return
     * @throws ApiException
     */
    public ItemUpdateDelistingResponse itemUpdateDelisting(Long numIid) throws ApiException {
        ItemUpdateDelistingRequest req = new ItemUpdateDelistingRequest();
        req.setNumIid(numIid);
        return client.execute(req, sessionKey);
    }


    /**
     * taobao.items.custom.get 根据外部ID取商品
     * <p/>
     * 跟据卖家设定的商品外部id获取商品 这个商品对应卖家从传入的session中获取，需要session绑定
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ItemsCustomGetResponse itemsCustomGet(Map<String, Object> argsMap) throws Exception {
        ItemsCustomGetRequest req = new ItemsCustomGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemsCustomGetResponse response = client.execute(req, sessionKey);
        return response;
    }


    /**
     * taobao.items.inventory.get 得到当前会话用户库存中的商品列表
     * <p/>
     * 获取当前用户作为卖家的仓库中的商品列表，并能根据传入的搜索条件对仓库中的商品列表进行过滤
     * 只能获得商品的部分信息，商品的详细信息请通过taobao.item.get获取
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<Item> getItemsInventory(Map<String, Object> argsMap) throws Exception {
        ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemsInventoryGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItems();
    }

    /**
     * taobao.items.list.get 批量获取商品信息
     * <p/>
     * 查看非公开属性时需要用户登录
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<Item> getItemsList(Map<String, Object> argsMap) throws Exception {
        ItemsListGetRequest req = new ItemsListGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ItemsListGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getItems();
    }

    /**
     * taobao.items.onsale.get 获取当前会话用户出售中的商品列表
     * <p/>
     * 获取当前用户作为卖家的出售中的商品列表，并能根据传入的搜索条件对出售中的商品列表进行过滤 只能获得商品的部分信息
     * 商品的详细信息请通过taobao.item.get获取
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public Map<String, Object> getItemsOnsale(Map<String, Object> argsMap) throws Exception {
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        Map<String, Object> temp = new HashMap<String, Object>();
        ItemsOnsaleGetResponse response = client.execute(req, sessionKey);
        temp.put(ConstantTaoBao.ITEMS, response.getItems());
        temp.put(ConstantTaoBao.TOTAL_RESULTS, response.getTotalResults());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return temp;
    }

    /**
     * taobao.product.add 上传一个产品，不包括产品非主图和属性图片
     * <p/>
     * 获取类目ID，必需是叶子类目ID；调用taobao.itemcats.get.v2获取
     * 传入关键属性,结构:pid:vid;pid:vid.调用taobao.itemprops.get.v2获取pid
     * 调用taobao.itempropvalues.get获取vid;如果碰到用户自定义属性,请用customer_props.
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public Product addProduct(Map<String, Object> argsMap) throws Exception {
        ProductAddRequest req = new ProductAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProduct();
    }

    /**
     * taobao.product.get 获取一个产品的信息
     * <p/>
     * 两种方式查看一个产品详细信息: 传入product_id来查询 传入cid和props来查询
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductGetResponse taobaoPproductGet(Map<String, Object> argsMap) throws Exception {
        ProductGetRequest req = new ProductGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductGetResponse response = client.execute(req, sessionKey);
        return response;
    }

    /**
     * taobao.product.img.delete 删除产品非主图
     * <p/>
     * 1.传入非主图ID 2.传入产品ID 删除产品非主图
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductImg deleteProductImg(Map<String, Object> argsMap) throws Exception {
        ProductImgDeleteRequest req = new ProductImgDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductImgDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductImg();
    }


    /**
     * taobao.product.img.upload 上传单张产品非主图，如果需要传多张，可调多次
     * <p/>
     * 1.传入产品ID 2.传入图片内容 注意：图片最大为500K,只支持JPG,GIF格式,如果需要传多张，可调多次
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductImg uploadProductImg(Map<String, Object> argsMap) throws Exception {
        ProductImgUploadRequest req = new ProductImgUploadRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductImgUploadResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductImg();
    }

    /**
     * taobao.product.propimg.delete 删除产品属性图
     * <p/>
     * 1.传入属性图片ID 2.传入产品ID 删除一个产品的属性图片
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductPropImg deleteProductPropimg(Map<String, Object> argsMap) throws Exception {
        ProductPropimgDeleteRequest req = new ProductPropimgDeleteRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductPropimgDeleteResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductPropImg();
    }

    /**
     * taobao.product.propimg.upload 上传单张产品属性图片，如果需要传多张，可调多次
     * <p/>
     * 传入产品ID 传入props,目前仅支持颜色属性.调用taobao.itemprops.get.v2取得颜色属性pid
     * 再用taobao.itempropvalues.get取得vid;格式:pid:vid,只能传入一个颜色pid:vid串; 传入图片内容
     * 注意：图片最大为2M,只支持JPG,GIF,如果需要传多张，可调多次
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductPropImg uploadProductPropimg(Map<String, Object> argsMap) throws Exception {
        ProductPropimgUploadRequest req = new ProductPropimgUploadRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductPropimgUploadResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductPropImg();
    }

    /**
     * taobao.product.update 修改一个产品，可以修改主图，不能修改子图片
     * <p/>
     * 传入产品ID 可修改字段：outer_id,binds,sale_props,name,price,desc,image
     * 注意：1.可以修改主图,不能修改子图片,主图最大500K,目前仅支持GIF,JPG 2.商城卖家产品发布24小时后不能作删除或修改操作
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public Product updateProduct(Map<String, Object> argsMap) throws Exception {
        ProductUpdateRequest req = new ProductUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProduct();
    }

    /**
     * taobao.products.get 获取产品列表
     * <p/>
     * 根据淘宝会员帐号搜索所有产品信息，只有天猫商家发布商品时才需要用到。
     * 注意：支持分页，每页最多返回100条,默认值为40,页码从1开始，默认为第一页
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<Product> getProducts(Map<String, Object> argsMap) throws Exception {
        ProductsGetRequest req = new ProductsGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        ProductsGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProducts();
    }


    /**
     * taobao.products.search 搜索产品信息
     * <p/>
     * 只有天猫商家发布商品时才需要用到； 两种方式搜索所有产品信息(二种至少传一种): 传入关键字q搜索 传入cid和props搜索
     * 返回值支持:product_id,name,pic_path,cid,props,price,tsc
     * 当用户指定了cid并且cid为垂直市场（3C电器城、鞋城）的类目id时，默认只返回小二确认的产品。
     * 如果用户没有指定cid，或cid为普通的类目，默认返回商家确认或小二确认的产品。
     * 如果用户自定了status字段，以指定的status类型为准
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public Map<String, Object> searchProducts(Map<String, Object> argsMap) throws Exception {
        ProductsSearchRequest req = new ProductsSearchRequest();
        ReflectUtils.executeMethods(req, argsMap);
        Map<String, Object> temp = new HashMap<String, Object>();
        ProductsSearchResponse response = client.execute(req, sessionKey);
        temp.put(ConstantTaoBao.TOTAL_RESULTS, response.getTotalResults());
        temp.put(ConstantTaoBao.PRODUCTS, response.getProducts());
        TaoBaoLogUtil.logTaoBaoApi(response);
        return temp;
    }


    /**
     * taobao.skus.custom.get 根据外部ID取商品SKU
     * <p/>
     * 跟据卖家设定的Sku的外部id获取商品，如果一个outer_id对应多个Sku会返回所有符合条件的sku
     * 这个Sku所属卖家从传入的session中获取，需要session绑定(注：iid标签里是num_iid的值，可以用作num_iid使用)
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<Sku> getSkusCustom(Map<String, Object> argsMap) throws Exception {
        SkusCustomGetRequest req = new SkusCustomGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        SkusCustomGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSkus();
    }

    /**
     * taobao.skus.quantity.update SKU库存修改
     * <p/>
     * 提供按照全量/增量的方式批量修改SKU库存的功能
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public SkusQuantityUpdateResponse updateSkusQuantity(Map<String, Object> argsMap) throws Exception {
        SkusQuantityUpdateRequest req = new SkusQuantityUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        SkusQuantityUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response;
    }

    /**
     * taobao.ticket.item.add 发布门票宝贝
     * <p/>
     * 发布门票商品接口，只支持新版门票发布 注意门票商品必须发布到对应达尔文体系下的产品上
     * 产品获取请走taobao.products.search接口。门票商品的产品规格信息包含门票商品的价格，库存，有效期等。是门票核心信息。
     * 一个门票宝贝可能包含多个产品规格（产品对应产品规格的获取请走tmall.product.specs.get接口）
     * 可以指定每个产品规格在不同时间段下不同有效期的门票的不同价格和库存。具体信息请参见skus字段描述。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public TicketItemProcessResult addTicketItem(Map<String, Object> argsMap) throws Exception {
        TicketItemAddRequest req = new TicketItemAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TicketItemAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getTicketItemProcessResult();
    }

    /**
     * taobao.ticket.item.get 获取新门票类目商品信息
     * <p/>
     * 获取新门票类目商品信息，目前只支持门票二期的商品
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public TicketItem getTicketItem(Map<String, Object> argsMap) throws Exception {
        TicketItemGetRequest req = new TicketItemGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TicketItemGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getTicketItem();
    }

    /**
     * taobao.ticket.item.update 编辑新门票类目商品
     * <p/>
     * 编辑新门票类目商品只有如下两种情况能够更换门票商品挂载的产品：从其他类目迁移到当前新门票类目
     * 或当前商品挂载的产品被屏蔽注意门票商品必须发布到对应达尔文体系下的产品上，产品获取请走taobao.products.search接口。
     * 门票商品的产品规格信息包含门票商品的价格，库存，有效期等。是门票核心信息。一个门票宝贝可能包含多个达尔文体系产品
     * （产品对应达尔文体系产品的获取请走tmall.product.specs.get接口）
     * 可以指定每个达尔文体系产品在不同时间段下不同有效期的门票的不同价格和库存。具体信息请参见skus字段描述。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public TicketItemProcessResult updateTicketItem(Map<String, Object> argsMap) throws Exception {
        TicketItemUpdateRequest req = new TicketItemUpdateRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TicketItemUpdateResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getTicketItemProcessResult();
    }

    /**
     * taobao.ticket.items.get 批量获取新门票类目商品信息
     * <p/>
     * 批量获取新门票类目商品信息
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<TicketItem> getTicketItems(Map<String, Object> argsMap) throws Exception {
        TicketItemsGetRequest req = new TicketItemsGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TicketItemsGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getTicketItems();
    }

    /**
     * taobao.topats.items.all.get 异步获取三个月内的所有商品详情
     * <p/>
     * 提供异步获取三个月内的所有（出售中和仓库中）商品详情信息接口。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public Task getTopatsItemsAll(Map<String, Object> argsMap) throws Exception {
        TopatsItemsAllGetRequest req = new TopatsItemsAllGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TopatsItemsAllGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getTask();
    }

    /**
     * taobao.ump.promotion.get 商品优惠详情查询
     * <p/>
     * 商品优惠详情查询，可查询商品设置的详细优惠。包括限时折扣，满就送等官方优惠以及第三方优惠
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public PromotionDisplayTop getPromotion(Map<String, Object> argsMap) throws Exception {
        UmpPromotionGetRequest req = new UmpPromotionGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        UmpPromotionGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getPromotions();
    }

    /**
     * tmall.brandcat.control.get 获取品牌类目的控制信息
     * <p/>
     * 获取到所有的类目，品牌的控制信息，达尔文类目是会以品牌和类目作为控制的力度来管理商品
     * 所有的控制信息可以通过这个接口获取到信息。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public BrandCatControlInfo getBrandcatControl(Map<String, Object> argsMap) throws Exception {
        TmallBrandcatControlGetRequest req = new TmallBrandcatControlGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallBrandcatControlGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getBrandCatControlInfo();
    }

    /**
     * tmall.brandcat.propinput.get 判断类目属性是否为可输入
     * <p/>
     * 针对监管类目品牌关键属性可输入判断逻辑比较复杂，提供简化属性可输入判断的接口，该接口兼容非关键属性和非监管类目品牌
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public PropertyInputDO getBrandcatPropinput(Map<String, Object> argsMap) throws Exception {
        TmallBrandcatPropinputGetRequest req = new TmallBrandcatPropinputGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallBrandcatPropinputGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getPropertyInput();
    }


    /**
     * tmall.brandcat.salespro.get 获取管控品牌类目的销售信息
     * <p/>
     * 获取被管控的品牌类目下销售信息，销售信息又分成两中，一种是规格属性，要求针对这个属性创建规格信息
     * 第二种是非规格属性（一般会被称为营销属性），这种信息主要是会影响到价格的变化，在做sku的时候会使用到。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<CatBrandSaleProp> getBrandcatSalespro(Map<String, Object> argsMap) throws Exception {
        TmallBrandcatSalesproGetRequest req = new TmallBrandcatSalesproGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallBrandcatSalesproGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getCatBrandSaleProps();
    }


    /**
     * tmall.item.desc.modules.get 商品描述模块信息获取
     * <p/>
     * 商品描述模块信息获取，包括运营设定的类目级别的模块信息以及用户自定义模块数量约束。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ModularDescInfo getItemDescModules(Map<String, Object> argsMap) throws Exception {
        TmallItemDescModulesGetRequest req = new TmallItemDescModulesGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallItemDescModulesGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getModularDescInfo();
    }


    /**
     * tmall.product.spec.add 添加产品规格
     * <p/>
     * 增加产品规格
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductSpec addProductSpec(Map<String, Object> argsMap) throws Exception {
        TmallProductSpecAddRequest req = new TmallProductSpecAddRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallProductSpecAddResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductSpec();
    }


    /**
     * tmall.product.spec.get 根据产品规格的Id号获取当个的规格信息
     * <p/>
     * 通过当个的spec_id获取到这个产品规格的信息，主要是因为产品规格是要经过审核的，所以通过这个接口可以获取到是否通过审核
     * 通过参看这个ProductSpec的status判断： 1:表示审核通过 3:表示等待审核。 如果你的id找不到数据，那么就是审核被拒绝。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public ProductSpec getProductSpec(Map<String, Object> argsMap) throws Exception {
        TmallProductSpecGetRequest req = new TmallProductSpecGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallProductSpecGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductSpec();
    }


    /**
     * tmall.product.spec.pic.upload 上传产品规格认证图片
     * <p/>
     * 上传指定类型的产品规格认证文件，并返回存有上传成功图片url的产品规格对象
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public String uploadProductSpecPic(Map<String, Object> argsMap) throws Exception {
        TmallProductSpecPicUploadRequest req = new TmallProductSpecPicUploadRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallProductSpecPicUploadResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getSpecPicUrl();
    }


    /**
     * tmall.product.specs.get 获取产品的规格信息
     * <p/>
     * 按productID下载或品牌下载产品规格，返回一组的产品规格信息。
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<ProductSpec> getProductSpecs(Map<String, Object> argsMap) throws Exception {
        TmallProductSpecsGetRequest req = new TmallProductSpecsGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallProductSpecsGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getProductSpecs();
    }


    /**
     * tmall.product.specs.ticket.get 产品规格审核信息获取接口
     * <p/>
     * 批量根据specId查询产品规格审核信息包括产品规格状态，申请人，拒绝原因等
     *
     * @param argsMap 包含参数的Map，键值表示要传入的参数名，value表示要传入的参数值:<br/>
     * @return
     * @throws Exception
     */
    public List<Ticket> getProductSpecsTicket(Map<String, Object> argsMap) throws Exception {
        TmallProductSpecsTicketGetRequest req = new TmallProductSpecsTicketGetRequest();
        ReflectUtils.executeMethods(req, argsMap);
        TmallProductSpecsTicketGetResponse response = client.execute(req, sessionKey);
        TaoBaoLogUtil.logTaoBaoApi(response);
        return response.getTickets();
    }
}
