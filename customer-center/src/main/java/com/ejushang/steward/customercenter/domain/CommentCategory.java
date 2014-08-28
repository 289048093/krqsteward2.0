package com.ejushang.steward.customercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:38
 */
@Table(name = "t_comment_category")
@Entity
public class CommentCategory implements EntityClass<Integer> {
    private Integer id;
    private String name;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name ="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
