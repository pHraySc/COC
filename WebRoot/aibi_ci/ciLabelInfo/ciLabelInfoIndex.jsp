<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String root = Configure.getInstance().getProperty("CENTER_CITYID");
String cityId = session.getAttribute("cocUserCityId").toString();
String enumClassConfig = Configure.getInstance().getProperty("ENUM_CLASS_CONFIG");
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="cityId" value="<%=cityId%>"></c:set>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
	<script type="text/javascript">
	function labelInfoMyself(){
		$("#labelInfoMyself").parent().find("li").removeClass("current");
		$("#labelInfoMyself").addClass("current");
		var _html = $("#labelInfoMyself").find("a").html();
		$("#pathLabelText").html(_html);
		$("#typeNameText").html("标签");
		$("#labelManagerList").empty();
		$("#labelManagerList").load("${ctx}/ci/ciLabelInfoAction!init.ai2do");
	}
	function ciLabelInfoApprove(){
		$("#ciLabelInfoApprove").parent().find("li").removeClass("current");
		$("#ciLabelInfoApprove").addClass("current");
		var _html = $("#ciLabelInfoApprove").find("a").html();
		$("#pathLabelText").html(_html);
		$("#typeNameText").html("标签");
		$("#labelManagerList").empty();
		$("#labelManagerList").load("${ctx}/ci/ciLabelInfoApproveAction!init.ai2do");
	}
	function ciEnumCategoryInfo(){
		$("#ciEnumCategoryInfo").parent().find("li").removeClass("current");
		$("#ciEnumCategoryInfo").addClass("current");
		var _html = $("#ciEnumCategoryInfo").find("a").html();
		$("#pathLabelText").html(_html);
		$("#typeNameText").html("枚举标签");
		$("#labelManagerList").empty();
		$("#totalSizeNum").parent().show();
		$("#labelManagerList").load("${ctx}/ci/ciLabelInfoAction!findVertAndEnumLabelIndex.ai2do");
	}
	function ciLabelInfoConfig(){
		$("#ciLabelInfoConfig").parent().find("li").removeClass("current");
		$("#ciLabelInfoConfig").addClass("current");
		var _html = $("#ciLabelInfoConfig").find("a").html();
		$("#pathLabelText").html(_html);
		$("#typeNameText").html("个标签");
		$("#labelManagerList").empty();
		$("#totalSizeNum").parent().show();
		$("#labelManagerList").load("${ctx}/ci/ciLabelInfoConfigAction!init.ai2do");
	}
	function ciLabelInfoClass(){
		$("#ciLabelInfoClass").parent().find("li").removeClass("current");
		$("#ciLabelInfoClass").addClass("current");
		var _html = $("#ciLabelInfoClass").find("a").html();
		$("#pathLabelText").html(_html);
		$("#typeNameText").html("标签分类");
		$("#labelManagerList").empty();
		$("#totalSizeNum").parent().show();
		$("#labelManagerList").load("${ctx}/ci/ciLabelInfoConfigAction!init.ai2do?pojo.classes=true");
	}
	$(function(){ //document.ready(function(){});
		if('${showType}' == 1){
			labelInfoMyself();
		}
/*if('${showType}' == 2){
			ciLabelInfoApprove();
		} */
		if('${showType}' == 3){
			ciEnumCategoryInfo();
		}
		if('${showType}' == 4){
			ciLabelInfoConfig();
		}
		if('${showType}' == 5){
			ciLabelInfoClass();
		}
	});
	</script>
<div class="navBox comWidth" id="navBox">
	 <div class="fleft navListBox">
			<ol>
				<c:choose>
			  	<c:when test="${!isAdmin || cityId != root}">
					<li id="labelInfoMyself" class="current"><a href="javascript:void(0);" onclick="labelInfoMyself();">我创建的标签</a></li>
			  	</c:when>
			  	<%-- </c:choose>
				<li id="ciLabelInfoApprove"><a href="javascript:void(0);" onclick="ciLabelInfoApprove();">标签审批</a></li>
				<c:choose> --%>
		  			<c:when test="${isAdmin}">
		  				<% if("true".equals(enumClassConfig)){%>
			  			<li id="ciEnumCategoryInfo"><a href="javascript:void(0);" onclick="ciEnumCategoryInfo();">枚举分类配置</a></li>
			  			<%} %>
		  			</c:when>
		  		</c:choose>
				<c:if test="${isAdmin  && cityId == root}">
					<li id="ciLabelInfoConfig"><a href="javascript:void(0);" onclick="ciLabelInfoConfig();">标签配置</a></li>
					<li id="ciLabelInfoClass"><a href="javascript:void(0);" onclick="ciLabelInfoClass();">标签分类配置</a></li>
				</c:if>
			</ol>
	 </div>
</div>
<div class="comWidth pathNavBox pathNavTagBox">
	<span class="pathNavHome"  style="cursor:default;"> 标签管理</span>
	<span class="pathNavArrowRight"></span>
	<span class="pathNavNext" id="pathLabelText"></span>
	<span class="amountItem"> 共<span class="amountNum" id="totalSizeNum"></span><span id="typeNameText">个标签</span></span>
</div>
<div class="comWidth newMsgCenterBox" id="labelManagerList"></div>
