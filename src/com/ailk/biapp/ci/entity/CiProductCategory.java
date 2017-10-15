// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiProductCategory.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiProductCategory
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer categoryId;
	private Integer parentId;
	private String categoryName;
	private String descTxt;
	private Integer isLeaf;

	public CiProductCategory()
	{
	}

	public CiProductCategory(String categoryName, String descTxt)
	{
		this.categoryName = categoryName;
		this.descTxt = descTxt;
	}

	public Integer getCategoryId()
	{
		return categoryId;
	}

	public Integer getIsLeaf()
	{
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public void setCategoryId(Integer categoryId)
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

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}
}
