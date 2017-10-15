<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>产品地图</title>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/map_scrollpagination.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	var actionUrl = $.ctx + "/ci/ciIndexAction!getProductFirstCategoryList.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: {},
		success: function(result){
			if(result.length == 0){
				$("#noResults").show();
				$("#loading").hide();
			}
			var html = '<ul> ';
			$.each(result, function(i, record) {
				var content;
				if(i == 0) {
					content = "<li class=\"top_menu_on\"><div class='bgleft'>&nbsp;</div><a href=\"javascript:void(0)\" onclick=\"showChildrenOfFirstL('"+record.categoryId+"',this)\" class=\"no"+(i+2)+"\"><span></span><p>"+record.categoryName+"</p></a><div class='bgright'>&nbsp;</div></li>";
					var id = record.categoryId;
					showChildrenOfFirstL(id);
					}
				else {
					content = "<li><div class='bgleft'>&nbsp;</div><a href=\"javascript:void(0)\" onclick=\"showChildrenOfFirstL('"+record.categoryId+"',this)\" class=\"no"+(i+2)+"\"><span></span><p>"+record.categoryName+"</p></a><div class='bgright'>&nbsp;</div></li>";
					}
				html += content;
			});
			html += '<div class="clear"></div> </ul>';
			$("#top_menu").append(html);
		}
	});
	

	//标签鼠标滑过加背景色
	$(document).on("mouseover", ".tag_List_ul li.canDrag", function(){
		$(this).addClass("hover");
	}).on("mouseout", ".tag_List_ul li", function(){
		$(this).removeClass("hover")
	});

});


function showChildrenOfFirstL(firstLevId,obj) {
	$(obj).parent().parent("ul").children("li").removeClass("top_menu_on");
	$(obj).parent().addClass("top_menu_on");
	
	var secondLLabelCount;
	var getChildrenUrl = $.ctx + "/ci/ciIndexAction!getProductSecondCategory.ai2do?categoryId="+firstLevId;
	$.ajax({
		type: "POST",
		url: getChildrenUrl,
		data: {},
		success: function(result){
			var html = '';
			$.each(result, function(i, record) {
				var content;
				if(i == 0) {
					content = "<div class=\"tag_padding\"></div>";
					content += "<div class=\"tag\" onclick=showChildrenOfSecondL("+record.categoryId+",this)>";
			        content +=     "<div class=\"bg_t\"></div>";
			        content +=     "<p>"+record.categoryName+"</p>";
			        content +=     "<div class=\"bg_b\"></div>";
			        content += "</div>";
			        //showChildrenOfSecondL(record.labelId);		
					}
				else {
					content = "<div class=\"tag_padding\"></div>";
					content += "<div class=\"tag\" onclick=showChildrenOfSecondL("+record.categoryId+",this)>";
			        content +=     "<div class=\"bg_t\"></div>";
			        content +=     "<p>"+record.categoryName+"</p>";
			        content +=     "<div class=\"bg_b\"></div>";
			        content += "</div>";
					}
				html += content;
			});
			$("#tag_curtain_CT").empty();
			$("#tag_curtain_CT").append(html);
			//标签拉绳效果
			setTagPos();
			$(window).bind("resize",setTagPos);
		}
	});
	$("#aibi_area_data").empty();
	showAllEffictiveLabel(firstLevId);
}

