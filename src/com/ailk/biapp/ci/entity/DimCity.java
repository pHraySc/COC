// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCity.java

package com.ailk.biapp.ci.entity;


public class DimCity
{

	private Integer cityId;
	private String cityName;
	private String cityDesc;
	private Integer parentId;
	private Integer columnId;
	private Integer sortNum;

	public DimCity()
	{
	}

	public Integer getCityId()
	{
		return cityId;
	}

	public void setCityId(Integer cityId)
	{
		this.cityId = cityId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getCityDesc()
	{
		return cityDesc;
	}

	public void setCityDesc(String cityDesc)
	{
		this.cityDesc = cityDesc;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public Integer getColumnId()
	{
		return columnId;
	}

	public void setColumnId(Integer columnId)
	{
		this.columnId = columnId;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}
}
