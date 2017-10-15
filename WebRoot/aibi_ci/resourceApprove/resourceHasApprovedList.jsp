<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	  $(".num").each(function(){
		if($(this).text() == '${pager.pageNum}'){
			$(this).addClass("num_on");
		}
	 });
	//定义限制宽度单元格的默认宽度
		var tableWidth=$(".mainTable").parent("div").width();
		$(".mainTable td div.nowrap").width(tableWidth-850);

		 $('#sortTable th').click(sortTable);
});
</script>	
<!-- area_operation end -->
<!-- area_data start -->
 <div id="labelList" class="tableStyleBox">
    <table id="sortTable" width="100%" class="tableStyle">
       <thead>
        <tr>
        <th class="header" dataType='num'>序号<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>资源名称<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>资源类型<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>创建人<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='date'>审批时间<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>审批结果<span class="sort">&nbsp;</span></th>
        <!--<th  class="noborder">操作</th>-->
        </tr>
        </thead>
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
              <td class="align_left"><div class="nowrap"><a href="javascript:showLabelDetail('${po.resourceId}','${po.resourceDetailLink}');" id="link-look" >${po.resourceName}</a></div></td>
              <td>${po.resourceTypeName == null ? '-' : po.resourceTypeName}</td>
              <td>${po.resourceCreateUserName}</td>
              <c:choose>
              	<c:when test = "${po.approveTime != null}">
		        	<td class="align_right">
              			<fmt:formatDate value="${po.approveTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
              		</td>
              	</c:when>
              	<c:otherwise>
              		<td>-</td>
              	</c:otherwise>
              </c:choose>
              <c:choose>
              	<c:when test="${po.approveResult == 1 }">
              		<td>通过</td>
              	</c:when>
              	<c:when test="${po.approveResult == 0 }">
              		<td>不通过</td>
              	</c:when>
              </c:choose>
          </tr>
        </c:forEach>
    </table>
    <div class="pagenum" id="pager">
    <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
    </div>
    <input type="hidden" name="totalSize" id="totalSize" value="${pager.totalSize}">
  </div>
<!-- area_data start -->
