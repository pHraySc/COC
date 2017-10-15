// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiSCUserSession.java

package com.ailk.biapp.ci.localization.sichuan.model;

import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserSession;
import java.io.Serializable;
import java.sql.Timestamp;

public class CiSCUserSession
	implements IUserSession, Serializable
{

	private static final long serialVersionUID = 1L;
	private String clientIp;
	private String serverIp;
	private String oaUserId;
	private Timestamp loginTime;
	private String sessionId;
	private IUser user;
	private String groupId;
	private String userID;

	public CiSCUserSession()
	{
	}

	public String getClientIP()
	{
		return clientIp;
	}

	public String getOAUserID()
	{
		return oaUserId;
	}

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
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
		return userID;
	}

	public String getUserName()
	{
		return user.getUsername();
	}

	public Timestamp getLoginTime()
	{
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime)
	{
		this.loginTime = loginTime;
	}

	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

	public void setOaUserId(String oaUserId)
	{
		this.oaUserId = oaUserId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public void setUser(IUser user)
	{
		this.user = user;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}
}
