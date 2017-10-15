<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/jquery.uploadify.js"></script>
	<script type="text/javascript">
	$(document).ready(function () {
		var enumCategoryId = '${param["enumCategoryId"]}';
		var columnId = '${param["columnId"]}';
		$("#columnId").val(columnId);
		$('#file_upload').uploadify({
			//buttonClass	: 'importBtn',
			btnBoxClass : 'fright',
			buttonText	: '浏览',
			width		: 60,
			height		: 24,
			fileObjName	: 'file',
			auto		: false,//为true当选择文件后就直接上传了，为false需要点击上传按钮才上传
			multi		: true,
			itemTemplate: true,
			fileTypeExts: '*.csv',
			fileTypeDesc: '导入文件格式只能是csv格式！',
	        swf			: '${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.swf',
	        uploader	: '${ctx}/ci/ciLabelInfoAction!uploadEnumListByImport.ai2do?columnId=' 
	        				+ columnId + '&enumCategoryId=' + enumCategoryId,
	        onSelect	: function(file) {
	        	var fileName = file.name;
	        	$('#fileName').val(fileName);
	        },
	        onUploadSuccess	: function(file, data, response) {
	        	var success = $.parseJSON(data).success;
	        	if(!success) {
	        		showAlert($.parseJSON(data).msg, 'failed');
	        		return;
	        	}
	        	$.fn.weakTip({"tipTxt":"上传成功！","backFn":function(){
	        		parent.iframeCloseDialog("#uploadDataDialog");
	        		parent.searchCategoryInfoList(columnId);
	    		}});
	        }
	    });
	});
	
	function submitUploadFile() {
		$('#file_upload').uploadify('upload', '*');
		/* $.fn.weakTip({"tipTxt":"上传成功！","backFn":function(){
			parent.iframeCloseDialog("#uploadDataDialog");
		}}); */
	}

</script>
</head>
<body>
<div>
<form id="saveForm" method="post">
<input id="columnId" type="hidden" name="ciEnumCategoryInfo.columnId" />
<div class="clientMsgBox bgwhite" >
	<ol class="clearfix bgwhite">
		<li class="enumCategory">
   			<label  class="fleft labFmt100 star">请选择文件：</label>
   			<input type="text" readonly="readonly" class="fleft inputFmt620" style="width:310px;"  
   				type="text" id="fileName" 
   					maxlength="35"  value="" />
   			<input type="file" name="file_upload" class="fright" id="file_upload" />
 		</li>
 	</ol>
 	
</div>
<div class="btnActiveBox">
	<a href="javascript:void(0);" id="saveBtn" onclick="submitUploadFile();" class="ensureBtn">确定</a>
</div>
</form>
</div>		
</body>
</html>