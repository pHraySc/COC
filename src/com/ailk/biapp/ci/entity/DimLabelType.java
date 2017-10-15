// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimLabelType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimLabelType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelTypeId;
	private String labelTypeName;
	private String labelTypeDesc;

	public DimLabelType()
	{
	}

	public DimLabelType(String labelTypeName, String labelTypeDesc)
	{
		this.labelTypeName = labelTypeName;
		this.labelTypeDesc = labelTypeDesc;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public String getLabelTypeName()
	{
		return labelTypeName;
	}

	public void setLabelTypeName(String labelTypeName)
	{
		this.labelTypeName = labelTypeName;
	}

	public String getLabelTypeDesc()
	{
		return labelTypeDesc;
	}

	public void setLabelTypeDesc(String labelTypeDesc)
	{
		this.labelTypeDesc = labelTypeDesc;
	}
}
