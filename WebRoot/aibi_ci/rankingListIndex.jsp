<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@page import="com.ailk.biapp.ci.util.cache.CacheBase"%>
<c:set var="DEFAULT_SCENE" value='<%=CacheBase.getInstance().getDimScene("1")%>'></c:set>
<script type="text/javascript">
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
</script>
<form id="multiSearchForm" >
	<input type="hidden" id="showListType"/>
	<input type="hidden" id="sceneName_"/>
	<input type="hidden" id="isAttention"/>
	<!--  页面路径导航  -->
	<div class="comWidth pathNavBox">
	     <span class="pathNavHome"> 排行榜</span>
	     <span class="pathNavArrowRight"></span>
	     <span class="pathNavNext" id="nameType"></span>
	</div>
	<!-- 排行榜 -->
  <div class="comWidth arrayListBox">
      <div class="fleft arrayListLeftBox">
	     <ol>
		   <li id="sysHotLabelRankId"> <a href="javascript:void(0);" class="current" onclick="sysHotLabelRankingList(1)"> 系统热门标签</a></li>
		   <li id="sysHotCustomRankId"> <a href="javascript:void(0);" onclick="sysHotCustomRankingList(1)"> 系统热门客户群</a></li>
		   <li id="lastLabelRankId"> <a href="javascript:void(0);" onclick="lastLabelRankingList()"> 最新发布标签</a></li>
		   <li id="lastCustomRankId"> <a href="javascript:void(0);" onclick="lastCustomRankingList()"> 最新发布客户群</a></li>
		 </ol>
	  </div>
	  <div class="fright arrayListRightBox">
	      <div class="dateArraySwitchBox">
		      <ol>
			     <li> 
				    <div  id="weekArray" class="current" arrayList= "weekArrayList" onclick="showTimeTypeList(1)">近一周排行 </div>
				</li> 
				<li> 
				    <div  id="oneMonthArray" arrayList= "oneMonthArrayList" onclick="showTimeTypeList(2)">近一个月排行 </div>
				</li> 
				<li> 
				    <div  id="threeMonthArray" arrayList= "threeMonthArrayList" onclick="showTimeTypeList(3)">近三个月排行 </div>
				</li> 
			 </ol>
		  </div>
		  <div id="loading" style="display:none;">正在加载，请稍候 ...</div>
		   <div class="tableStyleBox"  id="weekArrayList">
		   </div>
		   <div class="tableStyleBox hidden"  id="oneMonthArrayList">
		   </div>
		   <div class="tableStyleBox hidden"  id="threeMonthArrayList">
		   </div>
	  </div>
  </div>
</form>
