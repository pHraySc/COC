<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发布标签</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$("#saveBtn").bind("click", function(){
		publishLabel();
	});
});

//发布
function publishLabel(){
	var actionUrl = $.ctx + "/ci/ciLabelInfoConfigAction!publishLabel.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "configLabelInfo.labelId="+$("#labelId").val(),
		success: function(result){
			if(result.success){
				//showAlert("" +result.cmsg,"success",openLabelConfig);
				$.fn.weakTip({"tipTxt":result.cmsg,"backFn":function(){
					openLabelConfig();//刷新列表
				 }});
			}else{
				showAlert(result.cmsg,"failed");
			}
	    }
	});
}

//刷新标签维护页面
function openLabelConfig(){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=3&labelSearchType="+4;
	window.location = url;
}
</script>
</head>
<body>
	<jsp:include page="/aibi_ci/navigation.jsp"></jsp:include>
	<div class="save-tag-content">
		<form id="saveForm" method="post">
		<input type="hidden" id="labelId" name="configLabelInfo.labelId" value="${configLabelInfo.labelId }"/> 
			<input type="hidden" id="dimTransId" />
			<div class="save-customer-header comWidth clearfix ">
				<div class="logoBox clearfix">
					<div class="index_logo fleft" onclick="showHomePage();return false;"></div>
					<div class="secTitle fleft">标签发布</div>
				</div>
			</div>
			<div class="comWidth clearfix save-customer-inner save-tag-content">
				<div class="clearfix titleBox">
					<div class="starIcon fleft"></div>
					<div class="customer-title fleft">标签发布配置</div>
				</div>
				<div class="clientBaseList clearfix">
					<ul>
						<li class="clearfix">
						<p class="fleft fmt160"><span class="fleft"></span> <label>SQL：</label>
						</p>
						<p class="fleft textareaBox"><textarea class="fleft textarea628_60"
							id="countRulesDesc" name="configLabelInfo.countRulesDesc" readonly="readonly" >${pojo.sql }</textarea>
						</p>
						</li>
						<li class="clearfix">
						<p class="fleft fmt160"><span class="fleft"></span> <label>发布备注：</label>
						</p>
						<p class="fleft textareaBox"><textarea class="fleft textarea628_60"
							id="publishDesc"
							onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}"
							onblur="this.value = this.value.slice(0,170)"
							name="configLabelInfo.publishDesc"></textarea></p>
						</li>
					</ul>
				</div>
			</div>
			<div class="dialog_btn_div"><input id="saveBtn" name="" type="button" value="发布" class="tag_btn" /></div>
		</form>
	</div>
</body>
</html>
