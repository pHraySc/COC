<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
	String columnId = request.getParameter("columnId");
%>
<c:set var="columnId" value="<%=columnId%>"></c:set>
<div id="itemChooseListDiv${columnId}">
	<div id="loading" style="display:none;">正在加载，请稍候 ...</div>
	<div class="itemChooseContentBox">
	<ol class="clearfix" id="itemChooseDetailBox${columnId}">
		<c:forEach items="${pager.result}" var="map" varStatus="st">
			<li><a href="javascript:void(0);" ondblclick="addEnumItemByDbClick('${columnId}', this);" onclick="aAddOrRemoveClass(this);" id="${map['V_KEY']}" data="${map['V_KEY']}" title="${map['V_NAME']}">${map['V_NAME']}</a></li>
		</c:forEach>
	</ol>
	</div>
	<div class="itemChooseContentSegLine"></div>
	<div class="pagenum" id="pager">
		<jsp:include page="/aibi_ci/dialog/page4verticallabel.jsp" flush="true">
			<jsp:param value="${columnId}" name="columnId"/>
		</jsp:include>
	</div>
	<input id="totalSize${columnId}" type="hidden" value="${pager.totalSize}">
</div>