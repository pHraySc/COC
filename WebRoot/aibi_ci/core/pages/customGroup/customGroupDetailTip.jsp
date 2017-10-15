<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<div class="customer-details-info">
	<div class="market-customer-list-inner ">
		<ul class=" market-customer-list-inner clearfix">
			<li class="market-customer-attr-info first fleft">
				<span class="color_blue">编号：</span>
				<span>${ciCustomGroupInfo.customGroupId}</span>
			</li>
			<li class="item-seg-line-new"></li>	
			<li class="market-customer-attr-info fleft">
				<span class="color_blue">最新修改时间：</span>
				<span>
				<c:choose>
					<c:when test="${ciCustomGroupInfo.newModifyTime == null}">
			        	暂无内容
			        </c:when>
			        <c:otherwise>
			        	<fmt:formatDate value="${ciCustomGroupInfo.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			        </c:otherwise>
				</c:choose>
				</span>
			</li>
			<li class="item-seg-line-new"></li>
			<li class="market-customer-attr-info fleft">
				<span class="color_blue">创建人：</span>
				<span>${ciCustomGroupInfo.createUserName}</span> 
			</li>
		</ul>
		<ul class="market-customer-list-inner clearfix">
			<li class="market-customer-attr-info first fleft">
				<span class="color_blue">创建方式：</span>
				<span>${ciCustomGroupInfo.createType}</span> 
			</li>
			<li class="item-seg-line-new"></li>
		    <li class="market-customer-attr-info fleft">
		    	<span class="color_blue">更新周期：</span>
				<span>
					<c:if test="${ciCustomGroupInfo.updateCycle == 1}">一次性</c:if>
        			<c:if test="${ciCustomGroupInfo.updateCycle == 2}">月周期</c:if>
        			<c:if test="${ciCustomGroupInfo.updateCycle == 3}">日周期</c:if>
        		</span> 
			</li>
			<li class="item-seg-line-new"></li>
		    <li class="market-customer-attr-info fleft">
		    	<span class="color_blue">是否共享：</span>
				<span>
					<c:if test="${ciCustomGroupInfo.isPrivate == 0}">共享</c:if>
					<c:if test="${ciCustomGroupInfo.isPrivate == 1}">非共享</c:if>
					<c:if test="${empty ciCustomGroupInfo.isPrivate }">-</c:if>
				</span> 
			</li>
			<li class="item-seg-line-new"></li>
		    <li class="market-customer-attr-info fleft">
		    	<span class="color_blue">地市：</span>
				<span>${ciCustomGroupInfo.createCityName}</span> 
			</li> 
		</ul>
		<ul class="market-customer-list-inner clearfix">
			<li class="market-customer-attr-info fleft first">
				<span class="color_blue">描述：</span>
				<span title="${ciCustomGroupInfo.customGroupDesc}">${ciCustomGroupInfo.customGroupDesc}</span>
			</li>
		</ul>
		
		<ul class="market-customer-list-inner clearfix">
			<li class="market-customer-attr-info fleft first ">
				<span class="fleft market-rule-title color_blue">规则：</span>
				<span class="fleft">
				<em class="market-rule-text">
				${ciCustomGroupInfo.prodOptRuleShow}${ciCustomGroupInfo.simpleRule}${ciCustomGroupInfo.customOptRuleShow}
				<c:if test="${ciCustomGroupInfo.isOverlength == true}">
				...
				</c:if>
				${ciCustomGroupInfo.kpiDiffRule}
				<c:if test="${ciCustomGroupInfo.isOverlength == true}">
					<span class="market-rule-more" onclick="coreMain.showRule('${ciCustomGroupInfo.customGroupId}');">详情</span>
				</c:if>
				</em>
				
				
				</span >
			</li>
		</ul>
		
		<ul class="market-customer-list-inner clearfix">
			<li class="market-customer-attr-info fleft first more-text">
				<c:if test="${ciCustomGroupInfo.monthLabelDate != null && ciCustomGroupInfo.monthLabelDate != ''}">
				<label class="color_blue">数据月：</label>${ciCustomGroupInfo.monthLabelDate}&nbsp;&nbsp;
				</c:if>
				<c:if test="${ciCustomGroupInfo.dayLabelDate != null && ciCustomGroupInfo.dayLabelDate != ''}">
				<label class="color_blue">数据日：</label>${ciCustomGroupInfo.dayLabelDate}
				</c:if>
				<c:if test="${(ciCustomGroupInfo.dayLabelDate == null || ciCustomGroupInfo.dayLabelDate == '') && (ciCustomGroupInfo.monthLabelDate == null || ciCustomGroupInfo.monthLabelDate == '')}">
				<label class="color_blue">数据日期：</label>${ciCustomGroupInfo.dataDateStr}
				</c:if>
			</li>
		</ul>
	</div>
</div>
