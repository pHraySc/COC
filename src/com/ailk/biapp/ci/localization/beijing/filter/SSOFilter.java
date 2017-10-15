// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SSOFilter.java

package com.ailk.biapp.ci.localization.beijing.filter;

import com.ailk.biapp.ci.localization.beijing.listener.CiSessionListener;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

public class SSOFilter extends CASFilter
{

	private static Logger log = Logger.getLogger(SSOFilter.class);

	public SSOFilter()
	{
	}

	public void login(HttpServletRequest request, HttpServletResponse response, String userId)
		throws Exception
	{
		log.debug((new StringBuilder()).append("-----------------begin ci ssoFilter login userId=").append(userId).toString());
		userId = userId.toLowerCase();
		CiSessionListener csl = CiSessionListener.login(request, userId);
		request.getSession().setAttribute("aibi_component_privilege_sessionlistener", csl);
		request.getSession().setAttribute("biplatform_user", csl);
		request.getSession().setAttribute("ASIA_SESSION_NAME", csl);
		log.debug("-----------------end ci ssoFilter login");
	}

	public boolean islogin(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		HttpSession session = request.getSession();
		log.debug("-----------------begin ci ssoFilter islogin");
		return session.getAttribute("aibi_component_privilege_sessionlistener") != null || session.getAttribute("biplatform_user") != null || session.getAttribute("ASIA_SESSION_NAME") != null;
	}

}
