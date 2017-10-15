package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaChartConfig implements Serializable {
    private static final long serialVersionUID = -1323006385688621371L;
    private Integer primaryKeyId;
    private String chartId;
    private Integer dimNum;
    private Integer indexNum;
    private Integer isDefault;

    public CiIaChartConfig() {
    }

    public CiIaChartConfig(String chartId, Integer dimNum, Integer indexNum, Integer isDefault) {
        this.chartId = chartId;
        this.dimNum = dimNum;
        this.indexNum = indexNum;
        this.isDefault = isDefault;
    }

    public Integer getPrimaryKeyId() {
        return this.primaryKeyId;
    }

    public void setPrimaryKeyId(Integer primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public String getChartId() {
        return this.chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public Integer getDimNum() {
        return this.dimNum;
    }

    public void setDimNum(Integer dimNum) {
        this.dimNum = dimNum;
    }

    public Integer getIndexNum() {
        return this.indexNum;
    }

    public void setIndexNum(Integer indexNum) {
        this.indexNum = indexNum;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
}
