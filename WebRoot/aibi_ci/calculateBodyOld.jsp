<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String labelUpdateInBatches = Configure.getInstance().getProperty("LABEL_UPDATE_IN_BATCHES"); 
%>
<script type="text/javascript">
$(function() {
	//排序
	sortLabels();
	
	//统一调整标签条件设置区域宽度
	resizeConditionDiv();
	//遍历计算元素，如果没有设置值则默认显示“请点击设置”图片
	$('#tag_dropable .conditionCT').each(function(index, item) {
		var conditionMore = $(item).children('.conditionMore');
		var conditionMoreHtml = $(conditionMore).text();
		if($.trim(conditionMoreHtml) == '') {
			$(conditionMore).html('<a class="emptyGroup">&nbsp;</a>');
		}
	});
	
	//标签取反
	$("#tag_dropable").on("click",".conditionReverse .midCT,.right_bg",againstLabel);
	
	//label鼠标滑过
	$("#tag_dropable").on("mouseenter",".conditionCT",function(){
		$(this).addClass("conditionCTOnhover");
	}).on("mouseleave",".conditionCT",function(){
		$(this).removeClass("conditionCTOnhover");
	});
	//切换and or按钮鼠标滑过状态
	$("#tag_dropable").on("mouseenter",".chaining",function(){
		$(this).addClass("chainingOnhover");
	}).on("mouseleave",".chaining",function(){
		$(this).removeClass("chainingOnhover");
	});
	//提示icon鼠标滑过
	$("#tag_dropable").on("mouseenter",".conditionTipIcon",function(){
		$(this).addClass("conditionTipIcon_hover");
	}).on("mouseleave",".conditionTipIcon",function(){
		$(this).removeClass("conditionTipIcon_hover");
	});
	
	//点击切换"且"、"或"时，屏蔽右键菜单
	$("#tag_dropable").bind("contextmenu",function(e){  
		return false;  
	});
	
	//已经被删除的客户群，删除事件
	$("#tag_dropable").on("click",".delOverlay",function(){
		$(this).parent().siblings(".right_bg").find(".delLabel").click();
	});
	
	$(document).click(function(){
		$(".labelTip").hide();
		$(".customTip").hide();
		$(".tacticsTip").hide();
	})
	<% if (labelUpdateInBatches.equalsIgnoreCase("true")) { %>
	//校验日标签
	checkDayDate($("#dayDataDateHidden").val());
	//校验月标签是否合法
	checkDate($("#monthLabelDateHidden").val());
	<%}%>
	checkEffectDate();
	//计算高度
	adaptSize();
	
	//取反提示
	negationtipF();
	$(window).resize(function(){
		negationtipF();
    })
    
    $("#calculateAddLable").COClive('click', function() {
    	if('${versionFlag}' == "true"){
			var url = "${ctx}/core/ciMainAction!findMarketMain.ai2do?seachTypeId=1";
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    	}else{
    		linkToIndex();
    	}
	});
})

</script>
<h3><span class="clearLabels" onclick="linkToIndex()">添加标签</span><span class="clearLabels J_show_cart_box_calculate" onclick="clearLabels()">清空</span><span>配置标签及客户群</span></h3>
   
