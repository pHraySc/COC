<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU");
String labelNumber = Configure.getInstance().getProperty("LABEL_NUMBER");
String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");

String source = (String) request.getParameter("quoteSource");
%>
<script type="text/javascript">

function addCalcItem(id,typeId,isEditCustomFlag ,ev){
	var e = ev||event;
	    e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	var actionUrl=$.ctx+"/ci/ciIndexAction!saveShopSession.ai2do";
	var para={
			"pojo.calculationsId":id,
			"pojo.typeId":typeId,
			"pojo.isEditCustomFlag":isEditCustomFlag
	};
	
	/* $.fn.weakTip({"tipTxt":"斯蒂芬斯蒂芬斯蒂","backFn":function(){
	
	}}) */
	
	$.post(actionUrl, para, function(result){
		if (result.success){
			$("#shopTopNum").empty();
			$("#shopTopNum").append(result.numValue);
			
			var quoteSource = '<%=source%>';
			
			if(quoteSource == 'calculateCenter') {
				$.ajax({
					url: $.ctx + "/ci/ciIndexAction!findCalculateBody.ai2do",
					type: "POST",
					success: function(data){
						$("#calculateContain").html(data);
						submitRules();
					}
				});
			} else {
				$("#shopNum").empty();
				$("#shopNum").append(result.numValue);
				shopCarList();
				
				//添加到购物车 触发购物车内容展示出来
				$("#shopCartLetter").removeClass("shopCartLetterHover");
				var tar=$("#shopCartGoodsList");
				$('#shopCartEmptyTip').hide();
				if(!tar.is(":visible")){
					/**及时同步 购物篮和已选商品数量  **/
					$("#shopBasket").hide(); //购物篮隐藏
					$("#shopBasketSelected").show(); //已选购物篮显示
					$("#shopCartGoodsListBox").show();
					tar.css("visibility","visible").height("auto");
					var shopChartHeight = tar.height();
					tar.height(0).show();
					tar.animate({height:shopChartHeight},0,function(){tar.height("auto")})
				}
				
				$("#shopCartGoodsList").mCustomScrollbar("update");//购物车滚动条
			}
			
		}else{
			$.fn.weakTip({"tipTxt":result.msg})
		}
	});
	return false;
}
$.widget("custom.catcomplete", $.ui.autocomplete, {
	_renderMenu: function( ul, items ) {
		var that = this;
	   	$.each(items, function( index, item ) {
	       var $li = that._renderItemData(ul, item );
	           $li.find("a").append('<button class="autoAddCartBtn" onclick="addCalcItem(\'' + item.key + '\', ' + item.typeId + ', 0)">添加</button>'); 
		});
	}
});

function getCurrentUserId()
{	
	var currentUserId;
	var actionUrl = $.ctx + "/ci/customersManagerAction!findCurrentUserId.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		async:false,
		success: function(result){
			currentUserId = result;
		}
	});
	return currentUserId;
}

