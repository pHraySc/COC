<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<c:set var="source" value="${source}"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
<title>意见反馈详细信息</title>
<script type="text/javascript"> 
	$(document).ready(function() {
		queryFeedbackRecord();
		//控制页面下拉之后navigation.jsp不显示浮动搜索域
		$("#isCreateCustomPage").val("true");
	}); 
	//查询回复记录
	function queryFeedbackRecord () {
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
</script> 
</head>
 <body>
 <jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
	<div class="mainBody">
	    <div class="no-search-header-without-line comWidth clearfix">
			<div class="logoBox clearfix">
				<div class="index_logo fleft" onclick="showHomePage();return false;"></div>
				<div class="secTitle fleft">意见反馈详情</div>
			</div>
		</div>
	</div>
	<div class="container">
	<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
	<div class="navBox comWidth" id = "navBox">
		<div class="fleft navListBox">
			<li id="feedback" class="current"><a href="javascript:void(0);">意见反馈详情</a></li>
		</div>
	</div>
	</div>
	<div class="theme-inner">
		<div class="feedback-theme">
			<h2>
				<span class="fleft feedback-mes-icon"></span>
				<span class="fleft">${feedbackInfo.feedbackTitle}</span>
			</h2> 
		</div>	
		<input id="feedbackInfoId" name="feedbackRecordInfo.feedbackInfoId" value="${feedbackInfo.feedbackInfoId}" type="hidden"></input>
		<div class="feedback-info"></div>
		<div class="tagSaveBtnBox"> 
			<c:if test="${source == 1}">
				<a href="javascript:void(0);" onclick="returnFeedback(${source});" class="prevBtn fright" id="prevBtn">返回意见反馈</a>
			</c:if>
			<c:if test="${source == 0}">
				<a href="javascript:void(0);" onclick="returnFeedback(${source});" class="prevBtn fright" id="prevBtn">返回历史反馈</a>
			</c:if>
		</div>
	</div> 
</body>
</html>