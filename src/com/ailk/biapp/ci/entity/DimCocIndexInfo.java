// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCocIndexInfo.java

package com.ailk.biapp.ci.entity;

import java.util.Date;

public class DimCocIndexInfo
{

	private String indexCode;
	private String targetTableCode;
	private String indexDesc;
	private String dataType;
	private Integer statCycle;
	private String defaultValue;
	private String dataSrcColName;
	private String dataSrcCode;
	private Date effectiveTime;
	private Date invalidTime;
	private String indexTypeName;
	private String dataSrcName;
	private String targetTableName;
	private Date beginEffectiveTime;
	private Date endEffectiveTime;

	public DimCocIndexInfo()
	{
	}

	public String getTargetTableCode()
	{
		return targetTableCode;
	}

	public void setTargetTableCode(String targetTableCode)
	{
		this.targetTableCode = targetTableCode;
	}

	public String getIndexDesc()
	{
		return indexDesc;
	}

	public void setIndexDesc(String indexDesc)
	{
		this.indexDesc = indexDesc;
	}

	public String getIndexCode()
	{
		return indexCode;
	}

	public void setIndexCode(String indexCode)
	{
		this.indexCode = indexCode;
	}

	public Integer getStatCycle()
	{
		return statCycle;
	}

	public void setStatCycle(Integer statCycle)
	{
		this.statCycle = statCycle;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public String getDataSrcColName()
	{
		return dataSrcColName;
	}

	public void setDataSrcColName(String dataSrcColName)
	{
		this.dataSrcColName = dataSrcColName;
	}

	public String getDataSrcCode()
	{
		return dataSrcCode;
	}

	public void setDataSrcCode(String dataSrcCode)
	{
		this.dataSrcCode = dataSrcCode;
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

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getIndexTypeName()
	{
		return indexTypeName;
	}

	public void setIndexTypeName(String indexTypeName)
	{
		this.indexTypeName = indexTypeName;
	}

	public String getDataSrcName()
	{
		return dataSrcName;
	}

	public void setDataSrcName(String dataSrcName)
	{
		this.dataSrcName = dataSrcName;
	}

	public String getTargetTableName()
	{
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName)
	{
		this.targetTableName = targetTableName;
	}

	public Date getBeginEffectiveTime()
	{
		return beginEffectiveTime;
	}

	public void setBeginEffectiveTime(Date beginEffectiveTime)
	{
		this.beginEffectiveTime = beginEffectiveTime;
	}

	public Date getEndEffectiveTime()
	{
		return endEffectiveTime;
	}

	public void setEndEffectiveTime(Date endEffectiveTime)
	{
		this.endEffectiveTime = endEffectiveTime;
	}
}
