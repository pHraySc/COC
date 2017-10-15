$(function() {

	//标签双击加入购物车
	$('.J_label_market_addCart').COClive('dblclick', function() {
		$(this).addCartAnimate({
			isClick:true,
			hideFuc:function(ths){
				return LastPublishRankLabel.addLabelShopCart(ths);
			}
		});
	});
})