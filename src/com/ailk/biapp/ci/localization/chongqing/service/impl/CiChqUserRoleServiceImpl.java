// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiChqUserRoleServiceImpl.java

package com.ailk.biapp.ci.localization.chongqing.service.impl;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.chongqing.dao.ICiChqUserRoleJDao;
import com.ailk.biapp.ci.localization.chongqing.service.ICiChqUserRoleService;
import java.util.List;
import org.apache.log4j.Logger;

public class CiChqUserRoleServiceImpl
	implements ICiChqUserRoleService
{

	private Logger log;
	private ICiChqUserRoleJDao ciChqUserRoleJDao;

	public CiChqUserRoleServiceImpl()
	{
		log = Logger.getLogger(getClass());
	}

	public List getLoginUserInfo(String loginName)
		throws CIServiceException
	{
		List userInfoList = null;
		try
		{
			userInfoList = ciChqUserRoleJDao.selectLocalUserInfo(loginName);
		}
		catch (Exception e)
		{
			String error = "查询本地用户信息错误";
			log.error(error, e);
			throw new CIServiceException(error);
		}
		return userInfoList;
	}

	public boolean verifyLoginUser(String login_Name, int flag, String random_code)
		throws CIServiceException
	{
		boolean result = false;
		try
		{
			result = ciChqUserRoleJDao.verifyUserInfo(login_Name, flag, random_code);
		}
		catch (Exception e)
		{
			String error = "验证用户信息错误";
			log.error(error, e);
			throw new CIServiceException(error);
		}
		return result;
	}

	public boolean verifyIsAdmin(String login_Name)
		throws CIServiceException
	{
		boolean result = false;
		try
		{
			result = ciChqUserRoleJDao.isAdmin(login_Name);
		}
		catch (Exception e)
		{
			String error = "查询是否管理员错误";
			log.error(error, e);
			throw new CIServiceException(error);
		}
		return result;
	}
}
