<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>标签微分</title>

<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/js/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet"  />
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<script type="text/javascript">
$(function(){
	showLabelTree("ct");
	$("#createDialog").dialog({
		autoOpen: false,
		resizable:false,
		width: 660,
		title: "保存客户群",
		modal: true,
		close:function(){
			$("#createFrame").attr("src", "");
		}
	});
	$("#successDialog").dialog({
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	//高度
	$(".analyse_left").click(function(e){
		e=e||window.event;
		var tar=$(e.target||e.srcElement).parent();
		if(tar.hasClass("treenode")){
			setTimeout(autoHeight,100);
		}
	});
	var month = "${newLabelMonth}";
	
	if(month == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
			type: "POST",
			success: function(result){
				month = result;
				creatDateline($("#dateline_ct"),month,changeNum);
				if('${param["dataDate"]}'!=''){
					 var month2 = '${param["dataDate"]}';
					 if(month2.length ==6){
						//取消默认的日期选择
						$("li[_value='"+month+"']").removeClass("onchoose");
						//设置传入的日期为选择状态
						$("li[_value='"+month2+"']").addClass("onchoose");	 
						 month = month2;
					 }
				}
				changeNum(month);
			}
		});
	}else{
		creatDateline($("#dateline_ct"),month,changeNum);
		if('${param["dataDate"]}'!=''){
			 var month2 = '${param["dataDate"]}';
			 if(month2.length ==6){
				//取消默认的日期选择
				$("li[_value='"+month+"']").removeClass("onchoose");
				//设置传入的日期为选择状态
				$("li[_value='"+month2+"']").addClass("onchoose");
				month = month2;
			 }
		}
		changeNum(month);
	}
	$("#backBtn").click(function(){
		if(sortNum > 2){
			var historyStr = $("#baseForm").find("input[name$='"+(sortNum-1)+"].history']").val();
			$("#baseForm").find("input[name^='ciLabelRuleList["+(sortNum-1)+"']").remove();
			$("#baseForm").find("input[name^='ciLabelRuleList["+(sortNum-2)+"']").remove();
			sortNum -= 2;
			if(sortNum <=2 ){
				$("#backBtn").addClass("tag_btn_disable");
			}
			showHistory(historyStr);
			changeTitle();
			changeNum();
		}
		checkButton($(".analyse_show"),false);
	});
});
//自动高度
function autoHeight(){
	var _autoheight = $(".analyse_left").height();
	var _autoheight2 = $(".analyse_right").height();
	if(_autoheight2 > _autoheight){
		_autoheight = _autoheight2;
	}
	var contentFrame = $("iframe:visible",window.parent.document);
	contentFrame.height(_autoheight+50);
}

