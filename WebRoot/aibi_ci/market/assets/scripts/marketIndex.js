/********
 *  2015-1-1
 *	营销导航列表
 */
MarketIndex = function() {
	var MarketIndex = {
		/**
		 * 根据名称搜索结果
		 */
		searchByName	: function() {
			$('#labelCustomName').val($('#search-customer').val());
			var dataVal = $('#search-customer').attr('data-val');
			if(!dataVal || dataVal == 'false') {
				$('#labelCustomName').val('');
			}
			MarketIndex.search();
		},
		/**
		 * 所有查询列表入口
		 */
		search	: function() {
	        var actionUrl = $.ctx + '/ci/ciMarketAction!findMarketList.ai2do';
	        $.ajax({
	            url		: actionUrl,
	            type	: 'POST',
	            dataType: 'html',
	            data	: $('#queryForm').serialize(),
	            success	:function(html){
	                $('#customLabelList').html('');
	                $('#customLabelList').html(html);
	                var totalSize = $('#totalConfigSize').val();
		            if(totalSize){
			            $('#totalSizeNum').html(totalSize);
					}
		            //定位双击加入到购物车提示
		        	MarketIndex.locationAddShopCartTip();
		            MarketIndex.pagetable();
	            }
	        })
	    },
	    /**
	     * 定位双击加入到购物车的提示
	     */
	    locationAddShopCartTip	: function() {
	    	var addShopCartTip = getCookieByName('addShopCartTip');
	    	if(addShopCartTip == 1) {
	    		$('.J_add-cart-tip').hide();
	    	} else {
	    		if(!$.isNotBlank($('#parentMarketIdIconInput').val())){return;}
    	    	if($('.J_market_list_ul li:first-child').length==0){return;}
    	    	var x = -10; 
            	var y = $("#marketMainList").offset().left+80 > 220 ? 220 : $("#marketMainList").offset().left+80;
            	$('.J_add-cart-tip').css({left : y, top :x}).show();
		    }
	    },
	    /**
	     * 分页插件
	     */
		pagetable	: function() {
	    	var actionUrl = $.ctx + '/ci/ciMarketAction!findMarketList.ai2do?' + $('#pageForm').serialize();
	    	$('#customLabelList').page({
	    		url		: actionUrl,
	    		param	: $('#queryForm').serialize(),
	    		objId	: 'customLabelList',
	    		callback: MarketIndex.pagetable 
	    	});
	    },
		/**
		 * 操作
		 */
	    marketListOperate : function(target){
			$(target).COClive("click",function(e) {
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
				
				var selfIndex = $(".J_market_list_operation_btn").index($(this));
				$.each($(".J_market_list_operation_btn"),function(index,item){
					if(index != selfIndex){
					 var resultItemActiveList= $(item).next(".resultItemActiveList");
					     resultItemActiveList.hide();
					     $(item).removeClass("resultItemActiveOption");
					}
				});
				
				var _t=$(this);
				_t.next(".resultItemActiveList").children("ol").children("li:last-child").addClass("end");
				
				var resultItemActiveList= $(this).next(".resultItemActiveList");
				if(resultItemActiveList.is(":visible")){
						resultItemActiveList.hide();
						_t.removeClass("market-info-list-btn-active");
				}else{
					var resultListHeight = resultItemActiveList.height();
					var _tOffsetHeight= _t.offset().top + _t.height();
					var documentHeight = $(document.body).height();
					var resultHeight = resultListHeight + _tOffsetHeight;
					if(documentHeight<resultHeight){
						$(document.body).height(resultHeight + 20);
					}
				    resultItemActiveList.slideDown('fast',function(){_t.addClass("market-info-list-btn-active");});
				}
			});
		},
		/**
		 * 加入到购物车
		 * @parameter target 触发时间节点
		 */
		addShopCart	: function(target) {
			var labelOrCustomId;
			
			var labelId = $(target).attr('labelId');
			var customGroupId = $(target).attr('customGroupId');
			var type = $(target).attr('showType');
			//不适合营销用户，值为"-"
			var defaultOp = $('#defaultOp').val();
			
			if(type == 1) {
				labelOrCustomId = labelId;
			} else if(type == 2) {
				labelOrCustomId = customGroupId;
			}
			if(defaultOp == '-') {
				var num = coreMain.getShopCartListSize();
				if(num == 0) {
					coreMain.alertFailed('firstShopCartElementError', '不适合营销用户不能作为第一个计算元素！');
					return false;
				}
			}
			coreMain.addShopCart(labelOrCustomId, type, 0, defaultOp);
			return true;
		},
		/**
		 * 用户群、标签取消推荐customLabelSceneRel
		 */
		cancelRecommend : function(target){
			$(target).COClive("click",function(e) {
				var primaryKeyId = $(this).attr("primaryKeyId");
				var showType = $(this).attr("showType");
				var labelId = $(this).attr("labelId");
				var customGroupId = $(this).attr("customGroupId");
				var url = $.ctx + "/ci/ciMarketAction!deleteCustomOrLabelSceneRel.ai2do?";
				$.ajax({
					type: "POST",
					url: url,
					data:{"customLabelSceneRel.primaryKeyId":primaryKeyId,
						  "customLabelSceneRel.labelId":labelId,
						  "customLabelSceneRel.customGroupId":customGroupId,
						  "customLabelSceneRel.showType":showType
						 },
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.msg+"","backFn":MarketIndex.search()});
						}else{
							commonUtil.create_alert_dialog("confirmDialog", {
								"txt":result.msg+"",
								"type":"failed",
								"width":500,
								"height":200
							},function(){MarketIndex.search();});
						}
					}
				});
			});
		},
		/**
		 * 查询用户群标签详细信息
		 * @parameter type 类型：1标签 2用户群 
		 * @parameter el   返回页面拼接到的元素（jquery对象：$('#xxx')）
		 * @parameter labelOrcustomId 用户群或者标签ID
		 */
		queryCustomLabelDetail	: function(type, el, labelOrCustomId) {
			if(!labelOrCustomId) {
				commonUtil.create_alert_dialog('showDetailFailed_Id', {
					txt 	: '请传入用户群或标签ID！',
					type 	: 'failed', 
					width 	: 500,
					height 	: 200,
					param	: false
				});
				return;
			}
			var url = '';
			if(type == 1) {//标签
				url = $.ctx + '/ci/ciMarketAction!findLabelDetail.ai2do';
			} else if(type == 2) {//用户群
				url = $.ctx + '/ci/ciMarketAction!findCustomGroupDetail.ai2do';
			} else {
				commonUtil.create_alert_dialog('showDetailFailed_Type', {
					txt 	: '数据类型有误，无法区分是用户群还是标签！',
					type 	: 'failed', 
					width 	: 500,
					height 	: 200,
					param	: false
				});
				return;
			}
			
			$.ajax({
				url		: url + '?labelOrCustomId=' + labelOrCustomId,
				type	: 'POST',
				success	: function(data){
					el.html(data);
				}
			});
		}
	}
	return MarketIndex;
}();