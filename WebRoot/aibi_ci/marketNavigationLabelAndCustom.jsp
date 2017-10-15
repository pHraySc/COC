<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
%>
<c:set var="LABEL_TYPE_SIGN" value="<%=ServiceConstants.LABEL_TYPE_SIGN%>"></c:set>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="isNotContainLocalList" value="<%=isNotContainLocalList %>"></c:set>
<script type="text/javascript">
$(function(){
	$(document).click(function(){
		$(".label_custom_show").removeClass("onActive");
	});
});
$(document).ready( function() {
	var indexClick;
	var titleSceneName = $.trim($("#sceneNameNav").text());
	if(titleSceneName == '收藏'){
		titleSceneName = "我的收藏";
		$("#marketHelpCusotmText").text("推荐规则：1、优先展示"+titleSceneName+"中的官方推荐客户群，其次为系统热门排行客户群；2、最多展示20个客户群。");
		$("#marketHelpLabelText").text("推荐规则：1、优先展示"+titleSceneName+"中的官方推荐标签，其次为系统热门排行标签；2、最多展示20个标签。");
		$("#navigationCustomName").text("我收藏的客户群");
		$("#navigationLabelName").text("我收藏的标签");
	}else{
		titleSceneName = titleSceneName + "分类";
		$("#marketHelpCusotmText").text("推荐规则：1、优先展示"+titleSceneName+"中的官方推荐客户群，其次为系统热门排行客户群；2、最多展示20个客户群。");
		$("#marketHelpLabelText").text("推荐规则：1、优先展示"+titleSceneName+"中的官方推荐标签，其次为系统热门排行标签；2、最多展示20个标签。");
		$("#navigationCustomName").text("系统推荐客户群");
		$("#navigationLabelName").text("系统推荐标签");
	}
	//绑定隐藏提示层
	$(document).click(function(e){
	    var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	    $("#marketTipBox").hide();
	});
  	//帮助中心关闭事件 
	$(".marketHelpClose").click(function(){
	   var $parent  =$(this).parent();
           $parent.slideUp(300,function(){
		       $(this).prev().find(".marketHelp").removeClass("marketHelpActive");
		   });
		   
	});

	//点击事件切换
	$(".marketHelp").click(function(){
		if($(this).parent().next(".marketHelpDetailBox").is(":visible")){
            $(this).removeClass("marketHelpActive");
	        $(this).parent().next(".marketHelpDetailBox").slideUp(300);
		}else{
            $(this).addClass("marketHelpActive");
	        $(this).parent().next(".marketHelpDetailBox").slideDown(300);
		}
	  
	});

});
//标签详情
function showLabelDetail(ths,e){
	switchOnActive(ths,e);
	setTimeout(function(){showLabelDetail_(ths,e)},500);
}
//客户群详情
function showCustomDetail(ths,e){
	switchOnActive(ths,e);
	setTimeout(function(){showCustomDetail_(ths,e)},500);
}
//选中状态变更
function switchOnActive(ths,e){
	var index = $(".label_custom_show").index($(ths));
	$(".label_custom_show").each(function(i ,item){
		if(i!=index ){
			$(item).removeClass("onActive");
		}
	})
	if($(ths).hasClass("onActive")){
		indexClick=true;
		$(ths).removeClass("onActive");
	}else{
		$(ths).addClass("onActive");
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		indexClick=false;
	}
}

