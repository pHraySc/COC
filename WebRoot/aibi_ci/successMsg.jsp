<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>正确提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
<div class="dialog_table" style="padding-top:23px;">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
                <div class="sucess showtxt">成功！</div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<!-- <div class="dialog_btn_div">
	<input id="ok" name="" type="button" value="  " class="tag_btn"/>
</div> -->
<div class="btnActiveBox">
    <a href="javascript:void(0);" class="ensureBtn" id="ok">确定</a>
    <a href="javascript:void(0);" class="prevBtn fright prev_button hidden" id="goToHomePage">返回首页</a>
</div>
</body>
</html>
