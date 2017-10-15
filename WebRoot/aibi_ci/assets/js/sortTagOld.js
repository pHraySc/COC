$(function(){
	
	$(".controlAll .queryWay").click(function(){
		$(".controlAll .queryWay").removeClass("current");
		$(this).addClass("current");
		var txt = $(this).text();
		if(txt.indexOf('且') > 0) {
			$('.chaining span').text('且');
		} else if(txt.indexOf('或') > 0) {
			$('.chaining span').text('或');
		} else if(txt.indexOf('剔除') > 0) {
			$('.chaining span').text('剔除');
		}
		submitRules();
	});
	$(".controlAll .queryWay").hover(function(){
		$(this).addClass("queryWayHover");
	},function(){
		$(this).removeClass("queryWayHover");
	});
	
})

function againstLabel() {
	var tar=$(this).parent();
	if(tar.hasClass("conditionCT_active")){
		tar.attr('labelFlag', 1).removeClass("conditionCT_active");
		submitRules();
	}else{
		tar.addClass("conditionCT_active").attr('labelFlag', 0);
		submitRules();
	}
}

function adaptSize() {
	//调整计算中心框的最小高度
	var tagOffsetTop = $('#tag_dropable').offset().top;
	var windowClientHeight = $(window).height();
	var minHeight =windowClientHeight-tagOffsetTop-75;
	    minHeight = parseInt(minHeight,10);
	    $('#tag_dropable').css("minHeight",minHeight +"px");
	    $("#calcCenterBody").css("padding-bottom",$("#tagSaveActionBox").height()-20);
}

/**
 * 每执行一步操作对session缓存进行重置
 */
