// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiApproveUserInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiApproveUserInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer userInfoId;
	private String approveUserId;
	private String deptId;
	private String approveRoleId;
	private Timestamp addTime;

	public CiApproveUserInfo()
	{
	}

	public CiApproveUserInfo(String approveRoleId, Timestamp addTime)
	{
		this.approveRoleId = approveRoleId;
		this.addTime = addTime;
	}

	public String getApproveUserId()
	{
		return approveUserId;
	}

	public Integer getUserInfoId()
	{
		return userInfoId;
	}

	public void setUserInfoId(Integer userInfoId)
	{
		this.userInfoId = userInfoId;
	}

	public void setApproveUserId(String approveUserId)
	{
		this.approveUserId = approveUserId;
	}

	public String getApproveRoleId()
	{
		return approveRoleId;
	}

	public void setApproveRoleId(String approveRoleId)
	{
		this.approveRoleId = approveRoleId;
	}

	public Timestamp getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Timestamp addTime)
	{
		this.addTime = addTime;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}
}
