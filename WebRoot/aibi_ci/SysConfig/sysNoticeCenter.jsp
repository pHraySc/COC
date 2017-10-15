<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.string.StringUtil"%>
<%
String mailMenu = Configure.getInstance().getProperty("MAIL_NOTIFICATION_NONSTANDARD_CLASS_NAME");
String smsMenu = Configure.getInstance().getProperty("SMS_NOTIFICATION_NONSTANDARD_CLASS_NAME");
%>
<script >
var sendModeId = "1";
var typeId ="${pojo.typeId}";
$(function(){
	//alert(typeId+"-*");
	$("#messageSettingDiv ol li a").click(function(){
		$(".newMsgCenterLeftBox").find(".current").removeClass("current");
		$(this).addClass("current");
		sendModeId = $(this).attr("sendModeId");
	});
	init(typeId);
});
//全部打开 或者全部关闭
function openAll(inputName){
	//类型 1,系统公告；2,个人通知
	var noticeType;
	var noticeValue;
	var allId;
	if($.trim(inputName) == "isModel"){
		noticeType = "1";
		allId= "sysAllSetId";
		
	}else{
		noticeType = "2";
		allId = "personAllSetId";
		
	}
	//如果是全部关闭，
	if($("#"+allId).hasClass("allClose")){
		noticeValue = 0;
	}else{
		noticeValue = 1;
	}
	batchUserNoticeSet(noticeType,noticeValue);
	//如果是全部关闭，
	if($("#"+allId).hasClass("allClose")){
		$("input[name="+inputName+"]").each(function(){
		      $(this).attr("checked",true).val(1).parent().removeClass().addClass("msgStatusSelOn msgStatusSelOff");
		   });
		$("#"+allId).removeClass("allClose");
		$("#"+allId).html("");
		$("#"+allId).html("全部打开");
	}else{
		$("input[name="+inputName+"]").each(function(){
		      $(this).attr("checked",false).val(1).parent().removeClass().addClass("msgStatusSelOn");
		   });
		$("#"+allId).addClass("allClose");
		$("#"+allId).html("");
		$("#"+allId).html("全部关闭");
	}
   return false;
}
//客户群类消息
function customerMessage(){
	var para ={
			"personNotice.noticeTypeId":3
			};
	var noticeId = $("#noticeId").val();
	var actionUrl = 'ciPersonNoticeAction!showQuery.ai2do?' + $("#pageForm").serialize()+'&pojo.noticeId='+noticeId;
	$.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: para,
        success:function(html){
            $("#messageMain").html("");
            $("#messageMain").html(html);
            //pagetable();
        }
    });
}
//个人通知
function personMessage(noticeId){
	if(noticeId==undefined){
        noticeId = "";
    }
	$("#messageSetting").removeClass("current");
	$("#sysAnnouncement").removeClass("current");
	$("#messageNotice").addClass("current");
	$("#messageNoticeDiv").show();
	$("#messageSettingDiv").hide();
	
	$(".newMsgCenterLeftBox").find(".current").removeClass("current");
	$("#personNoticeId").addClass("current");
	var para = {
			};
	//var noticeId = $("#noticeId").val();
	var actionUrl = '${ctx}/ci/ciPersonNoticeAction!show.ai2do?pojo.noticeId='+noticeId;
	$.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: para,
        success:function(html){
		 	$("#sysAnnouncementList").hide();
		 	$("#messageMain").show();
            $("#resultList").html("");
            $("#resultList").html(html);
        }
    });
}

