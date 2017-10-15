<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="LABEL_TYPE_SIGN" value="<%=ServiceConstants.LABEL_TYPE_SIGN%>"></c:set>

<div class="customer-details-info">
	<div class="market-customer-list-inner ">
		<ul class=" market-customer-list-inner clearfix">
			<li class="market-customer-attr-info first fleft">
				<span class="color_blue">更新周期：</span>
				<span>
					<c:if test="${labelDetailInfo.updateCycle == 1}">日周期</c:if>
					<c:if test="${labelDetailInfo.updateCycle == 2}">月周期</c:if>
				</span>
			<li>
			<li class="item-seg-line-new"></li>
			<li class="market-customer-attr-info fleft">
				<span class="color_blue">用户数：</span>
				<span>
					<c:if test="${labelDetailInfo.labelTypeId == LABEL_TYPE_SIGN  && labelDetailInfo.customNum != null &&  labelDetailInfo.customNum != ''}">
						<fmt:formatNumber  type="number" value="${labelDetailInfo.customNum }" pattern="###,###,###"></fmt:formatNumber>
					</c:if>
				</span>
			<li>
			<li class="item-seg-line-new"></li>
			<li class="market-customer-attr-info fleft">
				<span class="color_blue">标签统计日期：</span>
				<span>
					<c:if test="${labelDetailInfo.dataDate != null && labelDetailInfo.dataDate != ''}">
						${dateFmt:dateFormat(labelDetailInfo.dataDate)}&nbsp;
					</c:if>
				</span> 
			<li>
		</ul>
		<ul class="market-customer-list-inner clearfix">
			<li class="market-customer-attr-info fleft first">
				<span class="color_blue">业务口径：</span>
				<span>${labelDetailInfo.busiCaliber}</span>
			</li>
		</ul>
		<ul class="market-customer-list-inner clearfix">
			<li class="market-customer-attr-info fleft first">
				<span class="color_blue">标签路径：</span>
				<span>${labelDetailInfo.currentLabelPath}</span>
			</li>
		</ul>
	</div>
</div>
