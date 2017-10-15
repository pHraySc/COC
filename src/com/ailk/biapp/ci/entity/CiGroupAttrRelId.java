// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiGroupAttrRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiGroupAttrRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String customGroupId;
	private String attrCol;
	private Date modifyTime;

	public CiGroupAttrRelId()
	{
	}

	public CiGroupAttrRelId(String customGroupId, String attrCol, Date modifyTime)
	{
		this.customGroupId = customGroupId;
		this.attrCol = attrCol;
		this.modifyTime = modifyTime;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getAttrCol()
	{
		return attrCol;
	}

	public void setAttrCol(String attrCol)
	{
		this.attrCol = attrCol;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiGroupAttrRelId))
		{
			return false;
		} else
		{
			CiGroupAttrRelId castOther = (CiGroupAttrRelId)other;
			return (getCustomGroupId() == castOther.getCustomGroupId() || getCustomGroupId() != null && castOther.getCustomGroupId() != null && getCustomGroupId().equals(castOther.getCustomGroupId())) && (getAttrCol() == castOther.getAttrCol() || getAttrCol() != null && castOther.getAttrCol() != null && getAttrCol().equals(castOther.getAttrCol())) && (getModifyTime() == castOther.getModifyTime() || getModifyTime() != null && castOther.getModifyTime() != null && getModifyTime().equals(castOther.getModifyTime()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getCustomGroupId() != null ? getCustomGroupId().hashCode() : 0);
		result = 37 * result + (getAttrCol() != null ? getAttrCol().hashCode() : 0);
		return result;
	}
}
