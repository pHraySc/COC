<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>系统自动匹配提示</title>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" colspan="2" style="padding-top:0px;padding-bottom:20px;">
				<div class="no no_nobg">您未进行系统自动匹配！</div>
				<!-- 保存失败
				<div class="no">保存失败！</div>
				 -->
			</td>
		</tr>
		<tr class="even">
			<td class="save_td save_text_td" style="text-align:left">
				<li>可选择”跳过“，直接进行手工匹配！</li>
				<li>可选择”系统匹配”，您的策略匹配中会出现<br/>
				如右图“系统推荐”的TAB页哦！</li>
			</td>
			<td width="283px" class="save_text_td" style="text-align:left">
				<img src="../assets/themes/default/images/img4.png" width="263" height="124" />
			</td>
		</tr>
		<tr class="even">
			<td class="save_td" colspan="2">
				<div class="show" style="background:#fff; border-color:#cde3f3; text-align:left">
					系统匹配功能，可能需要您等待几分钟哦！<br/>
					数据处理完毕后，会在<a href="#">系统通知消息</a>中提示您！感谢您的使用！
				</div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input name="" type="button" value="系统自动匹配" class="tag_btn"/>
	<input name="" type="button" value=" 跳 过 " class="tag_btn tag_btn_disable"/>
</div>
</body>
</html>