$(function() {
	$(document).click(function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
		$(".J_cart_operate").hide().css("margin","0");;
		$("#cia-bubble-dialog").remove(); 
		$("#marketMenus").remove();
		//$("#marketMapMenus").remove();
		$(".market-cart-of-ul").height("auto");
		$(".resultItemActiveList").slideUp("fast",function(){
			$(".market-info-list-btn-active").removeClass("market-info-list-btn-active");
			
		});//营销导航操作下拉隐藏
		$("#marketMainList,.main-content").removeAttr("style");
		index = 0;
	});
	
	coreMain.setleftMenu();
	if($("#marketMainList").length > 0){
		var left = $("#marketMainList").offset().left+80;
		var top = -10;
		$(".J_add-cart-tip").css({left:left,top:top}).show().removeClass("hidden");
	}
	$(window).resize(function(){
		coreMain.setleftMenu();
		if($("#marketMainList").length > 0){
			var left = $("#marketMainList").offset().left+80;
			var top = -10;
			$(".J_add-cart-tip").css({left:left,top:top}).show().removeClass("hidden");
		}
	});
	$(".show-menu-icon").hover(function(){
		$(".J_left_menus").addClass("show-menu-style").show();
		$(".J_market_content > div").css({margin:"0 0 0 20px"});
		$(this).hide();
	},function(){
		$(".J_left_menus,#cia-bubble-dialog").COClive("mouseover",function(){
			if( $(window).width() < 1250){
				$(".J_left_menus").addClass("show-menu-style").show();
				$(".J_market_content > div").css({margin:"0 0 0 20px"});
			}
		});
		$(".J_left_menus ,#cia-bubble-dialog").COClive("mouseleave",function(){
			if( $(window).width() < 1250){
				 var leftObj = $(".J_left_menus").removeClass("show-menu-style").hide();
				 var left = leftObj.offset().left;
				 $(".show-menu-icon").css({left:left}).show();
				 $(".J_market_content > div").css({margin:"0 0 0 20px"});
			}
		});
	});
	
	$(".J_market_Menus").live("click",function(){ 
		$(".J_market_group").addClass("hidden");
		$(".J_market_group").prev().find(".market-group-icon-hover").addClass("market-group-icon").removeClass("market-group-icon-hover"); 
		var list = $(this).next();
		list.removeClass("hidden");
		$(this).find(".market-group-icon").addClass("market-group-icon-hover").removeClass("market-group-icon");
//		var index = $(".J_market_Menus").index($(this));
//	    $("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon"+index);
	});
	
	var indexLinkType = $('#indexLinkTypeInput').val();
	
	if($.isNotBlank(indexLinkType)) {
		$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon0");
		var url = '';
		var title = '';
		if(indexLinkType == 1) {//我的客户群
			url = $.ctx + "/core/ciCustomerManagerAction!init.ai2do";
			title = '我的视图';
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#myCustomGroup").addClass("market-group-item-hover");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon2");
		} else if(indexLinkType == 2) {//客户群集市
			url = $.ctx + "/core/ciCustomerMarketAction!customerMarketIndex.ai2do";
			title = '全景视图';
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#customMarketLink").addClass("market-group-item-hover");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon1");
		} else if(indexLinkType == 3) {//标签集市
			url = $.ctx + "/core/labelMarketAction!findEffectiveLabelIndex.ai2do";
			title = '全景视图';
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#labelInfoIndex").addClass("market-group-item-hover");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon1");
		} else if(indexLinkType == 4){//我的收藏
			url = $.ctx + "/core/ciUserAttentionAction!userAttentionIndex.ai2do";
			title = '我的视图';
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#userAttention").addClass("market-group-item-hover");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon2");
		} else if(indexLinkType == 5){//排行榜
			url = $.ctx + "/core/ciRankingListAction!findSysHotRankLabelIndex.ai2do";
			title = '热门推荐';
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#sysHotLabelRankingListLink").addClass("market-group-item-hover");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon3");
		} else {
			coreMain.linkToMarketIndex();
			return;
		}
		coreMain.linkToOtherPage(url);
		$('#marketTaskTitle').html(title);
		//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
	} else {
		var seachTypeId = $("#seachTypeId_").val();
		var keyWord = $("#seachKeyWord_").val();
		if($.isNotBlank(seachTypeId)){
			coreMain.indexTopSearch(seachTypeId,keyWord);
		}else{
			coreMain.linkToMarketIndex();
		}
	}
	
	//=============================营销导航begin==================================
	//二级营销任务菜单链接
	coreMain.initMarketTaskMenu();
	//一级任务切换
	$('.J_market-task-menu').COClive('click', function() {
		var taskId = $(this).attr('taskId');
		$('#marketTaskIdFromIndexInput').val(taskId)
		coreMain.linkToMarketIndex();
		$('.market-group-item-hover').removeClass('market-group-item-hover');
		$(this).addClass('market-group-item-hover');
		$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon0");
		//轨迹跟踪 主页切换一级营销任务
		try{
			if(window.WebTrends){
				    Webtrends.multiTrack('WT.busi','YXDH','WT.oper','MKT_TASK_LEVEL1_HOME','WT.obj',taskId);
			}
		}catch(e){
		    alert(e);
		}
	});
	//绑定系统建设中
	$(".J_buiding").COClive("click",function(e){ 
		coreMain.systemBuiding();
	});
	//=============================营销导航end====================================
	
	
	//=============================标签集市begin==================================
	
	coreMain.changeMarketPageLink("#labelInfoIndex",{
		url			: $.ctx + "/core/labelMarketAction!findEffectiveLabelIndex.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#labelInfoIndex").addClass("market-group-item-hover");
			
			//轨迹跟踪  标签集市
			try{
				if(window.WebTrends){
				     Webtrends.multiTrack('WT.busi','QJBQ','WT.oper','ALL_LABEL_HOME','WT.obj','QJBQ');
				}
			}catch(e){
			    alert(e);
			}
		}
	});
	coreMain.loadLabelMenus();
	$(".firstLabelCategories").COClive("click",function(){
		var categoriesId = $(this).attr("label_value");
		var labelName = $(this).attr("title");
		var para = {
				"pojo.categoriesId":categoriesId
				}
		coreMain.labelMenuClick(para);
		$(this).addClass("market-group-item-hover");
	});
	//=============================标签集市end====================================

	
	//=============================客户群集市begin==================================
	coreMain.changeMarketPageLink("#customMarketLink",{
		url			: $.ctx + "/core/ciCustomerMarketAction!customerMarketIndex.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#seachTypeId_").val("");
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#customMarketLink").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("全景视图");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon1");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
			
			//轨迹跟踪  客户群集市 
			try{
				if(window.WebTrends){
				     Webtrends.multiTrack('WT.busi','QJKHQ','WT.oper','ALL_CUSTOM_HOME','WT.obj','QJKHQ');
				}
			}catch(e){
			    alert(e);
			}
		}
	});
	//=============================客户群集市end====================================
	
	
	//=============================我的客户群begin==================================
	coreMain.changeMarketPageLink("#myCustomGroup",{
		url			: $.ctx + "/core/ciCustomerManagerAction!init.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#myCustomGroup").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("我的视图");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon2");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
			
			//轨迹跟踪  我的客户群 
			try{
				if(window.WebTrends){
				     Webtrends.multiTrack('WT.busi','WDKHQ','WT.oper','MY_CUSTOM_HOME','WT.obj','WDKHQ');
				}
			}catch(e){
			    alert(e);
			}
		}
	});
	//=============================我的客户群end====================================

	
	//=============================我的收藏begin==================================
	coreMain.changeMarketPageLink("#userAttention",{
		url			: $.ctx + "/core/ciUserAttentionAction!userAttentionIndex.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#userAttention").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("我的视图");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon2");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
		}
	});
	
	//=============================我的收藏end====================================

	
	//=============================排行榜begin==================================
	
	coreMain.changeMarketPageLink("#sysHotLabelRankingListLink",{
		url			: $.ctx + "/core/ciRankingListAction!findSysHotRankLabelIndex.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#sysHotLabelRankingListLink").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("热门推荐");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon3");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
		}
	});
	
	coreMain.changeMarketPageLink("#sysHotCustomRankingListLink",{
		url			: $.ctx + "/core/ciRankingListAction!sysHotCustomRankingListIndex.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#sysHotCustomRankingListLink").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("热门推荐");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon3");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
		}
	});
	
	coreMain.changeMarketPageLink("#lastPublishRankLabelLink",{
		url			: $.ctx + "/core/ciRankingListAction!findlastPublishRankLabel.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#lastPublishRankLabelLink").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("热门推荐");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon3");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
		}
	});
	
	coreMain.changeMarketPageLink("#lastUsedCuntomLink",{
		url			: $.ctx + "/core/ciRankingListAction!latestCustomerList.ai2do",
		ajaxType	: "POST",
		successFunc	: function(data){
			$("#marketMainList").html(data);
			$(".market-group-item-hover").removeClass("market-group-item-hover");
			$("#lastUsedCuntomLink").addClass("market-group-item-hover");
			$('#marketTaskTitle').html("热门推荐");
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon3");
			//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
		}
	});
	
	//=============================排行榜end====================================
	
	
	//=============================购物车begin==================================
	
	//初始化购物车页面
	coreMain.refreshShopCart();
	
	$(".J_show_cart_details").COClive("click",function(e){ 
		var customer = $(this).parent().next(".J_show_customer_detail");
		if(customer.hasClass("hidden")){
			customer.removeClass("hidden");
			$(this).addClass("market-cart-tag-name-active");
		}else{
			customer.addClass("hidden");
			$(this).removeClass("market-cart-tag-name-active");
		}
	});
	var index = 0;
	$(".J_cart_show_op").COClive("click",function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
		$(".J_cart_operate").hide();
		var secLast = $(this).parent().next().next();
		var last = $(this).parent().next(); 
		if(last.length == 0 || secLast.length == 0){ 
			var height = $(".market-cart-of-ul").height();
			if(index == 0 && height > 432){
				$(".market-cart-of-ul").height(height + 90); 
			} 
			index += 1;
		}else{
			$(".market-cart-of-ul").height("auto");
		} 
		$(this).next().show();
	});
	//运算符选择操作
	$('.J_shopCart_operations').COClive('click', function(e) {
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
		$(this).parent().hide();
		var text = $(this).children('.J_shopcart_operations_text').text();
		$(this).parent().parent().children('.J_cart_show_op').children('.market-cart-operate').html(text);
		
		var opt = 'and';
		if(text == "且") {
			opt = "and";
		}else if(text == "或") {
			opt = "or";
		}else if(text == "剔除") {
			opt = "-";
		}
		//同步session
		var sort = $(this).parent().attr('sort');
		$.ajax({
			url		: $.ctx+'/core/ciMainAction!editShopCartOperator.ai2do',
			type	: 'post',
			data	: 'sort=' + sort + '&operator=' + opt,
			success	: function() {
				coreMain.refreshShopCart();
			}
		});
	});
	
	//标签、客户群提示点击其他地方时需要隐藏
	$(document).click(function(){
		$(".labelTip").hide();
		$(".customTip").hide();
	})
	
	//购物车设置规则
	$(".J_shopCart_ul .J_setting").COClive("click", function(e){
		var name = $(this).prev().text();
		var elementObjType = $(this).attr("labelTypeId");
		ShoppingCart.setElementObjAttr(this, elementObjType, name, 1);
	});
	
	//单个删除购物车内的元素
	$('.J_shopCart_delete_single').COClive('click', function(e) {
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
		var sort = $(this).attr('sort');
		coreMain.deleteShopCart(sort);
	})
	//清空购物车内容
	$('#clearShopCartBtn').COClive('click', function() {
		coreMain.clearShopCart();
	});
	//打开计算中心
	$('#openCalculateCenterBtn').COClive('click', function() {
		coreMain.openCalculateCenter();
	});
	//保存客户群，跳转到计算中心保存客户群
	$('#saveCustomGroupBtn').COClive('click', function() {
		if($(this).hasClass('market-cart-button-disable')) {return;}
		coreMain.forwardSaveCustomPage();
	});
	//规则预览
	$('#showShopCartRule').COClive('click', function() {
		var ruleLength = $('.J_shopCart_rule').length;
		if(ruleLength == 0) {return;}
		//var url = $.ctx + '/aibi_ci/core/pages/shopCart/shopCartRule.jsp';
		var url = $.ctx + "/core/ciMainAction!shopCartRule.ai2do";
		dialogUtil.create_dialog('queryGroupRuleDialog', {
			'title' 		:  '用户群的完整规则',
			'height'		: 'auto',
			'width' 		: 800,
			'frameSrc' 		: url,
			'frameHeight'	: 490,
			'position' 		: ['center','center'] 
		})
	});
	
	//客户群规模
	$('#customGroupExploreBtn').COClive('click', function() {
		coreMain.validateAndExplore();
	});
	
	//枚举型标签设置dialog
	$("#enumLabelSetting").dialog({ 
		width:710,
		autoOpen: false, 
		modal: true, 
		title:"条件设置", 
		dialogClass:"ui-dialogFixed",
		resizable:false, 
		draggable: true,
		close:function(event, ui){
			$("#enumLabelSettingFrame").attr("src", "");
		}
	});
	//文本型标签设置dialog
	$("#textLabelSetting").dialog({ 
		width:570,
		autoOpen: false, 
		modal: true, 
		title:"条件设置", 
		dialogClass:"ui-dialogFixed",
		resizable:false,
		draggable: true,
		close:function(event, ui){
			$("#textLabelSettingFrame").attr("src", "");
		}
	});
	//组合型标签设置dialog
	$("#vertLabelSetting").dialog({
		width:720,
		dialogClass:"ui-dialogFixed",
		autoOpen: false, 
		modal: true,
		title:"组合标签条件设置", 
		resizable:false, 
		draggable: true,
		close:function(event, ui){
			$("#vertLabelSettingFrame").attr("src", "");
		}
	});
	//=============================购物车end====================================
	
});