<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
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
<style type="text/css">
.modifyBtn {cursor: pointer;font-size:15px;font-weight: bold;color: #0d6bc1;text-decoration: underline;}
</style>
<c:set var="cityId" value="<%=cityId%>"></c:set>
<c:set var="root" value="<%=root%>"></c:set>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/sortTag.js"></script>
<script type="text/javascript">
$(function(){
	var target = parent.$("._idClass");
	var custSortNum = target.attr("sort");
	$(".J_custSortNum").val(custSortNum);
	var dataDate=$.trim("${param['ciLabelRule.dataDate']}");
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
	var elementType = "${elementType}";
	
	$('.switchOptionBox .radioBox').removeClass('current');
	if(elementType == 6) {
		$('.switchOptionBox .radioBox[value="1"]').addClass('current');
		$(".userListBox").hide();
	    var switchOptionId = $('.switchOptionBox .radioBox[value="1"]').attr("switchOptionId");
	    $("#"+ switchOptionId).show();
	} else {
		$('.switchOptionBox .radioBox[value="0"]').addClass('current');
		$(".userRuleBox").hide();
	    var switchOptionId = $('.switchOptionBox .radioBox[value="0"]').attr("switchOptionId");
	    $("#"+ switchOptionId).show();
	}
	
	var isEmpty= ${fn:length(ciCustomListInfoList) == 0 ? true : false};
	if(isEmpty){
		$('#customRuleContext').html("<font color='red'>该用户群不包含清单，无法使用用户群清单！</font>&nbsp;&nbsp;<a href=\"javascript:void(0);\" class=\"modifyBtn\" onclick=\"modifyCustomRule();\">修改</a>");//font换成span标签
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
			&& isHasList == 1 && (duplicateNum == null || duplicateNum == 0) && isLabelOffline != 1){//不是文件导入或者表导入创建的用户群
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
		$('#customRuleContext').html("<font color='red'>不是本地创建的用户群，没有权限使用用户群清单！</font>");
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
		$('#customRuleContext').html("<font color='red'>该用户群有重复用户数，只能使用用户群规则！</font>&nbsp;&nbsp;<a href=\"javascript:void(0);\" class=\"modifyBtn\" onclick=\"modifyCustomRule();\">修改</a>");
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
		$('#customRuleContext').html("<font color='red'>该用户群不包含清单，无法使用用户群清单！</font>");
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
			var source = $("#source").val();
			if (source == 1) {
				expandRulesToShoppingCart();
			} else if (source == 2) {
				expandRulesAppendDialog();
			}
		}else if(val==0){
			choiseList();
		}
	});
});

