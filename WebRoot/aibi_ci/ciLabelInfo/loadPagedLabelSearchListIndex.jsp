<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(document).ready( function() {
	  $("#viewNavBox .viewNavItem").hoverForIE6({current:"viewNavItemHover",delay:200});
 	  //工具栏定位
	  var bodyWidth = $("body").width();
	  var toolTipRight= parseInt((bodyWidth-1005)/2,10)-32 ;
	  toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
	  $("#toolTip").css("right",toolTipRight);
	  $("#shopCart").css("right",parseInt((bodyWidth-1005)/2,10));
	  //查询条件筛选条件删除
	  $(".selectedItemClose").click(function(){
	     var selectedDivId = $(this).parent().children().first(); 
	     $(this).parent().remove();
	     multiSearch(selectedDivId);
	  });
	  //输入框获得焦点显示按钮，失去焦点隐藏按钮
		$("#startDate").bind({
		      focus: function() {
		      	$("#dateSelf").show();
		      },
		      blur: function() {
		      	if($("#startDate").val() == "" && $("#endDate").val() == ""){
		       		$("#dateSelf").css({display:"none"}); 
		      	}
		      }
		});
	   	$("#endDate").bind({
	        focus: function() {
	        	$("#dateSelf").show();
	        },
	        blur: function() {
	        	if($("#startDate").val() == "" && $("#endDate").val() == ""){
		        	$("#dateSelf").css({display:"none"}); 
	        	}
	        }
	 	});
	   	$(".dateSelfDiv").bind({
	   		focus: function() {
				if($("#startDate").val() != "" || $("#endDate").val() != ""){
					$("#dateSelf").show();
				}
			},
	        blur: function() {
	        	if($("#startDate").val() == "" && $("#endDate").val() == ""){
		        	$("#dateSelf").css({display:"none"}); 
	        	}
	        }
	 	});
	   	//人气排序
	  $("#useNumOrder").hover(function(){
		    $(this).addClass("sortItemHover");
		},function(){
		    $(this).removeClass("sortItemHover");
		}).click(function(){
			if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
				$(this).attr("title","点击按人气从小到大排序");
			}else{
				if($(this).hasClass("desc")){
					$(this).attr("title","点击按人气从大到小排序");
				}else{
					$(this).attr("title","点击按人气从小到大排序");
				}
			}
			$("#sortList").find("li").each(function(i,v){
				if($(this).find("a").attr("id") != 'useNumOrder'){
					$(this).find("a").removeClass("desc");
					$(this).find("a").removeClass("asc");
				}
			});
			$("#sortList").find("li").find("a").removeClass("sortItemActive");
	 		$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
			if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
				$(this).addClass("sortItemActiveDown");
			}else{
				if($(this).hasClass("desc")){
					$(this).addClass("sortItemActive");
		           $(this).removeClass("sortItemActiveDown");
				}else{
				   $(this).addClass("sortItemActiveDown");
		           $(this).removeClass("sortItemActive");
				}
			}
			sortByName('USE_TIMES',this);
		});
	  //发布时间排序
	  $("#publishTimeOrder").hover(function(){
		    $(this).addClass("sortItemHover");
		},function(){
		    $(this).removeClass("sortItemHover");
		}).click(function(){
			if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
				$(this).attr("title","点击按标签发布时间从远至近排序");
			}else{
				if($(this).hasClass("desc")){
					$(this).attr("title","点击按标签发布时间从近至远排序");
				}else{
					$(this).attr("title","点击按标签发布时间从远至近排序");
				}
			}
			$("#sortList").find("li").each(function(i,v){
				if($(this).find("a").attr("id") != 'publishTimeOrder'){
					$(this).find("a").removeClass("desc");
					$(this).find("a").removeClass("asc");
				}
			});
			$("#sortList").find("li").find("a").removeClass("sortItemActive");
	 		$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
			if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
				$(this).addClass("sortItemActiveDown");
			}else{
				if($(this).hasClass("desc")){
					$(this).addClass("sortItemActive");
		           $(this).removeClass("sortItemActiveDown");
				}else{
				   $(this).addClass("sortItemActiveDown");
		           $(this).removeClass("sortItemActive");
				}
			}
			sortByName('PUBLISH_TIME',this);
		});
	  //添加你选项的查询条件
	  $(".selectOptionValue a").click(function(){
		  if($(this).parents("div").attr("id") != 'sceneName'){
			$(this).parent("li").parent("ul").children("li").each(function(){
	  			var elementE = $(this).children("a").attr("selectedElement");
	  			$(this).children("a").removeClass("selectOptionValueHover");
			});
		  }else{
			if($(this).attr("selectedElement") == "all"){
				$(this).parent("li").parent("ul").children("li").each(function(){
		  			var elementE = $(this).children("a").attr("selectedElement");
		  			$(this).children("a").removeClass("selectOptionValueHover");
				});
			}else{
				$("#sceneAll").removeClass("selectOptionValueHover");
			}
		  }
		  var ths = $(this);
		  $(this).addClass("selectOptionValueHover");
		  var closeLi = $(this).parent("li").parent("ul");
		  var selectedDivId = $(this).parents("div");
		  var selectedLiId = $(this).parent().attr("id");
		  var selectedLiIdStr = "";
		  if(selectedLiId != null && selectedLiId != ""){
			  selectedLiId = $.trim(selectedLiId).split("_");
			  selectedLiIdStr = selectedLiId[1];
		  }
		  var idStr = selectedDivId.attr("id")+"_"+selectedLiIdStr;
		  var value = $(this).html();
		  var flag = true;
	      var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		  var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\""+idStr+"\" >"+value+"</div>");
		  var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	      $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
          //同一种条件是互斥添加
	      huChi($selectedItemBox,idStr,$(this),flag,value);
  		
		  //绑定单击事件
		  $selectedItemClose.click(function(){
			  if(idStr.indexOf("sceneName") >= 0){
				  $(this).parent().remove();
				  ths.removeClass("selectOptionValueHover");
				  var f = false;
				  closeLi.children("li").each(function(){
					  f = $(this).children("a").hasClass("selectOptionValueHover");
					  if(f){return false;}
				  });
				  if(!f){
					  closeLi.children("li").each(function(){
				    	 if($(this).children("a").attr("selectedElement") == "all"){
				    		 $(this).children("a").addClass("selectOptionValueHover");
				    	 }
					  });
				  }
			  }else{
			     $(this).parent().remove();
			     closeLi.children("li").each(function(){
			    	 if($(this).children("a").attr("selectedElement") == "all"){
			    		 $(this).children("a").addClass("selectOptionValueHover");
			    	 }else{
			    		 $(this).children("a").removeClass("selectOptionValueHover");
			    	 }
				  });
			  }
		     multiLabelSearch();
		  });
		  multiLabelSearch();
	  });
});
//更多查询条件
$("#moreOptionActive").click(function(){
	if($("#moreSelectOptionList").is(":visible")){
   		$("#moreSelectOptionList").slideUp(500);
   		$(this).val("更多条件");
   		$(this).removeClass("up");
	}else{
   		$("#moreSelectOptionList").slideDown(500);
   		$(this).addClass("up");
   		$(this).val("收起");
	}
});
//标签收藏图标点击事件
function attentionLabelOper(labelId){
	var isAttention = $("#isAttention_"+labelId).val();
	if(isAttention == 'true'){
		//已经收藏，点击进行取消收藏动作
		var url=$.ctx+'/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data:{'labelId':labelId},
	      	success: function(result){
		       	if(result.success) {
		       		$.fn.weakTip({"tipTxt":result.message });
		       		//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+labelId).val('false');
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
					$("#attentionImg_"+labelId).attr("title",'点击添加标签收藏');
					$("#isAttentionShow_"+labelId).attr("style",'display: none');
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
		});
	}else{
		//没有收藏，点击进行收藏动作触发
		var url=$.ctx+'/ci/ciLabelAnalysisAction!addUserAttentionLabel.ai2do';
		$.ajax({
			type: "POST",
				url: url,
				data:{'labelId':labelId},
				success: function(result){
				if(result.success) {
					$.fn.weakTip({"tipTxt":result.message });
					$("#isAttention_"+labelId).val('true');
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
					$("#attentionImg_"+labelId).attr("title",'点击取消标签收藏');
					$("#isAttentionShow_"+labelId).attr("style",'display: block');
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
        });
	}
}
//标签默认排序
function sortLabelDefault(){
	$("#sortList").find("li").find("a").removeClass("sortItemActive");
	$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
	$("#sortList").find("li").find("a").removeClass("desc");
	$("#sortList").find("li").find("a").removeClass("asc");
	$("#useNumOrder").attr("title","点击按标签人气从大到小排序");
	$("#publishTimeOrder").attr("title","点击按标签发布时间从近至远排序");
	$("#order").val("");
	$("#orderBy").val("");
	multiLabelSearch();
}
//标签单排序
function sortByName(columnName,ele){
	var sort = "";
	if((!$(ele).hasClass("desc")) && (!$(ele).hasClass("asc"))){
		sort = "desc";
		$(ele).addClass("desc");
	}else{
		if($(ele).hasClass("desc")){
			$(ele).removeClass("desc");
			sort = "asc";
			$(ele).addClass("asc");
		}else{
			$(ele).removeClass("asc");
			sort = "desc";
			$(ele).addClass("desc");
		}
	}
	$("#order").val("");
	$("#orderBy").val("");
	$("#order").val(sort);
	$("#orderBy").val(columnName);
	multiLabelSearch();
}
//同一种条件是互斥添加
function huChi(selectedItemBox,selectedDivId,selectedObj,flag,value){
	var isScenceTypeArr = $.trim(selectedDivId).split("_");
	value = $.trim(value);
	if(isScenceTypeArr[0] == 'sceneName'){//标签分类 不互斥筛选
		var selectedE = "";
		if(selectedObj != null){
			selectedE = selectedObj.attr("selectedElement");
		}
	    if($(".selectedItemDetail").size() >= 1){
			if(selectedE != "all"){
				$(".selectedItemDetail").each(function(){
					var sceneName = $.trim($(this).html());
		          	if(value == sceneName){
		        	  	flag = false;
		          		return false;
		          	}else{
		          		flag = true;
		          	}
				});
	    	}else{
				$(".selectedItemDetail").each(function(){
					var idArr = $.trim($(this).attr("id")).split("_");
		          	if(idArr[0] == 'sceneName'){
		          		$(this).parent().remove();
		          	}
				});
				return false;
	    	}
			if(flag) $("#selectedItemListBox").append(selectedItemBox);
	    }else{
			if(selectedE != "all"){
				$("#selectedItemListBox").append(selectedItemBox);
			}
	    }
	}else{//互斥筛选
		var selectedE = "";
		if(selectedObj != null){
			selectedE = selectedObj.attr("selectedElement");
		}
	    if($(".selectedItemDetail").size() >= 1){
			$(".selectedItemDetail").each(function(){
	        	var idArr = $.trim($(this).attr("id")).split("_");
	        	var selectedDivIdArr = $.trim(selectedDivId).split("_");
	          	if(idArr[0] == selectedDivIdArr[0]){
	          		if(selectedE == "all"){
	          			$(this).parent().remove();
	          		}else{
		        	  	$(this).html(value);
		        	  	$(this).attr("id",selectedDivId);
	          		}
	        	  	flag = false;
	          		return false;
	          	}else{
	          		if(selectedE == "all"){
	          			flag = false;
	          		}else{
		          		flag = true;
	          		}
	          	}
			});
			if(flag) $("#selectedItemListBox").append(selectedItemBox);
	    }else{
			if(selectedE != "all"){
				$("#selectedItemListBox").append(selectedItemBox);
			}
	    }
	}
}
//点击时间筛选按钮
function shaiXuanDate(obj){
	obj.parent("div").parent("li").parent("ul").children("li").each(function(){
		  $(this).children("a").removeClass("selectOptionValueHover");
	});
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if("" != startDate && "" != endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >"+startDate+"至"+endDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType",null,true,startDate+"至"+endDate);
	  //绑定单击事件
		$selectedItemClose.click(function(){
			$("#dateSelf").css({display:"none"}); 
			$("#dateType li").each(function(){
			  	 if($(this).children("a").attr("selectedElement") == "all"){
			  		 $(this).children("a").addClass("selectOptionValueHover");
			  	 }else{
			  		 $(this).children("a").removeClass("selectOptionValueHover");
			  	 }
			});
		    $(this).parent().remove();
		    multiLabelSearch();
		});
		multiLabelSearch();
	}else if("" != startDate && "" == endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >大于"+startDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType",null,true,"大于"+startDate);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
			$("#dateSelf").css({display:"none"}); 
			$("#dateType li").each(function(){
			  	 if($(this).children("a").attr("selectedElement") == "all"){
			  		 $(this).children("a").addClass("selectOptionValueHover");
			  	 }else{
			  		 $(this).children("a").removeClass("selectOptionValueHover");
			  	 }
			});
			$(this).parent().remove();
			multiLabelSearch();
		});
		multiLabelSearch();
    }else if("" == startDate && "" != endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >小于"+endDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
		$selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
		huChi($selectedItemBox,"dateType",null,true,"小于"+endDate);
		//绑定单击事件
		$selectedItemClose.click(function(){
			$("#dateSelf").css({display:"none"}); 
			$("#dateType li").each(function(){
			  	 if($(this).children("a").attr("selectedElement") == "all"){
			  		 $(this).children("a").addClass("selectOptionValueHover");
			  	 }else{
			  		 $(this).children("a").removeClass("selectOptionValueHover");
			  	 }
			});
			$(this).parent().remove();
			multiLabelSearch();
		})
		multiLabelSearch();
    }
}

