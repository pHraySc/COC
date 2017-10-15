// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserCustomRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserCustomRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String customGroupId;
	private String associLabelId;

	public CiUserCustomRelId()
	{
	}

	public CiUserCustomRelId(String userId, String customGroupId, String associLabelId)
	{
		this.userId = userId;
		this.customGroupId = customGroupId;
		this.associLabelId = associLabelId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getAssociLabelId()
	{
		return associLabelId;
	}

	public void setAssociLabelId(String associLabelId)
	{
		this.associLabelId = associLabelId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserCustomRelId))
		{
			return false;
		} else
		{
			CiUserCustomRelId castOther = (CiUserCustomRelId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getCustomGroupId() == castOther.getCustomGroupId() || getCustomGroupId() != null && castOther.getCustomGroupId() != null && getCustomGroupId().equals(castOther.getCustomGroupId())) && (getAssociLabelId() == castOther.getAssociLabelId() || getAssociLabelId() != null && castOther.getAssociLabelId() != null && getAssociLabelId().equals(castOther.getAssociLabelId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getCustomGroupId() != null ? getCustomGroupId().hashCode() : 0);
		result = 37 * result + (getAssociLabelId() != null ? getAssociLabelId().hashCode() : 0);
		return result;
	}
}
