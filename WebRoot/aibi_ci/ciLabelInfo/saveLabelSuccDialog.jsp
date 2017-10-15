<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>成功提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".ok").empty();
	var successMsg = decodeURIComponent('${param["successMsg"]}');
	var labelName = decodeURIComponent('${param["labelName"]}');
	var labelId = decodeURIComponent('${param["labelId"]}');
	var msgType = decodeURIComponent('${param["msgType"]}');
	var labelType = '${param["labelType"]}';
	if(labelType == '1'){
		$(".isShowTip").hide();
	}
	if(successMsg != null && successMsg != ""){
		$(".ok").append(successMsg);
		$("#labelName").append(labelName);
		$("#msgType").append(msgType);
		//$("#labelLink").attr("href","javascript:window.parent.parent.showLabelDetail('"+labelId+"');");
		$("#labelLink").attr("href","javascript:void(0);");
	}
	$(".ensureBtn").bind("click", function(){
		parent.closeDialog("#successDialog");
	});
});
function toLabelList(){
	parent.closeDialog("#successDialog");
}
</script>
</head>
<body>
<div class="dialog_table error-dialog">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
				<div class="ok" style="padding-top:0px;"></div>
			</td>
		</tr>
		<tr class="even isShowTip">
			<td class="save_td">
				<a id="labelLink" href=""><span id="labelName"></span></a>&nbsp;已<span id="msgType" style="font-weight: normal;"></span>成功！
				<br/>你可以通过【标签管理 &raquo; 我创建的标签】页面查看已保存的标签
			</td>
		</tr>
		<tr class="even isShowTip">
			<td class="app_td">
				<textarea name="" class="tip_textarea" disabled="disabled">操作提示：初始创建的标签需要通过审批，再由后台维护人员初始化其对应的数据直至发布，标记为有效才可以使用，请您耐心等待。</textarea>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="btnActiveBox">
	<a href="javascript:void(0);" name=""  class="ensureBtn">确定</a>
</div>
</body>
</html>