function submitRules() {
	if($("#tag_dropable").hasClass("J_mainPageCustomRuleDiv")){//main.jsp客户群规则展开时不需要提交session
		return;
	}
	
	$('#inputList').empty();
	var html = '';
	var ruleStr = '';
	$('#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis').each(function(i, element){
		
		var calcuElement = '', //括号、操作符号、标签ID或者客户群清单表名
			minVal		 = '', //标识标签 0 1属性
			maxVal 		 = '', 
			elementType	 = '', //元素类型:1-运算符;2-标签ID;3-括号;4-产品ID;5-清单表名。
			labelFlag	 = ''; //标签是否取反
		
		var ele = $(element);
		
		if(ele.hasClass('conditionCT')) {//处理标签和客户群
			elementType				= ele.attr('elementType');
			var customOrLabelName 	= ele.attr('customOrLabelName');
			
			if(elementType == 2) {//标签
				labelFlag			= ele.attr('labelFlag');
				calcuElement 		= ele.attr('calcuElement');
				minVal				= ele.attr('minVal');
				maxVal				= ele.attr('maxVal');
				var effectDate		= ele.attr('effectDate');
				var labelTypeId 	= ele.attr('labelTypeId');
				var attrVal 		= ele.attr('attrVal');
				var startTime 		= ele.attr('startTime');
				var endTime 		= ele.attr('endTime');
				var contiueMinVal 	= ele.attr('contiueMinVal');
				var contiueMaxVal 	= ele.attr('contiueMaxVal');
				var leftZoneSign 	= ele.attr('leftZoneSign');
				var rightZoneSign 	= ele.attr('rightZoneSign');
				var exactValue 		= ele.attr('exactValue');
				var darkValue 		= ele.attr('darkValue');
				var attrName		= ele.attr('attrName');
				var queryWay		= ele.attr('queryWay');
				var unit			= ele.attr('unit');
				var customCreateTypeId = ele.attr('customCreateTypeId');
				var dataDate        = ele.attr('dataDate');
				var updateCycle     = ele.attr('updateCycle');
				var isNeedOffset	= ele.attr('isNeedOffset');
				
				html += '<input type="hidden" name="ciLabelRuleList[' + i + '].labelTypeId" value="' + labelTypeId + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].attrVal" value="' + attrVal + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].startTime" value="' + startTime + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].endTime" value="' + endTime + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].contiueMinVal" value="' + contiueMinVal + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].contiueMaxVal" value="' + contiueMaxVal + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].leftZoneSign" value="' + leftZoneSign + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].rightZoneSign" value="' + rightZoneSign + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].exactValue" value="' + exactValue + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].darkValue" value="' + darkValue + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].effectDate" value="' + effectDate + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].labelFlag" value="' + labelFlag + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[' + i + '].attrName" value="' + attrName + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].queryWay" value="' + queryWay + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].minVal" value="' + minVal + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].maxVal" value="' + maxVal + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].unit" value="' + unit + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].customCreateTypeId" value="' + customCreateTypeId + '"/>'+
						'<input type="hidden" name="ciLabelRuleList[' + i + '].dataDate" value="' + dataDate + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].updateCycle" value="' + updateCycle + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[' + i + '].isNeedOffset" value="' + isNeedOffset + '"/>';
				if(labelFlag != 1) {
					ruleStr += '(非)' + customOrLabelName;
				} else {
					ruleStr += customOrLabelName;
				}
				if(labelTypeId == 8) {
					var childs = ele.children('.child');
					for(var index=0; index<childs.length; index++) {
						var ele				= $(childs[index]);
						var labelFlag		= ele.attr('labelFlag');
						var calcE		 	= ele.attr('calcuElement');
						var minVal			= ele.attr('minVal');
						var maxVal			= ele.attr('maxVal');
						var effectDate		= ele.attr('effectDate');
						var lti 			= ele.attr('labelTypeId');
						var attrVal 		= ele.attr('attrVal');
						var startTime 		= ele.attr('startTime');
						var endTime 		= ele.attr('endTime');
						var contiueMinVal 	= ele.attr('contiueMinVal');
						var contiueMaxVal 	= ele.attr('contiueMaxVal');
						var leftZoneSign 	= ele.attr('leftZoneSign');
						var rightZoneSign 	= ele.attr('rightZoneSign');
						var exactValue 		= ele.attr('exactValue');
						var darkValue 		= ele.attr('darkValue');
						var attrName		= ele.attr('attrName');
						var queryWay		= ele.attr('queryWay');
						var columnCnName	= ele.attr('columnCnName');
						var childUnit		= ele.attr('unit');
						var dataDate        = ele.attr('dataDate');
						var updateCycle     = ele.attr('updateCycle');
						var isNeedOffset	= ele.attr('isNeedOffset');
						
						html += '<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].labelTypeId" value="' + lti + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].attrVal" value="' + attrVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].startTime" value="' + startTime + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].endTime" value="' + endTime + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].contiueMinVal" value="' + contiueMinVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].contiueMaxVal" value="' + contiueMaxVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].leftZoneSign" value="' + leftZoneSign + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].rightZoneSign" value="' + rightZoneSign + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].exactValue" value="' + exactValue + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].darkValue" value="' + darkValue + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].effectDate" value="' + effectDate + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].labelFlag" value="' + labelFlag + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].attrName" value="' + attrName + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].queryWay" value="' + queryWay + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].minVal" value="' + minVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].calcuElement" value="' + calcE + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].columnCnName" value="' + columnCnName + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].unit" value="' + childUnit + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].elementType" value="2"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].sortNum" value="' + index + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].maxVal" value="' + maxVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].dataDate" value="' + dataDate + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].updateCycle" value="' + updateCycle + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[' + i + '].childCiLabelRuleList[' + index + '].isNeedOffset" value="' + isNeedOffset + '"/>';
						if(lti == 4) {
							if(queryWay == 1){
								ruleStr += '[' + columnCnName + '：';
								if(contiueMinVal) {
									if(leftZoneSign == '>='){
										ruleStr += '大于等于' + contiueMinVal ;
									} else {
										ruleStr += '大于' + contiueMinVal;
									}
								}
								if(contiueMaxVal) {
									if(rightZoneSign == '<='){
										ruleStr += '小于等于' + contiueMaxVal ;
									} else {
										ruleStr += '小于' +contiueMaxVal;
									}
								}
								//ruleStr += ']';
							} else if(queryWay == 2){
								ruleStr += '[' + columnCnName + '：' + exactValue;// + ']';
							}
							if(ruleStr) {
								if(childUnit) {
									ruleStr += '(' + childUnit + ')';
								}
								ruleStr += ']';
							}
						} else if(lti == 5) {
							if(attrVal) {
								ruleStr += '[' + columnCnName + '：' + attrName + ']';
							}
						} else if(lti == 6) {
							var tempStr6 = '';
							if(queryWay == 1){
								if(startTime != ''){
									if(leftZoneSign == '>='){
										tempStr6 += '大于等于' + startTime;
									} else if(leftZoneSign == '>') {
										tempStr6 += '大于' + startTime;
									}
								}
								
								if(endTime != ''){
									if(rightZoneSign == '<='){
										tempStr6 += '小于等于' + endTime;
									} else if(rightZoneSign == '<') {
										tempStr6 += '小于' + endTime;
									}
								}
								if(isNeedOffset == 1) {
									tempStr6 += '（动态偏移更新）';
								}
							}else if(queryWay == 2){
								var itemValueArr = exactValue.split(",");
								if(itemValueArr.length == 3){
									
									var exactValueDateYear = itemValueArr[0];
									var exactValueDateMonth = itemValueArr[1];
									var exactValueDateDay =itemValueArr[2];
									
									if(exactValueDateYear && exactValueDateYear != "-1"){
										tempStr6 += exactValueDateYear+"年";
									}
									if(exactValueDateMonth && exactValueDateMonth != "-1"){
										tempStr6 += exactValueDateMonth+"月";
									}
									if(exactValueDateDay && exactValueDateDay != "-1"){
										tempStr6 += exactValueDateDay+"日";
									}
								}
							}
							
							if(tempStr6 != '') {
								ruleStr += '[' + columnCnName + '：' + tempStr6 + ']';
							}
						} else if(lti == 7) {
							if(queryWay == 1){
								if(darkValue) {
									ruleStr += '[' + columnCnName + '：' + darkValue + ']';
								}
							}else if(queryWay == 2){
								if(exactValue) {
									ruleStr += '[' + columnCnName + '：' + exactValue + ']';
								}
							}
						}
					}
				}
				if(labelTypeId == 4) {
					if(queryWay == 1){
						ruleStr += '[数值范围：';
						if(contiueMinVal) {
							if(leftZoneSign == '>='){
								ruleStr += '大于等于' + contiueMinVal ;
							} else {
								ruleStr += '大于' + contiueMinVal;
							}
						}
						if(contiueMaxVal) {
							if(rightZoneSign == '<='){
								ruleStr += '小于等于' + contiueMaxVal ;
							} else {
								ruleStr += '小于' +contiueMaxVal;
							}
						}
						if(unit) {
							ruleStr += '(' + unit + ')';
						}
						ruleStr += ']';
					} else if(queryWay == 2){
						ruleStr += "[数值范围：" + exactValue;// + ']';
						if(unit) {
							ruleStr += '(' + unit + ')';
						}
						ruleStr += ']';
					}
				} else if(labelTypeId == 5) {
					if(attrVal) {
						ruleStr += '[已选择条件：' + attrName + ']';
					}
				} else if(labelTypeId == 6) {
					var tempStr6 = '';
					if(queryWay == 1){
						if(startTime != ''){
							if(leftZoneSign == '>='){
								tempStr6 += '大于等于' + startTime;
							} else if(leftZoneSign == '>') {
								tempStr6 += '大于' + startTime;
							}
						}
						
						if(endTime != ''){
							if(rightZoneSign == '<='){
								tempStr6 += '小于等于' + endTime;
							} else if(rightZoneSign == '<') {
								tempStr6 += '小于' + endTime;
							}
						}
						if(isNeedOffset == 1) {
							tempStr6 += '（动态偏移更新）';
						}
					}else if(queryWay == 2){
						var itemValueArr = exactValue.split(",");
						if(itemValueArr.length == 3){
							
							var exactValueDateYear = itemValueArr[0];
							var exactValueDateMonth = itemValueArr[1];
							var exactValueDateDay =itemValueArr[2];
							
							if(exactValueDateYear && exactValueDateYear != "-1"){
								tempStr6 += exactValueDateYear+"年";
							}
							if(exactValueDateMonth && exactValueDateMonth != "-1"){
								tempStr6 += exactValueDateMonth+"月";
							}
							if(exactValueDateDay && exactValueDateDay != "-1"){
								tempStr6 += exactValueDateDay+"日";
							}
						}
					}
					
					
					if(tempStr6 != '') {
						ruleStr += '[已选择条件：' + tempStr6 + ']';
					}
				} else if(labelTypeId == 7) {
					if(queryWay == 1){
						if(darkValue) {
							ruleStr += '[模糊值：' + darkValue + ']';
						}
					}else if(queryWay == 2){
						if(exactValue) {
							ruleStr += '[精确值：' + exactValue + ']';
						}
					}
					
				}
				ruleStr += ' ';
			} else if(elementType == 5) {//客户群
				var attrVal 		= ele.attr('attrVal');
				var customId 		= ele.attr('customId');
				var dataCycle       = ele.attr('dataCycle');
				calcuElement 		= ele.attr('calcuElement');
				ruleStr 		   += '客户群：' + customOrLabelName;
				
				if(attrVal) {
					ruleStr += '[已选清单：' + attrVal + ']';
				}
				ruleStr += ' ';
				html += '<input type="hidden" name="ciLabelRuleList[' + i + '].attrVal" value="' + attrVal + '"/>';
				html += '<input type="hidden" name="ciLabelRuleList[' + i + '].customId" value="' + customId + '"/>';
				html += '<input type="hidden" name="ciLabelRuleList[' + i + '].labelFlag" value="' + dataCycle + '"/>';
			}
			html += '<input type="hidden" name="ciLabelRuleList[' + i + '].customOrLabelName" value="' + customOrLabelName + '"/>';
		} else if(ele.hasClass('chaining')) {//处理操作符
			
			elementType = 1;
			var name = ele.find("span").text();
			ruleStr += name + ' ';
			
			if(name == "且") {
				calcuElement = "and";
			}else if(name == "或") {
				calcuElement = "or";
			}else if(name == "剔除") {
				calcuElement = "-";
			}
		} else if(ele.hasClass('parenthesis')) {//处理括号
			
			var parenthesisEl = $('#parenthesis');
			if(!ele.hasClass('waitClose')) {
				elementType = 3;
				calcuElement = ele.attr('calcuElement');
				ruleStr += calcuElement;
				
				if(calcuElement == ')') {
					ruleStr += ' ';
				}
				var creat = ele.attr('creat');
				html += '<input type="hidden" name="ciLabelRuleList[' + i + '].createBrackets" value="' + creat + '"/>';
			} else {
				return;
			}
		}
		
		html += '<input type="hidden" name="ciLabelRuleList[' + i + '].calcuElement" value="' + calcuElement + '"/>' +
				'<input type="hidden" name="ciLabelRuleList[' + i + '].sortNum" value="' + (i+1) + '"/>' +
				'<input type="hidden" name="ciLabelRuleList[' + i + '].elementType" value="' + elementType + '"/>';
	});
	saveButtonsDisplay();
	$("#inputList").append(html);
	var url = $.ctx + "/ci/ciIndexAction!saveSession.ai2do";
	$('.labelsCTBtm').empty();
	$('.labelsCTBtm').height("auto");
	$('.labelsCTBtm').text(ruleStr);
	var data = $('#labelForm').serialize();
	$('#labelOptRuleShow').val(ruleStr);
	$.ajax({
		 url		: url,
         type		: 'post',
         async		: false,//同步
         data		: $("#labelForm").serialize(),
         success	: function (data) {
        	 editNavigationCarShopNum();
        	 $('#inputList').empty();
         }
	});
	negationtipF();
    return false;
}

