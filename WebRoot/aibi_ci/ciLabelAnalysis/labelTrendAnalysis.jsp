<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>标签趋势分析</title>

<script type="text/javascript">
var tag;//区分用户数还是使用次数
/* function FC_Rendered(DOMId){
	//alert("1 loaded");
	if(window.parent.loadFlag) {
		window.parent.loadLabelForm();		
	}
} */
	$(document).ready(function(){
		//判断是用户数趋势还是使用次数
		//加载标签关联用户趋势分析图
		loadLabelUserNumTrendChart();
		
		//为统计时间赋值

		var countDate = $("#dataDate",parent.document).val();
		if(countDate.length == 6){
			countDate=countDate.substring(0,4)+"-"+countDate.substring(4,6);
		}else if(countDate.length == 8){
			countDate=countDate.substring(0,4)+"-"+countDate.substring(4,6)+"-"+countDate.substring(6,8);
		}
		$('#countDate').html(countDate);

		
		$("#icon1").click(function(e){
			//alert("Click icon1");
			if ($(this).hasClass("icon_chart")) {
				
			} else {
				$(this).removeClass("icon_chart_gray").addClass("icon_chart");
				$("#icon2").removeClass("icon_list").addClass("icon_list_gray");
				$("#table1").hide();
				$("#chart1").show();
				if(tag == "number") {
					loadLabelUserNumTrendChart();
				}
				if(tag == "count") {
					loadLabelUseTimesTrendChart();
				}
			}
		});
		$("#icon2").click(function(e){
			//alert("Click icon2");
			if ($(this).hasClass("icon_list")) {
				
			} else {
				$(this).removeClass("icon_list_gray").addClass("icon_list");
				$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
				$("#chart1").hide();
				$("#table1").show();
				loadLabelTrendTable(tag);
			}
		});
	});
	
	//用户数趋势图
	function loadLabelUserNumTrendChart(){
		tag='number';
		if ($("#icon2").hasClass("icon_list")) {
			loadLabelTrendTable(tag);
			return;	
		}
		//从父页面获取
	    var dataDate = $("#dataDate",parent.document).val();
		var labelId = $("#labelId",parent.document).val();
		//var url=$.ctx+'/ci/ciLabelTrendAnalysisAction!findLabelUserNumTrend.ai2do?labelId='+labelId+'&dataDate='+dataDate;
		var url=escape($.ctx+'/ci/ciLabelTrendAnalysisAction!findLabelUserNumTrendChartData.ai2do?labelId='+labelId+'&dataDate='+dataDate);
		//alert(url);
		var noData = '标签用户数暂无统计数据';
        var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		//alert(url);
		$("#table1").hide();
		$("#chart1").show();
		
		//$('#chart1').load(url);
		var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "labelUserNumTrendChart",$('#chart1').width(), $('#chart1').height(), "0", "1");
   		bar.addParam("wmode","Opaque");
	    bar.setDataURL(url);
	    bar.render("chart1");
	}
	
	//使用次数趋势图
	function loadLabelUseTimesTrendChart(){
		tag='count';
		if ($("#icon2").hasClass("icon_list")) {
			loadLabelTrendTable(tag);
			return;	
		}
		//从父页面获取
	    var dataDate = $("#dataDate",parent.document).val();
		var labelId = $("#labelId",parent.document).val();
		//var url=$.ctx+'/ci/ciLabelTrendAnalysisAction!findLabelUserTimesTrend.ai2do?labelId='+labelId+'&dataDate='+dataDate;
		var url=escape($.ctx+'/ci/ciLabelTrendAnalysisAction!findLabelUserTimesTrendChartData.ai2do?labelId='+labelId+'&dataDate='+dataDate);
		var noData = '标签使用次数暂无统计数据';
        var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		
		$("#table1").hide();
		$("#chart1").show();
		//$('#chart1').load(url);
		
		var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "labelUseTimesTrendChart",$('#chart1').width(), $('#chart1').height(), "0", "1");
   		bar.addParam("wmode","Opaque");
	    bar.setDataURL(url);
	    bar.render("chart1");
	}	
	//加载标签趋势分析表格
	function loadLabelTrendTable(type){
		//从父页面获取
	    var dataDate = $("#dataDate",parent.document).val();
		var labelId = $("#labelId",parent.document).val();
		var url=$.ctx+'/ci/ciLabelTrendAnalysisAction!findLabelTrendTable.ai2do?labelId='+labelId+'&dataDate='+dataDate+'&type='+type;
		//alert(url);
		$("#chart1").hide();
		$("#table1").show();
		$('#table1').load(url);
	}
	
	
</script>
</head>

<body>
<div class="main_con_box">
<!--标题行开始 -->
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>趋势分析</span>统计日期：<label id="countDate"></label></div>
		<div class="tag_box_manage">
			<!--选择框 -->
			<div class="like_button_box" id="select_box" style="width:110px;">
				<div class="like_button_left"></div>
				<div class="like_button_middle" style="width:72px"><span>用户数趋势</span></div>
				<div class="like_button_hr"></div>
				<div class="like_button_middle" style="width:26px"><a href="javascript:void(0);" class="icon_select"></a></div>
				<div class="like_button_right"></div>
				<div class="clear"></div>
			</div>
			
			<!--显示方式 -->
			<div class="like_button_box">
				<div class="like_button_left"></div>
				<div class="like_button_middle"><a href="javascript:void(0);" id="icon1" class="icon_chart"></a></div>
				<div class="like_button_hr"></div>
				<div class="like_button_middle"><a href="javascript:void(0);" id="icon2" class="icon_list_gray"></a></div>
				<div class="like_button_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="clear"></div>
		<!--弹出层-->
		<div class="form-field">
			<ul>
				<li class="on"><a href="javascript:void(0)" onclick="loadLabelUserNumTrendChart();">用户数趋势</a></li>
				<li><a href="javascript:void(0)" onclick="loadLabelUseTimesTrendChart();">使用次数</a></li>
			</ul>
		</div> 
	</div>
	<!--标题行结束 -->
	<div class="aibi_chart_div" id="chart1" style="height:280px;width:100%;">
	</div>
	
	<div class="aibi_table_div" id="table1" style="display:none">
	</div>
</div>
</body>
</html>
