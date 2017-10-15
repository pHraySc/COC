/********
 *  2015-1-4
 *	我的用户群
 */
customManagerMain = function() {
	var marketTimer = null; 
	var customManagerMain = {
			/**
			 * 加载用户群分页列表
			 */
			loadList : function(option){
				var defaults = {
						url:"",
						data:"",
						ajaxType:"",
						successFunc:function(){}
					};
					var options = $.extend(defaults,option);
					$.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
					$.ajax({
						url		: options.url,
						data	: options.data,
						type	: options.ajaxType,
						success	: options.successFunc,
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
				var paramStr = $("#queryForm").serialize();
				var actionUrl = $.ctx + "/core/ciCustomerManagerAction!queryCustomersByPage.ai2do?"+$("#pageForm").serialize();
				$("#customersTable").page( {url:actionUrl,param:paramStr,objId:'customersTable',callback:customManagerMain.pageClick });
			},
			
			/**
			 * 点击查询用户群
			 */
			clickLoadCustomList : function(target){
				$(target).COClive("click",function() {
					var keyWord = $("#keyWordGroupName").val();
					if ("false" == $("#keyWordGroupName").attr("data-val")) {
				        	keyWord = "";
				    }
					$("#customGroupName").val(keyWord);
					$("#updateCycle").val($("#updateCycle_").val());
					$("#isPrivate").val($("#isPrivate_").val());
					$("#createTypeId").val($("#createTypeId_").val());
					$("#dataStatus").val($("#dataStatus_").val());
					customManagerMain.loadList({
						url			: $.ctx + "/core/ciCustomerManagerAction!queryCustomersByPage.ai2do",
						data 		: $("#queryForm").serialize(),
						ajaxType	: "POST",
						successFunc	: function(data){
						$("#customersTable").html(data);
						$("#totalCustomNum").html($("#pageTotalSize").val());
						customManagerMain.pageClick();
					    }
					});
				});
			},
			/**
			 * 查询用户群
			 */
			loadCustomList : function(){
				customManagerMain.loadList({
					url			: $.ctx + "/core/ciCustomerManagerAction!queryCustomersByPage.ai2do",
					data 		: $("#queryForm").serialize(),
					ajaxType	: "POST",
					successFunc	: function(data){
					$("#customersTable").html(data);
					$("#totalCustomNum").html($("#pageTotalSize").val());
					customManagerMain.pageClick();
				    }
				});
			},
			
			/**
			 * 模拟下拉框
			 */
			clickListStyle : function(target){
				$(target).COClive("click",function(){
					if($(this).nextAll(".querySelListBox").is(":hidden")){
			        	$(this).nextAll(".querySelListBox").slideDown();
			        	if($(this).hasClass("selBtn")){
			                $(this).addClass("selBtnActive");
			            }else{
			            	$(this).next(".selBtn").addClass("selBtnActive");
			            }
			       }else{
			        	 $(this).nextAll(".querySelListBox").slideUp();
			             $(this).removeClass("selBtnActive");

			            }
			    	return false;
				});
			},
			/**
			 * 获得点击下拉框值
			 */
			getClickListValue : function(target){
				$(target).COClive("click",function(){
					var selVal = $(this).attr("value");
			    	var selHtml = $(this).html();
			    	 $(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
			    	 $(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
			    	 $(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
			    	//向隐藏域中传递值
			       $(this).parents(".querySelListBox").slideUp();
			         return false;
				});
			},
			/**
			 * 用户群列表内存显示隐藏
			 */
			customListHidenOrShow : function(target){
				$(target).COClive("click",function(event){
					marketTimer && clearTimeout(marketTimer); 
					var J_com_show_arrow = $(this);
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
							return customManagerMain.addCustomShopCart(ths);
						}
				});
			});
			},
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
					if($('#'+customId).find("li").length == 0){
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
			 * 修改用户群
			 */
			editCustomerOpenCalculate : function(target){
				$(target).COClive("click",function(e){
					var e = e || window.event;
					e.stopPropagation ? e.stopPropagation() : e.cancelBubble=true;
					var customId = $(this).attr("customId");
					$("#edit_custom_input").val(customId);
					$("#edit_custom_form").submit();
				});
			},
			/**
			 * 修改用户群
			 */
			editCustomer : function(target){
				$(target).COClive("click",function(e){
					$("#isEditCustom").val("true");
					var customId = $(this).attr("customGroupId");
					var url = $.ctx + "/core/ciCustomerManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+customId;
					var dlgObj = dialogUtil.create_dialog("createEditCustomDialog", {
						"title" :  "用户群修改",
						"height": "auto",
						"width" : 660,
						"frameSrc" : url,
						"frameHeight":345,
						"position" : ['center','center'] 
					},function(){
						$("#isEditCustom").val("false");
					});
					$(".tishi").each(function(){
						var toffset=$(this).parent("td").offset();
						var td_height=$(this).parent("td").height();
						$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
					});
			    })
			},
			/**
			 * 删除用户群确认弹出框
			 */
			delOne : function(target){
				$(target).COClive("click",function(e){
					var customId = $(this).attr("customId");
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":"确认删除该用户群？",
						 "type":"confirm",
						 "width":500,
						 "height":200,
						 "param":customId
					},"customManagerMain.del");
			    });
			},
			/**
			 * 删除用户群
			 */
			del : function(customId){
				$("#isEditCustom").val("false");
				var actionUrl = $.ctx + "/core/ciCustomerManagerAction!delete.ai2do";
				$.ajax({
					type: "POST",
					url: actionUrl,
					data: "ciCustomGroupInfo.customGroupId="+customId,
					success: function(result){
						if(result.success){
							$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
								$("input[name='pager.totalSize']").val(0);
								customManagerMain.loadCustomList({
									url			: $.ctx + "/core/ciCustomerManagerAction!queryCustomersByPage.ai2do",
									data 		: $("#queryForm").serialize(),
									ajaxType	: "POST",
									successFunc	: function(data){
									$("#customersTable").html(data);
									$("#totalCustomNum").html($("#pageTotalSize").val());
									customManagerMain.pageClick();
								    }
								});
					 		}});
						}else{
							commonUtil.create_alert_dialog("confirmDialog", {
								 "txt":"删除失败：" +result.msg,
								 "type":"failed",
								 "width":500,
								 "height":200
							}); 
						}
					}
				});
			},
			/**
			 * 用户群导入
			 */
			importCustom : function(target){
				$(target).COClive("click",function(e){
					var improtInitUrl = $.ctx + "/core/ciCustomerManagerAction!importInit.ai2do";
					$("#isEditCustom").val("false");
					var dlgObj = dialogUtil.create_dialog("dialog-form", {
						"title" :  "导入创建用户群",
						"height": "auto",
						"width" : 680,
						"frameSrc" : improtInitUrl,
						"frameHeight":550,
						"position" : ["center","center"] 
					},"customManagerMain.loadCustomList").focus();
				});
			},
			/**
			 * 用户群详情
			 */
			queryGroupInfo : function(target){
				$(target).COClive("click",function(e){
					var customId = $(this).attr("customId");
					var customName = $(this).attr("customName");
					var url = $.ctx + "/core/ciCustomerManagerAction!findGroupInfo.ai2do?ciCustomGroupInfo.customGroupId="+customId;
					var dlgObj = dialogUtil.create_dialog("queryGroupInfoDialog", {
						"title" :  commonUtil.trim(customName+" 用户群的详情","g"),
						"height": "auto",
						"width" : 905,
						"frameSrc" : url,
						"frameHeight":500,
						"position" : ['center','center'] 
					}); 
				});
			},
			/**
			 * 显示用户群规则
			 */
			showRule : function(target){
				$(target).COClive("click",function(e){
					var customId = $(target).attr("customId");
					var url = $.ctx + "/core/ciCustomerManagerAction!showRule.ai2do?ciCustomGroupInfo.customGroupId="+customId;
					var dlgObj = dialogUtil.create_dialog("queryGroupRuleDialog", {
						"title" :  "用户群的完整规则",
						"height": "auto",
						"width" : 800,
						"frameSrc" : url,
						"frameHeight":400,
						"position" : ['center','center'] 
					}).focus(); 
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
						var keyWord = $("#keyWordGroupName").val();
						if ("false" == $("#keyWordGroupName").attr("data-val")) {
					        	keyWord = "";
					    }
						$("#customGroupName").val(keyWord);
						customManagerMain.loadCustomList();
					}
				});
			}
	}
	return customManagerMain;
}();