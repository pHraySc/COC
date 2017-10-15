// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCityThreadConfig.java

package com.ailk.biapp.ci.entity;


public class DimCityThreadConfig
{

	private String cityId;
	private String cityName;
	private Integer threadNum;

	public DimCityThreadConfig()
	{
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
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

	public Integer getThreadNum()
	{
		return threadNum;
	}

	public void setThreadNum(Integer threadNum)
	{
		this.threadNum = threadNum;
	}
}