$(document).click(function(){
	$("#tag_analyse_dropable .analyse_show_slided").removeClass("analyse_show_slided").find(".slidelist").hide();
})
//打开保存成功弹出层
function openSuccess(customerName, templateName){
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	templateName = encodeURIComponent(encodeURIComponent(templateName));
	$("#successDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
}

var ct_currentNodeId = '${ciLabelInfo.labelId}';
var my_currentNodeId = '${ciLabelInfo.labelId}';
function initTree() {
	//初始化第一级node的样式
	$.fn.zTree.init($("#analyse_tree"), setting, zNodes);
	zTree_Menu = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodes_0=zTree_Menu.getNodes()[0];
	curMenu = nodes_0;
	if(!curMenu){
		return;
	}
	var parentNode = nodes_0;
	while(curMenu.children && curMenu.children.length > 0){
		curMenu = curMenu.children[0];
	}
	var who = $('#labelType').val();
	var currentNodeId = "";
	if("ct" == who){
		currentNodeId = ct_currentNodeId ;
	}else{
		currentNodeId = my_currentNodeId;
	}
	var nodeObj = zTree_Menu.getNodeByParam("id",currentNodeId);
	if(nodeObj){
		curMenu = nodeObj;
		var parentNode = curMenu;
		while(parentNode.getParentNode() != null){
			parentNode = parentNode.getParentNode();
		}
	}
	if(curMenu.click != false){
		zTree_Menu.selectNode(curMenu);
	}else{
		$("#" + nodes_0.tId + "_a").click();
	}
	$("#" + curMenu.tId + "_a").click();
	var a = $("#" + parentNode.tId + "_a");
	a.addClass("cur");
	var par=a.parent();
	par.addClass("cur_parent");
	par.parent().find("> li.level0").each(function(){
		$(this).find("> a.level0").prepend("<div class='slideIcon'></div>");
	});
	//标签拖动
	$("#analyse_tree").mouseover(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		e=e||window.event;
		var tar=$(e.target||e.srcElement);
		var canDrag = 0;
		if(!tar.hasClass("treenode") || tar.hasClass("ui-draggable") || tar.hasClass("level0")) return;
		var tagId = parseInt(tar[0].id.replace(/analyse_tree_/,"").replace(/_a/,""));
		var nodeObj = zTree_Menu.getNodeByTId("analyse_tree_"+tagId);
		if(nodeObj.param.isCanDrag != null || nodeObj.param.isCanDrag != undefined){
			canDrag = nodeObj.param.isCanDrag;
		}
		if(canDrag == 0){
			$("#"+tar[0].id).css({"cursor":"default"});
			$("#"+tar[0].id).find(".button").css({"cursor":"default"});
			return;
		}
		var dropTar=$("#tag_analyse_dropable").parent();
		tar.draggable({ 
			revert: "invalid", 
			helper: "clone" ,
			start: function() {
				dropTar.css({"position":"relative","zindex":"600"});
				var startY=dropTar.offset().top;
				var fix=$(window.parent).scrollTop()-startY-180;
				var posy=$(this).offset().top;
				var margin=posy-startY-dropTar.height()/2;
				fix>0?dropTar.css("margin-top",margin):"";
			},
			stop:function(){
				dropTar.css("margin-top",8)
			}
		});
	});
	
	//$("#xxxxxxxxxxx").draggable({ revert: "invalid", helper: "clone" });
	//释放标签
	dropTag();
	autoHeight();
}
function initLabelTree(actionUrl) {
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.treeList;
			}else{
				alert(result.msg);
			}
		}
	});
	initTree();
}

function initLabelTreeByName(actionUrl,labelName) {
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		data: ({"labelName" : labelName}),
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.treeList;
			}else{
				alert(result.msg);
			}
		}
	});
	initTree();
}
function searchLabelByName() {
	var labelName = $('#searchLabelName').val();
	labelName = $.trim(labelName);
	var labelType = $('#labelType').val();
	var actionUrl = "";
	if('ct' == labelType) {
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTreeByName.ai2do";
	} else if('my' == labelType) {
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeByName.ai2do";
	}
	initLabelTreeByName(actionUrl,labelName);
}
function showLabelTree(who) {
	$('#searchLabelName').val('');
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
	$('#labelType').val(who);
	if('ct' == who) {
		$('#ct_tree').addClass('on');
		$('#my_tree').removeClass('on');
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
	} else if('my' == who) {
		$('#my_tree').addClass('on');
		$('#ct_tree').removeClass('on');
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTree.ai2do";
	}
	initLabelTree(actionUrl);
}
//释放正在拖动的标签
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_analyse_dropable").droppable({
		accept: "#analyse_tree a",
		greedy:true,
		drop: function( event, ui ) {
			$(this).find(".drag_tip").hide();
			var onDragTag=ui.draggable;
			tagOnDrop(onDragTag,$(this));
		}
	});
}
function tagOnDrop(dragtag,ct){
	var _txt=dragtag.find("span").eq(1).text();
	var tagId = parseInt(dragtag[0].id.replace(/analyse_tree_/,""));
	
	var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
	var who = $('#labelType').val();
	if("ct" == who){
		ct_currentNodeId = nodeObj.id;
	}else{
		my_currentNodeId = nodeObj.id;
	}
	var vflag = true;
	$("#baseForm").find("input[name$='.calcuElement']").each(function(){
		if(this.value == nodeObj.id){
			vflag = false;
		}
	});
	if( !vflag ){
		parent.showAlert("不能拖动主标签","failed");
		return;
	}
	if($(".analyse_show").size()>=10){
		parent.showAlert("最多只能与10个标签进行微分","failed");
		return;
	}
	$(".analyse_show").find("input[name='calcuElement']").each(function(){
		if(this.value == nodeObj.id){
			parent.showAlert("已经拖动了该标签","failed");
			vflag = false;
			return;
		}
	});
	if(!vflag ){
		return;
	}
	var html_obj=$(getShowHtmlOfTag(_txt,nodeObj.id,nodeObj.param.minVal,nodeObj.param.maxVal,nodeObj.param.attrVal,nodeObj.param.effectTime,nodeObj.param.failTime));
	ct.append(html_obj);
	//点击时显示已选中图标
	html_obj.click(function(){
		if($(this).hasClass("select")){
			$(this).removeClass("select");
			checkButton(this,false);
		}else{
			$(this).addClass("select");
			checkButton(this,true);
		}
	});
	setTimeout(showCount,200);
	setTimeout(autoHeight,100);
}

