$(function() {
	var marketTimer = null; 
	$('.market-main-scene').each(function(index, item) {
		if($(item).hasClass('active')) {
			$('#customLabelSceneId').val($(item).attr('sceneId'));
			$('#defaultOp').val($(this).attr('defaultOp'));
		}
	})
	
	//1.根据跳转过来的任务id找到父任务的图片更换左上角图片
//	var iconName = $('#parentMarketTaskIconInput').val();
//	if(iconName) {coreMain.changeTitleIcon(iconName);}
	//2.选中父菜单market-group-item-hover
	var parentMarketTaskId = $('#parentMarketIdIconInput').val();
	if(parentMarketTaskId) {coreMain.addMenuHover(parentMarketTaskId);}
	//3.更换左上角文字
	var childMarketTaskName = $('#childMarketTaskNameInput').val();
	if(childMarketTaskName) {coreMain.changeTitleText('营销视图');}
	
	//初始化列表
	MarketIndex.search();
	
	//搜索框输入框效果
	$('#search-customer').focusblur({
		'focusColor'	: '#222232',
		'blurColor' 	: '#8e8e8e'
	});
	
	//搜索查询
	$('#queryBtn').COClive('click', function() {
		MarketIndex.searchByName();
	}).bind('keypress', function(event){
        if(event.keyCode == '13') {
    		MarketIndex.searchByName();
        }
    });
	
	//搜索框阻止父节点点击查询事件
	$('#search-customer').click(function(e) {
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
	});
	
	//客户群标签详细信息
	$('.J_market_addCart').COClive('click',function(event){
		marketTimer && clearTimeout(marketTimer); 
		var J_com_show_arrow = $(this);
		var rule = $(this).parent().parent().next();
		marketTimer = setTimeout(function(){ 
	    		//if(J_com_show_arrow.hasClass('market-info-right-icon-active')){
	    			//J_com_show_arrow.removeClass('market-info-right-icon-active').addClass('market-info-right-icon');
	    		//}else{
	    		//	J_com_show_arrow.removeClass('market-info-right-icon').addClass('market-info-right-icon-active');
	    		//}
	    		if(rule.hasClass('hidden')){
	    			rule.removeClass('hidden');
	    		}else{
	    			rule.addClass('hidden');
	    			return;
	    		}
	    		
	    		var labelOrCustomId;
	    		var labelId = J_com_show_arrow.attr('labelId');
	    		var customGroupId = J_com_show_arrow.attr('customGroupId');
	    		var type = J_com_show_arrow.attr('showType');
	    		
	    		if(type == 1) {
	    			labelOrCustomId = labelId;
	    		} else if(type == 2) {
	    			labelOrCustomId = customGroupId;
	    		}
	    		
	    		if((type != 1 && type != 2) || !labelOrCustomId) {
	    			J_com_show_arrow.removeClass('market-info-right-icon').addClass('market-info-right-icon-active');
	    		}
	    		MarketIndex.queryCustomLabelDetail(type, rule, labelOrCustomId);
	    },300); 
		
	}).COClive("dblclick",function(){
		marketTimer && clearTimeout(marketTimer); 
		$(this).addCartAnimate({
			isClick:true,
			hideFuc:function(ths){
				return MarketIndex.addShopCart(ths);
			}
		});
		
	});
	
	//场景切换
	$('.market-main-scene').COClive('click', function() {
		$('.market-main-scene').removeClass('active');
		$(this).addClass('active');
		$('#customLabelSceneId').val($(this).attr('sceneId'));
		$('#defaultOp').val($(this).attr('defaultOp'));
		MarketIndex.search();
	});
	
	//操作下拉列表
	MarketIndex.marketListOperate(".J_market_list_operation_btn");
	
	//双击加入购物车
//	$(".J_market_addCart").addCartAnimate({
//		marketTimer:marketTimer,
//		hideFuc:function(ths){
//			return MarketIndex.addShopCart(ths);
//		}
//	});
	//操作按钮加入到购物车
	$('.J_market_list_add_cart').COClive('click', function() {
		MarketIndex.addShopCart(this);
	});
	//取消推荐
	MarketIndex.cancelRecommend(".J_market_list_cancel_recommend");
	
	//面包屑一级任务可以点击查询
	$('.J_parent_marketTask_path').COClive('click', function() {
		var marketTaskId = $(this).attr('taskId');
		$('#marketTaskIdFromIndexInput').val(marketTaskId);
		coreMain.linkToMarketIndex();
	});
	
	
	//控制添加购物车提示div的位置
	$(window).resize(function(){    
		MarketIndex.locationAddShopCartTip();
	});
	
	//关闭提示双击加入到购物车
	$('#closeAddShopCartTip').COClive('click', function() {
		$.cookie('addShopCartTip', 1, {expires : 7});
		$(this).parent().hide();
	});
})



























