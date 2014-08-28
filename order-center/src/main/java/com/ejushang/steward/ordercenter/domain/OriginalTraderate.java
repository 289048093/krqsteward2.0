package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.common.util.Money;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Shiro
 * Date: 14-8-21
 * Time: 下午5:11
 */
@Table(name = "t_original_traderate")
@Entity
public class OriginalTraderate implements EntityClass<Integer> {
    private Integer id;
    /**交易ID*/
    private Integer tid;
    /**子订单ID*/
    private Integer oid;
    /**评价者角色.可选值:seller(卖家),buyer(买家)*/
    private String role;
    /**评价者昵称*/
    private String nick;
    /**评价结果,可选值:good(好评),neutral(中评),bad(差评)*/
    private String result;
    /**评价创建时间,格式:yyyy-MM-dd HH:mm:ss*/
    private Date created;
    /**被评价者昵称*/
    private String ratedNick;
    /**商品标题*/
    private String itemTitle;
    /**商品价格,精确到2位小数;单位:元.如:200.07，表示:200元7分*/
    private Money itemPrice;
    /**评价内容,最大长度:500个汉字*/
    private String content;
    /**评价解释,最大长度:500个汉字*/
    private String reply;
    /**商品的数字ID*/
    private Integer numIid;
    /**评价信息是否用于记分， 可取值：true(参与记分)和false(不参与记分)*/
    private boolean validScore;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name ="tid")
    @Basic
    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
    @Column(name ="oid")
    @Basic
    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }
    @Column(name ="role")
    @Basic
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @Column(name ="nick")
    @Basic
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    @Column(name ="result")
    @Basic
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    @Column(name ="created")
    @Basic
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    @Column(name ="rated_nick")
    @Basic
    public String getRatedNick() {
        return ratedNick;
    }

    public void setRatedNick(String ratedNick) {
        this.ratedNick = ratedNick;
    }
    @Column(name ="item_title")
    @Basic
    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    @Column(name ="item_price")
    @Basic
    public Money getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Money itemPrice) {
        this.itemPrice = itemPrice;
    }
    @Column(name ="content")
    @Basic
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Column(name ="reply")
    @Basic
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
    @Column(name ="num_iid")
    @Basic
    public Integer getNumIid() {
        return numIid;
    }

    public void setNumIid(Integer numIid) {
        this.numIid = numIid;
    }
    @Column(name ="valid_score")
    @Basic
    public boolean isValidScore() {
        return validScore;
    }

    public void setValidScore(boolean validScore) {
        this.validScore = validScore;
    }
}
