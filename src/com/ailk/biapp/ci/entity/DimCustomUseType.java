// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCustomUseType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimCustomUseType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer useTypeId;
	private String useTypeName;
	private String useTypeDesc;

	public DimCustomUseType()
	{
	}

	public DimCustomUseType(Integer useTypeId)
	{
		this.useTypeId = useTypeId;
	}

	public DimCustomUseType(Integer useTypeId, String useTypeName, String useTypeDesc)
	{
		this.useTypeId = useTypeId;
		this.useTypeName = useTypeName;
		this.useTypeDesc = useTypeDesc;
	}

	public Integer getUseTypeId()
	{
		return useTypeId;
	}

	public void setUseTypeId(Integer useTypeId)
	{
		this.useTypeId = useTypeId;
	}

	public String getUseTypeName()
	{
		return useTypeName;
	}

	public void setUseTypeName(String useTypeName)
	{
		this.useTypeName = useTypeName;
	}

	public String getUseTypeDesc()
	{
		return useTypeDesc;
	}

	public void setUseTypeDesc(String useTypeDesc)
	{
		this.useTypeDesc = useTypeDesc;
	}
}
