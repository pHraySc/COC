<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
%>
<div class="submenu">
	<div class="submenu_left" style="float:left"></div>
	<div class="top_menu">
		<ul>
			<li style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/customersManagerAction!search.ai2do?initOrSearch=init" class="no9"><span></span><p>我的客户群</p></a><div class="bgright">&nbsp;</div></li>
		    <li style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/aibi_ci/template/templateList.jsp" class="no2"><span></span><p>我的模板</p></a><div class="bgright">&nbsp;</div></li>			<%
			  if("true".equals(customerAnalysisMenu)){
			%>
			<li style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/customersAnalysisAction!customersAnalysisIndex.ai2do" class="no4"><span></span><p>客户群分析</p></a><div class="bgright">&nbsp;</div></li>
			<%
			  }
			%>
			<div class="clear"></div>
		</ul>
	</div>
</div>
