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
$(document).ready(function(){
	customFileList();
});
function customFileList() {
	$("#customFileList").parent().find("li").removeClass("current");
	$("#customFileList").addClass("current");
	var _html = $("#customFileList").find("a").html();
	$("#customFileListIndex").empty();
	$("#customFileListIndex").load("${ctx}/ci/customersFileAction!index.ai2do");
}
</script>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
<!-- 分类开始 -->    
<div class="navBox comWidth" id = "navBox">
	 <div class="fleft navListBox">
	    <ol>
	    <li id="customFileList" class="current"><a id="customFileList" href="javascript:void(0);" onclick="customFileList();">清单下载</a></li>
		</ol>
	 </div>
  </div>
</div>
<div id="customFileListIndex"></div>
