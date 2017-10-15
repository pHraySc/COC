// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCocIndexTableInfo.java

package com.ailk.biapp.ci.entity;

import java.util.Date;

public class DimCocIndexTableInfo
{

	private String dataSrcCode;
	private String dataSrcTabName;
	private Integer tableDataCycle;
	private String joinColumn;
	private String joinRules;
	private Date effectiveTime;
	private Date invalidTime;

	public DimCocIndexTableInfo()
	{
	}

	public String getDataSrcCode()
	{
		return dataSrcCode;
	}

	public void setDataSrcCode(String dataSrcCode)
	{
		this.dataSrcCode = dataSrcCode;
	}

	public String getDataSrcTabName()
	{
		return dataSrcTabName;
	}

	public void setDataSrcTabName(String dataSrcTabName)
	{
		this.dataSrcTabName = dataSrcTabName;
	}

	public Integer getTableDataCycle()
	{
		return tableDataCycle;
	}

	public void setTableDataCycle(Integer tableDataCycle)
	{
		this.tableDataCycle = tableDataCycle;
	}

	public String getJoinColumn()
	{
		return joinColumn;
	}

	public void setJoinColumn(String joinColumn)
	{
		this.joinColumn = joinColumn;
	}

	public String getJoinRules()
	{
		return joinRules;
	}

	public void setJoinRules(String joinRules)
	{
		this.joinRules = joinRules;
	}

	public Date getEffectiveTime()
	{
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime)
	{
		this.effectiveTime = effectiveTime;
	}

	public Date getInvalidTime()
	{
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime)
	{
		this.invalidTime = invalidTime;
	}
}
