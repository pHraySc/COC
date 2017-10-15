<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>意见反馈</title>
<link href="${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/new/uploadify.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript"> 
	$(document).ready(function() {
		var attachmentSpanId = 0;
		//控制页面下拉之后navigation.jsp不显示浮动搜索域
		$("#isCreateCustomPage").val("true");
		queryFeedbackRecord();
		judgeUser();
		$("#update-file-btn").click(function(){
			$("#update-file-box").show();
			$("#update-file-btn").hide();
			
		});
	}); 
	//删除显示的上传成功结果
	function showResult(){
        $("#result").html("");
      }
	//判断当前用户是不是反馈回复人
	function judgeUser() {
		var canManageDeclare = $("#canManageDeclare").val();
		if ("true" == canManageDeclare) {
			$("#update-file-btn").hide();
			$("#dealUser").show();
			$("#dealUserConn").show();
		} else {
			$("#update-file-btn").show();
			$("#dealUser").hide();
			$("#dealUserConn").hide();
		}
	}
	
	//查询回复记录
	function queryFeedbackRecord() {
		$(".feedback-info").empty();
		var feedbackInfoId = $("#feedbackInfoId").val();
		if(feedbackInfoId != null && feedbackInfoId != "") {
		    //获取回复记录
			var actionUrl = $.ctx + "/ci/feedback/ciFeedbackAction!queryFeedbackRecord.ai2do";
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: "feedbackInfoId="+feedbackInfoId,
				success: function(result){
					replyRecord(result);
				}
			});
		}
	}
		
	//根据回复记录拼接页面
	function replyRecord(result) {
		var html="";
		var content="";
		$.each(result.feedbackRecordList, function(i,record) {
			var replyDay = record.day;
			var replyTime = commonUtil.dateFormat(new Date(record.replyTime),"HH:mm:ss",false);
			if(1 == record.replyType){
				content+="<div class='feed-expand'>";
				content+="<ul class='clearfix feed-expand'>";
				content+="<li class='left-worker'>";
				content+="<div class='left-worker-name fleft'><span>"+record.userNme+"</span><span style='margin-left:10px;color:#0058e0'>"+replyTime+"</span></div>";
				if (replyDay != null && replyDay != "") {
					content+="<div class='sent-date clearfix'><span class = 'gray-line fleft'></span>";
					content+= replyDay;
					content+="<span class = 'gray-line fright'></span></div>";
				}
				content+="</li>";
				content+="<li class='left-message'> ";
				content+="<div class='sent-message'> ";
				content+="<p class='triangle-right left fleft'>"+record.replyInfo+"</p>";
				content+="</div>";
				content+="<div class='upload-file-box clearfix'>";
				content+="<ul class='clearfix'>";
				if (record.attachmentList.length!=0) {
					$.each(record.attachmentList, function(i, attachment) {
						var url = '${ctx}/ci/feedback/ciFeedbackAction!downLoadAttachments.ai2do?fileId='+attachment.fileId;
						content+='<li class="file-item fleft"><a href="'+url+'">';
						content+=attachment.fileOldName;
						content+="</a></li>";
					});
				} else {
					content+="<li class='file-item fleft'><a href='javascript:void(0);'>";
					content+="</a></li>";
				}
				content+="</ul>";
				content+="</div>";
				content+="</li>";
				content+="</ul>";
				content+="</div>";
			} else {
				content+="<div class='feed-expand '>";
				content+="<ul class='clearfix feed-expand'>";
				content+="<li class='right-worker fright'>";
				content+="<div class='right-worker-name fright'><span style='margin-right:10px;color:#0058e0'>"+replyTime+"</span><span>"+record.replyName+"</span></div>";
				if (replyDay != null && replyDay != "") {
					content+="<div class='sent-date clearfix'><span class = 'gray-line fleft'></span>";
					content+= replyDay;
					content+="<span class = 'gray-line fright'></span></div>";
				}
				content+="</li>";
				content+="<li class='right-message'> ";
				content+="<div class='sent-message'>";
				content+="<p class='triangle-right right fright'>"+record.replyInfo+"</p>";
				content+="</div>";
				content+="<div class='upload-file-box clearfix'>";
				content+="<ul class='clearfix'> ";
				if ((""!=record.dealUser && null!=record.dealUser )|| (""!=record.dealUserConnc && null!=record.dealUserConnc)){
					if (record.dealUser!="") {
						content+="<li class='file-item fright'><a>"+record.dealUser+"</a></li> ";
					}
					if (record.dealUserConnc!="") {
						content+="<li class='file-item fright'><a>"+record.dealUserConnc+"</a></li> ";
					}
				} else {
					content+="<li class='file-item fright'><a href='javascript:void(0);'></a></li> ";
				}
				content+="</ul>";
				content+="</div>";
				content+="</li>";
				content+="</ul>";
				content+="</div>";
			}
		});
		html += content;
		$(".feedback-info").append(html);
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
	function arrangeAttachmentList() {
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
	//发布回复信息
	function publish() {
		var actionUrl = $.ctx + "/ci/feedback/ciFeedbackAction!feedbackReply.ai2do";
		if(validateForm()){
			arrangeAttachmentList();
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: $("#saveForm").serialize(),
				success: function(result){
					$("#attachmentList").empty();
					$(".upload_success").empty();
					queryFeedbackRecord();
					emptyForm();
				}
			});
		}
	}
	
	//判断回复信息栏是否为空
	function validateForm() {
		var	validater = $("#saveForm").validate({
			rules: {
				"feedbackRecordInfo.replyInfo": {
					required: true
				}
				
			},
			messages: {
				"feedbackRecordInfo.replyInfo": {
					required: "回复信息不能为空!"
				}
			},
			debug:true,
			errorPlacement:function(error,element) {
				$("#tip").hide();
				element.after(error);
			},
			success:function(element){
				element.remove();
			},
			errorElement: "div",
			errorClass: "tishi error"
		});
		if(!validater.form()){
			return false;
		}
		return true;
	} 
	
	//发布回复信息之后将回复信息栏置空
	function emptyForm() {
		$("#update-file-box").hide(); 
		$("#replyInfo").val("");
		$("#dealUserText").val("");
		$("#dealUserConnText").val("");
	}
	
