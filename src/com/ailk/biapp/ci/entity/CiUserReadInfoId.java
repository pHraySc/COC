// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserReadInfoId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiUserReadInfoId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String announcementId;

	public CiUserReadInfoId()
	{
	}

	public CiUserReadInfoId(String userId, String announcementId)
	{
		this.userId = userId;
		this.announcementId = announcementId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getAnnouncementId()
	{
		return announcementId;
	}

	public void setAnnouncementId(String announcementId)
	{
		this.announcementId = announcementId;
	}
}
