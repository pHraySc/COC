// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLogOverviewId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLogOverviewId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String thirdDeptId;
	private String dataDate;
	private String opTypeId;
	private String secondDeptId;

	public CiLogOverviewId()
	{
	}

	public CiLogOverviewId(String thirdDeptId, String dataDate, String opTypeId, String secondDeptId)
	{
		this.thirdDeptId = thirdDeptId;
		this.dataDate = dataDate;
		this.opTypeId = opTypeId;
		this.secondDeptId = secondDeptId;
	}

	public String getThirdDeptId()
	{
		return thirdDeptId;
	}

	public void setThirdDeptId(String thirdDeptId)
	{
		this.thirdDeptId = thirdDeptId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getOpTypeId()
	{
		return opTypeId;
	}

	public void setOpTypeId(String opTypeId)
	{
		this.opTypeId = opTypeId;
	}

	public String getSecondDeptId()
	{
		return secondDeptId;
	}

	public void setSecondDeptId(String secondDeptId)
	{
		this.secondDeptId = secondDeptId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiLogOverviewId))
		{
			return false;
		} else
		{
			CiLogOverviewId castOther = (CiLogOverviewId)other;
			return (getThirdDeptId() == castOther.getThirdDeptId() || getThirdDeptId() != null && castOther.getThirdDeptId() != null && getThirdDeptId().equals(castOther.getThirdDeptId())) && (getDataDate() == castOther.getDataDate() || getDataDate() != null && castOther.getDataDate() != null && getDataDate().equals(castOther.getDataDate())) && (getOpTypeId() == castOther.getOpTypeId() || getOpTypeId() != null && castOther.getOpTypeId() != null && getOpTypeId().equals(castOther.getOpTypeId())) && (getSecondDeptId() == castOther.getSecondDeptId() || getSecondDeptId() != null && castOther.getSecondDeptId() != null && getSecondDeptId().equals(castOther.getSecondDeptId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getThirdDeptId() != null ? getThirdDeptId().hashCode() : 0);
		result = 37 * result + (getDataDate() != null ? getDataDate().hashCode() : 0);
		result = 37 * result + (getOpTypeId() != null ? getOpTypeId().hashCode() : 0);
		result = 37 * result + (getSecondDeptId() != null ? getSecondDeptId().hashCode() : 0);
		return result;
	}
}
