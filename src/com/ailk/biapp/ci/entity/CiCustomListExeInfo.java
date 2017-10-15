// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomListExeInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiCustomListExeInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String exeInfoId;
	private String listTableName;
	private String expression;
	private String excpInfo;
	private Date startTime;
	private Date endTime;

	public CiCustomListExeInfo()
	{
	}

	public String getExeInfoId()
	{
		return exeInfoId;
	}

	public void setExeInfoId(String exeInfoId)
	{
		this.exeInfoId = exeInfoId;
	}

	public String getListTableName()
	{
		return listTableName;
	}

	public void setListTableName(String listTableName)
	{
		this.listTableName = listTableName;
	}

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public String getExcpInfo()
	{
		return excpInfo;
	}

	public void setExcpInfo(String excpInfo)
	{
		this.excpInfo = excpInfo;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
}
