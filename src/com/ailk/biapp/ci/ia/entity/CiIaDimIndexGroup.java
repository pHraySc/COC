package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaDimIndexGroup implements Serializable {
    private static final long serialVersionUID = -8591443556145720443L;
    private Integer groupId;
    private Integer worksheetChartRelId;
    private String groupName;
    private String dimValue;
    private Integer isIsometry;
    private String dimBegin;
    private String dimEnd;
    private String leftZoneSign;
    private String rightZoneSign;
    private Integer attrId;
    private Integer gradeNum;

    public CiIaDimIndexGroup() {
    }

    public CiIaDimIndexGroup(Integer attrId) {
        this.attrId = attrId;
    }

    public CiIaDimIndexGroup(Integer worksheetChartRelId, String groupName, String dimValue, Integer isIsometry, String dimBegin, String dimEnd, String leftZoneSign, String rightZoneSign, Integer attrId) {
        this.worksheetChartRelId = worksheetChartRelId;
        this.groupName = groupName;
        this.dimValue = dimValue;
        this.isIsometry = isIsometry;
        this.dimBegin = dimBegin;
        this.dimEnd = dimEnd;
        this.leftZoneSign = leftZoneSign;
        this.rightZoneSign = rightZoneSign;
        this.attrId = attrId;
    }

    public CiIaDimIndexGroup(Integer worksheetChartRelId, String groupName, String dimValue, Integer isIsometry, String dimBegin, String dimEnd, String leftZoneSign, String rightZoneSign, Integer attrId, Integer gradeNum) {
        this.worksheetChartRelId = worksheetChartRelId;
        this.groupName = groupName;
        this.dimValue = dimValue;
        this.isIsometry = isIsometry;
        this.dimBegin = dimBegin;
        this.dimEnd = dimEnd;
        this.leftZoneSign = leftZoneSign;
        this.rightZoneSign = rightZoneSign;
        this.attrId = attrId;
        this.gradeNum = gradeNum;
    }

    public Integer getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getWorksheetChartRelId() {
        return this.worksheetChartRelId;
    }

    public void setWorksheetChartRelId(Integer worksheetChartRelId) {
        this.worksheetChartRelId = worksheetChartRelId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDimValue() {
        return this.dimValue;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue;
    }

    public Integer getIsIsometry() {
        return this.isIsometry;
    }

    public void setIsIsometry(Integer isIsometry) {
        this.isIsometry = isIsometry;
    }

    public String getDimBegin() {
        return this.dimBegin;
    }

    public void setDimBegin(String dimBegin) {
        this.dimBegin = dimBegin;
    }

    public String getDimEnd() {
        return this.dimEnd;
    }

    public void setDimEnd(String dimEnd) {
        this.dimEnd = dimEnd;
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

    public Integer getAttrId() {
        return this.attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public Integer getGradeNum() {
        return this.gradeNum;
    }

    public void setGradeNum(Integer gradeNum) {
        this.gradeNum = gradeNum;
    }
}
