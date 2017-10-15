<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>

<html>
	<head>
		<%@ include file="../../html_include.jsp"%>
		<title></title>
		<%
			//这里放css和js脚本的引用。引用在assets.jsp里面定义的变量地址
		%>
		<style type="text/css">
		</style>
		<script type="text/javascript" language="javascript">
		var noData = '${noDataToDisplay}';
		var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		function FC_Rendered(DOMId){
    		//alert("3 loaded");
    	}
		$(document).ready(function(){
			var noLabelRel = '${noLabelRel}';
			if(noLabelRel != '1') {
				getLabelRelChart();
			}
		});

		//获取关联分析结果图
		function getLabelRelChart(){
		    var labelId = $('#labelId').val();
	    	var dataDate = $('#dataDate').val();
			var param = 'ciLabelRelModel.mainLabelId='+labelId;
				param += '&ciLabelRelModel.dataDate='+dataDate;

			var chart_url = escape($.ctx+'/ci/ciLabelRelAnalysisAction!findRelLabelByMainLabel.ai2do?'+param);

		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/DragNode.swf"+chartParam, "labelRelChart", $('#labelRelDiv').width(), $('#labelRelDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("labelRelDiv");		   
		}

		function openLabelRel() {
			var labelId  = $("#labelId",parent.document).val();
			//从父页面获取
			var dataDate = $("#dataDate",parent.document).val();
			var param = "ciLabelInfo.labelId="+labelId;
				param = param + "&dataDate="+dataDate;
				param = param + "&tab=rel";
			var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
			window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		}
		
		</script>
	</head>
	<body>
		<input id="labelId" type="hidden" value="${ciLabelRelModel.mainLabelId}"/>
		<input id="dataDate" type="hidden" value="${ciLabelRelModel.dataDate}"/>
		<div></div>
		<div class="main_con_box">
			<!--标题行开始 -->
			<div class="main_con_top">
				<div class="tag_box_title tag_box_title_glfx"><span>关联对比</span>统计日期：${dateFmt:dateFormat(ciLabelRelModel.dataDate)}&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="openLabelRel()">配置关联图谱</a></div>
				<div class="tag_box_manage">
					<!--选择框 -->
					<div class="like_button_box" id="select_box" style="width:110px;float:right">
						<div class="like_button_left"></div>
						<div class="like_button_middle" style="width:72px"><span>关联分析</span></div>
						<div class="like_button_hr"></div>
						<div class="like_button_middle" style="width:26px"><a href="javascript:void(0);" class="icon_select"></a></div>
						<div class="like_button_right"></div>
						<div class="clear"></div>
					</div>
				</div>
				<div class="clear"></div>
				<!--弹出层-->
				<div class="form-field" style="right:15px;_right:14px;float:right">
					<ul>
						<li class="on"><a href="javascript:void(0);" >关联分析</a></li>
						<li><a href="javascript:void(0);" onClick="window.parent.loadLabelContrast();">对比分析</a></li>
					</ul>
				</div> 
			</div>
			<div id="labelRelDiv" style="height:600px;width:100%;">
				<c:if test="${noLabelRel == 1}">             
                    <div class="aibi_table_div" style="height:600px;text-align:center;line-height:600px">
                       		<div class="aibi_chart_nodata">
        					<div class="nodata_txt"></div>
        			</div>
                    </div>
				</c:if>
			</div>		
		</div>
	</body>
</html>