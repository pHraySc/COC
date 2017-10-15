<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
	String indexTitle = Configure.getInstance().getProperty("INDEX_TITLE");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<c:set var="source" value="${source}"/>
<head>
<title><%=indexTitle%></title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/shopCart.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.cookie.js"></script>
<script type="text/javascript">
var month = "${newLabelMonth}";
//iframe 方式 子调用父方法 关闭弹出框
function iframeCloseDialog(obj){
   $(obj).dialog("close");
}
//30秒会操作:关闭演示弹出层
function closeProcessIntro(){   $("#processIframe").hide();}
$(document).ready(function(){
    //30秒会操作:演示弹出层宽和高动态设置
	$("#processIframe").height($(document).height());
    //30秒会操作:获取是否跳过演示标识值
	var cookieNums = getCookieByName("INDEX_NUMS_COOKIE_${userId}");
/* 	if(cookieNums<=1){
		canOperation30();
	} */
	//30秒会操作:检测F11按键动态改变高度
	$(document).keyup(function (ev) {
		var event = ev|| event;
		if(event.keyCode == "122" && !processFlag){
			$("#processIframe").height($(document).height());
			processIframe.window.changeBodyHeightWidth();
		}
	});
	
	//30秒会操作:关闭弹出框
	$(".dialogClose ,.btnActiveBox input").click(function(){ $(this).parent().parent().dialog("close");})
	$("#forward_seachTypeId").val('${seachTypeId}');
	$("#refreshType").val('${refreshType}');
	$("#sceneName").val(decodeURIComponent('${sceneName}'));
	$("#sceneId").val('${sceneId}');
	$("#isDefaultLoad").val('${isDefaultLoad}');
	$("#forward_keyword").val(decodeURIComponent('${seachKeyWord}'));
	$("#userOperType").val('${userOperType}');
	$("#userCityId").val('${cityId}');
	//时间轴
	if(month == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
			type: "POST",
			success: function(result){
				month = result;
				creatDateline($("#dateline_ct"),month,checkDate);
			}
		});
	}else{
		creatDateline($("#dateline_ct"),month,checkDate);
	}
	
	function formatItem(row) {
		return " <p>"+row[0] +" </p>";
	}
	
	function formatResult(row) {
		return row[0].replace(/(<.+?>)/gi, '');
	}
	
	
	//左侧导航及首页标签搜索tab页选中样式
	$(".bg01").addClass("active");
	$("#_index").addClass("menu_on");
	
	//点击标签取反
	$("#tag_dropable").click(function(e){
		var e=e||window.event;
		var tar=$(e.target||e.srcElement);
		if(!tar.hasClass("midCT")) return;
		var labelFlag = tar.parent().attr("flag");
		if(tar.parent().hasClass("conditionCT_active")){
			tar.parent().removeClass("conditionCT_active");
			if(labelFlag > 1){
				tar.parent().attr("flag", 3);
			}else{
				tar.parent().attr("flag", 1);
			}
		}else if(tar.parent().hasClass("conditionCT")){
			tar.parent().addClass("conditionCT_active");
			if(labelFlag > 1){
				tar.parent().attr("flag", 2);
			}else{
				tar.parent().attr("flag", 0);
			}
		}
	});
	
	$("#createDialog").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 660,
		title: "保存客户群",
		modal: true,
		resizable:false
	});
	
	$("#successDialog").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	$("#confirmDialog").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 500,
		height:185,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	$("#exploreBtn").bind("click",function(){
		toExplore();
	});
	
	$("#saveCustomerBtn").bind("click", function(){
		toSave();
	});
	
	//标签鼠标滑过加背景色
	$(document).on("mouseover", ".tagMapListBox .isCanDrag", function(){
		if($(this).hasClass("cannotDrag")) return;
		$(this).addClass("hover");
	}).on("mouseout", ".tagMapListBox .isCanDrag", function(){
		$(this).removeClass("hover");
	});
	//根据forwardType的值跳转到相应的页面：
	//默认进入首页
	//1:跳转到标签计算中心(创建客户群)；2:跳转到标签计算中心(修改客户群)；
	//3:跳转到标签管理模块；
	//4:跳转到消息管理模块；
	//5:跳转到我的收藏模块；
	//6:跳转到排行榜模块；
	//7:跳转到资源审批模块；
	//8:跳转到预警监控模块；
	//9:跳转到信息反馈模块；
	var forwardType = $("#forwardType").val();
	$("#quoteSource").val("");
	shopCarList();
	if(forwardType == null || forwardType == 0 || forwardType == ""){//默认进入首页
		$("#shopCartBox").show();
		$("#mainContent").load("${ctx}/aibi_ci/home/labelMap.jsp");
	}else if(forwardType == 1){//打开标签计算中心
		$("#shopCartBox").hide();
		$("#quoteSource").val("calculateCenter");
		
		var url = $.ctx + "/ci/ciIndexAction!findCalculateCenter.ai2do";
		if (getCookieByName("${version_flag}") == "false") {
			url = $.ctx + ctx"/ci/ciIndexAction!findCalculateCenterOld.ai2do";
		}
		
		if("${ciCustomGroupInfo.customGroupId}" != null && "${ciCustomGroupInfo.customGroupId}" != ""){
			url += "?ciCustomGroupInfo.customGroupId=" + "${ciCustomGroupInfo.customGroupId}";
		}
		$("#mainContent").load(url);
	}else if(forwardType == 2){
		$("#shopCartBox").hide();
		$("#quoteSource").val("calculateCenter");
		
		var url = $.ctx + "/ci/ciIndexAction!findCalculateCenter.ai2do";
		if (getCookieByName("${version_flag}") == "false") {
			url = $.ctx + "/ci/ciIndexAction!findCalculateCenterOld.ai2do";
		}
		$("#mainContent").load(url);
	}else if(forwardType == 3){//标签管理
		$("#shopCartBox").hide();
		var showType = "${labelSearchType}";
		$("#mainContent").load("${ctx}/ci/ciLabelInfoAction!labelManageIndex.ai2do?showType="+showType);
	}else if(forwardType == 4){//消息管理
		$("#shopCartBox").hide();
		var showType = "${messageSearchType}";
		var noticeId = "${noticeId}";
		var noticeType = "${noticeType}";
    	if(showType == ""){
	    	$("#mainContent").load("${ctx}/ci/ciPersonNoticeAction!noticeCenter.ai2do");
        }else{
        	if(noticeId != null && noticeId != ""){
		    	$("#mainContent").load("${ctx}/ci/ciPersonNoticeAction!noticeCenter.ai2do?pojo.noticeId="+noticeId+"&pojo.typeId="+showType+"&pojo.noticeType="+noticeType);
        	}else{
		    	$("#mainContent").load("${ctx}/ci/ciPersonNoticeAction!noticeCenter.ai2do?pojo.typeId="+showType+"&pojo.noticeType="+noticeType);
        	}
        	
        }
	}else if(forwardType == 5){//我的收藏
		$("#shopCartBox").show();
		var attentionTypeId = "${attentionTypeId}";
		if(attentionTypeId != ""){
			$("#mainContent").load("${ctx}/ci/ciIndexAction!attentionIndex.ai2do?attentionTypeId="+attentionTypeId);
		}else{
			$("#mainContent").load("${ctx}/ci/ciIndexAction!attentionIndex.ai2do");
		}
	}else if(forwardType == 6){//排行榜
		$("#mainContent").load("${ctx}/ci/ciIndexAction!rankingListViewIndex.ai2do");
	}else if(forwardType == 7){//资源审批
		$("#shopCartBox").hide();
		var showType = "${resourceShowType}";
		$("#mainContent").load("${ctx}/ci/ciApproveAction!init.ai2do?showType="+showType);
	}else if(forwardType == 8){//预警监控
		$("#shopCartBox").hide();
		var showType = "${alarmShowType}";
		$("#mainContent").load("${ctx}/ci/ciCustomersAlarmAction!alarmMain.ai2do?showType="+showType);
	}else if(forwardType == 9){//信息反馈
		$("#shopCartBox").hide();
		$("#mainContent").load("${ctx}/ci/feedback/ciFeedbackAction!init.ai2do?source=${source}");
	}else if(forwardType == 10){//客户群清单下载列表
		$("#shopCartBox").hide();
		$("#mainContent").load("${ctx}/ci/customersFileAction!init.ai2do");
	}else if(forwardType==11){//top100
		$("#mainContent").load("${ctx}/ci/ciIndexAction!findAllTop100Map.ai2do");
	}
});

