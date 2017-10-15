// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserLabelContrastId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserLabelContrastId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String labelId;
	private String contrastLabelId;

	public CiUserLabelContrastId()
	{
	}

	public CiUserLabelContrastId(String userId, String labelId, String contrastLabelId)
	{
		this.userId = userId;
		this.labelId = labelId;
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

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
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
		if (!(other instanceof CiUserLabelContrastId))
		{
			return false;
		} else
		{
			CiUserLabelContrastId castOther = (CiUserLabelContrastId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId())) && (getContrastLabelId() == castOther.getContrastLabelId() || getContrastLabelId() != null && castOther.getContrastLabelId() != null && getContrastLabelId().equals(castOther.getContrastLabelId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		result = 37 * result + (getContrastLabelId() != null ? getContrastLabelId().hashCode() : 0);
		return result;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiUserLabelContrastId [contrastLabelId=").append(contrastLabelId).append(", labelId=").append(labelId).append(", userId=").append(userId).append("]").toString();
	}
}
