// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimApproveLevel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimApproveLevel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String approveRoleId;
	private String approveRoleName;
	private Integer approveRoleType;
	private String nextRoleId;
	private String processId;
	private Integer approveLevel;

	public DimApproveLevel()
	{
	}

	public DimApproveLevel(String approveRoleName, Integer approveRoleType, String nextRoleId, String processId, Integer approveLevel)
	{
		this.approveRoleName = approveRoleName;
		this.approveRoleType = approveRoleType;
		this.nextRoleId = nextRoleId;
		this.processId = processId;
		this.approveLevel = approveLevel;
	}

	public String getApproveRoleId()
	{
		return approveRoleId;
	}

	public void setApproveRoleId(String approveRoleId)
	{
		this.approveRoleId = approveRoleId;
	}

	public String getApproveRoleName()
	{
		return approveRoleName;
	}

	public void setApproveRoleName(String approveRoleName)
	{
		this.approveRoleName = approveRoleName;
	}

	public Integer getApproveRoleType()
	{
		return approveRoleType;
	}

	public void setApproveRoleType(Integer approveRoleType)
	{
		this.approveRoleType = approveRoleType;
	}

	public String getNextRoleId()
	{
		return nextRoleId;
	}

	public void setNextRoleId(String nextRoleId)
	{
		this.nextRoleId = nextRoleId;
	}

	public String getProcessId()
	{
		return processId;
	}

	public void setProcessId(String processId)
	{
		this.processId = processId;
	}

	public Integer getApproveLevel()
	{
		return approveLevel;
	}

	public void setApproveLevel(Integer approveLevel)
	{
		this.approveLevel = approveLevel;
	}
}
