<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户群推送设置</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<style >
.disposable_push_platform ul , .cycle_push_platform ul , .day_cycle_push_platform ul{
	width:600px;
	padding-top:2px;
}
.disposable_push_platform ul li , .cycle_push_platform  ul li , .day_cycle_push_platform ul li{
	float:left; 
	white-space: nowrap; 
	word-break:keep-all;
	height:20px;
	line-height:20px; 
	text-align:left;
	text-indent:10px; 
	margin-bottom:10px; 
	margin-right:15px;
	overflow:hidden;
  }
.disposable_push_platform input, .cycle_push_platform input , .day_cycle_push_platform input,{float:left;margin-top:4px;}
</style>
</head>
<body>
<form id="pushForm" method="post" action="" enctype="multipart/form-data">
				<div class="dialog_table">
					<table width="100%" class="showTable  new_tag_table" cellpadding="0" cellspacing="0">
						<tr>
							<th width="110px">推送方式：</th>
							<td>
								<div class="new_tag_selectBox">
									<input type="hidden" id="customGroupIdHidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}" />
									<span id="selectCycle">
										<c:if test="${isPush == 1}">
											<input type="radio" id="disposableCreate" class="push_type_radio valign_middle" name="pushCycle"  value="1" />&nbsp;&nbsp;一次性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</c:if>
										<c:if test="${isPush != 1}">
											<input type="radio" id="disposableCreate" class="push_type_radio valign_middle" name="pushCycle"  value="1" checked />&nbsp;&nbsp;一次性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</c:if>
										
										<c:if test="${ciCustomGroupInfo.updateCycle == 3 && ciCustomGroupInfo.createTypeId != 12}">
											<c:if test="${isPush == 1}">
												<input type="radio" id="dayCreate" class="push_type_radio valign_middle" name="pushCycle"  value="3" checked/>&nbsp;&nbsp;日周期&nbsp;
											</c:if>
											<c:if test="${isPush != 1}">
												<input type="radio" id="dayCreate" class="push_type_radio valign_middle" name="pushCycle"  value="3" />&nbsp;&nbsp;日周期&nbsp;
											</c:if>
										</c:if>
										
										<c:if test="${ciCustomGroupInfo.updateCycle == 2 && ciCustomGroupInfo.createTypeId != 12}">
											<c:if test="${isPush == 1}">
												<input type="radio" id="monthCreate" class="push_type_radio valign_middle" name="pushCycle"  value="2" checked/>&nbsp;&nbsp;月周期&nbsp;
											</c:if>
											<c:if test="${isPush != 1}">
												<input type="radio" id="monthCreate" class="push_type_radio valign_middle" name="pushCycle"  value="2" />&nbsp;&nbsp;月周期&nbsp;
											</c:if>
										</c:if>
										
									</span>
								</div>
							</td>
							<td></td>
						</tr>
						<tr>
							<th style="vertical-align:top;">推送平台：</th>
							<td>
								<div class="disposable_push_platform">
								      <ul>
										<c:forEach items="${ciSysInfoList}" var="po" varStatus="st">
										   <li>
											  <input type="checkbox" id="ciSysInfoList${st.index }"  class="platform_checkbox valign_middle" name="sysIds"  value=<c:out value="${po.sysId}"></c:out> />
											  <label for="ciSysInfoList${st.index }" ><c:out value="${po.sysName}"></c:out></label>
										   </li>
										</c:forEach>
									  </ul>
								</div>
								<c:if test="${ciCustomGroupInfo.updateCycle == 3}">
								<div class="day_cycle_push_platform">
								    <ul>
										<c:forEach items="${sysInfoCycleList}" var="po" varStatus="st">
										   <li>
											<input type="checkbox" id="sysInfoCycleList${st.index }" class="platform_checkbox valign_middle" name="sysIds"  value=<c:out value="${po.sysId}"></c:out> />
											<label for="sysInfoCycleList${st.index}"><c:out value="${po.sysName}"></c:out></label>
										   </li>
										</c:forEach>
									 </ul>
								</div>	
								</c:if>
								<c:if test="${ciCustomGroupInfo.updateCycle == 2}">
								<div class="cycle_push_platform">
								    <ul>
										<c:forEach items="${sysInfoCycleList}" var="po" varStatus="st">
										   <li>
											<input type="checkbox" id="sysInfoCycleList${st.index }" class="platform_checkbox valign_middle" name="sysIds"  value=<c:out value="${po.sysId}"></c:out> />
											<label for="sysInfoCycleList${st.index }"><c:out value="${po.sysName}"></c:out></label>
										   </li>
										</c:forEach>
									 </ul>
								</div>
								</c:if>
							</td>
						</tr>
					</table>
				</div>
				<!-- 按钮区 -->
				<!-- <div class="dialog_btn_div">
					<input id="saveBtn" name="" type="button" value="确定" class="tag_btn"/>
				</div> -->
				<div class="btnActiveBox">
				    <a href="javascript:void(0);" class="ensureBtn" id="saveBtn">确定</a>
				</div>
		 </form>
