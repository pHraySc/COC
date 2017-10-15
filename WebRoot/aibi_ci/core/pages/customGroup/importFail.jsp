<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户群导入提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<style>

</style>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
				<div class="failed">导入失败</div>
			</td>
		</tr>
		<tr id="customer" class="even" style="display:none;">
			<td class="save_td save_text_td">
				<a href="javascript:void(0);"><span id="customerName"></span></a>&nbsp;&nbsp;用户群导入失败！
				<br/>您可以通过<a href="#">用户群管理</a>查看本次操作，并进行相应的用户群管理
			</td>
		</tr>
		<!-- <tr class="even">
			<td class="save_td">
				<div class="show">数据处理完毕后，会在<a href="javascript:void(0);">系统通知消息</a>中提示您！感谢您的使用！</div>
			</td>
		</tr> -->
	</table>
</div>
</body>
</html>