//系统消息
function sysMessage(noticeId){
	if(noticeId==undefined){
        noticeId = "";
    }
	$("#messageSetting").removeClass("current");
	$("#sysAnnouncement").removeClass("current");
	$("#messageNotice").addClass("current");
	$("#messageNoticeDiv").show();
	$("#messageSettingDiv").hide();
	
	$(".newMsgCenterLeftBox").find(".current").removeClass("current");
	$("#sysNoticeId").addClass("current");
	var para = {
	};
	var actionUrl = '${ctx}/ci/ciSysAnnouncementAction!show.ai2do?pojo.noticeId='+noticeId;
	$.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: para,
        success:function(html){
            $("#resultList").html("");
            $("#resultList").html(html);
            //pagetable();
        }
    });
}
//消息设置
function messageSet(sendModeIdStr){
	var para = {
			"fromPageFlag":1,
			"sendModeId":sendModeIdStr
			};
	var actionUrl = "${ctx}/ci/ciUserNoticeSetAction!initUserNoticeSet.ai2do";
	$.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: para,
        success:function(html){
			$("#sysAnnouncementList").hide();
			$("#messageMain").show();
            $("#resultList").html("");
            $("#resultList").html(html);
            //pagetable();
        }
    });
}
function messageSetClass(){
	$("#fixedSearchBox").hide();
	$("#messageNotice").removeClass("current");
	$("#sysAnnouncement").removeClass("current");
	$("#messageSetting").addClass("current");
	$("#messageNoticeDiv").hide();
	$("#messageSettingDiv").show();
	$("#systemMsgId").click();
	$("#systemMsgId").addClass("current");
}
//点击消息通知
function personNoticeInfo(){
	$("#fixedSearchBox").hide();
	$("#messageSetting").removeClass("current");
	$("#sysAnnouncement").removeClass("current");
	$("#messageNotice").addClass("current");
	$("#messageNoticeDiv").show();
	$("#messageSettingDiv").hide();
	$("#personNoticeId").click();
	//personMessage();
}

//批量设置
function batchUserNoticeSet(noticeType,isReceive){
	var url = "${ctx}/ci/ciUserNoticeSetAction!batchUserNoticeSet.ai2do";
	$.ajax({  
      type: 'post',  
      url: url,  
      cache: false,  
      data: {"noticeType":noticeType,"isReceive":isReceive,"sendModeId":sendModeId},  
      success: function(result){
          if(result.success){
        	  //history.go(0);
			  //alert(result.message);
		  }else{
			  //alert(result.message);
		  }
      }
    });  
}
//设置批量按钮状态
function checkUserNoticeSet(noticeType,isReceive,sendModeId){
	var url = "${ctx}/ci/ciUserNoticeSetAction!checkUserNoticeSet.ai2do";
	$.ajax({  
           type: 'post',  
           url: url,  
           cache: false,  
           data: {"noticeType":noticeType,"isReceive":isReceive,"sendModeId":sendModeId},  
           success: function(result){  
               if(result.success){
				   if(result.isBatch == 'true'){
					   if(noticeType == '1' && isReceive == '0'){
						   //$("#buttonOne").attr("class","btn_open");
						   $("#sysAllSetId").removeClass("allClose");
							$("#sysAllSetId").html("");
							$("#sysAllSetId").html("全部打开");
						   //alert('打开全部');
					   }
					   if(noticeType == '1' && isReceive == '1'){
						   //$("#buttonOne").attr("class","btn_close");
							$("#sysAllSetId").addClass("allClose");
							$("#sysAllSetId").html("");
							$("#sysAllSetId").html("全部关闭");
						   //alert('关闭全部');
					   }
					   if(noticeType == '2' && isReceive == '0'){
						   //$("#buttonTwo").attr("class","btn_open");
						   $("#personAllSetId").removeClass("allClose");
							$("#personAllSetId").html("");
							$("#personAllSetId").html("全部打开");
						   //alert('打开全部');
					   }
					   if(noticeType == '2' && isReceive == '1'){
						    //$("#buttonTwo").attr("class","btn_close");
						    $("#personAllSetId").addClass("allClose");
							$("#personAllSetId").html("");
							$("#personAllSetId").html("全部关闭");
						   //alert('关闭全部');
					   }
				   }
		       }else{
			       //alert(result.message);
		       }
           }
      }); 
	
}
//用户个性化设置
function userNoticeSet(originalCheckBox){
	var url = "${ctx}/ci/ciUserNoticeSetAction!userNoticeSet.ai2do";
	//alert(originalCheckBox.html()+"--**");
	//alert(originalCheckBox.attr('noticeId'));
	//alert(originalCheckBox.attr('noticeType'));
	//alert(originalCheckBox.attr('isSuccess'));
	//alert(originalCheckBox.attr('checked'));
	var checked = originalCheckBox.attr('checked');
	
	var noticeId = originalCheckBox.attr('noticeId');
	var noticeType = originalCheckBox.attr('noticeType');
	var isSuccess = originalCheckBox.attr('isSuccess');
	//return false;
	
	if(checked == 'checked'){
		//alert('点击触发后为不接受状态');
		var isReceive = '0';
		$.ajax({  
           type: 'post',  
           url: url,  
           cache: false,  
           data: {"noticeType":noticeType,"isReceive":isReceive,"noticeId":noticeId,"isSuccess":isSuccess,"sendModeId":sendModeId},  
           success: function(result){  
               if(result.success){
			       //alert(result.message);
				   checkUserNoticeSet(noticeType,isReceive,sendModeId);
				   //history.go(0);
		       }else{
			       //alert(result.message);
		       }
           }
         }); 
	}else{
		//alert('点击触发后为接受状态');
		var isReceive = '1';
		$.ajax({  
           type: 'post',  
           url: url,  
           cache: false,  
           data: {"noticeType":noticeType,"isReceive":isReceive,"noticeId":noticeId,"isSuccess":isSuccess,"sendModeId":sendModeId},  
           success: function(result){  
               if(result.success){
			       //alert(result.message);
				   checkUserNoticeSet(noticeType,isReceive,sendModeId)
				   //history.go(0);
		       }else{
			       //alert(result.message);
		       }
           }
         }); 
	}
}
function sysAnnouncementList(){
	$("#fixedSearchBox").hide();
	$("#messageSetting").removeClass("current");
	$("#messageNotice").removeClass("current");
	$("#sysAnnouncement").addClass("current");
	$("#messageNoticeDiv").hide();
	$("#messageSettingDiv").hide();
	
	var actionUrl = '${ctx}/ci/ciSysAnnouncementAction!init.ai2do';
	$.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        success:function(html){
            $("#messageMain").hide();
            $("#sysAnnouncementList").html("");
            $("#sysAnnouncementList").html(html);
            $("#sysAnnouncementList").show();
        }
    });
}

