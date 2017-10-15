// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiListFailureInfo.java

package com.ailk.biapp.ci.entity;

import java.util.Date;

public class CiListFailureInfo
{

	private String ListTableName;
	private Date InsertTime;

	public CiListFailureInfo()
	{
	}

	public CiListFailureInfo(String listTableName, Date insertTime)
	{
		ListTableName = listTableName;
		InsertTime = insertTime;
	}

	public String getListTableName()
	{
		return ListTableName;
	}

	public void setListTableName(String listTableName)
	{
		ListTableName = listTableName;
	}

	public Date getInsertTime()
	{
		return InsertTime;
	}

	public void setInsertTime(Date insertTime)
	{
		InsertTime = insertTime;
	}
}
