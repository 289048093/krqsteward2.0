package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-8-25
 * Time: 下午3:26
 */
@Table(name = "t_rate_info")
@Entity
public class RateInfo implements EntityClass<Integer>{
    private Integer id;
    private String userNick;
    private String content;
    private Date commentTime;
    private Boolean hasNegtv;
    private String appendContent;
    private Date appendTime;
    private Boolean appendHasNegtv;
    private Integer rateTagId;
    /**
     * 原始评价对应的标签列表
     */
    private List<RateTag> tags=new ArrayList<RateTag>(0);
    /**追加评价中带有的语义标签列表 */
    private List<RateTag>  appendTags =new ArrayList<RateTag>(0);
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name ="user_nick")
    @Basic
    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
    @Column(name ="content")
    @Basic
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Column(name ="comment_time")
    @Basic
    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
    @Column(name ="has_negtv")
    @Basic
    public Boolean getHasNegtv() {
        return hasNegtv;
    }

    public void setHasNegtv(Boolean hasNegtv) {
        this.hasNegtv = hasNegtv;
    }
    @Column(name ="append_content")
    @Basic
    public String getAppendContent() {
        return appendContent;
    }

    public void setAppendContent(String appendContent) {
        this.appendContent = appendContent;
    }
    @Column(name ="append_time")
    @Basic
    public Date getAppendTime() {
        return appendTime;
    }

    public void setAppendTime(Date appendTime) {
        this.appendTime = appendTime;
    }
    @Column(name ="append_has_negtv")
    @Basic
    public Boolean getAppendHasNegtv() {
        return appendHasNegtv;
    }

    public void setAppendHasNegtv(Boolean appendHasNegtv) {
        this.appendHasNegtv = appendHasNegtv;
    }
    @Column(name ="rate_tag_id")
    @Basic
    public Integer getRateTagId() {
        return rateTagId;
    }

    public void setRateTagId(Integer rateTagId) {
        this.rateTagId = rateTagId;
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rateInfo")
    public List<RateTag> getTags() {
        return tags;
    }

    public void setTags(List<RateTag> tags) {
        this.tags = tags;
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rateInfo")
    public List<RateTag> getAppendTags() {
        return appendTags;
    }

    public void setAppendTags(List<RateTag> appendTags) {
        this.appendTags = appendTags;
    }
}
