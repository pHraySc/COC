<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>新增意见反馈</title>
<link href="${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/new/uploadify.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var feedbackType = '${param["feedbackType"]}';
		//添加的附件标号
		var attachmentSpanId = 0;
		//查询所有有效标签
       	createLabelTree();
		//选择反馈类型
		selectFeedbackType(feedbackType);
		$("input[name='feedbackInfo.feedbackType']").change(function() { 
			selectFeedbackType(feedbackType);
		}); 
		
		$("#labelName").focusblur();
		$("#customerGroupName").focusblur();
		$("#feedbackTitle").focusblur();
		$("#feedbackInfo").focusblur();
		
		$("#update-file-btn").click(function(){
			var height=$(window.parent.document.body).find("#addFeedbackDialog_iframe").height();
			$(window.parent.document.body).find("#addFeedbackDialog_iframe").height(height+130);
			$("#update-file-box").show();
			$("#update-file-btn").hide();
		});
		
		$("#tableTypeBox div").bind("click", function() {
			var _this = $(this);
			var _value = _this.attr("value");
			var _id = _this.attr("id");
			$('#tableTypeId').val(_value);
			$('#tableTypeBox div').removeClass();
			_this.addClass('current');
			if(_id == "createUser"){
				$("#replayUserType").val(true);
			} else {
				$("#replayUserType").val(false);
			}
		});
		
		$("#feedbackTitle").blur(function () {
	 			$("#titleTip").empty();
	 			$("#titleTip").hide();
 		});
		$("#label").blur(function () {
 			$("#labelNameTip").empty();
 			$("#labelNameTip").hide();
		});
		$("#busiCustomName").blur(function () {
 			$("#customNameTip").empty();
 			$("#customNameTip").hide();
		});
		$("#feedbackInfo").blur(function () {
			$("#feedbackInfoTip").empty();
			$("#feedbackInfoTip").hide();
		});
	});

	//切换反馈类型
	function selectFeedbackType(feedbackType) {
		var div_height=document.getElementById("update-file-box").offsetHeight;
		if(1==$("input[name='feedbackInfo.feedbackType']:checked").val()){
			$(window.parent.document.body).find("#addFeedbackDialog_iframe").height(div_height+310);
			$("#customerGroup").hide();
			$("#label").show();
			$("#selectReply").hide();
			$("#busiLabelId").multiselect({
	            multiple: false,
	            closeable:false,
	            noneSelectedText: "请选择标签",
	            selectedList: 1
	        }).multiselectfilter({placeholder: '请输入关键字'}); 
            $("#busiLabelId").bind("change",function(){
            	$("#busiLabelId").multiselect({selectedText:$(this).find("option:selected").text()});
                $("#busiLabelName").val($(this).find("option:selected").text());
                $("#labelCustomId").val($("#busiLabelId").val());
            })
		} else if (2==$("input[name='feedbackInfo.feedbackType']:checked").val()) {
			$(window.parent.document.body).find("#addFeedbackDialog_iframe").height(div_height+310);
			$("#label").hide();
			$("#customerGroup").show();
			$("#selectReply").show();
			$("#replayUserType").val(true);
			//查询公有客户群
	     	createCustomGroupTree();
			
			$("#busiCustomId").multiselect({
	            multiple: false,
	            closeable:false,
	            noneSelectedText: "请选择客户群",
	            selectedList: 1
	        }).multiselectfilter({placeholder: '请输入关键字'}); 
            $("#busiCustomId").bind("change",function(){
            	$("#busiCustomId").multiselect({selectedText:$(this).find("option:selected").text()});
                $("#busiCustomName").val($(this).find("option:selected").text());
                $("#labelCustomId").val($("#busiCustomId").val());
            })
		} else if (3==$("input[name='feedbackInfo.feedbackType']:checked").val()) {
			$(window.parent.document.body).find("#addFeedbackDialog_iframe").height(div_height+225);
			$("#label").hide();
			$("#customerGroup").hide();
			$("#selectReply").hide();
		}
	}
	
	//查询标签，以做复制
	 function createLabelTree(){
        var actionUrl = $.ctx + "/ci/feedback/ciFeedbackAction!findAllEffectiveLabelList.ai2do";
        $.ajax({
            type: "POST",
            url: actionUrl,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var busiIdSelect = $("#busiLabelId");
                    busiIdSelect.html("");
                    busiIdSelect.append('<option value="">请选择标签</option>');
                    $.each(result.labelList,function(i,obj){
                        var option = '<option value="'+obj.labelId+'" userId="'+obj.createUserId+'">'+obj.labelName+'</option>';
                        busiIdSelect.append(option);
                    });
                    busiIdSelect.multiselect('refresh');
                }
            }
        });
    }
	//查询客户群，以做复制
 	function createCustomGroupTree(){
        var actionUrl = $.ctx + "/ci/feedback/ciFeedbackAction!findCustomerGroupTree.ai2do";
        $.ajax({
            type: "POST",
            url: actionUrl,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var busiCustomIdSelect = $("#busiCustomId");
                    busiCustomIdSelect.html("");
                    busiCustomIdSelect.append('<option value="">请选择客户群</option>'); 
                    $.each(result.treeList,function(i,obj){
                        var option = '<option value="'+obj['id']+'">'+obj['name']+'</option>';
                        busiCustomIdSelect.append(option);
                    });
                    busiCustomIdSelect.multiselect('refresh');
                }
            }
        });
    }
	
 	//删除附件
	function delAttachment(div,attmentSpanId,fileId) {
 		var swfu = $('#file_upload_dark').data('uploadify');
        var stats = swfu.getStats();
        stats.successful_uploads--;
        swfu.setStats(stats);
		$('#file_upload_dark').uploadify('cancel', fileId);
		div.parentNode.parentNode.removeChild(div.parentNode);
		var span = document.getElementById(attmentSpanId);
		$("#"+attmentSpanId).remove();
	}
 	
 	//整理上传的附件信息，以便将信息传给后台存入数据库
	function arrangeAttachmentList(){
		var html="";
		$("#attachmentList span").each(function(i, element){
			var ele = $(element);
			var fileName		= ele.attr('fileName');
			var fileOldName 	= ele.attr('fileOldName');
			var fileType 		= ele.attr('fileType');
			var fileUrl 		= ele.attr('fileUrl');
			var fileStartTime 		= ele.attr('fileStartTime');
			html+='<input type="hidden" name="attachmentFileList[' + i + '].fileName" value="' + fileName + '"/>' + 
				  '<input type="hidden" name="attachmentFileList[' + i + '].fileOldName" value="' + fileOldName + '"/>' + 
				  '<input type="hidden" name="attachmentFileList[' + i + '].fileType" value="' + fileType + '"/>' + 
				  '<input type="hidden" name="attachmentFileList[' + i + '].fileUrl" value="' + fileUrl + '"/>';
		});
		$("#attachmentListArranged").empty();
		$("#attachmentListArranged").append(html);
	}
 	
	//保存反馈信息
	function save() {
		var actionUrl = $.ctx + "/ci/feedback/ciFeedbackAction!saveFeedback.ai2do";
		if (validateForm())　{
			arrangeAttachmentList();
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: $("#saveForm").serialize(),
				success: function(result){
					if (result.success) {
						$("#attachmentList").empty();
						window.parent.openSuccessForSave(result.msg);
					}	else {
						openHasSameFeedback(result.msg);
					}
				}
			});
		}
	}
	
	function validateForm(){
		var feedbackTitle = $.trim($("#feedbackTitle").val());
	 	if ("请输入反馈标题" == feedbackTitle) {
	 		$("#feedbackTitle").val("");
		} else {
			$("#feedbackTitle").val(feedbackTitle);
		}
		
		if(1==$("input[name='feedbackInfo.feedbackType']:checked").val()){
			var busiLabelNameFlag = !!($("#busiLabelName").val() == "");
			if(busiLabelNameFlag){
				$("#labelNameTip").empty();
				$("#labelNameTip").show().append("请选择标签！");
				return false;
			}
		}
		if(2==$("input[name='feedbackInfo.feedbackType']:checked").val()){
			var busiLabelNameFlag = !!($("#busiCustomName").val() == "");
			if(busiLabelNameFlag){
				$("#customNameTip").empty();
				$("#customNameTip").show().append("请选择客户群！");
				return false;
			}
		}
		var feedbackTitleFlag = !!($("#feedbackTitle").val() == "");
		if(feedbackTitleFlag){
			$("#titleTip").empty();
			$("#titleTip").show().append("反馈主题不能为空！");
			return false;
		}
		
		var feedbackInfo = !!($("#feedbackInfo").val() == "");
		if(feedbackInfo) {
			$("#feedbackInfoTip").empty();
			$("#feedbackInfoTip").show().append("反馈描述不能为空！");
			return false;
		}
		return true;
	}
	
	function openHasSameFeedback(msg) {
		commonUtil.create_alert_dialog("hasSameFeedback", {
			 "txt":msg,
			 "type":"failed",
			 "width":500,
			 "height":200
		}); 
	}  
