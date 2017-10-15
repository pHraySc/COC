<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>客户群共享</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
var	validater;
$(document).ready(function(){
	var id = '${param["id"]}';
	var name = decodeURIComponent('${param["name"]}');
	var cityId='${param["cityId"]}';
	var msg=decodeURIComponent('${param["msg"]}');
	var createUserId='${param["createUserId"]}';
	//alert(id+"----"+name+"--"+cityId+"--"+"--"+msg);
	$("#customGroupId").val(id);
	$("#customGroupName").val(name);
	$("#createCityId").val(cityId);
	$("#createUserId").val(createUserId);
	//显示客户群名称重名问题
	$("#cnameTip").empty();
	$("#cnameTip").show().append(msg);
	//window.parent.$("#shareFrame").height(200);
	validater = $("#saveForm").validate({
		rules: {
			"ciTemplateInfo.customGroupName": {
				required: true
			}
		},
		messages: {
			"ciTemplateInfo.customGroupName": {
				required: "客户群名称不允许为空!"
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

	$("#customGroupName").focus();

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

//保存客户群
function submitForm(){
	if(validateForm()){
		var actionUrl = $.ctx + "/ci/customersManagerAction!shareCustom.ai2do";
		var customGroupName=$("#customGroupName").val();
		var customGroupId=$("#customGroupId").val();
		var createCityId=$("#createCityId").val();
		var createUserId=$("#createUserId").val();
		var para={
				"ciCustomGroupInfo.customGroupName":customGroupName,
				"ciCustomGroupInfo.customGroupId":customGroupId,
				"ciCustomGroupInfo.createCityId":createCityId,
				"ciCustomGroupInfo.createUserId":createUserId
				};
		$.post(actionUrl, para, function(result){
			if(result.success){
				var customGroupName = $("#customGroupName").val();
				$.fn.weakTip({"tipTxt":"客户群共享成功！","backFn":function(){
					parent.closeDialog("#shareCustomerDialog");
					parent.multiCustomSearch();
				 }});
				//parent.openShareSuccess(customGroupName,"");
			}else{
				var cmsg = result.msg;
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
//表单验证
function validateForm(){
	var cname = $.trim($("#customGroupName").val());
	if(cname == "请输入客户群名称"){
		$("#customGroupName").val("");
	}else{
		$("#customGroupName").val(cname);
	}
	if(!validater.form()){
		return false;
	}
	return true;
}
function cancel(){
	parent.closeDialog("#shareCustomerDialog");
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input id="customGroupId" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}"/>
<input id="createCityId" type="hidden" name="ciCustomGroupInfo.createCityId" />
<input id="createUserId" type="hidden" name="ciCustomGroupInfo.createUserId" />
<div class="clientMsgBox" >
	<ol class="clearfix">
		<li>
   			<label  class="fleft labFmt100 star">客户群名称：</label>
   			<input type="text"  class="fleft inputFmt235"   name="ciCustomGroupInfo.customGroupName" type="text" id="customGroupName" 
   					maxlength="35"  value="请输入客户群名称" />
   			<div id="cnameTip" class="tishi error" style="display:none;"></div>
 		</li>
 	</ol>
 </div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="saveBtn" name="" type="button" value="确认" class="tag_btn"/>
</div>
</form>
</body>
</html>