//标签设置系统推荐与取消系统推荐
var recomFlag = true;  
function changeRecomLabel(ths){
	if(recomFlag){
		recomFlag = false;
		var labelId = $(ths).attr("labelId");
		var actionUrl = $.ctx + "/ci/ciLabelInfoAction!changeRecommendLabel.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: {"labelId" : labelId},
			success: function(result){
				if(result.success){
					$.fn.weakTip({"tipTxt":"" +result.msg,"backFn":function(){
						multiLabelSearch();
						recomFlag = true;
					 }});
				}else{
					recomFlag = true;
					showAlert(result.msg,"failed");
				}
			}
		});
	}else{
		return false;
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
	});
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

</script>
<!--  页面路径导航  -->
<form id="multiSearchForm" >
<input type="hidden" id="formDataScope" name="dataScope" value="all"></input>
<input type="hidden" id="updateCycle" name="searchBean.updateCycle" ></input>
<input type="hidden" id="sceneIdL" name="searchBean.sceneId" ></input>
<input type="hidden" id="orderBy" name="pager.orderBy" ></input>
<input type="hidden" id="order" name="pager.order" ></input>
<div class="comWidth pathNavBox">
     <span class="pathNavHome inline-block" onclick="showRefreshLabelIndex()" style="cursor:default; "> 标签</span>
     <span class="pathNavArrowRight inline-block"></span>
     <span class="pathNavNext ellipsis"></span>
 	 <span class="amountItem inline-block"> 共<span class="amountNum"></span>个标签 </span>

