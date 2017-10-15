<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8" %>
<%@ taglib prefix="suite" uri="http://tag.pagecomponent.biframe.asiainfo.com/suite" %>
<%@ include file="/aibi_ci/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/aibi_ci/html_include.jsp" %>
    <title>系统公告<c:out value="${title}"/></title>
   	<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
   	<c:if test="${empty sysAnnouncement.announcementId}">
        <c:set var="title" value="添加"/>
        <c:set var="saveBtn" value="保存"/>
        <c:set var="processFun" value="saveSysAnnouncement()"/>
        <c:set var="isAdd" value="true"/>
    </c:if>
    <c:if test="${! empty sysAnnouncement.announcementId}">
        <c:set var="title" value="修改"/>
        <c:set var="saveBtn" value="修改"/>
        <c:set var="processFun" value="modifySysAnnouncement()"/>
        <c:set var="isAdd" value="false"/>
    </c:if>
    <script type="text/javascript">
        $(document).ready(function () {

            if("${isAdd}" == "true"){
                $("#announcementName").val("请输入系统公告标题");
                $.focusblur("#announcementName");

            }
            //修改页面
            var priorityId = "${sysAnnouncement.priorityId}";
            if(priorityId && priorityId != ""){
                if(priorityId == "0"){
                	$("#priorityIdBox div").removeClass();
                	$("#highId").addClass("current");
				}
                if(priorityId == "1"){
                	$("#priorityIdBox div").removeClass();
                	$("#normalId").addClass("current");
				}
            }
            
			$("#priorityIdBox div").click(function(){

				var index = $(this).index();
				$("#priorityIdBox div").removeClass();
				if(index == 1){
					//优先级：高
					$("#priorityId").val(0);
				}else 
      			if(index == 2){
          			//优先级：普通
      			  $("#priorityId").val(1);
				} 
			$(this).addClass("current");
			});
		});

        //校验表单
        function validateForm() {
            var announcementName = $.trim($("#announcementName").val());
            if (announcementName == "请输入系统公告标题") {
                $("#announcementName").val("");
            }
            var priorityId = $.trim($("#priorityId").val());
            if(priorityId == "" || priorityId == null){
                //默认为普通
            	$("#priorityId").val("1");
			}
            var validater = $("#saveForm").validate({
                rules: {
                    "sysAnnouncement.announcementName": {
                        required: true
                    },
                    "sysAnnouncement.announcementDetail":{
                        required: true
                    },
                    "sysAnnouncement.effectiveTime":{
                        required: true
                    }
                },
                messages: {
                    "sysAnnouncement.announcementName": {
                        required: "标题不允许为空!"
                    },
                    "sysAnnouncement.announcementDetail":{
                        required: "内容不允许为空!"
                    },
                    "sysAnnouncement.effectiveTime":{
                        required: "有效截止期不允许为空!"
                    }
                },
                errorPlacement: function (error, element) {
                	element.next("div").hide();
        			element.after(error);
                    //element.parent("div").next("div").hide();
                    //element.parent("div").next("div").after(error);
                },
                success: function (element) {
                    //element.prev("div").show();
                    element.remove();
                },
                errorElement: "div",
                errorClass: "tishi error"
            });
            if (!validater.form()) {
                return false;
            }
            return true;
        }

        //保存系统公告
        function saveSysAnnouncement() {
            if(validateForm()){
                var actionUrl = $.ctx + '/ci/ciSysAnnouncementAction!save.ai2do?';
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    data: $("#saveForm").serialize(),
                    success: function (result) {
                        if (result.success) {
                            //parent.showAlert("发布成功！","success");
                          //弱提示
	                        parent.closeDialog("#dialog_edit");
	                        parent.$.fn.weakTip({"tipTxt":"发布成功！"});
		                    parent.refreshList_reName();
                        } else {
                            if (result.errorType == "1") {
                                $("#nameTip").hide();
                                $("#nameTip").empty();
                                $("#nameTip").show().append("系统公告在库中已经存在!");
                            } else {
                            	parent.showAlert("保存失败！","failed");
                            }
                        }
                    }
                });
            }
        }

        //修改系统公告
        function modifySysAnnouncement() {
            if(validateForm()){
                var actionUrl = $.ctx + '/ci/ciSysAnnouncementAction!modify.ai2do?';
                $.ajax({
                    type: "POST",
                    url: actionUrl,
                    data: $("#saveForm").serialize(),
                    success: function (result) {
                        if (result.success) {
                        	//弱提示
	                        parent.closeDialog("#dialog_edit");
	                        parent.$.fn.weakTip({"tipTxt":"修改成功！"});
	                        parent.refreshList_reName();
                        } else {
                            if (result.errorType == "1") {
                                $("#nameTip").hide();
                                $("#nameTip").empty();
                                $("#nameTip").show().append("系统公告在库中已经存在!");
                            } else {
                            	parent.showAlert("修改失败！","failed");
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
    <style>
        .clientMsgBox ol li{
        	margin-bottom:20px;
        }
        /*第一层节点切换图标被main.css隐藏,显示出该图标*/
        .ztree li span.button.switch.level0 {display:inline-block;}
    </style>
</head>
<body>
<form id="saveForm">
<input name="sysAnnouncement.announcementId" id="announcementId" type="hidden" value="${sysAnnouncement.announcementId}">
<div class="clientMsgBox" >
	<ol class="clearfix">
		<li>
   			<label  class="fleft labFmt100 star">标题：</label>
   			<input name="sysAnnouncement.announcementName" type="text" id="announcementName"
                               class="fleft inputFmt305" style="cursor:text"
                               value="${sysAnnouncement.announcementName}"/>
            <div id="nameTip"  class="tishi error" style="display:none;"></div>
 		</li>
	 	<li>
   			<label  class="fleft labFmt100 star">内容：</label>
   			<textarea class="fleft textarea456" name="sysAnnouncement.announcementDetail" id="announcementDetail">${sysAnnouncement.announcementDetail}</textarea>
            <div class="tishi error" style="display:none;"></div>
 		</li>
  		<li>
   			<label class="fleft labFmt100 star">公告类型：</label>
		   	<span class="fleft"> 
		   	<select name="sysAnnouncement.typeId" id="typeId" class="fleft dialog_select">
		   	<c:forEach var="dimAnnouncementType" items="${dimAnnouncementTypeList}">
		   		<c:choose>
		   			<c:when test="${dimAnnouncementType.typeId == sysAnnouncement.typeId}">
						<option value="${dimAnnouncementType.typeId }" selected="selected">${dimAnnouncementType.typeName }</option>
		   			</c:when>
		   			<c:otherwise>
				   		 <option value="${dimAnnouncementType.typeId }">${dimAnnouncementType.typeName }</option>
		   			</c:otherwise>
		   		</c:choose>
		   	</c:forEach>
		   	</select>
		   	</span>
	   	</li>
		<li>
   			<label class="fleft labFmt100 star">公告优先级：</label>
			<div class="fleft createPeriodBox" id="priorityIdBox">
			   <input type="hidden" id="priorityId" name="sysAnnouncement.priorityId" value="${sysAnnouncement.priorityId }" />
			   <div id="highId">重要</div>
			   <div id="normalId" class="current">普通</div>
			</div>
			<!--<select name="sysAnnouncement.priorityId" id="priorityId" class="fleft dialog_select">
                        <option value="0" <c:if test="${sysAnnouncement.priorityId == 0 }">selected="selected" </c:if>>置顶</option>
                        <option value="1" <c:if test="${sysAnnouncement.priorityId == 1 }">selected="selected" </c:if>>高</option>
                        <option value="2" <c:if test="${sysAnnouncement.priorityId == 2 }">selected="selected" </c:if>>中</option>
                        <option value="3" <c:if test="${sysAnnouncement.priorityId == 3 }">selected="selected" </c:if>>低</option>
           </select>
		--></li>
		<li>
   			<label class="fleft labFmt100 star">有效截止期：</label>
			 <input name="sysAnnouncement.effectiveTime" type="text" id="effectiveTime" readonly="readonly"
                               class="new_tag_input tag_date_input"
                               value="<fmt:formatDate value="${sysAnnouncement.effectiveTime}" type="date"/>" onclick="WdatePicker({minDate:'%y-%M-{%d+1}'})"/>
                    <div class="tishi error" style="display:none;"></div>
		</li>
	</ol>
</div>
 <div class="btnActiveBox">
 <input id="confirmBtn" name="" type="button" value="保存" class="ensureBtn" onclick="${processFun}"/>
</div>
</form>
<!-- 一组表单 end -->
<div id="menuContent" class="x-form-field menuContent" style="position: absolute;display:none;width:308px">
    <ul id="treeDemo" class="ztree" style="margin-top:0;  height: 280px;"></ul>
</div>
</body>
</html>