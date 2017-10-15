<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>错误提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".no.no_nobg").empty();
	var errorMsg = decodeURIComponent('${param["errorMsg"]}');
	if(errorMsg != null && errorMsg != ""){
		$(".no.no_nobg").append(errorMsg);
	}
	$(".tag_btn").bind("click", function(){
		parent.closeDialog("#errorDialog");
	});
});
</script>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td">
				<!-- 错误信息 -->
				<div class="no no_nobg"></div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input name="" type="button" value=" 确 定 " class="tag_btn"/>
</div>
</body>
</html>
