/********
 *  2014-12-30
 *	用户群集市
 */
CustomMarketMain = function() {
	var marketTimer = null; 
	var CustomMarketMain = {
		/**
		 * 加载用户群分页列表
		 */
		loadCustomList : function(){
			if($("#search-customer").attr("data-val") == "true"){
				$("#customGroupName").val($("#search-customer").val());
			}
			$.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
			$.ajax({
				url			: $.ctx + "/core/ciCustomerMarketAction!customersList.ai2do",
				data 		: $("#multiSearchForm").serialize(),
				type	: "POST",
				success	: function(data){
					$("#customMarketList").html(data);
					$("#totalCustomNum").html($("#pageTotalSize").val());
					$('#marketTaskTitle').html("全景视图");
					$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon1");
					//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
					$(".market-group-item-hover").removeClass("market-group-item-hover");
					$("#customMarketLink").addClass("market-group-item-hover");
					CustomMarketMain.pageClick();
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
		pageClick :	function(){
			var paramStr = $("#multiSearchForm").serialize();
			var actionUrl = $.ctx + "/core/ciCustomerMarketAction!customersList.ai2do?"+$("#pageForm").serialize();
			$("#customMarketList").page( {url:actionUrl,param:paramStr,objId:'customMarketList',callback:CustomMarketMain.pageClick });
			coreMain.attentionBoxHover();
		},
		/**
		 * 初始化过滤条件  收起、更多样式
		 */
		moreClick : function(target){
			$(target).COClive("click",function() {
				var moreFilter = $("#moreFilterList");
				if(moreFilter.hasClass("hidden")){
					moreFilter.removeClass("hidden");
					$(".market-filter-more-list span:first").text("收起");
					$(".market-filter-more-list span:last").addClass("market-filter-more-up-icon");
				}else{
					moreFilter.addClass("hidden");
					$(".market-filter-more-list span:first").text("更多");
					$(".market-filter-more-list span:last").removeClass("market-filter-more-up-icon");
				}
			});
		},
		/**
		 * 范围筛选按钮 显示隐藏
		 */
		filterButtonEvent : function(){
			$(".J_filter_input").focus(function(){ 
				$(".J_num_filter_button").show(); 
				var val = this.value;
				if(!$.isNumeric(val)){
					$(this).val("");
				} 
			}).blur(function(){
				var first = $(".J_filter_input").first().val();
				var last = $(".J_filter_input").last().val();
				if(!$.isNumeric(first) && !$.isNumeric(last)){
					$(".J_num_filter_button").hide(); 
				}
			}).keydown(function(){
				var val = this.value;
				if(!$.isNumeric(val)){
					$(this).val("");
				} 
			}).keyup(function(){
				var val = this.value;
				if(!$.isNumeric(val)){
					$(this).val("");
				} 
			});
			$(".J_filter_date").focus(function(){ 
				$(".J_date_filter_button").show(); 
			}).blur(function(){
				var first = $(".J_filter_date").first().val();
				var last = $(".J_filter_date").last().val();
				if($.trim(first).length == 0 && $.trim(last).length == 0){
					$(".J_date_filter_button").hide(); 
				}
			});
		},
		/**
		 * 点击删除过滤条件
		 */
		deleteFilterClick : function(){
			$(target).COClive("click",function() {
				$(this).parent().remove();
			});
		},
		/**
		 * 多条件筛选查询
		 */
		multiCustomMarketSearch	: function(){
			$(".J_custom_multi_filter").val("");
			if($("#search-customer").attr("data-val") == "true"){
				$("#customGroupName").val($("#search-customer").val());
			}
			//1：获得查询条件
			$(".J_custom_filter_item_detail").each(function(){
				var idStr =$.trim($(this).attr("id")).split("_");
				var valStr = $(this).html();
				if("customNum" == idStr[0]){
					  if($(this).html().indexOf("大于")>=0){
						  var minNum = $.trim($(this).html()).split("大于");
						  $("#minCustomNum").attr("value",minNum[1]);
					  }else if($(this).html().indexOf("小于")>=0){
						  var maxNum = $.trim($(this).html()).split("小于");
						  $("#maxCustomNum").attr("value",maxNum[1]);
					  }else{
						  var nums = $.trim($(this).html()).split("-");
						  var minNum = nums[0];
						  var maxNum = nums[1];
						  $("#minCustomNum").attr("value",minNum);	
						  $("#maxCustomNum").attr("value",maxNum);	
					  }
				  }else if("updateCycle" == idStr[0]){
					  if(idStr.length > 1){
						  $("#updateCycle").attr("value",idStr[1]);	
					  }
				  }else if("createType" == idStr[0]){
					  if(idStr.length > 1){
					  	$("#createTypeId").attr("value",idStr[1]);	
					  }
				  }else if("dateType" == idStr[0]){
					  if($(this).html().indexOf("大于")>=0){
						  var startDate = $.trim($(this).html()).split("大于");
						  $("#customMarketStartDate").attr("value",startDate[1]);
					  }else if($(this).html().indexOf("小于")>=0){
						  var endDate = $.trim($(this).html()).split("小于");
						  $("#customMarketEndDate").attr("value",endDate[1]);
					  }else if($(this).html().indexOf("至")>=0){
						  var date = $.trim($(this).html()).split("至");
						  var startDate = date[0];
						  var endDate = date[1];
						  $("#customMarketStartDate").attr("value",startDate);
						  $("#customMarketEndDate").attr("value",endDate);
					  }else{
						  $("#dateType_").attr("value",idStr[1]);	
					  }
				  }else if("isPrivate" == idStr[0]){
					  if(idStr.length > 1){
						  $("#isPrivate").attr("value",idStr[1]);	
					  }
				  }else if("customDataStatus" == idStr[0]){
					  if(idStr.length > 1){
						  	$("#dataStatus").attr("value",idStr[1]);	
					  }
				  }else if("city" == idStr[0]){
					  if(idStr.length > 1){
						  	$("#createCityId").attr("value",idStr[1]);	
					  }
				  }else if("myCustom1" == idStr[0]){
					  $("#isMyCustomMarket").attr("value","true");
				  }
			});
			//2：加载用户群列表
			CustomMarketMain.loadCustomList();
		},
		/**
		 * 用户群集市 多条件筛选互斥添加
		 */
		mutualExclusion	: function(selectedItemBox,selectedDivId,selectedObj,value){
			//同一种条件是互斥添加
			var flag = true;
			value = $.trim(value);
			var selectedE = "";
			if(selectedObj != null){
				selectedE = selectedObj.attr("selectedElement");
				if(selectedE == "all" && selectedObj.hasClass("J_clear_date_input")){
					$(".J_filter_date").val("");
					$(".J_date_filter_button").hide();
				}
				if(selectedE == "all" && selectedObj.hasClass("J_clear_input")){
					$(".J_filter_input").val("");
					$(".J_num_filter_button").hide();
				}
			}
		    if($(".J_custom_filter_item").size() >= 1){
				$(".J_custom_filter_item_detail").each(function(){
		        	var idArr = $.trim($(this).attr("id")).split("_");
		        	var selectedDivIdArr = $.trim(selectedDivId).split("_");
		          	if(idArr[0] == selectedDivIdArr[0]){
		          		if(selectedE == "all"){
		          			$(this).parent().remove();
		          		}else{
			        	  	$(this).html(value);
			        	  	$(this).attr("id",selectedDivId);
		          		}
		        	  	flag = false;
		          		return false;
		          	}else{
		          		if(selectedE == "all"){
		          			flag = false;
		          		}else{
			          		flag = true;
		          		}
		          	}
				});
				if(flag) $("#customFilterItemList").append(selectedItemBox);
		    }else{
				if(selectedE != "all"){
					$("#customFilterItemList").append(selectedItemBox);
				}
		    }
		},
		/**
		 * 初始化点击过滤条件事件
		 */
		addFilterClick : function(target){
			$(target).COClive("click",function() {
				//添加你选项的查询条件
				$(this).parent("ul").children("li").each(function(){
					$(this).removeClass("active");
				});
				$(this).addClass("active");
				if($(this).hasClass("J_is_my_custom")){
					$("#isMyCustom").val("true");
				}else{
					$("#isMyCustom").val("false");
				}
				//拼筛选条件
				var selectedLiId = $(this).attr("id");
				var selectedLiIdStr = "";
				if(typeof(selectedLiId) !== 'undefined' && selectedLiId != null && selectedLiId != ""){
					selectedLiId = $.trim(selectedLiId).split("_");
					selectedLiIdStr = selectedLiId[1];
				}
				var selectedItemDetailId = $(this).parents("div").attr("id") + "_" + selectedLiIdStr;
				var value = $(this).html();
				var selectedItemBox = $("<li class=\"fleft market-filter-item J_custom_filter_item \"></li>");
				var selectedItemDetail = $("<span class=\"fleft J_custom_filter_item_detail\" id=\"" + selectedItemDetailId + "\">" + value + "</span>");
				var selectedItemClose = $("<span class=\"fleft del-filter-item J_selected_item_close\">x</span>");
				selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
				//同一种条件是互斥添加
				CustomMarketMain.mutualExclusion(selectedItemBox,selectedItemDetailId,$(this),value);
				//绑定单击事件
				var closeLi = $(this).parent("ul").children("li");
				selectedItemClose.click(function(){
					$(this).parent().remove();
					closeLi.each(function(){
				    	 if($(this).attr("selectedElement") == "all"){
				    		 $(this).addClass("active");
				    	 }else{
				    		 $(this).removeClass("active");
				    	 }
					 });
					CustomMarketMain.multiCustomMarketSearch();
				});
				CustomMarketMain.multiCustomMarketSearch();
			});
			event.stopPropagation?event.stopPropagation():event.cancelBubble=true;
		},
		/**
		 * 初始化用户群规模筛选
		 */
		customNumFilter : function(target){
			$(target).COClive("click",function() {
				//点击规模筛选按钮
				$("#customNum_").find("li").each(function(){
					$(this).removeClass("active");
			    });
				var minNum = $.trim($(".J_filter_input").first().val());
				var maxNum = $.trim($(".J_filter_input").last().val());
				if(maxNum != "" && parseInt(minNum,10) >= parseInt(maxNum,10)){
					commonUtil.create_alert_dialog("confirmDialog", {
						"txt":"最大值小于最小值！",
						"type":"failed",
						"width":500,
						"height":200
					});
					return;
				}
				if("" != minNum || "" != maxNum){
					var value = "";
					var selectedItemDetailId = "customNum";
					if("" != minNum && "" != maxNum){
						value = minNum + "-" + maxNum;
					}else if("" != maxNum && "" == minNum){
						value = "小于" + maxNum;
					}else if("" != minNum && "" == maxNum){
						value = "大于" + minNum;
					}
					var selectedItemBox = $("<li class=\"fleft market-filter-item J_custom_filter_item \"></li>");
					var selectedItemDetail = $("<span class=\"fleft J_custom_filter_item_detail\" id=\"" + selectedItemDetailId + "\">" + value + "</span>");
					var selectedItemClose = $("<span class=\"fleft del-filter-item J_selected_item_close\">x</span>");
					selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
					//同一种条件是互斥添加
					CustomMarketMain.mutualExclusion(selectedItemBox,selectedItemDetailId,$(this),value);
					//绑定单击事件
					var closeLi = $("#customNum_ ul li");
					selectedItemClose.click(function(){
						$(this).parent().remove();
						closeLi.each(function(){
							if($(this).attr("selectedElement") == "all"){
								if($(this).hasClass("J_clear_date_input")){
									 $(".J_filter_date").val("");
									 $(".J_date_filter_button").hide();
								 }
								 if($(this).hasClass("J_clear_input")){
									 $(".J_filter_input").val("");
									 $(".J_num_filter_button").hide();
								 }
								$(this).addClass("active");
							}else{
								$(this).removeClass("active");
							}
						});
						$("#customNum_").find("li").first().addClass("active");
						$(".J_custom_multi_filter").val("");
						CustomMarketMain.multiCustomMarketSearch();
					});
					CustomMarketMain.multiCustomMarketSearch();
				}
			});
		},
		/**
		 * 初始化用户群创建时间筛选
		 */
		customCreateTimeFilter : function(target){
			$(target).COClive("click",function() {
				//点击规模筛选按钮
				$("#dateType").find("li").each(function(){
					$(this).removeClass("active");
			    });
				var startDate = $.trim($(".J_filter_date").first().val());
				var endDate = $.trim($(".J_filter_date").last().val());
				if("" != startDate || "" != endDate){
					var value = "";
					var selectedItemDetailId = "dateType_";
					if("" != startDate && "" != endDate){
						value = startDate+"至"+endDate
					}else if("" != endDate && "" == startDate){
						value = "小于" + endDate;
					}else if("" != startDate && "" == endDate){
						value = "大于" + startDate;
					}
					var selectedItemBox = $("<li class=\"fleft market-filter-item J_custom_filter_item \"></li>");
					var selectedItemDetail = $("<span class=\"fleft J_custom_filter_item_detail\" id=\"" + selectedItemDetailId + "\">" + value + "</span>");
					var selectedItemClose = $("<span class=\"fleft del-filter-item J_selected_item_close\">x</span>");
					selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
					//同一种条件是互斥添加
					CustomMarketMain.mutualExclusion(selectedItemBox,selectedItemDetailId,$(this),value);
					//绑定单击事件
					var closeLi = $("#dateType ul li");
					selectedItemClose.click(function(){
						$(this).parent().remove();
						closeLi.each(function(){
							if($(this).attr("selectedElement") == "all"){
								if($(this).hasClass("J_clear_date_input")){
									 $(".J_filter_date").val("");
									 $(".J_date_filter_button").hide();
								 }
								 if($(this).hasClass("J_clear_input")){
									 $(".J_filter_input").val("");
									 $(".J_num_filter_button").hide();
								 }
								$(this).addClass("active");
							}else{
								$(this).removeClass("active");
							}
						});
						$("#dateType").find("li").first().addClass("active");
						$(".J_custom_multi_filter").val("");
						CustomMarketMain.multiCustomMarketSearch();
					});
					CustomMarketMain.multiCustomMarketSearch();
				}
			});
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
				var customId = $(this).attr('customId');
				coreMain.queryCustomLabelDetail(type, rule, customId);
			}).COClive("dblclick",function(){
				marketTimer && clearTimeout(marketTimer); 
				$(this).addCartAnimate({
					isClick:true,
					hideFuc:function(ths){
						return CustomMarketMain.addCustomShopCart(ths);
					}
			});
		});
		},
		searchButtonClick : function(target){
			$(target).COClive("click",function(e) { 
				CustomMarketMain.multiCustomMarketSearch();
			});
		},
		/**
		 * 用户群集市操作 start ============================================================
		 */
		/**
		 * 用户群操作
		 */
		marketCustomOperate : function(target){
			$(target).COClive("click",function(e) { 
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
				var selfIndex = $(".J_market_list_operation_btn").index($(this));
				var customId = $(this).attr("customId");
				$.each($(".J_market_list_operation_btn"),function(index,item){
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
							$("#marketMainList").height(resultHeight + 20);
						}
					    resultItemActiveList.slideDown('fast',function(){_t.addClass("market-info-list-btn-active");});
					}
				} 
			});
		},
		/**
		 * 用户群清单预览
		 */
		showCustomList : function(target){
			$(target).COClive("click",function() {
				var customId = $(this).attr('customId');
				var dataDate = $(this).attr('dataDate');
				var url = $.ctx + "/ci/customersManagerAction!findGroupList.ai2do?ciCustomGroupInfo.customGroupId="+customId+"&ciCustomGroupInfo.dataDate="+dataDate;
				var dlgObj = dialogUtil.create_dialog("queryListDialog", {
					"title" :  "用户群清单信息",
					"height": "auto",
					"width" : 700,
					"frameSrc" : url,
					"frameHeight":328,
					"position" : ['center','center'] 
				}); 
			});
		},
		/**
		 * 加入到购物车
		 * @parameter target 触发时间节点
		 */
		addCustomShopCart : function(target) {
			var customId = $(target).attr("customId");
			coreMain.addShopCart(customId, 2, 0, "");
			return true;
		},
		/**
		 * 加入到购物车失败提示
		 * @parameter target 触发时间节点
		 */
		addCustomShopCartFail : function(target) {
			$(target).COClive("dblclick",function(e){  
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
				var createCityId = $(this).attr("createCityId");
				var createUserId = $(this).attr("createUserId");
				var isPrivate = $(this).attr("isPrivate");
				var isContainLocalList = $(this).attr("isContainLocalList");
				var userId = $(this).attr("userId");
				var cityId = $(this).attr("cityId");
				var root = $(this).attr("root");
				if(isPrivate == 0){
					if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
						$.fn.weakTip({"tipTxt":"抱歉，您无权限添加到收纳篮！" });
					}else{
						$.fn.weakTip({"tipTxt":"抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！" });
					}
				}else{
					if(createUserId != userId){
						$.fn.weakTip({"tipTxt":"抱歉，您无权限添加到收纳篮!" });
					}else{
						$.fn.weakTip({"tipTxt":"抱歉，该用户群无规则、无清单可用，不能添加到收纳篮！" });
					}
				}
			});
		},
		searchCustomMarketEnter : function(target){
			$(target).COClive("keydown",function(event){  
				if(event.keyCode == 13){
					CustomMarketMain.loadCustomList();
				}
			});
		}

	}
	return CustomMarketMain;
}();