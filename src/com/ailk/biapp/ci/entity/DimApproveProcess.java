// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimApproveProcess.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimApproveProcess
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String processId;
	private String processName;
	private Integer status;
	private String resourceTypeId;

	public DimApproveProcess()
	{
	}

	public DimApproveProcess(String processName, Integer status)
	{
		this.processName = processName;
		this.status = status;
	}

	public String getProcessId()
	{
		return processId;
	}

	public String getResourceTypeId()
	{
		return resourceTypeId;
	}

	public void setResourceTypeId(String resourceTypeId)
	{
		this.resourceTypeId = resourceTypeId;
	}

	public void setProcessId(String processId)
	{
		this.processId = processId;
	}

	public String getProcessName()
	{
		return processName;
	}

	public void setProcessName(String processName)
	{
		this.processName = processName;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
