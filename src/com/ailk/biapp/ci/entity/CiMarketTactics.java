// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiMarketTactics.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class CiMarketTactics
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String tacticId;
	private String tacticName;
	private String tacticDesc;
	private String createUserId;
	private Timestamp createTime;
	private Short status;
	private String tacticNames;
	private String customGroupName;
	private String dateType;
	private List customGroupNames;
	private List productNames;

	public CiMarketTactics()
	{
	}

	public CiMarketTactics(String tacticName, String tacticDesc, String createUserId, Timestamp createTime, Short status)
	{
		this.tacticName = tacticName;
		this.tacticDesc = tacticDesc;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.status = status;
	}

	public String getTacticId()
	{
		return tacticId;
	}

	public void setTacticId(String tacticId)
	{
		this.tacticId = tacticId;
	}

	public String getTacticName()
	{
		return tacticName;
	}

	public void setTacticName(String tacticName)
	{
		this.tacticName = tacticName;
	}

	public String getTacticDesc()
	{
		return tacticDesc;
	}

	public void setTacticDesc(String tacticDesc)
	{
		this.tacticDesc = tacticDesc;
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

	public Short getStatus()
	{
		return status;
	}

	public void setStatus(Short status)
	{
		this.status = status;
	}

	public String getTacticNames()
	{
		return tacticNames;
	}

	public void setTacticNames(String tacticNames)
	{
		this.tacticNames = tacticNames;
	}

	public String getDateType()
	{
		return dateType;
	}

	public void setDateType(String dateType)
	{
		this.dateType = dateType;
	}

	public String getCustomGroupName()
	{
		return customGroupName;
	}

	public void setCustomGroupName(String customGroupName)
	{
		this.customGroupName = customGroupName;
	}

	public List getCustomGroupNames()
	{
		return customGroupNames;
	}

	public void setCustomGroupNames(List customGroupNames)
	{
		this.customGroupNames = customGroupNames;
	}

	public List getProductNames()
	{
		return productNames;
	}

	public void setProductNames(List productNames)
	{
		this.productNames = productNames;
	}
}
