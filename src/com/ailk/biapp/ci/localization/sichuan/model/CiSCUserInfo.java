// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiSCUserInfo.java

package com.ailk.biapp.ci.localization.sichuan.model;

import com.asiainfo.biframe.privilege.IUser;
import java.io.Serializable;

public class CiSCUserInfo
	implements Serializable, IUser
{

	private static final long serialVersionUID = 1L;
	private String loginNo;
	private String userName;
	private String groupId;
	private String cityId;
	private String groupName;
	private String countyId;
	private String sectionId;
	private String deptId;
	private String departmentName;
	private int userStatus;
	private String mobilePhone;
	private String email;
	private String password;
	private String userType;

	public CiSCUserInfo()
	{
	}

	public String getLoginNo()
	{
		return loginNo;
	}

	public void setLoginNo(String loginNo)
	{
		this.loginNo = loginNo;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getCountyId()
	{
		return countyId;
	}

	public void setCountyId(String countyId)
	{
		this.countyId = countyId;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getDepartmentName()
	{
		return departmentName;
	}

	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}

	public int getUserStatus()
	{
		return userStatus;
	}

	public void setUserStatus(int userStatus)
	{
		this.userStatus = userStatus;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCityid()
	{
		return cityId;
	}

	public int getDepartmentid()
	{
		return Integer.valueOf(deptId).intValue();
	}

	public String getDomainType()
	{
		return null;
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
		return loginNo;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public String getUserid()
	{
		return loginNo;
	}

	public String getUsername()
	{
		return userName;
	}
}
