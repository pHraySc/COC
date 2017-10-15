<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<%@ include file="/aibi_ci/html_include.jsp"%>
<%
int isContainLocalListInt = ServiceConstants.IS_CONTAIN_LOCAL_LIST;
String cityId = session.getAttribute("cocUserCityId").toString();
String root = Configure.getInstance().getProperty("CENTER_CITYID");
String labelUpdateInBatches = Configure.getInstance().getProperty("LABEL_UPDATE_IN_BATCHES"); 
%>
<c:set var="cityId" value="<%=cityId%>"></c:set>
<c:set var="root" value="<%=root%>"></c:set>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/sortTag.js"></script>
<script type="text/javascript">
$(function(){
	var dataDate=$.trim("${param['pojo.dataDate']}");
	if(dataDate != null && dataDate != ''){
		$(".userListOption").removeClass("current");
		$(".userListOption").each(function(i, element){
			if($(element).find("label").text()==dataDate){
				$(element).addClass("current");
			}
		});
	}
	
	var createTypeId = "${ciCustomGroupInfo.createTypeId}";
	var createCityId = "${ciCustomGroupInfo.createCityId}";
	var isHasList = "${ciCustomGroupInfo.isHasList}";
	var duplicateNum = "${ciCustomGroupInfo.duplicateNum}";
	var isLabelOffline = "${ciCustomGroupInfo.isLabelOffline}";
	var cityId ="${cityId}";
	var root = "${root}";
	var isEmpty= ${fn:length(ciCustomListInfoList) == 0 ? true : false};
	if(isEmpty){
		$('#customRuleContext').html("<font color='red'>该客户群不包含清单，无法使用客户群清单！</font>");//font换成span标签
		$(".radioBox").each(function(){
			var switchOptionId = $(this).attr("switchOptionId");
			$(".radioBox").removeClass("current");
			if(switchOptionId == "useRuleBox"){
				$(this).addClass("current");
				$(this).removeClass("disabled");
				$(".userListBox").hide();
				$("#"+ switchOptionId).show();
			}else{
				$(this).addClass("disabled");
			}
		});
	}else if(createTypeId!=7 && createTypeId!=12 && (createCityId == root || createCityId == cityId) 
			&& isHasList == 1 && (duplicateNum == null || duplicateNum == 0) && isLabelOffline != 1){//不是文件导入或者表导入创建的客户群
		//切换使用规则和使用清单操作
		$(".radioBox").click(function(){
		    $(".radioBox").removeClass("current");
		    $(this).addClass("current");
		    $(".userListBox").hide();
		    var switchOptionId = $(this).attr("switchOptionId");
		    $("#"+ switchOptionId).show();
		});
		$(".radioBox").hover(function(){
			$(this).addClass("hover");
		},function(){
			$(this).removeClass("hover");
		});
	} else if(createCityId != root && createCityId != cityId ) {
		$('#customRuleContext').html("<font color='red'>不是本地创建的客户群，没有权限使用客户群清单！</font>");
		$(".radioBox").each(function(){
			var switchOptionId = $(this).attr("switchOptionId");
			$(".radioBox").removeClass("current");
			if(switchOptionId == "useRuleBox"){
				$(this).addClass("current");
				$(this).removeClass("disabled");
				$(".userListBox").hide();
				$("#"+ switchOptionId).show();
			}else{
				$(this).addClass("disabled");
			}
		});
	}else if(duplicateNum != null && duplicateNum > 0){
		$('#customRuleContext').html("<font color='red'>该客户群有重复用户数，只能使用客户群规则！</font>");
		$(".radioBox").each(function(){
			var switchOptionId = $(this).attr("switchOptionId");
			$(".radioBox").removeClass("current");
			if(switchOptionId == "useRuleBox"){
				$(this).addClass("current");
				$(this).removeClass("disabled");
				$(".userListBox").hide();
				$("#"+ switchOptionId).show();
			}else{
				$(this).addClass("disabled");
			}
		});
	}else if(isHasList != '' && isHasList == 0){
		$('#customRuleContext').html("<font color='red'>该客户群不包含清单，无法使用客户群清单！</font>");
		$(".radioBox").each(function(){
			var switchOptionId = $(this).attr("switchOptionId");
			$(".radioBox").removeClass("current");
			if(switchOptionId == "useRuleBox"){
				$(this).addClass("current");
				$(this).removeClass("disabled");
				$(".userListBox").hide();
				$("#"+ switchOptionId).show();
			}else{
				$(this).addClass("disabled");
			}
		});
	}else{
		$(".radioBox").each(function(){
			if($(this).hasClass("current")){
				$(this).removeClass("disabled");
			}else{
				$(this).addClass("disabled");
			}
		})
	}
    
	//单选项
	$(".userListOption").click(function(){
	    $(".userListOption").removeClass("current");
	    $(this).addClass("current");
	});
	
	//确定
	$(".ensureBtn").click(function(){
		var val=$(".radioBox.current").attr("value");
		if(val==1){
			expandRules();
		}else if(val==0){
			choiseList();
		}
	});
});
//展开规则
function expandRules(){
	var html=$("#ruleHidden").html();
	parent.$("._idClass").parent().parent().before(html);
	parent.$("._idClass").parent().parent().remove();
	parent.sortLabels();
	parent.resizeConditionDiv();
	parent.submitRules();

	<% if (labelUpdateInBatches.equalsIgnoreCase("true")) { %>
	//校验日标签
	parent.checkDayDate(parent.$("#dayDataDateHidden").val());
	//校验月标签是否合法
	parent.checkDate(parent.$("#monthLabelDateHidden").val());
	<% } %>
	parent.checkEffectDate();
	closeThisDialog();
}
//选择清单
function choiseList(){
	var dataDate=$(".userListOption.current label").text();
	var listId=$(".userListOption.current input").val();
	parent.$("._idClass").parent().parent().attr("attrVal",dataDate);
	parent.$("._idClass").parent().parent().attr("calcuElement",listId);
	var $conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
    var p = $("<p><label>已选择清单：</label><span>"+dataDate+"</span></p>");
    $conditionMore.html('').append(p);
    parent.submitRules();
	closeThisDialog();
}
function closeThisDialog(){
	parent.iframeCloseDialog("#customerSetDialog");
}
</script>
</head>
<body>
<div class="switchOptionBox">
	<ol>
		<li>
			<div class="radioBox current" switchOptionId="useListBox" value="0">
				使用清单
			</div>
		</li>
		<li>
			<div class="radioBox " switchOptionId="useRuleBox" value="1" >
				使用规则
			</div>
		</li>
	</ol>
