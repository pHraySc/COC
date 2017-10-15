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
	//var labelName = decodeURIComponent('${param["labelName"]}');
	var labelIds = '${param["labelIds"]}';
	if(successMsg != null && successMsg != ""){
		$(".ok").append(successMsg);
		//$("#labelName").append(labelName);
		//$("#labelLink").attr("href","javascript:window.parent.parent.showLabelDetail('"+labelId+"');");
	}
	$(".tag_btn").bind("click", function(){
		parent.closeDialog("#successDialog");
	});
});
function toLabelList(){
	parent.closeDialog("#successDialog");
}
</script>
</head>
<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:10px;">
				<div class="ok" style="padding-top:0px;"></div>
			</td>
		</tr>
		<tr class="even">
			<td class="save_td">
				<c:forEach var="labelId" items='${param["labelIds"]}' varStatus="status1">
					<a id="labelLink" href="javascript:window.parent.parent.showLabelDetail('${labelId}');"><span id="labelName">${dim:labelName(labelId)}</span></a>
				</c:forEach>
				&nbsp;已审批成功！
			</td>
		</tr>
		<tr class="odd">
			<td class="app_td">
				<textarea name="" class="tip_textarea" disabled="disabled">操作提示：初始创建的标签需要通过审批，再由后台维护人员初始化其对应的数据直至发布，标记为有效才可以使用，请您耐心等待。</textarea>
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
