// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimExternalLabelCategory.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimExternalLabelCategory
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String categoryId;
	private String categoryName;
	private String parentId;
	private Integer sortNum;
	private String sysId;

	public DimExternalLabelCategory()
	{
	}

	public DimExternalLabelCategory(String categoryName, String parentId, Integer sortNum, String sysId)
	{
		this.categoryName = categoryName;
		this.parentId = parentId;
		this.sortNum = sortNum;
		this.sysId = sysId;
	}

	public String getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(String categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getSysId()
	{
		return sysId;
	}

	public void setSysId(String sysId)
	{
		this.sysId = sysId;
	}
}
