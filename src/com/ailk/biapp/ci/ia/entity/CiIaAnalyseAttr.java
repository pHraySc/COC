package com.ailk.biapp.ci.ia.entity;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import java.io.Serializable;

public class CiIaAnalyseAttr implements Serializable {
    private static final long serialVersionUID = 9003616611989411982L;
    private Integer attrId;
    private String attrName;
    private String guidelineTitle;
    private Integer dispOrder;
    private String guidelineUnit;
    private String guidelineMask;
    private Integer attrSource;
    private Integer attrType;
    private Integer status;
    private Integer isCalculate;
    private String customAttrRule;
    private CiLabelInfo ciLabelInfo;
    private CiMdaSysTableColumn ciMdaSysTableColumn;

    public CiIaAnalyseAttr() {
    }

    public CiIaAnalyseAttr(String attrName, String guidelineTitle, Integer dispOrder, String guidelineUnit, String guidelineMask, Integer attrSource, Integer attrType, Integer status, Integer isCalculate, String customAttrRule) {
        this.attrName = attrName;
        this.guidelineTitle = guidelineTitle;
        this.dispOrder = dispOrder;
        this.guidelineUnit = guidelineUnit;
        this.guidelineMask = guidelineMask;
        this.attrSource = attrSource;
        this.attrType = attrType;
        this.status = status;
        this.isCalculate = isCalculate;
        this.customAttrRule = customAttrRule;
    }

    public Integer getAttrId() {
        return this.attrId;
    }

    public Integer getIsCalculate() {
        return this.isCalculate;
    }

    public void setIsCalculate(Integer isCalculate) {
        this.isCalculate = isCalculate;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return this.attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getGuidelineTitle() {
        return this.guidelineTitle;
    }

    public void setGuidelineTitle(String guidelineTitle) {
        this.guidelineTitle = guidelineTitle;
    }

    public Integer getDispOrder() {
        return this.dispOrder;
    }

    public void setDispOrder(Integer dispOrder) {
        this.dispOrder = dispOrder;
    }

    public String getGuidelineUnit() {
        return this.guidelineUnit;
    }

    public void setGuidelineUnit(String guidelineUnit) {
        this.guidelineUnit = guidelineUnit;
    }

    public String getGuidelineMask() {
        return this.guidelineMask;
    }

    public void setGuidelineMask(String guidelineMask) {
        this.guidelineMask = guidelineMask;
    }

    public Integer getAttrType() {
        return this.attrType;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustomAttrRule() {
        return this.customAttrRule;
    }

    public void setCustomAttrRule(String customAttrRule) {
        this.customAttrRule = customAttrRule;
    }

    public Integer getAttrSource() {
        return this.attrSource;
    }

    public void setAttrSource(Integer attrSource) {
        this.attrSource = attrSource;
    }

    public CiMdaSysTableColumn getCiMdaSysTableColumn() {
        return this.ciMdaSysTableColumn;
    }

    public void setCiMdaSysTableColumn(CiMdaSysTableColumn ciMdaSysTableColumn) {
        this.ciMdaSysTableColumn = ciMdaSysTableColumn;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }
}
