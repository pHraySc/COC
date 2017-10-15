<%@ taglib prefix="suite" uri="http://tag.pagecomponent.biframe.asiainfo.com/suite" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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


    <title>标签预警<c:out value="${title}"/></title>
    <%@ include file="/aibi_ci/html_include.jsp" %>
    <script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
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
            	var labelId = '${param["labelId"]}';
            	//if：是从我创建的标签列表中新增预警
            	if(labelId != null && labelId != "") {
            		var labelName = decodeURIComponent('${param["labelName"]}');
            		$("#busiId").val(labelId);
            		$("#busiName").val(labelName);
                }else{
                    if(${isAdmin==false}){
                         $("#busiName").multiselect({
                             multiple: false,
                             closeable:false,
                           selectedList: 1
                         }).multiselectfilter(); 
                         $("#busiName").bind("change",{id:"busiName"},function(){
                             noneSelectedText: "请选择标签名称",
                                     $("#busiName_readOnly").val($(this).find("option:selected").text());
                             $("#busiId").val($(this).val());
                             var labelLevel = $(this).find("option:selected").attr("labelLevel");
                             thresholdTypeCon(labelLevel);
                         });
                    }
                	createTree();
                	if(${isAdmin==true}){
                		$("#busiName").val("请选择标签名称");
                        $("#busiName").bind("click",{id:"busiName"},showMenu);
                    }
                }   
                $("#maxValue").val("请输入上限值");
                $("#minValue").val("请输入下限值");
                $("#maxValue").bind("click",function(){
                    if($(this).val() == "请输入上限值"){
                        $(this).val("");
                    }
                });
                $("#minValue").bind("click",function(){
                    if($(this).val() == "请输入下限值"){
                        $(this).val("");
                    }
                });
            }
            
            $("#thresholdType").bind("change",function(){
                var thresholdType = $(this).val();
                if("in" == thresholdType || "out" == thresholdType){
                    $("#maxValueRequired").html("*");
                    $("#minValueRequired").html("*");
                    if($("#maxValue").val()=="")
                    	$("#maxValue").val("请输入上限值");
                    if($("#minValue").val()=="")
                    	$("#minValue").val("请输入下限值");
                }
                if("more" == thresholdType){
                    $("#maxValueRequired").html("*");
                    $("#minValueRequired").html("");
                    $("#minValue").val("");
                    if("${isAdd}" == "true"||("${isAdd}" == "false"&&$("#maxValue").val()==""))
                    	$("#maxValue").val("请输入上限值");
                }
                if("less" == thresholdType){
                    $("#maxValueRequired").html("");
                    $("#minValueRequired").html("*");
                    $("#maxValue").val("");
                    if("${isAdd}" == "true"||("${isAdd}" == "false"&&$("#minValue").val()==""))
                    	$("#minValue").val("请输入下限值");
                }
            });
            
            //第一次修改max、min value时候删除默认值
            /**$("#maxValue,#minValue").focus(function(){
            	if(!$(this).hasClass("changed")){
            		$(this).attr("value","").addClass("changed");
            	}
            })**/

        });

        //控制预警类型
        function thresholdTypeCon(level){
       	 //标签级别
           if(level=="3"){
           	$("#columnId option[value='2']").remove(); 
           	$("#columnId").multiselect('refresh');
           }else{
               if($("#columnId option[value='2']").length==0){
                   var option="<option value='2' <c:if test='${alarmThreshold.columnId == 2 }'>selected='selected' </c:if>>占比</option>";
                   $("#columnId").append(option);
                   $("#columnId").multiselect('refresh');
               }
           }
       }
        function createTree(){
            if(${isAdmin==true}){
            	var curMenu = null, zTree_Menu = null;
                var setting = {
                    view: {
                        showLine: true,
                        selectedMulti: false,
                        dblClickExpand: false
                    },
                    data: {
                    	key: {
                			title: "tip"
                		},
                        simpleData: {
                            enable: true
                        }
                    },
                    callback: {
                        onClick: zTreeOnClick
                    }
                };
                function treeSearch(){
                    var searchValue = $("#treeSearchInput").val();
                	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTreeByName.ai2do";
                	$.ajax({
            			type: "POST",
            			url: actionUrl,
            			async: false,
            			data: ({"labelName" : searchValue}),
            			dataType: "json",
            			success: function(result){
            				if(result.success){
            					zNodes = result.treeList;

            					//初始化第一级node的样式
                        		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
                        		zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
                        		if(zTree_Menu.getNodes().length>0){
                        			var nodes_0=zTree_Menu.getNodes()[0];
                            		var curMenu = nodes_0;
                            		if(!curMenu){
                            			return;
                            		}
                            		while(curMenu.children&&curMenu.children.length > 0){
                            			curMenu = curMenu.children[0];
                            		}
                            		zTree_Menu.selectNode(curMenu);
                            	}
            					$("#treeDemo").prepend("<li class='tree_analyse_search'><div class='analyse_search'  style='width:280px;'><input type='text' id='treeSearchInput' style='width:235px;'/><span id='treeSearchButton' class='icon_search'></span></div></li>")
                                $("#treeSearchInput").val(searchValue);
                                $("#treeSearchButton").bind('click',treeSearch);
            				}else{
            					showAlert(result.message,"failed");
            				}
            			}
            		});
                }
                var zNodes = "";
                var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            zNodes = result.treeList;
                            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                            //添加一个搜索框
                            $("#treeDemo").prepend("<li class='tree_analyse_search'><div class='analyse_search' style='width:280px;'><input type='text' id='treeSearchInput' style='width:235px;'/><span id='treeSearchButton' class='icon_search'></span></div></li>")
                            $("#treeSearchButton").bind('click',treeSearch);
                        } else {
                        	showAlert(result.msg,"failed");
                        }
                    }
                });
            }else{
            	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findMyLabelTree.ai2do";
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            var busiIdSelect = $("#busiName");
                            $.each(result.treeList,function(i,obj){
                                var labelLevel = "0";
                                if(obj['param']!=null){
                                    if(obj['param'].ciLabelExtInfo!=null){
                                        labelLevel = obj['param'].ciLabelExtInfo.labelLevel;
                                    }
                                }
                                var option = '<option value="'+obj['id']+'" labelLevel="'+labelLevel+'">'+obj['name']+'</option>';
                                busiIdSelect.append(option);
                            });
                            busiIdSelect.multiselect('refresh');
                        } else {
                        	showAlert(result.msg,"failed");
                        }
                    }
                });
            }
        }

        function zTreeOnClick(event, treeId, treeNode) {
        	var canDrag = 0;
			if(treeNode.param.isCanDrag != null || treeNode.param.isCanDrag != undefined){
				canDrag = treeNode.param.isCanDrag;
			}
			if(canDrag == 0){
				return;
			}
            if(treeNode.level >= 1){
                $("#busiName").val(treeNode.name);
                $("#busiId").val(treeNode.id);
                hideMenu();
            }
            thresholdTypeCon(treeNode.param.labelLevel);
        }

        //校验表单
        function validateForm() {
            var busiName = $.trim($("#busiName").val());
            if (busiName == "请选择标签名称") {
                $("#busiName").val("");
            }
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
                    "alarmThreshold.busiName": {
                        required: true
                    },
                    "alarmThreshold.maxValue":{
                        number:true
                    },
                    "alarmThreshold.minValue":{
                        number:true
                    }
                },
                messages: {
                    "alarmThreshold.busiName": {
                        required: "标签名称不允许为空!"
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
                    element.parent("div").next("div").hide();
                    element.parent("div").next("div").after(error);
                },
                success: function (element) {
                    element.prev("div").show();
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
                $("#maxValueRequired").html("*");
                $("#minValueRequired").html("*");
            }
            if("more" == thresholdType){
                $("#maxValue").rules("remove","required");
                $("#minValue").rules("remove","required");
                $("#maxValue").rules("add",{required:true});
                $("#maxValueRequired").html("*");
                $("#minValueRequired").html("");
            }
            if("less" == thresholdType){
                $("#maxValue").rules("remove","required");
                $("#minValue").rules("remove","required");
                $("#minValue").rules("add",{required:true});
                $("#maxValueRequired").html("");
                $("#minValueRequired").html("*");
            }

            var busiIdIsValid =true,otherIsValid=true;
            if (!validater.form()) {
                otherIsValid =  false;
            }
            if(${isAdmin==false}){
            	if($("#busiName").val()==""){
                    $("#nameTip").hide();
                    $("#nameTip").next().empty();
                    $("#nameTip").next().show().append("标签名称不允许为空!");
                    busiIdIsValid = false;
                }
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
                if(maxValue != "" && !isNumber(maxValue)){
                    $("#maxValueDiv").hide();
                    $("#maxValueDiv").next().empty();
                    $("#maxValueDiv").next().show().append("基础预警的上限值应该是正整数!");
                    isSuccess = false;
                }
                if(minValue != "" && !isNumber(minValue)){
                    $("#minValueDiv").hide();
                    $("#minValueDiv").next().empty();
                    $("#minValueDiv").next().show().append("基础预警的下限值应该是正整数!");
                    isSuccess = false;
                }
            }

            if(maxValue != "" && minValue != "" && Number(maxValue) < Number(minValue)){
                $("#maxValueDiv").hide();
                $("#maxValueDiv").next().empty();
                $("#maxValueDiv").next().show().append("上限值应该大于下限值!");
                isSuccess =  false;
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
            if(validateForm()){
            	var labelId = '${param["labelId"]}';
                var actionUrl = $.ctx + '/ci/ciLabelAlarmAction!addThreshold.ai2do?labelId=' + labelId;
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    data: $("#saveForm").serialize(),
                    success: function (result) {
                        if (result.success) {
                            parent.showAlert("保存成功！","success",parent.callBack);
                        } else {
                            if (result.errorType == "1") {
                                $("#nameTip").hide();
                                $("#nameTip").next().empty();
                                $("#nameTip").next().show().append("标签预警在相同条件(预警类型和预警级别相同)下已经存在!");
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
                var actionUrl = $.ctx + '/ci/ciLabelAlarmAction!modifyThreshold.ai2do?';
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    data: $("#saveForm").serialize(),
                    success: function (result) {
                        if (result.success) {
                            parent.showAlert("修改成功！","success",parent.callBack);
                        } else {
                            if (result.errorType == "1") {
                                $("#nameTip").hide();
                                $("#nameTip").next().empty();
                                $("#nameTip").next().show().append("标签预警在相同条件(预警类型和预警级别相同)下已经存在!");
                            } else {
                            	parent.showAlert("修改失败！","failed");
                            }
                        }
                    }
                });
            }
        }


        function showMenu(event) {
            var id = event.data.id;
            var cityObj = $("#" + id);
            var cityOffset = $("#" + id).offset();
            $("#menuContent").css({left: cityOffset.left + "px", top: cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

            $(document).bind("mousedown", onBodyDown);
        }

        function onBodyDown(event) {
            if (!(event.target.id == "busiName" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
                hideMenu();
            }
        }

        function hideMenu() {
            $("#menuContent").fadeOut("fast");
            $(document).unbind("mousedown", onBodyDown);
        }

    </script>
    <style>
        .ui-dialog .ui-dialog-content {
            overflow: hidden
        }
        /*第一层节点切换图标被main.css隐藏,显示出该图标*/
        .ztree li span.button.switch.level0 {display:inline-block; *display:inline; zoom:1;}
		.ztree li.level0 span.level0 {display:inline-block; *display:inline; zoom:1;}
         .x-form-field li:hover, .x-form-field li.odd:hover {
            background: white;
        }
        li.tree_analyse_search{background:#e0eef9;padding-top:6px;padding-bottom:6px;}
        li.tree_analyse_search .analyse_search{width:280px;background:none}
		li.tree_analyse_search .analyse_search input{width:250px; background:#fff;border:#c9c9c9 solid 1px;}
		.x-form-field li.tree_analyse_search:hover,li.tree_analyse_search .ui-multiselect-checkboxes li.odd .ui-state-hover{background:#e0eef9;}
        .ui-multiselect-hasfilter ul { position:relative; top:2px }
        .ui-multiselect-filter {background:#e0eef9;padding-top:6px;padding-bottom:6px;float:left;margin-left:6px;font-size:12px; margin-right:6px;}
        .ui-multiselect-filter input { width:243px; background:#fff;border:#c9c9c9 solid 1px; font-size:12px;margin-right:13px; margin-left:13px; height:10px; padding:5px; }
    </style>
</head>
<body>

<div class="dialog_table dialog_normal"><!-- 一组表单 start -->
    <form id="saveForm">
        <input id="busiId" name="alarmThreshold.busiId" type="hidden" value="${alarmThreshold.busiId}">
        <input name="alarmThreshold.thresholdId" id="thresholdId" type="hidden" value="${alarmThreshold.thresholdId}">
        <input name="alarmThreshold.refId" id="refId" type="hidden" value="${alarmThreshold.refId}">
        <input name="alarmThreshold.refType" id="refType" type="hidden" value="${alarmThreshold.refType}">
        <table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
            <tr>
                <th width="110px"><span class="aibi_font_red">*</span>标签：</th>
                <td>
                    <div class="new_tag_selectBox">
                    <c:if test="${param['labelId'] == null&&isAdmin==true}">
                        <input id="busiName" name="alarmThreshold.busiName" type="text"
                               class="new_tag_input" style="width:300px;cursor:text" value="${alarmThreshold.busiName}"
                               readonly="readonly"/>
                    </c:if>
                    <c:if test="${param['labelId'] == null&&isAdmin==false}">
                        <c:if test="${isAdd == true}">
                            <select name="alarmThreshold.busiName1" id="busiName" class="dialog_select" >
                                <option value="">请选择标签名称</option>
                            </select>
                            <input id="busiName_readOnly" name="alarmThreshold.busiName" type="hidden" value="">
                        </c:if>
                        <c:if test="${isAdd == false}">
                            <input id="busiName" name="alarmThreshold.busiName" type="hidden" value="${alarmThreshold.busiName}">
                            <input id="busiName_readOnly" name="alarmThreshold.busiName_readOnly" type="text" readonly="readonly" class="new_tag_input" style="width:300px;" value="${alarmThreshold.busiName}">
                        </c:if>
                    </c:if>
                    <c:if test="${param['labelId'] != null }">
                    	<input id="busiName" name="alarmThreshold.busiName" type="text"
                               class="new_tag_input" style="width:300px;cursor:text" value="${alarmThreshold.busiName}"
                               readonly="readonly"/>
                    </c:if>
                    </div>
                    <!-- 提示 -->
                    <div id="nameTip"></div>
                    <div class="tishi error" style="display:none;"></div>
                </td>

            </tr>
            <tr>
                <th><span class="aibi_font_red">*</span>预警类型：</th>
                <td>
                    <select name="alarmThreshold.columnId" id="columnId" class="dialog_select">
                        <option value="0" <c:if test="${alarmThreshold.columnId == 0 }">selected="selected" </c:if>>基础</option>
                        <option value="1" <c:if test="${alarmThreshold.columnId == 1 }">selected="selected" </c:if>>环比</option>
                        <c:if test="${isAdd==true||(ciLabelInfo!=null&&ciLabelInfo.ciLabelExtInfo!=null&&ciLabelInfo.ciLabelExtInfo.labelLevel!='3')}">
                            <option value="2" <c:if test="${alarmThreshold.columnId == 2 }">selected="selected" </c:if>>占比</option>
                        </c:if>
                    </select>
                    <div class="tishi error" style="display:none;"></div>
                </td>
            </tr>
            <tr>
                <th scope="row"><span class="aibi_font_red">*</span>预警级别：</th>
                <td>
                    <select name="alarmThreshold.levelId" id="levelId" class="dialog_select">
                        <option value="1" <c:if test="${alarmThreshold.levelId == 1 }">selected="selected" </c:if>>高</option>
                        <option value="2" <c:if test="${alarmThreshold.levelId == 2 }">selected="selected" </c:if>>中</option>
                        <option value="3" <c:if test="${alarmThreshold.levelId == 3 }">selected="selected" </c:if>>低</option>
                    </select>

                </td>
            </tr>
            <tr>
                <th><span class="aibi_font_red">*</span>区间类型：</th>
                <td>
                    <select name="alarmThreshold.thresholdType" id="thresholdType" class="dialog_select">
                        <option value="in" <c:if test="${alarmThreshold.thresholdType == 'in' }">selected="selected" </c:if>>大于等于下限且小于等于上限</option>
                        <option value="out" <c:if test="${alarmThreshold.thresholdType == 'out' }">selected="selected" </c:if>>小于等于下限或大于等于上限</option>
                        <option value="more" <c:if test="${alarmThreshold.thresholdType == 'more' }">selected="selected" </c:if>>大于等于上限值</option>
                        <option value="less" <c:if test="${alarmThreshold.thresholdType == 'less' }">selected="selected" </c:if>>小于等于下限值</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th><span class="aibi_font_red" id="maxValueRequired"><c:if test="${alarmThreshold.thresholdType != 'less'}">*</c:if></span>上限值：</th>
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
                    </div>
                    <div id="maxValueDiv" ></div>
                    <div class="tishi error" style="display:none;"></div>
                </td>
            </tr>
            <tr>
                <th><span class="aibi_font_red" id="minValueRequired"><c:if test="${alarmThreshold.thresholdType != 'more'}">*</c:if></span>下限值：</th>
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
                    
                        </div>
                    <div id="minValueDiv" ></div>
                    <div class="tishi error" style="display:none;"></div>
                </td>
            </tr>

        </table>
        <div class="dialog_btn_div">
            <button type="button" value="ok" id="confirmBtn" class="tag_btn" onclick="${processFun}"><c:out
                    value="${saveBtn}"/></button>
        </div>
    </form>
    <!-- 一组表单 end --></div>
<div id="menuContent" class="x-form-field menuContent" style="position: absolute;display:none;width:308px">
    <ul id="treeDemo" class="ztree" style="margin-top:0;  height: 280px;overflow-y: auto;"></ul>
</div>
</body>
</html>