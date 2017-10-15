<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>标签首页查询结果</title>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//时间轴
	var month = "${newLabelMonth}";
	if(month == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
			type: "POST",
			success: function(result){
				month = result;
				creatDateline($("#dateline_ct"),month);
			}
		});
	}else{
		creatDateline($("#dateline_ct"),month);
	}
	

	//左侧导航及首页标签搜索tab页选中样式
	$(".bg01").addClass("active");
	$("#_index").addClass("menu_on");
	
	$(".query_condition .icon_slideLeft").click(function(){
		var tar=$(this).parents(".query_condition");
		if(!tar.hasClass("query_condition_hided")){
			tar.addClass("query_condition_hided").animate({width:18},200);
			$(".customerBase").animate({"margin-left":28},200);
			$(this).addClass("icon_slideRight");
		}else{
			tar.removeClass("query_condition_hided").animate({width:190},200);
			$(".customerBase").animate({"margin-left":200},200);
			$(this).removeClass("icon_slideRight");
		}
	});


});

function loadLabelList(){
	$(window).unbind("scroll");
	$("#queryResultListBox").empty();
	$("#noMoreResults").hide();
	$("#noMoreLabelResults").hide();
	$("#noResultsLabel").hide();
    $("#noMoreResults").hide();
    $("#noResults").hide();

	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciLabelInfoAction!findUserUsedLabelNum.ai2do";
	$.post(getTotalUrl,{},function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			$(".amountNum").html(result.totalSize);
			var i = totalPage;
			if(i != 0){
				var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserUsedLabel.ai2do?pager.totalPage=" + totalPage;
				$("#queryResultListBox").scrollPagination({
					"contentPage" : actionUrl, // the url you are fetching the results
					"contentData" : {
						//"dataScope" : dataScope
					}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
					"scrollTarget" : $(window),// who gonna scroll? in this example, the full window
					"heightOffset" : 10,
					"currentPage" : 1,
					"totalPage" : totalPage,
					"beforeLoad" : function() { // before load function, you can display a preloader div
						$("#loading").fadeIn();
					},
					"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
						$(elementsLoaded).fadeInWithDelay();
						i--;
						if(i == 0){
							$("#loading").fadeOut("normal", function(){
								if($("#queryResultListBox").attr("scrollPagination") == "disabled"){
									$("#noMoreResults").fadeIn();
								}
							});
						}else{
							$("#loading").fadeOut("normal", function(){});
						}
	
						//增加标签分析等按钮的鼠标移入和移出特效效果(由于标签是通过滚动动态添加的，所以可以在此处为新加载的页面添加事件)
						$(".info_panel ul li").unbind("mouseenter").unbind("mouseleave");
						$(".info_panel ul li").on("mouseenter",function(){
							var _a=$(this).find("a");
							_a.css({"position":"absolute","left":"-3000px"});
							var _w=_a.width();
							_a.css({"position":"static","left":"0"});
							_a.width(0).show().animate({width:_w + 2},250);
						}).on("mouseleave",function(){
							var _a=$(this).find("a");
							_a.width(_a.width() - 2).hide();
						})
					}
				});
			}else{
				$("#noResults").show();
				$("#loading").hide();
			}
		} else {
			alert(result.msg);
		}
	});

	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity : 1 }, 200);
			delay += 100;
		});
	};
	
}
//显示标签操作列表
function showOperList(e,ths,labelId){
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
		    $resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveOption");});
		}
	}
}
//标签关注图标点击事件
function attentionOper(labelId){
	var isAttention = $("#isAttention_"+labelId).val();
	if(isAttention == 'true'){
		//已经关注，点击进行取消关注动作
		//alert('已经关注，点击进行取消关注动作');
		var url=$.ctx+'/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data:{'labelId':labelId},
	      	success: function(result){
		       	if(result.success) {
		       		$.fn.weakTip({"label":result.message });
		       		//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+labelId).val('false');
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
					$("#attentionImg_"+labelId).attr("title",'点击添加标签关注');
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
		});
	}else{
		//没有关注，点击进行关注动作触发
		//alert('没有关注，点击进行关注动作触发');
		var url=$.ctx+'/ci/ciLabelAnalysisAction!addUserAttentionLabel.ai2do';
		$.ajax({
			type: "POST",
				url: url,
				data:{'labelId':labelId},
				success: function(result){
				if(result.success) {
					$.fn.weakTip({"label":result.message });
					//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+labelId).val('true');
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
					$("#attentionImg_"+labelId).attr("title",'点击取消标签关注');
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
        });
	}
}
</script>
<style type="text/css">
     <%--设置标签微分等超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
</head>
<body class="body_bg">
<form id="multiSearchForm" >
<div class="comWidth pathNavBox">
     <span class="pathNavHome"> 我使用过的标签</span>
     <span class="pathNavArrowRight"></span>
     <span class="pathNavNext"></span>
  <span class="amountItem"> 共<span class="amountNum"></span>个标签 </span>
</div>
<HR width="100%" color="#ccccc" noShade> 
<br>
<!-- 查询条件 -->
    <!-- 查询结果列表 -->
<div class="comWidth queryResultBox">
	<div id="queryResultListBox"></div>
	<div id="noMoreLabelResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	<div id="noResultsLabel" class="noResults" style="display:none;">没有标签信息...</div>
</div>
</form>
</body>
</html>
