<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<ul class="market-customer-list-inner">
	<c:forEach items="${pager.result}" var="custom" varStatus="st">
		<li class="market-customer-item"> 
			<div class="clearfix market-customer-details J_customer_datils">
				<div class="fleft market-customer-info">
					<span class="fright">
						<em class="red">${st.count }</em>
					</span>
				</div>
				<div class="fleft market-customer-info customer-name">
					<span class="fleft market-info-text J_custom_market_addCart ellipsis-all" customGroupId="${custom.customGroupId}" title="${custom.customGroupName}">${custom.customGroupName} </span> 
				</div>
				
				<div class="fright market-customer-info">
					<span class="fright market-customer-date-box">
						<em>使用次数：</em>
						<em>${custom.useTimes }</em>
					</span>
					<span class="fright market-customer-date-box">
						<em>用户群规模：</em>
							<c:if test="${custom.customNum != null && custom.customNum != ''}">
								<em><fmt:formatNumber type="number" value="${custom.customNum}"  pattern="###,###,###" /></em>
							</c:if>
							<c:if test="${custom.customNum == null || custom.customNum == ''}">
								<em>-</em>
							</c:if>
					</span>
				</div>
			</div> 
		</li>
	</c:forEach>
</ul> 
						
