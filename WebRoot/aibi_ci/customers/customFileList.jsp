<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript"
	src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<%
		String isCustomVertAttr = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
%>
<c:set var="isCustomVertAttr" value="<%=isCustomVertAttr %>"></c:set>
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

	//生成客户群文件
	function createCustomersFile(listTableName){
		if($("#downFile").html() == "生成中"){
			return false;
		}
		var actionUrl = $.ctx + "/ci/customersFileAction!createCustomersFile.ai2do?listTableName="+listTableName;
		$.ajax({
			type: "POST",
			url: actionUrl,
			success: function(result){
				if(result.success){
					var dlgObj = commonUtil.create_alert_dialog("showAlertDialog", {
						 txt:result.msg,
						 type:"success",
						 width:500,
						 height:200,
						 param:true
					}) ; 
					$("#downFile").html("生成中");
				}else{
					var dlgObj = commonUtil.create_alert_dialog("showAlertDialog", {
						 txt:result.msg,
						 type:"failed",
						 width:500,
						 height:200,
						 param:true
					}) ; 
				}
			}
		});
	}
</script>
<!-- area_operation end -->
<!-- area_data start -->
<div id="declareList" class="tableStyleBox">
	<table id="sortTable" width="100%" class="tableStyle" cellpadding="0"
		cellspacing="0">
		<thead>
			<tr>
				<th width="85" class="header" dataType='text'>
					编号<span class="sort">&nbsp;</span>
				</th>
				<th width="200" class="header" dataType='text'>客户群清单<span class="sort">&nbsp;</span></th>
				<th width="60" class="header" dataType='text'>生成周期<span class="sort">&nbsp;</span></th>
				<th width="60" class="header" dataType='num'>用户数<span class="sort">&nbsp;</span></th>
				<c:if test="${isCustomVertAttr == 'true' }">
				<th width="60" class="header" dataType='num'>
					重复用户数<span class="sort">&nbsp;</span>
				</th>
				</c:if>
				<th width="80" class="header" dataType='text'>清单生成时间<span class="sort">&nbsp;</span></th>
				<th width="90">操作</th>
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
				<td class="align_left" style="text-indent: 7px"> ${po.customGroupId }
			    </td>
				<td class="align_left">
						${po.customGroupName}-${po.dataDate}清单
				</td>
				<td class="align_center">
					<c:if test="${po.updateCycle == 1}">
						<div class="nowrap">一次性</div>
					</c:if>
					<c:if test="${po.updateCycle == 2}">
						<div class="nowrap">月周期</div>
					</c:if>
					<c:if test="${po.updateCycle == 3}">
						<div class="nowrap">日周期</div>
					</c:if>
				</td>
				<td class="align_right">
					<c:choose>
					<c:when test="${po.customNum == null}">-</c:when>
					<c:otherwise>
						<fmt:formatNumber value="${po.customNum}" type="number" pattern="###,###,###"/>
					</c:otherwise>
				</c:choose>
				</td>
				<c:if test="${isCustomVertAttr == 'true' }">
					<td class="align_right" style="white-space:nowrap">
						<c:choose>
							<c:when test="${po.duplicateNum == null || po.duplicateNum == 0}">-</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${po.duplicateNum}" type="number" pattern="###,###,###"/>
							</c:otherwise>
						</c:choose>
					</td>
				</c:if>
				<td class="align_right">${po.dataTimeStr}</td>
				<td class="main_button_title">
					<a class="dsts" class="icon_hide" href="$.ctx + '/ci/customersFileAction!downCustomersFile.ai2do?listTableName=${po.listTableName}" >下载</a>
				</td>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagenum" id="pager">
		<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
	</div>
	<input type="hidden" name="totalSize" id="totalSize"
		value="${pager.totalSize}">
</div>


