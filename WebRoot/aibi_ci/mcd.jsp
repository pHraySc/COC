<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>客户经营中心</title>
<script type="text/javascript">
//营销活动
function toTab1(){
	//营销活动策划
	$.ajax({//记录日志
		url: "${ctx}/ci/marketingStrategyAction!createLogForMCD.ai2do?witchPage="+"MCD_URL_CAMP",
		type: "POST",
		success: function(result){}
	});
	var url = "<%=com.asiainfo.biframe.utils.config.Configure.getInstance().getProperty("MCD_URL_CAMP")%>";
	var frameObj = $('#tab1').find("iframe");
	if(frameObj.attr("src")==""){
		frameObj.attr("src",url);
		frameObj.load();
	}
	frameObj.parent().parent().find("div").hide();
	frameObj.parent().show();
}
//营销策略
function toTab2(){
	//营销案策划
	$.ajax({//记录日志
		url: "${ctx}/ci/marketingStrategyAction!createLogForMCD.ai2do?witchPage="+"MCD_URL_PLAN",
		type: "POST",
		success: function(result){}
	});
	var url = "<%=com.asiainfo.biframe.utils.config.Configure.getInstance().getProperty("MCD_URL_PLAN")%>";
	var frameObj = $('#tab2').find("iframe");
	if(frameObj.attr("src")==""){
		frameObj.attr("src",url);
		frameObj.load();
	}
	frameObj.parent().parent().find("div").hide();
	frameObj.parent().show();
}
</script>
</head>
<body class="body_bg">
<div class="sync_height">
    <jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
    <div class="mainCT">
		<div class="submenu">
			<div class="submenu_left" style="float: left;"></div>
			<div class="top_menu">
				<ul>
					<li class="top_menu_on" style="white-space: nowrap;"><div class="bgleft">&nbsp;</div><a class="no2" href="javascript:;" onclick="toTab2()"><span></span><p>营销策略</p></a><div class="bgright">&nbsp;</div></li>
					<li style="white-space: nowrap;"><div class="bgleft">&nbsp;</div><a class="no9" href="javascript:;" onclick="toTab1()"><span></span><p>营销活动</p></a><div class="bgright">&nbsp;</div></li>
					<div class="clear"></div>
				</ul>
			</div>
		</div>
		<div>
			<div id="tab1" style="display:none;">
				<iframe name="Frame" scrolling="no"  src="" id="Frame0"  framespacing="0" border="0" frameborder="0" style="width:100%;height:800px"></iframe>
			</div>
			<div id="tab2" style="display:none;">
				<iframe name="Frame" scrolling="no"  src="<%=com.asiainfo.biframe.utils.config.Configure.getInstance().getProperty("MCD_URL_PLAN")%>" id="Frame1"  framespacing="0" border="0" frameborder="0" style="width:100%;height:800px"></iframe>
			</div>
		</div> 
    </div>
    <div class="clear"></div>
</div><!--sync_height end -->
<script>
$(function(){
	$("#_mcd").addClass("menu_on");
	toTab2();
});
</script>
</body>
</html>
