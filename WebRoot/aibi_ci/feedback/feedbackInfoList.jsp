<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script>
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
	function showAlert(){
		commonUtil.create_alert_dialog("showCancelAlertDialog", {
			 "txt":"确定取消反馈？",
			 "type":"success",
			 "width":500,
			 "height":200,
			 "param":feedbackInfoId
		});
	}
</script>
<!-- area_operation end -->
<!-- area_data start -->
<style type="text/css"></style>
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
			<th class="noborder">操作</th>
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
					<a href="javascript:showFeedbackDetail('${po.feedbackInfoId}');"
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
				<c:if test="${po.dealStatusId == 1}">未处理</c:if>
				<c:if test="${po.dealStatusId == 2}">处理中</c:if> 
				<c:if test="${po.dealStatusId == 3}">已处理</c:if> 
				<c:if test="${po.dealStatusId == 4}">已取消</c:if> 
				<c:if test="${po.dealStatusId == 5}">已关闭</c:if>
			</td>
			<td class="center">
				<div class="nowrap">${po.replyUserName}</div>
			</td>
			<td class="align_left">
				<div class="resultItemActive marginAuto " onclick="showOperFeedbackList(event,this,'${po.feedbackInfoId}')" ><span class="dsts">操作</span>
					<div class="resultItemActiveList" id="${po.feedbackInfoId}">
						<ol>
							<c:if test="${canManageDeclare == false && po.isCreateUser == true}">
								<c:if test="${po.dealStatusId == 1}">
									<li><a href="javascript:void(0)" onclick="feedbackReply(${po.feedbackInfoId})">回复</a></li>
									<li><a href="javascript:void(0)" onclick="feedbackCancel(${po.feedbackInfoId})">取消反馈</a></li>
								</c:if>
								<c:if test="${po.dealStatusId == 3}">
									<li><a href="javascript:void(0)" onclick="feedbackFallback(${po.feedbackInfoId})">回退</a></li>
									<li><a href="javascript:void(0)" onclick="feedbackClose(${po.feedbackInfoId})">关闭反馈</a></li>
								</c:if>
							</c:if>
							<c:if test="${canManageDeclare == true || po.isCreateUser == false}">
								<li><a href="javascript:void(0)" onclick="feedbackReply(${po.feedbackInfoId})">回复</a></li>
								<c:if test="${po.dealStatusId == 1}">
									<li><a href="javascript:void(0)" onclick="feedabackDo(${po.feedbackInfoId})">开始处理</a></li>
								</c:if>
								<c:if test="${po.dealStatusId == 2}">
									<li><a href="javascript:void(0)" onclick="feedbackOver(${po.feedbackInfoId})">处理完成</a></li>
								</c:if>
							</c:if>
						</ol>
					</div>
				</div>
			</td>
		</c:forEach>
	</tbody>
</table>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>
<input type="hidden" name="totalSize" id="totalSize"
	value="${pager.totalSize}">


