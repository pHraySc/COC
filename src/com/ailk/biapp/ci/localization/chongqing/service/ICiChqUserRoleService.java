// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICiChqUserRoleService.java

package com.ailk.biapp.ci.localization.chongqing.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiChqUserRoleService
{

	public abstract List getLoginUserInfo(String s)
		throws CIServiceException;

	public abstract boolean verifyLoginUser(String s, int i, String s1)
		throws CIServiceException;

	public abstract boolean verifyIsAdmin(String s)
		throws CIServiceException;
}