var currentUserId = getCurrentUserId();
$(function(){
	
	$("#keyword").catcomplete({
		definedClass: 'index-new-search-autocomplete',
		source: function( request, response ) {
			var seachTypeId=$("#seachTypeId").val();
			if(seachTypeId == ""){
				seachTypeId = "1";
			}
			if(seachTypeId == "1"){
				$.ajax({
					url: "<%=request.getContextPath() %>/ci/ciIndexAction!indexQueryLabelName.ai2do",
					dataType: "jsonp",
					type: "POST",
					data: {
						keyWord: request.term,
						classId:$("#classId").val()
					},
					success: function( data ) {
						response( $.map( data.geonames, function( item ) {
							return {
								label: item.labelName,
								value: item.labelName,
								key : item.labelId,
								typeId : 1
							}
						}));
					}
				});
			}
			if(seachTypeId == "2"){
				$.ajax({
					 url: "<%=request.getContextPath() %>/ci/ciIndexAction!indexQueryCustomGroupName.ai2do",
					 dataType: "jsonp",
					 type: "POST",
					 data: {
						keyWord: request.term,
						classId:$("#classId").val()
					 },
					 success: function( data ) {
						response( $.map( data.geonames, function( item ) {
						 	return {
								label: item.customGroupName,
								value: item.customGroupName,
								key : item.customGroupId,
								typeId : 2
						 	}
						}));
					 }
				});
			}
			if(seachTypeId == "3"){
				$.ajax({
					 url: "<%=request.getContextPath() %>/ci/ciIndexAction!indexQueryTemplateName.ai2do",
					 dataType: "jsonp",
					 type: "POST",
					 data: {
						keyWord: request.term,
						classId:$("#classId").val()
					 },
					 success: function( data ) {
						response( $.map( data.geonames, function( item ) {
							return {
								label: item.templateName,
								value: item.templateName
							}
						}));
					 }
				});
			}
		},
		minLength: 1,
		autoFocus:true,
		select: function( event, ui ) {
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			$( ".ui-autocomplete" ).css("z-index",999);
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
	
	$("#keyword").focus(function(){
		if($(this).hasClass("defaulttip")){
			$(this).removeClass("defaulttip")
		}
		showSearchTip(currentUserId);
	}).click(function(){
		return false;
	}).keyup(function(){
		$('#searchHistoryBox').hide();
	});
	var keyword = $.trim($("#keyword").val());
	if(keyword == "请输入关键字"){
		$.focusblur("#keyword");
	}
	/*搜索区域下拉*/
	$("#searchSelect").cocSlideDown({
		targetList:$("#searchSelect_list"),
		slideSpeed:"fast"
	});
	$("#searchText").cocSlideDown({
		targetList:$("#searchText_list")
	});
	/*搜索区域“更多”下拉*/
	$("#searchSubMore").cocSlideDown({
		targetList:$("#searchSubMore_list")
	});
	$("#first").find("input").val("1");
	$("#seachTypeId").val($("#searchSelect").find("input").val());
	$("#searchSelect_list .slideDownList li a").click(function(){
		var txt=$(this).html();
		var seachTypeId = $(this).parent().find("input").val();
		$("#searchSelect").find("a").html(txt);
		$("#seachTypeId").val(seachTypeId);
	});
	$(".searchSubmit").bind("click", function(){
		var seachTypeId=$("#seachTypeId").val();
		var keyWord = $("#keyword").val();
		//更新最近搜索tip kongly
		updateSearchTip(keyWord,currentUserId);
		keyWord = encodeURIComponent(encodeURIComponent(keyWord));
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId="+seachTypeId+"&seachKeyWord="+keyWord;
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	});
	
});
jQuery.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});

};
//点击logo返回到首页
function showHomePage(){
	var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4";
	window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
</script>
 <!--标签名下拉层-->
 <div id="searchSelect_list" class="slideDownCT searchSelect_list">
 	<ul class="slideDownList">
     	<li id="first"><a href="javascript:void(0)">标签</a><input type="hidden" value="1" /> </li>
         <li><a href="javascript:void(0)">客户群</a><input type="hidden" value="2" /> </li>
         <% if ("true".equalsIgnoreCase(templateMenu)) { %>
         <li><a href="javascript:void(0)">模板</a><input type="hidden" value="3" /> </li>
         <% } %>
     </ul>
 </div>
<div class="indexSearch">
        <table width="100%">
            <tr>
                <td width="350" class="index_logo"  onclick="showHomePage();return false;">&nbsp;</td>
                <td>
                	<div class="fright searchControl">
                    	<div id="searchSelect" class="searchSelect searchSelect_opened fleft"><a href="javascript:void(0)">标签</a><input type="hidden" value="1" /> </div>
                       
                        <div class="searchText fleft" id="searchTextIdNew">
                        	<input type="text" id="keyword" value="请输入关键字" />
                        </div>
                        <div class="searchSubmit fright"><a href="javascript:void(0)">搜索</a></div>
                        <input type="hidden" id="classId" />
                        <input type="hidden" id="seachTypeId" />
                        <div id="searchHistoryBox" class="searchHistoryBox" >
                            <ul id="searchHistoryList"  >
                            	
                            </ul>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
 