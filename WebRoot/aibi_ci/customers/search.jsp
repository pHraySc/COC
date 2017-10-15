<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	$("#simpleSearchName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	$("#keyWordGroupName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
    //模拟下拉框
    $(".selBtn ,.selTxt").click(function(){
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
    })

    $(".querySelListBox a").click(function(){
    	var selVal = $(this).attr("value");
    	var selHtml = $(this).html();
    	 $(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
    	 $(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
    	 $(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
    	//向隐藏域中传递值
       $(this).parents(".querySelListBox").slideUp();
         return false;
    })
	//搜索区域高级查询
	$(".aibi_search ul li.more a").click(function(){
		var customGroupName =  $.trim($("#customGroupName").val());
		var simpleSearchName =  $.trim($("#simpleSearchName").val());
		var keyWordGroupName =  $.trim($("#keyWordGroupName").val());
		var dataVal = $("#keyWordGroupName").attr("data-val");
		var simpDataVal = $("#simpleSearchName").attr("data-val");
		if($(this).html()=="高级查询"){
			if(simpDataVal != "false"){
				$("#keyWordGroupName").attr("value",simpleSearchName).css("color","#222232");
				$("#keyWordGroupName").attr("data-val","true");
			}else{
				$("#keyWordGroupName").attr("value","").blur();
				$("#keyWordGroupName").attr("data-val","false");
			}
			$(".aibi_search ul").eq(0).hide();
			$(".aibi_search ul").eq(1).show();
		}else{
			if(dataVal != "false"){
				$("#simpleSearchName").val(keyWordGroupName).css("color","#222232");
				$("#simpleSearchName").attr("data-val","true");
			}else{
				$("#simpleSearchName").val("").blur();
				$("#simpleSearchName").attr("data-val","false");
			}
			$(".aibi_search ul").eq(0).show();
			$(".aibi_search ul").eq(1).hide();
		}
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
	 $("#periodSel1").multiselect({
         multiple: false,
         header: false,
         selectedList: 1,
         minWidth:119,
         height:"auto"
     });
	 $("#periodSel2").multiselect({
         multiple: false,
         header: false,
         selectedList: 1,
         height:"auto"
     });
	 $("#periodSel3").multiselect({
         multiple: false,
         header: false,
         selectedList: 1,
         height:"auto"
     });
	onLoadFirst();
	$("#_customer").addClass("menu_on");
	$(".no9").parent("li").addClass("top_menu_on");
	$(".no2").parent("li").removeClass("top_menu_on");
	$(".no4").parent("li").removeClass("top_menu_on");
	$("#add").click(function(){
		var url = $.ctx + "/aibi_ci/customers/selectLabel.jsp";
		window.open(url, "_blank", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	});

	$( "#dialog_edit" ).dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 730,
		title:"新增客户群预警",
		modal: true,
		resizable:false
	});
	//模糊查询
	$('#searchBtn').click(function() {
		if(!$("#deleteButton").hasClass("disable")){
			$("#deleteButton").addClass("disable");
		}
		$("#isSimpleSearch").val("false");
		search();
	});
	
});
function pagetable(){
	var paramStr = "";
	if($("#isSimpleSearch").val() == 'true'){
		var searchValue = $('#customGroupName').val();
		paramStr = {"ciCustomGroupInfo.customGroupName" : searchValue};
	}else{
		paramStr = $("#queryForm").serialize();
	}
	var actionUrl = 'customersManagerAction!search.ai2do?'+$("#pageForm").serialize();
	$("#customersTable").page( {url:actionUrl,param:paramStr,objId:'customersTable',callback:pagetable }
	);
	/*if($("#customersTable").find("tr").size()>1){
		$("#customersTable table").tablesorter({
			sortList: [[1,0]],
			headers: {0: { sorter: false} }
		});
	}*/
	$(".tableStyle").datatable();
	//checkDelete();
	checkPush();
}
function search(delFlag){
	var actionUrl = "";
    if(delFlag){
    	$("#isSimpleSearch").val("true");
    	actionUrl = 'customersManagerAction!search.ai2do?'+$("#searchValue").val();
	}else{
		$("#isSimpleSearch").val("false");
		var keyWord = $("#keyWordGroupName").val();
        if ("false" == $("#keyWordGroupName").attr("data-val")) {
        	keyWord = "";
        }
		$("#customGroupName").val(keyWord);
		$("#updateCycle").val($("#updateCycle_").val());
		$("#isPrivate").val($("#isPrivate_").val());
		$("#createTypeId").val($("#createTypeId_").val());
		$("#dataStatus").val($("#dataStatus_").val());
		$("#searchValue").val($("#pageForm").serialize());
		actionUrl = 'customersManagerAction!search.ai2do?'+$("#pageForm").serialize();
	}
	$.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: $("#queryForm").serialize(),
        success:function(html){
            $("#customersTable").html("");
            $("#customersTable").html(html);
            pagetable();
        }
    });
}
var pushFromSign = false;
//导入保存完客户群之后推送设置
function pushCustomGroup(customerGroupId) {
	pushFromSign = false;
	var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId=" 
		+ customerGroupId;
	$("#dialog_form").dialog("close");
	var dlgObj = dialogUtil.create_dialog("dialog_push_single", {
		"title" :  "查看",
		"height": "auto",
		"width" : 730,
		"frameSrc" : ifmUrl,
		"frameHeight":335,
		"position" : ['center','center'] 
	});
}
function pushCustomerGroupConfirm() {
	if($("#dialog_push_single_iframe")[0].contentWindow.validateIsNull()) {
		var dlgObj = commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"确认推送该客户群？",
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
	$("#dialog_push_single_iframe")[0].contentWindow.submitForm(url);
	$('#dialog_push_single').dialog('close');
}
function checkBoxAll(obj){
	$(".checkBox").each(function(){
		if($(this).prop("disabled") != true){
			$(this).prop("checked", obj.checked);
		}
	});
	checkDelete();
	checkPush();
}
//删除全部
function delAll(){
	var customIds = "";
	$(".checkBox").each(function(){
		var checked = this.checked;
		if(checked){
			customIds += (customIds == "" ? "" :',') + $(this).val();
		}
	});
	if(customIds != ""){
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"确认删除所选客户群？",
			 "type":"confirm",
			 "width":500,
			 "height":200,
			 "param":customIds
		},"del") ; 
	}
}
function checkDelete(){
	checkBindEvent('checkBox:checked', 'icon_button_delete', 'disable');
}

