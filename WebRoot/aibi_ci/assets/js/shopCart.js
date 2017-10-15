
/**
 * 加入购物车
 * @id 标签、客户群或者模板的ID
 * @typeId 类型：1、标签，2、客户群，3、模板
 * @isEditCustomFlag 是否是修改操作，1、修改操作，0、保存操作
 */
function addShopCart(id,typeId,isEditCustomFlag){
	
	if(typeId == 2) {
		var msg = $.ajax({
			url		: $.ctx+'/ci/ciIndexAction!findCusotmValidate.ai2do',
			data	: 'ciCustomGroupInfo.customGroupId=' + id,
			async	: false
		}).responseText;
		var msgObj = $.parseJSON(msg);
		var success = msgObj.success;
		if(!success) {
			//showAlert(msgObj.msg,"failed");
			$.fn.weakTip({"tipTxt":msgObj.msg });
			return;
		}
	}
	
	var actionUrl=$.ctx+"/ci/ciIndexAction!saveShopSession.ai2do";
	var para={
			"pojo.calculationsId":id,
			"pojo.typeId":typeId,
			"pojo.isEditCustomFlag":isEditCustomFlag
			};
	$.post(actionUrl, para, function(result){
		if (result.success){
			$("#shopNum").empty();
			$("#shopNum").append(result.numValue);
			$("#shopNumBasket").empty();
			$("#shopNumBasket").append(result.numValue);
			$("#shopTopNum").empty();
			$("#shopTopNum").append(result.numValue);
			shopCarList();//刷新购物车
			
			//添加到购物车 触发购物车内容展示出来
			$("#shopCartLetter").removeClass("shopCartLetterHover");
			var tar=$("#shopCartGoodsList");
			if(!tar.is(":visible")){
				/**及时同步 购物篮和已选商品数量  **/
				$("#shopBasket").hide(); //购物篮隐藏
				$("#shopCartEmptyTip").hide();
				$("#shopBasketSelected").show(); //已选购物篮显示
				$("#shopCartGoodsListBox").show();
				tar.css("visibility","visible").height("auto");
				var shopChartHeight = tar.height();
				tar.height(0).show();
				tar.animate({height:shopChartHeight},0,function(){tar.height("auto")})
			}
			
			$("#shopCartGoodsList").mCustomScrollbar("update");//购物车滚动条
		}else{
			showAlert(result.msg,"failed");
		}
	});
}
function addToShoppingCar(e,ths,typeId){
	$(ths).removeClass("onActive");
	$("#tag_tips .onshow").hide().removeClass("onshow");
	indexClick=true;
	e=e||window.event;
	var firsttimelabel_tip=$("#firsttimelabel_tip");
	if(firsttimelabel_tip){
		firsttimelabel_tip.remove();
	}
	var tar=$(ths);
	var targetCT=$("#shopCartInfo .shopCartIcon");
	var tarX=targetCT.offset().left;
	var tarY=targetCT.offset().top;
	var _clone=tar.clone();
	var cloneX=tar.offset().left;
	var cloneY=tar.offset().top;
	_clone.addClass("labelToCar").css({"width":tar.width()+"px","left":cloneX,"top":cloneY,"position":"absolute"})
	$("body").append(_clone);
	_clone.animate({left:tarX,top:tarY},350,function(){
		_clone.remove();
		var numCt=$("#shopCart .shopCartActive strong");
		var oldNum=parseInt(numCt.html());
		oldNum+=1;
		numCt.html(oldNum);
		var elementId=$(ths).attr("element");
		addShopCart(elementId,typeId,'0');
		//addToShoppingCar(tar);
	});
}

/**
 * 从购物车移出对象
 * @param calculationsSort排序
 * @return
 */
function deleteShopCart(calculationsSort){
	var actionUrl=$.ctx+"/ci/ciIndexAction!delShopSession.ai2do";
	var para={
			"pojo.calculationsSort":calculationsSort
			};
	$.post(actionUrl, para, function(result){
		if (result.success){
			$("#shopNum").empty();
			$("#shopNum").append(result.numValue);
			$("#shopNumBasket").empty();
			$("#shopNumBasket").append(result.numValue);
			$("#shopTopNum").empty();
			$("#shopTopNum").append(result.numValue);
			shopCarList();
			
			if(result.numValue == 0) {
				$("#shopCartGoodsListBox").hide();
				$("#shopCartEmptyTip").show();
				
				$("#shopBasket").show(); //购物篮隐藏
				$("#shopBasketSelected").hide(); //已选购物篮显示
			}
			
		}else{
			showAlert(result.msg,"failed");
		}
	});
}


