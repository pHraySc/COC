// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserLabelRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserLabelRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private Integer mainLabelId;
	private Integer associLabelId;

	public CiUserLabelRelId()
	{
	}

	public CiUserLabelRelId(String userId, Integer mainLabelId, Integer associLabelId)
	{
		this.userId = userId;
		this.mainLabelId = mainLabelId;
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

	public Integer getMainLabelId()
	{
		return mainLabelId;
	}

	public void setMainLabelId(Integer mainLabelId)
	{
		this.mainLabelId = mainLabelId;
	}

	public Integer getAssociLabelId()
	{
		return associLabelId;
	}

	public void setAssociLabelId(Integer associLabelId)
	{
		this.associLabelId = associLabelId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserLabelRelId))
		{
			return false;
		} else
		{
			CiUserLabelRelId castOther = (CiUserLabelRelId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getMainLabelId() == castOther.getMainLabelId() || getMainLabelId() != null && castOther.getMainLabelId() != null && getMainLabelId().equals(castOther.getMainLabelId())) && (getAssociLabelId() == castOther.getAssociLabelId() || getAssociLabelId() != null && castOther.getAssociLabelId() != null && getAssociLabelId().equals(castOther.getAssociLabelId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getMainLabelId() != null ? getMainLabelId().hashCode() : 0);
		result = 37 * result + (getAssociLabelId() != null ? getAssociLabelId().hashCode() : 0);
		return result;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiUserLabelRelId [associLabelId=").append(associLabelId).append(", mainLabelId=").append(mainLabelId).append(", userId=").append(userId).append("]").toString();
	}
}
