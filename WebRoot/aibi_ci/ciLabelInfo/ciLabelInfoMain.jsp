<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%
String labelAnalysisMenu = Configure.getInstance().getProperty("LABEL_ANALYSIS");
String myLabel = Configure.getInstance().getProperty("MY_LABEL");
%>
<div class="submenu">
	<div class="submenu_left" style="float:left"></div>
	<div class="top_menu">
		<ul>
		    <%if("true".equals(myLabel)){ %>
			<li id="createdLabelTitle" style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciLabelInfoAction!init.ai2do" class="no1"><span></span><p>我创建的标签</p></a><div class="bgright">&nbsp;</div></li>
			<%} %>
			<%-- <c:if test="${approver}">
				<li id="approveLabelTitle" style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciLabelInfoApproveAction!init.ai2do" class="no2"><span></span><p>标签审批</p></a><div class="bgright">&nbsp;</div></li>
			</c:if>	 --%>
            <!--	
			<li style="white-space:nowrap;"><a href="${ctx}/aibi_ci/ciLabelAnalysis/labelAnalysisIndex.jsp" class="no4"><span></span><p>标签分析</p></a></li>
            -->
            <%if("true".equals(labelAnalysisMenu)){ %>
            <li id="labelAnalysisTitle" style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciLabelAnalysisAction!initlabelAnalysisIndex.ai2do" class="no21"><span></span><p>标签分析</p></a><div class="bgright">&nbsp;</div></li>
            <%} %>
			<div class="clear"></div>
		</ul>
	</div>
</div>
