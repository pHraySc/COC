<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<c:set var="DIM_VIP_LEVEL" value="<%=CommonConstants.TABLE_DIM_VIP_LEVEL%>"></c:set>
<script type="text/javascript" language="javascript">

$(document).ready( function() {
	  $(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	 });
	pagetable();
});
function pagetable(){
	var param = 'ciLabelFormModel.labelId='+labelId;
		param += '&ciLabelFormModel.dataDate='+dataDate;
		param += '&ciLabelFormModel.updateCycle='+updateCycle;
	
	var actionUrl = 'ciLabelFormAnalysisAction!findVipHistoryData.ai2do?'+$("#pageForm").serialize() +'&'+param;
	$("#tablediv").page( {url:actionUrl,objId:'tablediv' });
}

function datatable() {
	$(".mainTable").datatable();
}

</script>
		<table width="100%" class="mainTable" cellpadding="0" cellspacing="0">
			<tr>
				<th>VIP等级</th>
				<c:if test="${!empty dateList}">
					<c:forEach items="${dateList}" var="dateInfo">
						<th><c:out value="${dateFmt:dateFormat(dateInfo)}" /></th>
					</c:forEach>
				</c:if>
			</tr>
			<c:choose>
				<c:when test="${!empty vipHisModelList}">
					<c:forEach items="${vipHisModelList}" var="vipHisModel" begin="${(pager.pageNum-1)*(pager.pageSize) }" end="${(pager.pageNum)*(pager.pageSize)-1}">
						<tr>
							<td class="align_left">
								<c:out value="${dim:toName(DIM_VIP_LEVEL,vipHisModel.vipLevelId)}"></c:out>
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${vipHisModel.his1}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${vipHisModel.his2}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${vipHisModel.his3}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${vipHisModel.his4}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${vipHisModel.his5}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${vipHisModel.his6}"  pattern="#,###" />
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="99" class="wushuju">${noDataToDisplay}</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</table>
	<div class="pagenum" id="pager">
	 <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
	</div>