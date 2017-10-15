<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>成功提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".ok.ok_nobg").empty();
	var errorMsg = decodeURIComponent('${param["successMsg"]}');
	if(errorMsg != null && errorMsg != ""){
		$(".ok.ok_nobg").append(errorMsg);
	}
	$("#okBtn").bind("click", function(){
		parent.closeDialog("#successDialog");
	});
});
</script>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td">
				<div class="ok ok_nobg"></div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="btnActiveBox">
    <a href="javascript:void(0);" class="ensureBtn" id="okBtn">确定</a>
</div>
</body>
</html>