//检测批量推送功能按钮是否能用
function checkPush() {
	checkBindEvent('checkBox:checked', 'icon_button_push', 'disable');
}

function checkBindEvent(checkBox, el, css) {
	if($("." + checkBox).size()>0){
		$("." + el).removeClass(css).removeAttr("title");	
	}else{
		$("." + el).addClass(css).attr("title","请至少选择一条记录！");
	}
}

function delOne(customGroupId){
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认删除该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":customGroupId
	},"del");
}
//删除
function del(customGroupId){
	$("#isEditCustom").val("false");
	var actionUrl = $.ctx + "/ci/customersManagerAction!delete.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "ciCustomGroupInfo.customGroupId="+customGroupId,
		success: function(result){
			if(result.success){
				$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
					$("input[name='pager.totalSize']").val(0);
					backMultiCustom();
		 		}});
			}else{
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":"删除失败：" +result.msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				}); 
			}
		}
	});
}
//简单查询
function simpleSearch(){
	if(!$("#deleteButton").hasClass("disable")){
		$("#deleteButton").addClass("disable");
	}
	$("#isSimpleSearch").val("true");
	var serachStr = "";
	var simpDataVal = $("#simpleSearchName").attr("data-val");
	if(simpDataVal == "false"){
		serachStr = "";
    } else {
         serachStr = $.trim($("#simpleSearchName").val());
    }
	$("#customGroupName").val(serachStr);
	$("#updateCycle").val($("#updateCycle_").val());
	$("#isPrivate").val($("#isPrivate_").val());
	$("#createTypeId").val($("#createTypeId_").val());
	$("#dataStatus").val($("#dataStatus_").val());

	$("input[type=reset]").trigger("click");
	$("#searchValue").val($("#pageForm").serialize());
	var actionUrl = 'customersManagerAction!search.ai2do';
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: {
			"ciCustomGroupInfo.customGroupName":serachStr
		},
        success:function(html){
            $("#customersTable").html("");
            $("#customersTable").html(html);
            pagetable();
        }
    })
}
function onLoadFirst(){
	var actionUrl = 'customersManagerAction!search.ai2do';
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: {},
        success:function(html){
            $("#customersTable").html("");
            $("#customersTable").html(html);
            pagetable();
        }
    })
}
//修改客户群
function editCustomer(typeId, cycle, cid){
	$("#isEditCustom").val("true");
	//if(typeId == 1 && cycle != 1){
		//var url = $.ctx + "/ci/customersManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+cid;
		//window.open(url, "_blank", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	//}else{
		var url = $.ctx + "/ci/customersManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+cid;
		openCreate(url);
	//}
}
//修改客户群进入标签计算中心
function editCustomerOpenCalculate(cid){
	$("#edit_custom_input").val(cid);
	$("#edit_custom_form").submit();
	//var url = "${ctx}/ci/ciIndexAction!findCalculateCenter.ai2do?ciCustomGroupInfo.customGroupId="+cid;
	//window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//打开共享成功弹出层
function openShareSuccess(customerName,templateName){
	templateName=encodeURIComponent(encodeURIComponent(templateName));
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	var ifmUrl = "${ctx}/aibi_ci/customers/shareSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	var dlgObj = dialogUtil.create_dialog("successShareDialog", {
		"title" :  "系统提示",
		"height": "auto",
		"width" : 550,
		"frameSrc" : ifmUrl,
		"frameHeight":265,
		"position" : ['center','center']
	},"simpleSearch");
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
				$.fn.weakTip({"tipTxt":"客户群共享成功！","backFn":function(){
					simpleSearch();
			 	}});
			}else{
				var msg = result.msg;
				if(msg.indexOf("重名") >= 0){
					customerShare(cid,cname,cityId,createUserId,msg);
				}else{
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					}); 
				}
			}
		});
}
//保存客户群弹出层
function openCreate(url){
	var dlgObj = dialogUtil.create_dialog("createDialog", {
		"title" :  "客户群修改",
		"height": "auto",
		"width" : 660,
		"frameSrc" : url,
		"frameHeight":345,
		"position" : ['center','center'] 
	},function(){
		$("#isEditCustom").val("false");
	});
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#createDialog").dialog("close");
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
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	var dlgObj = dialogUtil.create_dialog("successDialog", {
		"title" :  "系统提示",
		"height": "auto",
		"width" : 550,
		"frameSrc" : ifmUrl,
		"frameHeight":265,
		"position" : ['center','center'] 
	},function(){
		var url = "${ctx}/ci/customersManagerAction!search.ai2do?initOrSearch=init";
		window.location.href = url;
	});
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
function pushCustomerGroupSingle(customerGroupId, updateCycle, isPush, duplicateNum) {
	if(duplicateNum) {
		if(duplicateNum != 0) {
			$.fn.weakTip({"tipTxt":"客户群包含重复记录不能进行推送设置！"});
			return;
		}
	}
	$("#isEditCustom").val("false");
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
function callBack(){
    closeDialog("#dialog_edit");
}

//关闭确认弹出层
function closeDialog(dialogId){
	$(dialogId).dialog("close");
}

function importInit(){
	//调用iframe中的内容
	$("#dialog_form_iframe")[0].contentWindow.submitList();
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
//点击客户群名称 弹出客户群基本信息以及清单信息
function queryGroupInfo(id,customGroupName){
	var url = $.ctx + "/ci/customersManagerAction!findGroupInfo.ai2do?ciCustomGroupInfo.customGroupId="+id;
	openQueryGroupInfo(url,customGroupName);
}

//查询客户群清单表弹出层
function openQueryGroupInfo(url,customGroupName){
	var dlgObj = dialogUtil.create_dialog("queryGroupInfoDialog", {
		"title" :  commonUtil.trim(customGroupName+" 客户群的详情","g"),
		"height": "auto",
		"width" : 905,
		"frameSrc" : url,
		"frameHeight":500,
		"position" : ['center','center'] 
	}); 
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
//显示客户群操作列表
function showOperCustomList(e,ths,customId){
	var selfIndex = $(".resultItemActive").index($(ths));
	$.each($(".resultItemActive"),function(index,item){
		if(index != selfIndex){
		 var $resultItemActiveList= $(item).find(".resultItemActiveList");
		     $resultItemActiveList.hide();
		     $(item).removeClass("resultItemActiveOption");
		}
	 })
	if($('#'+customId).children("ol").children("li").length == 0){
		var createCityId = $(ths).attr("createCityId");
		var createUserId = $(ths).attr("createUserId");
		var isPrivate = $(ths).attr("isPrivate");
		var isContainLocalList = $(ths).attr("isContainLocalList");
		var userId = "${userId}";
		var cityId = "${cityId}";
		var root = "${root}";
		if(isPrivate == 0){
			if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
				$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
			}else{
				$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，无操作！" });
			}
		}else{
			if(createUserId != userId){
				$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
			}else{
				$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，无操作！" });
			}
		}
	}else{ 
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
}
function showSqlInfo(listTableName){
	var url = $.ctx + "/ci/customersManagerAction!findSqlInfoDialog.ai2do?ciCustomListInfo.listTableName="+listTableName;
	openSqlInfo(url,listTableName);
}
//查询客户群清单表弹出层
function openSqlInfo(url,listTableName){
	dialogUtil.create_dialog("querySqlInfoDialog", {
		"title" :  listTableName,
		"height": "auto",
		"width" : 900,
		"frameSrc" : url,
		"frameHeight":400,
		"position" : ['center','center']
	});
}
function customValidate2(customGroupId,typeId,isEditCustomFlag){
	var url = $.ctx + '/ci/ciIndexAction!findCusotmValidate.ai2do?ciCustomGroupInfo.customGroupId='+customGroupId;
	$.ajax({
		url: url,
		type: "POST",
		async: false,
		success: function(result){
			if(result.success){
				addShopCart(customGroupId,typeId,isEditCustomFlag);
			}else{
				$.fn.weakTip({"tipTxt":result.msg });
			}
		}
	});
}
//一次性客户群重新生成
function oneTimeRegenCustomer(customGroupId,createType){
	$("#isEditCustom").val("false");
	if(createType == 7){//文件创建的不支持
		return;
	}	
	var url = "${ctx}/ci/customersManagerAction!checkReGenerateCustomer.ai2do?ciCustomGroupInfo.customGroupId="+customGroupId;;
	$.ajax({
		url: url,
		success: function(result){
			if(result.success){
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":"抱歉，该客户群正在被使用，无法重新生成，请稍后再试",
					 "type":"success",
					 "width":500,
					 "height":200
				}); 
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				regenCustomer(customGroupId,createType);
			}
		}
	});
}
//客户群重新生成
function regenCustomer(customGroupId,createType,listTableName){
	$("#isEditCustom").val("false");
	if(createType == 7){//文件创建的不支持
		return;
	}
	var url = "${ctx}/ci/customersManagerAction!reGenerateCustomer.ai2do";
	if (!listTableName) {
		listTableName = "";
	}
	$.ajax({
		url: url,
		data:{
			"ciCustomGroupInfo.customGroupId":customGroupId,
			"listTableName":listTableName
		},
		success: function(result){
			if(result.success){
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":"已提交，请刷新查看",
					 "type":"success",
					 "width":500,
					 "height":200
				});
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":result.msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				});
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}
			search();
		}
	});
}

