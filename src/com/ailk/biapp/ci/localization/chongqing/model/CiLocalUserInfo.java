// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLocalUserInfo.java

package com.ailk.biapp.ci.localization.chongqing.model;

import com.asiainfo.biframe.privilege.IUser;
import java.io.Serializable;

public class CiLocalUserInfo
	implements Serializable, IUser
{

	private static final long serialVersionUID = 0xbf0f1a47da104fe6L;
	private int userId;
	private String name;
	private String loginName;
	private String passWord;
	private String email;
	private int cityId;
	private String cityName;
	private String duty;
	private int status;
	private int departId;
	private String departName;

	public CiLocalUserInfo()
	{
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String userName)
	{
		name = userName;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public String getPassWord()
	{
		return passWord;
	}

	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getCityId()
	{
		return cityId;
	}

	public void setCityId(int cityId)
	{
		this.cityId = cityId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getDuty()
	{
		return duty;
	}

	public void setDuty(String duty)
	{
		this.duty = duty;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getDepartId()
	{
		return departId;
	}

	public void setDepartId(int departmentId)
	{
		departId = departmentId;
	}

	public String getDepartName()
	{
		return departName;
	}

	public void setDepartName(String departmentName)
	{
		departName = departmentName;
	}

	public String getCityid()
	{
		return String.valueOf(cityId);
	}

	public int getDepartmentid()
	{
		return departId;
	}

	public String getDomainType()
	{
		return null;
	}

	public String getGroupId()
	{
		return null;
	}

	public String getLoginId()
	{
		return loginName;
	}

	public String getMobilePhone()
	{
		return null;
	}

	public String getUserid()
	{
		return loginName;
	}

	public String getUsername()
	{
		return name;
	}
}