function getShowHtmlOfTag(text,id,minVal,maxVal,attrVal,effectTime,failTime){
	var html='<div class="analyse_show">';
	html+='<div class="select_img"><img src="${ctx}/aibi_ci/assets/themes/default/images/select.gif" /></div>';
	html+='<div class="fleft">';
	html+='<div class="conditionCT">';
	html+='<div class="left_bg"><!--<a class="setting" onclick="setThis(this,event)" href="javascript:void(0)"></a>--><a onclick="onlydeleteThis(this)" href="javascript:void(0)"></a></div>';
	html+='<div class="midCT">'+text;
	html+='<input type="hidden" name="calcuElement" value="'+id+'" />';
	html+='<input type="hidden" name="minVal" value="'+minVal+'"/>';
	html+='<input type="hidden" name="maxVal" value="'+maxVal+'"/>';
	html+='<input type="hidden" name="attrVal" value="'+attrVal+'"/>';
	html+='<input type="hidden" name="effectTime" value="'+effectTime+'"/>';
	html+='<input type="hidden" name="failTime" value="'+failTime+'"/>';
	html+='<input type="hidden" name="sortNum" value="0"/>';
	html+='<input type="hidden" name="elementType" value="2"/>';
	html+='<input type="hidden" name="labelFlag" value="1"/>';
	html+='</div>';
	html+='<div class="right_bg"></div>';
	html+='</div>';
	html+='</div>';
	html+='<ul class="fright">';
	html+='<li class="num_ct loading">';
	html+='<font class="green_num bold"><img src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"/></font><p>用户数</p>';
	html+='</li>';
	html+='<li class="num_ct">';
	html+='<font class="blue_num bold"></font><p class="blue_num_p">占比</p>';
	html+='</li>';
	html+='<li class="btn_ct">';
	html+='<a class="btn_save" href="javascript:;" onclick="different('+id+',this)">继续微分</a>';
	html+='</li>';
	html+='</ul>';
	html+='<div class="slidelist">';
	html+='<ul>';
	html+='</ul>';
	html+='</div>';
	html+='</div>';
	return html;
}


function setThis(t,e){
	var e=e||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	var _topct=$(t).parents(".analyse_show");
	_topct.addClass("analyse_show_slided");
	var tar=_topct.find(".slidelist");
	var conditionCT=$(t).parent().parent();
	var posx=conditionCT.position().left+10;
	var posy=conditionCT.position().top+36;
	var _w=conditionCT.width()-2;
	tar.css({"left":posx,"top":posy}).width(_w).slideDown("fast");
}

function showCount(){
	$("#tag_analyse_dropable").find(".fright").each(function(index){
		if($(this).find(".loading").size() == 0 ){
			return ;
		}
		$(this).removeClass("loading");
		var id = $(this).parent().find('input[name="calcuElement"]').val();
		var actionUrl =  "${ctx}/ci/customersManagerAction!countIntersect4Labels.ai2do?1=1";
		var param = getParam($(this).parent());
		var obj = this;
		if($("#tag_analyse_dropable").find(".fright").length == index + 1) {
			$('#isLastLabel').val('1');
		} else {
			$('#isLastLabel').val('0');
		}
		
		$.post(actionUrl, $("#baseForm").serialize()+"&"+param, function(result){
				if(result.success){
					$(obj).find(".green_num").empty().append($.formatNumber(result.count, 3, ','));//客户数
					$(obj).find(".blue_num").empty().append(result.percent+"%");//占比
					$("#console").append("标签微分：(" +result.count+")"+result.sql+"<br/>");
				}else{
					parent.showAlert("标签在当前时间没有数据","failed");
					$(obj).find("font").empty().append();
					$("#console").append("客户群微分：(" +result.msg+")"+ "<br/>");
				}
			}
		);
	});
}

