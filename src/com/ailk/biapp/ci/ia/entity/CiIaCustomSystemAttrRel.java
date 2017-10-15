package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaCustomSystemAttrRel implements Serializable {
    private Integer ruleId;
    private Integer customAttrId;
    private Integer attrId;
    private Integer calcuVal;
    private String calcuOprator;
    private Integer elementType;

    public CiIaCustomSystemAttrRel() {
    }

    public CiIaCustomSystemAttrRel(Integer customAttrId, Integer attrId, Integer calcuVal, String calcuOprator, Integer elementType) {
        this.customAttrId = customAttrId;
        this.attrId = attrId;
        this.calcuVal = calcuVal;
        this.calcuOprator = calcuOprator;
        this.elementType = elementType;
    }

    public Integer getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getCustomAttrId() {
        return this.customAttrId;
    }

    public void setCustomAttrId(Integer customAttrId) {
        this.customAttrId = customAttrId;
    }

    public Integer getAttrId() {
        return this.attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public Integer getCalcuVal() {
        return this.calcuVal;
    }

    public void setCalcuVal(Integer calcuVal) {
        this.calcuVal = calcuVal;
    }

    public String getCalcuOprator() {
        return this.calcuOprator;
    }

    public void setCalcuOprator(String calcuOprator) {
        this.calcuOprator = calcuOprator;
    }

    public Integer getElementType() {
        return this.elementType;
    }

    public void setElementType(Integer elementType) {
        this.elementType = elementType;
    }
}
