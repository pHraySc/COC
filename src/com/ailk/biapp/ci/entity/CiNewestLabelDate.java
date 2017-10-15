// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiNewestLabelDate.java

package com.ailk.biapp.ci.entity;


public class CiNewestLabelDate
{

	private String dayNewestDate;
	private String monthNewestDate;
	private Integer dayNewestStatus;
	private Integer monthNewestStatus;

	public CiNewestLabelDate()
	{
	}

	public String getDayNewestDate()
	{
		return dayNewestDate;
	}

	public void setDayNewestDate(String dayNewestDate)
	{
		this.dayNewestDate = dayNewestDate;
	}

	public String getMonthNewestDate()
	{
		return monthNewestDate;
	}

	public void setMonthNewestDate(String monthNewestDate)
	{
		this.monthNewestDate = monthNewestDate;
	}

	public Integer getDayNewestStatus()
	{
		return dayNewestStatus;
	}

	public void setDayNewestStatus(Integer dayNewestStatus)
	{
		this.dayNewestStatus = dayNewestStatus;
	}

	public Integer getMonthNewestStatus()
	{
		return monthNewestStatus;
	}

	public void setMonthNewestStatus(Integer monthNewestStatus)
	{
		this.monthNewestStatus = monthNewestStatus;
	}
}
