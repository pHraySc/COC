<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>客户群查看</title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<style>
	.dialog_table{padding-top:4px;}
	.dialog_table .showTable th,.dialog_table .showTable td{padding-top:4px;padding-bottom:4px;line-height:24px;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	//表格斑马线
	aibiTableChangeColor(".showTable");
	var busiId = '${param["customerId"]}';
	if(busiId!=null && busiId!=""){
		var actionUrl = $.ctx + "/ci/customersManagerAction!findCustomer.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data:{"ciCustomGroupInfo.customGroupId":busiId},
			success: function(result){
			    if(result.customGroupInfo!=null){
			    	//设置客户群详情
					if(result.customGroupInfo.customGroupName!=null)
						$("#customerName").text(result.customGroupInfo.customGroupName);
					if(result.customGroupInfo.labelOptRuleShow!=null)
						var kpiDiffRule = "";
						if (result.customGroupInfo.kpiDiffRule != null && result.customGroupInfo.kpiDiffRule != "") {
							kpiDiffRule = result.customGroupInfo.kpiDiffRule;
						}
						$("#creationRule").html("<p class='strLenLimit' style='width:600px;' title='" + result.customGroupInfo.labelOptRuleShow + kpiDiffRule + "'>" + result.customGroupInfo.labelOptRuleShow + kpiDiffRule + "</p>");
					if(result.customGroupInfo.updateCycle==1)
						$("#creationCycle").text("一次性");
					else if(result.customGroupInfo.updateCycle==2)
						$("#creationCycle").text("月周期");
					else if(result.customGroupInfo.updateCycle==3)
						$("#creationCycle").text("日周期");
					if(result.customGroupInfo.customNum!=null)
						$("#customerNum").text(result.customGroupInfo.customNum);
					if(result.customGroupInfo.dataTime!=null)
						$("#dataTime").text(result.customGroupInfo.dataTime);
					if(result.customGroupInfo.createTime!=null)
						$("#creationTime").text(result.customGroupInfo.createTime);
					var createTypeList = result.customCreateType;
					jQuery.each(createTypeList, function(i,item){
						if(item.createTypeId==result.customGroupInfo.createTypeId)
							$("#creationType").text(item.createTypeName);
					})
					if(result.customGroupInfo.createUserName!=null)
						$("#creationPerson").text(result.customGroupInfo.createUserName);
					if(result.customGroupInfo.dataDate!=null && result.customGroupInfo.updateCycle==2){
						var date=result.customGroupInfo.dataDate;
					    var _year = date.substring(0,4);
					    var _month = date.substring(4,6);
						$("#statisticsTime").text(_year+"-"+_month);
					}else if(result.customGroupInfo.dataDate!=null && result.customGroupInfo.updateCycle==3){
						var date=result.customGroupInfo.dataDate;
					    var _year = date.substring(0,4);
					    var _month = date.substring(4,6);
					    var _day = date.substring(6,8);
						$("#statisticsTime").text(_year+"-"+_month+"-"+_day);
					}
						
					if(result.customGroupInfo.isAlarm)
						$("#alarmStatus").text("预警");
					else
						$("#alarmStatus").text("正常");
					if(result.customGroupInfo.customGroupDesc!=null)
						$("#note").text(result.customGroupInfo.customGroupDesc);
				}
			}
		})
	}
})
</script>
<div id="customerDetailDiv" class="dialog_table"  style="overflow: auto;">
		<table width="100%" class="showTable" cellpadding="0" cellspacing="0" style="overflow: scroll">
			<tr>
				<th width="110px">客户群名称：</th>
				<td id="customerName"></td>
			</tr>
			<tr>
				<th width="110px">生成规则：</th>
				<td id="creationRule" class="strLenLimit" style="width:500px"></td>
			</tr>
			<tr>
				<th width="110px">生成周期：</th>
				<td id="creationCycle"></td>
			</tr>
			<tr>
				<th width="110px">用户数：</th>
				<td id="customerNum"></td>
			</tr>
			<tr>
				<th width="110px">数据生成日期：</th>
				<td id="dataTime"></td>
			</tr>
			<tr>
				<th width="110px">创建日期	：</th>
				<td id="creationTime"></td>
			</tr>
			<tr>
				<th width="110px">创建方式	：</th>
				<td id="creationType"></td>
			</tr>
			<tr>
				<th width="110px">创建人：</th>
				<td id="creationPerson"></td>
			</tr>
			<tr>
				<th width="110px">统计时间：</th>
				<td id="statisticsTime"></td>
			</tr>
			<tr>
				<th width="110px">预警状态：</th>
				<td id="alarmStatus"></td>
			</tr>
			<tr>
				<th width="110px">客户群描述：</th>
				<td id="note"></td>
			</tr>	
		</table>
	</div>
	<div class="dialog_btn_div">
		<input name="" type="button" value=" 确 定 " onclick="parent.closeShowLabelDialog(); " class="tag_btn"/>
	</div>
</html>