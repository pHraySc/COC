<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设置规则</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/js/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script type="text/javascript">
var zNodes;
var newDay = "${dateFmt:dateFormat(newLabelDay)}";
$(document).ready(function(){
	//时间轴
	var month = "${newLabelMonth}";
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
	
	if(newDay == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestDay.ai2do",
			type: "POST",
			success: function(result){
				newDay = result.substring(0,4) + "-" + result.substring(4,6) + "-" + result.substring(6,8);
			}
		});
	}
	
	showLabelTree("ct");//左侧标签树
	
	<c:if test='${ciTemplateInfo.templateId == null || ciTemplateInfo.templateId == ""}'>
	var title = "创建模板";
	</c:if>
	<c:if test='${ciTemplateInfo.templateId != null && ciTemplateInfo.templateId != ""}'>
	var title = "编辑模板";
	</c:if>
	
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
	
	$("#editDialog").dialog({
		autoOpen: false,
		width: 660,
		title:title,
		modal: true,
		resizable:false
	});
	
	$("#successDialog").dialog({
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	$("#confirmDialog").dialog({
		autoOpen: false,
		width: 500,
		height:185,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	var len = $("#tag_dropable > .conditionCT").size();
	if(len > 0){
		$("#submitBtn").removeClass("tag_btn_dis");
		$("#submitBtn").bind("click", function(){
			toSave();
		});
		$("#clearBtn").removeClass("tag_btn_dis");
		$("#clearBtn").bind("click", function(){
			confirmClearAll();
		});
	}
});
//保存模板
function toSave(){
	createForm();
	if(validateSql()){
		openEdit();
	}
}
//打开编辑对话框
function openEdit(){
	$("#editDialog").dialog({
		close: function( event, ui ) {
			$("#editFrame").attr("src", "");
		}
	});
	$("#editDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/template/editTemplate.jsp";
	$("#editFrame").attr("src", ifmUrl).load(function(){resetForm();});
	$("#editDialog").dialog("open");
	
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
function resetForm(){
	var ifm = $("#editFrame").contents();
	//编辑时填充表单
	var templateId = $("#templateId").val();
	if(templateId != null && templateId != ""){
		ifm.find("#template_id").val(templateId);
		ifm.find("#template_name").val($("#templateName").val());
		ifm.find("#template_desc").val($("#templateDesc").val());
		ifm.find("#template_status").val($("#status").val());
		ifm.find("#template_userId").val($("#userId").val());
		ifm.find("#template_addTime").val($("#addTime").val());
		ifm.find("#template_sceneId").val($("#sceneId").val());
		ifm.find("#template_isPrivate").val($("#isPrivate").val());
	}
	ifm.find("#template_labelOptRuleShow").val($("#labelOptRuleShow").val());
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#editDialog").dialog("close");
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
		clearAll();	
	}
}

//清空数据探索条件
function clearCondition(){
	$("#parenthesis").removeClass("opened");
	$("#inputList").empty();
	$("#tag_dropable").empty();
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
			elementType = 2;
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
			//var name=cname.replace(/[\r\n]/g,"");
			//var str=name.replace("\t","ccc");
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
	/* 弱化提示
	var len = $("#tag_dropable > .conditionCT").size();
	if(len == 0){
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
		data: $("#labelForm").serialize(), 
		success: function(result){
			flag = result.success;
			if(!flag){
				showAlert(result.msg,"failed");
			}
		}
	});
	return flag;
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
			//$("#content").append(result.labelName);
		}
	});

	initTree();
}

function initTree() {
	//初始化第一级node的样式
	$.fn.zTree.init($("#analyse_tree"), setting, zNodes);
	zTree_Menu = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodes_0=zTree_Menu.getNodes()[0];
	curMenu = nodes_0;
	if(!curMenu){
		return;
	}
	while(curMenu.children&&curMenu.children.length > 0){
		curMenu = curMenu.children[0];
	}
	if(curMenu.click != false){
		zTree_Menu.selectNode(curMenu);
	}else{
		$("#" + nodes_0.tId + "_a").click();
	}
	$("#" + curMenu.tId + "_a").click();
	var a = $("#" + nodes_0.tId + "_a");
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
		var canDrag = 0;
		var tar=$(e.target||e.srcElement);
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
		var dropTar=$("#tag_dropable").parent();
		tar.draggable({ 
			revert: "invalid", 
			helper: "clone" ,
			start: function() {
				var startY=dropTar.offset().top;
				var fix=$(window.parent).scrollTop()-startY;
				var posy=$(this).offset().top;
				var margin=posy-startY-dropTar.height()/2;
				fix>0?dropTar.css("margin-top",margin):"";
			},
			stop:function(){
				dropTar.css("margin-top",8)
			}
		});
	})
	
	//释放标签
	dropTag();
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
			//$("#content").append(result.labelName);
		}
	});
	
	initTree();
}
//释放正在拖动的标签
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_dropable").droppable({
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
	var tagId = parseInt(dragtag[0].id.replace(/analyse_tree_/,""));
	var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
	if($(".conditionCT").length>=$._maxLabelNum){
		showAlert("最多拖动" + $._maxLabelNum + "个标签！","failed");
		return false;
	}
	//生成运算符
	var _chaining='<div class="chaining">';
	_chaining+='<div class="left_bg"></div>';
	_chaining+='<div class="midCT">';
	_chaining+='<span onmousedown="switchConnector_turn(this,event)">且</span><a onclick="switchConnector(this,event)" href="javascript:void(0)" class="icon_rightArr">&nbsp;</a>';
	_chaining+='</div>';
	_chaining+='<div class="right_bg"></div>';
	_chaining+='</div>';
	//标签内容
	var _txt=$.trim(nodeObj.name);
	var html='<div class="conditionCT';
	var type = nodeObj.param.labelTypeId;
	var choiceDate = $(".date_line").attr("_value");
	var effectDate = nodeObj.param.effectDate;
	if(choiceDate < effectDate){
		html+=' outOfDate';
	}
	if(newDay != null && nodeObj.param.updateCycle == 1){
		html+='" title="数据日期：' + newDay;
	}
	html+='" element="' + nodeObj.id + '" effectDate="' + effectDate + '" ';
	html+='min="0" max="1" ';
	if(type == 2){//得分类标签
		html+='minVal="'+ nodeObj.param.minVal + '" maxVal="'+ nodeObj.param.maxVal + '" ';
	}
	if(type == 3){//属性类标签
		html+='attrVal="' + nodeObj.param.attrVal + '" ';
	}
	html+='type="'+ nodeObj.param.labelTypeId + '" flag="1">';
	html+='<div class="left_bg">';
	html+='<a href="javascript:void(0)" onclick="deleteThis(this)"></a>';
	html+='</div><div class="midCT">'+_txt+'</div>';
	html+='<div class="right_bg"></div>';
	html+='</div>';
	
	var htmlobj=$(html);
	/* htmlobj.click(function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		var labelFlag = $(this).attr("flag");
		if($(this).hasClass("conditionCT_active")){
			$(this).removeClass("conditionCT_active");
			if(labelFlag > 1){
				$(this).attr("flag", 3);
			}else{
				$(this).attr("flag", 1);
			}
		}else if($(this).hasClass("conditionCT")){
			$(this).addClass("conditionCT_active");
			if(labelFlag > 1){
				$(this).attr("flag", 2);
			}else{
				$(this).attr("flag", 0);
			}
		}
	}); */
	if(ct.find(".conditionCT").length==0){
		ct.append(htmlobj);
	}else{
		ct.append(_chaining).append(htmlobj);
	}
	
	var len = $("#tag_dropable > .conditionCT").size();
	if(len > 0){
		$("#submitBtn").removeClass("tag_btn_dis");
		$("#submitBtn").unbind("click");
		$("#submitBtn").bind("click", function(){
			toSave();
		});
		$("#clearBtn").removeClass("tag_btn_dis");
		$("#clearBtn").unbind("click");
		$("#clearBtn").bind("click", function(){
			confirmClearAll();
		});
	}
}
function checkDate(choiceDate){
	//模板保存不需要校验日期，直接使用最新月
	/* $(".conditionCT").each(function(){
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
	}); */
}
//删除条件 同时删除关联的连接符和括号
function deleteThis(obj){
	//删除匹配的括号【与条件直接关联的括号】
	deleteCurlyBraces(obj);
	//删除关联的连接符
	deleteConnectFlags(obj);
	//删除自身
	$(obj).parent().parent().remove();
	
	var len = $("#tag_dropable > .conditionCT").size();
	if(len == 0){
		$("#submitBtn").addClass("tag_btn_dis");
		$("#submitBtn").unbind("click");
		$("#clearBtn").addClass("tag_btn_dis");
		$("#clearBtn").unbind("click");
	}
}

//checkbox单选
function controlAll(obj,text){
	if(obj.checked){
		$(obj).parent().parent().find("input:checked").not(obj).attr("checked",false);
		$("#tag_dropable").find(".chaining").find("span").text(text);
	}
}

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
//点击节点前判断是否第一级,不同展示方式
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

//系统标签和我的标签切换显示
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

//通过名称模糊查询标签树数据
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

//确认清空
function confirmClearAll(){
	openConfirm("确定要清空吗？", "");
}

//清空所有标签
function clearAll(){
	clearCondition();
	$("#submitBtn").addClass("tag_btn_dis");
	$("#submitBtn").unbind("click");
	$("#clearBtn").addClass("tag_btn_dis");
	$("#clearBtn").unbind("click");
}

</script>
</head>
<body class="body_bg">
<div class="top_menu analyse_top_menu">
    <ul>
        <li class="top_menu_on"><a href="#" class="no2"><span></span><p>设置规则</p></a></li>
        <div class="clear"></div>
    </ul>
</div>
<input id="labelType" type="hidden" value="ct"/>
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
                            <input type="text" id="searchLabelName"/>
                            <span class="icon_search" onclick="searchLabelByName();"></span>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        
        <div class="tree_area">
        	<ul id="analyse_tree" class="ztree"></ul>
        </div><!--tree_area end -->
        
    </div><!--analyse_tab_ct end -->
</div><!--analyse_left end -->

<div class="analyse_right" style="margin-top:0px">
	<dl class="analyse_right_dl marginTop_0" style="display:none;">
    	<dt><span>日期选择</span></dt>
        <dd>
        	<table class="commonTable mainTag_show">
            	<tr>
                	<td id="dateline_ct">
                    </td>
                </tr>
            </table>
        </dd>
    </dl>
    
    <dl class="analyse_right_dl">
    	<dt>
    		<ul class="controlAll">
				<li><input type="checkbox" onclick='controlAll(this,"且")'/>全部(且)</li>
	            <li><input type="checkbox" onclick='controlAll(this,"或")'/>全部(或)</li>
	            <li><input type="checkbox" onclick='controlAll(this,"剔除")'/>全部(剔除)</li>
	        </ul>
            <div id="parenthesis" class="parenthesis">
                <div class="leftParenthesis"></div>
                <div class="rightParenthesis"></div>
            </div>
            <span>标签及规则设置</span>
        </dt>
        <dd id="tag_dropable" class="tag_analyse_ct tag_dropable">
            <c:if test='${ciTemplateInfo.templateId != null && ciTemplateInfo.templateId != ""}'>
            <div class="drag_tip" style="display:none;"></div>
				<c:forEach var="ciLabelRule" items="${ciLabelRuleList}" varStatus="status">
				<c:choose>
					<c:when test='${ciLabelRule.elementType == 2}'>
						<div class="conditionCT 
							<c:if test="${ciLabelRule.labelFlag eq 0 || ciLabelRule.labelFlag eq 2}"> conditionCT_active</c:if>" 
							flag="${ciLabelRule.labelFlag}" type="${ciLabelRule.labelTypeId}" 
							<c:if test="${ciLabelRule.labelTypeId eq 2}"> maxVal="${ciLabelRule.maxVal}" minVal="${ciLabelRule.minVal}"</c:if>
							<c:if test="${ciLabelRule.labelTypeId ne 2}"> max="${ciLabelRule.maxVal}" min="${ciLabelRule.minVal}"</c:if> 
							element="${ciLabelRule.calcuElement}" attrVal="${ciLabelRule.attrVal}" effectDate="${ciLabelRule.effectDate}">
							
							<div class="left_bg">
							<a onclick="deleteThis(this)" href="javascript:void(0)"/></a>
							</div>
							<div class="midCT">${ciLabelRule.labelName}</div>
							<div class="right_bg"/></div>
						</div>
					</c:when>
					
					<c:when test='${ciLabelRule.elementType == 1}'>
						<div class="chaining">
							<div class="left_bg"></div>
							<div class="midCT">
								<c:choose>
									<c:when test='${ciLabelRule.calcuElement == "and"}'>
										<span onmousedown="switchConnector_turn(this,event)">且</span>
									</c:when>
									<c:when test='${ciLabelRule.calcuElement == "or"}'>
										<span onmousedown="switchConnector_turn(this,event)">或</span>
									</c:when>
									<c:when test='${ciLabelRule.calcuElement == "-"}'>
										<span onmousedown="switchConnector_turn(this,event)">剔除</span>
									</c:when>
									</c:choose>
									
									<a onclick="switchConnector(this,event)" href="javascript:void(0)" class="icon_rightArr">&nbsp;</a>
							</div>
							<div class="right_bg"></div>
						</div>
					</c:when>
					
					<c:when test='${ciLabelRule.elementType == 3}'>
						<script type="text/javascript">
							var _parenthesis = getCurlyBraceHTML('${ciLabelRule.calcuElement}',1,null,true);
							$("#tag_dropable").append(_parenthesis);
							$("#tag_dropable").find("script").remove();
						</script>
					</c:when>
				</c:choose>
				</c:forEach>
			</c:if>
			<c:if test='${ciTemplateInfo.templateId == null || ciTemplateInfo.templateId == ""}'>
			<div class="drag_tip"></div>
			</c:if>
        </dd>
    </dl>
    
    <div class="analyse_right_btns">
    	<input id="clearBtn" type="button" class="tag_btn tag_btn_dis" value="清空" />
    	<input id="submitBtn" type="button" class="tag_btn tag_btn_dis" value="保存" />
        <!-- <input type="button" class="tag_btn tag_btn_disable" value="取消" onclick="window.close();"/> -->
    </div>
</div><!--analyse_right end -->

<!-- 页面传值 -->
<input id="templateId" type="hidden" name="ciTemplateInfo.templateId" value="${ciTemplateInfo.templateId}" />
<input id="templateName" type="hidden" name="ciTemplateInfo.templateName" value="${ciTemplateInfo.templateName}" />
<input id="templateDesc" type="hidden" name="ciTemplateInfo.templateDesc" value="${ciTemplateInfo.templateDesc}" />
<input id="status" type="hidden" name="ciTemplateInfo.status" value="${ciTemplateInfo.status}" />
<input id="userId" type="hidden" name="ciTemplateInfo.userId" value="${ciTemplateInfo.userId}" />
<input id="addTime" type="hidden" name="ciTemplateInfo.addTime" value="<fmt:formatDate value="${ciTemplateInfo.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
<input id="sceneId" type="hidden" name="ciTemplateInfo.sceneId" value="${ciTemplateInfo.sceneId}" />
<input id="isPrivate" type="hidden" name="ciTemplateInfo.isPrivate" value="${ciTemplateInfo.isPrivate}" />
<input id="labelOptRuleShow" type="hidden" name="ciTemplateInfo.labelOptRuleShow" value="" />

<!-- form -->
<form id="labelForm" method="post">
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>

	<div id="inputList" style="display:none;"></div>
</form>
<div id="editDialog">
	<iframe id="editFrame" name="editFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:325px"></iframe>
</div>
<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:185px"></iframe>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
</body>
</html>

