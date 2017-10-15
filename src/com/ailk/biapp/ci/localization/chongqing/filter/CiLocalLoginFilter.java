// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLocalLoginFilter.java

package com.ailk.biapp.ci.localization.chongqing.filter;

import com.ailk.biapp.ci.localization.chongqing.model.CiChongqingUserSession;
import com.ailk.biapp.ci.localization.chongqing.service.ICiChqUserRoleService;
import com.asiainfo.biframe.exception.NotLoginException;
import com.asiainfo.biframe.log.LogInfo;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.LogRecord;
import javax.servlet.*;
import javax.servlet.http.*;

public class CiLocalLoginFilter
	implements Filter
{

	public CiLocalLoginFilter()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		HttpServletRequest hrequest = (HttpServletRequest)request;
		HttpServletResponse hresponse = (HttpServletResponse)response;
		IUserSession iUserSession = (IUserSession)hrequest.getSession().getAttribute("biplatform_user");
		String loginName = request.getParameter("su");
		String enterflag = request.getParameter("flag");
		String random_code = request.getParameter("random");
		if (null != loginName && null != enterflag)
			try
			{
				ICiChqUserRoleService ciChqUserRoleService = (ICiChqUserRoleService)SystemServiceLocator.getInstance().getService("ciChqUserRoleServiceImpl");
				boolean flag = true;
				if (!"true".equals(Configure.getInstance().getProperty("DEBUG")))
					flag = ciChqUserRoleService.verifyLoginUser(loginName, Integer.parseInt(enterflag), random_code);
				if (flag)
				{
					List userInfoList = ciChqUserRoleService.getLoginUserInfo(loginName);
					if (userInfoList != null && userInfoList.size() > 0)
					{
						CiChongqingUserSession userSession = new CiChongqingUserSession();
						userSession.setClientIp(hrequest.getRemoteAddr());
						userSession.setGroupId(null);
						userSession.setLoginTime(new Timestamp(System.currentTimeMillis()));
						userSession.setOaUserId(null);
						userSession.setSessionId(hrequest.getSession().getId());
						userSession.setServerIp(Configure.getInstance().getProperty("HOST_ADDRESS"));
						userSession.setUser((IUser)userInfoList.get(0));
						hrequest.getSession().setAttribute("biplatform_user", userSession);
						boolean isAdminUser = false;
						isAdminUser = ciChqUserRoleService.verifyIsAdmin(loginName);
						hrequest.getSession().setAttribute((new StringBuilder()).append("isAdminUser_").append(loginName).toString(), Boolean.valueOf(isAdminUser));
						LogInfo.setClientAddress(userSession.getClientIP());
						LogInfo.setOperatorID(userSession.getUserID());
						LogInfo.setOperatorName(userSession.getUserName());
						LogInfo.setSessionID(userSession.getSessionID());
						LogInfo.setHostAddress(userSession.getServerIp());
						chain.doFilter(hrequest, hresponse);
					} else
					{
						throw new NotLoginException();
					}
				} else
				{
					throw new NotLoginException();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		else
		if (iUserSession != null)
		{
			LogInfo.setClientAddress(iUserSession.getClientIP());
			LogInfo.setOperatorID(iUserSession.getUserID());
			LogInfo.setOperatorName(iUserSession.getUserName());
			LogInfo.setSessionID(iUserSession.getSessionID());
			LogInfo.setHostAddress(Configure.getInstance().getProperty("HOST_ADDRESS"));
			chain.doFilter(hrequest, hresponse);
		} else
		{
			throw new NotLoginException();
		}
	}

	public boolean isLoggable(LogRecord record)
	{
		return false;
	}

	public void destroy()
	{
	}

	public void init(FilterConfig filterconfig)
		throws ServletException
	{
	}
}
