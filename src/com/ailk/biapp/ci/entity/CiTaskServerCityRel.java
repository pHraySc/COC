// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiTaskServerCityRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiTaskServerCityRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer cityId;
	private String serverId;
	private String databseUrl;
	private String username;
	private String password;

	public CiTaskServerCityRel()
	{
	}

	public CiTaskServerCityRel(Integer cityId, String serverId, String databseUrl, String username, String password)
	{
		this.cityId = cityId;
		this.serverId = serverId;
		this.databseUrl = databseUrl;
		this.username = username;
		this.password = password;
	}

	public Integer getCityId()
	{
		return cityId;
	}

	public void setCityId(Integer cityId)
	{
		this.cityId = cityId;
	}

	public String getServerId()
	{
		return serverId;
	}

	public void setServerId(String serverId)
	{
		this.serverId = serverId;
	}

	public String getDatabseUrl()
	{
		return databseUrl;
	}

	public void setDatabseUrl(String databseUrl)
	{
		this.databseUrl = databseUrl;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
