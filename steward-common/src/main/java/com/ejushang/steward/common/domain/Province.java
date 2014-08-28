package com.ejushang.steward.common.domain;

import com.ejushang.steward.common.constant.Constants;
import com.ejushang.steward.common.domain.util.EntityClass;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * User: liubin
 * Date: 14-3-28
 */
@javax.persistence.Table(name = "t_province")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = Constants.ENTITY_CACHE_NAME)
@Entity
public class Province implements EntityClass<String> {

    private String id;

    private String name;


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
