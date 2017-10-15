// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomProductRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomProductRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer primaryId;
	private String userId;
	private String customGroupId;
	private Integer productId;
	private String createDate;
	private String status;

	public CiCustomProductRel()
	{
	}

	public CiCustomProductRel(String userId, String customGroupId, Integer productId, String createDate, String status)
	{
		this.userId = userId;
		this.customGroupId = customGroupId;
		this.productId = productId;
		this.createDate = createDate;
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

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
