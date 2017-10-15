$(function() {
	//客户群关键字样式
	$("#keyWordGroupName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	
	//加载客户群分页列表
	customManagerMain.loadCustomList();
	
	//查询客户群
	customManagerMain.clickLoadCustomList("#searchBtn");
	
	//客户群导入
	customManagerMain.importCustom("#importCustomBtn");
	
	//模拟下拉框
	customManagerMain.clickListStyle(".selBtn ,.selTxt");
	
	//得到点击下拉框的值
	customManagerMain.getClickListValue(".querySelListBox a");
	
	//客户群列表内存显示隐藏
	customManagerMain.customListHidenOrShow(".J_manager_addCart");
	
	//客户群操作列表展示
	customManagerMain.marketCustomOperate(".J_market_list_operation_btn");
	
	//客户群修改
	customManagerMain.editCustomerOpenCalculate(".J_customer_datils .J_edit_group_info");
	
	//客户群修改
	customManagerMain.editCustomer(".J_customer_datils .J_edit_custom_info");
	
	//客户群删除
	customManagerMain.delOne(".J_customer_datils .J_del_group_info");
	
	//客户群详情
	customManagerMain.queryGroupInfo(".J_customer_datils .J_customer_manager_detail");
	
	//客户群规则详情
	customManagerMain.showRule(".J_showRule");
	
	//客户群重新生成
	coreMain.oneTimeRegenCustomer({"target":".J_customer_datils .J_regen_customer","backFunc":customManagerMain.loadCustomList});
	
	//客户群清单预览
	coreMain.queryGroupList(".J_customer_datils .J_query_group_list");
	
	//客户群推送设置
	coreMain.pushCustomerGroupSingle(".J_customer_datils .J_push_group_info");
	
	//客户群清单下载
	coreMain.customListDown(".J_market_custom_list_down");
	
	//客户群统计失败重新生成
	coreMain.statistFailRegenCustomer({"target":".J_fail_regen_cusotmer","backFunc":customManagerMain.loadCustomList});
	
	//客户群共享确认
	coreMain.confirmShare({"target":".J_share_operate","backFunc":customManagerMain.loadCustomList});
	//客户群取消共享确认
	coreMain.confirmCancelShare({"target":".J_cancel_share_operate","backFunc":customManagerMain.loadCustomList});
	
	//客户群推荐设置
	coreMain.setRecomLabelOrCustom(".J_market_custom_set_recom",2);
	
	//操作按钮加入到购物车
	$('.J_customer_manager_addCart').COClive('click', function() {
		customManagerMain.addCustomShopCart(this);
	});
	
	//双击加入购物车
	//$(".J_customer_manager_addCart_db").addCartAnimate({
	//	hideFuc:function(ths){
	//		customManagerMain.addCustomShopCart(ths);
	//	}
	//});
	
	//双击加入购物车失败提示
	customManagerMain.addCustomShopCartFail(".J_customer_manager_addCart_fail");
	
	//关键字回车查询
	customManagerMain.searchCustomMarketEnter("#keyWordGroupName");
})