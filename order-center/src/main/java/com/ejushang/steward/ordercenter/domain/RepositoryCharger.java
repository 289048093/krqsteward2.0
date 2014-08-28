package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.ejushang.steward.common.hibernate.UglyTimestampUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 仓库和仓库管理员中间表
 * User:  Sed.Lee(李朝)
 * Date: 14-7-28
 * Time: 上午10:48
 */
@javax.persistence.Table(name = "t_repository_charger")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class RepositoryCharger implements EntityClass<Integer>, OperableData {

    private Integer id;

    private Integer repositoryId;

    private Repository repository;

    private Date createTime;

    private Date updateTime;

    /**
     * 管理员id
     */
    private Integer chargerId;

    @Override
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "repository_id")
    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", updatable = false, insertable = false)
    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Column(name = "charger_id")
    public Integer getChargerId() {
        return chargerId;
    }

    public void setChargerId(Integer chargerId) {
        this.chargerId = chargerId;
    }

    @javax.persistence.Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = UglyTimestampUtil.convertTimestampToDate(createTime);
    }


    @javax.persistence.Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setOperatorId(Integer operatorId) {
    }

    @Override
    @Transient
    public Integer getOperatorId() {
        return null;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = UglyTimestampUtil.convertTimestampToDate(updateTime);
    }

    @Override
    public String toString() {
        return String.format("{RepositoryCharger:{id:%d,repositoryId:%d,chargerId:%d}}", id, repositoryId, chargerId);
    }
}
