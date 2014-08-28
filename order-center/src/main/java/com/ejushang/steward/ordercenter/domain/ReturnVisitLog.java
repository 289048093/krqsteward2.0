package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;
import com.ejushang.steward.ordercenter.constant.DialStatus;
import com.ejushang.steward.ordercenter.constant.ReturnVisitStatus;
import com.ejushang.steward.ordercenter.constant.Satisfaction;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: codec.yang
 * Date: 2014/8/4 15:54
 */

@Table(name = "t_return_visit_log")
@Entity
public class ReturnVisitLog implements EntityClass<Integer> {
    private Integer id;
    private Integer taskId;
    private Integer orderId;
    private String visitorName;
    private String visitorRealname;
    private ReturnVisitStatus status;
    private Date createTime;
    private DialStatus dialStatus;
    private Date appointmentTime;
    private Boolean used=true;
    private String remark;
    private Boolean redirectAfterSale=false;
    private Boolean problemAcceptedSoon;
    private Boolean isProblemSolved;
    private Satisfaction serviceSatisfaction;
    private Boolean addToBlacklist=false;
    private Set<ReasonCode> reasonCodes=new HashSet<ReasonCode>(0);



    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "task_id")
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Column(name = "visitor_name")
    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    @Column(name = "visitor_realname")
    public String getVisitorRealname() {
        return visitorRealname;
    }

    public void setVisitorRealname(String visitorRealname) {
        this.visitorRealname = visitorRealname;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public ReturnVisitStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnVisitStatus status) {
        this.status = status;
    }

    @Column(name = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "dial_status")
    @Enumerated(EnumType.STRING)
    public DialStatus getDialStatus() {
        return dialStatus;
    }

    public void setDialStatus(DialStatus dialStatus) {
        this.dialStatus = dialStatus;
    }

    @Column(name = "appointment_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Column(name = "used")
    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @ManyToMany(fetch=FetchType.LAZY,targetEntity = ReasonCode.class)
    @JoinTable(
            name="t_returnvisitlog_reasoncode",
            joinColumns=@JoinColumn(name="log_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="reason_id",referencedColumnName="id")
    )
    public Set<ReasonCode> getReasonCodes() {
        return reasonCodes;
    }

    public void setReasonCodes(Set<ReasonCode> reasonCodes) {
        this.reasonCodes = reasonCodes;
    }

    @Column(name = "redirect_after_sale")
    public Boolean getRedirectAfterSale() {
        return redirectAfterSale;
    }

    public void setRedirectAfterSale(Boolean redirectAfterSale) {
        this.redirectAfterSale = redirectAfterSale;
    }

    @Column(name = "problem_accepted_soon")
    public Boolean getProblemAcceptedSoon() {
        return problemAcceptedSoon;
    }

    public void setProblemAcceptedSoon(Boolean problemAcceptedSoon) {
        this.problemAcceptedSoon = problemAcceptedSoon;
    }

    @Column(name = "is_problem_solved")
    public Boolean getIsProblemSolved() {
        return isProblemSolved;
    }

    public void setIsProblemSolved(Boolean isProblemSolved) {
        this.isProblemSolved = isProblemSolved;
    }

    @Column(name="service_satisfaction")
    @Enumerated(EnumType.STRING)
    public Satisfaction getServiceSatisfaction() {
        return serviceSatisfaction;
    }

    public void setServiceSatisfaction(Satisfaction serviceSatisfaction) {
        this.serviceSatisfaction = serviceSatisfaction;
    }

    @Column(name="add_to_blacklist")
    public Boolean getAddToBlacklist() {
        return addToBlacklist;
    }

    public void setAddToBlacklist(Boolean addToBlacklist) {
        this.addToBlacklist = addToBlacklist;
    }
}