function showChildrenOfSecondL(secondLevId,obj) {

	var _p=$(obj).prev(".tag_padding");
	if($(obj).hasClass("click_active")) return;
	_p.addClass("tag_padding_active").siblings(".tag_padding_active").removeClass("tag_padding_active");
	var _this=$(obj);
	
	var _beginHeight=_p.height();
	_p.animate({height: (_beginHeight+20)+"px"}, 180,function(){
		_p.animate({height: _beginHeight+"px"}, 180);
	});
	$(obj).animate({top: (_beginHeight+20)+"px"}, 180,function(){
		_this.addClass("tag_active click_active").siblings(".tag_active,.click_active").removeClass("tag_active click_active");
		$(obj).animate({top: _beginHeight+"px"}, 180,function(){

	
			$("#aibi_area_data").empty();
			$("#noMoreResults").hide();
			$("#loading").fadeIn();
			
			//参考下面 showAllEffictiveLabel方法中的注释
			$(window).unbind("scroll");
		    var actionUrl = $.ctx + "/ci/ciIndexAction!getSecondCategoryProductTree.ai2do?categoryId="+secondLevId;
			$.ajax({
				type : 'POST',
				url : actionUrl,
				data : {},
				success : function(data) {
					$("#aibi_area_data").html(data);
		
					//标签拖动
					$(".canDrag").not(".draggable").draggable({ revert: "invalid", helper: "clone" });
					
					$("#loading").fadeOut("normal", function(){						
						$("#noMoreResults").fadeIn();
					});
					//$("#aibi_area_data").stopScrollPagination();
				},
				dataType : 'html'
			});
		});
	});
}
//展示一、二、三级产品地图

function showAllEffictiveLabel(firstLevId) {

	//当第一次加载完成之后会显示该div，所以每次展示标签地图之前都需要初始化为不可显示。
	$("#noMoreResults").hide();
	//已经在scrollpagination.js 中调用绑定事件之前做了解除之前的绑定，但是在满足特定条件下，点击第一级标签时，
	//会首先去触发滚动事件（估计是后面的某块代码修改了页面中的内容，内容改变后有些情况下会自动触发滚动条事件），然后才
	//执行scrollPagination方法做新的绑定。但是这样不是我们想要的，所以就在触发scorll之前先解除之前的绑定。
	$(window).unbind("scroll");
	//设置产品地图页数为全局变量
	var totalPage;
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciIndexAction!queryProductMapTotalPage.ai2do?categoryId="+firstLevId;
	$.ajax({
		type: "POST",
		url: getTotalUrl,
		data: {},
		success: function(result) {
			totalPage = result;
			var i = totalPage;
			var acUrl = $.ctx + "/ci/ciIndexAction!getAllEffictiveProductByFirstL.ai2do?categoryId="+firstLevId+"&totalPage="+totalPage;
			var aibi_area_data=$("#aibi_area_data");
			$("#aibi_area_data").scrollPagination({
		        'contentPage': acUrl, // the url you are fetching the results
		        'contentData': {}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
		        'scrollTarget': $(window),// who gonna scroll? in this example, the full window
		        'heightOffset':10,	
		        'currentPage': 1,
		        'totalPage': totalPage,
		        "beforeLoad" : function() { // before load function, you can display a preloader div
					$("#loading").fadeIn();
				},
				"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
					//标签详细信息
					var tipsWidth=($("#aibi_area_data").eq(0).width()+2)*0.985;
					$("#tag_tips").width(tipsWidth);
					
					//标签拖动
					$(".canDrag").not(".draggable").draggable({ revert: "invalid", helper: "clone" });
					
					//当由于加载的第一页内容太少而没有出现滚动条时，自动触发滚动事件，加载下一页。
					if ($(window).height() >= $(document).height()){					
						$(window).scroll();
					}
					var dls=aibi_area_data.find("dl");
					dls.each(function(){
						$(this).find("> dd").each(function(){
							$(this).find("> table tr ").last().addClass("last");
						});
						$(this).find("> dd").last().addClass("last");
					});
					$(elementsLoaded).fadeInWithDelay();
					i--;
					//i<=0表示的是最后一个页面，只有当加载完最后一个页面时才显示 noMoreResults
					if(i<=0){
						$("#loading").fadeOut("normal", function(){	
							if($("#aibi_area_data").attr("scrollPagination") == "disabled"){
								$("#noMoreResults").fadeIn();
							}
						});
					}else{
						$("#loading").fadeOut("normal", function(){});
					}
				}
		    });
		}
	});
}



