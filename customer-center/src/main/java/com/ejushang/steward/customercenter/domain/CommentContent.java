package com.ejushang.steward.customercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:42
 */
@Table(name = "t_comment_content")
@Entity
public class CommentContent implements EntityClass<Integer> {
    private Integer id;
    private Date commentTime;
    private String content;
    private String reply;
    private Integer commentId;
    private Comment comment;

    @Column(name = "comment_id")
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name ="comment_time")
    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @javax.persistence.Column(name ="content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @javax.persistence.Column(name ="reply")
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="comment_id",insertable = false, updatable = false)
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