/**
 * 当手动修改运算符号，则把头部单选框选择项去掉
 */
function clearOptAllChaining() {
	$('.controlAll .queryWay').each(function(index, item) {
		if($(item).hasClass('current')) {
			$(item).removeClass('current');
		}
	});
}

/**
 * 修改导航计算元素个数
 */
function editNavigationCarShopNum() {
	var url = $.ctx + '/ci/ciIndexAction!findCarShopNum.ai2do';
	$.post(url, {}, function (response) {
		$('#shopTopNum').html(response.num);
	});
}

/**
 * 判断保存按钮是否可用
 */
function saveButtonsDisplay() {
	var flag = false;
	$('#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis').each(function(i, element) {
		flag = true;
	});
	if(!flag) {
		$('.tagSaveActionBox').find('input').addClass('tag_btn_dis');
		$('#calculateAddLable').removeClass('tag_btn_dis');
		$('#tagCalcSaveBtn').unbind('click');
		$('#tagCalcExplore').unbind('click');
		$('#tagCalcSaveTemplateBtn').unbind('click');
		$("#customerNum").empty();
	} else {
		if($('.tagSaveActionBox').find('input').hasClass('tag_btn_dis')) {
			$('.tagSaveActionBox').find('input').removeClass('tag_btn_dis');
			$('#tagCalcSaveBtn').bind('click', saveCustom);
			$('#tagCalcExplore').bind('click', explore);
			$('#tagCalcSaveTemplateBtn').bind('click', saveTemplate);
		}
	}
}

