$(function() {

	//初始化列表
	SysHotCustom.init();
	
	//时间范围切换
	$('.sys-hot-custom-datescope').COClive('click', function() {
		$('.sys-hot-custom-datescope').removeClass('active');
		$(this).addClass('active');
		//$('#customLabelSceneId').val($(this).attr('datescope'));
		SysHotCustom.show($(this));
	});
	
	//客户群双击加入购物车
	$('.J_custom_market_addCart').COClive('dblclick', function() {
		$(this).addCartAnimate({
			isClick:true,
			hideFuc:function(ths){
				return SysHotCustom.addCustomShopCart(ths);
			}
		});
	});
})