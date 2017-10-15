<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript">
	function closeDialog(){
		parent.iframeCloseDialog("#labelAttrValidateDialog");
	}	
	</script>
</head>
<body>
<div id="selectDragTip"> 
   <div class="selectDragBox">
   </div>
   <!-- 按钮操作-->
    <div class="btnActiveBox">
	   <input type="button" value="确定" class="ensureBtn" onclick="closeDialog();"/>
    </div>
</div>
</body>
</html>