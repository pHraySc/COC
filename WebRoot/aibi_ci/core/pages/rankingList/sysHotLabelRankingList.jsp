<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<ul class="market-customer-list-inner">
	<c:forEach items="${pager.result}" var="label" varStatus="st">
		<li class="market-customer-item"> 
			<div class="clearfix market-customer-details J_customer_datils">
				<div class="fleft market-customer-info">
					<span class="fright">
						<em class="red">${st.count }</em>
					</span>
				</div>
				<div class="fleft market-customer-info customer-name">
					<span class="fleft market-info-text J_label_market_addCart ellipsis-all"  labelId="${label.labelId}" title="${label.labelName}">${label.labelName} </span> 
				</div>
				<div class="fright market-customer-info">
					<span class="fright market-customer-date-box">
						<em>使用次数：</em>
						<em>${label.useTimes }</em>
					</span>
				</div>
			</div> 
		</li>
	</c:forEach>
</ul> 
						
