<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>确认提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".showtxt").empty();
	var confirmMsg = decodeURIComponent('${param["confirmMsg"]}');
	if(confirmMsg != null && confirmMsg != ""){
		$(".showtxt").append(confirmMsg);
	}
	$("#okBtn").bind("click", function(){
		var id = '${param["id"]}';
		if(id == null || id == ""){
			parent.closeConfirmDialog("#confirmDialog", true, "");
		}else{
			parent.closeConfirmDialog("#confirmDialog", true, id);
		}
	});
	$("#cancelBtn").bind("click", function(){
		parent.closeConfirmDialog("#confirmDialog", false, "");
	});
});
</script>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
				<div class="confirm showtxt"></div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="btnActiveBox">
    <a href="javascript:void(0);" class="cancelBtn" id="cancelBtn">取消</a>
    <a href="javascript:void(0);" class="ensureBtn" id="okBtn">确定</a>
</div>
</body>
</html>
