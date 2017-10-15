<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//设置选中页的效果serialize
	$(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	});
	$('#sortTable th').click(sortTable);
});

</script>	
<!-- area_data start -->
<div id="enumCategoryList">
<table width="100%" id="sortTable" class="tableStyle" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th style="width: 60px;" class="header" dataType='num'>序号<span class="sort">&nbsp;</span></th>
			<th class="header" dataType='text'>标签名称<span class="sort">&nbsp;</span></th>
			<th class="header" dataType='text'>标签类型<span class="sort">&nbsp;</span></th>
			<th width="250px" class="noborder">操作</th>
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
				<div class="nowrap" align="left">${po.labelName}</div>
			</td>
			<td>${po.labelTypeId == 5 ? "枚举标签" : "组合标签"} </td>
			<td><a href="javascript:setCategoryInfo('${po.labelId}')">设置</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="pagenum" id="pager"><jsp:include
	page="/aibi_ci/page_new_html.jsp" flush="true" /></div>
<input type="hidden" name="totalSize" id="enumCategoryTotalSize" value="${pager.totalSize}">
</div>
<!-- area_data start -->
