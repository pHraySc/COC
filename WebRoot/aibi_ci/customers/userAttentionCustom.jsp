<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(document).ready( function() {
	//工具栏定位
	var bodyWidth = $("body").width();
	var toolTipRight= parseInt((bodyWidth-1005)/2,10)-32 ;
	toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
	$("#toolTip").css("right",toolTipRight);
	$("#shopCart").css("right",parseInt((bodyWidth-1005)/2,10));
	
	//系统自动匹配提示
	$("#dialog-match").dialog({
		dialogClass:"ui-dialogFixed",
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
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 660,
		title: "保存模板",
		modal: true,
		resizable:false
	});
	
	//浙江审批
    $( "#dialog_download_approve" ).dialog({
    	dialogClass:"ui-dialogFixed",
        autoOpen: false,
        width: 480,
        resizable:false,
        title:"下载审批",
        modal: true,
        close:function(){
            $("#dialog_download_approve_detail").attr("src", "");
        }
    });
    
    //浙江审批
    $( "#dialog_commit_approve" ).dialog({
    	dialogClass:"ui-dialogFixed",
        autoOpen: false,
        width: 480,
        resizable:false,
        title:"提交审批",
        modal: true,
        close:function(){
            $("#dialog_commit_approve_detail").attr("src", "");
        }
    });
});
//客户群查询
//var thumbnailDatas = [];
//var customGroupIds = [];
//var customUpdateCycles = [];
function loadCustomList(){
	$(window).unbind("scroll");
	$("#fixedSearchBox").hide();
	$("#queryResultListBox").empty();
	$("#noMoreResults").hide();
	$("#noMoreLabelResults").hide();
	$("#noResultsLabel").hide();
    $("#noMoreResults").hide();
    $("#noResults").hide();
    $("#loading").hide();
    $("#noMoreCustomGroupResults").hide();
    $("#noResultsCustomGroup").hide();

	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/customersManagerAction!findUserAttentionCustomNum.ai2do";
	$.post(getTotalUrl,{},function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			$(".amountNum").html(result.totalSize);
			var i = totalPage;
			if(i != 0){
				var actionUrl = $.ctx + "/ci/customersManagerAction!findUserAttentionCustom.ai2do?pager.totalPage=" + totalPage;
				$("#queryResultListBox").scrollPagination({
					"contentPage" : actionUrl, // the url you are fetching the results
					"contentData" : {
						//"dataScope" : dataScope
					}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
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
						$(elementsLoaded).fadeInWithDelay();
						i--;
						if(i == 0){
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
				$("#noMoreCustomGroupResults").hide();
				$("#noResultsCustomGroup").show();
				$("#loading").hide();
			}
		} else {
			alert(result.msg);
		}
	});
	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity : 1 }, 200);
			delay += 100;
		});
	};
}

function showChart(chartStr,customId){
	var allStr = chartStr;
    if(null != allStr || "" != allStr){
	    var array = allStr.split("|");
	    var categoriesVar = eval('(' + array[0] + ')');
	    if(categoriesVar.length > 6){
	    	categoriesVar.splice(0,categoriesVar.length-6);
	    }
	    var dataVar = eval('(' + array[1] + ')');
	    if(dataVar.length > 6){
	    	dataVar.splice(0,dataVar.length-6);
	    }
	    $('#chart'+customId).highcharts({
	            chart: {
	                type: 'areaspline',
	                spacingTop: 0,
	                spacingRight: -7,
	        		spacingBottom: 0,
	        		spacingLeft: -5,
	                plotBorderWidth: null,
	                plotBackgroundColor: null
	            },
	            title: {
	                text: ''
	            },
	            exporting: {
	                enabled: false  //设置导出按钮不可用        
	            },
	            legend: {
	                layout: 'vertical',
	                align: 'left',
	                verticalAlign: 'top',
	                x: 150,
	                y: 100,
	                floating: true,
	                borderWidth: 0,
	                backgroundColor: '#FFFFFF',
	                enabled: false
	            },
	            xAxis: {
	                categories: categoriesVar,
	                tickmarkPlacement: 'on',//between
	                tickLength: 0,
	                lineWidth: 0,
	                labels:{
	                    formatter: function() {//去掉X轴的刻度显示  
	                        return '';
	                    },
	                        enabled: false
	                     }
	            },
	            yAxis: {
	                title: {
	                    text: ''
	                },
	               
	                gridLineWidth: null,
	                labels:{
	                    formatter: function() {//去掉Y轴的刻度显示  
	                        return '';
	                    },
	                        enabled: false
	                     }
	            },
	            tooltip: {
	                enabled: false,
	                shared: true,
	                valueSuffix: ''
	            },
	            credits: {
	                enabled: false
	            },
	            plotOptions: {
	                areaspline: {
	                    fillOpacity: 1,
	                    dataLabels:{
	                        enabled: false  //在数据点上显示对应的数据值
	                    },
	                    enableMouseTracking: true //取消鼠标滑向触发提示框
	                },
	                series: {
	                   lineColor: '#7DB708'           
	                }
	            },
	            series: [{
	            	name: '用户数',
	                data: dataVar,
	                color:'#F4F8EA',
	                marker: {
	                    enabled: true,
	                    fillColor: '#7DB708',
	                    lineColor: '#FFFFFF',
	                    lineWidth: 2,
	                    radius: 4.5,
	                    symbol: null
	                 }
	            }]
	        });
    	}
}

