<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
%>
<c:set var="SHOW_RECOMMEND_CUSTOMS_NUM" value="<%=ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM%>"></c:set>
<c:set var="CUSTOMERLIST_PAGESIZE" value="<%=CommonConstants.CUSTOMERLIST_PAGESIZE%>"></c:set>
<script type="text/javascript">
$(document).ready( function() {
	var isSysRecommendCustomsFlag='${param["isSysRecommendCustomsFlag"]}';
	$("#isSysRecommendCustomsFlag").val(isSysRecommendCustomsFlag);
	  $("#viewNavBox").hoverForIE6({current:"viewNavBoxHover",delay:200});
	  $("#viewNavBox .viewNavItem").hoverForIE6({current:"viewNavItemHover",delay:200});
 	  //工具栏定位
	  var bodyWidth = $("body").width();
	  var toolTipRight= parseInt((bodyWidth-1005)/2,10)-32 ;
	  toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
	  $("#toolTip").css("right",toolTipRight);
	  $("#shopCart").css("right",parseInt((bodyWidth-1005)/2,10));
	//导入客户群的表单
	$("#dialog-form").dialog({
		autoOpen: false,
		width: 580,
		modal: true,
		close: function() {
			simpleSearch();
		},
		resizable:false
	});
	$("#createDialog").dialog({
		autoOpen: false,
		width: 660,
		title: "客户群修改",
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
	$( "#dialog_edit" ).dialog({
		autoOpen: false,
		width: 730,
		title:"新增客户群预警",
		modal: true,
		resizable:false
	});

	$('#dialog_push_single').dialog({
		autoOpen	: false,
		width		: 730,
		title		: '客户群推送设置',
		modal		: true,
		resizable	: false
	});

	//客户群趋势明细信息查看窗口
	$( "#dialog_look2" ).dialog({
	  	autoOpen: false,
	  	width: 980,
		resizable:false,
	  	title:"查看",
	  	modal: true,
	 	close:function(){
			$("#dialog_detail2").attr("src", "");
	  	}
	});
	//客户群趋势明细信息查看窗口
	$( "#dialog_look" ).dialog({
	  	autoOpen: false,
	  	width: 980,
		resizable:false,
	  	title:"查看",
	  	modal: true,
	 	close:function(){
			$("#dialog_detail").attr("src", "");
	  	}
	});
	//客户群共享
	$("#shareCustomerDialog").dialog({
		autoOpen: false,
		width: 660,
		title: "客户群共享",
		modal: true,
		resizable:false
	});
	$("#successShareDialog").dialog({
		autoOpen: false,
		width: 450,
		title: "系统提示",
		modal: true,
		close: function(event, ui){
			var url = "${ctx}/ci/customersManagerAction!search.ai2do?initOrSearch=init";
			window.location.href = url;
		},
		resizable:false
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
	//提取模板弹出框
	$("#createTemplateDialog").dialog({
		autoOpen: false,
		width: 660,
		title: "保存模板",
		modal: true,
		resizable:false
	});
});
//var thumbnailDatas = [];
//var customGroupIds = [];
//var customUpdateCycles = []; 
function loadCustomsList(){
	$(window).unbind("scroll");
	$("#queryResultListBox").empty();
	$("#noMoreResults").hide();
	$("#noResults").hide();
	var keyword = $.trim($("#keyword").val());
	if(keyword == "请输入关键字"){
		keyword="";
	}
	var customGroupName=keyword;
	$("#customGroupName").val(customGroupName);
	$("#noMoreCustomGroupResults").hide();
	$("#noResultsCustomGroup").hide();
	//设置标签地图页数为全局变量
	var totalPage;
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/customersManagerAction!findCustomTotalNum.ai2do";
	$.post(getTotalUrl, $("#multiSearchForm").serialize(), function(result) {
		if (result.success) {
			$(".pathNavNext").html(keyword);
			$(".amountNum").html(result.totalSize);
			var formArray = $("#multiSearchForm").serializeArray();
			var data = convertArray(formArray);
			var totalPage = result.totalPage;
			var actionUrl = $.ctx + "/ci/customersManagerAction!customersList.ai2do?pager.totalPage=" + totalPage;
			var i = totalPage;
			var queryResultListBox = $("#queryResultListBox");
			if(i != 0){
				queryResultListBox.scrollPagination({
					"contentPage" : actionUrl, // the url you are fetching the results
					"contentData" : data, // these are the variables you can pass to the request, for example: children().size() to know which page you are
					"scrollTarget" : $(window),// who gonna scroll? in this example, the full window
					"heightOffset" : 10,
					"currentPage" : 1,
					"totalPage" : totalPage,
					"beforeLoad" : function() { // before load function, you can display a preloader div
						$("#loading").fadeIn();
						//thumbnailDatas = [];
						//customGroupIds = [];
						//customUpdateCycles = [];
					},
					"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
						if ($(window).height() >= $(document).height()){					
							$(window).scroll();
						}
						$(elementsLoaded).fadeInWithDelay();
						i--;
						if(i==0){
							$("#loading").fadeOut("normal", function(){
								if($("#queryResultListBox").attr("scrollPagination") == "disabled"){
									$("#noMoreCustomGroupResults").fadeIn();
								}
							});
						}else{
							$("#loading").fadeOut("normal", function(){});
						}
						//for(var j=0;j<thumbnailDatas.length;j++){
							//var thumbnailData = thumbnailDatas[j];
							//var customGroupId = customGroupIds[j];
							//var customUpdateCycle = customUpdateCycles[j];
							//周期性(有客户群缩略图数据) 显示静态图片
							//if(thumbnailData != null && thumbnailData !="" && customUpdateCycle != 1){
								//showChart(thumbnailData,customGroupId);
							//}else{//一次性  周期性(无客户群缩略图数据) 显示静态图片
								//$('#chart'+customGroupId).addClass("graphOnceShow");
							//}
						//} 
					}
				});
			}else{
				$("#noResultsCustomGroup").show();
				$("#loading").hide();
			}
		} else {
			showAlert(result.msg,"failed");
		}
	});
}
function delOne(customGroupId){
	openConfirm("确认删除该客户群？",customGroupId);
}
function shareOne(cid){
	showAlert("确认共享该客户群?共享之后将不能恢复操作!","confirm",shareCustomer,cid);
}

//删除
function del(customGroupId){
	var actionUrl = $.ctx + "/ci/customersManagerAction!delete.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "ciCustomGroupInfo.customGroupId="+customGroupId,
		success: function(result){
			if(result.success){
				showAlert("" +result.msg,"success");
				loadCustomsList();
			}else{
				showAlert("删除失败：" +result.msg,"failed");
			}
		}
	});
}
//打开确认弹出层
function openConfirm(confirmMsg, id){
	confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
	$("#confirmDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
	$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
function closeConfirmDialog(dialogId, flag,id){
	closeDialog(dialogId);
	if(flag){
		del(id);
	}
}

//修改客户群
function editCustomer(typeId, cycle, cid){
	//if(typeId == 1 && cycle != 1){
		//var url = $.ctx + "/ci/customersManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+cid;
		//window.open(url, "_blank", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	//}else{
		var url = $.ctx + "/ci/customersManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+cid;
		openCreate(url);
	//}
}
//打开共享成功弹出层
function openShareSuccess(customerName,templateName){
	$("#successShareDialog").dialog({
		close: function( event, ui ) {
			loadCustomsList();
		}
	});
	templateName=encodeURIComponent(encodeURIComponent(templateName));
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	$("#successShareDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/shareSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successShareFrame").attr("src", ifmUrl).load(function(){$("#successShareDialog").dialog("open");});
}
//客户群共享弹出层
function openCustomerShare(url){
	$("#shareCustomerDialog").dialog({
		close: function( event, ui ) {
			$("#shareCustomerFrame").attr("src", "");
		}
	});
	var ifmUrl = url;
	$("#shareCustomerFrame").attr("src", ifmUrl).load(function(){});
	$("#shareCustomerDialog").dialog("open");
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//客户群共享
function customerShare(id,name,cityId,createUserId,msg){
	msg=encodeURIComponent(encodeURIComponent(msg));
	name=encodeURIComponent(encodeURIComponent(name));
	var url = "${ctx}/aibi_ci/customers/shareCustomerForm.jsp?id=" + id + "&name=" + name+"&cityId="+ cityId+"&msg="+msg+"&createUserId="+createUserId;
	openCustomerShare(url);
}
function shareCustomer(cid){
	var cname=$("#share_"+cid).attr("c_name");
	var cityId=$("#share_"+cid).attr("c_cityId");
	var createUserId=$("#share_"+cid).attr("c_userId");
	var actionUrl=$.ctx + "/ci/customersManagerAction!shareCustom.ai2do"
	var para={
			"ciCustomGroupInfo.customGroupId":cid,
		 	"ciCustomGroupInfo.customGroupName":cname,
		 	"ciCustomGroupInfo.createCityId":cityId,
		 	"ciCustomGroupInfo.createUserId":createUserId
		};
	$.post(actionUrl, para, function(result){
			if(result.success){
				loadCustomsList();
				showAlert("客户群共享成功！","success");
			}else{
				var msg = result.msg;
				if(msg.indexOf("重名") >= 0){
					customerShare(cid,cname,cityId,createUserId,msg);
				}else{
					showAlert(cmsg,"failed");
				}
			}
		});
}
//保存客户群弹出层
function openCreate(url){
	$("#createDialog").dialog({
		close: function( event, ui ) {
			$("#createFrame").attr("src", "");
		}
	});
	var ifmUrl = url;
	$("#createFrame").attr("src", ifmUrl).load(function(){});
	$("#createDialog").dialog("open");
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//关闭推送弹出层
function closePush(){
	$("#dialog_push_single").dialog("close");
}
//关闭批量推送弹出层
function closePushBatch(){
	$("#dialog_push_batch").dialog("close");
}
//打开保存成功弹出层
function openSuccess(customerName, templateName){
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	templateName = encodeURIComponent(encodeURIComponent(templateName));
	$("#successDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
}

//配置客户群预警
function addAlarmThreshold(customGroupId,customGroupName){
	var customGroupName = encodeURIComponent(encodeURIComponent(customGroupName));
	var ifmUrl = "${ctx}/aibi_ci/customersAlarm/customersAlarmThresholdEdit.jsp?customGroupId="+customGroupId+"&customGroupName="+customGroupName;
	ifmUrl += "&fromPageFlag=1";
	$("#add_alarm_iframe").attr("src", ifmUrl);
	$("#dialog_edit").dialog( "open" );
	$(".tishi").each(function(){
        var toffset=$(this).parent("td").offset();
        var td_height=$(this).parent("td").height();
        $(this).css({top:toffset.top + td_height + 13, left: toffset.left});
    });
}

//单个推送客户群
function pushCustomerGroupSingle(customerGroupId, updateCycle, isPush) {
	var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId=" 
		+ customerGroupId + "&updateCycle=" + updateCycle + "&isPush=" + isPush;
	$('#push_single_alarm_iframe').attr('src', ifmUrl);
	$('#dialog_push_single').dialog('open');
}

//客户群趋势分析明细页面加载方法
function showCustomerTrend2(customGroupId) {
	    //alert(customGroupId+"--");
	    var ifmUrl = "${ctx}/ci/customersTrendAnalysisAction!initCustomersTrendDetail.ai2do?customGroupId="+customGroupId;
	    $('#dialog_detail2').attr('src', ifmUrl);
	    $('#dialog_look2').dialog('open' );
}
function callBack(){
    closeDialog("#dialog_edit");
}

//关闭确认弹出层
function closeDialog(dialogId){
	$(dialogId).dialog("close");
}

function importInit(){
	//调用iframe中的内容
	$("#importFile")[0].contentWindow.submitList();
}
//查看客户群清单表
function queryGroupList(id,dataDate){
	var url = $.ctx + "/ci/customersManagerAction!findGroupList.ai2do?ciCustomGroupInfo.customGroupId="+id+"&ciCustomGroupInfo.dataDate="+dataDate;
	openQueryList(url);
}

//查询客户群清单表弹出层
function openQueryList(url){
	$("#queryListDialog").dialog({
		autoOpen: false,
		width: 700,
		title: "客户群清单预览",
		modal: true,
		resizable:false
	});
	$("#queryListDialog").dialog({
		close: function( event, ui ) {
			$("#queryListFrame").attr("src", "");
		}
	});
	$("#queryListFrame").attr("src", url).load(function(){});
	$("#queryListDialog").dialog("open");
}
//客户群趋势分析明细页面加载方法
function showCustomerTrend(customGroupId) {
	    var ifmUrl = "${ctx}/ci/customersTrendAnalysisAction!initCustomersTrendDetail.ai2do?customGroupId="+customGroupId;
	    $("#dialog_detail").attr("src", ifmUrl);
	    $("#dialog_look").dialog("open");
}

//一次性客户群重新生成
function oneTimeRegenCustomer(customGroupId,carateType){
	if(carateType == 7){//文件创建的不支持
		return;
	}
	var url = "${ctx}/ci/customersManagerAction!checkReGenerateCustomer.ai2do?ciCustomGroupInfo.customGroupId="+customGroupId;;
	$.ajax({
		url: url,
		success: function(result){
			if(result.success){
				showAlert("抱歉，该客户群正在被使用，无法重新生成，请稍后再试","success");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				regenCustomer(customGroupId,carateType);
			}
		}
	});
	
}
//客户群重新生成
function regenCustomer(customGroupId,carateType){
	if(carateType == 7){//文件创建的不支持
		return;
	}
	var url = "${ctx}/ci/customersManagerAction!reGenerateCustomer.ai2do?ciCustomGroupInfo.customGroupId="+customGroupId;;
	$.ajax({
		url: url,
		success: function(result){
			if(result.success){
				showAlert("已提交，请刷新查看","success");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				showAlert(result.msg,"failed");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}
		}
	});
}
//客户群提取模板，打开编辑对话框
function createTemplate(customId,customName,customSceneId){
	customName = encodeURIComponent(encodeURIComponent(customName));
	$("#createTemplateDialog").dialog({
		close: function( event, ui ) {
			$("#createTemplateFrame").attr("src", "");
		}
	});
	$("#createTemplateDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/createTemplate.jsp?customId="+customId+"&customName="+customName+"&sceneId="+customSceneId;
	$("#createTemplateFrame").attr("src", ifmUrl).load(function(){//resetForm();
	});
	$("#createTemplateDialog").dialog("open");
	
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//产品手工匹配
function customMatch(){
	var listTableName = $("#matchListTableName").val();
	var customGroupId = $("#matchCustomGroupId").val();
	var dataDate = $("#matchDataDate").val();
	var customNum = $("#matchCustomNum").val();
	var param = "customGroup.listTableName="+listTableName;
	param = param + "&customGroup.customGroupId="+customGroupId;
	param = param + "&customGroup.customNum="+customNum;
	param = param + "&dataDate="+dataDate;
	param = param + "&fromPageFlag=1";
	param = param + "&tab=match";
	var actionUrl = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(actionUrl, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	$( "#dialog-match" ).dialog( "close" );
}
//产品系统匹配
function sysMatch(){
	var customGroupId = $("#matchCustomGroupId").val();
	var listTableName = $("#matchListTableName").val();
	var data = "customGroup.listTableName="+listTableName;
	data = data +"&customGroup.customGroupId="+customGroupId;
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
function customSysMatch(customGroupId,listTableName,customNum,dataDate,failedListCount){
	if(failedListCount >= 1){
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
				param = param + "&fromPageFlag=1";
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
				$("#matchDataDate").val(dataDate);
				$("#dialog-match").dialog("open");
			}
		}
	});
}

//对比分析连接
function openCustomContrast(customGroupId,listTableName,customNum,dataDate,failedListCount){
	if(failedListCount >= 1){
		showAlert("客户群清单未创建成功","failed");
		return;
	}
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//关联分析连接
function openCustomRel(customGroupId,listTableName,customNum,dataDate,failedListCount) {
	if(failedListCount >= 1){
		showAlert("客户群清单未创建成功","failed");
		return;
	}
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=rel";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//标签微分
function openlabelDeff(customGroupId,listTableName,customNum,dataDate){
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;		
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=tag";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//指标微分
function openIndexDeff(customGroupId,listTableName,customNum,dataDate){
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;		
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=sa";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//显示客户群操作列表
function showOperList(e,ths,customId){
	var selfIndex = $(".resultItemActive").index($(ths));
	$.each($(".resultItemActive"),function(index,item){
		if(index != selfIndex){
		 var $resultItemActiveList= $(item).find(".resultItemActiveList");
		     $resultItemActiveList.hide();
		     $(item).removeClass("resultItemActiveOption");
		}
	});
	
	$('#'+customId).children("ol").children("li:last-child").addClass("end");
	var e=e||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	var _t=$(ths);
	var $resultItemActiveList= $(ths).children(".resultItemActiveList");
	if($resultItemActiveList.is(":visible")){
			$resultItemActiveList.hide();
			_t.removeClass("resultItemActiveOption");
	}else{
		var resultListHeight = $resultItemActiveList.height();
		var _tOffsetHeight= _t.offset().top + _t.height();
		var documentHeight = $(document.body).height();
		var resultHeight = resultListHeight + _tOffsetHeight;
		if(documentHeight<resultHeight){
			$(document.body).height(resultHeight + 20);
		}
	    $resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveOption")});
	}
}
//客户群收藏图标点击事件
function attentionOper(customId){
	var isAttention = $("#isAttention_"+customId).val();
	if(isAttention == 'true'){
		//已经收藏，点击进行取消收藏动作
		var url=$.ctx+'/ci/customersManagerAction!delUserAttentionCustom.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data:{'customGroupId':customId},
	      	success: function(result){
		       	if(result.success) {
		       		$.fn.weakTip({"label":result.message });
		       		//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+customId).val('false');
					$("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
					$("#attentionImg_"+customId).attr("title",'点击收藏客户群');
					$("#isAttentionShow_"+customId).attr("style",'display: none');
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
		});
	}else{
		//没有收藏，点击进行收藏动作触发
		var url=$.ctx+'/ci/customersManagerAction!addUserAttentionCustom.ai2do';
		$.ajax({
			type: "POST",
				url: url,
				data:{'customGroupId':customId},
				success: function(result){
				if(result.success) {
					$.fn.weakTip({"label":result.message });
					//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+customId).val('true');
					$("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
					$("#attentionImg_"+customId).attr("title",'点击取消客户群收藏');
					$("#isAttentionShow_"+customId).attr("style",'display: block');
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
        });
	}
}
</script>
<!--  页面路径导航  -->
<form id="multiSearchForm" >
<input type="hidden" id="isSysRecommendCustomsFlag" name="isSysRecommendCustomsFlag" />
<input type="hidden" id="isCustomGroupMarket" name="isCustomGroupMarket" value="true"/>
	<input type="hidden" id="order" name="pager.order" value="desc"/>
	<input type="hidden" id="orderBy" name="pager.orderBy" value="USECOUNT"/>
<div class="comWidth pathNavBox">
     <span class="pathNavHome"> 系统热门客户群</span>
     <span class="pathNavArrowRight"></span>
     <span class="pathNavNext"></span>
  <span class="amountItem"> 共<span class="amountNum"></span>个客户群 </span>
</div>
<HR width="100%" color="#ccccc" noShade> 
<br>
<!-- 查询结果列表 -->
<div class="comWidth queryResultBox">
	<div class="comWidth queryResultListBox" id="queryResultListBox">
	</div>
	<div id="noMoreCustomGroupResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	<div id="noResultsCustomGroup" class="noResults" style="display:none;">没有客户群信息...</div>
</div>
<!-- 成功窗口 -->
<div id="successDialog" style="overflow:hidden;">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
<!-- 确认窗口 -->
<div id="confirmDialog" style="overflow:hidden;">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<!--“增加标签预警窗口”对应的dialog对应的div -->
<div id="dialog_edit" style="overflow:hidden;">
	<iframe id="add_alarm_iframe" name="add_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
<!-- 推送对应标签 -->
<div id="dialog_push_single" style="overflow:hidden;">
	<iframe id="push_single_alarm_iframe" name="push_single_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
<!-- 客户群趋势分析查看 -->
<div id="dialog_look" style="overflow:hidden;">
	<iframe name="dialog_detail" scrolling="no" allowtransparency="true" src="" id="dialog_detail"  framespacing="0" border="0" frameborder="0" style="width:100%;height:375px"></iframe>
</div>
<!-- 客户群趋势分析查看 -->
<div id="dialog_look2" style="overflow:hidden;">
	<iframe name="dialog_detail2" scrolling="no" allowtransparency="true" src="" id="dialog_detail2"  framespacing="0" border="0" frameborder="0" style="width:100%;height:375px"></iframe>
</div>
<!-- 客户群共享弹出页面 -->
<div id="shareCustomerDialog" style="overflow:hidden;">
	<iframe id="shareCustomerFrame" name="shareCustomerFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:345px"></iframe>
</div>
<!-- 客户群共享成果页面 -->
<div id="successShareDialog" style="overflow:hidden;">
	<iframe id="successShareFrame" name="successShareFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
<div id="queryListDialog" style="display:none; overflow:hidden;">
	<iframe id="queryListFrame" name="queryListFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:310px;"></iframe>
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
			<input type="hidden" id="matchDataDate" />
		</table>
	</div>
	<!-- 按钮区 -->
	<div class="dialog_btn_div">
		<input name="" type="button" id="sysMatch" value="系统自动匹配" class="tag_btn"/>
		<input name="" type="button" id="customMacth" onclick="customMatch();" value=" 跳 过 " class="tag_btn"/>
	</div>		                
</div>
<div id="createTemplateDialog">
	<iframe id="createTemplateFrame" name="createTemplateFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:225px"></iframe>
</div>
<div id="dialog-form" title="导入客户群" style="display:none;"><!-- dialog-form -->
  <iframe id="importFile" scrolling="yes" allowtransparency="true" src="" framespacing="0" border="0"	frameborder="0" style="width: 100%; height: 300px;"></iframe>
	<!-- 按钮区 -->
	<div class="dialog_btn_div" style="position: relative;">
		<%
              if("true".equals(marketingMenu)){
              %>
		    <input id="isAutoMatch" type="checkbox" class="valign_middle"/> 系统自动匹配产品&nbsp;&nbsp;
		<%
              }
		%>
		<input id="saveBtn" name="" type="button" value="保存" onclick="importInit();" class="tag_btn"/>
	</div>
</div><!-- dialog-form end-->
</form>