//显示标签详情
function showLabelDetail_(ths,e){
	if(indexClick!=false){
		return ;
	}else{
		var labelId = $(ths).attr('labelId');
		var url = $.ctx + '/ci/ciIndexAction!findLabelTipInfo.ai2do';
		var posx=Math.round($(ths).offset().left+$(ths).width()/2-170) + "px";
		var posy=Math.round($(ths).offset().top+$(ths).height() )+"px";
		$.post(url, {labelId : labelId}, function(response) {
				var labelInfo = response.labelInfo;
				var cycleStr = '日周期';
				var customNumDisplay = '';
				if(labelInfo.updateCycle == 2) {
					cycleStr = '月周期';
				}
				if(labelInfo.labelTypeId == 1 && labelInfo.customerNum != -1) {
					customNumDisplay = '<th>用户数：</th>' + 
		                '<td>' + labelInfo.customerNum + '</td>';
				}
				var desc = labelInfo.busiCaliber;
				if(desc == null) {desc = ''}
				
				var sDate = labelInfo.effecTimeStr;
				var eDate = labelInfo.failTimeStr;
				if(sDate == null) {sDate = '--'}
				if(eDate == null) {eDate = '--'}
				var html = '<div class="labelTipTop">&nbsp;</div>'+
				   '<div class="labelTipInner">'+
					'<div class="maintext">' + desc + '</div>'+
				 '<table>' + 
					'<tr>' + 
				     '<th width="85">更新周期：</th>' + 
				     '<td>' + cycleStr + '</td>' + 
				     '<th width="85">创建时间：</th>' + 
				     '<td>' + labelInfo.createTimeStr + '</td>' + 
				 '</tr>' + 
				 '<tr>' +
				     '<th>最新数据时间：</th>' + 
				     '<td>' + labelInfo.newDataDate + '</td>' + 
				     customNumDisplay + 
				 '</tr>' + 
					'</table>' + 
					'<div class="marketTipBottomBox">'+
		        '<div class="fleft">';
		        if($(ths).attr("isSysRecom") != null && $(ths).attr("isSysRecom") == 1){
		        	html += '<a href="javascript:void(0);" class="organ" title="系统推荐标签"/>';
		        }
		        if($(ths).attr("isHot") == 'true'){
		        	html += '<a href="javascript:void(0);" class="hot" title="系统热门标签"/>';
		        }
		        if($(ths).attr("isAttention") == 'true'){
		        	html += '<a href="javascript:void(0);" class="hideFont" title="我收藏的标签"/>';
		        }
		        if($(ths).attr("isHot") == 'true' || $(ths).attr("isAttention") == 'true' || ($(ths).attr("isSysRecom") != null && $(ths).attr("isSysRecom") == 1)){
					html += '<span class="storeSegLine"></span>';
			    }
		        if($(ths).attr("isAttention") == 'false'){
		        	html += '<input type="hidden" id="isAttentionLabel_'+labelId+'" value="'+$(ths).attr("isAttention")+'" />';
		        	html += '<a href="javascript:void(0);"  onclick="attentionLabelOper(this)" labelId="'+labelId+'" class="store" >收藏</a>';
		        }
		        if($(ths).attr("isAttention") == 'true'){
		        	html += '<input type="hidden" id="isAttentionLabel_'+labelId+'" value="'+$(ths).attr("isAttention")+'" />';
		        	html += '<a href="javascript:void(0);"  onclick="attentionLabelOper(this)" labelId="'+labelId+'" class="store" >取消收藏</a>';
		        }
		        html += '</div>'+
				'<div class="fright">'+
				   '<label>有效期：</label>'+
	            '<span>' + sDate + '</span>'+
				   '<span>至</span>'+
				   '<span>' + eDate + '</span>'+
				'</div>'+
		    '</div>'+
			'</div>';
				var _tip=$(html);
				$("#marketTipBox").empty().append(_tip);
				$("#marketTipBox").css({"left":posx,"top":posy}).show().click(function(){
					  return false;
				});
			});
		return false;
	}
}
//显示客户群详情
function showCustomDetail_(ths,e){
	if(indexClick!=false){
		return ;
	}else{
		var customGroupId = $(ths).attr('customId');
		var url = $.ctx + '/ci/ciIndexAction!findCustomTipInfo.ai2do';
		var posx=Math.round($(ths).offset().left+$(ths).width()/2-170) + "px";
		var posy=Math.round($(ths).offset().top+$(ths).height() )+"px";
		$.post(url, {'ciCustomGroupInfo.customGroupId' : customGroupId}, function(response) {
				var customGroupInfo = response.customGroupInfo;
				var productDate		= customGroupInfo.productDate;
				var desc 			= customGroupInfo.customGroupDesc;
				var customNum       =  customGroupInfo.customNum;
				if(desc == null) {desc = ''}
				var sDate = customGroupInfo.startDateStr;
				var eDate = customGroupInfo.endDateStr;
				if(sDate == null) {sDate = '--'}
				if(eDate == null) {eDate = '--'}
				if(customNum == null) {customNum = '-'}
				if(productDate == null) {productDate = '--'}
				var html = '<div class="labelTipTop">&nbsp;</div>'+
					   '<div class="labelTipInner">'+
				'<div class="maintext">' + desc + '</div>'+
				 '<table>' + 
					'<tr>' + 
			         '<th width="85">更新周期：</th>';
			         if(customGroupInfo.updateCycle == 1) {
				         html += '<td>一次性</td>';
			         }else if(customGroupInfo.updateCycle == 2){
				         html += '<td>月周期</td>';
			         }else if(customGroupInfo.updateCycle == 3){
				         html += '<td>日周期</td>';
			         }else if(customGroupInfo.updateCycle == 4){
				         html += '<td>无</td>';
			         }
			         html += '<th width="85">创建时间：</th>' + 
			         '<td>' + customGroupInfo.createTimeView + '</td>' + 
			     '</tr>' + 
			     '<tr>' +
			         '<th>用户数：</th>' + 
			         '<td>' + customNum + '</td>' + 
			         '<th>最新数据时间：</th>' + 
			         '<td>' + productDate + '</td>' + 
			     '</tr>' + 
			 	'</table>' + 
				'<div class="marketTipBottomBox">'+
			        '<div class="fleft">';
			        if($(ths).attr("isSysRecom") != null && $(ths).attr("isSysRecom") == 1){
			        	html += '<a href="javascript:void(0);" class="organ" title="系统推荐客户群"/>';
			        }
			        if($(ths).attr("isHotCustom") == 'true'){
			        	html += '<a href="javascript:void(0);" class="hot" title="系统热门客户群"/>';
			        }
			        if($(ths).attr("isAttention") == 'true'){
			        	html += '<a href="javascript:void(0);" class="hideFont" title="我收藏客户群"/>';
			        }
			        if($(ths).attr("isHotCustom") == 'true' || $(ths).attr("isAttention") == 'true' || ($(ths).attr("isSysRecom") != null && $(ths).attr("isSysRecom") == 1)){
						html += '<span class="storeSegLine"></span>';
				    }
			        if($(ths).attr("isAttention") == 'false'){
			        	html += '<input type="hidden" id="isAttentionCustom_'+customGroupId+'" value="'+$(ths).attr("isAttention")+'" />';
			        	html += '<a href="javascript:void(0);" customGroupId='+customGroupId+'  onclick="attentionCustomOper(this)" class="store" >收藏</a>';
			        }
			        if($(ths).attr("isAttention") == 'true'){
			        	html += '<input type="hidden" id="isAttentionCustom_'+customGroupId+'" value="'+$(ths).attr("isAttention")+'" />';
			        	html += '<a href="javascript:void(0);" customGroupId='+customGroupId+'  onclick="attentionCustomOper(this)" class="store" >取消收藏</a>';
			        }
			        html += '</div>';
			        if(customGroupInfo.updateCycle == 2){
			        	html += '<div class="fright">'+
						   '<label>有效期：</label>'+
			               '<span>' + sDate + '</span>'+
						   '<span>至</span>'+
						   '<span>' + eDate + '</span>'+
						'</div>';
			         }
			        
			        html += '</div>'+'</div>';
			var _tip=$(html);
			$("#marketTipBox").empty().append(_tip);
			$("#marketTipBox").css({"left":posx,"top":posy}).show().click(function(){
				  return false;
			});
		});
		return false;
	}
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
</script>
<!--  页面路径导航  -->
 <div class="comWidth marketContentBox"> 
    <div class="marketContentTitle">
	    <h4 id="navigationCustomName">  系统推荐客户群</h4>
	   <a href="javascript:void(0);" class="marketHelp" >
		    <p></p>
	   </a>
	   <a href="javascript:getMoreLabelAndCustom(2);" class="fright marketMore" >更多></a>
	</div>
	<div class="marketHelpDetailBox">
	   <div class="marketHelpPointer"></div>
	   <a href="javascript:void(0);" class="marketHelpClose"></a>
	   <ul>
		  <li id="marketHelpCusotmText"></li>
		  <li>
		     名词解释： <span class="organ">&nbsp;</span>  表示官方推荐客户群；    <span class="hot">&nbsp;</span>表示该客户群为系统中全量客户群排行TOP99的客户群；   <span class="hideFont">&nbsp;</span> 表示您自己收藏的客户群，可以在收藏夹中查看。
		  </li>
	  </ul>
	</div>
    <ol>
    <c:forEach items="${recommendCustomInfos }" var="recommendCustomInfo">
	  <li>
	  <c:if test="${recommendCustomInfo.isSysRecom!=null && recommendCustomInfo.isSysRecom==1 }"><a href="javascript:void(0);" class="organ" title="系统推荐客户群"/></c:if>
	  <c:if test="${recommendCustomInfo.isHotCustom=='true' }"><a href="javascript:void(0);" class="hot" title="系统热门客户群"/></c:if>
	  <c:if test="${recommendCustomInfo.isAttention=='true' }"><a href="javascript:void(0);" class="hideFont" title="我收藏的客户群"/></c:if>
	  <a class="label_custom_show" href="javascript:void(0);" <c:choose><c:when test='${!(recommendCustomInfo.isLabelOffline == 1 && recommendCustomInfo.updateCycle == 4) || recommendCustomInfo.createCityId == root || recommendCustomInfo.createCityId == cityId || recommendCustomInfo.isContainLocalList == isNotContainLocalList }'>ondblclick="customValidate(event,this,2);"</c:when><c:otherwise>
	  isPrivate="${recommendCustomInfo.isPrivate }" createUserId="${recommendCustomInfo.createUserId }" createCityId="${recommendCustomInfo.createCityId }" isContainLocalList="${recommendCustomInfo.isContainLocalList }" ondblclick="failAddShoppingCar(this);"
	  </c:otherwise></c:choose> onclick="showCustomDetail(this);" isHotCustom="${recommendCustomInfo.isHotCustom }" isAttention="${recommendCustomInfo.isAttention }" isSysRecom="${recommendCustomInfo.isSysRecom }" customId="${recommendCustomInfo.customGroupId }" element="${recommendCustomInfo.customGroupId }">${recommendCustomInfo.customGroupName } </a>
	  </li>
    </c:forEach>
	</ol>
 </div>
 <div class="comWidth marketContentBox"> 
    <div class="marketContentTitle">
	   <h4 id="navigationLabelName">  系统推荐标签</h4>
	   <a href="javascript:void(0);" class="marketHelp" >
		    <p></p>
	   </a>
	   <a href="javascript:getMoreLabelAndCustom(1);" class="fright marketMore">更多></a>
	</div>
	<div class="marketHelpDetailBox">
	   <div class="marketHelpPointer"></div>
	   <a href="javascript:void(0);" class="marketHelpClose"></a>
	   <ul>
		  <li id="marketHelpLabelText"></li>
		  <li>
		     名词解释： <span class="organ">&nbsp;</span>  表示官方推荐标签；    <span class="hot">&nbsp;</span>表示该标签为系统中全量标签排行TOP99的标签；   <span class="hideFont">&nbsp;</span> 表示您自己收藏的标签，可以在收藏夹中查看。
		  </li>
	  </ul>
	</div>
    <ol>
    <c:forEach items="${recommendLabelIfos }" var="recommendLabelIfo">
	    <li> 
		<c:if test="${recommendLabelIfo.isSysRecom!=null && recommendLabelIfo.isSysRecom==1 }"><a  href="javascript:void(0);"  class="organ" title="系统推荐标签"/></c:if>
	    <c:if test="${recommendLabelIfo.isHot=='true' }"><a href="javascript:void(0);" class="hot" title="系统热门标签"/></c:if>
		<c:if test="${recommendLabelIfo.isAttention=='true' }"><a  href="javascript:void(0);"  class="hideFont" title="我收藏的标签"/></c:if>
	    <a class="label_custom_show" href="javascript:void(0);" ondblclick="addToShoppingCar(event,this,1);" onclick="showLabelDetail(this);" labelId="${recommendLabelIfo.labelId }" isHot="${recommendLabelIfo.isHot }" isSysRecom="${recommendLabelIfo.isSysRecom }" isAttention="${recommendLabelIfo.isAttention }" element="${recommendLabelIfo.labelId }">${recommendLabelIfo.labelName  }</a>
	  	</li>
    </c:forEach>
	</ol>
 </div>
  <!--提示层 -->
<div  id="marketTipBox" class="labelTip"></div>
