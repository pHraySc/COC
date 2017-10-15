<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>成功提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	window.parent.$("#successShareFrame").height(270);
	var customerName = decodeURIComponent('${param["customerName"]}');
	var templateName = decodeURIComponent('${param["templateName"]}');
	if(null == customerName || customerName == "") {
		customerName = '${customerName}';
	}
	if(customerName != null && customerName != ""){
		$("#customerName").empty();
		$("#customerName").append(customerName);
		$("#customer").show();
	}
	if(templateName != null && templateName != ""){
		$("#templateName").empty();
		$("#templateName").append(templateName);
		$("#template").show();
	}
	if(customerName != null && customerName != ""&& templateName != null && templateName != ""){
		window.parent.$("#successShareFrame").height(window.parent.$("#successShareFrame").height() + 100);
	}

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

//跳转到客户群管理页面
function toCustomList(){
	var saveCustomerType = '${saveCustomerType}';
	//var url = $.ctx + "/ci/customersManagerAction!search.ai2do?initOrSearch=init";
	//closeWindowAndOpenUrl(url);
	window.parent.$("#successShareDialog").dialog("close");
}
//跳转到模板管理页面
function toTemplateList(){
	//var url = $.ctx + "/aibi_ci/template/templateList.jsp";
	//closeWindowAndOpenUrl(url);
	window.parent.$("#successShareDialog").dialog("close");
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
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr id="customer" class="even" style="display:none;">
			<td class="save_td save_text_td">
				<span id="customerName"></span>客户群共享成功！
			</td>
		</tr>
		<tr id="template" class="even" style="display:none;">
			<td class="save_td save_text_td">
				<span id="templateName"></span>模板共享成功！
			</td>
		</tr>
	</table>
</div>
</body>
</html>