<div id="tag_dropable" class="labelsCT">
<c:forEach var="result" items="${sessionModelList}" varStatus="st">
	
	<!-- 处理括号 -->
	<c:if test="${result.elementType == 3}">
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
           		dataDate="${result.dataDate }" updateCycle="${result.updateCycle }">
              	<div class="left_bg">&nbsp;</div>
               	<div class="midCT" onclick="againstLabel(event)">
               		${result.customOrLabelName}
               		<span twins="labelTip" labelId="${result.calcuElement}" 
               			class="conditionTipIcon labelConditionTipIcon" 
               			onclick="labelTip(event, this);">&nbsp;</span>
               	</div>
               	<div class="right_bg"><a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a></div>
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
          		dataDate="${result.dataDate }" updateCycle="${result.updateCycle}" isNeedOffset="${result.isNeedOffset}">
				<div class="left_bg">
					<a class="setting" onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')" ></a>
              	</div>
              	<div class="midCT" onclick="againstLabel(event)">
              		${result.customOrLabelName}
              		<span twins="labelTip" labelId="${result.calcuElement}" 
              			class="conditionTipIcon labelConditionTipIcon" 
              			onclick="labelTip(event, this);">&nbsp;</span></div>
              	<div class="right_bg"><a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a></div>
               
              	<c:if test="${result.labelTypeId == 4}">
					<div class="conditionMore" onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')">
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
					<div class="conditionMore" onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')" title="${result.attrName}">
	                	<c:if test="${not empty result.attrVal}">
						<p><label>已选择条件：</label><span><c:out value="${result.attrName}"></c:out></span></p>
						</c:if>
					</div>
				</c:if>
               
				<c:if test="${result.labelTypeId == 6}">
	                <div class="conditionMore" onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')">
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
	               	<div class="conditionMore" onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')" title="<c:if test="${result.queryWay == 1}"><c:if test="${not empty result.darkValue}">模糊值：${result.darkValue}</c:if></c:if><c:if test="${result.queryWay == 2}"><c:if test="${not empty result.exactValue}">精确值：${result.exactValue}</c:if></c:if>">
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
          		dataDate="${result.dataDate }" updateCycle="${result.updateCycle }" isNeedOffset="${result.isNeedOffset}">
				<div class="left_bg">
					<a onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')" class="setting"></a>
               	</div>
               	<div class="midCT">
               		${result.customOrLabelName}
               		<span twins="labelTip" labelId="${result.calcuElement}" 
               		class="conditionTipIcon labelConditionTipIcon" 
               		onclick="labelTip(event, this);">&nbsp;</span>
               	</div>
               	<div class="right_bg"><a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a></div>
               
               	<div class="conditionMore" onclick="setLabelAttr(this, ${result.labelTypeId},'${result.customOrLabelName}')" title="<c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if test="${item.labelTypeId == 4}"><c:if test="${result.queryWay == 1}">${item.columnCnName}：<c:if test="${not empty item.contiueMinVal}"><c:if test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if test="${not empty item.contiueMaxVal }"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}${item.unit}</c:if></c:if>;</c:if><c:if test="${item.queryWay == 2}"> ${item.columnCnName}：${item.exactValue}${item.unit};</c:if></c:if><c:if test="${item.labelTypeId == 5}"><c:if test="${not empty item.attrVal}">${item.columnCnName}：${item.attrName};</c:if></c:if><c:if test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.leftZoneSign || not empty item.rightZoneSign}">${item.columnCnName}：<c:if test="${not empty item.startTime}"><c:if test="${item.leftZoneSign eq '>='}">大于等于${item.startTime}</c:if><c:if test="${item.leftZoneSign eq '>'}">大于${item.startTime}</c:if></c:if><c:if test="${not empty item.endTime}"><c:if test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if>;</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">${item.columnCnName}：<c:if test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if>；</c:if></c:if></c:if><c:if test="${item.labelTypeId ==7 }"><c:if test="${item.queryWay == 1}"><c:if test="${not empty item.darkValue}">${item.columnCnName}：${item.darkValue};</c:if></c:if><c:if test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue}">${item.columnCnName}：${item.exactValue}</c:if></c:if></c:if></c:forEach>">
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
			customCreateTypeId="${result.customCreateTypeId }" customId="${result.customId}" dataCycle="${result.labelFlag}">
			<div class="left_bg">
				<a  class="setting" onclick="setLabelAttr(this,10,'${result.customOrLabelName}')"  data=""></a>
			</div>
			<div class="midCT">
				【群】${result.customOrLabelName}
				<span twins="customTip" customId="${result.customId}" class="conditionTipIcon" onclick="customTip(event, this)">&nbsp;</span>
			</div>
			<div class="right_bg"><a class="delLabel" href="javascript:void(0);" onclick="deleteThis(this)">&nbsp;</a></div>
			<div class="conditionMore" onclick="setLabelAttr(this,10,'${result.customOrLabelName}')">
				<c:if test="${not empty result.attrVal}">
					<p><label>已选择清单：</label><span>${result.attrVal}</span></p>
               	</c:if>
			</div>
			<c:if test="${result.customStatus == 0}">
				<div class="blackOverlay">&nbsp;</div>
				<div class="blackOverlayCT">
					<a class="delOverlay" href="javascript:void(0);">&nbsp;</a>
	                <div class="overlayTxt">该客户群已被删除</div>
				</div>
			</c:if>
		</div>
	</c:if><!-- 遍历购物车end -->
</c:forEach> 	
</div>

