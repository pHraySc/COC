// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomProductTacticsRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomProductTacticsRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer primaryId;
	private String userId;
	private String customGroupId;
	private Integer productId;
	private String tacticId;
	private String createTime;
	private String status;
	private String customGroupName;

	public CiCustomProductTacticsRel()
	{
	}

	public CiCustomProductTacticsRel(String userId, String customGroupId, Integer productId, String tacticId, String createTime, String status)
	{
		this.userId = userId;
		this.customGroupId = customGroupId;
		this.productId = productId;
		this.tacticId = tacticId;
		this.createTime = createTime;
		this.status = status;
	}

	public Integer getPrimaryId()
	{
		return primaryId;
	}

	public void setPrimaryId(Integer primaryId)
	{
		this.primaryId = primaryId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public String getTacticId()
	{
		return tacticId;
	}

	public void setTacticId(String tacticId)
	{
		this.tacticId = tacticId;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getCustomGroupName()
	{
		return customGroupName;
	}

	public void setCustomGroupName(String customGroupName)
	{
		this.customGroupName = customGroupName;
	}
}
