<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>创建客户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<script type="text/javascript">
<%
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
%>
var	validater;
$(document).ready(function(){
	var _isSuccess = '${resultMap["success"]}';
	var _msg = '${resultMap["msg"]}';
	if(_isSuccess == 'false'){
		showAlert(_msg,"failed",parent.closeCreate);
		parent.multiCustomSearch();
	}
	
	var scenePublicId="<%=ServiceConstants.ALL_SCENE_ID%>";
	validater = $("#saveForm").validate({
		rules: {
			"ciCustomGroupInfo.customGroupName": {
				required: true
			},
			"sceneIds": {
				required: true
			}
		},
		messages: {
			"ciCustomGroupInfo.customGroupName": {
				required: "客户群名称不允许为空!"
			},
			"sceneIds": {
				required: "客户群分类不能为空!"
			}
		},
		errorPlacement:function(error,element) {
			element.parent("div").next("div").hide();
			element.parent("div").next("div").after(error);
		},
		success:function(element){
			//element.prev("div").show();
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

		//模拟下拉框--打开方式 
	    $("#selBtn").toggle(function(){
	    	$("#selListBox").slideDown();
	    	$(this).addClass("selBtnActive");
	    },function(){
	        $("#selListBox").slideUp();
	        $(this).removeClass("selBtnActive");
	    })
		//模拟下拉框--打开方式 
	    $("#scenesTxt").toggle(function(){
	    	$("#selListBox").slideDown();
	    	$("#selBtn").addClass("selBtnActive");
	    },function(){
	        $("#selListBox").slideUp();
	        $("#selBtn").removeClass("selBtnActive");
	    })
	  //绑定隐藏提示层
		$(document).click(function(e){
		    var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
			$("#selListBox").slideUp();
			$(".selBtn").removeClass("selBtnActive");
		});
		//阻止事件冒泡
		$(".firstItem").click(function(ev){
			firstItemFn($("#firstItem_active"),'sceneIds' );
			var e = ev||event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		$(".selListBox li a").click(function(ev){
			if($(this).find("input").attr("checked")){
				$(this).find("input").attr("checked",false)
			}else{
				$(this).find("input").attr("checked",true)
			}
			selItemFn($(this).find("input"),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		$(".selListBox li a input").click(function(ev){
			selItemFn($(this),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})

		$(".selAllBox").click(function(ev){
			if($("#selBox_all").attr("checked")){
				$("#selBox_all").attr("checked",false)
			}else{
				$("#selBox_all").attr("checked",true)
			}
			selItemAllFn($("#selBox_all"),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
		$(".selAllBox input").click(function(ev){
			selItemAllFn($("#selBox_all"),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
	 	//默认选中场景
	 	/*var sceneIdList = $.trim($("#sceneIds").val());
	 	//alert(sceneIdList);
	 	var sceneIdArr = [];
	 	if(sceneIdList && sceneIdList != ""){
	 		sceneIdArr = sceneIdList.split(",");
	 		var textArr =[];
	 		//公共场景
	 		if($.inArray(scenePublicId,sceneIdArr)>=0){
		 		$("#firstItem_active").attr("checked",true);
		 		$("#scenesTxt").html($("#firstItem_active").attr("_sceneName"));
		 		$("#scenesTxt").attr("title",$("#firstItem_active").attr("_sceneName"));
		 	}else {
		 		var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
		 		$.each($inputArr,function(index ,item){
		 			if($.inArray($.trim($(item).val()),sceneIdArr)>=0){
		 				$(item).attr("checked",true);
		 				textArr.push($(item).attr("_sceneName"));
			 		}
		 		})
		 		$("#scenesTxt").html(textArr.join(","));
		 		$("#scenesTxt").attr("title",textArr.join(","));
			 }
		 }*/

	 	//参数回写
	    var selBakArr =  $("#sceneIds").val().split(",");
	    var firstItemVal  = $("#firstItem_active").val();
	    var selListInput = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	    //alert(selBakArr.length);
	    var titleArr = [];
	    for(var i = 0;i< selBakArr.length ;i++){
	        //全景视图
	        if(selBakArr[i] == firstItemVal){
	            titleArr.push($("#firstItem_active").attr("_sceneName"));
	            $("#firstItem_active").attr("checked",true);
	            break;
	        }else{
	            for(var j=0;j<selListInput.length ;j++){
	                if($(selListInput[j]).val() == selBakArr[i]){
	                   titleArr.push($(selListInput[j]).attr("_sceneName"));
	                   $(selListInput[j]).attr("checked",true);
	                   break ; 
	                }
	            }
	        }
	    }
	    //下拉框回显
	    $("#scenesTxt").html(titleArr.join(",")).attr("title",titleArr.join(","));
	 	
	//表格斑马线
	changeTableColor(".showTable");
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

function submitForm(){
	if(validateForm()){
		var actionUrl = $.ctx;
		if($("#custom_id").val() != null && $("#custom_id").val() != ""){
			actionUrl += "/ci/customersManagerAction!editCustomSimple.ai2do";
		}else if($("#custom_id").val() == null || $("#custom_id").val() == ""){
			actionUrl += "/ci/customersManagerAction!saveCustomer.ai2do";
		}
		$.post(actionUrl, $("#saveForm").serialize(), function(result){
			if(result.success){
				var customerName = $("#custom_name").val();
				if($("#custom_id").val() != null && $("#custom_id").val() != ""){
					//parent.showAlert("修改成功！","success", parent.multiCustomSearch);
					parent.backMultiCustom();
				}else if($("#custom_id").val() == null || $("#custom_id").val() == ""){
					parent.openSuccess(customerName, "");
				}
				parent.closeCreate();
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

	if($.trim($("#sceneIds").val()) == "") {
		$("#snameTip").empty();
		$("#snameTip").show().append("请选择客户群分类");
		return false;
	}
	
	if(!validater.form()){
		return false;
	}
	return true;
}
//多选下拉框全选
function selItemAllFn(obj,selId){
	$("#firstItem_active").attr("checked",false);
	var $obj =$(obj);
	var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	var _id =obj.attr("id");
	var flag = document.getElementById(_id).checked;
	var valArr = [];
	var textArr = [];
	$.each($inputArr,function(index ,item){
		$(item).attr("checked",flag);
		valArr.push($(item).val());
		var $labelText = $.trim($(item).attr("_sceneName"));
		textArr.push($labelText);
	});
	if(flag){
		$("#" +selId ).val(valArr.join(","));
		$("#scenesTxt").html(textArr.join(","));
		$("#scenesTxt").attr("title",textArr.join(","));
	}else{
		$("#" +selId ).val("");
		$("#scenesTxt").html("");
		$("#scenesTxt").attr("title","");
	}
}
//第一个参数
function firstItemFn(obj,selId){
	var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	$.each($inputArr,function(index ,item){
		$(item).attr("checked",false);
	})
	$("#selBox_all").attr("checked",false);
	if($(obj).attr("checked")){
		var itemVal =  $(obj).val();
		var optionArr =$("#"+selId).val(itemVal);
		var $labelText = $.trim($(obj).attr("_sceneName"));
		$("#scenesTxt").html($labelText);
		$("#scenesTxt").attr("title",$labelText);
	}else{
		$("#"+selId).val("");
		$("#scenesTxt").html("");
		$("#scenesTxt").attr("title","");
	}
}
function selItemFn(obj,selId){
	//全选和第一复选框不选中
	$("#firstItem_active").attr("checked",false);
	$("#selBox_all").attr("checked",false);
	//去掉第一个参数 
	var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
	var valArr = []; 
	var textArr = [];
	$.each($inputArr,function(index ,item){
		if($(item).attr("checked")){
			valArr.push($(item).val());
			var $labelText = $.trim($(item).attr("_sceneName"));
			textArr.push($labelText);
		}
	})
	$("#"+selId).val(valArr.join(","));
	$("#scenesTxt").html(textArr.join(","));
	$("#scenesTxt").attr("title",textArr.join(","));
	
}
</script>
</head>
<body>
<form id="saveForm" method="post">
<input id="custom_id" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}" />
<input type="hidden" name="ciCustomGroupInfo.createTypeId" value="${ciCustomGroupInfo.createTypeId}" />
<input type="hidden" name="ciCustomGroupInfo.updateCycle" value="${ciCustomGroupInfo.updateCycle}"/>
<input type="hidden" name="ciCustomGroupInfo.createUserId" value="${ciCustomGroupInfo.createUserId}" />
<input type="hidden" name="ciCustomGroupInfo.createTime" value='<fmt:formatDate value="${ciCustomGroupInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
<input type="hidden" name="ciCustomGroupInfo.createCityId" value="${ciCustomGroupInfo.createCityId}" />
<input type="hidden" name="ciCustomGroupInfo.isPrivate" value="${ciCustomGroupInfo.isPrivate}" />
<input type="hidden" name="ciCustomGroupInfo.isHasList" value="${ciCustomGroupInfo.isHasList}" />
<input type="hidden" name="ciCustomGroupInfo.isFirstFailed" value="${ciCustomGroupInfo.isFirstFailed}" />
<input type="hidden" name="ciCustomGroupInfo.listCreateTime" value="${ciCustomGroupInfo.listCreateTime}" />

<c:if test='${ciCustomGroupInfo.parentCustomId != null && ciCustomGroupInfo.parentCustomId != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.parentCustomId" value="${ciCustomGroupInfo.parentCustomId}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.prodOptRuleShow != null && ciCustomGroupInfo.prodOptRuleShow != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.prodOptRuleShow" value="${ciCustomGroupInfo.prodOptRuleShow}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.labelOptRuleShow != null && ciCustomGroupInfo.labelOptRuleShow != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value="${ciCustomGroupInfo.labelOptRuleShow}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.customOptRuleShow != null && ciCustomGroupInfo.customOptRuleShow != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" value="${ciCustomGroupInfo.customOptRuleShow}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.kpiDiffRule != null && ciCustomGroupInfo.kpiDiffRule != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.kpiDiffRule" value="${ciCustomGroupInfo.kpiDiffRule}"/>
</c:if>
<input type="hidden" name="ciCustomGroupInfo.status" value="${ciCustomGroupInfo.status}"/>
<input type="hidden" name="ciCustomGroupInfo.isSysRecom" value="${ciCustomGroupInfo.isSysRecom}"/>
<input type="hidden" name="ciCustomGroupInfo.dataStatus" value="${ciCustomGroupInfo.dataStatus}"/>
<input type="hidden" name="ciCustomGroupInfo.customNum" value="${ciCustomGroupInfo.customNum}"/>
<input type="hidden" name="ciCustomGroupInfo.tacticsId" value="${ciCustomGroupInfo.tacticsId}"/>
<input type="hidden" name="ciCustomGroupInfo.dayLabelDate" value="${ciCustomGroupInfo.dayLabelDate}"/>
<input type="hidden" name="ciCustomGroupInfo.monthLabelDate" value="${ciCustomGroupInfo.monthLabelDate}"/>
<input type="hidden" name="ciCustomGroupInfo.isFirstFailed" value="${ciCustomGroupInfo.isFirstFailed}"/>
<input type="hidden" name="ciCustomGroupInfo.thumbnailData" value="${ciCustomGroupInfo.thumbnailData}"/>
<input type="hidden" name="ciCustomGroupInfo.isContainLocalList" value="${ciCustomGroupInfo.isContainLocalList}"/>
<input type="hidden" name="ciCustomGroupInfo.isLabelOffline" value="${ciCustomGroupInfo.isLabelOffline}"/>
<input type="hidden" name="ciCustomGroupInfo.duplicateNum" value="${ciCustomGroupInfo.duplicateNum}" />
<input type="hidden" name="ciCustomGroupInfo.dataTime" value='<fmt:formatDate value="${ciCustomGroupInfo.dataTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
<c:if test='${ciCustomGroupInfo.startDate != null && ciCustomGroupInfo.startDate != ""}'>
<input type="hidden" name="ciCustomGroupInfo.startDate" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
</c:if>
<c:if test='${ciCustomGroupInfo.endDate != null && ciCustomGroupInfo.endDate != ""}'>
<input type="hidden" name="ciCustomGroupInfo.endDate" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
</c:if>
<c:if test='${ciCustomGroupInfo.newModifyTime != null && ciCustomGroupInfo.newModifyTime != ""}'>
<input type="hidden" name="ciCustomGroupInfo.newModifyTime" value='<fmt:formatDate value="${ciCustomGroupInfo.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
</c:if>
<c:if test='${ciCustomGroupInfo.dataDate != null && ciCustomGroupInfo.dataDate != ""}'>
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value="${ciCustomGroupInfo.dataDate}"/>
</c:if>
<c:if test='${ciCustomGroupInfo.templateId != null && ciCustomGroupInfo.templateId != ""}'>
	<input type="hidden" name="ciCustomGroupInfo.templateId" value="${ciCustomGroupInfo.templateId}"/>
</c:if>

<div class="dialog_table">
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th style="width:100px;padding-left:10px;"  ><label class="star labFmt100">客户群名称：</label></th>
			<td>
				<div class="new_tag_selectBox">
					<input name="ciCustomGroupInfo.customGroupName" type="text" id="custom_name" value="${ciCustomGroupInfo.customGroupName}" 
						maxlength="35" class="new_tag_input_edit" style="width:302px;*width:306px;cursor:text"/>
				</div>
				<!-- 提示 -->
				<div id="cnameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>	
		<tr>
			<th style="width:100px;padding-left:10px;" ><label class="star labFmt100">客户群分类：</label></th>
			<td>
				<div class="fleft selBox">
	             	  <div class="selTxt" id="scenesTxt" style="overflow:hidden;"></div>
	             	  <a href="javascript:void(0);" class="selBtn" id="selBtn"></a>
	             	  <div class="selListBox" id="selListBox">
	             	     <div class="firstItem">
            	     		<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
		             	  	 	<c:if test="${dimScenes.sortNum == 0}">
	             	  	 	 		<input type="checkbox" value="${dimScenes.sceneId}"  id="firstItem_active" name="_sceneBoxItemCheckShare" _sceneName="${dimScenes.sceneName}"/>
	             	  	 	 		<label for="firstItem_active">${dimScenes.sceneName}</label>
			             	  	 </c:if>
		             	  	 </c:forEach>
	             	  	 </div>
	             	  	 <ol>
	             	  	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
	             	  	 		<c:if test="${dimScenes.sortNum > 0}">
		             	  	 	 <li>
		             	  	 	 	<a href="javascript:void(0);" >
		             	  	 	 		<input type="checkbox" id="selBoxItem0${st.index}" name="_sceneBoxItemCheckList" value="${dimScenes.sceneId}" _sceneName="${dimScenes.sceneName}"/>
		             	  	 	 		${dimScenes.sceneName}<!--<label for="selBoxItem0${st.index}">${dimScenes.sceneName}</label>-->
		             	  	 	 	</a>
		             	  	 	 </li>
		             	  	 	 </c:if>
	             	  	 	</c:forEach>
	             	  	 </ol>
	             	  	 <div class="selAllBox">
	             	  	 	 <input type="checkbox" id="selBox_all">全选</input>
	             	  	 </div>
	             	  </div>
	             </div>
				<!-- 提示 -->
				<div id="snameTip" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
			</td>
		</tr>
		<tr>
			<th>客户群描述：</th>
			<td>
				<div>
					<textarea id="custom_desc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" onblur="this.value = this.value.slice(0,170)" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea">${ciCustomGroupInfo.customGroupDesc}</textarea>
				</div>
				<!-- 提示 -->
				<div class="tishi error" style="display:none;"></div>
			</td>
		</tr>
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
    <%if("true".equals(marketingMenu)){ %>
	<input id="isAutoMatch" type="checkbox" class="valign_middle" name="ciCustomGroupInfo.productAutoMacthFlag" value=""/> 系统自动匹配产品&nbsp;&nbsp;
	<%} %>
	<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
</div>
<input type="hidden" name="sceneIds" id="sceneIds" value="${sceneIds }"/>
</form>
</body>
</html>

