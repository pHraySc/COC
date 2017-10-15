$(function() {
	$("#search-customer").focusblur({
		"focusColor" : "#222232",
		"blurColor" : "#757575",
		"searchVal":"查询"
	});
	if($.isNotBlank($("#customGroupName").val())){
		$("#search-customer").val($("#customGroupName").val());
		$("#search-customer").attr("data-val","true");
		$("#search-customer").css("color","#232323")
	}
	//范围筛选按钮的显示 隐藏
	CustomMarketMain.filterButtonEvent();
	
	//初始化过滤条件  收起、更多样式
	CustomMarketMain.moreClick("#moreFilter");
	
	//点击客户群规模筛选事件
	CustomMarketMain.customNumFilter(".J_num_filter_button");
	
	//点击客户群规模筛选事件
	CustomMarketMain.customCreateTimeFilter(".J_date_filter_button");
	
	//初始化点击过滤条件事件
	CustomMarketMain.addFilterClick(".J_market_custom_filter_item");
	
	if($.isNotBlank($("#seachTypeId_").val()) == false){
		$("#city1_"+$("#userCityId").val()).click();
	}else{
		//加载客户群分页列表
		CustomMarketMain.loadCustomList();
	}
	
	//初始化查询点击事件
	CustomMarketMain.searchButtonClick(".J_search_custom_market");
	
	//客户群详细信息展示
	CustomMarketMain.showMarketCustomDetail(".J_customer_market_hiddenShow");
	
	//客户群操作列表的显示
	CustomMarketMain.marketCustomOperate(".J_market_list_operation_btn");
	
	//客户群清单下载
	coreMain.customListDown(".J_market_custom_list_down");
	
	//客户群不可进行清单下载
	coreMain.notShowCustomNum(".J_no_show_custom_num");
	
	//客户群推荐设置
	coreMain.setRecomLabelOrCustom(".J_market_custom_set_recom",2);
	
	//客户群共享确认
	coreMain.confirmShare({"target":".J_share_operate","backFunc":CustomMarketMain.loadCustomList});
	
	//客户群取消共享确认
	coreMain.confirmCancelShare({"target":".J_cancel_share_operate","backFunc":CustomMarketMain.loadCustomList});
	
	//客户群统计失败重新生成
	coreMain.statistFailRegenCustomer({"target":".J_fail_regen_cusotmer_market","backFunc":CustomMarketMain.loadCustomList});
	
	//客户群取消收藏
	coreMain.cancelAttentionCustom();
	
	//客户群添加收藏
	coreMain.addAttentionCustom();
	
	//操作按钮加入到购物车
	$('.J_customer_market_addCart').COClive('click', function() {
		CustomMarketMain.addCustomShopCart(this);
	});
	
	//双击加入购物车
//	$(".J_customer_market_addCart_db").addCartAnimate({
//		hideFuc:function(ths){
//			CustomMarketMain.addCustomShopCart(ths);
//		}
//	});
	
	//双击加入购物车失败提示
	CustomMarketMain.addCustomShopCartFail(".J_customer_market_addCart_fail");
	
	//客户群重新生成
	coreMain.oneTimeRegenCustomer({"target":".J_regen_customer_market","backFunc":CustomMarketMain.loadCustomList});
	
	//客户群清单预览
	coreMain.queryGroupList(".J_market_custom_list_show");
	
	//客户群推送设置
	coreMain.pushCustomerGroupSingle(".J_custom_market_push_set");
	
	//客户群集市关键字回车查询
	CustomMarketMain.searchCustomMarketEnter(".J_customer_market_enter");
	
});