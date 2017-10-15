<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<title>意见反馈</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#searchFeedbackTitle").focusblur();
		//设置子窗口中提示框相对于子窗口的位置，比较诡异：子窗口中的位置却要在父窗口中设定
		$(".tishi").each(function(){
			var toffset=$(this).parent("td").offset();
			var td_height=$(this).parent("td").height();
			$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
		});
		//给查询结果table设定斑马线效果
		//aibiTableChangeColor(".mainTable");
		search();
		$("#searchFeedbackButton").click(function(){
			search();
		});
	 //模拟下拉框
	       $(".selBtn ,.selTxt").click(function(){
	           if($(this).nextAll(".querySelListBox").is(":hidden")){
	           	$(this).nextAll(".querySelListBox").slideDown();
	           	if($(this).hasClass("selBtn")){
	                   $(this).addClass("selBtnActive");
	               }else{
	               	$(this).next(".selBtn").addClass("selBtnActive");
	               }
	          }else{
	               	 $(this).nextAll(".querySelListBox").slideUp();
	                    $(this).removeClass("selBtnActive");
	
	               }
	       	return false;
	       })
	
	       $(".querySelListBox a").click(function(){
	      	var selVal = $(this).attr("value");
	      	var selHtml = $(this).html();
	      	 $(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
	      	 $(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
	      	 $(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
	      	//向隐藏域中传递值
	         $(this).parents(".querySelListBox").slideUp();
	           return false;
	      })
	          $(document).click(function(ev){
	           var e = ev||event ;
	           e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	           $(".querySelListBox").slideUp();
	           $(".selBtn").removeClass("selBtnActive");
	     	   })
	     
	    var canManageDeclare = $("#canManageDeclare").val();
		if ("true"==canManageDeclare) {
			$("#add_feedback_div").hide();
		} 		
	});
	
			
	//打开添加意见反馈窗口
	function openAddDeclareDialog() {
		var ifmUrl = "${ctx}/aibi_ci/feedback/addFeedback.jsp";
		var digObj = dialogUtil.create_dialog("addFeedbackDialog", {
			"title":"新增意见反馈",
			"high":"auto",
			"width":730,
			"frameSrc":ifmUrl,
			"frameHeight":310,
	        "position" : ['center','center']
	   }); 
	}
	function openSuccessForSave(msg) {
		commonUtil.create_alert_dialog("successForSave", {
					 "txt":msg,
					 "type":"success",
					 "width":500,
					 "height":200
				},"closeSaveDialog").dialog({close:function(){
					 $(this).dialog("destroy");
					 search();
				}
		});
	}  
	
	//关闭保存窗口
	function closeSaveDialog(){
		$("#addFeedbackDialog").dialog("close");
		search();
	}
	
	function pagetable() {
    	var actionUrl = '${ctx}/ci/ciFeedbackAction!query.ai2do?' + $("#pageForm").serialize();
    	$("#declareList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'declareList',callback: pagetable });
		datatable();
    }
    function datatable() {
    	$(".commonTable mainTable").datatable();	
    }
    
	//查询反馈
	function search(){
		var searchTitle = "";
		var searchLabel = "";
		
	    if ("false" == $("#searchFeedbackTitle").attr("data-val")) {
			searchTitle = "";
		} else {
			searchTitle = $.trim($("#searchFeedbackTitle").val());
		}
		
		$("#feedbackTitle").val(searchTitle);
		$("#labelName").val(searchLabel);
		
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate != null && startDate != "" && endDate != null && endDate != "" && startDate>endDate){
			showAlert("开始时间不能大于结束时间","failed");
			return false;
		}
		
		var actionUrl = '${ctx}/ci/ciFeedbackAction!query.ai2do?' + $("#pageForm").serialize();
		$.ajax({
	    	url:actionUrl,
	        type:"POST",
	        dataType: "html",
	        data: $("#queryForm").serialize(),
	        success:function(html){
	        	$("#declareList").html("");
	            $("#declareList").html(html);
	            var totalSize = $("#totalSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
		            $("#pathFeedbackText").html(searchTitle);
				}
	            pagetable();
	            datatable();
	    	}
           });
	}
	
	//展示反馈详情
	function showFeedbackDetail(feedbackInfoId) {
		var url = $.ctx+"/ci/ciFeedbackAction!showFeedbackDetail.ai2do?feedbackInfoId="+feedbackInfoId+'&source=1';
		window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	//显示操作列表
	function showOperFeedbackList(e,ths,labelId){
		var selfIndex = $(".resultItemActive").index($(ths));
		$.each($(".resultItemActive"),function(index,item){
			if(index != selfIndex){
				var $resultItemActiveList= $(item).find(".resultItemActiveList");
				$resultItemActiveList.hide();
				$(item).removeClass("resultItemActiveOption");
			}
		});
		if($('#'+labelId ).children("ol").children("li").length > 0){
			$('#'+labelId).children("ol").children("li:last-child").addClass("end");
			var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			var _t=$(ths);
			var $resultItemActiveList= $(ths).children(".resultItemActiveList");
			if($resultItemActiveList.is(":visible")){
					$resultItemActiveList.hide();
					_t.removeClass("resultItemActiveOption");
			}else{
				var resultListHeight = $resultItemActiveList.height();
				var _tOffsetHeight= _t.offset().top + _t.height();
				var documentHeight = $(document.body).height();
				var resultHeight = resultListHeight + _tOffsetHeight;
				if(documentHeight<resultHeight){
					$(document.body).height(resultHeight + 20);
				}
			    $resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveOption")});
			}
		}
	}
	
	//打开回复页面
	function feedbackReply(feedbackInfoId){
		var url = $.ctx+"/ci/ciFeedbackAction!queryFeedbackInfoById.ai2do?feedbackInfoId="+feedbackInfoId;
		window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	
	// 开始处理
	function feedabackDo(feedbackInfoId) {
		commonUtil.create_alert_dialog("showDoAlertDialog", {
			"txt":"确定开始处理？",
			"type":"confirm",
			"width":500,
			"height":200,
			"param":feedbackInfoId
		},"doFeedback");
	}
	
	function doFeedback(feedbackInfoId) {
		var actionUrl = $.ctx + "/ci/ciFeedbackAction!startDealFeedback.ai2do";
		$.ajax({
			type:"POST",
			url:actionUrl,
			data: "feedbackInfoId="+feedbackInfoId,
			success: function(result){
				search();
			}
		});
	}
	
	//取消意见反馈
	function feedbackCancel(feedbackInfoId){
		commonUtil.create_alert_dialog("showCancelAlertDialog", {
			 "txt":"确定取消反馈？",
			 "type":"confirm",
			 "width":500,
			 "height":200,
			 "param":feedbackInfoId
		},"cancelFeedback");
    }
 	
	function cancelFeedback(feedbackInfoId) {
		var actionUrl = $.ctx + "/ci/ciFeedbackAction!cancelFeedback.ai2do";
		$.ajax({
			type:"POST",
			url:actionUrl,
			data: "feedbackInfoId="+feedbackInfoId,
			success: function(result){
				search();
			}
		});
	}
	
	// 处理完成
	function feedbackOver(feedbackInfoId) {
		commonUtil.create_alert_dialog("showOverAlertDialog", {
			"txt":"确定完成处理？",
			"type":"confirm",
			"width":500,
			"height":200,
			"param":feedbackInfoId
		},"overFeedback");
	}
	
	function overFeedback(feedbackInfoId) {
		var actionUrl = $.ctx + "/ci/ciFeedbackAction!finishFeedback.ai2do";
		$.ajax({
			type:"POST",
			url:actionUrl,
			data: "feedbackInfoId="+feedbackInfoId,
			success: function(result){
				search();
			}
		});
	}
	
	// 关闭意见反馈
	function feedbackClose(feedbackInfoId) {
		commonUtil.create_alert_dialog("showCloseAlertDialog", {
			"txt":"确定关闭反馈？",
			"type":"confirm",
			"width":500,
			"height":200,
			"param":feedbackInfoId
		},"closeFeedback");
	}
	
	function closeFeedback(feedbackInfoId) {
		var actionUrl = $.ctx + "/ci/ciFeedbackAction!closeFeedback.ai2do";
		$.ajax({
			type:"POST",
			url:actionUrl,
			data: "feedbackInfoId="+feedbackInfoId,
			success: function(result){
				search();
			}
		});
	}
	
	//意见反馈回退
	function feedbackFallback(feedbackInfoId) {
		commonUtil.create_alert_dialog("showCloseAlertDialog", {
			"txt":"确定回退反馈？",
			"type":"confirm",
			"width":500,
			"height":200,
			"param":feedbackInfoId
		},"fallbackFeedback");
	}
	
	function fallbackFeedback(feedbackInfoId) {
		var actionUrl = $.ctx + "/ci/ciFeedbackAction!fallbackFeedback.ai2do";
		$.ajax({
			type:"POST",
			url:actionUrl,
			data: "feedbackInfoId="+feedbackInfoId,
			success: function(result){
				search();
			}
		});
	}
	
</script>
</head>
<body>
<div class="comWidth pathNavBox pathNavTagBox">
	<span class="pathNavHome"  style="cursor:default;">意见反馈</span>
    <span class="pathNavArrowRight"></span>
    <span class="pathNavNext" id="pathFeedbackText"></span>
  	<span class="amountItem"> 共<span class="amountNum" id="totalSizeNum"></span><span id="typeNameText">个反馈</span></span>
</div>
<div class="aibi_search" style="height:35px">
	<form id="queryForm" action="" method="post">
		<input id="canManageDeclare" name="canManageDeclare" value="${canManageDeclare}" type="hidden"></input>
		<ul>
			<li>
				<input type="text" id="searchFeedbackTitle" name="searchFeedbackTitle" class="aibi_input"
		                           style="width:180px" value="反馈主题" data-val="false"></input>
				<input id="feedbackTitle" name="feedbackInfo.feedbackTitle" type="hidden"></input>
			</li>
			<li>
				<div class="fleft formQuerySelBox">
		      	  <div class="selTxt">全部反馈类型</div>
		      	  <a href="javascript:void(0);" class="selBtn"></a>
		      	  <div class="querySelListBox">
		      	     <input type="hidden" name="feedbackInfo.feedbackType" id="selectFeedbackType" value=""/>
		      	  	 <dl>
		      	  	 	<dd>
		      	  	 		<a href="javascript:void(0);"  value="">全部反馈类型</a>
		      	  	 		<a href="javascript:void(0);" value="1">标签</a>
		      	  	 		<a href="javascript:void(0);" value="2">客户群</a>
		      	  	 		<a href="javascript:void(0);" value="3">系统功能</a>
		      	  	 	</dd>
		      	  	 </dl>
		      	  </div>
				</div>
			</li>
			<li>
				<div class="fleft formQuerySelBox">
		      	  <div class="selTxt">全部处理状态</div>
		      	  <a href="javascript:void(0);" class="selBtn"></a>
		      	  <div class="querySelListBox">
		      	     <input type="hidden"  name="feedbackInfo.dealStatusId" id="dealStatusId" value=""/>
		      	  	 <dl>
		      	  	 	<dd>
		      	  	 		<a href="javascript:void(0);"  value="">全部处理状态</a>
		      	  	 		<a href="javascript:void(0);" value="1">未处理</a>
		      	  	 		<a href="javascript:void(0);" value="2">处理中</a>
		      	  	 		<a href="javascript:void(0);" value="3">已处理</a>
		      	  	 	</dd>
		      	  	 </dl>
		      	  </div>
				</div>
			</li>
			<c:if test="${canManageDeclare == false }">
			<li>
				<div class="fleft formQuerySelBox">
					<div class="selTxt">所有与我相关的</div>
					 <a href="javascript:void(0);" class="selBtn"></a>
		      	  		<div class="querySelListBox">
		      	     		<input type="hidden" name="feedbackInfo.sign" id="sign" value="0"/>
		      	  	 		<dl>
			      	  	 		<dd>
			      	  	 			<a href="javascript:void(0);"  value="0">所有与我相关的</a>
			      	  	 			<a href="javascript:void(0);"  value="1">本人创建</a>
			      	  	 			<a href="javascript:void(0);"  value="2">本人处理</a>
			      	  	 		</dd>
		      	  	 		</dl>
		      	  	 	</div>
				</div>
			</li>
			</c:if>
			<li>
		     <span class="line2_span">创建时间：</span>
		     <input style="cursor: pointer;" name="feedbackInfo.startDate" id="startDate" readonly type="text" class="defineDataInput datepickerBG"  
		     onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"/> -- 
			 <input style="cursor: pointer;" name="feedbackInfo.endDate" id="endDate" readonly type="text" class="defineDataInput datepickerBG"
			 onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"/>
			</li>
			<li>
				<a href="javascript:void(0);" id="searchFeedbackButton" class="queryBtn-bak">查询</a>
			</li>
		</ul>
		<div class="clear"></div>
		</form>
</div>
<div class="aibi_button_title " id="add_feedback_div">
	<ul>
		<li class="noborder"><a href="javascript:void(0);" id="add_feedback_button" class="icon_button_add" onclick="openAddDeclareDialog();"><span>新增</span></a></li>
	</ul>
</div>
<div class="aibi_area_data">
	<div id="declareList" class="tableStyleBox">
	</div>
</div>
</body>
</html>