//客户群关注图标点击事件
function attentionOperCustom(customId){
	var isAttention = $("#isAttention_"+customId).val();
	if(isAttention == 'true'){
		//已经关注，点击进行取消关注动作
		var url=$.ctx+'/ci/customersManagerAction!delUserAttentionCustom.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data:{'customGroupId':customId},
	      	success: function(result){
		       	if(result.success) {
		       		$.fn.weakTip({"tipTxt":result.message,"backFn":function(){
		       			loadCustomList();
		       	    }});
		       		//window.parent.showAlert(result.message,"success",loadCustomList);
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
		});
	}
}

function delOne(customGroupId){
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认删除该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":customGroupId
	},"del") ;
}
function shareOne(cid){
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认共享该客户群?",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":cid
	},"shareCustomer");
}
function cancelShareOne(cid){
	$("#isEditCustom").val("false");
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认取消共享该客户群?",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":cid
	},"cancelShareCustomer");
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
				showAlert("" +result.msg,"success",backMultiCustom);
			}else{
				showAlert("删除失败：" +result.msg,"failed");
			}
		}
	});
}

//客户群共享弹出层
function openCustomerShare(url){
	var dlgObj = dialogUtil.create_dialog("shareCustomerDialog", {
		"title" :  "客户群共享",
		"height": "auto",
		"width" : 660,
		"frameSrc" : url,
		"frameHeight":180,
		"position" : ['center','center'] 
		
	});
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
				showAlert("客户群共享成功！","success",backMultiCustom);
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
function cancelShareCustomer(cid){
	var actionUrl=$.ctx + "/ci/customersManagerAction!cancelShareCustom.ai2do"
	var para={
			"ciCustomGroupInfo.customGroupId":cid
		};
	$.post(actionUrl, para, function(result){
			if(result.success){
				$.fn.weakTip({"tipTxt":"取消客户群共享成功！","backFn":function(){
					backMultiCustom();
				 }});
			}else{
				var msg = result.msg;
				showAlert(msg,"failed");
			}
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
var pushFromSign = false;
//单个推送客户群
function pushCustomerGroupSingle(customerGroupId, updateCycle, isPush, duplicateNum) {
	if(duplicateNum) {
		if(duplicateNum != 0) {
			$.fn.weakTip({"tipTxt":"客户群包含重复记录不能进行推送设置！"});
			return;
		}
	}
	pushFromSign = true;
	var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId=" 
		+ customerGroupId + "&updateCycle=" + updateCycle + "&isPush=" + isPush;
	var dlgObj = dialogUtil.create_dialog("dialog_push_single", {
		"title" :  "客户群推送设置",
		"height": "auto",
		"width" : 730,
		"frameSrc" : ifmUrl,
		"frameHeight":335,
		"position" : ['center','center'] 
	});
}
function pushCustomerGroupConfirm() {
	if($("#dialog_push_single_iframe")[0].contentWindow.validateIsNull()) {
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"确认推送该客户群?",
			 "type":"confirm",
			 "width":500,
			 "height":200
		},"push");
	}
}
//推送提交
function push(){
	var url = '${ctx}/ci/customersManagerAction!pushCustomerAfterSave.ai2do';
	if(pushFromSign) {
		url = false;
	}
	$('#dialog_push_single').dialog('close');
	$("#dialog_push_single_iframe")[0].contentWindow.submitForm(url);
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
	var dlgObj = dialogUtil.create_dialog("queryListDialog", {
		"title" :  "客户群清单预览",
		"height": "auto",
		"width" : 700,
		"frameSrc" : url,
		"frameHeight":328,
		"position" : ['center','center'] 
	}); 
}
//客户群趋势分析明细页面加载方法
function showCustomerTrend(customGroupId) {
    var ifmUrl = "${ctx}/ci/customersTrendAnalysisAction!initCustomersTrendDetail.ai2do?customGroupId="+customGroupId;
    var dlgObj = dialogUtil.create_dialog("dialog_look", {
		"title" :  "客户群清单下载",
		"height": "auto",
		"width" : 980,
		"frameSrc" : ifmUrl,
		"frameHeight":376,
		"position" : ['center','center'] 
		
	});
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

//浙江审批初始化
function showDownloadApprove(listTableName){
    var ifmUrl = "${ctx}/ci/zjDownloadApproveAction!initDownloadApprove.ai2do?&listTableName=" + listTableName;
    $("#dialog_download_approve_detail").attr("src", ifmUrl);
    $("#dialog_download_approve").dialog("open");
}
//浙江审批提交
function showCommitApprove(listTableName, customGroupId) {
    var ifmUrl = "${ctx}/ci/zjDownloadApproveAction!initCommitApprove.ai2do?customGroupId="+ customGroupId + "&listTableName=" + listTableName;
    $("#dialog_commit_approve_detail").attr("src", ifmUrl);
    $("#dialog_commit_approve").dialog("open");
}
//浙江关闭审批提交
function closeCommitApprove(){
	$("#dialog_download_approve").dialog("close");
    $("#dialog_commit_approve").dialog("close");
}

//客户群设置系统推荐与取消系统推荐
var recomFlag = true;  
function changeRecomCustom(ths){
	if(recomFlag){
		recomFlag = false;
		var customId = $(ths).attr("customId");
		var actionUrl = $.ctx + "/ci/customersManagerAction!changeRecommendCustom.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: {"ciCustomGroupInfo.customGroupId" : customId},
			success: function(result){
				if(result.success){
					$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
						backMultiCustom();
						recomFlag = true;
					 }});
				}else{
					recomFlag = true;
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":result.msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					}); 
				}
			}
		});
	}else{
		return false;
	}
}
</script>
<form id="multiSearchForm" >
<div class="comWidth pathNavBox">
     <span class="pathNavHome"> 我收藏的客户群</span>
     <span class="pathNavNext"></span>
  <span class="amountItem"> 共<span class="amountNum"></span>个客户群 </span>
