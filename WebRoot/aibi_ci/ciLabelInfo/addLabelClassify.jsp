<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>添加标签分类</title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<style>
.ztree li span.button.level0 {
	display:inline-block;
}
.x-form-field li:hover{background:#fff;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	//表格斑马线
	changeTableColor(".showTable");
	$("body").bind("mousedown", 
			function(event){
				if (!(event.target.id == "labelTreeDiv" || $(event.target).parents("#labelTreeDiv").length>0)) {
					hideTree();
				}
	});
	$("#saveBtn").bind("click",saveClassify);
	//标题名称提示
	$("#labelName").click(function(){
		if($(this).val()=="请输入标签分类名称"){
			$(this).val("");
		}
	});
	var labelId = '${param["labelId"]}';
	if(labelId != null && labelId != "") {
		var actionUrl = $.ctx + "/ci/ciLabelInfoConfigAction!getLabelClassify.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data:{'labelId':labelId},
			success: function(result){
				$("#labelId").val(result.labelInfo.labelId);
				var parentName = result.parentName;
				if(result.labelInfo.parentId == '-1'){
					parentName = "根节点";
				}
				$("#labelName").val(result.labelInfo.labelName);
				$("#createDesc").val(result.labelInfo.createDesc);
				$("#parentId").val(result.labelInfo.parentId);
				$("#parentName").val(parentName);
			}
		});
	}
	showTree();
	$("#parentName").bind("click",{id:"parentName"},showMenu);
	$("#parentName").focus(showTree);
		
});
var zTree;
var setting = {
		view: {
			showLine: true,
			selectedMulti: false,
			dblClickExpand: false
		},
		data: {
			key: {
				title: "tip"
			},
			simpleData: {
				enable: true
			}
		},
		callback : {
			onClick: treeOnClick
		}
};
//点击树节点事件	
function treeOnClick(event, treeId, treeNode) {
	if (treeNode) {
		$("#parentId").val(treeNode.id);
		$("#parentName").val(treeNode.name);
		hideTree();
	}
}
//隐藏树
function hideTree() {
	$("#labelTreeDiv").fadeOut("fast");
}
function showTree(){
	var zNodes;
	$.ajax({
		type: "POST",
		url: "${ctx}/ci/ciLabelInfoConfigAction!getLabelTreeByLevel.ai2do",
		async: false,
		dataType: "json",
		success: function(result){
			zNodes = result;
			zTree = $.fn.zTree.init($("#labelTree"), setting, zNodes);  
		}
	});
}
//显示分类树
function showMenu(event) {
    var id = event.data.id;
    var cityObj = $("#" + id);
    var cityOffset = $("#" + id).offset();
    var width = cityObj.width()+25;
    $("#labelTreeDiv").css({"left": cityOffset.left, "top": cityOffset.top + cityObj.outerHeight(),"width":width }).slideDown("fast");
    $("#ztreeBox,#labelTree").css({"width":width,"background":"#fff","border-width":"1px","border-color":"#ccc"}); 
    $("#ztreeBox").height(121).mCustomScrollbar({
		theme:"minimal-dark", //滚动条样式
		scrollbarPosition:"inside", //滚动条位置
		scrollInertia:100,
		axis:"yx",
		mouseWheel:{
			preventDefault:true, //阻止冒泡事件
			axis:"yx"
		}
	});	
    $(document).bind("mousedown", onBodyDown);
}
//绑定文档点击事件，点击空白处收起分类树
function onBodyDown(event) {
    if (!( event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}
//隐藏分类树
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $(document).unbind("mousedown", onBodyDown);
}
function saveClassify(){
	if(validateForm()){
		$.ajax({
			type:"post",
			url:"${ctx}/ci/ciLabelInfoConfigAction!saveLabelClassify.ai2do",
			data:$("#saveForm").serialize(),
			async:false,
			dataType:"json",
			success:function(result){
				if(result.msgType == "sameLabel") {
					$("#sameNameTip").empty();	
					$("#sameNameTip").show().append(result.msgContent);					
				}else if(result.msgType == "sameClassify"){
					$("#sameClassifyTip").empty();	
					$("#sameClassifyTip").show().append(result.msgContent);		
				}else if(result.msgType == "noEditLabel"){
					parent.showAlert(result.msgContent, 'failed');
   					return;
				}else if(result.msgType == "successSave" || result.msgType == "successEdit") {
					var msgType = "";
					if(result.msgType == "successSave"){
						msgType = "保存";
					}else{
						msgType = "修改";
					}
					$("#successDialog").dialog("open")
					var successMsg = encodeURIComponent(encodeURIComponent(result.msgContent));
					var labelName = encodeURIComponent(encodeURIComponent(result.labelName));
					var labelId = encodeURIComponent(encodeURIComponent(result.labelId));
					var msgType = encodeURIComponent(encodeURIComponent(msgType));
					var ifmUrl= "${ctx}/aibi_ci/ciLabelInfo/saveLabelSuccDialog.jsp?successMsg="+successMsg+"&labelName="+labelName+"&labelId="+labelId+"&msgType="+msgType+"&labelType=1";
					var dlgObj = dialogUtil.create_dialog("successDialog", {
						"title" :  "系统提示",
						"height": "auto",
						"width" : 460,
						"frameSrc" : ifmUrl,
						"frameHeight":160,
						"position" : ['center','center'] 
						
					});
					dlgObj.dialog({
						  close: function( event, ui ) {
							  closeDialog("#successDialog");
						  }
					});
					window.parent.searchAfterDelete();//刷新列表
				}
			}
		});
	}
}
function validateForm(){
	if($("#labelName").val() == "请输入标签分类名称"){
		$("#labelName").val("");
	}else{
		$("#labelName").val($.trim($("#labelName").val()));
	}
	var	validater = $("#saveForm").validate({
		rules: {
			"ciLabelInfo.labelName": {required: true},
			"parentName": {required: true}
  		},
        messages: {
        	"ciLabelInfo.labelName": {
				required: "标签分类名称不允许为空!"
			},
        	"parentName": {
				required: "标签分类不允许为空!"
			}
  		},
  		errorPlacement:function(error,element) {
  			element.parent("div").next("div").hide();
			element.parent("div").next("div").after(error);
		},
		errorElement: "div",
		errorClass: "tishi-new error"
    });
	if(!validater.form()){
		return false;
	}
	return true;
}
function closeDialog(){
	parent.closeDialog("#label_classify_div");
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input type="hidden" id="labelId" name="ciLabelInfo.labelId" />
<div class="dialog_table">
<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
			<tr>
				<th style="width:100px;padding-left:10px;"  ><label class="star labFmt100">标签分类名称：</label></th>
				<td>
				<div class="new_tag_selectBox" style="float:left;">
					<input name="ciLabelInfo.labelName" type="text" id="labelName" value="请输入标签分类名称" 
						maxlength="35" class="new_tag_input" style="width:220px;*width:224px;cursor:text"/>
				</div>
				<div id="sameNameTip" class="tishi-new error" style="display:none;"></div><!-- 用于重名验证 -->
				</td>
				<td></td>
			</tr>
			<tr>
				<th style="width:100px;padding-left:10px;"  ><label class="star labFmt100">标签分类：</label></th>
				<td>
				<div class="selectCheckboxCT" style="float:left;">
					<input type="hidden" name="ciLabelInfo.parentId" id="parentId"/>
					<input name="parentName" type="text" id="parentName" value="" 
						maxlength="35" class="selectCheckbox" style="width:206px;cursor:text"  readonly="readonly" />
				</div>
				<div id="sameClassifyTip" class="tishi-new error" style="display:none;"></div>
				</td>
			</tr>
			<tr>
			<th style="width:100px;padding-left:10px;"  ><label class="labFmt100">备注：</label></th>
			<td><textarea id="createDesc" name="ciLabelInfo.createDesc" onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}"
				 onblur="this.value = this.value.slice(0,341)" class="new_tag_textarea"></textarea></td>
			</tr>
		</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="saveBtn" name="saveBtn" type="button" value="保存" class="tag_btn"/>
</div>
<div id="labelTreeDiv" class="zTreeDemoBackground"  style="display:none; position: absolute;" >
	<div class="ztreeBox" id="ztreeBox" >
    	<ul id="labelTree" class="ztree selectCheckboxZtree mScrool fleft" ></ul> 
    </div>
</div>
</form>
</body>
</html>
