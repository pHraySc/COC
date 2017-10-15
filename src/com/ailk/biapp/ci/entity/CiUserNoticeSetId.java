// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserNoticeSetId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserNoticeSetId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String noticeId;
	private Integer noticeType;
	private Integer isSuccess;
	private String sendModeId;

	public CiUserNoticeSetId()
	{
	}

	public CiUserNoticeSetId(String userId, String noticeId, Integer noticeType, Integer isSuccess, String sendModeId)
	{
		this.userId = userId;
		this.noticeId = noticeId;
		this.noticeType = noticeType;
		this.isSuccess = isSuccess;
		this.sendModeId = sendModeId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getNoticeId()
	{
		return noticeId;
	}

	public void setNoticeId(String noticeId)
	{
		this.noticeId = noticeId;
	}

	public Integer getNoticeType()
	{
		return noticeType;
	}

	public void setNoticeType(Integer noticeType)
	{
		this.noticeType = noticeType;
	}

	public Integer getIsSuccess()
	{
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiUserNoticeSetId))
		{
			return false;
		} else
		{
			CiUserNoticeSetId castOther = (CiUserNoticeSetId)other;
			return (getUserId() == castOther.getUserId() || getUserId() != null && castOther.getUserId() != null && getUserId().equals(castOther.getUserId())) && (getNoticeId() == castOther.getNoticeId() || getNoticeId() != null && castOther.getNoticeId() != null && getNoticeId().equals(castOther.getNoticeId())) && (getNoticeType() == castOther.getNoticeType() || getNoticeType() != null && castOther.getNoticeType() != null && getNoticeType().equals(castOther.getNoticeType())) && (getIsSuccess() == castOther.getIsSuccess() || getIsSuccess() != null && castOther.getIsSuccess() != null && getIsSuccess().equals(castOther.getIsSuccess())) && (getSendModeId() == castOther.getSendModeId() || getSendModeId() != null && castOther.getSendModeId() != null && getSendModeId().equals(castOther.getSendModeId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 37 * result + (getNoticeId() != null ? getNoticeId().hashCode() : 0);
		result = 37 * result + (getNoticeType() != null ? getNoticeType().hashCode() : 0);
		result = 37 * result + (getIsSuccess() != null ? getIsSuccess().hashCode() : 0);
		return result;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiUserNoticeSetId [isSuccess=").append(isSuccess).append(", noticeId=").append(noticeId).append(", noticeType=").append(noticeType).append(", userId=").append(userId).append("]").toString();
	}

	public String getSendModeId()
	{
		return sendModeId;
	}

	public void setSendModeId(String sendModeId)
	{
		this.sendModeId = sendModeId;
	}
}
