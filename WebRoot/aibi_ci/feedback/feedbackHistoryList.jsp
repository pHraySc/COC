<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<title>123意见反馈</title>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#totalSizeNum").html('${pager.totalSize}');
		$(".num").each(function() {
			if ($(this).text() == '${pager.pageNum}') {
				$(this).addClass("num_on");
			}
		});
		$('#sortTable th').click(sortTable);

		//定义限制宽度单元格的默认宽度
		var tableWidth = $(".mainTable").parent("div").width();
		$(".mainTable td div.nowrap").width(tableWidth - 740);
	});
	function showFeedbackDetailHistory(feedbackInfoId) {
		var url = $.ctx+"/ci/ciFeedbackAction!showFeedbackDetail.ai2do?feedbackInfoId="+feedbackInfoId+'&source=0';
		window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
</script>
<!-- area_operation end -->
<!-- area_data start -->
</head>
<body>
<table id="sortTable" width="100%" class="tableStyle" cellpadding="0"
	cellspacing="0">
	<thead>
		<tr>
			<th style="width: 50px;" dataType='num'>序号</th>
			<th dataType='text'>反馈主题</th>
			<th dataType='text'>反馈用户</th>
			<th dataType='text'>反馈类型</th>
			<th dataType='text'>反馈对象</th>
			<th dataType='date'>创建时间</th>
			<th dateType='text'>处理状态</th>
			<th dateType='text'>处理人</th>
		</tr>
	</thead>

	<tbody>
		<c:forEach items="${pager.result}" var="po" varStatus="st">
			<c:choose>
				<c:when test="${st.index % 2 == 0 }">
					<tr class="even">
				</c:when>
				<c:otherwise>
					<tr class="odd">
				</c:otherwise>
			</c:choose>
			<td>${st.index+1}</td>
			<td class="align_left">
				<div class="nowrap">
					<a href="javascript:showFeedbackDetailHistory('${po.feedbackInfoId}');"
						id="link-look"><span style="color:#0081CC">${po.feedbackTitle}</span></a>
				</div>
			</td>
			<td class="center"><div class="nowrap">${po.userName}</div></td>
			<td >
				<c:if test="${po.feedbackType == 1}">标签</c:if>
				<c:if test="${po.feedbackType == 2}">客户群</c:if> 
				<c:if test="${po.feedbackType == 3}">系统功能</c:if> 
			</td>
			<td class="align_left">
				<c:if test="${po.feedbackType == 1}">
					<div class="nowrap">${po.labelName}</div>
				</c:if>
				<c:if test="${po.feedbackType == 2}">
					<div class="nowrap">${po.customGroupName}</div>
				</c:if>
				<c:if test="${po.feedbackType == 3}">
					<div class="nowrap">系统功能</div>
				</c:if>
			</td>
			<td class="center">
				<fmt:formatDate value="${po.feedbackTime}"pattern="yyyy-MM-dd HH:mm:ss" />
			</td>
			<td>
				<c:if test="${po.dealStatusId == 4}">已取消</c:if> 
				<c:if test="${po.dealStatusId == 5}">已关闭</c:if>
			</td>
			<td class="center">
				<div class="nowrap">${po.replyUserName}</div>
			</td>
		</c:forEach>
	</tbody>
</table>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>
<input type="hidden" name="totalSize" id="totalSize"
	value="${pager.totalSize}">
</body>
</html>
