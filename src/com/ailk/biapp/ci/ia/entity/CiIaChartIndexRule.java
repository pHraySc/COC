package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaChartIndexRule implements Serializable {
    private static final long serialVersionUID = 6524545797046804337L;
    private Integer ruleId;
    private Integer attrId;
    private Integer type;
    private Integer aggregationId;
    private Integer worksheetChartRelId;
    private Integer isShowUnit;
    private String customUnit;
    private String dataFormat;
    private Integer sortType;

    public CiIaChartIndexRule() {
    }

    public CiIaChartIndexRule(Integer attrId, Integer type, Integer aggregationId, Integer worksheetChartRelId, Integer isShowUnit, String customUnit, String dataFormat, Integer sortType) {
        this.attrId = attrId;
        this.type = type;
        this.aggregationId = aggregationId;
        this.worksheetChartRelId = worksheetChartRelId;
        this.isShowUnit = isShowUnit;
        this.customUnit = customUnit;
        this.dataFormat = dataFormat;
        this.sortType = sortType;
    }

    public Integer getAggregationId() {
        return this.aggregationId;
    }

    public Integer getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getAttrId() {
        return this.attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public void setAggregationId(Integer aggregationId) {
        this.aggregationId = aggregationId;
    }

    public Integer getWorksheetChartRelId() {
        return this.worksheetChartRelId;
    }

    public void setWorksheetChartRelId(Integer worksheetChartRelId) {
        this.worksheetChartRelId = worksheetChartRelId;
    }

    public Integer getIsShowUnit() {
        return this.isShowUnit;
    }

    public void setIsShowUnit(Integer isShowUnit) {
        this.isShowUnit = isShowUnit;
    }

    public String getDataFormat() {
        return this.dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public Integer getSortType() {
        return this.sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCustomUnit() {
        return this.customUnit;
    }

    public void setCustomUnit(String customUnit) {
        this.customUnit = customUnit;
    }
}
