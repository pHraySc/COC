package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiTemplateInfo
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private String templateId;
  private String templateName;
  private String templateDesc;
  private String userId;
  private Date addTime;
  private Integer createType;
  private String labelOptRuleShow;
  private Integer status;
  private String sceneId;
  private Integer isPrivate;
  private Date newModifyTime;
  private Date startDate;
  private Date endDate;
  private String userName;
  private Date modifyStartDate;
  private Date modifyEndDate;
  private String dateType;
  private int isHotList;

  public CiTemplateInfo()
  {
  }

  public CiTemplateInfo(String templateName, String templateDesc, String userId, Date addTime, Integer createType, String labelOptRuleShow, Integer status, String sceneId, Integer isPrivate, Date newModifyTime)
  {
    this.templateName = templateName;
    this.templateDesc = templateDesc;
    this.userId = userId;
    this.addTime = addTime;
    this.createType = createType;
    this.labelOptRuleShow = labelOptRuleShow;
    this.status = status;
    this.sceneId = sceneId;
    this.isPrivate = isPrivate;
    this.newModifyTime = newModifyTime;
  }

  public String getTemplateId()
  {
    return this.templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return this.templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateDesc() {
    return this.templateDesc;
  }

  public void setTemplateDesc(String templateDesc) {
    this.templateDesc = templateDesc;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Date getAddTime() {
    return this.addTime;
  }

  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }

  public Integer getCreateType() {
    return this.createType;
  }

  public void setCreateType(Integer createType) {
    this.createType = createType;
  }

  public String getLabelOptRuleShow() {
    return this.labelOptRuleShow;
  }

  public void setLabelOptRuleShow(String labelOptRuleShow) {
    this.labelOptRuleShow = labelOptRuleShow;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public CiTemplateInfo clone() throws CloneNotSupportedException
  {
    CiTemplateInfo ciTemplateInfo = (CiTemplateInfo)super.clone();
    return ciTemplateInfo;
  }

  public String toString()
  {
    return "CiTemplateInfo [templateId=" + this.templateId + ", templateName=" + this.templateName + ", templateDesc=" + this.templateDesc + ", userId=" + this.userId + ", addTime=" + this.addTime + ", createType=" + this.createType + ", labelOptRuleShow=" + this.labelOptRuleShow + ", status=" + this.status + "]";
  }

  public Integer getIsPrivate()
  {
    return this.isPrivate;
  }

  public void setIsPrivate(Integer isPrivate) {
    this.isPrivate = isPrivate;
  }

  public String getSceneId() {
    return this.sceneId;
  }

  public void setSceneId(String sceneId) {
    this.sceneId = sceneId;
  }

  public Date getNewModifyTime() {
    return this.newModifyTime;
  }

  public void setNewModifyTime(Date newModifyTime) {
    this.newModifyTime = newModifyTime;
  }

  public Date getModifyStartDate() {
    return this.modifyStartDate;
  }

  public void setModifyStartDate(Date modifyStartDate) {
    this.modifyStartDate = modifyStartDate;
  }

  public Date getModifyEndDate() {
    return this.modifyEndDate;
  }

  public void setModifyEndDate(Date modifyEndDate) {
    this.modifyEndDate = modifyEndDate;
  }

  public String getDateType() {
    return this.dateType;
  }

  public void setDateType(String dateType) {
    this.dateType = dateType;
  }

  public int getIsHotList() {
    return this.isHotList;
  }

  public void setIsHotList(int isHotList) {
    this.isHotList = isHotList;
  }
}