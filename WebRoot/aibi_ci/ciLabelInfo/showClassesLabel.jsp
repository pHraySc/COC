<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>标签分类详情展示</title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<style>
	.dialog_table .showTable th,.dialog_table .showTable td{padding-top:4px;padding-bottom:4px;line-height:24px;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		//表格斑马线
		aibiTableChangeColor(".showTable");
		var labelId = '${param["labelId"]}';
		var showConfigContent = '${param["showConfigContent"]}';
		var showWhichContent = '${param["showWhichContent"]}';

		if(labelId != null && labelId != "") {
	    //获取标签详细信息
		var actionUrl = $.ctx + "/ci/ciLabelInfoAction!getLabelDetailInfo.ai2do?showConfigContent="
		                      + showConfigContent + "&showWhichContent=" + showWhichContent;
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId,
			success: function(result){
			    //设置标签详细信息
				$("#labelName").text(result.ciLabelInfo.labelName);
				if(result.ciLabelInfo.createDesc == null){
					$("#createDesc").text("");
				}else{
					$("#createDesc").text(result.ciLabelInfo.createDesc);
				}
				if(result.configLabelInfo != null){
					if(result.configLabelInfo.parentName !=null ){
						$("#parentName").text(result.configLabelInfo.parentName);
					}
				}
			}
		});	
		
		}		
	});
</script>
</head>
<body>
   	<div id="labelDetailDiv" class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<th width="110px">标签名称：</th>
			<td id="labelName">&nbsp;</td>
		</tr>
		<tr>
			<th>备注：</th>
			<td><div class="textarea_look" id="createDesc">&nbsp;</div></td>
		</tr>
		<tr>
			<th>父分类：</th>
			<td><div class="textarea_look" id="parentName">&nbsp;</div></td>
		</tr>
	</table>
	</div>
	<!-- 按钮区 -->
	<div class="dialog_btn_div">
		<input name="" type="button" value=" 确 定 " onclick="parent.closeShowLabelDialog(); " class="tag_btn"/>
	</div>
</body>
</html>