function changeNum(month){
	if(month){
		$("input[name='ciCustomGroupInfo.dataDate']").val(month);
	}
	var actionUrl =  "${ctx}/ci/customersManagerAction!countIntersect4Labels.ai2do?1=1";
	var obj = this;
	$(".analyse_right_dl:first").find(".green_num").empty().append('<img src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"/>');
	$.post(actionUrl, $("#baseForm").serialize(), function(result){
			if(result.success){
				$(".analyse_right_dl:first").find(".green_num").empty().append($.formatNumber(result.count, 3, ','));
				$("#console").append("标签微分：(" +result.count+")"+result.sql+"<br/>");
				
			}else{
				$(".analyse_right_dl:first").find(".green_num").empty().append("");
				parent.showAlert("主标签在当前时间没有数据","failed");
				$("#console").append("标签微分：(" +result.msg+")"+"<br/>");
			}
		}
	);
	$("#tag_analyse_dropable").find(".fright").addClass("loading").find("font").empty().append('<img src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"/>');
	showCount();
}

function different(id,obj){
	//var url = "${ctx }/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?ciLabelInfo.labelId="+id+"&tab=tag";
	//window.open(url,"_blank");
	var divObj = $(obj).parent().parent().parent();
	if(divObj.find(".green_num").text()==''||divObj.find(".green_num").text()=='0'){
		parent.showAlert("已经没有数据了","failed");
		return;
	}
	var txt = $.trim(divObj.find(".midCT").text());
	var _html = '';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].calcuElement" value="'+divObj.find("input[name='calcuElement']").val()+'"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].minVal" value="'+divObj.find("input[name='minVal']").val()+'"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].maxVal" value="'+divObj.find("input[name='maxVal']").val()+'"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].attrVal" value="'+divObj.find("input[name='attrVal']").val()+'"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].sortNum" value="'+sortNum+'"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].elementType" value="2"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].labelFlag" value="1"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].baseParam" value="true"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].text" value="'+txt+'"/>';
	sortNum += 1;
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].calcuElement" value="and"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].minVal" value=""/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].maxVal" value=""/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].sortNum" value="'+sortNum+'"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].elementType" value="1"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].baseParam" value="true"/>';
	_html += '<input type="hidden" name="ciLabelRuleList['+sortNum+'].history" value="'+getHistoryData()+'"/>';
	sortNum += 1;
	$("#baseForm").append(_html);
	changeTitle();
	
	var effectTime = divObj.find("input[name='effectTime']").val();
	var failTime = divObj.find("input[name='failTime']").val();
	
	divObj.parent().empty();
	
	if(effectTime && effectTime != ''&& effectTime > $("input[name='ciCustomGroupInfo.startDateStr']").val()){
		$("input[name='ciCustomGroupInfo.startDateStr']").val(effectTime);
	}
	
	if(failTime && failTime != '' && failTime < $("input[name='ciCustomGroupInfo.endDateStr']").val()){
		$("input[name='ciCustomGroupInfo.startDateStr']").val(failTime);
	}
	$("#backBtn").removeClass("tag_btn_disable");
	changeNum();
	checkButton($(".analyse_show"),false);
}

function getHistoryData(){
	var str = "";
	$("#tag_analyse_dropable").find(".midCT").each(function(){
		str += encodeURI($(this).find("input").serialize());
		str += "&text="+encodeURI($.trim($(this).text()))+",";
	});
	return str;
}

function showHistory(str){
	var parms = str.split(",");
	$("#tag_analyse_dropable").empty();
	for (var i=0;i<parms.length ;i++){
		if(parms[i]==null||$.trim(parms[i]).length==0){
			return;
		}
		var id = getFromUrlParam(parms[i],"calcuElement");
		var text = getFromUrlParam(parms[i],"text");
		var minVal = getFromUrlParam(parms[i],"minVal");
		var maxVal = getFromUrlParam(parms[i],"maxVal");
		var effectTime = getFromUrlParam(parms[i],"effectTime");
		var failTime = getFromUrlParam(parms[i],"failTime");
		var attrVal = getFromUrlParam(parms[i],"attrVal");
		var html_obj=$(getShowHtmlOfTag(text,id,minVal,maxVal,attrVal,effectTime,failTime));
		$("#tag_analyse_dropable").append(html_obj);
		html_obj.click(function(){
			if($(this).hasClass("select")){
				$(this).removeClass("select");
				checkButton(this,false);
			}else{
				$(this).addClass("select");
				checkButton(this,true);
			}
		});
	}
	setTimeout(showCount,200);
	setTimeout(autoHeight,100);
}

