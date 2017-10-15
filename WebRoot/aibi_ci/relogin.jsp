<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.ailk.biapp.ci.util.PrivilegeServiceUtil"%>
<head>
<title>重新登录</title>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery-1.8.3.js"></script>
<%
session.removeAttribute("edu.yale.its.tp.cas.client.filter.user");
session.removeAttribute("edu.yale.its.tp.cas.client.filter.receipt");
PrivilegeServiceUtil.removeCityIdSession(session);
session.invalidate();


%>
<script type="text/javascript">
window.top.location='${ctx}';
</script>
</head>
<body>
</body>
</html>
