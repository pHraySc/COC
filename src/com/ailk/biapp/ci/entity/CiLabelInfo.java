package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CiLabelInfo
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private Integer labelId;
  private String labelName;
  private Integer updateCycle;
  private Integer parentId;
  private Date effecTime;
  private Date failTime;
  private String createUserId;
  private Date createTime;
  private String createDesc;
  private Integer dataStatusId;
  private Integer labelTypeId;
  private String publishUserId;
  private Date publishTime;
  private String publishDesc;
  private String dataSource;
  private String deptId;
  private String busiLegend;
  private String applySuggest;
  private Integer sortNum;
  private String sortType;
  private Integer labelNum;
  private CiLabelExtInfo ciLabelExtInfo;
  private String busiCaliber;
  private Integer isSysRecom;
  private Integer labelPriority;
  private String dataDate;
  private String labelTypeName;
  private String dataStatusName;
  private String approveStatusName;
  private String lastApproveUserId;
  private Date lastApproveTime;
  private String createUserName;
  private String lastApproveUserName;
  private Integer labelLevel;
  private String isStatUserNum;
  private String labelLevelDesc;
  private String labelIdLevelDesc;
  private String currApproveStatusId;
  private String approveRoleType;
  private String effectDate;
  private Date startDate;
  private Date endDate;
  private String effecTimeStr;
  private String failTimeStr;
  private Long customerNum;
  private String createTimeStr;
  private String newDataDate;
  private String sceneId;
  private String labelSceneNames;
  private String isAttention = "false";
  private String columnCnName;
  private String columnId;
  private String labelOrCustomId;
  private String labelOrCustomName;
  private String attrVal;
  private String dataDateStr;
  private Double avgScore;
  private Integer useTimes;
  private List<CiLabelSceneRel> sceneList;
  private String labelCategories;

  public CiLabelInfo clone()
    throws CloneNotSupportedException
  {
    CiLabelInfo ciLabelInfo = (CiLabelInfo)super.clone();
    if (null != this.ciLabelExtInfo)
      ciLabelInfo.setCiLabelExtInfo(this.ciLabelExtInfo.clone());

    return ciLabelInfo;
  }

  public CiLabelInfo()
  {
  }

  public CiLabelInfo(String labelName, Integer updateCycle, Integer parentId, Date effecTime, Date failTime, String createUserId, Date createTime, String createDesc, Integer dataStatusId, Integer labelTypeId, String publishUserId, Date publishTime, String publishDesc, String busiCaliber, String dataSource, String deptId)
  {
    this.labelName = labelName;
    this.updateCycle = updateCycle;
    this.parentId = parentId;
    this.effecTime = effecTime;
    this.failTime = failTime;
    this.createUserId = createUserId;
    this.createTime = createTime;
    this.createDesc = createDesc;
    this.dataStatusId = dataStatusId;
    this.labelTypeId = labelTypeId;
    this.publishUserId = publishUserId;
    this.publishTime = publishTime;
    this.publishDesc = publishDesc;
    this.busiCaliber = busiCaliber;
    this.dataSource = dataSource;
    this.deptId = deptId;
  }

  public Integer getLabelId()
  {
    return this.labelId;
  }

  public String getDataDate() {
    return this.dataDate;
  }

  public void setDataDate(String dataDate) {
    this.dataDate = dataDate;
  }

  public void setLabelId(Integer labelId) {
    this.labelId = labelId;
  }

  public String getLabelName() {
    return this.labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public Integer getUpdateCycle() {
    return this.updateCycle;
  }

  public void setUpdateCycle(Integer updateCycle) {
    this.updateCycle = updateCycle;
  }

  public Integer getParentId() {
    return this.parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public String getCreateUserId() {
    return this.createUserId;
  }

  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  public String getCreateDesc() {
    return this.createDesc;
  }

  public void setCreateDesc(String createDesc) {
    this.createDesc = createDesc;
  }

  public Integer getDataStatusId() {
    return this.dataStatusId;
  }

  public void setDataStatusId(Integer dataStatusId) {
    this.dataStatusId = dataStatusId;
  }

  public Integer getLabelTypeId() {
    return this.labelTypeId;
  }

  public void setLabelTypeId(Integer labelTypeId) {
    this.labelTypeId = labelTypeId;
  }

  public String getPublishUserId() {
    return this.publishUserId;
  }

  public void setPublishUserId(String publishUserId) {
    this.publishUserId = publishUserId;
  }

  public String getPublishDesc() {
    return this.publishDesc;
  }

  public void setPublishDesc(String publishDesc) {
    this.publishDesc = publishDesc;
  }

  public String getBusiCaliber() {
    return this.busiCaliber;
  }

  public void setBusiCaliber(String busiCaliber) {
    this.busiCaliber = busiCaliber;
  }

  public String getDataSource() {
    return this.dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public String getDeptId() {
    return this.deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }

  public CiLabelExtInfo getCiLabelExtInfo() {
    return this.ciLabelExtInfo;
  }

  public void setCiLabelExtInfo(CiLabelExtInfo ciLabelExtInfo) {
    this.ciLabelExtInfo = ciLabelExtInfo;
  }

  public Date getEffecTime() {
    return this.effecTime;
  }

  public void setEffecTime(Date effecTime) {
    this.effecTime = effecTime;
  }

  public Date getFailTime() {
    return this.failTime;
  }

  public void setFailTime(Date failTime) {
    this.failTime = failTime;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getPublishTime() {
    return this.publishTime;
  }

  public void setPublishTime(Date publishTime) {
    this.publishTime = publishTime;
  }

  public String getLabelTypeName() {
    return this.labelTypeName;
  }

  public void setLabelTypeName(String labelTypeName) {
    this.labelTypeName = labelTypeName;
  }

  public String getDataStatusName() {
    return this.dataStatusName;
  }

  public void setDataStatusName(String dataStatusName) {
    this.dataStatusName = dataStatusName;
  }

  public String getApproveStatusName() {
    return this.approveStatusName;
  }

  public void setApproveStatusName(String approveStatusName) {
    this.approveStatusName = approveStatusName;
  }

  public String getCurrApproveStatusId() {
    return this.currApproveStatusId;
  }

  public void setCurrApproveStatusId(String currApproveStatusId) {
    this.currApproveStatusId = currApproveStatusId;
  }

  public String getEffectDate() {
    return this.effectDate;
  }

  public void setEffectDate(String effectDate) {
    this.effectDate = effectDate;
  }

  public String getBusiLegend() {
    return this.busiLegend;
  }

  public void setBusiLegend(String busiLegend) {
    this.busiLegend = busiLegend;
  }

  public String getApplySuggest() {
    return this.applySuggest;
  }

  public void setApplySuggest(String applySuggest) {
    this.applySuggest = applySuggest;
  }

  public String getLastApproveUserId() {
    return this.lastApproveUserId;
  }

  public void setLastApproveUserId(String lastApproveUserId) {
    this.lastApproveUserId = lastApproveUserId;
  }

  public Date getLastApproveTime() {
    return this.lastApproveTime;
  }

  public void setLastApproveTime(Date lastApproveTime) {
    this.lastApproveTime = lastApproveTime;
  }

  public String getCreateUserName() {
    return this.createUserName;
  }

  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }

  public String getLastApproveUserName() {
    return this.lastApproveUserName;
  }

  public void setLastApproveUserName(String lastApproveUserName) {
    this.lastApproveUserName = lastApproveUserName;
  }

  public Integer getSortNum() {
    return this.sortNum;
  }

  public void setSortNum(Integer sortNum) {
    this.sortNum = sortNum;
  }

  public Integer getLabelLevel() {
    return this.labelLevel;
  }

  public void setLabelLevel(Integer labelLevel) {
    this.labelLevel = labelLevel;
  }

  public String getIsStatUserNum() {
    return this.isStatUserNum;
  }

  public void setIsStatUserNum(String isStatUserNum) {
    this.isStatUserNum = isStatUserNum;
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

  public Integer getLabelNum() {
    return this.labelNum;
  }

  public void setLabelNum(Integer labelNum) {
    this.labelNum = labelNum;
  }

  public String getEffecTimeStr() {
    return this.effecTimeStr;
  }

  public void setEffecTimeStr(String effecTimeStr) {
    this.effecTimeStr = effecTimeStr;
  }

  public String getFailTimeStr() {
    return this.failTimeStr;
  }

  public void setFailTimeStr(String failTimeStr) {
    this.failTimeStr = failTimeStr;
  }

  public Long getCustomerNum() {
    return this.customerNum;
  }

  public void setCustomerNum(Long customerNum) {
    this.customerNum = customerNum;
  }

  public String getCreateTimeStr() {
    return this.createTimeStr;
  }

  public void setCreateTimeStr(String createTimeStr) {
    this.createTimeStr = createTimeStr;
  }

  public String getNewDataDate() {
    return this.newDataDate;
  }

  public void setNewDataDate(String newDataDate) {
    this.newDataDate = newDataDate;
  }

  public String getSceneId()
  {
    return this.sceneId;
  }

  public void setSceneId(String sceneId) {
    this.sceneId = sceneId;
  }

  public String getLabelSceneNames() {
    return this.labelSceneNames;
  }

  public void setLabelSceneNames(String labelSceneNames) {
    this.labelSceneNames = labelSceneNames;
  }

  public Integer getIsSysRecom() {
    return this.isSysRecom;
  }

  public void setIsSysRecom(Integer isSysRecom) {
    this.isSysRecom = isSysRecom;
  }

  public String getIsAttention() {
    return this.isAttention;
  }

  public void setIsAttention(String isAttention) {
    this.isAttention = isAttention;
  }

  public String toString()
  {
    return "CiLabelInfo [applySuggest=" + this.applySuggest + ", approveStatusName=" + this.approveStatusName + ", busiCaliber=" + this.busiCaliber + ", busiLegend=" + this.busiLegend + ", ciLabelExtInfo=" + this.ciLabelExtInfo + ", createDesc=" + this.createDesc + ", createTime=" + this.createTime + ", createUserId=" + this.createUserId + ", currApproveStatusId=" + this.currApproveStatusId + ", dataDate=" + this.dataDate + ", dataSource=" + this.dataSource + ", dataStatusId=" + this.dataStatusId + ", dataStatusName=" + this.dataStatusName + ", deptId=" + this.deptId + ", effecTime=" + this.effecTime + ", effectDate=" + this.effectDate + ", failTime=" + this.failTime + ", labelId=" + this.labelId + ", labelName=" + this.labelName + ", labelTypeId=" + this.labelTypeId + ", labelTypeName=" + this.labelTypeName + ", parentId=" + this.parentId + ", publishDesc=" + this.publishDesc + ", publishTime=" + this.publishTime + ", publishUserId=" + this.publishUserId + ", updateCycle=" + this.updateCycle + ", isSysRecom=" + this.isSysRecom + "]";
  }

  public String getColumnCnName()
  {
    return this.columnCnName;
  }

  public void setColumnCnName(String columnCnName) {
    this.columnCnName = columnCnName;
  }

  public String getColumnId() {
    return this.columnId;
  }

  public void setColumnId(String columnId) {
    this.columnId = columnId;
  }

  public String getLabelOrCustomId() {
    return this.labelOrCustomId;
  }

  public void setLabelOrCustomId(String labelOrCustomId) {
    this.labelOrCustomId = labelOrCustomId;
  }

  public String getLabelOrCustomName() {
    return this.labelOrCustomName;
  }

  public void setLabelOrCustomName(String labelOrCustomName) {
    this.labelOrCustomName = labelOrCustomName;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof CiLabelInfo))
      return false;
    CiLabelInfo ciLabelInfo = (CiLabelInfo)obj;
    boolean isEquals = false;
    if (this.labelId == null) {
      if (ciLabelInfo.labelId == null)
      return false;
    }
    if (!(this.labelId.equals(ciLabelInfo.labelId)))
      return false;

    if (this.labelOrCustomId == null) {
     if (ciLabelInfo.labelOrCustomId == null)
      return false;
    }
    if (!(this.labelOrCustomId.equals(ciLabelInfo.labelOrCustomId))) {
      return false;
    }

    return isEquals;
  }

  public Integer getLabelPriority()
  {
    return this.labelPriority;
  }

  public void setLabelPriority(Integer labelPriority) {
    this.labelPriority = labelPriority;
  }

  public int hashCode()
  {
    int result = 17;
    result = 37 * result + ((getLabelId() == null) ? 0 : getLabelId().hashCode());

    result = 37 * result + ((getLabelOrCustomId() == null) ? 0 : getLabelOrCustomId().hashCode());

    return result;
  }

  public String getLabelLevelDesc() {
    return this.labelLevelDesc;
  }

  public void setLabelLevelDesc(String labelLevelDesc) {
    this.labelLevelDesc = labelLevelDesc;
  }

  public String getAttrVal() {
    return this.attrVal;
  }

  public void setAttrVal(String attrVal) {
    this.attrVal = attrVal;
  }

  public String getSortType() {
    return this.sortType;
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  public String getDataDateStr() {
    return this.dataDateStr;
  }

  public void setDataDateStr(String dataDateStr) {
    this.dataDateStr = dataDateStr;
  }

  public String getApproveRoleType() {
    return this.approveRoleType;
  }

  public void setApproveRoleType(String approveRoleType) {
    this.approveRoleType = approveRoleType;
  }

  public List<CiLabelSceneRel> getSceneList() {
    return this.sceneList;
  }

  public void setSceneList(List<CiLabelSceneRel> sceneList) {
    this.sceneList = sceneList;
  }

  public Double getAvgScore() {
    return this.avgScore;
  }

  public void setAvgScore(Double avgScore) {
    this.avgScore = avgScore;
  }

  public Integer getUseTimes() {
    return this.useTimes;
  }

  public void setUseTimes(Integer useTimes) {
    this.useTimes = useTimes;
  }

  public String getLabelIdLevelDesc() {
    return this.labelIdLevelDesc;
  }

  public void setLabelIdLevelDesc(String labelIdLevelDesc) {
    this.labelIdLevelDesc = labelIdLevelDesc;
  }

  public String getLabelCategories() {
    return this.labelCategories;
  }

  public void setLabelCategories(String labelCategories) {
    this.labelCategories = labelCategories;
  }
}