// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCrmLabelType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimCrmLabelType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer typeId;
	private String typeName;
	private String typeDesc;
	private String icon;

	public DimCrmLabelType()
	{
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getTypeDesc()
	{
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc)
	{
		this.typeDesc = typeDesc;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}
}
