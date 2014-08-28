package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.ordercenter.constant.BlacklistType;
import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.domain.util.OperableData;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:54
 */

@Table(name = "t_blacklist")
@Entity
public class Blacklist  implements EntityClass<Integer>,OperableData {
    private Integer id;
    private BlacklistType type;
    private String value;

    private Date createTime;
    private Date updateTime;

    private Integer operatorId;

    public Blacklist(){}

    public Blacklist(BlacklistType type, String value){
        this.type=type;
        this.value=value;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @javax.persistence.Column(name ="type")
    public BlacklistType getType() {
        return type;
    }

    public void setType(BlacklistType type) {
        this.type = type;
    }

    @javax.persistence.Column(name ="value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @javax.persistence.Column(name ="create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @javax.persistence.Column(name ="operator_id")
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    @javax.persistence.Column(name ="update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
