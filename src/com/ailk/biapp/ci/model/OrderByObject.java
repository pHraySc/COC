package com.ailk.biapp.ci.model;

public class OrderByObject {
    private String orderBy = "";
    private int ascOrDesc = 1;

    public OrderByObject() {
    }

    public OrderByObject(String orderBy, int ascOrDesc) {
        this.orderBy = orderBy;
        this.ascOrDesc = ascOrDesc;
    }

    public OrderByObject(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setAscOrDesc(int ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public int getAscOrDesc() {
        return this.ascOrDesc;
    }
}
