<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="source" value="${source}"/>
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
	if('${source}'=="1"){
		feedbackIndex();
	}else if('${source}'=="0"){
		feedbackHistoryIndex();
	}
});
function feedbackIndex() {
	$("#feedback").parent().find("li").removeClass("current");
	$("#feedback").addClass("current");
	var _html = $("#feedback").find("a").html();
	$("pathFeedbackText").html(_html);
	$("typeNameText").html("反馈");
	$("#feedbackIndex").empty();
	$("totalSizeNum").parent().show();
	$("#feedbackIndex").load("${ctx}/ci/ciFeedbackAction!index.ai2do");
}

function feedbackHistoryIndex() {
	$("#feedbackHistory").parent().find("li").removeClass("current");
	$("#feedbackHistory").addClass("current");
	var _html = $("#feedbackHistory").find("a").html();
	$("pathFeedbackText").html(_html);
	$("typeNameText").html("反馈历史");
	$("#feedbackIndex").empty();
	$("totalSizeNum").parent().show();
	$("#feedbackIndex").load("${ctx}/ci/feedback/ciFeedbackAction!historyIndex.ai2do");
} 
</script>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
<!-- 分类开始 -->    
<div class="navBox comWidth" id = "navBox">
	 <div class="fleft navListBox">
	    <ol>
		    <li id="feedback" class="current"><a href="javascript:void(0);" onclick="feedbackIndex();">意见反馈</a></li>
		    <li id="feedbackHistory" class="current"><a href="javascript:void(0);" onclick="feedbackHistoryIndex()">历史反馈</a></li>
		</ol>
	 </div>
  </div>
</div>
<div class="comWidth newMsgCenterBox" id="feedbackIndex"></div>
