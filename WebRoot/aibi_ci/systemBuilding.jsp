<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>系统正在建设中</title> 
</head>
<body class="md-waiting">
	<jsp:include page="/aibi_ci/navigation.jsp"></jsp:include>
  	<div class="goto-Index">
  		<a class="goto-Index-btn" href="${ctx}/ci/ciMarketAction!newMarketIndex.ai2do"></a>
  	</div>
</body>
</html>