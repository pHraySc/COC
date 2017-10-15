// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelBrandUserNumId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLabelBrandUserNumId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String dataDate;
	private Integer labelId;
	private Integer cityId;
	private Integer vipLevelId;
	private Integer brandId;

	public CiLabelBrandUserNumId()
	{
	}

	public CiLabelBrandUserNumId(String dataDate, Integer labelId, Integer cityId, Integer vipLevelId, Integer brandId)
	{
		this.dataDate = dataDate;
		this.labelId = labelId;
		this.cityId = cityId;
		this.vipLevelId = vipLevelId;
		this.brandId = brandId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public Integer getCityId()
	{
		return cityId;
	}

	public void setCityId(Integer cityId)
	{
		this.cityId = cityId;
	}

	public Integer getVipLevelId()
	{
		return vipLevelId;
	}

	public void setVipLevelId(Integer vipLevelId)
	{
		this.vipLevelId = vipLevelId;
	}

	public Integer getBrandId()
	{
		return brandId;
	}

	public void setBrandId(Integer brandId)
	{
		this.brandId = brandId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiLabelBrandUserNumId))
		{
			return false;
		} else
		{
			CiLabelBrandUserNumId castOther = (CiLabelBrandUserNumId)other;
			return (getDataDate() == castOther.getDataDate() || getDataDate() != null && castOther.getDataDate() != null && getDataDate().equals(castOther.getDataDate())) && (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId())) && (getCityId() == castOther.getCityId() || getCityId() != null && castOther.getCityId() != null && getCityId().equals(castOther.getCityId())) && (getVipLevelId() == castOther.getVipLevelId() || getVipLevelId() != null && castOther.getVipLevelId() != null && getVipLevelId().equals(castOther.getVipLevelId())) && (getBrandId() == castOther.getBrandId() || getBrandId() != null && castOther.getBrandId() != null && getBrandId().equals(castOther.getBrandId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getDataDate() != null ? getDataDate().hashCode() : 0);
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		result = 37 * result + (getCityId() != null ? getCityId().hashCode() : 0);
		result = 37 * result + (getVipLevelId() != null ? getVipLevelId().hashCode() : 0);
		result = 37 * result + (getBrandId() != null ? getBrandId().hashCode() : 0);
		return result;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiLabelBrandUserNumId [brandId=").append(brandId).append(", cityId=").append(cityId).append(", dataDate=").append(dataDate).append(", labelId=").append(labelId).append(", vipLevelId=").append(vipLevelId).append("]").toString();
	}
}
