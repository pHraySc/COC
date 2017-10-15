// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomModifyHistory.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiCustomModifyHistory
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer historyId;
	private String customGroupId;
	private Date modifyTime;
	private String modifyUserId;
	private String customGroupName;
	private String customGroupDesc;
	private String parentCustomId;
	private String templateId;
	private Integer updateCycle;
	private String prodOptRuleShow;
	private String labelOptRuleShow;
	private String customOptRuleShow;
	private String kpiDiffRule;
	private Date startDate;
	private Date endDate;
	private Integer isPrivate;
	private String createCityId;
	private String thumbnailData;
	private Integer isSysRecom;
	private Integer isContainLocalList;
	private String sceneIds;
	private Integer isHasList;
	private String monthLabelDate;
	private String dayLabelDate;
	private String tacticsId;
	private Integer listMaxNum;

	public CiCustomModifyHistory()
	{
	}

	public Integer getHistoryId()
	{
		return historyId;
	}

	public void setHistoryId(Integer historyId)
	{
		this.historyId = historyId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public String getModifyUserId()
	{
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId)
	{
		this.modifyUserId = modifyUserId;
	}

	public String getCustomGroupName()
	{
		return customGroupName;
	}

	public void setCustomGroupName(String customGroupName)
	{
		this.customGroupName = customGroupName;
	}

	public String getCustomGroupDesc()
	{
		return customGroupDesc;
	}

	public void setCustomGroupDesc(String customGroupDesc)
	{
		this.customGroupDesc = customGroupDesc;
	}

	public String getParentCustomId()
	{
		return parentCustomId;
	}

	public void setParentCustomId(String parentCustomId)
	{
		this.parentCustomId = parentCustomId;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public Integer getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public String getLabelOptRuleShow()
	{
		return labelOptRuleShow;
	}

	public void setLabelOptRuleShow(String labelOptRuleShow)
	{
		this.labelOptRuleShow = labelOptRuleShow;
	}

	public String getCustomOptRuleShow()
	{
		return customOptRuleShow;
	}

	public void setCustomOptRuleShow(String customOptRuleShow)
	{
		this.customOptRuleShow = customOptRuleShow;
	}

	public String getKpiDiffRule()
	{
		return kpiDiffRule;
	}

	public void setKpiDiffRule(String kpiDiffRule)
	{
		this.kpiDiffRule = kpiDiffRule;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getProdOptRuleShow()
	{
		return prodOptRuleShow;
	}

	public void setProdOptRuleShow(String prodOptRuleShow)
	{
		this.prodOptRuleShow = prodOptRuleShow;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiCustomModifyHistory [historyId=").append(historyId).append(", customGroupId=").append(customGroupId).append(", modifyTime=").append(modifyTime).append(", modifyUserId=").append(modifyUserId).append(", customGroupName=").append(customGroupName).append(", customGroupDesc=").append(customGroupDesc).append(", parentCustomId=").append(parentCustomId).append(", templateId=").append(templateId).append(", updateCycle=").append(updateCycle).append(", prodOptRuleShow=").append(prodOptRuleShow).append(", labelOptRuleShow=").append(labelOptRuleShow).append(", customOptRuleShow=").append(customOptRuleShow).append(", kpiDiffRule=").append(kpiDiffRule).append(", startDate=").append(startDate).append(", endDate=").append(endDate).append("]").toString();
	}

	public Integer getIsPrivate()
	{
		return isPrivate;
	}

	public void setIsPrivate(Integer isPrivate)
	{
		this.isPrivate = isPrivate;
	}

	public String getCreateCityId()
	{
		return createCityId;
	}

	public void setCreateCityId(String createCityId)
	{
		this.createCityId = createCityId;
	}

	public String getThumbnailData()
	{
		return thumbnailData;
	}

	public void setThumbnailData(String thumbnailData)
	{
		this.thumbnailData = thumbnailData;
	}

	public Integer getIsSysRecom()
	{
		return isSysRecom;
	}

	public void setIsSysRecom(Integer isSysRecom)
	{
		this.isSysRecom = isSysRecom;
	}

	public String getSceneIds()
	{
		return sceneIds;
	}

	public void setSceneIds(String sceneIds)
	{
		this.sceneIds = sceneIds;
	}

	public Integer getIsContainLocalList()
	{
		return isContainLocalList;
	}

	public void setIsContainLocalList(Integer isContainLocalList)
	{
		this.isContainLocalList = isContainLocalList;
	}

	public Integer getIsHasList()
	{
		return isHasList;
	}

	public void setIsHasList(Integer isHasList)
	{
		this.isHasList = isHasList;
	}

	public String getMonthLabelDate()
	{
		return monthLabelDate;
	}

	public void setMonthLabelDate(String monthLabelDate)
	{
		this.monthLabelDate = monthLabelDate;
	}

	public String getDayLabelDate()
	{
		return dayLabelDate;
	}

	public void setDayLabelDate(String dayLabelDate)
	{
		this.dayLabelDate = dayLabelDate;
	}

	public String getTacticsId()
	{
		return tacticsId;
	}

	public void setTacticsId(String tacticsId)
	{
		this.tacticsId = tacticsId;
	}

	public Integer getListMaxNum()
	{
		return listMaxNum;
	}

	public void setListMaxNum(Integer listMaxNum)
	{
		this.listMaxNum = listMaxNum;
	}
}
