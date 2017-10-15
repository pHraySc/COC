// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimPriorityLevel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimPriorityLevel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer priorityId;
	private String priorityName;
	private String priorityDesc;

	public DimPriorityLevel()
	{
	}

	public DimPriorityLevel(String priorityName, String priorityDesc)
	{
		this.priorityName = priorityName;
		this.priorityDesc = priorityDesc;
	}

	public Integer getPriorityId()
	{
		return priorityId;
	}

	public void setPriorityId(Integer priorityId)
	{
		this.priorityId = priorityId;
	}

	public String getPriorityName()
	{
		return priorityName;
	}

	public void setPriorityName(String priorityName)
	{
		this.priorityName = priorityName;
	}

	public String getPriorityDesc()
	{
		return priorityDesc;
	}

	public void setPriorityDesc(String priorityDesc)
	{
		this.priorityDesc = priorityDesc;
	}
}
