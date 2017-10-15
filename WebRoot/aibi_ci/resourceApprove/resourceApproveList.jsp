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
	$(".mainTable td div.nowrap").width(tableWidth-1000);
	$('#sortTable th').click(sortTable);
});
</script>	
<!-- area_operation end -->
<!-- area_data start -->
 <div id="labelList" class="tableStyleBox">
    <table id="sortTable" width="100%" class="tableStyle" cellpadding="0" cellspacing="0">
    	<thead>
        <tr>
        <th style="cursor: pointer;width: 30px;"><input type="checkbox" name="checkBox" id="checkBoxAll" onclick="checkBoxAll(this)"/></th>
        <th style="width: 50px;" class="header" dataType='num'>序号<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>资源名称<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>创建人<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>资源类型<span class="sort">&nbsp;</span></th>
        <th style="width: 100px;" class="header" dataType='text'>上一级审批人<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='date'>上一级审批时间<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>审批状态<span class="sort">&nbsp;</span></th>
        <th style="width:200px;">操作</th>
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
          	  <td style="cursor: pointer;width: 30px;"><input name="statusIds" type="checkbox" class="checkBox" value="${po.statusId}" onclick="checkDelete();addRowClass(this);" /></td>
              <td>${st.index+1}</td>
             <%--  <td><input type="hidden" value="${po.resourceId}"/></td> --%>
              <td class="align_left"><div class="nowrap"><a href="javascript:void(0);" onclick="showLabelDetail('${po.resourceId}','${po.resourceDetailLink}');" id="link-look" >${po.resourceName}</a></div></td>
              <td>${po.resourceCreateUserName}</td>
              <td>${po.resourceTypeName == null ? '-' : po.resourceTypeName}</td>
              <td>${po.lastApproveUserName == null ? '-' : po.lastApproveUserName}</td>
              <c:choose>
              	<c:when test = "${po.lastApproveTime != null}">
		        	<td class="align_right">
              			<fmt:formatDate value="${po.lastApproveTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
              		</td>
              	</c:when>
              	<c:otherwise>
              		<td>-</td>
              	</c:otherwise>
              </c:choose>
              <td>${po.approveStatusName}</td>
              <td>
              <c:if test="${po.sortNum == 1}">
                <a class="icon_appOk" href="javascript:void(0);" onclick="approveOk(${po.statusId});" >审批通过</a>
                <a class="icon_appNo" href="javascript:void(0);" onclick="approveNo(${po.statusId});">审批不通过</a>
              </c:if>
              </td>
			</tr>
        </c:forEach>
    </table>
    <div class="pagenum" id="pager">
    <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
    </div>
    <input type="hidden" name="totalSize" id="totalSize" value="${pager.totalSize}">
  </div>