function getFromUrlParam(str,paramName){
	if(!str){
		return "";
	}
	var keyValues  = str.split("&");
	var paramValue = "";
	for(var i =0;i<keyValues.length;i++){
		if(keyValues[i].indexOf("=") > 0){
			if(keyValues[i].split("=")[0].toLowerCase() == paramName.toLowerCase()){
				paramValue = keyValues[i].split("=")[1];
				break;
			}
		}
	}
	return decodeURI(paramValue);
}

function changeTitle(){
	var desc = " ";
	var i = 0 ;
	//alert($("#baseForm").find("input[name$='.text']").size());
	$("#baseForm").find("input[name$='.text']").each(function(){
		if(i != 0 ){
			desc +=" 且 "
		}
		desc += this.value;
		i++;
	});
	$(".dateline_userNum").find("img").attr("title",desc);
	
	var title = desc.substr(10);
	$("#showDesc").text(desc).removeAttr("title").attr("title",title);
}
function checkButton(obj,flag){
	if(flag){
		$("#saveBtn").removeClass("tag_btn_disable").removeAttr("title").unbind("click").bind("click",function(){
			if($(obj).parent().parent().find(".select").size() > 0 ){
				openCreate();
			}
		});
	}else{
		if($(obj).parent().parent().find(".select").size() == 0 ){
			$("#saveBtn").addClass("tag_btn_disable").unbind("click").attr("title","请至少选择一个标签，才能保存客户群！");
		}
	}
}

var sortNum = 2;
function getParam(obj){
	var param = "";
	obj.find("input:hidden").each(function(){
		param += "&ciLabelRuleList["+sortNum+"]."+this.name+"="+this.value;
	});
	return param;
}

function getFullParam(){
	var baseParam = $("#baseForm").serialize();
	var param = "";
	var sortNumTemp = sortNum;
	$(".analyse_show").each(function(){
		if($(this).hasClass("select") ){//选中的才序列化
			$(this).find("input:hidden").each(function(){
				var valueStr = this.value;
				if(this.name == "sortNum"){
					valueStr = sortNumTemp;
				}
				param += "&ciLabelRuleList["+sortNumTemp+"]."+this.name+"="+valueStr;
				
			});
			sortNumTemp ++;
		}
	});
	return baseParam + param;
}

function getCustomGroupNames(){
	var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
	var sortNum_=0;
	var param = "";
	$(".analyse_show").each(function(){
		if($(this).hasClass("select") ){//选中的才序列化
			var sortNumTemp = sortNum_ ++;
			param += "&ciCustomGroupInfoList["+sortNumTemp+"].customGroupName="+encodeURIComponent(encodeURIComponent($.trim($(this).find(".midCT").text())));
		}
	});
	param += "&ciCustomGroupInfo.customGroupName="+encodeURIComponent(encodeURIComponent($.trim($("#showDesc").text())));
	param += "&ciCustomGroupInfo.startDateStr="+$("input[name='ciCustomGroupInfo.startDateStr']").val();
	param += "&ciCustomGroupInfo.endDateStr="+$("input[name='ciCustomGroupInfo.endDateStr']").val();
	//alert(param);
	return param ;
}

