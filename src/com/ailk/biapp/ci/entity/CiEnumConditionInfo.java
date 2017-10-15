// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiEnumConditionInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiEnumConditionInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String primaryId;
	private String enumId;
	private String enumName;
	private String enumCategoryId;
	private Integer sortNum;

	public CiEnumConditionInfo()
	{
	}

	public CiEnumConditionInfo(String primaryId, String enumId, String enumName, String enumCategoryId, Integer sortNum)
	{
		this.primaryId = primaryId;
		this.enumId = enumId;
		this.enumName = enumName;
		this.enumCategoryId = enumCategoryId;
		this.sortNum = sortNum;
	}

	public String getPrimaryId()
	{
		return primaryId;
	}

	public void setPrimaryId(String primaryId)
	{
		this.primaryId = primaryId;
	}

	public String getEnumId()
	{
		return enumId;
	}

	public void setEnumId(String enumId)
	{
		this.enumId = enumId;
	}

	public String getEnumName()
	{
		return enumName;
	}

	public void setEnumName(String enumName)
	{
		this.enumName = enumName;
	}

	public String getEnumCategoryId()
	{
		return enumCategoryId;
	}

	public void setEnumCategoryId(String enumCategoryId)
	{
		this.enumCategoryId = enumCategoryId;
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
