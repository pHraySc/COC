package com.ailk.biapp.ci.service.impl;

import java.io.Serializable;

public class ReviseDateModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reviseType;
    private String reviseMonth;
    private String reviseDay;

    public ReviseDateModel() {
    }

    public String getReviseType() {
        return this.reviseType;
    }

    public void setReviseType(String reviseType) {
        this.reviseType = reviseType;
    }

    public String getReviseMonth() {
        return this.reviseMonth;
    }

    public void setReviseMonth(String reviseMonth) {
        this.reviseMonth = reviseMonth;
    }

    public String getReviseDay() {
        return this.reviseDay;
    }

    public void setReviseDay(String reviseDay) {
        this.reviseDay = reviseDay;
    }

    public String toString() {
        return "ReviseDateModel [reviseType=" + this.reviseType + ", reviseMonth=" + this.reviseMonth + ", reviseDay=" + this.reviseDay + "]";
    }
}
