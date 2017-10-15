<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include.jsp"%>
<html>
	<head>
		<%@ include file="../html_include.jsp"%>
	<script type="text/javascript">
	$(function(){
		var type = '${param.type}';
		if(type == 'rel') {
			toLabelRel();
		}
		if(type == 'constrast') {
			toLabelConstrast();
		}
	})

	function toLabelRel() {
		var mainLabelId = ${param.mainLabelId};
		var dataDate = '${param.dataDate}';
		
		var param = "ciLabelRelModel.mainLabelId="+mainLabelId;
		param = param + "&ciLabelRelModel.dataDate="+dataDate;
		var url = $.ctx+"/ci/ciLabelRelAnalysisAction!toLabelRelAnalysis.ai2do?"+param;
		$('#contentDiv').load(url);
	}

	function toLabelConstrast() {
		var mainLabelId = ${param.mainLabelId};
		var dataDate = '${param.dataDate}';
		var param = "mainLabelId="+mainLabelId;
		param = param + "&dataDate="+dataDate;
		var url = $.ctx + "/ci/ciLabelConstrastAnalysisAction!initLabelConstrast.ai2do?" + param;
		$('#contentDiv').load(url);
	}
	
	</script>
</head>

<body class="body_bg">
<input id="dataDate" type="hidden" value="${param.dataDate}"/>
<div class="top_menu analyse_top_menu">
    <ul>
        <li class="top_menu_on"><a href="javascript:void(0);" class="no7" onClick="toLabelRel();"><span></span><p>关联分析</p></a></li>
        <li><a href="javascript:void(0);" class="no8" onClick="toLabelConstrast();"><span></span><p>对比分析</p></a></li>
        <div class="clear"></div>
    </ul>
</div>
<div id="contentDiv">

</div>
</body>
</html>
