// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserAttentionLabelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserAttentionLabelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private Integer labelId;

	public CiUserAttentionLabelId()
	{
	}

	public CiUserAttentionLabelId(String userId, Integer labelId)
	{
		this.userId = userId;
		this.labelId = labelId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserAttentionLabelId))
		{
			return false;
		} else
		{
			CiUserAttentionLabelId castOther = (CiUserAttentionLabelId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		return result;
	}
}
