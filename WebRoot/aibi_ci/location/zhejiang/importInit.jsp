<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入客户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<style>
	.ui-tooltip {
		padding: 5px 5px;
		background:#f1f2f7; color:#333;
		border:1px solid #bbb;
		font-size:12px; font-weight:normal;
	}
	.ui-tooltip ul{list-style:none; padding:0; margin:0;}
	.ui-tooltip ul li{height:16px; line-height:16px;}
	#secenId {width:310px;}
</style>
</head>
<body>
<form id="saveForm" method="post" action="<%=request.getContextPath() %>/ci/customersManagerAction!createCustomByImportTable.ai2do" enctype="multipart/form-data">
				<div class="dialog_table">
					<table width="100%" class="showTable  new_tag_table" cellpadding="0" cellspacing="0">
						<tr>
							<th width="110px"><span>*</span>客户群名称：</th>
							<td>
								<div class="new_tag_selectBox">
									<input name="ciCustomGroupInfo.customGroupName"  onkeyup="$('#cnameTip').hide();" id="customGroupName" type="text" 
											maxlength="35" class="new_tag_input" style="width:302px;*width:306px;cursor:text"/>
								</div>
								<!-- 提示 -->
								<div id="cnameTip" class="tishi error"  style="display:none;"></div><!-- 用于重名验证 -->
							</td>
							<td></td>
						</tr>
						<tr>
							<th width="110px"><span>*</span>客户群分类：</th>
							<td>
								<div class="new_tag_selectBox">
									<base:DCselect selectId="sceneId" selectName="ciCustomGroupInfo.sceneId"
					                  tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
					                   nullName="请选择" nullValue="" selectValue="${ciCustomGroupInfo.sceneId}"/>
								</div>
								<!-- 提示 -->
								<div id="sceneTip" class="tishi error"  style="display:none;"></div><!-- 用于重名验证 -->
							</td>
							<td></td>
						</tr>
						<tr>
							<th>客户群规则描述：</th>
							<td>
								<div>
									<textarea id="custom_desc"  onkeyup="$('#descTip').hide();"  name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea"></textarea>
								</div>
								<!-- 提示 -->
								<div id="descTip" class="tishi error"  style="display:none;"></div>
							</td>
						</tr>
						<tr>
							<th>失效时间：</th>
							<td>
								<div>
									<input id="customEndDate"  
									  readonly type="text" class="aibi_input aibi_date"  
									  onclick="WdatePicker({minDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" 
						 			  name="ciCustomGroupInfo.endDate">
								</div>
							</td>
						</tr>
						<tr>
							<th>更新周期：</th>
							<td>
								<div  class="new_tag_selectBox">
									<select id="customUpdateCycle" onchange="$('#cycleTip').hide();"  name="ciCustomGroupInfo.updateCycle">
										<option value="">请选择</option>
										<option value="1">日</option>
										<option value="2">月</option>
										<option value="3">其他</option>
									</select>
								</div>
								<!-- 提示 -->
								<div id="cycleTip" class="tishi error"  style="display:none;"></div>
							</td>
						</tr>
						<tr>
							<th>数据库表名：</th>
							<td>
								<div>
									<input style="width:350px"  onkeyup="$('#tabelTip').hide();"  name="ciCustomGroupInfo.tabelName" id="customTabelName" type="text" class="aibi_input" value=""/>
								</div>
								<!-- 提示 -->
								<div id="tabelTip" class="tishi error"  style="display:none;"></div>
							</td>
						</tr>
						<tr>
							<th>数据库字段名：</th>
							<td>
								<div>
									<input style="width:350px" onkeyup="$('#colTip').hide();" name="columnName" id="columnName" type="text" class="aibi_input" value=""/>
								</div>
								<!-- 提示 -->
								<div id="colTip" class="tishi error"  style="display:none;"></div>
							</td>
						</tr>
						<tr>
							<th>数据库字段中文名：</th>
							<td>
								<div>
									<input style="width:350px" onkeyup="$('#colCnTip').hide();" name="columnCnName" id="columnCnName" type="text" class="aibi_input" value=""/>
								</div>
								<!-- 提示 -->
								<div id="colCnTip" class="tishi error"  style="display:none;"></div>
							</td>
						</tr>
					
						<tr>
							<td>
								<div>
									<input name="saveBtn"  id="saveBtn" type="button" onclick="submitList()"  class="tag_btn" value="保存"/>
								</div>
							</td>
						</tr>
						<tr><td></td><td></td><td></td></tr>
					</table>
				</div>
				<input type="hidden" name="ciCustomGroupInfo.createTypeId" id="createTypeId" value="12"/>
				
		 </form>
