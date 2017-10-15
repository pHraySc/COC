<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.ailk.biapp.ci.localization.zhejiang.mode.ZjUserInfo" %>
<%@ page import="com.ailk.biapp.ci.localization.zhejiang.mode.ZjUserSession" %>
<%@ page import="com.ailk.biapp.ci.localization.zhejiang.service.IZjUserService" %>
<%@ page import="com.ailk.biapp.ci.util.PrivilegeServiceUtil" %>
<%@ page import="com.asiainfo.biframe.privilege.IUserSession" %>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ page import="com.asiainfo.biframe.utils.spring.SystemServiceLocator"%>
<%@ page import="com.asiainfo.biframe.log.LogInfo"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.ailk.biapp.ci.util.CILogServiceUtil"%>
<%@ page import="java.util.List"%>
<%@ page import="java.sql.Timestamp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Super</title>
</head>
<body>
<%
	String userid = request.getParameter("userid");
	try {
	    IZjUserService zjUserService = (IZjUserService) SystemServiceLocator.getInstance().getService("zjUserServiceImpl");
	    List<ZjUserInfo> userInfoList = zjUserService.getLoginUserInfo(userid);
	    if (null != userInfoList && userInfoList.size() > 0) {
	        ZjUserInfo zjUserInfo = userInfoList.get(0);
	        ZjUserSession zjUserSession = new ZjUserSession();
	        zjUserSession.setClientIp(request.getRemoteAddr());
	        zjUserSession.setGroupId(null);
	        zjUserSession.setLoginTime(new Timestamp(System.currentTimeMillis()));
	        zjUserSession.setOaUserId(null);
	        zjUserSession.setSessionId(request.getSession().getId());
	        zjUserSession.setServerIp(Configure.getInstance().getProperty("HOST_ADDRESS"));
	        zjUserSession.setUser(zjUserInfo);
	        zjUserSession.setUserID(zjUserInfo.getUserid());
	        request.getSession().setAttribute(IUserSession.ASIA_SESSION_NAME, zjUserSession);
	        PrivilegeServiceUtil.setCityIdSession(request.getSession(), zjUserInfo.getUserid());
	        PrivilegeServiceUtil.removeShopCarSession(request.getSession());
	        String sysFlag = request.getParameter("sysFlag");
	        request.getSession().setAttribute("SYS_FLAG", sysFlag);
	        request.getSession().setAttribute("SUPER_LOGIN", "1");
	        request.getSession().setAttribute("ssoStatus", "false");
	        
	     	// 登录系统时记录日志
			CILogServiceUtil.getLogServiceInstance().loginLog(zjUserSession.getSessionID(), 
					zjUserSession.getUserID(), zjUserSession.getUserName(), zjUserSession.getClientIP(), sysFlag, request);
	        
	        %>
	        <script>window.location.href='http://jyfxcoctest.zj.chinamobile.com:8080/coczj/ci/ciIndexAction!labelIndex.ai2do?sysFlag=ZJ_JYFX_PORTAL';</script>
	        <% 
	        }else{//如果没有本地经分帐号
	        	%>
		        <script>alert('登录错误');</script>
		        <% 
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    %>
        <script>alert('登录错误');</script>
        <% 
	}
%>


</body>
</html>