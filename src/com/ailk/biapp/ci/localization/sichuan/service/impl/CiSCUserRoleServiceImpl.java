// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiSCUserRoleServiceImpl.java

package com.ailk.biapp.ci.localization.sichuan.service.impl;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.localization.sichuan.dao.ICiSCUserRoleJDao;
import com.ailk.biapp.ci.localization.sichuan.service.ICiSCUserRoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ciSCUserRoleServiceImpl")
public class CiSCUserRoleServiceImpl
	implements ICiSCUserRoleService
{

	@Autowired
	private ICiSCUserRoleJDao ciSCUserRoleJDao;
	private Logger log;

	public CiSCUserRoleServiceImpl()
	{
		log = Logger.getLogger(getClass());
	}

	public List getLoginUserInfo(String loginNo)
		throws CIServiceException
	{
		List userInfoList = null;
		try
		{
			userInfoList = ciSCUserRoleJDao.selectLocalUserInfo(loginNo);
		}
		catch (Exception e)
		{
			String error = "查询本地用户信息错误";
			log.error(error, e);
			throw new CIServiceException(error);
		}
		return userInfoList;
	}

	public boolean verifyIsAdmin(String loginNo)
		throws CIServiceException
	{
		return ciSCUserRoleJDao.isAdmin(loginNo);
	}

	public boolean verifyLoginUser(String loginNo, String password)
		throws CIServiceException
	{
		boolean result = false;
		try
		{
			result = ciSCUserRoleJDao.verifyUserInfo(loginNo, password);
		}
		catch (Exception e)
		{
			String error = "验证用户信息错误";
			log.error(error, e);
			throw new CIServiceException(error);
		}
		return result;
	}
}
