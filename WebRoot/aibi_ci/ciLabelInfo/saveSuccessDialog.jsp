<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>成功提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	window.parent.$("#successFrame").height(270);
	//设置弹出窗口内的滚动条
	setTimeout(function(){
		var _autoheight = $(document).height();
		var contentFrame = $("iframe:visible",window.parent.document);
		var frameHeiht = parseInt(contentFrame.css("height"));
		if(_autoheight > frameHeiht){
			$(".dialog_table").height(frameHeiht+"px");
			$(".dialog_table").css("overflow","scroll");
		}
	},500);
});

</script>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
				<div class="ok" style="padding-top:0px;">导入成功！</div>
			</td>
		</tr>
		<tr id="importLabel" class="even" style="display:none;">
			<td class="save_td save_text_td">
				<span id="marketingName"></span>数据文件上传成功！
			</td>
		</tr>
		<tr class="even" id="notice">
			<td class="save_td">
				<div class="show">感谢您的使用！</div>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
