<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
	<head>
		<title>用户群完整规则</title>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<script type="text/javascript">
			$(function(){
				//规则滚动条
				$("#ruleBox").mCustomScrollbar({
					theme:"minimal-dark", //滚动条样式
					scrollbarPosition:"outside", //滚动条位置
					scrollInertia:500,
					mouseWheel:{
						preventDefault:true //阻止冒泡事件
					}
				});
			});
		</script>
	</head>
	<body>
		<div class="clientMsgBox rule-title">
			<ol class="clearfix">
				<li>
		   			<label class="fleft" style="marggin-left:10px;"><b>待创建用户群</b></label>
		 		</li>
		 	</ol>
		</div>
		<div class="ruleBox" id="ruleBox">
			<div class="ruleBoxInner fleft">
				<c:forEach var="result" items="${sessionModelList}">
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
					<c:if test="${result.elementType == 6}">
						（<c:forEach var="item" items="${result.childCiLabelRuleList}">
							<c:if test="${item.elementType == 3}">
								<c:if test="${item.calcuElement == '(' }">
								<c:out value="("></c:out>
								</c:if>
								<c:if test="${item.calcuElement == ')' }">
								<c:out value=") "></c:out>
								</c:if>
				            </c:if>
				            <c:if test="${item.elementType == 1}">
			                	<c:if test="${item.calcuElement == 'and' }">
			                	<c:out value="且 "></c:out>
			                	</c:if>
			                    	<c:if test="${item.calcuElement == 'or' }">
			                    	<c:out value="或 "></c:out>
			                	</c:if>
			                	<c:if test="${item.calcuElement == '-' }">
			                	<c:out value="剔除 "></c:out>
			                	</c:if>
				            </c:if>
				            <c:if test="${item.elementType == 2}">
								<c:if test="${item.labelFlag != 1}">
								<c:out value="(非)"></c:out></c:if><c:out value="${item.customOrLabelName}"></c:out><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}"><c:out value="[数值范围："></c:out><c:if test="${not empty item.contiueMinVal }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if test="${not empty item.unit}">(${item.unit})</c:if><c:out value="]"></c:out></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[数值范围：${item.exactValue}<c:if test="${item.unit}">(${item.unit})</c:if>]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">[已选择条件：${item.attrName}]</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.startTime || not empty item.endTime}"><c:out value="[已选择条件："></c:out><c:if test="${not empty item.startTime }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }"><c:out value="[已选择条件："></c:out><c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if><c:out value="]"></c:out></c:if></c:if></c:if><c:if test="${item.labelTypeId == 7}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">[模糊值：${item.darkValue}]</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[精确值：${item.exactValue}]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 8}"><c:forEach var="item" items="${item.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}">[${item.columnCnName}：<c:if test="${not empty item.contiueMinVal }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if test="${not empty item.unit}">(${item.unit})</c:if>]</c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}<c:if test="${not empty item.unit}">(${item.unit})</c:if>]</c:if></c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">[${item.columnCnName}：${item.attrName}]</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.startTime || not empty item.endTime}">[${item.columnCnName}：<c:if test="${not empty item.startTime }"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">[${item.columnCnName}：<c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if><c:out value="]"></c:out></c:if></c:if></c:if><c:if test="${item.labelTypeId == 7}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">[${item.columnCnName}：${item.darkValue}]</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}]</c:if>
								</c:if>
								</c:if>
								</c:forEach>
								</c:if>
							</c:if>
							<c:if test="${item.elementType == 5}">
				            	<c:out value="用户群：${item.customOrLabelName}"></c:out><c:if test="${not empty item.attrVal}"><c:out value="[已选清单：${item.attrVal}]"></c:out>
				            	</c:if>
							</c:if>
						</c:forEach>）
					</c:if>
				</c:forEach>
			</div>
	    </div>
	</body>
</html>

