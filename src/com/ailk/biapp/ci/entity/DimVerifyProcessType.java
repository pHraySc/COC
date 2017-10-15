// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimVerifyProcessType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimVerifyProcessType
	implements Serializable
{

	private static final long serialVersionUID = 0x728692c0ea547f2aL;
	private Short processTypeId;
	private String processTypeName;
	private String processTypeDesc;
	private Short sortNum;

	public DimVerifyProcessType()
	{
	}

	public DimVerifyProcessType(String processTypeName, String processTypeDesc, Short sortNum)
	{
		this.processTypeName = processTypeName;
		this.processTypeDesc = processTypeDesc;
		this.sortNum = sortNum;
	}

	public Short getProcessTypeId()
	{
		return processTypeId;
	}

	public void setProcessTypeId(Short processTypeId)
	{
		this.processTypeId = processTypeId;
	}

	public String getProcessTypeName()
	{
		return processTypeName;
	}

	public void setProcessTypeName(String processTypeName)
	{
		this.processTypeName = processTypeName;
	}

	public String getProcessTypeDesc()
	{
		return processTypeDesc;
	}

	public void setProcessTypeDesc(String processTypeDesc)
	{
		this.processTypeDesc = processTypeDesc;
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