//加载标签地图
function loadMap(){
	$("#shopCartBox").show();
	$("#mainContent").load("${ctx}/aibi_ci/home/labelMap.jsp");
}
//保存客户群
function toSave(){
	createForm();
	if(validateSql()){
		openCreate();
	}
}
//保存客户群弹出层
function openCreate(){
	$("#createDialog").dialog({
		close: function( event, ui ) {
			$("#createFrame").attr("src", "");
		}
	});
	var ifmUrl = "${ctx}/aibi_ci/customers/createCustomer.jsp";
	$("#createFrame").attr("src", ifmUrl).load(function(){resetForm();});
	$("#createDialog").dialog("open");
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#createDialog").dialog("close");
	clearCondition();
}
//打开保存成功弹出层
function openSuccess(customerName, templateName){
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	templateName = encodeURIComponent(encodeURIComponent(templateName));
	$("#successDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
}

//清空数据探索条件
function clearCondition(){
	$("#parenthesis").removeClass("opened");
	$("#inputList").empty();
	$("#tag_dropable").empty();
	$("#customerNum").empty().append("<p></p>");
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml == null || innerHtml ==""){
		$("#exploreBtn").hide();
		$("#saveCustomerBtn").hide();
		$("#exploreBtnDis").show();
		$("#saveCustomerBtnDis").show();
	}
}
//还原iframe中的表单
function resetForm(){
	var ifm = $("#createFrame").contents();
	//删除表单list
	ifm.find("#listForm").empty();
	//编辑时填充表单
	/*
	var templateId = $("#templateId").val();
	if(templateId != null && templateId != ""){
		ifm.find("#template_id").val(templateId);
		ifm.find("#template_name").val($("#templateName").val());
		ifm.find("#custom_desc").val($("#templateDesc").val());
		ifm.find("#template_status").val($("#status").val());
		ifm.find("#template_userId").val($("#userId").val());
		ifm.find("#template_addTime").val($("#addTime").val());
	}
	ifm.find("#template_labelOptRuleShow").val($("#labelOptRuleShow").val());
	*/
	ifm.find("#custom_desc").val("");
}
//数据探索
function toExplore(){
	show_overlay();
	//控制按钮
	$("#exploreBtn").hide();
	$("#saveCustomerBtn").hide();
	$("#exploreBtnDis").show();
	$("#saveCustomerBtnDis").show();
	$("#customerNum").empty().append("请稍候...");
	createForm();
	if(validateSql()){
		submitForExplore();
	}else{
		$("#customerNum").empty().append("<p></p>");
		$("#exploreBtn").show();
		$("#saveCustomerBtn").show();
		$("#exploreBtnDis").hide();
		$("#saveCustomerBtnDis").hide();
		hide_overlay();
	}
}
//数据探索提交表单
function submitForExplore(){
	var single_ele = $("#tag_dropable .conditionCT").not(".conditionCT_active");
	var active_ele = $("#tag_dropable .conditionCT_active");
	var length = single_ele.size();
	if (length == 1 && active_ele.size() == 0){
		var url = $.ctx + '/ci/ciIndexAction!getSingleLabelCustomNum.ai2do';
		$.ajax({
			url: url,
			type: "POST",
			data:'labelId=' + single_ele.attr("element") + '&dataDate=' + $("#dataDate").val() + '&exploreFromModuleFlag=1',
			success: function(result){
				if (result.success) {
					var num = $.formatNumber(result.retCustomNum, 3, ',');
					$("#customerNum").empty().append("用户数&nbsp;<p>" + num + "</p>");
					resizeDropct();
				} else {
					showAlert(result.msg,"failed");
				}
				$("#exploreBtn").show();
				$("#saveCustomerBtn").show();
				$("#exploreBtnDis").hide();
				$("#saveCustomerBtnDis").hide();
				hide_overlay();
			}
		});
	}else{
		var actionUrl = $.ctx + "/ci/customersManagerAction!findCustomerNumForExplore.ai2do";
		$.post(actionUrl, $("#exploreForm").serialize(), function(result){
			if (result.success) {
				var num = "";
				if((result.msg != null && result.msg != "") || result.msg == 0){
					num = $.formatNumber(result.msg, 3, ',');
				}
				$("#customerNum").empty().append("用户数&nbsp;<p>" + num + "</p>");
			} else {
				showAlert(result.msg,"failed");
			}
			$("#exploreBtn").show();
			$("#saveCustomerBtn").show();
			$("#exploreBtnDis").hide();
			$("#saveCustomerBtnDis").hide();
			hide_overlay();
		});
	}
}

