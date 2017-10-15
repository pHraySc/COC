<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
var initParam;
var paramMult;
$(document).ready(function(){
    var beginDate='${param["ciLogStatAnalysisModel.beginDate"] }';
    var endDate='${param["ciLogStatAnalysisModel.endDate"]}';
    var parentId='${param["ciLogStatAnalysisModel.parentId"]}';
    var secondDeptId ='${param["ciLogStatAnalysisModel.secondDeptId"]}';
    var tab = '${param["tab"]}';
    var thirdFlag='${param["thirdFlag"]}';
	initParam = "ciLogStatAnalysisModel.beginDate="+beginDate
			+"&ciLogStatAnalysisModel.endDate="+endDate
			+"&ciLogStatAnalysisModel.secondDeptId="+secondDeptId
			+"&ciLogStatAnalysisModel.parentId="+parentId
			+"&tab="+tab;
	
	//多部门参数
		paramMult= "ciLogStatAnalysisModel.beginDate="+beginDate
		+"&ciLogStatAnalysisModel.endDate="+endDate
		+"&ciLogStatAnalysisModel.secondDeptId="+secondDeptId
		+"&ciLogStatAnalysisModel.parentId="+parentId
		+"&ciLogStatAnalysisModel.thirdDeptId="+thirdFlag
		+"&tab="+tab;
		
	if('${param["tab"]}'=='one'){
		$(".top_menu").find("li").removeClass("top_menu_on").eq(0).addClass("top_menu_on").find("a")[0].click();
		var url = $.ctx+"/ci/ciLogStatAnalysisAction!findOneDeptOpLogMain.ai2do?"+initParam;
		$("#Frame0").attr("src",url);
		$("#Frame0").load();
		document.title = "部门趋势对比";		
	}else if('${param["tab"]}'=='mul'){
		$(".top_menu").find("li").removeClass("top_menu_on").eq(1).addClass("top_menu_on").find("a")[0].click();
		var url = $.ctx+"/ci/ciLogStatAnalysisAction!logMultDeptCompareInit.ai2do?"+paramMult;
		$("#Frame0").attr("src",url);
		$("#Frame0").load();
		document.title = "多部门对比";			
	}
});
function a(){
	var url = $.ctx+"/ci/ciLogStatAnalysisAction!findOneDeptOpLogMain.ai2do?"+initParam;
	if($(".top_menu").find("li").eq(0).attr("class")!="top_menu_on"){
		$("#Frame0").attr("src",url);
		$("#Frame0").load();
		document.title = "部门趋势对比";		
	}
}
function b(){
	var url = $.ctx+"/ci/ciLogStatAnalysisAction!logMultDeptCompareInit.ai2do?"+paramMult;
	if($(".top_menu").find("li").eq(1).attr("class")!="top_menu_on"){
		$("#Frame0").attr("src",url);
		$("#Frame0").load();
		document.title = "多部门对比";			
	}
}
</script>
</head>
<body>
<div class="newHeader">
	<div class="header_left" style="background:url(${ctx}/aibi_ci/assets/themes/default/images/header_left_<%=teleOperator %>.png) no-repeat;">
        <img src="${ctx }/aibi_ci/assets/themes/default/images/title_<%=coc_province %>.png" /></div>
	<div class="clear"></div>
</div>
<div class="submenu">
	<div class="top_menu">
		<ul>
            <li><div class="bgleft">&nbsp;</div><a href="javascript:void(0);" onclick="a()" class="no4"><span></span><p>部门趋势对比</p></a><div class="bgright">&nbsp;</div></li>
			<li><div class="bgleft">&nbsp;</div><a href="javascript:void(0);" onclick="b()" class="no7"><span></span><p>多部门对比</p></a><div class="bgright">&nbsp;</div></li>
			<div class="clear"></div>
		</ul>
	</div>
</div>
<div>
	<div id="content">
		<iframe name="Frame" src=""  scrolling="no"  id="Frame0"  framespacing="0" src="" border="0" frameborder="0" style="width:100%;height:1000px"></iframe>
	</div>
</div>
</body>
</html>
