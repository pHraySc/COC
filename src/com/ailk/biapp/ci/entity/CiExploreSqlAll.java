// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiExploreSqlAll.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiExploreSqlAll
	implements Serializable
{

	private Integer primaryId;
	private Integer recordId;
	private String sqlPart;
	private Integer sortNum;

	public CiExploreSqlAll()
	{
	}

	public CiExploreSqlAll(Integer recordId, String sqlPart, Integer sortNum)
	{
		this.recordId = recordId;
		this.sqlPart = sqlPart;
		this.sortNum = sortNum;
	}

	public CiExploreSqlAll(Integer primaryId, Integer recordId, String sqlPart, Integer sortNum)
	{
		this.primaryId = primaryId;
		this.recordId = recordId;
		this.sqlPart = sqlPart;
		this.sortNum = sortNum;
	}

	public Integer getPrimaryId()
	{
		return primaryId;
	}

	public void setPrimaryId(Integer primaryId)
	{
		this.primaryId = primaryId;
	}

	public Integer getRecordId()
	{
		return recordId;
	}

	public void setRecordId(Integer recordId)
	{
		this.recordId = recordId;
	}

	public String getSqlPart()
	{
		return sqlPart;
	}

	public void setSqlPart(String sqlPart)
	{
		this.sqlPart = sqlPart;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}
}
