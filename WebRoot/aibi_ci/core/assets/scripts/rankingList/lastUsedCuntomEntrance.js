$(function() {

	//客户群双击加入购物车
	$('.J_custom_market_addCart').COClive('dblclick', function() {
		$(this).addCartAnimate({
			isClick:true,
			hideFuc:function(ths){
				return LastUsedCuntom.addCustomShopCart(ths);
			}
		});
	});
})