function valiCustomVal() {
	var result = true;
	
	$('#tag_dropable .conditionCT').each(function(i, element) {
		var elementType = $(element).attr('elementType');
		if(elementType == 5) {
			var attrVal = $(element).attr('attrVal');
			if(!attrVal) {
				result = false;
			}
		}
	});
	
	return result;
}

function editSessionByOperator(opt) {
	var url = $.ctx + "/ci/ciIndexAction!editSessionByOperator.ai2do";
	var param = {calcuElement	: opt};
	$.post(url, param, function(response) {});
}

//清空所有计算元素
function clearLabels(){
	var innerHtml = $.trim($("#tag_dropable").html());
	if(innerHtml == null || innerHtml ==""){
		return;
	}
	commonUtil.create_alert_dialog("clearCalculateCenterOld", {
		txt 	: "确定清空？",
		type 	: "confirm", 
		width 	: 500,
		height 	: 200
	}, clearCalEles);
	//showAlert("确定要清空计算中心吗？","confirm",clearCalEles);
}

//清空操作
function clearCalEles(){
	
	var clearCarShopItemUrl = $.ctx + '/ci/ciIndexAction!delAllCarShopSession.ai2do';
	$.post(clearCarShopItemUrl, {}, function(result) {
		$('#tag_dropable').html('');
		$('.labelsCTBtm').html('');
		$('.labelsCTBtm').height(0);
		$('#shopTopNum').empty().html(0);
		saveButtonsDisplay();
		negationtipF();
	});
}

//跳转到首页	
function linkToIndex() {
	window.location = $.ctx + '/ci/ciIndexAction!labelIndex.ai2do';
}

function dragParenthesis(){
	var parenthesis=$("#parenthesis > div");
	parenthesis.draggable({
		revert: "invalid",
		scope: "parenthesis",
		cursor:'pointer',
		cursorAt:{left:-4,top:-10},
		helper:function(){
			var cls=$(this).parent().hasClass("opened")?"dragingRightPar":"dragingLeftPar";
			var n=$("<span class='"+cls+"'></span>");
			n.appendTo('body');
			return n;
		},
		start:function(){
			parenthesis_possibility_position($(this));
		},
		drag:function(){
		},
		stop:function(source, ui){
			$("#tag_dropable .dragTempCT").remove();
		}
	});
}

function sortLabels(){
	var labels=$("#tag_dropable .conditionCT");
	labels.draggable({
		revert: "invalid",
		cursor:'pointer',
		cursorAt:{left:2,top:2},
		helper:function(){
			//var _ct=$('<div></div>');
			//var _prev=$(this).prev();
			//_ct.append($(this).clone());
			var _clone=$(this).clone();
			_clone.find(".conditionMore").remove();
			var overlay=$('<div class="dragOverlay">&nbsp;</div>');
			_clone.append(overlay);
			_clone.appendTo('body');
			//_prev.addClass("onSortingLabel");
			$(this).css("visibility","hidden");//.addClass("onSortingLabel");
			//_prev.before($('<div class="dragTempCT"></div>'));
			return _clone;
		},
		start:function(){
			$("#tag_dropable .chaining").hide();
			var tarAry=$("#tag_dropable .conditionCT,#tag_dropable .rightParenthesis,#tag_dropable .leftParenthesis").not($(this));
			var dragTempCT='<div class="dragTempCT"></div>';
			tarAry.each(function(){
				$(this).after(dragTempCT);
			});
			/*
			var lastRightParenthesis=$("#tag_dropable .conditionCT:last").nextAll(".rightParenthesis:last")||null;
			//$("#tag_dropable .conditionCT:last").next().html("1111111111111");
			if(lastRightParenthesis!=null){
				lastRightParenthesis.after(dragTempCT);
			}
			*/
			$("#tag_dropable").prepend(dragTempCT);
			$(this).css("visibility","visible").hide().addClass("onSortingLabel");
			labels_possibility_position($(this));
		},
		drag:function(){},
		stop:function(source){
			$("#tag_dropable .chaining").show();
			$("#tag_dropable .dragTempCT").remove();
			$("#tag_dropable .onSortingLabel").removeClass("onSortingLabel").show();
			submitRules();
		}
	});
}

