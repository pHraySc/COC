// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiSessionListener.java

package com.ailk.biapp.ci.localization.beijing.listener;

import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserSession;
import java.io.Serializable;

class CiUserSession
	implements IUserSession, Serializable
{

	private static final long serialVersionUID = 0x9b845c2e44101382L;
	private String clientIp;
	private String oaUserId;
	private String sessionId;
	private IUser user;
	private String groupId;

	public CiUserSession(String clientIp, String oaUserId, String sessionId, IUser user, String groupId)
	{
		this.clientIp = clientIp;
		this.oaUserId = oaUserId;
		this.sessionId = sessionId;
		this.user = user;
		this.groupId = groupId;
	}

	public String getClientIP()
	{
		return clientIp;
	}

	public String getOAUserID()
	{
		return oaUserId;
	}

	public String getSessionID()
	{
		return sessionId;
	}

	public IUser getUser()
	{
		return user;
	}

	public String getUserCityID()
	{
		return user.getCityid();
	}

	public String getUserID()
	{
		return user.getUserid();
	}

	public String getUserName()
	{
		return user.getUsername();
	}

	public String getGroupId()
	{
		return groupId;
	}
}
