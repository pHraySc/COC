// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomUseStatId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomUseStatId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String customId;
	private Integer useTypeId;
	private String dataDate;

	public CiCustomUseStatId()
	{
	}

	public CiCustomUseStatId(String customId, Integer useTypeId, String dataDate)
	{
		this.customId = customId;
		this.useTypeId = useTypeId;
		this.dataDate = dataDate;
	}

	public String getCustomId()
	{
		return customId;
	}

	public void setCustomId(String customId)
	{
		this.customId = customId;
	}

	public Integer getUseTypeId()
	{
		return useTypeId;
	}

	public void setUseTypeId(Integer useTypeId)
	{
		this.useTypeId = useTypeId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiCustomUseStatId))
		{
			return false;
		} else
		{
			CiCustomUseStatId castOther = (CiCustomUseStatId)other;
			return (getCustomId() == castOther.getCustomId() || getCustomId() != null && castOther.getCustomId() != null && getCustomId().equals(castOther.getCustomId())) && (getUseTypeId() == castOther.getUseTypeId() || getUseTypeId() != null && castOther.getUseTypeId() != null && getUseTypeId().equals(castOther.getUseTypeId())) && (getDataDate() == castOther.getDataDate() || getDataDate() != null && castOther.getDataDate() != null && getDataDate().equals(castOther.getDataDate()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getCustomId() != null ? getCustomId().hashCode() : 0);
		result = 37 * result + (getUseTypeId() != null ? getUseTypeId().hashCode() : 0);
		result = 37 * result + (getDataDate() != null ? getDataDate().hashCode() : 0);
		return result;
	}
}
