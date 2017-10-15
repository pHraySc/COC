// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelStatusHistoryId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiLabelStatusHistoryId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelId;
	private String dataDate;
	private Timestamp dataInsertTime;

	public CiLabelStatusHistoryId()
	{
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public Timestamp getDataInsertTime()
	{
		return dataInsertTime;
	}

	public void setDataInsertTime(Timestamp dataInsertTime)
	{
		this.dataInsertTime = dataInsertTime;
	}
}
