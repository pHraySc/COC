<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
		<c:set var="DIM_VIP_LEVEL" value="<%=CommonConstants.TABLE_DIM_VIP_LEVEL%>"></c:set>
		<script type="text/javascript" language="javascript">
	    var customGroupId = $('#customGroupId').val();
    	var dataDate = $('#dataDate').val();
		$(document).ready(function(){

			aibiTableChangeColor(".showTable");
			aibiTableChangeColor(".mainTable");
			$("#icon1").removeClass("icon_chart_gray").addClass("icon_chart");
			$("#icon2").removeClass("icon_list").addClass("icon_list_gray");
			$("#icon1").unbind("click");
			$("#icon2").unbind("click");
			$("#icon1").click(function(e){
				if ($(this).hasClass("icon_chart")) {
					
				} else {
					$(this).removeClass("icon_chart_gray").addClass("icon_chart");
					$("#icon2").removeClass("icon_list").addClass("icon_list_gray");
					$("#tablediv").hide();
					$("#chartdiv").show();
				}
				getVipLevelFormChart();
				getVipLevelFormHisChart();
			});
			$("#icon2").click(function(e){
				if ($(this).hasClass("icon_list")) {
					
				} else {
					$(this).removeClass("icon_list_gray").addClass("icon_list");
					$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
					$("#chartdiv").hide();
					$("#tablediv").show();
				}
				getVipLevelFormHisTable();
			});
			/* getVipLevelFormChart();
			getVipLevelFormHisChart();
			getVipLevelFormHisTable(); */
			
			$("#icon1").click();
		});

		//获取VIP等级构成图
		function getVipLevelFormChart(){

			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!vipFormChartData.ai2do?'+param);
		    //环形图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MultiLevelPie.swf"+chartParam, "",$('#vipFormDiv').width(), $('#vipFormDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("vipFormDiv");		   
		}

		//VIP等级构成历史柱状图
		function getVipLevelFormHisChart(){
			$('#returnLink').hide();
			$('#vipFormHisDiv').height("259px");
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!vipTrendChartData.ai2do?'+param);

		    //堆积柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/StackedColumn2D.swf"+chartParam, "",$('#vipFormHisDiv').width(), $('#vipFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("vipFormHisDiv");		   
		}
		
		//VIP等级历史表格数据
		function getVipLevelFormHisTable() {

	    	$('#tablediv').load(
	    		$.ctx+'/ci/customersFormAnalysisAction!vipHistoryData.ai2do',
	    		{
	    			'ciCustomGroupForm.customGroupId':customGroupId,'ciCustomGroupForm.dataDate':dataDate,
	    			'ciCustomGroupForm.updateCycle':updateCycle
	    		},
	    		function() {
		    	}
		   );	
		}

		function getVipTrendChartByVipLevelId(vipLevelId) {

			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.vipLevelId='+vipLevelId;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!vipTrendChartDataByVipLevelId.ai2do?'+param);

		    //柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "",$('#vipFormHisDiv').width(), $('#vipFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("vipFormHisDiv");
		}

		</script>
		
		<!--品牌构成图 -->
		<div class="aibi_chart_div" id="chartdiv" style="height:259px;">
			<table width="100%">
				<tr>
					<td id="vipFormDiv" style="height:259px;width:40%;"></td>
					<td style="height:259px;width:60%;" >
						<div id="vipFormHisDiv" style="height:259px;"></div>
						<div id="returnLink" style="display:none;">
							<a href="javascript:void(0);" onclick="getVipLevelFormHisChart();return false;">返回</a>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<!-- 品牌历史数据表格 -->
		<div class="aibi_table_div" id="tablediv" style="display:none;">
		</div>