//排序标签开始时找出可合理drop标签的位置
function labels_possibility_position(_t){
	$("#tag_dropable .dragTempCT").droppable({
		greedy:true,
		//scope: "parenthesis",
		tolerance:"pointer",
		activeClass: "ui-state-active",
		hoverClass: "dragEnter",
		drop: function( event, ui ) {
			var conditionCT=_t.clone();
			var chaining='<div class="chaining">';
                chaining+='<span onmousedown="switchConnector_turn(this,event)">且</span>';
                chaining+='<a class="icon_rightArr" href="javascript:void(0)" onclick="switchConnector(this,event)">&nbsp;</a>';
                chaining+='</div>';
			var dragTempCTIndex=$(this).parent().find(".dragTempCT").index($(this)[0]);
			var conditionCTIndex=_t.parent().find(".conditionCT").index(_t[0]);
			var isDraggingTheLastOne=conditionCTIndex==(_t.parent().find(".conditionCT").length-1)&&_t.nextAll(".rightParenthesis").length==0?true:false;
			var isDraggingTheFirstOne=conditionCTIndex==0&&$(this).prevAll(".leftParenthesis").length==0&&_t.prevAll(".leftParenthesis").length==0?true:false;
			var isDragToTheLast=dragTempCTIndex==($(this).parent().find(".dragTempCT").length-1)?true:false;
			var isDragToTheFirst=dragTempCTIndex==0?true:false;
			//目标区域是否在括号内的第一个位置
			var isInParFirst=$(this).prev().hasClass("leftParenthesis")?true:false;
			//起始位置是否在括号内的第一个位置
			var isStartFromParenthesisFirst=_t.prev().prev().hasClass("leftParenthesis")?true:false;
			//起始位置是否在括号内最后位置
			var isStartFromParenthesisLast=_t.next().hasClass("rightParenthesis")?true:false;
			//括号里只有一个标签
			var isOnlyoneInParenthesis=_t.prev().prev().hasClass("leftParenthesis")&&_t.next().hasClass("rightParenthesis")?true:false;
			//目标区域在空括号里
			var isToEmptyParenthesis=$(this).prev().hasClass("leftParenthesis")&&($(this).next().hasClass("rightParenthesis")||$(this).next().hasClass("onSortingLabel"))?true:false;
			//如果拖动的是最后一个，并且释放到是最后一个，等于没有拖动；
			if(isDraggingTheLastOne==true){
				if(isDragToTheLast==true) return;
			//如果拖动的是第一个，并且释放到第一个，等于没有拖动；
			}else if(isDraggingTheFirstOne==true){
				if(isDragToTheFirst==true) return;
			}
			if(dragTempCTIndex==0||isInParFirst==true){
				//如果是第一的位置，向后添加连接符；或者目标区域在左括号的右侧1号位，也可认为是第一的位置;
				//但是括号里只有一个元素，则不需要添连接符
				if(isToEmptyParenthesis!=true){
					$(this).after(chaining);
				}
				$(this).after(conditionCT);
			}else{
				//向前添加连接符
				$(this).before(chaining);
				$(this).before(conditionCT);
			}
			
			//如果括号里只有一个标签，那么拖拽它时不用删除旁边的符号
			if(isOnlyoneInParenthesis!=true){
				//如果不是拖拽的最后一个并且不是在括号最后位置，或者目标区域在括号的第一个位置并且不在括号内最后位置，或者在括号第一位置，这时删除后面紧跟的连接符；否则删除前面紧挨的连接符；
				if((!isDraggingTheLastOne&&!isStartFromParenthesisLast)||(isInParFirst==true&&isStartFromParenthesisLast==false&&!isDraggingTheLastOne)||isStartFromParenthesisFirst==true){
					_t.nextAll(".chaining").eq(0).remove();
					//alert(1);
				}
				else{
					_t.prevAll(".chaining").eq(0).remove();
					//alert(2);
				}
			}else{
				//括号里只有一个标签
			}
			
			//删除原标签;在此句以后，不要再使用_t了；
			_t.remove();
			
			$("#tag_dropable,body").css("cursor","default");
			
			//有时间重写，单独的_t.sortLabels(),不用所有的都绑定；
			sortLabels();
			
			//最后删除内部无内容的空括号和它后面的连接符
			$("#tag_dropable .leftParenthesis").each(function(){
				var _next=$(this).nextAll().not(".dragTempCT").eq(0);
				if(_next.attr("creat")==$(this).attr("creat")){
					//alert(_next.attr("class"));
					//三步,1：空括号左侧右侧都有符号，且右侧无label，删除右侧符号
					//2:空括号左侧有符号且右侧无label，删除左侧符号
					//3:空括号右侧有符号且左侧无label，也删除右侧符号（合并到1）
					var leftHas=$(this).prevAll().not(".dragTempCT").eq(0).hasClass("chaining")?true:false;
					var rightHas=_next.nextAll().not(".dragTempCT").eq(0).hasClass("chaining")?true:false;
					if(rightHas==true || leftHas==true&&rightHas==true){
						_next.nextAll(".chaining").eq(0).remove();
					}else if( (leftHas==true&&$(this).nextAll(".conditionCT").length==0) || (leftHas==true&&_next.nextAll().not(".dragTempCT").eq(0).hasClass("rightParenthesis")) ){
						_next.prevAll(".chaining").eq(0).remove();
					}
					var prev=$(this).prev().prev();
					_next.remove();
					$(this).remove();
					
					if(prev.hasClass("leftParenthesis")){
						arguments.callee.apply(prev[0],arguments);
					}
				}
			});
		}
	})
}

