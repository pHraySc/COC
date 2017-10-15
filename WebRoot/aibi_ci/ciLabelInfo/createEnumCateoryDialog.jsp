<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
	<script type="text/javascript">
	$(document).ready(function () {
		var columnId = '${param["columnId"]}';
		$("#columnId").val(columnId);
		$.focusblur("#enumCategoryName");
		
		validater = $("#saveForm").validate({
			rules: {
				"ciEnumCategoryInfo.enumCategoryName": {
					required: true
				}
			},
			messages: {
				"ciEnumCategoryInfo.enumCategoryName": {
					required: "分类名称不允许为空！"
				}
			},
			errorPlacement:function(error,element) {
				element.next("div").hide();
				element.after(error);
			},
			success:function(element){
				element.remove();
			},
			errorElement: "div",
			errorClass: "tishi error"
		});
	});

	jQuery.focusblur = function(focusid){
		var focusblurid = $(focusid);
		var defval = focusblurid.val();
		focusblurid.focus(function(){
			var thisval = $(this).val();
			$(this).css("color","#323232");
			if(thisval==defval){
				$(this).val("");
			}
		});
		focusblurid.blur(function(){
			var thisval = $(this).val();
			if(thisval==""){
				$(this).css("color","#8e8e8e");
				$(this).val(defval);
			}
		});
	};
	
	//表单验证
	function validateForm(){
		var cname = $.trim($("#enumCategoryName").val());
		if(cname == "请输入分类名称"){
			$("#enumCategoryName").val("");
			cname ="";
		}else{
			$("#enumCategoryName").val(cname);
		}
		
		if(!validater.form()){
			return false;
		}
		
		return true;
	}	
	
	function submitCategoryInfo(){
		if(validateForm()){
			var actionUrl = "${ctx}/ci/ciLabelInfoAction!saveCategoryInfo.ai2do";
			$.post(actionUrl, $("#saveForm").serialize(), function(result){
				if(result.success){
					$.fn.weakTip({"tipTxt":"分类创建成功","backFn":function(){
						parent.iframeCloseDialog("#saveEnumCateoryDialog");
						parent.searchCategoryInfoList($("#columnId").val());
					 }});
				}else{
					var cmsg = result.msg;
					if(cmsg.indexOf("重名") >= 0){
						$("#enumCategoryNameTip").empty();
						$("#enumCategoryNameTip").show().append(cmsg);
					}else{
						parent.showAlert(cmsg,"failed");
					}
				}
			});
		}
	}
</script>
</head>
<body>
<div>
<form id="saveForm" method="post">
<input id="columnId" type="hidden" name="ciEnumCategoryInfo.columnId" />
<div class="clientMsgBox bgwhite" >
	<ol class="clearfix bgwhite">
		<li class="enumCategory">
   			<label  class="fleft labFmt100 star">分类名称：</label>
   			<input type="text"  class="fleft inputFmt620" style="width:310px;" name="ciEnumCategoryInfo.enumCategoryName" type="text" id="enumCategoryName" 
   					maxlength="35"  value="请输入分类名称" />
   			<div id="enumCategoryNameTip" class="tishi error" style="display:none;"></div>
 		</li>
 		<li>
 			<label class="fleft labFmt100">描述：</label>
			<textarea class="fleft textarea628_60"  id="custom_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciEnumCategoryInfo.descTxt" >${ciEnumCategoryInfo.descTxt}</textarea>
 		</li>
 	</ol>
 	
</div>
<div class="btnActiveBox">
	<a href="javascript:void(0);" id="saveBtn" onclick="submitCategoryInfo();" class="ensureBtn">保存</a>
</div>
</form>
</div>		
</body>
</html>