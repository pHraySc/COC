<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript">
$(document).ready(function () {
	//关闭确认窗口，并处理关闭确认之后的业务逻辑 1:删除操作  2:标记已读操作
	var optionDelOrUpdate = '';//设置closeConfirmDialog回调
	search($("#noticeIdInput").val());
	$("#confirmDialog").dialog({
		autoOpen: false,
		width: 500,
		height:185,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	$("#personNoticeCheckBoxAllA").click(function(){
		if($(this).hasClass("checkBoxOn")){
			$(this).removeClass("checkBoxOn");
			$(this).find("input").attr("checked", false);
		}else{
			$(this).addClass("checkBoxOn");
			$(this).find("input").attr("checked", true);
		}
		checkBoxAll(this);
	});
	$("#_personNoticeTitle").click(function(){
		$("#personNoticeCheckBoxAllA").click();
	});
});
       
function pagetable() {
	$(window).scrollTop(0);
	var actionUrl = 'ciPersonNoticeAction!showQuery.ai2do?' + $("#pageForm").serialize()+"&personNotice.readStatus="+$("#readStatus").val();
	$("#public_centerDiv").page({url: actionUrl, objId: 'public_centerDiv', callback: pagetable });
	checkDelete();
}

function search(noticeId) {
	$("#readStatus").val("");
	$("#noticeIdInput").val("")
	if(noticeId==undefined){
        noticeId = "";
    }
    var actionUrl = 'ciPersonNoticeAction!showQuery.ai2do?' + $("#pageForm").serialize()+'&noticeId='+noticeId;
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        success:function(html){
            $("#public_centerDiv").html("");
            $("#public_centerDiv").html(html);
            pagetable();
            $("input[name='personNoticeCheckBox']").each(function(){
                if($.trim($(this).val()) == noticeId){
                	 $(this).parent().addClass("checkBoxOn");
                	 $(this).attr("checked", true);
                    }
    		});
            var _personNoticeTitle=$.trim($("#_personNoticeTitle").html());
            if(_personNoticeTitle=="" || _personNoticeTitle == "未读的个人通知"){
            	$("#_personNoticeTitle").html("个人通知");
			}
            var notReadCount = $("#personNotReadCount").val();
			var totalCount = $("#personTotal").val();
            $("#notReadCount").html(notReadCount);
            $("#totalCounts").html(totalCount);
           	//$("#a_notRead").removeAttr("disabled");
           	//$("#a_allRead").attr("disabled","disabled");
            $("#a_notRead").show();
        	$("#a_notReadSpan").hide();
        	$("#a_allRead").hide();
        	$("#a_allReadSpan").show();
        	checkDelete();
        }
    });
}

function datatable(obj) {
	$.post("${ctx}/ci/ciPersonNoticeAction!showNoticeLog.ai2do?noticeName=" + encodeURI(encodeURI($(obj).attr("noticeName"))));
	if($(obj).attr("readStatus")!="2"){
   		var actionUrl = 'ciPersonNoticeAction!updateReadStatus.ai2do?pojo.noticeId=' + $(obj).attr("noticeId");
       	$.ajax({
			url:actionUrl,
           	type:"POST",
           	success:function(result){
				flag = result.success;
       	    	if(flag){
	       	    	$(obj).parent().parent().removeClass("checkBoxChecked");
	       	    	$("#notReadCount").html(result.readStatusNot);
	       	    	var readStatus = $("#readStatus").val();
	       	    	if(readStatus == '1'){
						notReadSearch();
					}else{
						search($("#noticeIdInput").val());
			  		}
           		}
           }
       });
	}
}
function updateReadStatusYesAll(){
  	var noticeIds = "";
  	optionDelOrUpdate = '2';
	$("input[name='personNoticeCheckBox']").each(function(){
		var checked = this.checked;
		if(checked){
			noticeIds += (noticeIds == "" ? "" :',') + $(this).val();
		}
	});
	if(noticeIds != ""){
		openConfirmUpdateReadStatus("确认将所选通知标记为已读？",noticeIds);
	}
}
//打开确认弹出层,由于confirmDialog中调用了父页面的closeConfirmDialog和“#confirmDialog”
//所以在父页面中必须要有这两个内容
function openConfirmUpdateReadStatus(confirmMsg, id){
	confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
	var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
	$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
//标记未读标签为已读标签的具体业务逻辑，支持批量标记已读，其中 $(".num_on").click 实现了页码保持：在哪个页码里做的标记已读，标记已读后还是在哪页
function updateReadStatus(noticeId){
	var actionUrl = $.ctx + "/ci/ciPersonNoticeAction!updateAllReadStatus.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "noticeIds="+noticeId,
		success: function(result){
			if(result.success){
				//弱提示
               	$.fn.weakTip({"tipTxt":result.msg});
				$("input[name='pager.totalSize']").val(0);
				//先刷新页面，然后点击到对应的页码
				var num_on=$(".num_on");
				var pageNum=num_on.text();
				var readStatus = $("#readStatus").val();
				if(readStatus == '1'){
					notReadSearch();
				}else{
					search($("#noticeIdInput").val());
				}
				$(".num").each(function () {
					if ($(this).text() == pageNum) {
					    $(this).addClass("num_on");
					    $(this).click();
					}
				});
			}else{
				showAlert("标记已读失败：" +result.msg,"failed");
			}
		}
	});	
}

