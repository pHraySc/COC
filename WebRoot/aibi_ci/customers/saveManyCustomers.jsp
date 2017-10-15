<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>创建客户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<style>
	.secenId {width:310px;}
</style>
</head>
<body>
<form id="saveForm" method="post">
<input type="hidden" name="ciCustomGroupInfo.productAutoMacthFlag" value="0"/>
<!-- // other page send -->
<%-- <input type="hidden" name="ciCustomGroupInfo.dataDate" value="${ciCustomGroupInfo.dataDate}"/> --%>
<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		
		<c:forEach var="ciCustomGroupInfoV" items="${ciCustomGroupInfoList }" varStatus="st">
		<tr>
			<th width="110px"><span>*</span>客户群名称：</th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciCustomGroupInfoList[${st.index}].customGroupName" type="text" id="ciCustomGroupInfoList[${st.index}].customGroupName" value="${ciCustomGroupInfoV.customGroupName }" 
						maxlength="35" class="new_tag_input" style="width:302px;*width:306px;cursor:text" onblur="validateCustomerName(this)"  onkeyup="validateCustomerName(this)"/>
					<c:if test="${ciCustomGroupInfo.updateCycle != 1 }">
						&nbsp;<input id="ciCustomGroupInfoList[${st.index}].saveTemplate" type="checkbox" name="ciCustomGroupInfoList[${st.index}].saveTemplate" onclick="showOrHide(this)" value="true" class="valign_middle"/> 同时保存模板
					</c:if>
					<input id="ciCustomGroupInfoList[${st.index}].labelOptRuleShow" type="hidden" name="ciCustomGroupInfoList[${st.index}].labelOptRuleShow" value="${ciCustomGroupInfoV.labelOptRuleShow }" />
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none"></div><!-- 用于重名验证 -->
			</td>
		</tr>
		
		<tr style="display:none">
			<th width="110px"><span>*</span>模板名称：</th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciTemplateInfoList[${st.index}].templateName" type="text" id="ciTemplateInfoList[${st.index}].templateName" value="${ciCustomGroupInfoV.customGroupName }-客户群模板" 
						maxlength="40" class="new_tag_input" style="width:302px;*width:306px;cursor:text" onblur="validateTemplateName(this)"  onkeyup="validateTemplateName(this)" />
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none"></div><!-- 用于重名验证 -->
			</td>
		</tr>
		<tr>
			<th width="110px"><span>*</span>客户群分类：</th>
			<td>
				<div class="new_tag_selectBox">
					<base:DCselect selectId="secenId${st.index}" selectClass="secenId" selectName="ciCustomGroupInfoList[${st.index}].sceneId" 
						tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
			        	nullName="请选择" nullValue="" selectValue="ciCustomGroupInfoList.sceneId"/>
			    </div> 
				<!-- 提示 -->
				<div id="snameTip${st.index}" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
			</td>
		</tr>
		</c:forEach>
		<tr <c:if test="${ciCustomGroupInfo.updateCycle == 1 }">style="display:none"</c:if>>
			<th><span>*</span>生成周期：</th>
			<td>
				<div class="new_tag_selectBox">
				  <span id="selectCycle">
					<label><input class="valign_middle" type="radio" checked="checked" id="onceCreate" name="ciCustomGroupInfo.updateCycle"  value="1"  />&nbsp;一次性生成&nbsp;&nbsp;&nbsp;&nbsp;</label>
					<label><input class="valign_middle" type="radio" id="monthCreate" name="ciCustomGroupInfo.updateCycle"  value="2" />&nbsp;按月生成</label>
				  </span>
				</div>
			</td>
		</tr>
		
		<tr id="period" <c:if test="${ciCustomGroupInfo.updateCycle == 1 }">style="display:none"</c:if>>
			<th><span>*</span>客户群有效期：</th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciCustomGroupInfo.startDateStr" type="text" id="startDate" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'${minDate}'})"/>
					<span style="float:left;padding-left:3px;padding-right:3px;">--</span>
					<input name="ciCustomGroupInfo.endDateStr" type="text" id="endDate" value="${ciCustomGroupInfo.endDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'${maxDate}'})"/>
				</div>
				<!-- 提示 -->
			</td>
		</tr>
		<tr>
			<th>客户群描述：</th>
			<td>
				<div>
					<textarea id="custom_desc" style="overflow:hidden;width:400px"  onblur="shortThis(this);" onkeyup="shortThis(this);" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea">${ciCustomGroupInfo.customGroupDesc}</textarea>
				</div>
				<!-- 提示 -->
			</td>
		</tr>
	</table>
