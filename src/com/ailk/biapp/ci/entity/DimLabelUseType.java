// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimLabelUseType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimLabelUseType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelUseTypeId;
	private String labelUseTypeName;
	private String labelUseTypeDesc;

	public DimLabelUseType()
	{
	}

	public DimLabelUseType(String labelUseTypeName, String labelUseTypeDesc)
	{
		this.labelUseTypeName = labelUseTypeName;
		this.labelUseTypeDesc = labelUseTypeDesc;
	}

	public Integer getLabelUseTypeId()
	{
		return labelUseTypeId;
	}

	public void setLabelUseTypeId(Integer labelUseTypeId)
	{
		this.labelUseTypeId = labelUseTypeId;
	}

	public String getLabelUseTypeName()
	{
		return labelUseTypeName;
	}

	public void setLabelUseTypeName(String labelUseTypeName)
	{
		this.labelUseTypeName = labelUseTypeName;
	}

	public String getLabelUseTypeDesc()
	{
		return labelUseTypeDesc;
	}

	public void setLabelUseTypeDesc(String labelUseTypeDesc)
	{
		this.labelUseTypeDesc = labelUseTypeDesc;
	}

	public String toString()
	{
		return (new StringBuilder()).append("DimLabelUseType [labelUseTypeDesc=").append(labelUseTypeDesc).append(", labelUseTypeId=").append(labelUseTypeId).append(", labelUseTypeName=").append(labelUseTypeName).append("]").toString();
	}
}