//拖拽括号开始时找出可合理drop的位置
function parenthesis_possibility_position(_t){
	//若果没有单独的左边括号
	if(!_t.parent().hasClass("opened")){
		var tarAry=$("#tag_dropable .conditionCT,#tag_dropable .leftParenthesis");
		tarAry.each(function(){
			var dragTempCT=$('<div class="dragTempCT"></div>');
			$(this).before(dragTempCT);
			dragTempCT.droppable({
				greedy:true,
				scope: "parenthesis",
				activeClass: "ui-state-active",
				hoverClass: "dragEnter",
				drop: function( event, ui ) {
					var uuid = newGuid();
					var span=$(getCurlyBraceHTML('(',1,uuid));
					$(this).replaceWith(span);
					_t.parent().addClass("opened");
					$("#tag_dropable .dragTempCT").remove();
					var preEl = $(span[0]).prev();
				}
			})
		})
	}//endof 若果没有单独的左边括号
	else{
		var wait=$("#tag_dropable .waitClose");
		var totalAry=[];
		if(wait.prev().hasClass("leftParenthesis")){
			var creat=wait.prev().attr("creat");
			var stopAry=wait.nextAll(".conditionCT,.parenthesis");
			for(var k=0;k<stopAry.length;k++){
				if($(stopAry[k]).attr("creat")==creat) break;
				totalAry.push(stopAry[k]);
			}
		}
		else{
			var stopAry=wait.nextAll(".conditionCT,.parenthesis");
			for(var k=0;k<stopAry.length;k++){
				if(!$(stopAry[k]).hasClass("conditionCT") && stopAry.filter("[creat='"+$(stopAry[k]).attr("creat")+"']").length<2) break;
				totalAry.push(stopAry[k]);
			}
		}
		var tarAry=[];
		$(totalAry).each(function(){
			var ary=[];
			var ary2=$(this).prevAll(".conditionCT,.parenthesis").andSelf();
			for(var i=ary2.length-1;i>=0;i--){	
				if($(ary2[i]).hasClass("waitClose")) break;
				else{
					ary.push($(ary2[i]));
				}
			}
			var left=[]
			for(var i=ary.length-1;i>=0;i--){
				//$("#easyButton").append('<div style="color:green">'+$(ary[i]).attr("class")+'</div>');
				if($(ary[i]).hasClass("leftParenthesis")){
					left.push($(ary[i]));
				}
				else if($(ary[i]).hasClass("rightParenthesis")){
					//right.push($(ary[i]));
					for(var j=0;j<left.length;j++){
						if( $(left[j]).attr("creat")==$(ary[i]).attr("creat") ){
							left=left.slice(0,j).concat(left.slice(j+1,left.length));
							break;
						}
					}
				}
				else{}
			}
		
			if(left.length==0 &&( $(this).hasClass("conditionCT")||$(this).hasClass("rightParenthesis")) ){
				tarAry.push($(this));
			}
		})
		$(tarAry).each(function(){
			$(this).after('<div class="dragTempCT"></div>');
			$(this).next().droppable({
				greedy:true,
				scope: "parenthesis",
				activeClass: "ui-state-active",
				hoverClass: "dragEnter",
				drop: function( event, ui ) {
					//$(this).replaceWith('<span class="parenthesis rightParenthesis" creat="'+$("#easyConDropDisplay .waitClose").attr("creat")+'"<span class="delCondition" onclick="deletePar(this,e)"></span></span>');
					var span=$(getCurlyBraceHTML(')',1,$("#tag_dropable .waitClose").attr("creat")),true);
					$(this).replaceWith(span);
					$("#tag_dropable .waitClose").removeClass("waitClose");
					_t.parent().removeClass("opened");
					$("#tag_dropable .dragTempCT").remove();
					//右边括号
					submitRules();
				}
			})
		})
	}//endof 有单独的左括号
}




//封装括号HTML
var createdLeftPar=new Array();
function getCurlyBraceHTML(flag,count,uuidObj,needDelete){
	var tmp="";
	var htmlText = "";
	if(flag=="("){
		tmp = "<span class='parenthesis leftParenthesis waitClose' creat='@' selfid='curly_brace_block' calcuElement='" + flag + "'></span>";
	}
	else{

		if(needDelete == true || needDelete==null) {
			tmp = "<span class='parenthesis rightParenthesis' creat='@' selfid='curly_brace_block' calcuElement='" + flag + "'><span class='delCondition' onclick='deletePar(this,event)'></span></span>";
		} else {
			tmp = "<span class='parenthesis rightParenthesis' creat='@' selfid='curly_brace_block' calcuElement='" + flag + "'></span>";
		}
	}
	
	var rslt="";
	if(uuidObj){
		rslt=tmp.replace('@',uuidObj);
	}
	else{
		for(var i=0;i<count;i++){
			if(flag=="("){
				var uuid = newGuid();
				var replaced=tmp.replace('@',uuid);
				rslt+=replaced.replace('waitClose','');
				createdLeftPar.push(uuid);
			}
			else{
				rslt+=tmp.replace('@',createdLeftPar.pop());
			}
		}	
	}
	
	var tmpCT=$("<div></div>").append(rslt);
	var str=$(tmpCT).html();
	$(tmpCT).remove();
	htmlText = htmlText + str;
	return htmlText;
}

