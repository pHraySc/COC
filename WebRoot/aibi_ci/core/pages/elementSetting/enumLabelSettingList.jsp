<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
	$(function(){
		var pageNum=$.trim("${pager.pageNum}");
		$(".num").each(function(){
			var txtNum=$.trim($(this).text());
			if(pageNum != ""){
				if(txtNum == pageNum){
					$(this).addClass("num_on");
				}
			}
		});
	});
</script>

<div class="itemChooseContentBox">
<ol class="clearfix" id="itemChooseDetailBox">
	<c:forEach items="${pager.result}" var="map" varStatus="st">
		<li><a href="javascript:void(0);" ondblclick="addEnumItemByDbClick(this);" onclick="aAddOrRemoveClass(this);" id="${map['V_KEY']}" data="${map['V_KEY']}" title="${map['V_NAME']}">${map['V_NAME']}</a></li>
	</c:forEach>
</ol>
</div>
<div class="itemChooseContentSegLine"></div>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_dialog_html.jsp" flush="true" />
</div>
<input type="hidden" name ="totalSize" id ="totalSize" value="${pager.totalSize}">
