// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomUseStat.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiCustomUseStatId

public class CiCustomUseStat
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiCustomUseStatId id;
	private Integer useTimes;

	public CiCustomUseStat()
	{
	}

	public CiCustomUseStat(CiCustomUseStatId id)
	{
		this.id = id;
	}

	public CiCustomUseStat(CiCustomUseStatId id, Integer useTimes, Integer isTemplate)
	{
		this.id = id;
		this.useTimes = useTimes;
	}

	public CiCustomUseStatId getId()
	{
		return id;
	}

	public void setId(CiCustomUseStatId id)
	{
		this.id = id;
	}

	public Integer getUseTimes()
	{
		return useTimes;
	}

	public void setUseTimes(Integer useTimes)
	{
		this.useTimes = useTimes;
	}
}
