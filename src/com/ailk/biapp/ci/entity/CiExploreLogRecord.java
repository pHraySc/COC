// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiExploreLogRecord.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiExploreLogRecord
	implements Serializable
{

	private Integer recordId;
	private String userId;
	private String useLabels;
	private Timestamp exeTime;
	private Integer exploreTimes;

	public CiExploreLogRecord()
	{
	}

	public CiExploreLogRecord(Integer recordId, String userId, String useLabels, Timestamp exeTime, Integer exploreTimes)
	{
		this.userId = userId;
		this.useLabels = useLabels;
		this.exeTime = exeTime;
		this.exploreTimes = exploreTimes;
	}

	public Integer getRecordId()
	{
		return recordId;
	}

	public void setRecordId(Integer recordId)
	{
		this.recordId = recordId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUseLabels()
	{
		return useLabels;
	}

	public void setUseLabels(String useLabels)
	{
		this.useLabels = useLabels;
	}

	public Timestamp getExeTime()
	{
		return exeTime;
	}

	public void setExeTime(Timestamp exeTime)
	{
		this.exeTime = exeTime;
	}

	public Integer getExploreTimes()
	{
		return exploreTimes;
	}

	public void setExploreTimes(Integer exploreTimes)
	{
		this.exploreTimes = exploreTimes;
	}
}
