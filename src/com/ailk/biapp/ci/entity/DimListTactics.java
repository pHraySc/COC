// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimListTactics.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimListTactics
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String tacticsId;
	private String tacticsName;
	private String descTxt;

	public String getTacticsId()
	{
		return tacticsId;
	}

	public void setTacticsId(String tacticsId)
	{
		this.tacticsId = tacticsId;
	}

	public String getTacticsName()
	{
		return tacticsName;
	}

	public void setTacticsName(String tacticsName)
	{
		this.tacticsName = tacticsName;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public DimListTactics(String tacticsId, String tacticsName, String descTxt)
	{
		this.tacticsId = tacticsId;
		this.tacticsName = tacticsName;
		this.descTxt = descTxt;
	}

	public DimListTactics()
	{
	}
}
