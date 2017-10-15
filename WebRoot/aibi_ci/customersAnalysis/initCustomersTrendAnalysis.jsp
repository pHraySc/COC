<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>客户群趋势分析</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
/* function FC_Rendered(DOMId){
	if(window.parent.loadFlag) {
		window.parent.loadCustomGroupForm();
	}
} */
    $(document).ready(function(){

		//加载标签关联用户趋势分析图
        loadCustomersTrendChart();
		
		//为统计时间赋值
		var countDate = $("#dataDate",parent.document).val();		
		if(countDate.length == 6){
			countDate=countDate.substring(0,4)+"-"+countDate.substring(4,6);
		}else if(countDate.length == 8){
			countDate=countDate.substring(0,4)+"-"+countDate.substring(4,6)+"-"+countDate.substring(6,8);
		}
		$('#countDate').html(countDate);	
		$("#icon1").click(function(e){
			if ($(this).hasClass("icon_chart")) {
				
			} else {
				$(this).removeClass("icon_chart_gray").addClass("icon_chart");
				$("#icon2").removeClass("icon_list").addClass("icon_list_gray");
				$("#table1").hide();
				$("#chart1").show();
			}
		});
		$("#icon2").click(function(e){
			if ($(this).hasClass("icon_list")) {
				
			} else {
				$(this).removeClass("icon_list_gray").addClass("icon_list");
				$("#icon1").removeClass("icon_chart").addClass("icon_chart_gray");
				$("#chart1").hide();
				$("#table1").show();
			}
		});
	});
	//加载客户群趋势分析图
	function loadCustomersTrendChart(){
		var customGroupId = '${customGroupInfo.customGroupId}';
    	var dataDate = $("#dataDate",parent.document).val();
		var url=escape($.ctx+'/ci/customersTrendAnalysisAction!customersTrendAnalysis.ai2do?customGroupInfo.customGroupId='+customGroupId+'&dataDate='+dataDate);
		var noData = '客户群用户数暂无统计数据';
                var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		$("#table1").hide();
		$("#chart1").show();
		var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "customersTrendChart",$('#chart1').width(), $('#chart1').height(), "0", "1");
   		bar.addParam("wmode","Opaque");
	    bar.setDataURL(url);
	    bar.render("chart1");
	}
	//加载客户群趋势分析表格
	function loadCustomersTrendChartTable(){
		var customGroupId = '${customGroupInfo.customGroupId}';
    	var dataDate = $("#dataDate",parent.document).val();
		var url=$.ctx+'/ci/customersTrendAnalysisAction!customersTrendAnalysisTable.ai2do?customGroupInfo.customGroupId='+customGroupId+'&dataDate='+dataDate;
		$.ajax({
            url:url,
            type:"POST",
            dataType: "html",
            data: "",
            success:function(html){
                $("#customersTrendTable").html(html);
                pagetable();
            }
        })
        $("#chart1").hide();
		$("#table1").show();
	}
	function pagetable(){
		var customGroupId = '${customGroupInfo.customGroupId}';
    	var dataDate = $("#dataDate",parent.document).val();
	    var actionUrl = $.ctx+'/ci/customersTrendAnalysisAction!customersTrendAnalysisTable.ai2do?customGroupInfo.customGroupId='+customGroupId+'&dataDate='+dataDate+'&'+$("#pageForm").serialize()+'';
	    $("#customersTrendTable").page( {url:actionUrl,objId:'customersTrendTable',callback:pagetable});
	    $(".commonTable mainTable").datatable();
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
            
			<div class="like_button_box" id="select_box" style="width:110px;display:none">
				<div class="like_button_left"></div>
                
				<div class="like_button_middle" style="width:72px"><span>用户数趋势</span></div>
                
				<div class="like_button_hr"></div>
				<div class="like_button_middle" style="width:26px"><a href="javascript:void(0);" class="icon_select"></a></div>
				<div class="like_button_right"></div>
				<div class="clear"></div>
			</div>
            
			
			<!--显示方式 -->
			<div class="like_button_box" style="float: right;">
				<div class="like_button_left"></div>
				<div class="like_button_middle"><a href="javascript:loadCustomersTrendChart();" id="icon1" class="icon_chart"></a></div>
				<div class="like_button_hr"></div>
				<div class="like_button_middle"><a href="javascript:loadCustomersTrendChartTable();" id="icon2" class="icon_list_gray"></a></div>
				<div class="like_button_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="clear"></div>
		<!--弹出层
		<div class="form-field">
			<ul>
				<li class="on"><a href="javascript:loadCustomersTrendChart();">用户数趋势</a></li>
				<li><a href="javascript:loadCustomersTrendChartTable();">使用次数</a></li>
			</ul>
		</div> 
	--></div>
	<!--标题行结束 -->
	<div class="aibi_chart_div" id="chart1" style="height:280px">
	</div>
	<div class="aibi_table_div" id="table1" style="display:none">
		 <jsp:include page="/aibi_ci/customersAnalysis/customersTrendAnalysisTable.jsp" />
	</div>
</div>
</body>
</html>