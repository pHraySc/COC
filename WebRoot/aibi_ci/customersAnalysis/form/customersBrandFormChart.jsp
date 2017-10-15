<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../../include.jsp"%>
		<c:set var="DIM_BRAND" value="<%=CommonConstants.TABLE_DIM_BRAND%>"></c:set>
		<script type="text/javascript" language="javascript">
		/* function FC_Rendered(DOMId){
			if(window.parent.loadFlag) {
				window.parent.loadCustomersRel();
    			window.parent.loadFlag = false;
    		}
		} */
	    var customGroupId = $('#customGroupId').val();
    	var dataDate = $('#dataDate').val();
    	var updateCycle = $('#updateCycle').val();
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
				getBrandFormChart();
				getBrandFormHisChart();
			});
			$("#icon2").click(function(e){
				if ($(this).hasClass("icon_list")) {
					
				} else {
					$(this).removeClass("icon_list_gray").addClass("icon_list");
					$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
					$("#chartdiv").hide();
					$("#tablediv").show();
					getBrandFormHisTable();
				}
			});
			/* getBrandFormChart();
			getBrandFormHisChart();
			getBrandFormHisTable(); */
			
			$("#icon1").click();
		});

		//获取品牌构成图
		function getBrandFormChart(){

			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersBrandFormChartData.ai2do?'+param);

		    //环形图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MultiLevelPie.swf"+chartParam, "customerBrandFormChart",$('#brandFormDiv').width(), $('#brandFormDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("brandFormDiv");		   
		}

		//品牌构成历史柱状图
		function getBrandFormHisChart(){
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
			    param += '&ciCustomGroupForm.dataDate='+dataDate;
			    param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersBrandTrendChartData.ai2do?'+param);

		    //堆积柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/StackedColumn2D.swf"+chartParam, "",$('#brandFormHisDiv').width(), $('#brandFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("brandFormHisDiv");		   
		}

		function getSubBrandFormChart(brandId) {
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.brandId='+brandId;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;
			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersSubBrandFormChartData.ai2do?'+param);

		    //柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Column2D.swf"+chartParam, "",$('#brandFormHisDiv').width(), $('#brandFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("brandFormHisDiv");
		}
		
		//品牌历史表格数据
		function getBrandFormHisTable() {
	    	$('#tablediv').load(
	    		$.ctx+'/ci/customersFormAnalysisAction!customersBrandHistoryData.ai2do',
	    		{
	    			'ciCustomGroupForm.customGroupId':customGroupId,'ciCustomGroupForm.dataDate':dataDate,
	    			'ciCustomGroupForm.updateCycle':updateCycle
	    		},
	    		function() {
		    	}
		   );	
		}

		</script>
		<!--品牌构成图 -->
		<div class="aibi_chart_div" id="chartdiv" style="height:259px;">
			<table width="100%">
				<tr>
					<td id="brandFormDiv" style="height:259px;width:40%;"></td>
					<td style="height:259px;width:60%;">
						<div id="brandFormHisDiv" style="height:259px;"></div>
					</td>
				</tr>
			</table>
		</div>
		<!-- 品牌历史数据表格 -->
		<div class="aibi_table_div" id="tablediv" style="display:none;">
		</div>
