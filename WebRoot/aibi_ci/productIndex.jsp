<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>觅产品</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<script type="text/javascript">
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
	
	//左侧导航及首页标签搜索tab页选中样式
	$(".bg03").addClass("active");
	$("#_index").addClass("menu_on");
	
	//点击标签取反
	$("#tag_dropable").click(function(e){
		e=e||window.event;
		var tar=$(e.target||e.srcElement);
		if(!tar.hasClass("midCT")) return;
		var _p=tar.parent();
		if(_p.hasClass("conditionCT_long")) return;
		if(_p.hasClass("conditionCT_active")){
			_p.removeClass("conditionCT_active");
			_p.attr("flag", 1);
		}else if(tar.parent().hasClass("conditionCT")){
			_p.addClass("conditionCT_active");
			_p.attr("flag", 0);
		}
	});

	
	
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

	$(".searchSubmit").bind("click", function(){
		//本来在标签地图时的查询和标签列表时的查询处理逻辑应该有区别，但是为了简单，都统一load一下就完了。
		$("#mainContent").load("${ctx}/aibi_ci/productList.jsp",function (){loadLabelList();});
	});
	
	$("#exploreBtn").bind("click",function(){
		toExplore();
	});
	
	$("#saveCustomerBtn").bind("click", function(){
		toSave();
	});

	//随着输入自动去查询名称相似的产品名称，并展示出来
	$("#keyword").autocomplete({
		source: function( request, response ) {
			$.ajax({
				url: "<%=request.getContextPath() %>/ci/ciIndexAction!indexQueryProductName.ai2do",
				dataType: "jsonp",
				type: "POST",
				data: {
					keyWord: request.term,
					classId:$("#classId").val()
				},
				success: function( data ) {
				response(
					$.map( data.geonames, function( item ) {
						return {
							label: item.productName,
							value: item.productName
						}
					}));
				}
			});
		},
		minLength: 1,
		autoFocus:true,
		select: function( event, ui ) {
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			$( ".ui-autocomplete" ).css("z-index",999);
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
	$("#mainContent").load("${ctx}/aibi_ci/productMap.jsp");
	
});

function loadMap(){
	$("#mainContent").load("${ctx}/aibi_ci/productMap.jsp");
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
	var actionUrl = $.ctx + "/ci/customersManagerAction!findCustomerNumForExplore.ai2do";
	$.post(actionUrl, $("#exploreForm").serialize(), function(result){
		if (result.success) {
			var num = "";
			if((result.msg != null && result.msg != "") || result.msg == 0){
				num = $.formatNumber(result.msg, 3, ',');
			}
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
	});
}
//整合条件，拼接表单
function createForm(){
	$("#dataDate").val($(".date_line").attr("_value"));
	
	$("#inputList").empty();
	var prodOptRule = "";
	var html = "";
	$("#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis").each(function(i, element){
		var ele = $(element);
		var calcuElement = "", minVal="", maxVal="", elementType="", labelFlag="", nameStr="";
		if(ele.hasClass("conditionCT")){//处理标签
			calcuElement = ele.attr("element");
			minVal = ele.attr("min");
			maxVal = ele.attr("max");
			elementType = ele.attr("elementType");
			labelFlag = ele.attr("flag");
			nameStr = ele.children(".midCT").text();
			if(labelFlag != null && labelFlag != ""){
				if(labelFlag == 0){
					nameStr = "非 " + nameStr + " ";
				}else if(labelFlag == 1){
					nameStr = nameStr + " ";
				}
			}else{
				nameStr = minVal + " <= " + nameStr + " <= " + maxVal + " ";
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
		prodOptRule += nameStr;
		html += '<input type="hidden" name="ciLabelRuleList[' + i + '].calcuElement" value="' + calcuElement + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].minVal" value="' + minVal + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].maxVal" value="' + maxVal + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].sortNum" value="' + (i+1) + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].elementType" value="' + elementType + '"/>';
		if(elementType == 4){
			html += '<input type="hidden" name="ciLabelRuleList[' + i + '].labelFlag" value="' + labelFlag + '"/>';
		}
	});
	$("#prodOptRuleShow").val(prodOptRule);
	$("#inputList").append(html);
}
//SQL验证
function validateSql(){
	var flag = true;
	/* 提示弱化
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml == null || innerHtml ==""){
		showAlert("请拖拽至少一个产品！","failed");
		return false;
	} */
	if($(".waitClose").length>0){
		showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
		return false;
	}
	if($(".outOfDate").length>0){
		showAlert("有红框的产品，当前选择的月份数据未生成！","failed");
		return false;
	}
	if($(".conditionCT").length>$._maxLabelNum){
		showAlert("最多拖动" + $._maxLabelNum + "个产品！","failed");
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



//释放正在拖动的产品
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_dropable").droppable({
		accept: ".tag_List_ul li,.canDrag",
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
	//产品内容
	var _txt=dragtag.attr("labelName");
	var html='<div class="conditionCT';
	var choiceDate = $(".date_line").attr("_value");
	var effectDate = dragtag.attr("effectDate");
	if(choiceDate < effectDate){
		html+=' outOfDate';
	}
	html+='" element="' + dragtag.attr("element") + '" effectDate="' + dragtag.attr("effectDate") + '" ';
	html+='min="'+ dragtag.attr("min") + '" max="'+ dragtag.attr("max") + '" ';
	html+='type="'+ dragtag.attr("type") + '" elementType="' + dragtag.attr("elementType") + '" flag="1">';
	html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this)"></a></div>';
	html+='<div class="midCT">'+_txt+'</div>';
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


//产品详情
function showTip(e,ths){
	var e=e||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$("#tag_tips .onshow").hide().removeClass("onshow");
	var _p=$(ths);
	if(_p.hasClass("tagChoosed")){
		_p.removeClass("tagChoosed");
		return;
	}else{
		$(".tagChoosed").removeClass("tagChoosed");
	}
	_p.addClass("tagChoosed");
	var posy=_p.offset().top+_p.height();
	var posx=_p.parents("dd").offset().left-1;
	//减去左侧菜单的宽度
	var iconx=_p.offset().left+_p.find("a").width()/2-8-$(".menulist").width();
	var tipCT=$("#tag_tips");
	tipCT.css({"top":posy,"left":posx});
	var html = '<div class="tips">' + 
				'<div class="icon"></div>' + 
					'<div class="ct">' + 
						'<table class="commonTable">' + 
							'<tr>' + 
								'<th>产品名称：</th>' +
								'<td>' + $(ths).attr("labelName") + '</td>' +
								'<th></th>' +
								'<td>' + '</td>' +
							'</tr>' +
						'</table>' +
						'<div class="info_panel">' +
							'<ul>' +
								'<li class="icon07" element="' + $(ths).attr("element") + 
								'" labelName="'+ $(ths).attr("labelName") +
								'" min="' + $(ths).attr("min") + 
								'" max="' + $(ths).attr("max") + 
								'" elementType="' + $(ths).attr("elementType") + 
								'" effectDate="' + $(ths).attr("effectDate") +
								'" type="' + $(ths).attr("type") + '" onclick="addTag(this);"><a href="javascript:void(0)">增加</a></li>' +
							'</ul>' +
						'</div>' +
					'</div>' +
				'</div>';
	var _tip=$(html);
	$("#tag_tips .group").empty().append(_tip);
	_tip.find(".icon").css("margin-left",iconx+"px");
	_tip.addClass("onshow").slideDown("fast");
	_tip.click(function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
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
<style type="text/css">
body,html{height:100%;}
</style>
</head>

<body class="body_bg">
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
<div class="mainCT">

<!-- search -->
<jsp:include page="/aibi_ci/index_top.jsp" ></jsp:include>

<div id="mainContent">
	
</div>

</div><!-- mainCT end -->
    <div class="clear"></div>
</div><!-- sync_height end -->

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
</div><!--tag_operation end -->


<!-- form -->
<form id="exploreForm">
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>
	<!-- <input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value=""/> -->
	<input id="prodOptRuleShow" type="hidden" name="ciCustomGroupInfo.prodOptRuleShow" value=""/>
	<%-- <input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" value="${ciCustomGroupInfo.customOptRuleShow}"/>
	<input type="hidden" name="ciCustomGroupInfo.kpiDiffRule" value="${ciCustomGroupInfo.kpiDiffRule}"/> --%>
	<input type="hidden" name="ciCustomGroupInfo.createTypeId" value="8"/>
	<input type="hidden" name="exploreFromModuleFlag" value="2"/>
	<input type="hidden" name="saveCustomersFlag" value="3"/>
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
</body>
</html>
