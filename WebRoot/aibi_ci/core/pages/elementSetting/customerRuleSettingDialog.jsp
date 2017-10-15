<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<script type="text/javascript">
$(function() {
	var target = parent.$("._idClass");
	var custSortNum = target.attr("sort");
	$(".J_custSortNum").val(custSortNum);
	//枚举型标签设置dialog
	$("#enumLabelSetting").dialog({ 
		width:710,
		autoOpen: false, 
		modal: true, 
		title:"条件设置", 
		dialogClass:"ui-dialogFixed",
		resizable:false, 
		draggable: true,
		close:function(event, ui){
			$("#enumLabelSettingFrame").attr("src", "");
		}
	});
	//文本型标签设置dialog
	$("#textLabelSetting").dialog({ 
		width:570,
		autoOpen: false, 
		modal: true, 
		title:"条件设置", 
		dialogClass:"ui-dialogFixed",
		resizable:false,
		draggable: true,
		close:function(event, ui){
			$("#textLabelSettingFrame").attr("src", "");
		}
	});
	//组合型标签设置dialog
	$("#vertLabelSetting").dialog({
		width:720,
		dialogClass:"ui-dialogFixed",
		autoOpen: false, 
		modal: true,
		title:"组合标签条件设置", 
		resizable:false, 
		draggable: true,
		close:function(event, ui){
			$("#vertLabelSettingFrame").attr("src", "");
		}
	});
})
</script>

<%-- <c:if test="${source == 2}">
<script type="text/javascript">
	var _parenthesisLeft = getCurlyBraceHTML("(",1,null,true);
	$("#ruleHidden").append(_parenthesisLeft);
	$("#ruleHidden").find("script").remove();
