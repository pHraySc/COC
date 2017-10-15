package com.ailk.biapp.ci.model;


public class KpiMapData
{

	private String svcId;
	private String cityId;
	private String brandId;
	private String structId;
	private String idxId;
	private Double value;
	private String tipInfo;
	private String color;

	public KpiMapData()
	{
	}

	public String getSvcId()
	{
		return svcId;
	}

	public void setSvcId(String svcId)
	{
		this.svcId = svcId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getBrandId()
	{
		return brandId;
	}

	public void setBrandId(String brandId)
	{
		this.brandId = brandId;
	}

	public String getStructId()
	{
		return structId;
	}

	public void setStructId(String structId)
	{
		this.structId = structId;
	}

	public String getIdxId()
	{
		return idxId;
	}

	public void setIdxId(String idxId)
	{
		this.idxId = idxId;
	}

	public Double getValue()
	{
		return value;
	}

	public void setValue(Double value)
	{
		this.value = value;
	}

	public String getTipInfo()
	{
		return tipInfo;
	}

	public void setTipInfo(String tipInfo)
	{
		this.tipInfo = tipInfo;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}
}
