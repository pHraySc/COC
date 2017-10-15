package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiCustomListInfo
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private String listTableName;
  private String dataDate;
  private String customGroupId;
  private Long customNum;
  private Long ringNum;
  private Integer dataStatus;
  private Date dataTime;
  private String excpInfo;
  private Integer fileCreateStatus;
  private Integer fileApproveStatus;
  private String monthLabelDate;
  private String dayLabelDate;
  private Long duplicateNum;
  private Integer listMaxNum;
  private String dataTimeStr;
  private String createUserId;
  private String customGroupName;
  private String createUserName;
  private Integer updateCycle;
  private Date startDate;
  private Date endDate;
  private Integer isStrengthen;

  public Integer getUpdateCycle()
  {
    return this.updateCycle;
  }

  public void setUpdateCycle(Integer updateCycle) {
    this.updateCycle = updateCycle;
  }

  public Integer getFileApproveStatus() {
    return this.fileApproveStatus;
  }

  public void setFileApproveStatus(Integer fileApproveStatus) {
    this.fileApproveStatus = fileApproveStatus; }

  public String getCustomGroupName() {
    return this.customGroupName;
  }

  public Integer getFileCreateStatus() {
    return this.fileCreateStatus;
  }

  public void setFileCreateStatus(Integer fileCreateStatus) {
    this.fileCreateStatus = fileCreateStatus;
  }

  public void setCustomGroupName(String customGroupName) {
    this.customGroupName = customGroupName;
  }

  public String getCustomGroupId()
  {
    return this.customGroupId;
  }

  public void setCustomGroupId(String customGroupId) {
    this.customGroupId = customGroupId;
  }

  public Long getCustomNum() {
    return this.customNum;
  }

  public void setCustomNum(Long customNum) {
    this.customNum = customNum;
  }

  public Long getRingNum() {
    return this.ringNum;
  }

  public void setRingNum(Long ringNum) {
    this.ringNum = ringNum;
  }

  public Integer getDataStatus() {
    return this.dataStatus;
  }

  public void setDataStatus(Integer dataStatus) {
    this.dataStatus = dataStatus;
  }

  public Date getDataTime() {
    return this.dataTime;
  }

  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
  }

  public String getExcpInfo() {
    return this.excpInfo;
  }

  public void setExcpInfo(String excpInfo) {
    this.excpInfo = excpInfo;
  }

  public String getListTableName() {
    return this.listTableName;
  }

  public void setListTableName(String listTableName) {
    this.listTableName = listTableName;
  }

  public String getDataDate() {
    return this.dataDate;
  }

  public void setDataDate(String dataDate) {
    this.dataDate = dataDate;
  }

  public String getMonthLabelDate() {
    return this.monthLabelDate;
  }

  public void setMonthLabelDate(String monthLabelDate) {
    this.monthLabelDate = monthLabelDate;
  }

  public String getDayLabelDate() {
    return this.dayLabelDate;
  }

  public void setDayLabelDate(String dayLabelDate) {
    this.dayLabelDate = dayLabelDate;
  }

  public CiCustomListInfo clone() throws CloneNotSupportedException {
    CiCustomListInfo ciCustomListInfo = (CiCustomListInfo)super.clone();
    return ciCustomListInfo;
  }

  public String toString()
  {
    return "CiCustomListInfo [listTableName=" + this.listTableName + ", dataDate=" + this.dataDate + ", customGroupId=" + this.customGroupId + ", customNum=" + this.customNum + ", ringNum=" + this.ringNum + ", dataStatus=" + this.dataStatus + ", dataTime=" + this.dataTime + ", excpInfo=...]";
  }

  public String getDataTimeStr()
  {
    return this.dataTimeStr;
  }

  public void setDataTimeStr(String dataTimeStr) {
    this.dataTimeStr = dataTimeStr;
  }

  public String getCreateUserId() {
    return this.createUserId;
  }

  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  public String getCreateUserName() {
    return this.createUserName;
  }

  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }

  public Long getDuplicateNum() {
    return this.duplicateNum;
  }

  public void setDuplicateNum(Long duplicateNum) {
    this.duplicateNum = duplicateNum;
  }

  public Integer getListMaxNum() {
    return this.listMaxNum;
  }

  public void setListMaxNum(Integer listMaxNum) {
    this.listMaxNum = listMaxNum;
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

  public Integer getIsStrengthen()
  {
    return this.isStrengthen;
  }

  public void setIsStrengthen(Integer isStrengthen)
  {
    this.isStrengthen = isStrengthen;
  }
}