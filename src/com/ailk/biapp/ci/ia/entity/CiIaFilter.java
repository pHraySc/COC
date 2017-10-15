package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaFilter implements Serializable {
    private static final long serialVersionUID = 7711950225243332269L;
    private Integer filterId;
    private Integer worksheetChartRelId;
    private Integer attrId;
    private Integer labelFlag;
    private String attrVal;
    private String startTime;
    private String endTime;
    private String contiueMinVal;
    private String contiueMaxVal;
    private String leftZoneSign;
    private String rightZoneSign;
    private String exactValue;
    private String darkValue;

    public CiIaFilter() {
    }

    public CiIaFilter(Integer worksheetChartRelId, Integer attrId, Integer labelFlag, String attrVal, String startTime, String endTime, String contiueMinVal, String contiueMaxVal, String leftZoneSign, String rightZoneSign, String exactValue, String darkValue) {
        this.worksheetChartRelId = worksheetChartRelId;
        this.attrId = attrId;
        this.labelFlag = labelFlag;
        this.attrVal = attrVal;
        this.startTime = startTime;
        this.endTime = endTime;
        this.contiueMinVal = contiueMinVal;
        this.contiueMaxVal = contiueMaxVal;
        this.leftZoneSign = leftZoneSign;
        this.rightZoneSign = rightZoneSign;
        this.exactValue = exactValue;
        this.darkValue = darkValue;
    }

    public Integer getFilterId() {
        return this.filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public Integer getWorksheetChartRelId() {
        return this.worksheetChartRelId;
    }

    public void setWorksheetChartRelId(Integer worksheetChartRelId) {
        this.worksheetChartRelId = worksheetChartRelId;
    }

    public Integer getLabelFlag() {
        return this.labelFlag;
    }

    public void setLabelFlag(Integer labelFlag) {
        this.labelFlag = labelFlag;
    }

    public String getAttrVal() {
        return this.attrVal;
    }

    public void setAttrVal(String attrVal) {
        this.attrVal = attrVal;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContiueMinVal() {
        return this.contiueMinVal;
    }

    public void setContiueMinVal(String contiueMinVal) {
        this.contiueMinVal = contiueMinVal;
    }

    public String getContiueMaxVal() {
        return this.contiueMaxVal;
    }

    public void setContiueMaxVal(String contiueMaxVal) {
        this.contiueMaxVal = contiueMaxVal;
    }

    public String getLeftZoneSign() {
        return this.leftZoneSign;
    }

    public void setLeftZoneSign(String leftZoneSign) {
        this.leftZoneSign = leftZoneSign;
    }

    public String getRightZoneSign() {
        return this.rightZoneSign;
    }

    public void setRightZoneSign(String rightZoneSign) {
        this.rightZoneSign = rightZoneSign;
    }

    public String getExactValue() {
        return this.exactValue;
    }

    public void setExactValue(String exactValue) {
        this.exactValue = exactValue;
    }

    public String getDarkValue() {
        return this.darkValue;
    }

    public void setDarkValue(String darkValue) {
        this.darkValue = darkValue;
    }

    public Integer getAttrId() {
        return this.attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }
}
