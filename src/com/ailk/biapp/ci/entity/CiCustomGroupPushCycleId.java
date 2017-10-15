// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomGroupPushCycleId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomGroupPushCycleId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String customGroupId;
	private String sysId;

	public CiCustomGroupPushCycleId()
	{
	}

	public CiCustomGroupPushCycleId(String customGroupId, String sysId)
	{
		this.customGroupId = customGroupId;
		this.sysId = sysId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getSysId()
	{
		return sysId;
	}

	public void setSysId(String sysId)
	{
		this.sysId = sysId;
	}
}
