<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(document).ready( function() {
	//设置选中页的效果
	$(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	});
});
</script>	
<div id="enumValueListTable">
<table width="100%" id="sortTable" class="tableStyle" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th style="width: 60px;"  dataType='num'>序号</th><!-- class="header" -->
			<th dataType='text'>ID</th><!--  class="header"  -->
			<th dataType='text'>值</th><!--  class="header"  -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pager.result}" var="po" varStatus="st">
		<tr class='${st.index % 2 == 0 ?"even":"odd" }'>
			<td>${st.index+1 }</td>
			<td class="align_left" title="${po['V_KEY']}">
				<div class="nowrap" align="left" >${po['V_KEY']}</div>
			</td>
			<td class="align_left" title="${empty po['V_NAME'] ? "-" : po['V_NAME']}"><div class="nowrap" align="left" >${empty po['V_NAME'] ? "-" : po['V_NAME']}</div></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="pagenum" id="pager"><jsp:include
	page="/aibi_ci/page_new_html.jsp" flush="true" /></div>
</div>
