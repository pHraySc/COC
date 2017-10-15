<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
		<c:set var="DIM_VIP_LEVEL" value="<%=CommonConstants.TABLE_DIM_VIP_LEVEL%>"></c:set>
		<script type="text/javascript" language="javascript">
	    
		var labelId = $('#labelId').val();
    	var dataDate = $('#dataDate').val();
    	function FC_Rendered(DOMId){
    		//chars loaded callback
    	}
    	$(document).ready(function(){

			aibiTableChangeColor(".showTable");
			aibiTableChangeColor(".mainTable");
			$("#icon1").removeClass("icon_chart_gray").addClass("icon_chart");
			$("#icon2").removeClass("icon_list").addClass("icon_list_gray");
			$("#icon1").click(function(e){
				if ($(this).hasClass("icon_chart")) {
					
				} else {
					$(this).removeClass("icon_chart_gray").addClass("icon_chart");
					$("#icon2").removeClass("icon_list").addClass("icon_list_gray");
				}
				$("#tablediv").hide();
				$("#chartdiv").show();
			});
			$("#icon2").click(function(e){
				if ($(this).hasClass("icon_list")) {
					
				} else {
					$(this).removeClass("icon_list_gray").addClass("icon_list");
					$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
				}
				$("#chartdiv").hide();
				$("#tablediv").show();
			});
			getVipLevelFormChart();
			getVipLevelFormHisChart();
			getVipLevelFormHisTable();
		});

		//获取VIP等级构成图
		function getVipLevelFormChart(){

			var param = 'ciLabelFormModel.labelId='+labelId;
				param += '&ciLabelFormModel.dataDate='+dataDate;
				param += '&ciLabelFormModel.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/ciLabelFormAnalysisAction!findVipFormChartData.ai2do?'+param);
		    //环形图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MultiLevelPie.swf"+chartParam, "vipLevelFormChart",$('#vipFormDiv').width(), $('#vipFormDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("vipFormDiv");		   
		}

		//VIP等级构成历史柱状图
		function getVipLevelFormHisChart(){
			var param = 'ciLabelFormModel.labelId='+labelId;
				param += '&ciLabelFormModel.dataDate='+dataDate;
				param += '&ciLabelFormModel.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/ciLabelFormAnalysisAction!findVipTrendChartData.ai2do?'+param);

		    //堆积柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/StackedColumn2D.swf"+chartParam, "vipLevelFormHisChart",$('#vipFormHisDiv').width(), $('#vipFormHisDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("vipFormHisDiv");		   
		}
		
		//VIP等级历史表格数据
		function getVipLevelFormHisTable() {

	    	$('#tablediv').load(
	    			$.ctx+'/ci/ciLabelFormAnalysisAction!findVipHistoryData.ai2do',
	    		{
	    			'ciLabelFormModel.labelId':labelId,'ciLabelFormModel.dataDate':dataDate,
	    			'ciLabelFormModel.updateCycle':updateCycle
	    		},
	    		function() {
		    	}
		   );	
		}

		function getVipTrendChartByVipLevelId(vipLevelId) {

			var param = 'ciLabelFormModel.labelId='+labelId;
				param += '&ciLabelFormModel.dataDate='+dataDate;
				param += '&ciLabelFormModel.vipLevelId='+vipLevelId;
				param += '&ciLabelFormModel.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/ciLabelFormAnalysisAction!findVipTrendChartDataByVipLevelId.ai2do?'+param);

		    //柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "vipTrendChartByVipLevelId",$('#vipFormHisDiv').width(), $('#vipFormHisDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("vipFormHisDiv");
		}

		</script>
		
		<!--品牌构成图 -->
		<div class="aibi_chart_div" id="chartdiv" style="height:280px;width:100%;">
			<table width="100%">
				<tr>
					<td id="vipFormDiv" style="height:280px;width:40%;"></td>
					<td style="height:280px;width:60%;" >
						<div id="vipFormHisDiv" style="height:280px;width:100%;"></div>
					</td>
				</tr>
			</table>
		</div>
		
		<!-- 品牌历史数据表格 -->
		<div class="aibi_table_div" id="tablediv" style="display:none;">
		</div>
