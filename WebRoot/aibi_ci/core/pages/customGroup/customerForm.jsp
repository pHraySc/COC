<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>创建用户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<script type="text/javascript">
<%
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
%>
var	validater;
$(document).ready(function(){
	var _isSuccess = '${resultMap["success"]}';
	var _msg = '${resultMap["msg"]}';
	if(_isSuccess == 'false'){
		showAlert(_msg,"failed",parent.closeCreate);
		parent.multiCustomSearch();
	}
	var scenePublicId="<%=ServiceConstants.ALL_SCENE_ID%>";
	validater = $("#saveForm").validate({
		rules: {
			"ciCustomGroupInfo.customGroupName": {
				required: true
			}
		},
		messages: {
			"ciCustomGroupInfo.customGroupName": {
				required: "用户群名称不允许为空!"
			}
		},
		errorPlacement:function(error,element) {
			element.parent("div").next("div").hide();
			element.parent("div").next("div").after(error);
		},
		success:function(element){
			element.remove();
		},
		errorElement: "div",
		errorClass: "tishi error"
	});
	
	$("#saveBtn").bind("click", function(){
		submitForm();
	});
	
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入用户群名称"){
		$.focusblur("#custom_name");
	}
	//表格斑马线
	changeTableColor(".showTable");
});

jQuery.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});
};

function submitForm(){
	if(validateForm()){
		var actionUrl = $.ctx + "/core/ciCustomerManagerAction!editCustomSimple.ai2do";
		$.post(actionUrl, $("#saveForm").serialize(), function(result){
			if(result.success){
				var customerName = $("#custom_name").val();
				if($("#custom_id").val() != null && $("#custom_id").val() != ""){
					$.fn.weakTip({"tipTxt":"用户群修改成功！","backFn":function(){
						parent.customManagerMain.loadCustomList();
						parent.$('#createEditCustomDialog').dialog('close');
			 		}});
				}
			}else{
				var cmsg = result.cmsg;
				if(cmsg.indexOf("重名") >= 0){
					$("#cnameTip").empty();
					$("#cnameTip").show().append(cmsg);
				}else{
					parent.showAlert(cmsg,"failed");
				}
			}
		});
	}
}

function validateForm(){
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入用户群名称"){
		$("#custom_name").val("");
	}else{
		$("#custom_name").val(cname);
	}
	if(!validater.form()){
		return false;
	}
	return true;
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input id="custom_id" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}" />
<input type="hidden" name="ciCustomGroupInfo.createTypeId" value="${ciCustomGroupInfo.createTypeId}" />
<input type="hidden" name="ciCustomGroupInfo.updateCycle" value="${ciCustomGroupInfo.updateCycle}"/>
<input type="hidden" name="ciCustomGroupInfo.createUserId" value="${ciCustomGroupInfo.createUserId}" />
<input type="hidden" name="ciCustomGroupInfo.createTime" value='<fmt:formatDate value="${ciCustomGroupInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
<input type="hidden" name="ciCustomGroupInfo.createCityId" value="${ciCustomGroupInfo.createCityId}" />
<input type="hidden" name="ciCustomGroupInfo.isPrivate" value="${ciCustomGroupInfo.isPrivate}" />
<input type="hidden" name="ciCustomGroupInfo.isHasList" value="${ciCustomGroupInfo.isHasList}" />
<input type="hidden" name="ciCustomGroupInfo.isFirstFailed" value="${ciCustomGroupInfo.isFirstFailed}" />
<input type="hidden" name="ciCustomGroupInfo.listCreateTime" value="${ciCustomGroupInfo.listCreateTime}" />

