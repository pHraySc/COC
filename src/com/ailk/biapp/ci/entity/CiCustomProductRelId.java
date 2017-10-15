// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomProductRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomProductRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String customGroupId;
	private String productNo;

	public CiCustomProductRelId()
	{
	}

	public CiCustomProductRelId(String userId, String customGroupId, String productNo)
	{
		this.userId = userId;
		this.customGroupId = customGroupId;
		this.productNo = productNo;
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

	public String getProductNo()
	{
		return productNo;
	}

	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiCustomProductRelId))
		{
			return false;
		} else
		{
			CiCustomProductRelId castOther = (CiCustomProductRelId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getCustomGroupId() == castOther.getCustomGroupId() || getCustomGroupId() != null && castOther.getCustomGroupId() != null && getCustomGroupId().equals(castOther.getCustomGroupId())) && (getProductNo() == castOther.getProductNo() || getProductNo() != null && castOther.getProductNo() != null && getProductNo().equals(castOther.getProductNo()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getCustomGroupId() != null ? getCustomGroupId().hashCode() : 0);
		result = 37 * result + (getProductNo() != null ? getProductNo().hashCode() : 0);
		return result;
	}
}