<script type="text/javascript">
$(document).ready(function(){
	var _isSuccess = '${resultMap["success"]}';
	var _msg = '${resultMap["msg"]}';
	if(_isSuccess == 'false'){
		parent.showAlert(_msg,"failed",parent.closePush);
		parent.multiCustomSearch();
	}
});
var isPush = '${isPush}';
var sysIds = [];
var pushCycle = '${ciCustomGroupInfo.updateCycle}';
$('.cycle_push_platform').hide();
$('.day_cycle_push_platform').hide();
$('.disposable_push_platform').show();
if(isPush == 1) {
	$('.disposable_push_platform').hide();
	if(pushCycle == 2) {
		$('.cycle_push_platform').show();
	} else {
		$('.day_cycle_push_platform').show();
	}
	
	var cId = $('#customGroupIdHidden').val();
	var getPushedCycleUrl = '${ctx}/ci/customersManagerAction!findPushedCycle.ai2do';
	$.post(getPushedCycleUrl, 'ciCustomGroupInfo.customGroupId=' + cId, function(result) {
		if(result.success){
			sysIds = result.msg.split(',');
			for(var i=0; i<sysIds.length; i++) {
				if(pushCycle == 2) {
					$('.cycle_push_platform input[type=checkbox]').each(function() {
						if(sysIds[i] == this.value) {
							this.checked = true;
						}
					});
				} else {
					$('.day_cycle_push_platform input[type=checkbox]').each(function() {
						if(sysIds[i] == this.value) {
							this.checked = true;
						}
					});
				}
			}
		}else{
			var msg = result.msg;
			parent.showAlert(msg,"failed");
		}
	});
}

