﻿<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
<title>标签维护-标签配置</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/shopCart.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<style type="text/css">
	html,body{height:100%;}
</style>
<script type="text/javascript">
var	validater;
var zTree;
var zNodes;
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
		callback : {
			onClick: treeOnClick
		}
};
var _sortNum = 0;
$(document).ready(function(){
	jQuery.validator.addMethod("dependIndex", function(value, element) {
		var r =/^[^\u4e00-\u9fa5]+$/; 
		return r.test($("#dependIndex").val())  
	 }, "只能包括英文字母、数字、下划线、逗号");   
	jQuery.validator.addMethod("countRules", function(value, element) {       
		var r =/^[^\u4e00-\u9fa5]+$/; 
		return r.test($("#countRules").val())  
	 }, "只能包括英文字母、数字、下划线、逗号");   
	validater = $("#saveForm").validate({
		rules: {
			"configLabelInfo.dependIndex": {
				required:function(){
					return $("#dimLabelType option:selected").val() != '8';
				},
				dependIndex:function(){
					return $("#dimLabelType option:selected").val() != '8';
				}
				/*required:true,
				dependIndex:true*/
			},
			"configLabelInfo.countRules": {
				required:function(){
				    return $("#dimLabelType option:selected").val() != '8'&&$("#dimLabelType option:selected").val() != '5';
			    }
			},
			"configLabelInfo.parentName": {
				required: true
			},
			"configLabelInfo.tableId": {
				required: true
			},
			"configLabelInfo.dimTransId":{
				required:function(){
					return $("#dimLabelType option:selected").val() == '3';
				}
			},
			"configLabelInfo.dataType": {
				required:function(){
					return $("#dimLabelType option:selected").val() != '8';
				}
			},
			"failTimeShow": {
				required: true
			},
			"effecTimeShow": {
				required: true
			}
		},
		messages: {
			"configLabelInfo.dependIndex": {
				required: "规则依赖的指标不允许为空!",
				dependIndex: "规则依赖的指标不能为汉字!"
			},
			"configLabelInfo.countRules": {
				required: "具体规则不能为空!"
			},
			"configLabelInfo.parentName": {
				required: "标签分类不能为空!"
			},
			"configLabelInfo.tableId": {
				required: "标签宽表不能为空!"
			},
			"configLabelInfo.dimTransId":{
				required:"标签维表不允许为空！"
			},
			"configLabelInfo.dataType":{
				required:"数据类型不允许为空！"
			},
			"failTimeShow": {
				required: "标签失效日期不允许为空！"
			},
			"effecTimeShow": {
				required: "标签生效日期不允许为空！"
			}
			
		},
		errorPlacement:function(error,element) {
			element.parents("li").append(error);
		},
		success:function(element){
			element.remove();
		},
		errorElement: "div",
		errorClass: "tishi-page error"
	});

	$("#newTable").hide();
	$("#tableTypeBox div").bind("click", function() {
			var _this = $(this);
			var _value = _this.attr("value");
			var _id = _this.attr("id");
			$('#tableTypeId').val(_value);
			$('#tableTypeBox div').removeClass();
			_this.addClass('current');
			if(_id == "exist"){
				$("#newTable").hide();
				$("#existTable").show();
			}else{
				$("#newTable").show();
				$("#existTable").hide();
			}
	});
	$("div[id^='isNeedAuthorityBox'] div").bind("click", function() {
		var _this = $(this);
		var _value = _this.attr("value");
		_this.parent().children("input").val(_value);
		_this.parent().children("div").removeClass();
		_this.addClass('current');
	});

	if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_ENUM%>"){
		$("#period").show();
		$("#isNeedAuthorityBoxDiv").show();
		var _typeValue = $("#dimTransId option:selected").attr("_typeValue").toLowerCase();
		if(_typeValue.indexOf("char")>=0){
			$("#dataType").attr("dataType","char");
		}
	}else {
		$("#period").hide();
		$("#isNeedAuthorityBoxDiv").hide();
		$("#dataType").removeAttr("dataType");
	}
	if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_VERT%>"){
		$("#vertDiv").show();
	}else{
		$("#vertDiv").hide();
	}
	if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_ENUM%>" || $("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_VERT%>"){
		$("#countRulesDiv").hide();
	}else{
		$("#countRulesDiv").show();
	}
	if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_VERT%>"){
		$("#dependIndexDiv").hide();
		$("#countRulesDescDiv").hide();
		$("#dataTypeDiv").hide();
	}else{
		$("#dependIndexDiv").show();
		$("#countRulesDescDiv").show();
		$("#dataTypeDiv").show();
	}

	//只有枚举型、组合型标签没有具体指标规则
	$("#dimLabelType").live("change", function(){
		var labelTypeVert = "<%=ServiceConstants.LABEL_TYPE_VERT%>";
		var labelTypeEnum = "<%=ServiceConstants.LABEL_TYPE_ENUM%>";
		var labelTypeSign = "<%=ServiceConstants.LABEL_TYPE_SIGN%>";
		var _typeId = $(this).children("option:selected").val();
		$("#dataType").removeAttr("dataType");
		//枚举标签，选择维表
		if(labelTypeEnum == _typeId) {
			$("#period").show();
			$("#isNeedAuthorityBoxDiv").show();
			$("#vertDiv").hide();
			
			$("#countRulesDiv").hide();
			$("#dependIndexDiv").show();
			$("#countRulesDescDiv").show();
			$("#dataTypeDiv").show();
			var _typeValue = $("#dimTransId option:selected").attr("_typeValue").toLowerCase();
			if(_typeValue.indexOf("char")>=0){
				$("#dataType").attr("dataType","char");
			}
		}else if(labelTypeVert == _typeId) {
			$("#period").hide();
			$("#isNeedAuthorityBoxDiv").hide();
			$("#vertDiv").show();
			$("#countRulesDiv").hide();
			$("#dependIndexDiv").hide();
			$("#countRulesDescDiv").hide();
			$("#dataTypeDiv").hide();
			$("#dataType").removeAttr("dataType");
		}else {
			$("#period").hide();
			$("#isNeedAuthorityBoxDiv").hide();
			$("#vertDiv").hide();
			$("#countRulesDiv").show();
			$("#dependIndexDiv").show();
			$("#countRulesDescDiv").show();
			$("#dataTypeDiv").show();
			$("#dataType").removeAttr("dataType");
		}
		changeSysTable();
	});
	//列名不允许为空
	$("input[id^='columnName_']").live("keyup",function(){
		$(this).parent().next("div").hide();
	});
	//列中文名不允许为空
	$("input[id^='columnCnName_']").live("keyup",function(){
		$(this).parent().next("div").hide();
	});
	//列数据类型不允许为空
	$("input[id^='dataType_']").live("keyup",function(){
		$(this).parent().next("div").hide();
	});
	 //模拟下拉框
	/*$(".selBtn ,.selTxt").click(function(){
		if($(this).nextAll(".querySelListBox").is(":hidden")){
			$(this).nextAll(".querySelListBox").slideDown();
			if($(this).hasClass("selBtn")){
                $(this).addClass("selBtnActive");
            }else{
            	$(this).next(".selBtn").addClass("selBtnActive");
            }
		}else{
			$(this).nextAll(".querySelListBox").slideUp();
			$(this).removeClass("selBtnActive");
			
		}
    	return false;
	});

	$(".querySelListBox a").click(function(){
		var selVal = $(this).attr("value");
		var selHtml = $(this).html();
		$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
		$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
		$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
		//向隐藏域中传递值
		$(this).parents(".querySelListBox").slideUp();
			return false;
	});
	$(document).click(function(ev){
		var e = ev||event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		$(".querySelListBox").slideUp();
		$(".selBtn").removeClass("selBtnActive");
	});*/

	/*$("div[id^='isMustColumn'] div").bind("click", function() {
		var _this = $(this);
		var _value = _this.attr("value");
		_this.parent().children("input").val(_value);
		_this.parent().children("div").removeClass();
		_this.addClass('current');
	});*/

	$("select[id^='dimVertLabelType']").live("change", function(){
		var labelTypeEnum = "<%=ServiceConstants.LABEL_TYPE_ENUM%>";
		var _typeId = $(this).children("option:selected").val();
		var _id = $(this).attr("id");
		var _index = _id.indexOf("_");
		var _numId = _id.substring(_index+1);
		$("#dataType_"+_numId).removeAttr("dataType");
		//枚举标签，选择维表
		if(labelTypeEnum == _typeId) {
			$("#period_"+_id).show();
			$("#isNeedAuthorityBoxDiv_"+_id).show();
			var _typeValue = $("#dimTransId_"+_numId+" option:selected").attr("_typeValue").toLowerCase();
			if(_typeValue.indexOf("char")>=0){
				$("#dataType_"+_numId).attr("dataType","char");
			}
		}else{
			$("#period_"+_id).hide();
			$("#isNeedAuthorityBoxDiv_"+_id).hide();
			$("#dataType_"+_numId).removeAttr("dataType");
		}
	});

	//标签维表切换
	$("#dimTransId").live("change", function(){
		$("#dataType").removeAttr("dataType");
		var _typeValue = $(this).children("option:selected").attr("_typeValue").toLowerCase();
		$("#dataType").removeAttr("dataType");
		if(_typeValue.indexOf("char")>=0){
			$("#dataType").attr("dataType","char");
		}
	});
	$("select[id^='dimTransId_']").live("change", function(){
		var _typeId = $(this).children("option:selected").val();
		var _id = $(this).attr("id");
		var _index = _id.indexOf("_");
		var _numId = _id.substring(_index+1);
		
		$("#dataType_"+_numId).removeAttr("dataType");
		var _typeValue = $(this).children("option:selected").attr("_typeValue").toLowerCase();
		if(_typeValue.indexOf("char")>=0){
			$("#dataType_"+_numId).attr("dataType","char");
		}
	});
	
	$("#saveBtn22").bind("click", function(){
		submitList();
	});
	$("#parentName").bind("click",{id:"parentName"},showMenu);
	initTree();
	if($("#columnListSize")) {
		var _vauleTemp = $("#columnListSize").val();
		if(_vauleTemp!=""&& _vauleTemp!="undefined"){
			_sortNum=$("#columnListSize").val();
		}
	}

	//更新周期
	$("#labelUpdateCycleBox div").bind('click', changeCycle);
	
	if($("#labelUpdateCycle").val() == 2){
		$("#failTimeShow").unbind("click");
		$("#failTimeShow").click(function(){
			 WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM',minDate:'%y-{%M+1}',maxDate:'2037-12'})
		 });
		$("#effecTimeShow").unbind("click");
		$("#effecTimeShow").click(function(){
			 WdatePicker({onpicked:function(){setEffecTime();},dateFmt:'yyyy-MM'})
		 });
	}else if($("#labelUpdateCycle").val() == 1){
		$("#failTimeShow").unbind("click");
		$("#failTimeShow").click(function(){
			WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d+1}',maxDate:'2037-12-31'})
		 });
		$("#effecTimeShow").unbind("click");
		$("#effecTimeShow").click(function(){
			 WdatePicker({onpicked:function(){setEffecTime();},dateFmt:'yyyy-MM-dd'})
		 });
	}

	setFailTime();
	setEffecTime();
});
	//提示
	$(document).tooltip({
		items:"#dependIndexTip,#countRulesTip,#countRulesDescTip,#dataTypeTip",
		content:function(){
			var element = $( this );
			if ( element.is( "#dependIndexTip" ) ) {
				return "指标名称用逗号分割，如A00225,A00226,A00227";
			}
			if ( element.is( "#countRulesTip" ) ) {
				return "如A00225+A00226>0 and A00227=1或者A00227";
			}
			if ( element.is( "#countRulesDescTip" ) ) {
				return "如本地主叫+本地被叫分钟数>0 且为存量用户";
			}
			if ( element.is( "#dataTypeTip" ) ) {
				return "请输入数据库中有的数据类型";
			}
		}
			 
	});

	//点击树节点事件	
	function treeOnClick(event, treeId, treeNode) {
		if (treeNode) {
			$("#parentId").val(treeNode.id);
			$("#parentName").val(treeNode.name);
			hideMenu();
		}
	}

	//标签分类树初始化
	function initTree(){
		var treeUrl = "${ctx}/ci/ciLabelInfoConfigAction!queryLabelTree.ai2do";
		if($("#period").is(":hidden")){
			treeUrl = treeUrl + "?configLabelInfo.labelTypeId=" + 1;
		}else{
			treeUrl = treeUrl + "?configLabelInfo.labelTypeId=" + 3;
		}
		$.ajax({
			type: "POST",
			url: treeUrl,
			async: false,
			dataType: "json",
			success: function(result){
				zNodes = result;
				zTree = $.fn.zTree.init($("#labelTree"), setting, zNodes);  
			}
		});
	}

	//显示分类树
	function showMenu(event) {
	    var id = event.data.id;
	    var cityObj = $("#" + id);
	    var cityOffset = $("#" + id).offset();
	    $("#menuContent").css({"left": cityOffset.left + "px", "top": cityOffset.top + cityObj.outerHeight() + "px","width":cityObj.width() + "px"}).slideDown("fast");
	    $("#ztreeBox,#labelTree").css({"width":(cityObj.width()) + "px"}); 
	    $("#ztreeBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"yx",
			mouseWheel:{
				preventDefault:true, //阻止冒泡事件
				axis:"yx"
			}
		});	
	    $(document).bind("mousedown", onBodyDown);
	}

	//绑定文档点击事件，点击空白处收起分类树
	function onBodyDown(event) {
	    if (!( event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
	        hideMenu();
	    }
	}

	//隐藏分类树
	function hideMenu() {
	    $("#menuContent").fadeOut("fast");
	    $(document).unbind("mousedown", onBodyDown);
	}
	
	//表单验证
	function validateForm(){
		var failTimeShow = $.trim($("#failTimeShow").val());
		if(failTimeShow == ""){
			$("#failTimeShow").val("");
		}else{
			$("#failTimeShow").val(failTimeShow);
		}
		
		var effecTimeShow = $.trim($("#effecTimeShow").val());
		if(effecTimeShow == ""){
			$("#effecTimeShow").val("");
		}else{
			$("#effecTimeShow").val(effecTimeShow);
		}
		
		var flag = validater.form();
		if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_VERT%>"){
			$(".requiredClass").each(function(){
				   var _value = $.trim($(this).val());
				   var _id = $(this).attr("id");
				   var msg = "";
				   if(_value == ""){
						if(_id.indexOf("columnName_")>=0){
						   msg="列名不允许为空！";
						}
						if(_id.indexOf("columnCnName_")>=0){
						   msg="列中文名不允许为空！";
						}
						if(_id.indexOf("dataType_")>=0){
						   msg="列数据类型允许为空！";
						}
						$(this).parent().next("div").empty().show().append(msg);
						flag = false;
					}
				   
			});
		}
		if($("fieldset[id^='vertColumnFieldset']").length>6) {
			flag = false ;
			showAlert("组合标签列信息不能超过6个，请重新输入！","failed");
			return false;
		}
		var _columnName = [];
		var _columnCnName = [];
		$("fieldset[id^='vertColumnFieldset']").each(function(){
			var columnNameTemp = $(this).find("input[id^='columnName_']").val();
			var columnCnNameTemp = $(this).find("input[id^='columnCnName_']").val();
			if(columnNameTemp != ""){
				_columnName.push(columnNameTemp);
			}
			if(columnNameTemp != ""){
				_columnCnName.push(columnCnNameTemp);
			}
		});
		var columnNameNary=_columnName.sort();
		for(var i=0;i<_columnName.length;i++){
			if (columnNameNary[i]==columnNameNary[i+1]){
				flag = false ;
				showAlert("列名不能重复，请重新输入！","failed");
				return false;
			}
		}
		var columnCnNameNary=_columnCnName.sort();
		for(var i=0;i<_columnCnName.length;i++){
			if (columnCnNameNary[i]==columnCnNameNary[i+1]){
				flag = false ;
				showAlert("列中文名不能重复，请重新输入！","failed");
				return false;
			}
		}
		$("input[id^='dataType']").each(function(){
			var _dataTypeValue = $(this).val().toLowerCase();
			if($(this).attr("dataType")){
				var dataTypeAttr = $(this).attr("dataType");
				if(_dataTypeValue.indexOf(dataTypeAttr)<0){
					flag = false;
					showAlert("枚举型标签对应的数据类型为字符串类型，请重新输入数据类型！","failed");
					return false;
				}
			}
		});
		//日期类标签只能选择一个指标
		if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_DATE%>"){
			var dependIndex = $("#dependIndex").val();
			if(dependIndex.indexOf(",") >= 0) {
				flag = false;
				$("#dependIndex").focus();
				$("#dependIndexTip_error").empty().html("日期型标签对应依赖的指标只能选择一个，请重新选择！").show();
				//showAlert("日期型标签对应依赖的指标只能选择一个，请重新选择！","failed");
				return false;
			}
		}
		//日期类标签只能选择一个指标
		if($("#dimLabelType option:selected").val() == "<%=ServiceConstants.LABEL_TYPE_ENUM%>"){
			var dependIndex = $("#dependIndex").val();
			if(dependIndex.indexOf(",") >= 0) {
				flag = false;
				$("#dependIndex").focus();
				$("#dependIndexTip_error").empty().html("枚举型标签对应依赖的指标只能选择一个，请重新选择！").show();
				//showAlert("日期型标签对应依赖的指标只能选择一个，请重新选择！","failed");
				return false;
			}
		}
		return flag;
	}
	//保存发布配置
	function submitList(){
		if(validateForm()){
			var actionUrl = $.ctx + "/ci/ciLabelInfoConfigAction!saveConfigLabelInfo.ai2do";
			$.post(actionUrl,$("#saveForm").serialize(), function(result){
				if(result.success){
					//alert("保存成功")
					publishLabel($("#labelId").val());
				}else{
					parent.showAlert(result.cmsg,"failed");
				}
			});
		}
	}
	//进入发布页面
	function publishLabel(labelId) {
		var url =  $.ctx + "/ci/ciLabelInfoConfigAction!getVerifySql.ai2do?configLabelInfo.labelId="+labelId;
		window.location = url;
	}
	
	//添加列信息
	function addColumnInfo() {
		_sortNum++;
		var _obj = $("#vertColumnFieldset_0");
		var vertColumnFieldsetDiv = _obj.clone(true);
		//_obj.parent().after(vertColumnFieldsetDiv);
		_obj.parent().parent().append(vertColumnFieldsetDiv);
		vertColumnFieldsetDiv.find("fieldset[id^='vertColumnFieldset_']").removeAttr("id").attr("id","vertColumnFieldset_"+_sortNum);
		vertColumnFieldsetDiv.find("select[id^='dimVertLabelType_']").attr("id","dimVertLabelType_"+_sortNum).attr("name","ciMdaSysTableColumnList["+_sortNum+"].vertLabelTypeId").val("");
		vertColumnFieldsetDiv.find("li[id^='period_dimVertLabelType']").attr("id","period_dimVertLabelType_"+_sortNum).hide();
		vertColumnFieldsetDiv.find("li[id^='isNeedAuthorityBoxDiv_dimVertLabelType']").attr("id","isNeedAuthorityBoxDiv_dimVertLabelType_"+_sortNum).hide();
		vertColumnFieldsetDiv.find("select[id^='dimTransId_']").attr("id","dimTransId_"+_sortNum).attr("name","ciMdaSysTableColumnList["+_sortNum+"].dimTransId").val("");
		vertColumnFieldsetDiv.find("input[id^='columnName_']").attr("id","columnName_"+_sortNum).attr("name","ciMdaSysTableColumnList["+_sortNum+"].columnName").val("");
		vertColumnFieldsetDiv.find("input[id^='columnCnName_']").attr("id","columnCnName_"+_sortNum).attr("name","ciMdaSysTableColumnList["+_sortNum+"].columnCnName").val("");
		vertColumnFieldsetDiv.find("input[id^='dataType_']").attr("id","dataType_"+_sortNum).attr("name","ciMdaSysTableColumnList["+_sortNum+"].dataType").val("").removeAttr("dataType");
		vertColumnFieldsetDiv.find("input[id^='isMustColumn_']").attr("id","isMustColumn_"+_sortNum).attr("name","ciMdaSysTableColumnList["+_sortNum+"].isMustColumn").val("0");
		vertColumnFieldsetDiv.find("div[id^='cNameTip_']").attr("id","cNameTip_"+_sortNum).empty().hide();
		vertColumnFieldsetDiv.find("div[id^='cnNameTip_']").attr("id","cnNameTip_"+_sortNum).empty().hide();
		vertColumnFieldsetDiv.find("div[id^='dTypeTip_']").attr("id","dTypeTip_"+_sortNum).empty().hide();
		vertColumnFieldsetDiv.find("div[id^='mustColumn_']").removeClass();
		vertColumnFieldsetDiv.find("div[id^='notMustColumn_']").addClass("current");
		vertColumnFieldsetDiv.find("a[name='delColumnInfoIcon']").show();
	}
	//删除列信息
	function delColumnInfo(a) {
		$(a).parent().parent().remove();
	}
	//关闭指标检索弹出框
	function closeAddLabelDialog(){
		$("#index_code_div").dialog("close");
	}
	
	//打开编辑指标检索弹出框
	function editDialog(){
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/kpiSearchDialog.jsp";
		var dlgObj = dialogUtil.create_dialog("index_code_div", {
			"title" :  "指标编码信息",
			"height": "auto",
			"width" : 730,
			"frameSrc" : ifmUrl,
			"frameHeight":567,
			"position" : ['center','center'] 
			
		});
	}
	//切换更新周期
	function changeCycle() {
		var dateFmt = 'yyyy-MM';
		var minDate = '%y-{%M+1}';
		var maxDate = '2037-12'
		var _this = $(this);
		var cycle = _this.attr('cycle');
		
		$("#labelUpdateCycleBox div").removeClass();
		_this.addClass("current");
		var defaultFailDate = '2037-12-31';
		var effecTime = new Date();
		var nowyear = effecTime.getFullYear();
		var nowmonth = effecTime.getMonth()+1;
		var nowdate = effecTime.getDate();
		if(nowmonth<10){
			nowmonth = '0'+nowmonth;
		}
		if(nowdate<10){
			nowdate = '0'+nowdate;
		}
		var effecTimeStr = '2015-07-01';
		
		if(cycle == 1) {//日周期
			$('#labelUpdateCycle').val(1);
			dateFmt = 'yyyy-MM-dd';
			minDate = '%y-%M-{%d+1}';
			maxDate = '2037-12-31';
			effecTimeStr = nowyear+'-'+nowmonth+'-'+nowdate;
		} else if(cycle == 2) {//月周期
			$('#labelUpdateCycle').val(2);
			dateFmt = 'yyyy-MM';
			minDate = '%y-{%M+1}';
			defaultFailDate = '2037-12';
			maxDate = '2037-12';
			effecTimeStr = nowyear+'-'+nowmonth;
		}
		
		$("#failTimeShow").val(defaultFailDate);
		$("#failTime").val(defaultFailDate);
		$("#failTimeShow").unbind("click");
		$("#failTimeShow").click(function(){
			 WdatePicker({onpicked:function(){setFailTime();}, dateFmt:dateFmt, minDate:minDate,maxDate:maxDate})
	    });
		
		$("#effecTimeShow").val(effecTimeStr);
		$("#effecTime").val(effecTimeStr);
		$("#effecTimeShow").unbind("click");
		$("#effecTimeShow").click(function(){
			 WdatePicker({onpicked:function(){setEffecTime();}, dateFmt:dateFmt})
	    });
		changeSysTable();
	}

	function changeSysTable(){
		var updateCycle =$("#labelUpdateCycle").val();
		var _typeId = $("#dimLabelType option:selected").val();
		var labelTypeId = 1;
		if(_typeId == "<%=ServiceConstants.LABEL_TYPE_VERT%>"){
			labelTypeId = "<%=ServiceConstants.LABEL_TYPE_VERT%>";
		}else {
			labelTypeId = 1;
		}
		var pojo = {
				"pojo.updateCycle":updateCycle,
				"pojo.labelTypeId":labelTypeId
		};
		var actionUrl = "${ctx}/ci/ciLabelInfoConfigAction!queryCiSysMdaTable.ai2do";
		$.ajax({
			url:actionUrl,
			type:"POST",
			dataType: "json",
			async	: false,
			data: pojo,
			success:function(data){
				var result = data.resultTableList;
				var html = "<option value=''></option>";
				for(var index=0; index<result.length; index++){
					var  tableName = result[index].tableName;
					var  tableId = result[index].tableId;
					html += "<option value='"+tableId+"'>"+tableName+"</option>"
					//alert(tableName+"_"+tableId);
				}
				$("#tableId").html(html);
			}
		});
	} 
	function setFailTime(){
		if($("#failTimeShow").val().length == 7){
			$("#labelFailTime").val($("#failTimeShow").val()+"-01");
		}else{
			$("#labelFailTime").val($("#failTimeShow").val());
		}
	}
	
	function setEffecTime(){
		if($("#effecTimeShow").val().length == 7){
			$("#labelEffecTime").val($("#effecTimeShow").val()+"-01");
		}else{
			$("#labelEffecTime").val($("#effecTimeShow").val());
		}
	}
