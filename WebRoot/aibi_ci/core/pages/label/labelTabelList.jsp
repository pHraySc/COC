<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="LABEL_TYPE_SIGN" value="<%=ServiceConstants.LABEL_TYPE_SIGN%>"></c:set>
<ul class="market-customer-list-inner">
	<c:forEach items="${labelDetailList}" var="label" >
		<li class="market-customer-item"> 
			<div class="clearfix market-customer-details J_customer_datils">
				<div class="fleft market-customer-info customer-name">
					
					<span class="fleft market-info-text ellipsis-all J_label_market_addCart" labelId="${label.labelId}" title="${label.labelName}">${label.labelName} </span>
					<ol class="hotState fleft newVersion" id="custom_label_sign_${label.labelId }">
						<c:if test="${label.isSysRecom!=null && label.isSysRecom==1 }">
							<li><a class="organ"></a></li>
						</c:if>
						<c:if test="${label.isHot=='true' }">
							<li><a class="hot"></a></li>
						</c:if>
						<li <c:if test="${label.isAttention=='true' }">style="display: block"</c:if><c:if test="${label.isAttention=='false' }">style="display: none"</c:if> id="isAttentionShow_${label.labelId}">
							<a  class="hideFont"></a>
						</li>
					</ol>
					<input type="hidden" id="isAttention_${label.labelId}" value="${label.isAttention}" />
					<%-- <c:if  test="${label.isAttention == 'true'}">
						<span class="collectStar fleft newVersion" labelorcustomid="${label.labelId }" isattention="true">
							<img class="J_label_cancel_attention" title="点击可取消收藏此标签" id="attentionImg_${label.labelId }" labelid="${label.labelId }" style="cursor: pointer;" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png">
						</span>
					</c:if>
					<c:if  test="${label.isAttention == 'false'}">
						<span class="collectStar fleft newVersion" labelorcustomid="${label.labelId }" isattention="false">
							<img class="J_label_attention" title="点击可收藏此标签" id="attentionImg_${label.labelId }" labelid="${label.labelId }" style="cursor: pointer;" src="${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png">
						</span>
					</c:if> --%>
				</div>
				<div class="fright market-customer-info">
					<div class="fright market-info-list-box ">
						<a class="market-info-list-text J_label_market_list_add_cart" href="javascript:void(0);" labelId="${label.labelId}">添加</a>
						<div></div>
						<a class="market-info-list-btn J_label_market_operation_btn"  labelId="${label.labelId }"></a>
						<span class="resultItemActiveList" id="operate_${label.labelId }">
							<ol class="clearfix">
								<!-- 收藏或取消收藏 begin-->
								<c:if  test="${label.isAttention == 'true'}">
									<li>
										<a class="J_label_cancel_attention" id="attentionImg_${label.labelId }"  labelid="${label.labelId }">取消收藏</a>
									</li>
								</c:if>
								<c:if  test="${label.isAttention == 'false'}">
									<li>
										<a class="J_label_attention" id="attentionImg_${label.labelId }"  labelid="${label.labelId }">收藏</a>
									</li>
								</c:if>
								<!-- 收藏或取消收藏 end-->
								<li><a class="J_label_market_set_recom" href="javascript:void(0);" labelId="${label.labelId}" useTimes="${label.useTimes }">推荐设置</a></li>
		           	  			<li><a class="J_label_market_list_add_cart" href="javascript:void(0);" labelId="${label.labelId}">添加到收纳篮</a></li>
							</ol>
						</span>
					</div>
					<!-- <span class="fright market-info-right-icon market-navigation-icon J_label_market_show_arrow"></span> -->
					<%-- <span class="fright market-customer-operate resultItemActive" labelId="${label.labelId }">
					    <span class="dsts">操作</span>
					   	<span class="resultItemActiveList" id="operate_${label.labelId }">
						<ol class="clearfix">
							<!-- 收藏或取消收藏 begin-->
							<c:if  test="${label.isAttention == 'true'}">
								<li>
									<a class="J_label_cancel_attention" id="attentionImg_${label.labelId }"  labelid="${label.labelId }">取消收藏</a>
								</li>
							</c:if>
							<c:if  test="${label.isAttention == 'false'}">
								<li>
									<a class="J_label_attention" id="attentionImg_${label.labelId }"  labelid="${label.labelId }">收藏</a>
								</li>
							</c:if>
							<!-- 收藏或取消收藏 end-->
							<li><a class="J_label_market_set_recom" href="javascript:void(0);" labelId="${label.labelId}" useTimes="${label.useTimes }">推荐设置</a></li>
	           	  			<li><a class="J_label_market_list_add_cart" href="javascript:void(0);" labelId="${label.labelId}">添加到收纳篮</a></li>
						</ol>
						</span>
					</span> --%>
					<span class="fright market-customer-date-box">
						<em>数据日期：</em>
						<em>
							<c:if test="${label.dataDate != null && label.dataDate != ''}">
								${dateFmt:dateFormat(label.dataDate)}&nbsp;
							</c:if>
						</em>
					</span> 
					<span class="fright">
						<em>更新周期：</em>
						<em>
							<c:if test="${label.updateCycle == 1}">日周期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:if>
							<c:if test="${label.updateCycle == 2}">月周期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:if>
						</em>
					</span> 
				</div>
			</div> 
			<div class=" market-customer-list-inner hidden">
				<div class="customer-details-info">
					<div class="market-customer-list-inner ">
						<ul class=" market-customer-list-inner clearfix">
							<c:if test="${label.labelTypeId == LABEL_TYPE_SIGN  && label.customNum != null &&  label.customNum != ''}">
							<li class="market-customer-attr-info fleft first">
								<span class="color_blue">用户数：</span>
								<span><fmt:formatNumber  type="number" value="${label.customNum }" pattern="###,###,###"></fmt:formatNumber></span>
							<li>
							</c:if>
						</ul>
						<ul class="market-customer-list-inner clearfix">
							<li class="market-customer-attr-info fleft first">
							<span class="color_blue">业务口径：</span>
							<span>${label.busiCaliber}</span>
							<li>
						</ul>
						<ul class="market-customer-list-inner clearfix">
							<li class="market-customer-attr-info fleft first">
							<span class="color_blue">标签路径：</span>
							<span>${label.currentLabelPath}</span>
							<li>
						</ul>
					</div>
				</div>
			</div>
		</li>
	</c:forEach>
</ul> 
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
</div>
<input type="hidden" id="labelPageTotalSize" value="${pager.totalSize}"/>
		