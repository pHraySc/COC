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
	  <th width="225">客户群名称</th>
	  <th width="200">客户群规模</th>
	  <c:choose>
		  <c:when test="${rankingListType == 1 }">
		  <th width="200">修改时间</th>
		  </c:when>
		  <c:otherwise>
		  <th width="200">使用次数</th>
		  </c:otherwise>
	  </c:choose>
	 </tr>
   </thead>
   <tbody>
   <c:if test="${pager.result== null || fn:length(pager.result) == 0}">
   	 <tr><td colspan="4" class="arrayTopColor">无客户群记录</td></tr>
   </c:if>
   <c:forEach items="${pager.result}" var="po" varStatus="st">
	 <tr <c:if test="${st.count%2 == 0 }">class='even'</c:if>>
		<td class="arrayTopColor">${st.count}</td>
		<td style="cursor: pointer;" class="align_left" <c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4) || custom.createCityId == root || custom.createCityId == cityId || custom.isContainLocalList == isNotContainLocalList }">ondblclick="addToShoppingCar(event,this,2);"</c:if> element="${po.customGroupId}">${po.customGroupName }</td>
		<td class="align_right">
		<c:if test="${po.customNum == null || po.customNum == ''}">-&nbsp;
			</c:if>
			<c:if test="${po.customNum != null && po.customNum != ''}">
			<fmt:formatNumber type="number" value="${po.customNum}"  pattern="###,###,###" /></td>
			</c:if>
		<c:choose>
		  <c:when test="${rankingListType == 1 }">
		  <td class="align_right"><fmt:formatDate value="${po.newModifyTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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

