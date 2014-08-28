package com.ejushang.steward.ordercenter.vo;

import com.ejushang.steward.ordercenter.domain.Product;
import com.ejushang.steward.ordercenter.domain.Repository;

/**
 * 库存VO
 *
 * User: Sed.Li(李朝)
 * Date: 14-4-14
 * Time: 下午2:34
 */
public class StorageVO {
    private Integer id;

    /**
     * 库存数量
     */
    private Integer amount;

    /**
     * 操作前库存
     */
    private Integer beforeAmount;

    /**
     * 操作后库存
     */
    private Integer afterAmount;


    /**
     * 产品ID
     */
    private Integer productId;

    private Product product;

    /**
     * 仓库ID
     */
    private Integer repositoryId;

    private Repository repository;

    /**
     *  校验状态信息
     */
    private String statusMsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Integer getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(Integer beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public Integer getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(Integer afterAmount) {
        this.afterAmount = afterAmount;
    }
}
