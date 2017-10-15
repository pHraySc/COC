<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU");
String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>创建客户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
var	validater;
var _submitFlag=true;
var setting = {
		data: {
			key: {
				name:"labelName"
			},
			simpleData: {
				enable: true,
				idKey:"labelId",
				pIdKey:"parentId",
				rootPid:"-1"
			}
		},
        callback: {
        	onClick: zTreeOnClick
        }
	};
var zTree;
var zNodes = "" ;
$(function(){
	<% if (!"true".equalsIgnoreCase(templateMenu)) { %>
	$("#saveCustomerFrame",parent.document.body).height(539);
	<% } %>
	 //展开操作 
	   
	$("#unfoldAction").click(function(){
		$("#foldAction").show();
        $(this).hide();
        $(".clientMsgBox").hide(); 
        $(".btnActiveBox").hide();
        $("#attrList_a").css("height","480px");
        if($("#labelListBox li").length == 0){
            $("#labelListBox").css("height","auto");
		}else{
            $("#labelListBox").css("height","100px");
		}
        $("#labelListBox").css("max-height","100px");
        $("#customAttrListTd").css("height","200px");
        $("#labelListTd").css("height","200px");
        parent.$("#saveCustomerDialog").prev(".ui-widget-header ").hide();
        $("#customAttrListTd").mCustomScrollbar("update");
        $("#labelListTd").mCustomScrollbar("update");
        $("#labelListBox").mCustomScrollbar("update");
    });
   	//折叠操作
    $("#foldAction").click(function(){
        $("#unfoldAction").show();
        $(this).hide();
        $(".clientMsgBox").show(); 
        $(".btnActiveBox").show();
        $("#attrList_a").css("height","308px");
        if($("#labelListBox li").length == 0){
            $("#labelListBox").css("height","auto");
		}else{
            $("#labelListBox").css("height","65px");
		}
         $("#labelListBox").css("max-height","65px");
        $("#customAttrListTd").css("height","80px");
        $("#labelListTd").css("height","80px");
         parent.$("#saveCustomerDialog").prev(".ui-widget-header ").show();
       //设置高度 
     	setCheckBoxHeight();
     	$("#customAttrListTd").mCustomScrollbar("update");
        $("#labelListTd").mCustomScrollbar("update");
        $("#labelListBox").mCustomScrollbar("update");
    });
    
	var scenePublicId="<%=ServiceConstants.ALL_SCENE_ID%>";
	validater = $("#saveForm").validate({
		rules: {
			"ciCustomGroupInfo.customGroupName": {
				required: true
			}/*,
			 "ciCustomGroupInfo.startDate": {
				required: true
			}, 
			"endDateByMonth": {
				required: true
			}*/
		},
		messages: {
			"ciCustomGroupInfo.customGroupName": {
				required: "客户群名称不允许为空！"
			}/*,
			"ciCustomGroupInfo.startDate": {
				required: "有效期开始时间不能为空！"
			}, 
			"endDateByMonth": {
				required: "有效期结束时间不能为空！"
			}*/
		},
		errorPlacement:function(error,element) {
			element.next("div").hide();
			element.after(error);
		},
		success:function(element){
			element.remove();
		},
		errorElement: "div",
		errorClass: "tishi error"
	});
	
	//是否存为模板
    $(".selectBox").click(function(){
	   $(".selectBox").show();
	   $(this).hide();
	   if($(this).hasClass("handFillDate")){
	       //$(".tipMsg").hide();
		   $("#template_name").hide();
		   $("#isSaveTemplate").val("");
	   }else{
	       //$(".tipMsg").show();
		   $("#template_name").show();
		   $("#isSaveTemplate").val("1");
		   var cname = $("#custom_name").val();
			if(cname != null && cname != "" && cname != "请输入客户群名称"){
				$("#template_name").val(cname + "-模板");
			}else{
				$("#template_name").val("请输入模板名称");
			}
			var name = $.trim($("#template_name").val());
			if(name == "请输入模板名称"){
				$.focusblur("#template_name");
			}
			$("#template_name").rules("add", {
				required: true,
				messages: {
					required: "模板名称不允许为空!"}
			});
	   }
	});
	
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入客户群名称"){
		$.focusblur("#custom_name");
	}
	var queryLabelParamInputValue = $.trim($("#queryLabelParamInput").val());
	if(queryLabelParamInputValue == "模糊查询"){
		$.focusblur("#queryLabelParamInput");
	}
	var queryAttrParamInputValue = $.trim($("#queryAttrParamInput").val());
	if(queryAttrParamInputValue == "模糊查询"){
		$.focusblur("#queryAttrParamInput");
	}
	
	var template_name=$.trim($("#template_name").val());
	if(template_name == "请输入模板名称"){
		$.focusblur("#template_name");
	}
	
	var cycle = $("#updateCycle",parent.document).val();
	if(cycle == null || cycle ==""){
		cycle = 1;
	}
	$("#createPeriod").val(cycle);
	
	if(cycle == 1){
		$("#period_monthly").hide();
		$("#period_daily").hide();
	}

	var cid = $.trim($("#customGroupId", parent.document).val());
	//生成周期
	$("#createPeriodBox div").bind('click', changeCycle);
	
	$('#endDateByMonth').bind('click', function() {
		var minDate = '#F{$dp.$D(\'startDateByMonth\',{M:1})}';
		if(cid != null && cid != "") {
			minDate = '%y-{%M+1}';
		}
		WdatePicker({dateFmt:'yyyy-MM', minDate:minDate})
	});
	$('#endDateByDay').bind('click', function() {
		var minDate = '#F{$dp.$D(\'startDateByDay\',{d:1})}';
		if(cid != null && cid != "") {
			minDate = '%y-%M-{%d+1}';
		}
		WdatePicker({dateFmt:'yyyy-MM-dd', minDate: minDate})
	});

	
	if(cid != null && cid != ""){
		var cycleStr = "";
		if(cycle == 1){
			cycleStr="一次性生成";
		}else if(cycle == 2){
			cycleStr="按月生成";
		}else if(cycle == 3){
			cycleStr="按日生成";
		}
		var inputForCycle = '<input type="text" value="' + cycleStr + '" readonly ' + 
			'class="new_tag_input" style="width:302px;*width:306px;cursor:text"/>' + 
			'<input name="ciCustomGroupInfo.updateCycle" id="cycle" type="hidden" value="" ' + 
			'class="new_tag_input"/>';
		$("#selectCycle").after(inputForCycle).remove();
		$("#cycle").val(cycle);
	}else{
		var d = $("#dataDate", parent.document).val();
		if(d != null && d!= ""){
			var _year = d.substring(0,4);
			var _month = d.substring(5,7);
			$("#startDateHidden").val(_year+"-"+_month+"-01");
			$("#startDateByMonth").val(_year+"-"+_month);
		}
	}
	$("#startDateHidden").attr("disabled", "true");
	$("#startDateByMonth").attr("disabled", "true");
	$("#startDateByDay").attr("disabled", "true");
	//模糊查询
	$('#queryLabel').click(function() {
		queryLabelOrCustomAttrList();
	});

	//设置高度 
	setCheckBoxHeight();

	$(window).load(function(){
		//标签属性滚动条
		$("#labelListTd").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			mouseWheel:{
				preventDefault:true //阻止冒泡事件
			}
		});
			//客户群清单属性滚动条
		$("#customAttrListTd").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			mouseWheel:{
				preventDefault:true //阻止冒泡事件
			}
		});
			//选中的属性滚动条
		$("#labelListBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			mouseWheel:{
				preventDefault:true //阻止冒泡事件
			}
		});
	});
	//模拟下拉框--打开方式 
    $(".selBtn").click(function(){
    	var open = $("#selListBox").attr("open");
    	if(!open){
    		$("#snameTip").hide();
    	    $("#selListBox").slideDown().attr("open",true);
    	    $(this).addClass("selBtnActive");
    	}else{
   		  $(this).next("#selListBox").slideUp().removeAttr("open");
   	       $(this).removeClass("selBtnActive");
    	}
    	return false;
    });
    $("#scenesTxt").click(function(){
    	var open = $("#selListBox").attr("open");
    	if(!open){
    		$("#snameTip").hide();
        	$("#selListBox").slideDown().attr("open",true);
        	$("#selBtn").addClass("selBtnActive");
    	}else{
    	    $("#selListBox").slideUp().removeAttr("open");
            $("#selBtn").removeClass("selBtnActive");
    	}
    	return false;
    });
