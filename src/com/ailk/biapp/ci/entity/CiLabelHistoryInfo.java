// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelHistoryInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiLabelHistoryInfo
	implements Serializable, Cloneable
{

	private static final long serialVersionUID = 1L;
	private Integer historyId;
	private Integer labelId;
	private String labelName;
	private Integer updateCycle;
	private Integer parentId;
	private Date effecTime;
	private Date failTime;
	private String modifyUserId;
	private Date modifyTime;
	private String modifyDesc;
	private Integer dataStatusId;
	private Integer labelTypeId;
	private String dataSource;
	private String deptId;
	private String busiLegend;
	private String applySuggest;
	private Integer sortNum;
	private String busiCaliber;
	private Integer opType;
	private Integer isSysRecom;

	public CiLabelHistoryInfo()
	{
	}

	public CiLabelHistoryInfo(String labelName, Integer updateCycle, Integer parentId, Date effecTime, Date failTime, String modifyUserId, Date modifyTime, 
			String modifyDesc, Integer dataStatusId, Integer labelTypeId, String busiCaliber, String dataSource, Integer opType, String deptId)
	{
		this.labelName = labelName;
		this.updateCycle = updateCycle;
		this.parentId = parentId;
		this.effecTime = effecTime;
		this.failTime = failTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
		this.modifyDesc = modifyDesc;
		this.dataStatusId = dataStatusId;
		this.labelTypeId = labelTypeId;
		this.busiCaliber = busiCaliber;
		this.dataSource = dataSource;
		this.deptId = deptId;
		this.opType = opType;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public Integer getHistoryId()
	{
		return historyId;
	}

	public void setHistoryId(Integer historyId)
	{
		this.historyId = historyId;
	}

	public String getModifyUserId()
	{
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId)
	{
		this.modifyUserId = modifyUserId;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public String getModifyDesc()
	{
		return modifyDesc;
	}

	public void setModifyDesc(String modifyDesc)
	{
		this.modifyDesc = modifyDesc;
	}

	public Integer getOpType()
	{
		return opType;
	}

	public void setOpType(Integer opType)
	{
		this.opType = opType;
	}

	public void setLabelId(Integer labelId)
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

	public Integer getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public Integer getDataStatusId()
	{
		return dataStatusId;
	}

	public void setDataStatusId(Integer dataStatusId)
	{
		this.dataStatusId = dataStatusId;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public String getBusiCaliber()
	{
		return busiCaliber;
	}

	public void setBusiCaliber(String busiCaliber)
	{
		this.busiCaliber = busiCaliber;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public Date getEffecTime()
	{
		return effecTime;
	}

	public void setEffecTime(Date effecTime)
	{
		this.effecTime = effecTime;
	}

	public Date getFailTime()
	{
		return failTime;
	}

	public void setFailTime(Date failTime)
	{
		this.failTime = failTime;
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

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public Integer getIsSysRecom()
	{
		return isSysRecom;
	}

	public void setIsSysRecom(Integer isSysRecom)
	{
		this.isSysRecom = isSysRecom;
	}
}