</script>
</head>
<body>
 	<jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
<div>
	<form id="saveForm" method="post">
		<input type="hidden" name="configLabelInfo.labelId" id="labelId" value="${configLabelInfo.labelId }"/>
		<input type="hidden" name="configLabelInfo.countRulesCode" id="countRulesCode" value="${configLabelInfo.countRulesCode }"/>
		<input type="hidden" name="configLabelInfo.columnId" id="columnId" value="${configLabelInfo.columnId }"/>
		<div class="save-customer-header comWidth clearfix ">
			<div class="logoBox clearfix">
				<div class="index_logo fleft" onclick="showHomePage();return false;"></div>
				<div class="secTitle fleft">标签维护</div>
			</div>
		</div>
		<div class="comWidth clearfix save-customer-inner save-tag-content" > 
			<div class="clearfix titleBox">
				<div class="starIcon fleft"></div>
				<div class="customer-title fleft">标签发布配置</div> 
			</div>
			<div class="clientBaseList clearfix" >
				<ul>
					<li class="clearfix">
						<div class="fleft fmt160"> 
							<span class="star fleft"></span>
							<label>更新周期：</label>
						</div>
						<div id="labelUpdateCycleBox" class="fleft createPeriodBox">
							<input id="labelUpdateCycle" name="configLabelInfo.updateCycle" type="hidden" value="<c:if test="${empty configLabelInfo.updateCycle }">2</c:if><c:if test="${not empty configLabelInfo.updateCycle }">${configLabelInfo.updateCycle }</c:if>" />
							<c:choose>
									<c:when test="${configLabelInfo.updateCycle == 1}">
										<div cycle="1" class="current">日</div> 
						   				<div cycle="2">月</div>
									</c:when>
									<c:otherwise>
										<div cycle="1">日</div> 
						   				<div cycle="2" class="current">月</div>
									</c:otherwise>
							</c:choose> 
						</div>
					</li>
					<li class="clearfix">
						<div class="fleft fmt160"> 
							<span class="star fleft"></span>
							<label>生效日期：</label>
						</div>
						<p class="customer-date-box fleft">
							<c:choose>
									<c:when test="${configLabelInfo.updateCycle == 1}">
										<input id="effecTimeShow" name="effecTimeShow" type="text" readonly
										class="customer-date-input  new_tag_input tag_date_input" value="<c:if test="${empty configLabelInfo.effecTime }"></c:if><c:if test="${not empty configLabelInfo.effecTime }">${fn:substring( configLabelInfo.effecTime,0,10)}</c:if>" />
									</c:when>
									<c:otherwise>
										<input id="effecTimeShow" name="effecTimeShow" type="text" readonly
										class="customer-date-input  new_tag_input tag_date_input" value="<c:if test="${empty configLabelInfo.effecTime }"></c:if><c:if test="${not empty configLabelInfo.effecTime }">${fn:substring( configLabelInfo.effecTime,0,7)}</c:if>"/>
									</c:otherwise>
							</c:choose> 
							<input id="labelEffecTime" name="configLabelInfo.effecTime" type="hidden" value="<c:if test="${empty configLabelInfo.effecTime }"></c:if><c:if test="${not empty configLabelInfo.effecTime }">${configLabelInfo.effecTime }</c:if>"/>
						</p>
						<div id="effecTimeTip" class="tishi-page error" style="display:none;"></div>
					</li>
					<li class="clearfix">
						<div class="fleft fmt160"> 
							<span class="star fleft"></span>
							<label>失效日期：</label>
						</div>
						<p class="customer-date-box fleft">
							<c:choose>
									<c:when test="${configLabelInfo.updateCycle == 1}">
										<input id="failTimeShow" name="failTimeShow" type="text" readonly
										class="customer-date-input  new_tag_input tag_date_input" value="<c:if test="${empty configLabelInfo.failTime }">2037-12-31</c:if><c:if test="${not empty configLabelInfo.failTime }">${fn:substring( configLabelInfo.failTime,0,10)}</c:if>" />
									</c:when>
									<c:otherwise>
										<input id="failTimeShow" name="failTimeShow" type="text" readonly
										class="customer-date-input  new_tag_input tag_date_input" value="<c:if test="${empty configLabelInfo.failTime }">2037-12</c:if><c:if test="${not empty configLabelInfo.failTime }">${fn:substring( configLabelInfo.failTime,0,7)}</c:if>"/>
									</c:otherwise>
							</c:choose> 
							<input id="labelFailTime" name="configLabelInfo.failTime" type="hidden" value="<c:if test="${empty configLabelInfo.failTime }">2037-12</c:if><c:if test="${not empty configLabelInfo.failTime }">${configLabelInfo.failTime }</c:if>"/>
						</p>
						<div id="failTimeTip" class="tishi-page error" style="display:none;"></div>
					</li>
					<li class="clearfix">
			 			<div class="fleft fmt160">
							<p class="star fleft"></p>
							<label>标签类型：</label>
						</div>
			 			<div class="fleft">
			 				<select name="configLabelInfo.labelTypeId" id="dimLabelType" class="fleft dialog_select">
							   	<c:forEach var="_dimLabelType" items="${dimLabelTypeList}">
							   		<c:choose>
							   			<c:when test="${_dimLabelType.labelTypeId == configLabelInfo.labelTypeId}">
											<option value="${_dimLabelType.labelTypeId }" selected="selected" >${_dimLabelType.labelTypeName }</option>
							   			</c:when>
							   			<c:otherwise>
									   		 <option value="${_dimLabelType.labelTypeId }" >${_dimLabelType.labelTypeName }</option>
							   			</c:otherwise>
							   		</c:choose>
							   	</c:forEach>
							</select>
						</div>
			 		</li>
					<li class="clearfix" id="dependIndexDiv">
						<p class="fleft fmt160">
							<span class="star fleft"></span>
							<label>规则依赖的指标：</label>
						</p>
						<p class="fleft inputBox">
			   				<input type="text"  name="configLabelInfo.dependIndex" type="text" id="dependIndex"  value="${configLabelInfo.dependIndex }"
			   					maxlength="100"  onkeyup="$('#dependIndexTip_error').empty().hide();return false;"/>
						</p>
				   		<span id="dependIndexTip" class="fleft greenTip inline_block" >&nbsp;</span>
				   		<a  class="fleft infoTip show-dialog-link" href="javascript:void(0);" onclick="editDialog();">指标查询</a>
				   		<div id="dependIndexTip_error"  class="tishi-page error"  style="display:none;"></div>
			 		</li>
			 		<li class="clearfix" id="countRulesDiv">
			 			<p class="fleft fmt160">
							<span class="star fleft"></span>
							<label>具体规则：</label>
						</p>
						<p class="fleft inputBox">
				   			<input type="text"  name="configLabelInfo.countRules" type="text" id="countRules" value="${configLabelInfo.countRules }"
				   					maxlength="2048"  value="" />
						</p>
				   		<span id="countRulesTip" class="fleft greenTip inline_block" >&nbsp;</span>
			 		</li>
			 		<li class="clearfix" id="countRulesDescDiv">
			 			<p class="fleft fmt160">
							<span class="fleft"></span>
							<label>规则描述：</label>
						</p>
						<p class="fleft textareaBox config-label">
							<textarea class="fleft config-label"  id="countRulesDesc" onkeyup="if(this.value.length>600){this.value = this.value.slice(0,600)}" onblur="this.value = this.value.slice(0,600)" name="configLabelInfo.countRulesDesc">${configLabelInfo.countRulesDesc }</textarea>
						</p> 
						<span id="countRulesDescTip" class="fleft greenTip inline_block" style="vertical-align:top;">&nbsp;</span>
			 		</li>
			 		<li class="clearfix">
			 			<p class="fleft fmt160">
							<span class="star fleft"></span>
							<label>标签分类：</label>
						</p>
						<p  class="fleft inputBox">
							<input type="hidden" name="configLabelInfo.parentId" id="parentId" value="${configLabelInfo.parentId }"/>
							<input name="configLabelInfo.parentName" type="text" id="parentName" value="${configLabelInfo.parentName }" 
									maxlength="35" class="selectCheckbox"   readonly="readonly"/>
						</p>
			 		</li>
			 		<li class="clearfix" id="period">
			 			<div class="fleft fmt160">
							<p class="star fleft"></p>
							<label>标签维表：</label>
						</div>
			 			<div class="fleft">
			 				<select name="configLabelInfo.dimTransId" id="dimTransId" class="fleft dialog_select">
							   	<c:forEach var="dimTransId" items="${dimTableDefineList}">
							   		<c:choose>
							   			<c:when test="${dimTransId.dimId == configLabelInfo.dimTransId}">
											<option value="${dimTransId.dimId }" selected="selected" _typeValue="${dimTransId.dimCodeColType }">${dimTransId.dimTablename }</option>
							   			</c:when>
							   			<c:otherwise>
									   		 <option value="${dimTransId.dimId }" _typeValue="${dimTransId.dimCodeColType }">${dimTransId.dimTablename }</option>
							   			</c:otherwise>
							   		</c:choose>
							   	</c:forEach>
							</select>
						</div>
			 		</li>
			 		<li class="clearfix" id="isNeedAuthorityBoxDiv">
			 			<div class="fleft fmt160">
							<p class="star fleft"></p>
							<label>过滤权限：</label>
						</div>
						<div id="isNeedAuthorityBox" class="fleft createPeriodBox">
						   <input id="isNeedAuthority" name="configLabelInfo.isNeedAuthority" type="hidden" value="<c:if test="${empty configLabelInfo.isNeedAuthority }">0</c:if><c:if test="${not empty configLabelInfo.isNeedAuthority }">${configLabelInfo.isNeedAuthority }</c:if>" />
						   <c:choose>
						   		<c:when test="${configLabelInfo.isNeedAuthority == 1}">
						   			<div value="1" id="needAuthority" class="current">是</div> 
								   	<div value="0" id="notNeedAuthority" >否</div>
						   		</c:when>
						   		<c:otherwise>
								   <div value="1" id="needAuthority" >是</div> 
								   <div value="0" id="notNeedAuthority" class="current">否</div>
						   		</c:otherwise>
						   </c:choose> 
						</div>
			 		</li>
			 		<li class="clearfix" id="dataTypeDiv">
			 			<p class="fleft fmt160">
							<span class="star fleft"></span>
							<label>数据类型：</label>
						</p>
						<p class="fleft inputBox">
				   			<input type="text"  name="configLabelInfo.dataType" type="text" id="dataType"  value="${configLabelInfo.dataType }"
				   					maxlength="32"  value="" />
						</p>
				   		<span id="dataTypeTip" class="fleft greenTip inline_block" >&nbsp;</span>
			 		</li>
			 		<li class="clearfix">
			 			<div class="fleft fmt160">
							<p class="star fleft"></p>
							<label>宽表选择：</label>
						</div>
						<div id="tableTypeBox" class="fleft createPeriodBox">
						   <input id="tableTypeId" name="configLabelInfo.createTableType" type="hidden" value="1" /> 
						   <div value="1" id="exist" class="current">已有宽表</div> 
						   <div value="2" id="new" >新建宽表</div>
						</div>
			 		</li>
			 		<li class="clearfix">
			 			<div class="fleft fmt160">
							<p class="star fleft"></p>
							<label>标签所属宽表表名：</label>
						</div>
						<div class="fleft">
							<div class="fleft" id="existTable">
								<select name="configLabelInfo.tableId" id="tableId" class="fleft dialog_select">
									<option value=""></option>
								   	<c:forEach var="ciMdaSysTable" items="${ciMdaSysTableList}">
								   		<c:choose>
								   			<c:when test="${ not empty configLabelInfo.updateCycle}">
								   				<c:choose>
								   					<c:when test="${ not empty configLabelInfo.labelTypeId && configLabelInfo.labelTypeId == 8}">
								   						<c:choose>
											   			<c:when test="${ciMdaSysTable.updateCycle == configLabelInfo.updateCycle && ciMdaSysTable.tableType == 8}">
										   				<c:choose>
												   			<c:when test="${ciMdaSysTable.tableId == configLabelInfo.tableId}">
																<option value="${ciMdaSysTable.tableId }" selected="selected">${ciMdaSysTable.tableName }</option>
												   			</c:when>
												   			<c:otherwise>
														   		 <option value="${ciMdaSysTable.tableId }">${ciMdaSysTable.tableName }</option>
												   			</c:otherwise>
														</c:choose>
														</c:when>
														</c:choose>
								   					</c:when>
								   					<c:otherwise>
										   				<c:choose>
											   			<c:when test="${ciMdaSysTable.updateCycle == configLabelInfo.updateCycle && ciMdaSysTable.tableType == 1}">
										   				<c:choose>
												   			<c:when test="${ciMdaSysTable.tableId == configLabelInfo.tableId}">
																<option value="${ciMdaSysTable.tableId }" selected="selected">${ciMdaSysTable.tableName }</option>
												   			</c:when>
												   			<c:otherwise>
														   		 <option value="${ciMdaSysTable.tableId }">${ciMdaSysTable.tableName }</option>
												   			</c:otherwise>
														</c:choose>
														</c:when>
														</c:choose>
								   					</c:otherwise>
								   				</c:choose>
								   			</c:when>
								   			<c:otherwise>
								   				<c:choose>
								   				<c:when test="${ciMdaSysTable.updateCycle == 2 && ciMdaSysTable.tableType == 1}">
								   					<c:choose>
													<c:when test="${ciMdaSysTable.tableId == configLabelInfo.tableId}">
														<option value="${ciMdaSysTable.tableId }" selected="selected">${ciMdaSysTable.tableName }</option>
										   			</c:when>
										   			<c:otherwise>
												   		 <option value="${ciMdaSysTable.tableId }">${ciMdaSysTable.tableName }</option>
										   			</c:otherwise>
										   			</c:choose>
								   				</c:when>
								   				</c:choose>
								   			</c:otherwise>
								   		</c:choose>
								   	</c:forEach>
								</select>
							</div>
							<div class="fleft inputBox" id="newTable">
								<input name="configLabelInfo.tableName" type="text" id="tableName" value="" 
									maxlength="2048" />
							</div>
						</div>
			 		</li>
			 	</ul>
			 </div>
			<div class="tagList clearfix attrList" id="vertDiv">
				<div class="clearfix customer-title-item">
					<div class="customer-title fleft">请添加组合标签列信息</div>
					<div class="fleft checkboxItem" >
						<input type="button" id="addAttributeBtn" value="添加列信息" class="fleft addFileImportBtn" onclick="addColumnInfo();"/>
					</div>
				</div>
				<!--<div class="clearfix customer-tag-list-box">
					<div class="fleft tagListBox">
						<div class="tagListBoxInner"> 
							 --><ul class="clearfix">
							 <c:choose>
							 	<c:when test="${not empty ciMdaSysTableColumnList && fn:length(ciMdaSysTableColumnList)>0}">
								 <c:forEach items="${ciMdaSysTableColumnList}" var="ciMdaSysTableColumn" varStatus="status">
								 	<fieldset style="width:99.9%;" id="vertColumnFieldset_${status.index}">
								 		<legend>列信息</legend>
								 		<li class="clearfix">
								 			<a href="javascript:void(0);" name="delColumnInfoIcon" class="fright removeColumnInfoBtn <c:if test="${status.index == 0 }">hidden</c:if>" onclick="delColumnInfo(this);"></a>
								 		</li>
								 		<li class="clearfix">
								 			<div class="fleft fmt160">
												<p class="star fleft"></p>
												<label>标签类型：</label>
											</div>
								 			<div class="fleft">
								 				<select name="ciMdaSysTableColumnList[${status.index}].vertLabelTypeId" id="dimVertLabelType_${status.index}" class="fleft dialog_select">
												   	<c:forEach var="dimLabelType" items="${dimLabelTypeNotContainVertList}">
												   		<c:choose>
												   			<c:when test="${dimLabelType.labelTypeId == ciMdaSysTableColumn.vertLabelTypeId}">
																<option value="${dimLabelType.labelTypeId }" selected="selected">${dimLabelType.labelTypeName }</option>
												   			</c:when>
												   			<c:otherwise>
														   		 <option value="${dimLabelType.labelTypeId }">${dimLabelType.labelTypeName }</option>
												   			</c:otherwise>
												   		</c:choose>
												   	</c:forEach>
												</select>
											</div>
								 		</li>
									 	<li class="clearfix" id="period_dimVertLabelType_${status.index}" style="display:<c:if test="${ciMdaSysTableColumn.vertLabelTypeId == 5 }">block;</c:if><c:if test="${ciMdaSysTableColumn.vertLabelTypeId != 5 }">none;</c:if>">
								 			<div class="fleft fmt160">
												<p class="star fleft"></p>
												<label>标签维表：</label>
											</div>
								 			<div class="fleft">
								 				<select name="ciMdaSysTableColumnList[${status.index}].dimTransId" id="dimTransId_${status.index}" class="fleft dialog_select">
												   	<c:forEach var="dimTransId" items="${dimTableDefineList}">
												   		<c:choose>
												   			<c:when test="${dimTransId.dimId == ciMdaSysTableColumn.dimTransId}">
																<option value="${dimTransId.dimId }" selected="selected" _typeValue="${dimTransId.dimCodeColType }">${dimTransId.dimTablename }</option>
												   			</c:when>
												   			<c:otherwise>
														   		 <option value="${dimTransId.dimId }" _typeValue="${dimTransId.dimCodeColType }">${dimTransId.dimTablename }</option>
												   			</c:otherwise>
												   		</c:choose>
												   	</c:forEach>
												</select>
											</div>
								 		</li>
								 		<li class="clearfix" id="isNeedAuthorityBoxDiv_dimVertLabelType_${status.index}" style="display:<c:if test="${ciMdaSysTableColumn.vertLabelTypeId == 5 }">block;</c:if><c:if test="${ciMdaSysTableColumn.vertLabelTypeId != 5 }">none;</c:if>">
								 			<div class="fleft fmt160">
												<p class="star fleft"></p>
												<label>过滤权限：</label>
											</div>
											<div id="isNeedAuthorityBox_${status.index}" class="fleft createPeriodBox">
											   <input id="isNeedAuthority_${status.index}" name="ciMdaSysTableColumnList[${status.index}].isNeedAuthority" type="hidden" value="<c:if test="${empty ciMdaSysTableColumn.isNeedAuthority }">0</c:if><c:if test="${not empty ciMdaSysTableColumn.isNeedAuthority }">${ciMdaSysTableColumn.isNeedAuthority }</c:if>" />
											   <c:choose>
											   		<c:when test="${ciMdaSysTableColumn.isNeedAuthority == 1}">
											   			<div value="1" id="needAuthority_${status.index}" class="current">是</div> 
													   	<div value="0" id="notNeedAuthority_${status.index}" >否</div>
											   		</c:when>
											   		<c:otherwise>
													   <div value="1" id="needAuthority_${status.index}" >是</div> 
													   <div value="0" id="notNeedAuthority_${status.index}" class="current">否</div>
											   		</c:otherwise>
											   </c:choose> 
											</div>
								 		</li>
								 		<li class="clearfix">
								 			<p class="fleft fmt160">
												<span class="star fleft"></span>
												<label>列名 ：</label>
											</p>
											<p class="fleft inputBox">
									   			<input type="text"  name="ciMdaSysTableColumnList[${status.index}].columnName" type="text" id="columnName_${status.index}"  value="${ciMdaSysTableColumn.columnName }"
									   					maxlength="2048"  class="requiredClass" />
											</p>
											<div id="cNameTip_${status.index}" class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
								 		</li>
								 		<li class="clearfix">
								 			<p class="fleft fmt160">
												<span class="star fleft"></span>
												<label>列中文名：</label>
											</p>
											<p class="fleft inputBox">
									   			<input type="text"  name="ciMdaSysTableColumnList[${status.index}].columnCnName" type="text" id="columnCnName_${status.index}"  value="${ciMdaSysTableColumn.columnCnName }"
									   					maxlength="2048"   class="requiredClass" />
											</p>
											<div id="cnNameTip_${status.index}"  class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
								 		</li>
								 		
								 		<li class="clearfix">
								 			<p class="fleft fmt160">
												<span class="star fleft"></span>
												<label>列数据类型：</label>
											</p>
											<p class="fleft inputBox">
									   			<input type="text"  name="ciMdaSysTableColumnList[${status.index}].dataType" type="text" id="dataType_${status.index}"  value="${ciMdaSysTableColumn.dataType }"
									   					maxlength="2048"  class="requiredClass" />
											</p>
											<div id="dTypeTip_${status.index}" class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
								 		</li>
								 		<li class="clearfix">
											<div class="fleft fmt160">
												<p class="star fleft"></p>
												<label>必选列：</label>
											</div>
											<div id="isMustColumn${status.index}Box" class="fleft createPeriodBox">
											   <input id="isMustColumn_${status.index}" name="ciMdaSysTableColumnList[${status.index}].isMustColumn" type="hidden" value="<c:if test="${empty ciMdaSysTableColumn.isMustColumn }">0</c:if><c:if test="${not empty ciMdaSysTableColumn.isMustColumn }">${ciMdaSysTableColumn.isMustColumn }</c:if>" />
											   <c:choose>
											   		<c:when test="${ciMdaSysTableColumn.isMustColumn == 1}">
											   			<div value="1" id="mustColumn_${status.index}" class="current">是</div> 
													   	<div value="0" id="notMustColumn_${status.index}" >否</div>
											   		</c:when>
											   		<c:otherwise>
													   <div value="1" id="mustColumn_${status.index}" >是</div> 
													   <div value="0" id="notMustColumn_${status.index}" class="current">否</div>
											   		</c:otherwise>
											   </c:choose> 
											</div>
								 		</li>
							 		</fieldset>
								 </c:forEach>
							 	</c:when>
							 	<c:otherwise>
						 		<fieldset style="width:99.9%;" id="vertColumnFieldset_0">
							 		<legend>列信息</legend>
							 		<li class="clearfix">
							 			<a href="javascript:void(0);" name="delColumnInfoIcon" class="fright removeColumnInfoBtn hidden" onclick="delColumnInfo(this)"></a>
							 		</li>
							 		<li class="clearfix">
							 			<div class="fleft fmt160">
											<p class="star fleft"></p>
											<label>标签类型：</label>
										</div>
							 			<div class="fleft">
							 				<select name="ciMdaSysTableColumnList[0].vertLabelTypeId" id="dimVertLabelType_0" class="fleft dialog_select">
											   	<c:forEach var="dimLabelType" items="${dimLabelTypeNotContainVertList}">
											   		<c:choose>
											   			<c:when test="${dimLabelType.labelTypeId == ciMdaSysTableColumnList[0].vertLabelTypeId}">
															<option value="${dimLabelType.labelTypeId }" selected="selected">${dimLabelType.labelTypeName }</option>
											   			</c:when>
											   			<c:otherwise>
													   		 <option value="${dimLabelType.labelTypeId }">${dimLabelType.labelTypeName }</option>
											   			</c:otherwise>
											   		</c:choose>
											   	</c:forEach>
											</select>
										</div>
							 		</li>
								 	<li class="clearfix" id="period_dimVertLabelType_0" style="display:none;">
							 			<div class="fleft fmt160">
											<p class="star fleft"></p>
											<label>标签维表：</label>
										</div>
							 			<div class="fleft">
							 				<select name="ciMdaSysTableColumnList[0].dimTransId" id="dimTransId_0" class="fleft dialog_select">
											   	<c:forEach var="dimTransId" items="${dimTableDefineList}">
											   		<c:choose>
											   			<c:when test="${dimTransId.dimId == ciMdaSysTableColumnList[0].dimTransId}">
															<option value="${dimTransId.dimId }" selected="selected" _typeValue="${dimTransId.dimCodeColType }">${dimTransId.dimTablename }</option>
											   			</c:when>
											   			<c:otherwise>
													   		 <option value="${dimTransId.dimId }" _typeValue="${dimTransId.dimCodeColType }">${dimTransId.dimTablename }</option>
											   			</c:otherwise>
											   		</c:choose>
											   	</c:forEach>
											</select>
										</div>
							 		</li>
							 		<li class="clearfix" id="isNeedAuthorityBoxDiv_dimVertLabelType_0" style="display:none;">
								 			<div class="fleft fmt160">
												<p class="star fleft"></p>
												<label>过滤权限：</label>
											</div>
											<div id="isNeedAuthorityBox_0" class="fleft createPeriodBox">
											   <input id="isNeedAuthority_0" name="ciMdaSysTableColumnList[0].isNeedAuthority" type="hidden" value="0" />
											   <div value="1" id="needAuthority_0" >是</div> 
											   <div value="0" id="notNeedAuthority_0" class="current">否</div>
											</div>
								 		</li>
							 		<li class="clearfix">
							 			<p class="fleft fmt160">
											<span class="star fleft"></span>
											<label>列名：</label>
										</p>
										<p class="fleft inputBox">
								   			<input type="text"  name="ciMdaSysTableColumnList[0].columnName" type="text" id="columnName_0"  value="${ciMdaSysTableColumnList[0].columnName }"
								   					maxlength="2048"  class="requiredClass" />
										</p>
										<div id="cNameTip_0" class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
							 		</li>
							 		<li class="clearfix">
							 			<p class="fleft fmt160">
											<span class="star fleft"></span>
											<label>列中文名：</label>
										</p>
										<p class="fleft inputBox">
								   			<input type="text"  name="ciMdaSysTableColumnList[0].columnCnName" type="text" id="columnCnName_0"  value="${ciMdaSysTableColumnList[0].columnCnName }"
								   					maxlength="2048"  class="requiredClass" />
										</p>
										<div id="cnNameTip_0"  class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
							 		</li>
							 		
							 		<li class="clearfix">
							 			<p class="fleft fmt160">
											<span class="star fleft"></span>
											<label>列数据类型：</label>
										</p>
										<p class="fleft inputBox">
								   			<input type="text"  name="ciMdaSysTableColumnList[0].dataType" type="text" id="dataType_0"  value="${ciMdaSysTableColumnList[0].dataType }"
								   					maxlength="2048" class="requiredClass" />
										</p>
										<div id="dTypeTip_0" class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
							 		</li>
							 		<li class="clearfix">
										<div class="fleft fmt160">
											<p class="star fleft"></p>
											<label>必选列：</label>
										</div>
										<div id="isMustColumn0Box" class="fleft createPeriodBox">
										   <input id="isMustColumn_0" name="ciMdaSysTableColumnList[0].isMustColumn" type="hidden" value="1" /> 
										   <div value="1" id="mustColumn_0" class="current">是</div> 
										   <div value="0" id="notMustColumn_0" >否</div>
										</div>
							 		</li>
						 		</fieldset>
							 	</c:otherwise>
							 </c:choose>
							</ul> 
						<!-- </div> 
					</div> -->
				</div>
				<div class="customer-bottom-content clearfix">
					<div class="tagSaveBtnBox">
						<input id="saveBtn22" name="" type="button" value="下一步" />   
						<a href="javascript:void(0);" onclick="showLabelConfig(4);return false;" class="prevBtn fright" id="prevBtn">返回标签配置</a>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
<div id="menuContent" class="zTreeDemoBackground"  style="display:none; position: absolute;" >
	<div class="ztreeBox" id="ztreeBox" >
	    	<ul id="labelTree" class="ztree selectCheckboxZtree mScrool fleft" ></ul>
    </div>
</div>
<input name="pojo.columnListSize" type="hidden" value="${pojo.columnListSize }" id="columnListSize"/>
</body>
</html>