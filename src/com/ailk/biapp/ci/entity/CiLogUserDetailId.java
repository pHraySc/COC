// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLogUserDetailId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLogUserDetailId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String dataDate;
	private String userId;
	private String opTypeId;

	public CiLogUserDetailId()
	{
	}

	public CiLogUserDetailId(String dataDate, String userId, String opTypeId)
	{
		this.dataDate = dataDate;
		this.userId = userId;
		this.opTypeId = opTypeId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getOpTypeId()
	{
		return opTypeId;
	}

	public void setOpTypeId(String opTypeId)
	{
		this.opTypeId = opTypeId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiLogUserDetailId))
		{
			return false;
		} else
		{
			CiLogUserDetailId castOther = (CiLogUserDetailId)other;
			return (getDataDate() == castOther.getDataDate() || getDataDate() != null && castOther.getDataDate() != null && getDataDate().equals(castOther.getDataDate())) && (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getOpTypeId() == castOther.getOpTypeId() || getOpTypeId() != null && castOther.getOpTypeId() != null && getOpTypeId().equals(castOther.getOpTypeId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getDataDate() != null ? getDataDate().hashCode() : 0);
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getOpTypeId() != null ? getOpTypeId().hashCode() : 0);
		return result;
	}
}
