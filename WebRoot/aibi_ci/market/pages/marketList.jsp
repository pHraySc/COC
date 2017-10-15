<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<ul class="market-customer-list-inner J_market_list_ul">
	<c:forEach var="po" items="${pager.result}" varStatus="st">
	<li class="market-customer-item"> 
		<div class="clearfix market-customer-details J_customer_datils">
			<div class="fleft market-customer-info label-name">
				<c:choose>
					<c:when test="${po.showType == 1}">
						<span class="fleft market-navigation-icon tag-left-icon"></span>
					</c:when>
					<c:when test="${po.showType == 2}">
						<span class="fleft market-navigation-icon customer-left-icon"></span>
					</c:when>
				</c:choose>
				<span class="fleft market-info-text ellipsis-all J_market_addCart ellipsis-all" showType="${po.showType}" labelId="${po.labelId}" customGroupId="${po.customGroupId}" title="${po.labelCustomName}">${po.labelCustomName}</span> 
			</div>
			<div class="fright market-customer-info">
			
				<div class="fright market-info-list-box ">
					<a class="market-info-list-text J_market_list_add_cart" showType="${po.showType}" labelId="${po.labelId}" customGroupId="${po.customGroupId}">添加</a>
					<div></div>
					<a class="market-info-list-btn J_market_list_operation_btn"></a>
					<span class="resultItemActiveList">
						<ol class="clearfix">
							<c:if test="${isAdmin}">
							<li class="hand J_market_list_cancel_recommend" primaryKeyId="${po.primaryKeyId}" 
								showType="${po.showType}" labelId="${po.labelId}" 
								customGroupId="${po.customGroupId}"><a>取消推荐</a></li>
							</c:if>
							<li class="hand J_market_list_add_cart" showType="${po.showType}" labelId="${po.labelId}" customGroupId="${po.customGroupId}"><a>添加到收纳篮</a></li>
						</ol>
					</span>
				</div>
				
				<%-- <span class="fright market-info-right-icon market-navigation-icon J_com_show_arrow custom-label-show-detail" showType="${po.showType}" labelId="${po.labelId}" customGroupId="${po.customGroupId}"></span> --%> 
				<%-- <span class="fright market-customer-operate resultItemActive" >
					<span class="dsts">操作</span>
					<span class="resultItemActiveList">
						<ol class="clearfix">
							<c:if test="${isAdmin}">
							<li class="J_market_list_cancel_recommend" primaryKeyId="${po.primaryKeyId}" 
								showType="${po.showType}" labelId="${po.labelId}" 
								customGroupId="${po.customGroupId}"><a>取消推荐</a></li>
							</c:if>
							<li class="J_market_list_add_cart" showType="${po.showType}" labelId="${po.labelId}" customGroupId="${po.customGroupId}"><a>添加到收纳篮</a></li>
						</ol>
					</span>
				</span> --%>
				<span class="fright market-customer-date-box">
					<em>数据日期：</em>
					<em>${po.dataDate}</em>
				</span>
				<span class="fright" title="${po.avgScore}">
					<em class="fleft">推荐度：</em>
					<c:if test="${empty po.avgScore}">
						<em class="fleft market-star-last">暂无评分</em>
					</c:if>
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:if test="${i <= po.avgScore}">
							<em class="fleft market-y-star market-navigation-icon <c:if test="${i == 5}">market-star-last</c:if>"></em>
						</c:if>
						<c:if test="${i > po.avgScore && i < po.avgScore + 1}">
							<em class="fleft market-half-star market-navigation-icon <c:if test="${i == 5}">market-star-last</c:if>"></em>
						</c:if>
						<c:if test="${i - 1 >= po.avgScore}">
							<em class="fleft market-hollow-star market-navigation-icon <c:if test="${i == 5}">market-star-last</c:if>"></em>
						</c:if>
					</c:forEach>
				</span> 
			</div>
		</div> 
		<div class="market-customer-list-inner hidden"></div>
	</li>
	</c:forEach>
</ul> 
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>
<input type="hidden" name="totalSize" id="totalConfigSize" value="${pager.totalSize}">
