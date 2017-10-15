// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiEnumCategoryInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiEnumCategoryInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String enumCategoryId;
	private String enumCategoryName;
	private String descTxt;
	private String userId;
	private String cityId;
	private Integer status;
	private Integer columnId;
	private Integer enumNum;

	public CiEnumCategoryInfo()
	{
	}

	public CiEnumCategoryInfo(String enumCategoryId, String enumCategoryName, String descTxt, String userId, String cityId, Integer status, Integer columnId, 
			Integer enumNum)
	{
		this.enumCategoryId = enumCategoryId;
		this.enumCategoryName = enumCategoryName;
		this.descTxt = descTxt;
		this.userId = userId;
		this.cityId = cityId;
		this.status = status;
		this.columnId = columnId;
		this.enumNum = enumNum;
	}

	public String getEnumCategoryId()
	{
		return enumCategoryId;
	}

	public void setEnumCategoryId(String enumCategoryId)
	{
		this.enumCategoryId = enumCategoryId;
	}

	public String getEnumCategoryName()
	{
		return enumCategoryName;
	}

	public void setEnumCategoryName(String enumCategoryName)
	{
		this.enumCategoryName = enumCategoryName;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getColumnId()
	{
		return columnId;
	}

	public void setColumnId(Integer columnId)
	{
		this.columnId = columnId;
	}

	public Integer getEnumNum()
	{
		return enumNum;
	}

	public void setEnumNum(Integer enumNum)
	{
		this.enumNum = enumNum;
	}
}