//展开规则到购物车
function expandRulesToShoppingCart(){
	var target = parent.$("._idClass");
	target.attr("elementType", 6);
	var custParentId = target.attr("calcuElement");
	//拼接用户群规则到对应的用户群下
	var html = "";
	$("#ruleHidden .conditionCT,#ruleHidden .chaining,#ruleHidden .parenthesis").each(function(i, element){
		/* var elementType = "";
		var labelFlag = "";
		var labelTypeId = "";
		var parentId = "";
		var calcuElement = "";
		var customId = "";
		var effectDate = "";
		var attrVal = "";
		var startTime = "";
		var endTime = "";
		var unit = ""
		var contiueMinVal = "";
		var contiueMaxVal = "";
		var rightZoneSign = "";
		var leftZoneSign = "";
		var exactValue = "";
		var darkValue = "";
		var columnCnName = "";
		var customOrLabelName = "";
		var attrName = "";
		var queryWay = "";
		var maxVal = "";
		var minVal = "";
		var dataDate = "";
		var updateCycle = "";
		var isNeedOffset = ""; */
		
		var calcuElement = "", //括号、操作符号、标签ID或者用户群清单表名
		minVal		 = "", //标识标签 0 1属性
		maxVal 		 = "", 
		elementType	 = "", //元素类型:1-运算符;2-标签ID;3-括号;4-产品ID;5-清单表名。
		labelFlag	 = ""; //标签是否取反
	
		var ele = $(element);
		
		if (ele.hasClass("conditionCT")) {//处理标签和用户群
			elementType				= ele.attr("elementType");
			var customOrLabelName 	= ele.attr("customOrLabelName");
			if (elementType == 2) {//标签
				labelFlag			= ele.attr("labelFlag");
				calcuElement 		= ele.attr("calcuElement");
				minVal				= ele.attr("minVal");
				maxVal				= ele.attr("maxVal");
				var effectDate		= ele.attr("effectDate");
				var labelTypeId 	= ele.attr("labelTypeId");
				var attrVal 		= ele.attr("attrVal");
				var startTime 		= ele.attr("startTime");
				var endTime 		= ele.attr("endTime");
				var contiueMinVal 	= ele.attr("contiueMinVal");
				var contiueMaxVal 	= ele.attr("contiueMaxVal");
				var leftZoneSign 	= ele.attr("leftZoneSign");
				var rightZoneSign 	= ele.attr("rightZoneSign");
				var exactValue 		= ele.attr("exactValue");
				var darkValue 		= ele.attr("darkValue");
				var attrName		= ele.attr("attrName");
				var queryWay		= ele.attr("queryWay");
				var unit			= ele.attr("unit");
				var customCreateTypeId = ele.attr("customCreateTypeId");
				var dataDate        = ele.attr("dataDate");
				var updateCycle     = ele.attr("updateCycle");
				var labelOrCustomName	= ele.attr('labelOrCustomName')
				var isNeedOffset	= ele.attr("isNeedOffset");
				if (!$.isNotBlank(isNeedOffset)){
					isNeedOffset = "";
				}
				
				html += "<div class='child' elementType='" + elementType + "' labelFlag='" + labelFlag
						+ "' labelTypeId='" + labelTypeId + "' parentId='" + custParentId
						+ "' calcuElement='" + calcuElement + "' customId='" + customId + "' effectDate='" + effectDate 
						+ "' attrVal='" + attrVal + "' startTime='" + startTime + "' unit='" + unit
						+ "' endTime='" + endTime + "' contiueMinVal='" + contiueMinVal + "' contiueMaxVal='" + contiueMaxVal
						+ "' rightZoneSign='" + rightZoneSign + "' leftZoneSign='" + leftZoneSign
						+ "' exactValue='" + exactValue + "' darkValue='" + darkValue + "' columnCnName='" + columnCnName
						+ "' customOrLabelName='" + customOrLabelName + "' attrName='" + attrName
						+ "' queryWay='" + queryWay + "' maxVal='" + maxVal + "' minVal='" + minVal
						+ "' dataDate='" + dataDate  + "' updateCycle='" + updateCycle  + "' isNeedOffset='" + isNeedOffset + "'>"
				
				if (labelTypeId == 8) {
					var childs = ele.children(".child");
					for(var index=0; index<childs.length; index++) {
						var ele				= $(childs[index]);
						var labelFlag		= ele.attr("labelFlag");
						var calcE		 	= ele.attr("calcuElement");
						var minVal			= ele.attr("minVal");
						var maxVal			= ele.attr("maxVal");
						var effectDate		= ele.attr("effectDate");
						var lti 			= ele.attr("labelTypeId");
						var attrVal 		= ele.attr("attrVal");
						var startTime 		= ele.attr("startTime");
						var endTime 		= ele.attr("endTime");
						var contiueMinVal 	= ele.attr("contiueMinVal");
						var contiueMaxVal 	= ele.attr("contiueMaxVal");
						var leftZoneSign 	= ele.attr("leftZoneSign");
						var rightZoneSign 	= ele.attr("rightZoneSign");
						var exactValue 		= ele.attr("exactValue");
						var darkValue 		= ele.attr("darkValue");
						var attrName		= ele.attr("attrName");
						var queryWay		= ele.attr("queryWay");
						var columnCnName	= ele.attr("columnCnName");
						var childUnit		= ele.attr("unit");
						var dataDate        = ele.attr("dataDate");
						var updateCycle     = ele.attr("updateCycle");
						var isNeedOffset	= ele.attr("isNeedOffset");
						var parentId		= ele.attr("parentId");
						if (!$.isNotBlank(isNeedOffset)){
							isNeedOffset = "";
						}
						
						html += "<div class='grandson' elementType='" + elementType + "' labelFlag='" + labelFlag
								+ "' labelTypeId='" + lti + "' parentId='" + parentId
								+ "' calcuElement='" + calcE + "' customId='" + customId + "' effectDate='" + effectDate
								+ "' attrVal='" + attrVal + "' startTime='" + startTime + "' unit='" + childUnit
								+ "' endTime='" + endTime + "' contiueMinVal='" + contiueMinVal + "' contiueMaxVal='" + contiueMaxVal
								+ "' rightZoneSign='" + rightZoneSign + "' leftZoneSign='" + leftZoneSign
								+ "' exactValue='" + exactValue + "' darkValue='" + darkValue + "' columnCnName='" + columnCnName
								+ "' customOrLabelName='" + customOrLabelName + "' attrName='" + attrName
								+ "' queryWay='" + queryWay + "' maxVal='" + maxVal + "' minVal='" + minVal
								+ "' dataDate='" + dataDate  + "' updateCycle='" + updateCycle  + "' isNeedOffset='" + isNeedOffset + "'></div>";
					}
				}
				html += "</div>";
			} else if (elementType == 5) {//用户群
				var attrVal 		= ele.attr("attrVal");
				var customId 		= ele.attr("customId");
				var dataCycle       = ele.attr("dataCycle");
				calcuElement 		= ele.attr("calcuElement");
				var isNeedOffset	= ele.attr("isNeedOffset");
				if (!$.isNotBlank(isNeedOffset)){
					isNeedOffset = "";
				}
				
				html += "<div class='child' elementType='" + elementType + "' parentId='" + custParentId
						+ "' calcuElement='" + calcuElement + "' customId='" + customId
						+ "' attrVal='" + attrVal + "' customOrLabelName='" + customOrLabelName
						+ "' labelFlag='" + dataCycle  + "' isNeedOffset='" + isNeedOffset + "'></div>";
			}
		} else if (ele.hasClass("chaining")) {//处理操作符
			elementType = 1;
			var name = ele.find("span").text();
			if(name == "且") {
				calcuElement = "and";
			}else if(name == "或") {
				calcuElement = "or";
			}else if(name == "剔除") {
				calcuElement = "-";
			}
			
			html += "<div class='child' elementType='" + elementType + "' parentId='" + custParentId
					+ "' calcuElement='" + calcuElement + "'></div>";
		} else if (ele.hasClass("parenthesis")) {//处理括号
			var parenthesisEl = $("#parenthesis");
			if(!ele.hasClass("waitClose")) {
				elementType = 3;
				calcuElement = ele.attr("calcuElement");
				
				var creat = ele.attr("creat");
				
				html += "<div class='child' elementType='" + elementType + "' parentId='" + custParentId
						+ "' calcuElement='" + calcuElement + "' createBrackets='" + creat + "'></div>";
			} else {
				return;
			}
		}
	});
	
	target.html(html);
	var sort = $(".J_custSortNum").val();
	parent.coreMain.submitShopCartSession(sort);//提交session
	closeThisDialog();
}