$("#saveBtn").bind("click", function(){
	//submitForm();
	parent.pushCustomerGroupConfirm();
});
$('.push_type_radio').bind('click', function() {
	var checkedVal = $('.push_type_radio:checked').val();
	$('.platform_checkbox').attr('checked', false);
	if(checkedVal == 1) {
		$('.cycle_push_platform').hide();
		$('.day_cycle_push_platform').hide();
		$('.disposable_push_platform').show();
	} else if(checkedVal == 2) {
		$('.disposable_push_platform').hide();
		$('.day_cycle_push_platform').hide();
		$('.cycle_push_platform').show();
	} else if(checkedVal == 3) {
		$('.disposable_push_platform').hide();
		$('.cycle_push_platform').hide();
		$('.day_cycle_push_platform').show();
	}
	for(var i=0; i<sysIds.length; i++) {
		if(pushCycle == 2){
			$('.cycle_push_platform input[type=checkbox]').each(function() {
				if(sysIds[i] == this.value) {
					this.checked = true;
				}
			});
		}else if(pushCycle == 3){
			$('.day_cycle_push_platform input[type=checkbox]').each(function() {
				if(sysIds[i] == this.value) {
					this.checked = true;
				}
			});
		}
	}
});
//提交推送
function submitForm(url) {
	<% if(coc_province.equals("zhejiang")){ %>
		//浙江营销管理校验
		var customGroupId = $("#customGroupIdHidden").val();
		var sysId = "";
		var result=0;
		
		var _updateCycle = $('.push_type_radio[name="pushCycle"]:checked').val();
		var pushPlatformEl = '';
		
		if(_updateCycle == 1) {
			pushPlatformEl = 'disposable_push_platform';
		} else if(_updateCycle == 2) {
			pushPlatformEl = 'cycle_push_platform';
		} else if(_updateCycle == 3) {
			pushPlatformEl = 'day_cycle_push_platform';
		}
		
		var num = 0;
		$('.' + pushPlatformEl + ' input[type=checkbox]').each(function() {
			if($(this).attr("checked")=="checked"){
				sysId=$(this).val();
				if(sysId.split('_')[1] === 'MARKET'){
					++num;
					if(num>1){
						return false;
					}
				}
			}
		});
		if(num>1){
			parent.showAlert("推送营销管理平台不允许选择多个平台渠道","failed");
			return;
		}
		$('.' + pushPlatformEl + ' input[type=checkbox]').each(function() {
			if($(this).attr("checked")=="checked"){
				sysId=$(this).val();
				result = checkSysId(sysId,customGroupId);
				if(result>0){
					return false;
				}
			}
		});
		if(result>0){
			parent.showAlert("推送设置失败<br/>营销管理平台不存在该客户群创建者的账号","failed");
			return;
		}
		
	<% } %>
	var actionUrl = "${ctx}/ci/customersManagerAction!pushCustomerGroupSingle.ai2do?isPush=" + isPush;
	if(url) {
		actionUrl = url;
	}
	
	//提交推送的时候取消其他选中的信息，防止重复提交
	var currentPushCycle = $('.push_type_radio[name="pushCycle"]:checked').val();
	if(currentPushCycle == 1) {//一次性
		$('.day_cycle_push_platform input[type="checkbox"]').attr('checked', false); 
		$('.cycle_push_platform input[type="checkbox"]').attr('checked', false); 
	} else if(currentPushCycle == 2) {//月
		$('.disposable_push_platform input[type="checkbox"]').attr('checked', false); 
	} else if(currentPushCycle == 3) {//日
		$('.disposable_push_platform input[type="checkbox"]').attr('checked', false); 
	}
	var customGroupInfoId = $('#customGroupIdHidden').val();
	$.ajax({
		url		: '${ctx}/ci/customersManagerAction!findCustomer.ai2do?ciCustomGroupInfo.customGroupId=' + customGroupInfoId,
        type	: 'post',
        async	: false,//同步
        data	: $("#labelForm").serialize(),
        success	: function (data) {
        	var duplicateNum = data.customGroupInfo.duplicateNum;
        	if(duplicateNum != 0 && duplicateNum != null) {
        		window.parent.$.fn.weakTip({"tipTxt":"客户群包含重复记录不能进行推送设置！"});
        		if(url) {
        			window.setTimeout(function() {
            			var url = $.ctx + "/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1";
    					window.parent.location.href = url;
            		}, 1500);
        		}
        		return;
        	}
        	$.ajax({
        		url		: actionUrl,
                type	: 'post',
                async	: false,//同步
                data	: $("#pushForm").serialize(),
                success	: function (result) {
	        		if(result.success){
	        			if(parent.$("#isPushCustom").val() == "true"){
	        				if(getCookieByName("${version_flag}") == "true"){
		        				parent.showAlert(result.msg, "success" , function() {
		           					var url = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?indexLinkType=2";
		           					window.parent.location.href = url;
		            			}, null, 500, 200, -1, "${ctx}/ci/ciMarketAction!newMarketIndex.ai2do", function() {
		            				window.parent.location.href = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1";
		            			});
	        				}else{
		        				parent.showAlert(result.msg, "success" , function() {
		           					var url = $.ctx + "/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1";
		           					window.parent.location.href = url;
		            			}, null, 500, 200, -1, $.ctx + '/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4', function() {
		            				window.parent.location.href = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1";
		            			});
	        				}
	        			} else {
	        				parent.showAlert(result.msg, "success");
	        			}
	        			
	        		}else{
	        			var msg = result.msg;
	        			parent.showAlert(msg,"failed");
	        		}
        		}
        	});
        	
        /*	$.post(actionUrl, $("#pushForm").serialize(), function(result){
        		if(result.success){
        			if(parent.$("#isPushCustom").val() == "true"){
        				parent.showAlert(result.msg, "success" , function() {
           					var url = $.ctx + "/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1";
           					window.parent.location.href = url;
            			}, null, 500, 200, -1, $.ctx + '/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4', function() {
            				window.parent.location.href = $.ctx + "/ci/ciIndexAction!findCalculateCenter.ai2do";
            			});
        			} else {
        				parent.showAlert(result.msg, "success");
        			}
        			
        		}else{
        			var msg = result.msg;
        			parent.showAlert(msg,"failed");
        		}
        	});*/
        }
	});
}
//保存完客户群之后进行推送，判断是否有选中的推送平台
function validateIsNull() {
	var currentPushCycle = $('.push_type_radio[name="pushCycle"]:checked').val();
	var disposablePlateform = $('.disposable_push_platform input[type="checkbox"]:checked').val();
	var monthPlateform = $('.cycle_push_platform input[type="checkbox"]:checked').val();
	var dayPlateform = $('.day_cycle_push_platform input[type="checkbox"]:checked').val();
	if(currentPushCycle == 1 && !disposablePlateform) {
		commonUtil.create_alert_dialog("selectPushPlatform", {
			txt 	: "请选择推送平台！",
			type 	: "failed", 
			width 	: 500,
			height 	: 200
		});
		//parent.showAlert("请选择推送平台！","failed");
		return false;
	} else if(currentPushCycle == 2 && isPush != 1 && !monthPlateform) {
		commonUtil.create_alert_dialog("selectPushPlatform", {
			txt 	: "请选择推送平台！",
			type 	: "failed", 
			width 	: 500,
			height 	: 200
		});
		//parent.showAlert("请选择推送平台！","failed");
		return false;
	}else if(currentPushCycle == 3 && isPush != 1 && !dayPlateform) {
		commonUtil.create_alert_dialog("selectPushPlatform", {
			txt 	: "请选择推送平台！",
			type 	: "failed", 
			width 	: 500,
			height 	: 200
		});
		//parent.showAlert("请选择推送平台！","failed");
		return false;
	} else {
		return true;
	}
}

function hideTip(){
	$("#cnameTip").hide();;
}

//浙江验证营销管理是否有用户
function checkUserExistOnYXGLPlatform(customGroupId){ 
    var exist = false;
    $.ajax({
        url : "${ctx}/ci/zjDownloadApproveAction!checkUserExist.ai2do",
        type: 'post',
        data: {customGroupId: customGroupId},
        dataType: 'json',
        async: false,
        success: function(result){
            exist = result.success;
        }
    });
    return exist;
}

//浙江检查sysId
function checkSysId(sysId,customGroupId){
	if(sysId.split('_')[1] === 'MARKET'){
        if(!checkUserExistOnYXGLPlatform(customGroupId)){
            return 1;
        }else{
        	return 0;
        }
    }else{
    	return 0;
    }
}

</script>
</body>
</html>