function init(type){
	if(type == "" ||type =="undefined"){
		$("#messageSetting").click();
	}
	//个人通知
	if(type == "1"){
		var noticeType ="${pojo.noticeType}";
		//$("#messageNotice").click();
		if(noticeType == 1){
			personMessage($("#noticeId").val());
		}else{
			sysMessage($("#noticeId").val());
		}
	}//公告
	if(type == "2"){
		//$("#sysNoticeId").click();
		sysMessage($("#noticeId").val());
	}
	if(type == "3"){
		$("#sysAnnouncement").click();
	}
}
</script>
	<!-- 首页搜索区域 -->
	 <div class="navBox comWidth" id="navBox">
	 <div class="fleft navListBox">
	    <ol>
		  <li class="msgCenter" id="messageNotice" onclick="personNoticeInfo();"><a href="javascript:void(0);" >消息通知</a></li>
		  <li class="current" id="messageSetting" onclick="messageSetClass();"><a href="javascript:void(0);">消息设置</a></li>
		   <c:choose>
		   <c:when test="${isAdmin}">
		  <li class="msgCenter" id="sysAnnouncement" onclick="sysAnnouncementList();"><a href="javascript:void(0);">公告管理</a></li>
		  </c:when>
		  </c:choose>
		</ol>
	 </div>
  </div>
     <div class="comWidth newMsgCenterBox padTop24" id="messageMain">
	    <div class="fleft newMsgCenterLeftBox" id="messageSettingDiv">
		  <ol>
		    <li>
			  <a href="javascript:void(0);" class="current" onclick="messageSet('1');" sendModeId="1" id="systemMsgId"> 系统内部消息</a>
			</li>
			<% if (StringUtil.isNotEmpty(smsMenu)) { %>
			 <li>
			  <a href="javascript:void(0);" onclick="messageSet('2');" sendModeId="2"> 短信 </a>
			</li>
			<% } %>
			<% if (StringUtil.isNotEmpty(mailMenu)) { %>
			 <li>
			  <a href="javascript:void(0);" onclick="messageSet('3');" sendModeId="3"> Email</a>
			</li>
			<% } %>
		</ol>
		</div>
		<div class="fleft newMsgCenterLeftBox hidden" id="messageNoticeDiv">
		  <ol>
		    <li>
			  <a href="javascript:void(0);" onclick="personMessage();" id="personNoticeId"> 个人通知</a>
			</li>
			 <li class="current" >
			  <a href="javascript:void(0);" onclick="sysMessage();" id="sysNoticeId"> 系统公告 </a>
			</li>
			</ol>
		</div>
		<div class="fright newMsgCenterRightBox" id="resultList">
		</div>
	 </div>
	 <div class="comWidth newMsgCenterBox padTop24" style="display:none;" id="sysAnnouncementList"></div>
	 <div style="height: 50px;"></div>
<input type="hidden" name="typeId" name="pojo.typeId" value="${pojo.typeId }"/>
<input id="noticeId" type="hidden" name="pojo.noticeId" value="${pojo.noticeId }"/>
