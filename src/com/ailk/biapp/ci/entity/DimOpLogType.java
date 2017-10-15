// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimOpLogType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimOpLogType
	implements Serializable
{

	private static final long serialVersionUID = 0x2bcdcbe44cced2d3L;
	private String opTypeId;
	private String opTypeName;
	private String parentId;
	private String descTxt;
	private Short sortNum;

	public DimOpLogType()
	{
	}

	public DimOpLogType(String opTypeName, String parentId, String descTxt, Short sortNum)
	{
		this.opTypeName = opTypeName;
		this.parentId = parentId;
		this.descTxt = descTxt;
		this.sortNum = sortNum;
	}

	public String getOpTypeId()
	{
		return opTypeId;
	}

	public void setOpTypeId(String opTypeId)
	{
		this.opTypeId = opTypeId;
	}

	public String getOpTypeName()
	{
		return opTypeName;
	}

	public void setOpTypeName(String opTypeName)
	{
		this.opTypeName = opTypeName;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public Short getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Short sortNum)
	{
		this.sortNum = sortNum;
	}
}