//点击批量删除时所调用的js方法用来删除选中的标签
function delAll(){
	optionDelOrUpdate = '1';
	var noticeIds = "";
	$("input[name='personNoticeCheckBox']").each(function(){
		var checked = this.checked;
		if(checked){
			noticeIds += (noticeIds == "" ? "" :',') + $(this).val();
		}
   	});
	if(noticeIds != ""){
		openConfirm("确认删除所选通知？",noticeIds);
	}
}
//打开确认弹出层,由于confirmDialog中调用了父页面的closeConfirmDialog和“#confirmDialog”
//所以在父页面中必须要有这两个内容
function openConfirm(confirmMsg, id){
	confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
	var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
	$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
//关闭确认窗口，并处理关闭确认之后的业务逻辑，由confirmDialog.jsp调用
function closeConfirmDialog(dialogId, flag,id){
	$(dialogId).dialog("close");
	if(flag){
		if(optionDelOrUpdate == '1'){
			del(id);
		}else if(optionDelOrUpdate == '2'){
			updateReadStatus(id);
		}else if(optionDelOrUpdate == '3'){
			readAll();
		}else if(optionDelOrUpdate == '4'){
			deleteAll();
		}
	}
}
//删除标签的具体业务逻辑，支持批量删除，其中 $(".num_on").click 实现了页码保持：在哪个页码里做的删除，删除后还是在哪页
function del(noticeId){
	var actionUrl = $.ctx + "/ci/ciPersonNoticeAction!del.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "noticeIds="+noticeId,
		success: function(result){
			if(result.success){
				 //弱提示
               	$.fn.weakTip({"tipTxt":result.msg});
				$("input[name='pager.totalSize']").val(0);
				//先刷新页面，然后点击到对应的页码
				 var num_on=$(".num_on");
				 var pageNum=num_on.text();
				 var readStatus = $("#readStatus").val();
				 if(readStatus == '1'){
					 notReadSearch();
				 }else{
					 search($("#noticeIdInput").val());
				 }
				 $(".num").each(function () {
			            if ($(this).text() == pageNum) {
   				            $(this).addClass("num_on");
			                $(this).click();
			            }
			        });
			}else{
				showAlert("删除失败：" +result.msg,"failed");
			}
		}
	});	
}
//做全选checkBox操作时调用的js方法，做全选后调用方法设置删除是否可见
function checkBoxAll(obj){
	var _checked = $(obj).attr("_checked"); 
	var _checkedFlag = true;
	if(_checked == "false"){
		$(obj).attr("_checked",true);
		_checkedFlag = true;
   	}else{
   		$(obj).attr("_checked",false);
   		_checkedFlag = false;
    }
	$("input[name='personNoticeCheckBox']").each(function(){
		if($(this).prop("disabled") != true){
			if(_checkedFlag){
				if($(this).parent().hasClass("checkBoxOn")){
	   			}else{	
	   				$(this).parent().addClass("checkBoxOn");
	       		}
	   		}else{
				if($(this).parent().hasClass("checkBoxOn")){
					$(this).parent().removeClass("checkBoxOn");
				}
			}
			$(this).attr("checked", _checkedFlag);
		}
	});
}
//设置删除是否可见
function checkDelete(){
	 $("#personNoticeCheckBoxAllA").attr("_checked",false);
	 $("#personNoticeCheckBoxAllA").removeClass("checkBoxOn");
}
function notReadSearch(){
	var noticeId = $("#noticeIdInput").val();
   	if(noticeId==undefined){
           noticeId = "";
   	}
    var readStatus = "1";
    $("#readStatus").val(readStatus);
    var para={"personNotice.readStatus":readStatus};
	var actionUrl = 'ciPersonNoticeAction!showQuery.ai2do?' + $("#pageForm").serialize()+'&noticeId='+noticeId;
	$.ajax({
		url:actionUrl,
		type:"POST",
		dataType: "html",
		data:para,
		success:function(html){
			$("#public_centerDiv").html("");
			$("#public_centerDiv").html(html);
			pagetable();
			var _personNoticeTitle=$.trim($("#_personNoticeTitle").html());
			if(_personNoticeTitle=="" || _personNoticeTitle == "个人通知"){
         		$("#_personNoticeTitle").html("未读的个人通知");
			}
			var notReadCount = $("#personNotReadCount").val();
   			var totalCount = $("#personTotal").val();
			$("#notReadCount").html(notReadCount);
			$("#totalCounts").html(totalCount);
               
			$("#a_notRead").hide();
           	$("#a_notReadSpan").show();
           	$("#a_allReadSpan").hide();
           	$("#a_allRead").show();
           	checkDelete();
		}
	});
}

