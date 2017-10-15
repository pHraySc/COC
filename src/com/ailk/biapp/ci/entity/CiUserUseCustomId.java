// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserUseCustomId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiUserUseCustomId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String deptId;
	private String customId;
	private Date useTime;

	public CiUserUseCustomId()
	{
	}

	public CiUserUseCustomId(String userId, String deptId, String customId, Date useTime)
	{
		this.userId = userId;
		this.deptId = deptId;
		this.customId = customId;
		this.useTime = useTime;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public String getCustomId()
	{
		return customId;
	}

	public void setCustomId(String customId)
	{
		this.customId = customId;
	}

	public Date getUseTime()
	{
		return useTime;
	}

	public void setUseTime(Date useTime)
	{
		this.useTime = useTime;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserUseCustomId))
		{
			return false;
		} else
		{
			CiUserUseCustomId castOther = (CiUserUseCustomId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getDeptId() == castOther.getDeptId() || getDeptId() != null && castOther.getDeptId() != null && getDeptId().equals(castOther.getDeptId())) && (getCustomId() == castOther.getCustomId() || getCustomId() != null && castOther.getCustomId() != null && getCustomId().equals(castOther.getCustomId())) && (getUseTime() == castOther.getUseTime() || getUseTime() != null && castOther.getUseTime() != null && getUseTime().equals(castOther.getUseTime()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getDeptId() != null ? getDeptId().hashCode() : 0);
		result = 37 * result + (getCustomId() != null ? getCustomId().hashCode() : 0);
		result = 37 * result + (getUseTime() != null ? getUseTime().hashCode() : 0);
		return result;
	}
}
