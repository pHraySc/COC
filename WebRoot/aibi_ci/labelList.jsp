<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>标签首页查询结果</title>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script type="text/javascript">
$(document).ready(function(){

	//左侧导航及首页标签搜索tab页选中样式
	$(".bg01").addClass("active");
	$("#_index").addClass("menu_on");	
	checkSelect();

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
	$("#labelListDiv").empty();
	$("#noMoreResults").hide();
	$("#noResults").hide();

	//从index_top.jsp中获取查询条件
	var keyword = $.trim($("#keyword").val());
	if(keyword == "请输入关键字"){
		keyword="";
	}
	var labelKeyWord = keyword;
	var topLabelId = $("#classId").val();
	//从本页面中获取查询条件
	var dataScope = $("#formDataScope").val();
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciIndexAction!findEffectiveLabelNum.ai2do";
	$.post(getTotalUrl,{"labelKeyWord":labelKeyWord,"topLabelId":topLabelId,"dataScope":dataScope}, function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			var actionUrl = $.ctx + "/ci/ciIndexAction!findEffectiveLabel.ai2do?pager.totalPage=" + totalPage;
			var i = totalPage;
			if(i != 0){
			$("#labelListDiv").scrollPagination({
				"contentPage" : actionUrl, // the url you are fetching the results
				"contentData" : {
				     "labelKeyWord" : labelKeyWord,
				     "topLabelId" : topLabelId,
				     "dataScope" : dataScope
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
					//当加载完最后一页时才需要显示出noMoreResults
					i--;
					if(i == 0){
					$("#loading").fadeOut("normal", function(){
						if($("#labelListDiv").attr("scrollPagination") == "disabled"){
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
		}
	});

	// code for fade in element by element
	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity : 1 }, 200);
			delay += 100;
		});
	};
	
}

//下面的四个js函数为点击日期条件，根据日期条件进行查询标签列表的函数
function showLabelAll(){
	$("#formDataScope").val("all");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/labelListIndex.jsp?dataScope=all";
}

function showLabelOneDay(){
	$("#formDataScope").val("oneDay");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/labelListIndex.jsp?dataScope=oneDay";
}

function showLabelOneMonth(){
	$("#formDataScope").val("oneMonth");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/labelListIndex.jsp?dataScope=oneMonth";
}

function showLabelThreeMonth(){
	$("#formDataScope").val("threeMonth");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/labelListIndex.jsp?dataScope=threeMonth";
} 

//时间条件选择处理
function checkSelect(){
	var dataScope = $("#formDataScope").val();
	$(".condition_list li").removeClass("on");
	if(dataScope=='all' || dataScope==""){
		$('#all').addClass("on");
	}
	if(dataScope=='oneDay'){
		$('#oneDay').addClass("on");
	}
	if(dataScope=='oneMonth'){
		$('#oneMonth').addClass("on");
	}
	if(dataScope=='threeMonth'){
		$('#threeMonth').addClass("on");
	}
}
/*
 * 由于标签首页中标签地图与我关注的标签等多个地方都用到了这几个js方法，因此把他们放到了index.jsp中共用。
 
//打开标签微分链接
function openLabelDifferential(labelId){
	var param = "ciLabelInfo.labelId="+labelId;
	param = param + "&tab=tag";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//打开标签关联分析链接
function openLabelRel(labelId) {
	var param = "ciLabelInfo.labelId="+labelId;
		param = param + "&ciLabelInfo.dataDate="+labelMon;
		param = param + "&tab=rel";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");	
}
//标签对比分析连接
function openLabelContrast(labelId){
	var param = "ciLabelInfo.labelId="+labelId;
		param = param + "&dataDate="+labelMon;
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	
}

//标签分析连接
function openLabelDetail(labelId){
	var url = $.ctx+"/ci/ciLabelAnalysisAction!initlabelAnalysisIndex.ai2do?selectLabelId="+labelId;
	window.location.href = url;
}
*/
//添加标签到浮动层
function addLabel(id){
	tagOnDrop($("#"+id), $("#tag_dropable"));
}
//释放正在拖动的标签
function dropTag(){
	//本页无拖拽
	/* $("#tag_dropable").droppable({
		accept: "#analyse_tree a",
		greedy:true,
		drop: function( event, ui ) {
			$(this).find(".drag_tip").hide();
			var onDragTag=ui.draggable;
			tagOnDrop(onDragTag,$(this));
		}
	}); */
}
</script>
<style type="text/css">
     <%--设置标签微分等超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
</head>

<body class="body_bg">

<div class="customerBase_querylist">
       <dl class="query_condition">
       	<dt><span class="icon_slideLeft"></span>条件</dt>
           <dd>
           	  <ul class="condition_list">
               	   <li id="all"><a href="#" onclick="showLabelAll()" >全部时间</a></li>
                   <li id="oneDay"><a href="#" onclick="showLabelOneDay()">一天内</a></li>
                   <li id="oneMonth"><a href="#" onclick="showLabelOneMonth()">一个月内</a></li>
                   <li id="threeMonth"><a href="#" onclick="showLabelThreeMonth()">三个月内</a></li>
               </ul>
           </dd>
       </dl><!--query_condition end -->
       <div class="customerBase" id="labelListDiv"></div><!--customerBase end -->
       <div class="loading" id="loading">正在加载，请稍候 ...</div>
	   <div class="loading" id="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	   <div id="noResults" style="display:none;">没有查询到 ...</div>
</div><!--customerBase_querylist end -->

<!--设置隐藏域，用来记录时间查询条件-->
<form id="searchLabelForm" action="" method="post">
  <input type="hidden" id="formDataScope" name="dataScope" value="all"></input>
</form>
</body>
</html>
