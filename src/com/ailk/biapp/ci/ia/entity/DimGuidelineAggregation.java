package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class DimGuidelineAggregation implements Serializable {
    private static final long serialVersionUID = -5488481575918679161L;
    private Integer aggregationId;
    private String aggregationName;
    private String aggregationDesc;
    private String aggregationFunc;

    public DimGuidelineAggregation() {
    }

    public DimGuidelineAggregation(String aggregationName, String aggregationDesc, String aggregationFunc) {
        this.aggregationName = aggregationName;
        this.aggregationDesc = aggregationDesc;
        this.aggregationFunc = aggregationFunc;
    }

    public Integer getAggregationId() {
        return this.aggregationId;
    }

    public void setAggregationId(Integer aggregationId) {
        this.aggregationId = aggregationId;
    }

    public String getAggregationName() {
        return this.aggregationName;
    }

    public void setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
    }

    public String getAggregationDesc() {
        return this.aggregationDesc;
    }

    public void setAggregationDesc(String aggregationDesc) {
        this.aggregationDesc = aggregationDesc;
    }

    public String getAggregationFunc() {
        return this.aggregationFunc;
    }

    public void setAggregationFunc(String aggregationFunc) {
        this.aggregationFunc = aggregationFunc;
    }
}
