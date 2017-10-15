// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimApproveStatus.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimApproveStatus
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String approveStatusId;
	private String approveRoleId;
	private String approveStatusName;
	private String approveStatusDesc;
	private Long sortNum;
	private String processId;

	public DimApproveStatus()
	{
	}

	public DimApproveStatus(String approveRoleId, String approveStatusName, String approveStatusDesc, Long sortNum)
	{
		this.approveRoleId = approveRoleId;
		this.approveStatusName = approveStatusName;
		this.approveStatusDesc = approveStatusDesc;
		this.sortNum = sortNum;
	}

	public String getApproveStatusId()
	{
		return approveStatusId;
	}

	public void setApproveStatusId(String approveStatusId)
	{
		this.approveStatusId = approveStatusId;
	}

	public String getApproveRoleId()
	{
		return approveRoleId;
	}

	public void setApproveRoleId(String approveRoleId)
	{
		this.approveRoleId = approveRoleId;
	}

	public String getApproveStatusName()
	{
		return approveStatusName;
	}

	public void setApproveStatusName(String approveStatusName)
	{
		this.approveStatusName = approveStatusName;
	}

	public String getApproveStatusDesc()
	{
		return approveStatusDesc;
	}

	public void setApproveStatusDesc(String approveStatusDesc)
	{
		this.approveStatusDesc = approveStatusDesc;
	}

	public Long getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Long sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getProcessId()
	{
		return processId;
	}

	public void setProcessId(String processId)
	{
		this.processId = processId;
	}
}
