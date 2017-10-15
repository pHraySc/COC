<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>客户群趋势分析明细</title>

<script type="text/javascript">
$(function(){
	var _isSuccess = '${resultMap["success"]}';
	var _msg = '${resultMap["msg"]}';
	if(_isSuccess == 'false'){
		parent.showAlert(_msg,"failed",parent.closeDialog,"#dialog_look");
		parent.multiCustomSearch();
	}
	aibiTableChangeColor(".mainTable");
	$(".showTable .mainTable tr:last td").css("border-bottom","1px solid #c2bdbd");
	//客户群趋势分析图加载
	loadCustomerChart();
	//客户趋势分析表格加载
	loadCustomerTable();
});
//客户趋势分析表格加载方法
function loadCustomerTable(){
	$("#loading").fadeIn();
	var customGroupId = $('#customGroupId').val();
	var dataDate = $('#dataDate').val();
	var actionUrl=$.ctx+'/ci/customersTrendAnalysisAction!findCustomerTrendTable.ai2do?customGroupId='+customGroupId+'&dataDate='+dataDate;
	//alert(actionUrl);
	$('#aibiTableDiv').load(actionUrl,null,function(){
		$("#loading").fadeOut();
	});
}

//客户群趋势分析图加载方法
var noData = '客户群暂无统计数据';
var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
function loadCustomerChart(){
	
	var customGroupId = $('#customGroupId').val();
	var dataDate = $('#dataDate').val();
	//var customGroupId = 'abc';
	//var dataDate = '201305';
	var param='customGroupId='+customGroupId+'&dataDate='+dataDate;
	var chart_url = escape($.ctx+'/ci/customersTrendAnalysisAction!customersTrendChartData2.ai2do?'+param);
	
	var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "",$('#aibiChartDiv').width(), $('#aibiChartDiv').height(), "0", "0");
    bar.addParam("wmode","Opaque");
	bar.setDataURL(chart_url);
	bar.render("aibiChartDiv");
}
function pushCustomer(customGroupId,dataDate,sysId){
	<% if(coc_province.equals("zhejiang")){ %>
	if(sysId.split('_')[1] === 'MARKET'){
        if(!checkUserExistOnYXGLPlatform(customGroupId)){
            parent.showAlert("推送失败<br/>营销管理平台不存在该客户群创建者的账号","failed");
            return;
        }
    }
	<% } %>
	if(!customGroupId||!dataDate){
		return;
	}
	var url = "${ctx}/ci/customersManagerAction!publishCustomer.ai2do";
	var data = "ciCustomListInfo.customGroupId="+encodeURI(customGroupId)+
		"&ciCustomListInfo.dataDate="+dataDate+
		"&ciCustomPushReq.sysId="+sysId;
	//alert(data);
	$.ajax({
		url: url,
		type:"get",
		data:data,
		success: function(result){
			if(result.success){
				parent.showAlert(result.msg,"success");
				$("#console").append("发布：" +result.msg+"<br/>");
			}else{
				parent.showAlert(result.msg,"failed");
				$("#console").append("发布：" +result.msg+"<br/>");
			}
		}
	});
}
function addToPanel(obj,dataDate){
	parent.addToPanel(null,dataDate,'${customGroupInfo.json}');
}

//浙江验证营销管理是否有用户
function checkUserExistOnYXGLPlatform(customGroupId){ 
    var exist = false;
    $.ajax({
        url : "${ctx}/ci/zjDownloadApproveAction!checkUserExist.ai2do",
        type: 'post',
        data: {customGroupId: customGroupId},
        dataType: 'json',
        async: false,
        success: function(result){
            exist = result.success;
        }
    });
    return exist;
}
</script>
</head>

<body>

<div class="dialog_table dialog_full_table">
	<div class="customerTop" style="background:#ecf4f9 url(${ctx}/aibi_ci/assets/themes/default/images/cu_top_bg.gif) repeat-x bottom; border-bottom:none;padding-bottom:10px;">
            <input type="hidden" id="customGroupId" value="${customGroupInfo.customGroupId}" />
            <input type="hidden" id="dataDate" value="${dataDate}" />
		<dl>
			<dt style="text-align:left;">${customGroupInfo.customGroupName}</dt>
			<dd style="cursor: default;text-align:left;" title="客户群规则 : ${customGroupInfo.prodOptRuleShow}${customGroupInfo.labelOptRuleShow}${customGroupInfo.customOptRuleShow}${customGroupInfo.kpiDiffRule}">${customGroupInfo.prodOptRuleShow}
							${customGroupInfo.labelOptRuleShow}${customGroupInfo.customOptRuleShow}
							${customGroupInfo.kpiDiffRule}
			</dd>
		</dl>
	</div>
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="v_top" width="50%">
                <!-- 客户群趋势图展示区域 -->
				<div id='aibiChartDiv' class="aibi_chart_div" style="width:100%;height:290px;border:#ccc solid 1px; background:#fff">
				</div>
			</td>
			<td class="v_top" width="50%" style="padding-left:0px">
				<!--  <div class="aibi_button_title" style="border-left:#ccc solid 1px;border-right:#ccc solid 1px;width:auto;">-->
<!-- 					<ul> -->
<%-- 						<li style="background:none"><a onclick="parent.addToPanel(this);" data="${customGroupInfo.json}" class="icon_button_oper"><span>运算</span></a></li> --%>
<!-- 					</ul> -->
				<!--  </div>-->
                <!-- 客户群趋势列表展示区域 -->
				<div id='aibiTableDiv' class="aibi_table_div" >
					<div class="loading" id="loading">正在加载，请稍候 ...</div>
				</div>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
