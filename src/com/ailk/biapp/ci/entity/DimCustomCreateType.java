// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCustomCreateType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimCustomCreateType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer createTypeId;
	private String createTypeName;
	private String createTypeDesc;

	public DimCustomCreateType()
	{
	}

	public DimCustomCreateType(String createTypeName, String createTypeDesc)
	{
		this.createTypeName = createTypeName;
		this.createTypeDesc = createTypeDesc;
	}

	public Integer getCreateTypeId()
	{
		return createTypeId;
	}

	public void setCreateTypeId(Integer createTypeId)
	{
		this.createTypeId = createTypeId;
	}

	public String getCreateTypeName()
	{
		return createTypeName;
	}

	public void setCreateTypeName(String createTypeName)
	{
		this.createTypeName = createTypeName;
	}

	public String getCreateTypeDesc()
	{
		return createTypeDesc;
	}

	public void setCreateTypeDesc(String createTypeDesc)
	{
		this.createTypeDesc = createTypeDesc;
	}
}
