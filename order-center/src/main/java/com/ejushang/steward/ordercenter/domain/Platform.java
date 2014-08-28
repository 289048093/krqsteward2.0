package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午12:06
 * 目前正在运营的平台
 */
@Table(name = "t_platform")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Platform implements EntityClass<Integer> {

    private Integer id;
    /**
     * 平台名
     */
    private String name;

    /**
     * 平台类型编码
     */
    private String type;


    /**
     * 产品产品地址前缀
     */
    private String prodLinkPrefix;

    /**
     * 库存占比
     */
    private Integer storagePercent;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "type")
    @Basic
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "prod_link_prefix")
    @Basic
    public String getProdLinkPrefix() {
        return prodLinkPrefix;
    }

    public void setProdLinkPrefix(String prodLinkPrefix) {
        this.prodLinkPrefix = prodLinkPrefix;
    }

    @Column(name = "storage_percent")
    @Basic
    public Integer getStoragePercent() {
        return storagePercent;
    }

    public void setStoragePercent(Integer storagePercent) {
        this.storagePercent = storagePercent;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", prodLinkPrefix='" + prodLinkPrefix + '\'' +
                ", storagePercent=" + storagePercent +
                '}';
    }
}
