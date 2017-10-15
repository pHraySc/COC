<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" />
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<%
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
%>
<title>客户群分析</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#_customer").addClass("menu_on");
		$(".no4").parent("li").addClass("top_menu_on");
		$(".no2").parent("li").removeClass("top_menu_on");
		$(".no9").parent("li").removeClass("top_menu_on");
		showLabelTree();

		var defaultCustomGroupId = decodeURIComponent('${param["customGroup.customGroupId"]}');
		if(defaultCustomGroupId==null||defaultCustomGroupId==""){
			var ifmUrl = $.ctx + "/aibi_ci/customersAnalysis/customersAnalysisWelcome.jsp";
			$("#analysisFrame").attr("src", ifmUrl).load(function(){
				var mainheight = $(this).contents().find("body").height();
				$(this).height(mainheight);
				$(this).width("100%");
			});
		}
		var mainheight = $("body").height();
		$("#analysisFrame").height(mainheight);
		$("#analysisFrame").width("100%");
		checkCanExplor();
		
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
		
		//网页置顶
		window.scrollTo(0,1);

		 $("#sysMatch").click(function(e){
			 sysMatch();
		 });
	});
	function changeNum(month){
		$("input[name='ciCustomGroupInfo.dataDate']").val(month);
		$("#tag_dropable").find(".midCT").each(function(){
			var data = $(this).attr("data");
			if(data && data.length > 0){
				var jsondata =$.parseJSON(decodeURIComponent(data));
				if(jsondata.updateCycle == 1){//非周期
				}else if(jsondata.updateCycle == 2){//月周期
					var hasList  = false;
					for(var i=0;i< jsondata.successList.length;i++){
						var listinfo  = jsondata.successList[i];
						if(listinfo.dataDate == month){
							hasList = true;
							break;
						}
					}
					if(!hasList){
						$(this).parent().addClass("outOfDate").removeAttr("title").attr("title","周期性客户群在当前日期没有数据,系统将使用有数据的最新日期");
					}else{
						$(this).parent().removeClass("outOfDate").removeAttr("title");
					}
				}
			}
		});
	}
	//客户群和我的客户群切换显示
	function showLabelTree() {
		$('#ct_tree').addClass('on');
		$('#my_tree').removeClass('on');
		var actionUrl = $.ctx + "/ci/customersManagerAction!findCustomGroupTree.ai2do";
		initLabelTree(actionUrl);
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
					showAlert(result.msg,"failed");
				}
			}
		});
		initTree();
	}
	
    function initTree() {
		//初始化第一级node的样式
		$.fn.zTree.init($("#analyse_tree"), setting, zNodes);
		zTree_Menu = $.fn.zTree.getZTreeObj("analyse_tree");
		var nodes_0=zTree_Menu.getNodes()[0];
		//aj 接收初始参数
		var defaultCustomGroupId = decodeURIComponent('${param["customGroup.customGroupId"]}');
		if(defaultCustomGroupId!=null&&defaultCustomGroupId!=""){
			nodes_0=zTree_Menu.getNodeByParam("id",defaultCustomGroupId);
		}
		curMenu = nodes_0;
		if(!curMenu){
			return;
		}
		while(curMenu.children&&curMenu.children.length > 0){
			curMenu = curMenu.children[0];
		}
		zTree_Menu.selectNode(curMenu);
		if(defaultCustomGroupId!=null&&defaultCustomGroupId!=""){
			$("#" + curMenu.tId + "_a").click();
		}else{
			initClick();
		}
		var a = $("#" + nodes_0.tId + "_a");
		var par=a.parents(".level0");
		a.addClass("cur");
		par.addClass("cur_parent");
		par.parent().find("> li.level0").each(function(){
			$(this).find("> a.level0").prepend("<div class='slideIcon'></div>");
		});
		$("#analyse_tree").mouseover(function(e){
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			e=e||window.event;
			var tar=$(e.target||e.srcElement);
			if(tar.hasClass("treenode") && !tar.hasClass("ui-draggable") && tar.find("> .ico_docu").length>0){
				tar.draggable({ revert: "invalid", helper: "clone" });
			}else return;
		});
		dropTag();
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
		//恢复原有样式
		var icon = $(".curSelectedNode").find(".button");
		if(icon.css("background")){
			var bg = icon.css("background").replace("_on","");
			icon.css("background", bg);
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
				
				var cur_parent=a.parent();
				a.addClass("cur");
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
		return true;
	}

	function onClick(e, treeId, node) {
		if ((node.isParent && node.level !== 0) || !node.isParent) {
		var ifmUrl = $.ctx + "/ci/customersAnalysisAction!customersAnalysis.ai2do?customGroup.customGroupId=" + node.id;
		$("#analysisFrame").attr("src", ifmUrl).load(function(){
			var mainheight = $(this).contents().find("body").height();
			$(this).height(mainheight);
			$(this).width("100%");
		});
		if(!$(this).hasClass("level0")){
			$(document).scrollTop(120);
		}
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
	
	//通过名称模糊查询标签树数据
	function searchCustomByName() {
		var customGroupName = $('#searchCustomName').val();
		customGroupName = $.trim(customGroupName);
		var actionUrl = $.ctx + "/ci/customersManagerAction!findCustomGroupTree.ai2do";
		/* if('ct' == labelType) {
			actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTreeByName.ai2do";
		} else if('my' == labelType) {
			actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeByName.ai2do";
		} */
		initCustomGroupTreeByName(actionUrl,customGroupName);
	}
	
	function initCustomGroupTreeByName(actionUrl,customGroupName) {
		$.ajax({
			type: "POST",
			url: actionUrl,
			async: false,
			data: ({"customGroupName" : customGroupName}),
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
				var tagId = parseInt(onDragTag[0].id.replace(/analyse_tree_/,""));
				
				var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
				var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
				tagOnDrop($(this),$("#tag_dropable"),null,nodeObj.param);
			}
		});
	}
	//打开保存成功弹出层
	function openSuccess(customerName, templateName){
		customerName = encodeURIComponent(encodeURIComponent(customerName));
		templateName = encodeURIComponent(encodeURIComponent(templateName));
		$("#successDialog").dialog("close");
		var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
		$("#successFrame").attr("src", ifmUrl).load(function(){});
		$("#successDialog").dialog("open");
	}
	function tagOnDrop(dragtag,ct,dataDate,jsonStr){
		if($(".tag_operation_overlay:visible").size() > 0){
			return;
		}
		if($(".conditionCT").length>=$._maxLabelNum){
			showAlert("最多拖动" + $._maxLabelNum + "个客户群！","failed");
			return false;
		}
		var jsondata;
		if(jsonStr){
			jsondata =$.parseJSON(decodeURIComponent(jsonStr));
		}else{
			jsondata =$.parseJSON(decodeURIComponent(dragtag.attr("data")));
		}
		if(jsondata.successList.length ==0 ){
			showAlert("该客户群没有可用的清单","failed");
			return;
		}
		var data = dragtag.attr("data")||jsonStr;
		$("input[name='ciCustomGroupInfo.dataDate']").val(jsondata.dataDate);
		var calcuElement ;
		var dataStr = "数据日期：";
		if(dataDate){
			for(var i=0;i< jsondata.successList.length;i++){
				var listinfo  = jsondata.successList[i];
				if(listinfo.dataDate == dataDate){
					calcuElement = listinfo.listTableName;
					if(dataDate.length == 6){
						dataDate = dataDate.substring(0,4)+"-"+dataDate.substring(4,6);
					}
					dataStr += dataDate;
					break;
				}
			}
		}else{
			calcuElement = jsondata.customGroupId;
			dataDate = jsondata.dataDate;
			if(dataDate.length == 6){
				dataDate = dataDate.substring(0,4)+"-"+dataDate.substring(4,6);
			}
			dataStr += dataDate;
		}
		if(!calcuElement){
			showAlert("该客户群没有可用的清单"+dataDate,"failed");
			return;
		}
		if(jsondata.updateCycle == 1){
			dataStr = "";
		}
		var _chaining='<div class="chaining">';
		_chaining+='<div class="left_bg"></div>';
		_chaining+='<div class="midCT">';
		_chaining+='<input type=hidden name="calcuElement" value="and" />';
		//kongly
//		_chaining+='<input type=hidden name="elementType" value="0" />';
		_chaining+='<input type=hidden name="elementType" value="1" />';
		_chaining+='<span onmousedown="switchConnector_turn(this,event)">和</span><a onclick="switchConnector(this,event)" href="javascript:void(0)" class="icon_rightArr">&nbsp;</a>';
		_chaining+='</div>';
		_chaining+='<div class="right_bg"></div>';
		_chaining+='</div>';
		var html='<div class="conditionCT">';
		html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this)"></a></div>';
		html+='<div class="midCT" data="'+dragtag.attr("data")+'" title="'+dataStr+'">'+jsondata.customGroupName+'';
		html+='<input type=hidden name="calcuElement" value="'+calcuElement+'" />';
		//kongly
//		html+='<input type=hidden name="elementType" value="1" />';
		html+='<input type=hidden name="elementType" value="5" />';
		html+='</div><div class="right_bg"></div>';
		html+='</div>';
		if(ct.find(".conditionCT").length==0){
			ct.append(html);
		}else{
			ct.append(_chaining+html);
		}
		
		checkCanExplor();
	}
	//删除条件 同时删除关联的连接符和括号
	function deleteThis(obj){
		//删除匹配的括号【与条件直接关联的括号】
		deleteCurlyBraces(obj);
		//删除关联的连接符
		deleteConnectFlags(obj);
		//删除自身
		$(obj).parent().parent().remove();
		checkCanExplor();
	}
	function checkCanExplor(){
		if($("#tag_dropable").find(".midCT").size()>0){
			$(".btn_explore").removeClass("btn_explore_dis").unbind("click").bind("click",function(){
				queryCount(this);
			}).removeAttr("title");
			$(".btn_save").removeClass("btn_save_dis").unbind("click").bind("click",function(){
				if($(".waitClose").length>0){
					showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
					return;
				}
				if(validateSql()){
					$( "#dialog-form" ).dialog( "open" );
				}
			}).removeAttr("title");
		}else{
			$(".customerNum").hide().find("p").empty();
			$(".btn_explore").addClass("btn_explore_dis").unbind("click").attr("title","请先添加客户群！");
			$(".btn_save").addClass("btn_save_dis").unbind("click").attr("title","请先添加客户群！");
		}
	}
	
	//SQL验证 kongly 2014-5-1
	function validateSql(){
		var flag = true;
		var actionUrl = $.ctx + "/ci/customersManagerAction!validateCreateCustomGroupSql.ai2do";
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
	
	function addToPanel(obj,dataDate,jsonStr){
		tagOnDrop($(obj),$("#tag_dropable"),dataDate,jsonStr);
	}
	function queryCount(obj){
		var data = getParams();
		data += "&ciCustomGroupInfo.dataDate=" + $("input[name='ciCustomGroupInfo.dataDate']").val();
		showCount(data);
	}
	function controlAll(obj,text){
		if(obj.checked){
			$(obj).parent().parent().find("input:checked").not(obj).attr("checked",false);
			$("#tag_dropable").find(".chaining").find("span").text(text);
		}
	}
	function getParams(){
		var data = "";
		var sortNum = 0; 
		var dataCN = "";
		$("#tag_dropable").find(".chaining").find("span").each(function(){
			if($(this).text() == "且"){
				$(this).parent().find('input[name="calcuElement"]').val("and");
			}
			if($(this).text() == "或"){
				$(this).parent().find('input[name="calcuElement"]').val("or");
			}
			if($(this).text() == "剔除"){
				$(this).parent().find('input[name="calcuElement"]').val("-");
			}
		});
		$("#tag_dropable").find(".parenthesis,.midCT").each(function(){
			var calcuElement ;
			var elementType;
			if ($(this).hasClass("midCT")) {
				dataCN += $.trim($(this).text())+" ";
			}
			$(this).find("input:hidden").each(function(){
				if(this.name=='calcuElement'){
					calcuElement = this.value;
				}
				if(this.name=='elementType'){
					elementType = this.value;
				}
				if(this.value =='(' || this.value==')'){
					calcuElement = this.value;
					//kongly
//					elementType = 0;
					elementType = 3;
					dataCN += this.value+" ";
				}
			});
			//kongly
//			data += "&ciCustomSourceRelList["+sortNum+"].calcuElement="+calcuElement;
//			data += "&ciCustomSourceRelList["+sortNum+"].elementType="+elementType;
//			data += "&ciCustomSourceRelList["+sortNum+"].sortNum="+sortNum;
			
			data += "&ciLabelRuleList["+sortNum+"].calcuElement="+calcuElement;
			data += "&ciLabelRuleList["+sortNum+"].elementType="+elementType;
			data += "&ciLabelRuleList["+sortNum+"].sortNum="+sortNum;
			sortNum++;
		});
		$("input[name='ciCustomGroupInfo.customOptRuleShow']").val(dataCN);
		
		//给data后面拼一个标签的cilabelrule测试混合运算
		/*
		data += "&ciLabelRuleList["+sortNum+"].calcuElement=and";
		data += "&ciLabelRuleList["+sortNum+"].elementType=1";
		data += "&ciLabelRuleList["+sortNum+"].sortNum="+(sortNum);
		
		data += "&ciLabelRuleList["+(sortNum+1)+"].calcuElement=93";
		data += "&ciLabelRuleList["+(sortNum+1)+"].elementType=2";
		data += "&ciLabelRuleList["+(sortNum+1)+"].sortNum="+(sortNum+1);
		data += "&ciLabelRuleList["+(sortNum+1)+"].minVal=0";
		data += "&ciLabelRuleList["+(sortNum+1)+"].maxVal=1";
		data += "&ciLabelRuleList["+(sortNum+1)+"].labelFlag=1";
		*/
		
		
		return data;
	}
	var showCount = function(data){
		if(data.length==0){
			return;
		}
		if($(".waitClose").length>0){
			showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
			return false;
		}
		$(".btn_explore").addClass("btn_explore_dis");
		$(".customerNum").find("p").empty().append("请稍候…");
		var actionUrl = "<%=request.getContextPath() %>/ci/customersManagerAction!countOfCustomerCalc.ai2do";
		show_overlay();
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: data,
			success: function(result){
				if(result.success){
					$(".customerNum").show().empty().append("用户数<p>"+$.formatNumber(result.count, 3, ',')+"</p>");
					$("#console").append("客户群运算：" +result.msg+"<br/>");
				}else{
					showAlert(result.msg,"failed");
					$("#console").append("客户群运算：" +result.msg+"<br/>");
				}
				hide_overlay();
				$(".btn_explore").removeClass("btn_explore_dis");
			}
		});
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

	//产品手工匹配
	function customMatch(){
		var listTableName = $("#matchListTableName").val();
		var customGroupId = $("#matchCustomGroupId").val();
		var customNum = $("#matchCustomNum").val();
		var param = "customGroup.listTableName="+listTableName;
		param = param + "&customGroup.customGroupId="+customGroupId;
		param = param + "&customGroup.customNum="+customNum;
		param = param + "&tab=match";
		var actionUrl = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
		window.open(actionUrl, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		$( "#dialog-match" ).dialog( "close" );
	}
	//产品系统匹配
	function sysMatch(){
		var listTableName = $("#matchListTableName").val();
		var customGroupId = $("#matchCustomGroupId").val();
		var data = "customGroup.listTableName="+listTableName;
		data = data + "&customGroup.customGroupId="+customGroupId;
		var actionUrl = $.ctx+"/ci/marketingStrategyAction!sysMatch.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: data,
			success: function(result){
				if(result.success){
					$( "#sysMatchTitle" ).html("");
					$( "#sysMatchTitle" ).html("已经开始进行系统匹配");
					$("#sysMatch").addClass("tag_btn_disable").unbind("click").removeAttr("title").attr("title","已经进行系统匹配！");
				}else{
					$( "#sysMatchTitle" ).hide();
					$( "#failSysMatchTitle" ).show();
				}
			}
		});
	}

	//判断客户群是否进行过系统自动匹配
	function customSysMatch(customGroupId,dataDate,listTableName,customNum){
		if(customNum == ""){
			showAlert("客户群清单未创建成功","failed");
			return;
		}
		var actionUrl = $.ctx + "/ci/marketingStrategyAction!isCustomProductMatch.ai2do?customGroup.listTableName="+listTableName+"&customGroup.customGroupId="+customGroupId;
		$.ajax({
			type: "POST",
			url: actionUrl,
			success: function(result){
				if(result.success){
					var param = "customGroup.listTableName="+listTableName;
					param = param + "&customGroup.customGroupId="+customGroupId;
					param = param + "&customGroup.customNum="+customNum;
					param = param + "&dataDate="+dataDate;
					param = param + "&tab=match";
					var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
					window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
					$( "#dialog-match" ).dialog( "close" );
				}else{
					if(result.matchIng){
						$( "#sysMatchTitle" ).html("");
						$( "#sysMatchTitle" ).html("已经开始进行系统匹配");
						$("#sysMatch").addClass("tag_btn_disable").unbind("click").removeAttr("title").attr("title","已经进行系统匹配！");
					}
					$("#matchCustomGroupId").val(customGroupId);
					$("#matchListTableName").val(listTableName);
					$("#matchCustomNum").val(customNum);
					$( "#dialog-match" ).dialog( "open" );
				}
			}
		});
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
</script>
</head>
<body>
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp"/>

<div class="mainCT">
<jsp:include page="/aibi_ci/customers/customerMain.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td style="padding-top:2px;" width="227px" class="l_d v_top">
		<!--左侧menu -->
		<div class="analyse_left" style="margin-left: 0px; margin-top: 0px; height:auto;">
			<ul class="analyse_tab">
		    	<li style="border-right: 0px" >我的客户群</li>
		    </ul>
		    
		    <div class="analyse_tab_ct" id="analyse_tab_ct">
		        <div class="analyse_date">
		            <table class="commonTable">
		                <tr>
		                    <td>
		                        <div class="analyse_search">
		                            <input type="text" id="searchCustomName"/>
		                            <span class="icon_search" onclick="searchCustomByName();"></span>
		                        </div>
		                    </td>
		                </tr>
		            </table>
		        </div>
		        
		        <div id="tree_area" class="tree_area">
		        	<ul id="analyse_tree" class="ztree"></ul>
		        </div><!--tree_area end -->
		        
		    </div><!--analyse_tab_ct end -->
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

<div class="tag_operation tag_operation_hided">
	<div class="tag_operation_inner">
		<div class="tag_operation_overlay">
        	<div class="loadding"></div>
        </div>
		<div class="tag_dropCT">
			<div id="tag_dropable" class="tag_dropable">
				 
			</div><!--#tag_dropable end -->
		</div><!--.tag_dropCT end -->
		<ul class="btns">
			<li class="customerNum" style="display:;"><p></p></li>
			<li><a href="javascript:void(0)" class="btn_explore">探索</a></li>
			<li><a href="javascript:void(0)" class="btn_save" >保存客户群</a></li>
			<li><span class="icon_expand" onclick="expandTagperation(this)">展开</span></li>
		</ul>
		<div class="icon_sjts"><span style="font-size: 10px">客户群运算</span></div>
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
		
	</div>
</div><!--tag_operation end -->
<div id="dialog-form" title="保存客户群" style="display:none;"><!-- dialog-form -->
	 <form id="saveForm">
			<input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" id="ciCustomGroupInfo.customOptRuleShow" value=""/>
			<!-- //TODO -->
			<input type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>
			<!-- //TODO -->
			<input type="hidden" name="ciCustomGroupInfo.productAutoMacthFlag" value=""/>
			<div class="dialog_table">
				<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
					<tr>
						<th width="110px"><span>*</span>客户群名称：</th>
						<td>
							<div class="new_tag_selectBox">
								<input name="ciCustomGroupInfo.customGroupName" id="ciCustomGroupInfo.customGroupName" type="text" value="请输入客户群名称" 
										maxlength="35" class="new_tag_input" style="width:302px;*width:306px;cursor:text"/>
								<input style="display:none" /><!-- 防止回车提交 -->
							</div>
							<!-- 提示 -->
							<div class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
						</td>
					</tr>
					<tr>
						<th width="110px"><span>*</span>客户群分类：</th>
						<td>
							<div class="new_tag_selectBox">
								<base:DCselect selectId="secenId" selectName="ciCustomGroupInfo.sceneId" 
									tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
						        	nullName="请选择" nullValue="" selectValue="${ciCustomGroupInfo.sceneId}"/>
						    </div> 
							<!-- 提示 -->
							<div id="snameTip" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
						</td>
					</tr>
					<tr>
						<th>客户群描述：</th>
						<td>
							<div>
								<textarea id="custom_desc" style="overflow:hidden;width:400px"  onblur="shortThis(this);" onkeyup="shortThis(this);" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea"  cols="30"></textarea>
							</div>
							<!-- 提示 -->
						</td>
					</tr>
				</table>
			</div>
			<!-- 按钮区 -->
			<div class="dialog_btn_div" style="position:relative;">
			<%if("true".equals(marketingMenu)){ %>
				<input type="checkbox" id="isAutoMatch" class="valign_middle"/> 系统自动匹配产品&nbsp;&nbsp;
			<%} %>
				<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
			</div>
	 </form>
</div><!-- dialog-form end-->
<div id="successDialog" style="display:none;">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<div id="dialog-match" title="系统自动匹配提示" style="display:none;"><!-- "dialog-match" -->
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" colspan="2" style="padding-top:0px;padding-bottom:20px;">
				<div id="sysMatchTitle" class="no no_nobg">您未进行系统自动匹配！</div>
				<div id="failSysMatchTitle" class="no" style="display: none">系统自动匹配失败！</div>
			</td>
		</tr>
		<tr class="even">
			<td class="save_td save_text_td" style="text-align:left">
				<li>可选择”跳过“，直接进行手工匹配！</li>
				<li>可选择”系统匹配”，您的策略匹配中会出现<br/>
				如右图“系统推荐”的TAB页哦！</li>
			</td>
			<td width="283px" class="save_text_td" style="text-align:left">
				<img src="<%=request.getContextPath()%>/aibi_ci/assets/themes/default/images/img4.png" width="263" height="124" />
			</td>
		<tr class="even">
			<td class="save_td" colspan="2">
				<div class="show" >
					系统匹配功能，可能需要您等待几分钟哦！<br/>
					数据处理完毕后，会在<span>系统通知消息</span>中提示您！感谢您的使用！
				</div>
			</td>
		</tr>
		<input type="hidden" id="matchCustomGroupId" />
		<input type="hidden" id="matchListTableName" />
		<input type="hidden" id="matchCustomNum" />
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input name="" type="button" id="sysMatch"  value="系统自动匹配" class="tag_btn"/>
<input name="" type="button" id="customMacth" onclick="customMatch();" value=" 跳 过 " class="tag_btn"/>
</div>		                
</div><!-- "dialog-match" end-->
<script type="text/javascript">
//保存表单
$(function(){
	//保存客户群的表单
	$("#dialog-form").dialog({
		autoOpen: false,
		width: 550,
		resizable:false,
		modal: true,
		close: function() {
			//清空表单
			$("#saveForm").find("input[type=text],textarea").each(function(){
				$(this).val("");
				$("div[for='"+this.name+"']").remove();
			});
			$(".tishi").not(".error").show();
			$("#isAutoMatch").prop("checked",false);
		}
	});

	//系统自动匹配提示
	$("#dialog-match").dialog({
		autoOpen: false,
		width: 550,
		resizable:false,
		modal: true,
		close: function() {
			$( "#sysMatchTitle" ).html("");
			$( "#sysMatchTitle" ).html("您未进行系统自动匹配！");
			$("#sysMatch").removeClass("tag_btn_disable").removeAttr("title").unbind("click").bind("click",function(){
				sysMatch();
			});
		}
	});
	
	$("#saveBtn").bind("click", function(){
		submitList();
	});
	$("#cancelBtn").bind("click", function(){
		$( "#dialog-form" ).dialog( "close" );
	});
	$.focusblur($("input[name='ciCustomGroupInfo.customGroupName']:last"));
	
	$("#isAutoMatch").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		if(this.checked){
			$("input[name='ciCustomGroupInfo.productAutoMacthFlag']").val(1);
		}else{
			$("input[name='ciCustomGroupInfo.productAutoMacthFlag']").val(0);
		}
	});
});
function shortThis(obj){
	var oldValue = $(obj).val();
	if(oldValue.length > 170 ){
		oldValue = oldValue.substr(0,170);
		$(obj).val(oldValue);
	}
}
$.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});
};

