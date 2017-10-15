<%@ taglib prefix="suite" uri="http://tag.pagecomponent.biframe.asiainfo.com/suite" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/aibi_ci/html_include.jsp" %>
    <style type="text/css">
    </style>
    <script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
    <c:if test="${empty alarmThreshold.thresholdId}">
        <c:set var="title" value="添加"/>
        <c:set var="saveBtn" value="保存"/>
        <c:set var="processFun" value="saveAlarmThreshold()"/>
        <c:set var="isAdd" value="true"/>
    </c:if>
    <c:if test="${! empty alarmThreshold.thresholdId}">
        <c:set var="title" value="修改"/>
        <c:set var="saveBtn" value="修改"/>
        <c:set var="processFun" value="modifyAlarmThreshold()"/>
        <c:set var="isAdd" value="false"/>
    </c:if>


    <title>客户群预警<c:out value="${title}"/></title>

    <script type="text/javascript">
        $(document).ready(function () {
            aibiTableChangeColor(".showTable");
             
            $("#thresholdType,#levelId,#columnId").multiselect({
                multiple: false,
                header: false,
                height: "auto",
                selectedList: 1
            });
            if("${isAdd}" == "true"){
            	
            	$("#maxValue").focusblur({ 
            		"focusColor":"#222232" ,
            	    "blurColor":"#757575"  
            	});
            	$("#minValue").focusblur({ 
            		"focusColor":"#222232" ,
            	    "blurColor":"#757575"  
            	});
            	
                var customGroupId = '${param["customGroupId"]}';
                if(customGroupId != null && customGroupId != "") {
            		var customGroupName = decodeURIComponent('${param["customGroupName"]}');
            		$("#busiId").val(customGroupId);
            		$("#busiName").val(customGroupName);
                }else{
                	$("#busiId").multiselect({
                        multiple: false,
                        closeable:false,
                        noneSelectedText: "请选择客户群",
                        selectedList: 1
                    }).multiselectfilter(); 
                        $("#busiId").bind("change",function(){
                        	$("#busiId").multiselect({selectedText:$(this).find("option:selected").text()});
                            $("#busiName").val($(this).find("option:selected").text());
                        })
                    createTree();
                }
                $("#maxValue").val("请输入上限值");
                $.focusblur("#maxValue");
                $("#minValue").val("请输入下限值");
                $.focusblur("#minValue");
               
            }else{
                /*var busiIdSelect = $("#busiId");
                busiIdSelect.attr("disabled","disabled");
                busiIdSelect.append('<option value="${alarmThreshold.busiId}">${alarmThreshold.busiName}</option>');
                busiIdSelect.multiselect('refresh');*/
            }

            $("#thresholdType").bind("change",function(){
                var thresholdType = $(this).val();
                if("in" == thresholdType || "out" == thresholdType){
                    if($("#maxValue").val()==""){
                    	$("#maxValue").val("请输入上限值");
                    }
                    if($("#minValue").val()==""){
                    	$("#minValue").val("请输入下限值");
                    }
                    $("#maxValue").attr("disabled",false);
                	$("#minValue").attr("disabled",false);
                    if("${isAdd}" == "false"){
                    $("#maxValue").attr("disabled",false);
                	$("#minValue").attr("disabled",false);
                	$("#minValue").val("");
                	$("#maxValue").val("");
                    }
                }
                if("more" == thresholdType){
                    $("#minValue").val("");
                	$("#maxValue").val("");
                    if("${isAdd}" == "true"){
                    	$("#maxValue").val("请输入上限值");
                    	$("#maxValue").attr("disabled",false);
                    	$("#minValue").attr("disabled",true);
                    }
                    if("${isAdd}" == "false"){
                    	$("#maxValue").attr("disabled",false);
                    	$("#minValue").attr("disabled",true);
                    }
                }
                if("less" == thresholdType){
                    $("#maxValue").val("");
                	$("#minValue").val("");
                    if("${isAdd}" == "true"){
                    	$("#minValue").val("请输入下限值");
                    	$("#maxValue").attr("disabled",true);
                    	$("#minValue").attr("disabled",false);
                    }
                    if("${isAdd}" == "false"){
                    	$("#maxValue").attr("disabled",true);
                    	$("#minValue").attr("disabled",false);
                    }
                }
            });

        });

        function createTree(){
            var actionUrl = $.ctx + "/ci/ciCustomersAlarmAction!findCustomerGroupTree.ai2do";
            $.ajax({
                type: "POST",
                url: actionUrl,
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        var busiIdSelect = $("#busiId");
                        $.each(result.treeList,function(i,obj){
                            var option = '<option value="'+obj['id']+'">'+obj['name']+'</option>';
                            busiIdSelect.append(option);
                        });
                        busiIdSelect.multiselect('refresh');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        //校验表单
        function validateForm() {
            var maxValue = $.trim($("#maxValue").val());
            if(maxValue == "请输入上限值"){
                $("#maxValue").val("");
                maxValue="";
            }
            var minValue = $.trim($("#minValue").val());
            if(minValue == "请输入下限值"){
                $("#minValue").val("");
                minValue="";
            }

            var validater = $("#saveForm").validate({
                rules: {
                    "alarmThreshold.busiId": {
                        minlength: 5
                    },
                    "alarmThreshold.maxValue":{
                        number:true
                    },
                    "alarmThreshold.minValue":{
                        number:true
                    }
                },
                messages: {
                    "alarmThreshold.busiId": {
                    	 required: "客户群名称不允许为空!",
                    },
                    "alarmThreshold.maxValue":{
                        required:"上限值不允许为空!",
                        number:"上限值应该为数值"
                    },
                    "alarmThreshold.minValue":{
                        required:"下限值不允许为空!",
                        number:"下限值应该为数值"
                    }
                },
                errorPlacement: function (error, element) {
                    element.next("div").hide();
                    element.after(error);
                },
                success: function (element) {
                    //element.prev("div").show();
                    element.remove();
                },
                errorElement: "div",
                errorClass: "tishi error"
            });
            var thresholdType = $("#thresholdType").val();
            if("in" == thresholdType || "out" == thresholdType){
                $("#maxValue").rules("remove","required");
                $("#minValue").rules("remove","required");
                $("#maxValue").rules("add",{required:true});
                $("#minValue").rules("add",{required:true});
                
                if(maxValue =="" && Number(maxValue) < Number(minValue)){
                    $("#maxValueDiv").hide();
                    $("#maxValueDiv").next().empty();
                    $("#maxValueDiv").next().show().append("上限值不允许为空!");
                    isSuccess = false;
                    }
            }
            if("more" == thresholdType){
                $("#maxValue").rules("remove","required");
                $("#minValue").rules("remove","required");
                $("#maxValue").rules("add",{required:true});
            }
            if("less" == thresholdType){
                $("#maxValue").rules("remove","required");
                $("#minValue").rules("remove","required");
                $("#minValue").rules("add",{required:true});
            }
            var busiIdIsValid =true,otherIsValid=true;
            if (!validater.form()) {
                otherIsValid =  false;
            }

            if($("#busiId").val()==""){
                $("#nameTip").hide();
                $("#nameTip").empty();
                $("#nameTip").show().append("客户群名称不允许为空!");
                busiIdIsValid = false;
            }
            
            if(!busiIdIsValid || ! otherIsValid){
                return false;
            }

            var isSuccess = true;
            //如果是占比
            if($("#columnId").val() == "2"){
                if(maxValue != "" && (Number(maxValue) >1 || Number(maxValue)<0 )){
                    $("#maxValueDiv").hide();
                    $("#maxValueDiv").next().empty();
                    $("#maxValueDiv").next().show().append("占比预警的上限值应该在0到1之间!");
                    isSuccess = false;
                }
                if(minValue != "" && (Number(minValue) >1 || Number(minValue)<0 )){
                    $("#minValueDiv").hide();
                    $("#minValueDiv").next().empty();
                    $("#minValueDiv").next().show().append("占比预警的下限值应该在0到1之间!");
                    isSuccess = false;
                }
            }else if($("#columnId").val() == "0"){     //客户数
                var isSuccess = true;
                if(maxValue != "" && !isNumber(maxValue)){
                    $("#maxValueDiv").hide();
                    $("#maxValueDiv").next().empty();
                    $("#maxValueDiv").next().show().append("上限值应该是正整数!");
                    isSuccess = false;
                }
                if(minValue != "" && !isNumber(minValue)){
                    $("#minValueDiv").hide();
                    $("#minValueDiv").next().empty();
                    $("#minValueDiv").next().show().append("下限值应该是正整数!");
                    isSuccess = false;
                }
            }

            if(isSuccess && maxValue != "" && minValue != "" && Number(maxValue) < Number(minValue)){
                $("#maxValueDiv").hide();
                $("#maxValueDiv").next().empty();
                $("#maxValueDiv").next().show().append("上限值应该大于下限值!");
                isSuccess = false;
            }
            return isSuccess;
        }

        //是否是正整数
        function isNumber(oNum){
            if(!oNum) return false;
            var strP=/^\d+$/; //正整数
            if(!strP.test(oNum)) return false;
            return true;
        }

        //保存预警阈值
        function saveAlarmThreshold() {
        	 var customGroupId = '${param["customGroupId"]}';
            if(validateForm()){
                //$("#busiName").val($("#busiId").find("option:selected").text());
                var actionUrl = $.ctx + '/ci/ciCustomersAlarmAction!addThreshold.ai2do';
                var fromPageFlag = '${param["fromPageFlag"]}';
                if(fromPageFlag != null && fromPageFlag == 1){
                	actionUrl += "?fromPageFlag=1";
                }
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    data: $("#saveForm").serialize(),
                    success: function (result) {
                        if (result.success) {
                            //alert("客户群预警保存成功！");
                            //parent.showAlert("保存成功！","success");
                            parent.closeDialog("#dialog_edit");
	                    parent.$.fn.weakTip({"tipTxt":"保存成功！"});
                        if(customGroupId == null || customGroupId == "") {
		                parent.refreshList();                        
                        }
			    } else {
                            if (result.errorType == "1") {
                                $("#nameTip").hide();
                                $("#nameTip").empty();
                                $("#nameTip").show().append("类型和级别相同的预警已存在!");
                            } else {
                                parent.showAlert("保存失败！","failed");
                            }
                        }
                    }
                });
            }
        }

        //修改预警阈值
        function modifyAlarmThreshold() {
            if(validateForm()){
                var actionUrl = $.ctx + '/ci/ciCustomersAlarmAction!modifyThreshold.ai2do?';
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    data: $("#saveForm").serialize(),
                    success: function (result) {
                        if (result.success) {
                            //alert("客户群预警修改成功！");
                            parent.closeDialog("#dialog_edit");
                            parent.$.fn.weakTip({"tipTxt":"修改成功!"});
                            parent.refreshList();
                        } else {
                            if (result.errorType == "1") {
                                $("#nameTip").hide();
                                $("#nameTip").empty();
                                $("#nameTip").show().append("类型和级别相同的预警已存在!");
                            } else {
                                parent.showAlert("保存失败！","failed");
                            }
                        }
                    }
                });
            }
        }
        jQuery.focusblur = function(focusid){
            var focusblurid = $(focusid);
            var defval = focusblurid.val();
            focusblurid.focus(function(){
                var thisval = $(this).val();
                $(this).css("color","#323232");
                if(thisval==defval){
                    $(this).val("");
                }
            });
            focusblurid.blur(function(){
                var thisval = $(this).val();
                if(thisval==""){
	                $(this).css("color","#8e8e8e");
                    $(this).val(defval);
                }
            });
        };
    </script>
  