//整合条件，拼接表单
function createForm(){
	$("#dataDate").val($(".date_line").attr("_value"));
	
	$("#inputList").empty();
	var labelOptRule = "";
	var html = "";
	$("#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis").each(function(i, element){
		var ele = $(element);
		var calcuElement = "", minVal="", maxVal="", elementType="", labelFlag="", nameStr="", type="", attrVal="";
		if(ele.hasClass("conditionCT")){//处理标签
			calcuElement = ele.attr("element");
			type = ele.attr("type");
			elementType = ele.attr("elementType");
			nameStr = ele.children(".midCT").text();
			minVal = ele.attr("min");
			maxVal = ele.attr("max");
			labelFlag = ele.attr("flag");
			if(type == 1){//标识类标签
				if(labelFlag != null && labelFlag != ""){
					if(labelFlag == 0){
						nameStr = "非 " + nameStr + " ";
					}else if(labelFlag == 1){
						nameStr = nameStr + " ";
					}
				}
			}else if(type == 2){//得分类标签
				if(labelFlag != null && labelFlag != ""){
					if(labelFlag == 0){
						nameStr = "非 " + nameStr + " ";
					}else if(labelFlag == 1){
						nameStr = nameStr + " ";
					}else if(labelFlag == 2){//为后续准备，选得分之后的取反标记
						nameStr = nameStr + " < " + minVal + " 或者 " + nameStr + " > " + maxVal + " ";
					}else if(labelFlag == 3){//为后续准备，选得分之后的标记
						nameStr = minVal + " <= " + nameStr + " <= " + maxVal + " ";
					}
					if(labelFlag > 1){
						minVal = ele.attr("minVal");
						maxVal = ele.attr("maxVal");
					}
				}
			}else if(type == 3){//属性类标签
				attrVal = ele.attr("attrVal");
				if(labelFlag != null && labelFlag != ""){
					if(labelFlag == 0){
						nameStr = "非 " + nameStr + " ";
					}else if(labelFlag == 1){
						nameStr = nameStr + " ";
					}
				}
			}
		}else if(ele.hasClass("chaining")){//处理逻辑运算
			var name = ele.find("span").text();
			if(name == "且"){
				calcuElement = "and";
			}else if(name == "或"){
				calcuElement = "or";
			}else if(name == "剔除"){
				calcuElement = "-";
			}
			nameStr += name + " ";
			elementType = 1;
		}else if(ele.hasClass("parenthesis")){//处理括号
			calcuElement = ele.children("input").val();
			elementType = 3;
			nameStr = calcuElement + " ";
		}
		labelOptRule += nameStr;
		html += '<input type="hidden" name="ciLabelRuleList[' + i + '].calcuElement" value="' + calcuElement + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].minVal" value="' + minVal + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].maxVal" value="' + maxVal + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].sortNum" value="' + (i+1) + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].elementType" value="' + elementType + '"/>';
		if(elementType == 2){
			html += '<input type="hidden" name="ciLabelRuleList[' + i + '].labelFlag" value="' + labelFlag + '"/>';
			if(type == 3){//属性类标签
				html += '<input type="hidden" name="ciLabelRuleList[' + i + '].attrVal" value="' + attrVal + '"/>';
			}
		}
	});
	$("#labelOptRuleShow").val(labelOptRule);
	$("#inputList").append(html);
}
//SQL验证
function validateSql(){
	var flag = true;
	/* 提示弱化
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml == null || innerHtml ==""){
		showAlert("请拖拽至少一个标签！","failed");
		return false;
	} */
	if($(".waitClose").length>0){
		showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
		return false;
	}
	if($(".outOfDate").length>0){
		showAlert("有红框的标签，当前选择的月份数据未生成！","failed");
		return false;
	}
	if($(".conditionCT").length>$._maxLabelNum){
		showAlert("最多拖动" + $._maxLabelNum + "个标签！","failed");
		return false;
	}
	var actionUrl = $.ctx + "/ci/customersManagerAction!validateSql.ai2do";
	$.ajax({
		url: actionUrl,
	    type: "POST",
	    async: false,//同步
		data: $("#exploreForm").serialize(), 
		success: function(result){
			flag = result.success;
			if(!flag){
				showAlert(result.msg,"failed");
			}
		}
	});
	return flag;
}