//删除条件 同时删除关联的连接符和括号
function deleteThis(obj){
	//删除匹配的括号【与条件直接关联的括号】
	deleteCurlyBraces(obj);
	//删除关联的连接符
	deleteConnectFlags(obj);
	//删除自身
	$(obj).parent().parent().remove();
	submitRules();//删除计算元素之后提交到缓存中
	$(".labelTip").hide();
	$(".customTip").hide();
	$("#calcCenterBody").css("padding-bottom",$("#tagSaveActionBox").height()-20) ;
}
//删除连接符
function deleteConnectFlags(obj){
	var thisConditionCT=$(obj).parents(".conditionCT");
	var pre = thisConditionCT.prev();
	var nex = thisConditionCT.next();
	if(pre.hasClass("waitClose")){
		pre.prev().remove();
		pre.remove();
		$("#parenthesis").removeClass("opened");
		if(thisConditionCT.prevAll(".conditionCT").length == 0){
			thisConditionCT.next(".chaining").remove();
		}
	}
	else{
		if(pre.hasClass("chaining")){
			pre.remove();
		}else if(nex.hasClass("chaining")){
			nex.remove();
		}
	}
}
//删除1个或多个连续的相匹配的{或}
function deleteCurlyBraces(obj){
	var pre = $(obj).parent().parent().prev();
	var nex = $(obj).parent().parent().next();
	if(pre.attr('selfid') == 'curly_brace_block' && nex.attr('selfid') == 'curly_brace_block'){
		pre.remove();
		nex.remove();
		deleteCurlyBraces(obj);//递归删除
	}else{
		return;
	}
}
//括号删除按钮触发事件处理函数
function deletePar(t,evt){
	var e=evt||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$(".onDelPar").removeClass("onDelPar");
	$(t).addClass("onDelPar");
	var posX=$(t).offset().left+9;
	var posY=$(t).offset().top+7;
	if($("body > #delPar").length==0){
		var _ul=$('<ul id="delPar"><li><a href="javascript:void(0)">删除括号</a></li><li><a href="javascript:void(0)">删除括号与内容</a></li></ul>');
		_ul.appendTo("body");
		$(document).click(function(){$("#delPar").hide()});
		$("#delPar li").eq(0).bind("click",delThisPars);
		$("#delPar li").eq(1).bind("click",delThisParsAndCT);
	}
	var tar=$("#delPar"),winW=$(window).width(),tarW=tar.width();
	if(posX+tarW>winW) posX=winW-tarW-6;
	tar.css({"left":posX+"px","top":posY+"px"}).slideDown("fast");
}
//删除括号的处理函数
function delThisPars(){
	var creat=$("body .onDelPar").parent().attr("creat");
	if($(".onDelPar").parent().prev().attr("creat") && $(".onDelPar").parent().prev().attr("creat") == creat){
		//删除前面或后面的连接符
		deleteConnectFlag($(".onDelPar").parent());
	}
	var leftBrackets = $(".onDelPar").parent().siblings("[creat='"+creat+"']");
	var rightBrackets = $(".onDelPar").parent();
	
	leftBrackets.remove();
	rightBrackets.remove();
	$("#delPar").hide();
	submitRules();
}

/**
 * 删除括号内容
 * @param leftEl 左边括号节点
 * @param rightEl 右边括号节点
 */
function delBracketsSession(leftEl, rightEl) {
	var minBracketsSortNum = leftEl.attr("sortNum");
	var maxBracketsSortNum = rightEl.attr("sortNum");
	
	var delCarShopItemUrl = $.ctx + "/ci/ciIndexAction!delCarShopByBrackets.ai2do";
	var para = {
		minSortNum	: minBracketsSortNum,
		maxSortNum	: maxBracketsSortNum
	};
	$.post(delCarShopItemUrl, para, function(result) {
		if(!result.success) {
			showAlert(result.msg, "failed");
		}
	});
}

//仅删除括号的处理函数
function onlyDelThisPars(){
	var creat=$("body .onDelPar").parent().attr("creat");	
	
	var leftBrackets = $(".onDelPar").parent().siblings("[creat='"+creat+"']");
	var rightBrackets = $(".onDelPar").parent();
	
	leftBrackets.remove();
	rightBrackets.remove();
	$("#delPar").hide();
}
//删除括号及整个块的处理函数
function delThisParsAndCT(){
	var creat=$("body .onDelPar").parent().attr("creat");
	//遍历删除括号内部所有的块
	var ary=$(".onDelPar").parent().siblings("[creat='"+creat+"']").nextAll();
	for(var i=0;i<ary.length;i++){
		if(ary.eq(i).attr('creat') == creat ) {
			break;
		} else {
			ary.eq(i).remove();
		}
	}
	var nextObj = $(".onDelPar").parent().next();
	//删除前面或后面的连接符
	deleteConnectFlag($(".onDelPar").parent());
	//调用删除括号的方法
	onlyDelThisPars();

	//处理剩余空括号
	if( nextObj.attr('creat') && nextObj.attr('creat') == nextObj.prev().attr('creat')){
		delEmptyParsAndCF(nextObj);
	}
	submitRules();
}
//循环删除空括号和前面的连接符
function delEmptyParsAndCF(obj){
	//删除前面或后面的连接符
	deleteConnectFlag(obj);
	
	var nextObj = obj.next();
	
	//删除自己和与自己配对的括号
	obj.prev().remove();
	obj.remove();
	
	//判断如果后面是括号，则循环执行删除
	if(nextObj.attr('creat') && nextObj.attr('creat') == nextObj.prev().attr('creat')){
		delEmptyParsAndCF(nextObj);
	}else{
		return;
	}
}
//删除连接符号
function deleteConnectFlag(obj){
	if(obj.prev().prev().hasClass("chaining")){
		obj.prev().prev().remove();
	}
	if(obj.prevAll(".conditionCT").length == 0){ //|| obj.prev().prev().attr('selfid')=='curly_brace_block'){
		obj.next(".chaining").remove();
	}
}

