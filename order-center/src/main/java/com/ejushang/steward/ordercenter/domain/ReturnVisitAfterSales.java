package com.ejushang.steward.ordercenter.domain;

import com.ejushang.steward.common.domain.util.EntityClass;

import javax.persistence.*;

/**
 * Created by: codec.yang
 * Date: 2014/8/15 11:41
 */
@Table(name = "t_returnvisit_aftersales")
@Entity
public class ReturnVisitAfterSales implements EntityClass<Integer> {

    private Integer id;
    private Integer returnVisitId;
    private String afterSalesNo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="return_visit_id")
    public Integer getReturnVisitId() {
        return returnVisitId;
    }

    public void setReturnVisitId(Integer returnVisitId) {
        this.returnVisitId = returnVisitId;
    }

    @Column(name="after_sales_no")
    public String getAfterSalesNo() {
        return afterSalesNo;
    }

    public void setAfterSalesNo(String afterSalesNo) {
        this.afterSalesNo = afterSalesNo;
    }
}
