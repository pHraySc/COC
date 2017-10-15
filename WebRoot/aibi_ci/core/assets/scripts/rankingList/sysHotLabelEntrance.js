$(function() {

	//初始化列表
	SysHotLabel.init();
	
	//时间范围切换
	$('.sys-hot-label-datescope').COClive('click', function() {
		$('.sys-hot-label-datescope').removeClass('active');
		$(this).addClass('active');
		//$('#customLabelSceneId').val($(this).attr('datescope'));
		SysHotLabel.show($(this));
	});
	
	//标签双击加入购物车
	$('.J_label_market_addCart').COClive('dblclick', function() {
		$(this).addCartAnimate({
			isClick:true,
			hideFuc:function(ths){
				return SysHotLabel.addLabelShopCart(ths);
			}
		});
	});
})