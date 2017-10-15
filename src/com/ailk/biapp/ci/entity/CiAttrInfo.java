// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiAttrInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiAttrInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String attrId;
	private String attrName;
	private Integer updateCycle;
	private Timestamp effecTime;
	private Timestamp failTime;
	private String createUserId;
	private Timestamp createTime;
	private Integer dataStatusId;
	private Timestamp publishTime;
	private String createDesc;
	private Integer attrTypeId;
	private String columnId;
	private String busiCaliber;
	private String techCaliber;
	private String owner;

	public CiAttrInfo()
	{
	}

	public CiAttrInfo(String attrName, Integer updateCycle, Timestamp effecTime, Timestamp failTime, String createUserId, Timestamp createTime, Integer dataStatusId, 
			Timestamp publishTime, String createDesc, Integer attrTypeId, String columnId, String busiCaliber, String techCaliber, String owner)
	{
		this.attrName = attrName;
		this.updateCycle = updateCycle;
		this.effecTime = effecTime;
		this.failTime = failTime;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.dataStatusId = dataStatusId;
		this.publishTime = publishTime;
		this.createDesc = createDesc;
		this.attrTypeId = attrTypeId;
		this.columnId = columnId;
		this.busiCaliber = busiCaliber;
		this.techCaliber = techCaliber;
		this.owner = owner;
	}

	public String getAttrId()
	{
		return attrId;
	}

	public void setAttrId(String attrId)
	{
		this.attrId = attrId;
	}

	public String getAttrName()
	{
		return attrName;
	}

	public void setAttrName(String attrName)
	{
		this.attrName = attrName;
	}

	public Integer getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public Timestamp getEffecTime()
	{
		return effecTime;
	}

	public void setEffecTime(Timestamp effecTime)
	{
		this.effecTime = effecTime;
	}

	public Timestamp getFailTime()
	{
		return failTime;
	}

	public void setFailTime(Timestamp failTime)
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

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Integer getDataStatusId()
	{
		return dataStatusId;
	}

	public void setDataStatusId(Integer dataStatusId)
	{
		this.dataStatusId = dataStatusId;
	}

	public Timestamp getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime)
	{
		this.publishTime = publishTime;
	}

	public String getCreateDesc()
	{
		return createDesc;
	}

	public void setCreateDesc(String createDesc)
	{
		this.createDesc = createDesc;
	}

	public Integer getAttrTypeId()
	{
		return attrTypeId;
	}

	public void setAttrTypeId(Integer attrTypeId)
	{
		this.attrTypeId = attrTypeId;
	}

	public String getColumnId()
	{
		return columnId;
	}

	public void setColumnId(String columnId)
	{
		this.columnId = columnId;
	}

	public String getBusiCaliber()
	{
		return busiCaliber;
	}

	public void setBusiCaliber(String busiCaliber)
	{
		this.busiCaliber = busiCaliber;
	}

	public String getTechCaliber()
	{
		return techCaliber;
	}

	public void setTechCaliber(String techCaliber)
	{
		this.techCaliber = techCaliber;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}
}
