// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimAnnouncementType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimAnnouncementType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer typeId;
	private String typeName;
	private String typeDesc;
	private Integer hasSuccessFail;

	public DimAnnouncementType()
	{
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public DimAnnouncementType(Integer typeId, String typeName, String typeDesc, Integer hasSuccessFail)
	{
		this.typeId = typeId;
		this.typeName = typeName;
		this.typeDesc = typeDesc;
		this.hasSuccessFail = hasSuccessFail;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getTypeDesc()
	{
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc)
	{
		this.typeDesc = typeDesc;
	}

	public Integer getHasSuccessFail()
	{
		return hasSuccessFail;
	}

	public void setHasSuccessFail(Integer hasSuccessFail)
	{
		this.hasSuccessFail = hasSuccessFail;
	}

	public String toString()
	{
		return (new StringBuilder()).append("DimAnnouncementType [hasSuccessFail=").append(hasSuccessFail).append(", typeDesc=").append(typeDesc).append(", typeId=").append(typeId).append(", typeName=").append(typeName).append("]").toString();
	}
}
