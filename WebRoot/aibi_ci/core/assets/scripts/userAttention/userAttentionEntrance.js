$(function() {

	//初始化列表
	UserAttention.init();
	
	//收藏类型切换
	$(".user-attention-type").COClive("click", function() {
		$(".user-attention-type").removeClass("active");
		$(this).addClass("active");
		UserAttention.show($(this));
	});
	
	//标签操作按钮加入到购物车
	$(".J_label_market_list_add_cart").COClive("click", function() {
		UserAttention.addLabelShopCart(this);
	});
	
	//标签双击加入购物车
//	$(".J_label_market_addCart").addCartAnimate({
//		hideFuc:function(ths){
//			UserAttention.addLabelShopCart(ths);
//		}
//	});
	
	
	//客户群操作按钮加入到购物车
	$(".J_custom_market_list_add_cart").COClive("click", function() {
		UserAttention.addCustomShopCart(this);
	});
	
	//客户群双击加入购物车
//	$(".J_custom_market_addCart").addCartAnimate({
//		hideFuc:function(ths){
//			UserAttention.addCustomShopCart(ths);
//		}
//	});
	
	//点击收藏图标取消收藏事件
	UserAttention.cancelAttentionLabel();
	
	//点击收藏图标取消收藏事件
	UserAttention.cancelAttentionCustomGroup();
})