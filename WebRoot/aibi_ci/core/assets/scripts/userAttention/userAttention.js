/********
 *  2015-1-1
 *	热门标签
 */
UserAttention = function() {
	var marketTimer = null; 
	var UserAttention = {
		/**
		 * 所有查询列表入口
		 */
		show	: function(target) {
			if(target.attr("userAttentionType") == '1'){
				 var actionUrl = $.ctx + '/core/ciUserAttentionAction!findUserAttentionLabel.ai2do';
				 $.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
			        $.ajax({
			            url		: actionUrl,
			            type	: 'POST',
			            dataType: 'html',
			            success	:function(html){
			            	$('#userAttentionCustomList').html('');
			                $('#userAttentionLabelList').html('');
			                $('#userAttentionLabelList').html(html);
			                $("#userAttentionTotalSize").html($("#labelPageTotalSize").val());
			                $("#userAttrntionTypeText").html('标签');
			                //$("#userAttrntionTypeTitle").html('标签收藏');
			                
			                //标签操作列表的显示
			                UserAttention.userAttentionLabelOperate(".J_label_attention_operation_btn");
			            	
			            	//标签详细信息展示
			                UserAttention.labelMarketDetail(".J_label_market_addCart");
			            	
			                UserAttention.labelPageClick();
			            	
			            },
					   	complete: function() {
					   		$.unblockUI(); 
					   	},
					   	stop: function() {
					   		$.unblockUI(); 
					   	}
			        });
			}else if(target.attr("userAttentionType") == '2'){
				 var actionUrl = $.ctx + '/core/ciUserAttentionAction!findUserAttentionCustom.ai2do';
				 $.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
			        $.ajax({
			            url		: actionUrl,
			            type	: 'POST',
			            dataType: 'html',
			            success	:function(html){
			                $('#userAttentionCustomList').html('');
			                $('#userAttentionLabelList').html('');
			                $('#userAttentionCustomList').html(html);
			                $("#userAttentionTotalSize").html($("#customPageTotalSize").val());
			                $("#userAttrntionTypeText").html('用户群');
			                //$("#userAttrntionTypeTitle").html('用户群收藏');
			                //用户群操作列表的显示
			                UserAttention.userAttentionCustomOperate(".J_custom_attention_operation_btn");
			            	
			            	//用户群详细信息展示
			                UserAttention.showMarketCustomDetail(".J_custom_market_addCart_display");
			            	
			                UserAttention.customPageClick();
			            	
			            },
					   	complete: function() {
					   		$.unblockUI(); 
					   	},
					   	stop: function() {
					   		$.unblockUI(); 
					   	}
			        });
			}
	       
	    },
	    /**
	     * 显示列表
	     */
		init	: function() {
			 var actionUrl = $.ctx + '/core/ciUserAttentionAction!findUserAttentionLabel.ai2do';
			 $.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
		        $.ajax({
		            url		: actionUrl,
		            type	: 'POST',
		            dataType: 'html',
		            success	:function(html){
		                $('#userAttentionLabelList').html('');
		                $('#userAttentionLabelList').html(html);
		                $("#userAttentionTotalSize").html($("#labelPageTotalSize").val());
		                $("#userAttrntionTypeText").html('标签');
		                //$("#userAttrntionTypeTitle").html('标签收藏');
		                //标签操作列表的显示
		                UserAttention.userAttentionLabelOperate(".J_label_attention_operation_btn");
		            	
		            	//标签详细信息展示
		                UserAttention.labelMarketDetail(".J_label_market_addCart");
		            	
		                UserAttention.labelPageClick();
		            	
		            },
				   	complete: function() {
				   		$.unblockUI(); 
				   	},
				   	stop: function() {
				   		$.unblockUI(); 
				   	}
		        });
	    },
	    /**
		 * 分页触发事件
		 */
		labelPageClick :	function(){
			var actionUrl = $.ctx + "/core/ciUserAttentionAction!findUserAttentionLabel.ai2do?"+$("#pageForm").serialize();
			$("#userAttentionLabelList").page( {url:actionUrl,objId:'userAttentionLabelList',callback:UserAttention.labelPageClick });
			//收藏图标鼠标悬浮事件
        	coreMain.attentionBoxHover();
		},
		/**
		 * 分页触发事件
		 */
		customPageClick :	function(){
			var actionUrl = $.ctx + "/core/ciUserAttentionAction!findUserAttentionCustom.ai2do?"+$("#pageForm").serialize();
			$("#userAttentionCustomList").page( {url:actionUrl,objId:'userAttentionCustomList',callback:UserAttention.customPageClick });
			//收藏图标鼠标悬浮事件
        	coreMain.attentionBoxHover();
		},
	    /**
		 * 展示用户群详情
		 */
		showMarketCustomDetail : function(target){
			$(target).COClive("click",function(event){
				marketTimer && clearTimeout(marketTimer); 
				var rule = $(this).parent().parent().next();
				marketTimer = setTimeout(function(){ 
					if(rule.hasClass("hidden")){
						rule.removeClass("hidden");
					}else{
						rule.addClass("hidden");
					}
				},300);
				var type = $(this).attr('showType');
				var customId = $(this).attr('customGroupId');
				coreMain.queryCustomLabelDetail(type, rule, customId);
			}).COClive("dblclick",function(){
				marketTimer && clearTimeout(marketTimer); 
				$(this).addCartAnimate({
					isClick:true,
					hideFuc:function(ths){
						return UserAttention.addCustomShopCart(ths);
					}
			});
		});
		},
	    /**
		 * 用户群操作
		 */
	    userAttentionCustomOperate : function(target){
			$(target).COClive("click",function(e) { 
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
				var selfIndex = $(".J_custom_attention_operation_btn").index($(this));
				var customId = $(this).attr("customId");
				$.each($(".J_custom_attention_operation_btn"),function(index,item){
					if(index != selfIndex){
					 var resultItemActiveList= $(item).next(".resultItemActiveList");
					     resultItemActiveList.hide();
					     $(item).removeClass("resultItemActiveOption");
					}
				});
				if($('#operate_'+customId).find("li").length == 0){
					var createCityId = $(this).attr("createCityId");
					var createUserId = $(this).attr("createUserId");
					var isPrivate = $(this).attr("isPrivate");
					var isNotContainLocalList = $("#isNotContainLocalList").val();
					var isContainLocalList = $(this).attr("isContainLocalList");
					var userId = $("#userId").val();
					var cityId = $("#cityId").val();
					var root = $("#root").val();
					if(isPrivate == 0){
						if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
							$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
						}else{
							$.fn.weakTip({"tipTxt":"抱歉，该用户群无规则、无清单可用，无操作！" });
						}
					}else{
						if(createUserId != userId){
							$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
						}else{
							$.fn.weakTip({"tipTxt":"抱歉，该用户群无规则、无清单可用，无操作！" });
						}
					}
				}else{ 
					$('#operate_'+customId).children("ol").children("li:last-child").addClass("end");
					var _t=$(this);
					var resultItemActiveList= $(this).next(".resultItemActiveList");
					if(resultItemActiveList.is(":visible")){
							resultItemActiveList.hide();
							_t.removeClass("resultItemActiveOption");
					}else{
						var resultListHeight = resultItemActiveList.height();
						var _tOffsetHeight= _t.offset().top + _t.height();
						var documentHeight = $(document.body).height();
						var resultHeight = resultListHeight + _tOffsetHeight;
						if(documentHeight<resultHeight){
							$(".main-content").height(resultHeight + 20);
						}
					    resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveOption");});
					}
				} 
			});
		},
		/**
		 * 标签下拉详情
		 */
		labelMarketDetail : function(target){
			$(target).COClive("click",function(event){
				marketTimer && clearTimeout(marketTimer); 
				var rule = $(this).parent().parent().next();
				marketTimer = setTimeout(function(){ 
					if(rule.hasClass("hidden")){
						rule.removeClass("hidden");
					}else{
						rule.addClass("hidden");
					}
				},300);
			}).COClive("dblclick",function(){
				marketTimer && clearTimeout(marketTimer); 
				$(this).addCartAnimate({
					isClick:true,
					hideFuc:function(ths){
						return UserAttention.addLabelShopCart(ths);
					}
			});
		});
		},
		/**
		 * 标签操作
		 */
		userAttentionLabelOperate : function(target){
			$(target).COClive("click",function(e) { 
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
				var selfIndex = $(".J_label_attention_operation_btn").index($(this));
				var labelId = $(this).attr("labelId");
				$.each($(".J_label_attention_operation_btn"),function(index,item){
					if(index != selfIndex){
					 var resultItemActiveList= $(item).next(".resultItemActiveList");
					     resultItemActiveList.hide();
					     $(item).removeClass("resultItemActiveOption");
					}
				});
				if($('#operate_'+labelId).find("li").length == 0){
					$.fn.weakTip({"tipTxt":"抱歉，该标签无操作！" });
				}else{ 
					$('#operate_'+labelId).children("ol").children("li:last-child").addClass("end");
					var _t=$(this);
					var resultItemActiveList= $(this).next(".resultItemActiveList");
					if(resultItemActiveList.is(":visible")){
							resultItemActiveList.hide();
							_t.removeClass("resultItemActiveOption");
					}else{
						var resultListHeight = resultItemActiveList.height();
						var _tOffsetHeight= _t.offset().top + _t.height();
						var documentHeight = $(document.body).height();
						var resultHeight = resultListHeight + _tOffsetHeight;
						if(documentHeight<resultHeight){
							$(".main-content").height(resultHeight + 20);
						}
					    resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveOption");});
					}
				} 
			});
		},
		/**
		 * 加入到购物车
		 * @parameter target 触发时间节点
		 */
		addLabelShopCart : function(target) {
			var labelId = $(target).attr("labelId");
			coreMain.addShopCart(labelId, 1, 0, "");
			return true;
		},
		/**
		 * 加入到购物车
		 * @parameter target 触发时间节点
		 */
		addCustomShopCart : function(target) {
			var customId = $(target).attr("customGroupId");
			coreMain.addShopCart(customId, 2, 0, "");
			return true;
		},
		/**
		 * 标签收藏图标点击取消收藏标签事件
		 * @parameter labelId 标签Id
		 */
		cancelAttentionLabel : function() {
			$(".J_label_market_list_cancel_attention").COClive("click", function() {
				var labelId = $(this).attr("labelid");
				var url=$.ctx + "/core/ciUserAttentionAction!delUserAttentionLabel.ai2do";
				$.ajax({
					type: "POST",
					url: url,
					data:{"labelId":labelId},
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.message });
				       		UserAttention.init();
						} else {
							commonUtil.create_alert_dialog("confirmDialog", {
								 "txt":result.message,
								 "type":"failed",
								 "width":500,
								 "height":200
							});
						}
					}
				});
			});
		},
		/**
		 * 用户群收藏图标点击取消收藏事件
		 * @parameter customGroupId 用户群Id
		 */
		cancelAttentionCustomGroup : function() {
			$(".J_custom_market_list_cancel_attention").COClive("click", function() {
				var customGroupId = $(this).attr("customid");
				var ths = $(this);
				var url=$.ctx + "/core/ciUserAttentionAction!delUserAttentionCustom.ai2do";
				$.ajax({
					type: "POST",
					url: url,
					data:{"customGroupId":customGroupId},
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.message });
				       		UserAttention.show(ths);
						} else {
							commonUtil.create_alert_dialog("confirmDialog", {
								 "txt":result.message,
								 "type":"failed",
								 "width":500,
								 "height":200
							});
						}
					}
				});
			});
		}
	}
	return UserAttention;
}();