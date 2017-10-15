// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimVerifyIndexShow.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimVerifyIndexShow
	implements Serializable
{

	private static final long serialVersionUID = 0x2986f58ff039a158L;
	private String indexId;
	private String indexName;
	private String granularity;
	private String range;

	public DimVerifyIndexShow()
	{
	}

	public DimVerifyIndexShow(String indexName, String granularity, String range)
	{
		this.indexName = indexName;
		this.granularity = granularity;
		this.range = range;
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

	public String getGranularity()
	{
		return granularity;
	}

	public void setGranularity(String granularity)
	{
		this.granularity = granularity;
	}

	public String getRange()
	{
		return range;
	}

	public void setRange(String range)
	{
		this.range = range;
	}
}
