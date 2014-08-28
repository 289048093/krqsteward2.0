package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_storage")
@Entity
public class Storage implements EntityClass<Integer>{

    private Integer id;

    /**
     * 库存数量
     */
    private Integer amount;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="repository_id", insertable = false, updatable = false)
    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "product_id")
    @Basic
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    @javax.persistence.Column(name = "repository_id")
    @Basic
    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }


    @javax.persistence.Column(name = "amount")
    @Basic
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
