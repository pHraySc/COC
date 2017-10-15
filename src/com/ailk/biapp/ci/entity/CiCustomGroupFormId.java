// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomGroupFormId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomGroupFormId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String listTableName;
	private Short cityId;
	private Short vipLevelId;
	private Short brandId;

	public CiCustomGroupFormId()
	{
	}

	public CiCustomGroupFormId(String listTableName, Short cityId, Short vipLevelId, Short brandId)
	{
		this.listTableName = listTableName;
		this.cityId = cityId;
		this.vipLevelId = vipLevelId;
		this.brandId = brandId;
	}

	public String getListTableName()
	{
		return listTableName;
	}

	public void setListTableName(String listTableName)
	{
		this.listTableName = listTableName;
	}

	public Short getCityId()
	{
		return cityId;
	}

	public void setCityId(Short cityId)
	{
		this.cityId = cityId;
	}

	public Short getVipLevelId()
	{
		return vipLevelId;
	}

	public void setVipLevelId(Short vipLevelId)
	{
		this.vipLevelId = vipLevelId;
	}

	public Short getBrandId()
	{
		return brandId;
	}

	public void setBrandId(Short brandId)
	{
		this.brandId = brandId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiCustomGroupFormId))
		{
			return false;
		} else
		{
			CiCustomGroupFormId castOther = (CiCustomGroupFormId)other;
			return (getListTableName() == castOther.getListTableName() || getListTableName() != null && castOther.getListTableName() != null && getListTableName().equals(castOther.getListTableName())) && (getCityId() == castOther.getCityId() || getCityId() != null && castOther.getCityId() != null && getCityId().equals(castOther.getCityId())) && (getVipLevelId() == castOther.getVipLevelId() || getVipLevelId() != null && castOther.getVipLevelId() != null && getVipLevelId().equals(castOther.getVipLevelId())) && (getBrandId() == castOther.getBrandId() || getBrandId() != null && castOther.getBrandId() != null && getBrandId().equals(castOther.getBrandId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getListTableName() != null ? getListTableName().hashCode() : 0);
		result = 37 * result + (getCityId() != null ? getCityId().hashCode() : 0);
		result = 37 * result + (getVipLevelId() != null ? getVipLevelId().hashCode() : 0);
		result = 37 * result + (getBrandId() != null ? getBrandId().hashCode() : 0);
		return result;
	}
}
