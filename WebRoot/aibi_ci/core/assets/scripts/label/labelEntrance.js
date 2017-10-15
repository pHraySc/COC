$(function() {
	$("#labelSearchName").focusblur({
		"focusColor" : "#222232",
		"blurColor" : "#8e8e8e",
		"searchVal":"查询"
	});
	if($.isNotBlank($("#labelName").val())){
		$("#labelSearchName").val($("#labelName").val());
		$("#labelSearchName").attr("data-val","true");
		$("#labelSearchName").css("color","#232323")
	}
	
	var para = $("#queryLabelForm").serializeArray();
	//console.log(para);
	LabelMain.labelInfoList(para);
	
	//点击标签分类路径加载的方法
	$(".J_labelCategories").COClive("click",function(){
		var categoriesId = $(this).attr("_categoriesId");
		$("#categoriesId").val(categoriesId);
		var labelName = $("#labelSearchName").val();
		if (labelName == "查询") {
			labelName = "";
	    }
	    $("#labelName").val(labelName);
		var para = $("#queryLabelForm").serializeArray();
		//删除后面的路径
		$(this).nextAll(".J_labelCategoriesArrow").remove();
		$(this).nextAll(".J_labelCategories").remove();
		LabelMain.labelInfoList(para);
	});
	
	//模糊查询
	$("#searchLabelIcon").COClive("click",function(){
		var labelName = "";
		if($("#labelSearchName").attr("data-val") == "true"){
			labelName = $("#labelSearchName").val();
		}
	    $("#labelName").val(labelName);
		var para = $("#queryLabelForm").serializeArray();
		LabelMain.labelInfoList(para);
	});
	$('#labelSearchName').bind('keypress', function(event){
        if(event.keyCode == '13') {
        	var labelName = "";
    		if($("#labelSearchName").attr("data-val") == "true"){
    			labelName = $("#labelSearchName").val();
    		}
    	    $("#labelName").val(labelName);
    		var para = $("#queryLabelForm").serializeArray();
    		LabelMain.labelInfoList(para);
        }
    });
	/** 多条件查询--begin **/
	//初始化点击过滤条件事件
	LabelMain.addLabelFilterClick(".J_label_market_filter_item");
	//范围筛选按钮的显示 隐藏
	LabelMain.labelFilterButtonEvent();
	//点击范围筛选按钮
	LabelMain.labelCreateTimeFilter(".J_label_date_button");
	/** 多条件查询--end **/
	//标签集市下拉详情展示
	LabelMain.labelMarketDetail(".J_label_market_addCart");
	//标签集市操作列表的显示
	LabelMain.labelMarketOperate(".J_label_market_operation_btn");
	//推荐操作
	coreMain.setRecomLabelOrCustom(".J_label_market_set_recom",1);
	//操作按钮加入到购物车
	$('.J_label_market_list_add_cart').COClive('click', function() {
		LabelMain.addLabelShopCart(this);
	});
	//双击加入购物车
//	$(".J_label_market_addCart").addCartAnimate({
//		hideFuc:function(ths){
//			LabelMain.addLabelShopCart(ths);
//		}
//	});
	//收藏图标点击取消收藏
	coreMain.cancelAttentionLabel();
	//收藏图标点击添加收藏
	coreMain.addAttentionLabel();
});