// 	$("#scenesTxt").toggle(function(){
// 		$("#snameTip").hide();
//     	$("#selListBox").slideDown();
//     	$("#selBtn").addClass("selBtnActive");
//     },function(){
//         $("#selListBox").slideUp().removeAttr("open");
//         $("#selBtn").removeClass("selBtnActive");
//     })
  //绑定隐藏提示层
	$(document).click(function(e){
	    var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
		$("#selListBox").slideUp().removeAttr("open");
		$(".selBtn").removeClass("selBtnActive");
// 		return false;
	});
	//阻止事件冒泡
	$(".firstItem").click(function(ev){
		firstItemFn($("#firstItem_active"),'sceneIds' );
		var e = ev||event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	$(".selListBox li a").click(function(ev){
		if($(this).find("input").attr("checked")){
			$(this).find("input").attr("checked",false)
		}else{
			$(this).find("input").attr("checked",true)
		}
		selItemFn($(this).find("input"),'sceneIds' );
		var e = ev || event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	
	$(".selListBox li a input").click(function(ev){
		selItemFn($(this),'sceneIds' );
		var e = ev || event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})

	$(".selAllBox").click(function(ev){
		if($("#selBox_all").attr("checked")){
			$("#selBox_all").attr("checked",false)
		}else{
			$("#selBox_all").attr("checked",true)
		}
		selItemAllFn($("#selBox_all"),'sceneIds' );
		var e = ev || event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	$(".selAllBox input").click(function(ev){
		selItemAllFn($("#selBox_all"),'sceneIds' );
		var e = ev || event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	//是否共享
    $(".is_private").click(function(){
    	var customGroupId = $("#custom_id").val();
   		if(customGroupId) {
   			var isPrivate = $("#is_private_source").val();
   			var dataStatus = $("#data_status").val();
   			if(isPrivate == 1) {
   				if(dataStatus != 3) {
   					parent.showAlert('不能共享状态为不成功的客户群！', 'failed');
   					return;
   				}
   			} else if(isPrivate == 0) {
   				var actionUrl=$.ctx + "/ci/customersManagerAction!findIfShare.ai2do"
   				var para={
   						"ciCustomGroupInfo.customGroupId":customGroupId
   					};
   				var responseText = $.ajax({
   					url : actionUrl,
   					data : 'ciCustomGroupInfo.customGroupId=' +customGroupId,
   					async: false
   				}).responseText;
   				var resultObj = eval('(' + responseText + ')');
   				if(!resultObj.success) {
   					parent.showAlert(resultObj.msg, 'failed');
   					return;
   				}
   			}
   		}
		
        $(".is_private").children("input")[0].checked=false;
        $(".is_private").show();
        $(this).hide();
        $(".is_private:visible").find("input")[0].checked=true;
        var _valTemp = $.trim($(".is_private:visible").find("input").val());
        $("#is_private").val(_valTemp);
    })
	//是否包含清单
	$(".is_hasList").click(function(){
		var customGroupId = $("#custom_id").val();
   		var isHasList = $('#is_hasList_source').val();
   		var oldIsHasList = $('#oldIsHasList').val();
   		if(customGroupId && isHasList == 1) {//不包含清单的客户群可以修改生成周期
   			parent.showAlert("不可将已包含清单的客户群改为不包含清单的客户群！", "failed");
   			return;
   		}
   		 
        $(".is_hasList").children("input")[0].checked=false;
        $(".is_hasList").show();
        $(this).hide();
        $(".is_hasList:visible").find("input")[0].checked=true;
        var _valTemp = $.trim($(".is_hasList:visible").find("input").val());
        $("#is_hasList").val(_valTemp);
        if(_valTemp == 0){
        	$("#attrList_a").hide();
        	//清空属性
        	$("#attrList_a input[type=checkbox]").each(function() {
				this.checked = false;
			});
        	$("#labelListBox ol").empty();
        	$("#labelListBox").css("height","auto");
        	$("#parentName").val("属性分类");
        	 
        	$("#createPeriodBox_li").hide();
        	$("#period_monthly").hide();
     		$("#period_daily").hide();
     		$("#sysRecommendTimeLi").hide();
     		var isSysRecommendTime = $("#isSysRecommendTime").val();
     			//否的情况下
   			if(isSysRecommendTime == '0'){
   				$("#sysRecommendTimeYes").show();
   				$("#sysRecommendTimeNot").hide();
   				$("#isSysRecommendTime").val("1");
   				$("#monthTimeDiv").hide();
   				$("#dayTimeDiv").hide();
   			}
   			 window.parent.$("#saveCustomerFrame").height(window.parent.$("#saveCustomerFrame").height() - 278);
			 $("body").css({"background":"#f4f9fd"});
   			 var left = window.screen.width/2-window.parent.$("#saveCustomerFrame").width()/2;
			 var top = 135;//(window.screen.height-window.screenTop)/2 - window.parent.$("#saveCustomerFrame").height()/2; 
			 window.parent.$("#saveCustomerDialog").parent().css({'left':left,'top':top})
         }else{
        	 $("#attrList_a").show();
        	 $("#createPeriodBox_li").show();
        	 var updateCycle = $("#createPeriod").val();
        	 if(updateCycle == 2){
        		 $("#period_monthly").show();
        		 $("#sysRecommendTimeLi").show();
        	 }
        	 if(updateCycle == 3){
        		 $("#period_daily").show();
        		 $("#sysRecommendTimeLi").show();
        	 }
     	     window.parent.$("#saveCustomerFrame").height(window.parent.$("#saveCustomerFrame").height() + 278);
     	    $("body").css({"background":"#fff"});
     		 var left = window.screen.width/2-window.parent.$("#saveCustomerFrame").width()/2;
			 var top = 10; 
   			 window.parent.$("#saveCustomerDialog").parent().css({'left':left,'top':top});
   			 var customGroupId = $("#custom_id").val();
   			 var oldIsHasList = $('#oldIsHasList').val();
        	 if(customGroupId != null && customGroupId != "" && oldIsHasList != null && oldIsHasList != "" && oldIsHasList == 0){
   				 //由模板客户群修改为含清单的客户群时  默认给客户群赋周期性 一次性
        		 if($("#createPeriod").val() == null || $("#createPeriod").val() == "4" || $("#createPeriod").val() == ""){
	        		 $("#createPeriod").val(1);
        		 }
        	 }
         }
    });
    //是否系统推荐时间
	$(".sysRecommendTime").click(function(){
		$(".sysRecommendTime").show();
	   	$(this).hide();
	   	if($(this).hasClass("isSysRecommendTime")){
			var createPeriod =$("#createPeriod").val();
			if(createPeriod == '2'){
				$("#monthTimeDiv").show();
				$("#monthCycleTime").rules("add", {
					required: true,
					messages: {
					required: "清单生成时间不能为空!"}
				});
			}
			if(createPeriod == '3'){
				$("#dayTimeDiv").show();
				$("#dayCycleTime").rules("add", {
					required: true,
					messages: {
					required: "清单生成时间不能为空!"}
				});
			}
			$("#isSysRecommendTime").val("0");
		}else{
			$("#dayTimeDiv").hide();
			$("#monthTimeDiv").hide();
			$("#isSysRecommendTime").val("1");
		}
	});
  
	//测试标签树json串
	initTree();
	$("#parentName").bind("click",{id:"parentName"},showMenu);
	
});

jQuery.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		$(this).css("color","#323232");
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).css("color","#8e8e8e");
			$(this).val(defval);
		}
	});
};
//获得指定年月最后一天日期
function getLastDay(year,month){        
	var new_year = year;    //取当前的年份         
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）         
	if(month>12){            //如果当前大于12月，则年份转到下一年         
		new_month -=12;        //月份减         
		new_year++;            //年份增         
	}        
	var new_date = new Date(new_year,new_month,1);                //取当年当月中的第一天         
	return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期         
}

function changeCycle(){
	var customGroupId = $("#custom_id").val();
	var isHasListSource = $('#is_hasList_source').val();
	if(customGroupId && isHasListSource == 1) {//不包含清单的客户群可以修改生成周期
		parent.showAlert("不可以修改客户群生成周期！", "failed");
		return;
	}
    var index = $(this).index();
    var flagConstraint = '${flagConstraint}';
    if('false' == flagConstraint){
    	index = index + 1;
    }
    $("#createPeriodBox div").removeClass();
	//切换分割线
    if(index == 1){
		$("#createPeriodBox").find("div").eq(0).addClass("periodSegLine");
	  	$("#createPeriod").val(3);
	  	$("#period_daily").show();
	  	$("#period_monthly").hide();
	  	if(window.parent.$("#saveCustomerFrame").height()<574){
		  	window.parent.$("#saveCustomerFrame").height(window.parent.$("#saveCustomerFrame").height() + 35);
		}
	  	var d = $("#dayDataDateHidden", parent.document).val();
		if(d != null && d!= ""){
			var _year = d.substring(0,4);
			var _month = d.substring(4,6);
			var _day = d.substring(6,8);
			$("#startDateHidden").val(_year+"-"+_month+"-"+_day);
			$("#startDateByDay").val(_year+"-"+_month+"-"+_day);
		}
		$("#sysRecommendTimeLi").show();
		var isSysRecommendTime = $("#isSysRecommendTime").val();
		//否的情况下
		if(isSysRecommendTime == '0'){
			$("#sysRecommendTimeYes").show();
			$("#sysRecommendTimeNot").hide();
			$("#isSysRecommendTime").val("1");
			$("#monthTimeDiv").hide();
			$("#dayTimeDiv").hide();
		}
		//日客户群
		$("#dataDateHidden",parent.document).val($("#dayDataDateHidden",parent.document).val());
	}else if(index == 2){
		$("#createPeriodBox").find("div").eq(1).addClass("periodSegLine");
	  	$("#createPeriod").val(2);
	  	$("#period_monthly").show();
	  	$("#period_daily").hide();
	  	if(window.parent.$("#saveCustomerFrame").height()<574){
		  	window.parent.$("#saveCustomerFrame").height(window.parent.$("#saveCustomerFrame").height() + 35);
		}
	  	var d = $("#dataDate", parent.document).val();
		if(d != null && d!= ""){
			var _year = d.substring(0,4);
			var _month = d.substring(5,7);
			$("#startDateHidden").val(_year+"-"+_month+"-01");
			$("#startDateByMonth").val(_year+"-"+_month);
		}
		$("#sysRecommendTimeLi").show();
		var isSysRecommendTime = $("#isSysRecommendTime").val();
		//否的情况下
		if(isSysRecommendTime == '0'){
			$("#sysRecommendTimeYes").show();
			$("#sysRecommendTimeNot").hide();
			$("#isSysRecommendTime").val("1");
			$("#monthTimeDiv").hide();
			$("#dayTimeDiv").hide();
		}
		//月客户群
		$("#dataDateHidden",parent.document).val($("#monthLabelDateHidden",parent.document).val());
	}else if(index == 3){
		$("#createPeriodBox ").find("div").eq(2).addClass("periodSegLine");
	  	$("#createPeriod").val(1);
	  	//$("#period").hide();
	  	$("#period_monthly").hide();
	  	$("#period_daily").hide();
	  	if(window.parent.$("#saveCustomerFrame").height()>539){
			window.parent.$("#saveCustomerFrame").height(window.parent.$("#saveCustomerFrame").height() - 35);
		}
	 	$("#sysRecommendTimeLi").hide();
	  	var isSysRecommendTime = $("#isSysRecommendTime").val();
		//否的情况下
		if(isSysRecommendTime == '0'){
			$("#sysRecommendTimeYes").show();
			$("#sysRecommendTimeNot").hide();
			$("#isSysRecommendTime").val("1");
			$("#monthTimeDiv").hide();
			$("#dayTimeDiv").hide();
		}
		//一次性客户群
		$("#dataDateHidden",parent.document).val($("#monthLabelDateHidden",parent.document).val());
	} 
	$(this).addClass("current");
}

//保存
function submitList(){
	_submitFlag=false;
	if(validateForm()){
		//加上遮罩层
		showLoading('正在保存客户群，请耐心等候...');
		
		var actionUrl = $.ctx;
		if($("#custom_id").val() != null && $("#custom_id").val() != ""){
			actionUrl += "/ci/customersManagerAction!editCustomer.ai2do";
		}else if($("#custom_id").val() == null || $("#custom_id").val() == ""){
			actionUrl += "/ci/customersManagerAction!saveCustomer.ai2do";
			//actionUrl += "/ci/customersManagerAction!saveCustomerByCustomerCalcOnly.ai2do";
		}
		var customGroupId = $("#custom_id").val();
		var oldIsHasList = $('#oldIsHasList').val();
		if(!$("#period_monthly").is(":hidden")){
			$("#startDateHidden").removeAttr("disabled");
			var ym = $("#endDateByMonth").val();
			var year = ym.substring(0,4);
	        var month = ym.substring(5,7);
		    if(month.length == 2 && month.substring(0,1)=='0'){
		        month = month.substring(1);
		    }
		    var d = getLastDay(year,month);
		    $("#endDateHidden").val($("#endDateByMonth").val()+"-"+d);
			if(customGroupId != null && customGroupId != "" && oldIsHasList != null && oldIsHasList != "" && oldIsHasList == 0){
		   		//由模板客户群修改为含清单的客户群时  给有效期的开始时间赋值
		   		var ym = $("#startDateByMonth").val();
				var year = ym.substring(0,4);
		        var month = ym.substring(5,7);
			    if(month.length == 2 && month.substring(0,1)=='0'){
			        month = month.substring(1);
			    }
				$("#startDateHidden").val(year+"-"+month+"-01");
		   	}
		}
		if(!$("#period_daily").is(":hidden")){
			$("#startDateHidden").removeAttr("disabled");
			var ym = $("#endDateByDay").val();
			var year = ym.substring(0,4);
	        var month = ym.substring(5,7);
		    if(month.length == 2 && month.substring(0,1)=='0'){
		        month = month.substring(1);
		    }
		    var d = getLastDay(year,month);
			$("#endDateHidden").val($("#endDateByDay").val());
		   	if(customGroupId != null && customGroupId != "" && oldIsHasList != null && oldIsHasList != "" && oldIsHasList == 0){
		   		//由模板客户群修改为含清单的客户群时  给有效期的开始时间赋值
		   		$("#startDateHidden").val($("#startDateByDay").val());
		   	}
		}

		var isSysRecommendTime =$("#isSysRecommendTime").val();
		if(isSysRecommendTime == '1'){
			$("#listCreateTime").val("");
		}
		$.post(actionUrl, $("#labelForm",parent.document).serialize()+"&"+$("#saveForm").serialize(), function(result){
			if(result.success){
				var customerName = $("#custom_name").val();
				var templateName = "";
				if($("#isSaveTemplate").val() == "1"){
					templateName = $("#template_name").val();
				}
				var customGroupId = result.customGroupId;
				var isHasList = result.isHasList;
				parent.openSuccess(customGroupId, customerName, templateName, isHasList);
				parent.iframeCloseDialog("#saveCustomerDialog");
				//parent.closeCreate();
				parent.$("#customGroupId").val("");
			}else{
				if($("#isSaveTemplate").val() == "1"){
					var msg = result.msg;
					var cmsg = result.cmsg;
					if(msg.indexOf("重名") >= 0){
						$("#templateNameTip").empty();
						$("#templateNameTip").show().append(msg);
					}
					if(cmsg.indexOf("重名") >= 0){
						$("#customNameTip").empty();
						$("#customNameTip").show().append(cmsg);
					}
					if(cmsg.indexOf("重名") < 0 && msg.indexOf("重名") < 0){
						parent.showAlert(cmsg,"failed");
					}
				}else{
					var cmsg = result.cmsg;
					if(cmsg.indexOf("重名") >= 0){
						$("#customNameTip").empty();
						$("#customNameTip").show().append(cmsg);
					}else{
						parent.showAlert(cmsg,"failed");
					}
				}
			}
			
			//去掉遮罩层
			closeLoading();
		});
		
	}
	_submitFlag=true;
}
//避免多次点击保存
function submitListInit(){
	if(_submitFlag){
		submitList();
	}
}
//表单验证
function validateForm(){
	if($("#isSaveTemplate").val() == "1"){
		var name = $.trim($("#template_name").val());
		if(name == "请输入模板名称"){
			$("#template_name").val("");
			name="";
		}else{
			$("#template_name").val(name);
		}
	}
	if($.trim($("#sceneIds").val()) == "") {
		$("#snameTip").empty();
		$("#snameTip").show().append("客户群分类不能为空");
		return false;
	}
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入客户群名称"){
		$("#custom_name").val("");
		cname ="";
	}else{
		$("#custom_name").val(cname);
	}
	
	if($("#isAutoMatch").is(":checked")){
		$("#isAutoMatch").val(1);
	}else{
		$("#isAutoMatch").val(0);
	}

	if($.trim($("#createPeriod").val()) == "3") {
		if(!$("#period_daily").is(":hidden")){
			$("#endDateByDay").rules("add", {
				required: true,
				messages: {
					required: "有效结束时间不能为空!"
				}
			});
		}
		//校验日客户群数start-----
		var customGroupId = $("#custom_id").val();
		var flagEndDate = false;
		var flagIsHasList = false;
		var oldEndDateStr = $("#oldEndDate").val();
		var endDateStr = $("#endDateByDay").val();
		var oldEndDate = new Date(oldEndDateStr.replace("-", "/").replace("-", "/"));  
		var endDate = new Date(endDateStr.replace("-", "/").replace("-", "/"));  
		if(oldEndDate<endDate && customGroupId != null && customGroupId != ""){
			flagEndDate = true;
		}
		var oldIsHasList = $("#oldIsHasList").val();
		var isHasList = $("#is_hasList").val();
		if(oldIsHasList == 0 && isHasList == 1 && (customGroupId != null || customGroupId != "")){
			flagIsHasList = true;
		}
		//1、新建客户群时校验
		//2、修改客户群日周期客户群修改为更大的终止日时校验
		//3、修改客户群无清单到有清单时校验
   		if((customGroupId == null || customGroupId == "") || flagEndDate || flagIsHasList) {
			var checkDailyFlag;
			var actionUrl = $.ctx + "/ci/customersManagerAction!checkIfDailyCustomGroupCanCreate.ai2do";
			$.ajax({
				type: "POST",
				url: actionUrl,
				async:false,
				success: function(result){
					if(result.success){
						checkDailyFlag = true;
					}else{
						checkDailyFlag = false;
					}
				}
			});
			if(!checkDailyFlag){
				$("#dailyTip").empty();
				$("#dailyTip").show().append("日客户群数超过规定，请联系管理员");
				return false;
			}
   		}
   		//校验日客户群数end-----
	}else if($.trim($("#createPeriod").val()) == "2"){
		if(!$("#period_monthly").is(":hidden")){
			$("#endDateByMonth").rules("add", {
				required: true,
				messages: {
					required: "有效结束时间不能为空!"}
			});
		}
	}
	if(!validater.form()){
		return false;
	}
	//属性个数的验证
	var i = 0;
	$("#labelListTdTwo").find("input[name^='ciLabelInfoList']").each(function(index ,item){
		if($(item).attr("checked")){
			i++ ; 
		}
	});
	$("#customAttrListTdTwo").find("input[name^='ciGroupAttrRelList']").each(function(index ,item){
		if($(item).attr("checked")){
			i++ ; 
		}
	});
	if(i > $._maxAttrNum){
		parent.showAlert("所选属性应小于  "+$._maxAttrNum+" 个","failed");
		return false;
	}
	return true;
}

