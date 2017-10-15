<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
		<c:set var="DIM_BRAND" value="<%=CommonConstants.TABLE_DIM_BRAND%>"></c:set>
		<script type="text/javascript" language="javascript">
	    var labelId = $('#labelId').val();
    	var dataDate = $('#dataDate').val();
    	/* function FC_Rendered(DOMId){
    		//alert("2 loaded");
    		if(window.parent.loadFlag) {
    			window.parent.loadLabelRel();
    			window.parent.loadFlag = false;
    		}
    		
    	} */
    	var updateCycle = $('#updateCycle').val();
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
				if(formTypeSign == 'City') {
					getCityFormHisTable();
				} else if(formTypeSign == 'Vip') {
					getVipLevelFormHisTable();
				} else {
					getBrandFormHisTable();
				}
				
				if ($(this).hasClass("icon_list")) {
					
				} else {
					$(this).removeClass("icon_list_gray").addClass("icon_list");
					$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
				}
				$("#chartdiv").hide();
				$("#tablediv").show();
			});
			getBrandFormChart();
			getBrandFormHisChart();
			
		});

		//获取品牌构成图
		function getBrandFormChart(){

			var param = 'ciLabelFormModel.labelId='+labelId;
				param += '&ciLabelFormModel.dataDate='+dataDate;
				param += '&ciLabelFormModel.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/ciLabelFormAnalysisAction!findBrandFormChartData.ai2do?'+param);

		    //环形图
		    //alert(chartParam);
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MultiLevelPie.swf"+chartParam, "brandFormChart",$('#brandFormDiv').width(), $('#brandFormDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("brandFormDiv");		   
		}

		//品牌构成历史柱状图
		function getBrandFormHisChart(){
			var param = 'ciLabelFormModel.labelId='+labelId;
				param += '&ciLabelFormModel.dataDate='+dataDate;
				param += '&ciLabelFormModel.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/ciLabelFormAnalysisAction!findBrandTrendChartData.ai2do?'+param);

		    //堆积柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/StackedColumn2D.swf"+chartParam, "",$('#brandFormHisDiv').width(), $('#brandFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("brandFormHisDiv");		   
		}

		function getSubBrandFormChart(brandId) {
			var param = 'ciLabelFormModel.labelId='+labelId;
				param += '&ciLabelFormModel.dataDate='+dataDate;
				param += '&ciLabelFormModel.brandId='+brandId;
				param += '&ciLabelFormModel.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/ciLabelFormAnalysisAction!findSubBrandFormChartData.ai2do?'+param);

		    //柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Column2D.swf"+chartParam, "",$('#brandFormHisDiv').width(), $('#brandFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("brandFormHisDiv");
		}
		
		//品牌历史表格数据
		function getBrandFormHisTable() {

	    	$('#tablediv').load(
	    			$.ctx+'/ci/ciLabelFormAnalysisAction!findBrandHistoryData.ai2do',
	    		{
	    			'ciLabelFormModel.labelId':labelId,'ciLabelFormModel.dataDate':dataDate,
	    			'ciLabelFormModel.updateCycle':updateCycle
	    		},
	    		function() {
		    	}
		   );	
		}

		</script>
		
		<!--品牌构成图 -->
		<div class="aibi_chart_div" id="chartdiv" style="height:280px;width:100%;">
			<table width="100%">
				<tr>
					<td id="brandFormDiv" style="height:280px;width:40%;"></td>
					<td style="height:280px;width:60%;">
						<div id="brandFormHisDiv" style="height:280px;"></div>
					</td>
				</tr>
			</table>
		</div>
		
		<!-- 品牌历史数据表格 -->
		<div class="aibi_table_div" id="tablediv" style="display:none;">
		</div>
