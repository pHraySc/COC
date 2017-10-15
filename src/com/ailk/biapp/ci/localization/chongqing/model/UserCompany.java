// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UserCompany.java

package com.ailk.biapp.ci.localization.chongqing.model;

import com.asiainfo.biframe.privilege.IUserCompany;

public class UserCompany
	implements IUserCompany
{

	private Integer deptid;
	private String title;
	private String serviceCode;
	private Integer parentid;
	private String status;

	public UserCompany()
	{
	}

	public void setDeptid(Integer deptid)
	{
		this.deptid = deptid;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setServiceCode(String serviceCode)
	{
		this.serviceCode = serviceCode;
	}

	public void setParentid(Integer parentid)
	{
		this.parentid = parentid;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Integer getDeptid()
	{
		return deptid;
	}

	public Integer getParentid()
	{
		return parentid;
	}

	public String getServiceCode()
	{
		return serviceCode;
	}

	public String getStatus()
	{
		return status;
	}

	public String getTitle()
	{
		return title;
	}
}
