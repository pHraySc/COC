// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyConclusionEditUser.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiVerifyConclusionEditUser
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private Timestamp addTime;

	public CiVerifyConclusionEditUser()
	{
	}

	public CiVerifyConclusionEditUser(Timestamp addTime)
	{
		this.addTime = addTime;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Timestamp getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Timestamp addTime)
	{
		this.addTime = addTime;
	}
}