<!--
//ztree
var curMenu = null, zTree_Menu = null;
var setting = {
	view: {
		showLine: true,
		selectedMulti: false,
		dblClickExpand: false
	},
	data: {
		key: {
			title: "tip"
		},
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: this.beforeClick,
		onClick: this.onClick
	}
};
function beforeClick(treeId, node) {
	if(node.click != false){
		//恢复原有样式
		var icon = $(".curSelectedNode").find(".button");
		if(icon.css("background")){
			var bg = icon.css("background").replace("_on","");
			icon.css("background", bg);
		}
	}
	
	if (node.isParent) {
		if (node.level === 0) {
			var pNode = curMenu;
			while (pNode && pNode.level !==0) {
				pNode = pNode.getParentNode();
			}
			if (pNode !== node) {
				var a = $("#" + pNode.tId + "_a");
				a.removeClass("cur");
				zTree_Menu.expandNode(pNode, false,true,true,true);
			}
			a = $("#" + node.tId + "_a");
			a.addClass("cur");
			var cur_parent=a.parent();
			var open_close=true;
			if(cur_parent.hasClass("cur_parent")){
				cur_parent.removeClass("cur_parent");
				open_close=false;
			}else{
				cur_parent.addClass("cur_parent").siblings(".cur_parent").removeClass("cur_parent");
			}
			var isOpen = false;
			if(node.children){
				for (var i=0,l=node.children.length; i<l; i++) {
					if(node.children[i].open) {
						isOpen = true;
						break;
					}
				}	
			}else{
				isOpen = true;
			}
			if (isOpen) {
				zTree_Menu.expandNode(node, open_close);
				curMenu = node;
			} else {
				zTree_Menu.expandNode(node.children[0].isParent?node.children[0]:node, open_close,true,true,true);
				curMenu = node.children[0];
			}
			
		} else {
			//非叶子节点的点击展开功能屏蔽，否则和点击事件同时调用，展开节点很慢，可以通过点击非叶子节点前的加号展开下一级节点
			//zTree_Menu.expandNode(node,true,true,true,true);
		}
	}
	//如果返回return !node.isParent，那么就会有false的情况，根据ztree的api当返回false时，onClick事件就会失效
	//return true;
	return (node.click != false);
}

//节点点击事件
function onClick(e, treeId, node) {
	if ((node.isParent && node.level !== 0) || !node.isParent) {
		initClick();
	}
}

//初始化节点选中事件
function initClick() {
	var icon = $(".curSelectedNode").find(".button");
	var bg = icon.css("background");
	var last = bg.lastIndexOf("/");
	var right = bg.lastIndexOf(")");
	var begin = bg.substring(0, last+1);
	var middle = bg.substring(last+1, right);
	var iconExt = middle.substring(middle.indexOf("."), middle.length);
	var iconName = middle.substring(0, middle.indexOf(".")) + "_on" + iconExt;
	var end = bg.substring(right, bg.length);
	var newBg = begin + iconName + end;
	icon.css("background", newBg);
}

function onlydeleteThis(obj){
	$(obj).parents(".analyse_show").remove();
	checkButton($(".analyse_show"),false);
}

//保存客户群弹出层
function openCreate(){
	$("#createDialog").dialog("close");
	var ifmUrl = $.ctx + "/ci/customersManagerAction!saveManyCiCustoms.ai2do?"+getCustomGroupNames();
	$("#createFrame").attr("src", ifmUrl).load(function(){
		$("#isAutoMatch").prop("checked",false);
	});
	$("#createDialog").dialog("open");
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#createDialog").dialog("close");
}
//-->
</script>
</head>

