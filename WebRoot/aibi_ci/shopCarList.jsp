<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
	<!-- 购物车-->
<%@ include file="/aibi_ci/include.jsp"%>
<%@page import="java.util.Iterator"%>


<c:forEach  var="result"  items="${ciLabelRuleList}" varStatus="status">
	<c:if test="${result.elementType == 2 || result.elementType == 5}">
	<li>
		<div class="shopCartGoodsBox">
		    <h6><c:if test="${result.elementType == 5 }">【群】</c:if><c:out value="${result.customOrLabelName}"></c:out></h6>
		</div>
		<div class="shopCartGoodsActiveBox">
			<a href="javascript:void(0);" onclick="deleteShopCart('${result.labelOrCustomSort}');"></a>
		</div>
	</li>
	</c:if>
</c:forEach>