//下拉切换and or
function switchConnector(t,e){
	var e=e||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$(t).blur();
	$("#tag_dropable .onConnector").removeClass("onConnector");
	$(t).addClass("onConnector");
	var posX=$(t).parent().offset().left;
	var posY=$(t).offset().top+38;
	if($("body > #connector").length==0){
		var _ul=$('<ul id="connector"><li><a href="javascript:void(0)">且</a></li><li><a href="javascript:void(0)">或</a></li><li><a href="javascript:void(0)">剔除</a></li></ul>');
		_ul.appendTo("body");
		$(document).click(function(){
			$(t).removeClass("onConnector");
			_ul.hide();
		});
		_ul.find("li").bind("click",function(){
			$("#tag_dropable .onConnector").prev().html($(this).find("a").html());
			$(this).parent().hide();
			clearOptAllChaining();
			submitRules();
		});
		$(window).scroll(connectorPosFixed);
	}
	var tar=$("#connector"),winW=$(window).width(),tarW=tar.width(),winH=$(window).height()+$(window).scrollTop(),tarH=tar.height();
	if(posX+tarW>winW) posX=winW-tarW-14;
	if(posY+tarH>winH) posY=posY-tarH-40;
	tar.css({"left":posX+"px","top":posY+"px"}).show();
}
var delay_connectorPosFixed=false;
function connectorPosFixed(){
	var connector=$("#connector");
	if(connector.css("display")!="block") return;
	
	if(delay_connectorPosFixed!=false) clearTimeout(delay_connectorPosFixed);
	delay_connectorPosFixed=setTimeout(function(){
		var now_winscrollTop=$(window).scrollTop();
		var onConnectorCT=$("#tag_dropable .onConnector");
		var winW=$(window).width(),tarW=connector.width(),winH=$(window).height()+$(window).scrollTop(),tarH=connector.height();
		var posX=onConnectorCT.offset().left-38;
		var posY=onConnectorCT.offset().top+39;
		if(posX+tarW>winW) posX=winW-tarW-14;
		if(posY+tarH>winH) posY=posY-tarH-40;
		connector.animate({top:posY,left:posX},200);
		delay_connectorPosFixed=false;
	},100);
}

var connectFlagArray=[ "且","或","剔除" ];
function switchConnector_turn(t,e){
	var e=e||window.event;
	var obj=$(t);
	var old=obj.html();
	obj.parent().addClass("chainingOnclick");
	setTimeout(function(){
		obj.parent().removeClass("chainingOnclick");
	},200)
	var next="";
	if (/msie/.test(navigator.userAgent.toLowerCase()) && !$.support.leadingWhitespace){
		if(e.button==1) next=true;
		else if(e.button==2) next=false;
	}else{
		if(e.button==0) next=true;
		else if(e.button==2) next=false;
	}
	var _new="";
	for(var i=0;i<connectFlagArray.length;i++){
		if(connectFlagArray[i]==old){
			if(next==true){
				//左键
				if(i==connectFlagArray.length-1) _new=connectFlagArray[0];
				else _new=connectFlagArray[i+1];
			}else{
				//右键
				if(i==0) _new=connectFlagArray[connectFlagArray.length-1];
				else _new=connectFlagArray[i-1];
			}
		}
	}
	obj.html(_new);
	clearOptAllChaining();
	submitRules();
	return false;
}

//生成唯一标识
function newGuid(){ 
	var guid = ""; 
	for (var i = 1; i <= 32; i++){ 
		var n = Math.floor(Math.random()*16.0).toString(16); 
		guid += n; 
		if((i==8)||(i==12)||(i==16)||(i==20)) 
			guid += "-"; 
	} 
	return guid; 
} 

//统一调整标签条件设置区域宽度
function resizeConditionDiv(){
	$("#tag_dropable .conditionCT").each(function(){
		if($(this).hasClass("conditionCT_short")){
			var _w=$(this).width();
			if(_w<96){
				$(this).width(96).find(".midCT").width(54);
			}
		}else{
			var _w=$(this).width();
			if(_w<165){
				//TODO 判断_w是否是小数，然后对象+1或-1
				$(this).width(165);
				$(this).find(".midCT").width(83);
				$(this).find(".conditionMore").width(153);
			}else{
				$(this).width(_w + 1);
				$(this).find(".conditionMore").width(_w-12);
			}
		}
	});
}