</head>
<body>

<div class="dialog_table dialog_normal"><!-- 一组表单 start -->
    <form id="saveForm">

        <input name="alarmThreshold.thresholdId" id="thresholdId" type="hidden" value="${alarmThreshold.thresholdId}">
        <input name="alarmThreshold.refId" id="refId" type="hidden" value="${alarmThreshold.refId}">
        <input name="alarmThreshold.refType" id="refType" type="hidden" value="${alarmThreshold.refType}">

        <table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
            <tr>
                <th style="width:100px;padding-left:10px;" ><label class="star labFmt100">客户群：</label>
                <td>
                    <c:if test="${isAdd == true}">
                        <c:if test="${param['customGroupId'] != null }">
                    	<input id="busiId" name="alarmThreshold.busiId" type="hidden" value="">
                    		<input id="busiName" name="alarmThreshold.busiName" type="text"
                               class="new_tag_input" style="width:300px;cursor:text" value=""
                               readonly="readonly"/>
		                </c:if>
		                  <!-- 提示 -->
		                <c:if test="${param['customGroupId'] == null }">
		                <div style="float:left;">
                        <select name="alarmThreshold.busiId" id="busiId"  class="dialog_select" style="width: 310px;">
                            <option value="">请选择客户群</option>
                        </select>
                        </div>
                        <input id="busiName" name="alarmThreshold.busiName" type="hidden" value=""> 
                        </c:if>
		            </c:if>
                    <c:if test="${isAdd == false}">
                            <input id="busiId" name="alarmThreshold.busiId" type="hidden" value="${alarmThreshold.busiId}">
                            <input id="busiName" name="alarmThreshold.busiName" type="text" readonly="readonly" class="new_tag_input" style="width:300px;" value="${alarmThreshold.busiName}">
                    </c:if>
                    <div id="nameTip" class="tishi error" style="display:none;float:left"></div>
                    
                </td>
            </tr>
            <tr>
                <th style="width:100px;padding-left:10px;" ><label class="star labFmt100">预警类型：</label>
                <td>
                    <select name="alarmThreshold.columnId" id="columnId" class="fleft dialog_select" style="width: 310px">
                        <option value="0" <c:if test="${alarmThreshold.columnId == 0 }">selected="selected" </c:if>>基础</option>
                        <option value="1" <c:if test="${alarmThreshold.columnId == 1 }">selected="selected" </c:if>>环比</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th style="width:100px;padding-left:10px;" ><label class="star labFmt100">预警级别：</label>
                <td>
                    <select name="alarmThreshold.levelId" id="levelId" class="fleft dialog_select" style="width: 310px">
                        <option value="1" <c:if test="${alarmThreshold.levelId == 1 }">selected="selected" </c:if>>高</option>
                        <option value="2" <c:if test="${alarmThreshold.levelId == 2 }">selected="selected" </c:if>>中</option>
                        <option value="3" <c:if test="${alarmThreshold.levelId == 3 }">selected="selected" </c:if>>低</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th  style="width:100px;padding-left:10px;" ><label class="star labFmt100">区间类型：</label>
                <td>
                    <select name="alarmThreshold.thresholdType" id="thresholdType" class="fleft dialog_select" style="width: 310px">
                        <option value="in" <c:if test="${alarmThreshold.thresholdType == 'in' }">selected="selected" </c:if>>大于等于下限且小于等于上限</option>
                        <option value="out" <c:if test="${alarmThreshold.thresholdType == 'out' }">selected="selected" </c:if>>小于等于下限或大于等于上限</option>
                        <option value="more" <c:if test="${alarmThreshold.thresholdType == 'more' }">selected="selected" </c:if>>大于等于上限值</option>
                        <option value="less" <c:if test="${alarmThreshold.thresholdType == 'less' }">selected="selected" </c:if>>小于等于下限值</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th id="name" style="width:100px;padding-left:10px;" ><c:if test="${alarmThreshold.thresholdType != 'less'}"></c:if><label class="star labFmt100">上限值：</label></th>
                <td>
                    <div class="new_tag_selectBox">
                        <c:choose>
                            <c:when test="${alarmThreshold.thresholdType != 'less'}">
                                 <input name="alarmThreshold.maxValue" type="text" id="maxValue"
                               class="new_tag_input" style="width:300px;cursor:text"
                               value="<fmt:formatNumber value="${alarmThreshold.maxValue}" type="number" pattern="##0.##"/>"/>
                            </c:when>
                            <c:otherwise>
                                <input name="alarmThreshold.maxValue" type="text" id="maxValue"
                               class="new_tag_input" style="width:300px;cursor:text"
                               value="<fmt:formatNumber value="" type="number" pattern="##0.##"/>"/>
                            </c:otherwise>
                        </c:choose>
                       	<div id="maxValueDiv"></div>
                    	<div class="tishi error" style="display:none;"></div>
                    </div>
                </td>
            </tr>
            <tr>
                <th style="width:100px;padding-left:10px;" ><c:if test="${alarmThreshold.thresholdType != 'more'}"></c:if><label class="star labFmt100">下限值：</label></th>
                <td>
                    <div class="new_tag_selectBox">
                    <c:choose>
                            <c:when test="${alarmThreshold.thresholdType != 'more'}">
                              <input name="alarmThreshold.minValue" type="text"   id="minValue"
                               class="new_tag_input" style="width:300px;cursor:text"
                               value="<fmt:formatNumber value="${alarmThreshold.minValue}" type="number" pattern="##0.##"/>"/>
                            </c:when>
                            <c:otherwise>
                               <input name="alarmThreshold.minValue" type="text"   id="minValue"
                               class="new_tag_input" style="width:300px;cursor:text"
                               value="<fmt:formatNumber value="" type="number" pattern="##0.##"/>"/>
                            </c:otherwise>
                        </c:choose>
                     <div id="minValueDiv"></div>
                     <div class="tishi error" style="display:none;"></div>
                        </div>
                </td>
            </tr>

        </table>
        <div class="dialog_btn_div" style="margin-top:8px;">
            <button type="button" value="ok" id="confirmBtn" class="tag_btn" onclick="${processFun}"><c:out
                    value="${saveBtn}"/></button>
        </div>
    </form>
    <!-- 一组表单 end --></div>
<div id="menuContent" class="x-form-field menuContent" style="position: absolute;display:none;width:308px">
    <ul id="treeDemo" class="ztree" style="margin-top:0;  height: 280px;"></ul>
</div>
</body>
</html>