//展开规则追加到规则设置dialog
function expandRulesAppendDialog(){
	parent.$("#tag_dropable").show();
	var html = $("#ruleHidden").html();
	var target = parent.$("._idClass").parent().parent();
	var source = $('#source').val();
	if(source == 2) {
		var target = parent.$("._idClass").parent().parent('.conditionCT');
	}
	target.before(html);
	target.remove();
	//删除括号上的‘x’
	parent.$(".delCondition").remove();
	parent.resizeConditionDiv();//重新算标签规则div宽度
	
	closeThisDialog();
}

//展开规则到规则设置dialog
function expandRulesToDialog(target_setting){
	var target = parent.$("._idClass");
	parent.$("#tag_dropable").show();
	var html = $("#ruleHidden").html();
	var source = $("#source").val();
	var target = null;
	if (source == 1) {
		parent.$("#tag_dropable").html(html);
		//删除括号上的‘x’
		parent.$(".delCondition").remove();
		parent.coreMain.expandCustomRules(target_setting);
	}
	
	closeThisDialog();
}

//选择清单
function choiseList(){
	var dataDate=$(".userListOption.current label").text();
	var listId=$(".userListOption.current input").val();
	
	var source = $("#source").val();
	var target = null;
	if (source == 1) {
		target = parent.$("._idClass");
	} else if (source == 2) {
		target = parent.$("._idClass").parent().parent();
	}
	
	target.attr("attrVal",dataDate);
	target.attr("calcuElement",listId);
	if (source == 2) {
		var $conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
	    var p = $("<p><label>已选择清单：</label><span>"+dataDate+"</span></p>");
	    $conditionMore.html("").append(p);
	} else if (source == 1) {
		var sort = target.attr("sort");
		target.attr("attrVal",dataDate);
		target.attr("elementType",5);
		parent.coreMain.submitShopCartSession(sort);//提交session
	}
	closeThisDialog();
}