</script>
</c:if> --%>
<c:forEach var="result" items="${ciLabelRuleList}">
	<input type="hidden" class="J_custSortNum" name="custSortNum" value=""/>
	<!-- 处理括号 -->
	<c:if test="${result.elementType == 3}">
		<!-- <script type="text/javascript">
			var _parenthesis = getCurlyBraceHTML("${result.calcuElement}",1,null,true);
			$("#ruleHidden").append(_parenthesis);
			$("#ruleHidden").find("script").remove();
		</script> -->
		<c:if test="${result.calcuElement == '(' }">
			<span class="parenthesis leftParenthesis" selfid="curly_brace_block" calcuElement="${result.calcuElement }"
				creat="${result.createBrackets }"></span>
		</c:if>
		<c:if test="${result.calcuElement == ')' }">
			<span class="parenthesis rightParenthesis" calcuElement="${result.calcuElement }"
				creat="${result.createBrackets}" selfid="curly_brace_block">
				<span class="delCondition" onclick="deletePar(this,event)"></span>
	        </span>
		</c:if>
    </c:if>
	<!-- 处理操作运算符 -->
	<c:if test="${result.elementType == 1}">
		<div class="chaining" sortnum="${result.sortNum}">
           	<c:if test="${result.calcuElement eq 'and' }">
           		<span onmousedown="switchConnector_turn(this,event)">且</span>
           	</c:if>
			<c:if test="${result.calcuElement eq 'or' }">
				<span onmousedown="switchConnector_turn(this,event)">或</span>
              	</c:if>
			<c:if test="${result.calcuElement eq '-' }">
				<span onmousedown="switchConnector_turn(this,event)">剔除</span>
              	</c:if>
			<a class="icon_rightArr" href="javascript:void(0);" onclick="switchConnector(this,event)">&nbsp;</a>
		</div>
	</c:if>
	 
	<c:if test="${result.elementType == 2}">
		<c:if test="${result.labelTypeId == 1 || result.labelTypeId == null}">
			<div class="conditionCT conditionReverse conditionCT_short <c:if test="${result.labelFlag != 1 && result.labelFlag != null}">conditionCT_active</c:if>" _statusIndex="labelIndex_${st.index}" elementType="${result.elementType}" labelFlag="${result.labelFlag }"
           		labelTypeId="${result.labelTypeId}" customId="${result.customId}" effectDate="${result.effectDate}"
           		attrVal="${result.attrVal }" startTime="${result.startTime }" endTime="${result.endTime }" 
           		contiueMinVal="${result.contiueMinVal }" contiueMaxVal="${result.contiueMaxVal }"
           		rightZoneSign="${result.rightZoneSign }" exactValue="${result.exactValue }" darkValue="${result.darkValue }" 
           		customOrLabelName="${result.customOrLabelName}" leftZoneSign="${result.leftZoneSign }" 
           		queryWay="${result.queryWay }" attrName="${result.attrName }" unit="${result.unit}"
           		maxVal="${result.maxVal}" minVal="${result.minVal}" calcuElement="${result.calcuElement}" 
           		dataDate="${result.dataDate }" updateCycle="${result.updateCycle }" customOrLabelName="${result.customOrLabelName}">
              	<div class="left_bg">&nbsp;</div>
               	<div class="midCT" onclick="coreMain.reverseLabel(this);">
               		${result.customOrLabelName}
               		<span twins="labelTip" labelId="${result.calcuElement}" 
               			class="conditionTipIcon labelConditionTipIcon" 
               			onclick="coreMain.labelTip(event, this);">&nbsp;</span>
               	</div>
               	<div class="right_bg"><!-- <a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a> --></div>
			</div>
		</c:if>
          	
		<c:if test="${result.labelTypeId > 3 && result.labelTypeId < 8}">
			<div class="conditionCT conditionReverse <c:if test="${result.labelFlag != 1 && result.labelFlag != null}">conditionCT_active</c:if>" _statusIndex="labelIndex_${st.index}" elementType="${result.elementType}" labelFlag="${result.labelFlag }"
          		labelTypeId="${result.labelTypeId}" customId="${result.customId}" effectDate="${result.effectDate}"
          		attrVal="${result.attrVal }" startTime="${result.startTime }" endTime="${result.endTime }" 
          		contiueMinVal="${result.contiueMinVal }" contiueMaxVal="${result.contiueMaxVal }" 
          		rightZoneSign="${result.rightZoneSign }" exactValue="${result.exactValue }" darkValue="${result.darkValue }" 
          		customOrLabelName="${result.customOrLabelName}" leftZoneSign="${result.leftZoneSign }" 
          		queryWay="${result.queryWay }" attrName="${result.attrName }" unit="${result.unit}"
          		maxVal="${result.maxVal}" minVal="${result.minVal}" calcuElement="${result.calcuElement}" 
          		dataDate="${result.dataDate }" updateCycle="${result.updateCycle}" isNeedOffset="${result.isNeedOffset}" customOrLabelName="${result.customOrLabelName}">
			<div class="left_bg">
				<a class="setting" onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)" ></a>
                  	<!-- <a class="delLabel" href="javascript:void(0);"></a> -->
              	</div>
              	<div class="midCT" onclick="coreMain.reverseLabel(this);">
              		${result.customOrLabelName}
              		<span twins="labelTip" labelId="${result.calcuElement}" 
              			class="conditionTipIcon labelConditionTipIcon" 
              			onclick="coreMain.labelTip(event, this);">&nbsp;</span></div>
              	<div class="right_bg"><!-- <a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a> --></div>
               
              	<c:if test="${result.labelTypeId == 4}">
				<div class="conditionMore" onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)">
                <c:if test="${result.queryWay == 1}">
               		<p><label>数值范围：</label><span><c:if test="${not empty result.contiueMinVal}"><c:if test="${result.leftZoneSign eq '>=' }">大于等于${result.contiueMinVal}</c:if><c:if test="${result.leftZoneSign eq '>' }">大于${result.contiueMinVal}</c:if></c:if><c:if test="${not empty result.contiueMaxVal }"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.contiueMaxVal}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.contiueMaxVal}</c:if></c:if><c:if test="${not empty result.unit}">(${result.unit})</c:if>
                	</span></p>
                </c:if>
                <c:if test="${result.queryWay == 2}">
                	<p><label>数值范围：</label><span>${result.exactValue}<c:if test="${not empty result.unit}">(${result.unit})</c:if></span></p>
                </c:if>
                </div>
              	</c:if>
			<c:if test="${result.labelTypeId == 5}">
				<div class="conditionMore" onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)" title="${result.attrName}">
                	<c:if test="${not empty result.attrVal}">
					<p><label>已选择条件：</label><span><c:out value="${result.attrName}"></c:out></span></p>
					</c:if>
				</div>
			</c:if>
               
			<c:if test="${result.labelTypeId == 6}">
                <div class="conditionMore" onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)">
	                <c:if test="${result.queryWay == 1}">
						<c:if test="${not empty result.leftZoneSign || not empty result.rightZoneSign}">
	                	<p><label>已选择条件：</label><span><c:if test="${not empty result.startTime}"><c:if test="${result.leftZoneSign eq '>='}">大于等于${result.startTime}</c:if><c:if test="${result.leftZoneSign eq '>'}">大于${result.startTime}</c:if></c:if><c:if test="${not empty result.endTime}"><c:if test="${result.rightZoneSign eq '<='}">小于等于${result.endTime}</c:if><c:if test="${result.rightZoneSign eq '<'}">小于${result.endTime}</c:if></c:if><c:if test="${result.isNeedOffset == 1}">（动态偏移更新）</c:if>
	                	</span></p>
	                	</c:if>
	                </c:if>
	                <c:if test="${result.queryWay == 2}">
	                	<c:if test="${not empty result.exactValue }">
	                	<p><label>已选择条件：</label><span><c:if test="${not empty fn:split(result.exactValue,',')[0] && fn:split(result.exactValue,',')[0]!='-1'}">${fn:split(result.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(result.exactValue,',')[1] && fn:split(result.exactValue,',')[1]!='-1'}">${fn:split(result.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(result.exactValue,',')[2] && fn:split(result.exactValue,',')[2]!='-1'}">${fn:split(result.exactValue,',')[2]}日</c:if></span></p>
	                	</c:if>
	                </c:if>
                </div>
			</c:if>
              	<c:if test="${result.labelTypeId ==7 }">
               	<div class="conditionMore" onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)" title="<c:if test="${result.queryWay == 1}"><c:if test="${not empty result.darkValue}">模糊值：${result.darkValue}</c:if></c:if><c:if test="${result.queryWay == 2}"><c:if test="${not empty result.exactValue}">精确值：${result.exactValue}</c:if></c:if>">
               		<c:if test="${result.queryWay == 1}">
	               		<c:if test="${not empty result.darkValue}">
	               			<p><label>模糊值：</label><span>${result.darkValue}</span></p>
	               		</c:if>
               		</c:if>
               		<c:if test="${result.queryWay == 2}">
               			<c:if test="${not empty result.exactValue}">
	               			<p><label>精确值：</label><span>${result.exactValue}</span></p>
	               		</c:if>
               		</c:if>
               	</div>
              	</c:if>
			</div>
		</c:if>
           
		<c:if test="${result.labelTypeId == 8}">
          	<div class="conditionCT <c:if test="${result.labelFlag != 1 && result.labelFlag != null}">conditionCT_active</c:if>" _statusIndex="labelIndex_${st.index}" elementType="${result.elementType}" labelFlag="${result.labelFlag }" 
          		labelTypeId="${result.labelTypeId}" customId="${result.customId}" effectDate="${result.effectDate}"
          		attrVal="${result.attrVal }" startTime="${result.startTime }" endTime="${result.endTime }" 
          		contiueMinVal="${result.contiueMinVal }" contiueMaxVal="${result.contiueMaxVal }" 
          		rightZoneSign="${result.rightZoneSign }" exactValue="${result.exactValue}" darkValue="${result.darkValue }" 
          		customOrLabelName="${result.customOrLabelName}" leftZoneSign="${result.leftZoneSign }" 
          		queryWay="${result.queryWay }" attrName="${result.attrName }" unit="${result.unit}"
          		maxVal="${result.maxVal}" minVal="${result.minVal}" calcuElement="${result.calcuElement}" 
          		dataDate="${result.dataDate }" updateCycle="${result.updateCycle }" isNeedOffset="${result.isNeedOffset}" customOrLabelName="${result.customOrLabelName}">
				<div class="left_bg">
					<a onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)" class="setting"></a>
               	</div>
               	<div class="midCT">
               		${result.customOrLabelName}
               		<span twins="labelTip" labelId="${result.calcuElement}" 
               		class="conditionTipIcon labelConditionTipIcon" 
               		onclick="coreMain.labelTip(event, this);">&nbsp;</span>
               	</div>
               	<div class="right_bg"><!-- <a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a> --></div>
               
               	<div class="conditionMore" onclick="ShoppingCart.setElementObjAttr(this, ${result.labelTypeId},'${result.customOrLabelName}',2)" title="<c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${result.queryWay == 1}">${item.columnCnName}：<c:if test="${not empty item.contiueMinVal}"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}${item.unit}</c:if></c:if>;</c:if><c:if test="${item.queryWay == 2}"> ${item.columnCnName}：${item.exactValue}${item.unit};</c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">${item.columnCnName}：${item.attrName};</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.leftZoneSign || not empty item.rightZoneSign}">${item.columnCnName}：<c:if test="${not empty item.startTime}"><c:if test="${item.leftZoneSign eq '>='}">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>'}">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime}"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if>;</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">${item.columnCnName}：<c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if>；</c:if></c:if></c:if><c:if test="${item.labelTypeId ==7 }"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">${item.columnCnName}：${item.darkValue};</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">${item.columnCnName}：${item.exactValue}</c:if></c:if></c:if></c:forEach>">
               		<p>
               		<c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}"><label>${item.columnCnName}：</label><span><c:if test="${not empty item.contiueMinVal}"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if test="${not empty item.unit}">(${item.unit})</c:if></span>；</c:if><c:if test="${item.queryWay == 2}"><label>${item.columnCnName}：</label><span>${item.exactValue}<c:if test="${not empty item.unit}">(${item.unit})</c:if></span>；</c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}"><label>${item.columnCnName}：</label><span><c:out value="${item.attrName}"></c:out></span>；</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.leftZoneSign || not empty item.rightZoneSign}"><label>${item.columnCnName}：</label><span><c:if test="${not empty item.startTime}"><c:if test="${item.leftZoneSign eq '>='}">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>'}">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime}"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if></c:if></span>；</c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }"><label>${item.columnCnName}：</label><span><c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if>；</span></c:if></c:if></c:if><c:if test="${item.labelTypeId ==7 }"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}"><label>${item.columnCnName}：</label><span>${item.darkValue}</span>；</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}"><label>${item.columnCnName}：</label><span>${item.exactValue}</span>；</c:if></c:if></c:if></c:forEach></p>
               	</div>
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
           	</div>
		</c:if>
	</c:if>
	
	<c:if test="${result.elementType == 5}">
		<div class="conditionCT" _statusIndex="customerIndex_${st.index}" elementType="${result.elementType}" calcuElement="${result.calcuElement}" 
			customOrLabelName="${result.customOrLabelName}" attrVal="${result.attrVal }"
			customCreateTypeId="${result.customCreateTypeId }" customId="${result.customId}"  dataCycle="${result.labelFlag}" customOrLabelName="${result.customOrLabelName}">
			<div class="left_bg">
				<!--  <a class="setting" onclick="ShoppingCart.setElementObjAttr(this,10,'${result.customOrLabelName}',2)"></a>
				<a class="delLabel" href="javascript:void(0);"></a> -->
				<a class="setting" onclick="ShoppingCart.setElementObjAttr(this,10,'${result.customOrLabelName}',2)"  data=""></a>
			</div>
			<div class="midCT">
				【群】${result.customOrLabelName}
				<span twins="customTip" customId="${result.customId}" class="conditionTipIcon" onclick="coreMain.customTip(event, this)">&nbsp;</span>
			</div>
			<div class="right_bg"><!-- <a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a> --></div>
			<div class="conditionMore" onclick="ShoppingCart.setElementObjAttr(this,10,'${result.customOrLabelName}',2)">
				<c:if test="${not empty result.attrVal}">
					<p><label>已选择清单：</label><span>${result.attrVal}</span></p>
               	</c:if>
			</div>
			<c:if test="${result.customStatus == 0}">
				<div class="blackOverlay">&nbsp;</div>
				<div class="blackOverlayCT">
					<a class="delOverlay" href="javascript:void(0);">&nbsp;</a>
	                <div class="overlayTxt">该用户群已被删除</div>
				</div>
			</c:if>
		</div>
	</c:if><!-- 遍历购物车end -->
</c:forEach>
<%-- <c:if test="${source == 2}">
<script type="text/javascript">
	var _parenthesisRight = getCurlyBraceHTML(")",1,null,true);
	$("#ruleHidden").append(_parenthesisRight);
	$("#ruleHidden").find("script").remove();
</script>
</c:if> --%>

