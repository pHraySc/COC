<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/rankingList/lastUsedCuntom.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/rankingList/lastUsedCuntomEntrance.js"></script>
<div class="main-list-content fright">
	<div class="main-list-content-inner">
		<div class="content-COC-list">
			<div class="market-filter-content">
				<div class="market-customer-list">
					<div class="market-customer-list-inner">
						<div class="customer-list-search-box">
							<span class="fleft">当前位置：</span>
							<span class="fleft">最新发布用户群</span>
						</div>
						<div class="market-customer-list-inner">
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
												<span class="fright  market-customer-date-box">
													<em>修改时间：</em>
													<em><fmt:formatDate value="${custom.newModifyTime }" pattern="yyyy-MM-dd HH:mm:ss"/></em>
												</span>
											</div>
											<c:if test="${custom.customNum != null && custom.customNum != ''}">
												<div class="fright market-customer-info">
													<span class="fright  market-customer-date-box">
														<em>用户群规模：</em>
														<em><fmt:formatNumber type="number" value="${custom.customNum}"  pattern="###,###,###" /></em>
													</span>
												</div>
											</c:if>
											<c:if test="${custom.customNum == null || custom.customNum == ''}">
												<div class="fright market-customer-info">
													<span class="fright  market-customer-date-box">
														<em>用户群规模：</em>
														<em>-</em>
													</span>
												</div>
											</c:if>
										</div> 
									</li>
								</c:forEach>
							</ul> 
						</div> 
					</div>
				</div>
			</div> 
		</div> 
	</div>
</div>


<!-- 
<script type="text/javascript">
$(document).ready(function(){
});
</script>
<table class="tableStyle">
   <thead>
	 <tr>
	  <th width="80">排行</th>
	  <th width="225">用户群名称</th>
	  <th width="200">用户群规模</th>
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
   	 <tr><td colspan="4" class="arrayTopColor">无用户群记录</td></tr>
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
 -->
