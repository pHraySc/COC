<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@page import="com.ailk.biapp.ci.constant.CommonConstants"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<c:set var="intervalDays" value="15"/><!-- 趋势预览显示天数，默认15天 -->
<head>
<title>日志统计</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		
		$("#showchartImg").click(function(){
			var endDate = "20140103";
			var beginDate = getBeginDate(endDate,'${intervalDays }' ) ;
			var secondDeptId = $(this).parent().find("a").attr('secondDeptId');
			var secondDeptName = $(this).parent().find("a").html();
			var title = secondDeptName + "(" + beginDate + "至" + endDate + ")";
			$("#trendDialog").dialog({
				autoOpen: false,
				resizable:false,
				height: 450,
				width: 800,
				title:  title,
				modal: true,
				position: [150,100],
				close:function(){}
			});
			$("#trendDialog").dialog("open");
			
			var url=$.ctx+"/ci/ciLogStatAnalysisAction!findAnaOpTypeJson.ai2do";
			$.ajax({
				type: "POST",
				url: url,
		      	data:"",
		      	success: function(result){
		       		var list = result.opTypeModelList;
		       		var html = "";
		       		for(var i=0; i<list.length; i++){
		       			var opTypeId = list[i].opTypeId;
		       			var opTypeName = list[i].opTypeName;
		       			html += "<input type='checkbox' name='opTypeId' value='" + opTypeId + "'/>" + opTypeName + "&nbsp;";
		       		}
		       		$("#dialog_btn_div").html(html);
		            
		            $("input[name='opTypeId']").click(function(){
		            	this.blur();  
		            	this.focus();
		             });
		             
		            $("input[name='opTypeId']").change(function(){
			              var opTypeId = "";
			              $("input[name='opTypeId']:checked").each(function(){
			              	 opTypeId += $(this).val() + ",";
			              });
			              opTypeId = opTypeId.substring(0, opTypeId.length-1);
			              var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
				               + "&ciLogStatAnalysisModel.endDate=" + endDate
				               + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId
				               + "&ciLogStatAnalysisModel.opTypeId=" + opTypeId;
			              loadDigChart(param);
		             });
		            
					$("input[name='opTypeId']:first").attr("checked",true);
					var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
			               + "&ciLogStatAnalysisModel.endDate=" + endDate
			               + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId
			               + "&ciLogStatAnalysisModel.opTypeId=" + $("input[name='opTypeId']:first").val();
		            loadDigChart(param);
				}
			});

		});

	});
	function loadDigChart(param){
		var url=escape("${ctx}/ci/ciLogStatAnalysisAction!findOneDeptOpLogTrend.ai2do?"+param);
		var noData = "该部门暂无数据";
        var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		var bar = new FusionCharts("${ctx}/aibi_ci/assets/flash/ScrollLine2D.swf"+chartParam, "",$("#trendChart").width(), $("#trendChart").height(), "0", "0");
		bar.addParam("wmode","Opaque");
		bar.setDataURL(url);
	    bar.render("trendChart");
	}
	
	function toADeptOpTypesMain(obj){
		var beginDate = "20131210" ;
		var endDate = "20131230";
		var secondDeptId = $(obj).attr('secondDeptId');
		var secondDeptName = $(obj).html();
		var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
	        + "&ciLogStatAnalysisModel.endDate=" + endDate
	        + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId;
		var url = $.ctx+"/ci/ciLogStatAnalysisAction!findOneDeptOpLogMain.ai2do?"+param;
		window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		
	}
	
	function getBeginDate(endDate, intervalDays){
		var endDateTemp;
		if( endDate.length >= 8 ){
			endDateTemp = new Date(endDate.substring(0,4),endDate.substring(4,6)-1,endDate.substring(6,8));
		}
		var beginDateTemp = new Date(endDateTemp);
		beginDateTemp.setDate(endDateTemp.getDate()-intervalDays);
		beginDate = $.datepicker.formatDate("yymmdd",beginDateTemp);
		return beginDate;
	}
	
</script>
<style type="text/css">
body,html{height:100%;}
.showchartImg{
	background-image: url(${ctx}/aibi_ci/assets/themes/default/images/icon_ll.png)!important;
	background-repeat: no-repeat !important;
	background-position: -26px 0px !important;
	cursor: pointer;
	overflow: hidden;
	vertical-align: middle;
	display: inline-block;
}
.diaglog_checkbox_div{
	text-align:center; 
	background: #efefef; 
	padding-top:8px; 
	padding-bottom: 8px;
	width:100%;
	position: absolute;
	bottom: 0px;
	height: 30px;
}
.ui-dialog .ui-dialog-titlebar{line-height:auto;}
</style>
</head>
<body>
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp"/>

<div class="mainCT">
	<div class="submenu">
		<div class="submenu_left" style="float:left"></div>
		<div class="top_menu">
			<ul>
				<li id="logStatTitle" style="white-space:nowrap;" class="top_menu_on"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciLogStatAnalysisAction!logStatIndex.ai2do" class="no2"><span></span><p>日志统计</p></a><div class="bgright">&nbsp;</div></li>
	            <li id="logAnalysisTitle" style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciLogStatAnalysisAction!logAnalysisIndex.ai2do" class="no21"><span></span><p>日志分析</p></a><div class="bgright">&nbsp;</div></li>
				<div class="clear"></div>
			</ul>
		</div>
	</div>
	<div class="aibi_search" style="height:35px">
		<form id="queryForm" action="" method="post">
		<ul class="clearfix">
			<li>
			     <span class="line2_span">记录日志时间：</span>
			     <input name="searchConditionInfo.startDate" id="startDate" readonly type="text" class="aibi_input aibi_date"  onclick="WdatePicker()" value=""/> -- 
				 <input name="searchConditionInfo.endDate" id="endDate" readonly type="text" class="aibi_input aibi_date"  onclick="WdatePicker()" value=""/>
			</li>
			<li><input id="searchLabelButton" name="" type="button" value="" class="aibi_search_btn"/></li>
			<li><a href="javascript:void(0)" onclick="javascript: toADeptOpTypesMain(this)" secondDeptId="0">测试部门</a>
				<span id="showchartImg" class="showchartImg">&nbsp;&nbsp;&nbsp;</span>
			</div></li>
		</ul>
		<div class="clear"></div>
		</form>
	</div>
</div>
<!-- mainCT end -->

<!-- 单部门多操作类型操作趋势图 -->
<div id="trendDialog" style="display:none;">
	<div>
		<table width="100%" >
			<tr>
				<td style="text-line:23px; color:#444; height:27px; font: 16px;">最近${intervalDays}天趋势</td>
			</tr>
			<tr>
				<td>
					<div id="trendChart" style="width:780px; height:350px"></div>
				</td>
			</tr>
		</table>
	</div>
	<!-- 按钮区 -->
	<div id="dialog_btn_div" class="diaglog_checkbox_div">
		<input type="checkbox" name="opTypeId" value="1"/>登陆次数&nbsp;
		<input type="checkbox" name="opTypeId" value="2" />单标签次数&nbsp;
	</div>
</div>
</body>
</html>