</script> 
</head>
 <body>
 <jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
	<div class="mainBody">
	    <div class="no-search-header-without-line comWidth clearfix">
			<div class="logoBox clearfix">
				<div class="index_logo fleft" onclick="showHomePage();return false;"></div>
				<div class="secTitle fleft">意见反馈回复</div>
			</div>
		</div>
	</div>
	<div class="navBox comWidth" id = "navBox">
		<div class="fleft navListBox">
			<li id="feedback" class="current"><a href="javascript:void(0);">意见反馈回复</a></li>
		</div>
	</div>
	<div class="container">
		<div class="theme-inner">
			<div class="feedback-theme">
				<h2>
					<span class="fleft feedback-mes-icon"></span>
					<span class="fleft">${feedbackInfo.feedbackTitle}</span>
				</h2> 
			</div>	
			<div class="feedback-info"></div>
			<div id="progress"></div>
			<form id="saveForm" method="post">
				<div id="attachmentList" class="hidden"></div>
				<div id="attachmentListArranged" class="hidden"></div>
				<input id="canManageDeclare" name="feedbackInfo.canManageDeclare" value="${canManageDeclare}" type="hidden"></input>
				<input id="feedbackInfoId" name="feedbackRecordInfo.feedbackInfoId" value="${feedbackInfo.feedbackInfoId}" type="hidden"></input>
				<div class="feedback-send-box">
					<div class="sendBox">
						<textarea id="replyInfo" rows="" cols="" class="sendTextBox fleft" id="input_detail" name="feedbackRecordInfo.replyInfo" title="消息输入框"></textarea> 
						<a class="update-file-btn fright" id="update-file-btn" href="javascript:void(0);" title="附件" action-type="plugin"> 添加附件 </a> 
					</div>
					<div class="upload-box hidden" id="update-file-box">
						<div class="fright upload-box-btn">
							<input type="file" id="file_upload_dark" name="files" />
						</div>
						<div class="upload_success clearfix"></div>
					</div>
					<div class="sent-btn-box clearfix"> 
						<label id="dealUser">联系人：<input type="text" id="dealUserText" name="feedbackRecordInfo.dealUser" style="border:1px solid #b8b8b8;width:200px;height:20px;"/></label> 
						<label  id="dealUserConn">电话：<input type="text" id="dealUserConnText" name="feedbackRecordInfo.dealUserConnc" style="border:1px solid #b8b8b8;width:200px;height:20px;"/></label>
						<div class="fright"><button type="button" class="button" onclick="publish()">发布</button></div>
						<a href="javascript:void(0);" onclick="showFeedback();" class="prevBtn fright" id="prevBtn" style="padding-top: 15px;">返回意见反馈</a>
					</div> 
					<div id="tip" class="tishi error" style="display:none;"></div>
				</div>
			</form>
		</div>
	</div> 
</body>
</html>