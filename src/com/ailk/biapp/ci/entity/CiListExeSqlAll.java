// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiListExeSqlAll.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiListExeSqlAll
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer primaryId;
	private String exeInfoId;
	private String sqlPart;
	private Integer sortNum;

	public CiListExeSqlAll()
	{
	}

	public CiListExeSqlAll(Integer primaryId, String exeInfoId, String sqlPart, Integer sortNum)
	{
		this.primaryId = primaryId;
		this.exeInfoId = exeInfoId;
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

	public String getExeInfoId()
	{
		return exeInfoId;
	}

	public void setExeInfoId(String exeInfoId)
	{
		this.exeInfoId = exeInfoId;
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

	public String toString()
	{
		return (new StringBuilder()).append("CiListExeSqlAll [primaryId=").append(primaryId).append(", exeInfoId=").append(exeInfoId).append(", sqlPart=").append(sqlPart).append(", sortNum=").append(sortNum).append("]").toString();
	}
}
