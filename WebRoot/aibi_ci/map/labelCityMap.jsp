<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/aibi_ci/html_include.jsp" %>
<title>地图</title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/dialogLang/language.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.blockUI.js"></script>
<script language="javaScript"
	src="${ctx}/aibi_ci/assets/js/jquery.metadata.min.js"></script>
<script language="javaScript"
	src="${ctx}/aibi_ci/assets/js/jquery.maphilight.js"></script>
	<script language="javaScript"
	src="${ctx}/aibi_ci/assets/js/jquery.tip.js"></script>
<script type="text/javascript">
		$(function() {
			$.blockUI({message:"<div class='aibi_progress'><p><br/>正在加载，请稍候...</p></div>"});
			//渲染地图
			$('.map').maphilight({alwaysOn:true});
			setTimeout($.unblockUI,2000); 
		});
		function showCityDel(cityid)
		{
			if(cityid!="999"){
				parent.getCityTrendChartByCityId(cityid);
			}else{
				parent.getCityFormHisChart();
		    }
		}
</script>
<style type="text/css">
    .aibi_progress {
	width:300px;
	height:30px;
	position:absolute;
	left:50%;
	top:50%;
	margin-left:-150px;
	margin-top:-15px;
}
</style>

</head>
<body>
<form name="mapForm" action="${ctx}/ci/ciMapAction!showMap.ai2do">
<input type="hidden" name="cityId" id="cityId" value="${cityId}">
</form>
<table id="t_map" border="0" cellspacing="0" cellpadding="0" style="z-index: 100;">
	<tr>
		<td style="padding-left: 60px;"><img class="map"
			src="${ctx}/aibi_ci/assets/images/${cityMap.fileName}"
			width="${cityMap.width}" height="${cityMap.height}" border="0"
			usemap="#${cityMap.cityid}"/></td>
		<td class="valign-top">
		<div id="center"
			style="position:relative; z-index:999; left: ${cityMap.centerLeft}; top: ${cityMap.centerTop};">
	 <map name="mcenter" id="mcenter">
		</map></div>
		</td>
	</tr>
</table>
<!-- 地图热区配置 -->
<map name="${cityMap.cityid}" id="${cityMap.cityid}">
	<c:set value="999" var="allProvince"></c:set>
	<c:forEach var="maphilight" items="${cityMap.maphilights}">
			<area shape="${maphilight.shape}" id="${maphilight.id}"
				class="{fillColor:'${maphilight.fillColor}',fillOpacity:${maphilight.fillOpacity},strokeColor:'${maphilight.strokeColor}',stroke:${maphilight.stroke},strokeOpacity:${maphilight.strokeOpacity},strokeWidth:${maphilight.strokeWidth}}"
				coords="${maphilight.coords}" href="${maphilight.herf}"
				title="${maphilight.toolTip.tipInfo}" />
	</c:forEach>
</map>
