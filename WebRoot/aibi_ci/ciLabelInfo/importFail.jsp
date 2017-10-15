<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>标签导入</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
function toCiLabelList(){
	var url = $.ctx + "/ci/ciLabelInfoAction!labelManageIndex.ai2do?showType=4";
	closeWindowAndOpenUrl(url);
  }
//如果父页面存则在关闭当前页面，在父页面跳转到新的url，否则在本页跳转到新的url
function closeWindowAndOpenUrl(url){
	var pw = parent.window.opener;
	var ppw = parent.parent.window.opener;
    if(pw!=null && typeof(pw) != "undefined" && !pw.closed){
		pw.top.location = url;
		parent.self.close();
	}else if(ppw!=null && typeof(ppw) != "undefined" && !ppw.closed){
		ppw.top.location = url;
		parent.parent.self.close();
	}else{
		top.location = url;
	}
}
</script>
<style>

</style>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
				<div class="failed">上传失败</div>
			</td>
		</tr>
		<tr id="customer" class="even">
			<td class="save_td save_text_td">
				<a href="javascript:void(0);"><span id="customerName"></span></a>&nbsp;&nbsp;标签上传失败！${error}
			</td>
		</tr>
	</table>
</body>
</html>
