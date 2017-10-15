// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiTaskServerInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiTaskServerInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String serverId;
	private String serverName;
	private String serverIp;
	private String serverPort;
	private Integer isExeTask;
	private String wsdlAddr;

	public CiTaskServerInfo()
	{
	}

	public CiTaskServerInfo(String serverId, String serverName, String serverIp, String serverPort, Integer isExeTask, String wsdlAddr)
	{
		this.serverId = serverId;
		this.serverName = serverName;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.isExeTask = isExeTask;
		this.wsdlAddr = wsdlAddr;
	}

	public String getServerId()
	{
		return serverId;
	}

	public void setServerId(String serverId)
	{
		this.serverId = serverId;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public String getServerPort()
	{
		return serverPort;
	}

	public void setServerPort(String serverPort)
	{
		this.serverPort = serverPort;
	}

	public Integer getIsExeTask()
	{
		return isExeTask;
	}

	public void setIsExeTask(Integer isExeTask)
	{
		this.isExeTask = isExeTask;
	}

	public String getWsdlAddr()
	{
		return wsdlAddr;
	}

	public void setWsdlAddr(String wsdlAddr)
	{
		this.wsdlAddr = wsdlAddr;
	}
}
