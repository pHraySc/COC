// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiTagInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiTagInfo
	implements Serializable
{

	private static final long serialVersionUID = 0x3681e1218253f67cL;
	private String tagId;
	private String tagName;
	private String isIdentify;
	private Integer tagType;
	private String tagUnit;
	private String typeName;
	private String icon;

	public String getTagId()
	{
		return tagId;
	}

	public void setTagId(String tagId)
	{
		this.tagId = tagId;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public String getIsIdentify()
	{
		return isIdentify;
	}

	public void setIsIdentify(String isIdentify)
	{
		this.isIdentify = isIdentify;
	}

	public Integer getTagType()
	{
		return tagType;
	}

	public void setTagType(Integer tagType)
	{
		this.tagType = tagType;
	}

	public String getTagUnit()
	{
		return tagUnit;
	}

	public void setTagUnit(String tagUnit)
	{
		this.tagUnit = tagUnit;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public CiTagInfo(String tagId, String tagName, String isIdentify, Integer tagType, String tagUnit)
	{
		this.tagId = tagId;
		this.tagName = tagName;
		this.isIdentify = isIdentify;
		this.tagType = tagType;
		this.tagUnit = tagUnit;
	}

	public CiTagInfo()
	{
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiTagInfo [tagId=").append(tagId).append(", tagName=").append(tagName).append(", isIdentify=").append(isIdentify).append(", tagType=").append(tagType).append(", tagUnit=").append(tagUnit).append("]").toString();
	}
}
