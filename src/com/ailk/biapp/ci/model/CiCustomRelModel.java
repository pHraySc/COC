package com.ailk.biapp.ci.model;

import java.text.DecimalFormat;

public class CiCustomRelModel {
    private String dataDate;
    private String customGroupId;
    private String customGroupName;
    private Integer relLabelId;
    private String relLabelName;
    private String relLabelIds;
    private String relLabelNames;
    private Integer mainCustomUserNum;
    private String listTableName;
    private Integer relLabelUserNum;
    private Integer overlapUserNum;
    private String overlapUserNumStr;
    private String proportion;
    private Integer radius;
    private Integer x;
    private Integer y;

    public CiCustomRelModel() {
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getCustomGroupId() {
        return this.customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public String getCustomGroupName() {
        return this.customGroupName;
    }

    public void setCustomGroupName(String customGroupName) {
        this.customGroupName = customGroupName;
    }

    public Integer getRelLabelId() {
        return this.relLabelId;
    }

    public void setRelLabelId(Integer relLabelId) {
        this.relLabelId = relLabelId;
    }

    public String getRelLabelIds() {
        return this.relLabelIds;
    }

    public void setRelLabelIds(String relLabelIds) {
        this.relLabelIds = relLabelIds;
    }

    public String getRelLabelNames() {
        return this.relLabelNames;
    }

    public void setRelLabelNames(String relLabelNames) {
        this.relLabelNames = relLabelNames;
    }

    public Integer getMainCustomUserNum() {
        return this.mainCustomUserNum;
    }

    public void setMainCustomUserNum(Integer mainCustomUserNum) {
        this.mainCustomUserNum = mainCustomUserNum;
    }

    public Integer getRelLabelUserNum() {
        return this.relLabelUserNum;
    }

    public void setRelLabelUserNum(Integer relLabelUserNum) {
        this.relLabelUserNum = relLabelUserNum;
    }

    public Integer getOverlapUserNum() {
        return this.overlapUserNum;
    }

    public void setOverlapUserNum(Integer overlapUserNum) {
        this.overlapUserNum = overlapUserNum;
    }

    public String getOverlapUserNumStr() {
        return this.overlapUserNumStr;
    }

    public void setOverlapUserNumStr(String overlapUserNumStr) {
        this.overlapUserNumStr = overlapUserNumStr;
    }

    public String getProportion() {
        double temp = 0.0D;
        if(this.getOverlapUserNum() != null && this.getMainCustomUserNum() != null && this.getOverlapUserNum().intValue() != 0 && this.getMainCustomUserNum().intValue() != 0) {
            temp = this.getOverlapUserNum().doubleValue() / this.getMainCustomUserNum().doubleValue() * 100.0D;
            DecimalFormat df = new DecimalFormat("0.00");
            this.proportion = df.format(temp);
        } else {
            this.proportion = "0.00";
        }

        return this.proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public Integer getRadius() {
        return this.radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public String getRelLabelName() {
        return this.relLabelName;
    }

    public void setRelLabelName(String relLabelName) {
        this.relLabelName = relLabelName;
    }
}
