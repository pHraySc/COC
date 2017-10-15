
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<%
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="isNotContainLocalList" value="<%=isNotContainLocalList %>"></c:set>
<script type="text/javascript">
$(function(){
	if('${attentionTypeId}' == 2){
		backMultiCustom();
	}else{
		multiLabelSearch();
	}
});
function multiLabelSearch(){
	$("#fixedSearchBox").hide();
	$("#labelAttention").parent().find("li").removeClass("current");
	$("#labelAttention").addClass("current");
	$("#attentionList").empty();
	$("#attentionList").load("${ctx}/aibi_ci/ciLabelInfo/userAttentionLabel.jsp",function(){loadLabelList();});
}
function backMultiCustom(){
	$("#fixedSearchBox").hide();
	$("#customAttention").parent().find("li").removeClass("current");
	$("#customAttention").addClass("current");
	$("#attentionList").empty();
	$("#attentionList").load("${ctx}/aibi_ci/customers/userAttentionCustom.jsp",function(){loadCustomList();});
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#createDialog").dialog("close");
	clearCondition();
}
function failAddShoppingCar(ths){
	var createCityId = $(ths).attr("createCityId");
	var createUserId = $(ths).attr("createUserId");
	var isPrivate = $(ths).attr("isPrivate");
	var isContainLocalList = $(ths).attr("isContainLocalList");
	var userId = "${userId}";
	var cityId = "${cityId}";
	var root = "${root}";
	if(isPrivate == 0){
		if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
			$.fn.weakTip({"tipTxt":"抱歉，您无权限添加到收纳篮！" });
		}else{
			$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！" });
		}
	}else{
		if(createUserId != userId){
			$.fn.weakTip({"tipTxt":"抱歉，您无权限添加到收纳篮!" });
		}else{
			$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！" });
		}
	}
}
//显示客户群操作列表
function showOperCustomList(e,ths,customId){
	var selfIndex = $(".resultItemActive").index($(ths));
	$.each($(".resultItemActive"),function(index,item){
		if(index != selfIndex){
		 var $resultItemActiveList= $(item).find(".resultItemActiveList");
		     $resultItemActiveList.hide();
		     $(item).removeClass("resultItemActiveOption");
		}
	 })
	if($('#'+customId).children("ol").children("li").length == 0){
		var createCityId = $(ths).attr("createCityId");
		var createUserId = $(ths).attr("createUserId");
		var isPrivate = $(ths).attr("isPrivate");
		var isContainLocalList = $(ths).attr("isContainLocalList");
		var userId = "${userId}";
		var cityId = "${cityId}";
		var root = "${root}";
		if(isPrivate == 0){
			if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
				alert(2);
				$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
			}else{
				$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，无操作！" });
			}
		}else{
			if(createUserId != userId){
				$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
			}else{
				$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，无操作！" });
			}
		}
	}else{
		$('#'+customId).children("ol").children("li:last-child").addClass("end");
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
//显示标签操作列表
function showOperLabelList(e,ths,labelId){
	var selfIndex = $(".resultItemActive").index($(ths));
	$.each($(".resultItemActive"),function(index,item){
		if(index != selfIndex){
		 var $resultItemActiveList= $(item).find(".resultItemActiveList");
		     $resultItemActiveList.hide();
		     $(item).removeClass("resultItemActiveOption");
		}
	 })
	if($('#'+labelId).children("ol").children("ul").children("li").length > 0){
		$('#'+labelId).children("ol").children("ul").children("li:last-child").addClass("end");
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
function customValidate(e,ths,typeId){
	var customId = $(ths).attr("element");
	var url = $.ctx + '/ci/ciIndexAction!findCusotmValidate.ai2do?ciCustomGroupInfo.customGroupId='+customId;
	$.ajax({
		url: url,
		type: "POST",
		async: false,
		success: function(result){
			if(result.success){
				addToShoppingCar(e,ths,typeId);
			}else{
				$.fn.weakTip({"tipTxt":result.msg });
			}
		}
	});
}
function customValidate2(customGroupId,typeId,isEditCustomFlag){
	var url = $.ctx + '/ci/ciIndexAction!findCusotmValidate.ai2do?ciCustomGroupInfo.customGroupId='+customGroupId;
	$.ajax({
		url: url,
		type: "POST",
		async: false,
		success: function(result){
			if(result.success){
				addShopCart(customGroupId,typeId,isEditCustomFlag);
			}else{
				$.fn.weakTip({"tipTxt":result.msg });
			}
		}
	});
}
//展示客户群规则
function showRule(id){
	var url = $.ctx + "/ci/customersManagerAction!showRule.ai2do?ciCustomGroupInfo.customGroupId="+id;
	var dlgObj = dialogUtil.create_dialog("queryGroupRuleDialog", {
		"title" :  "客户群的完整规则",
		"height": "auto",
		"width" : 800,
		"frameSrc" : url,
		"frameHeight":400,
		"position" : ['center','center'] 
	}).focus(); 
}
</script>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
<!-- 分类开始 -->    
	<div class="navBox comWidth" id = "navBox">
		<div class="fleft navListBox">
			<ol>
				<li id="labelAttention" class="current"><a href="javascript:void(0);" onclick="multiLabelSearch()">标签收藏</a></li>
				<li id="customAttention"><a href="javascript:void(0);" onclick="backMultiCustom()">客户群收藏</a></li>
			</ol>
		</div>
	</div>
</div>
<div id="attentionList"></div>