//释放正在拖动的标签
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_dropable").droppable({
		accept: ".tagList_ul li,.canDrag",
		greedy:true,
		drop: function( event, ui ) {
			var onDragTag=ui.draggable;
			tagOnDrop(onDragTag,$(this));
		}
	});
}
//拼接条件显示区
function tagOnDrop(dragtag,ct){
	if($(".conditionCT").length>=$._maxLabelNum){
		showAlert("最多拖动" + $._maxLabelNum + "个标签！","failed");
		return false;
	}
	var _chaining='<div class="chaining">';
	_chaining+='<div class="left_bg"></div>';
	_chaining+='<div class="midCT">';
	_chaining+='<span onmousedown="switchConnector_turn(this,event)">且</span><a onclick="switchConnector(this,event)" href="javascript:void(0)" class="icon_rightArr">&nbsp;</a>';
	_chaining+='</div>';
	_chaining+='<div class="right_bg"></div>';
	_chaining+='</div>';
	var _txt=dragtag.attr("labelName");
	var html='<div class="conditionCT';
	var type = dragtag.attr("type");
	var choiceDate = $(".date_line").attr("_value");
	var effectDate = dragtag.attr("effectDate");
	if(choiceDate < effectDate){
		html+=' outOfDate';
	}
	var newDay = "${dateFmt:dateFormat(newLabelDay)}";
	if(newDay != null && dragtag.attr("updateCycle") == 1){
		html+='" title="数据日期：' + newDay;
	}
	html+='" element="' + dragtag.attr("element") + '" effectDate="' + dragtag.attr("effectDate") + '" ';
	html+='min="0" max="1" ';
	if(type == 2){//得分类标签
		html+='minVal="'+ dragtag.attr("min") + '" maxVal="'+ dragtag.attr("max") + '" ';
	}
	if(type == 3){//属性类标签
		html+='attrVal="' + dragtag.attr("attrVal") + '" ';
	}
	html+='type="'+ dragtag.attr("type") + '" elementType="' + dragtag.attr("elementType") + '" flag="1">';
	html+='<div class="left_bg">';
	html+='<a href="javascript:void(0)" onclick="deleteThis(this)"></a>';
	html+='</div><div class="midCT">'+_txt+'</div>';
	html+='<div class="right_bg"></div>';
	html+='</div>';
	if(ct.find(".conditionCT").length==0){
		ct.append(html);
	}else{
		ct.append(_chaining+html);
	}
	var tar=$("body > .tag_operation");
	if(!tar.hasClass("firstdragDone")){
		tar.addClass("firstdragDone").find(".firsttime_tip").show();
	}
	//拖动时变色
	changeColorWhenDrag();
	
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml != null && innerHtml !=""){
		$("#exploreBtn").show();
		$("#saveCustomerBtn").show();
		$("#exploreBtnDis").hide();
		$("#saveCustomerBtnDis").hide();
	}
}
function checkDate(choiceDate){
	$(".conditionCT").each(function(){
		var effectDate = $(this).attr("effectDate");
		if(choiceDate < effectDate){
			if(!$(this).hasClass("outOfDate")){
				$(this).addClass("outOfDate");
			}
		}else{
			if($(this).hasClass("outOfDate")){
				$(this).removeClass("outOfDate");
			}
		}
	});
}
function addTag(obj){
	tagOnDrop($(obj), $("#tag_dropable"));
}
//删除条件 同时删除关联的连接符和括号
function deleteThis(obj){
	//删除匹配的括号【与条件直接关联的括号】
	deleteCurlyBraces(obj);
	//删除关联的连接符
	deleteConnectFlags(obj);
	//删除自身
	$(obj).parent().parent().remove();
	
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml == null || innerHtml ==""){
		$("#exploreBtn").hide();
		$("#saveCustomerBtn").hide();
		$("#exploreBtnDis").show();
		$("#saveCustomerBtnDis").show();
		$("#customerNum").empty().append("<p></p>");
	}
}
//checkbox单选
function controlAll(obj,text){
	if(obj.checked){
		$(obj).parent().parent().find("input:checked").not(obj).attr("checked",false);
		$("#tag_dropable").find(".chaining").find("span").text(text);
	}
}

