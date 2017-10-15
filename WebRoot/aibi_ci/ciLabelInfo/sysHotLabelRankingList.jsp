<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
});
</script>
<table class="tableStyle">
   <thead>
	 <tr>
	  <th width="80">排行</th>
	  <th width="425">标签名称</th>
	  <c:choose>
		  <c:when test="${rankingListType == 1 }">
		  <th width="200">发布时间</th>
		  </c:when>
		  <c:otherwise>
		  <th width="200">使用次数</th>
		  </c:otherwise>
	  </c:choose>
	 </tr>
   </thead>
   <tbody>
   <c:if test="${pager.result== null || fn:length(pager.result) == 0}">
   	 <tr><td colspan="3" class="arrayTopColor">无标签记录</td></tr>
   </c:if>
   <c:forEach items="${pager.result}" var="po" varStatus="st">
	 <tr <c:if test="${st.count%2 == 0 }">class='even'</c:if>>
		<td class="arrayTopColor">${st.count}</td>
		<td style="cursor: pointer;" class="align_left" ondblclick="addToShoppingCar(event,this,1);" element="${po.labelId}">${po.labelName }</td>
		<c:choose>
		  <c:when test="${rankingListType == 1 }">
		  <td class="align_right">${po.publishTime }</td>
		  </c:when>
		  <c:otherwise>
		  <td class="align_right">${po.useTimes }</td>
		  </c:otherwise>
	  </c:choose>
	 </tr>
   </c:forEach>
   </tbody>
</table>
<div style="height: 34px;"></div>
