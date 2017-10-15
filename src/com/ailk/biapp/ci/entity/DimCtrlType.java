// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCtrlType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimCtrlType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String ctrlTypeId;
	private String ctrlTypeName;
	private String ctrlTypeEname;
	private String descTxt;

	public DimCtrlType()
	{
	}

	public DimCtrlType(String ctrlTypeName, String ctrlTypeEname, String descTxt)
	{
		this.ctrlTypeName = ctrlTypeName;
		this.ctrlTypeEname = ctrlTypeEname;
		this.descTxt = descTxt;
	}

	public String getCtrlTypeId()
	{
		return ctrlTypeId;
	}

	public void setCtrlTypeId(String ctrlTypeId)
	{
		this.ctrlTypeId = ctrlTypeId;
	}

	public String getCtrlTypeName()
	{
		return ctrlTypeName;
	}

	public void setCtrlTypeName(String ctrlTypeName)
	{
		this.ctrlTypeName = ctrlTypeName;
	}

	public String getCtrlTypeEname()
	{
		return ctrlTypeEname;
	}

	public void setCtrlTypeEname(String ctrlTypeEname)
	{
		this.ctrlTypeEname = ctrlTypeEname;
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
