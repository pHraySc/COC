// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCocLabelColumnInfo.java

package com.ailk.biapp.ci.entity;


public class DimCocLabelColumnInfo
{

	private String labelCode;
	private String columnName;
	private String columnCnName;
	private Integer isMust;
	private Integer columnOpType;
	private String columnDataType;
	private String dimId;
	private String targetTableCode;
	private Integer sortNum;
	private String unit;
	private Integer isNeedAuthority;

	public DimCocLabelColumnInfo()
	{
	}

	public String getLabelCode()
	{
		return labelCode;
	}

	public void setLabelCode(String labelCode)
	{
		this.labelCode = labelCode;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getColumnCnName()
	{
		return columnCnName;
	}

	public void setColumnCnName(String columnCnName)
	{
		this.columnCnName = columnCnName;
	}

	public Integer getIsMust()
	{
		return isMust;
	}

	public void setIsMust(Integer isMust)
	{
		this.isMust = isMust;
	}

	public Integer getColumnOpType()
	{
		return columnOpType;
	}

	public void setColumnOpType(Integer columnOpType)
	{
		this.columnOpType = columnOpType;
	}

	public String getColumnDataType()
	{
		return columnDataType;
	}

	public void setColumnDataType(String columnDataType)
	{
		this.columnDataType = columnDataType;
	}

	public String getDimId()
	{
		return dimId;
	}

	public void setDimId(String dimId)
	{
		this.dimId = dimId;
	}

	public String getTargetTableCode()
	{
		return targetTableCode;
	}

	public void setTargetTableCode(String targetTableCode)
	{
		this.targetTableCode = targetTableCode;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public Integer getIsNeedAuthority()
	{
		return isNeedAuthority;
	}

	public void setIsNeedAuthority(Integer isNeedAuthority)
	{
		this.isNeedAuthority = isNeedAuthority;
	}
}