</script>
</head>

<body>
	<form id="saveForm" method="post">
		<input type="hidden" id="feedbackInfoId"
			name="feedbackInfo.feedbackInfoId" />
		<div id="attachmentList" class="hidden"></div>
		<div id="attachmentListArranged" class="hidden"></div>
		<div class="clientMsgBox pt30">
			<ol class="clearfix">
				<li><label class="fleft labFmt100 star">反馈类型：</label>
					<div class="fleft">
						<input type="radio" name="feedbackInfo.feedbackType" value="1" checked="checked" />&nbsp;标签&nbsp;&nbsp;&nbsp;
						<input type="radio" name="feedbackInfo.feedbackType" value="2"/>&nbsp;客户群&nbsp;&nbsp;&nbsp;
						<input type="radio" name="feedbackInfo.feedbackType" value="3"/>&nbsp;系统功能
					</div>
				</li>
				<li id="label">
	                <label class="fleft labFmt100 star">标签名称：</label> 
	                 <!-- 提示 -->
	                <div style="float:left;">
	                   <select id="busiLabelId"  class="dialog_select" style="width: 310px;">
	                       <option value="">请选择标签</option>
	                   </select>
	                </div>
	                <input id="busiLabelName" name="feedbackInfo.labelName" type="hidden" value=""/> 
	                <div id="labelNameTip" class="tishi error" style="display:none;"></div>
                </li>
                <li id="customerGroup">
	                <label class="fleft labFmt100 star">客户群：</label> 
	                 <!-- 提示 -->
	                <div style="float:left;">
                        <select id="busiCustomId"  class="dialog_select" style="width: 310px;">
                            <option value="">请选择客户群</option>
                        </select>
                    </div>
                    <input id="busiCustomName" name="feedbackInfo.customGroupName" type="hidden" value=""/> 
	                <div id="customNameTip" class="tishi error" style="display:none;"></div>
                </li>
				<li id="selectReply" class="clearfix">
					<label class="fleft labFmt100 star">回复人选择：</label> 
					<div id="tableTypeBox" class="fleft createPeriodBox">
					   <input id="replayUserType" name="feedbackInfo.isCreateUser" type="hidden" value="" /> 
					   <div id="createUser" class="current">创建人</div> 
					   <div id="admin" >管理员</div>
					</div>
			 	</li>
				<li>
					<label class="fleft labFmt100 star">反馈主题：</label> 
					<input id="feedbackTitle" name="feedbackInfo.feedbackTitle"
						type="text" class="fleft inputFmt305" maxlength="40"
						value="请输入反馈标题" />
					<div id="titleTip" class="tishi error" style="display:none;"></div>
				</li>
				<li>
					<label class="fleft labFmt100 star">反馈描述：</label> 
						<textarea class="fleft textarea456" name="feedbackInfo.feedbackInfo" id="feedbackInfo"
							onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}"
							onblur="this.value = this.value.slice(0,341)" ></textarea>
					<div class="sendBox ">
						<a class="update-file-btn fright" id="update-file-btn"
							href="javascript:void(0);" title="附件" action-type="plugin">
							添加附件 </a>
					</div>
					<div id="feedbackInfoTip" class="tishi error" style="display:none;"></div>
				</li>
			</ol>
			<input id="labelCustomId" name="feedbackInfo.labelCustomId" type="hidden"/> 
		</div>
		<div class="upload-box hidden" id="update-file-box">
			<div class="fright upload-box-btn">
				<input type="file" id="file_upload_dark" name="files" />
			</div>
			<div class="upload_success clearfix"></div>
		</div>
		<!-- 按钮区  -->
		<div class="btnActiveBox">
			<a href="javascript:void(0);" id="saveBtm" name="saveBtm"
				class="ensureBtn" onclick="save()">提交</a>
		</div>
	</form>
</body>
</html>