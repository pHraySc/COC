package com.ailk.biapp.ci.model;

import java.text.DecimalFormat;

public class CiLabelRelModel {
    private String dataDate;
    private Integer mainLabelId;
    private String mainLabelName;
    private Integer relLabelId;
    private String relLabelName;
    private String relLabelIds;
    private String relLabelNames;
    private Long mainLabelUserNum;
    private Long relLabelUserNum;
    private Long overlapUserNum;
    private String overlapUserNumStr;
    private String proportion;
    private Integer radius;
    private Integer x;
    private Integer y;
    private Integer m_x;
    private Integer m_y;
    private String mainLabelUserNumShow;

    public CiLabelRelModel() {
    }

    public Integer getM_x() {
        return this.m_x;
    }

    public void setM_x(Integer mX) {
        this.m_x = mX;
    }

    public Integer getM_y() {
        return this.m_y;
    }

    public void setM_y(Integer mY) {
        this.m_y = mY;
    }

    public String getRelLabelName() {
        return this.relLabelName;
    }

    public void setRelLabelName(String relLabelName) {
        this.relLabelName = relLabelName;
    }

    public String getOverlapUserNumStr() {
        return this.overlapUserNumStr;
    }

    public void setOverlapUserNumStr(String overlapUserNumStr) {
        this.overlapUserNumStr = overlapUserNumStr;
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

    public String getRelLabelNames() {
        return this.relLabelNames;
    }

    public void setRelLabelNames(String relLabelNames) {
        this.relLabelNames = relLabelNames;
    }

    public String getMainLabelName() {
        return this.mainLabelName;
    }

    public void setMainLabelName(String mainLabelName) {
        this.mainLabelName = mainLabelName;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public Integer getMainLabelId() {
        return this.mainLabelId;
    }

    public void setMainLabelId(Integer mainLabelId) {
        this.mainLabelId = mainLabelId;
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

    public Long getMainLabelUserNum() {
        return this.mainLabelUserNum;
    }

    public void setMainLabelUserNum(Long mainLabelUserNum) {
        this.mainLabelUserNum = mainLabelUserNum;
    }

    public Long getRelLabelUserNum() {
        return this.relLabelUserNum;
    }

    public void setRelLabelUserNum(Long relLabelUserNum) {
        this.relLabelUserNum = relLabelUserNum;
    }

    public Long getOverlapUserNum() {
        return this.overlapUserNum;
    }

    public void setOverlapUserNum(Long overlapUserNum) {
        this.overlapUserNum = overlapUserNum;
    }

    public String getProportion() {
        double temp = 0.0D;
        if(this.getOverlapUserNum() != null && this.getMainLabelUserNum() != null && this.getOverlapUserNum().longValue() != 0L && this.getMainLabelUserNum().longValue() != 0L) {
            temp = this.getOverlapUserNum().doubleValue() / this.getMainLabelUserNum().doubleValue() * 100.0D;
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

    public void setMainLabelUserNumShow(String format) {
        this.mainLabelUserNumShow = format;
    }

    public String getMainLabelUserNumShow() {
        return this.mainLabelUserNumShow;
    }
}