//客户群设置系统推荐与取消系统推荐
var recomFlag = true;  
function changeRecomCustom(ths){
	$("#isEditCustom").val("false");
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
function shareOne(cid){
	$("#isEditCustom").val("false");
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认共享该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":cid
	},"shareCustomer");
}
function cancelShareOne(cid){
	$("#isEditCustom").val("false");
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认取消共享该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":cid
	},"cancelShareCustomer");
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
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				});
			}
	});
}
//导入加提示框
var cookieNums = getCookieByName("IMPORT_NUMS_COOKIE_${userId}");
if(cookieNums>1){
	$("#importClientBtnTipMy").hide();	
}else{
	$("#importClientBtnTipMy").show();
}
function importClientBtnTip_remove(ts){
	$(ts).remove();
}
</script>
<!--<div style="height:40px;"></div>-->
	<div class="comWidth pathNavBox">
	     <span class="pathNavHome"  style="cursor:default;"> 我的客户群</span>
	     <span class="pathNavArrowRight" id="pathNavArrowRightMyCustom"></span>
	     <span class="pathNavNext" id="pathCustomText"></span>
	  <span class="amountItem"> 共<span class="amountNum" id="totalSizeNum"></span>个客户群 </span>
	</div>
    <div class="comWidth newMsgCenterBox">
		<!--<jsp:include page="/aibi_ci/customers/customerMain.jsp"/>-->
		<div class="aibi_search">
			<!-- 简单查询 -->
			<ul class="clearfix">
				<li>
				<input type="text"  id="simpleSearchName" class="search_input fleft" data-val="false"  value="模糊查询" />
				</li>
				<li style="margin-left:10px;width:90px">
				<a href="javascript:void(0);" id="simpleSearchBtn" class="queryBtn-bak" onclick="simpleSearch()" >查询</a>
				</li>
		        <li class="more"><a href="javascript:void(0);">高级查询</a></li>
			</ul>
			<!-- 高级查询 -->
			<form id="queryForm" action="" method="post">
				<input  name="ciCustomGroupInfo.customGroupName" type="hidden"  id="customGroupName" class="search_input fleft"/>
				<ul class="clearfix" style="display:none;">
					<li><label  class="fleft">客户群关键字：</label>
					<input  type="text" class="search_input fleft" id="keyWordGroupName" data-val="false" value="模糊查询" />
					</li>
					<%-- <li>
					<span>客户群分类：</span>
					<select name="ciCustomGroupInfo.sceneId" id="periodSel4" multiple="multiple">
							<option value="all" selected="selected">  所有分类  </option>
							<c:forEach items="${dimScenes }" var="dimScens">
							   <c:if test="${dimScens.sceneId > 0 }">
									<option value="${dimScens.sceneId }" selected="selected">${dimScens.sceneName }</option>
			                   </c:if>
							</c:forEach>
					</select>
					</li> --%>
					<!-- <li style="width:350px">
						<span class="line2_span">创建时间：</span>
						<input name="ciCustomGroupInfo.startDate" id="startDate" readonly type="text" class="aibi_input aibi_date"  
						onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}" 
						value="${ciCustomGroupInfo.startDate}"/> -- 
						<input name="ciCustomGroupInfo.endDate" id="endDate" readonly type="text" class="aibi_input aibi_date"  
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" 
						value="${ciCustomGroupInfo.endDate}"/>
					</li> -->
					<!--  
					<li>
						<span class="line2_span">使用的标签：</span><input name="ciCustomGroupInfo.labelName" id="labelName" type="text" class="aibi_input" value="${ciCustomGroupInfo.labelName}"/>
						<div id="divPop1" class="x-form-field"><ul></ul></div>
					</li>
					-->
					<%-- 列表中没有显示的该列地方
					<li>
						<span class="line2_span">模板名称：</span><input name="ciCustomGroupInfo.templateName" id="labelName" type="text" class="aibi_input" value="${ciCustomGroupInfo.templateName}"/>
					</li> --%>
					<li>
					<div class="fleft formQuerySelBox">
			      	  <div class="selTxt">所有创建方式</div>
			      	  <a href="javascript:void(0);" class="selBtn"></a>
			      	  <div class="querySelListBox">
			      	     <input type="hidden"  value="" id="createTypeId_"/>
			      	  	 <dl>
			      	  	 	<dd>
			      	  	 		<a href="javascript:void(0);"  value="">所有创建方式</a>
			      	  	 		<c:forEach items="${customCreateTypes}" var="customCreateType" varStatus="st">
			      	  	 			<a href="javascript:void(0);" value="${customCreateType.createTypeId }">${customCreateType.createTypeName }</a>
			      	  	 		</c:forEach>
			      	  	 	</dd>
			      	  	 </dl>
			      	  </div>
					</div>
					<input name="ciCustomGroupInfo.createTypeId" id="createTypeId" type="hidden"/>
					</li>
					<li>
					<div class="fleft formQuerySelBox">
			      	  <div class="selTxt">所有数据状态</div>
			      	  <a href="javascript:void(0);" class="selBtn"></a>
			      	  <div class="querySelListBox">
			      	     <input type="hidden"   value="" id="dataStatus_"/>
			      	  	 <dl>
			      	  	 	<dd>
			      	  	 		<a href="javascript:void(0);"  value="">所有数据状态</a>
			      	  	 		<c:forEach items="${customDataStatus}" var="customDataStatus_" varStatus="st">
			      	  	 			<a href="javascript:void(0);" value="${customDataStatus_.dataStatusId }">${customDataStatus_.dataStatusName }</a>
			      	  	 		</c:forEach>
			      	  	 	</dd>
			      	  	 </dl>
			      	  </div>
					</div>
					<input name="ciCustomGroupInfo.dataStatus" id="dataStatus" type="hidden"/>
					</li>
					<li>
					<div class="fleft formQuerySelBox">
			      	  <div class="selTxt">全部生成周期</div>
			      	  <a href="javascript:void(0);" class="selBtn"></a>
			      	  <div class="querySelListBox">
			      	     <input type="hidden" value="" id="updateCycle_"/>
			      	  	 <dl>
			      	  	 	<dd>
			      	  	 		<a href="javascript:void(0);"  value="">全部生成周期</a>
			      	  	 		<a href="javascript:void(0);" value="1">一次性</a>
			      	  	 		<a href="javascript:void(0);" value="3">日周期</a>
			      	  	 		<a href="javascript:void(0);" value="2">月周期</a>
			      	  	 	</dd>
			      	  	 </dl>
			      	  </div>
					</div>
					<input name="ciCustomGroupInfo.updateCycle" id="updateCycle" type="hidden"/>
					</li>
					<li>
					<div class="fleft formQuerySelBox">
			      	  <div class="selTxt">全部共享状态</div>
			      	  <a href="javascript:void(0);" class="selBtn"></a>
			      	  <div class="querySelListBox">
			      	     <input type="hidden"  id="isPrivate_"  value=""/>
			      	  	 <dl>
			      	  	 	<dd>
			      	  	 		<a href="javascript:void(0);"  value="">全部共享状态</a>
			      	  	 		<a href="javascript:void(0);" value="1">非共享</a>
			      	  	 		<a href="javascript:void(0);" value="0">共享</a>
			      	  	 	</dd>
			      	  	 </dl>
			      	  </div>
					</div>
					<input name="ciCustomGroupInfo.isPrivate" id="isPrivate" type="hidden"/>
					</li>
					<li style="margin-left:10px;width:90px">
					<a href="javascript:void(0);" id="searchBtn" class="queryBtn-bak">查询</a>
					</li>
			        <li class="more"><a href="javascript:void(0);">简单查询</a></li>
				</ul>
			</form>
			<div class="clear"></div>
		</div>
		<div class="aibi_button_title">
			<ul>
				<!-- <li id="add"><a href="javascript:void(0);" class="icon_button_add"><span>增加</span></a></li> -->
				<li><a href="#" class="icon_button_delete disable" onclick="delAll()" title="请至少选择一条记录！" id="deleteButton"><span>删除</span></a></li>
				<li class="noborder"><a href="javascript:void(0);" class="icon_button_import"><span>导入</span></a></li>
				<!-- <li class="noborder"><a href="#" class="icon_button_push long_100 disable" title="请至少选择一条记录！"><span>推送设置</span></a></li> -->
			</ul>
		</div>
		<!-- 批量推送对应标签 -->
		<div id="dialog_push_batch" style="display:none;">
			<iframe id="push_batch_alarm_iframe" name="push_batch_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
		</div>
		<!-- area_operation end -->
		<!-- area_data start -->
		<div class="aibi_area_data">
			 <div class="content" >
			 <div id="customersTable"></div>
			</div>
		</div>
		<!-- area_data start -->
		<script type="text/javascript">
		$(function(){
			//重新加载
			function reloadGrid(){
				$("#groupGrid").datagrid("reload");
			}
			var havePushFun = '${havePushFun}';
			/* if(havePushFun == 0) {
				$('li.noborder').hide();
				$('li.noborder').prev().addClass('noborder');
			} */	
			$('#dialog_push_batch').dialog({
				autoOpen	: false,
				width		: 730,
				title		: '批量客户群推送',
				modal		: true,
				resizable	: false
			});
			aibiTableChangeColor(".showTable");
			//打开导入输入框
			$(".icon_button_import").click(function() {
				var improtInitUrl = "${ctx}/ci/customersFileAction!importInit.ai2do";
				$("#isEditCustom").val("false");
				var dlgObj = dialogUtil.create_dialog("dialog-form", {
					"title" :  "导入创建客户群",
					"height": "auto",
					"width" : 680,
					"frameSrc" : improtInitUrl,
					"frameHeight":550,
					"position" : ["center","center"] 
				},"backMultiCustom").focus();
				//$('.dialog_btn_div').show();
			});

			$('.icon_button_push').click(function() {
				var labelIds = '';
				var index = 0;
				var sign = false;
				$('.checkBox').each(function(){
					var checked = this.checked;
					if(checked){
						sign = true;
						labelIds += (labelIds == '' ? '' : '&ciCustomGroupInfoList[' + index 
								+ '].customGroupId=') + $(this).val();
						index++;
					}
				});
				if(!sign) return;
				
				if(labelIds != ''){
					labelIds = '?ciCustomGroupInfoList[0].customGroupId=' + labelIds;
				}

				//一下代码为当用户批量选择客户群有不成功的时候提示用户
				//根据用户需要 如果 用户需要则打开注释的代码即可
				/*var haveSuccessUrl = '${ctx}/ci/customersManagerAction!havaSuccessCustomGroup.ai2do' + labelIds;
				var resultData = $.ajax({
					async		: false,
					url			: haveSuccessUrl
				}).responseText;
				var resultDataObj = eval('(' + resultData + ')');
				var result = resultDataObj.msg;
				if(!resultDataObj.success) {
					showAlert(result,"failed");
					return;
				}*/
				
				var ifmUrl = '${ctx}/ci/customersManagerAction!prePushCustomerBatch.ai2do' + labelIds;
				var dlgObj = dialogUtil.create_dialog("dialog_push_batch", {
					"title" :  "批量客户群推送",
					"height": "auto",
					"width" : 730,
					"frameSrc" : ifmUrl,
					"frameHeight":335,
					"position" : ['center','center'] 
				});
				//$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
			});
		});
		</script>
	</div><!-- mainCT end -->
    <div class="clear"></div>
<input type="hidden" id="searchValue" value=""/>
<!--“增加标签预警窗口”对应的dialog对应的div -->
<div id="dialog_edit">
	<iframe id="add_alarm_iframe" name="add_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:370px"></iframe>
</div>
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
<div style="height:45px;"></div>