</div>
<HR width="100%" color="#ccccc" noShade> 
<br>
<!-- 查询条件 -->
    <!-- 查询结果列表 -->
<div class="comWidth queryResultBox">
	<div  id="queryResultListBox"></div>
	<div id="loading">正在加载，请稍候 ...</div>
	<div id="noMoreCustomGroupResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	<div id="noResultsCustomGroup" class="noResults" style="display:none;">没有客户群信息...</div>
</div>
<!--“增加标签预警窗口”对应的dialog对应的div -->
<div id="dialog_edit" style="overflow:hidden;">
	<iframe id="add_alarm_iframe" name="add_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
<!-- 客户群趋势分析查看 -->
<div id="dialog_look" style="overflow:hidden;">
	<iframe name="dialog_detail" scrolling="no" allowtransparency="true" src="" id="dialog_detail"  framespacing="0" border="0" frameborder="0" style="width:100%;height:375px"></iframe>
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
<div id="createTemplateDialog" style="overflow-y:hidden;">
	<iframe id="createTemplateFrame" name="createTemplateFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:280px"></iframe>
</div>
</form>

<!-- zhejiang 审批初始页面 -->
<div id="dialog_download_approve" style="overflow:hidden;">
    <iframe name="dialog_download_approve_detail" scrolling="no" allowtransparency="true" src="" id="dialog_download_approve_detail"
            framespacing="0" border="0" frameborder="0" style="width:100%;height:200px"></iframe>
</div>

<!-- zhejiang 审批提交页面 -->
<div id="dialog_commit_approve" style="overflow:hidden;">
    <iframe name="dialog_commit_approve_detail" scrolling="no" allowtransparency="true" src="" id="dialog_commit_approve_detail"
            framespacing="0" border="0" frameborder="0" style="width:100%;height:275px"></iframe>
</div>