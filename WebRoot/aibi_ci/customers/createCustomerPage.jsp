<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
<title>计算中心-客户群保存</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU");
String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/shopCart.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
	var _closeType = "";
	var defaultLabelAttr = true;
	var customGroupName = "";
	var	validater;
	var _submitFlag=true;
	var pushDialogCloseType = 1;//推送窗口关闭方式 1：手动点X关闭窗口 0：其他方式关闭  ；为1的时候页面跳转到计算中心
	//var selectedSortAttrArr = [];//下拉框已选中的排序属性
	//var selectedVertAttr = {};//下拉框已选中的纵表排序属性
	var setting = {
		view: {
			showIcon: showIconForTree
		},
		data: {
			key: {
				name:"labelOrCustomName"
			},
			simpleData: {
				enable: true,
				idKey:"labelOrCustomId",
				pIdKey:"parentId"
			}
		},
		callback:{
			onExpand:onExpand,
			onClick: zTreeOnClick
		}
	};
	var zTree;
	var zNodes = "" ;
	$(function(){
		if(getCookieByName("${version_flag}") == "true"){
 			$("#sceneListLi").hide();
 	 	} else {
 	 		$("#sceneListLi").show();
	 		$("#sceneIds").blur(function () {
		 		if($.trim($("#sceneIds").val()) == "") {
		 			$("#snameTip").empty();
		 			$("#snameTip").show().append("客户群分类不能为空");
		 			$(document).scrollTop(20);
		 			return false;
		 		}
	 		});
 	 	}
 		$("#custom_name").blur(function () { 
 			if($.trim($("#custom_name").val()) == "") {
 				$("#customNameTip").empty();
 				$("#customNameTip").show().append("客户群名称不能为空");
 				$(document).scrollTop(20);
 				return false;
 			}
 		});
 		$("#isCreateCustomPage").val("true");
 		customGroupName = '${ciCustomGroupInfo.customGroupName}';
 		$("#successDialog").dialog({
			width:640,
			autoOpen: false, 
			dialogClass:"ui-dialogFixed",
			modal: true,
			title:"系统提示", 
			position:["center","center"],
			resizable:false, 
			draggable: true,
			close:function(event, ui){
				$("#successFrame").attr("src", "");
				if($("#isPushCustom").val() != "true"){
					//openCalculateCenter();
					if(getCookieByName("${version_flag}") == "true"){
						window.location.href = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?indexLinkType=2";
					}else{
						window.location.href = '${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1';
					}
				}
			}
		});
		$('#dialog_push_single').dialog({
			dialogClass	: "ui-dialogFixed",
			autoOpen	: false,
			width		: 730,
			title		: '客户群推送设置',
			modal		: true,
			resizable	: false,
			close:function(event, ui){
				if(pushDialogCloseType == 1) {
					if (getCookieByName("${version_flag}") == "false") {
						window.location.href = '${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1';
					}else{
						window.location.href = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?indexLinkType=2";
					}
				}
			}
		});
		$("#verticalLabelSetDialog").dialog({
			width:720,
			dialogClass:"ui-dialogFixed",
			autoOpen: false, 
			modal: true,
			title:"纵表标签条件设置", 
			resizable:false, 
			draggable: true,
			close:function(){
				$("#verticalLabelSetFrame").attr("src", "");
				if(_closeType == ""){
					if($('#vertAttrSetDialogSelectLabelId').attr('selectedLabelId') && $('#vertAttrSetDialogSelectLabelId').attr('selectedLabelId').length>0){
						var checkedboxId =$('#vertAttrSetDialogSelectLabelId').attr('selectedLabelId');
						if($('#vertLabelAttrListTd').find('li[vertLabelId="'+checkedboxId+'"]').length>0){
						}else{
							$('#tagListBox ul li input[id="' + checkedboxId + '"]').attr('checked',false);
						}
					}
				}else{
					_closeType = "";
				}
			}
		});
		//标签客户群属性模糊查询
		$('#queryLabel').click(function() {
			$(".tagListBoxInner").css("background","#FFF");
			queryLabelOrCustomAttrList();
		});
		//标签客户群树 分类查询
		$("#querytree").click(function() {
			defaultLabelAttr = false;
			initTree();
		});
		$("#tagCalcSaveBtn").click(function(){
			//submitListInit();
			submitConfirm();
		});
		$("#prevCalculateBtn").click(function(){
			//修改客户群进入标签计算中心
			var customGroupId = $("#custom_id").val();
			var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=2";
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		});
		var scenePublicId="<%=ServiceConstants.ALL_SCENE_ID%>";
		initTree();
		resetForm();
		$("#labelOrCustom").val('-1');
		var cname = $.trim($("#custom_name").val());
		if(cname == "请输入客户群名称"){
			$.focusblurfocus("#custom_name");
		}
		$("#queryLabelParamInput").focusblur({ 
			"focusColor":"#222232" ,
		    "blurColor":"#757575"  
		});
		$("#queryztreeInput").focusblur({ 
			"focusColor":"#222232" ,
		    "blurColor":"#757575"  
		});
		$("#tagListBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"y",
			mouseWheel:{
				preventDefault:true //阻止冒泡事件
			}
		});
		$(document).click(function(e){
		    var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
			$("#selListBox").slideUp().removeAttr("open");
			$(".selectAttrBox").slideUp().removeAttr("open");
			$(".vertSelectAttrBox").slideUp().removeAttr("open");
			$(".selBtn").removeClass("selBtnActive");
		});
		//阻止事件冒泡
		$(".firstItem").click(function(ev){
			firstItemFn($("#firstItem_active"),'sceneIds' );
			var e = ev||event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		//模拟下拉框--打开方式 
	    $("#selBtn").click(function(){
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
		
		//排序属性 下拉
	    $(".selectAttrBtn").live('click', function(){
			var open = $(this).next('.selectAttrBox').attr("open");
	    	if(!open){
	    		var li = '';
	    		var attrs = $('#labelListBox ul li');
	    		attrs.each(function(index, item) {
	    			var isVert = $(item).find('div').attr('isVert');
	    			var columnId = $(item).find('div').attr('checkedboxid');
	    			var attrSource = $(item).find('div').attr('attrSource');
	    			var attrName = $.trim($(item).find('p').text());
	    			if(isVert == 1) {
	    				var verticalLabelAttr = $('#vertLabelAttrListTd li');
	    				verticalLabelAttr.each(function(index, item) {
	    					var vertLabelId = $(this).attr('vertLabelId');
			    			if(vertLabelId == columnId) {
			    				var pName = $('#two_' + vertLabelId).next().text();
			    				var ul = $(item).find('ul');
			    				ul.each(function(i, e) {
			    					var cId = $($(e).find('input')[1]).val();
			    					var name = $($(e).find('input')[2]).val();
		    						li += '<li attrSource="2" isVert="1" parentLabelId="' + vertLabelId + '" columnId="' + cId + 
		    							'"><a class="ellipsis" href="javascript:void(0);" title="' + pName + '-' + name + '">' + pName + '-' + name + '</a></li>';
			    				});
			    				return false;
			    			}
	    				});
	    				return true;
	    			}
	    			li += '<li attrSource="' + attrSource + '" isVert="' + isVert + '" columnId="' + columnId 
						+ '"><a class="ellipsis" title="' + attrName + '" href="javascript:void(0);" >' + attrName + '</a></li>'
	    		});
	    		$(this).next('.selectAttrBox').find('ol').html('').append(li);
	    		$(this).next('.selectAttrBox').slideDown().attr("open", true);
				$(this).addClass("selBtnActive");
	    	} else {
	    		$(this).next('.selectAttrBox').slideUp().removeAttr("open");
				$(this).removeClass("selBtnActive");
	    	}
	    	return false;
	    });
	    $(".sortAttrName").live('click', function(){
			var open = $(this).next().next('.selectAttrBox').attr("open");
	    	if(!open){
	    		var li = '';
	    		var attrs = $('#labelListBox ul li');
	    		attrs.each(function(index, item) {
	    			var isVert = $(item).find('div').attr('isVert');
	    			var columnId = $(item).find('div').attr('checkedboxid');
	    			var attrSource = $(item).find('div').attr('attrSource');
	    			var attrName = $.trim($(item).find('p').text());
	    			if(isVert == 1) {
	    				var verticalLabelAttr = $('#vertLabelAttrListTd li');
	    				verticalLabelAttr.each(function(index, item) {
	    					var vertLabelId = $(this).attr('vertLabelId');
			    			if(vertLabelId == columnId) {
			    				var pName = $('#two_' + vertLabelId).next().text();
			    				var ul = $(item).find('ul');
			    				ul.each(function(i, e) {
			    					var cId = $($(e).find('input')[1]).val();
			    					var name = $($(e).find('input')[2]).val();
		    						li += '<li attrSource="2" isVert="1" parentLabelId="' + vertLabelId + '" columnId="' + cId + 
		    							'"><a class="ellipsis" href="javascript:void(0);" title="' + pName + '-' + name + '">' + pName + '-' + name + '</a></li>';
			    				});
			    				return false;
			    			}
	    				});
	    				return true;
	    			}
	    			li += '<li attrSource="' + attrSource + '" isVert="' + isVert + '" columnId="' + columnId 
						+ '"><a class="ellipsis" title="' + attrName + '" href="javascript:void(0);" >' + attrName + '</a></li>'
	    		});
	    		$(this).next().next('.selectAttrBox').find('ol').html('').append(li);
	    		$(this).next().next('.selectAttrBox').slideDown().attr("open", true);
				$(this).next('.selectAttrBtn').addClass("selBtnActive");
	    	} else {
	    		$(this).next().next('.selectAttrBox').slideUp().removeAttr("open");
				$(this).next('.selectAttrBtn').removeClass("selBtnActive");
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
		
		//排序属性下拉选择
		$(".selectAttrBox ol a").live('click', function(ev){
			var isVert = $(this).parent().attr('isVert');
			var columnId = $(this).parent().attr('columnId');
			var attrSource = $(this).parent().attr('attrSource');
			var name = $(this).text();
			var parentLabelId = '';
			if(isVert == 1) {
				parentLabelId = $(this).parent().attr('parentLabelId');
			}
			
			$(this).parents('.selBox').find('.sortAttrName')
				.attr('parentLabelId', parentLabelId)//纵表标签父标签
				.attr('columnId', columnId)
				.attr('attrSource', attrSource)
				.attr('title', name)
				.attr('isVert', isVert).html(name);	
		})
		
		//排序属性升序还是降序
		$('.sortType').live('click', function(ev) {
			var sortVal = $(this).next().next().val();
			if(sortVal == 'asc') {
				$(this).next().next().val('desc');
				$(this).html('↓降序');
			} else {
				$(this).next().next().val('asc');
				$(this).html('↑升序');
			}
		});
		//删除排序属性
		$('.delSortType').live('click', function(ev) {
			var columnId = $(this).parent().parent().find('.sortAttrName').attr('columnId');
			$(this).parent().parent().remove();
			
		});
		
		$("#selListBox li a").click(function(ev){
			if($(this).find("input").attr("checked")){
				$(this).find("input").attr("checked",false)
			}else{
				$(this).find("input").attr("checked",true)
			}
			selItemFn($(this).find("input"),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
		$("#selListBox li a input").click(function(ev){
			selItemFn($(this),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		//展开
		$("#showRules").click(function(){ 
		    var isOpen = $(this).attr("isOpen");
			if(isOpen == "false"){
				$(this).attr("isOpen","true").removeClass("arrowDown").addClass("arrowUp").html("展开");
				$(".customer-rules,.customer-rules-content").height("auto") 
			}else{
				$(".customer-rules,.customer-rules-content").height("")
				$(this).attr("isOpen","false").removeClass("arrowUP").addClass("arrowDown").html("折叠"); 
			}
	    });
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
		});
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
	    	$(".is_bring_phone").css({"display":"block"});
	    	$(".is_bring_phone").removeClass("hidden");
        	$(".no_bring_phone").css({"display":"none"});
        	$(".no_bring_phone").addClass("hidden");
	    	var customGroupId = $("#custom_id").val();
			var isHasList = $('#is_hasList_source').val();
			var oldIsHasList = $('#oldIsHasList').val();
			if(customGroupId && isHasList == 1) {//不包含清单的客户群可以修改生成周期
				parent.showAlert("不可将已包含清单的客户群改为不包含清单的客户群！", "failed");
				return;
			}
	    	$(".is_hasList").show();
	        $(this).hide(); 
	        var _valTemp = $.trim($(".is_hasList:visible").find("input").val());
	        $("#is_hasList").val(_valTemp);
	        if(_valTemp == 0){
	        	//隐藏属性
	        	$(".attrList").hide();
	        	//清空属性
	       	 	$(".attrList input[type=checkbox]").prop("checked",false);
	       	 	$("#labelListBox ul").empty();
	    		$("#labelListBox").css("height","auto");
	    	 	$("#queryztreeInput").val("模糊查询");
	        	$("#is_show_phone").hide();
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
				var updateCycle = $("#createPeriod").val();
				if(updateCycle == 4){
		       		$("#createPeriod").val(4);
		       	}
				$("#listNumUl").hide();
	        }else{
	        	$("#is_show_phone").show();
	        	$("#createPeriodBox_li").show();
		       	var updateCycle = $("#createPeriod").val();
		       	if(updateCycle == 2){
		       		$("#period_monthly").show();
		       		$("#sysRecommendTimeLi").show();
		       		$("#updateCycle").val(2);
		       	}
		       	if(updateCycle == 3){
		       		$("#period_daily").show();
		       		$("#sysRecommendTimeLi").show();
		       		$("#updateCycle").val(3);
		       	}
				if(updateCycle == 4){
		       		$("#createPeriod").val(1);
		       	}
				$("#listNumUl").show();
	        }
	    });
	    //是否只带出手机号
		$(".is_phone").click(function(){
			$(".is_phone").children("input")[0].checked=false;
			$(".is_phone").show();
			$(this).hide();
			$(".is_phone").find("input")[0].checked=true;
			var _valTemp = $.trim($(".is_phone:visible").find("input").val());
			var customGroupId = $("#custom_id").val();
			if(customGroupId) {
			}
	        if(_valTemp == 0){
	        	//隐藏属性
	        	$(".attrList").hide();
	        	//清空属性
	       	 	$(".attrList input[type=checkbox]").prop("checked",false);
	       	 	$("#labelListBox ul").empty();
	    		$("#labelListBox").css("height","auto");
	    	 	$("#queryztreeInput").val("模糊查询");
	    	 	$(".listMaxNumNo").css({"display":"block"});
	    	 	$(".listMaxNumYes").css({"display":"none"});
	    	 	$('.listMaxNumNo').removeClass('hidden');
				$('.listMaxNumYes').addClass('hidden');
	    	 	$('.sortAttrBox').remove();
				$('.vertSortAttrBox').remove();
				$("#listMaxNumInput").val('');
				$("#listMaxNumBox").hide();
	        }else{
	        	//显示属性
	        	$(".attrList").show();
	        }
		});
		$("#labelListBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"y",
			mouseWheel:{
				preventDefault:true, //阻止冒泡事件 
			}
		});
		$(".clientSelectItemClose").live("click",function(){
			$(this).parent().parent().remove();
			var checkedboxId = $(this).parent().attr("checkedboxId");
			var isVert = $(this).parent().attr("isVert");
			if(isVert == 1){
				if($('#vertLabelAttrListTd').find('li[vertLabelId="'+checkedboxId+'"]').length>0){
					$('#vertLabelAttrListTd').find('li[vertLabelId="'+checkedboxId+'"]').remove();
				}
				var selectedSortAttrs = $('.sortAttrBox');
				selectedSortAttrs.each(function(index, item) {
					var labelId = $(item).find('.sortAttrName').attr('parentLabelId');
					if(labelId == checkedboxId) {
						$(item).remove();
					}
				});
			}else{
				$("#two_" + checkedboxId).attr("checked",false);
			}
			
			$("#" + checkedboxId).attr("checked",false);
			if($("#labelListBox li").length==0){
				$("#sysAutoMatch").prop("checked",false);
				$("#labelListBox").css("height","auto");
			}
			//删除对应排序属性
			deleteSortAttrsSelected(checkedboxId);
		});
		$("#customer-tagListBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"y",
			mouseWheel:{
				preventDefault:true, //阻止冒泡事件 
			}
		});
		//系统推荐时间
		$(".sysRecommendTime").click(function(){ 
	 	   $(".sysRecommendTime").show();
		   $(this).hide();
		   if($(this).hasClass("isSysRecommendTime")){
				var createPeriod =$("#createPeriod").val();
				if(createPeriod == '2'){
					$("#monthTimeDiv").show();
				}
				if(createPeriod == '3'){
					$("#dayTimeDiv").show();
				}
				$("#isSysRecommendTime").val("0");
			}else{
				$("#dayTimeDiv").hide();
				$("#monthTimeDiv").hide();
				$("#isSysRecommendTime").val("1");
			}
	    });
		//是否存为模板
	    $(".selectBox").click(function(){
		   $(".selectBox").show();
		   $(this).hide();
		   if($(this).hasClass("handFillDate")){
			   $("#template_name").hide();
			   $("#isSaveTemplate").val("");
		   }else{
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
		
		//是否限制清单数量
	    $(".isListMaxNum").click(function(){
	    	
			$(".isListMaxNum").children("input")[0].checked = false;
			$(".isListMaxNum").show();
			$(this).hide();
			$(".isListMaxNum:visible").find("input")[0].checked = true;
			
			var attrs = $('#labelListBox ul li');
			
			var _valTemp = $.trim($(".isListMaxNum:visible").find("input").val());
			if(_valTemp == 1) {
				$('#listMaxNumBox').show();
			} else {
				$('.sortAttrBox').remove();
				$('.vertSortAttrBox').remove();
				$("#listMaxNumInput").val('');
				$("#listMaxNumBox").hide();
			}
		});
		//为限制清单数量添加排序字段
		$('#addSortAttrBtn').click(function() {
			var maxSortAttrNum = '${sortAttrNum}';//配置文件中配置的排序字段的数量
			var currentSortAttrNum = $('.sortAttrBox:visible').length;//已经添加的排序字段的数量
			var selectedAttr = $('#labelListBox ul li');//选择的属性的数量
			var selectedAttrNum = 0;
			
			selectedAttr.each(function(index, item) {
				var isVert = $(item).find('div').attr('isVert');
				if(isVert == 1) {
					var columnId = $(item).find('div').attr('checkedboxid');
					var verticalLabelAttr = $('#vertLabelAttrListTd li');
    				verticalLabelAttr.each(function(index, item) {
    					var vertLabelId = $(this).attr('vertLabelId');
		    			if(vertLabelId == columnId) {
		    				var pName = $('#' + vertLabelId).attr('labelName');
		    				var n = $(item).find('ul').length;
		    				selectedAttrNum = selectedAttrNum + n;
		    				return false;
		    			}
    				});
					return true;
				}
				selectedAttrNum ++;
			});
			
			if(selectedAttrNum == 0) {
				//$.fn.weakTip({"tipTxt":"请先为客户群选择属性！"});
				commonUtil.create_alert_dialog('showSortError1AlertDialog', {
					 type	: 'failed',//failed
					 width	: 500,
					 txt	: '请先为客户群选择属性！',
					 height	: 200
				});
				return;
			}
			
			if(currentSortAttrNum >= selectedAttrNum) {
				commonUtil.create_alert_dialog('showSortError2AlertDialog', {
					 type	: 'failed',//failed
					 width	: 500,
					 txt	: '已超出已选属性个数！',
					 height	: 200
				});
				//$.fn.weakTip({"tipTxt":"已超出已选属性个数！"});
				return;
			}
			if(currentSortAttrNum >= maxSortAttrNum) {
				commonUtil.create_alert_dialog('showSortError3AlertDialog', {
					 type	: 'failed',//failed
					 width	: 500,
					 txt	: '排序字段超过系统最大数量！',
					 height	: 200
				});
				//$.fn.weakTip({"tipTxt":"排序字段超过最大数量！"});
				return;
			}
			
			var attrHtml = '<li class="clearfix sortAttrBox">' + 
				'<div class="fleft fmt150">' + 
					'<p class="fleft"></p>' + 
					'<label>排序属性：</label>' + 
				'</div>' + 
				'<div class="fleft selBox">' + 
					'<div class="selTxt sortAttrName" style="overflow:hidden;">请选择</div>' + 
					'<a href="javascript:void(0);" class="selBtn selectAttrBtn"></a>' + 
					'<div class="selListBox selectAttrBox">' + 
						'<ol></ol>' + 
					'</div>' + 
				'</div>' + 
				'<div class="fleft operation">' + 
					'<a class="sortType fleft" href="javascript:void(0);">↑升序</a>' +
					'<a class="delSortType fleft close-icon mt" href="javascript:void(0);"></a>' + 
					'<input class="sortTypeHidden hidden" value="asc"/>' + 
				'</div>' + 
			'</li>';
			$('#listNumUl').append(attrHtml);
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

		var cid ='${ciCustomGroupInfo.customGroupId}';
		//生成周期
		$("#createPeriodBox div").bind('click', changeCycle);
		$('#endDateByMonth').bind('click', function() {
			var minDate = '#F{$dp.$D(\'startDateByMonth\',{M:1})}';
			if(cid != null && cid != "") {
				minDate = $(this).val();
			}
			WdatePicker({dateFmt:'yyyy-MM', minDate:minDate})
		});
		$('#endDateByDay').bind('click', function() {
			var minDate = '#F{$dp.$D(\'startDateByDay\',{d:1})}';
			if(cid != null && cid != "") {
				minDate = $(this).val();
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
			var d = '${ciCustomGroupInfo.dataDate}';
			if(d != null && d!= ""){
				$("#startDateHidden").val(d+"-01");
				$("#startDateByMonth").val(d);
			}
		}
		$("#startDateHidden").attr("disabled", "true");
		$("#startDateByMonth").attr("disabled", "true");
		$("#startDateByDay").attr("disabled", "true");
		
		var actionUrl=$.ctx + "/ci/customersManagerAction!queryVertLabelIdFormSession.ai2do";
		$.ajax({
			url : actionUrl,
			async: false,
			dataType:"json",
			success: function(result){
				if(result){
					for(var i=0;i<result.length;i++){
						var labelId = result[i];
						$('#queryVertLabelIdFormSessionDiv').append('<li selectedVertLabelId = "'+labelId+'"></li>');
					}
				}
			}
		});
	 });
	 jQuery.focusblurfocus = function(focusid){
		var focusblurid = $(focusid);
		var defval = focusblurid.val();
		focusblurid.focus(function(){ 
			var thisval = $(this).val(); 
			$(this).css("color","#222232");
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
	function showIconForTree(treeId, treeNode) {
		return !treeNode.isParent;
	};
	function onExpand(event, treeId, treeNode) {
		$("#HztreeBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"yx",
			mouseWheel:{
				preventDefault:true, //阻止冒泡事件
				axis:"yx"
			}
		});	
		 
	};
	//修改回显
	function resetForm(isOpen){
		//if(!isOpen){return;}ciGroupAttrRelListModify
		//编辑时填充表单
		var customGroupId = $("#custom_id").val();
		if(customGroupId != null && customGroupId != ""){//修改客户群
			$("#custom_name").val(customGroupName);
			//分类参数回显start
		    var selBakArr = $("#sceneIds").val().split(",");
		    var firstItemVal = $("#firstItem_active").val();
		    var selListInput = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
		    var titleArr = [];
		    for(var i = 0;i< selBakArr.length ;i++){
		        //公共场景
		        if(selBakArr[i] == firstItemVal){
		            titleArr.push($("#firstItem_active").attr("_sceneName"));
		            $("#firstItem_active").attr("checked",true);
		            break;
		        }else{
		            for(var j=0;j<selListInput.length ;j++){
		                if($(selListInput[j]).val() == selBakArr[i]){
		                   titleArr.push($(selListInput[j]).attr("_sceneName"));
		                   $(selListInput[j]).attr("checked",true);
		                   break ; 
		                }
		            }
		        }
		    }
		    $("#scenesTxt").html(titleArr.join(",")).attr("title",titleArr.join(","));
			//分类参数回显end
			
			//是否共享
			$("#is_private_source").val($("#is_private").val());
			if($("#is_private").val() == 1) {
				$(".is_private").children("input")[0].checked = false;
				$(".is_private").show();
				$(".isShared").hide();
				$(".notShared").children("input")[0].checked = true;
			} else {
				$(".is_private").children("input")[0].checked = false;
				$(".is_private").show();
				$(".notShared").hide();
				$(".isShared").children("input")[0].checked = true;
			}
			
			//是否包含清单
			$("#is_hasList_source").val('${ciCustomGroupInfo.isHasList}');
			$("#is_hasList").val('${ciCustomGroupInfo.isHasList}');
			if($("#is_hasList").val() == 1) {
				$(".is_hasList").children("input")[0].checked = false;
				$(".is_hasList").show();
				$(".notHasList").hide();
				$(".isHasList").children("input")[0].checked = true;
			} else {
				$(".is_hasList").children("input")[0].checked = false;
				$(".is_hasList").show();
				$(".isHasList").hide();
				$(".notHasList").children("input")[0].checked = true;
				$("#attrList_a").hide();
				$(".attrList").hide();
				$("#createPeriodBox_li").hide();
				$("#period_monthly").hide();
				$("#period_daily").hide();
				$("#sysRecommendTimeLi").hide();
				$("#is_show_phone").hide();
				$("#listNumUl").hide();
				return;
			}
			//是否只生成手机号
			var attrRelSize = ${fn:length(ciGroupAttrRelListModify)};
			if(attrRelSize > 0){//有其他属性
				$(".no_bring_phone").css({"display":"block"});
		    	$(".no_bring_phone").removeClass("hidden");
	        	$(".is_bring_phone").css({"display":"none"});
	        	$(".is_bring_phone").addClass("hidden");
	        	$(".attrList").show();
	        	$("#listNumUl").show();
			}
			//周期性设置
			var updateCycle = $.trim($('#updateCycle').val());
			$('#createPeriod').val(updateCycle);
			$("#createPeriodBox div").removeClass('current');
			//var flagConstraint = '${flagConstraint}';
			if(updateCycle == 2) {//月周期
				$("#createPeriodBox div[flag='month']").addClass('current');
				$('#period_monthly').show();
				var startDate = $("#startDate").val();
				var sYear = startDate.substring(0,4);
				var sMonth = startDate.substring(5,7);
				$("#startDateHidden").val(startDate);
				$("#startDateByMonth").val(sYear+"-"+sMonth);
				var endDate = $("#oldEndDate").val();
				var eYear = endDate.substring(0,4);
				var eMonth = endDate.substring(5,7);
				$("#endDateHidden").val(endDate);
				$("#endDateByMonth").val(eYear+"-"+eMonth);
				
			} else if(updateCycle == 3) {//日周期
				$("#createPeriodBox div[flag='day']").addClass('current');
				$('#period_daily').show();
			}
			
			//带出是否预约时间生成清单
			var listCreateTime = $.trim($('#listCreateTime').val());
			$("#sysRecommendTimeLi").show();
			$("#listCreateTime").val(listCreateTime);
			if(listCreateTime) {
				$("#sysRecommendTimeYes").hide();
				$("#sysRecommendTimeNot").show();
				$("#isSysRecommendTime").val("0");
				if(updateCycle == 2) {
					$("#monthTimeDiv").show();
					$("#monthCycleTime").val(listCreateTime)
				} else if(updateCycle == 3) {
					$("#dayTimeDiv").show();
					$("#dayCycleTime").val(listCreateTime)
				}
			} else {
				$("#sysRecommendTimeYes").show();
				$("#sysRecommendTimeNot").hide();
				$("#isSysRecommendTime").val("1");
				$("#monthTimeDiv").hide();
				$("#dayTimeDiv").hide();
			}
			
			//带出客户群的属性TODO
			var selfVelAttrRel = $.trim($('#selectedCustomGroupValAttrRel').html());
			var tag = '';
			//遍历纵表标签属性
			if(selfVelAttrRel.length > 0) {
				var count = $('#vertLabelAttrListTd').find('ul').length;
				var attrValTotal = [];
				var otherAttr = [];
				$('#selectedCustomGroupValAttrRel li').each(function(index, item) {
					var flag = true;
					var div = $(this).find('div');
					var p = $(this).find('p').first();
				    var displayTitle =  p.attr('title');
					var attrSource = div.attr('attrSource');
					var checkedboxId = div.attr('checkedboxId');
					var attrVal = div.attr('attrVal');
					var attrName = div.attr('attrName');
					var vertLabelId = checkedboxId.split("_")[0];
					var attrColName = div.attr('attrColName');
					var displayContext	=  '<label>' + attrColName + '：</label>';
					var labelOrCustomColumn = div.attr('labelOrCustomColumn');
					$('#selectedCustomGroupAttrRel li').each(function(index, item) {
						var isVert = div.attr('isVert');
						if(isVert == '1'){
							var div1 = $(this).find('div');
							var p1 = $(this).find('div').find('p').first();
							var vertLabelId1 = div1.attr('checkedboxId');
							if(vertLabelId == vertLabelId1){
								flag = false;
								var displayTitle1 = p1.attr('title');
								var displayContext1 = p1.text();
								displayTitle1 +=  ','+displayTitle;
								displayContext1 =  '<label>' + displayTitle1 + '</label>';
								p1.html("");
								p1.html(displayContext1);
								p1.attr('title',displayTitle1);
								return false;
							}
						}
					});
					if(flag){
						var $li = '<li><div checkedboxId ="' + vertLabelId + 
						'" isVert=\"1\" attrSource=\"2\"><p class=\"fleft\" title=\"'+displayTitle+'\" vertLabelId=\"' + vertLabelId + 
						'\" onclick="popVertAttrSet('+vertLabelId+');" columnId=\"'+labelOrCustomColumn+'\">' + displayContext + 
						'</p><p class="clientSelectItemClose fleft"></p></div></li>'
						$("#selectedCustomGroupAttrRel").append($li);
					}
					
					//纵表标签属性写入提交的FORM
					count = getUniqueIndex(count);
					tag += '<ul count="'+count+'" vertLabelId="'+vertLabelId+'" columnId="'+labelOrCustomColumn+'">';
					tag += '<input name="vertLabelAttrList['+count+'].labelId" ';
					tag += 'type="hidden" value="'+vertLabelId+'"  id="'+vertLabelId+'_'+labelOrCustomColumn+'"/>';
					tag += '<input name="vertLabelAttrList['+count+'].columnId" type="hidden" value="'+labelOrCustomColumn+'"/>';
					tag += '<input name="vertLabelAttrList['+count+'].columnCnName" type="hidden" value="'+attrColName+'"/>';
					if(attrVal == null){
						
					}else{
						tag += '<input name="vertLabelAttrList['+count+'].attrVal" type="hidden" value="'+attrVal+'"/>';
						//attrValTotal.push({"vertLabelId":vertLabelId,"columnId":labelOrCustomColumn,"attrVal":attrVal,"attrName":attrName});
						$('#vertLabelAttrListTd').append('<li vertLabelId=\"' + vertLabelId + '\" attrVal = \"' + attrVal + '\" attrName = \"' + attrName + '\" columnId = \"' + labelOrCustomColumn + '\" otherAttr=\"\"></li>');
					}
					tag += '</ul>';
					count++;
					
					$('#temp_for_vert_attr').append(tag);
					tag ='';
				});
				
				$('#initModifyVertattrDiv').append($('#vertLabelAttrListTd').html());
				$('#temp_for_vert_attr ul').each(function(index, item) {
					vertLabelId = $(item).attr('vertLabelId');
					if($(item).attr('attrVal') && $(item).attr('attrVal').length>0){

					}else{
						if($('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').attr('otherAttr').length == 0){
							$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').attr('otherAttr',$(item).attr('columnId'));
						}else{
							var oldOtherAttr =$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').attr('otherAttr');
							$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').attr('otherAttr',oldOtherAttr+','+$(item).attr('columnId'))
						}
					}
					
					$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').append(item);
				});
				$('#temp_for_vert_attr').html('');
				$('#initModifyVertattrDiv li').each(function(index, item) {
					vertLabelId = $(item).attr('vertLabelId');
					
					var oldOtherAttr =$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').attr('otherAttr');
					$('#initModifyVertattrDiv').find('li[vertLabelId="'+vertLabelId+'"]').attr('otherAttr',oldOtherAttr);
				});
				
				
			}
			var selfAttrRel = $.trim($('#selectedCustomGroupAttrRel').html());
			if(selfAttrRel.length > 0) {
				$("#labelListBox").show().css("height","65px");
				$('#labelListBox ul').html(selfAttrRel).show();
				//选中展示框里的属性
				$('#selectedCustomGroupAttrRel li').each(function(index, item) {
					var div = $(this).find('div');
					var attrSource = div.attr('attrSource');
					var checkedboxId = div.attr('checkedboxId');
					var vertLabelId = div.attr('labelOrCustomId');
					var isVert = div.attr('isVert');
					var attrColName = div.attr('attrColName');
					var labelOrCustomColumn = div.attr('labelOrCustomColumn');
					if(attrSource == 2) {
						$('#tagListBox ul li input[id="' + checkedboxId + '"]').attr('checked', true);
						if(isVert == 0){
							$('#labelListTdTwo li input[id="two_' + checkedboxId + '"]').attr('checked', true);
						}
					} else if(attrSource == 3) {
						$('#customAttrListTdTwo li input[id="two_' 
							+ checkedboxId + '"]').attr('checked', true);
					}
				});
			}
			//排序属性回显
			var listMaxNum = $.trim($('#listMaxNum').val());
			if(listMaxNum) {
				$('#listMaxNumInput').val(listMaxNum);
				$('.listMaxNumYes').removeClass('hidden');
				$('.listMaxNumNo').addClass('hidden');
				$('#listMaxNumBox').show();
				$('#listNumUl').append($('#oldSortAttr li'));
			}
		}
	}
	function queryLabelOrCustomAttrList(){
		var queryParam = $('#queryLabelParamInput').val();
		var parentId = $("#parentId").val();
		var level = $("#level").val();
		var labelOrCustom = $("#labelOrCustom").val();
		var customListDate = $("#customListDate").val();
		if(queryParam == "模糊查询"){
			queryParam = "";
		}
		if(labelOrCustom == '-1'){
			var queryLabelUrl = $.ctx + "/ci/customersManagerAction!findLabelsByLabelName.ai2do";
			$.post(queryLabelUrl, {labelName : queryParam,parentId:parentId,level:level}, function(response) {
				var tag = '';
				for(var index=0; index<response.length; index++) {
					var key = response[index].labelId;
					var value = response[index].labelName;
					var labelOrCustomId = response[index].labelOrCustomId;
					var labelTypeId = response[index].labelTypeId;
					var labelName= response[index].labelName;
					
					var columnId = response[index].columnId;
					var columnCnName = response[index].columnCnName;
					//alert(key+"_"+value+"_"+labelOrCustomId+"_"+labelTypeId+"_"+labelName+"_"+columnId+"_"+columnCnName);
					var keyTemp ="";
					if(columnId != null && columnId != ""){
						//keyTemp = key+'_'+columnId;
						keyTemp = key;
					}else{
						keyTemp = key;
					}
					
					var checkedVal = $("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked");
					if(labelTypeId == 8){
						if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').length>0){
							checkedVal = true;
						}		
					}
					
					tag += '<li><div class="clearfix"><label><p class="fleft chechBoxItem">';
					tag += '<input name="ciLabelInfoListOne[' + index + '].labelId" type="checkbox"  value="';
					tag += key+'"  id="'+keyTemp+'" labeltypeid="'+labelTypeId+'" labelName="'+labelName+'"';
	
					if(checkedVal){
						if(checkedVal == "checked" || checkedVal == true){
							tag += ' checked="' + checkedVal + '" ';
						}	
					}
					tag += ' onclick="checkLabelInfo(this);" /> </p>';
					tag += '<p class="fleft checkboxTxt" title="'+value+'"> '+value+'</p></label></div></li>';
					tag += '<input name="ciLabelInfoListOne[' + index + '].columnId" type="hidden"  value="'+columnId+'" />';
					tag += '<input name="ciLabelInfoListOne[' + index + '].columnCnName" type="hidden"  value="'+columnCnName+'" />';
					
				}
				$('#tagListBox ul').empty().append(tag).mCustomScrollbar("update");
			});
		}else{
			//var paramObj = window.parent.$("#labelForm").serialize();
			//var queryLabelUrl = $.ctx + "/ci/customersManagerAction!findLabelsByLabelName.ai2do?" + paramObj;
			var queryCustomAttrUrl = $.ctx + "/ci/customersManagerAction!findCustomAttrByColumnName.ai2do";
			$.post(queryCustomAttrUrl, {labelName : queryParam,parentId:parentId,customListDate:customListDate}, function(response) {
				$('#tagListBox ul').empty();
				for(var i=0; i<response.length; i++) {
					var tag = '';
					var item = response[i];
					var showName = commonUtil.trim(item.attrColName+ "(" +item.customName+ "[" +item.dataDateStr+"])","g");
					var checkedVal = $("#customAttrListTdTwo").find("input[id='two_" +item.labelOrCustomId +'-'+item.columnName+"']").attr("checked");
					tag += '<li><div class="clearfix"><label><p class="fleft chechBoxItem">';
					tag += '<input name="ciGroupAttrRelListOne[' + i + '].id.attrCol" type="checkbox" id="' +item.labelOrCustomId +'-'+ item.columnName + '"  value="' + item.columnName + '" ';
					if(checkedVal){
						if(checkedVal == "checked" || checkedVal == true){
							tag += ' checked="' + checkedVal + '" ';
						}	
					}
					tag += ' onclick="checkAttrInfo(this);" ></p>';
					tag += '<p class="fleft checkboxTxt" title="'+showName+'">'+showName+'</p></label></div></li>';
					tag += '<input name="ciGroupAttrRelListOne[' + i + '].id.customGroupId" ';
					tag += 'type="hidden" value="' + item.customId + '"/>';
					tag += '<input name="ciGroupAttrRelListOne[' + i + '].attrColName" ';
					tag += 'type="hidden" value="' + item.attrColName + '"/>';
					tag += '<input name="ciGroupAttrRelListOne[' + i + '].attrColType" ';
					tag += 'type="hidden" value="' + item.attrColType + '"/>';
					tag += '<input name="ciGroupAttrRelListOne[' + i + '].labelOrCustomId"';
					tag += ' type="hidden" value="' + item.labelOrCustomId + '"/>';
					$('#tagListBox ul').append(tag).mCustomScrollbar("update");
				}
			});
		}
		//修改客户群,回显原客户群属性
		$('#selectedCustomGroupAttrRel li').each(function(index, item) {
			var div = $(this).find('div');
			var checkedboxId = div.attr('checkedboxId');
			var labelOrCustomColumn = div.attr('labelOrCustomColumn');
			$('#tagListBox ul li input[id="' + checkedboxId + '"]').attr('checked', true);
		});
	}
	function zTreeOnClick(event, treeId, treeNode) {
		$(".tagListBoxInner").css("background","#FFF");
		var labelOrCustom = '-1';
		var level = treeNode.level;
		
		if(level == '0'){
			labelOrCustom = treeNode.labelOrCustomId;
		}else if(level == '1'){
			labelOrCustom = treeNode.getParentNode().labelOrCustomId;
		}else if(level == '2'){
			labelOrCustom = treeNode.getParentNode().getParentNode().labelOrCustomId;
		}else if(level == '3'){
			labelOrCustom = treeNode.getParentNode().getParentNode().getParentNode().labelOrCustomId;
		}
		var tlid = (treeNode.labelOrCustomId === "-1" || 
			treeNode.labelOrCustomId === "-2") ? "" : treeNode.labelOrCustomId;
		
		$("#parentId").val(tlid);
		//获得清单日期
		$("#customListDate").val("");
		var reg = /^.*\[(\d+)\]$/;
		var customListDate = reg.exec(treeNode.labelOrCustomName);
		if(customListDate != null && customListDate.length > 1){
			$("#customListDate").val(customListDate[1]);
		}
		$("#labelOrCustom").val(labelOrCustom);
		$("#level").val(level);
		queryLabelOrCustomAttrList();
	}
	//iframe 方式 子调用父方法 关闭弹出框
	function iframeCloseDialog(obj){
		$("._idClass").removeClass("_idClass");
		$(obj).dialog("close");
	}
	//iframe 方式 子调用父方法 关闭弹出框
	function iframeCloseDialog2(obj,type){
		$("._idClass").removeClass("_idClass");
		_closeType = type;
		$(obj).dialog("close");
	}
	//推送设置
	function pushCustomGroup(customerGroupId) {
		var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId=" 
			+ customerGroupId;
		$("#successDialog").dialog("close");
		$('#push_single_alarm_iframe').attr('src', ifmUrl);
		$('#dialog_push_single').dialog('open');
	}
	function pushCustomerGroupConfirm() {
		if($("#push_single_alarm_iframe")[0].contentWindow.validateIsNull()) {
			commonUtil.create_alert_dialog("clearCalculateCenterOld", {
				txt 	: "确认推送该客户群？",
				type 	: "confirm", 
				width 	: 500,
				height 	: 200
			}, push);
			//showAlert("确认推送该客户群？","confirm",push);
		}
	}
	//推送提交
	function push(){
		var url = '${ctx}/ci/customersManagerAction!pushCustomerAfterSave.ai2do';
		pushDialogCloseType = 0;
		$('#dialog_push_single').dialog('close');
		$("#push_single_alarm_iframe")[0].contentWindow.submitForm(url);
	}
	//关闭推送弹出层
	function closePush(){
		openCalculateCenter();
		$("#dialog_push_single").dialog("close");
	}
	
	//设置排序属性到form
	function setSortAttr2Form() {
		var attrs = $('.sortAttrBox');
		var attrsHtml = '';
		var sort = 0;
		var result = {success: true, msg : ''};
		var ids = [];
		
		var _valTemp = $.trim($(".isListMaxNum:visible").find("input").val());
		if(_valTemp == 0) {
			return result;
		}
		
		//验证限制数量格式和大小
		var num = $('#listMaxNumInput').val();
		if(isNaN(num)) {
			result.success = false;
			result.msg = '限制清单数量请输入数字！';
			return result;
		}
		if(num < 1) {
			result.success = false;
			result.msg = '限制清单数量必须大于0！';
			return result;
		}
		
		if((num + '').indexOf('.') != -1) {
			result.success = false;
			result.msg = '限制清单数量不能为小数！';
			return result;
		}
		
		attrs.each(function(index, item) {
			var labelId = $(item).find('.selTxt').attr('columnId');
			
			if(labelId) {//有选中的属性
				//验证重复排序字段
				if(ids.indexOf(labelId) != -1) {
					result.success = false;
					result.msg = '排序字段重复选择！';
					return false;
				}
				ids.push(labelId);
				
				var attrSource = $(item).find('.selTxt').attr('attrSource');//2标签属性3客户群属性
				var isVert = $(item).find('.selTxt').attr('isVert');
				var sortType = $(item).find('.sortTypeHidden').val();
				var column = '';
				
				if(attrSource == 3) {//客户群
					column = labelId.substring(labelId.indexOf('-') + 1, labelId.length);
					labelId = labelId.substring(0, labelId.indexOf('-'));
				}
				
				if(isVert == 1) {
					column = labelId;
				}
				attrsHtml += '<div class="hidden"><input name="sortAttrs[' + sort + '].labelOrCustomId" value="' + labelId + '"/>' + 
					'<input name="sortAttrs[' + sort + '].labelOrCustomColumn" value="' + column + '"/>' + 
					'<input name="sortAttrs[' + sort + '].attrSource" value="' + attrSource + '"/>' + 
					'<input name="sortAttrs[' + sort + '].isVerticalAttr" value="' + isVert + '"/>' + 
					'<input name="sortAttrs[' + sort + '].sortNum" value="' + sort + '"/>' +
					'<input name="sortAttrs[' + sort + '].sortType" value="' + sortType + '"/></div>';
				sort ++;
			} else {
				result.success = false;
				result.msg = '排序属性不能为空！';
				return false;
			}
		});
		var maxSortAttrNum = '${sortAttrNum}';//配置文件中配置的排序字段的数量
		if(sort > maxSortAttrNum) {
			result.success = false;
			result.msg = '排序字段超过系统最大数量！';
			return result;
		}
		$('#sortAttrs4Submit').html('').append(attrsHtml);
		$('#labelForm').append($('#sortAttrs4Submit'));
		return result;
	}
	
	//保存客户群
	function submitList(){
		_submitFlag = false;
		//if(validateForm()){
			
			//加上遮罩层
			$.blockUI(); 
			var actionUrl = $.ctx;
			var customGroupId = $("#custom_id").val();
			if(customGroupId != null && customGroupId != ""){
				actionUrl += "/ci/customersManagerAction!editCustomer.ai2do";
			}else if(customGroupId == null || customGroupId == ""){
				actionUrl += "/ci/customersManagerAction!saveCustomer.ai2do";
			}
			
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
			
			$.post(actionUrl, $("#labelForm").serialize()+"&"+$("#saveForm").serialize(), function(result){
				if(result.success){
					var customerName = $("#custom_name").val();
					var templateName = "";
					if($("#isSaveTemplate").val() == "1"){
						templateName = $("#template_name").val();
					}
					var customGroupId = result.customGroupId;
					var isHasList = result.isHasList;
					openSuccess(customGroupId, customerName, templateName, isHasList);
					//$.unblockUI();
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
							$(document).scrollTop(20);
						}
						if(cmsg.indexOf("重名") < 0 && msg.indexOf("重名") < 0){
							parent.showAlert(cmsg,"failed");
						}
					}else{
						var cmsg = result.cmsg;
						if(cmsg.indexOf("重名") >= 0){
							$("#customNameTip").empty();
							$("#customNameTip").show().append(cmsg);
							$(document).scrollTop(20)
						}else{
							parent.showAlert(cmsg,"failed");
						}
					}
					$.unblockUI();
				}
			});
			
		//}
		_submitFlag=true;
	}
	//避免多次点击保存
	function submitListInit(){
		if(_submitFlag){
			submitList();
		}
	}
	
	//提交确认提示框
	function submitConfirm() {
		if(validateForm()){
			var dlgObj = commonUtil.create_alert_dialog("showAlertDialog", {
				"txt"	: "确定保存客户群？",
				"type"	: "confirm",   
				"width"	: 500,
				"height": 200
			}, "submitListInit") ; 
		}
	}
	//打开保存成功弹出层
	function openSuccess(customerId, customerName, templateName, isHasList){
		customerName = encodeURIComponent(encodeURIComponent(customerName));
		templateName = encodeURIComponent(encodeURIComponent(templateName));
		//$("#successDialog").dialog("close");
		var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="
				+ customerName + "&templateName=" + templateName + "&customerId=" + customerId + "&isHasList=" + isHasList+"&userId=${userId}";
		$("#successFrame").attr("src", ifmUrl);
		$("#successDialog").dialog("open");
		$.unblockUI();
		clearCalEles_();//保存成功后清空计算中心
	}
	//清空操作
	function clearCalEles_(){
		var clearCarShopItemUrl = $.ctx + '/ci/ciIndexAction!delAllCarShopSession.ai2do';
		$.post(clearCarShopItemUrl, {}, function(result) {});
	}
	
	function validateNull(id){
		var $input = $("#" + id);
		var val = $input.val();
		if($.trim(val) == "") {
			$input.parent().parent().next().empty().hide();
			return false;
		}
	}
	
	//表单验证
	function validateForm(){
		$("#customNameTip").hide();
		$("#customNameTip").empty();
		$("#dailyTip").hide();
		$("#dailyTip").empty();
		$("#monthlyTip").hide();
		$("#monthlyTip").empty();
		if($("#isSaveTemplate").val() == "1"){
			var name = $.trim($("#template_name").val());
			if(name == "请输入模板名称"){
				$("#template_name").val("");
				name="";
			}else{
				$("#template_name").val(name);
			}
		}
		var cname = $.trim($("#custom_name").val());
		if(cname == "请输入客户群名称"){
			$("#custom_name").val("");
			cname ="";
		}else{
			$("#custom_name").val(cname);
		}
		if($.trim($("#custom_name").val()) == "") {
			$("#customNameTip").empty();
			$("#customNameTip").show().append("客户群名称不能为空");
			$(document).scrollTop(20);
			return false;
		}
		if(getCookieByName("${version_flag}") != "true"){
			if($.trim($("#sceneIds").val()) == "") {
				$("#snameTip").empty();
				$("#snameTip").show().append("客户群分类不能为空");
				$(document).scrollTop(20);
				return false;
			}
 	 	}
		if($("#isAutoMatch").is(":checked")){
			$("#isAutoMatch").val(1);
		}else{
			$("#isAutoMatch").val(0);
		}
		if($("#is_hasList").val() == 1){
			if($.trim($("#createPeriod").val()) == "3") {//校验日客户群数
				if($("#endDateByDay").val() == ""){
					$("#dailyTip").empty();
					$("#dailyTip").show().append("有效结束时间不能为空!");
					$(document).scrollTop(20);
					return false;
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
						$(document).scrollTop(20);
						return false;
					}
		   		}
		   	}else if($.trim($("#createPeriod").val()) == "2"){
					if($("#endDateByMonth").val() == ""){
							$("#monthlyTip").empty();
							$("#monthlyTip").show().append("有效结束时间不能为空!");
							$(document).scrollTop(20);
							return false;
					}
			}
			if($("#isSysRecommendTime").val() == "0"){
				var createPeriod =$("#createPeriod").val();
				if(createPeriod == '2'){
					if($.trim($("#monthCycleTime").val()) == "") {
						$("#monthCycleTimeTip").empty();
						$("#monthCycleTimeTip").show().append("清单生成时间不能为空!");
						$(document).scrollTop(20);
						return false;
					}
				}
				if(createPeriod == '3'){
					if($.trim($("#dayCycleTime").val()) == "") {
						$("#dayCycleTimeTip").empty();
						$("#dayCycleTimeTip").show().append("清单生成时间不能为空!");
						$(document).scrollTop(20);
						return false;
					}
				}
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
			//把排序属性和对应的排序方式加入到form当中
			var result = setSortAttr2Form();
			if(!result.success) {
				commonUtil.create_alert_dialog('showSortErrorAlertDialog', {
					 type	: 'failed',//failed
					 width	: 500,
					 txt	: result.msg,
					 height	: 200
				});
				return false;
			}
		}
		
		return true;
	}
		
	//点击logo返回到首页
	function showHomePage(){
		if(getCookieByName("${version_flag}") == "true"){
			var url = "${ctx}/ci/ciMarketAction!newMarketIndex.ai2do";
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		}else{
			var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4";
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		}
	}
	//标签分类显示
	function initTree(){
		var labelName = $("#queryztreeInput").val();
		if(labelName == "模糊查询"){
			labelName = "";
		}
		var actionUrl = $.ctx + "/ci/customersManagerAction!queryLabelTypeTree.ai2do";
		$.post(actionUrl,{labelNameKeyWord:labelName}, function(response) {
			if(response.result == true){
				var allItem = {open:true,"labelOrCustomId":"-1", "parentId":0, "labelOrCustomName":"全部标签",isParent:true};
				var labelSize = response.labelSize;
				var customSize = response.customSize;
				var labelList = response.labelList;
				if(customSize > 0){
					var allItem1 = {open:true,"labelOrCustomId":"-2", "parentId":0, "labelOrCustomName":"全部客户群",isParent:true};
					labelList.unshift(allItem1);
				}
				if(labelSize > 0){
					labelList.unshift(allItem);
				}
				zNodes=labelList;
				zTree = $.fn.zTree.init($("#labelTypetree"), setting, zNodes);
				if(defaultLabelAttr){
					//默认选中全部
					//defaultLabelAttrFun();
					//$(document).scrollTop(20);
				}
			}else{
				parent.showAlert(response.message, "failed");
			}
		})
	}
	/* 默认选中全部标签
	function defaultLabelAttrFun(){
		var treeObj = $.fn.zTree.getZTreeObj("labelTypetree");
		var node = treeObj.getNodeByTId("-1");
		var nodes = treeObj.getNodes();
		if (nodes.length>0) {
			treeObj.selectNode(nodes[0]);
		}
	} */
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
		var flag = $(this).attr("flag");
		var oldFlag = $("#createPeriodBox").find(".current").attr("flag");
		if (flag == oldFlag) {
			return;
		}
	    $("#createPeriodBox div").removeClass();
	    
	    $("#monthCycleTimeTip,#dayCycleTimeTip").hide();
		//切换分割线
		if(flag == "day"){
			// $("#createPeriodBox").find("div").eq(0).addClass("periodSegLine");
			$("#createPeriod").val(3);
			$("#period_daily").show();
			$("#period_monthly").hide();
			var d = $("#dayDataDateHidden").val();
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
			var customGroupId = $("#custom_id").val();
			if(customGroupId == null || customGroupId == ""){
				//日客户群
				$("#dataDateHidden").val($("#dayDataDateHidden").val());
			}
		}else if(flag == "month"){
			//$("#createPeriodBox").find("div").eq(1).addClass("periodSegLine");
			$("#createPeriod").val(2);
			$("#period_monthly").show();
			$("#period_daily").hide();
			var d = $("#monthLabelDateHidden").val();
			if(d != null && d!= ""){
				var _year = d.substring(0,4);
				var _month = d.substring(4,6);
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
			var customGroupId = $("#custom_id").val();
			if(customGroupId == null || customGroupId == ""){
				//月客户群
				$("#dataDateHidden").val($("#monthLabelDateHidden").val());
			}
		}else if(flag == "once"){
			//$("#createPeriodBox ").find("div").eq(2).addClass("periodSegLine");
			$("#createPeriod").val(1);
			//$("#period").hide();
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
			var customGroupId = $("#custom_id").val();
			if(customGroupId == null || customGroupId == ""){
				//一次性客户群
				$("#dataDateHidden").val($("#monthLabelDateHidden").val());
			}
		} 
		$(this).addClass("current");
	}
	
	function datePicked(){
	 	var d = $("#monthCycleTime").val();
	 	if(d != null && d!= ""){
	 		$("#listCreateTime").val(d);
		}
	}
	function selectedAutoMatch(){
		var checkedVal =$("#sysAutoMatch").attr("checked");
		var queryLabelUrl = $.ctx + "/ci/customersManagerAction!findCiLabelInfoListBySys.ai2do";
		var count = $('#vertLabelAttrListTd').find('ul').length;
		$.post(queryLabelUrl, {}, function(response) {
			var attrSource = 3;
			
			for(var index=0; index<response.length; index++) {
				var columnId = response[index].columnId;
				var key = response[index].labelId;
				var name = response[index].labelName;
				var labelTypeId = response[index].labelTypeId;
				
				var tag = '';
				var keyTemp ="";
				
				if(columnId != null && columnId != ""){
					keyTemp = key+'_'+columnId;
				}else{
					keyTemp = key;
					attrSource = 2;
				}
				if(checkedVal == "checked" || checkedVal == true){
					$("#tagListBox").find("input[id='"+keyTemp+"']").attr("checked",true);
					//TODO自动匹配到纵表标签时，打开弹出框
					//var labelTypeId = $("#tagListBox").find("input[id='"+keyTemp+"']").attr("labeltypeid");
					//$("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked",true);
					//alert(labelTypeId);
					if(labelTypeId == 8){
						//var calcuElement = mainObj.attr("calcuElement");
						//ajax取主列值
						
						$("#tagListBox").find("input[id='"+key+"']").attr("checked",true);
						var tag = '';
						var displayContext	= [];
						var displayTitle = [];
						var attrVal = '';
						var attrName = '';
						var otherAttrs = [];
						
		   				var actionUrl=$.ctx + "/ci/customersManagerAction!queryVertLabelWithAllSelectedColumnFromSession.ai2do";
		   				var para={"labelId":key};
		   				$.ajax({
		   					url : actionUrl,
		   					data : para,
		   					async: false,
		   					dataType:"json",
		   					success: function(result){
		   						if(result){
		   							for(var i=0;i<result.length;i++){
		   								var labelRule = result[i];
		   								if(labelRule.isMustColumn == 1){
			  								count = getUniqueIndex(count);
			  								tag += '<ul index="'+count+'">';
			  								tag += '<input name="vertLabelAttrList['+count+'].labelId" ';
			  								tag += 'type="hidden" value="'+key+'"  id="'+key+'_'+labelRule.calcuElement+'"/>';
			  								tag += '<input name="vertLabelAttrList['+count+'].columnId" type="hidden" value="'+labelRule.calcuElement+'"/>';
			  								tag += '<input name="vertLabelAttrList['+count+'].columnCnName" type="hidden" value="'+labelRule.columnCnName+'"/>';
			  								tag += '<input name="vertLabelAttrList['+count+'].attrVal" type="hidden" value="'+labelRule.attrVal+'"/>';
			  								tag += '</ul>';
			  								count++;
			 								
			  								displayContext.push(labelRule.columnCnName);
			  								displayTitle.push(labelRule.columnCnName);
			  								attrVal = labelRule.attrVal;
			  								attrName = labelRule.attrName;
			  								columnId = labelRule.calcuElement;
		   									
		   								}else{
		   									count = getUniqueIndex(count);
			  								tag += '<ul index="'+count+'">';
			  								tag += '<input name="vertLabelAttrList['+count+'].labelId" ';
			  								tag += 'type="hidden" value="'+key+'"  id="'+key+'_'+labelRule.calcuElement+'"/>';
			  								tag += '<input name="vertLabelAttrList['+count+'].columnId" type="hidden" value="'+labelRule.calcuElement+'"/>';
			  								tag += '<input name="vertLabelAttrList['+count+'].columnCnName" type="hidden" value="'+labelRule.columnCnName+'"/>';
			  								tag += '</ul>';
			  								count++;
			 								
			  								displayContext.push(labelRule.columnCnName);
			  								displayTitle.push(labelRule.columnCnName);
			  								otherAttrs.push(labelRule.calcuElement);
		   								}
		   							}
		   						}
		   					}
		   				});
		   				tag ='<li vertLabelId=\"' + key + 
		   				'\" attrVal=\"'+attrVal+'\" attrName=\"'+attrName+'\" columnId=\"'+columnId+'\" otherAttr=\"'+otherAttrs.join(',')+'\">'+tag+'</li>';
		   				
		   				if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').length>0){
		   					$('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').remove();
		   				}
		   				$('#vertLabelAttrListTd').append(tag);
		   				
		   				if($('#labelListBox').find('div[checkedboxId="'+key+'"]')){
		   					$('#labelListBox').find('div[checkedboxId="'+key+'"]').parent().remove();
		   				}
		   				var $li = '<li><div attrSource="2" checkedboxId =' + key + 
		   					' isVert=\"1\"><p class=\"fleft\" title=\"'+displayTitle.join(',')+'\" vertLabelId=\"' + key + 
		   					'\" onclick="popVertAttrSet('+key+');" attrval=\"'+attrVal+'\" attrName=\"'+attrName+'\" columnId=\"'+columnId+'\">' + displayContext.join(',') + 
		   					'</p><p class="clientSelectItemClose fleft"></p></div></li>'
		   				
		   				$("#labelListBox ul").append($li);
						$("#labelListBox").show().css("height","65px");
						
					}else{
						$("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked",true);
						setLabelAttrListTwoText($("#labelListTdTwo").find("input[id='two_"+keyTemp+"']"), attrSource);
					}
				}else{
					$("#labelListTdTwo").find("input[id='two_"+keyTemp+"']").attr("checked",false);
					$("#tagListBox").find("input[id='"+keyTemp+"']").attr("checked",false);
					if(labelTypeId == 8){
						if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').length>0){
							$('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').remove();
						}
					}
					setLabelAttrListTwoText($("#labelListTdTwo").find("input[id='two_"+keyTemp+"']"), attrSource);
				}
			}
			
			var labelOrCustom = $("#labelOrCustom").val();
			if(checkedVal == "checked" || checkedVal == true){
				$("#customAttrListTdTwo").find("input[type='checkbox']").attr("checked",true);
				if(labelOrCustom == "-2"){
					$("#tagListBox").find("input[type='checkbox']").attr("checked",true);
				}
			}else{
				$("#customAttrListTdTwo").find("input[type='checkbox']").attr("checked",false);
				if(labelOrCustom == "-2"){
					$("#tagListBox").find("input[type='checkbox']").attr("checked",false);
				}
			}
			$("#customAttrListTdTwo").find("input[type='checkbox']").each(function(){
				setLabelAttrListTwoText($(this), 3);
			});
			if($("#labelListBox li").length > 0){
				$("#labelListBox").show().css("height","65px");
			}
		});
	}
	
	function getUniqueIndex(count) {
		if($('#vertLabelAttrListTd').find('ul[count="'+count+'"]').length>0){
			count++;
			return getUniqueIndex(count);
		}else{
			return count;
		}
	}
	
	function setLabelAttrListTwoText(ths, attrSource){
		//选择客户群列表请选项
		var checked = $(ths).attr("checked");
		var selfIdTemp = $(ths).attr("id");
		var selfId = selfIdTemp.substring(4,selfIdTemp.length);
		var labelTxt = commonUtil.trim($(ths).next().text(),"g");
		if(checked){//勾选复选框
			var $li = '<li><div attrSource="' + attrSource + '" checkedboxId =' + selfId + 
				' isVert=\"0\"><p class=\"fleft\" title=\"'+labelTxt+'\">' + labelTxt + 
				'</p><p class="clientSelectItemClose fleft"></p></div></li>'
			var flag = true;
			$("#labelListBox div").each(function(index ,item){
				var checkedboxId = $(item).attr("checkedboxId");
				if(selfId == checkedboxId){
					flag = false;
				}
			});
			if(flag){
				$("#labelListBox ul").append($li);
			}
		}else{
			$("#labelListBox div").each(function(index ,item){
				var checkedboxId = $(item).attr("checkedboxId");
				var isVert = $(item).attr('isVert');
				if(selfId == checkedboxId){
					$(item).parent("li").remove();
					//删除对应排序属性
					deleteSortAttrsSelected(selfId);
				}
			})
			if($("#labelListBox li").length == 0){
				$("#labelListBox").css("height","auto");
			}
			
		}
	}
	
	function deleteSortAttrsSelected(selfId) {
		var selectedSortAttrs = $('.sortAttrBox');
		for(var index=0; index<selectedSortAttrs.length; index++) {
			var item = selectedSortAttrs[index];
			var labelId = $(item).find('.sortAttrName').attr('columnId');
			var isVert = $(item).find('.sortAttrName').attr('isVert');
			if(isVert == 1) {
				labelId = $(item).find('.sortAttrName').attr('parentLabelId');
			}
			
			/* var listName = $('#two_' + selfId).parent().next().next().next().next().val();
			var column = selfId.substring(selfId.indexOf('_') + 1, selfId.length);
			var columnId = listName + '-' + column; */
			
			if(labelId == selfId) {
				$(item).remove();
			}
		}
	}
	
	//选择标签属性
	//TODO如果选中纵表标签时，弹出弹出框
	function checkLabelInfo(obj){
		var checkedVal =$(obj).attr("checked");
		var labelTypeId = $(obj).attr("labeltypeid");
		var key = $(obj).attr("id");
		var name = $(obj).attr("labelName");
		if(labelTypeId == 8){
			if(checkedVal){
				if(checkedVal == "checked" || checkedVal == true){
					popVertAttrSet(key);
					$('#vertAttrSetDialogSelectLabelId').attr('selectedLabelId',key);
				}else{
				
					if($('#labelListBox').find('div[checkedboxId="'+key+'"]')){
						$('#labelListBox').find('div[checkedboxId="'+key+'"]').parent().remove();
					}
					if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').length>0){
						$('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').remove();
					}
				}
			}else{
			
				if($('#labelListBox').find('div[checkedboxId="'+key+'"]')){
					$('#labelListBox').find('div[checkedboxId="'+key+'"]').parent().remove();
				}	
				if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').length>0){
					$('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').remove();
				}
				if($("#labelListBox li").length == 0){
					$("#labelListBox").css("height","auto");
				}
				//设置排序字段
				var selectedSortAttrs = $('.sortAttrBox');
				selectedSortAttrs.each(function(index, item) {
					var labelId = $(item).find('.sortAttrName').attr('parentLabelId');
					if(labelId == key) {
						$(item).remove();
					}
				});
				
			}
		}else{
			$("#labelListTdTwo").find("input[id='two_"+$(obj).attr("id")+"']").click();
			setLabelAttrListText(obj, 2);
		}
		
		if($("#labelListBox li").length == 0){
			$("#sysAutoMatch").prop("checked",false);
			$("#labelListBox").css("height","auto");
		}
	}
	
	function popVertAttrSet4AutoMatch(obj){
		var key = $(obj).attr("vertLabelId");
		if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]')){
			var attrVal = $(obj).attr("attrVal");
			var attrName = $(obj).attr("attrName");
			var columnId = $(obj).attr("columnId"); 
			
			var ifmUrl = '${ctx}/ci/ciIndexAction!findVerticalLabelAttr.ai2do';
			var form = '<form id="postData_form" action="' + ifmUrl + '" method="post" target="_self">' + 
				'<input name="attrVal" type="hidden" value="' + attrVal + '"/>' +
				'<input name="attrName" type="hidden" value="' + attrName + '"/>' +
				'<input name="labelId" type="hidden" value="' + key + '"/>' +
				'<input name="columnId" type="hidden" value="' + columnId + '"/>' +
				'</form>';
			document.getElementById('verticalLabelSetFrame').contentWindow.document.write(form);
			document.getElementById('verticalLabelSetFrame').contentWindow.document.getElementById('postData_form').submit();
			$("#verticalLabelSetDialog").dialog("open");
		}else{
			var iframeUrl = "${ctx}/ci/ciIndexAction!findVerticalLabelAttr.ai2do?labelId=" + key;
			$("#verticalLabelSetDialog").dialog("option","title", "组合标签-属性选择");
			$("#verticalLabelSetFrame").attr("src", iframeUrl).load(function(){ });
			$("#verticalLabelSetDialog").dialog("open");
		}
	}
	
	//TODO
	function popVertAttrSetFromSession(labelId){
		var key = labelId;
		var attrVal = '';
		var attrName = '';
		var otherAttrs = '';
		var columnId = '';
		var actionUrl=$.ctx + "/ci/customersManagerAction!queryVertLabelWithAllSelectedColumnFromSession.ai2do";
		var para={"labelId":key};
		$.ajax({
			url : actionUrl,
			data : para,
			async: false,
			dataType:"json",
			success: function(result){
				if(result){
					for(var i=0;i<result.length;i++){
						var labelRule = result[i];
						if(labelRule.isMustColumn == 1){
							attrVal = labelRule.attrVal;
							attrName = labelRule.attrName;
							columnId = labelRule.calcuElement;
						}else{
							if(otherAttrs.length>0){
								otherAttrs = otherAttrs+','+labelRule.calcuElement;
							}else{
								otherAttrs = labelRule.calcuElement;
							}
						}
					}
				}
			}
		});
		
		$("#verticalLabelSetDialog").dialog("option","title", "组合标签-属性选择");
		
		var ifmUrl = '${ctx}/ci/ciIndexAction!findVerticalLabelAttr.ai2do';
		var form = '<form id="postData_form" action="' + ifmUrl + '" method="post" target="_self">' + 
			'<input name="attrVal" type="hidden" value="' + attrVal + '"/>' +
			'<input name="attrName" type="hidden" value="' + attrName + '"/>' +
			'<input name="labelId" type="hidden" value="' + key + '"/>' +
			'<input name="columnId" type="hidden" value="' + columnId + '"/>' +
			'<input name="otherAttrs" type="hidden" value="' + otherAttrs + '"/>' +
			'</form>';
		document.getElementById('verticalLabelSetFrame').contentWindow.document.write(form);
		document.getElementById('verticalLabelSetFrame').contentWindow.document.getElementById('postData_form').submit();
		$("#verticalLabelSetDialog").dialog("open");
		
	}
	
	function popVertAttrSet(labelId){
		var key = labelId;
		
		if($('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]').length>0){
			obj = $('#vertLabelAttrListTd').find('li[vertLabelId="'+key+'"]');
			//alert(1);
			var attrVal = obj.attr("attrVal");
			var attrName = obj.attr("attrName");
			var columnId = obj.attr("columnId"); 
			var otherAttrs = obj.attr("otherAttr");
			
			var ifmUrl = '${ctx}/ci/ciIndexAction!findVerticalLabelAttr.ai2do';
			var form = '<form id="postData_form" action="' + ifmUrl + '" method="post" target="_self">' + 
				'<input name="attrVal" type="hidden" value="' + attrVal + '"/>' +
				'<input name="attrName" type="hidden" value="' + attrName + '"/>' +
				'<input name="labelId" type="hidden" value="' + key + '"/>' +
				'<input name="columnId" type="hidden" value="' + columnId + '"/>' +
				'<input name="otherAttrs" type="hidden" value="' + otherAttrs + '"/>' +
				'</form>';
			document.getElementById('verticalLabelSetFrame').contentWindow.document.write(form);
			document.getElementById('verticalLabelSetFrame').contentWindow.document.getElementById('postData_form').submit();
			$("#verticalLabelSetDialog").dialog("open");
		}else if($('#initModifyVertattrDiv').find('li[vertLabelId="'+key+'"]').length>0){
			obj = $('#initModifyVertattrDiv').find('li[vertLabelId="'+key+'"]');
			//alert(2);
			var attrVal = obj.attr("attrVal");
			var attrName = obj.attr("attrName");
			var columnId = obj.attr("columnId"); 
			var otherAttrs = obj.attr("otherAttr");
			
			var ifmUrl = '${ctx}/ci/ciIndexAction!findVerticalLabelAttr.ai2do';
			var form = '<form id="postData_form" action="' + ifmUrl + '" method="post" target="_self">' + 
				'<input name="attrVal" type="hidden" value="' + attrVal + '"/>' +
				'<input name="attrName" type="hidden" value="' + attrName + '"/>' +
				'<input name="labelId" type="hidden" value="' + key + '"/>' +
				'<input name="columnId" type="hidden" value="' + columnId + '"/>' +
				'<input name="otherAttrs" type="hidden" value="' + otherAttrs + '"/>' +
				'</form>';
			document.getElementById('verticalLabelSetFrame').contentWindow.document.write(form);
			document.getElementById('verticalLabelSetFrame').contentWindow.document.getElementById('postData_form').submit();
			$("#verticalLabelSetDialog").dialog("open");
		}else if($('#queryVertLabelIdFormSessionDiv').find('li[selectedVertLabelId="'+key+'"]').length>0){
			//alert(3);
			popVertAttrSetFromSession(key);
		}else{
			//alert(4);
			var iframeUrl = "${ctx}/ci/ciIndexAction!findVerticalLabelAttr.ai2do?labelId=" + key;
			$("#verticalLabelSetDialog").dialog("option","title", "组合标签-属性选择");
			$("#verticalLabelSetFrame").attr("src", iframeUrl).load(function(){ });
			$("#verticalLabelSetDialog").dialog("open");
		}
	}
	
	//选择客户群属性
	function checkAttrInfo(obj){
		$("#customAttrListTdTwo").find("input[id='two_"+$(obj).attr("id")+"']").click();
		setLabelAttrListText(obj, 3);
	}
	//标签属性
	function setLabelAttrListText(ths, attrSource){
		//选择客户群列表请选项
		var checked = $(ths).attr("checked");
		var selfId = $(ths).attr("id");
		var labelTxt = $(ths).parent().next().text();
		var labelTypeId = $(ths).attr('labelTypeId');
		if(checked){//勾选复选框
			$("#labelListBox").show().css("height","65px");
			var $li = '<li><div attrSource="' + attrSource + '" checkedboxId =' + selfId + 
				' isVert=\"0\" ><p class=\"fleft\" title=\"'+labelTxt+'\">' + labelTxt + 
				'</p><p class="clientSelectItemClose fleft"></p></div></li>'
			$("#labelListBox ul").append($li);
		}else{
			$("#labelListBox div").each(function(index ,item){
				var checkedboxId = $(item).attr("checkedboxId");
				if(selfId == checkedboxId){
					$(item).parent("li").remove();
				}
			})
			if($("#labelListBox li").length == 0){
				$("#sysAutoMatch").prop("checked",false);
				$("#labelListBox").css("height","auto");
			}
			//同时删除排序属性
			deleteSortAttrsSelected(selfId);
		}
	}
	//删除数组中的元素
	function delArrItem(arr, item) {
		var temp = [];
		for(var i=0; i<arr.length; i++) {
			if(item != arr[i]) {
				temp.push(arr[i]);
			}
		}
		arr = temp;
		return arr;
	}
</script> 
</head>
 <body>
 	<jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
<form id="saveForm" method="post">
	<input id="custom_id" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}"/>
	<input id="oldIsHasList" type="hidden" name="oldIsHasList" value="${ciCustomGroupInfo.isHasList }"/>
	<input id="oldEndDate" type="hidden" name="oldEndDate" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>' />
	<input id="updateCycle" type="hidden" name="cupdateCycle_" value="${ciCustomGroupInfo.updateCycle}" />
	<input id="data_status" type="hidden" name="ciCustomGroupInfo.dataStatus" value="${ciCustomGroupInfo.dataStatus}" />
	<input id="startDate" type="hidden" name="startDate_" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>' />
	<!-- 添加是否私有与创建地市字段 -->
	<input id="isPrivate" type="hidden" name="isPrivate_" value="${ciCustomGroupInfo.isPrivate }"/>
	<input id="create_city_id" type="hidden" name="ciCustomGroupInfo.createCityId" value="${ciCustomGroupInfo.createCityId }"/>
	<input id="listMaxNum" type="hidden" value="${ciCustomGroupInfo.listMaxNum}"/>
	
	<input id="newLabelDay" type="hidden" name="newLabelDay" value="${newLabelDayFormat}" />
	<div class="container">
		<div class="save-customer-header comWidth clearfix">
			<div class="logoBox clearfix">
				<div class="index_logo fleft" onclick="showHomePage();return false;"></div>
				<div class="secTitle fleft">计算中心</div>
			</div>
			
		</div>
		<div class="comWidth clearfix save-customer-inner"> 
			<div class="clearfix titleBox">
				<div class="starIcon fleft"></div>
				<div class="customer-title fleft">确定并保存客户群</div> 
			</div>
			<div class="clientBaseList clearfix">
				<ul>
					<li class="clearfix mt15">
						<p class="fleft fmt150">
							<span class="star fleft"></span>
							<label>客户群名称：</label>
						</p>
						<p class="fleft inputBox">
							<input type="text" name="ciCustomGroupInfo.customGroupName" maxlength="35" value="请输入客户群名称" id="custom_name" />
						</p>
						<div id="customNameTip" class="tishi-page error" style="display:none;"></div>
					</li>
					<% if ("true".equalsIgnoreCase(templateMenu)) { %>
				 	<li id="isSaveTemplateInfo" class="clearfix">
				 	<% }else{ %>
				 	<li id="isSaveTemplateInfo" class="clearfix" style="display:none;" >
				 	<% } %>
				 		<div class="fleft fmt150"> 
							<label>存为模板：</label>
						</div>
						<div class="nullItemBox isShared hidden is_private fleft">
							<input type="radio" name="isPrivateTemp" class="hidden" value="0" />
							<p class="fleft nullItemText">是</p>
							<div class="fleft nullItemOption">&nbsp;</div>
						</div>
						<div class="nullItemBox notShared is_private fleft">
							<input type="radio" name="isPrivateTemp" class="hidden" value="1" checked="checked" />
							<div class="fleft nullItemOption">&nbsp;</div>
							<p class="fleft nullItemText">否</p>
						</div> 
						<input id="isSaveTemplate" type="hidden" name="isSaveTemplate" />
					 	<input type="text" class="fleft inputFmt235 hidden"  name="ciTemplateInfo.templateName"  id="template_name" maxlength="40"  value="请输入模板名称" />
				   		<div id="templateNameTip" class="tishi-page error" style="display:none;"></div>
					</li>
					<li class="clearfix" id="sceneListLi">
						<div class="fleft fmt150">
							<p class="star fleft"></p>
							<label>客户群分类：</label>
						</div>
					      <div class="fleft selBox">
			             	  <div class="selTxt" id="scenesTxt" style="overflow:hidden;">请选择场景分类</div>
			             	  <a href="javascript:void(0);" class="selBtn" id="selBtn"></a>
			             	  <div class="selListBox" id="selListBox">
			             	     <div class="firstItem">
				             	     <c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
				             	  	 	<c:if test="${dimScenes.sortNum == 0}">
			             	  	 	 		<input type="checkbox" value="${dimScenes.sceneId}"  id="firstItem_active" name="_sceneBoxItemCheckShare" _sceneName="${dimScenes.sceneName}"/>
			             	  	 	 		<label for="firstItem_active">${dimScenes.sceneName}</label>
					             	  	 </c:if>
				             	  	 </c:forEach>
			             	  	 </div>
			             	  	 <ol>
			             	  	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
			             	  	 		<c:if test="${dimScenes.sortNum > 0}">
				             	  	 	 <li>
				             	  	 	 	<a href="javascript:void(0);" >
				             	  	 	 		<input type="checkbox" id="selBoxItem0${st.index}" name="_sceneBoxItemCheckList"  
				             	  	 	 			value="${dimScenes.sceneId}" _sceneName="${dimScenes.sceneName}"/>
				             	  	 	 		${dimScenes.sceneName}
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
						<div id="snameTip" class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
					</li>
					<li class="clearfix">
						<div class="fleft fmt150"> 
							<label>共享：</label>
						</div>
						<div class="nullItemBox isShared hidden is_private fleft">
							<input type="radio" name="isPrivateTemp" class="hidden" value="0" />
							<p class="fleft nullItemText">是</p>
							<div class="fleft nullItemOption">&nbsp;</div>
						</div>
						<div class="nullItemBox notShared is_private fleft">
							<input type="radio" name="isPrivateTemp" class="hidden" value="1" checked="checked" />
							<div class="fleft nullItemOption">&nbsp;</div>
							<p class="fleft nullItemText">否</p>
						</div> 
						<input type="hidden" id="is_private" name="ciCustomGroupInfo.isPrivate" value="${ciCustomGroupInfo.isPrivate }" />
           				<input type="hidden" id="is_private_source" value="1" />
					</li>
					<li class="clearfix">
						<div class="fleft fmt150"> 
							<label>包含清单：</label>
						</div>
						<div class="nullItemBox is_hasList isHasList fleft">
							<input type="radio" name="isHasListTemp" class="hidden" value="1" checked="checked" />
							<p class="fleft nullItemText">是</p>
							<div class="fleft nullItemOption">&nbsp;</div>
						</div>
						<div class="nullItemBox hidden is_hasList notHasList fleft">
							<input type="radio" name="isHasListTemp" class="hidden" value="0" />
							<div class="fleft nullItemOption">&nbsp;</div>
							<p class="fleft nullItemText">否</p>
						</div>
						<input type="hidden" id="is_hasList" name="ciCustomGroupInfo.isHasList" value="1" />
           				<input type="hidden" id="is_hasList_source" value="1" />
					</li>
					<li class="clearfix" id="createPeriodBox_li">
						<div class="fleft fmt150"> 
							<label>生成周期：</label>
						</div>
						<div class="fleft createPeriodBox" id="createPeriodBox">
						   	<input type="hidden" id="createPeriod" name="ciCustomGroupInfo.updateCycle" /> 
						   	<c:if test="${flagConstraint == 'true' }">
							<div style="display: ${flagConstraint == 'true' ? 'block' : 'none' }" flag="day">日</div>
						  	<p class="line-left fleft"></p> 
						   	</c:if>
						   	<div flag="month">月</div>
						   	<p class="line-left fleft"></p>
						   	<div class="current" flag="once">一次性</div>
						</div>
						<p class="fleft infoTip width360">&nbsp;&nbsp;客户群保存成功后，一次性生成最新数据清单. </p>
					</li>
					<li id="period_monthly" class="clearfix hidden">
						<div class="fleft fmt150">
							<label>客户群有效期：</label>
						</div>
						<div class="fleft customer-effective-date" id=""> 
							<p class="customer-date-box fleft">
								<input name="startDateByMonth" disabled="disabled" 
									class="customer-date-input  new_tag_input tag_date_input" 
									id="startDateByMonth" onclick="WdatePicker({dateFmt:'yyyy-MM', maxDate:'#F{$dp.$D(\'endDateByDay\')}'})" 
									type="text" readonly value="${ciCustomGroupInfo.startDate}" />
							</p>
							<p class="customer-date-connector fleft">--</p>
							<p class="customer-date-box fleft">
								<input name="endDateByMonth" id="endDateByMonth" type="text" value='' readonly 
									class="customer-date-input  new_tag_input tag_date_input" onblur="validateNull('endDateByMonth');"/> 
							</p>
						</div>
						<!-- 提示 -->
						<div id="monthlyTip" class="tishi-page error"  style="display:none;"></div>
					</li> 
					<li id="period_daily" class="clearfix hidden">
						<div class="fleft fmt150"> 
							<label>客户群有效期：</label>
						</div>
						<div class="fleft customer-effective-date" id=""> 
							<p class="customer-date-box fleft">
								<input name="startDateByDay" disabled="disabled" type="text" id="startDateByDay" 
									value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>' 
									readonly class="new_tag_input tag_date_input customer-date-input"   
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDateByDay\')}'})"/>
							</p>
							<p class="customer-date-connector fleft">--</p>
							<p class="customer-date-box fleft">
								<input name="endDateByDay" type="text" id="endDateByDay" 
									value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>' 
									readonly class="customer-date-input new_tag_input tag_date_input" onblur="validateNull('endDateByDay');"/>
							</p>
						</div>
						<!-- 提示 -->
						<div id="dailyTip" class="tishi-page error"  style="display:none;"></div><!-- 日客户群数量验证提示 -->
					</li>
					<input name="ciCustomGroupInfo.startDate" type="hidden" id="startDateHidden" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>'/>
	  				<input name="ciCustomGroupInfo.endDate" type="hidden" id="endDateHidden" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>'/>
					<li id="sysRecommendTimeLi" class="clearfix hidden">
						<div class="fleft fmt150"> 
							<label>系统推荐时间：</label>
						</div>  
						<div class="nullItemBox sysRecommendTime isSysRecommendTime" id="sysRecommendTimeYes" >
							<p class="fleft nullItemText">是</p>
							<div class="fleft nullItemOption">&nbsp;</div>
						</div>
						<div class="nullItemBox sysRecommendTime hidden" id="sysRecommendTimeNot" >
							<div class="fleft nullItemOption">&nbsp;</div>
							<p class="fleft nullItemText">否</p>
						</div> 
						<input name="ciCustomGroupInfo.isSysRecommendTime" type="hidden" id="isSysRecommendTime" value="1" />
						<input type="hidden" name="ciCustomGroupInfo.listCreateTime" id="listCreateTime" value="${ciCustomGroupInfo.listCreateTime}"/>
						<div class="fleft hidden" id="dayTimeDiv"> 
							<p class="customer-sys-date-txt fleft">每日：</p>
							<p class="customer-date-box fleft"> 
								<input class="customer-date-input  new_tag_input tag_date_input" id="dayCycleTime" onblur="validateNull('dayCycleTime');"
									onclick="WdatePicker({vel:'listCreateTime',dateFmt:'HH:mm:00',disabledDates:['23\:..\:..']})" type="text" />
							</p>
						</div>
						<div id="dayCycleTimeTip" class="tishi-page error" style="display:none;"></div>
						<div class="fleft hidden" id="monthTimeDiv"> 
							<p class="customer-sys-date-txt fleft">每月：</p>
							<p class="customer-date-box fleft">
								<input class="customer-date-input new_tag_input tag_date_input" id="monthCycleTime" onblur="validateNull('monthCycleTime');"
									onclick="WdatePicker({onpicked:datePicked,dateFmt:'dd HH:mm:ss',disabledDates:['....-..-.. 23\:..\:..','....-..-.. 23\:..\:..']})" type="text" />
							</p>
						</div>
			 			<div id="monthCycleTimeTip" class="tishi-page error" style="display:none;"></div>
					</li>
					<li class="clearfix">
						<p class="fleft fmt150">
							<span class="fleft"></span>
							<label>客户群描述：</label>
						</p>
						<p class="fleft textareaBox">
							<textarea id="custom_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciCustomGroupInfo.customGroupDesc" >${ciCustomGroupInfo.customGroupDesc}</textarea>
						</p> 
					</li>
					<li class="clearfix" id="is_show_phone" style="display: none">
						<div class="fleft fmt150"> 
							<label>只生成手机号：</label>
						</div>
						<div class="nullItemBox is_phone is_bring_phone fleft">
							<input type="radio" name="isPhone" class="hidden" value="0" checked="checked"/>
							<p class="fleft nullItemText">是</p>
							<div class="fleft nullItemOption">&nbsp;</div>
						</div>
						<div class="nullItemBox hidden is_phone no_bring_phone fleft">
							<input type="radio" name="isPhone" class="hidden" value="1" />
							<div class="fleft nullItemOption">&nbsp;</div>
							<p class="fleft nullItemText">否</p>
						</div> 
						<p class="fleft infoTip width360">&nbsp;&nbsp;需添加输出属性，请选择否 </p>
					</li>
				</ul>
			</div>
			<div class="tagList clearfix attrList" id="attrList_a" style="display: none;">
				<div class="clearfix customer-title-item">
					<div class="customer-title fleft">请选择输出属性</div>
					<div class="fleft checkboxItem" >
						<label id="sysAutoMatch_" >
							<input id="sysAutoMatch" type="checkbox" onclick="selectedAutoMatch();" class="fleft" />自动匹配
						</label>
					</div>
					<div class="fright tipBox">（可不选，默认生成清单只包含号码）</div>
				</div>
				<div class="clientSelectListBox hidden" id="labelListBox">
					<ul class="sysTagList clearfix">
					</ul>
				</div>
				<div class="clearfix customer-tag-list-box">
					<div class="fleft menuListBox" id="VztreeBox">
						<div  class="clearfix searchTagBox">
							<div class="fright ztreeSearchBox searchTagInput">
								<input name="queryParam" class="fleft clientSearchInput" id="queryztreeInput" 
									style="color: rgb(142, 142, 142);" onkeydown="if(event.keyCode == 13){$('#querytree').click();}"  
									type="text" value="模糊查询" />
								<p class="fleft searchBtutton" id="querytree" type="button"></p>
							</div> 
						</div>
						<div id="HztreeBox" class="menuListBoxInner">
							<ul class="ztree ztreeListBox" id="labelTypetree"> </ul>
						</div>
					</div>
					<div class="fleft tagListBox">
						<div  class="clearfix searchTagBox">
							<div class="fright clientSearchBox searchTagInput">
								<input name="queryParam" class="fleft clientSearchInput" id="queryLabelParamInput" 
									style="color: rgb(142, 142, 142);" onkeydown="if(event.keyCode == 13){$('#queryLabel').click();}" 
									type="text" value="模糊查询" />
								<input class="fleft searchBtn" id="queryLabel" type="button" />
							</div> 
						</div>
						<div class="tagListBoxInner" id="tagListBox"> 
							 <ul class="clearfix taglabelList">
							</ul> 
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
					 	<ol id="vertLabelAttrListTd" style="display:none;">
					 	</ol>
					 	<ol id="customAttrListTdTwo" style="display:none;">
					  	<c:forEach var="ciGroupAttrRel" items="${ciGroupAttrRelList}" varStatus="st">
					  		<li>
					  			<input name="ciGroupAttrRelList[${st.index}].id.attrCol" 
							type="checkbox" id="two_${ciGroupAttrRel.labelOrCustomId}-${ciGroupAttrRel.columnName}" value="${ciGroupAttrRel.columnName}" /><label>${ciGroupAttrRel.attrColName}(${ciGroupAttrRel.customName}[${ciGroupAttrRel.dataDateStr}])</label>
							</li>
							<input name="ciGroupAttrRelList[${st.index}].id.customGroupId" type="hidden" value="${ciGroupAttrRel.customId}"/>
							<input name="ciGroupAttrRelList[${st.index}].attrColType" type="hidden" value="${ciGroupAttrRel.attrColType}"/>
							<input name="ciGroupAttrRelList[${st.index}].attrColName"
								type="hidden" value="${ciGroupAttrRel.attrColName}"/>
							<input name="ciGroupAttrRelList[${st.index}].labelOrCustomId" type="hidden" value="${ciGroupAttrRel.labelOrCustomId}"/>
						</c:forEach>
						</ol>
					</div>
				</div>
			</div>
			<div class="clientBaseList-of clientBaseList" id="listNumUl" >
				<ul>
					<li class="clearfix">
						<div class="fleft fmt150"> 
							<label>限制清单数量：</label>
						</div>
						<div class="nullItemBox fleft hidden isListMaxNum listMaxNumYes">
							<input type="radio" name="isListMaxNum" class="hidden" value="1" checked="checked" />
							<p class="fleft nullItemText">是</p>
							<div class="fleft nullItemOption">&nbsp;</div>
						</div>
						<div class="nullItemBox fleft isListMaxNum listMaxNumNo">
							<input type="radio" name="isListMaxNum" class="hidden" value="0" />
							<div class="fleft nullItemOption">&nbsp;</div>
							<p class="fleft nullItemText">否</p>
						</div>
						<div class="fleft hidden listMaxNumBox" id="listMaxNumBox"> 
							<p class="customer-sys-date-txt fleft">数量：</p>
							<p class="customer-date-box fleft customer-btn-box"> 
								<input name="ciCustomGroupInfo.listMaxNum" class="search_input fleft" id="listMaxNumInput"  type="text" />
							</p>
							<c:if test="${sortAttrNum > 0}">
								<p class="fleft customer-btn-box">
									<input type="button" id="addSortAttrBtn" value="添加排序属性" class="addFileImportBtn"/>
								</p>
							</c:if>
						</div>
					</li>
				</ul>
			</div>
			<input id="parentId" type="hidden" name="parentId" value=""/>
			<input id="level" type="hidden" name="level" value=""/>
			<input id="labelOrCustom" type="hidden" name="labelOrCustom" value=""/>
			<input id="customListDate" type="hidden" name="customListDate" value=""/>
			
			<div class="clearfix titleBox">
				<div class="starIcon fleft"></div>
				<div class="customer-title fleft">确定客户群规则</div> 
			</div>
			<div class="open-close-box">
				<div class="open-close-icon arrowDown" id="showRules" isOpen="false">折叠</div>
			</div>
			<div class="customer-rules">
				<div class="customer-rules-title">客户群规则</div>
				<div class="customer-rules-content" id="labelOptRuleShow_">${ciCustomGroupInfo.labelOptRuleShow}</div>
			</div>
			<div class="customer-bottom-content clearfix">
				<div class="tagSaveBtnBox">
					<input id="tagCalcSaveBtn" type="button" value="保存客户群" />   
					<a href="javascript:void(0);" class="prevBtn fright" id="prevCalculateBtn">返回计算中心</a>
				</div>
			</div>
		</div>  
	</div> 
	<input type="hidden" name="sceneIds" id="sceneIds" value="${ciCustomGroupInfo.sceneId }" />
</form>
<form id="labelForm" method="post">
	<div id="inputList" style="display:none;"></div>
	<input id="dataDateHidden" type="hidden" name="ciCustomGroupInfo.dataDate" value="${ciCustomGroupInfo.dataDate }" />
	<input id="createTypeId" type="hidden" name="ciCustomGroupInfo.createTypeId" value="1" />
	<c:if test='${ciCustomGroupInfo.createUserId != null && ciCustomGroupInfo.createUserId != ""}'>
		<input id="createUserId" type="hidden" name="ciCustomGroupInfo.createUserId" value="${ciCustomGroupInfo.createUserId}" />
	</c:if>
	<c:if test='${ciCustomGroupInfo.createTime != null && ciCustomGroupInfo.createTime != ""}'>
		<input id="createTime" type="hidden" name="ciCustomGroupInfo.createTime" value='<fmt:formatDate value="${ciCustomGroupInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
	</c:if>
	<input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value="${ciCustomGroupInfo.labelOptRuleShow }"/>
	<input type="hidden" name="saveCustomersFlag" value="5"/>
	<input type="hidden" name="exploreFromModuleFlag" value="1"/>
	<input type="hidden" name="ciCustomGroupInfo.dayLabelDate" id="dayDataDateHidden" value="${ciCustomGroupInfo.dayLabelDate }"/>
	<input type="hidden"  name="ciCustomGroupInfo.monthLabelDate" id="monthLabelDateHidden" value="${ciCustomGroupInfo.monthLabelDate }"/>
	<input type="hidden"  name="ciCustomGroupInfo.tacticsId" value="${ciCustomGroupInfo.tacticsId }"></input>
</form>
<div id="successDialog" style="overflow:hidden;">
	<iframe id="successFrame" name="successFrame" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:426px;" scrolling="no"  ></iframe>
</div>
<div id="verticalLabelSetDialog">
	<iframe id="verticalLabelSetFrame" name="verticalLabelSetFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:480px"></iframe>
</div>
<!-- 推送对应标签 -->
<div id="dialog_push_single" style="overflow:hidden;">
	<iframe id="push_single_alarm_iframe" name="push_single_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
<div id="temp_for_vert_attr" style="overflow:hidden;"></div>
<div id="queryVertLabelIdFormSessionDiv" class="hidden" style="overflow:hidden;"></div>
<div id="initModifyVertattrDiv" style="overflow:hidden;"></div>
<div id="vertAttrSetDialogSelectLabelId" style="overflow:hidden;"></div>
 <ul class="sysTagList clearfix" id="selectedCustomGroupAttrRel" style="display:none;">
	<c:forEach var="ciGroupAttrRel" items="${ciGroupAttrRelListModify}" varStatus="st">
	<c:if test="${(ciGroupAttrRel.attrSource == 2 && not empty ciGroupAttrRel.labelOrCustomColumn) == false}">
	 	<li>
	 		<div attrSource="${ciGroupAttrRel.attrSource}"
	 			<c:if test="${ciGroupAttrRel.attrSource == 2 && empty ciGroupAttrRel.labelOrCustomColumn}">
	 				checkedboxId="${ciGroupAttrRel.labelOrCustomId}" isVert="0" 
	 			</c:if>
	 			<c:if test="${ciGroupAttrRel.attrSource == 3}">
	 				checkedboxId="${ciGroupAttrRel.labelOrCustomId}-${ciGroupAttrRel.labelOrCustomColumn}" isVert="0"
	 			</c:if>
	 			attrSource="${ciGroupAttrRel.attrSource}" labelOrCustomColumn="${ciGroupAttrRel.labelOrCustomColumn}">
				<p class="fleft" title="<c:if test='${ciGroupAttrRel.attrSource == 3}'>${ciGroupAttrRel.customName}-${ciGroupAttrRel.labelOrCustomId}-</c:if>${ciGroupAttrRel.attrColName}">
				<c:if test="${ciGroupAttrRel.attrSource == 3}">${ciGroupAttrRel.customName}-${ciGroupAttrRel.labelOrCustomId}-</c:if>${ciGroupAttrRel.attrColName}</p>
				<p class="clientSelectItemClose fleft"></p>
			</div>
		</li>
		</c:if>
	</c:forEach>
 </ul>
  <ul class="sysTagList clearfix" id="selectedCustomGroupValAttrRel" style="display:none;">
	<c:forEach var="ciGroupAttrRel" items="${ciGroupAttrRelListModify}" varStatus="st">
	<c:if test="${ciGroupAttrRel.attrSource == 2 && not empty ciGroupAttrRel.labelOrCustomColumn}">
	 	<li>
	 		<div attrSource="${ciGroupAttrRel.attrSource}" 
	 			checkedboxId="${ciGroupAttrRel.labelOrCustomId}_${ciGroupAttrRel.labelOrCustomColumn}" isVert="1" attrColName="${ciGroupAttrRel.attrColName}"
	 					<c:if test="${ not empty ciGroupAttrRel.attrVal}"> attrVal="${ciGroupAttrRel.attrVal}" </c:if>
	 					<c:if test="${ not empty ciGroupAttrRel.attrName}"> attrName="${ciGroupAttrRel.attrName}" </c:if>
	 			attrSource="${ciGroupAttrRel.attrSource}" labelOrCustomColumn="${ciGroupAttrRel.labelOrCustomColumn}">
	 			<p class="fleft" title="${ciGroupAttrRel.attrColName }" vertLabelId="${ciGroupAttrRel.labelOrCustomId }" onclick="popVertAttrSet(${ciGroupAttrRel.labelOrCustomId });">${ciGroupAttrRel.attrColName}</p>
				<p class="clientSelectItemClose fleft"></p>
			</div>
		</li>
	</c:if>
	</c:forEach>
 </ul>
 <div id="oldSortAttr" class="hidden">
 	<c:forEach var="groupAttrRel" items="${sortAttrs}" varStatus="st">
 		<li class="clearfix sortAttrBox"> 
			<div class="fleft fmt150"> 
				<p class="fleft"></p>
				<label>排序属性：</label> 
			</div>
			<div class="fleft selBox"> 
				<div class="selTxt sortAttrName" style="overflow:hidden;" columnId="<c:if test="${groupAttrRel.attrSource == 3}">${groupAttrRel.labelOrCustomId}-${groupAttrRel.labelOrCustomColumn}</c:if><c:if test="${groupAttrRel.attrSource == 2}"><c:if test="${groupAttrRel.isVerticalAttr == 1}">${groupAttrRel.labelOrCustomColumn}</c:if><c:if test="${groupAttrRel.isVerticalAttr == 0}">${groupAttrRel.labelOrCustomId}</c:if></c:if>" 
					parentLabelId="${groupAttrRel.labelOrCustomId}" isVert="${groupAttrRel.isVerticalAttr}" attrSource="${groupAttrRel.attrSource}" title="<c:if test="${groupAttrRel.attrSource == 3}">${groupAttrRel.customName}-${groupAttrRel.labelOrCustomId}-</c:if>${groupAttrRel.attrColName}"><c:if test="${groupAttrRel.attrSource == 3}">${groupAttrRel.customName}-${groupAttrRel.labelOrCustomId}-</c:if>${groupAttrRel.attrColName}</div> 
				<a href="javascript:void(0);" parentLabelId="${groupAttrRel.labelOrCustomId}" class="selBtn selectAttrBtn"></a>
				<div class="selListBox selectAttrBox">
					<ol></ol> 
				</div> 
			</div>
			<div class="fleft operation">
				<a class="sortType fleft" href="javascript:void(0);"><c:if test="${groupAttrRel.sortType == 'asc'}">↑升序</c:if><c:if test="${groupAttrRel.sortType == 'desc'}">↓降序</c:if></a>
				<a class="close-icon fleft mt delSortType" href="javascript:void(0);"></a> 
				<input class="sortTypeHidden hidden" value="${groupAttrRel.sortType}"/>
			</div>
		</li>
 	</c:forEach>
 </div>
	<div id="sortAttrs4Submit" class="hidden"></div>
 <input type="hidden" id="isPushCustom" value="" /> 
</body>
</html>