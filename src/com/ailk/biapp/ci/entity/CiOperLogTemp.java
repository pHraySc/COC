// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiOperLogTemp.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiOperLogTemp
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String logId;
	private String operateDate;
	private String sessionId;
	private String operatorId;
	private String operatorName;
	private String bassId;
	private String operaterType;
	private String operaterName;
	private String appId;
	private String appName;
	private String resourceId;
	private String extResourceId;
	private String resourceName;
	private String resourceType;
	private String resourcePath;
	private String msg;
	private String operationResult;
	private String logLevel;
	private String tabHeader;
	private String clientAddress;
	private String serverAddress;
	private String threadName;
	private int traffic;
	private String logServerIp;
	private String logServerPort;
	private String thirdDeptId;
	private String thirdDeptName;
	private String secondDeptId;
	private String secondDeptName;

	public CiOperLogTemp()
	{
	}

	public CiOperLogTemp(String logId, String operateDate, String sessionId, String operatorId, String operatorName, String bassId, String operaterType, 
			String operaterName, String appId, String appName, String resourceId, String extResourceId, String resourceName, String resourceType, 
			String resourcePath, String msg, String operationResult, String logLevel, String tabHeader, String clientAddress, String serverAddress, 
			String threadName, int traffic, String logServerIp, String logServerPort, String thirdDeptId, String thirdDeptName, String secondDeptId, 
			String secondDeptName)
	{
		this.logId = logId;
		this.operateDate = operateDate;
		this.sessionId = sessionId;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.bassId = bassId;
		this.operaterType = operaterType;
		this.operaterName = operaterName;
		this.appId = appId;
		this.appName = appName;
		this.resourceId = resourceId;
		this.extResourceId = extResourceId;
		this.resourceName = resourceName;
		this.resourceType = resourceType;
		this.resourcePath = resourcePath;
		this.msg = msg;
		this.operationResult = operationResult;
		this.logLevel = logLevel;
		this.tabHeader = tabHeader;
		this.clientAddress = clientAddress;
		this.serverAddress = serverAddress;
		this.threadName = threadName;
		this.traffic = traffic;
		this.logServerIp = logServerIp;
		this.logServerPort = logServerPort;
		this.thirdDeptId = thirdDeptId;
		this.thirdDeptName = thirdDeptName;
		this.secondDeptId = secondDeptId;
		this.secondDeptName = secondDeptName;
	}

	public String getLogId()
	{
		return logId;
	}

	public void setLogId(String logId)
	{
		this.logId = logId;
	}

	public String getOperateDate()
	{
		return operateDate;
	}

	public void setOperateDate(String operateDate)
	{
		this.operateDate = operateDate;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getOperatorId()
	{
		return operatorId;
	}

	public void setOperatorId(String operatorId)
	{
		this.operatorId = operatorId;
	}

	public String getOperatorName()
	{
		return operatorName;
	}

	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}

	public String getBassId()
	{
		return bassId;
	}

	public void setBassId(String bassId)
	{
		this.bassId = bassId;
	}

	public String getOperaterType()
	{
		return operaterType;
	}

	public void setOperaterType(String operaterType)
	{
		this.operaterType = operaterType;
	}

	public String getOperaterName()
	{
		return operaterName;
	}

	public void setOperaterName(String operaterName)
	{
		this.operaterName = operaterName;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getExtResourceId()
	{
		return extResourceId;
	}

	public void setExtResourceId(String extResourceId)
	{
		this.extResourceId = extResourceId;
	}

	public String getResourceName()
	{
		return resourceName;
	}

	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}

	public String getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}

	public String getResourcePath()
	{
		return resourcePath;
	}

	public void setResourcePath(String resourcePath)
	{
		this.resourcePath = resourcePath;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getOperationResult()
	{
		return operationResult;
	}

	public void setOperationResult(String operationResult)
	{
		this.operationResult = operationResult;
	}

	public String getLogLevel()
	{
		return logLevel;
	}

	public void setLogLevel(String logLevel)
	{
		this.logLevel = logLevel;
	}

	public String getTabHeader()
	{
		return tabHeader;
	}

	public void setTabHeader(String tabHeader)
	{
		this.tabHeader = tabHeader;
	}

	public String getClientAddress()
	{
		return clientAddress;
	}

	public void setClientAddress(String clientAddress)
	{
		this.clientAddress = clientAddress;
	}

	public String getServerAddress()
	{
		return serverAddress;
	}

	public void setServerAddress(String serverAddress)
	{
		this.serverAddress = serverAddress;
	}

	public String getThreadName()
	{
		return threadName;
	}

	public void setThreadName(String threadName)
	{
		this.threadName = threadName;
	}

	public int getTraffic()
	{
		return traffic;
	}

	public void setTraffic(int traffic)
	{
		this.traffic = traffic;
	}

	public String getLogServerIp()
	{
		return logServerIp;
	}

	public void setLogServerIp(String logServerIp)
	{
		this.logServerIp = logServerIp;
	}

	public String getLogServerPort()
	{
		return logServerPort;
	}

	public void setLogServerPort(String logServerPort)
	{
		this.logServerPort = logServerPort;
	}

	public String getThirdDeptId()
	{
		return thirdDeptId;
	}

	public void setThirdDeptId(String thirdDeptId)
	{
		this.thirdDeptId = thirdDeptId;
	}

	public String getThirdDeptName()
	{
		return thirdDeptName;
	}

	public void setThirdDeptName(String thirdDeptName)
	{
		this.thirdDeptName = thirdDeptName;
	}

	public String getSecondDeptId()
	{
		return secondDeptId;
	}

	public void setSecondDeptId(String secondDeptId)
	{
		this.secondDeptId = secondDeptId;
	}

	public String getSecondDeptName()
	{
		return secondDeptName;
	}

	public void setSecondDeptName(String secondDeptName)
	{
		this.secondDeptName = secondDeptName;
	}
}
