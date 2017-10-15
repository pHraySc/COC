<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<c:set var="DIM_CITY" value="<%=CommonConstants.TABLE_DIM_CITY%>"></c:set>
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
	
	var actionUrl = 'ciLabelFormAnalysisAction!findCityHistoryData.ai2do?'+$("#pageForm").serialize() +'&'+param;
	$("#tablediv").page( {url:actionUrl,objId:'tablediv' });
}

function datatable() {
	$(".mainTable").datatable();
}

</script>
		<table width="100%" class="mainTable" cellpadding="0" cellspacing="0">
			<tr>
				<th>分公司</th>
				<c:if test="${!empty dateList}">
					<c:forEach items="${dateList}" var="dateInfo">
						<th><c:out value="${dateFmt:dateFormat(dateInfo)}" /></th>
					</c:forEach>
				</c:if>
			</tr>
			<c:choose>
				<c:when test="${!empty cityHisModelList}">
					<c:forEach items="${cityHisModelList}" var="cityHisModel" begin="${(pager.pageNum-1)*(pager.pageSize) }" end="${(pager.pageNum)*(pager.pageSize)-1}">
						<tr>
							<td class="align_left">
								<c:if test="${cityHisModel.parentId == 0}">
									<c:out value="${dim:toName(DIM_CITY,cityHisModel.cityId)}"></c:out>
								</c:if>
								<c:if test="${cityHisModel.parentId != 0}">
									&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${dim:toName(DIM_CITY,cityHisModel.cityId)}"></c:out>
								</c:if>
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${cityHisModel.his1}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${cityHisModel.his2}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${cityHisModel.his3}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${cityHisModel.his4}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${cityHisModel.his5}"  pattern="#,###" />
							</td>
							<td class="align_right">
								<fmt:formatNumber type="number" value="${cityHisModel.his6}"  pattern="#,###" />
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