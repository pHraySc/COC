/**
 * 营销导航-标签列表js
 */
LabelMain = function() {
	var marketTimer = null; 
	var LabelMain = {
			labelInfoList: function(para) {
				$.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
				$.ajax({
					url		: $.ctx + "/core/labelMarketAction!findEffectiveLabelList.ai2do",
					type	: "POST",
					data	: para,
					success	: function(data){
						$("#labelTableList").html(data);
						var labelName = $("#labelName").val();
						if (labelName != "") {
							$("#labelSearchName").val(labelName);
					    }
						$("#labelTotalSize").html($("#labelPageTotalSize").val());
						LabelMain.labelPagetable();
						$('#marketTaskTitle').html("全景视图");
						$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon1");
						//$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/KuaiJieTongDao.png) no-repeat center center');
					},
				   	complete: function() {
				   		$.unblockUI(); 
				   	},
				   	stop: function() {
				   		$.unblockUI(); 
				   	}
				});
			},
			labelPagetable :function() {
				//收藏图标鼠标悬浮事件
				coreMain.attentionBoxHover();
				var actionUrl = $.ctx +"/core/labelMarketAction!findEffectiveLabelList.ai2do?" + $("#pageForm").serialize();
				$("#labelTableList").page({
					url:actionUrl,
					param:$("#queryLabelForm").serializeArray(),
					objId:'labelTableList',
					callback: LabelMain.labelPagetable 
				});
			},
			/**多条件查询begin**/
			/**
			 * 初始化点击过滤条件事件
			 */
			addLabelFilterClick : function(target){
				$(target).COClive("click",function() {
					//添加你选项的查询条件
					$(this).parent("ul").children("li").each(function(){
						$(this).removeClass("active");
					});
					$(this).addClass("active");
					//拼筛选条件
					var selectedLiId = $(this).attr("id");
					var selectedLiIdStr = "";
					if(typeof(selectedLiId) !== 'undefined' && selectedLiId != null && selectedLiId != ""){
						selectedLiId = $.trim(selectedLiId).split("_");
						selectedLiIdStr = selectedLiId[1];
					}
					var selectedItemDetailId = $(this).parents("div").attr("id") + "_" + selectedLiIdStr;
					var value = $(this).html();
					var selectedItemBox = $("<li class=\"fleft market-filter-item J_label_filter_item \"></li>");
					var selectedItemDetail = $("<span class=\"fleft J_label_filter_item_detail\" id=\"" + selectedItemDetailId + "\">" + value + "</span>");
					var selectedItemClose = $("<span class=\"fleft del-filter-item J_selected_item_close\">x</span>");
					selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
					//同一种条件是互斥添加
					LabelMain.labelMutualExclusion(selectedItemBox,selectedItemDetailId,$(this),value);
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
						LabelMain.labelMultConditionSearch();
					});
					LabelMain.labelMultConditionSearch();
				});
				event.stopPropagation?event.stopPropagation():event.cancelBubble=true;
			},
			/**
			 * 发布时间范围筛选按钮 显示隐藏
			 */
			labelFilterButtonEvent : function(){
				$(".J_Label_date").focus(function(){ 
					$(".J_label_date_button").show(); 
				}).blur(function(){
					var first = $(".J_Label_date").first().val();
					var last = $(".J_Label_date").last().val();
					if($.trim(first).length == 0 && $.trim(last).length == 0){
						$(".J_label_date_button").hide(); 
					}
				});
			},
			/**
			 * 标签集市 多条件筛选互斥添加
			 */
			labelMutualExclusion	: function(selectedItemBox,selectedDivId,selectedObj,value){
				//同一种条件是互斥添加
				var flag = true;
				value = $.trim(value);
				var selectedE = "";
				if(selectedObj != null){
					selectedE = selectedObj.attr("selectedElement");
				}
			    if($(".J_label_filter_item").size() >= 1){
					$(".J_label_filter_item_detail").each(function(){
			        	var idArr = $.trim($(this).attr("id")).split("_");
			        	var selectedDivIdArr = $.trim(selectedDivId).split("_");
//			        	alert(idArr[0]+","+selectedDivIdArr[0]);
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
					if(flag) $("#labelFilterItemList").append(selectedItemBox);
			    }else{
					if(selectedE != "all"){
						$("#labelFilterItemList").append(selectedItemBox);
					}
			    }
			},
			/**
			 * 点击标签发布时间范围
			 */
			labelCreateTimeFilter : function(target){
				$(target).COClive("click",function() {
					//点击规模筛选按钮
					$("#dateType1").find("li").each(function(){
						$(this).removeClass("active");
				    });
					var startDate = $.trim($(".J_Label_date").first().val());
					var endDate = $.trim($(".J_Label_date").last().val());
					if("" != startDate || "" != endDate){
						var value = "";
						var selectedItemDetailId = "dateType1";
						if("" != startDate && "" != endDate){
							value = startDate+"至"+endDate
						}else if("" != endDate && "" == startDate){
							value = "小于" + endDate;
						}else if("" != startDate && "" == endDate){
							value = "大于" + startDate;
						}
						var selectedItemBox = $("<li class=\"fleft market-filter-item J_label_filter_item \"></li>");
						var selectedItemDetail = $("<span class=\"fleft J_label_filter_item_detail\" id=\"" + selectedItemDetailId + "\">" + value + "</span>");
						var selectedItemClose = $("<span class=\"fleft del-filter-item J_selected_item_close\">x</span>");
						selectedItemBox.append(selectedItemDetail).append(selectedItemClose);
						//同一种条件是互斥添加
						LabelMain.labelMutualExclusion(selectedItemBox,selectedItemDetailId,$(this),value);
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
							$("#dateType1").find("li").first().addClass("active");
							LabelMain.labelMultConditionSearch();
						});
						LabelMain.labelMultConditionSearch();
					}
				});
			},
			/**
			 * 多条件关联查询
			 */
			labelMultConditionSearch:function(){
				$(".J_label_multi_filter").val("");
				//获得查询条件
				$(".J_label_filter_item_detail").each(function(){
					var idStr =$.trim($(this).attr("id")).split("_")
					var valStr = $(this).html();
					 if("updateCycle1" == idStr[0]){
						  if(idStr.length > 1){
							  $("#updateCycle").attr("value",idStr[1]);	
						  }
					  }else if("dateType1" == idStr[0]){
						  if($(this).html().indexOf("大于")>=0){
							  var startDate = $.trim($(this).html()).split("大于");
							  $("#startDate").attr("value",startDate[1]);
						  }else if($(this).html().indexOf("小于")>=0){
							  var endDate = $.trim($(this).html()).split("小于");
							  $("#endDate").attr("value",endDate[1]);
						  }else if($(this).html().indexOf("至")>=0){
							  var date = $.trim($(this).html()).split("至");
							  var startDate = date[0];
							  var endDate = date[1];
							  $("#startDate").attr("value",startDate);
							  $("#endDate").attr("value",endDate);
						  }else{
							  var valStr ="";
							  if("1" == idStr[1]){
								  valStr = "oneDay";
							  }else if("2" == idStr[1]){
								  valStr = "oneMonth";
							  }else if("3" == idStr[1]){
								  valStr = "threeMonth";
							  }
							  $("#formDataScope").attr("value",valStr);	
						  }
					  }
				});
				var para = $("#queryLabelForm").serializeArray();
				LabelMain.labelInfoList(para);
			},
			/**多条件end**/
			/**
			 * 标签下拉详情
			 */
			labelMarketDetail : function(target) {
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
							return LabelMain.addLabelShopCart(ths);
						}
				});
			});
		},
		/**
		 * 标签操作
		 */
		labelMarketOperate : function(target){
			$(target).COClive("click",function(e) { 
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
				
				var selfIndex = $(".J_label_market_operation_btn").index($(this));
				var labelId = $(this).attr("labelId");
				$.each($(".J_label_market_operation_btn"),function(index,item){
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
		}
	}
	return LabelMain;
}();
