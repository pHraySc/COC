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
<div id="contentDiv" >
	<table width="100%" id="sortTable" class="commonTable mainTable" cellpadding="0"
		cellspacing="0">
		<thead>
	    	<tr>
	            <th class="header" dataType='date'>
	            	操作时间<span class="sort">&nbsp;</span>
	            </th>
	            <th class="header" dataType='text'>
	            	操作人<span class="sort">&nbsp;</span>
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
		<c:forEach items="${oneDeptOpTypesTableList }" var="map" varStatus="st">
			<c:choose>
				<c:when test="${st.index % 2 == 0 }">
					<tr class="even">
				</c:when>
				<c:otherwise>
					<tr class="odd">
				</c:otherwise>
			</c:choose>
				<td class="align_right">
					<c:if test="${empty map['data_Date']}">-</c:if>
					${dateFmt:dateFormat(map['data_Date']) }
				</td>
				<td class="align_center">
					<c:if test="${empty map['user_Name']}">-</c:if>
					${map['user_Name']}
				</td>
				<td class="align_left">
					<c:if test="${empty  map['third_Dept_Name']}">-</c:if>
					${map['third_Dept_Name']}
				</td>
				<c:forEach var="opTypeId" items="${headStrs }" varStatus="status">
				<c:set var="opTimes" value="op_Times_${status.count }"></c:set>
				<td class="align_right">
					<c:if test="${empty map[opTimes]}">-</c:if>
					${map[opTimes] }
				</td>
				</c:forEach>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagenum" id="pager">
		<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
	</div>
</div>