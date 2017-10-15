<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>Insert title here</title>
<script type="text/javascript">
	$(document).ready(function(){
		loadUserNumChart();
	});
	//潜在用户数
	function loadUserNumChart(){
		var offerId = '${ciVerifyRuleRel.offerId}';
		var url=escape($.ctx+'/ci/ciProductVerifyInfoAction!userNumHisInfo.ai2do?ciVerifyRuleRel.offerId='+offerId);
		var noData = '产品暂无统计数据';
                var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		$("#chart").show();
		var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "",$('#chart').width(), $('#chart').height(), "0", "0");
		bar.addParam("wmode","Opaque");
	    bar.setDataURL(url);
	    bar.render("chart");
	}
</script>
</head>
<body>
<div class="main_con_box verify_main_con_box">
    	<div class="aibi_chart_div" id="chart" style="width:100%;height:280px">
</div><!--main_con_box end -->
</div>
</body>
</html>