</div>
<div id="useListBox" class="userListBox ">
	<ol class="clearfix">
		<c:forEach items="${ciCustomListInfoList}" var="customListInfo" varStatus="status">
			<li>
				<div 
				<c:if test="${status.index==0}">
					class="userListOption current"
				</c:if>
				<c:if test="${status.index!=0}">
					class="userListOption"
				</c:if>
				>
					<input type="radio" name="userListOption" value="${customListInfo.listTableName }" class="hidden"/>
					<label>${customListInfo.dataDate}</label>
				</div>
			</li>
		</c:forEach>
	</ol>
	<div class="importFontStyleBox">请选择一个清单！
		<c:if test="${ciCustomGroupInfo.createTypeId ==7 || ciCustomGroupInfo.createTypeId ==12}">
			<font color="red">(导入创建客户群不能选择规则)</font>
		</c:if>
		<c:if test="${ciCustomGroupInfo.isLabelOffline == 1}">
			<font color="red">(客户群已停用，只能使用已有清单！)</font>
		</c:if>
	</div>
</div>
<div id="useRuleBox" class="userListBox hidden">
	<div class="useRuleContent">
		<p>${ciCustomGroupInfo.labelOptRuleShow}</p>
	</div>
	<div id="customRuleContext" class="importFontStyleBox"><font color="red">将客户群展开成规则后将不能再回退到选择清单！</font></div>
</div>
<!-- 按钮操作-->
<div class="btnActiveBox">
	<input type="button" value="确定" class="ensureBtn"/>
</div>
<div id="ruleHidden" style="display:none;">
<script type="text/javascript">
	var _parenthesisLeft = getCurlyBraceHTML('(',1,null,true);
	$("#ruleHidden").append(_parenthesisLeft);
	$("#ruleHidden").find("script").remove();
</script>
<c:forEach var="result" items="${ciLabelRuleList}">
	<!-- 处理括号 -->
	<c:if test="${result.elementType == 3}">
		<script type="text/javascript">
			var _parenthesis = getCurlyBraceHTML('${result.calcuElement}',1,null,true);
			$("#ruleHidden").append(_parenthesis);
			$("#ruleHidden").find("script").remove();
		</script>
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
                  	<!-- <a class="delLabel" href="javascript:void(0);"></a> -->
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
			customCreateTypeId="${result.customCreateTypeId }" customId="${result.customId}"  dataCycle="${result.labelFlag}">
			<div class="left_bg">
				<!--  <a class="setting" onclick="setLabelAttr(this,10,'${result.customOrLabelName}')"></a>
				<a class="delLabel" href="javascript:void(0);"></a> -->
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
<script type="text/javascript">
	var _parenthesisRight = getCurlyBraceHTML(')',1,null,true);
	$("#ruleHidden").append(_parenthesisRight);
	$("#ruleHidden").find("script").remove();
</script>
</div>			
</body>
</html>