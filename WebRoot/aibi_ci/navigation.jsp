<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure"%>
<%@ page import="com.ailk.biapp.ci.model.CiShopSessionModel"%>
<%@ page import="com.asiainfo.biframe.privilege.IUserSession"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String alarmMenu = Configure.getInstance().getProperty("AlARM_MENU");
String myLabel = Configure.getInstance().getProperty("MY_LABEL");
String logMenu = Configure.getInstance().getProperty("LOG_ANALYSIS");
String helperMenu = Configure.getInstance().getProperty("HELPER_MENU"); 
String standardHelperMenu = Configure.getInstance().getProperty("STANDARD_HELPER_MENU");
String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
String downLoadList = Configure.getInstance().getProperty("DOWNLOAD_LIST");
String feedbackMenu = Configure.getInstance().getProperty("FEEDBACK_MENU");
String enumClassConfig = Configure.getInstance().getProperty("ENUM_CLASS_CONFIG");
String rankingList = Configure.getInstance().getProperty("RANKING_LIST");
boolean logAnalyseOperRight = false;
String userId = (String)request.getAttribute("userId");
final IUserSession mysession = (IUserSession) session.getAttribute(IUserSession.ASIA_SESSION_NAME);
String userName = "";
if (mysession != null) {
	if (userId == null || userId.equals("")) {
		if ("nj".equalsIgnoreCase(CommonConstants.base)) {
			userId = mysession.getUserID().toLowerCase();
		} else {
			userId = mysession.getUserID();
		}
	}
	userName = mysession.getUserName();
}
try {
    logAnalyseOperRight = PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight(userId, "999071600",
            "CI-TC-LOGANALYSE");
} catch (Throwable e) {
    System.err.println("PrivilegeServiceUtil.getUserPrivilegeService().haveOperRight error");
    e.printStackTrace();
}

String root = Configure.getInstance().getProperty("CENTER_CITYID");
String cityId = "";
boolean isAdmin = false;
if(session != null){
	cityId = (String) session.getAttribute("cocUserCityId");
	isAdmin = session.getAttribute("isAdminUser_" + userId) == null 
			? false : (Boolean) session.getAttribute("isAdminUser_" + userId);
}

