<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="../../include.jsp"%>
<%
	String MAP_SHOW = Configure.getInstance().getProperty("MAP_SHOW"); 
	String NO_RING_PROVINCE = Configure.getInstance().getProperty("NO_RING_PROVINCE");
%>
		<c:set var="DIM_CITY" value="<%=CommonConstants.TABLE_DIM_CITY%>"></c:set>
		<script type="text/javascript" language="javascript">
	    var customGroupId = $('#customGroupId').val();
    	var dataDate = $('#dataDate').val();
		var noRingProvince = '<%=NO_RING_PROVINCE%>';
		var province = '<%=coc_province%>';
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
				}

				$("#tablediv").hide();
				if(noRingProvince.indexOf(province) >= 0) { //地市过多的省份，柱状图显示
					$("#columnChartDiv").show();
					$("#chartdiv").hide();
					getMoreCityFormChart();
				} else { //地市、分公司较少的省份，环形图和堆积图显示
					$("#chartdiv").show();
					$("#columnChartDiv").hide();
					getCityFormHisChart();
					if($('#mapIframe').attr("mapShow")=="true"){
						getMap();
					}else{
						getCityFormChart();
					}
				}
			});
			$("#icon2").click(function(e){
				if ($(this).hasClass("icon_list")) {
					
				} else {
					$(this).removeClass("icon_list_gray").addClass("icon_list");
					$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
				}
				$("#tablediv").show();
				getCityFormHisTable();
				if(noRingProvince.indexOf(province) >= 0) { //地市过多的省份，柱状图显示
					$("#columnChartDiv").hide();
				} else { //地市、分公司较少的省份，环形图和堆积图显示
					$("#chartdiv").hide();
				}
			});

			/* if(noRingProvince.indexOf(province) >= 0) { //地市过多的省份，柱状图显示
				$('#chartdiv').hide();
				$('#columnChartDiv').show();
				getMoreCityFormChart();
			} else { //地市、分公司较少的省份，环形图和堆积图显示
				$('#chartdiv').show();
				$('#columnChartDiv').hide();
				getCityFormHisChart();
				if($('#mapIframe').attr("mapShow")=="true"){
					getMap();
				}else{
					getCityFormChart();
				}
			}

			getCityFormHisTable(); */
			$("#icon1").click();
		});

		function getMap(){
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
			param += '&ciCustomGroupForm.dataDate='+dataDate;
			param += '&ciCustomGroupForm.updateCycle='+updateCycle;
			$('#mapIframe').attr("src","${ctx}/ci/ciMapAction!showCustomerMap.ai2do?"+param);
		}
		//获取品牌构成图
		function getCityFormChart(){

			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersCityFormChartData.ai2do?'+param);

		    //环形图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MultiLevelPie.swf"+chartParam, "",$('#cityFormDiv').width(), $('#cityFormDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("cityFormDiv");		   
		}

		//地域构成历史柱状图
		function getCityFormHisChart(){
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersCityTrendFormChartData.ai2do?'+param);

		    //堆积柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/StackedColumn2D.swf"+chartParam, "",$('#cityFormHisDiv').width(), $('#cityFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("cityFormHisDiv");		   
		}

		function getCityTrendChartByCityId(cityId) {
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
				param += '&ciCustomGroupForm.dataDate='+dataDate;
				param += '&ciCustomGroupForm.cityId='+cityId;
				param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersCityTrendFormChartDataById.ai2do?'+param);

		    //折线图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "",$('#cityFormHisDiv').width(), $('#cityFormHisDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("cityFormHisDiv");
		}
		
		//地域历史表格数据
		function getCityFormHisTable() {
	    	$('#tablediv').load(
	    		$.ctx+'/ci/customersFormAnalysisAction!cityHistoryData.ai2do',
	    		{
	    			'ciCustomGroupForm.customGroupId':customGroupId,'ciCustomGroupForm.dataDate':dataDate,
	    			'ciCustomGroupForm.updateCycle':updateCycle
	    		},
	    		function() {
		    	}
		   );	
		}

		//获取地域(分公司)柱状图，适用于地市较多的省份，如重庆
		function getMoreCityFormChart(){
			$('#returnToColumn').hide();
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
			param += '&ciCustomGroupForm.dataDate='+dataDate;
			param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersMoreCityFormChartData.ai2do?'+param);

		    //柱状图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Column2D.swf"+chartParam, "moreCityFormChart",$('#columnChartDiv').width(), $('#columnChartDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("columnChartDiv");		   
		}

		//获取某个地域的近12个月的客户数趋势图
		function getMoreCityTrendChart(cityId) {
			$('#returnToColumn').show();
			var param = 'ciCustomGroupForm.customGroupId='+customGroupId;
			param += '&ciCustomGroupForm.dataDate='+dataDate;
			param += '&ciCustomGroupForm.cityId='+cityId;
			param += '&ciCustomGroupForm.updateCycle='+updateCycle;

			var chart_url = escape($.ctx+'/ci/customersFormAnalysisAction!customersMoreCityTrendFormChartDataById.ai2do?'+param);

		    //折线图
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "moreCityTrendChart",$('#columnChartDiv').width(), $('#columnChartDiv').height(), "0", "1");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("columnChartDiv");
		}

		function returnToColumn() {
			$('#returnToColumn').hide();
			getMoreCityFormChart();
		}
		
		</script>
		
		<!--品牌构成图 -->
		<div class="aibi_chart_div" id="chartdiv" style="height:280px;">
			<table width="100%">
				<tr>
					<td id="cityFormDiv" style="height:280px;width:40%;">
					    <iframe id="mapIframe" style="" mapShow=<%=MAP_SHOW%> frameborder="0" scrolling="no" width="100%" height="100%"></iframe>
					</td>
					<td style="height:280px;width:60%;">
						<div id="cityFormHisDiv" style="height:280px;"></div>
					</td>
				</tr>
			</table>
		</div>
		
		<!--品牌构成图 不包括环形图和地图，只有柱状图 -->
		<div class="aibi_chart_div" id="columnChartDiv" style="height:280px;width:100%;display:none;">
			
		</div>
		<div id="returnToColumn" style="display:none;margin-left:10px;margin-bottom:15px;">
			 <!--<a href="javascript:;" onclick="getMoreCityFormChart();return false;">返回</a> -->
			<img alt="" style="max-width:20px;max-height:20px;cursor: pointer;" src="${ctx}/aibi_ci/assets/images/returnToColumn.jpg" onclick="getMoreCityFormChart();return false;" title="返回"> 
		</div>
		
		<!-- 品牌历史数据表格 -->
		<div class="aibi_table_div" id="tablediv" style="display:none;">
		</div>