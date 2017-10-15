// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DispatchServlet.java

package com.ailk.biapp.ci.localization.shanxi.servlet;

import com.asiainfo.biframe.utils.config.Configure;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;

public abstract class DispatchServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	public DispatchServlet()
	{
	}

	public void destroy()
	{
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		String userId = getUserId(request, response);
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(Configure.getInstance().getProperty("bi_sso_ip"), Integer.parseInt(Configure.getInstance().getProperty("bi_sso_port")));
		PostMethod post = new PostMethod(Configure.getInstance().getProperty("bi_sso_register_path"));
		NameValuePair userIdPair = new NameValuePair("userId", userId);
		post.setRequestBody(new NameValuePair[] {
			userIdPair
		});
		client.executeMethod(post);
		String responseStr = new String(post.getResponseBodyAsString().getBytes("GBK"));
		StringBuffer logOutBuf = new StringBuffer();
		if ("true".equals(Configure.getInstance().getProperty("bi_sso_log")))
		{
			logOutBuf.append("uuid get from sso: ").append(responseStr);
			System.out.println(logOutBuf.toString());
		}
		post.releaseConnection();
		String queryUrl = request.getQueryString();
		StringBuffer auSb = new StringBuffer();
		if ("true".equals(Configure.getInstance().getProperty("bi_sso_log")))
		{
			logOutBuf.append("request url: ").append(request.getQueryString());
			System.out.println(logOutBuf);
		}
		if (queryUrl.indexOf("?") > -1 && queryUrl.indexOf("?") != queryUrl.length() && queryUrl.indexOf("&") != queryUrl.length())
			auSb.append("&");
		else
			auSb.append("?");
		auSb.append(responseStr);
		String url = (new StringBuilder()).append(queryUrl).append(auSb.toString()).toString();
		if ("true".equals(Configure.getInstance().getProperty("bi_sso_log")))
		{
			logOutBuf.append("redirect url: ").append(queryUrl).append(auSb.toString());
			System.out.println(logOutBuf);
		}
		response.sendRedirect(url);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doGet(request, response);
	}

	public abstract String getUserId(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse);

	public void init()
		throws ServletException
	{
	}
}