//关闭用户群设置dialog
function closeThisDialog(){
	parent.ShoppingCart.iframeCloseDialog("#customerSetting");
}

//修改规则
function modifyCustomRule(){
	var target = parent.$(".J_setting[sort='"+$(".J_custSortNum").val()+"']");
	expandRulesToDialog(target);
}
</script>
</head>
<body>
<input type="hidden" id="source" name="source" value="${source}"/>
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
			<font color="red">(导入创建用户群不能选择规则)</font>
		</c:if>
		<c:if test="${ciCustomGroupInfo.isLabelOffline == 1}">
			<font color="red">(用户群已停用，只能使用已有清单！)</font>
		</c:if>
	</div>
</div>
<div id="useRuleBox" class="userListBox hidden">
	<div class="useRuleContent">
		<p>
		<c:if test="${empty ciCustomGroupInfo.labelOptRuleShow}">
			<c:forEach var="result" items="${ciLabelRuleList}">
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
		</c:if>
		<c:if test="${!empty ciCustomGroupInfo.labelOptRuleShow}">
			${ciCustomGroupInfo.labelOptRuleShow}
		</c:if>
		</p>
	</div>
	<div id="customRuleContext" class="importFontStyleBox">
		<c:if test="${source == 1}">
			<font color="red">规则不满足？请点击</font>&nbsp;&nbsp;<a href="javascript:void(0);" class="modifyBtn" onclick="modifyCustomRule(this);">修改</a>
		</c:if>
		<c:if test="${source == 2}">
			<font color="red">将用户群展开成规则后将不能再回退到选择清单！</font>
		</c:if>
	</div>
</div>
<!-- 按钮操作-->
<div class="btnActiveBox">
	<input type="button" value="确定" class="ensureBtn"/>
</div>
<div id="ruleHidden" style="display:none;">
<c:if test="${source == 2}">
<script type="text/javascript">
	var _parenthesisLeft = getCurlyBraceHTML("(",1,null,true);
	$("#ruleHidden").append(_parenthesisLeft);
	$("#ruleHidden").find("script").remove();
</script>
</c:if>
<c:forEach var="result" items="${ciLabelRuleList}">
	<input type="hidden" class="J_custSortNum" name="custSortNum" value=""/>
	<!-- 处理括号 -->
	<c:if test="${result.elementType == 3}">
		<script type="text/javascript">
			var _parenthesis = getCurlyBraceHTML("${result.calcuElement}",1,null,true);
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
<c:if test="${source == 2}">
<script type="text/javascript">
	var _parenthesisRight = getCurlyBraceHTML(")",1,null,true);
	$("#ruleHidden").append(_parenthesisRight);
	$("#ruleHidden").find("script").remove();
</script>
</c:if>
</div>

</body>
</html>