<body class="body_bg">
	<input id="labelType" type="hidden" value="ct" />
	<div class="analyse_left">
		<ul class="analyse_tab">
			<li class="on" id="ct_tree" onclick="showLabelTree('ct');">标签库</li>
			<li class="noborder" id="my_tree" onclick="showLabelTree('my');">我的标签库</li>
		</ul>

		<div class="analyse_tab_ct">
			<div class="analyse_date">
				<table class="commonTable">
					<tr>
						<td>
							<div class="analyse_search">
								<input type="text" id="searchLabelName" /> 
								<span class="icon_search" onclick="searchLabelByName();"></span>
							</div>
						</td>
					</tr>
				</table>
			</div>

			<div class="tree_area">
				<ul id="analyse_tree" class="ztree"></ul>
			</div>
			<!--tree_area end -->

		</div>
		<!--analyse_tab_ct end -->
	</div>
	<!--analyse_left end -->

	<div class="analyse_right" style="margin-top: 0px">
		<dl class="analyse_right_dl marginTop_0">
			<dt>
				<span>日期选择</span>
			</dt>
			<dd>
				<table class="commonTable mainTag_show">
					<tr>
						<td rowspan="2"><div id="dateline_ct"></div></td>
						<td width="150" class="dateline_userNum">
						<font class="green_num bold">${ciLabelInfo.ciLabelExtInfo.customNum}</font>
							<p>
								用户数
								<%--                    			<img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" title="${ciLabelInfo.labelName}"/> --%>
							</p>
							<form id="baseForm">
								<input type="hidden" id="isLastLabel" name="isLastLabel" value="0" />
								<input type="hidden" name="ciCustomGroupInfo.dataDate" value="" />
								<input type="hidden" name="ciCustomGroupInfo.startDateStr" value="<fmt:formatDate value="${ciLabelInfo.effecTime }" pattern="yyyy-MM-dd"/>" />
								<input type="hidden" name="ciCustomGroupInfo.endDateStr" value="<fmt:formatDate value="${ciLabelInfo.failTime }" pattern="yyyy-MM-dd"/>" />
								
								<input type="hidden" name="ciLabelRuleList[0].calcuElement" value="${ciLabelInfo.labelId }" /> 
								<input type="hidden" name="ciLabelRuleList[0].minVal" value="${ciLabelInfo.ciLabelExtInfo.minVal}" />
								<input type="hidden" name="ciLabelRuleList[0].maxVal" value="${ciLabelInfo.ciLabelExtInfo.maxVal}" />
								<input type="hidden" name="ciLabelRuleList[0].attrVal" value="${ciLabelInfo.ciLabelExtInfo.attrVal}" />
								<input type="hidden" name="ciLabelRuleList[0].sortNum" value="0" />
								<input type="hidden" name="ciLabelRuleList[0].elementType" value="2" />
								<input type="hidden" name="ciLabelRuleList[0].labelFlag" value="1" />
								<input type="hidden" name="ciLabelRuleList[0].baseParam" value="true" />
								<input type="hidden" name="ciLabelRuleList[0].text" value="${fn:trim(ciLabelInfo.labelName)}" />
								
								<input type="hidden" name="ciLabelRuleList[1].calcuElement" value="and" /> 
								<input type="hidden" name="ciLabelRuleList[1].minVal" value="" />
								<input type="hidden" name="ciLabelRuleList[1].maxVal" value="" />
								<input type="hidden" name="ciLabelRuleList[1].sortNum" value="1" />
								<input type="hidden" name="ciLabelRuleList[1].elementType" value="1" />
								<input type="hidden" name="ciLabelRuleList[1].baseParam" value="true" />
							</form>
						</td>
					</tr>
				</table>
			</dd>
		</dl>

		<dl class="analyse_right_dl">
			<dt>
				<label style="width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; word-break: keep-all;">与主标签(<font style="font-weight: bold;">${ciLabelInfo.labelName}</font>)关联分析展示</label>&nbsp;<label id="showDesc" style="width: 900px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; word-break: keep-all;">${ciLabelInfo.labelName}</label>
			</dt>
			<dd id="tag_analyse_dropable" class="tag_analyse_ct tag_dropable">
				<div class="drag_tip"></div>
			</dd>
		</dl>

		<div class="analyse_right_btns">
			<input type="button" class="tag_btn tag_btn_disable" id="saveBtn" value="保存为客户群" />
			<input type="button" class="tag_btn tag_btn_disable" id="backBtn" value="返回上一步" />
		</div>
	</div>
	<!--analyse_right end -->
	<div id="createDialog" style="display: none;">
		<iframe id="createFrame" name="createFrame" scrolling="yes" allowtransparency="true" src="" framespacing="0" border="0"	frameborder="0" style="width: 100%; height: 415px"></iframe>
		<br />
		<div class="dialog_btn_div" style="position: relative;">
			<%
				if ("true".equals(marketingMenu)) {
			%>
			<input id="isAutoMatch" type="checkbox" class="valign_middle" name="ciCustomGroupInfo.productAutoMacthFlag" value="1" />
			系统自动匹配产品&nbsp;&nbsp;
			<%
				}
			%>
			<input id="saveCustomersBtn" name="" type="button" value="保存" class="tag_btn" />
		</div>
	</div>
	<div id="successDialog" style="display: none;">
		<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width: 100%; height: 265px"></iframe>
	</div>
	<script type="text/javascript">
	$(function(){
		//调用iframe中的内容
		$("#saveCustomersBtn").click(function(){
			createFrame.submitList();
		});
		$("#isAutoMatch").click(function(e){
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			if(this.checked){
				createFrame.setProductAutoMacthFlag(1);
			}else{
				createFrame.setProductAutoMacthFlag(0);
			}
		});
		
	});
	</script>
</body>
</html>
