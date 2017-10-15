// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiPersonIndexInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiPersonIndexInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String indexId;
	private String indexName;
	private Integer typeId;
	private Integer isImportant;

	public CiPersonIndexInfo()
	{
	}

	public String getIndexId()
	{
		return indexId;
	}

	public void setIndexId(String indexId)
	{
		this.indexId = indexId;
	}

	public String getIndexName()
	{
		return indexName;
	}

	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
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
