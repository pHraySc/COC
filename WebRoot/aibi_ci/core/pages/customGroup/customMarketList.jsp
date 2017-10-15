<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="isNotContainLocalList" value="<%=isNotContainLocalList %>"></c:set>
<div class="market-customer-list-inner">
	<ul class="market-customer-list-inner">
		<c:forEach var="po" items="${ciCustomGroupInfoList}" varStatus="st">
			<li class="market-customer-item"> 
				<div class="clearfix market-customer-details J_customer_datils">
					<div class="fleft market-customer-info customer-name">
						
						<!-- 双击加入购物车-----------start -->
						<c:if test="${po.isPrivate == 0}">
						<c:choose> 
							<c:when test='${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }'>
								<c:choose>
									<c:when test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
										<span class="fleft market-info-text ellipsis-all J_customer_market_addCart_db J_customer_market_hiddenShow"  showType="2" customId="${po.customGroupId }" title="${po.customGroupName }">${po.customGroupName } </span> 
									</c:when>
									<c:otherwise>
										<span class="fleft market-info-text ellipsis-all J_customer_market_addCart_fail J_customer_market_hiddenShow" showType="2" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }" userId="${userId}" cityId="${cityId}" root="${root}" title="${po.customGroupName }">${po.customGroupName } </span> 
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<span class="fleft market-info-text ellipsis-all J_customer_market_addCart_fail J_customer_market_hiddenShow" showType="2"  customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"  userId="${userId}" cityId="${cityId}" root="${root}" title="${po.customGroupName }">${po.customGroupName } </span> 
							</c:otherwise>
						</c:choose>
						</c:if>
						<c:if test="${po.isPrivate == 1}">
							<c:choose>
							<c:when test='${po.createUserId == userId}'>
								<c:choose>
									<c:when test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
										<span class="fleft market-info-text ellipsis-all J_customer_market_addCart_db J_customer_market_hiddenShow" showType="2"  customId="${po.customGroupId }" title="${po.customGroupName }">${po.customGroupName } </span> 
									</c:when>
									<c:otherwise>
									<span class="fleft market-info-text ellipsis-all J_customer_market_addCart_fail J_customer_market_hiddenShow" showType="2"  customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"  userId="${userId}" cityId="${cityId}" root="${root}" title="${po.customGroupName }">${po.customGroupName } </span> 
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<span class="fleft market-info-text ellipsis-all J_customer_market_addCart_fail J_customer_market_hiddenShow" showType="2" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"  userId="${userId}" cityId="${cityId}" root="${root}" title="${po.customGroupName }">${po.customGroupName } </span> 
							</c:otherwise>
							</c:choose>
						</c:if>
						<!-- 双击加入购物车-----------end -->
						<ol class="hotState fleft newVersion" id="custom_label_sign_${po.customGroupId }">
								<c:if test="${po.isSysRecom!=null && po.isSysRecom==1 }">
								<li><a class="organ"></a></li>
							</c:if>
							<c:if test="${po.isHotCustom=='true' }">
								<li><a class="hot"></a></li>
							</c:if>
							<li <c:if test="${po.isAttention=='true' }">style="display: block"</c:if><c:if test="${po.isAttention=='false' }">style="display: none"</c:if> id="isAttentionShow_${po.customGroupId}">
								<a  class="hideFont"></a>
							</li>
						</ol>
						<input type="hidden" id="isAttention_${po.customGroupId}" value="${po.isAttention}" />
						<%-- <c:if  test="${po.isAttention == 'true'}">
							<span class="collectStar fleft newVersion" labelorcustomid="${po.customGroupId }" isattention="true">
								<img class="J_custom_cancel_attention" title="点击可取消收藏此用户群" id="attentionImg_${po.customGroupId }" customId="${po.customGroupId }" style="cursor: pointer;" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png">
							</span>
						</c:if>
						<c:if  test="${po.isAttention == 'false'}">
							<span class="collectStar fleft newVersion" labelorcustomid="${po.customGroupId }" isattention="false">
								<img class="J_custom_attention" title="点击可收藏此用户群" id="attentionImg_${po.customGroupId }" customId="${po.customGroupId }" style="cursor: pointer;" src="${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png">
							</span>
						</c:if> --%>
					</div>
					<div class="fright market-customer-info">
						<div class="fright market-info-list-box ">
							<a class="market-info-list-text J_customer_market_addCart" showType="2" customId="${po.customGroupId }">添加</a>
							<div></div>
							<a class="market-info-list-btn J_market_list_operation_btn" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"></a>
							<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
							<c:if test="${isAdmin||(po.createCityId==root) || ( po.createCityId == cityId) || (po.createUserId == userId) || po.isPrivate == 0}">
							<span class="resultItemActiveList" id="operate_${po.customGroupId }">
								<ol class="clearfix">
									<c:if test="${po.dataStatus == 0 || po.dataStatus == 3}">
										<c:if test="${po.dataStatus == 3 && po.isHasList == 1}">
											<c:if test="${cityId == root || po.createCityId == root || po.createCityId == cityId }">
												<li><a href="javascript:void(0);" class="J_market_custom_list_show" customId="${po.customGroupId}" dataDate="${po.dataDate}">清单预览</a></li>
											</c:if>					
										</c:if>
									</c:if>
									<%--  创建中、待创建、成功含清单的用户群，可以进行清单下载： 客户数不为null--%>
									<c:if test="${po.dataStatus != 0 && po.isHasList == 1 && po.customNum != null}">
										<c:if test="${po.createUserId == userId }">
											<li><a href="javascript:void(0);" class="J_market_custom_list_down" customId="${po.customGroupId}">清单下载</a></li>
										</c:if>
									</c:if>
									<c:if test="${po.dataStatus == 0 || po.dataStatus == 3}">
										<c:if test="${havePushFun == 1}">
										<c:if test="${po.dataStatus == 3 && po.isHasList == 1}">
												<c:if test="${po.createUserId == userId }">
													<li><a href="javascript:pushCustomerGroupSingle('${po.customGroupId}','${po.updateCycle}', '${po.isPush}', '${po.duplicateNum}');" >推送设置</a></li>
												</c:if>
										</c:if>
										</c:if>
										<c:if test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0)&& po.isHasList == 1 }">
										<c:if test="${po.failedListCount >= 1  && po.createUserId == userId && po.createTypeId == 1}">
										<li><a class="J_fail_regen_cusotmer_market" href="javascript:void(0);" customId="${po.customGroupId }" createTypeId="${po.createTypeId}" dataDate="">统计失败重新生成</a></li>
										</c:if>
										<c:if test="${po.failedListCount < 1  && po.createTypeId == 1 && po.updateCycle==1 && po.isPrivate != 0 && po.createUserId == userId}">
											<li><a class="J_regen_customer_market" href="javascript:void(0);" customId="${po.customGroupId}" createTypeId="${po.createTypeId}">重新生成</a></li>
										</c:if>
										</c:if>
										<c:if test="${po.isPrivate != 0}">
											<c:if test="${po.dataStatus == 3 && po.createUserId == userId}">
											<li id="share_${po.customGroupId}" c_name="${po.customGroupName}" c_cityId="${po.createCityId}" c_userId="${po.createUserId}">
												<a href="javascript:void(0);" class="J_share_operate" customId="${po.customGroupId }">共享</a>
											</li>
											</c:if>
										</c:if>
										<c:if test="${po.isPrivate == 0}">
										<c:if test="${po.createUserId == userId && po.isSysRecom == 0}">
										<li id="share_${po.customGroupId}">
											<a href="javascript:void(0);" class="J_cancel_share_operate" customId="${po.customGroupId }">取消共享</a>
										</li>
										</c:if>
										</c:if>
										
										<!-- 收藏或取消收藏 begin-->
										<c:if  test="${po.isAttention == 'true'}">
											<li>
												<a href="javascript:void(0);" class="J_custom_cancel_attention" id="attentionImg_${po.customGroupId }" customId="${po.customGroupId }">取消收藏</a>
											</li>
										</c:if>
										<c:if  test="${po.isAttention == 'false'}">
											<li>
												<a href="javascript:void(0);" class="J_custom_attention" id="attentionImg_${po.customGroupId }" customId="${po.customGroupId }">收藏</a>
											</li>
										</c:if>
										<!-- 收藏或取消收藏 end-->
										
										<c:if test="${po.dataStatus == 3}">
											<c:if test="${(root == cityId) || (root != cityId && po.createCityId == cityId)}">
												<c:if test="${isAdmin && po.isPrivate == 0}">
												<li><a class="J_market_custom_set_recom" href="javascript:void(0);" customId="${po.customGroupId}" useTimes="${po.useTimes }">推荐设置</a></li>
												</c:if>
											</c:if>
										</c:if>
									</c:if>
									<!-- 包含下线或者停用标签的用户群不能加入购物车 -->
									<c:if test="${po.isPrivate == 0}">
										<c:if test="${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }">
											<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
											<!-- 模板用户群可以加入购物车 -->
											<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
												<li><a href="javascript:void(0);" class="J_customer_market_addCart"  customId="${po.customGroupId }">添加到收纳篮</a></li>
											</c:if>
										</c:if>
									</c:if>
									<c:if test="${po.isPrivate == 1}">
										<c:if test="${po.createUserId == userId }">
											<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
											<!-- 模板用户群可以加入购物车 -->
											<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
												<li><a href="javascript:javascript:void(0);" class="J_customer_market_addCart"  customId="${po.customGroupId }">添加到收纳篮</a></li>
											</c:if>
										</c:if>
									</c:if>
									<%-- 包含下线或者停用标签的用户群不能加入购物车 --%>
								</ol>
							</span>
							</c:if>
							</c:if>
						</div>
						<span class="fright market-customer-status">
							<em class="fleft " title="数据状态">${po.dataStatusStr}</em> 
							<em class="fleft">
							<c:if test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0) && po.dataStatus == 0 && po.createTypeId == 1 && po.createUserId == userId}">
								<a title="重新生成" class="newVersion query-item-total-icon-new fleft J_fail_regen_cusotmer_market" customId="${po.customGroupId}" createTypeId="${po.createTypeId}" />
							</c:if>
							</em> 
						</span> 
						<span class="fright">
							<c:if test="${po.dataStatus != 0 && po.isHasList == 1 && po.customNum != null}">
							<c:if test="${po.customNum >= 0}">
							<em class="fleft market-text-last">用户数：<a style="text-decoration: underline;" href="javascript:void(0)" <c:if test="${po.createUserId != userId }">class="J_no_show_custom_num"</c:if><c:if test="${po.createUserId == userId }">class="J_market_custom_list_down" customId="${po.customGroupId}"</c:if>><fmt:formatNumber type="number" value="${po.customNum}"  pattern="###,###,###" /></a></em>
							</c:if>
							</c:if>
						</span> 
					</div>
				</div>
				<div class="market-customer-list-inner hidden"></div>
			</li>
		</c:forEach>
	</ul> 
</div> 
<input id="pageTotalSize" value="${pager.totalSize }" type="hidden"/>
<input id="cityId" type="hidden" value="${cityId }"/>
<input id="userId" type="hidden" value="${userId }"/>
<input id="root" type="hidden" value="${root }"/>
<input id="isNotContainLocalList" type="hidden" value="${isNotContainLocalList }"/>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>
