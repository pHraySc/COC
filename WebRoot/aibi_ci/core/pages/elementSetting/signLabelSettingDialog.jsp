<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(function(){
	var labelFlag = '${param["ciLabelRule.labelFlag"]}';
	
	if(labelFlag == "" || labelFlag=="undefined"){
		labelFlag = "1";
	}
	$(".queryMethod").each(function(){
		if($(this).attr("value") == labelFlag){
			$(this).addClass("current");
		}else{
			$(this).removeClass("current");
		}
	});
	
	//数值查询选项
	$(".queryMethod").click(function(){
		$(".queryMethod").removeClass("current");
		$(this).addClass("current");
	});
	$(".queryMethod").hover(function(){
		$(this).addClass("hover");
	},function(){
		$(this).removeClass("hover");
	});
});

function setLabelValue(){
	var labelFlag="";
	$(".queryMethod").each(function(){
		if($(this).hasClass("current")){
			labelFlag= $(this).attr("value");
		}
	});
	var source = $("#source").val();
	var target = null;
	if (source == 1) {
		target = parent.$("._idClass");
	} else if (source == 2) {
		target = parent.$("._idClass").parent().parent();
	}
	target.attr("labelFlag", labelFlag);
	if (source == 1) {
		var sort = target.attr('sort');
		parent.coreMain.submitShopCartSession(sort);//提交session
	}
	parent.ShoppingCart.iframeCloseDialog("#signLabelSetting");
}	
</script>
</head>
<body>	 
<div class="number-value-setting-box" style="margin-top:15px;">
	<dl>
		<dt style="margin-left:50px;">
			<div class="queryMethod" value="1">是</div>
		</dt>
		<dt style="margin-left:50px;">
			<div class="queryMethod" value="0">否</div>
		</dt>
	</dl>
</div>
<!-- 按钮操作-->
<div class="btnActiveBox">
	<input type="button" value="确定" class="ensureBtn" id="ensureBtn" onclick="setLabelValue();"/>
</div>
<input type="hidden" id="source" name="source" value="${source}"/>
</body>
</html>