// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiExternalSysLabelRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiExternalSysLabelRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String labelId;
	private String categoryId;

	public CiExternalSysLabelRelId()
	{
	}

	public CiExternalSysLabelRelId(String labelId, String categoryId)
	{
		this.labelId = labelId;
		this.categoryId = categoryId;
	}

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
	}

	public String getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(String categoryId)
	{
		this.categoryId = categoryId;
	}
}
