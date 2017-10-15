<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>爱营销</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<script type="text/javascript">
$().ready(function() {
	$(".searchSubmit").bind("click", function(){
		loadCustom(null,null,$("#keyword").val());
	});
	function formatItem(row) {
		return " <p>"+row[0] +" </p>";
	}
	function formatResult(row) {
		return row[0].replace(/(<.+?>)/gi, '');
	}
	function selectItem(li) {
		//makeSearchUrl(document.formkeyword);
	}
	$("#dataStatusId").multiselect({
		multiple: false,
		height: "auto",
		header: false,
		selectedList: 1
	});
	$("#keyword").autocomplete({
		source: function( request, response ) {
			$.ajax({
				url: "<%=request.getContextPath() %>/ci/ciIndexAction!indexQueryCiMarketTacticsName.ai2do",
			 	dataType: "jsonp",
			 	type: "POST",
			 	data: {
					keyWord: request.term
				},
			 	success: function( data ) {
					response( $.map( data.geonames, function( item ) {
				 		return {
							label: item.tacticName,
							value: item.tacticName
				 		}
					}));
			 	}
			});
		},
		minLength: 1,
		autoFocus:true,
		select: function( event, ui ) {},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
});

</script>
<style type="text/css">
body,html{height:100%;}
</style>
</head>
<body class="body_bg">
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
<div class="mainCT">
	<jsp:include page="/aibi_ci/index_top.jsp" ></jsp:include>	
	<div id="contentLoad">
		<form id="multiSearchForm" >
			<input id="dateType" name="marketTactics.dateType" type="hidden"/>
			<input id="tacticName" name="marketTactics.tacticName" type="hidden"/>
		</form>
		<div class="customerBase_querylist">
			<dl class="query_condition">
				<dt><span class="icon_slideLeft"></span>条件</dt>
				<dd>
					<ul class="condition_list">
						<li onclick="loadCustom(null,null)"><a href="#" >全部时间</a></li>
						<li onclick="loadCustom(1,null)"><a href="#">一天内</a></li>
						<li onclick="loadCustom(2,null)"><a href="#">一个月内</a></li>
						<li onclick="loadCustom(3,null)"><a href="#">三个月内</a></li>
					</ul>
				</dd>
			</dl><!--query_condition end -->
			<div class="customerBase" id="marketingStategyList"></div><!--customerBase end -->
			<div style="float:right;width:80%; text-align:center;">
			<div class="loading" id="loading">正在加载，请稍候 ...</div>
			<div class="loading" id="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
			<div id="noResults" style="display:none;">没有查询到 ...</div>
			</div>
			<div class="clear"></div>
		</div><!--customerBase_querylist end -->
	</div><!--contentLoad end -->
</div><!--mainCT end -->
</div>
<script>
//查询相关js
$(".bg04").addClass("active");
$("#_index").addClass("menu_on");
search();
function search(){
	$("#noMoreResults").hide();
	$("#noResults").hide();
	$("#marketingStategyList").empty();
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/marketingStrategyAction!findMarketTacticsTotalNum.ai2do";
	var tacticName = $("#tacticName").val();
	var dateType = $("#dateType").val();
	$.post(getTotalUrl, $("#multiSearchForm").serialize(), function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			var actionUrl = $.ctx + "/ci/marketingStrategyAction!marketingStategyList.ai2do?pager.totalPage=" + totalPage;
			var i = totalPage;
			if(i != 0){
			$("#marketingStategyList").scrollPagination({
				"contentPage" : actionUrl, // the url you are fetching the results
				"contentData" : {
				     "marketTactics.dateType" : dateType,
				     "marketTactics.tacticName" : tacticName
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
						if($("#marketingStategyList").attr("scrollPagination") == "disabled"){
							$("#noMoreResults").fadeIn();
						}
					});
					}else{
						$("#loading").fadeOut("normal", function(){});
					}
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
	// code for fade in element by element
	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity : 1 }, 200);
			delay += 100;
		});
	};
	return false;
}
$(".condition_list li").click(function(){
	if($(this).hasClass("on")) return;
	$(this).addClass("on").siblings(".on").removeClass("on");
})
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

function loadCustom(dateType,createTypeId,tacticName){
	if(dateType != null){
		$("#dateType").attr("value",dateType);
	}else{
		$("#dateType").attr("value","");
	}
	if(createTypeId != null){
		$("#dateType").attr("value","");
	}else{
		$("#createTypeId").attr("value","");
	}
	if(tacticName != null){
		$("#tacticName").attr("value",tacticName);
	}
	search();
}
</script>
</body>
</html>