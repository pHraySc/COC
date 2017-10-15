<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<style>

</style>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//表格斑马线
	aibiTableChangeColor(".showTable");
	
	$("#saveBtn").bind("click", function(){
		submitList();
	});
	
	var name = $.trim($("#template_name").val());
	if(name == "请输入模板名称"){
		$.focusblur("#template_name");
	}
	
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
		var actionUrl = $.ctx;
		var templateId = $("#template_id").val();
		if(templateId == null || templateId == ""){
			actionUrl += "/ci/templateManageAction!saveTemplate.ai2do";
		}else{
			actionUrl += "/ci/templateManageAction!editTemplate.ai2do";
		}
		$.post(actionUrl, $("#saveForm").serialize()+"&"+$("#labelForm",parent.document).serialize(), function(result){
			var message = result.msg;
			if(result.success){
				parent.showAlert(message,"success",parent.showTemplateList);
				//var templateName = $("#template_name").val();
				//parent.openSuccess("", templateName);
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
			"ciTemplateInfo.sceneId": {
				required: true
			}
		},
		messages: {
			"ciTemplateInfo.templateName": {
				required: "模板名称不允许为空!"
			},
			"ciTemplateInfo.sceneId": {
				required: "请选择模板分类！"
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
<input id="template_id" type="hidden" name="ciTemplateInfo.templateId" value="${ciTemplateInfo.templateId }"/>
<input id="template_status" type="hidden" name="ciTemplateInfo.status" value="${ciTemplateInfo.status }"/>
<input id="template_userId" type="hidden" name="ciTemplateInfo.userId" value="${ciTemplateInfo.userId }"/>
<input id="template_addTime" type="hidden" name="ciTemplateInfo.addTime"  value="${ciTemplateInfo.addTime }"/>
<input id="template_isPrivate" type="hidden" name="ciTemplateInfo.isPrivate"  value="${ciTemplateInfo.isPrivate }"/>
<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th style="width:100px;padding-left:10px;"><label class="star labFmt100">模板名称：</label></th>
			<td>
				<div class="new_tag_selectBox">
				<c:if test=""></c:if>
				<c:choose>
					<c:when test="${not empty ciTemplateInfo.templateName }">
						<input name="ciTemplateInfo.templateName" type="text" id="template_name" 
						maxlength="40" class="new_tag_input" style="width:302px;*width:306px;cursor:text"  value="${ciTemplateInfo.templateName }"/>
					</c:when>
					<c:otherwise>
						<input name="ciTemplateInfo.templateName" type="text" id="template_name" value="请输入模板名称" 
							maxlength="40" class="new_tag_input" style="width:302px;*width:306px;cursor:text" />
					</c:otherwise>
				</c:choose>
				</div>
				<!-- 提示 -->
				<div id="nameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>
		<tr>
			<th style="width:100px;padding-left:10px;"><label class="star labFmt100">模板分类：</label></th>
			<td>
				<div style="width:180px;">
					<base:DCselect selectId="template_sceneId"  selectName="ciTemplateInfo.sceneId" tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
			                         nullName="请选择" nullValue="" selectValue="${ciTemplateInfo.sceneId}"/>
			    </div> 
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<tr>
			<th>模板描述：</th>
			<td>
				<div>
					<textarea id="template_desc"  onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciTemplateInfo.templateDesc" class="new_tag_textarea">${ciTemplateInfo.templateDesc }</textarea>
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<!-- 
		<tr>
			<th>使用规则：</th>
			<td><textarea id="template_labelOptRuleShow"  value="${ciTemplateInfo.userId }" readonly="readonly" name="ciTemplateInfo.labelOptRuleShow" class="new_tag_textarea"></textarea>
			</td>
		</tr>
		 -->
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
</div>

</form>
</body>
</html>

