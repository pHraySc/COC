<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
String isCustomVertAttr = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="isNotContainLocalList" value="<%=isNotContainLocalList %>"></c:set>
<c:set var="isCustomVertAttr" value="<%=isCustomVertAttr %>"></c:set>
<div class="market-customer-list-inner">
	<ul class="market-customer-list-inner">
		<c:forEach var="po" items="${ciCustomGroupInfoList}" varStatus="st">
			<li class="market-customer-item"> 
				<div class="clearfix market-customer-details J_customer_datils">
					<div class="fleft market-customer-info customer-name">
						
						<span class="fleft market-info-text J_custom_market_addCart_display ellipsis-all"  showType="2" customGroupId="${po.customGroupId}" title="${po.customGroupName }">${po.customGroupName } </span> 
						<ol class="hotState fleft newVersion" id="custom_label_sign_${po.customGroupId }">
							<c:if test="${po.isSysRecom!=null && po.isSysRecom==1 }">
								<li><a class="organ"></a></li>
							</c:if>
							<c:if test="${po.isHotCustom=='true' }">
								<li><a class="hot"></a></li>
							</c:if>
							<li <c:if test="${po.isAttention=='true' }">style="display: block"</c:if><c:if test="${po.isAttention=='false' }">style="display: none"</c:if> id="isAttentionShow_${po.customGroupId}">
								<a class="hideFont"/></a>
							</li>
						</ol>
						<input type="hidden" id="isAttention_${po.customGroupId}" value="${po.isAttention}" />
						<%-- <c:if  test="${po.isAttention == 'true'}">
							<span class="collectStar fleft newVersion" labelorcustomid="${po.customGroupId}">
								<img class="J_custom_market_list_cancel_attention" userAttentionType="2" title="点击可取消收藏此用户群" id="attentionImg_${po.customGroupId}" customId="${po.customGroupId}" style="cursor: pointer;" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png">
							</span>
						</c:if> --%>
					</div>
					<div class="fright market-customer-info">
					
						<div class="fright market-info-list-box ">
						<a class="market-info-list-text J_custom_market_list_add_cart" customGroupId="${po.customGroupId}" href="javascript:void(0)">添加</a>
						<div></div>
						<a class="market-info-list-btn J_custom_attention_operation_btn" customId="${po.customGroupId}"></a>
						<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
							<c:if test="${isAdmin||(po.createCityId==root) || ( po.createCityId == cityId) || (po.createUserId == userId) || po.isPrivate == 0}">
							<span class="resultItemActiveList" id="operate_${po.customGroupId }">
								<ol class="clearfix">
									<li><a class="J_custom_market_list_cancel_attention" userAttentionType="2" href="javascript:void(0);" customId="${po.customGroupId}">取消收藏</a></li>
									<!-- 包含下线或者停用标签的用户群不能加入购物车 -->
									<c:if test="${po.isPrivate == 0}">
										<c:if test="${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }">
											<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
											<!-- 模板用户群可以加入购物车 -->
											<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
												<li><a class="J_custom_market_list_add_cart" customGroupId="${po.customGroupId}" href="javascript:void(0)">添加到收纳篮</a></li>
											</c:if>
										</c:if>
									</c:if>
									<c:if test="${po.isPrivate == 1}">
										<c:if test="${po.createUserId == userId }">
											<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
											<!-- 模板用户群可以加入购物车 -->
											<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
												<li><a class="J_custom_market_list_add_cart" customGroupId="${po.customGroupId}" href="javascript:void(0)">添加到收纳篮</a></li>
											</c:if>
										</c:if>
									</c:if>
								</ol>
							</span>
							</c:if>
							</c:if>
					</div>
						<%-- <span class="fright market-info-right-icon market-navigation-icon J_market_custom_detail" showType="2" customId="${po.customGroupId }"></span> 
						<span class="fright market-customer-operate resultItemActive J_resultItemActiveCustom" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }">
							<span class="dsts">操作</span>
							<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
							<c:if test="${isAdmin||(po.createCityId==root) || ( po.createCityId == cityId) || (po.createUserId == userId) || po.isPrivate == 0}">
							<span class="resultItemActiveList" id="operate_${po.customGroupId }">
								<ol class="clearfix">
									<li><a class="J_custom_market_list_cancel_attention" userAttentionType="2" href="javascript:void(0);" customId="${po.customGroupId}">取消收藏</a></li>
									<!-- 包含下线或者停用标签的用户群不能加入购物车 -->
									<c:if test="${po.isPrivate == 0}">
										<c:if test="${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }">
											<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
											<!-- 模板用户群可以加入购物车 -->
											<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
												<li><a class="J_custom_market_list_add_cart" customGroupId="${po.customGroupId}" href="javascript:void(0)">添加到收纳篮</a></li>
											</c:if>
										</c:if>
									</c:if>
									<c:if test="${po.isPrivate == 1}">
										<c:if test="${po.createUserId == userId }">
											<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
											<!-- 模板用户群可以加入购物车 -->
											<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
												<li><a class="J_custom_market_list_add_cart" customGroupId="${po.customGroupId}" href="javascript:void(0)">添加到收纳篮</a></li>
											</c:if>
										</c:if>
									</c:if>
								包含下线或者停用标签的用户群不能加入购物车
								</ol>
							</span>
							</c:if>
							</c:if>
						</span> --%>
					</div>
				</div>
				<div class="market-customer-list-inner hidden"></div>
			</li>
		</c:forEach>
	</ul> 
</div> 
<input id="customPageTotalSize" value="${pager.totalSize }" type="hidden"/>
<input id="cityId" type="hidden" value="${cityId }"/>
<input id="userId" type="hidden" value="${userId }"/>
<input id="root" type="hidden" value="${root }"/>
<input id="isNotContainLocalList" type="hidden" value="${isNotContainLocalList }"/>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>