function submitList(){
	var nameObj = $("input[name='ciCustomGroupInfo.customGroupName']:last");
	if(nameObj.val()=="请输入客户群名称"){
		nameObj.val("");
	}
	if(validator.form()){
//kongly
//		var actionUrl = $.ctx + "/ci/customersManagerAction!saveCustomerByCustomerCalc.ai2do";
		var actionUrl = $.ctx + "/ci/customersManagerAction!saveCustomerByCustomerCalcOnly.ai2do";
		var data = getParams();
		if(data.length == 0 ){
			showAlert("请先拖拽客户群","failed");
			return;
		}
		data += "&"+$("#saveForm").serialize();
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: data,
			success: function(result){
				if(result.success){
					openSuccess(nameObj.val(),"");
					$( "#dialog-form" ).dialog( "close" );
				}else{
					showAlert(result.msg,"failed");
				}
			}
		});
	}else{
		validator.focusInvalid();
	}
}
//清空数据探索条件
function clearCondition(){
	$("#parenthesis").removeClass("opened");
	$("#tag_dropable").empty();
	$(".customerNum").hide().empty();
	checkCanExplor();
}
var validator = $("#saveForm").validate({
	rules: {
		"ciCustomGroupInfo.customGroupName": {
			required: true,
			remote:{//验证用户名是否存在
					type:"POST",
					url:$.ctx + "/ci/customersManagerAction!isNameExist.ai2do", //
					data:{
						"ciCustomGroupInfo.customGroupName":function(){return $("#saveForm").find("input[name='ciCustomGroupInfo.customGroupName']").val();}
					}
				}
		}
	}, 
	messages: {
		"ciCustomGroupInfo.customGroupName": {
			required:"请输入客户群名称",
			remote:jQuery.format("名称已经存在")
		}
	},
	errorPlacement:function(error,element) {
		element.parent().parent().append(error);
	},
	success:function(element){
		element.parent().parent().find("div[class='tishi error']").remove();
	},
	errorElement: "div",
	errorClass: "tishi error"
}); 
</script>
</body>
</html>