//选择标签属性
function checkLabelInfo(obj){
	$("#labelListTdTwo").find("input[id='two_"+$(obj).attr("id")+"']").click();
	setLabelAttrListText(obj);
	//alert($("#labelListTdTwo").find("input[id='two_"+$(obj).attr("id")+"']").attr("checked"));
}

//选择客户群属性
function checkAttrInfo(obj){
	$("#customAttrListTdTwo").find("input[id='two_"+$(obj).attr("id")+"']").click();
	setLabelAttrListText(obj);
}
//标签属性
function setLabelAttrListText(ths){
	if($("#foldAction").is(":hidden")){
		$("#labelListBox").height(65);
	}else{
        $("#labelListBox").height(100);
	}
	//选择客户群列表请选项
	var checked = $(ths).attr("checked");
	var selfId = $(ths).attr("id");
	var labelTxt = $(ths).siblings("label").text();
	if(checked){//勾选复选框
		var $Li = $("<li></li>");
		var $clientSelectItem = $("<div class=\"clientSelectItem\"  checkedboxId = "+selfId+"> </div>");
		var  $clientSelectItemText =$("<span class=\"clientSelectItemText\">"+labelTxt+"</span>");
		var  $clientSelectItemClose =$("<a href=\"javascript:void(0);\" class=\"clientSelectItemClose\"></a>");
		$clientSelectItem.append($clientSelectItemText).append($clientSelectItemClose);
		//绑定单击事件
		$clientSelectItem.find(".clientSelectItemClose").click(function(){
			var checkedboxId = $(this).parent().attr("checkedboxId");
			$("#" + checkedboxId).attr("checked",false);
			$("#two_" + checkedboxId).attr("checked",false);
			$(this).parents("li").remove();
			if($("#labelListBox li").length == 0){
              $("#labelListBox").css("height","auto");
			}
		})
		$("#labelListBox ol").append($Li.append($clientSelectItem));
	}else{
		$("#labelListBox .clientSelectItem").each(function(index ,item){
			var checkedboxId = $(item).attr("checkedboxId");
			if(selfId == checkedboxId){
				$(item).parent("li").remove();
			}
		})
		if($("#labelListBox li").length==0){
			$("#labelListBox").css("height","auto");
		}
		
	}
}
//客户群属性
function setCustomAttrListText(ths){
	//选择客户群列表请选项
	var checked = $(ths).attr("checked");
	var selfId = $(ths).attr("id");
	var labelTxt = $(ths).siblings("label").text();
	if(checked){//勾选复选框
		var $Li = $("<li></li>");
		var $clientSelectItem = $("<div class=\"clientSelectItem\"  checkedboxId = "+selfId+"> </div>");
		var  $clientSelectItemText =$("<span class=\"clientSelectItemText\">"+labelTxt+"</span>");
		var  $clientSelectItemClose =$("<a href=\"javascript:void(0);\" class=\"clientSelectItemClose\"></a>");
		$clientSelectItem.append($clientSelectItemText).append($clientSelectItemClose);
		//绑定单击事件
		$clientSelectItem.find(".clientSelectItemClose").click(function(){
			var checkedboxId = $(this).parent().attr("checkedboxId");
			$("#" + checkedboxId).attr("checked",false);
			$("#two_" + checkedboxId).attr("checked",false);
			$(this).parent().remove();
		})
		$("#customAttrListBox").append($Li.append($clientSelectItem));
	}else{
		$("#customAttrListBox .clientSelectItem").each(function(index ,item){
			var checkedboxId = $(item).attr("checkedboxId");
			if(selfId == checkedboxId){
				$(item).parent("li").remove();
			}
		})
	}
}
//设置复选框BOX 高度 
function setCheckBoxHeight(){
	if($("#customAttrListTd").length >0){
		 $("#labelListBox").css("maxHeight","65px");
	     $("#labelListTd").height(93);
	     $("#customAttrListTd").height(80);
   }else{
     $("#labelListBox").css("maxHeight","100px");
     $("#labelListTd").height(175);
   }
	
}
//多选下拉框全选
function selItemAllFn(obj,selId){
	$("#firstItem_active").attr("checked",false);
	var $obj =$(obj);
	//var $inputArr = $obj.parent().prev("ol").find("input");
	var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	//var flag = obj.checked;
	var _id =obj.attr("id");
	var flag = document.getElementById(_id).checked;
	var valArr = [];
	var textArr = [];
	$.each($inputArr,function(index ,item){
		$(item).attr("checked",flag);
		valArr.push($(item).val());
		var $labelText = $.trim($(item).attr("_sceneName"));
		//alert($labelText+"*");
		textArr.push($labelText);
	});
	if(flag){
		$("#" +selId ).val(valArr.join(","));
		$("#scenesTxt").html(textArr.join(","));
		$("#scenesTxt").attr("title",textArr.join(","));
	}else{
		$("#" +selId ).val("");
		$("#scenesTxt").html("");
		$("#scenesTxt").attr("title","");
	}
}
//第一个参数
function firstItemFn(obj,selId){
	//var $inputArr = $(obj).parent().next("ol").find("input");
	var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	$.each($inputArr,function(index ,item){
		$(item).attr("checked",false);
	})
	$("#selBox_all").attr("checked",false);
	if($(obj).attr("checked")){
		var itemVal =  $(obj).val();
		var optionArr =$("#"+selId).val(itemVal);
		var $labelText = $.trim($(obj).attr("_sceneName"));
		$("#scenesTxt").html($labelText);
		$("#scenesTxt").attr("title",$labelText);
	}else{
		$("#"+selId).val("");
		$("#scenesTxt").html("");
		$("#scenesTxt").attr("title","");
	}
}
function selItemFn(obj,selId){
	//全选和第一复选框不选中
	$("#firstItem_active").attr("checked",false);
	$("#selBox_all").attr("checked",false);
	//去掉第一个参数 
	//var $inputArr  = $(obj).parents("ol").find("input");
	var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	var valArr = []; 
	var textArr = [];
	$.each($inputArr,function(index ,item){
		if($(item).attr("checked")){
			valArr.push($(item).val());
			var $labelText = $.trim($(item).attr("_sceneName"));
			textArr.push($labelText);
		}
	})
	$("#"+selId).val(valArr.join(","));
	$("#scenesTxt").html(textArr.join(","));
	$("#scenesTxt").attr("title",textArr.join(","));
}

