<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<title>标签分析</title>
<script type="text/javascript">
	$(document).ready(function(){
		
		$("#_label").addClass("menu_on");
		$("#labelAnalysisTitle").addClass("top_menu_on");
		$("#approveLabelTitle").removeClass("top_menu_on");
		$("#createdLabelTitle").removeClass("top_menu_on");
		
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
		
		showLabelTree("ct");//左侧标签树
		
		aibiTableChangeColor(".showTable");
		
		$("#createDialog").dialog({
			autoOpen: false,
			width: 660,
			title: "保存客户群",
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
		
		var selectLabelId = '${selectLabelId}';
		if(selectLabelId=='' || selectLabelId==null){
			var ifmUrl = $.ctx + "/aibi_ci/ciLabelAnalysis/labelAnalysisWelcome.jsp";
			$("#analysisFrame").attr("src", ifmUrl).load(function(){
				var mainheight = $(this).contents().find("body").height();
				$(this).height(mainheight);
				$(this).width("100%");
			});
		}
		
		var mainheight = $("body").height();
		$("#analysisFrame").height(mainheight);
		$("#analysisFrame").width("100%");
		
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
		
		$("#exploreBtn").bind("click",function(){
			toExplore();
		});
		
		$("#saveCustomerBtn").bind("click", function(){
			toSave();
		});
		//网页置顶
		window.scrollTo(0,1);
	});

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
					showAlert(result.msg,"failed");
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
		
		var nodes_0;
		// 通过参数传递的需要被选择的节点标签ID
		var selectLabelId = '${selectLabelId}';
		if(selectLabelId=='' || selectLabelId==null){
			nodes_0=zTree_Menu.getNodes()[0];
			curMenu = nodes_0;
			var parentNode = nodes_0;
			if(!curMenu){
				return;
			}
			while(curMenu.children&&curMenu.children.length > 0){
				curMenu = curMenu.children[0];
			}
		}else{
			nodes_0=zTree_Menu.getNodeByParam("id",selectLabelId, null);
			curMenu = nodes_0;
		}
		
		if(curMenu.click != false){
			zTree_Menu.selectNode(curMenu);
		}else{
			$("#" + nodes_0.tId + "_a").click();
		}
		
		if(selectLabelId=='' || selectLabelId==null){
			initClick();
		}else{
			$("#" + curMenu.tId + "_a").click();
		}
		var a = $("#" + nodes_0.tId + "_a");
		a.parents("li.level0").addClass("cur_parent").find("> a.treenode").addClass("cur");
		var par=a.parents(".ztree");
		par.find("> li.level0").each(function(){
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
			tar.draggable({ revert: "invalid", helper: "clone" });
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
					showAlert(result.msg,"failed");
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
		if($(".conditionCT").length>=$._maxLabelNum){
			showAlert("最多拖动" + $._maxLabelNum + "个标签！","failed");
			return false;
		}
		var tagId = parseInt(dragtag[0].getAttribute("id").replace(/analyse_tree_/,""));
		var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
		var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
		
		//生成运算符
		var _chaining='<div class="chaining">';
		_chaining+='<div class="left_bg"></div>';
		_chaining+='<div class="midCT">';
		_chaining+='<span onmousedown="switchConnector_turn(this,event)">且</span><a onclick="switchConnector(this,event)" href="javascript:void(0)" class="icon_rightArr">&nbsp;</a>';
		_chaining+='</div>';
		_chaining+='<div class="right_bg"></div>';
		_chaining+='</div>';
		//标签内容
		var _txt=nodeObj.name;
		var html='<div class="conditionCT';
		var type = nodeObj.param.labelTypeId;
		var choiceDate = $(".date_line").attr("_value");
		var effectDate = nodeObj.param.effectDate;
		if(choiceDate < effectDate){
			html+=' outOfDate';
		}
		var newDay = "${dateFmt:dateFormat(newLabelDay)}";
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
	//通过“添加”按钮添加标签到浮动层
	function addLabel(id){
		addLabelToDropPanel($("#analysisFrame").contents().find("#"+id), $("#tag_dropable"));
	}
	//拼接条件显示区
	function addLabelToDropPanel(dragtag,ct){
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
		var _txt=dragtag.attr("name");
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
		html+='type="'+ dragtag.attr("type") + '" flag="1">';
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
		
		var innerHtml = $("#tag_dropable").html();
		if(innerHtml != null && innerHtml !=""){
			$("#exploreBtn").show();
			$("#saveCustomerBtn").show();
			$("#exploreBtnDis").hide();
			$("#saveCustomerBtnDis").hide();
		}
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
			//选中样式处理
			initClick();
			var canDrag = 0;
			if(node.param.isCanDrag != null || node.param.isCanDrag != undefined){
				canDrag = node.param.isCanDrag;
			}
			if(canDrag == 0){
				return;
			}
			var ifmUrl = $.ctx + "/ci/ciLabelAnalysisAction!initLabelAnalysis.ai2do?labelId=" + node.id + "&fromPageFlag=${fromPageFlag }";
			$("#analysisFrame").attr("src", ifmUrl).load(function(){
				var mainheight = $(this).contents().find("body").height();
				$(this).height(mainheight);
				$(this).width("100%");
			});
			if(!$(this).hasClass("level0")){
				$(document).scrollTop(120);
			}
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
	
	//保存客户群
	function toSave(){
		createForm();
		if(validateSql()){
			openCreate();
		}
	}
	//保存客户群弹出层
	function openCreate(){
		$("#createDialog").dialog("close");
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
			ifm.find("#template_desc").val($("#templateDesc").val());
			ifm.find("#template_status").val($("#status").val());
			ifm.find("#template_userId").val($("#userId").val());
			ifm.find("#template_addTime").val($("#addTime").val());
		}
		ifm.find("#template_labelOptRuleShow").val($("#labelOptRuleShow").val());
		*/
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
				data:'labelId=' + single_ele.attr("element") + '&dataDate=' + $("#dataDate").val() + '&exploreFromModuleFlag=3',
				success: function(result){
					if (result.success) {
						var num = $.formatNumber(result.retCustomNum, 3, ',');
						$("#customerNum").empty().append("用户数&nbsp;<p>" + num + "</p>");
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
			showAlert("请拖拽至少一个标签！");
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
	
	//点击探索按钮时候显示遮罩
	function show_overlay(){
		$("body > .tag_operation .tag_operation_overlay").show();
		$("#tag_dropable").droppable("disable");
	}
	function hide_overlay(){
		$("body > .tag_operation .tag_operation_overlay").hide();
		$("#tag_dropable").droppable("enable");
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
	
	//标签使用日志分页查询
	function showLabelUsedLog(labelId){
		//标签审批弹出层
		$( "#dialog_useLog" ).dialog({
		  	autoOpen: false,
		  	height:430,
		  	width: 830,
		  	position: [300,100],
		  	title:"标签使用日志列表",
		  	modal: true,
			resizable:false
	    });
		//var labelId = $("#labelId").val();
		var ifmUrl = "${ctx}/ci/ciLabelInfoAction!useTimesDetail.ai2do?enterType=labelAnalysis&labelId="+labelId;
		$("#dialog_useLog" ).dialog("close" );
		$("#dialog_useLogFrame").attr("src", ifmUrl);
	    $("#dialog_useLog" ).dialog("open" );
	}

	//显示标签审批窗口
	function showLabelProcess(labelId) {
		$( "#dialog_look" ).dialog({
		  	autoOpen: false,
		  	height:430,
		  	width: 730,
		  	title:"标签审批",
		  	modal: true,
			resizable:false
	    });
		
		//var labelId  = $("#labelId").val();
	    var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/showLabel.jsp?labelId="+labelId+"&showWhichContent=labelFlow";
		$("#dialog_look" ).dialog("close" );
	    $("#dialog_detail").attr("src", ifmUrl);
	    $("#dialog_look" ).dialog("open" );
    }
</script>
</head>

<body>
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp"/>

<div class="mainCT">
<jsp:include page="/aibi_ci/ciLabelInfo/ciLabelInfoMain.jsp"/>
<input id="labelType" type="hidden" value="ct"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td style="padding-top:2px;" width="227px" class="l_d v_top">
		<!--左侧menu -->
		<div class="analyse_left" style="margin-left: 0px; margin-top: 0px; height:auto;">
			<ul class="analyse_tab">
		    	<li class="on" id="ct_tree" onclick="showLabelTree('ct');">标签库</li>
		        <li class="noborder" id="my_tree" onclick="showLabelTree('my');">我的标签库</li>
		    </ul>
		    
		    <div class="analyse_tab_ct" id="analyse_tab_ct">
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
		        
		        <div id="tree_area" class="tree_area">
		        	<ul id="analyse_tree" class="ztree"></ul>
		        </div><!--tree_area end -->
		        
		    </div><!--analyse_tab_ct end -->
		   	<div style="height:70px"></div> 
		</div><!--analyse_left end -->
	</td>
    <td class="l_d v_top">
    	<iframe id="analysisFrame" name="analysisFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0"></iframe>
	</td>
  </tr>
</table>
</div><!-- mainCT end -->
    <div class="clear"></div>
</div><!-- sync_height end -->
<!-- 底部浮动层 -->
<div class="tag_operation tag_operation_hided">
	<div class="tag_operation_inner">
		<div class="tag_operation_overlay">
        	<div class="loadding"></div>
        </div>
        <div class="tag_dropCT">
            <div id="dateline_ct"></div>
            <div id="tag_dropable" class="tag_dropable">
            </div><!--#tag_dropable end -->
        </div><!--.tag_dropCT end -->
        <ul class="btns">
            <li class="customerNum" id="customerNum"><p></p></li>
            <li id="exploreBtn" style="display:none;"><a href="javascript:void(0)" class="btn_explore btn_explore">探索</a></li>
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
			<li><input type="checkbox" onclick='controlAll(this,"和")'/>全部(和)</li>
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
</div><!--tag_operation end -->

<!-- form -->
<form id="exploreForm">
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>
	<input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value=""/>
	<%-- <input type="hidden" name="ciCustomGroupInfo.prodOptRuleShow" value="${ciCustomGroupInfo.prodOptRuleShow}"/>
	<input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" value="${ciCustomGroupInfo.customOptRuleShow}"/>
	<input type="hidden" name="ciCustomGroupInfo.kpiDiffRule" value="${ciCustomGroupInfo.kpiDiffRule}"/> --%>
	<input type="hidden" name="ciCustomGroupInfo.createTypeId" value="1"/>
	<input type="hidden" name="exploreFromModuleFlag" value="3"/>
	<input type="hidden" name="saveCustomersFlag" value="4"/>
	<div id="inputList" style="display:none;"></div>
</form>
<div id="createDialog">
	<iframe id="createFrame" name="createFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:300px"></iframe>
</div>
<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:365px"></iframe>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>

<!-- 为了让标签分析页面的查看日志和查看审批流程信息的弹出框的遮罩覆盖住整个的页面，需要把这两个dialog放到这个大页面中 -->
<!-- 标签审批查询 ,默认不显示-->
<div id="dialog_look" style="display:none;">
	<iframe name="dialog_detail" scrolling="no" allowtransparency="true" src="" id="dialog_detail"  framespacing="0" border="0" frameborder="0" style="width:100%;height:100%"></iframe>
</div>

<!-- 标签使用日志查询 ,默认不显示-->
<div id="dialog_useLog" style="display:none;">
	<iframe name="dialog_useLogFrame" scrolling="no" allowtransparency="true" src="" id="dialog_useLogFrame"  framespacing="0" border="0" frameborder="0" style="width:100%;height:100%"></iframe>
</div>
</body>
</html>
