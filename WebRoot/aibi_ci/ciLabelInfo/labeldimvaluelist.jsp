<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script  type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<style type="text/css">
	.dialog_btn_div{*position:static;}
</style>
<!-- area_operation end -->
<!-- area_data start -->
<div id="labelList">
	<table width="100%" id="sortTable" class="commonTable mainTable" cellpadding="0" cellspacing="0">
		<thead>
	        <tr>
	        	<th  class="header" dataType='text'>序号<span class="sort">&nbsp;</span></th>
		        <th  class="header" dataType='text'>维值ID<span class="sort">&nbsp;</span></th>
		        <th  class="header" dataType='text'>维值<span class="sort">&nbsp;</span></th>
	        </tr>
       	</thead>
       	<tbody>
	        <c:forEach items="${pager.result}" var="map" varStatus="st">
	        <tr>
	        	<c:forEach items="${map}" var="mapItem" varStatus="st">
	        	<td>${mapItem.value}</td>
	        	</c:forEach>
	        </tr>
	        </c:forEach>
        </tbody>
    </table>
    <div class="pagenum" id="pager">
    <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
    </div>
  </div>
