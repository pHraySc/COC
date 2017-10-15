// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCustomDataStatus.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimCustomDataStatus
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer dataStatusId;
	private String dataStatusName;
	private String dataStatusDesc;
	private Integer sortNum;

	public DimCustomDataStatus()
	{
	}

	public DimCustomDataStatus(Integer dataStatusId, String dataStatusName, String dataStatusDesc, Integer sortNum)
	{
		this.dataStatusId = dataStatusId;
		this.dataStatusName = dataStatusName;
		this.dataStatusDesc = dataStatusDesc;
		this.sortNum = sortNum;
	}

	public Integer getDataStatusId()
	{
		return dataStatusId;
	}

	public void setDataStatusId(Integer dataStatusId)
	{
		this.dataStatusId = dataStatusId;
	}

	public String getDataStatusName()
	{
		return dataStatusName;
	}

	public void setDataStatusName(String dataStatusName)
	{
		this.dataStatusName = dataStatusName;
	}

	public String getDataStatusDesc()
	{
		return dataStatusDesc;
	}

	public void setDataStatusDesc(String dataStatusDesc)
	{
		this.dataStatusDesc = dataStatusDesc;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public int hashCode()
	{
		int prime = 31;
		int result = 1;
		result = 31 * result + (dataStatusDesc != null ? dataStatusDesc.hashCode() : 0);
		result = 31 * result + (dataStatusId != null ? dataStatusId.hashCode() : 0);
		result = 31 * result + (dataStatusName != null ? dataStatusName.hashCode() : 0);
		result = 31 * result + (sortNum != null ? sortNum.hashCode() : 0);
		return result;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DimCustomDataStatus other = (DimCustomDataStatus)obj;
		if (dataStatusDesc == null)
		{
			if (other.dataStatusDesc != null)
				return false;
		} else
		if (!dataStatusDesc.equals(other.dataStatusDesc))
			return false;
		if (dataStatusId == null)
		{
			if (other.dataStatusId != null)
				return false;
		} else
		if (!dataStatusId.equals(other.dataStatusId))
			return false;
		if (dataStatusName == null)
		{
			if (other.dataStatusName != null)
				return false;
		} else
		if (!dataStatusName.equals(other.dataStatusName))
			return false;
		if (sortNum == null)
		{
			if (other.sortNum != null)
				return false;
		} else
		if (!sortNum.equals(other.sortNum))
			return false;
		return true;
	}

	public String toString()
	{
		return (new StringBuilder()).append("DimCustomDataStatus [dataStatusId=").append(dataStatusId).append(", dataStatusName=").append(dataStatusName).append(", dataStatusDesc=").append(dataStatusDesc).append(", sortNum=").append(sortNum).append("]").toString();
	}
}
