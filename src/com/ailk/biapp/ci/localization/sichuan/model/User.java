// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   User.java

package com.ailk.biapp.ci.localization.sichuan.model;

import com.asiainfo.biframe.privilege.IUser;

public class User
	implements IUser
{

	private String userid;
	private String username;
	private String cityid;
	private int departmentid;
	private String groupId;
	private String email;
	private String mobilePhone;
	private String loginId;
	private String domainType;

	public User()
	{
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setCityid(String cityid)
	{
		this.cityid = cityid;
	}

	public void setDepartmentid(int departmentid)
	{
		this.departmentid = departmentid;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public void setDomainType(String domainType)
	{
		this.domainType = domainType;
	}

	public String getCityid()
	{
		return cityid;
	}

	public int getDepartmentid()
	{
		return departmentid;
	}

	public String getDomainType()
	{
		return domainType;
	}

	public String getEmail()
	{
		return email;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public String getLoginId()
	{
		return loginId;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public String getUserid()
	{
		return userid;
	}

	public String getUsername()
	{
		return username;
	}
}
