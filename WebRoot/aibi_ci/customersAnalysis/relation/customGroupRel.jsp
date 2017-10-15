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
		$(document).ready(function(){
			var noCustomRel = ${noCustomRel};
			if(noCustomRel != 0) {
				getLabelRelChart();
			}
		});

		//获取关联分析结果图
		function getLabelRelChart(){
			var customGroupId = '${ciCustomRelModel.customGroupId}';
	    	var dataDate = '${ciCustomRelModel.dataDate}';
			var param = 'ciCustomRelModel.customGroupId='+customGroupId;
			param += '&ciCustomRelModel.dataDate='+dataDate;
			var chart_url = escape($.ctx+'/ci/customersRelAnalysisAction!customRelChar.ai2do?'+param);
		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/DragNode.swf"+chartParam, "",$('#labelRelDiv').width(), $('#labelRelDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("labelRelDiv");		   
		}
		//关联分析连接
		function openCustomRel() {
			if($('#customNum').val() == ""){
				window.parent.showAlert("客户群清单未创建成功","failed");
				return;
			}
			var customGroupId = $("#customGroupId",parent.document).val();
			var listTableName = $("#listTableName",parent.document).val();
			var customNum = $("#customNum").val();
			var dataDate = $("#dataDate").val();
			var param = "customGroup.customGroupId="+customGroupId;
			    param = param + "&customGroup.listTableName="+listTableName;
			    param = param + "&customGroup.customNum="+customNum;
				param = param + "&dataDate="+dataDate;
				param = param + "&tab=rel";
			var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
			window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		}
		</script>
	</head>
	<body>
		<input id=customGroupId type="hidden" value="${ciCustomRelModel.customGroupId}"/>
		<input id="dataDate" type="hidden" value="${ciCustomRelModel.dataDate}"/>
		<div></div>
		<div class="main_con_box">
			<!--标题行开始 -->
			<div class="main_con_top">
				<div class="tag_box_title tag_box_title_glfx"><span>关联分析</span>统计日期：${dateFmt:dateFormat(ciCustomRelModel.dataDate)}&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="openCustomRel()">配置关联图谱</a></div>
				<div class="tag_box_manage">
					<!--选择框 -->
					<div class="like_button_box" id="select_box" style="width:110px;float: right;">
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
				<div class="form-field" style="right:15px;_right:14px">
					<ul>
						<li class="on"><a href="javascript:void(0);" >关联分析</a></li>
						<li><a href="javascript:void(0);" onClick="window.parent.loadCustomersContrast();">对比分析</a></li>
					</ul>
				</div> 
			</div>
			<div id="labelRelDiv" style="height:600px;">
				<c:if test="${noCustomRel == 0}">
                    <!--
					<div class="wushuju" style="margin-top: 250px;text-align: center;font-size: 18px;" >
						还没有设置关联关系
					</div>
                    -->
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