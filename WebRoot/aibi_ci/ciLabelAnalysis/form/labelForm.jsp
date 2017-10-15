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
		<script type="text/javascript" src="<%=request.getContextPath()%>/aibi_chart/chartswf/FusionCharts.js"></script>
		<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
		<script language="javaScript" src="${ctx}/aibi_ci/assets/js/datatable.js"></script>
		<script type="text/javascript" language="javascript">
		var noData = '${noDataToDisplay}';
		var formTypeSign = "Brand";
		var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		$(document).ready(function(){
			chgLabelForm("Brand");
			
		});

		//加载品牌、VIP等级、地域(分公司)构成数据
		function chgLabelForm(formType) {
			formTypeSign = formType;
			$('#labelFormDiv').load($.ctx+"/aibi_ci/ciLabelAnalysis/form/label"+formType+"Form.jsp");
		}


		</script>
	</head>
	<body>
		<input id="labelId" type="hidden" value="${ciLabelFormModel.labelId}"/>
		<input id="dataDate" type="hidden" value="${ciLabelFormModel.dataDate}"/>
		<input id="updateCycle" type="hidden" value="${ciLabelFormModel.updateCycle}"/>
		<div></div>
		<div class="main_con_box">
			<!--标题行开始 -->
			<div class="main_con_top">
				<div class="tag_box_title tag_box_title_gcfx"><span>构成分析</span>统计日期：${dateFmt:dateFormat(ciLabelFormModel.dataDate)}&nbsp;&nbsp;&nbsp;<a href="ciLabelFormAnalysisAction!export.ai2do?ciLabelFormModel.labelId=${ciLabelFormModel.labelId}&ciLabelFormModel.dataDate=${ciLabelFormModel.dataDate}&ciLabelFormModel.updateCycle=${ciLabelFormModel.updateCycle}">下载表格</a></div>	
					<div class="tag_box_manage">
					<!--选择框 -->
					<div class="like_button_box" id="select_box" style="width:110px;">
						<div class="like_button_left"></div>
						<div class="like_button_middle" style="width:72px"><span>品牌</span></div>
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
						<li class="on"><a href="javascript:void(0);" onclick="chgLabelForm('Brand');return false;">品牌</a></li>
						<li><a href="javascript:void(0);" onclick="chgLabelForm('City');return false;">分公司</a></li>
						<li><a href="javascript:void(0);" onclick="chgLabelForm('Vip');return false;">VIP等级</a></li>
					</ul>
				</div> 
			</div>
			<div id="labelFormDiv">
				
			</div>
		</div>
	</body>
</html>