function searchR(){
	search("");
}
      
function readAll(){
  	var actionUrl = $.ctx + "/ci/ciPersonNoticeAction!updateAllRead.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		success: function(result){
			if(result.success){
				 //弱提示
	              	$.fn.weakTip({"tipTxt":result.msg});
				$("input[name='pager.totalSize']").val(0);
				//先刷新页面，然后点击到对应的页码
				 var num_on=$(".num_on");
				 var pageNum=num_on.text();
				 var readStatus = $("#readStatus").val();
				 if(readStatus == '1'){
					 notReadSearch();
				 }else{
					 search($("#noticeIdInput").val());
				 }
				 $(".num").each(function () {
			            if ($(this).text() == pageNum) {
	  				            $(this).addClass("num_on");
			                $(this).click();
			            }
			        });
			}else{
				showAlert("全部标记已读失败：" +result.msg,"failed");
			}
		}
	});
}

function deleteAll(){
  	var actionUrl = $.ctx + "/ci/ciPersonNoticeAction!delAll.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		success: function(result){
			if(result.success){
				//弱提示
				$.fn.weakTip({"tipTxt":result.msg});
				$("input[name='pager.totalSize']").val(0);
				//先刷新页面，然后点击到对应的页码
				var num_on=$(".num_on");
				var pageNum=num_on.text();
				var readStatus = $("#readStatus").val();
				if(readStatus == '1'){
					notReadSearch();
				}else{
					search($("#noticeIdInput").val());
				}
				$(".num").each(function () {
					if ($(this).text() == pageNum) {
					    $(this).addClass("num_on");
					    $(this).click();
					}
				});
			}else{
				showAlert("全部删除失败：" +result.msg,"failed");
			}
		}
	});	
}

function readAllConfirm(){
	optionDelOrUpdate = '3';
	var notReadCount = $.trim($("#notReadCount").html());
	if(notReadCount > 0){
		openConfirm("确认将全部通知标记为已读？","");
	}
}
function deleteAllConfirm(){
	optionDelOrUpdate = '4';
	var totalCounts = $.trim($("#totalCounts").html());
	if(totalCounts > 0){
		openConfirm("确认全部删除通知？","");
	}
}
</script>
<input id="noticeIdInput" type="hidden" name="pojo.noticeId" value="${pojo.noticeId }"/>
<div class="" id="public_centerDiv22">
	<div class="newMsgContentWrap">
		<div class="clearfix title-box-tip">
			<a class="fleft big-title J_checkAll" href="javascript:void(0);" id="_personNoticeTitle" title="点击勾选当页个人通知！">个人通知</a>
			<p class="fleft">(
				<a href="javascript:void(0);" id="a_allRead" onclick="searchR();"  class="notReadSpan" >全部</a>
				<span id="a_allReadSpan" style="display:none;" >全部</span>
				<span id="totalCounts"></span>条
				<span>其中 </span>
				<a href="javascript:void(0);" id="a_notRead"  onclick="notReadSearch();" class="notReadSpan" >未读消息 </a>
				<span id="a_notReadSpan" style="display:none;"  >未读消息</span>
				<span id="notReadCount"></span> 条)</p>
		</div>
		<h3 class="operation-box">
			<label class="fl">
				<a href="javascript:void(0);" onclick="return false;" class="checkBoxAll frist" id="personNoticeCheckBoxAllA" _checked="false" >
					<input type="checkbox" name="personNoticeCheckBoxAll" id="checkBox_all" >
				</a>
			</label>
			<a  class="fl" href="javascript:void(0);"  onclick="delAll()" title="请至少选择一条记录！">删除</a>
			<a  class="fl" href="javascript:void(0);"  onclick="updateReadStatusYesAll()" title="请至少选择一条记录！">标记已读</a>
			<a  class="fl" href="javascript:void(0);"  onclick="readAllConfirm()">全部已读</a>
			<a  class="fl" href="javascript:void(0);"  onclick="deleteAllConfirm()">全部删除</a>
		</h3>
		
		<div id="public_centerDiv">
		</div>
	</div>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<input type="hidden" name="personNotice.readStatus" id="readStatus" value="${personNotice.readStatus }"></input>