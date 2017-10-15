<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script  type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	//设置选中页的效果
	$(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	});
	$('#sortTable th').click(sortTable);

});
</script>	
<!-- area_data start -->
<div id="categoryInfoListTable">
<table width="100%" id="sortTable" class="tableStyle" cellpadding="0" cellspacing="0">
	<thead>
		<tr><!--  class="header" -->
			<th style="width: 60px;" ><input type="checkbox" name="enumCategoryCheckBoxAll"  id="enumCategoryCheckBoxAll" onclick="checkBoxAll(this);"/></th>
			<th style="width: 60px;"  class="header" dataType='num'>序号<span class="sort">&nbsp;</span></th>
			<th class="header" dataType='text'>分类名称<span class="sort">&nbsp;</span></th>
			<th class="header" dataType='text'>分类描述<span class="sort">&nbsp;</span></th>
			<th class="header" dataType='text'>维值数量<span class="sort">&nbsp;</span></th>
			<th  width="250px" class="noborder">操作</th><!-- class="header" -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pager.result}" var="po" varStatus="st">
		<tr class='${st.index % 2 == 0 ?"even":"odd" }'>
			<td><input type="checkbox" name="enumCategoryCheckBox"  id="checkBox${st.index+1}" value="${po.enumCategoryId}" /></td>
			<td>${st.index+1 }</td>
			<td class="align_left">
				<div class="nowrap" align="left">
					<a href="javascript:openEnumCategoryInfoViewDialog('${po.enumCategoryId}','${po.enumCategoryName}');" >${po.enumCategoryName}</a>
				</div>
			</td>
			<td>${empty po.descTxt ? "-" : po.descTxt}</td>
			<td>${po.enumNum}</td>
			<td >
				<a href="javascript:openUploadDataDialog('${po.enumCategoryId}');" class="layout">${po.enumNum == 0 ? "导入" : "替换"}</a>
				<a href="javascript:delOne('${po.enumCategoryId}');">删除</a>
			</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="pagenum" id="pager"><jsp:include
	page="/aibi_ci/page_new_html.jsp" flush="true" /></div>
</div>
<!-- area_data start -->
