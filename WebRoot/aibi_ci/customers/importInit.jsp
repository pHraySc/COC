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
<form id="saveForm" method="post" action="<%=request.getContextPath() %>/ci/customersFileAction!uploadCustomGroupFile.ai2do" enctype="multipart/form-data">
				<div class="dialog_table">
					<table width="100%" class="showTable  new_tag_table" cellpadding="0" cellspacing="0">
						<tr>
							<th width="110px"><span>*</span>客户群名称：</th>
							<td>
								<div class="new_tag_selectBox">
									<input name="ciCustomGroupInfo.customGroupName"  onkeyup="hideTip()" id="customGroupName" type="text" 
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
									<base:DCselect selectId="secenId" selectName="ciCustomGroupInfo.sceneId" 
										tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
							        	nullName="请选择" nullValue="" selectValue="${ciCustomGroupInfo.sceneId}"/>
							    </div> 
								<!-- 提示 -->
								<div id="snameTip" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
							</td>
						</tr>
						<tr>
							<th>客户群描述：</th>
							<td>
								<div>
									<textarea id="custom_desc" onkeyup="this.value = this.value.slice(0,170)" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea"></textarea>
								</div>
								<!-- 提示 -->
							</td>
						</tr>
						<tr>
							<th width="110px"><span>*</span>导入文件：</th>
							<td>
								<div class="new_tag_selectBox"> 
									<input type="file" name="file" id="customGroupFile"  class="new_tag_input" style="width:302px;*width:306px;" />
								</div>
								<div id="fnameTip" class="tishi error"  style="display:none;"></div>
							</td>
							<td><a href="<%=request.getContextPath() %>/ci/customersFileAction!downImportTempletFile.ai2do" title="">导入文件模板</a>
                            <span id="importTip" class="greenTip inline_block">&nbsp;</span>
                            </td>
						</tr>
						<tr>
							<th>添加属性：</th>
							<td>
								<div id="attributeDiv"></div>
								<div id="anameTip" class="tishi error"  style="display:none;"></div>
							</td>
							<td>
								<div id="addAttributeBtn" class="icon_button_add_attr icon_button_normal" ><span>添加属性</span></div>
			                </td>
			                
						</tr>
						<tr><td></td><td></td><td></td></tr>
					</table>
				</div>
				<input type="hidden" name="ciCustomGroupInfo.productAutoMacthFlag" id="productAutoMacthFlag" />
		 </form>
<script type="text/javascript">
var attrNum = 0;
$(document).ready(function(){
	$("#secenId").multiselect({
        multiple	: false,
        header		: false,
        height		: '130px',
        selectedList: 1
    });
	//表格斑马线
	changeTableColor(".showTable");
	
	$( document ).tooltip({
		items: "#importTip",
		content: function() {
			var html='<ul class="importCustermersTip">' + 
				'<li>1.客户群数据文件必须是csv,txt格式文件;</li>' + 
				'<li>2.文件名中不能包含中文字符;</li>' + 
				'<li>3.每行一条记录;</li>' + 
				'<li>4.文件中多个属性用逗号分隔;</li>' + 
				'<li>5.添加属性的个数与顺序必须与文件中一致;</li>' + 
				'</ul>';
			return html;
		}
	});

	//添加属性
	$('#addAttributeBtn').unbind("click").bind('click', function() {
		addAttribute();
	});
});

//添加属性
function addAttribute() {
	var attrHtml = '<div style="margin:5px 5px 5px 0px;"><input name="attrList[' + attrNum + 
		']" type="text" maxlength="35" class="new_tag_input custom_attr_input"' +  
		'style="width:295px;*width:295px;cursor:text;margin-right:5px;">' +
		'<a href="javascript:void(0);" class="icon_button_delete_attr" onclick="delAttribute(this)"><span>删除</span></a></div>'
	$('#attributeDiv').append(attrHtml);
	attrNum ++;
	if(attrNum >= 6) { //属性最多只能有6个
		$('#addAttributeBtn').unbind("click").bind('click', function() {
			parent.parent.showAlert('一次导入最多只能添加六个属性！','failed');
		});
	}
}

//删除属性文本框
function delAttribute(a) {
	$(a).parent().remove();
	attrNum --;
	if(attrNum < 6) { //属性最多只能有6个
		$('#addAttributeBtn').unbind("click").bind('click', function() {
			addAttribute();
		});
	}
}

//保存
function submitList(){
	if(validateForm()){
		var actionUrl = $.ctx+'/ci/customersManagerAction!isNameExist.ai2do';
		$.post(actionUrl,$("#saveForm").serialize(), function(result){
			if(result){
				window.parent.$('.dialog_btn_div').hide()
				$('#saveForm').submit();
			}else{
				$('#cnameTip').empty();
				$('#cnameTip').show().append('客户群重名');
			}
		});
	}
}

function hideTip(){
	$("#cnameTip").hide();
}


//表单验证
function validateForm(){
	var cname = $.trim($("#customGroupName").val());
	if(cname == ""){
		$("#customGroupName").val("");
		$("#cnameTip").empty();
		$("#cnameTip").show().append("请输入客户群名称");
		return false;
	}else{
		$("#customGroupName").val(cname);
	}
	var sname = $('#secenId').val();
	if($.trim(sname) == "") {
		$("#snameTip").empty();
		$("#snameTip").show().append("请选择客户群分类");
		return false;
	}
	var file = $("#customGroupFile").val();
	var fileName = file.substr(file.indexOf("."));   
	if(fileName == ""){
		$("#fnameTip").empty();
		$("#fnameTip").show().append("请选择文件");
		return false;
	}
    if(fileName != ".txt" && fileName != ".csv" && fileName != ".xls"){
    	$("#fnameTip").empty();
		$("#fnameTip").show().append("请选择合法格式文件");
		return false;
    }

    if($("#isAutoMatch").prop("checked")==true) {
		$("#productAutoMacthFlag").val("1");
	}
    
	return customGroupAttValueRepeat();
}
//验证属性是否重复
function customGroupAttValueRepeat() {
	var flag = true;
	var array = [];
    var attrsInput = $('.custom_attr_input');
    for(var i=0; i<attrsInput.length; i++) {
    	var attrVal = $.trim($(attrsInput[i]).val());
    	if($.inArray(attrVal, array) != -1) {
			flag = false;
		} else {
			array.push(attrVal);  
		}
    }
    if(!flag) {
    	$("#anameTip").empty();
		$("#anameTip").show().append("客户群属性名称重复！");
    } else {
    	$("#anameTip").hide();
    }
    return flag;
}

</script>
</body>
</html>