</div>
<div id="listForm" style="display:none"></div>
<script type="text/javascript">
var	validater;
$(document).ready(function(){
	var snames = $('.secenId');
	for(var i=0; i<snames.length; i++) {
		$('#secenId' + i).multiselect({
	        multiple	: false,
	        header		: false,
	        height		: '130px',
	        selectedList: 1
	    });
	}
	
	
	validater = $("#saveForm").validate({
		rules: {
			<c:forEach var="ciCustomGroupInfo" items="${ciCustomGroupInfoList }" varStatus="st">
			"ciCustomGroupInfoList[${st.index}].customGroupName": {
				required: true
				,remote:{//验证用户名是否存在
					type:"POST",
					url:$.ctx + "/ci/customersManagerAction!isNameExist.ai2do", //
					data:{
						"ciCustomGroupInfo.customGroupName":function(){return $("#saveForm").find('input[name="ciCustomGroupInfoList[${st.index}].customGroupName"]').val();}
					}
				}
			},
			</c:forEach>
			"ciCustomGroupInfo.startDateStr": {
				required: true
			},
			"ciCustomGroupInfo.endDateStr": {
				required: true
			}
		},
		messages: {
			<c:forEach var="ciCustomGroupInfo" items="${ciCustomGroupInfoList}" varStatus="st">
			"ciCustomGroupInfoList[${st.index}].customGroupName": {
				required: "客户群名称不允许为空!",
				maxlength: $.validator.format("长度不能大于 {0} "),
				remote:jQuery.format("客户群名称已经存在")
			},
			</c:forEach>
			"ciCustomGroupInfo.startDateStr": {
				required: "有效期开始时间不能为空！"
			},
			"ciCustomGroupInfo.endDateStr": {
				required: "有效期结束时间不能为空！"
			}
		},
		errorPlacement:function(error,element) {
			element.parent().parent().append(error);
		},
		success:function(element){
			element.parent().parent().find("div[class='tishi error']").remove();
		},
		errorElement: "div",
		errorClass: "tishi error"
	});
	
	$("#saveBtn").bind("click", function(){
		submitList();
	});
	$("#saveForm").valid();
	
	var cycle = "${ciCustomGroupInfo.updateCycle}";
	if(cycle == null || cycle ==""){
		cycle = 1;
	}
	
	if(cycle == 1){
		$("#period").hide();
	}

	$("#selectCycle").find("input[name='ciCustomGroupInfo.updateCycle']").change(function(){
		var v = $(this).val();
		if(v == 1){
			$("#period").hide();
		}else{
			if(v == 3){
				$("input[name='ciCustomGroupInfo.startDateStr']").replaceWith('<input name="ciCustomGroupInfo.startDateStr" type="text" id="startDate" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({maxDate:\'#F{$dp.$D(\\\'endDate\\\')}\',minDate:\'${ciCustomGroupInfo.startDateStr}\',dateFmt:\'yyyy-MM-dd\'})"/>');
					$("input[name='ciCustomGroupInfo.endDateStr']").replaceWith('<input name="ciCustomGroupInfo.endDateStr" type="text" id="endDate" value="${ciCustomGroupInfo.endDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({minDate:\'#F{$dp.$D(\\\'startDate\\\')}\',maxDate:\'${ciCustomGroupInfo.endDateStr}\',dateFmt:\'yyyy-MM-dd\'})"/>');
			}else if(v==2){
				$("input[name='ciCustomGroupInfo.startDateStr']").replaceWith('<input name="ciCustomGroupInfo.startDateStr" type="text" id="startDate" value="${ciCustomGroupInfo.startDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({maxDate:\'#F{$dp.$D(\\\'endDate\\\')}\',minDate:\'${ciCustomGroupInfo.startDateStr}\',dateFmt:\'yyyy-MM\'})"/>');
					$("input[name='ciCustomGroupInfo.endDateStr']").replaceWith('<input name="ciCustomGroupInfo.endDateStr" type="text" id="endDate" value="${ciCustomGroupInfo.endDate}" readonly class="new_tag_input tag_date_input" onclick="WdatePicker({minDate:\'#F{$dp.$D(\\\'startDate\\\')}\',maxDate:\'${ciCustomGroupInfo.endDateStr}\',dateFmt:\'yyyy-MM\'})"/>');
			}
			$("#period").show();
		}
		//表格斑马线
		changeTableColor(".showTable");
	});
	//表格斑马线
	changeTableColor(".showTable");
});
function shortThis(obj){
	var oldValue = $(obj).val();
	if(oldValue.length > 170 ){
		oldValue = oldValue.substr(0,170);
		$(obj).val(oldValue);
	}
}
function showOrHide(obj){
	$(obj).parent().parent().parent().next().toggle(
		function(){
			//表格斑马线
			changeTableColor(".showTable");
		}
	);
}

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