function setLabelAttrListTwoText(ths){
	if($("#foldAction").is(":hidden")){
			$("#labelListBox").height(65);
	}else{
        $("#labelListBox").height(100);
	}
	//选择客户群列表请选项
	var checked = $(ths).attr("checked");
	var selfIdTemp = $(ths).attr("id");
	var selfId = selfIdTemp.substring(4,selfIdTemp.length);
	var labelTxt = $(ths).siblings("label").text();
	if(checked){//勾选复选框
		var $Li = $("<li></li>");
		var $clientSelectItem = $("<div class=\"clientSelectItem\"  checkedboxId = "+selfId+"> </div>");
		var  $clientSelectItemText =$("<span class=\"clientSelectItemText\">"+labelTxt+"</span>");
		var  $clientSelectItemClose =$("<a href=\"javascript:void(0);\" class=\"clientSelectItemClose\"></a>");
		$clientSelectItem.append($clientSelectItemText).append($clientSelectItemClose);
		//绑定单击事件
		$clientSelectItem.find(".clientSelectItemClose").click(function(){
			var checkedboxId = $(this).parent().attr("checkedboxId");
			$("#" + checkedboxId).attr("checked",false);
			$("#two_" + checkedboxId).attr("checked",false);
			$(this).parents("li").remove();
			if($("#labelListBox li").length == 0){
              $("#labelListBox").css("height","auto");
			}
		})
		$("#labelListBox ol").append($Li.append($clientSelectItem));
	}else{
		$("#labelListBox .clientSelectItem").each(function(index ,item){
			var checkedboxId = $(item).attr("checkedboxId");
			if(selfId == checkedboxId){
				$(item).parent("li").remove();
			}
		})
		if($("#labelListBox li").length==0){
			$("#labelListBox").css("height","auto");
		}
		
	}
}
function selectedAutoMatch(){
	var checkedVal =$("#sysAutoMatch").attr("checked");
	var queryLabelUrl = $.ctx + "/ci/customersManagerAction!findCiLabelInfoListBySys.ai2do";
	$.post(queryLabelUrl, function(response) {
		var tag = '';
		for(var index=0; index<response.length; index++) {
			var columnId = response[index].columnId;
			var key = response[index].labelId;
			
			var keyTemp ="";
			if(columnId != null && columnId != ""){
				keyTemp = key+'_'+columnId;
			}else{
				keyTemp = key;
			}
			if(checkedVal == "checked" || checkedVal == true){
				$("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked",true);
				$("#labelListTd").find("input[id='"+keyTemp+"']").attr("checked",true);
			}else{
				$("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked",false);
				$("#labelListTd").find("input[id='"+keyTemp+"']").attr("checked",false);
			}
			setLabelAttrListTwoText($("#labelListTdTwo").find("input[id='two_"+keyTemp+"']"));
		}
	});
	if(checkedVal == "checked" || checkedVal == true){
		$("#customAttrListTdTwo").find("input[type='checkbox']").attr("checked",true);
		$("#customAttrListTd").find("input[type='checkbox']").attr("checked",true);
		
	}else{
		$("#customAttrListTdTwo").find("input[type='checkbox']").attr("checked",false);
		$("#customAttrListTd").find("input[type='checkbox']").attr("checked",false);
	}
	$("#customAttrListTdTwo").find("input[type='checkbox']").each(function(){
		setLabelAttrListTwoText($(this));
	});
}

