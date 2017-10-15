// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLogOverview.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLogOverviewId

public class CiLogOverview
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLogOverviewId id;
	private String thirdDeptName;
	private String secondDeptName;
	private Integer opTimes;

	public CiLogOverview()
	{
	}

	public CiLogOverview(CiLogOverviewId id)
	{
		this.id = id;
	}

	public CiLogOverview(CiLogOverviewId id, String thirdDeptName, String secondDeptName, Integer opTimes)
	{
		this.id = id;
		this.thirdDeptName = thirdDeptName;
		this.secondDeptName = secondDeptName;
		this.opTimes = opTimes;
	}

	public CiLogOverviewId getId()
	{
		return id;
	}

	public void setId(CiLogOverviewId id)
	{
		this.id = id;
	}

	public String getThirdDeptName()
	{
		return thirdDeptName;
	}

	public void setThirdDeptName(String thirdDeptName)
	{
		this.thirdDeptName = thirdDeptName;
	}

	public String getSecondDeptName()
	{
		return secondDeptName;
	}

	public void setSecondDeptName(String secondDeptName)
	{
		this.secondDeptName = secondDeptName;
	}

	public Integer getOpTimes()
	{
		return opTimes;
	}

	public void setOpTimes(Integer opTimes)
	{
		this.opTimes = opTimes;
	}
}
