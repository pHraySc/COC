<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<form id="shopCartForm">
<input name="ciCustomGroupInfo.dataDate" type="hidden" value="${newLabelMonth}" />
<input id="newLabelMonthInput" name="ciCustomGroupInfo.monthLabelDate" type="hidden" value="${newLabelMonth}" />
<input id="newLabelDayInput" name="ciCustomGroupInfo.dayLabelDate" type="hidden" value="${newLabelDay}" />
</form>
<form id="updateSessionForm">
	<div id="updateSessionDiv" style="display:none;"></div>
</form>
<c:set var="firstSign" value="1" scope="page"></c:set>
<div class="coc-cart-inner">
	<input type="hidden" value="false" class="J_is_calcucenter_to_createpage"/>
	<div class="market-cart-main J_show_cart_box"> 
		<div class="market-cart-top">
			<span class="fleft market-navigation-icon market-cart-user-icon"></span>
			<span class="fleft market-cart-user-text ellipsis-all"><span>您的目标用户 <i id="shopCartCalcuItemNum">36</i></span></span> 
			<span class="fright market-navigation-icon market-cart-goto-center" title="进入计算中心" id="openCalculateCenterBtn"></span> 
			<span class="fright market-navigation-icon market-cart-tag-del clear-cart-all" title="清空" id="clearShopCartBtn"></span>
		</div>
		<div class="market-cart-list-box">
			<div class="market-cart-list-box market-cart-of J_cart_box" id="marketCart">
				<c:choose>
					<c:when test="${empty shopCartRules}">
						<!--空购物车-->
						<div class="market-cart-list-empty">
						    <div class="add-cart-icons"></div>
							<p>双击左侧标签或点击添加按钮， 开始建立您的目标用户群吧～</p>
						</div>
					</c:when>
					<c:otherwise>
						<ul class="market-cart-list-box market-cart-of-ul J_shopCart_ul">
							<c:forEach  var="result"  items="${shopCartRules}" varStatus="status">
								<c:if test="${status.index % 2 == 0}">
									<li class="clearfix of J_shopCart_rule">
										<div class="clearfix market-cart-item <c:if test="${status.index % 4 == 0}">even</c:if> coc-relative J_shopCart_rule_div">
											<c:if test="${firstSign != 1}">
												<span class="fleft J_cart_show_op">
												<c:if test="${shopCartRules[status.index - 1].calcuElement eq 'and' }">
													<span class="fleft market-cart-operate">且</span>
									           	</c:if>
												<c:if test="${shopCartRules[status.index - 1].calcuElement eq 'or' }">
													<span class="fleft market-cart-operate">或</span>
												</c:if>
												<c:if test="${shopCartRules[status.index - 1].calcuElement eq '-' }">
													<span class="fleft market-cart-operate">剔除</span>
												</c:if>
													<span class="fleft market-navigation-icon market-cart-operate-more"></span> 
												</span>
												<div class="J_cart_operate cart-operate-dialog hidden"  sort="${shopCartRules[status.index - 1].sortNum}">
													<p class="operate-dialog-item active coc-relative"> 
														<span  class="J_shopcart_operations_text"><c:if test="${shopCartRules[status.index - 1].calcuElement eq 'and' }">且</c:if>
															<c:if test="${shopCartRules[status.index - 1].calcuElement eq 'or' }">或</c:if>
															<c:if test="${shopCartRules[status.index - 1].calcuElement eq '-' }">剔除</c:if></span>
														<span class="operate-setting-icon market-navigation-icon"></span>
													</p>
													<p class="operate-dialog-item coc-relative J_shopCart_operations"> 
														<span class="J_shopcart_operations_text">且</span>
													</p> 
													<p class="operate-dialog-item coc-relative J_shopCart_operations">
														<span  class="J_shopcart_operations_text">或</span>
													</p> 
													<p class="operate-dialog-item coc-relative J_shopCart_operations">
														<span  class="J_shopcart_operations_text">剔除</span>
													</p> 
												</div>
											</c:if>
											<c:set var="firstSign" value="0" scope="page"></c:set>
											
											<span class="fleft J_cart_show_op cart_show_none_op"></span>
											<span class="fleft market-cart-tag-name ellipsis-all J_show_cart_details" title="${result.customOrLabelName}"><c:if test="${result.elementType == 5 || result.elementType == 6}">【群】</c:if>${result.customOrLabelName}</span>
											<span class="fleft market-navigation-icon market-cart-tag-new-setting J_setting" title="设置" 
												elementType="${result.elementType}" labelFlag="${result.labelFlag}"
												labelTypeId="${result.labelTypeId}" customId="${result.customId}" effectDate="${result.effectDate}"
												attrVal="${result.attrVal }" startTime="${result.startTime }" endTime="${result.endTime }" 
												contiueMinVal="${result.contiueMinVal}" contiueMaxVal="${result.contiueMaxVal}"
												rightZoneSign="${result.rightZoneSign}" exactValue="${result.exactValue}" darkValue="${result.darkValue}" 
												customOrLabelName="${result.customOrLabelName}" leftZoneSign="${result.leftZoneSign}" 
												queryWay="${result.queryWay}" attrName="${result.attrName}" unit="${result.unit}"
												maxVal="${result.maxVal}" minVal="${result.minVal}" calcuElement="${result.calcuElement}" 
												dataDate="${result.dataDate}" updateCycle="${result.updateCycle}" 
												isHasList="${result.isHasList}" isNeedOffset="${result.isNeedOffset}"
												customCreateTypeId="${result.customCreateTypeId }" dataCycle="${result.labelFlag}" sort="${result.sortNum}">
												
												<c:if test="${result.labelTypeId == 8}">	
													<c:forEach var="item" items="${result.childCiLabelRuleList}">
											            <div class="child" elementType="${item.elementType}" labelFlag="${item.labelFlag}" 
															labelTypeId="${item.labelTypeId}" parentId="${item.parentId }" 
															calcuElement="${item.calcuElement}" customId="${item.customId}" effectDate="${item.effectDate}" 
															attrVal="${item.attrVal}" startTime="${item.startTime}" unit="${item.unit}"
															endTime="${item.endTime}" contiueMinVal="${item.contiueMinVal}" contiueMaxVal="${item.contiueMaxVal}" 
															rightZoneSign="${item.rightZoneSign}" leftZoneSign="${item.leftZoneSign}" 
															exactValue="${item.exactValue}" darkValue="${item.darkValue}" columnCnName="${item.columnCnName}"
															customOrLabelName="${item.customOrLabelName}" attrName="${item.attrName}" 
															queryWay="${item.queryWay}" maxVal="${item.maxVal}" minVal="${item.minVal}" 
															dataDate="${item.dataDate }" updateCycle="${item.updateCycle }" isNeedOffset="${item.isNeedOffset}"></div>
									            	</c:forEach>
								            	</c:if>
								            	
								            	<c:if test="${result.elementType == 6}">
								            		<c:forEach var="item" items="${result.childCiLabelRuleList}">
											            <div class="child" elementType="${item.elementType}" labelFlag="${item.labelFlag}" 
															labelTypeId="${item.labelTypeId}" parentId="${item.parentId }" 
															calcuElement="${item.calcuElement}" customId="${item.customId}" effectDate="${item.effectDate}" 
															attrVal="${item.attrVal}" startTime="${item.startTime}" unit="${item.unit}"
															endTime="${item.endTime}" contiueMinVal="${item.contiueMinVal}" contiueMaxVal="${item.contiueMaxVal}" 
															rightZoneSign="${item.rightZoneSign}" leftZoneSign="${item.leftZoneSign}" 
															exactValue="${item.exactValue}" darkValue="${item.darkValue}" columnCnName="${item.columnCnName}"
															customOrLabelName="${item.customOrLabelName}" attrName="${item.attrName}" 
															queryWay="${item.queryWay}" maxVal="${item.maxVal}" minVal="${item.minVal}" customOrLabelName="${item.customOrLabelName}"
															dataDate="${item.dataDate }" updateCycle="${item.updateCycle }" isNeedOffset="${item.isNeedOffset}">
															<c:if test="${item.labelTypeId == 8}">	
																<c:forEach var="grandson" items="${item.childCiLabelRuleList}">
														            <div class="grandson" elementType="${grandson.elementType}" labelFlag="${grandson.labelFlag}" 
																		labelTypeId="${grandson.labelTypeId}" parentId="${grandson.parentId }" 
																		calcuElement="${grandson.calcuElement}" customId="${grandson.customId}" effectDate="${grandson.effectDate}" 
																		attrVal="${grandson.attrVal}" startTime="${grandson.startTime}" unit="${grandson.unit}"
																		endTime="${grandson.endTime}" contiueMinVal="${grandson.contiueMinVal}" contiueMaxVal="${grandson.contiueMaxVal}" 
																		rightZoneSign="${grandson.rightZoneSign}" leftZoneSign="${grandson.leftZoneSign}" 
																		exactValue="${grandson.exactValue}" darkValue="${grandson.darkValue}" columnCnName="${grandson.columnCnName}"
																		customOrLabelName="${grandson.customOrLabelName}" attrName="${grandson.attrName}" 
																		queryWay="${grandson.queryWay}" maxVal="${grandson.maxVal}" minVal="${grandson.minVal}" 
																		dataDate="${grandson.dataDate}" updateCycle="${grandson.updateCycle }" isNeedOffset="${grandson.isNeedOffset}"></div>
												            	</c:forEach>
											            	</c:if>	
														</div>
									            	</c:forEach>
								            	</c:if>
											</span>
												
											<span class="fleft market-navigation-icon market-cart-tag-new-del J_shopCart_delete_single" title="删除" sort="${result.labelOrCustomSort}"></span>
										</div>
										<div class="cart-customer-details market-navigation-icon J_show_customer_detail hidden" labelTypeId="${result.labelTypeId}">
											<c:if test="${result.labelTypeId == 1}">
								               		<span title="已选： <c:if test="${result.labelFlag == 0}">否</c:if><c:if test="${result.labelFlag == 1}">是</c:if>">已选： <c:if test="${result.labelFlag == 0}">否</c:if><c:if test="${result.labelFlag == 1}">是</c:if>
								                	</span>
							              	</c:if>
											<c:if test="${result.labelTypeId == 4}">
								                <c:if test="${result.queryWay == 1}">
								               		<span title="数值范围：<c:if test="${not empty result.contiueMinVal}"><c:if test="${result.leftZoneSign eq '>=' }">大于等于${result.contiueMinVal}</c:if><c:if test="${result.leftZoneSign eq '>' }">大于${result.contiueMinVal}</c:if></c:if><c:if test="${not empty result.contiueMaxVal }"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.contiueMaxVal}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.contiueMaxVal}</c:if></c:if><c:if test="${not empty result.unit}">(${result.unit})</c:if>">
								               		数值范围：<c:if test="${not empty result.contiueMinVal}"><c:if test="${result.leftZoneSign eq '>=' }">大于等于${result.contiueMinVal}</c:if><c:if test="${result.leftZoneSign eq '>' }">大于${result.contiueMinVal}</c:if></c:if><c:if test="${not empty result.contiueMaxVal }"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.contiueMaxVal}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.contiueMaxVal}</c:if></c:if><c:if test="${not empty result.unit}">(${result.unit})</c:if>
								                	</span>
								                </c:if>
								                <c:if test="${result.queryWay == 2}">
								                	<span title="数值范围：${result.exactValue}<c:if test="${not empty result.unit}">(${result.unit})</c:if>">数值范围：${result.exactValue}<c:if test="${not empty result.unit}">(${result.unit})</c:if></span>
								                </c:if>
							              	</c:if>
											<c:if test="${result.labelTypeId == 5}">
							                	<c:if test="${not empty result.attrVal}">
													<span title="已选择条件：${result.attrName}">已选择条件：${result.attrName}</span>
												</c:if>
											</c:if>
							               
											<c:if test="${result.labelTypeId == 6}">
								                <c:if test="${result.queryWay == 1}">
													<c:if test="${not empty result.leftZoneSign || not empty result.rightZoneSign}">
								                	<span title="已选择条件：<c:if test="${not empty result.startTime}"><c:if test="${result.leftZoneSign eq '>='}">大于等于${result.startTime}</c:if><c:if test="${result.leftZoneSign eq '>'}">大于${result.startTime}</c:if></c:if><c:if test="${not empty result.endTime}"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.endTime}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.endTime}</c:if></c:if><c:if test="${result.isNeedOffset == 1}">（动态偏移更新）</c:if>">
								                	已选择条件：<c:if test="${not empty result.startTime}"><c:if test="${result.leftZoneSign eq '>='}">大于等于${result.startTime}</c:if><c:if test="${result.leftZoneSign eq '>'}">大于${result.startTime}</c:if></c:if><c:if test="${not empty result.endTime}"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.endTime}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.endTime}</c:if></c:if><c:if test="${result.isNeedOffset == 1}">（动态偏移更新）</c:if>
								                	</span>
								                	</c:if>
								                </c:if>
								                <c:if test="${result.queryWay == 2}">
								                	<c:if test="${not empty result.exactValue }">
								                	<span title="已选择条件：<c:if test="${not empty fn:split(result.exactValue,',')[0] && fn:split(result.exactValue,',')[0]!='-1'}">${fn:split(result.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(result.exactValue,',')[1] && fn:split(result.exactValue,',')[1]!='-1'}">${fn:split(result.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(result.exactValue,',')[2] && fn:split(result.exactValue,',')[2]!='-1'}">${fn:split(result.exactValue,',')[2]}日</c:if>">
								                	已选择条件：<c:if test="${not empty fn:split(result.exactValue,',')[0] && fn:split(result.exactValue,',')[0]!='-1'}">${fn:split(result.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(result.exactValue,',')[1] && fn:split(result.exactValue,',')[1]!='-1'}">${fn:split(result.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(result.exactValue,',')[2] && fn:split(result.exactValue,',')[2]!='-1'}">${fn:split(result.exactValue,',')[2]}日</c:if></span>
								                	</c:if>
								                </c:if>
											</c:if>
							              	<c:if test="${result.labelTypeId ==7 }">
							               		<c:if test="${result.queryWay == 1}">
								               		<c:if test="${not empty result.darkValue}">
								               			<span title="模糊值：${result.darkValue}">模糊值：${result.darkValue}</span>
								               		</c:if>
							               		</c:if>
							               		<c:if test="${result.queryWay == 2}">
							               			<c:if test="${not empty result.exactValue}">
								               			<span title="精确值：${result.exactValue}">精确值：${result.exactValue}</span>
								               		</c:if>
							               		</c:if>
							              	</c:if>
							              	<c:if test="${result.labelTypeId == 8}">
							              		<c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}">${item.columnCnName}：<c:if test="${not empty item.contiueMinVal}"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}${item.unit}</c:if></c:if>;</c:if><c:if test="${item.queryWay == 2}"> ${item.columnCnName}：${item.exactValue}${item.unit};</c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">${item.columnCnName}：${item.attrName};</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.leftZoneSign || not empty item.rightZoneSign}">${item.columnCnName}：<c:if test="${not empty item.startTime}"><c:if test="${item.leftZoneSign eq '>='}">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>'}">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime}"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if>;</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">${item.columnCnName}：<c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if>；</c:if></c:if></c:if><c:if test="${item.labelTypeId ==7 }"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">${item.columnCnName}：${item.darkValue};</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">${item.columnCnName}：${item.exactValue}</c:if></c:if></c:if></c:forEach>
							              	</c:if>
							              	<c:if test="${result.elementType ==5}">
								              	<c:if test="${not empty result.attrVal}">
													<span title="已选择清单：${result.attrVal}">已选择清单：${result.attrVal}</span>
								               	</c:if>
							               	</c:if>
							               	<c:if test="${result.elementType == 6}">
							               		<span title="<c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if test="${item.elementType == 3}"><c:if test="${item.calcuElement == '(' }"><c:out value="("></c:out></c:if><c:if test="${item.calcuElement == ')' }"><c:out value=") "></c:out></c:if></c:if><c:if test="${item.elementType == 1}"><c:if test="${item.calcuElement == 'and' }"><c:out value="且 "></c:out></c:if><c:if test="${item.calcuElement == 'or' }"><c:out value="或 "></c:out></c:if><c:if test="${item.calcuElement == '-' }"><c:out value="剔除 "></c:out></c:if></c:if><c:if test="${item.elementType == 2}"><c:if test="${item.labelFlag != 1}"><c:out value="(非)"></c:out></c:if><c:out value="${item.customOrLabelName}"></c:out><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}"><c:out value="[数值范围："></c:out><c:if test="${not empty item.contiueMinVal }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if test="${not empty item.unit}">(${item.unit})</c:if><c:out value="]"></c:out></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[数值范围：${item.exactValue}<c:if test="${item.unit}">(${item.unit})</c:if>]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">[已选择条件：${item.attrName}]</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.startTime || not empty item.endTime}"><c:out value="[已选择条件："></c:out><c:if test="${not empty item.startTime }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }"><c:out value="[已选择条件："></c:out><c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if><c:out value="]"></c:out></c:if></c:if></c:if><c:if test="${item.labelTypeId == 7}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">[模糊值：${item.darkValue}]</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[精确值：${item.exactValue}]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 8}"><c:forEach var="item" items="${item.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}">[${item.columnCnName}：<c:if test="${not empty item.contiueMinVal }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if test="${not empty item.unit}">(${item.unit})</c:if>]</c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}<c:if test="${not empty item.unit}">(${item.unit})</c:if>]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">[${item.columnCnName}：${item.attrName}]</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.startTime || not empty item.endTime}">[${item.columnCnName}：<c:if test="${not empty item.startTime }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">[${item.columnCnName}：<c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if><c:out value="]"></c:out></c:if></c:if></c:if><c:if test="${item.labelTypeId == 7}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">[${item.columnCnName}：${item.darkValue}]</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}]</c:if></c:if></c:if></c:forEach></c:if></c:if><c:if test="${item.elementType == 5}"><c:out value="用户群：${item.customOrLabelName}"></c:out><c:if test="${not empty item.attrVal}"><c:out value="[已选清单：${item.attrVal}]"></c:out></c:if></c:if></c:forEach>">
								              	<c:forEach var="result" items="${result.childCiLabelRuleList}">
													<c:if test="${result.elementType == 3}">
														<c:if test="${result.calcuElement == '(' }">
														<c:out value="("></c:out>
														</c:if>
														<c:if test="${result.calcuElement == ')' }">
														<c:out value=") "></c:out>
														</c:if>
										            </c:if>
										            <c:if test="${result.elementType == 1}">
										               	<c:if test="${result.calcuElement == 'and' }">
										               	<c:out value="且 "></c:out>
										               	</c:if>
										                   	<c:if test="${result.calcuElement == 'or' }">
										                   	<c:out value="或 "></c:out>
										               	</c:if>
										               	<c:if test="${result.calcuElement == '-' }">
										               	<c:out value="剔除 "></c:out>
										               	</c:if>
										            </c:if>
										            <c:if test="${result.elementType == 2}">
														<c:if test="${result.labelFlag != 1}">
														<c:out value="(非)"></c:out></c:if><c:out value="${result.customOrLabelName}"></c:out><c:if test="${result.labelTypeId == 4}"><c:if test="${result.queryWay == 1}"><c:out value="[数值范围："></c:out><c:if test="${not empty result.contiueMinVal }"><c:if test="${result.leftZoneSign eq '>=' }">大于等于${result.contiueMinVal}</c:if><c:if test="${result.leftZoneSign eq '>' }">大于${result.contiueMinVal}</c:if></c:if><c:if test="${not empty result.contiueMaxVal }"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.contiueMaxVal}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.contiueMaxVal}</c:if></c:if><c:if test="${not empty result.unit}">(${result.unit})</c:if><c:out value="]"></c:out></c:if><c:if test="${result.queryWay == 2}"><c:if test="${not empty result.exactValue}">[数值范围：${result.exactValue}<c:if test="${result.unit}">(${result.unit})</c:if>]</c:if></c:if></c:if><c:if test="${result.labelTypeId == 5}"><c:if test="${not empty result.attrVal}">[已选择条件：${result.attrName}]</c:if></c:if><c:if test="${result.labelTypeId == 6}"><c:if test="${result.queryWay == 1}"><c:if test="${not empty result.startTime || not empty result.endTime}"><c:out value="[已选择条件："></c:out><c:if test="${not empty result.startTime }"><c:if test="${result.leftZoneSign eq '>=' }">大于等于${result.startTime}</c:if><c:if test="${result.leftZoneSign eq '>' }">大于${result.startTime}</c:if></c:if><c:if test="${not empty result.endTime }"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.endTime}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.endTime}</c:if></c:if><c:if test="${result.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if test="${result.queryWay == 2}"><c:if test="${not empty result.exactValue }"><c:out value="[已选择条件："></c:out><c:if test="${not empty fn:split(result.exactValue,',')[0] && fn:split(result.exactValue,',')[0]!='-1'}">${fn:split(result.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(result.exactValue,',')[1] && fn:split(result.exactValue,',')[1]!='-1'}">${fn:split(result.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(result.exactValue,',')[2] && fn:split(result.exactValue,',')[2]!='-1'}">${fn:split(result.exactValue,',')[2]}日</c:if><c:out value="]"></c:out></c:if></c:if></c:if><c:if test="${result.labelTypeId == 7}"><c:if test="${result.queryWay == 1}"><c:if test="${not empty result.darkValue}">[模糊值：${result.darkValue}]</c:if></c:if><c:if test="${result.queryWay == 2}"><c:if test="${not empty result.exactValue}">[精确值：${result.exactValue}]</c:if></c:if></c:if><c:if test="${result.labelTypeId == 8}"><c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}">[${item.columnCnName}：<c:if test="${not empty item.contiueMinVal }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if test="${not empty item.unit}">(${item.unit})</c:if>]</c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}<c:if test="${not empty item.unit}">(${item.unit})</c:if>]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">[${item.columnCnName}：${item.attrName}]</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.startTime || not empty item.endTime}">[${item.columnCnName}：<c:if test="${not empty item.startTime }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">[${item.columnCnName}：<c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if><c:out value="]"></c:out></c:if></c:if></c:if><c:if test="${item.labelTypeId == 7}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">[${item.columnCnName}：${item.darkValue}]</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}]</c:if>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
													</c:if>
													<c:if test="${result.elementType == 5}">
										            	<c:out value="用户群：${result.customOrLabelName}"></c:out><c:if test="${not empty result.attrVal}"><c:out value="[已选清单：${result.attrVal}]"></c:out>
										            	</c:if>
													</c:if>
												</c:forEach>
												</span>
							               	</c:if>
										</div>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>
			<ul class="market-cart-list-box">
				<li class="show-cart-user-number">
					<span>用户数</span>
					<span id="customGroupNumSpan" class="cus-num-box">0</span>
				</li>
			</ul>
			<ul class="market-cart-list-box">
				<li class="market-cart-buttons">
					<c:if test="${firstSign != 1}">
					<a class="cus-rule-op fleft" href="javascript:void(0);" id="showShopCartRule">规则预览</a>
					</c:if>
					<c:if test="${firstSign != 1}">
					<a class="cus-rule-op fleft" href="javascript:void(0);" id="customGroupExploreBtn">客户群规模</a>
					</c:if>
					<button class="fright market-cart-button<c:if test="${firstSign == 1}">-disable</c:if>" 
						type="button" id="saveCustomGroupBtn">保存客户群</button>
				</li>
			</ul>
		</div>
	</div>
</div>
