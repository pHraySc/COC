package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.List;

public class CiLabelRule
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private String ruleId;
  private String customId;
  private String calcuElement;
  private Double minVal;
  private Double maxVal;
  private Long sortNum;
  private Integer customType;
  private Integer elementType;
  private Integer labelFlag;
  private String parentId;
  private String labelName;
  private String effectDate;
  private Integer updateCycle;
  private boolean baseParam;
  private String dataDate;
  private String attrVal;
  private String startTime;
  private String endTime;
  private String contiueMinVal;
  private String contiueMaxVal;
  private String leftZoneSign;
  private String rightZoneSign;
  private String exactValue;
  private String darkValue;
  private String tableName;
  private Integer isNeedOffset;
  private Integer labelTypeId;
  private String customGroupId;
  private String customOrLabelName;
  private Integer isEditCustomFlag;
  private String createBrackets;
  private String isNull;
  private String queryWay;
  private String attrName;
  private Integer customCreateTypeId;
  private Integer labelOrCustomSort;
  private String unit;
  private String columnCnName;
  private Integer customStatus;
  private Integer isMustColumn = Integer.valueOf(0);
  private Integer isHasList;
  private List<CiLabelRule> childCiLabelRuleList;

  public CiLabelRule()
  {
  }

  public boolean getBaseParam()
  {
    return this.baseParam;
  }

  public void setBaseParam(boolean baseParam) {
    this.baseParam = baseParam;
  }

  public String getRuleId() {
    return this.ruleId;
  }

  public void setRuleId(String ruleId) {
    this.ruleId = ruleId;
  }

  public String getCustomId() {
    return this.customId;
  }

  public void setCustomId(String customId) {
    this.customId = customId;
  }

  public String getCalcuElement() {
    return this.calcuElement;
  }

  public void setCalcuElement(String calcuElement) {
    this.calcuElement = calcuElement;
  }

  public Double getMinVal() {
    return this.minVal;
  }

  public void setMinVal(Double minVal) {
    this.minVal = minVal;
  }

  public Double getMaxVal() {
    return this.maxVal;
  }

  public void setMaxVal(Double maxVal) {
    this.maxVal = maxVal;
  }

  public Long getSortNum() {
    return this.sortNum;
  }

  public void setSortNum(Long sortNum) {
    this.sortNum = sortNum;
  }

  public Integer getCustomType() {
    return this.customType;
  }

  public void setCustomType(Integer customType) {
    this.customType = customType;
  }

  public Integer getElementType() {
    return this.elementType;
  }

  public void setElementType(Integer elementType) {
    this.elementType = elementType;
  }

  public Integer getLabelFlag() {
    return this.labelFlag;
  }

  public void setLabelFlag(Integer labelFlag) {
    this.labelFlag = labelFlag;
  }

  public String getLabelName() {
    return this.labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public String getEffectDate() {
    return this.effectDate;
  }

  public void setEffectDate(String effectDate) {
    this.effectDate = effectDate;
  }

  public Integer getUpdateCycle() {
    return this.updateCycle;
  }

  public void setUpdateCycle(Integer updateCycle) {
    this.updateCycle = updateCycle;
  }

  public String getAttrVal() {
    return this.attrVal;
  }

  public void setAttrVal(String attrVal) {
    this.attrVal = attrVal;
  }

  public Integer getLabelTypeId() {
    return this.labelTypeId;
  }

  public void setLabelTypeId(Integer labelTypeId) {
    this.labelTypeId = labelTypeId;
  }

  public CiLabelRule clone() throws CloneNotSupportedException
  {
    CiLabelRule ciLabelRule = (CiLabelRule)super.clone();
    return ciLabelRule;
  }

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.calcuElement == null) ? 0 : this.calcuElement.hashCode());
    result = 31 * result + ((this.customId == null) ? 0 : this.customId.hashCode());
    result = 31 * result + ((this.customType == null) ? 0 : this.customType.hashCode());
    result = 31 * result + ((this.elementType == null) ? 0 : this.elementType.hashCode());
    result = 31 * result + ((this.labelFlag == null) ? 0 : this.labelFlag.hashCode());
    result = 31 * result + ((this.maxVal == null) ? 0 : this.maxVal.hashCode());
    result = 31 * result + ((this.minVal == null) ? 0 : this.minVal.hashCode());
    result = 31 * result + ((this.ruleId == null) ? 0 : this.ruleId.hashCode());
    result = 31 * result + ((this.sortNum == null) ? 0 : this.sortNum.hashCode());
    return result;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (super.getClass() != obj.getClass())
      return false;
    CiLabelRule other = (CiLabelRule)obj;
    if (this.calcuElement == null) {
      if (other.calcuElement == null)
      return false; }
    if (!(this.calcuElement.equals(other.calcuElement)))
      return false;
    if (this.customId == null) {
      label63: if (other.customId == null) 
      return false; }
    if (!(this.customId.equals(other.customId)))
      return false;
    if (this.customType == null) {
       if (other.customType == null) 
      return false; }
    if (!(this.customType.equals(other.customType)))
      return false;
    if (this.elementType == null) {
       if (other.elementType == null) 
      return false; }
    if (!(this.elementType.equals(other.elementType)))
      return false;
    if (this.labelFlag == null) {
       if (other.labelFlag == null) 
      return false; }
    if (!(this.labelFlag.equals(other.labelFlag)))
      return false;
    if (this.maxVal == null) {
       if (other.maxVal == null) 
      return false; }
    if (!(this.maxVal.equals(other.maxVal)))
      return false;
    if (this.minVal == null) {
       if (other.minVal == null)
      return false; }
    if (!(this.minVal.equals(other.minVal)))
      return false;
    if (this.ruleId == null) {
      if (other.ruleId == null) 
      return false; 
    }
    if (!(this.ruleId.equals(other.ruleId)))
      return false;
    if (this.sortNum == null) {
      if (other.sortNum == null)  
      return false;
    }
    return (this.sortNum.equals(other.sortNum));
  }

  public CiLabelRule(String ruleId, String customId, String calcuElement, Double minVal, Double maxVal, Long sortNum, Integer customType, Integer elementType, Integer labelFlag, String attrVal, String startTime, String endTime, String contiueMinVal, String contiueMaxVal, String leftZoneSign, String rightZoneSign, String exactValue, String darkValue)
  {
    this.ruleId = ruleId;
    this.customId = customId;
    this.calcuElement = calcuElement;
    this.minVal = minVal;
    this.maxVal = maxVal;
    this.sortNum = sortNum;
    this.customType = customType;
    this.elementType = elementType;
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

  public String toString()
  {
    return "CiLabelRule [ruleId=" + this.ruleId + ", customId=" + this.customId + ", calcuElement=" + this.calcuElement + ", minVal=" + this.minVal + ", maxVal=" + this.maxVal + ", sortNum=" + this.sortNum + ", customType=" + this.customType + ", elementType=" + this.elementType + ", labelFlag=" + this.labelFlag + ",attrVal =" + this.attrVal + ",startTime=" + this.startTime + ", endTime=" + this.endTime + ",contiueMinVal=" + this.contiueMinVal + ",contiueMaxVal=" + this.contiueMaxVal + ",leftZoneSign=" + this.leftZoneSign + ",rightZoneSign=" + this.rightZoneSign + ",exactValue=" + this.exactValue + ", darkValue=" + this.darkValue + "]";
  }

  public String getStartTime()
  {
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

  public String getLeftZoneSign()
  {
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

  public String getCustomGroupId() {
    return this.customGroupId;
  }

  public void setCustomGroupId(String customGroupId) {
    this.customGroupId = customGroupId;
  }

  public String getCustomOrLabelName() {
    return this.customOrLabelName;
  }

  public void setCustomOrLabelName(String customOrLabelName) {
    this.customOrLabelName = customOrLabelName;
  }

  public Integer getIsEditCustomFlag() {
    return this.isEditCustomFlag;
  }

  public void setIsEditCustomFlag(Integer isEditCustomFlag) {
    this.isEditCustomFlag = isEditCustomFlag;
  }

  public String getCreateBrackets() {
    return this.createBrackets;
  }

  public void setCreateBrackets(String createBrackets) {
    this.createBrackets = createBrackets;
  }

  public String getIsNull() {
    return this.isNull;
  }

  public void setIsNull(String isNull) {
    this.isNull = isNull;
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

  public String getQueryWay() {
    return this.queryWay;
  }

  public void setQueryWay(String queryWay) {
    this.queryWay = queryWay;
  }

  public String getAttrName() {
    return this.attrName;
  }

  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

  public Integer getCustomCreateTypeId() {
    return this.customCreateTypeId;
  }

  public void setCustomCreateTypeId(Integer customCreateTypeId) {
    this.customCreateTypeId = customCreateTypeId;
  }

  public Integer getLabelOrCustomSort() {
    return this.labelOrCustomSort;
  }

  public void setLabelOrCustomSort(Integer labelOrCustomSort) {
    this.labelOrCustomSort = labelOrCustomSort;
  }

  public String getParentId() {
    return this.parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public List<CiLabelRule> getChildCiLabelRuleList() {
    return this.childCiLabelRuleList;
  }

  public void setChildCiLabelRuleList(List<CiLabelRule> childCiLabelRuleList) {
    this.childCiLabelRuleList = childCiLabelRuleList;
  }

  public String getUnit() {
    return this.unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getColumnCnName() {
    return this.columnCnName;
  }

  public void setColumnCnName(String columnCnName) {
    this.columnCnName = columnCnName;
  }

  public Integer getCustomStatus() {
    return this.customStatus;
  }

  public void setCustomStatus(Integer customStatus) {
    this.customStatus = customStatus;
  }

  public String getDataDate() {
    return this.dataDate;
  }

  public void setDataDate(String dataDate) {
    this.dataDate = dataDate;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public Integer getIsNeedOffset() {
    return this.isNeedOffset;
  }

  public void setIsNeedOffset(Integer isNeedOffset) {
    this.isNeedOffset = isNeedOffset;
  }

  public Integer getIsMustColumn()
  {
    return this.isMustColumn;
  }

  public void setIsMustColumn(Integer isMustColumn) {
    this.isMustColumn = isMustColumn;
  }

  public Integer getIsHasList() {
    return this.isHasList;
  }

  public void setIsHasList(Integer isHasList) {
    this.isHasList = isHasList;
  }
}