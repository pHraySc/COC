<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>创建客户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<script type="text/javascript">
var	validater;
$(document).ready(function(){
	var id = '${param["id"]}';
	var rule = decodeURIComponent('${param["rule"]}');
	$("#templateId").val(id);
	$("#labelOptRuleShow").val(rule);
	window.parent.$("#createFrame").height(365);
	validater = $("#saveForm").validate({
		rules: {
			"ciCustomGroupInfo.customGroupName": {
				required: true
			},
			"dataDate": {
				required: true
			},
			/* "ciCustomGroupInfo.startDate": {
				required: true
			}, */
			"endDateByMonth": {
				required: true
			}
		},
		messages: {
			"ciCustomGroupInfo.customGroupName": {
				required: "客户群名称不允许为空!"
			},
			"dataDate": {
				required: "统计月份不能为空！"
			},
			/* "ciCustomGroupInfo.startDate": {
				required: "有效期开始时间不能为空！"
			}, */
			"endDateByMonth": {
				required: "有效期结束时间不能为空！"
			}
		},
		errorPlacement:function(error,element) {
			element.parent("div").next("div").hide();
			element.parent("div").next("div").after(error);
		},
		success:function(element){
			element.remove();
		},
		errorElement: "div",
		errorClass: "tishi error"
	});
	
	$("#saveBtn").bind("click", function(){
		submitForm();
	});
	
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入客户群名称"){
		$.focusblur("#custom_name");
	}
	
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入客户群名称"){
		$.focusblur("#custom_name");
	}
	
	var cycle = $("#updateCycle",parent.document).val();
	if(cycle == null || cycle ==""){
		cycle = 1;
	}
	
	$("input[type='radio'][name='ciCustomGroupInfo.updateCycle'][value='"+cycle+"']").attr("checked", true);
	
	if(cycle == 1){
		$("#period").hide();
	}else{
		window.parent.$("#createFrame").height(window.parent.$("#createFrame").height() + 60);
	}

	$("input[type='radio'][name='ciCustomGroupInfo.updateCycle']").change(function(){
		var v = $(this).val();
		if(v == 1){
			if(!$("#period").is(":hidden")){
				window.parent.$("#createFrame").height(window.parent.$("#createFrame").height() - 60);
			}
			$("#period").hide();
		}else{
			if($("#period").is(":hidden")){
				window.parent.$("#createFrame").height(window.parent.$("#createFrame").height() + 60);
			}
			$("#period").show();
		}
		//表格斑马线
		changeTableColor(".showTable");
	});
	//表格斑马线
	changeTableColor(".showTable");
	
	$("#startDateHidden").attr("disabled", "true");
	$("#startDateByMonth").attr("disabled", "true");
	
	var queryTemplateNameUrl = $.ctx + "/ci/templateManageAction!findTemplateById.ai2do";
	//查询模板应用场景
	$.post(queryTemplateNameUrl, {templateId : id}, function(result){
		console.dir(result);
		$("#sceneName").empty();
		$("#sceneName").append(result.sceneName);
		$('#sceneId').val(result.sceneId);
	});
});

