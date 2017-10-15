// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLogUserDetail.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLogUserDetailId

public class CiLogUserDetail
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLogUserDetailId id;
	private String userName;
	private String thirdDeptId;
	private String thirdDeptName;
	private String secondDeptId;
	private String secondDeptName;
	private Integer opTimes;

	public CiLogUserDetail()
	{
	}

	public CiLogUserDetail(CiLogUserDetailId id)
	{
		this.id = id;
	}

	public CiLogUserDetail(CiLogUserDetailId id, String userName, String thirdDeptId, String thirdDeptName, String secondDeptId, String secondDeptName, Integer opTimes)
	{
		this.id = id;
		this.userName = userName;
		this.thirdDeptId = thirdDeptId;
		this.thirdDeptName = thirdDeptName;
		this.secondDeptId = secondDeptId;
		this.secondDeptName = secondDeptName;
		this.opTimes = opTimes;
	}

	public CiLogUserDetailId getId()
	{
		return id;
	}

	public void setId(CiLogUserDetailId id)
	{
		this.id = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
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

	public Integer getOpTimes()
	{
		return opTimes;
	}

	public void setOpTimes(Integer opTimes)
	{
		this.opTimes = opTimes;
	}
}
