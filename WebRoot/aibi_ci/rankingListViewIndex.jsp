<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/shopCart.js"></script>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script type="text/javascript">
$(function(){
	var rankingTypeId = '${rankingTypeId}';
	if(rankingTypeId != ""){
		rankingListIndex(rankingTypeId);
	}else{
		rankingListIndex(1);
	}
});
//系统热门标签排行榜列表showListType：1 
//近一周：1 近一个月：2 近三个月：3
function sysHotLabelRankingList(timeType){
	$("#loading").show();
	$("#weekArrayList").empty();
	$("#oneMonthArrayList").empty();
	$("#threeMonthArrayList").empty();
	$("#nameType").html("系统热门标签");
	$("#showListType").attr("value",1);
	$("#sysHotLabelRankId").parent().find("li").find("a").removeClass("current");
	$("#sysHotLabelRankId").find("a").addClass("current");
	$(".dateArraySwitchBox").show();
	var actionUrl = "";
	if(timeType == 1){
		$("#oneMonthArray").removeClass("current");
		$("#threeMonthArray").removeClass("current");
		$("#weekArray").addClass("current");
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSysHotRankLabel.ai2do?dataScope=oneWeek";
	}else if(timeType == 2){
		$("#threeMonthArray").removeClass("current");
		$("#weekArray").removeClass("current");
		$("#oneMonthArray").addClass("current");
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSysHotRankLabel.ai2do?dataScope=oneMonth";
	}else{
		$("#oneMonthArray").removeClass("current");
		$("#weekArray").removeClass("current");
		$("#threeMonthArray").addClass("current");
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSysHotRankLabel.ai2do?dataScope=threeMonth";
	}
	var para = {};
	$.ajax({
      url:actionUrl,
      type:"POST",
      dataType: "html",
      data: para,
      success:function(html){
	    $("#loading").hide();
          if(timeType == 1){
          	$("#oneMonthArrayList").hide();
          	$("#threeMonthArrayList").hide();
          	$("#weekArrayList").show();
	            $("#weekArrayList").html(html);
          }else if(timeType == 2){
          	$("#threeMonthArrayList").hide();
          	$("#weekArrayList").hide();
          	$("#oneMonthArrayList").show();
          	$("#oneMonthArrayList").html(html);
          }else{
          	$("#oneMonthArrayList").hide();
          	$("#weekArrayList").hide();
          	$("#threeMonthArrayList").show();
          	$("#threeMonthArrayList").html(html);
          }
      }
  });
}
//系统热门客户群排行榜列表 showListType：2 
function sysHotCustomRankingList(timeType){
	$("#loading").show();
	$("#weekArrayList").empty();
	$("#oneMonthArrayList").empty();
	$("#threeMonthArrayList").empty();
	$("#nameType").html("系统热门客户群");
	$("#showListType").attr("value",2);
	$("#sysHotCustomRankId").parent().find("li").find("a").removeClass("current");
	$("#sysHotCustomRankId").find("a").addClass("current");
	$(".dateArraySwitchBox").show();
	var actionUrl = "";
	if(timeType == 1){
		$("#oneMonthArray").removeClass("current");
		$("#threeMonthArray").removeClass("current");
		$("#weekArray").addClass("current");
		actionUrl = $.ctx + "/ci/customersManagerAction!sysHotCustomRankingList.ai2do?ciCustomGroupInfo.dateType=4&isSysRecommendCustomsFlag=true";
	}else if(timeType == 2){
		$("#threeMonthArray").removeClass("current");
		$("#weekArray").removeClass("current");
		$("#oneMonthArray").addClass("current");
		actionUrl = $.ctx + "/ci/customersManagerAction!sysHotCustomRankingList.ai2do?ciCustomGroupInfo.dateType=2&isSysRecommendCustomsFlag=true";
	}else{
		$("#oneMonthArray").removeClass("current");
		$("#weekArray").removeClass("current");
		$("#threeMonthArray").addClass("current");
		actionUrl = $.ctx + "/ci/customersManagerAction!sysHotCustomRankingList.ai2do?ciCustomGroupInfo.dateType=3&isSysRecommendCustomsFlag=true";
	}
	var para = {};
	$.ajax({
      url:actionUrl,
      type:"POST",
      dataType: "html",
      data: para,
      success:function(html){
      	$("#loading").hide();
          if(timeType == 1){
          	$("#oneMonthArrayList").hide();
          	$("#threeMonthArrayList").hide();
          	$("#weekArrayList").show();
	            $("#weekArrayList").html(html);
          }else if(timeType == 2){
          	$("#threeMonthArrayList").hide();
          	$("#weekArrayList").hide();
          	$("#oneMonthArrayList").show();
          	$("#oneMonthArrayList").html(html);
          }else{
          	$("#oneMonthArrayList").hide();
          	$("#weekArrayList").hide();
          	$("#threeMonthArrayList").show();
          	$("#threeMonthArrayList").html(html);
          }
      }
  });
	
}
//最新标签排行榜列表 showListType：3 
function lastLabelRankingList(){
	$("#loading").show();
	$("#weekArrayList").empty();
	$("#oneMonthArrayList").empty();
	$("#threeMonthArrayList").empty();
	$("#nameType").html("最新发布标签");
	$("#lastLabelRankId").parent().find("li").find("a").removeClass("current");
	$("#lastLabelRankId").find("a").addClass("current");
	$(".dateArraySwitchBox").hide();
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findlastPublishRankLabel.ai2do";
	var para = {};
	$.ajax({
		url:actionUrl,
		type:"POST",
		dataType: "html",
		data: para,
		success:function(html){
			$("#loading").hide();
	    	$("#oneMonthArrayList").hide();
	    	$("#threeMonthArrayList").hide();
	    	$("#weekArrayList").show();
	        $("#weekArrayList").html(html);
		}
	});
}
//最新客户群排行榜列表showListType：4 
function lastCustomRankingList(){
	$("#loading").show();
	$("#weekArrayList").empty();
	$("#oneMonthArrayList").empty();
	$("#threeMonthArrayList").empty();
	$("#nameType").html("最新发布客户群");
	$("showListType").attr("value",4);
	$(".dateArraySwitchBox").hide();
	$("#lastCustomRankId").parent().find("li").find("a").removeClass("current");
	$("#lastCustomRankId").find("a").addClass("current");
	var actionUrl = $.ctx + "/ci/customersManagerAction!latestCustomerList.ai2do";
	var para = {};
	$.ajax({
		url:actionUrl,
		type:"POST",
		dataType: "html",
		data: para,
		success:function(html){
			$("#loading").hide();
	    	$("#oneMonthArrayList").hide();
	    	$("#threeMonthArrayList").hide();
	    	$("#weekArrayList").show();
	        $("#weekArrayList").html(html);
		}
	});
}
//根据时间段类型(一周、一个月、三个月)来刷新页面
function showTimeTypeList(timeType){
	var showListType = $("#showListType").val();
	if(timeType == 1){//近一周内
		$("#oneMonthArray").removeClass("current");
		$("#threeMonthArray").removeClass("current");
		$("#weekArray").addClass("current");
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSysHotRankLabel.ai2do?dataScope=oneWeek";
		if(showListType == 1){//系统热门标签排行榜列表 showListType：1 
			sysHotLabelRankingList(1);
		}else if(showListType == 2){//系统热门客户群排行榜列表 showListType：2 
			sysHotCustomRankingList(1);
		}else if(showListType == 3){//最新标签排行榜列表 showListType：3 
			lastLabelRankingList(1);
		}else{//最新客户群排行榜列表showListType：4 
			lastCustomRankingList(1);
		}
	}else if(timeType == 2){//近一个月内
		$("#threeMonthArray").removeClass("current");
		$("#weekArray").removeClass("current");
		$("#oneMonthArray").addClass("current");
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSysHotRankLabel.ai2do?dataScope=oneMonth";
		if(showListType == 1){//系统热门标签排行榜列表 showListType：1 
			sysHotLabelRankingList(2);
		}else if(showListType == 2){//系统热门客户群排行榜列表 showListType：2 
			sysHotCustomRankingList(2);
		}else if(showListType == 3){//最新标签排行榜列表 showListType：3 
			lastLabelRankingList(2);
		}else{//最新客户群排行榜列表showListType：4 
			lastCustomRankingList(2);
		}
	}else{//近三个月内
		$("#oneMonthArray").removeClass("current");
		$("#weekArray").removeClass("current");
		$("#threeMonthArray").addClass("current");
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSysHotRankLabel.ai2do?dataScope=threeMonth";
		if(showListType == 1){//系统热门标签排行榜列表 showListType：1 
			sysHotLabelRankingList(3);
		}else if(showListType == 2){//系统热门客户群排行榜列表 showListType：2 
			sysHotCustomRankingList(3);
		}else if(showListType == 3){//最新标签排行榜列表 showListType：3 
			lastLabelRankingList(3);
		}else{//最新客户群排行榜列表showListType：4 
			lastCustomRankingList(3);
		}
	}
	
}
//排行榜 系统热门标签：1 最新发布标签：2 系统热门客户群：3 最新客户群：4
function rankingListIndex(typeId){
	$("#importClientBtnTip").hide();
	$("#firsttimelabel_tip").hide();
	$("#viewNavBox").removeClass("viewNavBoxClick");
	$(".pathNavBox").remove();
	$(window).unbind("scroll");
	//清空地图
	$("#rankingList").empty();
	$("#rankingList").load("${ctx}/ci/ciIndexAction!rankingListIndex.ai2do",function (){
		if(typeId == 1){
			sysHotLabelRankingList(1);
		}else if(typeId == 2){
			lastLabelRankingList(1);
		}else if(typeId == 3){
			sysHotCustomRankingList(1);
		}else{
			lastCustomRankingList(1);
		}
	});
}
</script>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
<!-- 分类开始 -->    
<div class="navBox comWidth" id = "navBox">
	 <div class="fleft navListBox">
	    <ol>
	    	<li class="current"><a href="javascript:void(0);" onclick="rankingListIndex(1)">排行榜</a></li>
		</ol>
	 </div>
  </div>
</div>
<div id="rankingList"></div>
