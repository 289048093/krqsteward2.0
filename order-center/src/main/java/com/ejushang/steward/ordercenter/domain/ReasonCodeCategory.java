package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/26 17:54
 */
@Table(name = "t_reason_code_category")
@Entity
public class ReasonCodeCategory implements EntityClass<Integer>,OperableData {
    private Integer id;
    private String name;
    private Integer parentId;
    private ReasonCodeCategory parent;
    private List<ReasonCodeCategory> children = new ArrayList<ReasonCodeCategory>();
    private Set<ReasonCode> reasonCodes = new HashSet<ReasonCode>();
    private int level;

    private Date createTime;
    private Date updateTime;
    private Integer operatorId;

    public static final int LEVEL_1=1;
    public static final int LEVEL_2=2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="parent_id")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Column(name="create_time")
    @JsonIgnore
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="update_time")
    @JsonIgnore
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name="operator_id")
    @JsonIgnore
    public Integer getOperatorId() {
        return operatorId;
    }


    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    public ReasonCodeCategory getParent() {
        return parent;
    }

    public void setParent(ReasonCodeCategory parent) {
        this.parent = parent;
    }

    @Transient
    public List<ReasonCodeCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ReasonCodeCategory> children) {
        this.children = children;
    }

    @Transient
    public Set<ReasonCode> getReasonCodes() {
        return reasonCodes;
    }

    public void setReasonCodes(Set<ReasonCode> reasonCodes) {
        this.reasonCodes = reasonCodes;
    }
}
