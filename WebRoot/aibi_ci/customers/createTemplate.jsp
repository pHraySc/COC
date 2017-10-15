<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/dialog_overload.css" rel="stylesheet" />
<script type="text/javascript">
$(document).ready(function(){
	var customName = decodeURIComponent('${param["customName"]}');
	var customSceneId='${param["sceneId"]}';
	//表格斑马线
	aibiTableChangeColor(".showTable");
	
	$("#saveBtn").bind("click", function(){
		submitList();
	});
	
	var name = $.trim($("#template_name").val());
	if(name == "请输入模板名称"){
		$.focusblur("#template_name");
	}
	$("#template_name").val(customName+"-模板");
	$("#template_sceneId").val(customSceneId);
	var texts=$("#template_sceneId").find("option:selected").text();
	$("#template_sceneId2").empty();
	$("#template_sceneId2").append(texts);
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

function submitList(){
	if(validateForm()){
		var actionUrl = $.ctx + "/ci/customersManagerAction!createTemplateByCustomers.ai2do";
		$.post(actionUrl, $("#saveForm").serialize(), function(result){
			var message = result.msg;
			if(result.success){
				var templateName = $("#template_name").val();
				parent.openSuccess("", templateName);
				parent.closeCreate();
			}else{
				if(message.indexOf("重名") >= 0){
					$("#nameTip").empty();
					$("#nameTip").show().append(message);
				}else{
					parent.showAlert(message,"failed");
				}
			}
		});
	}
}

function validateForm(){
	var name = $.trim($("#template_name").val());
	if(name == "请输入模板名称"){
		$("#template_name").val("");
	}else{
		$("#template_name").val(name);
	}
	var	validater = $("#saveForm").validate({
		rules: {
			"ciTemplateInfo.templateName": {
				required: true
			},
			"ciTemplateInfo.sceneId":{
				required:true
			}
		},
		messages: {
			"ciTemplateInfo.templateName": {
				required: "模板名称不允许为空!"
			},
			"ciTemplateInfo.sceneId":{
				required: "请选择模板分类!"
			}
		},
		errorPlacement:function(error,element) {
			element.parent("div").next("div").hide();
			element.parent("div").next("div").after(error);
		},
		success:function(element){
			//element.prev("div").show();
			element.remove();
		},
		errorElement: "div",
		errorClass: "tishi error"
	});
	if(!validater.form()){
		return false;
	}
	return true;
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input type="hidden" name="ciCustomGroupInfo.customGroupId" value='${param["customId"]}'/>
<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th style="width:100px;padding-left:10px;"><label class="star labFmt100">模板名称：</label></th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciTemplateInfo.templateName" type="text" id="template_name" value="请输入模板名称" 
						maxlength="40" class="new_tag_input" style="width:302px;*width:306px;cursor:text"/>
				</div>
				<!-- 提示 -->
				<div id="nameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>
		<tr>
			<th style="width:100px;padding-left:10px;"><label class="star labFmt100">模板分类：</label></th>
			<td>
				<div class="new_tag_selectBox">
					  <span id="template_sceneId2"></span>
					  <div style="display:none; height:0px;">
						<base:DCselect selectId="template_sceneId"  selectName="ciTemplateInfo.sceneId" tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
					                         nullName="无" nullValue="" />
					  </div>
			    </div> 
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<tr>
			<th>模板描述：</th>
			<td>
				<div>
					<textarea id="template_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciTemplateInfo.templateDesc" class="new_tag_textarea"></textarea>
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<!-- <tr>
			<th>使用规则：</th>
			<td><textarea id="template_labelOptRuleShow" readonly="readonly" name="ciTemplateInfo.labelOptRuleShow" class="new_tag_textarea"></textarea>
			</td>
		</tr> -->
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
</div>

</form>
</body>
</html>

