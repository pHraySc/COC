<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@page import="com.ailk.biapp.ci.util.cache.CacheBase"%><html>
<c:set var="DEFAULT_SCENE" value='<%=CacheBase.getInstance().getDimScene("1")%>'></c:set>
<script type="text/javascript">
$(document).ready( function() {
	$("#sceneIds").find("div").removeClass("current");
	var sceneName = '${DEFAULT_SCENE}';
	$("#"+sceneName+"1").addClass("current");
	showSysRecommendByScene(1,sceneName);
});
function showSysRecommendByScene(sceneId,sceneName){
	$("#isAttention").attr("value","");
	$("#sceneName_").attr("value",sceneName);
	$("#sceneId").attr("value",sceneId);
	$("#sceneNameNav").html(sceneName);
	$("#sceneIds").find("div").removeClass("current");
	$("#"+sceneName+sceneId).addClass("current");
	$("#sysRecommendList").empty();
	$("#sysRecommendList").load($.ctx + "/ci/ciIndexAction!showMarketNavigation.ai2do?searchBean.sceneId="+sceneId+"&searchBeanCustom.sceneId="+sceneId);
}
function showCollect(){
	$("#sceneName_").attr("value","");
	$("#sceneId").attr("value","");
	$("#isAttention").attr("value","true");
	$("#sceneNameNav").html("收藏");
	$("#sceneIds").find("div").removeClass("current");
	$("#collect").addClass("current");
	$("#sysRecommendList").empty();
	$("#sysRecommendList").load($.ctx + "/ci/ciIndexAction!showMarketNavigation.ai2do?searchBean.isAttention=true&searchBeanCustom.isAttention=true");
}

//客户群关注图标点击事件
function attentionCustomOper(ths){
	var customId = $(ths).attr("customGroupId");
	var isAttention = $("#isAttentionCustom_"+customId).val();
	if(isAttention == 'true'){
		//已经关注，点击进行取消关注动作
		//alert('已经关注，点击进行取消关注动作');
		var url=$.ctx+'/ci/customersManagerAction!delUserAttentionCustom.ai2do';
		$.ajax({

			type: "POST",
			url: url,
	      	data:{'customGroupId':customId},
	      	success: function(result){
		       	if(result.success) {
		       		//操作成功 弱提示
		       		$.fn.weakTip({"tipTxt":result.message,"backFn":function(){
						switchWay();
		       	    }});
		       		//window.parent.showAlert(result.message,"success", switchWay);
				}else{
					commonUtil.create_alert_dialog("failedDialog", {
			   			 "txt":result.message,
			   			 "type":"failed",
			   			 "width":500,
			   			 "height":200
			   		}); 
				}
			}
		});
	}else{
		//没有关注，点击进行关注动作触发
		//alert('没有关注，点击进行关注动作触发');
		var url=$.ctx+'/ci/customersManagerAction!addUserAttentionCustom.ai2do';
		$.ajax({
			type: "POST",
				url: url,
				data:{'customGroupId':customId},
				success: function(result){
				if(result.success) {
					$.fn.weakTip({"tipTxt":result.message,"backFn":function(){
						switchWay();
		       	    }});
					//window.parent.showAlert(result.message,"success", switchWay);
					$("#isAttention_"+customId).val('true');
					$("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
					$("#attentionImg_"+customId).attr("title",'点击取消客户群收藏');
					$("#attentionImg_"+customId).attr("title","点击取消客群户收藏");
				}else{
					commonUtil.create_alert_dialog("failedDialog", {
			   			 "txt":result.message,
			   			 "type":"failed",
			   			 "width":500,
			   			 "height":200
			   		}); 
				}
			}
        });
	}
	
}

//选择刷新哪种页面：收藏还是场景
function switchWay(){
	var isAttention = $("#isAttention").attr("value");
	var sceneName = $("#sceneName_").attr("value");
	var sceneId = $("#sceneId").attr("value");
	if($("#isAttention").attr("value") == ""){
		showSysRecommendByScene(sceneId,sceneName);
	}else{
		showCollect();
	}
}

//标签关注图标点击事件
function attentionLabelOper(ths){
	var labelId = $(ths).attr("labelId");
	var isAttention = $("#isAttentionLabel_"+labelId).val();
	if(isAttention == 'true'){
		//已经关注，点击进行取消关注动作
		var url=$.ctx+'/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
		$.ajax({
			type: "POST",
			url: url,
	      	data:{'labelId':labelId},
	      	success: function(result){
		       	if(result.success) {
		       		$.fn.weakTip({"tipTxt":result.message,"backFn":function(){
						switchWay();
		       	    }});
		       		//window.parent.showAlert(result.message,"success",switchWay);
				}else{
					commonUtil.create_alert_dialog("failedDialog", {
			   			 "txt":result.message,
			   			 "type":"failed",
			   			 "width":500,
			   			 "height":200
			   		}); 
				}
			}
		});
	}else{
		//没有关注，点击进行关注动作触发
		var url=$.ctx+'/ci/ciLabelAnalysisAction!addUserAttentionLabel.ai2do';
		$.ajax({
			type: "POST",
				url: url,
				data:{'labelId':labelId},
				success: function(result){
				if(result.success) {
					$.fn.weakTip({"tipTxt":result.message,"backFn":function(){
						switchWay();
		       	    }});
					//window.parent.showAlert(result.message,"success",switchWay);
				}else{
					commonUtil.create_alert_dialog("failedDialog", {
			   			 "txt":result.message,
			   			 "type":"failed",
			   			 "width":500,
			   			 "height":200
			   		}); 
				}
			}
        });
	}
}

//获得更多标签 1;获得更多客户群 2
function getMoreLabelAndCustom(typeId){
	var isAttention = $("#isAttention").attr("value");
	var sceneName = $("#sceneName_").attr("value");
	var sceneId = $("#sceneId").attr("value");
	if($("#isAttention").attr("value") == ""){
		if(typeId == 1){
			forwardMoreLabelMarket(sceneId,sceneName);
		}else{
			forwardMoreGustomGroupMarket(sceneId,sceneName);
		}
		showSysRecommendByScene(sceneId,sceneName);
	}else{
		var url = "";
		if(typeId == 1){
			url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=5&attentionTypeId=1";
		}else{
			url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=5&attentionTypeId=2";
		}
	    window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
}
</script>
<!--  页面路径导航  -->
<form id="multiSearchForm" >
<input type="hidden" id="sceneId"/>
<input type="hidden" id="sceneName_"/>
<input type="hidden" id="isAttention"/>
  <div class="comWidth pathNavBox">
       <span class="pathNavHome"> 营销导航</span>
       <span class="pathNavArrowRight"></span>
       <span class="pathNavNext" id="sceneNameNav"></span>
  </div>
<div class="comWidth marketNavBox"> 
    <ol id="sceneIds">
    <c:forEach items="${dimScenes }" var="dimScene" varStatus="statusL1">
    <c:if test="${dimScene.sceneId > 0 }">
	  <li>
	     <div id="${dimScene.sceneName }${dimScene.sceneId}" class="marketNav${statusL1.count-2 }" onclick="showSysRecommendByScene(${dimScene.sceneId},'${dimScene.sceneName }')">
		    <p></p>
		    <h4>${dimScene.sceneName }</h4>
		 </div>
	  </li>
    </c:if>
    </c:forEach>
	   <li>
	     <div id="collect" class="marketNav8" onclick="showCollect()">
		    <p></p>
		    <h4>收藏</h4>
		 </div>
	  </li>
	</ol>
 </div>
 <div id="sysRecommendList"></div>
</form>
