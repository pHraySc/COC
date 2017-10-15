// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimBrand.java

package com.ailk.biapp.ci.entity;


public class DimBrand
{

	private Integer brandId;
	private String brandName;
	private String brandDesc;
	private Integer parentId;
	private Integer columnId;
	private Integer sortNum;

	public DimBrand()
	{
	}

	public Integer getBrandId()
	{
		return brandId;
	}

	public void setBrandId(Integer brandId)
	{
		this.brandId = brandId;
	}

	public String getBrandName()
	{
		return brandName;
	}

	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}

	public String getBrandDesc()
	{
		return brandDesc;
	}

	public void setBrandDesc(String brandDesc)
	{
		this.brandDesc = brandDesc;
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
