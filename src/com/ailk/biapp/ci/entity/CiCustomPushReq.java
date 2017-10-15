package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CiCustomPushReq
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private String reqId;
  private String userId;
  private String listTableName;
  private String sysId;
  private Integer status;
  private Date reqTime;
  private Date startTime;
  private Date endTime;
  private String exeInfo;
  private List<String> sysIds;

  public String getReqId()
  {
    return this.reqId;
  }

  public void setReqId(String reqId) {
    this.reqId = reqId;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getListTableName() {
    return this.listTableName;
  }

  public void setListTableName(String listTableName) {
    this.listTableName = listTableName;
  }

  public String getSysId() {
    return this.sysId;
  }

  public void setSysId(String sysId) {
    this.sysId = sysId;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getReqTime() {
    return this.reqTime;
  }

  public void setReqTime(Date reqTime) {
    this.reqTime = reqTime;
  }

  public Date getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return this.endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getExeInfo() {
    return this.exeInfo;
  }

  public void setExeInfo(String exeInfo) {
    this.exeInfo = exeInfo;
  }

  public List<String> getSysIds() {
    return this.sysIds;
  }

  public void setSysIds(List<String> sysIds) {
    this.sysIds = sysIds;
  }

  public String toString() {
    return "CiCustomPushReq [reqId=" + this.reqId + ", userId=" + this.userId + ", listTableName=" + this.listTableName + ", sysId=" + this.sysId + ", status=" + this.status + ", reqTime=" + this.reqTime + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", exeInfo=" + this.exeInfo + "]";
  }

  public CiCustomPushReq clone()
    throws CloneNotSupportedException
  {
    return ((CiCustomPushReq)super.clone());
  }
}