function queryLabelOrCustomAttrList(){
	var queryParam = $('#queryLabelParamInput').val();
	var parentId = $("#parentId").val();
	var level = $("#level").val();
	if(queryParam == "模糊查询"){
		queryParam = "";
	}
	var paramObj = window.parent.$("#labelForm").serialize();
	var queryLabelUrl = $.ctx + "/ci/customersManagerAction!findLabelsByLabelName.ai2do?" + paramObj;
	$.post(queryLabelUrl, {labelName : queryParam,parentId:parentId,level:level}, function(response) {
		var tag = '';
		for(var index=0; index<response.length; index++) {
			var key = response[index].labelId;
			var value = response[index].labelName;
			
			var columnId = response[index].columnId;
			var columnCnName = response[index].columnCnName;
			
			var keyTemp ="";
			if(columnId != null && columnId != ""){
				keyTemp = key+'_'+columnId;
			}else{
				keyTemp = key;
			}
			
			var checkedVal = $("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked");
			tag += '<li>';
			tag += '<input name="ciLabelInfoListOne[' + index + '].labelId" type="checkbox"  value="';
			tag += key+'"  id="'+keyTemp+'" ';

			if(checkedVal){
				if(checkedVal == "checked" || checkedVal == true){
					tag += ' checked="' + checkedVal + '" ';
				}	
			}
			tag += ' onclick="checkLabelInfo(this);" /> ';
			tag += 	'<label title=" '+value+'" for="'+keyTemp+'"> '+value+'</label></li>';
			tag+=   '<input name="ciLabelInfoListOne[' + index + '].columnId" type="hidden"  value="'+columnId+'" />';
			tag+=   '<input name="ciLabelInfoListOne[' + index + '].columnCnName" type="hidden"  value="'+columnCnName+'" />';
		}
		$('#labelListTd ol').empty().append(tag).mCustomScrollbar("update");
	});
	var queryCustomAttrUrl = $.ctx + "/ci/customersManagerAction!findCustomAttrByColumnName.ai2do?" + paramObj;
	$.post(queryCustomAttrUrl, {labelName : queryParam}, function(response) {
		var tag = '';
		for(var i=0; i<response.length; i++) {
			var item = response[i];
			var checkedVal = $("#customAttrListTdTwo").find("input[id='two_"+item.customId +'_'+item.columnName+"']").attr("checked");
			
			tag += '<li>';
			tag += '<input name="ciGroupAttrRelListOne[' + i + '].id.attrCol"';
			tag += 'type="checkbox" id="' +item.customId +'_'+ item.columnName + '"  value="' + item.columnName + '" ';
			if(checkedVal){
				if(checkedVal == "checked" || checkedVal == true){
					tag += ' checked="' + checkedVal + '" ';
				}	
			}
			tag += ' onclick="checkAttrInfo(this);" >';
			tag += '<label title="'+item.customName + '-' + item.attrColName+'" for="' +item.customId +'_'+ item.columnName + '">';
			tag += item.customName + '-' + item.attrColName + '</label>';
			tag += '<input name="ciGroupAttrRelListOne[' + i + '].id.customGroupId" ';
			tag += 'type="hidden" value="' + item.customId + '"/>';
			tag += '<input name="ciGroupAttrRelListOne[' + i + '].attrColName" ';
			tag += 'type="hidden" value="' + item.attrColName + '"/>';
			tag += '<input name="ciGroupAttrRelListOne[' + i + '].attrColType" ';
			tag += 'type="hidden" value="' + item.attrColType + '"/>';
			tag += '<input name="ciGroupAttrRelListOne[' + i + '].labelOrCustomId"';
			tag += ' type="hidden" value="' + item.labelOrCustomId + '"/>';
			tag += '</label></li>';
		}
		$('#customAttrListTd ol').empty().append(tag).mCustomScrollbar("update");
	});
	setCheckBoxHeight();
}

