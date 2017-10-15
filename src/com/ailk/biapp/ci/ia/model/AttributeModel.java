package com.ailk.biapp.ci.ia.model;

import com.ailk.biapp.ci.ia.entity.CiIaDimIndexGroup;
import com.ailk.biapp.ci.ia.model.FilterModel;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

public class AttributeModel {
    private String attrId;
    private String attrName;
    private String originalAttrId;
    private String originalAttrName;
    private Integer attrSource;
    private String guidelineTitle;
    private String guidelineUnit;
    private String guidelineMask;
    private Integer attrType;
    private Integer iscalculate;
    private Integer aggregationId;
    private String customSystemAttr;
    private String customSystemAttrText;
    private List<CiIaDimIndexGroup> group;
    private List<FilterModel> include;
    private List<FilterModel> exclude;
    private Integer labelTypeId;

    public AttributeModel() {
    }

    public String toString() {
        return "AttibuteModel [ attrId=" + this.attrId + ", attrName=" + this.attrName + ", guidelineTitle=" + this.guidelineTitle + ", guidelineUnit=" + this.guidelineUnit + ", guidelineMask=" + this.guidelineMask + ", attrType=" + this.attrType + ", iscalculate=" + this.iscalculate + ", aggregationId=" + this.aggregationId + ", customSystemAttr=" + this.customSystemAttr + ", group=" + this.group + ", include=" + this.include + ", exclude=" + this.exclude + "]";
    }

    public String getAttrId() {
        return this.attrId;
    }

    public String getOriginalAttrId() {
        return this.originalAttrId;
    }

    public void setOriginalAttrId(String originalAttrId) {
        this.originalAttrId = originalAttrId;
    }

    public String getOriginalAttrName() {
        return this.originalAttrName;
    }

    public void setOriginalAttrName(String originalAttrName) {
        this.originalAttrName = originalAttrName;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return this.attrName;
    }

    public Integer getAttrSource() {
        return this.attrSource;
    }

    public void setAttrSource(Integer attrSource) {
        this.attrSource = attrSource;
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

    public Integer getIscalculate() {
        return this.iscalculate;
    }

    public void setIscalculate(Integer iscalculate) {
        this.iscalculate = iscalculate;
    }

    public Integer getAggregationId() {
        return this.aggregationId;
    }

    public void setAggregationId(Integer aggregationId) {
        this.aggregationId = aggregationId;
    }

    public String getCustomSystemAttr() {
        return this.customSystemAttr;
    }

    public void setCustomSystemAttr(String customSystemAttr) {
        this.customSystemAttr = customSystemAttr;
    }

    public String getCustomSystemAttrText() {
        return this.customSystemAttrText;
    }

    public void setCustomSystemAttrText(String customSystemAttrText) {
        this.customSystemAttrText = customSystemAttrText;
    }

    public List<CiIaDimIndexGroup> getGroup() {
        return this.group;
    }

    public void setGroup(List<CiIaDimIndexGroup> group) {
        this.group = group;
    }

    public List<FilterModel> getInclude() {
        return this.include;
    }

    public void setInclude(List<FilterModel> include) {
        this.include = include;
    }

    public List<FilterModel> getExclude() {
        return this.exclude;
    }

    public void setExclude(List<FilterModel> exclude) {
        this.exclude = exclude;
    }

    public Integer getLabelTypeId() {
        return this.labelTypeId;
    }

    public void setLabelTypeId(Integer labelTypeId) {
        this.labelTypeId = labelTypeId;
    }

    public static void main(String[] args) {
        AttributeModel model = new AttributeModel();
        model.setAttrId("1");
        model.setAttrName("品牌");
        model.setGuidelineTitle("总收入");
        model.setGuidelineUnit("元");
        model.setGuidelineMask("");
        model.setAttrType(Integer.valueOf(1));
        model.setIscalculate(Integer.valueOf(0));
        model.setAggregationId(Integer.valueOf(1));
        model.setCustomSystemAttr("A*0.8+100");
        ArrayList group = new ArrayList();
        CiIaDimIndexGroup group1 = new CiIaDimIndexGroup();
        group1.setAttrId(Integer.valueOf(1));
        group1.setGroupName("分组1");
        group1.setIsIsometry(Integer.valueOf(0));
        group1.setDimBegin("10");
        group1.setDimEnd("50");
        group1.setLeftZoneSign("[");
        group1.setRightZoneSign(")");
        group.add(group1);
        model.setGroup(group);
        JSONObject json = JSONObject.fromObject(model);
        System.out.println(json.toString());
    }
}
