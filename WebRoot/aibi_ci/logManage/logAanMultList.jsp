<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure"%>
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<script type="text/javascript">
$(document).ready( function() {
	  $(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	 });
	  $('#sortTable th').click(sortTable);
});

</script>
<style type="text/css">
th.select { 
background-image:none; 
background-color:#C4D5D9; 
} 
</style>
<div id="multDeptOptyesLists" >
	<table width="100%" id="sortTable" class="commonTable mainTable" cellpadding="0"
		cellspacing="0" style="word-wrap: break-word; word-break: break-all;">
		<thead>
	    	<tr>
	            <th class="header">
	            	操作时间
	            </th><!--
	            <th class="header" dataType='text'>
	            	操作人<span class="sort">&nbsp;</span>
	            </th>
	            --><th class="header" dataType='text'>
	            	二级部门名称<span class="sort">&nbsp;</span>
	            </th>
	            <th class="header" dataType='text'>
	            	三级部门名称<span class="sort">&nbsp;</span>
	            </th>
	            <c:forEach var="opTypeId" items="${headStrs }">
	            <th class="header" dataType='num'>
	            	${dim:toName(DIM_OP_LOG_TYPE,opTypeId) }<span class="sort">&nbsp;</span>
	            </th>
	            </c:forEach>
	        </tr>
	    </thead>
		<tbody>
		<c:forEach items="${multDeptOpTypesTableList }" var="model" varStatus="st">
			<c:choose>
				<c:when test="${st.index % 2 == 0 }">
					<tr class="even">
				</c:when>
				<c:otherwise>
					<tr class="odd">
				</c:otherwise>
			</c:choose>
				<td class="align_right">
					<c:if test="${empty model.dataDate}">-</c:if>
					${model.dataDate}
				</td>
				<!--<td class="align_left">
					${model.userName}
				</td>
				--><td class="align_left">
					<c:if test="${empty model.secondDeptName}">-</c:if>
					${model.secondDeptName}
				</td>
				<td class="align_left">
					<c:if test="${empty model.thirdDeptName}">-</c:if>
					${model.thirdDeptName}
				</td>
				<c:if test="${fn:length(headStrs) > 0 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes1}">-</c:if>
					${model.opTimes1}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 1 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes2}">-</c:if>
					${model.opTimes2}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 2 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes3}">-</c:if>
					${model.opTimes3}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 3 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes4}">-</c:if>
					${model.opTimes4}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 4 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes5}">-</c:if>
					${model.opTimes5}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 5 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes6}">-</c:if>
					${model.opTimes6}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 6 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes7}">-</c:if>
					${model.opTimes7}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 7 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes8}">-</c:if>
					${model.opTimes8}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 8 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes9}">-</c:if>
					${model.opTimes9}
				</td>
				</c:if>
				<c:if test="${fn:length(headStrs) > 9 }">
				<td class="align_right">
					<c:if test="${empty model.opTimes10}">-</c:if>
					${model.opTimes10}
				</td>
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagenum" id="pager">
		<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
	</div>
</div>