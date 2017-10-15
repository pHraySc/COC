<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="LABEL_TYPE_SIGN" value="<%=ServiceConstants.LABEL_TYPE_SIGN%>"></c:set>
<%
	String labelNumber = Configure.getInstance().getProperty("LABEL_NUMBER");
	String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
%>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>

<script type="text/javascript">
	$(function(){
		$("#shopNum").html($("#shopCartGoodsList ol li").length);
		$("#shopNumBasket").html($("#shopCartGoodsList ol li").length);
		$("#shopCartBox").show();
		$("#quoteSource").val("");
		$(document).click(function(){
			$(".label_map_show").removeClass("onActive");
		});
		//标签分类
		$("#labelMarketLink").cocFanAnimate({
			targetObj:"#tagNavMapListBox2",
			hoverClass:"viewNavLiHover",
			top:($("#labelMarketLink").offset().top + $("#labelMarketLink").height() + 4),
			position:"absolute",
			absoluteObj:".loginfInner"
		});
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
	$(document).ready(function(){
		$(document).bind("click",function(){
			var e=e||window.event;
			e.stopPropagation? e.stopPropagation() : e.cancelBubble=true;
			$("#searchHistoryBox").hide();
		});
		$("#importClientBtnTip").hide();
		$("#firsttimelabel_tip").hide();
		var indexClick;
		//加载客户群集市
		var userOperType = $("#userOperType").val();
		if(userOperType == '' || userOperType == '0'){
			fowardIndexPage();
		}else{
			if(userOperType == '1'){//点击我的关注
				showUserAttentionLabel();
			}else if(userOperType == '2'){//点击我的使用
				showUserUsedLabel();
			}
		}
	});
	//加载标签列表
	function showLabelIndex(){
		$("#viewNavBox").removeClass("viewNavBoxClick");
		$(".pathNavBox").remove();
		$("#labelMarketLink").parent().find("li").removeClass("current");
		$("#labelMarketLink").addClass("current");
		$("#aibi_area_data").empty();
		$("#aibi_area_data").load("${ctx}/ci/ciIndexAction!effectiveLabelIndex.ai2do",function (){loadLabelList();});
	}
	//点击标签搜索列表导航  标签 获得所有标签列表
	function showRefreshLabelIndex(){
		var seachTypeId=$("#seachTypeId").val();
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId="+seachTypeId+"&seachKeyWord=";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	function fowardIndexPage(){
		$("#aibi_area_data").empty();
		$("#importClientBtnTip").hide();
		var seachTypeId=$("#forward_seachTypeId").val();
		var keyWord = $("#keyword").val();
		var dataVal = $("#keyword").attr("data-val");
		if(dataVal == "false"){
			keyWord = "";
		}
		if(seachTypeId == ""){
			var refreshType = $("#refreshType").val();
			if(refreshType == ''){
				seachTypeId="4";
			}else{
				seachTypeId = refreshType;
			}
		}
		if(seachTypeId != null && seachTypeId != ""){
			//标签列表
			if(seachTypeId == "1"){
				showLabelIndex();
			}
			//客户群集市
			if(seachTypeId == "2"){
				var isDefaultLoad = $("#isDefaultLoad").val();
				if(isDefaultLoad == 1){
					getDefaultCustoms();
				}else{
					showCustomGroupMarket();
				}
			}
			//模板列表
			if(seachTypeId == "3"){
				showTemplateListIndex();
			}
			//营销导航
			if(seachTypeId == "4"){
				showMarketNavigation();
			}
			//标签全景视图
			if(seachTypeId == "6"){
				var labelTypeMap = $("#labelTypeMap").val();
				showAllLabelMap(labelTypeMap);
			}
			//更多客户群
			if(seachTypeId == "7"){
				var sceneId = $("#sceneId").val();
				var sceneName = $("#sceneName").val();
				getMoreCustom(sceneId,sceneName);
			}
			//更多标签
			if(seachTypeId == "8"){
				var sceneId = $("#sceneId").val();
				var sceneName = $("#sceneName").val();
				getMoreLabel(sceneId,sceneName);
			}
			//我的客户群
			if(seachTypeId == "9"){
                showMyCustomGroup();
			}
		}
	}
	//获得标签查询列表
	function loadLabelList(){
		$(window).unbind("scroll");
		$("#importClientBtnTip").hide();
		$("#firsttimelabel_tip").hide();

		$("#queryResultListBox").empty();
		$("#noMoreResults").hide();
		var keyWord = $.trim($("#keyword").val());
		var dataVal = $("#keyword").attr("data-val");
		if(dataVal == "false"){
			keyWord="";
		}
		if(keyWord == ""){
			keyWord = $.trim($("#fixedSearchText").val());
			var fixedDataVal = $("#fixedSearchText").attr("data-val");
			if(fixedDataVal == "false"){
				keyWord = "";
			}
		}
		var labelKeyWord = keyWord;
		var topLabelId = $("#classId").val();
		var updateCycle = $("#updateCycle").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var dataScope = $("#formDataScope").val();
		var order = $("#order").val();
		var orderBy = $("#orderBy").val();
		var sceneId = $("#sceneIdL").val();
		$("#noMoreLabelResults").hide();
		$("#noResultsLabel").hide();
		//设置标签地图页数为全局变量
		var totalPage;
		//需要获取总页数
		var getTotalUrl = $.ctx + "/ci/ciIndexAction!findEffectiveLabelNum.ai2do";
		$.post(getTotalUrl, {"labelKeyWord":labelKeyWord,"topLabelId":topLabelId,"dataScope":dataScope,"searchBean.updateCycle":updateCycle,"searchBean.sceneId":sceneId,"searchBean.startDate":startDate,"searchBean.endDate":endDate,"pager.orderBy":orderBy,"pager.order":order}, function(result) {
			if (result.success) {
				$(".pathNavNext").html(labelKeyWord);
				if(keyWord == ''){
					$(".pathNavArrowRight").hide();
				}
				$(".amountNum").html(result.totalSize);
				var totalPage = result.totalPage;
				var actionUrl = $.ctx + "/ci/ciIndexAction!findEffectiveLabel.ai2do?pager.totalPage=" + totalPage;
				var i = totalPage;
				var queryResultListBox = $("#queryResultListBox");
				if(i != 0){
					queryResultListBox.scrollPagination({
						"contentPage" : actionUrl, // the url you are fetching the results
						"contentData" : {
							"labelKeyWord" : labelKeyWord,
							"topLabelId" : topLabelId,
							"searchBean.updateCycle" : updateCycle,
							"searchBean.startDate" : startDate,
							"searchBean.endDate" : endDate,
							"pager.orderBy" : orderBy,
							"pager.order" : order,
							"searchBean.sceneId" : sceneId,
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
							i--;
							if(i==0){
								$("#loading").fadeOut("normal", function(){
									if($("#queryResultListBox").attr("scrollPagination") == "disabled"){
										$("#noMoreLabelResults").fadeIn();
									}
								});
							}else{
								$("#loading").fadeOut("normal", function(){
									if ($(window).height() >= $(document).height()){
										$(window).scroll();
									}
								});
							}
						}
					});
				}else{
					$("#noResultsLabel").show();
					$("#loading").hide();
				}
			} else {
				commonUtil.create_alert_dialog("failedDialog", {
					"txt":result.msg,
					"type":"failed",
					"width":500,
					"height":200
				});
			}
		});
	}
	//标签查询  过滤条件的添加
	function multiLabelSearch(){
		$(window).scrollTop(0);
		$("#formDataScope").val("");
		$("#startDate").val("");
		$("#endDate").val("");
		$("#updateCycle").val("");
		$("#sceneIdL").val("");
		var sceneIdStrs = "";
		$(".selectedItemDetail").each(function(){
			var idStr =$.trim($(this).attr("id")).split("_")
			var valStr = $(this).html();
			if("sceneName" == idStr[0]){
				if(idStr.length > 1){
					sceneIdStrs += idStr[1]+",";
					$("#sceneIdL").attr("value",sceneIdStrs);
				}
			}else if("updateCycle" == idStr[0]){
				if($.trim($(this).html()) == "日周期") valStr = 1;
				if($.trim($(this).html()) == "月周期") valStr = 2;
				$("#updateCycle").attr("value",valStr);
			}else if("dateType" == idStr[0]){
				if($(this).html().indexOf("大于")>=0){
					var startDate = $.trim($(this).html()).split("大于");
					$("#startDate").attr("value",startDate[1]);
				}else if($(this).html().indexOf("小于")>=0){
					var endDate = $.trim($(this).html()).split("小于");
					$("#endDate").attr("value",endDate[1]);
				}else if($(this).html().indexOf("至")>=0){
					var date = $.trim($(this).html()).split("至");
					var startDate = date[0];
					var endDate = date[1];
					$("#startDate").attr("value",startDate);
					$("#endDate").attr("value",endDate);
				}else{
					if($.trim($(this).html()) == "一天") valStr = "oneDay";
					if($.trim($(this).html()) == "一个月") valStr = "oneMonth";
					if($.trim($(this).html()) == "三个月") valStr = "threeMonth";
					$("#formDataScope").attr("value",valStr);
				}
			}
		});
		loadLabelList();
	}
	//营销导航
	function showMarketNavigation(){
		$("#importClientBtnTip").hide();
		$("#firsttimelabel_tip").hide();
		$("#keyword").val( "请输入关键字");
		$("#viewNavBox").removeClass("viewNavBoxClick");
		$(".pathNavBox").remove();
		$("#marketLink").parent().find("li").removeClass("current");
		$("#marketLink").addClass("current");
		$(window).unbind("scroll");
		$("#aibi_area_data").empty();
		$("#aibi_area_data").load($.ctx + "/ci/ciIndexAction!marketNavigationIndex.ai2do");
	}

	//营销导航获得更多标签
	function getMoreLabel(sceneId,sceneName){
		$("#importClientBtnTip").hide();
		$("#firsttimelabel_tip").hide();
		$("#keyword").val( "请输入关键字");
		$(".pathNavBox").remove();
		$("#moreMarketSceneId").attr("value",sceneId);
		$("#moreMarketSceneName").attr("value",sceneName);
		$("#aibi_area_data").load("${ctx}/ci/ciIndexAction!effectiveLabelIndex.ai2do",function (){
			var moreMarketSceneId = $("#moreMarketSceneId").val();
			var moreMarketSceneName = $("#moreMarketSceneName").val();
			if(moreMarketSceneId != ""){
				sceneId = moreMarketSceneId;
				$("#moreMarketSceneId").attr("value","");
				$("#sceneName1_"+sceneId).children("a").addClass("selectOptionValueHover");
				$("#sceneAll").removeClass("selectOptionValueHover");
				var idStr = "sceneName_"+sceneId;
				var value = moreMarketSceneName;
				var selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
				var selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\""+idStr+"\" >"+value+"</div>");
				var selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
				selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
				$("#selectedItemListBox").append(selectedItemBox);
				var closeLi = $("#sceneName1_"+sceneId).parent();
				selectedItemClose.click(function(){
					$(this).parent().remove();
					$("#sceneName1_"+sceneId).children("a").removeClass("selectOptionValueHover");
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
					multiLabelSearch();
				});
				$("#useNumOrder").addClass("sortItemActiveDown");
				$("#useNumOrder").addClass("asc");
				sortByName('USE_TIMES',$("#useNumOrder"));
			}
		});
		$("#customerInfoLink").parent().find("li").removeClass("current");
		<% if ("true".equalsIgnoreCase(templateMenu)) { %>
		$("#templateInfoLink").parent().find("li").removeClass("current");
		<% } %>
		$("#labelMarketLink").addClass("current");
	}
	//默认加载客户群集市  加地市筛选条件
	function getDefaultCustoms(){
		$("#keyword").val( "请输入关键字");
		$("#firsttimelabel_tip").hide();
		$("#customerInfoLink").parent().find("li").removeClass("current");
		$("#customerInfoLink").addClass("current");
		$(".pathNavBox").remove();
		$("#aibi_area_data").empty();
		var cityId = $("#userCityId").val();
		if(cityId != ""){
			$("#aibi_area_data").load($.ctx+"/ci/customersManagerAction!loadPagedCustomGroupMarketIndex.ai2do",function(){
				$("#timeOrder").attr("title","点击按最新修改时间从远到近排序");
				$("#timeOrder").addClass("desc");
				$("#timeOrder").addClass("sortItemActiveDown");
				$("#order").val("desc");
				$("#orderBy").val("NEW_MODIFY_TIME");
				$("#city_"+cityId).find("a").click();
			});
		}
	}
	//营销导航获得更多客户群
	function getMoreCustom(sceneId,sceneName){
		$("#keyword").val( "请输入关键字");
		$("#firsttimelabel_tip").hide();
		$("#customerInfoLink").parent().find("li").removeClass("current");
		$("#customerInfoLink").addClass("current");
		$(".pathNavBox").remove();
		$("#aibi_area_data").empty();
		$("#moreMarketSceneId").attr("value",sceneId);
		$("#moreMarketSceneName").attr("value",sceneName);
		var sceneId = $("#moreMarketSceneId").val();
		$("#aibi_area_data").load($.ctx+"/ci/customersManagerAction!loadPagedCustomGroupMarketIndex.ai2do",function(){
			if(sceneId != ""){
				$(".pathNavArrowRight").hide();
				$("#sceneId").attr("value",sceneId);
				$("#moreMarketSceneId").attr("value","");
				$("#sceneAll").removeClass("selectOptionValueHover");
				$("#sceneName1_"+sceneId).children("a").addClass("selectOptionValueHover");
				var idStr = "sceneName_"+sceneId;
				var value = $("#moreMarketSceneName").val();
				var selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
				var selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\""+idStr+"\" >"+value+"</div>");
				var selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
				selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
				$("#selectedItemListBox").append(selectedItemBox);
				var closeLi = $("#sceneName1_"+sceneId).parent();
				selectedItemClose.click(function(){
					$(this).parent().remove();
					$("#sceneName1_"+sceneId).children("a").removeClass("selectOptionValueHover");
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
					multiCustomSearch();
				});
				$("#useNumOrder").addClass("sortItemActiveDown");
				$("#useNumOrder").addClass("asc");
				sortByName('USECOUNT',$("#useNumOrder"));
			}
		});
	}
	//展示客户群集市页面
	function showCustomGroupMarket(){
		$("#firsttimelabel_tip").hide();
		$("#viewNavBox").removeClass("viewNavBoxClick");
		$("#customerInfoLink").parent().find("li").removeClass("current");
		$("#customerInfoLink").addClass("current");
		$(".pathNavBox").remove();
		$("#aibi_area_data").empty();
		//当第一次加载完成之后会显示该div，所以每次展示标签地图之前都需要初始化为不可显示。
		$("#aibi_area_data").load($.ctx+"/ci/customersManagerAction!loadPagedCustomGroupMarketIndex.ai2do",function(){showCustomGroupMarketList();});
	}
	function showMyCustomGroup(){
		$("#firsttimelabel_tip").hide();
		$("#viewNavBox").removeClass("viewNavBoxClick");
		$("#myCustomGroupLink").parent().find("li").removeClass("current");
		$("#myCustomGroupLink").addClass("current");
		$(".pathNavBox").remove();
		$("#aibi_area_data").empty();
		//当第一次加载完成之后会显示该div，所以每次展示标签地图之前都需要初始化为不可显示。
		$("#aibi_area_data").load($.ctx+"/ci/customersManagerAction!search.ai2do?initOrSearch=init");
	}
	function forwardLabelMap(){
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=6";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	function forwardGustomGroupMarket(){
		//默认加载客户群 加上本地市过滤   搜索框加载客户群集市不加本地市过滤 1:默认加载客户群集市
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=2&isDefaultLoad=1";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	function forwardMarket(){
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=4";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	function forwardTemplate(){
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=3";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	//	新增Top100页面
	function forwardTop100() {
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=11";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	function forwardMyCustomGroup(){
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=9";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
    function forwardDataSource(){
        var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=9";
        window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    }
    function forwardLabelCount(){
        var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=9";
        window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
    }
	function forwardMoreGustomGroupMarket(sceneId,sceneName){
		sceneName = encodeURIComponent(encodeURIComponent(sceneName));
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=7&sceneId="+sceneId+"&sceneName="+sceneName;
		window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	function forwardMoreLabelMarket(sceneId,sceneName){
		sceneName = encodeURIComponent(encodeURIComponent(sceneName));
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?refreshType=8&sceneId="+sceneId+"&sceneName="+sceneName;
		window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	//var thumbnailDatas = [];
	//var customGroupIds = [];
	//var customUpdateCycles = [];
	//展示客户群集市列表
	function showCustomGroupMarketList(){
		//已经在scrollpagination.js 中调用绑定事件之前做了解除之前的绑定，但是在满足特定条件下，点击第一级标签时，
		//会首先去触发滚动事件（估计是后面的某块代码修改了页面中的内容，内容改变后有些情况下会自动触发滚动条事件），然后才
		//执行scrollPagination方法做新的绑定。但是这样不是我们想要的，所以就在触发scorll之前先解除之前的绑定。
		$(window).unbind("scroll");
		$("#firsttimelabel_tip").hide();
		$("#queryResultListBox").empty();
		$("#noMoreResults").hide();
		$("#noResults").hide();
		var keyWord = $.trim($("#keyword").val());
		if(keyWord == "请输入关键字"){
			keyWord="";
		}
		if(keyWord == ""){
			keyWord = $.trim($("#fixedSearchText").val());
			var fixedDataVal = $("#fixedSearchText").attr("data-val");
			if(fixedDataVal == "false"){
				keyWord = "";
			}
		}
		var customGroupName=keyWord;
		$("#customGroupName").val(customGroupName);
		$("#noMoreCustomGroupResults").hide();
		$("#noResultsCustomGroup").hide();
		//设置标签地图页数为全局变量
		var totalPage;
		//需要获取总页数
		var getTotalUrl = $.ctx + "/ci/customersManagerAction!findCustomTotalNum.ai2do";
		$.post(getTotalUrl, $("#multiSearchForm").serialize(), function(result) {
			if (result.success) {
				if(keyWord == ''){
					$(".pathNavArrowRight").hide();
				}
				$(".pathNavNext").html(keyWord);
				$(".amountNum").html(result.totalSize);
				var formArray = $("#multiSearchForm").serializeArray();
				var data = convertArray(formArray);
				var totalPage = result.totalPage;
				var actionUrl = $.ctx + "/ci/customersManagerAction!customersList.ai2do?pager.totalPage=" + totalPage;
				var i = totalPage;
				var queryResultListBox = $("#queryResultListBox");
				if(i != 0){
					queryResultListBox.scrollPagination({
						"contentPage" : actionUrl, // the url you are fetching the results
						"contentData" : data, // these are the variables you can pass to the request, for example: children().size() to know which page you are
						"scrollTarget" : $(window),// who gonna scroll? in this example, the full window
						"heightOffset" : 10,
						"currentPage" : 1,
						"totalPage" : totalPage,
						"beforeLoad" : function() { // before load function, you can display a preloader div
							$("#loading").fadeIn();
							//thumbnailDatas = [];
							//customGroupIds = [];
							//customUpdateCycles = [];
						},
						"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
							$(elementsLoaded).fadeInWithDelay();
							i--;
							if(i==0){
								$("#loading").fadeOut("normal", function(){
									if($("#queryResultListBox").attr("scrollPagination") == "disabled"){
										$("#noMoreCustomGroupResults").fadeIn();
									}
								});
							}else{
								$("#loading").fadeOut("normal", function(){
									if ($(window).height() >= $(document).height()){
										$(window).scroll();
									}
								});
							}
							//for(var j=0;j<thumbnailDatas.length;j++){
							//var thumbnailData = thumbnailDatas[j];
							//var customGroupId = customGroupIds[j];
							//var customUpdateCycle = customUpdateCycles[j];
							//周期性(有客户群缩略图数据) 显示静态图片
							//if(thumbnailData != null && thumbnailData !="" && customUpdateCycle != 1){
							//showChart(thumbnailData,customGroupId);
							//}else{//一次性  周期性(无客户群缩略图数据) 显示静态图片
							//$('#chart'+customGroupId).addClass("graphOnceShow");
							//}
							//}
						}
					});
				}else{
					$("#noResultsCustomGroup").show();
					$("#loading").hide();
				}
			} else {
				commonUtil.create_alert_dialog("failedDialog", {
					"txt":result.msg,
					"type":"failed",
					"width":500,
					"height":200
				});
			}
		});
	}

	function backMultiCustom(){
		//客户群集市列表 , 我的客户群列表 返回刷新不同的页面
		var seachTypeId=$("#forward_seachTypeId").val();
		if(seachTypeId == ""){
			var refreshType = $("#refreshType").val();
			if(refreshType == ''){
				seachTypeId="4";
			}else{
				seachTypeId = refreshType;
			}
		}
		if(seachTypeId == '9'){
			//返回我的客户群列表
			var isEditCustom = $("#isEditCustom").val();
			if(isEditCustom == 'true'){
				$("#isEditCustom").val("false");
				$.fn.weakTip({"tipTxt":"客户群修改成功！","backFn":function(){
					if($("#isSimpleSearch").val() == "true"){
						simpleSearch()
					}else{
						search();
					}
				}});
			}else{
				if($("#isSimpleSearch").val() == "true"){
					simpleSearch()
				}else{
					search();
				}
			}

		}else{
			//返回客户群集市列表
			multiCustomSearch();
		}
	}
	function multiCustomSearch(){
		$(window).scrollTop(0);
		$("#updateCycle").val("");
		$("#maxCustomNum").val("");
		$("#minCustomNum").val("");
		$("#dateType_").val("");
		$("#isPrivate").val("");
		$("#createTypeId").val("");
		$("#startDate").val("");
		$("#endDate").val("");
		$("#dataStatus").val("");
		$("#sceneIdC").val("");
		$("#createCityId").val("");
		var sceneIdStrs = "";
		if($("#myCreateCustom input").attr("checked")){
			$("#isMyCustom").attr("value","true");
		}else{
			$("#isMyCustom").attr("value","false");
		}
		$(".selectedItemDetail").each(function(){
			var idStr =$.trim($(this).attr("id")).split("_")
			var valStr = $(this).html();
			if("sceneName" == idStr[0]){
				if(idStr.length > 1){
					sceneIdStrs += idStr[1]+",";
					$("#sceneIdC").attr("value",sceneIdStrs);
				}
			}else if("customNum" == idStr[0]){
				if($(this).html().indexOf("大于")>=0){
					var minNum = $.trim($(this).html()).split("大于");
					$("#minCustomNum").attr("value",minNum[1]);
				}else if($(this).html().indexOf("小于")>=0){
					var maxNum = $.trim($(this).html()).split("小于");
					$("#maxCustomNum").attr("value",maxNum[1]);
				}else{
					var nums = $.trim($(this).html()).split("-");
					var minNum = nums[0];
					var maxNum = nums[1];
					$("#minCustomNum").attr("value",minNum);
					$("#maxCustomNum").attr("value",maxNum);
				}
			}else if("updateCycle" == idStr[0]){
				if($.trim($(this).html()) == "一次性") valStr = 1;
				if($.trim($(this).html()) == "日周期") valStr = 3;
				if($.trim($(this).html()) == "月周期") valStr = 2;
				$("#updateCycle").attr("value",valStr);
			}else if("createTypeName" == idStr[0]){
				if(idStr.length > 1){
					$("#createTypeId").attr("value",idStr[1]);
				}
			}else if("dateType" == idStr[0]){
				if($(this).html().indexOf("大于")>=0){
					var startDate = $.trim($(this).html()).split("大于");
					$("#startDate").attr("value",startDate[1]);
				}else if($(this).html().indexOf("小于")>=0){
					var endDate = $.trim($(this).html()).split("小于");
					$("#endDate").attr("value",endDate[1]);
				}else if($(this).html().indexOf("至")>=0){
					var date = $.trim($(this).html()).split("至");
					var startDate = date[0];
					var endDate = date[1];
					$("#startDate").attr("value",startDate);
					$("#endDate").attr("value",endDate);
				}else{
					if($.trim($(this).html()) == "一天") valStr = 1;
					if($.trim($(this).html()) == "一个月") valStr = 2;
					if($.trim($(this).html()) == "三个月") valStr = 3;
					$("#dateType_").attr("value",valStr);
				}
			}else if("isPrivate" == idStr[0]){
				if($.trim($(this).html()) == "共享") valStr = 0;
				if($.trim($(this).html()) == "非共享") valStr = 1;
				$("#isPrivate").attr("value",valStr);
			}else if("customDataStatus" == idStr[0]){
				if(idStr.length > 1){
					$("#dataStatus").attr("value",idStr[1]);
				}
			}else if("city" == idStr[0]){
				if(idStr.length > 1){
					$("#createCityId").attr("value",idStr[1]);
				}
			}
		});
		showCustomGroupMarketList();
	}
	//客户群模板首页
	function showTemplateIndex(){
		$("#importClientBtnTip").hide();
		$("#firsttimelabel_tip").hide();
		showHotShareTemplate();
		showTemplateList();
	}
	function showHotShareTemplate(){
		var getTotalUrl = $.ctx + "/ci/templateManageAction!loadHotShareTemplate.ai2do";
		$("#hotMasterPlateListBox").load(getTotalUrl);
	}
	function showTemplateList(){
		$("#noMoreTemplateResults").hide();
		$("#noMoreResults").hide();
		$("#noResults").hide();
		$("#noResultsTemplate").hide();
		$("#masterPlateResultTBox").empty();
		var keyWord = $.trim($("#keyword").val());
		if(keyWord == "请输入关键字"){
			keyWord="";
		}
		$("#templateName").val(keyWord);
		//需要获取总页数
		var getTotalUrl = $.ctx + "/ci/templateManageAction!findTemplateTotalNum.ai2do";
		$.post(getTotalUrl, $("#multiSearchForm").serialize(), function(result) {
			if (result.success) {
				if(keyWord == ''){
					$(".pathNavArrowRight").hide();
				}
				$(".pathNavNext").html(keyWord);
				$(".amountNum").html(result.totalSize);
				var formArray = $("#multiSearchForm").serializeArray();
				var data = convertArray(formArray);
				var totalPage = result.totalPage;
				var actionUrl = $.ctx + "/ci/templateManageAction!findTemplateList.ai2do?pager.totalPage=" + totalPage;
				var i = totalPage;
				if(i != 0){
					$("#masterPlateResultTBox").scrollPagination({
						"contentPage" : actionUrl, // the url you are fetching the results
						"contentData" : data, // these are the variables you can pass to the request, for example: children().size() to know which page you are
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
									if($("#masterPlateResultTBox").attr("scrollPagination") == "disabled"){
										$("#noMoreTemplateResults").fadeIn();
									}
								});
							}else{
								$("#loading").fadeOut("normal", function(){
									if ($(window).height() >= $(document).height()){
										$(window).scroll();
									}
								});
							}
						}
					});
				}else{
					$("#noResultsTemplate").show();
					$("#loading").hide();
				}
			} else {
				commonUtil.create_alert_dialog("failedDialog", {
					"txt":result.msg,
					"type":"failed",
					"width":500,
					"height":200
				});
			}
		});
	}
	//多条件客户群模板查询
	function multiTemplateSearch(){
		$("#sceneId").val("");
		$("#isPrivate").val("");
		$("#startDate").val("");
		$("#endDate").val("");
		$("#dateType_").val("");
		$(".selectedItemDetail").each(function(){
			var idStr =$.trim($(this).attr("id")).split("_")
			var valStr = $(this).html();
			if("sceneName" == idStr[0]){
				if(idStr.length > 1){
					$("#sceneId").attr("value",idStr[1]);
				}
			}else if("dateType" == idStr[0]){
				if($(this).html().indexOf("大于")>=0){
					var startDate = $.trim($(this).html()).split("大于");
					$("#startDate").attr("value",startDate[1]);
				}else if($(this).html().indexOf("小于")>=0){
					var endDate = $.trim($(this).html()).split("小于");
					$("#endDate").attr("value",endDate[1]);
				}else if($(this).html().indexOf("至")>=0){
					var date = $.trim($(this).html()).split("至");
					var startDate = date[0];
					var endDate = date[1];
					$("#startDate").attr("value",startDate);
					$("#endDate").attr("value",endDate);
				}else{
					if($.trim($(this).html()) == "一天") valStr = 1;
					if($.trim($(this).html()) == "一个月") valStr = 2;
					if($.trim($(this).html()) == "三个月") valStr = 3;
					$("#dateType_").attr("value",valStr);
				}
			}else if("isPrivate" == idStr[0]){
				if($.trim($(this).html()) == "公有") valStr = 0;
				if($.trim($(this).html()) == "私有") valStr = 1;
				$("#isPrivate").attr("value",valStr);
			}
		});
		showTemplateList();
	}
	//标签收藏图标点击事件
	function attentionOper(labelId){
		var isAttention = $("#isAttention1_"+labelId).val();
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
						$("#isAttention1_"+labelId).val('false');
						$("#isAttention_"+labelId).attr("isAttention","false");
						$("#textAttention").html("添加收藏");
						$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
						$("#attentionImg_"+labelId).attr("title",'点击添加标签收藏');
						$("#isAttentionShow_"+labelId).attr("style",'display: none');
					}else{
						commonUtil.create_alert_dialog("failedDialog", {
							"txt":result.message,
							"type":"failed",
							"width":500,
							"height":200
						});
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
						//window.parent.showAlert(result.message,"success");
						$("#isAttention1_"+labelId).val('true');
						$("#isAttention_"+labelId).attr("isAttention","true");
						$("#textAttention").html("取消收藏");
						$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
						$("#attentionImg_"+labelId).attr("title",'点击取消标签收藏');
						$("#isAttentionShow_"+labelId).attr("style",'display: block');
					}else{
						commonUtil.create_alert_dialog("failedDialog", {
							"txt":result.message,
							"type":"failed",
							"width":500,
							"height":200
						});
					}
				}
			});
		}
	}
	//标签详情
	function showTip(e,ths){
		var index = $(".label_map_show").index($(ths));
		$(".label_map_show").each(function(i ,item){
			if(i!=index ){
				$(item).removeClass("onActive");
			}
		})
		if($(ths).hasClass("onActive")){
			indexClick=true;
			$(ths).removeClass("onActive");
		}else{
			$(ths).addClass("onActive");
			var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			indexClick=false;

		}
		setTimeout(function(){cc(e,ths)},500);
	}
	//展示标签详情
	function cc(e,ths){
		if(indexClick!=false){
			return ;
		}else{
			$("#tag_tips .onshow").hide().removeClass("onshow");
			var _p=$(ths);
			_p.find("a").blur();
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
			var iconx=_p.offset().left-posx+_p.width()/2;
			var tipCT=$("#tag_tips");
			tipCT.css({"top":posy,"left":posx});
			var updateCycle = "";
			if($(ths).attr("updateCycle") == 1){
				updateCycle = "日周期";
			}else if($(ths).attr("updateCycle") == 2){
				updateCycle = "月周期";
			}
			//cusNumFlag查看详情中用户数避免多次查询，0:第一次去后台查询 1：直接用第一次查询的结果
			if($(ths).attr("cusNumFlag") == "0"){
				var url = $.ctx + '/ci/ciIndexAction!getLabelInfoCustomNum.ai2do';
				$.ajax({
					url: url,
					type: "POST",
					async: false,
					data:'&labelId=' + $(ths).attr("element") + '&customNum=' + $(ths).attr("customNum") ,
					success: function(result){
						if(result.success){
							privCustomNum =  result.retCustomNum;
							$(ths).attr("cusNumFlag","1");
							$(ths).attr("customNum",result.retCustomNum);
							if(result.busiCaliber){
								$(ths).attr("busiCaliber",result.busiCaliber);
							}else{
								$(ths).attr("busiCaliber","");
							}
							if(result.applySuggest){
								$(ths).attr("applySuggest",result.applySuggest);
							}else{
								$(ths).attr("applySuggest","");
							}
							if(result.newDataDate){
								$(ths).attr("newDataDate",result.newDataDate);
							}else{
								$(ths).attr("newDataDate","");
							}
							if(result.isAttention){
								$(ths).attr("isAttention",result.isAttention);
							}else{
								$(ths).attr("isAttention","false");
							}
						}else{
							commonUtil.create_alert_dialog("failedDialog", {
								"txt":"获取首页标签地图showTip展示详细信息时出错",
								"type":"failed",
								"width":500,
								"height":200
							});
						}
					}
				});
			}
			var privCustomNum = $(ths).attr("customNum");
			var typeId = ${LABEL_TYPE_SIGN};
			var temp = "";
			temp +=  '<li class="temp" onclick="attentionOper('+ $(ths).attr("element") +')">';
			temp +='<input type="hidden" id="isAttention1_'+$(ths).attr("element")+'" value="'+$(ths).attr("isAttention")+'" />';
			if($(ths).attr("isAttention") == 'false'){
				temp +=	'<span  class="collectStar" isAttention="'+$(ths).attr("isAttention")+'" labelId="'+ $(ths).attr("element") +'"><img style="cursor:pointer" title="点击添加标签收藏"  id="attentionImg_'+ $(ths).attr("element") +'" src="${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png" /><a href="javascript:void(0);" id="textAttention">添加收藏</a></span>';
			}
			if($(ths).attr("isAttention") == 'true'){
				temp +='<span  class="collectStar" isAttention="'+$(ths).attr("isAttention")+'" labelId="'+ $(ths).attr("element") +'"><img style="cursor:pointer" title="点击取消标签收藏"  id="attentionImg_'+ $(ths).attr("element") +'" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png" /><a href="javascript:void(0);" id="textAttention">取消收藏</a></span>';
			}
			temp +=  '</li>';
			var html = '<div class="tips">' +
					'<div class="icon"></div>' +
					'<div class="ct">' +
					'<table class="commonTable">' +
					'<tr>' +
					'<th>标签名称：</th>' +
					'<td><font color="#0d69b8">' + $(ths).attr("labelName") + '</font></td>';
			if($(ths).attr("type") == typeId){
				html +=
						'<th>用户数：</th>' +
						'<td>' + $.formatNumber(privCustomNum, 3, ',') + '</td>';
			}else{
				html +=
						'<th></th>' +
						'<td></td>';
			}
			html+= '</tr>' +
					'<tr>' +
					'<th>业务口径：</th>' +
					'<td colspan=3>' + $(ths).attr("busiCaliber") + '</td>' +
					'</tr>' +
					'<tr>' +
					'<th>更新周期：</th>' +
					'<td colspan=3>' + updateCycle +'</td>' +
					'</tr>' +
					'<tr>' +
					'<th>标签分类：</th>' +
					'<td colspan=3>' + $(ths).attr("labelSceneNames") +'</td>' +
					'</tr>' +
					'<tr>' +
					'<th>应用建议：</th>' +
					'<td colspan=3>' + $(ths).attr("applySuggest") +'</td>' +
					'</tr>' +
					'<tr>' +
					'<th>数据日期：</th>' +
					'<td colspan=3>' + $(ths).attr("dataDateStr") +'</td>' +
					'</tr>' +
					'</table>' +
					'<div class="info_panel">' +
					'<ul>' +temp +

					'<li class="icon07" element="' + $(ths).attr("element") +
					'" labelName="'+ $(ths).attr("labelName") +
					'" min="' + $(ths).attr("min") +
					'" max="' + $(ths).attr("max") +
					'" elementType="' + $(ths).attr("elementType") +
					'" effectDate="' + $(ths).attr("effectDate") +
					'" updateCycle="' + $(ths).attr("updateCycle") +
					'" attrVal="' + $(ths).attr("attrVal") +
					'" type="' + $(ths).attr("type") + '" onclick="addShopCart('+ $(ths).attr("element") +',1,0);"><a href="javascript:void(0)" style="left:0px;width:85px;display:inline-block;position:static;">添加到收纳篮</a></li>';
			html +=
					'<li style="display:none" class="icon_tag" title="标签分析" onclick="openLabelAnalysis('+ $(ths).attr("element") +');"><a href="javascript:void(0)">标签分析</a></li>' +
					'<li style="display:none" class="icon_wtag" title="标签微分" onclick="openLabelDifferential('+ $(ths).attr("element") +');"><a href="javascript:void(0)">标签微分</a></li>';
			if($(ths).attr("updateCycle") == 2) {
				html +=
						'<li style="display:none" class="icon04" title="关联分析" onclick="openLabelRel('+ $(ths).attr("element") +');"><a href="javascript:void(0)">关联分析</a></li>' +
						'<li style="display:none" class="icon05" title="对比分析" onclick="openLabelContrast('+ $(ths).attr("element") +');"><a href="javascript:void(0)">对比分析</a></li>'
			}
			+'</ul>' +
			'</div>' +
			'</div>' +
			'</div>';
			var _tip=$(html);
			$("#tag_tips").show();
			$("#tag_tips .group").empty().append(_tip).css("margin-bottom","45px");
			_tip.find(".icon").css("margin-left",iconx+"px");
			_tip.addClass("onshow").slideDown("fast");
			_tip.click(function(e){
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			});
			//收藏小盒子样式的改变
			$(".collectStar").hover(function(){
				var labelId = $(this).attr("labelId");
				var isAttention = $("#isAttention1_"+labelId).val();
				if(isAttention == 'true'){//已关注的标签
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
				}else{//未关注的标签
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
				}
			},function(){
				var labelId = $(this).attr("labelId");
				var isAttention = $("#isAttention1_"+labelId).val();
				if(isAttention == 'true'){//已关注的标签
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
				}else{//未关注的标签
					$("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
				}
			});
			//增加标签分析等按钮的鼠标移入和移出特效效果(由于这几个按钮是在调用showTip方法时动态加入的，所以需要在这个地方给这几个按钮加事件)
			//$(".info_panel ul li").unbind("mouseenter").unbind("mouseleave");
			//$(".info_panel ul li").on("mouseenter",function(){
			//	var _a=$(this).find("a");
			//	_a.css({"position":"absolute","left":"-3000px"});
			//	var _w=_a.width();
			//	_a.css({"position":"static","left":"0"});
			//	_a.width(0).show().animate({width:_w + 2},250);
			//}).on("mouseleave",function(){
			//	var _a=$(this).find("a");
			//	_a.width(_a.width() - 2).hide();
			//})
		}
	}

	function firsttimelabel_tip_remove(){
		//$(ts).remove();
		$("#firsttimelabel_tip").remove();
	}

</script>
<style type="text/css">
	<%--设置标签微分等超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
<%--设置营销导航传过来的场景ID--%>
<input type="hidden" id="moreMarketSceneId"/>
<input type="hidden" id="moreMarketSceneName"/>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>

	<!-- <div id="tag_curtain_CT" class="tag_curtain_CT">
    </div> -->
	<!--tag_curtain_CT end -->

	<!-- 分类开始 -->
	<div class="navBox comWidth" id = "navBox">
		<div class="fleft navListBox">
			<ol>
				<li id="marketLink"><a href="javascript:void(0);" onclick="forwardMarket();">营销导航</a></li>
				<li class="viewNavLi" id="labelMarketLink"><a href="javascript:void(0);" class="hover" onclick="forwardLabelMap();">标签集市</a></li>
				<li id="top100Link"><a href="javascript:void(0);" onclick="forwardTop100();">TOP100</a></li>
				<li id="customerInfoLink"><a href="javascript:void(0);" onclick="forwardGustomGroupMarket();">客户群集市</a></li>
				<% if ("true".equalsIgnoreCase(templateMenu)) { %>
				<li id="templateInfoLink"><a href='javascript:void(0);' onclick="forwardTemplate();" >客户群模板</a></li>
				<% } %>
				<li id="myCustomGroupLink"><a href="javascript:void(0);" onclick="forwardMyCustomGroup();">我的客户群</a></li>
			</ol>
		</div>
	</div>
	<!--allsort end-->

	<!-- <div class="searcbox"></div>
	
	<div class="carbox"></div> -->

</div>
<!-- 分类结束 -->

<!-- tag_curtain end-->

<div id="aibi_area_data">
</div>
<div id="loading" style="display:none;">正在加载，请稍候 ...</div>
<div id="noMoreResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
<div id="noResults" class="noResults" style="display:none;">没有标签信息...</div>
<!-- 点击添加购物车提醒 -->
<div id="firsttimelabel_tip" class="addShopCartGoodsTip hidden"><!-- hidden -->
	<img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/icon-add-cart-tip.png" usemap="#addShopMap"  width="256" height="108" />
</div>
<!-- 我知道了,热点 -->
<map name="addShopMap" id="addShopMap">
	<area shape="rect" coords="160,75, 255,105"  href ="javascript:firsttimelabel_tip_remove();" />
</map>
<!-- 点击导入提醒 -->
<div id="importClientBtnTip" class="importClientBtnTip hidden" onclick="importClientBtnTip_remove(this);"></div>
<div id="importClientBtnTipMy" class="importClientBtnTipMy hidden" >
	<a href="javascript:void(0);" onclick="importClientBtnTip_remove('#importClientBtnTipMy');" class="importClientBtnTipMyOk"></a>
</div>

<!--  标签地图中标签详细信息弹出层div模板-->
<div id="tag_tips" class="tag_tips">
	<div class="group">
	</div>
</div>
<input type="hidden" id="isEditCustom" value=""/>
<input type="hidden" id="isSimpleSearch" value=""/>