function initTree(labelName){
	if(!labelName){
		labelName = "";
	}
	var actionUrl = $.ctx + "/ci/customersManagerAction!queryLabelTypeTree.ai2do";
	$.post(actionUrl,{labelNameKeyWord:labelName}, function(response) {
		var allItem = {open:true,"labelId":"-1", "parentId":0, "labelName":"全部",isParent:true,icon:"${ctx}/aibi_ci/assets/images/treetable/1_open.png"}; 
		response.unshift(allItem)
		zNodes=response;
		zTree = $.fn.zTree.init($("#labelTypetree"), setting, zNodes);
		$("#ztreeBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"yx",
			mouseWheel:{
				preventDefault:true //阻止冒泡事件
			}
		});
	})
}

function showMenu(event) {
    var id = event.data.id;
    var cityObj = $("#" + id);
    var cityOffset = $("#" + id).offset();
    $("#menuContent").css({left: cityOffset.left + "px", top: cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
    $(document).bind("mousedown", onBodyDown);
}

function onBodyDown(event) {
    if (!(event.target.id == "busiName" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}

function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $(document).unbind("mousedown", onBodyDown);
}
function zTreeOnClick(event, treeId, treeNode) {
	var tlid = treeNode.labelId === "-1" ? "" : treeNode.labelId;
	$("#parentId").val(tlid);
	$("#parentName").val(treeNode.labelName);
	var level = treeNode.level;
	$("#level").val(level);
	queryLabelOrCustomAttrList();
	hideMenu();
}

function datePicked(){
 	var d = $("#monthCycleTime").val();
 	if(d != null && d!= ""){
 		$("#listCreateTime").val(d);
	}
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input id="custom_id" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId }"/>
<input id="data_status" type="hidden" name="ciCustomGroupInfo.dataStatus" value="${ciCustomGroupInfo.dataStatus }"/>
<input id="create_city_id" type="hidden" name="ciCustomGroupInfo.createCityId" />
<input id="oldIsHasList" type="hidden" name="oldIsHasList" value="${ciCustomGroupInfo.isHasList }"/>
<input id="oldEndDate" type="hidden" name="oldEndDate" value=""/>
<div class="clientMsgBox" >
	<ol class="clearfix">
		<li>
   			<label  class="fleft labFmt100 star">客户群名称：</label>
   			<input type="text"  class="fleft inputFmt620" style="width:310px;"  name="ciCustomGroupInfo.customGroupName" type="text" id="custom_name" 
   					maxlength="35"  value="请输入客户群名称" />
   			<div id="customNameTip" class="tishi error" style="display:none;"></div>
 		</li>
	 	<% if ("true".equalsIgnoreCase(templateMenu)) { %>
	 	<li id="isSaveTemplateInfo">
	 	<% }else{ %>
	 	<li id="isSaveTemplateInfo" style="display:none;">
	 	<% } %>
	   		<label class="fleft labFmt100">存为模板：</label>
	   		<div class="fleft">
	     		<div class="selectBox" >
		   			<p class="fleft selectText">否</p>
		   		<div class="fleft selectOption">&nbsp;</div>
		 		</div>
		 		<div class="selectBox hidden  handFillDate" >
			   		<div class="fleft selectOption">&nbsp;</div>
			   			<p class="fleft selectText">是</p>
			 	</div>
			 	<input id="isSaveTemplate" type="hidden" name="isSaveTemplate" />
			 	<input type="text" class="fleft inputFmt235 hidden"  name="ciTemplateInfo.templateName"  id="template_name" maxlength="40"  value="请输入模板名称" />
		   		<div id="templateNameTip" class="tishi error" style="display:none;"></div>
	  		</div>
		</li>
  		<li>
   			<label class="fleft labFmt100 star">客户群分类：</label>
   			<div class="fleft selBox">
             	  <div class="selTxt" id="scenesTxt" style="overflow:hidden;"></div>
             	  <a href="javascript:void(0);" class="selBtn" id="selBtn"></a>
             	  <div class="selListBox" id="selListBox">
             	     <div class="firstItem">
             	  	 	 <input type="checkbox" value="<%=ServiceConstants.ALL_SCENE_ID%>"  id="firstItem_active" name="_sceneBoxItemCheckShare"_sceneName="公共场景"/>
             	  	 	 <label for="firstItem_active">公共场景</label>
             	  	 </div>
             	  	 <ol>
             	  	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
             	  	 		<c:if test="${dimScenes.sortNum > 0}">
	             	  	 	 <li>
	             	  	 	 	<a href="javascript:void(0);" >
	             	  	 	 		<input type="checkbox" id="selBoxItem0${st.index}" name="_sceneBoxItemCheckList"  value="${dimScenes.sceneId}" _sceneName="${dimScenes.sceneName}"/>
	             	  	 	 		${dimScenes.sceneName}<!--<label for="selBoxItem0${st.index}">${dimScenes.sceneName}</label>-->
	             	  	 	 	</a>
	             	  	 	 </li>
	             	  	 	 </c:if>
             	  	 	</c:forEach>
             	  	 </ol>
             	  	 <div class="selAllBox">
             	  	 	 <input type="checkbox" id="selBox_all">全选</input>
             	  	 </div>
             	  </div>
             </div>
   			<!-- 提示 -->
			<div id="snameTip" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
 		</li>
		<li>
			<label class="fleft labFmt100">是否共享：</label>
			<div class="nullItemBox isShared hidden is_private">
				<input type="radio" name="isPrivateTemp" class="hidden" value="0" />
				<p class="fleft nullItemText">是</p>
				<div class="fleft nullItemOption">&nbsp;</div>
			</div>
			<div class="nullItemBox notShared is_private">
				<input type="radio" name="isPrivateTemp" class="hidden" value="1" checked="checked" />
				<div class="fleft nullItemOption">&nbsp;</div>
				<p class="fleft nullItemText">否</p>
			</div>
            <input type="hidden" id="is_private" name="ciCustomGroupInfo.isPrivate" value="1" />
            <input type="hidden" id="is_private_source" value="1" />
            
            <label class="fleft labFmt100">是否包含清单：</label>
			<div class="nullItemBox is_hasList isHasList">
				<input type="radio" name="isHasListTemp" class="hidden" value="1" checked="checked"/>
				<p class="fleft nullItemText">是</p>
				<div class="fleft nullItemOption">&nbsp;</div>
			</div>
			<div class="nullItemBox hidden is_hasList notHasList">
				<input type="radio" name="isHasListTemp" class="hidden" value="0" />
				<div class="fleft nullItemOption">&nbsp;</div>
				<p class="fleft nullItemText">否</p>
			</div>
            <input type="hidden" id="is_hasList" name="ciCustomGroupInfo.isHasList" value="1" />
            <input type="hidden" id="is_hasList_source" value="1" />
        </li>
 		<li id="createPeriodBox_li">
		    <label class="fleft labFmt100">生成周期：</label>
			<div class="fleft createPeriodBox" id="createPeriodBox">
			   	<input type="hidden" id="createPeriod" name="ciCustomGroupInfo.updateCycle"  />
			   	<c:if test="${flagConstraint == 'true' }">
			  		<div style="display: ${flagConstraint == 'true' ? 'block' : 'none' }" flag="day">日</div>
			  	 	<p class="line-left fleft"></p> 
			   	</c:if>
			   	<div>月</div>
			   	<div class="current">一次性</div>
			</div>
			<p class="fleft tagDesc width360">客户群保存成功后，一次性生成最新数据清单. </p>
			<!-- <p class="fleft tagDesc width430">客户群保存成功后，一次性生成最新数据清单. </p> -->
	  	</li>
	  	<li id="period_monthly" class="hidden">
			<label class="fleft labFmt100">客户群有效期：</label>
			<div>
				<input name="startDateByMonth" type="text" id="startDateByMonth" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" 
					onclick="WdatePicker({dateFmt:'yyyy-MM', maxDate:'#F{$dp.$D(\'endDateByMonth\')}'})"/>
				<span style="float:left;padding-left:3px;padding-right:3px;">--</span>
				<input name="endDateByMonth" type="text" id="endDateByMonth" value='' readonly class="new_tag_input tag_date_input" />
			</div>
	  	</li>
	  	<li id="period_daily" class="hidden">
			<label class="fleft labFmt100">客户群有效期：</label>
			<div>
				<input name="startDateByDay" type="text" id="startDateByDay" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDateByDay\')}'})"/>
				<span style="float:left;padding-left:3px;padding-right:3px;">--</span>
				<input name="endDateByDay" type="text" id="endDateByDay" value='' readonly class="new_tag_input tag_date_input" 
				/>
			</div>
			<!-- 提示 -->
			<div id="dailyTip" class="tishi error"  style="display:none;"></div><!-- 日客户群数量验证提示 -->
	  	</li>
	  	<input name="ciCustomGroupInfo.startDate" type="hidden" id="startDateHidden" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>'/>
	  	<input name="ciCustomGroupInfo.endDate" type="hidden" id="endDateHidden" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>'/>
	  	
	  	<li id="sysRecommendTimeLi" class="hidden">
        	<label class="fleft labFmt100">系统推荐时间：</label>
			<div class="nullItemBox sysRecommendTime isSysRecommendTime" id="sysRecommendTimeYes">
				<p class="fleft nullItemText">是</p>
				<div class="fleft nullItemOption">&nbsp;</div>
			</div>
			<div class="nullItemBox sysRecommendTime hidden" id="sysRecommendTimeNot">
				<div class="fleft nullItemOption">&nbsp;</div>
				<p class="fleft nullItemText">否</p>
			</div>
			<input name="ciCustomGroupInfo.isSysRecommendTime" type="hidden" id="isSysRecommendTime" value="1" />
			<div class="fleft hidden" id="dayTimeDiv">
				<label class="fleft labFmt76">每日</label>
				<input type="text"  id="dayCycleTime"  class="new_tag_input tag_date_input" onclick="WdatePicker({vel:'listCreateTime',dateFmt:'HH:mm:00',disabledDates:['23\:..\:..']})"/>
			</div>
			<div  class="fleft hidden" id="monthTimeDiv">
				<label class="fleft labFmt76">每月</label>
				<input type="text"  id="monthCycleTime"  class="new_tag_input tag_date_input" onclick="WdatePicker({onpicked:datePicked,dateFmt:'dd HH:mm:ss',disabledDates:['....-..-.. 23\:..\:..','....-..-.. 23\:..\:..']})"/>
			</div>
			<input type="hidden" name="ciCustomGroupInfo.listCreateTime" id="listCreateTime"/>
        </li>
		<!--<li>
		   <label class="fleft labFmt100">清单生成日期：</label>
		<div class="createDateBox" >
		   <p class="fleft selectText">系统默认</p>
		   <div class="fleft selectOption">&nbsp;</div>
		 </div>
		<p class="fleft tagDesc width515">系统默认每月 <strong>11</strong> 日 <strong>00:00:00</strong>开始生成客户群清单</p>
		 </li>
		 -->
		<li>
		   <label class="fleft labFmt100">客户群描述：</label>
		   <textarea class="fleft textarea628_30"  id="custom_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciCustomGroupInfo.customGroupDesc" >${ciCustomGroupInfo.customGroupDesc}</textarea>
		</li>
	</ol>
</div>

<!-- 客户群清单列表 -->
<div id="attrList_a" style="height:308px;">
<div class="clientBaseListBox">
    <div class="clientBaseTitle">
	    <div class="fleft clientBaseMainTitle fB">
	    	选择输出属性
		</div>
		<div class="fleft">
			<input id="parentName" type="text" class="inputFmt170 lMargin22"  readonly="readonly" value="属性分类" />
		</div>
		<div  class="fleft sysAutoMatch" >
			<input type="checkbox" id="sysAutoMatch" onclick="selectedAutoMatch();" class="fleft" /><label class="fleft"  for="sysAutoMatch">系统自动匹配</label>
		</div>
	 	<div class="fright foldActionBox">
              <a href="javascript:void(0);" class="foldAction"  id="foldAction"></a>
              <a href="javascript:void(0);" class="unfoldAction" id="unfoldAction"></a>

        </div>
        <div class="fright clientSearchBox">
		    <input id="queryLabelParamInput" name="queryParam" type="text" class="fleft clientSearchInput"  value="模糊查询"  onkeydown="if(event.keyCode == 13){$('#queryLabel').click();}" />
			<input id="queryLabel" type="button" class="fleft searchBtn"  />
	 	</div>
 	</div>
 	<div class="clientSelectListBox" id="labelListBox">
 		<ol></ol>
    </div>
    <div id="labelListTd" class="clientBaseList">
	 	<ol >
	 		<c:forEach var="ciLabelInfo" items="${ciLabelInfoList}" varStatus="st">
	 			<li>
		  			<input name="ciLabelInfoListOne[${st.index}].labelId" 
						type="checkbox" value="${ciLabelInfo.labelId}" 
						<c:if test="${ not empty ciLabelInfo.columnId}">
							id="${ciLabelInfo.labelId}_${ciLabelInfo.columnId}"
						</c:if>
						<c:if test="${ empty ciLabelInfo.columnId}">
							id="${ciLabelInfo.labelId}"
						</c:if>   
						onclick="checkLabelInfo(this);" /> 
					<label title=" ${ciLabelInfo.labelName}"  
						<c:if test="${ not empty ciLabelInfo.columnId}">
							for="${ciLabelInfo.labelId}_${ciLabelInfo.columnId}"
						</c:if>
						<c:if test="${ empty ciLabelInfo.columnId}">
							for="${ciLabelInfo.labelId}"
						</c:if>   
					> ${ciLabelInfo.labelName}</label>
				</li>
				<input name="ciLabelInfoListOne[${st.index}].columnId" type="hidden" value="${ciLabelInfo.columnId}"/>
				<input name="ciLabelInfoListOne[${st.index}].columnCnName" type="hidden" value="${ciLabelInfo.columnCnName}"/>
			</c:forEach>
	 	</ol>
    </div>
 	<ol id="labelListTdTwo" style="display:none;">
 		<c:forEach var="ciLabelInfo" items="${ciLabelInfoList}" varStatus="st">
 			<li>
  			<input name="ciLabelInfoList[${st.index}].labelId" 
		type="checkbox" value="${ciLabelInfo.labelId}"  <c:if test="${ not empty ciLabelInfo.columnId}">id="two_${ciLabelInfo.labelId}_${ciLabelInfo.columnId}"</c:if><c:if test="${ empty ciLabelInfo.columnId}">id="two_${ciLabelInfo.labelId}"</c:if> />
		<label> ${ciLabelInfo.labelName}</label>
		</li>
		<input name="ciLabelInfoList[${st.index}].columnId" type="hidden" value="${ciLabelInfo.columnId}"/>
		<input name="ciLabelInfoList[${st.index}].columnCnName" type="hidden" value="${ciLabelInfo.columnCnName}"/>
	</c:forEach>
 	</ol>
</div>
<c:if test="${!empty ciGroupAttrRelList}">
<div class="clientBaseListBox">
	<div class="clientBaseTitle">
     	<div class="fleft clientBaseMainTitle fB">选择输出属性</div>
	 	<div class="fleft clientBaseMainDescTitle">
   		(来源于客户群清单)
 		</div>
 	</div> 
 	<h4></h4>
 	<div id="customAttrListTd"  class="clientBaseList"> 
	  	<ol >
	  	<c:forEach var="ciGroupAttrRel" items="${ciGroupAttrRelList}" varStatus="st">
	  		<li>
	  			<input name="ciGroupAttrRelListOne[${st.index}].id.attrCol" 
					type="checkbox" id="${ciGroupAttrRel.customId}_${ciGroupAttrRel.columnName}" 
					value="${ciGroupAttrRel.columnName}" onclick="checkAttrInfo(this);"/>
				<label title="${ciGroupAttrRel.customName}-${ciGroupAttrRel.attrColName}" 
					for="${ciGroupAttrRel.customId}_${ciGroupAttrRel.columnName}">
					${ciGroupAttrRel.customName}-${ciGroupAttrRel.attrColName}</label>
			</li>
			<input name="ciGroupAttrRelListOne[${st.index}].id.customGroupId" type="hidden" value="${ciGroupAttrRel.customId}"/>
			<input name="ciGroupAttrRelListOne[${st.index}].attrColType" type="hidden" value="${ciGroupAttrRel.attrColType}"/>
			<input name="ciGroupAttrRelListOne[${st.index}].attrColName"
				type="hidden" value="${ciGroupAttrRel.attrColName}"/>
			<input name="ciGroupAttrRelListOne[${st.index}].labelOrCustomId" type="hidden" value="${ciGroupAttrRel.labelOrCustomId}"/>
		</c:forEach>
		</ol>
	</div>
	<ol id="customAttrListTdTwo" style="display:none;">
  	<c:forEach var="ciGroupAttrRel" items="${ciGroupAttrRelList}" varStatus="st">
  		<li>
  			<input name="ciGroupAttrRelList[${st.index}].id.attrCol" 
		type="checkbox" id="two_${ciGroupAttrRel.customId}_${ciGroupAttrRel.columnName}" value="${ciGroupAttrRel.columnName}" /><label>${ciGroupAttrRel.customName}-${ciGroupAttrRel.attrColName}</label>
		</li>
		<input name="ciGroupAttrRelList[${st.index}].id.customGroupId" type="hidden" value="${ciGroupAttrRel.customId}"/>
		<input name="ciGroupAttrRelList[${st.index}].attrColType" type="hidden" value="${ciGroupAttrRel.attrColType}"/>
		<input name="ciGroupAttrRelList[${st.index}].attrColName"
			type="hidden" value="${ciGroupAttrRel.attrColName}"/>
		<input name="ciGroupAttrRelList[${st.index}].labelOrCustomId" type="hidden" value="${ciGroupAttrRel.labelOrCustomId}"/>
	</c:forEach>
	</ol>
</div>
</c:if>
</div>
<div id="menuContent" class="zTreeDemoBackground"  style="display:none; position: absolute;">
	<div class="ztreeBox" id="ztreeBox">
    	<ul id="labelTypetree" class="ztree selectCheckboxZtree mScrool"></ul>
    </div>
</div>
<input id="parentId" type="hidden" name="parentId" value=""/>
<input id="level" type="hidden" name="level" value=""/>
<!-- 按钮区 -->
<div class="btnActiveBox">
    <%
       if("true".equals(marketingMenu)){
    %>
	<input id="isAutoMatch" type="checkbox" name="ciCustomGroupInfo.productAutoMacthFlag" class="ensureBtn"  value="${ciCustomGroupInfo.productAutoMacthFlag}"/> 系统自动匹配产品&nbsp;&nbsp;
	<%} %>
	<a href="javascript:void(0);" id="saveBtn" onclick="submitListInit();" class="ensureBtn">保存</a>
</div>
<input type="hidden" name="sceneIds" id="sceneIds" value=""/>
<input type="hidden"  id="flagConstraint" value="${flagConstraint }"/>
</form>
</body>
</html>

