package com.ailk.biapp.ci.feedback.entity;

import java.io.Serializable;

public class DimDeclareDealStatus implements Serializable {
    private Integer dealStatusId;
    private String dealStatusName;
    private Integer sortNum;

    public DimDeclareDealStatus() {
    }

    public DimDeclareDealStatus(String dealStatusName, Integer sortNum) {
        this.dealStatusName = dealStatusName;
        this.sortNum = sortNum;
    }

    public Integer getDealStatusId() {
        return this.dealStatusId;
    }

    public void setDealStatusId(Integer dealStatusId) {
        this.dealStatusId = dealStatusId;
    }

    public String getDealStatusName() {
        return this.dealStatusName;
    }

    public void setDealStatusName(String dealStatusName) {
        this.dealStatusName = dealStatusName;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
}
