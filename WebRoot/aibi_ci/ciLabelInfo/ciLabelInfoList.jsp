<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
   	String labelSubmit = Configure.getInstance().getProperty("LABEL_SUBMIT"); 
	String labelCategoryType = Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE");
%>
<script  type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<c:set var="labelCategoryType" value="<%=labelCategoryType %>"></c:set>
<script type="text/javascript">
$(document).ready( function() {
	  //设置选中页的效果
	  $(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	 });
	  $('#sortTable th').click(sortTable);
});
</script>	
<style type="text/css">
	.dialog_btn_div{*position:static;}
</style>
<!-- area_operation end -->
<!-- area_data start -->
 <div id="labelList">
    <table width="100%" id="sortTable" class="tableStyle" cellpadding="0" cellspacing="0">
      <thead>
        <tr>
        <th  style="cursor: pointer;width: 30px;"><input type="checkbox" name="checkBox" id="checkBoxAll" onclick="checkBoxAll(this)" /></th>
        <th style="width: 50px;"  class="header" dataType='num'>序号<span class="sort">&nbsp;</span></th>
        <th  class="header" dataType='text'>标签名称<span class="sort">&nbsp;</span></th>
        <th  class="header" dataType='text'>标签类型<span class="sort">&nbsp;</span></th>
        <c:if test="${isAdmin}">
        <th  class="header" dataType='text'>创建人<span class="sort">&nbsp;</span></th>
        </c:if>
        <th class="header" dataType='date'>创建时间<span class="sort">&nbsp;</span></th>
        <th  class="header" dataType='text'>更新周期<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>数据状态<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>标签审批状态<span class="sort">&nbsp;</span></th>
        <th  width="250px"  class="noborder">操作</th>
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

              <c:choose>
              	<c:when test="${po.approveRoleType != 2 && ( po.sortNum == 0 || po.sortNum == 2)}">
              		<td style="cursor: pointer;"><input type="checkbox" name="checkBox" class="checkBox" id="checkBox${st.index+1}" value="${po.labelId}" onclick="checkDelete();addRowClass(this)"/></td>
              	</c:when>
              	<c:otherwise>
              		<td style="cursor: pointer;"><input type="checkbox" name="checkBox" class="checkBox" id="checkBox${st.index+1}" value="" disabled/></td>
              	</c:otherwise>
              </c:choose>
              <td>${st.index+1}</td>
              <td class="align_left"><div class="nowrap" align="left"><a href="javascript:showLabelDetail('${po.labelId}');" id="link-look" >${po.labelName}</a></div></td>
              <td>${po.labelTypeName == null ? '-' : po.labelTypeName}</td>
               <c:if test="${isAdmin}">
              <td>${po.createUserName == null ? '-' : po.createUserName}</td>
               </c:if>
              <td class="align_right"><fmt:formatDate value="${po.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
              <td>
              <c:if test="${po.updateCycle == 2 }">月周期</c:if>
              <c:if test="${po.updateCycle == 1 }">日周期</c:if>
              <c:if test="${po.updateCycle == 0 }">非周期</c:if>
              </td>              
              <td>${po.dataStatusName}</td>
              <c:choose>
              	<c:when test="${po.approveRoleType == 2 && po.sortNum == 1}">
              		<td class="green"><c:if test="${empty po.approveStatusName}">-</c:if>${po.approveStatusName}</td>
              	</c:when>
              	<c:when test="${po.approveRoleType == 1 && po.sortNum == 2}">
              		<td class="red"><c:if test="${empty po.approveStatusName}">-</c:if>${po.approveStatusName}</td>
              	</c:when>
              	<c:otherwise>
              		<td><c:if test="${empty po.approveStatusName}">-</c:if>${po.approveStatusName}</td>
              	</c:otherwise>   	
              </c:choose>    
              
              <td class="align_left main_icon">
              <% int _flag=0; %>
              <c:choose>
              	<c:when test="${po.approveRoleType != 2 && ( po.sortNum == 0 || po.sortNum == 2)}">
					<%
						_flag++;
	                    if(!"false".equals(labelSubmit)){
	                %>
					<a class="icon_refer" href="javascript:void(0);" onclick="submitApprove('${po.labelId}','${po.labelName}');">提交审批</a>
	                <%
	                     }
	                %>
	                <a class="icon_edit" href="javascript:void(0);" onclick="editDialog('${po.labelId}');">修改</a>
	                <a class="icon_del" href="javascript:void(0);" onclick="deleteLabel('${po.labelId}');">删除</a>
              	</c:when>
              </c:choose>
              <% 
				if(_flag==0){%>
					-
				<%}%>
              </td>
          </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagenum" id="pager">
    <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
    </div>
    <input type="hidden" name="totalSize" id="totalSize" value="${pager.totalSize}">
  </div>
<!-- area_data start -->
