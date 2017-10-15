// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserCustomContrastId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserCustomContrastId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String customGroupId;
	private String contrastLabelId;

	public CiUserCustomContrastId()
	{
	}

	public CiUserCustomContrastId(String userId, String customGroupId, String contrastLabelId)
	{
		this.userId = userId;
		this.customGroupId = customGroupId;
		this.contrastLabelId = contrastLabelId;
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

	public String getContrastLabelId()
	{
		return contrastLabelId;
	}

	public void setContrastLabelId(String contrastLabelId)
	{
		this.contrastLabelId = contrastLabelId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserCustomContrastId))
		{
			return false;
		} else
		{
			CiUserCustomContrastId castOther = (CiUserCustomContrastId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getCustomGroupId() == castOther.getCustomGroupId() || getCustomGroupId() != null && castOther.getCustomGroupId() != null && getCustomGroupId().equals(castOther.getCustomGroupId())) && (getContrastLabelId() == castOther.getContrastLabelId() || getContrastLabelId() != null && castOther.getContrastLabelId() != null && getContrastLabelId().equals(castOther.getContrastLabelId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getCustomGroupId() != null ? getCustomGroupId().hashCode() : 0);
		result = 37 * result + (getContrastLabelId() != null ? getContrastLabelId().hashCode() : 0);
		return result;
	}
}
