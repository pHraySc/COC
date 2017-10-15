<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<title>反馈历史</title>
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
	     
	    /* var canManageDeclare = $("#canManageDeclare").val();
		if ("true"==canManageDeclare) {
			$("#add_feedback_div").hide();
		} */ 		
	});
			
	function pagetable() {
    	var actionUrl = '${ctx}/ci/feedback/ciFeedbackAction!queryHistory.ai2do?' + $("#pageForm").serialize();
    	$("#declareList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'declareList',callback: pagetable });
		datatable();
    	//每刷新一次查询结果，都需要判断是否显示批量删除按钮
    	checkDelete();
    }
    function datatable() {
    	$(".commonTable mainTable").datatable();	
    }
    
    
  //做全选checkBox操作时调用的js方法，做全选后调用方法设置删除是否可见
    function checkBoxAll(obj) {
    	$(".checkBox").each(function(){
    		if($(this).prop("disabled") != true){
    			$(this).prop("checked", obj.checked);
    		}
    	  });
    	checkDelete();
    }
  	//设置删除是否可见
	function checkDelete() {
		if($(".checkBox:checked").size()>0){
			$(".icon_button_delete").removeClass("disable").removeAttr("title");	
		}else{
			$(".icon_button_delete").addClass("disable").attr("title","请至少选择一条未提交审批记录！");
		}
	}
	jQuery.focusblur = function (focusid) {
		var focusblurid = $(focusid);
	    var defval = focusblurid.val();
	    focusblurid.focus(function (){
	    	var thisval = $(this).val();
	        if (thisval == defval){
	        	$(this).val("");
	        	$(this).attr("flag","true");
	        }
		});
	    focusblurid.blur(function () {
	        var thisval = $(this).val();
	        if (thisval == "") {
	            $(this).val(defval);
	            $(this).attr("flag","false");
	        }
	    });
	}
	
	function declareDelete(declareInfoId) {
		openConfirm(declareInfoId);
	}		
	
	//删除反馈
	function del(declareId){
		var actionUrl = $.ctx + "/ci/feedback/ciFeedbackAction!delete.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "declareIds="+declareId,
			success: function(result){
				if(result.success){
					showAlert("" +result.msg,"success");
					$("input[name='pager.totalSize']").val(0);
					search();//刷新列表
				}else{
					showAlert("删除失败：" +result.msg,"failed");
				}
			}
		});	
	}
	//删除所选中的反馈
	function delAll(){
		var declareIds = "";
		$(".checkBox").each(function(){
			var checked = this.checked;
			if(checked){
				declareIds += (declareIds == "" ? "" :',') + $(this).val();
			}
		});
		if(declareIds != ""){
			openConfirm();
		}
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
		
		var actionUrl = '${ctx}/ci/feedback/ciFeedbackAction!queryHistory.ai2do?';
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
	            checkDelete();
	            datatable();
	    	}
           });
	}
	
	//展示反馈详情
	/*function showFeedbackDetail(declareInfoId) {
		var ifmUrl = "${ctx}/aibi_ci/labelDeclare/showFeedbackDetail.jsp?feedbackInfoId="+feedbackInfoId;
		var digObj = dialogUtil.create_dialog("showDeclareDialog",{
			"title":"意见反馈详情",
			"high":"auto",
			"width":730,
			"frameSrc":ifmUrl,
			"frameHeight":550,
	        "position" : ['center','center']
		});
	}*/
	
	// 历史记录页面
	function feedbackRecordShow(feedbackInfoId) {
		//var url = $.ctx + "/ci/feedback/ciFeedbackAction!queryFeedba"
	}
		
</script>
</head>
<body>
<div class="comWidth pathNavBox pathNavTagBox">
	<span class="pathNavHome"  style="cursor:default;">历史反馈</span>
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
		      	  	 		<a href="javascript:void(0);" value="4">已取消</a>
		      	  	 		<a href="javascript:void(0);" value="5">已关闭</a>
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
<div class="aibi_area_data">
	<div id="declareList" class="tableStyleBox"></div>
</div>
	</body>
</html>