//打开标签微分链接
function openLabelDifferential(labelId){
	var param = "ciLabelInfo.labelId="+labelId;
	param = param + "&fromPageFlag=1&tab=tag";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//打开标签关联信息链接
function openLabelRel(labelId) {
	var param = "ciLabelInfo.labelId="+labelId;
		param = param + "&ciLabelInfo.dataDate="+month;
		param = param + "&fromPageFlag=1&tab=rel";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");	
}

//标签对比分析连接
function openLabelContrast(labelId){
	var param = "ciLabelInfo.labelId="+labelId;
		param = param + "&dataDate="+month;
		param = param + "&fromPageFlag=1&tab=ant";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");	
}

//标签分析连接
function openLabelAnalysis(labelId){
	var param = "selectLabelId="+labelId + "&fromPageFlag=1";
    var url = $.ctx+"/ci/ciLabelAnalysisAction!initlabelAnalysisIndex.ai2do?"+param;
    top.location.href = url;
    //window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");	
}

//点击探索按钮时候显示遮罩
function show_overlay(){
	$("body > .tag_operation .tag_operation_overlay").show();
	$("#tag_dropable").droppable("disable");
}
function hide_overlay(){
	$("body > .tag_operation .tag_operation_overlay").hide();
	$("#tag_dropable").droppable("enable");
}
//点击首页标签地图'展示全部'时候显示遮罩
function showOverlay(){
	//$("body > .labelPageOverlay .tag_operation_overlay").show();
	if('undefined' == typeof(document.body.style.maxHeight)){
		$(window).bind("scroll",ie6_noscroll)
	}else{
		$("body,html").css("overflow","hidden");
	}
	var obj=$(window);
	var w=obj.width();
	var h=obj.height();
	var _left=obj.scrollLeft();
	var _top=obj.scrollTop();
	var html=$('<div class="overlay"><div class="loadding"></div></div>');
	html.css({"left":_left,"top":_top,"width":w,"height":h});
	$("body").append(html);
}
function hideOverlay(){
	//$("body > .labelPageOverlay .tag_operation_overlay").hide();
	$("body").find("> .overlay").remove();
	if('undefined' == typeof(document.body.style.maxHeight)){
		$(window).unbind("scroll",ie6_noscroll)
	}else{
		$("body,html").css("overflow","auto");
	}
}
//确认清空
function confirmClearAll(){
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml == null || innerHtml ==""){
		return;
	}
	openConfirm("确定要清空吗？", "");
}
//打开确认弹出层
function openConfirm(confirmMsg, id){
	confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
	$("#confirmDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
	$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
//关闭确认弹出层
function closeConfirmDialog(dialogId, isSome, id){
	$(dialogId).dialog("close");
	if(isSome){
		clearCondition();	
	}
}
//用户关注标签查询
function showUserAttentionLabel(){
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/ciLabelInfo/userAttentionLabel.jsp",function(){loadLabelList();});
}
//用户使用过的标签查询
function showUserUsedLabel(){
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/ciLabelInfo/userUsedLabel.jsp",function(){loadLabelList();});
}
//三十秒会操作
function canOperation30(){
	$("#processIframe").show();
}
</script>
</head>

<body>
<input type="hidden" id="quoteSource"/>
<input type="hidden" id="forward_seachTypeId"/>
<input type="hidden" id="forward_keyword"/>
<input type="hidden" id="userOperType"/>
<input type="hidden" id="refreshType"/>
<input type="hidden" id="sceneName"/>
<input type="hidden" id="sceneId"/>
<input type="hidden" id="isDefaultLoad"/>
<input type="hidden" id="userCityId"/>
<input type="hidden" id="labelTypeMap"/>
<input type="hidden" id="c1LabelId"/>
<input type="hidden" id="l1LabelName"/>
<input type="hidden" id="c2LabelId"/>
<input type="hidden" id="l2LabelName"/>
<input type="hidden" id="c3LabelId"/>
<input type="hidden" id="l3LabelName"/>
<input type="hidden" id="forwardType" name="forwardType" value="${forwardType }"/>
<input type="hidden" id="calculateCenterPreUrl" name="url" value="${url}"/>
<!-- <div class="sync_height"> -->
    <jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
    <div class="mainBody">
        <!-- search -->
        <jsp:include page="/aibi_ci/index_top.jsp" ></jsp:include>
        <div id="mainContent"></div>
    </div>
    <div class="clear"></div>
<!--</div>sync_height end -->
<!--<div class="tag_operation tag_operation_hided">
	<div class="tag_operation_inner">
		<div class="tag_operation_overlay">
        	<div class="loadding"></div>
        </div>
        <div class="tag_dropCT">
            <div id="dateline_ct"></div>
            <div id="tag_dropable" class="tag_dropable">
            </div>#tag_dropable end 
        </div>.tag_dropCT end 
        <ul class="btns">
            <li class="customerNum" id="customerNum"><p></p></li>
            <li id="exploreBtn" style="display:none;"><a href="javascript:void(0)" class=" btn_explore">探索</a></li>
            <li id="saveCustomerBtn" style="display:none;"><a href="javascript:void(0)" class="btn_save btn_save">保存客户群</a></li>
            <li id="exploreBtnDis"><a href="javascript:void(0)" class="btn_explore btn_explore_dis">探索</a></li>
            <li id="saveCustomerBtnDis"><a href="javascript:void(0)" class="btn_save btn_save_dis">保存客户群</a></li>
            <li><span class="icon_expand" onclick="expandTagperation(this)">展开</span></li>
        </ul>
        <div class="icon_sjts">数据探索</div>
        <div id="parenthesis" class="parenthesis">
            <div class="leftParenthesis"></div>
            <div class="rightParenthesis"></div>
        </div>
        <ul class="controlAll">
			<li><input type="checkbox" onclick='controlAll(this,"且")'/>全部(且)</li>
            <li><input type="checkbox" onclick='controlAll(this,"或")'/>全部(或)</li>
            <li><input type="checkbox" onclick='controlAll(this,"剔除")'/>全部(剔除)</li>
        </ul>
        
        <div class="clearAll"><a href="javascript:void(0)" onclick="confirmClearAll();">清空</a></div>
        
        <div class="firsttime_tip">
            <div class="inner">
                <div class="close"><a href="javascript:void(0);" onclick="firsttime_tip_remove(this)">×</a></div>
                <p>鼠标单击标签可取反!</p>
            </div>
            <div class="arrow"></div>
        </div>
    </div>
</div>tag_operation end -->
<jsp:include page="/aibi_ci/toolTip.jsp"></jsp:include>
<jsp:include page="/aibi_ci/shopCart.jsp"></jsp:include>
<!-- form -->
<form id="exploreForm">
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>
	<input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value=""/>
	<%-- <input type="hidden" name="ciCustomGroupInfo.prodOptRuleShow" value="${ciCustomGroupInfo.prodOptRuleShow}"/>
	<input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" value="${ciCustomGroupInfo.customOptRuleShow}"/>
	<input type="hidden" name="ciCustomGroupInfo.kpiDiffRule" value="${ciCustomGroupInfo.kpiDiffRule}"/> --%>
	<input type="hidden" name="ciCustomGroupInfo.createTypeId" value="1"/>
	<input type="hidden" name="exploreFromModuleFlag" value="1"/>
	<input type="hidden" name="saveCustomersFlag" value="1"/>
	<div id="inputList" style="display:none;"></div>
</form>
<div id="createDialog" style="overflow-y:hidden;">
	<iframe id="createFrame" name="createFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:310px"></iframe>
</div>
<div id="successDialog" style="overflow:hidden;">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:365px"></iframe>
</div>
<div id="confirmDialog" style="overflow:hidden;">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<iframe style="display:none;" id="processIframe" class="processIframe" src="${ctx}/aibi_ci/index30.jsp" framespacing="0" frameborder="0" width="100%" height="100%" scrolling="no" > </iframe>
</body>
</html>
