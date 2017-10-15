<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${ciLabelInfo.labelName }</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">

	$(document).ready(function(){
		//alert('${ciLabelInfo.updateCycle}');
		//alert('${param["tab"]}');
		if('${param["tab"]}'=='tag'){
			$(".top_menu").find("li").removeClass("top_menu_on").eq(2).addClass("top_menu_on").find("a")[0].click();
		}else if('${param["tab"]}'=='rel'){
			$(".top_menu").find("li").removeClass("top_menu_on").eq(0).addClass("top_menu_on").find("a")[0].click();
		}else  if('${param["tab"]}'=='ant'){
			$(".top_menu").find("li").removeClass("top_menu_on").eq(1).addClass("top_menu_on").find("a")[0].click();
		}
	});

	function toLabelRel() {
		var labelId = '${ciLabelInfo.labelId }';
		var dataDate ='${param["dataDate"]}';
		var url = $.ctx+'/ci/ciLabelRelAnalysisAction!toLabelRelAnalysis.ai2do?ciLabelRelModel.mainLabelId='+labelId+'&ciLabelRelModel.dataDate='+dataDate;
		var frameObj = $('#rel').find("iframe");
		if(frameObj.attr("src")==""){
			frameObj.attr("src",url);
		}
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	}

	function toLabelContrast() {
		var labelId = '${ciLabelInfo.labelId }';
		var dataDate ='${param["dataDate"]}';
		var param = "mainLabelId="+labelId+"&dataDate="+dataDate;
		var url = $.ctx + "/ci/ciLabelConstrastAnalysisAction!initLabelConstrast.ai2do?" + param;
		var frameObj = $('#ant').find("iframe");
		if(frameObj.attr("src")==""){
			frameObj.attr("src",url);
		}
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	}

	function toLabelDiff() {
		var labelId = '${ciLabelInfo.labelId }';
		var dataDate ='${param["dataDate"]}';
		var param = "ciLabelInfo.labelId="+labelId +'&dataDate='+dataDate;
		var url = $.ctx + "/ci/ciLabelInfoAction!labelDifferential.ai2do?" + param;
		var frameObj = $('#tag').find("iframe");
		if(frameObj.attr("src")==""){
			frameObj.attr("src",url);
		}
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	}

	window.scrollTo(0,1);
	
</script>
</head>
<body>
<div class="newHeader">
	<div class="header_left" style="background:url(${ctx}/aibi_ci/assets/themes/default/images/header_left_<%=teleOperator %>.png) no-repeat;">
        <img src="${ctx }/aibi_ci/assets/themes/default/images/title_<%=coc_province %>.png" /></div>
	<div class="clear"></div>
</div>
<div class="customerTop">
	<dl class="tag">
		<dt>${ciLabelInfo.labelName}</dt>
		<dd title="标签规则">${ciLabelInfo.busiCaliber}</dd>
	</dl>
</div>
<div class="submenu">
	<div class="top_menu">
		<ul>
            <c:choose>
            <c:when test="${ciLabelInfo.updateCycle == '2'}">
            <li class="top_menu_on" ><div class="bgleft">&nbsp;</div><a href="javascript:void(0);" class="no7" onclick="toLabelRel();"><span></span><p>关联分析</p></a><div class="bgright">&nbsp;</div></li>
			<li><div class="bgleft">&nbsp;</div><a href="javascript:void(0);" class="no8" onclick="toLabelContrast();"><span></span><p>对比分析</p></a><div class="bgright">&nbsp;</div></li>
            </c:when>
            </c:choose>
            <c:choose>
            <c:when test="${ciLabelInfo.updateCycle == '1'}">
            <li class="top_menu_on" style="display:none;" ><div class="bgleft">&nbsp;</div><a href="javascript:void(0);" class="no7" onclick="toLabelRel();"><span></span><p>关联分析</p></a><div class="bgright">&nbsp;</div></li>
			<li style="display:none;"><div class="bgleft">&nbsp;</div><a href="javascript:void(0);" class="no8" onclick="toLabelContrast();"><span></span><p>对比分析</p></a><div class="bgright">&nbsp;</div></li>
            </c:when>
            </c:choose>
			<li><div class="bgleft">&nbsp;</div><a href="javascript:void(0);"  class="no4" onclick="toLabelDiff();"><span></span><p>标签微分</p></a><div class="bgright">&nbsp;</div></li>
			<div class="clear"></div>
		</ul>
	</div>
</div>
<div>
	<div id="rel" style="display:none;">
		<iframe name="Frame" scrolling="no"  id="Frame0"  framespacing="0" src="" border="0" frameborder="0" style="width:100%;height:1000px"></iframe>
	</div>
	<div id="ant" style="display:none;">
		<iframe name="Frame" scrolling="no"  id="Frame1"  framespacing="0" src="" border="0" frameborder="0" style="width:100%;height:900px"></iframe>
	</div>
	<div id="tag" style="display:none;">
		<iframe name="Frame" scrolling="no"  id="Frame2"  framespacing="0" src="" border="0" frameborder="0" style="width:100%;height:900px"></iframe>
	</div>
</div>
</body>
</html>
