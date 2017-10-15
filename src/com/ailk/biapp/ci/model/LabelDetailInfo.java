package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelDetailInfo
	implements Serializable
{

	private static final long serialVersionUID = 0x3c79dad4680f385bL;
	private String labelId;
	private String labelName;
	private String parentId;
	private Double minVal;
	private Double maxVal;
	private Integer labelTypeId;
	private String effecTime;
	private String effectDate;
	private String failTime;
	private String createUserId;
	private String createUser;
	private String publishUserId;
	private String publishUser;
	private String techCaliber;
	private String dataStatus;
	private String updateCycle;
	private String updateCycleName;
	private String createTime;
	private String createDesc;
	private String publishTime;
	private String busiCaliber;
	private String customNum;
	private String useTimes;
	private String dataSource;
	private String currApproveStatus;
	private String isHot;
	private String isNew;
	private String isAttention;
	private String isAlarm;
	private String dataDate;
	private String year;
	private String month;
	private String day;
	private String busiLegend;
	private String applySuggest;
	private boolean hasAlarm;
	private String attrVal;
	private int isInPrilabel;
	private String currentLabelPath;
	private String labelIdLevelDesc;
	private Integer isSysRecom;
	private String labelSceneNames;
	private String labelSceneIds;
	private String dataDateStr;
	private Double avgScore;

	public LabelDetailInfo()
	{
		isHot = "false";
		isNew = "false";
		isAttention = "false";
		isAlarm = "false";
		isInPrilabel = 1;
	}

	public String getIsAlarm()
	{
		return isAlarm;
	}

	public void setIsAlarm(String isAlarm)
	{
		this.isAlarm = isAlarm;
	}

	public String getBusiLegend()
	{
		return busiLegend;
	}

	public void setBusiLegend(String busiLegend)
	{
		this.busiLegend = busiLegend;
	}

	public String getApplySuggest()
	{
		return applySuggest;
	}

	public void setApplySuggest(String applySuggest)
	{
		this.applySuggest = applySuggest;
	}

	public String getCurrApproveStatus()
	{
		return currApproveStatus;
	}

	public void setCurrApproveStatus(String currApproveStatus)
	{
		this.currApproveStatus = currApproveStatus;
	}

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
	}

	public String getLabelName()
	{
		return labelName;
	}

	public void setLabelName(String labelName)
	{
		this.labelName = labelName;
	}

	public String getEffecTime()
	{
		return effecTime;
	}

	public void setEffecTime(String effecTime)
	{
		this.effecTime = effecTime;
	}

	public String getFailTime()
	{
		return failTime;
	}

	public void setFailTime(String failTime)
	{
		this.failTime = failTime;
	}

	public String getCreateUserId()
	{
		return createUserId;
	}

	public void setCreateUserId(String createUserId)
	{
		this.createUserId = createUserId;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public String getPublishUserId()
	{
		return publishUserId;
	}

	public void setPublishUserId(String publishUserId)
	{
		this.publishUserId = publishUserId;
	}

	public String getPublishUser()
	{
		return publishUser;
	}

	public String getLabelSceneIds()
	{
		return labelSceneIds;
	}

	public void setLabelSceneIds(String labelSceneIds)
	{
		this.labelSceneIds = labelSceneIds;
	}

	public void setPublishUser(String publishUser)
	{
		this.publishUser = publishUser;
	}

	public String getTechCaliber()
	{
		return techCaliber;
	}

	public void setTechCaliber(String techCaliber)
	{
		this.techCaliber = techCaliber;
	}

	public String getDataStatus()
	{
		return dataStatus;
	}

	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}

	public String getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(String updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(String publishTime)
	{
		this.publishTime = publishTime;
	}

	public String getBusiCaliber()
	{
		return busiCaliber;
	}

	public void setBusiCaliber(String busiCaliber)
	{
		this.busiCaliber = busiCaliber;
	}

	public String getCustomNum()
	{
		return customNum;
	}

	public void setCustomNum(String customNum)
	{
		this.customNum = customNum;
	}

	public String getUseTimes()
	{
		return useTimes;
	}

	public void setUseTimes(String useTimes)
	{
		this.useTimes = useTimes;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getIsHot()
	{
		return isHot;
	}

	public void setIsHot(String isHot)
	{
		this.isHot = isHot;
	}

	public String getIsNew()
	{
		return isNew;
	}

	public void setIsNew(String isNew)
	{
		this.isNew = isNew;
	}

	public String getIsAttention()
	{
		return isAttention;
	}

	public void setIsAttention(String isAttention)
	{
		this.isAttention = isAttention;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getMonth()
	{
		return month;
	}

	public void setMonth(String month)
	{
		this.month = month;
	}

	public String getDay()
	{
		return day;
	}

	public void setDay(String day)
	{
		this.day = day;
	}

	public String getUpdateCycleName()
	{
		return updateCycleName;
	}

	public void setUpdateCycleName(String updateCycleName)
	{
		this.updateCycleName = updateCycleName;
	}

	public Double getMinVal()
	{
		return minVal;
	}

	public void setMinVal(Double minVal)
	{
		this.minVal = minVal;
	}

	public Double getMaxVal()
	{
		return maxVal;
	}

	public void setMaxVal(Double maxVal)
	{
		this.maxVal = maxVal;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public String getEffectDate()
	{
		return effectDate;
	}

	public void setEffectDate(String effectDate)
	{
		this.effectDate = effectDate;
	}

	public boolean isHasAlarm()
	{
		return hasAlarm;
	}

	public void setHasAlarm(boolean hasAlarm)
	{
		this.hasAlarm = hasAlarm;
	}

	public String getAttrVal()
	{
		return attrVal;
	}

	public void setAttrVal(String attrVal)
	{
		this.attrVal = attrVal;
	}

	public int getIsInPrilabel()
	{
		return isInPrilabel;
	}

	public void setIsInPrilabel(int isInPrilabel)
	{
		this.isInPrilabel = isInPrilabel;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getCurrentLabelPath()
	{
		return currentLabelPath;
	}

	public void setCurrentLabelPath(String currentLabelPath)
	{
		this.currentLabelPath = currentLabelPath;
	}

	public Integer getIsSysRecom()
	{
		return isSysRecom;
	}

	public void setIsSysRecom(Integer isSysRecom)
	{
		this.isSysRecom = isSysRecom;
	}

	public String getLabelSceneNames()
	{
		return labelSceneNames;
	}

	public void setLabelSceneNames(String labelSceneNames)
	{
		this.labelSceneNames = labelSceneNames;
	}

	public String getDataDateStr()
	{
		return dataDateStr;
	}

	public void setDataDateStr(String dataDateStr)
	{
		this.dataDateStr = dataDateStr;
	}

	public String getCreateDesc()
	{
		return createDesc;
	}

	public void setCreateDesc(String createDesc)
	{
		this.createDesc = createDesc;
	}

	public Double getAvgScore()
	{
		return avgScore;
	}

	public void setAvgScore(Double avgScore)
	{
		this.avgScore = avgScore;
	}

	public String getLabelIdLevelDesc()
	{
		return labelIdLevelDesc;
	}

	public void setLabelIdLevelDesc(String labelIdLevelDesc)
	{
		this.labelIdLevelDesc = labelIdLevelDesc;
	}

	public LabelDetailInfo(String labelId, String labelName, String parentId, Double minVal, Double maxVal, Integer labelTypeId, String effecTime, 
			String effectDate, String failTime, String createUserId, String createUser, String publishUserId, String publishUser, String techCaliber, 
			String dataStatus, String updateCycle, String updateCycleName, String createTime, String publishTime, String busiCaliber, String customNum, 
			String useTimes, String dataSource, String currApproveStatus, String isHot, String isNew, String isAttention, String isAlarm, 
			String dataDate, String year, String month, String day, String busiLegend, String applySuggest, boolean hasAlarm, 
			String attrVal, int isInPrilabel, String currentLabelPath, Integer isSysRecom)
	{
		this.isHot = "false";
		this.isNew = "false";
		this.isAttention = "false";
		this.isAlarm = "false";
		this.isInPrilabel = 1;
		this.labelId = labelId;
		this.labelName = labelName;
		this.parentId = parentId;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.labelTypeId = labelTypeId;
		this.effecTime = effecTime;
		this.effectDate = effectDate;
		this.failTime = failTime;
		this.createUserId = createUserId;
		this.createUser = createUser;
		this.publishUserId = publishUserId;
		this.publishUser = publishUser;
		this.techCaliber = techCaliber;
		this.dataStatus = dataStatus;
		this.updateCycle = updateCycle;
		this.updateCycleName = updateCycleName;
		this.createTime = createTime;
		this.publishTime = publishTime;
		this.busiCaliber = busiCaliber;
		this.customNum = customNum;
		this.useTimes = useTimes;
		this.dataSource = dataSource;
		this.currApproveStatus = currApproveStatus;
		this.isHot = isHot;
		this.isNew = isNew;
		this.isAttention = isAttention;
		this.isAlarm = isAlarm;
		this.dataDate = dataDate;
		this.year = year;
		this.month = month;
		this.day = day;
		this.busiLegend = busiLegend;
		this.applySuggest = applySuggest;
		this.hasAlarm = hasAlarm;
		this.attrVal = attrVal;
		this.isInPrilabel = isInPrilabel;
		this.currentLabelPath = currentLabelPath;
		this.isSysRecom = isSysRecom;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LabelDetailInfo [labelId=").append(labelId).append(", labelName=").append(labelName).append(", parentId=").append(parentId).append(", minVal=").append(minVal).append(", maxVal=").append(maxVal).append(", labelTypeId=").append(labelTypeId).append(", effecTime=").append(effecTime).append(", effectDate=").append(effectDate).append(", failTime=").append(failTime).append(", createUserId=").append(createUserId).append(", createUser=").append(createUser).append(", publishUserId=").append(publishUserId).append(", publishUser=").append(publishUser).append(", techCaliber=").append(techCaliber).append(", dataStatus=").append(dataStatus).append(", updateCycle=").append(updateCycle).append(", updateCycleName=").append(updateCycleName).append(", createTime=").append(createTime).append(", publishTime=").append(publishTime).append(", busiCaliber=").append(busiCaliber).append(", customNum=").append(customNum).append(", useTimes=").append(useTimes).append(", dataSource=").append(dataSource).append(", currApproveStatus=").append(currApproveStatus).append(", isHot=").append(isHot).append(", isNew=").append(isNew).append(", isAttention=").append(isAttention).append(", isAlarm=").append(isAlarm).append(", dataDate=").append(dataDate).append(", year=").append(year).append(", month=").append(month).append(", day=").append(day).append(", busiLegend=").append(busiLegend).append(", applySuggest=").append(applySuggest).append(", hasAlarm=").append(hasAlarm).append(", attrVal=").append(attrVal).append(", isInPrilabel=").append(isInPrilabel).append(", currentLabelPath=").append(currentLabelPath).append(", isSysRecom=").append(isSysRecom).append("]").toString();
	}
}
