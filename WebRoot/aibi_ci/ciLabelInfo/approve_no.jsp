<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>审批不通过</title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var labelIds = '${param["labelIds"]}';
    $("#approveNo").click(function(){
        var actionUrl =  $.ctx + "/ci/ciLabelInfoApproveAction!approveNoPass.ai2do";
    	$.ajax({
			type: "POST",
			url: actionUrl,
			data: {
				"labelIds" : labelIds,
				"approveDesc" : $("#approveDesc").val()
			},
			success: function(result){
				//alert("已不通过");
				window.parent.closeApproveNoDialog(); 
				window.parent.openSuccessForNo("" + result.msg,labelIds);
			}
		});
    });

});

</script>
</head>

<body>
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
				<div class="no">审批不通过！</div>
			</td>
		</tr>
		<tr class="even">
			<td class="app_td"><textarea id="approveDesc" name="approveDesc"  onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" 
				onblur="this.value = this.value.slice(0,170)" class="new_tag_textarea">填写审批意见</textarea></td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="approveNo" name="approveNo" type="button" value=" 确 定 " class="tag_btn"/>
</div>
</body>
</html>
