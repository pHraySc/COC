<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>模板共享</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<script type="text/javascript">
var	validater;
$(document).ready(function(){
	var id = '${param["id"]}';
	var name = decodeURIComponent('${param["name"]}');
	var msg=decodeURIComponent('${param["msg"]}');
	$("#templateId").val(id);
	$("#templateName").val(name);
	//重名
	$("#cnameTip").empty();
	$("#cnameTip").show().append(msg);
	window.parent.$("#shareFrame").height(245);
	validater = $("#saveForm").validate({
		rules: {
			"ciTemplateInfo.templateName": {
				required: true
			}
		},
		messages: {
			"ciTemplateInfo.templateName": {
				required: "模板名称不允许为空!"
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

	$("#templateName").focus();
	
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

//保存客户群
function submitForm(){
	if(validateForm()){
		var actionUrl = $.ctx + "/ci/templateManageAction!saveShareTemplate.ai2do";
		var templateName=$("#templateName").val();
		var templateId=$("#templateId").val();
	
		var para=templateName+"&"+templateId;
		
		$.post(actionUrl, $("#saveForm").serialize(), function(result){
			if(result.success){
				var templateName = $("#templateName").val();
				parent.openShareSuccess("", templateName);
				parent.closeDialog("#shareDialog");
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
//表单验证
function validateForm(){
	var cname = $.trim($("#templateName").val());
	if(cname == "请输入模板名称"){
		$("#templateName").val("");
	}else{
		$("#templateName").val(cname);
	}
	if(!validater.form()){
		return false;
	}
	return true;
}
function cancel(){
	parent.closeDialog("#shareDialog");
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input id="templateId" type="hidden" name="ciTemplateInfo.templateId" value="${ciCustomGroupInfo.templateId}"/>
<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th width="110px"><span>*</span>模板名称：</th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciTemplateInfo.templateName" type="text" id="templateName" 
						maxlength="35" class="new_tag_input" style="width:302px;*width:306px;cursor:text"/>
				</div>
				<!-- 提示 -->
				<div id="cnameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="cancelBtn" name="" type="button" value="取消" class="tag_btn" onclick="cancel();" />
	<input id="saveBtn" name="" type="button" value="确认" class="tag_btn"/>
</div>
</form>
</body>
</html>