<c:if test='${ciCustomGroupInfo.parentCustomId != null && customGroupInfo.parentCustomId != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.parentCustomId" value="${ciCustomGroupInfo.parentCustomId}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.prodOptRuleShow != null && customGroupInfo.prodOptRuleShow != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.prodOptRuleShow" value="${ciCustomGroupInfo.prodOptRuleShow}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.labelOptRuleShow != null && customGroupInfo.labelOptRuleShow != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value="${ciCustomGroupInfo.labelOptRuleShow}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.customOptRuleShow != null && customGroupInfo.customOptRuleShow != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" value="${ciCustomGroupInfo.customOptRuleShow}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.kpiDiffRule != null && customGroupInfo.kpiDiffRule != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.kpiDiffRule" value="${ciCustomGroupInfo.kpiDiffRule}"/>
</c:if>
<input type="hidden" name="ciCustomGroupInfo.status" value="${ciCustomGroupInfo.status}"/>
<input type="hidden" name="ciCustomGroupInfo.isSysRecom" value="${ciCustomGroupInfo.isSysRecom}"/>
<input type="hidden" name="ciCustomGroupInfo.dataStatus" value="${ciCustomGroupInfo.dataStatus}"/>
<input type="hidden" name="ciCustomGroupInfo.customNum" value="${ciCustomGroupInfo.customNum}"/>
<input type="hidden" name="ciCustomGroupInfo.tacticsId" value="${ciCustomGroupInfo.tacticsId}"/>
<input type="hidden" name="ciCustomGroupInfo.dayLabelDate" value="${ciCustomGroupInfo.dayLabelDate}"/>
<input type="hidden" name="ciCustomGroupInfo.monthLabelDate" value="${ciCustomGroupInfo.monthLabelDate}"/>
<input type="hidden" name="ciCustomGroupInfo.isFirstFailed" value="${ciCustomGroupInfo.isFirstFailed}"/>
<input type="hidden" name="ciCustomGroupInfo.thumbnailData" value="${ciCustomGroupInfo.thumbnailData}"/>
<input type="hidden" name="ciCustomGroupInfo.isContainLocalList" value="${ciCustomGroupInfo.isContainLocalList}"/>
<input type="hidden" name="ciCustomGroupInfo.isLabelOffline" value="${ciCustomGroupInfo.isLabelOffline}"/>
<input type="hidden" name="ciCustomGroupInfo.duplicateNum" value="${ciCustomGroupInfo.duplicateNum}" />
<input type="hidden" name="ciCustomGroupInfo.dataTime" value='<fmt:formatDate value="${ciCustomGroupInfo.dataTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
<c:if test='${ciCustomGroupInfo.startDate != null && customGroupInfo.startDate != ""}'>
<input type="hidden" name="ciCustomGroupInfo.startDate" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
</c:if>
<c:if test='${ciCustomGroupInfo.endDate != null && customGroupInfo.endDate != ""}'>
<input type="hidden" name="ciCustomGroupInfo.endDate" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
</c:if>
<c:if test='${ciCustomGroupInfo.newModifyTime != null && customGroupInfo.newModifyTime != ""}'>
<input type="hidden" name="ciCustomGroupInfo.newModifyTime" value='<fmt:formatDate value="${ciCustomGroupInfo.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
</c:if>
<c:if test='${ciCustomGroupInfo.dataDate != null && customGroupInfo.dataDate != ""}'>
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value="${ciCustomGroupInfo.dataDate}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.templateId != null && customGroupInfo.templateId != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.templateId" value="${ciCustomGroupInfo.templateId}"/>
</c:if>

<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th style="width:100px;padding-left:10px;"  ><label class="star labFmt100">用户群名称：</label></th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciCustomGroupInfo.customGroupName" type="text" id="custom_name" value="${ciCustomGroupInfo.customGroupName}" 
						maxlength="35" class="new_tag_input_edit" style="width:302px;*width:306px;cursor:text"/>
				</div>
				<!-- 提示 -->
				<div id="cnameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>	
		<tr>
			<th>用户群描述：</th>
			<td>
				<div>
					<textarea id="custom_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea">${ciCustomGroupInfo.customGroupDesc}</textarea>
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
</div>
</form>
</body>
</html>

