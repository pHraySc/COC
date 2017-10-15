// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserUseLabelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiUserUseLabelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String deptId;
	private Integer labelId;
	private Date useTime;

	public CiUserUseLabelId()
	{
	}

	public CiUserUseLabelId(String userId, String deptId, Integer labelId, Date useTime)
	{
		this.userId = userId;
		this.deptId = deptId;
		this.labelId = labelId;
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

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
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
		if (!(other instanceof CiUserUseLabelId))
		{
			return false;
		} else
		{
			CiUserUseLabelId castOther = (CiUserUseLabelId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getDeptId() == castOther.getDeptId() || getDeptId() != null && castOther.getDeptId() != null && getDeptId().equals(castOther.getDeptId())) && (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId())) && (getUseTime() == castOther.getUseTime() || getUseTime() != null && castOther.getUseTime() != null && getUseTime().equals(castOther.getUseTime()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getDeptId() != null ? getDeptId().hashCode() : 0);
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		result = 37 * result + (getUseTime() != null ? getUseTime().hashCode() : 0);
		return result;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiUserUseLabelId [deptId=").append(deptId).append(", labelId=").append(labelId).append(", useTime=").append(useTime).append(", userId=").append(userId).append("]").toString();
	}
}
