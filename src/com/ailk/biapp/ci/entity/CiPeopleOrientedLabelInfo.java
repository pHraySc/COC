// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiPeopleOrientedLabelInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiPeopleOrientedLabelInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String labelId;
	private Integer typeId;
	private Integer isImportant;

	public CiPeopleOrientedLabelInfo()
	{
	}

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public Integer getIsImportant()
	{
		return isImportant;
	}

	public void setIsImportant(Integer isImportant)
	{
		this.isImportant = isImportant;
	}
}