//产品详情
function showTip(e,ths){
	var e=e||window.event;
	$.post("${ctx}/ci/ciIndexAction!getProductDetail.ai2do?productId=" + $(ths).attr("element") + "&productName=" + $(ths).attr("labelName") );
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$("#tag_tips .onshow").hide().removeClass("onshow");
	var _p=$(ths);
	if(_p.hasClass("tagChoosed")){
		_p.removeClass("tagChoosed");
		return;
	}else{
		$(".tagChoosed").removeClass("tagChoosed");
	}
	_p.addClass("tagChoosed");
	var posy=_p.offset().top+_p.height();
	var posx=_p.parents("dd").offset().left-1;
	//减去左侧菜单的宽度
	var iconx=_p.offset().left+_p.find("a").width()/2-8-$(".menulist").width();
	var tipCT=$("#tag_tips");
	tipCT.css({"top":posy,"left":posx});
	var html = '<div class="tips">' + 
				'<div class="icon"></div>' + 
					'<div class="ct">' + 
					'<table class="commonTable">' + 
							'<tr>' + 
								'<th>产品名称：</th>' +
								'<td>' + $(ths).attr("labelName") + '<a href="javascript:openVerifyPage('+$(ths).attr("element")+')" class="verify" onmouseover="verify(this,'+ $(ths).attr("element") +')" onmouseout=verifyhide(this)>规则校验报告</a></td>' +
								'<th></th>' +
								'<td></td>' +
							'</tr>' +
							'<tr>' + 
								'<th>适用品牌：</th>' +
								'<td colspan=3>' + $(ths).attr("brandNames") + '</td>' +
							'</tr>' +
							'<tr>' + 
								'<th>有效期：</th>' +
								'<td colspan=3>' + $(ths).attr("effectPeriod") + '</td>' +
							'</tr>' +
							'<tr>' + 
								'<th>推荐理由：</th>' +
								'<td colspan=3>' + $(ths).attr("descTxt") + '</td>' +
							'</tr>' +
							'<tr>' + 
								'<th>产品涵义：</th>' +
								'<td colspan=3>' + $(ths).attr("productMean") + '</td>' +
							'</tr>' +
		
						'</table>' +
						'<div class="info_panel">' +
							'<ul>' +
								'<li class="icon07" element="' + $(ths).attr("element") + 
								'" labelName="'+ $(ths).attr("labelName") +
								'" min="' + $(ths).attr("min") + 
								'" max="' + $(ths).attr("max") + 
								'" elementType="' + $(ths).attr("elementType") + 
								'" effectDate="' + $(ths).attr("effectDate") +
								'" type="' + $(ths).attr("type") + '" onclick="addTag(this);"><a href="javascript:void(0)">添加</a></li>' +
							'</ul>' +
						'</div>' +
					'</div>' +
				'</div>';
	var _tip=$(html);
	$("#tag_tips .group").empty().append(_tip);
	_tip.find(".icon").css("margin-left",iconx+"px");
	_tip.addClass("onshow").slideDown("fast");
	_tip.click(function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	});

	//增加产品中的“添加”这个按钮的鼠标移入和移出特效效果(由于这几个按钮是在调用showTip方法时动态加入的，所以需要在这个地方给这几个按钮加事件)
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

//产品验证相关
function verify(ts,productId){
	$("#verify_pop").remove();
	var vefifyResultUrl = $.ctx + "/ci/ciProductVerifyInfoAction!getVefifyResult.ai2do?productInfo.productId="+productId;
	//alert()
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
					<%-- 不需要根据星级提升或者下降来显示不同的颜色
					if(record.startRise == 1){
						content += '<p class="green">';
					}else if(record.startRise == -1){
						content += '<p class="red">';
					}
					--%>
					content += '<p>'
					content += '<span title="查全率">'+record.recallStr + '</span></p><span title="提升倍数">' + record.upgradeMultipleStr + '倍</span></div>'+
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
     <%--设置标签微分等超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
</head>

<body>


<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
    
    <div id="tag_curtain_CT" class="tag_curtain_CT">
    </div><!--tag_curtain_CT end -->
</div><!-- tag_curtain end-->

<div id="aibi_area_data" class="body_bg">
</div>
<div id="loading">正在加载，请稍候 ...</div>
<div id="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
<div id="noResults" style="display:none;">没有产品信息...</div>
<!--  标签地图中标签详细信息弹出层div模板-->
<div id="tag_tips" class="tag_tips">
	<div class="group">
	</div>
</div>

</body>
</html>
