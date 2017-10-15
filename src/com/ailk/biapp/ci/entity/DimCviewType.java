// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCviewType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimCviewType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer cviewTypeId;
	private String cviewTypeName;
	private String descTxt;

	public DimCviewType()
	{
	}

	public DimCviewType(String cviewTypeName, String descTxt)
	{
		this.cviewTypeName = cviewTypeName;
		this.descTxt = descTxt;
	}

	public Integer getCviewTypeId()
	{
		return cviewTypeId;
	}

	public void setCviewTypeId(Integer cviewTypeId)
	{
		this.cviewTypeId = cviewTypeId;
	}

	public String getCviewTypeName()
	{
		return cviewTypeName;
	}

	public void setCviewTypeName(String cviewTypeName)
	{
		this.cviewTypeName = cviewTypeName;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}
}
