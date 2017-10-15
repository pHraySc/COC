<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
//标签查询
function loadLabelList(){
	$(window).unbind("scroll");
	$("#fixedSearchBox").hide();
	$("#queryResultListBox").empty();
	$("#noMoreResults").hide();
	$("#noMoreLabelResults").hide();
	$("#noResultsLabel").hide();
    $("#noMoreResults").hide();
    $("#noResults").hide();
    $("#loadingLabel").hide();

	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciLabelInfoAction!findUserAttentionLabelNum.ai2do";
	$.post(getTotalUrl,{},function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			$(".amountNum").html(result.totalSize);
			var i = totalPage;
			if(i != 0){
				var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserAttentionLabel.ai2do?pager.totalPage=" + totalPage;
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
						$("#loadingLabel").fadeIn();
					},
					"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
						$(elementsLoaded).fadeInWithDelay();
						i--;
						if(i == 0){
							$("#loadingLabel").fadeOut("normal", function(){
								if($("#queryResultListBox").attr("scrollPagination") == "disabled"){
									$("#noMoreLabelResults").fadeIn();
								}
							});
						}else{
							$("#loadingLabel").fadeOut("normal", function(){});
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
				$("#noResultsLabel").show();
				$("#loadingLabel").hide();
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
//添加标签到浮动层
function addLabel(id){
	tagOnDrop($("#"+id), $("#tag_dropable"));
}
//标签关注图标点击事件
function attentionLabelOper(labelId){
	var isAttention = $("#isAttention_"+labelId).val();
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
		       			loadLabelList();
		       	    }})
		       		//window.parent.showAlert(result.message,"success",loadLabelList);
				}else{
					window.parent.showAlert(result.message,"failed");
				}
			}
		});
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

</script>
<style type="text/css">
     <%--设置标签微分等超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>

<form id="multiSearchForm" >
<div class="comWidth pathNavBox">
     <span class="pathNavHome"> 我收藏的标签</span>
     <span class="pathNavNext"></span>
  <span class="amountItem"> 共<span class="amountNum"></span>个标签 </span>
</div>
<HR width="100%" color="#ccccc" noShade> 
<br>
<!-- 查询条件 -->
    <!-- 查询结果列表 -->
<div class="comWidth queryResultBox">
	<div id="queryResultListBox"></div>
	<div id="loadingLabel" class="_loading">正在加载，请稍候 ...</div>
	<div id="noMoreLabelResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	<div id="noResultsLabel" class="noResults" style="display:none;">没有标签信息...</div>
</div>
</form>
