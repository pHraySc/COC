<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.ailk.biapp.ci.util.PrivilegeServiceUtil" %>
<%@ page import="com.asiainfo.biframe.privilege.IUserSession" %>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ page import="java.util.List"%>
<%@ page import="java.sql.Timestamp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>CAS适配</title>
</head>
<body>
<%
	IUserSession mysession = (IUserSession) request.getSession().getAttribute(IUserSession.ASIA_SESSION_NAME);
	try {
        String userId = mysession.getUserID();
        PrivilegeServiceUtil.setCityIdSession(request.getSession(), userId);
        PrivilegeServiceUtil.removeShopCarSession(request.getSession());
        
        %>
        <!-- 这里需要修改成cas跳转到的页面URL -->
        <script>window.location.href='http://jyfxcoctest.zj.chinamobile.com:8080/ci/ci/ciIndexAction!labelIndex.ai2do?sysFlag=ZJ_JYFX_PORTAL';</script>
        <% 
        
	} catch (Exception e) {
	    e.printStackTrace();
	    %>
        <script>alert('登录错误');</script>
        <% 
	}
%>


</body>
</html>