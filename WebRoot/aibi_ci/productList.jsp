<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>产品首页查询结果</title>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script type="text/javascript">
$(document).ready(function(){

	//左侧导航及首页标签搜索tab页选中样式
	$(".bg03").addClass("active");
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
function loadLabelList(productName,topLabelId){

	$("#productListDiv").empty();
	$("#noMoreResults").hide();
	$("#noResults").hide();
	//从index_top.jsp中获取查询条件
	var productName = $("#keyword").val();
	var topLabelId = $("#classId").val();
	//从本页面中获取查询条件
	var dataScope = $("#formDataScope").val();
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciIndexAction!findEffectiveProductNum.ai2do";
	$.post(getTotalUrl,{"productName":productName,"topLabelId":topLabelId,"dataScope":dataScope}, function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			var actionUrl = $.ctx + "/ci/ciIndexAction!findEffectiveProduct.ai2do?pager.totalPage=" + totalPage;
			var i = totalPage;
			if(i != 0){
			$("#productListDiv").scrollPagination({
				"contentPage" : actionUrl, // the url you are fetching the results
				"contentData" : {
				         "productName" : productName,
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
						if($("#productListDiv").attr("scrollPagination") == "disabled"){
							$("#noMoreResults").fadeIn();
						}
					});
					}else{
						$("#loading").fadeOut("normal", function(){});
					}

					//增加校验结果报告按钮的鼠标移入和移出特效效果(由于标签是通过滚动动态添加的，所以可以在此处为新加载的页面添加事件)
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


//添加产品到浮动层
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


function showLabelAll(){
	$("#formDataScope").val("all");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/productListIndex.jsp?dataScope=all";
}

function showLabelOneDay(){
	$("#formDataScope").val("oneDay");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/productListIndex.jsp?dataScope=oneDay";
}

function showLabelOneMonth(){
	$("#formDataScope").val("oneMonth");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/productListIndex.jsp?dataScope=oneMonth";
}

function showLabelThreeMonth(){
	$("#formDataScope").val("threeMonth");
	loadLabelList();
	checkSelect();
	//window.location.href = "${ctx}/aibi_ci/productListIndex.jsp?dataScope=threeMonth";
} 
//时间条件选择处理
function checkSelect(){
	var dataScope = $("#formDataScope").val();
	$(".condition_list li").removeClass("on");
	//alert(dataScope);
	if(dataScope=='all' || dataScope==""){
		//alert($('#all').html());
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

//在标签查询列表中也需要添加到产品验证的链接
//产品验证相关
function verify(ts,productId){
	$("#verify_pop").remove();
	var vefifyResultUrl = $.ctx + "/ci/ciProductVerifyInfoAction!getVefifyResult.ai2do?productInfo.productId="+productId;
	$.ajax({
		type: "GET",
		url: vefifyResultUrl,
		data: {},
		success: function(result){
			var html='<div id="verify_pop" class="verify_pop">'+
			'<div class="arr_icon"></div>'+
			'<div class="verify_pop_inner">'+
				'<dl>'+
					'<dt>产品规则星级评定</dt>'+
					'<dd>根据产品潜在用户的查全率及提升倍数进行产品的星级评定，星级评定即可帮助业务人员进行产品和用户的精准营销，又可根据评定的星级对产品策略进行监控。</dd>'+
				'</dl>' +
				'<table cellpadding="0" cellspacing="0" width="100%">' +
				' <tr>';

			<%--
			//暂时不需要这个功能
			//保证有三个月的数据，如果不够三个月，则在td中显示没有数据
			var len = result.length;
			var emptyContent="";
			for(var i=0;i<3;i++){
				if((len+i)<3){
					emptyContent += "<td>没有数据</td>";
				}
			}
			html += emptyContent;
            --%>
			//拼接上有数据的月份
			$.each(result, function(i, record) {
				//首先根据starsId画出星图
				var starsHtml = '<ul class="starsList">';
				for(var i=0;i<5;i++){
					if((record.starsId-1)/2 > i){
						starsHtml +=  '<li>&nbsp;</li>';
					}else if((record.starsId-1)/2 == i){
						starsHtml += '<li class="half">&nbsp;</li>';
					}else if((record.starsId-1)/2 < i){
						starsHtml += '<li class="dis">&nbsp;</li>';
					}
				}
				starsHtml += '</ul>';
				var content;
				content = '<td>'+ 
					'<div class="topstars">' +
					'<font>'+ record.starsId/2 +'</font>' +
					starsHtml +
					'</div>'+
					'<div class="fleft">'+ record.dataDate.substring(0,4)+ '年' + record.dataDate.substring(4) +'月</div>'+
					'<div class="fright">';
					if(record.startRise == 1){
						content += '<p class="green">';
					}else if(record.startRise == -1){
						content += '<p class="red">';
					}
					content += record.recallStr + '</p>' + record.upgradeMultipleStr + '倍</div>'+
					'</td>';
				html += content;
			});
			html += '</tr>'+
				'</table>'+
			'</div>'+
		'</div>';
			
			var obj=$(html);
			obj.appendTo($("body"));
			var left=$(ts).offset().left-220;
			var top=$(ts).offset().top+13;
			var arr_margin=230;
			if(left+obj.width()>$(window).width()){
				var newleft=$(window).width()-obj.width()-2;
				arr_margin+=left-newleft;
				left=newleft;
			}else if(left<0){
				var newleft=2;
				arr_margin+=left+newleft;
				left=newleft;
			}
			if(top>($(window).height()+$(window).scrollTop()-obj.height())){
				top=top-obj.height()-10;
				obj.find(".arr_icon").hide();
			}
			obj.css({"left":left,"top":top}).show().find(".arr_icon").css("margin-left",arr_margin);
		}
	});
					
}

function verifyhide(ts){
	var tik=setTimeout(function(){$("#verify_pop").remove()},200);
	$("#verify_pop").mouseenter(function(){
		clearTimeout(tik);
		tik=null;
	}).mouseleave(function(){$(this).remove()})
}

function openVerifyPage(productId){
	var url = $.ctx + "/ci/ciProductVerifyInfoAction!verifyIndex.ai2do?productInfo.productId="+productId;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}

</script>
<style type="text/css">
     <%--设置校验结果报告超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
</head>

<body class="body_bg">

<div class="customerBase_querylist">
       <dl class="query_condition">
       	<dt><span class="icon_slideLeft"></span>条件</dt>
           <dd>
           	<ul class="condition_list">
               	   <li id="all"><a href="#"  onclick="showLabelAll()">全部时间</a></li>
                   <li id="oneDay"><a href="#" onclick="showLabelOneDay()">一天内</a></li>
                   <li id="oneMonth"><a href="#" onclick="showLabelOneMonth()">一个月内</a></li>
                   <li id="threeMonth"><a href="#" onclick="showLabelThreeMonth()">三个月内</a></li>
               </ul>
           </dd>
       </dl><!--query_condition end -->
       <div class="customerBase" id="productListDiv"></div><!--customerBase end -->
       <div class="loading" id="loading">正在加载，请稍候 ...</div>
	   <div class="loading" id="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	   <div id="noResults" style="display:none;">没有查询到 ...</div>
</div><!--customerBase_querylist end -->

<!--设置隐藏域，用来记录时间查询条件-->
<form id="searchLabelForm" action="" method="post">
  <input type="hidden" id="formDataScope" name="dataScope" value="all"/>
</form>

</body>
</html>
