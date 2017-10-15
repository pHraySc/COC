package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CiLabelExtInfo
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private Integer labelId;
  private Integer labelLevel;
  private String ctrlTypeId;
  private String techCaliber;
  private Double minVal;
  private Double maxVal;
  private Integer isStatUserNum;
  private Integer isLeaf;
  private Integer customNum;
  private String owner;
  private String attrVal;
  private String countRulesCode;
  private CiMdaSysTableColumn ciMdaSysTableColumn;
  private Set<CiLabelVerticalColumnRel> ciLabelVerticalColumnRels = new HashSet(0);
  private Set<CiLabelSceneRel> ciLabelSceneRels = new HashSet(0);

  public CiLabelExtInfo()
  {
  }

  public Integer getLabelId()
  {
    return this.labelId;
  }

  public CiLabelExtInfo clone() throws CloneNotSupportedException
  {
    CiLabelExtInfo ciLabelExtInfo = (CiLabelExtInfo)super.clone();
    if (null != this.ciMdaSysTableColumn)
      ciLabelExtInfo.setCiMdaSysTableColumn(this.ciMdaSysTableColumn.clone());

    return ciLabelExtInfo;
  }

  public CiLabelExtInfo(Integer labelId, Integer labelLevel, String ctrlTypeId, String techCaliber, Double minVal, Double maxVal, Integer isStatUserNum, Integer isLeaf, Integer customNum, String owner, String attrVal, Set<CiLabelVerticalColumnRel> ciLabelVerticalColumnRels)
  {
    this.labelId = labelId;
    this.labelLevel = labelLevel;
    this.ctrlTypeId = ctrlTypeId;
    this.techCaliber = techCaliber;
    this.minVal = minVal;
    this.maxVal = maxVal;
    this.isStatUserNum = isStatUserNum;
    this.isLeaf = isLeaf;
    this.customNum = customNum;
    this.owner = owner;
    this.attrVal = attrVal;
    this.ciLabelVerticalColumnRels = ciLabelVerticalColumnRels;
  }

  public String getAttrVal() {
    return this.attrVal;
  }

  public void setAttrVal(String attrVal) {
    this.attrVal = attrVal;
  }

  public void setLabelId(Integer labelId) {
    this.labelId = labelId;
  }

  public Integer getLabelLevel() {
    return this.labelLevel;
  }

  public void setLabelLevel(Integer labelLevel) {
    this.labelLevel = labelLevel;
  }

  public String getCtrlTypeId() {
    return this.ctrlTypeId;
  }

  public void setCtrlTypeId(String ctrlTypeId) {
    this.ctrlTypeId = ctrlTypeId;
  }

  public String getTechCaliber() {
    return this.techCaliber;
  }

  public void setTechCaliber(String techCaliber) {
    this.techCaliber = techCaliber;
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

  public Integer getIsStatUserNum() {
    return this.isStatUserNum;
  }

  public void setIsStatUserNum(Integer isStatUserNum) {
    this.isStatUserNum = isStatUserNum;
  }

  public Integer getIsLeaf() {
    return this.isLeaf;
  }

  public void setIsLeaf(Integer isLeaf) {
    this.isLeaf = isLeaf;
  }

  public Integer getCustomNum() {
    return this.customNum;
  }

  public void setCustomNum(Integer customNum) {
    this.customNum = customNum;
  }

  public String getOwner() {
    return this.owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public CiMdaSysTableColumn getCiMdaSysTableColumn() {
    return this.ciMdaSysTableColumn;
  }

  public void setCiMdaSysTableColumn(CiMdaSysTableColumn ciMdaSysTableColumn) {
    this.ciMdaSysTableColumn = ciMdaSysTableColumn;
  }

  public Set<CiLabelVerticalColumnRel> getCiLabelVerticalColumnRels() {
    return this.ciLabelVerticalColumnRels;
  }

  public void setCiLabelVerticalColumnRels(Set<CiLabelVerticalColumnRel> ciLabelVerticalColumnRels) {
    this.ciLabelVerticalColumnRels = ciLabelVerticalColumnRels;
  }

  public Set<CiLabelSceneRel> getCiLabelSceneRels() {
    return this.ciLabelSceneRels;
  }

  public void setCiLabelSceneRels(Set<CiLabelSceneRel> ciLabelSceneRels) {
    this.ciLabelSceneRels = ciLabelSceneRels;
  }

  public String getCountRulesCode() {
    return this.countRulesCode;
  }

  public void setCountRulesCode(String countRulesCode) {
    this.countRulesCode = countRulesCode;
  }

  public String toString()
  {
    return "CiLabelExtInfo [labelId=" + this.labelId + ", labelLevel=" + this.labelLevel + ", ctrlTypeId=" + this.ctrlTypeId + ", techCaliber=" + this.techCaliber + ", minVal=" + this.minVal + ", maxVal=" + this.maxVal + ", isStatUserNum=" + this.isStatUserNum + ", isLeaf=" + this.isLeaf + ", customNum=" + this.customNum + ", owner=" + this.owner + ", ciMdaSysTableColumn=" + this.ciMdaSysTableColumn + ", attrVal=" + this.attrVal + "]";
  }
}