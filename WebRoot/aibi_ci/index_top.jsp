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
%>
<script type="text/javascript">
//点击搜索 根据过滤条件获得标签列表、客户列表
function showSearchIndex(){
	var seachTypeId=$("#seachTypeId").val();
	$("#forward_seachTypeId").val(seachTypeId);
	var keyWord = $("#keyword").val();
	updateSearchTip(keyWord,currentUserId);
	$("#isDefaultLoad").val("");
	$("#userOperType").val("")
	$("#mainContent").load("${ctx}/aibi_ci/home/labelMap.jsp");
	//fowardIndexPage();
}
function showTemplateListIndex(){
	$("#importClientBtnTip").hide();
	$("#firsttimelabel_tip").hide();
	$("#viewNavBox").removeClass("viewNavBoxClick");
	$("#templateInfoLink").parent().find("li").removeClass("current");
	$("#templateInfoLink").addClass("current");
	//清空地图
	$("#aibi_area_data").empty();
	$("#aibi_area_data").load($.ctx+"/ci/templateManageAction!loadPagedTemplateIndex.ai2do",function(){showTemplateIndex();});
}
function addShopCar(id,typeId,isEditCustomFlag ,ev){
	var e = ev||event;
	    e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	var actionUrl=$.ctx+"/ci/ciIndexAction!saveShopSession.ai2do";
	var para={
			"pojo.calculationsId":id,
			"pojo.typeId":typeId,
			"pojo.isEditCustomFlag":isEditCustomFlag
			};
	$.post(actionUrl, para, function(result){
		if (result.success){
			$("#shopTopNum").empty();
			$("#shopTopNum").append(result.numValue);
			var quoteSource = $("#quoteSource").val();
			if(quoteSource == 'calculateCenter') {
				var calculateBodyUrl = $.ctx + "/ci/ciIndexAction!findCalculateBody.ai2do";
				if (getCookieByName("${version_flag}") == "false") {
					calculateBodyUrl = $.ctx + "/ci/ciIndexAction!findCalculateBodyOld.ai2do";
				}
				$.ajax({
					url: calculateBodyUrl,
					type: "POST",
					success: function(data){
						$("#calculateContain").html(data);
						$('#parenthesis').removeClass('opened');
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
}
$.widget("custom.catcomplete", $.ui.autocomplete, { 
	_renderMenu: function( ul, items ) {
		var that = this;
	   	$.each(items, function( index, item ) {
	       	var $li = that._renderItemData(ul, item );
	       	var a_html = $li.find("a").text();
       		var isCreateCustomPage = $("#isCreateCustomPage").val();
	       	if(isCreateCustomPage == "true" ){
	       	 	$li.find("a").html('<span class="tag-name-search ellipsis-all">'+a_html+'</span><div class="fright tag-num" style="display:none;">约19739次</div>');
			}else{
				//如果没有购物车 则 不显示添加按钮
				if($(".J_show_cart_box_calculate").length > 0){
					 	$li.find("a").html('<span class="tag-name-search ellipsis-all">'+a_html+'</span><button class="autoAddCartBtn" onclick="addShopCarByVersion(\'' + item.key + '\', ' + item.typeId + ', 0)">添加</button><div class="fright tag-num" style="display:none;">约19739次</div>');
				}else{
					if($(".J_show_cart_box").length > 0){
						if($(".J_show_cart_box").is(":hidden")){
							$li.find("a").html('<span class="tag-name-search ellipsis-all">'+a_html+'</span><div class="fright tag-num" style="display:none;">约19739次</div>');
						}else{
							$li.find("a").html('<span class="tag-name-search ellipsis-all">'+a_html+'</span><button class="autoAddCartBtn" onclick="addShopCarByVersion(\'' + item.key + '\', ' + item.typeId + ', 0)">添加</button><div class="fright tag-num" style="display:none;">约19739次</div>');
						}
					}else{
						$li.find("a").html('<span class="tag-name-search ellipsis-all">'+a_html+'</span><div class="fright tag-num" style="display:none;">约19739次</div>');
					}
				}
			}
		});
	} 
});

function getCurrentUserId(){	
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
function addShopCarByVersion(labelOrCustomId, typeId, isEditCustomFlag){
	if(getCookieByName("${version_flag}") == "true"){
		var e = event;
	    e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	    var quoteSource = $("#quoteSource").val();
		if(quoteSource == 'calculateCenter') {
			addShopCar(labelOrCustomId ,typeId, isEditCustomFlag);
		} else {
			coreMain.addShopCart(labelOrCustomId ,typeId, isEditCustomFlag, "");
		}
	}else{
		addShopCar(labelOrCustomId ,typeId, isEditCustomFlag);
	}
}
var currentUserId = getCurrentUserId();
$(function(){
	if (getCookieByName("${version_flag}") == "false") {
		$(".J_search_custom_showname").html("客户群");
	}else{
		$(".J_search_custom_showname").html("用户群");
	}
	
	$("#keyword").focusblur({ 
		"focusColor":"#222232",
	    "blurColor":"#757575",
	    "searchVal":"请输入关键字"
	});
	$("#searchSubmitTop").COClive("click", function(){
		var seachTypeId=$("#seachTypeId").val();
		var keyWord = $("#keyword").val();
		var keyWordDataVal = $("#keyword").attr("data-val");
		//判断到新版的集市列表  还是 旧版的集市列表 
		if(getCookieByName("${version_flag}") == "true"){
			if(keyWordDataVal == "false"){
				keyWord = "";
			}
			//更新最近搜索tip
			updateSearchTip(keyWord,currentUserId);
			
			if($.isNotBlank("${forwardNewPageFlag}") && "${forwardNewPageFlag}" == "false"){
				//切换到新集市列表
				if(seachTypeId == '1'){
					//标签集市
					$.ajax({
						url		: $.ctx + "/core/labelMarketAction!findEffectiveLabelIndex.ai2do",
						type	: "POST",
						data	:{"ciLabelInfo.labelName":keyWord},
						success	: function(data){
							$("#marketMainList").html(data);
							$(".market-group-item-hover").removeClass("market-group-item-hover");
							$("#labelInfoIndex").addClass("market-group-item-hover");
						}
					});
				}else{
					//客户群集市
					$.ajax({
						url		: $.ctx + "/core/ciCustomerMarketAction!customerMarketIndex.ai2do",
						type	: "POST",
						data	:{"ciCustomGroupInfo.customGroupName":keyWord},
						success	: function(data){
							$("#seachTypeId_").attr("value",seachTypeId);
							$("#marketMainList").html(data);
						}
					});
				}
			}else{
				var seachKeyWord = "";
				if($.isNotBlank(keyWord)){
					seachKeyWord = encodeURIComponent(encodeURIComponent(keyWord));
				}
				var url = "${ctx}/core/ciMainAction!findMarketMain.ai2do?seachTypeId="+seachTypeId+"&seachKeyWord="+seachKeyWord;
				window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
			}
			
		}else{
			//切换到旧集市列表
			shopCarList();
			$("#fixedSearchText").attr("data-val",$("#keyword").attr("data-val"));
			//更新最近搜索tip
			updateSearchTip(keyWord,currentUserId);
			//标签列表
			if(seachTypeId == "1"){
				showSearchIndex();
			}
			//客户群集市
			if(seachTypeId == "2"){
				showSearchIndex();
			}
			//模板列表
			if(seachTypeId == "3"){
				showTemplateListIndex();
			}
		}
		
	});
	
	$("#keyword").catcomplete({
		definedClass: 'index-search-autocomplete',
		source: function( request, response ) {
			var seachTypeId=$("#seachTypeId").val();
			if(seachTypeId == ""){
				seachTypeId = "1";
			}
			if(seachTypeId == "1"){
				$.ajax({
					url: "${ctx}/ci/ciIndexAction!indexQueryLabelName.ai2do",
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
					 url: "${ctx}/ci/ciIndexAction!indexQueryCustomGroupName.ai2do",
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
					 url: "${ctx}/ci/ciIndexAction!indexQueryTemplateName.ai2do",
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
			if(seachTypeId == "4"){
				var userId = getCurrentUserId();
				var searchTipStr = getCookieByName('searchTip_'+userId);
				 $("#seachTypeId").val($(window).data("seachTypeId"));
				 $(window).removeData("seachTypeId");
				if(searchTipStr!=null){
					searchTipStr = unescape(searchTipStr);
					var searchTipArray = searchTipStr.split('|').reverse();
					response( $.map(searchTipArray , function( item ) {
						return {
							label: item,
							value: item
						}
					}));
				}
			}
		},
		minLength: 1,
		autoFocus:true,
		select: function( event, ui ) {
			$("#keyword").attr("data-val","true"); 
			$("#fixedSearchText").attr("data-val","true"); 
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			if($.browser.version == 8){
				$( ".ui-autocomplete" ).width(425);
			}
			$( ".ui-autocomplete" ).css("z-index",999);
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		} 
	});
	var seachTypeId=$("#forward_seachTypeId").val();
	if(seachTypeId != null && seachTypeId != ""){
		//标签列表
		if(seachTypeId == "1"){
			$("#searchSelect").find("a").html("标签");
			$("#searchSelect").find("input").val("1");
		}
		//客户群集市
		if(seachTypeId == "2"){
			if (getCookieByName("${version_flag}") == "false") {
				$("#searchSelect").find("a").html("客户群");
			}else{
				$("#searchSelect").find("a").html("用户群");
			}
			$("#searchSelect").find("input").val("2");
		}
		<% if ("true".equalsIgnoreCase(templateMenu)) { %>
		//模板列表
		if(seachTypeId == "3"){
			$("#searchSelect").find("a").html("模板");
			$("#searchSelect").find("input").val("3");
		}
		<% } %>
	}
	
	$("#keyword").focus(function(){
		if($(this).hasClass("defaulttip")){
			$(this).removeClass("defaulttip")
		}
		var key = $(this).val();
		if($.trim(key) == '') {
			var histroyId = $("#seachTypeId").val();
			$(window).data("seachTypeId",histroyId);
			 $("#seachTypeId").val(4)
			$(this).catcomplete("search",  " ") ;
			$(".autoAddCartBtn").hide();
		} else {
			$(this).catcomplete("search", $(this).val());
		}
	}).click(function(){
		return false;
	}).keyup(function(){
		$('#searchHistoryBox').hide();
	});
	
	/*搜索区域下拉*/
	$("#searchSelect").cocSlideDown({
		targetList:$("#searchSelect_list"),
		slideSpeed:"fast"
	});
	$("#searchText").cocSlideDown({
		targetList:$("#searchText_list")
	});
	<% if ("true".equalsIgnoreCase(templateMenu)) { %>
	/*搜索区域“更多”下拉*/
	$("#searchSubMore").cocSlideDown({
		targetList:$("#searchSubMore_list");
	});
	<% } %>
	$("#first").find("input").val("1");
	$("#seachTypeId").val($("#searchSelect").find("input").val());
	$("#searchSelect_list .slideDownList li a").click(function(){
		var txt=$(this).html();
		var seachTypeId = $(this).parent().find("input").val();
		$("#searchSelect").find("a").html(txt);
		$("#seachTypeId").val(seachTypeId);
	});
});
//系统推荐标签查询
function showSysRecommendLabel(){
	//window.location.href = "${ctx}/ci/ciIndexAction!sysRecommendLabel.ai2do?dataScope=all";
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/ciLabelInfo/sysRecommendLabel.jsp",function (){loadRecommendLabelList();});
}

//最新发布标签查询
function showNewPublishLabel(){
	//window.location.href = "${ctx}/ci/ciIndexAction!newPublishLabel.ai2do?dataScope=oneMonth";
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/ciLabelInfo/newPublishLabel.jsp",function (){loadLabelList();});
}
//系统热门客户群
function showSysRecommendCustomsList(){
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/customers/showSysRecommendCustomsList.jsp?isSysRecommendCustomsFlag=true",function (){loadCustomsList();});
}
//最新客户群
function showLatestCustomersList(){
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/customers/latestCustomersList.jsp",function(){loadCustomsList();});
}
//最新模板
function showLatestTemplateList(){
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/template/templateList.jsp?isLatestTemplateFlag=true",function(){loadCustomsList();});
}
//系统热门客户群模板
function showSysRecommendTemplateList(){
	$("#keyword").val("请输入关键字");
	$("#topInfoLink").parent().find("li").removeClass("current");
	$("#topInfoLink").addClass("current");
	$("#aibi_area_data").load("${ctx}/aibi_ci/template/templateList.jsp?isSysRecommendTemplateFlag=true",function (){loadCustomsList();});
}
//点击logo返回到首页
function showHomePage(){
	if(getCookieByName("${version_flag}") == "true"){
		var url = "${ctx}/ci/ciMarketAction!newMarketIndex.ai2do";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}else{
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
}
function forwardRankingList(typeId){
	$("#shopCartBox").show();
	$("#mainContent").load("${ctx}/ci/ciIndexAction!rankingListViewIndex.ai2do?rankingTypeId="+typeId);
}
</script>
  <!--标签名下拉层-->
  <div id="searchSelect_list" class="slideDownCT searchSelect_list">
  	<ul class="slideDownList">
      	<li id="first"><a href="javascript:void(0)">标签</a><input type="hidden" value="1" /> </li>
          <li><a class="J_search_custom_showname" href="javascript:void(0)">客户群</a><input type="hidden" value="2" /> </li>
          <% if ("true".equalsIgnoreCase(templateMenu)) { %>
          <li><a href="javascript:void(0)">模板</a><input type="hidden" value="3" /> </li>
          <% } %>
      </ul>
  </div>
<div class="indexSearch">
        <table width="100%">
            <tr>
                <td width="350" class="index_logo" onclick="showHomePage();return false;">&nbsp;</td>
                <td>
                	<div class="fright searchControl">
                    	<div id="searchSelect" class="searchSelect searchSelect_opened fleft"><a href="javascript:void(0)">标签</a>
                    	<input type="hidden" value="1" /> </div>
                       
                        <div class="searchText fleft" id="searchTextId">
                        	<input type="text" id="keyword" value="请输入关键字" data-val="false"/>
                        </div>
                        <div class="searchSubmit fright" id="searchSubmitTop"><a href="javascript:void(0)">搜索</a></div>
                        <input type="hidden" id="classId" />
                        <input type="hidden" id="seachTypeId" />
                        <div id="searchHistoryBox" class="searchHistoryBox" >
                            <ul id="searchHistoryList">
                            	
                            </ul>
                        </div>
                    </div>
                    
                 	<ul class="searchSub fleft">
	                <li style="display: none"><a href="javascript:void(0);"  onclick="showUserAttentionLabel()" >我关注的标签</a></li>
	                <li style="display: none"><a href="javascript:void(0);"  onclick="showUserUsedLabel()">我使用过的标签</a></li>
	                <li style="display: none"><a href="javascript:void(0);"  onclick="forwardRankingList(1)">系统热门标签</a></li>
	                <li style="display: none"><a href="javascript:void(0);"  onclick="forwardRankingList(2)">最新发布标签</a></li>
	                <li style="display: none"><a href="javascript:void(0);"  onclick="forwardRankingList(3)">系统热门客户群</a></li>
	                <li style="display: none" class="nobg"><a href="javascript:void(0);"  onclick="forwardRankingList(4);">最新发布客户群</a></li>
					</ul>
					<% if ("true".equalsIgnoreCase(templateMenu)) { %>
                    <a id="searchSubMore" href="javascript:void(0)" class="searchSubMore fright">更多</a>
                    <!--搜索更多下拉层-->
                    <div id="searchSubMore_list" class="slideDownCT searchSubMore_list">
                        <ul class="slideDownList">
		               	    <li><a href="javascript:void(0);" onclick="showSysRecommendTemplateList();">系统热门模板</a></li>
		               	    <li><a href="javascript:void(0);" onclick="showLatestTemplateList();">最新发布模板</a></li>
                        </ul>
                    </div>
                    <% } %>
                </td>
            </tr>
        </table>
    </div>
 