</div>
<!-- 查询条件 -->
  <div class="comWidth queryItemBox">
     <!-- 选择结果 -->
     <div class="selectedItemList">
	    <dl>
		  <dt class="fleft">您的选择：</dt>
		  <dd class="fleft"  id="selectedItemListBox">
		  </dd>
		</dl>
	 </div>
     <!-- 选择条件列表 -->
	 <div  class="comWidth selectOptionBox clearfix">
	 <div class="selectOptionList ">
		     <div class="selectOptionKey">
		        标签分类:
		     </div>
			 <div class="selectOptionValue" id="sceneName">
		         <ul>
				    <li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);" id="sceneAll"> 全部</a></li>
                    <c:forEach items="${dimScenes }" var="dimScens">
                    <c:if test="${dimScens.sceneId > 0}">
	                    <li id="sceneName1_${dimScens.sceneId }"><a href="javascript:void(0);"> ${dimScens.sceneName }</a></li>
                    </c:if>
		         	</c:forEach>
				 </ul>
		     </div>
		</div>
	    <div class="selectOptionList ">
		     <div class="selectOptionKey">
		        发布时间:
		     </div>
			 <div class="selectOptionValue" id="dateType">
		         <ul>
				    <li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
                    <li><a href="javascript:void(0);"> 一天</a></li>
					<li><a href="javascript:void(0);"> 一个月</a></li>
					<li><a href="javascript:void(0);"> 三个月</a></li>
					<li>
					<div id="dateFilter" class="defineSection">
						<input name="searchBean.startDate" id="startDate" readonly type="text" class="defineDataInput datepickerBG"  
						onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}" 
						/> -- 
						<input name="searchBean.endDate" id="endDate" readonly type="text" class="defineDataInput datepickerBG"  
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" 
						/>
					</div>
				    </li>
				    <li><div class="dateSelfDiv" style="height: 20px; width: 60px; margin-left: 6px;"><input id="dateSelf" type="button" style="display:none;" class="btn_normal" onclick="shaiXuanDate($(this))" value="时间筛选"/></div></li>
				 </ul>
		     </div>
		</div>
	 </div>
	 <div id="moreSelectOptionList" class="comWidth clearfix hidden">
		<div class="selectOptionList">
		     <div class="selectOptionKey">
		       更新周期:
		     </div>
			 <div class="selectOptionValue" id="updateCycle">
		         <ul>
				    <li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
                    <li><a href="javascript:void(0);"> 日周期</a></li>
					<li><a href="javascript:void(0);"> 月周期</a></li>
				 </ul>
		     </div>
		</div>
	 </div>
	 <div class="moreOptionBox">
	     <input type="button"  value="更多条件" id="moreOptionActive" class="hand"/>
	 </div>	
  </div>
    <!-- 查询结果列表 -->
<div class="comWidth queryResultBox">
	<div class="queryResultSort">
	     <ol class="fleft clearfix" id="sortList">
		   <li class="defaultSort">
		      <span style="cursor: pointer;" onclick="sortLabelDefault()"> 默认排序</span>
		   </li>
		   <li>
		     <a href="javascript:void(0);" id="useNumOrder" title="点击按标签人气从大到小排序"> 人气<span></span></a>
		   </li>
		   <li>
		     <a href="javascript:void(0);" id="publishTimeOrder" title="点击按标签发布时间从近至远排序"> 发布时间<span></span></a>
		   </li>
		 </ol>
	 <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/icon_show_mode.png"  
			width="63" height ="27" class=" fright" usemap="#planetmap"  style="display: none;"/>
	</div>
	<div id="queryResultListBox"></div>
	<div id="noMoreLabelResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	<div id="noResultsLabel" class="noResults" style="display:none;">没有标签信息...</div>
</div>
</form>
