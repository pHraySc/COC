// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SSOServlet.java

package com.ailk.biapp.ci.localization.shanxi.servlet;

import com.ailk.biapp.ci.localization.shanxi.utils.DES;
import com.asiainfo.biframe.privilege.base.vo.PrivilegeUserSession;
import javax.servlet.http.*;

// Referenced classes of package com.ailk.biapp.ci.localization.shanxi.servlet:
//			DispatchServlet

public class SSOServlet extends DispatchServlet
{

	private static final long serialVersionUID = 0x735ac8adb0720dfcL;

	public SSOServlet()
	{
	}

	public String getMainAccount(HttpServletRequest request, HttpServletResponse response)
	{
		String mainAccount = "";
		PrivilegeUserSession sessionUser = (PrivilegeUserSession)request.getSession().getAttribute("biplatform_user");
		if (sessionUser != null)
			try
			{
				mainAccount = sessionUser.getOAUserID();
				mainAccount = DES.encrypt(mainAccount);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		return mainAccount;
	}

	public String getUserId(HttpServletRequest request, HttpServletResponse response)
	{
		String userId = "";
		PrivilegeUserSession sessionUser = (PrivilegeUserSession)request.getSession().getAttribute("biplatform_user");
		if (sessionUser != null)
			try
			{
				userId = sessionUser.getUserID();
				userId = DES.encrypt(userId);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		return userId;
	}
}