String versionShowCookieName = ServiceConstants.COOKIE_NAME_FOR_SHOW_VERSION + "_" + userId;
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="cityId" value="<%=cityId%>"></c:set>
<c:set var="userId" value="<%=userId%>"></c:set>
<c:set var="isAdmin" value="<%=isAdmin%>"></c:set>
<c:set var="userName" value="<%=userName%>"></c:set>
<c:set var="versionShowCookieName" value="<%=versionShowCookieName%>"></c:set>
<script type="text/javascript">
var currentUserId = "${userId}";
$(function(){
	if (getCookieByName("${version_flag}") == "false") {
		$(".J_search_custom_showname").html("客户群");
		$("#new_main_css").remove();
		if (getCookieByName("${versionShowCookieName}") == "true") {
			$(".J_new_version").show();
			$(".J_old_version").hide();
		}
		$(".J_new_version_main").hide();
		$(".J_old_version_main").show();
		$(".J_version_map").show();
		$(".J_version_map").show();
	}else{
		$(".J_search_custom_showname").html("用户群");
		if (getCookieByName("${versionShowCookieName}") == "true") {
			$(".J_new_version").hide();
			$(".J_old_version").show();
		}
		$(".J_new_version_main").show();
		$(".J_old_version_main").hide();
		$(".J_version_map").hide();
		$(".J_version_map").hide();
	}
	//新旧版本的切换 start =========================================
	$(".J_old_version").COClive("click",function(e) {
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
		addCookie("${version_flag}","false");
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	});
	$(".J_new_version").COClive("click",function(e) {
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
		addCookie("${version_flag}","true");
		var url = "${ctx}/ci/ciMarketAction!newMarketIndex.ai2do";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	});
	//新旧版本的切换 end =========================================
	showLabelMarketCategory();
	//消息下拉
	$("#mMessage").cocFanAnimate({
		targetObj	: "#mMessage_list",
		hoverClass  : "colorfulMidHover",
		absoluteObj : "#mMessage"
	});
	$("#myCollectionMessage").cocFanAnimate({
		targetObj	: "#collectMessageShow",
		hoverClass  : "colorfulMidHover",
		absoluteObj : "#myCollectionMessageDiv",
		getData		: function() {
			var url = $.ctx + '/ci/ciIndexAction!findMyCollection.ai2do';
			$.post(url, {}, function(response) {
				var rules = response.rules;
				var num   = response.num;
				var ol = $('#navigationCalcElement');
				if(rules) {
					ol.empty();
					if(rules.length == 0){
						$("#collectionMessageList").hide();
						$("#emptyCollectionMessageList").show();
					}else{
						var index = 5;
						if(rules.length < index) {
							index = rules.length;
						}
						for(var i=0; i<index; i++) {
							var rule = rules[i];
							var labelCustomNameStr = rule.labelCustomName;
							if(rule.typeId == 2){
								labelCustomNameStr = '【群】'+labelCustomNameStr;
							}
							var isCreateCustomPage = $("#isCreateCustomPage").val();
							if(isCreateCustomPage == "true" ){
								ol.append('<li title="'+labelCustomNameStr+'" style="cursor: default;"><p>' + labelCustomNameStr +'</p></li>');
							}else{
								//如果没有购物车 则 不显示添加按钮
								if($(".J_show_cart_box_calculate").length > 0){
										ol.append('<li title="'+labelCustomNameStr+'" style="cursor: default;"><p>' + labelCustomNameStr +'</p><button class="autoAddCartBtn" onclick="addShopBasketByVersion(\'' + rule.labelCustomId + '\', ' + rule.typeId + ', 0)">添加</button></li>');
								}else{
									if($(".J_show_cart_box").length > 0){
										if($(".J_show_cart_box").is(":hidden")){
											ol.append('<li title="'+labelCustomNameStr+'" style="cursor: default;"><p>' + labelCustomNameStr +'</p></li>');
										}else{
											ol.append('<li title="'+labelCustomNameStr+'" style="cursor: default;"><p>' + labelCustomNameStr +'</p><button class="autoAddCartBtn" onclick="addShopBasketByVersion(\'' + rule.labelCustomId + '\', ' + rule.typeId + ', 0)">添加</button></li>');
										}
									}else{
										ol.append('<li title="'+labelCustomNameStr+'" style="cursor: default;"><p>' + labelCustomNameStr +'</p></li>');
									}
								}
							}
						}
						$("#emptyCollectionMessageList").hide();
						$("#collectionMessageList").show();
					}
				}
			});
		}
	});
	//显示消息
	var mposx=$(".userMessage").offset().left+15;
	$("#mesNotice").css({"left":mposx});
	$(window).resize(function(){
		var mposx=$(".userMessage").offset().left+15;
		$("#mesNotice").css({"left":mposx});
	});
	//导航地图列表
	$("#navMapIcon").cocFanAnimate({
		targetObj:"#navMapListBox",
		hoverClass:"comboxHover",
		getData:judgeApprover
	});
   //标签分类
   $("#tagNavMap").cocFanAnimate({
         targetObj:"#tagNavMapListBox",
		 hoverClass:"tagNavMapHover",
		 top: 30,
		 absoluteObj:".loginfInner"
   })
	$("#navMapListBox a").click(function(){
		$("#navMapListBox").slideUp();
	});
	$("#tagNavMapListBox a").click(function(){
		$("#tagNavMapListBox").slideUp();
	});
	$("#tagNavMapListBox2 a").click(function(){
		$("#tagNavMapListBox2").slideUp();
	});
	//绑定隐藏提示层
	$(document).click(function(e){
	    var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		$("#navMapListBox").slideUp();
		$("#tagNavMapListBox").slideUp();
		$("#tagNavMapListBox2").slideUp();
	});
	//文档滚动事件监听 $(window.document).scroll此写法不能兼容IE
	var flag =  false;
	window.onscroll = function(){
		if($.isNotBlank("${fixedSearchFlag}") || "${fixedSearchFlag}" == "false"){
			return;
		}
		var scrollTop = $(window).scrollTop();
		if(scrollTop > 45){
			$("#searchTextId input").blur();
			$("#searchTextIdNew input").blur();
			var isCreateCustomPage = $("#isCreateCustomPage").val();
			if(isCreateCustomPage == "true" ){
				$("#fixedSearchBox").hide();
				$('.index-search-autocomplete').hide();
				$('.index-new-search-autocomplete').hide();
			} else {
				var searchTypeId = $('#seachTypeId').val();
				$("#fixedSearchBox").show();
				//悬浮框显示的时候,应把普通搜索框的值赋值给悬浮框
				$('#fixedSeachTypeId').val(searchTypeId);
				var searchVal = $("#keyword").val();
				var searchDateVal = $('#keyword').attr("data-val");
				if(!flag){
					$("#fixedSearchText").val(searchVal);
					$("#fixedSearchText").attr("data-val",searchDateVal);
				}
				flag = true;
				if(searchDateVal == "true"){
					$("#fixedSearchText").css("color","#232323")
				}else{
					$("#fixedSearchText").css("color","#757575")
				}
				//标签列表
				if(searchTypeId == "1"){
					$("#fixedSearchSelect").find("a").html("标签");
					$("#fixedSearchSelect").find("input").val("1");
				}
				//客户群集市
				if(searchTypeId == "2"){
					if (getCookieByName("${version_flag}") == "false") {
						$("#fixedSearchSelect").find("a").html("客户群");
					}else{
						$("#fixedSearchSelect").find("a").html("用户群");
					}
					$("#fixedSearchSelect").find("input").val("2");
				}
			}
		} else {
			if(flag == true){
				//悬浮框消失的时候,应把悬浮框的值赋值给普通搜索框
				var fixedSearchDataVal = $('#fixedSearchText').attr("data-val");
				var fixedSearchVal = $("#fixedSearchText").val();
				$('#keyword').val(fixedSearchVal);
				$('#keyword').attr("data-val",fixedSearchDataVal);
				if(fixedSearchDataVal == "true"){
					$("#keyword").css("color","#232323")
				}else{
					$("#keyword").css("color","#757575")
				}
		    	$('.navigation-search-autocomplete').hide();
				$("#fixedSearchBox").hide();
			}
			flag = false;
		}
	}
	$('#fixedSearchText').bind('keyup', function() {
		var fixedDataVal = $("#fixedSearchText").attr("data-val");
		var keyWord = $('#fixedSearchText').val();
		$('#keyword').val(keyWord);
		$('#keyword').attr("data-val",fixedDataVal);
	});
	
	/*搜索区域下拉*/
	$("#fixedSearchSelect").toggle(function(){
			var posx = $(this).parent().width()-$(this).width()-30;
			$("#fixedSearchSelect_list").css({"right":posx+"px","z-index":1000 ,"top":44+"px"}).slideDown();
	 	},function(){
			$("#fixedSearchSelect_list").slideUp();
	});

	$("#fixedSearchText").catcomplete({
		definedClass: 'navigation-search-autocomplete',
		source: function( request, response ) {
		var seachTypeId=$("#fixedSeachTypeId").val();
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
						classId:$("#fixedClassId").val()
					},
					success: function( data ) {
					response(
						$.map( data.geonames, function( item ) {
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
						classId:$("#fixedClassId").val()
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
						classId:$("#fixedClassId").val()
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
				$("#fixedSeachTypeId").val($(window).data("seachTypeId"));
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
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			$( ".ui-autocomplete" ).css({"z-index":1005,"position":"fixed","top":80});
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
	
	$("#fixedSearchText").focusblur({ 
		"focusColor" : "#222232" ,
	    "blurColor"	 : "#757575",
	    "searchVal":"请输入关键字"  
	});
	
	$("#fixedSearchText").focus(function(){
		if($(this).hasClass("defaulttip")){
			$(this).removeClass("defaulttip")
		}
		var key = $(this).val();
		if($.trim(key) == '') {
// 			$("#searchText_history_Box").css(
// 				{"left":480+"px","z-index":1000 ,"top":44+"px"}).slideDown();
// 			showSearchTipInfo(currentUserId,"fixedSearchText","searchText_history_List","searchText_history_Box");
			var histroyId = $("#fixedSeachTypeId").val();
			$(window).data("seachTypeId",histroyId);
			 $("#fixedSeachTypeId").val(4)
			$(this).catcomplete("search",  " ") ;
			$(".autoAddCartBtn").hide();
		} else {
			$(this).catcomplete("search", $(this).val());
		}
	}).click(function(){
		return false;
	}).keyup(function(){
		$('#searchText_history_Box').hide();
	});
	
	$("#fixedSearchSubmit").COClive("click", function(){
		var versionFlag = getCookieByName("${version_flag}");
		var seachTypeId=$("#fixedSeachTypeId").val();
		var keyWord = $("#fixedSearchText").val();
		var dataVal = $("#fixedSearchText").attr("data-val");
		$("#keyword").attr("data-val",dataVal);
		//判断到新版的集市列表  还是 旧版的集市列表 
		if(versionFlag == "true"){
			if(dataVal == "false"){
				keyWord = "";
			}else{
				$("keyWord").val(keyWord);
				$("#keyword").css("color","#757575");
			}
			//更新最近搜索tip
			updateSearchTip(keyWord,currentUserId);
			if($.isNotBlank("${forwardNewPageFlag}") && "${forwardNewPageFlag}" == "false"){
				//切换到新集市列表
				if(seachTypeId == '1'){
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
				if(seachTypeId == '1'){
					$('#marketTaskTitle').html('全景视图');
					$(".market-group-item-hover").removeClass("market-group-item-hover");
					$("#labelInfoIndex").addClass("market-group-item-hover");
				}
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
			if(dataVal == "false"){
				keyWord = "";
			}
			if(seachTypeId == ""||seachTypeId == null){
				seachTypeId = "1";
			}
			$("#forward_seachTypeId").val(seachTypeId);
			//更新最近搜索tip kongly
			updateSearchTip(keyWord,currentUserId);
			$("#isDefaultLoad").val("");
			$("#userOperType").val("")
			$(window).scrollTop(0);
			$("#mainContent").load("${ctx}/aibi_ci/home/labelMap.jsp");
		}
	});
});

//判断当前用户审批权限
function judgeApprover(){
	$("#resourceApprove").hide();
	var actionUrl = $.ctx + "/ci/ciApproveAction!judgeCurrentUserIsApprover.ai2do";
	$.ajax({
		type : 'POST',
		url : actionUrl,
		data : {},
		dataType : 'json',
		success : function(result) {	
			 if (result.approver) {
				$("#resourceApprove").show();
			}  
		}
	});
}

//展示1-3级标签分类
function showLabelMarketCategory(){
	var actionUrl = $.ctx + "/ci/ciIndexAction!findLabelCategory.ai2do";
	$.ajax({
		type : 'POST',
		url : actionUrl,
		data : {},
		dataType : 'html',
		success : function(result) {	
			$("#tagNavMapListBox").html("");
			$("#tagNavMapListBox2").html("");
			$("#tagNavMapListBox").append(result);
			$("#tagNavMapListBox2").append(result);
			$(".secondTagNavListBox").each(function(index,item){
				var $item= $(item);
				var $itemNext =$item.find("div:last");
				var nextLength = $itemNext.find("dl").length;
				if(nextLength == 0){
					$item.find("div:first").removeClass("tagLeftBox");
				}
			});
		}
	});
}

//展示4-6级标签地图 标签地图点击 一级标签:1 二级标签:2 三级标签:3
function showAllLabelMap(labelType) {
	var c1LabelId = $("#c1LabelId").val();
	var c2LabelId = $("#c2LabelId").val();
	var c3LabelId = $("#c3LabelId").val();
	var l1LabelName = $("#l1LabelName").val();
	var l2LabelName = $("#l2LabelName").val();
	var l3LabelName = $("#l3LabelName").val();
	$("#labelMarketLink").parent().find("li").removeClass("current");
	$("#labelMarketLink").addClass("current");
	$("#importClientBtnTip").hide();
	$("#firsttimelabel_tip").hide();
	$("#keyword").val( "请输入关键字");
	$(".pathNavBox").remove();
	//清空地图
	$("#aibi_area_data").empty();
	//当第一次加载完成之后会显示该div，所以每次展示标签地图之前都需要初始化为不可显示。
	$("#noMoreResults").hide();
	$("#viewNavBox").removeClass("viewNavBoxHover");
	//已经在scrollpagination.js 中调用绑定事件之前做了解除之前的绑定，但是在满足特定条件下，点击第一级标签时，
	//会首先去触发滚动事件（估计是后面的某块代码修改了页面中的内容，内容改变后有些情况下会自动触发滚动条事件），然后才
	//执行scrollPagination方法做新的绑定。但是这样不是我们想要的，所以就在触发scorll之前先解除之前的绑定。
	$(window).unbind("scroll");
	//设置标签地图页数为全局变量
	var totalPage;
	var labelId = "";
	if(labelType == 1){//点击一级标签
	    labelId = c1LabelId;
		if(labelId != null && labelId != ""){
			$("#viewNavBox").addClass("viewNavBoxClick");
			//点击一级标签显示标签导航
			$("#navBox").after('<div class="comWidth pathNavBox"><span class="pathNavNext">'+l1LabelName+'</span></div>');
		}
	}else if(labelType == 2){//点击二级标签
		labelId = c2LabelId;
		if(labelId != null && labelId != ""){
			$("#viewNavBox").addClass("viewNavBoxClick");
			//点击二级标签显示标签导航
			$("#navBox").after('<div class="comWidth pathNavBox"><span style="cursor: pointer;" class="pathNavHome" c1LabelName="'+l1LabelName+'" c1LabelId="'+c1LabelId+'" onclick="showAllLabelMap(1)" >'+l1LabelName+'</span><span class="pathNavArrowRight"></span><span class="pathNavNext">'+l2LabelName+'</span></div>');
		}
	}else if(labelType == 3){//点击三级标签
		labelId = c3LabelId;
		if(labelId != null && labelId != ""){
			$("#viewNavBox").addClass("viewNavBoxClick");
			//点击三级标签显示标签导航
			$("#navBox").after('<div class="comWidth pathNavBox"><span style="cursor: pointer;" class="pathNavHome" c1LabelName="'+l1LabelName+'" c1LabelId="'+c1LabelId+'" onclick="showAllLabelMap(1)" >'+l1LabelName+'</span><span class="pathNavArrowRight"></span><span style="cursor: pointer;" class="pathNavHome" c1LabelName="'+l1LabelName+'" c1LabelId="'+c1LabelId+'" c2LabelName="'+l2LabelName+'" c2LabelId="'+c2LabelId+'" onclick = "showAllLabelMap(2)" >'+l2LabelName+'</span><span class="pathNavArrowRight"></span><span class="pathNavNext">'+l3LabelName+'</span></div>');
		}
	}
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciIndexAction!findAllLabelMapTotalPage.ai2do";
	$.ajax({
		type: "POST",
		url: getTotalUrl,
		data: {"labelId":labelId,"labelType":labelType},
		success: function(result) {
			totalPage = result;
			var i = totalPage;
			var acUrl = $.ctx + "/ci/ciIndexAction!findAllLabelMap.ai2do?labelId="+labelId+"&labelType="+labelType+"&totalPage="+totalPage;
			var aibi_area_data=$("#aibi_area_data");
			aibi_area_data.scrollPagination({
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
						$("#loading").fadeOut("normal", function(){
							var areaDataHeight = parseInt(($("#aibi_area_data").height()+$("#aibi_area_data").offset().top),10) ;
							if($(window).height() >= areaDataHeight){
								$(window).scroll();
								$(document.body).css("min-height",($(window).height()+20)+"px");
							}
						});
					}
					$(".label_map_show").first().addClass("firstLable_tooltip");
					//判断是否是第一次登陆，第一次登陆显示拖动提示
					var cookieNums = getCookieByName("INDEX_LABEL_TIP_NUMS_COOKIE_"+currentUserId);
					if(cookieNums>1){
						$("#firsttimelabel_tip").hide();	
					}else{
						var first=$(".firstLable_tooltip");
						var firstOffset = $(".firstLable_tooltip").offset();
						$("#firsttimelabel_tip").css({"left":(firstOffset.left+ first.width()/4)+ "px", "top": firstOffset.top+first.height()+ "px","marginLeft":"0px"}).show();
					}
				}
			});
		}
	});
}
function forwardShowAllLabelMap(ths,labelType){
	var c1LabelId = "";
	var c2LabelId = "";
	var c3LabelId = "";
	var l1LabelName = "";
	var l2LabelName = "";
	var l3LabelName = "";
	if(labelType == 1){//点击一级标签
	    labelId = $(ths).attr("c1LabelId");
		if(labelId != null && labelId != ""){
			l1LabelName = $(ths).attr("c1LabelName");
			c1LabelId = labelId;
		}
	}else if(labelType == 2){//点击二级标签
		labelId = $(ths).attr("c2LabelId");
		if(labelId != null && labelId != ""){
			l1LabelName = $(ths).attr("c1LabelName");
			c1LabelId = $(ths).attr("c1LabelId");
			l2LabelName = $(ths).attr("c2LabelName");
			c2LabelId = labelId;
		}
	}else if(labelType == 3){//点击三级标签
		labelId = $(ths).attr("c3LabelId");
		if(labelId != null && labelId != ""){
			l1LabelName = $(ths).attr("c1LabelName");
			c1LabelId = $(ths).attr("c1LabelId");
			l2LabelName = $(ths).attr("c2LabelName");
			c2LabelId = $(ths).attr("c2LabelId");
			l3LabelName = $(ths).attr("c3LabelName");
			c3LabelId = labelId;
		}
	}
	$("#c1LabelId").val(c1LabelId);
	$("#l1LabelName").val(l1LabelName);
	$("#c2LabelId").val(c2LabelId);
	$("#l2LabelName").val(l2LabelName);
	$("#c3LabelId").val(c3LabelId);
	$("#l3LabelName").val(l3LabelName);
	$("#labelTypeMap").val(labelType);
	$("#refreshType").val(6);
	$("#forward_seachTypeId").val("")
	$("#userOperType").val("")
	$("#mainContent").load("${ctx}/aibi_ci/home/labelMap.jsp");
	//fowardIndexPage();
}
function addShopBasket(id,typeId,isEditCustomFlag ,ev){
	var e = ev||event;
	    e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	var actionUrl=$.ctx+"/ci/ciIndexAction!saveShopSession.ai2do";
	var para={
			"pojo.calculationsId":$.trim(id),
			"pojo.typeId":typeId,
			"pojo.isEditCustomFlag":isEditCustomFlag
			};
	$.post(actionUrl, para, function(result){
		if (result.success){
			var quoteSource = $("#quoteSource").val();
			$("#shopTopNum").empty();
			$("#shopTopNum").append(result.numValue);
			
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
			$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){}})
		}
	});
	return false;
}
function addShopBasketByVersion(labelOrCustomId, typeId, isEditCustomFlag){
	if(getCookieByName("${version_flag}") == "true"){
		var e = event;
	    e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	    labelOrCustomId = $.trim(labelOrCustomId);
	    var quoteSource = $("#quoteSource").val();
	    if(quoteSource == 'calculateCenter') {
	    	addShopCar(labelOrCustomId ,typeId, isEditCustomFlag);
		} else {
			coreMain.addShopCart(labelOrCustomId ,typeId, isEditCustomFlag, "");
		}
	}else{
		addShopBasket(labelOrCustomId ,typeId, isEditCustomFlag);
	}
}
$.widget("custom.catcomplete", $.ui.autocomplete, {
	_renderMenu: function( ul, items ) {
		var that = this;
	   	$.each(items, function( index, item ) {
	       var $li = that._renderItemData(ul, item );
	           $li.find("a").append('<button class="autoAddCartBtn" onclick="addShopBasketByVersion(\'' + item.key + '\', ' + item.typeId + ', 0)">添加</button>'); 
		});
	}
});

/**
 * 取字符串的前toCount个字符
 *
 * @param str 被处理字符串
 * @param toCount 截取长度
 * @param more 后缀字符串
 * @version 2004.11.24
 * @author zhulx
 * @return String
 */
//获取字符数组 

 String.prototype.toCharArray = function() {
     return this.split("");
 }
String.prototype.getBytes = function() {       
	 var cArr = this.match(/[^\x00-\xff]/ig);       
	 return this.length + (cArr == null ? 0 : cArr.length);        
} 
function substring(str,toCount,more){
	var reInt = 0;
	var reStr = "";
	if (str == null)
		return "";
	var tempChar = str.toCharArray();
	for (var kk = 0; kk < tempChar.length && toCount > reInt; kk++) {
		var s1 = tempChar[kk];
		var b = s1.getBytes();
		reInt = reInt+b;
		reStr = reStr+tempChar[kk];
	}
	if (toCount == reInt || (toCount == reInt - 1))
		reStr += more;
	return reStr;
}
//删除此条消息提醒
function closeNotice(ts){
	$(ts).parent().remove();
    $.ajax({
        type: "POST",
        url: $.ctx + '/ci/ciIndexAction!closeNotice.ai2do?noticeId='+$(ts).attr("noticeId"),
        success: function (result) {
	}});
}
//关闭个人通知，并且标记为已读
function closePersonNotice(obj){
	$(obj).parent().remove();
	var noticeId = $(obj).attr("noticeId");
	var actionUrl =  $.ctx + '/ci/ciPersonNoticeAction!updateShowTipStatus.ai2do?pojo.noticeId=' + noticeId;
    $.ajax({
        url:actionUrl,
        type:"POST",
        success:function(result){
       }
    })
}
//关闭系统公告，并且标记为已读
function closeSysNotice(obj){
	$(obj).parent().remove();
	var noticeId = $(obj).attr("noticeId");
	var actionUrl =  $.ctx + '/ci/ciSysAnnouncementAction!updateNotReadStatus.ai2do?sysAnnouncement.announcementId='+noticeId;
	$.ajax({
		type: "POST",
        url: actionUrl,
        success: function (result) {
        }
    });
}
//点击固定区域的标签客户群选项
function _fixedSearchOnClick(obj){
	var txt=$(obj).find("a").html();
	var seachTypeId = $(obj).find("input").val();
	$("#fixedSearchSelect").find("a").html(txt);
	$("#fixedSeachTypeId").val(seachTypeId);
	$("#fixedSearchSelect").click();
	
	//同步index_top.jsp中的搜索方式
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
	$('#seachTypeId').val(seachTypeId);
}

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
	if(getCookieByName("${version_flag}") == "true"){
		var url = "${ctx}/ci/ciMarketAction!newMarketIndex.ai2do";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}else{
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
}
//点击到我的收藏
function showMyCollection(){
	var url = "";
	if(getCookieByName("${version_flag}") == "true"){
		url = "${ctx}/core/ciMainAction!findMarketMain.ai2do?indexLinkType=4";
	}else{
		url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=5";
	}
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//点击到排行榜
function rankingListViewIndex(){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=6";
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//点击资源审批
function showResourceApprove(showType){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=7&resourceShowType="+showType;
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//点击预警监控
function showAlarmIndex(showType){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=8&alarmShowType="+showType;
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//点击信息反馈，进入意见反馈页面
function showFeedback(){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=9&source=1";
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//从回复页面点击“返回意见反馈页面”，根据source返回相应的页面	0：历史反馈		1：意见反馈
function returnFeedback(source) {
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=9&source="+source;
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//点击客户群清单下载列表
function downLoadList(){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=10";
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
</script>
<!--  导航地图子列表 -->
<div class="navMapListBox" id="navMapListBox">
	<dl class="hidden J_new_version_main">
		<dt><a href="${ctx}/ci/ciMarketAction!newMarketIndex.ai2do" target="_new">首页</a></dt>
		<dd><a href="${ctx}/core/ciMainAction!findMarketMain.ai2do?indexLinkType=3" target="_new">标签集市</a></dd>
		<dd><a href="${ctx}/core/ciMainAction!findMarketMain.ai2do?indexLinkType=2" target="_new">用户群集市</a></dd>
		<dd><a href="${ctx}/core/ciMainAction!findMarketMain.ai2do?indexLinkType=1" target="_new">我的用户群</a></dd>
	</dl>
	<dl class="hidden J_old_version_main">
		<dt><a href="${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4" target="_new">首页</a></dt>
		<dd><a href="${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4" target="_new">营销导航</a></dd>
		<dd><a href="${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=6" target="_new">标签集市</a></dd>
		<dd><a href="${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1" target="_new">客户群集市</a></dd>
		<dd><a href="${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=9" target="_new">我的客户群</a></dd>
		<!-- <dd><a href="${ctx}/cia/toushiAnalysisAction!forwardMain.ai2do" target="_blank">智能分析</a></dd> -->
		<% if ("true".equalsIgnoreCase(templateMenu)) { %>
		<dd><a href="${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=3" target="_new">客户群模板</a></dd>
		<% } %>
	</dl>
	<div class="navMapSegLine"></div>
	<dl>
		<dt class="pink">
			<c:choose>
				<c:when test="${isAdmin}">
				<% if("true".equals(enumClassConfig)){%>
					<a href="javascript:void(0);" onclick="showLabelConfig(3);return false;" >标签管理</a>
				<%} else{%>
					<a href="javascript:void(0);" onclick="showLabelConfig(4);return false;" >标签管理</a>
				<%} %>
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0);" onclick="showLabelConfig(1);return false;" >标签管理</a>
				</c:otherwise>
			</c:choose>
		</dt>
		<c:choose>
		  	<c:when test="${!isAdmin || cityId != root}">
				<!--<dd><a href="javascript:void(0);" onclick="showLabelConfig(1);return false;" >我创建的标签</a></dd>-->
		  	</c:when>
		  	<c:when test="${isAdmin}">
		  		<% if("true".equals(enumClassConfig)){%>	
				<dd><a href="javascript:void(0);" onclick="showLabelConfig(3);return false;" >枚举分类配置</a></dd>
				<%} %>
			</c:when>
		</c:choose>
		<c:choose>
		  	<c:when test="${isAdmin && cityId == root}">
				<dd><a href="javascript:void(0);" onclick="showLabelConfig(4);return false;" >标签配置</a></dd>
				<dd><a href="javascript:void(0);" onclick="showLabelConfig(5);return false;" >标签分类配置</a></dd>
		  	</c:when>
		</c:choose>
	</dl>
	<div class="navMapSegLine"></div>
	<dl>
		<dt class="orange"><a href="javascript:void(0);" onclick="showMessageConfig(1);return false;" >消息管理</a></dt>
		<dd><a href="javascript:void(0);" onclick="showMessageConfig(1);return false;" >消息通知</a></dd>
		<dd><a href="javascript:void(0);" onclick="showMessageConfig('');return false;" >消息设置</a></dd>
		<c:choose>
		  	<c:when test="${isAdmin}">
				<dd><a href="javascript:void(0);" onclick="showMessageConfig(3);return false;" >公告管理</a></dd>
			</c:when>
		</c:choose>
	</dl>
	<div class="navMapSegLine"></div>
	<dl>
		<dt class="other">更多</dt>

		<c:choose>
			<c:when test='${cookie[version_flag].value eq "true"}'>
				<dd><a href="${ctx}/core/ciMainAction!findMarketMain.ai2do?indexLinkType=4" target="_new">我的收藏</a></dd>
				<% if("true".equals(rankingList)){%>
				<dd><a href="${ctx}/core/ciMainAction!findMarketMain.ai2do?indexLinkType=5" target="_new">热门推荐</a></dd>
				<%} %>
			</c:when>
			<c:otherwise>
				<dd><a href="javascript:void(0);"  onclick="showMyCollection();return false;">我的收藏</a></dd>
				<% if("true".equals(rankingList)){%>
				<dd><a href="javascript:void(0);"  onclick="rankingListViewIndex();return false;">排行榜</a></dd>
				<%} %>
			</c:otherwise>
		</c:choose>
		<% if("true".equals(downLoadList)){%>
		<dd><a href="javascript:void(0);"  onclick="downLoadList();return false;">清单下载</a></dd>
		<%} %>
		<dd id="resourceApprove"><a href="javascript:void(0);"  onclick="showResourceApprove(1);return false;">资源审批</a></dd>
		<% if("true".equals(feedbackMenu)){ %>
		<dd><a href="javascript:void(0);"  onclick="showFeedback();return false;">意见反馈</a></dd>
		 <% }%>
		<% if("true".equals(alarmMenu)){ %>
        <dd><a href="javascript:void(0);"  onclick="showAlarmIndex(1);return false;">预警监控</a></dd>
        <% }%>
	</dl>
</div>
<div class="topLoginf">
	<div class="loginfInner">
	<ul id="mesNotice" class="msgNoticeTip">
    </ul>
    <ul class="hotLinks fleft">
    	<li class="comboxIndex" id="navMapIcon"><a href="javascript:void(0);" class="icon_nav">导航地图</a></li>
    	<li class="separate hidden J_version_map" >&nbsp;</li>
        <li class="tagNavMap hidden J_version_map" id="tagNavMap"><a href="javascript:void(0);">标签分类</a></li>
    	<%if("true".equals(myLabel)){ %>
        <li style="display: none"  id="_label"><a href="${ctx}/ci/ciLabelInfoAction!init.ai2do" class="icon_boat">标签管理</a></li>
        <%}else{ %>
        <li style="display: none" style="display: none" id="_label"><a href="${ctx}/ci/ciLabelAnalysisAction!initlabelAnalysisIndex.ai2do" class="icon_boat">标签管理</a></li>
        <%} %>
        <li style="display: none" id="_customer"><a href="${ctx}/ci/customersManagerAction!search.ai2do?initOrSearch=init" class="icon_boat">客户群管理</a></li>
		<!--         <li><a href="javascript:void(0);" class="icon_people"><p>觅产品</p></a></li> -->
		<%
           if("true".equals(marketingMenu)){
        %>
        <li style="display: none" id="_mcd"><a href="${ctx}/aibi_ci/mcd.jsp" class="icon_boat">营销策划</a></li>
        <%
           };
        %>
       <!--  <li><a href="${ctx}/ci/peopleBaseAction!index.ai2do" target="_blank" class="icon_boat"><p>人为本</p></a></li> -->
        <% if("true".equals(alarmMenu)){ %>
        <li style="display: none" id="alarm_index"><a href="${ctx}/ci/ciLabelAlarmAction!init.ai2do" class="icon_boat">预警监控</a></li>
        <% }%>
        <%//if("true".equals(logMenu) && logAnalyseOperRight){ //根据是否有管理员权限判断%>
        <% if ("true".equals(logMenu)) { %>
        <li style="display: none" id="_log"><a href="${ctx}/ci/ciLogStatAnalysisAction!logAnalysisIndex.ai2do" class="icon_boat">日志管理</a></li>
		<% } %>
    </ul>
<!--sidebar end -->

	<div class="userMessage fright">
    	<span class="sayHi">欢迎，${userName}</span>
			<div class="colorfulCT mMessage" id="mMessageDiv">
            	<div class="colorfulLeft">&nbsp;</div>
                <div class="colorfulMid" id="mMessage">
                	<font class="bigNum" id="noticeCount"></font>
                    <span  onclick="showNotice();return false;">消息</span>
                </div>
                <!--消息下拉-->
	            <div id="mMessage_list" class="mMessage_list">
	            </div>
            </div>
            <div class="colorfulCTSegLine"></div>
            <div  class="colorfulCT mMessage" id="myCollectionMessageDiv">
            <div class="colorfulMid" id="myCollectionMessage">
                <span onclick="showMyCollection();return false;">我的收藏</span>
            </div>
            <div class="colorfulRight">&nbsp;</div>
            <!-- 我的收藏下拉 -->
            <div id="collectMessageShow" class="mMessage_list">
                <div class="mMessageTop">&nbsp;</div>
                <dl id="collectionMessageList">
                    <dt>最近收藏的 <a href="javascript:void(0);" class="mMsgClose" onclick="closeSlipTip(this)"></a></dt>
                    <dd>
                        <ol id="navigationCalcElement">
                        </ol>
                        <div class="tipCreateBox"><a href="javascript:void(0);" onclick="showMyCollection()" class="tipCreate">更多</a></div>
                    </dd>
                </dl>
                 <dl id="emptyCollectionMessageList" style="display: none;">
                	<dt>最近收藏的 <a href="javascript:void(0);" class="mMsgClose" onclick="closeSlipTip(this)"></a></dt>
                	<dd>
	                	<div class="msgEmptyTip">最近未收藏客户群或标签</div>
                    </dd>
                    <div class="tipCreateBox"><a href="javascript:void(0);" onclick="showMyCollection()" class="tipCreate">更多</a></div>
                </dl>
            </div>
        	</div>
            	<%if(!"false".equals(helperMenu)){ %>
            		<%if("false".equals(standardHelperMenu)){ %>
                	<div class="helper">
                    	<a href="#" class="icon_help" onclick="window.open('${ctx}/ci/ciIndexAction!toHelpPage.ai2do');">帮助</a>
                    </div>
                    <%}else{ %>
                    <div class="helper">
                    	<a href="#" class="icon_help" onclick="downStandardHelp()">帮助</a>
                    </div>
                    <%} %>
                <%} %>
            	<% 
            		String needExit = Configure.getInstance().getProperty("NEED_EXIT"); 
            		if (needExit != null && needExit.equalsIgnoreCase("true")) {
            	%>
                <a href="javascript:quitSys();" class="icon_quit">退出</a>
                <% } %>
               	<a href="javascript:void(0);" class="icon_change hidden J_new_version">切换新版本</a>
              	<a href="javascript:void(0);" class="icon_change hidden J_old_version">切换旧版本</a>
    </div>
</div>
</div><!--header end -->
<!-- 固定搜索框 -->
  <div class="fixedSearchBox" id="fixedSearchBox" >
        <div class="comWidth clearfix" style="position:relative;">
		    <div class="fleft fixedLogo" onclick="showHomePage();return false;"></div>
		    <div class="fright searchControl" >
				<div class="searchSelect fleft" id="fixedSearchSelect"><a href="javascript:void(0)">标签</a><input type="hidden" value="1" /></div>
				<div class="searchText fleft" ><input type="text" value="请输入关键字" id="fixedSearchText" data-val="false"/></div>
				<div class="searchSubmit fright" id="fixedSearchSubmit"><a href="javascript:void(0)">搜索</a></div>
				<input type="hidden" id="fixedSeachTypeId" />
				<input type="hidden" id="fixedClassId" />
            </div>
              <!--标签名下拉层-->
		    <div id="fixedSearchSelect_list" class="slideDownCT searchSelect_list">
				<ul class="slideDownList">
					<li id="first" onclick="_fixedSearchOnClick(this);"><a href="javascript:void(0)">标签</a><input type="hidden" value="1" /> </li>
			        <li onclick="_fixedSearchOnClick(this);"><a class="J_search_custom_showname" href="javascript:void(0)">客户群</a><input type="hidden" value="2" /> </li>
		            <% if ("true".equalsIgnoreCase(templateMenu)) { %>
		            	<li onclick="_fixedSearchOnClick(this);"><a href="javascript:void(0)">模板</a><input type="hidden" value="3" /> </li>
		            <% } %>
				</ul>
		   </div>
		   <!--标签下拉层-->
			<div id="searchText_history_Box" class="slideDownCT searchText_list">
				<ul id="searchText_history_List" class="slideDownList">
				</ul>
			</div>
		</div>
	
  </div>
<div class="tagNavMapListBox "  id="tagNavMapListBox"></div>
<div class="tagNavMapListBoxOther"  id="tagNavMapListBox2"></div>

<script type="text/javascript">
    getNotice();
    window.setInterval(getNotice,1000*60*5);
    function viewDetail(obj){
    	var noticeId = obj.attr("noticeId");
    	var actionUrl =  $.ctx + '/ci/ciPersonNoticeAction!updateReadStatus.ai2do?pojo.noticeId=' + noticeId;
        $.ajax({
            url:actionUrl,
            type:"POST",
            success:function(result){
        	    flag = result.success;
        	    if(flag){
            	    obj.parents("dl").hide();
        	    	showNotice(noticeId);
            	}
           }
        })
    }
    function viewSysAnnounceDetail(obj){
    	var noticeId = obj.attr("noticeId");
    	var actionUrl =  $.ctx + '/ci/ciSysAnnouncementAction!updateReadStatus.ai2do?sysAnnouncement.announcementId='+noticeId;
    	$.ajax({
    		type: "POST",
            url: actionUrl,
            success: function (result) {
    			flag = result.success;
    			if(flag){
    				obj.parents("dl").hide();
    		    	showAnnounce(noticeId);
        		}
            }
        });
    }
    function getNotice(){
        var actionUrl = $.ctx + '/ci/ciIndexAction!getNotice.ai2do?';
        $.ajax({
            type: "POST",
            url: actionUrl,
            success: function (result) {
                var noticeModel = result.noticeModel;
                if("欢迎，"==$("#welcome").text()){
                    $("#welcome").html("<div style='width:110px;   overflow:hidden; text-overflow:ellipsis; white-space:nowrap;'><a title="+noticeModel.userName+">欢迎，"+noticeModel.userName+"</a></div>");
                }
                $("#messageArea").html("");
                $("#noticeCount").html(0);
                var sysCount = 0;
                if(noticeModel.sysCount){
                	sysCount = noticeModel.sysCount
                }
                var personNoticeCount = 0;
                if(noticeModel.personNoticeCount){
                	personNoticeCount = noticeModel.personNoticeCount;
                }
                var totalNoticeCount = sysCount + personNoticeCount;
                var messageTxt ='<div class="mMessageTop">&nbsp;</div><dl><dt>消息中心<a href="javascript:void(0);" class="mMsgClose" onclick="closeSlipTip(this)"></a></dt><dd>'+ 
                		'<ul>' +
                		'<li><a href="javascript:void(0);" onclick="showMessageConfig(1,2)">查看公告</a>'+sysCount+'条系统公告</li>'+
                		'<li><a href="javascript:void(0);" onclick="showMessageConfig(1,1)">查看通知</a>'+personNoticeCount+'条个人通知</li>'+
                		'<div class="tipCreateBox"><a href="javascript:void(0);" onclick="showMessageConfig()"  class="tipSet">提醒设置</a></div>'+
                		'</ul></dd></dl>'
                if(totalNoticeCount>0){
                	$("#noticeCount").css("display","block");
                	if(totalNoticeCount>99){
	                	$("#noticeCount").html("99+");
                	}else{
                		$("#noticeCount").html(totalNoticeCount);
                	}
                }else{
                	$("#noticeCount").html(0);
                	messageTxt ='<div class="mMessageTop">&nbsp;</div><dl><dt>消息中心<a href="javascript:void(0);" class="mMsgClose" onclick="closeSlipTip(this)"></a></dt><dd>'+
                	'<div class="msgEmptyTip">消息中心是空的</div>'+
            		'<div class="tipCreateBox"><a href="javascript:void(0);" onclick="showMessageConfig()"  class="tipSet">提醒设置</a></div>'+
            		'</dd></dl>';
                }
                $("#mMessage_list").html(messageTxt);
                var alarmTxt = '<ul>' +
                        '<li class="first">'+noticeModel.customerAlarmCount+'条客户群预警，<a href="${ctx}/ci/ciCustomersAlarmAction!initRecord.ai2do?fromPageFlag=1">查看</a></li>' +
                        '<li>'+noticeModel.labelAlarmCount+'条标签预警，<a href="${ctx}/ci/ciLabelAlarmAction!initRecord.ai2do?fromPageFlag=1">查看</a></li>' +
                        '</ul>' + 
                        '<div class="tipCreateBox"><a href="${ctx}/ci/ciUserNoticeSetAction!initUserNoticeSet.ai2do?fromPageFlag=2" class="tipSet">提醒设置</a></div>';
                $("#alarmArea").html(alarmTxt);
                if(noticeModel.customerAlarmCount+noticeModel.labelAlarmCount>0){
                	$("#alarmCount").css("display","block");
                	$("#alarmCount").html(noticeModel.customerAlarmCount+noticeModel.labelAlarmCount);
                }else{
                	$("#alarmCount").css("display","none");
                }
                var recentNoticeMap = noticeModel.recentNoticeMap;
                $("#mesNotice").html("");
                for(var key in recentNoticeMap){
                    var noticeObj = recentNoticeMap[key];
                    var noticeName = noticeObj.noticeName;
                    //var noticeName = substring(noticeObj.noticeName,50,"...");
                    var li = "<li title='"+noticeName+"'><a href='javascript:void(0);' class='msgEdit'  noticeId='"+noticeObj.noticeId+"' onclick='viewDetail($(this));return false;'>"+noticeName+"</a><a  href='javascript:void(0);' noticeId='"+noticeObj.noticeId+"'  class='msgClose'  onclick='closePersonNotice(this)'></a></li>"
                    $("#mesNotice").append(li);
                }
                var sysNoticeMap = noticeModel.sysNoticeMap;
                for(var key in sysNoticeMap){
                    var noticeObj = sysNoticeMap[key];
                    var	announcementName = noticeObj.announcementName;
                    //var	announcementName = substring(noticeObj.announcementName,50,"...");
                    var li = "<li title='"+announcementName+"'><a  class='msgEdit'  href='javascript:void(0);' noticeId='"+noticeObj.announcementId+"' onclick='viewSysAnnounceDetail($(this));return false;'>"+announcementName+"</a> <a  href='javascript:void(0);'  class='msgClose' onclick='closeSysNotice(this)' noticeId='"+noticeObj.announcementId+"'></a></li>"
                    $("#mesNotice").append(li);
                }
                $(".main_notice dl dd .close").click(function(){
                    $(this).parents("dl").hide();
                    $.ajax({
                        type: "POST",
                        url: $.ctx + '/ci/ciIndexAction!closeNotice.ai2do?noticeId='+$(this).attr("noticeId"),
                        success: function (result) {

                        }});
                })

            }
        });
    }
    $(document).ready(function () {
        $("#notice_show").dialog({
            autoOpen: false,
            width: 900,
            resizable: false,
            title: "通知公告",
            modal: true
        });
        $('#notice_show').bind('dialogclose', function(event, ui) {
        	getNotice();
        });
    });
    
    //显示系统公告
    function showAnnounce(noticeId){
    	if(noticeId==undefined)
            noticeId = "";
    	var type="2"; 
    	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=4&messageSearchType="+type+"&noticeId="+noticeId+"&noticeType="+type;
        window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    }
    //显示个人通知
    function showNotice(noticeId){
        if(noticeId==undefined)
            noticeId = "";
        var type="1"; 
        var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=4&messageSearchType="+type+"&noticeId="+noticeId+"&noticeType="+type;
        window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    }

    
    function quitSys(){
    	var url = "${ctx}/aibi_ci/relogin.jsp";
    	window.location.href = url;
    }

    //标准版本帮助下载功能(只含操作手册)
    function downStandardHelp(){
        var param = '' + 'id='+encodeURI(encodeURI('COCyhsc.pdf'));
    	var actionUrl =  "${ctx}/ci/ciIndexAction!helpDown.ai2do?"+param;
    	window.location.href = actionUrl;
    }
    
    function closeSlipTip(ths) {
    	$(ths).parents(".mMessage_list").slideUp();
    }
  	//导航菜单更改为异步刷新-标签管理
    function showLabelConfig(showType){
    	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=3&labelSearchType="+showType;
    	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    }
  //导航菜单更改为异步刷新- 消息管理
    function showMessageConfig(showType,noticeType){
    	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=4&messageSearchType="+showType+"&noticeType="+noticeType;
    	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    }
  
</script>
<div id="notice_show" style="display:none;width:900px;height:600px">
    <iframe name="notice_show" scrolling="no" allowtransparency="true" src=""
            id="notice_show_frame" framespacing="0" border="0" frameborder="0"
            style="width:900px;height:600px"></iframe>
</div>
<input type="hidden" id="isCreateCustomPage" value=""/>
<%-- <div class="mainCT">
	<iframe name="main" scrolling="no" src="${ctx}/aibi_ci/ciLabelInfo/ciLabelInfoMap.jsp" id="main" framespacing="0" border="0" frameborder="0" style="width:100%;height:100%"></iframe>
</div><!--mainCT end --> --%>

