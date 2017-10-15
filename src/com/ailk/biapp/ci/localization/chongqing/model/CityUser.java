// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CityUser.java

package com.ailk.biapp.ci.localization.chongqing.model;

import com.asiainfo.biframe.privilege.ICity;

public class CityUser
	implements ICity
{

	private String cityId;
	private String parentId;
	private String dmCityId;
	private String dmDeptId;
	private String dmCountyId;
	private String cityName;
	private int sortNum;
	private String dmTypeId;
	private String dmTypeCode;

	public CityUser()
	{
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public void setDmCityId(String dmCityId)
	{
		this.dmCityId = dmCityId;
	}

	public void setDmDeptId(String dmDeptId)
	{
		this.dmDeptId = dmDeptId;
	}

	public void setDmCountyId(String dmCountyId)
	{
		this.dmCountyId = dmCountyId;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public void setSortNum(int sortNum)
	{
		this.sortNum = sortNum;
	}

	public void setDmTypeId(String dmTypeId)
	{
		this.dmTypeId = dmTypeId;
	}

	public void setDmTypeCode(String dmTypeCode)
	{
		this.dmTypeCode = dmTypeCode;
	}

	public String getCityId()
	{
		return cityId;
	}

	public String getParentId()
	{
		return parentId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public String getDmCityId()
	{
		return dmCityId;
	}

	public String getDmCountyId()
	{
		return dmCountyId;
	}

	public String getDmDeptId()
	{
		return dmDeptId;
	}

	public String getDmTypeCode()
	{
		return dmTypeCode;
	}

	public String getDmTypeId()
	{
		return dmTypeId;
	}

	public int getSortNum()
	{
		return sortNum;
	}
}