function submitList(){
	if(validateForm(false)){
		var actionUrl = $.ctx;
		actionUrl += "/ci/customersManagerAction!saveCustomerByCustomerCalc.ai2do";
		$.post(actionUrl, $("#saveForm").serialize()+"&"+parent.getFullParam(), function(result){
			if(result.success){
				parent.openSuccess(result.names||"",result.templateNames||"");
				parent.closeCreate();
			}else{
				parent.showAlert(result.msg,"failed");
			}
		});
	}
}

function validateForm(initPage){
	//alert("start validateForm");
	var flag = validater.form();
	//alert("step 1 :"+flag);
	if(!flag){
		//validater.focusInvalid();
	}
	//验证表单里是否有同名的客户群
	$("#saveForm").find("input[name$='.customGroupName']").each(function(){
		var tempFlag = validateCustomerName(this);
		if(!tempFlag){
			flag = tempFlag;//验证不通过
		}
	});
	
	//alert("step 2 :"+flag);
	//验证表单里是否有同名的模板
	$("#saveForm").find("input:checked").each(function(){
		var tNameObj = $(this).parent().parent().parent().next("tr").find("input[name$='.templateName']");
		if(tNameObj.size() > 0 ){
			var tempFlag = validateTemplateName(tNameObj[0]);
			if(!tempFlag){
				flag = tempFlag;//验证不通过
			}
		}
	});
	
	if(!initPage) {
		var flag = true;
		var snames = $('.secenId');
		for(var i=0; i<snames.length; i++) {
			var sname = $(snames[i]).val();
			if($.trim(sname) == "") {
				$("#snameTip" + i).empty();
				$("#snameTip" + i).show().append("请选择客户群分类");
				flag = false;
			}
		}
		return flag;
	}
	
	//alert("step 3 :"+flag);
	return flag;
}
function validateCustomerName(obj){
	var flag = true;
	var msg = "";
	$("#saveForm").find("input[name$='.customGroupName']").not("input[name='"+obj.name+"']").each(function(){
		if($.trim(this.value) == $.trim(obj.value)){
			flag = false;
			msg = "表单中有相同的客户群名称";
		}
	});
	if(!flag){
		var error ="<div class='tishi error' >" +msg +"</div>";
		$(obj).parent().parent().append(error);
	}else{
		$(obj).parent().parent().find("div[class='tishi error']").remove();
		$(obj).valid();
	}
	return flag;
}
function validateTemplateName(obj){
	var existsNames = "";
	var flag = true;
	var msg = "";
	$("#saveForm").find("input:checked").each(function(){
		var tNameObj = $(this).parent().parent().parent().next("tr").find("input[name$='.templateName']").not("input[name='"+obj.name+"']");
		var tName = tNameObj.val();
		existsNames += ",'"+$.trim(tName)+"'";
	});
	if(existsNames.indexOf("'"+$.trim(obj.name)+"',") != -1 ){
		msg = "表单中有相同的模板名称";
		flag = false;
	}
	if(flag){
		var html = $.ajax({
				type: "POST",
				url: $.ctx + "/ci/customersManagerAction!isTemplateNameExist.ai2do",
				data: encodeURI("ciTemplateInfo.templateName="+obj.value),
				async: false
			 }).responseText;
		if(!eval(html)){
			msg = "模板名称重复";
			flag = false;
		}
	}
	if(!flag){
		var error ="<div class='tishi error' >" +msg +"</div>";
		$(obj).parent().parent().append(error);
	}else{
		$(obj).parent().parent().find("div[class='tishi error']").remove();
	}
	return flag;
}
function setProductAutoMacthFlag(v){
	$("input[name='ciCustomGroupInfo.productAutoMacthFlag']").val(v);
}
$(function(){
	validateForm(true);//初始化页面校验一下表单，因为都已经填好了
});
</script>
</form>
</body>
</html>

