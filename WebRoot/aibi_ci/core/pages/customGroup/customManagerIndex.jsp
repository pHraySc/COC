<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/customManagerIndex.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/customManagerEntrance.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/main.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的用户群</title>
</head>
<script type="text/javascript">
$(document).ready( function() {
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

function importClientBtnTip_remove(ts){
	$(ts).remove();
}

var pushFromSign = false;

//单个推送用户群
function pushCustomerGroupSingle(customerGroupId, updateCycle, isPush, duplicateNum) {
	if(duplicateNum) {
		if(duplicateNum != 0) {
			$.fn.weakTip({"tipTxt":"用户群包含重复记录不能进行推送设置！"});
			return;
		}
	}
$("#isEditCustom").val("false");
pushFromSign = true;
var ifmUrl = "${ctx}/core/customGroupOperateAction!prePushCustomerSingle.ai2do?customGroupInfo.customGroupId="
    + customerGroupId + "&updateCycle=" + updateCycle + "&isPush=" + isPush;
var dlgObj = dialogUtil.create_dialog("dialog_push_single", {
    "title" :  "用户群推送设置",
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
			 "txt":"确认推送该用户群？",
			 "type":"confirm",
			 "width":500,
			 "height":200
		},"push"); 
	}
}
//推送提交
function push(){
	var url = '${ctx}/core/customGroupOperateAction!pushCustomerAfterSave.ai2do';
	if(pushFromSign) {
		url = false;
	}
	$("#dialog_push_single_iframe")[0].contentWindow.submitForm(url);
	$('#dialog_push_single').dialog('close');
}
</script>
<body>
<div class="main-list-content-inner">
	<div class="content-COC-list">
		<div class="market-customer-list">
			<div class="market-customer-list-inner">
				<div class="customer-list-search-box">
					<span class="fleft">当前位置：</span>
					<span class="fleft">我的用户群</span>
				</div>
				
				<div class="aibi_search">
			<!-- 查询 -->
			<form id="queryForm" action="" method="post">
				<input  name="ciCustomGroupInfo.customGroupName" type="hidden"  id="customGroupName" class="search_input fleft"/>
				<ul class="clearfix">
					<li>
					<input  type="text" class="search_input fleft" id="keyWordGroupName" data-val="false" value="用户群关键字" />
					</li>
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
					<li style="margin-left:10px;width:40px">
					<a href="javascript:void(0);" id="searchBtn" class="queryBtn-bak">查询</a>
					</li>
					<li style="margin-left:30px;width:40px">
					<a href="javascript:void(0);" id="importCustomBtn" class="impBtn-bak">导入</a>
					</li>
				</ul>
			</form>
			<div class="clear"></div>
		</div>
				
			 <div id="customersTable">
			 </div>	
			</div>
		</div>
	</div> 
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
</body>
</html>