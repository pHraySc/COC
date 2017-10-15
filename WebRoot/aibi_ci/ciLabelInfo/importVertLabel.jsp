<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入组合标签</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>

</head>
<body>
<form id="saveForm" method="post" action="<%=request.getContextPath() %>/ci/ciLabelInfoConfigAction!loadVertLabelInfoFile.ai2do" enctype="multipart/form-data">
	<div  id="fileImportBox"  class="importWayBox"> 
		<ol class="clearfix">
			<li></li>
			<li></li>
			<li> 
		    	<label  class="fleft labFmt110 star">导入文件： </label>
			    <div class="fleft" >
			       <input  type="text"  class="fleft inputFmt230" name="labelFileText" id="labelFileText"  readonly="readonly"/>
				   <input  type="button"  class="fleft importFileBtn" value="浏览"  />
			       <input onkeyup="$('#fnameTip').hide();"  type="file"  class="fleft customGroupFile" id="labelFile" name="file" />
			       <a id="importTip" class="fleft downAndHelp " href="<%=request.getContextPath() %>/ci/ciLabelInfoConfigAction!downImportTempletFile.ai2do?pojo.isVertLabel=1" title="">示例</a>
			       <div id="fnameTip" class="tishi error"  style="display:none;margin-top: 0px;"></div>
		    	</div>
			</li> 
		</ol>
	</div>
	<!-- 按钮区 -->
	<div class="dialog_btn_div">
		<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
	</div>
</form>
<script type="text/javascript">
$(document).ready(function(){
	//表格斑马线
	changeTableColor(".showTable");
	$( document ).tooltip({
		items: "#importTip",
		content: function() {
			var html="<ul class='importCustermersTip'><li>1.标签数据文件必须是csv格式文件;</li><li>2.文件编码必须为UTF-8无BOM格式;</li></ul>";
			return html;
		}
	});
	$("#saveBtn").bind("click",function(){
		saveImport();
	});
	
	//统一浏览器对FILE 文件获取上传路径的差异 
	$("#labelFile").change(function(){
		$("#labelFileText").val($(this).val());
	  	var file = $(this).val();
		var fileName = file.substr(file.lastIndexOf("."));   
		fileName = fileName.toUpperCase();
		if(fileName != ".csv" && fileName != ".CSV"){
	    	$("#fnameTip").empty();
			$("#fnameTip").show().append("请选择合法格式文件！");
		}else{
			$("#fnameTip").empty();
			$("#fnameTip").hide();
		}
	});
	
});

function hideTip(){
	$("#labelNameTip").hide();
}

function saveImport(){
	if(validateForm()){
		//导入标签保存
		$("#saveForm").submit();
	}
}

//表单验证
function validateForm(){
	var file = $("#labelFile").val();
	var fileName = file.substr(file.indexOf("."));   
	if(fileName == ""){
		$("#fnameTip").empty();
		$("#fnameTip").show().append("请选择文件");
		return false;
	}
    if(fileName != ".csv" && fileName != ".txt"){
    	$("#fnameTip").empty();
		$("#fnameTip").show().append("请选择合法格式文件");
		return false;
    }
    $("#fnameTip").hide();
    return true;
}

</script>
</body>
</html>