// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelProductRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLabelProductRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String labelId;
	private String productNo;
	private String activeId;

	public CiLabelProductRelId()
	{
	}

	public CiLabelProductRelId(String userId, String labelId, String productNo, String activeId)
	{
		this.userId = userId;
		this.labelId = labelId;
		this.productNo = productNo;
		this.activeId = activeId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
	}

	public String getProductNo()
	{
		return productNo;
	}

	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	public String getActiveId()
	{
		return activeId;
	}

	public void setActiveId(String activeId)
	{
		this.activeId = activeId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiLabelProductRelId))
		{
			return false;
		} else
		{
			CiLabelProductRelId castOther = (CiLabelProductRelId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId())) && (getProductNo() == castOther.getProductNo() || getProductNo() != null && castOther.getProductNo() != null && getProductNo().equals(castOther.getProductNo())) && (getActiveId() == castOther.getActiveId() || getActiveId() != null && castOther.getActiveId() != null && getActiveId().equals(castOther.getActiveId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		result = 37 * result + (getProductNo() != null ? getProductNo().hashCode() : 0);
		result = 37 * result + (getActiveId() != null ? getActiveId().hashCode() : 0);
		return result;
	}
}
