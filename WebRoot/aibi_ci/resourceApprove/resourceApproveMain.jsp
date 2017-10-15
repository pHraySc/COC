<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String root = Configure.getInstance().getProperty("CENTER_CITYID");
String cityId = session.getAttribute("cocUserCityId").toString();
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="cityId" value="<%=cityId%>"></c:set>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
	function resourceApproveIndex() {
		$("#resourceApprove").parent().find("li").removeClass("current");
		$("#resourceApprove").addClass("current");
		var _html = $("#resourceApprove").find("a").html();
		$("#resourceApproveIndex").empty();
		$("#resourceApproveIndex").load("${ctx}/ci/ciApproveAction!index.ai2do");
	}

	$(function() {
		if ('${showType}' == 1) {
			resourceApproveIndex();
		}
	});
</script>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
<!-- 分类开始 -->    
<div class="navBox comWidth" id = "navBox">
	 <div class="fleft navListBox">
	    <ol>
	    <li id="resourceApprove" class="current"><a href="javascript:void(0);" onclick="resourceApproveIndex();">资源审批</a></li>
		</ol>
	 </div>
  </div>
</div>
<div id="resourceApproveIndex"></div>
