// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimVipLevel.java

package com.ailk.biapp.ci.entity;


public class DimVipLevel
{

	private Integer vipLevelId;
	private String vipLevelName;
	private String vipLevelDesc;
	private Integer columnId;
	private Integer sortNum;

	public DimVipLevel()
	{
	}

	public Integer getVipLevelId()
	{
		return vipLevelId;
	}

	public void setVipLevelId(Integer vipLevelId)
	{
		this.vipLevelId = vipLevelId;
	}

	public String getVipLevelName()
	{
		return vipLevelName;
	}

	public void setVipLevelName(String vipLevelName)
	{
		this.vipLevelName = vipLevelName;
	}

	public String getVipLevelDesc()
	{
		return vipLevelDesc;
	}

	public void setVipLevelDesc(String vipLevelDesc)
	{
		this.vipLevelDesc = vipLevelDesc;
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
