// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiSCUserRoleJDao.java

package com.ailk.biapp.ci.localization.sichuan.dao;

import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiSCUserRoleJDao
{

	public abstract List selectLocalUserInfo(String s)
		throws CIServiceException;

	public abstract boolean verifyUserInfo(String s, String s1)
		throws CIServiceException;

	public abstract boolean isAdmin(String s)
		throws CIServiceException;
}
