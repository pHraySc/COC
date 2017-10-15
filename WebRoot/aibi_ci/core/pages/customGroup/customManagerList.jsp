<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_SCENE" value="<%=CommonConstants.TABLE_DIM_SCENE%>"></c:set>
<%
	String productMenu = Configure.getInstance().getProperty(
			"PRODUCT_MENU");
	String marketingMenu = Configure.getInstance().getProperty(
			"MARKETING_MENU");
	String personMenu = Configure.getInstance().getProperty(
			"PERSON_MENU");
	String alarmMenu = Configure.getInstance().getProperty(
			"AlARM_MENU");
	String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
	String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
	String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
	String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
	String root = Configure.getInstance().getProperty("CENTER_CITYID");
	int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
	String isCustomVertAttr = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
%>
<c:set var="isCustomVertAttr" value="<%=isCustomVertAttr %>"></c:set>
<script type="text/javascript">
$(document).ready( function() {
	$(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	});
});
</script>
<div class="market-customer-list-inner">
	<ul class="market-customer-list-inner">
		<c:forEach items="${pager.result}" var="po" varStatus="st">
			<li class="market-customer-item"> 
				<div class="clearfix market-customer-details J_customer_datils">
					<div class="fleft market-customer-info customer-name">
						<!-- 双击加入购物车-----------start -->
						<c:if test="${po.isPrivate == 0}">
						<c:choose> 
							<c:when test='${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }'>
								<c:choose>
									<c:when test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
										<span class="fleft market-info-text ellipsis-all J_manager_addCart J_customer_manager_addCart_db J_custom_hidden_show" title="${po.customGroupName }" customName="${po.customGroupName}" customId="${po.customGroupId }">${po.customGroupName } </span> 
									</c:when>
									<c:otherwise>
										<span class="fleft market-info-text ellipsis-all J_manager_addCart J_customer_manager_addCart_fail J_custom_hidden_show" title="${po.customGroupName }" customName="${po.customGroupName}" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }" userId="${userId}" cityId="${cityId}" root="${root}" >${po.customGroupName } </span> 
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<span class="fleft market-info-text ellipsis-all J_manager_addCart J_customer_manager_addCart_fail" title="${po.customGroupName }" customName="${po.customGroupName}" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"  userId="${userId}" cityId="${cityId}" root="${root}" >${po.customGroupName } </span> 
							</c:otherwise>
						</c:choose>
						</c:if>
						<c:if test="${po.isPrivate == 1}">
							<c:choose>
							<c:when test='${po.createUserId == userId}'>
								<c:choose>
									<c:when test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
										<span class="fleft market-info-text ellipsis-all J_manager_addCart J_customer_manager_addCart_db" title="${po.customGroupName }" customName="${po.customGroupName}"  customId="${po.customGroupId }">${po.customGroupName } </span> 
									</c:when>
									<c:otherwise>
									<span class="fleft market-info-text ellipsis-all J_manager_addCart J_customer_manager_addCart_fail" title="${po.customGroupName }" customName="${po.customGroupName}" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"  userId="${userId}" cityId="${cityId}" root="${root}" >${po.customGroupName } </span> 
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<span class="fleft market-info-text ellipsis-all J_manager_addCart J_customer_manager_addCart_fail" title="${po.customGroupName }" customName="${po.customGroupName}" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"  userId="${userId}" cityId="${cityId}" root="${root}" >${po.customGroupName } </span> 
							</c:otherwise>
							</c:choose>
						</c:if>
						<!-- 双击加入购物车-----------end -->
					</div>
					<div class="fright market-customer-info">
					
					   <div class="fright market-info-list-box ">
							<a class="market-info-list-text J_customer_manager_addCart"  customId="${po.customGroupId}">添加</a>
							<div></div>
							<a class="market-info-list-btn J_market_list_operation_btn" customId="${po.customGroupId }" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"></a>
							 <c:if test="${isAdmin||(po.createCityId==root) || ( po.createCityId == cityId) || (po.createUserId == userId) || po.isPrivate == 0}">
						   	<span class="resultItemActiveList" id="${po.customGroupId }">
								<ol>
									<!-- 不含清单且含有下线标签的用户群 有删除操作无其他操作权限 -->
									<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
									<c:if test="${po.dataStatus == 0 || po.dataStatus == 3 }">
										<c:if test="${po.dataStatus == 3 && po.isHasList == 1}">
										<c:if test="${cityId == root || po.createCityId == root || po.createCityId == cityId }">
											<li><a href="javascript:void(0);" class="J_query_group_list" customId="${po.customGroupId}" dataDate="${po.dataDate}" >清单预览</a></li>
										</c:if>
										<c:if test="${po.listTableName != null}">
									    <% if("true".equals(indexDifferentialMenu)){ %>
					                         	<li style="display:none"><a href="javascript:openIndexDeff('${po.customGroupId}','${po.listTableName}','${po.customNum}','${po.dataDate}')">指标微分</a></li>
									    <%};if("true".equals(customerAnalysisMenu)){
					                             %>
					                         	<%
					                                 };if("true".equals(marketingMenu)){
					                             %>
					                         	<li><a href="javascript:customSysMatch('${po.customGroupId}','${po.listTableName}','${po.customNum}','${po.dataDate}','${po.failedListCount}')">营销策略匹配</a></li>
					                         	<%}; %>
							            </c:if>
								        </c:if>
								      </c:if>
								      <!-- 创建中、待创建、成功含清单的用户群，可以进行清单下载： 用户数不为null-->
								      <c:if test="${po.dataStatus != 0 && po.isHasList == 1 && po.customNum != null}">
								        <c:if test="${po.createUserId == userId }">
										<li><a class="J_market_custom_list_down" customId="${po.customGroupId}" href="javascript:void(0);" >清单下载</a></li>
										</c:if>
									  </c:if>
									  <c:if test="${po.dataStatus == 0 || po.dataStatus == 3 }">
								        <c:if test="${havePushFun == 1}">
										<c:if test="${po.dataStatus == 3 && po.isHasList == 1}">
				                        	<c:if test="${po.createUserId == userId}">
					                      	   	<li><a href="javascript:pushCustomerGroupSingle('${po.customGroupId}','${po.updateCycle}', '${po.isPush}', '${po.duplicateNum}');" >推送设置</a></li>
				                        	</c:if>
										</c:if>
										</c:if>
										<c:if test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0) && po.isHasList == 1}">
										<c:if test="${po.failedListCount >= 1  && po.createUserId == userId && po.createTypeId == 1}">
										<li><a class="J_fail_regen_cusotmer" href="javascript:void(0);" customId="${po.customGroupId }" createTypeId="${po.createTypeId}" dataDate="" >统计失败重新生成</a></li>
										</c:if>
										
										<c:if test="${po.failedListCount < 1  && po.createTypeId==1 && po.updateCycle==1 && po.isPrivate != 0 && po.createUserId == userId}">
											<li><a class="J_regen_customer" customId="${po.customGroupId}" createTypeId="${po.createTypeId}" >重新生成</a></li>
										</c:if>
										</c:if>
										<c:if test="${po.isPrivate != 0}">
											<c:if test="${po.dataStatus == 3 && po.createUserId == userId}">
											<li id="share_${po.customGroupId}" c_name="${po.customGroupName}" c_cityId="${po.createCityId}" c_userId="${po.createUserId}">
												<a href="javascript:void(0);" class="J_share_operate" customId="${po.customGroupId }">共享</a>
											</li>
											</c:if>
										</c:if>
										
										<c:if test="${po.isPrivate == 0 && po.isSysRecom == 0}">
										<li id="share_${po.customGroupId}">
											<a href="javascript:void(0);" class="J_cancel_share_operate" customId="${po.customGroupId}">取消共享</a>
										</li>
										</c:if>
										<c:if test="${po.dataStatus == 3}">
											<c:if test="${(root == cityId) || (root != cityId && po.createCityId == cityId)}">
												<c:if test="${isAdmin && po.isPrivate == 0}">
												<li><a class="J_market_custom_set_recom" href="javascript:void(0);" customId="${po.customGroupId}" useTimes="${po.useTimes }">推荐设置</a></li>
												</c:if>
											</c:if>
										</c:if>
										<c:if test="${po.isLabelOffline != 1}">
										<c:if test="${isAdmin || po.createUserId == userId}">
											<c:choose>
												<c:when test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0) && (po.dataStatus == 0 || po.dataStatus == 3) && po.createTypeId==1 && (po.updateCycle == 2 || po.updateCycle == 3|| po.updateCycle == 4) }">
									               <li><a href="javascript:void(0)"; class="J_edit_group_info" customId="${po.customGroupId}" >修改</a></li>
												</c:when>
												<c:otherwise>
									               <li><a href="javascript:void(0)"; class="J_edit_custom_info" createTypeId="${po.createTypeId}" updateCycle="${po.updateCycle}" customGroupId="${po.customGroupId}">修改</a></li>
												</c:otherwise>
											</c:choose>
										</c:if>
										</c:if>
									</c:if>
									</c:if>
									<c:if test="${po.dataStatus == 3 || po.dataStatus == 0}">
									<c:if test="${isAdmin || po.createUserId == userId}">
										<li <c:if test="${po.isLabelOffline == 1 && po.updateCycle == 4}">title='此用户群不含清单且含有下线标签'</c:if>><a href="javascript:void(0)"; class="J_del_group_info" customId="${po.customGroupId}">删除</a></li>
									</c:if>
									</c:if>
									<li><a href="javascript:javascript:void(0);" class="J_customer_manager_detail"  customName="${po.customGroupName}" customId="${po.customGroupId }">查看详情</a></li>
									<!-- 不含清单且含有下线标签的用户群 有删除操作无其他操作权限 -->
									<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
										<c:if test="${po.dataStatus == 0 || po.dataStatus == 3 }">
											<% if ("true".equalsIgnoreCase(templateMenu)) { %>
											<c:if test="${po.createTypeId==1}">
												<li><a href="javascript:createTemplate('${po.customGroupId}','${po.customGroupName}','${po.sceneId}');" >提取模板</a></li>
											</c:if>
											<% } %>
										</c:if>
										<!-- 包含下线或者停用标签的用户群不能加入购物车 -->
										<c:if test="${po.isPrivate == 0}">
											<c:if test="${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }">
												<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
												<!-- 模板用户群可以加入购物车 -->
												<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
													<li><a href="javascript:void(0);" class="J_customer_manager_addCart"  customId="${po.customGroupId }">添加到收纳篮</a></li>
												</c:if>
											</c:if>
										</c:if>
										<c:if test="${po.isPrivate == 1}">
											<c:if test="${po.createUserId == userId }">
												<!-- 包含清单用户群：1、用户群数为空  && 2、数据状态为失败 不能加入购物车 -->
												<!-- 模板用户群可以加入购物车 -->
												<c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
													<li><a href="javascript:javascript:void(0);" class="J_customer_manager_addCart"  customId="${po.customGroupId }">添加到收纳篮</a></li>
												</c:if>
											</c:if>
										</c:if>
										<%-- 包含下线或者停用标签的用户群不能加入购物车 --%>
									 	</c:if>
								  	</ol>
							   	</span>
								</c:if>
						</div>
						
						
						
						<span class="fright market-customer-status">
							<em class="fleft " title="数据状态">${po.dataStatusStr}</em> 
							<em class="fleft">
							<c:if test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0) && po.dataStatus == 0 && po.createTypeId == 1 && po.createUserId == userId}">
								<a title="重新生成" class="newVersion query-item-total-icon-new fleft J_fail_regen_cusotmer" customId="${po.customGroupId}" createTypeId="${po.createTypeId}" />
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
				<div class=" market-customer-list-inner hidden">
					<div class="customer-details-info">
						<div class="market-customer-list-inner ">
							<ul class=" market-customer-list-inner clearfix">
								<li class="market-customer-attr-info first fleft">
									<span class="color_blue">编号：</span>
									<span>${po.customGroupId}</span>
								<li>	
								<li class="item-seg-line-new"></li>	
								<li class="market-customer-attr-info fleft">
									<span class="color_blue">最新修改时间：</span>
									<span><fmt:formatDate value="${po.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
								<li>
								<li class="item-seg-line-new"></li>	
								<li class="market-customer-attr-info fleft">
									<span class="color_blue">创建人：</span>
									<span>${po.createUserName}</span>
								<li>
							</ul>
							<ul class=" market-customer-list-inner clearfix">
								<li class="market-customer-attr-info first fleft">
									<span class="color_blue">创建方式：</span>
									<span>${po.createType}</span>
								<li>
								<li class="item-seg-line-new"></li>	
							    <li class="market-customer-attr-info fleft">
							    	<span class="color_blue">生成周期：</span>
							        <span>
										<c:if test="${po.updateCycle == 1}">一次性</c:if>
							        	<c:if test="${po.updateCycle == 2}">月周期</c:if>
							        	<c:if test="${po.updateCycle == 3}">日周期</c:if>
						        	</span>
								<li>
								<li class="item-seg-line-new"></li>	
								 <li class="market-customer-attr-info fleft">
								 	<span class="color_blue">是否共享：</span>
									<span>
										<c:if test="${po.isPrivate == 0}">共享</c:if>
										<c:if test="${po.isPrivate == 1}">非共享</c:if>
										<c:if test="${empty po.isPrivate }">-</c:if>
									</span> 
								<li>
								<li class="item-seg-line-new"></li>	
							    <li class="market-customer-attr-info fleft">
							    	<span class="color_blue">地市：</span>
									<span>${po.createCityName }</span>
								<li> 
							</ul>
							<ul class="market-customer-list-inner clearfix">
								<li class="market-customer-attr-info first fleft">
									<label class="color_blue">重复用户数：</label>${po.duplicateNum}
								<li> 
							</ul>
							<ul class="market-customer-list-inner clearfix">
								<li class="market-customer-attr-info fleft first">
									<span class="color_blue">描述：</span>
									<span>${po.customGroupDesc}</span>
								</li>
							</ul>
							<ul class="market-customer-list-inner clearfix">
								<li class="market-customer-attr-info first fleft ">
									<span class="fleft market-rule-title color_blue" >规则：</span>
									<span class="fleft">
										<em class="market-rule-text">
											${po.prodOptRuleShow}${po.simpleRule}${po.customOptRuleShow}
											<c:if test="${po.isOverlength == true}">
											...
											</c:if>
											${po.kpiDiffRule}
											<c:if test="${po.isOverlength == true}">
											<a href="javascript:void(0);" class="J_showRule" customId="${po.customGroupId}" >详情</a>
											</c:if>
										</em>
									</span >
								</li>
							</ul>
							<ul class="market-customer-list-inner clearfix">
								<li class="market-customer-attr-info fleft first more-text">
									<c:if test="${po.monthLabelDate != null && po.monthLabelDate != ''}">
									<span class="color_blue">数据月：</span>
									<span>${po.monthLabelDate}&nbsp;&nbsp;</span>
									</c:if>
									<c:if test="${po.dayLabelDate != null && po.dayLabelDate != ''}">
									<span class="color_blue">数据日：</span><span>${po.dayLabelDate}</span>
									</c:if>
									<c:if test="${(po.dayLabelDate == null || po.dayLabelDate == '') && (po.monthLabelDate == null || po.monthLabelDate == '')}">
									<span class="color_blue">数据日期：</span><span>${po.dataDateStr}</span>
									</c:if>
								</li>
							</ul>
							
						</div>
					</div>
				</div>
			</li>
		</c:forEach>
	</ul> 
</div> 
<input id="pageTotalSize" value="${pager.totalSize }" type="hidden"/>
<input id="cityId" type="hidden" value="${cityId }"/>
<input id="userId" type="hidden" value="${userId }"/>
<input id="root" type="hidden" value="${root }"/>
<form id="edit_custom_form" method="post" action="${ctx}/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1">
	<input id="edit_custom_input" type="hidden" name="ciCustomGroupInfo.customGroupId" />
</form>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>