jQuery.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});
};
//统计日期选择后的操作
function datePicked(){
	var d = $("#dataDate").val();
	if(d != null && d!= ""){
		var _year = d.substring(0,4);
		var _month = d.substring(5,7);
		$("#startDateHidden").val(d+"-01");
		$("#startDateByMonth").val(d);
		$("#dataDateHidden").val(_year+_month);
	}
}
function changeEndDate(){
	$("#endDateByMonth").val("");
	$("#endDateHidden").val("");
}
//获得指定年月最后一天日期
function getLastDay(year,month){        
	var new_year = year;    //取当前的年份         
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）         
	if(month>12){            //如果当前大于12月，则年份转到下一年         
		new_month -=12;        //月份减         
		new_year++;            //年份增         
	}        
	var new_date = new Date(new_year,new_month,1);                //取当年当月中的第一天         
	return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期         
}
//保存客户群
function submitForm(){
	if(validateForm()){
		var actionUrl = $.ctx + "/ci/templateManageAction!createCustomer.ai2do";
		
		if(!$("#period").is(":hidden")){
			$("#startDateHidden").removeAttr("disabled");
			var ym = $("#endDateByMonth").val();
			var year = ym.substring(0,4);
	        var month = ym.substring(5,7);
		    if(month.length == 2 && month.substring(0,1)=='0'){
		        month = month.substring(1);
		    }
		    var d = getLastDay(year,month);
			$("#endDateHidden").val($("#endDateByMonth").val()+"-"+d);
		}
		
		$.post(actionUrl, $("#saveForm").serialize(), function(result){
			if(result.success){
				var customerName = $("#custom_name").val();
				parent.openSaveSuccess(customerName, "");
				parent.closeDialog("#createDialog");
			}else{
				var cmsg = result.cmsg;
				if(cmsg.indexOf("重名") >= 0){
					$("#cnameTip").empty();
					$("#cnameTip").show().append(cmsg);
				}else{
					parent.showAlert(cmsg,"failed");
				}
			}
		});
	}
}
//表单验证
function validateForm(){
	var cname = $.trim($("#custom_name").val());
	if(cname == "请输入客户群名称"){
		$("#custom_name").val("");
	}else{
		$("#custom_name").val(cname);
	}
	if($("#isAutoMatch").is(":checked")){
		$("#isAutoMatch").val(1);
	}else{
		$("#isAutoMatch").val(0);
	}
	if(!validater.form()){
		return false;
	}
	return true;
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input id="custom_id" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}" />
<input type="hidden" name="ciCustomGroupInfo.createTypeId" value="1"/>
<input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value="${ciCustomGroupInfo.labelOptRuleShow}"/>
<input id="templateId" type="hidden" name="ciCustomGroupInfo.templateId" value="${ciCustomGroupInfo.templateId}"/>
<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th width="110px"><span>*</span>客户群名称：</th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciCustomGroupInfo.customGroupName" type="text" id="custom_name" value="请输入客户群名称" 
						maxlength="35" class="new_tag_input" style="width:302px;*width:306px;cursor:text"/>
				</div>
				<!-- 提示 -->
				<div id="cnameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>
		<tr>
			<th width="110px"><span>*</span>客户群分类：</th>
			<td>
				<input id="sceneId" name="ciCustomGroupInfo.sceneId" type="hidden"/>
				<span id="sceneName"></span>
			</td>
		</tr>
		<tr>
			<th><span>*</span>统计月份：</th>
			<td>
				<div class="new_tag_selectBox">
					<input id="dataDate" type="text" name="dataDate" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({onpicked:datePicked, ychanged:changeEndDate, Mchanged:changeEndDate, dateFmt:'yyyy-MM', maxDate:'${param['month']}', minDate:'#F{$dp.$DV(\'${param['month']}\',{y:-1});}'})"/>
					<input id="dataDateHidden" type="hidden" name="ciCustomGroupInfo.dataDate"  value='' pattern="yyyyMM"/>
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<tr>
			<th><span>*</span>生成周期：</th>
			<td>
				<div class="new_tag_selectBox">
					<input class="valign_middle" type="radio" id="onceCreate" name="ciCustomGroupInfo.updateCycle"  value="1"  />&nbsp;一次性生成&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="valign_middle" type="radio" id="monthCreate" name="ciCustomGroupInfo.updateCycle"  value="2" />&nbsp;&nbsp;按月生成
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<tr id="period">
			<th><span>*</span>客户群有效期：</th>
			<td>
				<div class="new_tag_selectBox">
				<%-- 
					<input name="ciCustomGroupInfo.startDate" type="text" id="startDate" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
					<span style="float:left;padding-left:3px;padding-right:3px;">--</span>
					<input name="ciCustomGroupInfo.endDate" type="text" id="endDate" value="${ciCustomGroupInfo.endDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\',{M:1})}'})"/>
				 --%>
					<input name="startDateByMonth" type="text" id="startDateByMonth" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({dateFmt:'yyyy-MM', maxDate:'#F{$dp.$D(\'endDateByMonth\')}'})"/>
					<input name="ciCustomGroupInfo.startDate" type="hidden" id="startDateHidden" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>'/>
					<span style="float:left;padding-left:3px;padding-right:3px;">--</span>
					<input name="endDateByMonth" type="text" id="endDateByMonth" value='' readonly class="new_tag_input tag_date_input" onclick="WdatePicker({dateFmt:'yyyy-MM', minDate:'#F{$dp.$D(\'startDateByMonth\',{M:1})}'})"/>
					<input name="ciCustomGroupInfo.endDate" type="hidden" id="endDateHidden" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>'/>
				
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
		<tr>
			<th>客户群描述：</th>
			<td>
				<div>
					<textarea id="custom_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea"></textarea>
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
    <%
       if("true".equals(marketingMenu)){
    %>
	<input id="isAutoMatch" type="checkbox" class="valign_middle" name="ciCustomGroupInfo.productAutoMacthFlag" value="" /> 系统自动匹配产品&nbsp;&nbsp;
	<%} %>
	<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
</div>

</form>
</body>
</html>

