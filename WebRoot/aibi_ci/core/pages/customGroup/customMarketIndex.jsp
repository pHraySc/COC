<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户群集市首页</title>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/customMarketIndex.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/customMarketEntrance.js"></script>
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
			<form id="multiSearchForm" class="_groupManageInfo">
				<input class="J_custom_multi_filter" type="hidden" id="order" name="pager.order" />
				<input class="J_custom_multi_filter" type="hidden" id="orderBy" name="pager.orderBy" />
				<input class="J_custom_multi_filter" id="customGroupName" name="ciCustomGroupInfo.customGroupName" type="hidden" value="${ciCustomGroupInfo.customGroupName }"/>
				<input class="J_custom_multi_filter" id="minCustomNum" name="ciCustomGroupInfo.minCustomNum" type="hidden"/>
				<input class="J_custom_multi_filter" id="maxCustomNum" name="ciCustomGroupInfo.maxCustomNum" type="hidden"/>
				<input class="J_custom_multi_filter" id="dateType_" name="ciCustomGroupInfo.dateType" type="hidden"/>
				<input class="J_custom_multi_filter" id="createTypeId" name="ciCustomGroupInfo.createTypeId" type="hidden"/>
				<input class="J_custom_multi_filter" id="updateCycle" name="ciCustomGroupInfo.updateCycle" type="hidden"/>
				<input class="J_custom_multi_filter" id="isPrivate" name="ciCustomGroupInfo.isPrivate" type="hidden"/>
				<input class="J_custom_multi_filter" id="dataStatus" name="ciCustomGroupInfo.dataStatus" type="hidden"/>
				<input class="J_custom_multi_filter" id="isCustomGroupMarket" name="isCustomGroupMarket" type="hidden" value="true"/>
				<input class="J_custom_multi_filter" id="createCityId" name="ciCustomGroupInfo.cityId" type="hidden"/>
				<input class="J_custom_multi_filter" id="isMyCustomMarket" name="ciCustomGroupInfo.isMyCustom" type="hidden"/>
				<input class="J_custom_multi_filter" id="customMarketStartDate" name="ciCustomGroupInfo.startDate" type="hidden" />
				<input class="J_custom_multi_filter" id="customMarketEndDate" name="ciCustomGroupInfo.endDate" type="hidden"/>
				<div class="market-filter-content">
					<div class="market-filter-header">
						<div class="market-filter-header-left fleft">筛选条件：</div>
						<div class="market-filter-header-main fleft">
							<div class="market-filter-header-main-inner">
								<ul class="market-filter-header-main clearfix" id="customFilterItemList"> 
								</ul>
							</div>
						</div>
					</div>
					<div class="market-filter-list">
						<!--TODO 条件筛选器-->
						<div class="market-filter-more-list" id="moreFilter">
							<span class="fleft">更多</span>
							<span class="fleft market-filter-more-list-icon market-navigation-icon"></span>
						</div>
						<div class="market-filter-list-box">
							<div class="market-filter-box-left fleft">地市：</div>
							<div class="market-filter-header-main fleft">
								<div class="market-filter-header-main-inner" id="city">
									<ul class="market-filter-row clearfix J_custom_market_default_filter"> 
										<li class="fleft market-filter-row-item J_market_custom_filter_item active" selectedElement="all">全部</li>
										<c:forEach var="city" items="${cityMap}">   
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="city1_${city.key }">${city.value }</li>
										</c:forEach>
									</ul>
								</div> 
							</div>
						</div>
						<div class="market-filter-list-box">
							<div class="market-filter-box-left fleft">创建方式：</div>
							<div class="market-filter-header-main fleft">
								<div class="market-filter-header-main-inner" id="createType">
									<ul class="market-filter-row clearfix"> 
										<li class="fleft market-filter-row-item J_market_custom_filter_item active" selectedElement="all">全部</li>
										<c:forEach items="${customCreateTypes }" var="customCreateType">
											<li class="fleft market-filter-row-item J_market_custom_filter_item"  id="createTypeName_${customCreateType.createTypeId }">${customCreateType.createTypeName }</li>
										</c:forEach> 
									</ul>
								</div> 
							</div>
						</div>
						<div class="market-filter-list-box">
							<div class="market-filter-box-left fleft">用户群筛选：</div>
							<div class="market-filter-header-main fleft">
								<div class="market-filter-header-main-inner" id="customNum_">
									<ul class="market-filter-row clearfix"> 
										<li class="fleft market-filter-row-item J_market_custom_filter_item J_clear_input active" selectedElement="all">全部</li> 
										<li class="fleft market-filter-row-item J_market_custom_filter_item">1-1000</li>  
										<li class="fleft market-filter-row-item J_market_custom_filter_item">1000-5000</li>  
										<li class="fleft market-filter-row-item J_market_custom_filter_item">5000-25000</li>  
										<li class="fleft market-filter-row-input">
											<p class="fleft filter-input-box">
												<input type="text" class="J_filter_input"/>
											</p>
											<p class="fleft">—</p>
											<p class="fleft filter-input-box">
												<input type="text" class="J_filter_input"/>
											</p>
											<p class="fleft">
												<button type="button" class="filter-button btn_normal J_num_filter_button hidden">规模筛选</button>
											</p>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="more-filter-content hidden" id="moreFilterList">
							<div class="market-filter-list-box">
								<div class="market-filter-box-left fleft">创建时间：</div>
								<div class="market-filter-header-main fleft">
									<div class="market-filter-header-main-inner" id="dateType">
										<ul class="market-filter-row clearfix"> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item J_clear_date_input active" selectedElement="all">全部</li> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="dateType1_1">一天</li>  
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="dateType1_2">一个月</li>  
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="dateType1_3">三个月</li>  
											<li class="fleft market-filter-row-input">
												<p class="fleft">
													<input type="text" class="J_filter_date Wdate" id="wdateBrfore" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'wdateAfter\')}'})"/>
												</p>
												<p class="fleft">—</p>
												<p class="fleft">
													<input type="text" class="J_filter_date Wdate" id="wdateAfter" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'wdateBrfore\')}',maxDate:'%y-%M-%d'})"/>
												</p> 
												<p class="fleft">
													<button type="button" class="filter-button btn_normal J_date_filter_button hidden">时间筛选</button>
												</p>
											</li>
										</ul>
									</div> 
								</div>
							</div>
							<div class="market-filter-list-box">
								<div class="market-filter-box-left fleft">生成周期：</div>
								<div class="market-filter-header-main fleft">
									<div class="market-filter-header-main-inner" id="updateCycle">
										<ul class="market-filter-row clearfix"> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item active" selectedElement="all">全部</li> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="updateCycle1_1">一次性</li>  
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="updateCycle1_3">日周期</li>  
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="updateCycle1_2">月周期</li>  
										</ul>
									</div> 
								</div> 
							</div>
							<div class="market-filter-list-box">
								<div class="market-filter-box-left fleft">是否共享：</div>
								<div class="market-filter-header-main fleft">
									<div class="market-filter-header-main-inner" id="isPrivate">
										<ul class="market-filter-row clearfix"> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item active" selectedElement="all">全部</li> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="isPrivate1_0">共享</li>  
											<li class="fleft market-filter-row-item J_market_custom_filter_item" id="isPrivate1_1">非共享</li>   
										</ul>
									</div> 
								</div> 
							</div>
							<div class="market-filter-list-box">
								<div class="market-filter-box-left fleft">数据状态：</div>
								<div class="market-filter-header-main fleft">
									<div class="market-filter-header-main-inner" id="customDataStatus">
										<ul class="market-filter-row clearfix"> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item active" selectedElement="all">全部</li>
											<c:forEach items="${customDataStatus }" var="customDataStatus_">
												<li class="fleft market-filter-row-item J_market_custom_filter_item" id="customDataStatus1_${customDataStatus_.dataStatusId }">${customDataStatus_.dataStatusName }</li>
											</c:forEach>  
										</ul>
									</div> 
								</div> 
							</div>   
							<div class="market-filter-list-box">
								<div class="market-filter-box-left fleft">用户群范围：</div>
								<div class="market-filter-header-main fleft">
									<div class="market-filter-header-main-inner" id="myCustom1">
										<ul class="market-filter-row clearfix"> 
											<li class="fleft market-filter-row-item J_market_custom_filter_item active" selectedElement="all">全部</li>
											<li class="fleft market-filter-row-item J_market_custom_filter_item J_is_my_custom" id="myCustom_1">我的用户群</li>
										</ul>
									</div> 
								</div> 
							</div>   
						</div>
					</div>
				</div>
			</form>
			<div class="market-customer-list">
				<div class="market-customer-list-inner">
					<div class="customer-list-search-box">
						<span class="fleft">当前位置：</span>
						<span class="fleft" >用户群集市</span>
						<p class="fright market-navigation-icon customer-search-box">
							<input type="text" class="customer-search-box-input J_customer_market_enter" value="查询" id="search-customer" data-val="false" />
							<i class="cus-search-icon J_search_custom_market"></i>
						</p>
					</div>
					<div id="customMarketList" class="market-customer-list-inner">
					</div>
				</div>
			</div>
			<div id="customMarketLoading" style="display:none;">正在加载，请稍候 ...</div>
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
	<input id="userCityId" type="hidden" value="${cityId }"/>
</body>
</html>