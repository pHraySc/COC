// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimLabelDataStatus.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimLabelDataStatus
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer dataStatusId;
	private String dataStatusName;
	private String dataStatusDesc;

	public DimLabelDataStatus()
	{
	}

	public DimLabelDataStatus(String dataStatusName, String dataStatusDesc)
	{
		this.dataStatusName = dataStatusName;
		this.dataStatusDesc = dataStatusDesc;
	}

	public Integer getDataStatusId()
	{
		return dataStatusId;
	}

	public void setDataStatusId(Integer dataStatusId)
	{
		this.dataStatusId = dataStatusId;
	}

	public String getDataStatusName()
	{
		return dataStatusName;
	}

	public void setDataStatusName(String dataStatusName)
	{
		this.dataStatusName = dataStatusName;
	}

	public String getDataStatusDesc()
	{
		return dataStatusDesc;
	}

	public void setDataStatusDesc(String dataStatusDesc)
	{
		this.dataStatusDesc = dataStatusDesc;
	}
}