<script type="text/javascript">
var attrNum = 0;
$(document).ready(function(){
	//表格斑马线
	changeTableColor(".showTable");
});
//保存
function submitList(){
	//校验是否有重名客户群
	if(validateForm()){
		var actionUrl = $.ctx+'/ci/customersManagerAction!isNameExist.ai2do';
		$.post(actionUrl,$("#saveForm").serialize(), function(result){
			if(result){
				window.parent.$('.dialog_btn_div').hide();
				var submitUrl =  $.ctx+'/ci/customersManagerAction!createCustomByImportTable.ai2do';
				var  columnNames  =   $("#columnName").val().split(',');
				var columnCnNames  =   $("#columnCnName").val().split(',');
				var params = $("#saveForm").serialize();
				for(var i = 0;i<columnNames.length;i++){
						params = params +'&importColumnList['+i+'].columnName='+columnNames[i]+'&importColumnList['+i+'].columnCnName='+columnCnNames[i];
				}
				//alert(params);
				$.post(submitUrl,params, function(result){
					if(result.success){
						parent.showAlert(result.msg,"success");
					}else{
						parent.showAlert(result.msg,"failed");
					}
				});
			}else{
				$('#cnameTip').empty();
				$('#cnameTip').show().append('客户群重名');
			}
		});
	}
}


//表单验证
function validateForm(){
	var customGroupName = $.trim($("#customGroupName").val());
	if(customGroupName == ""){
		$("#customGroupName").val("");
		$("#cnameTip").empty();
		$("#cnameTip").show().append("请输入客户群名称");
		return false;
	}else{
		$("#customGroupName").val(customGroupName);
	}
	var custom_desc = $.trim($("#custom_desc").val());
	if(custom_desc == ""){
		$("#custom_desc").val("");
		$("#descTip").empty();
		$("#descTip").show().append("请输入客户群描述");
		return false;
	}else{
		$("#custom_desc").val(custom_desc);
	}
	var customUpdateCycle = $.trim($("#customUpdateCycle").val());
	if(customUpdateCycle == ""){
		$("#customUpdateCycle").val("");
		$("#cycleTip").empty();
		$("#cycleTip").show().append("请选择客户群周期");
		return false;
	}else{
		$("#customUpdateCycle").val(customUpdateCycle);
	}
	var customTabelName = $.trim($("#customTabelName").val());
	if(customTabelName == ""){
		$("#customTabelName").val("");
		$("#tabelTip").empty();
		$("#tabelTip").show().append("请输入表名");
		return false;
	}else{
		$("#customTabelName").val(customTabelName);
	}
	var columnName = $.trim($("#columnName").val());
	if(columnName == ""){
		$("#columnName").val("");
		$("#colTip").empty();
		$("#colTip").show().append("请输入字段名");
		return false;
	}else{
		$("#columnName").val(columnName);
	}
	var columnCnName = $.trim($("#columnCnName").val());
	if(columnCnName == ""){
		$("#columnCnName").val("");
		$("#colCnTip").empty();
		$("#colCnTip").show().append("请输入字段中文名");
		return false;
	}else{
		$("#columnCnName").val(columnCnName);
	}

	return true;
}
//验证属性是否重复


</script>
</body>
</html>