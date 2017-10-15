/********
 *  2014-12-30
 *	营销导航主页方法
 */
coreMain = function() {
	var coreMain = {
		sysIds : [],
		setLeftMenu : function(){
			var screenWidth = $(window).width();
			if(screenWidth < 1280){
				$(".left-menu-list").hide();
			}else{
				$(".left-menu-list").show();
			}
		},
		linkToMarketIndex : function() {
			var childMarketTaskId = $('#marketTaskIdFromIndexInput').val();
			$.ajax({
				url		: $.ctx + "/ci/ciMarketAction!findMarketIndex.ai2do?customLabelSceneRel.marketTaskId=" + childMarketTaskId,
				type	: "POST",
				success	: function(data){
					$("#marketMainList").html(data);
				}
			});
		},
		/**
		 * 跳转到除了营销导航页面的其他页面
		 * @parameter url
		 */
		linkToOtherPage	: function(url) {
			$.ajax({
				url		: url,
				type	: "POST",
				success	: function(data){
					$("#marketMainList").html(data);
				}
			});
		},
		/**
		 * 更换左上角图片
		 */
		changeTitleIcon	: function(iconName) {
			$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon0");
//			$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx 
//					+ '/aibi_ci/market/assets/images/' + iconName + ') no-repeat center center');
		},
		/**
		 * 为营销任务添加选中样式
		 */
		addMenuHover	: function(marketTaskId) {
			$('#parentMarketTask_' + marketTaskId).addClass('market-group-item-hover');
		},
		/**
		 * 更换左上角文本
		 */
		changeTitleText	: function(txt) {
			$('#marketTaskTitle').html(txt);
		},
		/**
		 * 首页搜索 
		 * @parameter seachTypeId 类型：1标签 2用户群 
		 * @parameter keyWord 搜索的关键字
		 */
		indexTopSearch : function(seachTypeId,keyWord){
			if($.isNotBlank(seachTypeId) && $.isNotBlank(keyWord)){
				keyWord = decodeURIComponent(keyWord);
				$("#keyword").val(keyWord);
				$("#keyword").attr("data-val","true");
				$("#keyword").css("color","#232323")
			}				
			//切换到新集市列表
			if(seachTypeId == '1'){
				$.ajax({
					url		: $.ctx + "/core/labelMarketAction!findEffectiveLabelIndex.ai2do",
					type	: "POST",
					data	:{"ciLabelInfo.labelName":keyWord},
					success	: function(data){
						$("#marketMainList").html(data);
						$(".market-group-item-hover").removeClass("market-group-item-hover");
						$("#labelInfoIndex").addClass("market-group-item-hover");
					}
				});
			}else{
				$.ajax({
					url		: $.ctx + "/core/ciCustomerMarketAction!customerMarketIndex.ai2do",
					type	: "POST",
					data	:{"ciCustomGroupInfo.customGroupName":keyWord},
					success	: function(data){
						$("#marketMainList").html(data);
					}
				});
			}
		},
		changeMarketPageLink : function(target,option){
			var defaults = {
				url			: '',
				ajaxType	: '',
				data		: '',
				successFunc	: function(){}
			};
			var options = $.extend(defaults,option);
			$(target).COClive('click', function() {
				$.ajax({
					url		: options.url,
					type	: options.ajaxType,
					data	: options.data,
					success	: options.successFunc
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
				url = $.ctx + '/core/ciMainAction!findLabelDetail.ai2do';
			} else if(type == 2) {//用户群
				url = $.ctx + '/core/ciMainAction!findCustomGroupDetail.ai2do';
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
		},
		/**
		 * 显示隐藏的用户群规则
		 */
		showRule : function(id) {
			var url =$.ctx + '/core/ciCustomerManagerAction!showRule.ai2do?ciCustomGroupInfo.customGroupId='+id;
			dialogUtil.create_dialog('queryGroupRuleDialog', {
				'title' 		:  '用户群的完整规则',
				'height'		: 'auto',
				'width' 		: 800,
				'frameSrc' 		: url,
				'frameHeight'	: 400,
				'position' 		: ['center','center'] 
			}).focus(); 
		},
		/**
		 * 隐藏左侧菜单
		 */
		setleftMenu : function(){
			var width = $(window).width();
			if($(".J_left_menus").length>0 ){
				if(width < 1250){
					var leftObj = $(".J_left_menus").hide();
					 var left = leftObj.offset().left;
					 $(".show-menu-icon").css({left:left}).show();
					 $(".J_market_content > div").css({margin:"0 0 0 20px"});
					 
				}else{
					$(".show-menu-icon").hide();
					$(".J_left_menus").show();
					 $(".J_market_content > div").removeAttr("style");
				}
			}
				
		},
		/**
		 * 初始化 营销任务二级菜单功能链接
		 */
		initMarketTaskMenu : function() {
			var marketTaskMenu = $('.J_market-task-menu');
			for(var i=0; i<marketTaskMenu.length; i++) {
				var _data = $(marketTaskMenu[i]).attr('data-attr');
				var parentIcon = $(marketTaskMenu[i]).attr('icon');
				var pId = $(marketTaskMenu[i]).attr('id');
				var menuList = [];
				if(_data) {
					var data = eval('(' + _data + ')');
					for(var j=0; j<data.length; j++) {
						var name = data[j].marketTaskName;
						var id = data[j].marketTaskId;
						var obj = {
							name		: name,
							id			: id, 
							parentIcon	: parentIcon,
							pId			: pId,
							click		: function() {
								var ths = this;
								$.ajax({
									url		: $.ctx + "/ci/ciMarketAction!findMarketIndex.ai2do?customLabelSceneRel.marketTaskId=" + ths.id,
									type	: "POST",
									parentIcon	: ths.parentIcon,
									success	: function(data){
										var menuIdMarket = $(ths).attr('pId');
										$(".market-group-item-hover").removeClass("market-group-item-hover");
										if(!$("#"+menuIdMarket).hasClass("market-group-item-hover")){
											$("#"+menuIdMarket).addClass("market-group-item-hover");
										}
										//1.改变坐上角标题 更换一级任务图标
										$('#marketTaskTitle').html('营销视图');
										$("#menu-top-icon").removeClass().addClass("menu-top-icon market-navigation-icon").addClass("menu-top-icon0");
//										$('#firstMarketTaskIcon').css('background', 'url(' + $.ctx + '/aibi_ci/market/assets/images/' 
//												+ $(ths).attr('parentIcon') + ') no-repeat center center');
										//2.刷新中间列表
										$("#marketMainList").html(data);
										
										//轨迹跟踪 主页切换二级营销任务
										try{
											if(window.WebTrends){
												    Webtrends.multiTrack('WT.busi','YXDH','WT.oper','MKT_TASK_LEVEL2_HOME','WT.obj',ths.id);
											}
										}catch(e){
										    alert(e);
										}
									}
								});
							}
						}
						menuList.push(obj);
					}
					$(marketTaskMenu[i]).hoverShowMenu({
						menuList	: [menuList]
					})
				}
			}
		},
		/**
		 * 标签分类菜单分级展示
		 */
		loadLabelMenus :function() {
			var key ={
					name:"labelName",
					id:"labelId"
				}; 
			var menuList = $(".J_group_menus .market-group-item-no-dot");  
			for(var i = 0,len = menuList.length;i < len;i++){
				var _data = $(menuList[i]).attr("data-attr");
				//二级三级分类转JSON
				var resultData = eval("(" +_data+")");
				var option ={ 
						click:function(event){
							var categoriesId = this.id;
							var labelName = this.title;
							var para = {
								"pojo.categoriesId":categoriesId,
								"pid":event.data.firstCategoryId,
								"labelName":labelName
							};
							coreMain.labelMenuClick(para);
						},
						menuList:{data: resultData} 
				    };
				$(menuList[i]).tagNavMap(key,option);
			} 
		},
		/**
		 * 点击二三级标签分类，展现对应的标签列表
		 */
		labelMenuClick:function(para){
			$(".market-group-item-hover").removeClass("market-group-item-hover"); 
			$.blockUI({message: "<h1 style='margin: 5px;'>列表加载中...</h1>"}); //添加遮罩层
			$.ajax({
				url		: $.ctx + "/core/labelMarketAction!findEffectiveLabelIndex.ai2do",
				type	: "POST",
				data	: para,
				success	: function(data){
					$("#marketMainList").html(data);
					$("#marketMapMenus").remove();
					$("#"+para.pid).addClass("market-group-item-hover");
				},
			   	complete: function() {
			   		$.unblockUI(); 
			   	},
			   	stop: function() {
			   		$.unblockUI(); 
			   	}
			});
		},
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * 加入购物车
		 * @id 标签、用户群或者模板的ID
		 * @typeId 类型：1、标签，2、用户群，3、模板
		 * @defaultOp 操作类型:and,or,- ;没有值为为空字符串，为空时后台默认是and
		 * @isEditCustomFlag 是否是修改操作，1、修改操作，0、保存操作
		 */
		addShopCart	: function(id,typeId,isEditCustomFlag,defaultOp) {
			if(typeId == 2) {
				var msg = $.ajax({
					url		: $.ctx+'/ci/ciIndexAction!findCusotmValidate.ai2do',
					data	: 'ciCustomGroupInfo.customGroupId=' + $.trim(id),
					async	: false
				}).responseText;
				var msgObj = $.parseJSON(msg);
				var success = msgObj.success;
				if(!success) {
					$.fn.weakTip({"tipTxt":msgObj.msg});
					return;
				}
			}
			
			var actionUrl=$.ctx+"/core/ciMainAction!saveShopSession.ai2do";
			var para={
					"pojo.calculationsId"	: $.trim(id),
					"pojo.typeId"			: typeId,
					"pojo.isEditCustomFlag"	: isEditCustomFlag,
					"pojo.defaultOp"        : defaultOp
			};
			$.post(actionUrl, para, function(result){
				if (result.success){
					coreMain.refreshShopCart();
				}else{
					showAlert(result.msg, "failed");
				}
			});
		},
		/**
		 * 获取购物车计算元素个数 不包含子计算元素
		 */
		getShopCartListSize	: function() {
			return $('.J_shopCart_rule').length;
		},
		/**
		 * 删除购物车元素
		 * @parameter calculationsSort 购物车内的排序序号
		 */
		deleteShopCart	: function(calculationsSort) {
			var actionUrl=$.ctx+"/core/ciMainAction!delShopSession.ai2do";
			var para={"pojo.calculationsSort":calculationsSort};
			
			$.post(actionUrl, para, function(result){
				if (result.success){
					coreMain.refreshShopCart();
				}else{
					showAlert(result.msg,"failed");
				}
			});
		},
		/**
		 * 清空购物车
		 */
		clearShopCart	: function() {
			var num = coreMain.getShopCartListSize();
			if(num == 0) {return;}
			commonUtil.create_alert_dialog("clearShopCartConfirmDialog", {
				 "txt"		: "确定清空？",
				 "type"		: "confirm",
				 "width"	: 500,
				 "height"	: 200
			}, function() {
				var clearCarShopItemUrl = $.ctx + '/ci/ciIndexAction!delAllCarShopSession.ai2do';
				$.post(clearCarShopItemUrl, {}, function(result) {
					coreMain.refreshShopCart();
				});
			}) ;
		},
		/**
		 * 打开计算中心
		 */
		openCalculateCenter	: function() {
			var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1";
			var  form = $("<form></form>")
	        form.attr('action',url)
	        form.attr('method','post')
	        var input = $("<input type='hidden' name='url' />")
	        var thisUrl = location.href;
	        input.attr('value', thisUrl)
	        
			form.append(input)
	        form.appendTo("body")
	        form.css('display','none')
	        form.submit()
			//window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		},
		/**
		 * 判断购物车元素个数是否超出限定个数
		 * @return 超出：true 未超出： false 
		 */
		isOverLength	: function(limitNum) {
			var flag = false;
			if(coreMain.calcuElementNum() > limitNum) {
				flag = true;
			}
			return flag;
		},
		/**
		 * 购物车内标签和用户群的个数 包含子计算元素
		 */
		calcuElementNum	: function() {
			var num = 0;
			var ruleElements = $('.J_shopCart_rule');
			for(var i=0; i<ruleElements.length; i++) {
				var settingEl = $(ruleElements[i]).children('.J_shopCart_rule_div').children('.J_setting');
				var elementType = settingEl.attr('elementType');
				if(elementType == 6) {
					var children = settingEl.children('.child');
					for(var j=0; j<children.length; j++) {
						var childElementType = $(children[j]).attr('elementType');
						if(childElementType == 2 || childElementType == 5) {
							num ++;
						}
					}
				} else {
					num ++;
				}
				
			}
			return num;
		},
		/**
		 * 枚举文本标签选择个数是否超出规定个数
		 */
		isEnumOrTextLabelOverLength : function(limitCount) {
			var result = false; 
			$('.J_shopCart_ul .J_setting[labelTypeId="5"],.J_shopCart_ul .J_setting[labelTypeId="7"]').each(function(index, item) {
				var labelTypeId = $(item).attr('labelTypeId');
				var str = '';
				if(labelTypeId == 5) {
					str = $(item).attr('attrVal');
				} else if(labelTypeId == 7) {
					str = $(item).attr('exactValue');
				}
				
				if($.isNotBlank(str) && str.split(",").length > limitCount){
					result = true;
					return false;
				}
			});
			
			$('.J_shopCart_ul .J_setting[labelTypeId="8"]').each(function(index, item) {
				var children = $(item).children('.child');
				for(var i=0; i<children.length; i++) {
					var child = $(children[i]);
					var labelTypeId = child.attr('labelTypeId');
					
					var str = '';
					if(labelTypeId == 5) {
						str = child.attr('attrVal');
					} else if(labelTypeId == 7) {
						str = child.attr('exactValue');
					}
					if($.isNotBlank(str) && str.split(",").length > limitCount){
						result = true;
						break;
					}
				}
			})
			
			$('.J_shopCart_ul .J_setting[elementtype="6"]').each(function(index, item) {
				var children = $(item).children('.child');
				for(var i=0; i<children.length; i++) {
					var child = $(children[i]);
					var labelTypeId = child.attr('labelTypeId');
					
					var str = '';
					if(labelTypeId == 5) {
						str = child.attr('attrVal');
					} else if(labelTypeId == 7) {
						str = child.attr('exactValue');
					} else if(labelTypeId == 8) {
						var grandson = child.children('.grandson');
						for(var j=0; j<grandson.length; j++) {
							var gs = $(grandson[j]);
							var grandsonLabelTypeId = gs.attr('labelTypeId');
							
							var s = '';
							if(grandsonLabelTypeId == 5) {
								s = gs.attr('attrVal');
							} else if(grandsonLabelTypeId == 7) {
								s = gs.attr('exactValue');
							}
							if($.isNotBlank(s) && s.split(",").length > limitCount){
								result = true;
								break;
							}
						}
					}
					
					if($.isNotBlank(str) && str.split(",").length > limitCount || result){
						result = true;
						break;
					}
				}
			})
			return result;
		},
		/**
		 * 遍历购物车属性是否设置完毕
		 */
		attrCompleted	: function() {
			var result = true;
			$('.J_shopCart_rule .J_show_customer_detail').each(function(index, item) {
				var labelTypeId = $(item).attr('labelTypeId');
				if(labelTypeId != 1) {
					var ruleText = $.trim($(item).html());
					if(!$.isNotBlank(ruleText)) {
						result = false;
						return false
					}
				}
			});
			return result;
		},
		/**
		 * 判断标签是否具有最新数据日期的数据
		 */
		haveNewestData	: function() {
			var result = true;
			$('.J_shopCart_ul .J_setting').each(function(index, item) {
				var elementType = $(item).attr('elementType');
				if(elementType == 2) {
					var updateCycle = $(item).attr('updateCycle');
					var dataDate = $(item).attr('dataDate');
					
					var newLabelDay = $('#newLabelDayInput').val();
					var newLabelMonth = $('#newLabelMonthInput').val();
					if(updateCycle == 1) {//日周期
						if(newLabelDay > dataDate) {
							result = false;
						}
					} else if(updateCycle == 2) {//月周期
						if(newLabelMonth > dataDate) {
							result = false;
						}
					}
				} else if(elementType == 6) {
					var children = $(item).children('.child');
					for(var i=0; i<children.length; i++) {
						var child = $(children[i]);
						var childElemenType = child.attr('elementType');
						if(childElemenType == 2) {
							var updateCycle = child.attr('updateCycle');
							var dataDate = child.attr('dataDate');
							
							var newLabelDay = $('#newLabelDayInput').val();
							var newLabelMonth = $('#newLabelMonthInput').val();
							if(updateCycle == 1) {//日周期
								if(newLabelDay > dataDate) {
									result = false;
								}
							} else if(updateCycle == 2) {//月周期
								if(newLabelMonth > dataDate) {
									result = false;
								}
							}
						}
					}
				}
				if(!result) {
					return false;
				}
			});
			return result;
		},
		/**
		 * 购物车是否包含用户群或者纵表标签
		 */
		haveCustomOrVerticalLabel	: function() {
			var result = false;
			$('.J_shopCart_ul .J_setting').each(function(){
				if($(this).attr('labelTypeId') == 8 || $(this).attr('elementType') == 5 || $(this).attr('elementType') == 6){//纵表标签和用户群
					result= true;
					return false;
				}
			});
			return result;
		},
		/**
		 * 探索验证，并调用探索
		 */
		validateAndExplore	: function() {
			var _EnumCount = 100;//枚举类型最多选的个数
			//if($('#customGroupExploreBtn').hasClass('market-cart-button-disable')) {return;}
			if($('#customGroupExploreBtn').is(":hidden")) {return;}
			
			//判断选择的计算元素个数是否超过限制
			if(coreMain.isOverLength($._maxLabelNum)) {
				coreMain.alertFailed('isOverLength', '计算元素不能超过' + $._maxLabelNum + '个！');
				return;
			}
			
			//枚举 精确值 数量不能超过100个
			if(coreMain.isEnumOrTextLabelOverLength(_EnumCount)) {
				coreMain.alertFailed('isEnumOrTextLabelOverLength', '配置的条件中值超过' + _EnumCount + '个，可以保存用户群后查看用户数！');
				return;
			}
			
			//有计算有元素没有设置属性
			if(!coreMain.attrCompleted()) {
				coreMain.alertFailed('attrCompleted', '计算元素没有设置属性，无法进行计算！');
				return;
			} 
			//判断标签是否有数据
			if(!coreMain.haveNewestData()) {
				coreMain.alertFailed('haveNoDataDate', '标签数据对应最新数据日期没有数据，无法进行规模探索！');
				return;
			}
			
			//验证sql
			var validateSqlUrl = $.ctx + '/core/ciMainAction!validateSql.ai2do';
			var sign = true;
			$.ajax({
				url		: validateSqlUrl,
				type	: 'POST',
				async	: false,//同步
				data	: $('#shopCartForm').serialize(), 
				success	: function(result){
					flag = result.success;
					if(!flag){
						sign = false;
						coreMain.alertFailed('validateSql', result.msg);
					}
				}
			});
			if(!sign) {return};
			
			coreMain.explore();
		},
		/**
		 * 探索
		 */
		explore	: function() {
			var actionUrl = $.ctx + '/core/ciMainAction!explore.ai2do';
			//探索加遮罩
			$('#marketCart').prepend('<div class="market-cart-list-overlay"><div class="loading">正在加载，请稍后···</div></div>');
			//探索按钮置灰
			//('#customGroupExploreBtn').addClass('market-cart-button-disable');
			//$('#customGroupExploreBtn').removeClass('market-cart-button');
			
			$.post(actionUrl, $('#shopCartForm').serialize(), function(result){
				//去除遮罩
				$('#marketCart').find('.market-cart-list-overlay').remove();
				//$('#customGroupExploreBtn').removeClass('market-cart-button-disable');
				//$('#customGroupExploreBtn').addClass('market-cart-button');
				
				if (result.success) {
					var num = result.msg;
					if($.isNotBlank(num)) {
						num = $.formatNumber(num, 3, ',');
						$('#customGroupNumSpan').html(num);
					} else {
						$('#customGroupNumSpan').html(0);
					}
				} else {
					showAlert(result.msg,"failed");
				}
			});
		},
		/**
		 * 重新刷新购物车页面
		 */
		refreshShopCart	: function() {
			$.ajax({
				url		: $.ctx + "/core/ciMainAction!findShopCart.ai2do",
				type	: "POST",
				success	: function(data){
					$("#shopCartList").html(data);
					coreMain.settingStyle();
					$('#shopCartCalcuItemNum').text(coreMain.getShopCartListSize());
					//购物车滚动条
					$("#marketCart").mCustomScrollbar({
						theme				: "minimal-dark", //滚动条样式
						scrollbarPosition	: "inside", //滚动条位置
						scrollInertia		: 500,
						axis				: "y",
						mouseWheel			: {
							preventDefault	: true //阻止冒泡事件 
						}
					});
				}
			});
		},
		/**
		 * 遍历计算元素设置属性齿轮样式（设置完之后需要齿轮需要加对号）
		 */
		settingStyle : function() {
			$('.J_shopCart_rule .J_show_customer_detail').each(function(index, item) {
				var ruleText = $.trim($(item).html());
				if(ruleText != '') {//market-cart-tag-setting-red
					$(item).parent().children().children('.J_setting').removeClass('market-cart-tag-new-setting').addClass('market-cart-tag-new-setting');;
				} else {
					$(item).parent().children().children('.J_setting').removeClass('market-cart-tag-new-setting')
						.removeClass('market-cart-tag-new-setting-active').addClass('market-cart-tag-setting-red');;
				}
			});
		},
		/**
		 * 设置完属性之后提交session
		 * 单个session提交，因为括号在页面上不显示 没法提交到后台
		 * @parameter sort 计算元素总体序号
		 */
		submitShopCartSession	: function(sort) {
			var el;
			var html = '';
			$('.J_shopCart_ul .J_setting').each(function(index, item) {
				if($(item).attr('sort') == sort) {
					el = $(item);
					return false;
				}
			})
			if(!el){coreMain.alertFailed('updateSessionException', '提交session异常');return;}
			
			var elementType = el.attr('elementType');
			var customOrLabelName 	= el.attr('customOrLabelName');
			var calcuElemnt = '';
			if(elementType == 2) {
				calcuElement		= el.attr('calcuElement');
				var labelFlag		= el.attr('labelFlag');
				var minVal			= el.attr('minVal');
				var maxVal			= el.attr('maxVal');
				var effectDate		= el.attr('effectDate');
				var labelTypeId 	= el.attr('labelTypeId');
				var attrVal 		= el.attr('attrVal');
				var startTime 		= el.attr('startTime');
				var endTime 		= el.attr('endTime');
				var contiueMinVal 	= el.attr('contiueMinVal');
				var contiueMaxVal 	= el.attr('contiueMaxVal');
				var leftZoneSign 	= el.attr('leftZoneSign');
				var rightZoneSign 	= el.attr('rightZoneSign');
				var exactValue 		= el.attr('exactValue');
				var darkValue 		= el.attr('darkValue');
				var attrName		= el.attr('attrName');
				var queryWay		= el.attr('queryWay');
				var unit			= el.attr('unit');
				var customCreateTypeId = el.attr('customCreateTypeId');
				var dataDate        = el.attr('dataDate');
				var updateCycle     = el.attr('updateCycle');
				var isNeedOffset	= el.attr('isNeedOffset');
				
				html += '<input type="hidden" name="ciLabelRuleList[0].labelTypeId" value="' + labelTypeId + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].attrVal" value="' + attrVal + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].startTime" value="' + startTime + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].endTime" value="' + endTime + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].contiueMinVal" value="' + contiueMinVal + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].contiueMaxVal" value="' + contiueMaxVal + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].leftZoneSign" value="' + leftZoneSign + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].rightZoneSign" value="' + rightZoneSign + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].exactValue" value="' + exactValue + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].darkValue" value="' + darkValue + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].effectDate" value="' + effectDate + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].labelFlag" value="' + labelFlag + '"/>' + 
						'<input type="hidden" name="ciLabelRuleList[0].attrName" value="' + attrName + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].queryWay" value="' + queryWay + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].minVal" value="' + minVal + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].maxVal" value="' + maxVal + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].unit" value="' + unit + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].customCreateTypeId" value="' + customCreateTypeId + '"/>'+
						'<input type="hidden" name="ciLabelRuleList[0].dataDate" value="' + dataDate + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].updateCycle" value="' + updateCycle + '"/>' +
						'<input type="hidden" name="ciLabelRuleList[0].isNeedOffset" value="' + isNeedOffset + '"/>';
				if(labelTypeId == 8) {
					var childs = el.children('.child');
					for(var index=0; index<childs.length; index++) {
						var ele				= $(childs[index]);
						var labelFlag		= ele.attr('labelFlag');
						var calcE		 	= ele.attr('calcuElement');
						var minVal			= ele.attr('minVal');
						var maxVal			= ele.attr('maxVal');
						var effectDate		= ele.attr('effectDate');
						var lti 			= ele.attr('labelTypeId');
						var attrVal 		= ele.attr('attrVal');
						var startTime 		= ele.attr('startTime');
						var endTime 		= ele.attr('endTime');
						var contiueMinVal 	= ele.attr('contiueMinVal');
						var contiueMaxVal 	= ele.attr('contiueMaxVal');
						var leftZoneSign 	= ele.attr('leftZoneSign');
						var rightZoneSign 	= ele.attr('rightZoneSign');
						var exactValue 		= ele.attr('exactValue');
						var darkValue 		= ele.attr('darkValue');
						var attrName		= ele.attr('attrName');
						var queryWay		= ele.attr('queryWay');
						var columnCnName	= ele.attr('columnCnName');
						var childUnit		= ele.attr('unit');
						var dataDate        = ele.attr('dataDate');
						var updateCycle     = ele.attr('updateCycle');
						var isNeedOffset	= ele.attr('isNeedOffset');
						
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].labelTypeId" value="' + lti + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].attrVal" value="' + attrVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].startTime" value="' + startTime + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].endTime" value="' + endTime + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].contiueMinVal" value="' + contiueMinVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].contiueMaxVal" value="' + contiueMaxVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].leftZoneSign" value="' + leftZoneSign + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].rightZoneSign" value="' + rightZoneSign + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].exactValue" value="' + exactValue + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].darkValue" value="' + darkValue + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].effectDate" value="' + effectDate + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].labelFlag" value="' + labelFlag + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].attrName" value="' + attrName + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].queryWay" value="' + queryWay + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].minVal" value="' + minVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].calcuElement" value="' + calcE + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].columnCnName" value="' + columnCnName + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].unit" value="' + childUnit + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].elementType" value="2"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].sortNum" value="' + index + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].maxVal" value="' + maxVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].dataDate" value="' + dataDate + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].updateCycle" value="' + updateCycle + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].isNeedOffset" value="' + isNeedOffset + '"/>';
					}
				}
			} else if(elementType == 5) {
				var attrVal 		= el.attr('attrVal');
				var customId 		= el.attr('customId');
				var dataCycle       = el.attr('dataCycle');
				calcuElement 		= el.attr('calcuElement');
				
				html += '<input type="hidden" name="ciLabelRuleList[0].attrVal" value="' + attrVal + '"/>';
				html += '<input type="hidden" name="ciLabelRuleList[0].customId" value="' + customId + '"/>';
				html += '<input type="hidden" name="ciLabelRuleList[0].labelFlag" value="' + dataCycle + '"/>';
			} else if(elementType == 6) {
				var attrVal 		= el.attr('attrVal');
				var customId 		= el.attr('customId');
				var dataCycle       = el.attr('dataCycle');
				calcuElement 		= el.attr('calcuElement');
				
				html += '<input type="hidden" name="ciLabelRuleList[0].attrVal" value="' + attrVal + '"/>';
				html += '<input type="hidden" name="ciLabelRuleList[0].customId" value="' + customId + '"/>';
				html += '<input type="hidden" name="ciLabelRuleList[0].labelFlag" value="' + dataCycle + '"/>';
				
				var childs = el.children('.child');
				
				// 需要区分标签、用户群、运算符和括号
				for(var index=0; index<childs.length; index++) {
					var child 	= $(childs[index]);
					var et		= child.attr('elementType');
					
					if(et == 2) {
						var calElement		= child.attr('calcuElement');
						var labelFlag		= child.attr('labelFlag');
						var minVal			= child.attr('minVal');
						var maxVal			= child.attr('maxVal');
						var effectDate		= child.attr('effectDate');
						var labelTypeId 	= child.attr('labelTypeId');
						var attrVal 		= child.attr('attrVal');
						var startTime 		= child.attr('startTime');
						var endTime 		= child.attr('endTime');
						var contiueMinVal 	= child.attr('contiueMinVal');
						var contiueMaxVal 	= child.attr('contiueMaxVal');
						var leftZoneSign 	= child.attr('leftZoneSign');
						var rightZoneSign 	= child.attr('rightZoneSign');
						var exactValue 		= child.attr('exactValue');
						var darkValue 		= child.attr('darkValue');
						var attrName		= child.attr('attrName');
						var queryWay		= child.attr('queryWay');
						var unit			= child.attr('unit');
						var customCreateTypeId = child.attr('customCreateTypeId');
						var dataDate        = child.attr('dataDate');
						var updateCycle     = child.attr('updateCycle');
						var isNeedOffset	= child.attr('isNeedOffset');
						var clName 			= child.attr('customOrLabelName');
						
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].labelTypeId" value="' + labelTypeId + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].elementType" value="' + et + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].customOrLabelName" value="' + clName + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].calcuElement" value="' + calElement + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].attrVal" value="' + attrVal + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].startTime" value="' + startTime + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].endTime" value="' + endTime + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].contiueMinVal" value="' + contiueMinVal + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].contiueMaxVal" value="' + contiueMaxVal + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].leftZoneSign" value="' + leftZoneSign + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].rightZoneSign" value="' + rightZoneSign + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].exactValue" value="' + exactValue + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].darkValue" value="' + darkValue + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].effectDate" value="' + effectDate + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].labelFlag" value="' + labelFlag + '"/>' + 
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].attrName" value="' + attrName + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].queryWay" value="' + queryWay + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].minVal" value="' + minVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].maxVal" value="' + maxVal + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].unit" value="' + unit + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].customCreateTypeId" value="' + customCreateTypeId + '"/>'+
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].dataDate" value="' + dataDate + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].updateCycle" value="' + updateCycle + '"/>' +
								'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].isNeedOffset" value="' + isNeedOffset + '"/>';
						if(labelTypeId == 8) {
							var grandson = child.children('.grandson');
							for(var j=0; j<grandson.length; j++) {
								var ele				= $(grandson[j]);
								var labelFlag		= ele.attr('labelFlag');
								var calcEl		 	= ele.attr('calcuElement');
								var minVal			= ele.attr('minVal');
								var maxVal			= ele.attr('maxVal');
								var effectDate		= ele.attr('effectDate');
								var lti 			= ele.attr('labelTypeId');
								var attrVal 		= ele.attr('attrVal');
								var startTime 		= ele.attr('startTime');
								var endTime 		= ele.attr('endTime');
								var contiueMinVal 	= ele.attr('contiueMinVal');
								var contiueMaxVal 	= ele.attr('contiueMaxVal');
								var leftZoneSign 	= ele.attr('leftZoneSign');
								var rightZoneSign 	= ele.attr('rightZoneSign');
								var exactValue 		= ele.attr('exactValue');
								var darkValue 		= ele.attr('darkValue');
								var attrName		= ele.attr('attrName');
								var queryWay		= ele.attr('queryWay');
								var columnCnName	= ele.attr('columnCnName');
								var childUnit		= ele.attr('unit');
								var dataDate        = ele.attr('dataDate');
								var updateCycle     = ele.attr('updateCycle');
								var isNeedOffset	= ele.attr('isNeedOffset');
								
								html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].labelTypeId" value="' + lti + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].attrVal" value="' + attrVal + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].startTime" value="' + startTime + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].endTime" value="' + endTime + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].contiueMinVal" value="' + contiueMinVal + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].contiueMaxVal" value="' + contiueMaxVal + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].leftZoneSign" value="' + leftZoneSign + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].rightZoneSign" value="' + rightZoneSign + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].exactValue" value="' + exactValue + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].darkValue" value="' + darkValue + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].effectDate" value="' + effectDate + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].labelFlag" value="' + labelFlag + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].attrName" value="' + attrName + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].queryWay" value="' + queryWay + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].minVal" value="' + minVal + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].calcuElement" value="' + calcEl + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].columnCnName" value="' + columnCnName + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].unit" value="' + childUnit + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].elementType" value="2"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].sortNum" value="' + index + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].maxVal" value="' + maxVal + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].dataDate" value="' + dataDate + '"/>' + 
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].updateCycle" value="' + updateCycle + '"/>' +
										'<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].childCiLabelRuleList[' + j + '].isNeedOffset" value="' + isNeedOffset + '"/>';
							}
						}
					} else if(et == 5) {
						var attrVal 		= child.attr('attrVal');
						var customId 		= child.attr('customId');
						var dataCycle       = child.attr('dataCycle');
						var cElement 		= child.attr('calcuElement');
						var clName = child.attr('customOrLabelName');
						
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].calcuElement" value="' + cElement + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].attrVal" value="' + attrVal + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].customOrLabelName" value="' + clName + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].customId" value="' + customId + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].labelFlag" value="' + dataCycle + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].elementType" value="' + et + '"/>';
					} else if(et == 1) {//处理操作符
						var calElement = child.attr('calcuElement');
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].calcuElement" value="' + calElement + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].elementType" value="' + et + '"/>';
					} else if(et == 3) {//处理括号
						var creat 		= child.attr('creat');
						var calcuEle 	= child.attr('calcuElement');
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].createBrackets" value="' + creat + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].calcuElement" value="' + calcuEle + '"/>';
						html += '<input type="hidden" name="ciLabelRuleList[0].childCiLabelRuleList[' + index + '].elementType" value="' + et + '"/>';
					}
				}
			}
			html += '<input type="hidden" name="ciLabelRuleList[0].customOrLabelName" value="' + customOrLabelName + '"/>' +
				'<input type="hidden" name="ciLabelRuleList[0].calcuElement" value="' + calcuElement + '"/>' +
				'<input type="hidden" name="ciLabelRuleList[0].sortNum" value="' + sort + '"/>' +
				'<input type="hidden" name="ciLabelRuleList[0].elementType" value="' + elementType + '"/>';
			$("#updateSessionDiv").html(html);
			var url = $.ctx + "/core/ciMainAction!updateSession.ai2do";
			var data = $('#updateSessionForm').serialize();
			$.ajax({
				 url		: url,
		         type		: 'post',
		         async		: false,//同步
		         data		: 'sort=' + sort + '&' + $("#updateSessionForm").serialize(),
		         success	: function (data) {
		        	 $("#updateSessionDiv").empty();
		        	 coreMain.refreshShopCart();
		         }
			});
		},
		///////////////////////////////////////////////////////////////////////////////////
		/**
		 * 失败提示框
		 */
		alertFailed	: function(target, txt) {
			commonUtil.create_alert_dialog('overLengthDialog', {
				txt		: txt,
				type	: 'failed',   
				width	: 500,
				height	: 200
			});
		},
		/**
		 * 用户群标签推荐
		 */
		setRecomLabelOrCustom : function(target,showType){
			$(target).COClive("click",function(e) {
				var useTimes = $(this).attr("useTimes");
				var actionUrl = "";
				var title = "";
				if(showType == 2){
					var customId = $(this).attr("customId");
					title = "用户群推荐设置";
					actionUrl = $.ctx + "/core/customGroupOperateAction!recomLabelOrCustomPage.ai2do?customLabelSceneRel.customGroupId="+customId+"&customLabelSceneRel.useTimes="+useTimes
					+"&customLabelSceneRel.showType="+showType;
				}else{
					var labelId = $(this).attr("labelId");
					title = "标签推荐设置";
					actionUrl = $.ctx + "/core/customGroupOperateAction!recomLabelOrCustomPage.ai2do?customLabelSceneRel.labelId="+labelId+"&customLabelSceneRel.useTimes="+useTimes
					+"&customLabelSceneRel.showType="+showType;
				}
				var dlgObj = dialogUtil.create_dialog("setRecomLabelOrCustomDialog", {
					"title" : title,
					"height": "auto",
					"width" : 600,
					"frameSrc" : actionUrl,
					"frameHeight" : 210,
					"position" : ['center','center'] 
				});
			});
		},
		/**
		 * 模拟用户群推荐页面下拉框
		 */
		simulationComboBox : function(target){
			$(target).COClive("click",function(e) {
				var selVal = $(this).attr("value");
				var selHtml = $(this).html();
				$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
				$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
				//向隐藏域中传递值
				$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
				$(this).parents(".querySelListBox").slideUp();
				return false;
			});
		},
		simulationComboEvent : function(target){
			$(target).COClive("click",function(e) {
				if($(this).parent().hasClass("J_first_level_task_flag")){
					//清除之前选择的二级营销任务、场景
					$(".J_second_level_task_show").html("请选择二级营销任务");
					$(".J_second_first_level_scene_show").html("请选择场景分类");
					$("#secondLevelTask").val("");
					$("#firstLevelScene").val("");
				}
				if($(this).parent().hasClass("J_second_level_task_flag")){
					//清除之前选择的场景
					$(".J_second_first_level_scene_show").html("请选择场景分类");
					$("#firstLevelScene").val("");
				}
				//下拉框互斥 start
				var selfIndex;
				if($(this).hasClass("selBtn")){
					selfIndex = $(".selBtn").index((this));
				}else{
					selfIndex = $(".selTxt").index((this));
				}
				$.each($(".selBtn"),function(index,item){
					if(index != selfIndex){
						$(this).next(".querySelListBox").slideUp();
					}
				});
				$.each($(".selTxt"),function(index,item){
					if(index != selfIndex){
						$(this).next(".querySelListBox").slideUp();
					}
				});
				//下拉框互斥 end
				
				$("#firstLevelTaskTip").hide();
				$("#secondLevelTaskTip").hide();
				$("#firstLevelSceneTip").hide();
				//一级营销任务没有选择 则不能选择二级营销任务、一级场景分类、二级场景分类
				//二级营销任务没有选择 则不能选择一级场景分类、二级场景分类
				//一级场景分类没有选择 则不能选择二级场景分类
				var firstLevelTaskFlag = !!($("#firstLevelTask").val().length == 0);
				var secondLevelTaskFlag = !!($("#secondLevelTask").val().length == 0);
				var firstLevelSceneFlag = !!($("#firstLevelScene").val().length == 0);
				if($(this).hasClass("secondLevelTask")){
					if(firstLevelTaskFlag){
						commonUtil.create_alert_dialog("showAlertDialog", {
							"txt":"请先选择一级营销任务",
							"type":"failed",   
							"width":500,
							"height" :200
						}); 
						return;
					}else{
						var firstLevelTaskVal = $("#firstLevelTask").val();
						var actionUrl = $.ctx + "/core/customGroupOperateAction!findSecondTasks.ai2do";
						$.ajax({
							url: actionUrl,
							type: "POST",
							data: {
								"taskId" : firstLevelTaskVal
							},
							success: function(result){
								flag = result.success;
								if(flag){
									var secondLevelTasks = result.secondLevelTasks;
									//清空之前的二级营销任务列表
									$("#secondLevelTaskList").empty();
									for(var index=0; index<secondLevelTasks.length; index++) {
										var marketTaskId = secondLevelTasks[index].marketTaskId;
										var marketTaskName = secondLevelTasks[index].marketTaskName;
										//拼二级营销任务下拉框
										var secondLevelTask = "<a href='javascript:void(0);' value='"+marketTaskId+"'>"+marketTaskName+"</a>";
										$("#secondLevelTaskList").append(secondLevelTask)
									}
									$("#secondLevelTaskList a").click(function(){
										var selVal = $(this).attr("value");
										var selHtml = $(this).html();
										$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
										$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
										$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
										//向隐藏域中传递值
										$(this).parents(".querySelListBox").slideUp();
										return false;
									});
								}else{
									commonUtil.create_alert_dialog("showAlertDialog", {
										"txt":"根据一级营销任务查询二级营销任务出错",
										"type":"failed",   
										"width":500,
										"height" :200
									}); 
								}
							}
						});
					}
				}else if($(this).hasClass("firstLevelScene")){
					if(secondLevelTaskFlag){	
						commonUtil.create_alert_dialog("showAlertDialog", {
							"txt":"请先选择二级营销任务",
							"type":"failed",   
							"width":500,
							"height" :200
						}); 
						return;
					}
					
				}
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
		 * 用户群推荐验证
		 */
		validateRecomForm : function(){
			var firstLevelTaskFlag = !!($("#firstLevelTask").val().length == 0);
			var secondLevelTaskFlag = !!($("#secondLevelTask").val().length == 0);
			var firstLevelSceneFlag = !!($("#firstLevelScene").val().length == 0);
			if(firstLevelTaskFlag){
				$("#firstLevelTaskTip").html("一级营销场景不允许为空！").show();
				return false;
			}
			if(secondLevelTaskFlag){
				$("#secondLevelTaskTip").html("二级营销场景不允许为空！").show();
				return false;
			}
			if(firstLevelSceneFlag){
				$("#firstLevelSceneTip").html("场景分类不允许为空！").show();
				return false;
			}
			return true;
		},
		/**
		 * 提交用户群推荐信息
		 */
		submitRecomForm : function(target){
			$(target).COClive("click",function(e) {
				if(coreMain.validateRecomForm()){
					$(this).attr("id","");
					var actionUrl = $.ctx + "/core/customGroupOperateAction!saveCustomOrLabelSceneRel.ai2do";
					$.post(actionUrl, $("#saveForm").serialize(), function(result){
						if(result.success){
							var showType = $("#showType").val();
							var labelCustomId = "";
							if(showType == "1"){
								labelCustomId = $("#recomLabelId").val();
							}else{
								labelCustomId = $("#recomCustomId").val();
							}
							var customLabelSign = parent.$("#custom_label_sign_"+labelCustomId)
							$.fn.weakTip({"tipTxt":"" +result.msg,"backFn":function(){
								if(showType == "2"){
									parent.$("#share_"+labelCustomId).hide();
								}
								var recomSignFlag = true;
								customLabelSign.find("a").each(function(){
									if($(this).hasClass("organ")){
										recomSignFlag = false;
										return;
									}
								});
								if(recomSignFlag){
									if(customLabelSign.find("li").length > 0){
										customLabelSign.prepend($("<li><a class=\"organ\"/></li>"));
									}else{
										customLabelSign.append($("<li><a class=\"organ\"/></li>"));
									}
								}
								parent.$("#setRecomLabelOrCustomDialog").dialog("close");
							 }});
						}else{
							commonUtil.create_alert_dialog("showAlertDialog", {
								"txt":result.msg,
								"type":"failed",   
								"width":500,
								"height" :200
							},function(){parent.$("#setRecomLabelOrCustomDialog").dialog("close");}); 
						}
					});
				}else{
					$(this).attr("id","saveBtn");
				}
			});
		},
		/**
		 * 用户群清单下载
		 * @parameter target （jquery对象：$('#xxx')）
		 */
		customListDown : function(target){
			$(target).COClive("click",function(e) {
				var customId = $(this).attr('customId');
				var ifmUrl = $.ctx + "/core/customGroupOperateAction!initCustomersTrendDetail.ai2do?customGroupId="+customId;
			    var dlgObj = dialogUtil.create_dialog("dialog_look", {
					"title" :  "用户群清单下载",
					"height": "auto",
					"width" : 980,
					"frameSrc" : ifmUrl,
					"frameHeight":376,
					"position" : ['center','center'] 
				});
			    
			});
		},
		notShowCustomNum : function(target){
			$(target).COClive("click",function() {
				$.fn.weakTip({"tipTxt":"抱歉不可操作！非本人创建用户群。" });			    
			});
		},
		/**
		 * 用户群加载趋势图
		 */
		loadCustomerChart : function(){
			$("#loading").fadeIn();
			var customGroupId = $('#customGroupId').val();
			var dataDate = $('#dataDate').val();
			var actionUrl=$.ctx+'/core/customGroupOperateAction!findCustomerTrendTable.ai2do?customGroupId='+customGroupId+'&dataDate='+dataDate;
			$('#aibiTableDiv').load(actionUrl,null,function(){
				$("#loading").fadeOut();
			});
		},
		/**
		 * 用户群加载下载列表
		 */
		loadCustomerTable : function(){
			var noData = '用户群暂无统计数据';
			var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
			var customGroupId = $('#customGroupId').val();
			var dataDate = $('#dataDate').val();
			var param='customGroupId='+customGroupId+'&dataDate='+dataDate;
			var chart_url = escape($.ctx+'/core/customGroupOperateAction!customersTrendChartData.ai2do?'+param);
			
			var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/Line.swf"+chartParam, "",$('#aibiChartDiv').width(), $('#aibiChartDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
			bar.setDataURL(chart_url);
			bar.render("aibiChartDiv");
		},
		/**
		 * 清单下载页面：显示趋势图、下载列表
		 */
		showCustomerChartAndDownList : function(){
			var _isSuccess = $("#isSuccess").val();
			var _msg = $("#msg").val();
			if(_isSuccess == 'false'){
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":_msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				});
				parent.CustomMarketMain.loadCustomList({
					url			: $.ctx + "/core/ciCustomerMarketAction!customersList.ai2do",
					data 		: $("#multiSearchForm").serialize(),
					ajaxType	: "POST",
					successFunc	: function(data){
						$("#customMarketList" , parent.document).html(data);
						$("#totalCustomNum" , parent.document).html($("#pageTotalSize" , parent.document).val());
						parent.CustomMarketMain.pageClick();
					}
				});
			}
			aibiTableChangeColor(".mainTable");
			$(".showTable .mainTable tr:last td").css("border-bottom","1px solid #c2bdbd");
			//用户群趋势分析图加载
			coreMain.loadCustomerChart();
			//客户趋势分析表格加载
			coreMain.loadCustomerTable();
		},
		/**
		 * 确认共享用户群
		 * @parameter target 触发事件的元素（jquery对象：$('#xxx')）
		 */
		confirmShare : function(options){
			$(options.target).COClive("click",function(e) {
				var customId = $(this).attr("customId");
				options.customId = customId;
				commonUtil.create_alert_dialog("confirmDialog", {
					"txt":"确认共享该用户群？",
					"type":"confirm",
					"width":500,
					"height":200,
					"param":options,
					isFunc:true
				},coreMain.shareCustomer);
			});
		},
		/**
		 * 确认取消共享用户群
		 * @parameter target 触发事件的元素（jquery对象：$('#xxx')）
		 */
		confirmCancelShare:function(options){
			$(options.target).COClive("click",function(e) {
				var customId = $(this).attr("customId");
				options.customId = customId;
				commonUtil.create_alert_dialog("confirmDialog", {
					"txt":"确认取消共享该用户群？",
					"type":"confirm",
					"width":500,
					"height":200,
					"param":options,
					isFunc:true
				},coreMain.cancelShareCustomer);
			});
		},
		/**
		 * 共享用户群
		 * @parameter customId 用户群ID 
		 */
		shareCustomer : function(options){
			var customId = options.customId;
			var cname=$("#share_"+customId).attr("c_name");
			var cityId=$("#share_"+customId).attr("c_cityId");
			var createUserId=$("#share_"+customId).attr("c_userId");
			var actionUrl=$.ctx + "/core/customGroupOperateAction!shareCustom.ai2do"
			var para={
					"customGroupInfo.customGroupId":customId,
				 	"customGroupInfo.customGroupName":cname,
				 	"customGroupInfo.createCityId":cityId,
				 	"customGroupInfo.createUserId":createUserId
				};
			$.post(actionUrl, para, function(result){
				if(result.success){
					$.fn.weakTip({"tipTxt":"用户群共享成功！","backFn":options.backFunc});
				}else{
					var msg = result.msg;
					if(msg.indexOf("重名") >= 0){
						var backFunc = options.backFunc;
						coreMain.customerShare({"customId":customId,"cname":cname,"cityId":cityId,"createUserId":createUserId,"msg":msg,"backFunc":backFunc});
					}else{
						commonUtil.create_alert_dialog("confirmDialog", {
							 "txt":msg,
							 "type":"failed",
							 "width":500,
							 "height":200
						}); 
					}
				}
			});
		},
		/**
		 * 取消用户群共享
		 * @parameter customId 用户群ID 
		 */
		cancelShareCustomer : function(options){
			var customId = options.customId;
			var actionUrl=$.ctx + "/core/customGroupOperateAction!cancelShareCustom.ai2do"
			var para={
					"customGroupInfo.customGroupId":customId
				};
			$.post(actionUrl, para, function(result){
				if(result.success){
					$.fn.weakTip({"tipTxt":"取消用户群共享成功！","backFn":options.backFunc});
				}else{
					var msg = result.msg;
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					}); 
				}
			});
		},
		/**
		 * 用户群共享重名： 弹出框修改用户群名称之后共享
		 * @parameter id 用户群ID
		 * @parameter name 用户群名称
		 * @parameter cityId 地市ID
		 * @parameter msg 提示语
		 * @parameter createUserId 创建人ID
		 */
		customerShare : function(options){
			var msg = encodeURIComponent(encodeURIComponent(options.msg));
			var name = encodeURIComponent(encodeURIComponent(options.cname));
			var id = options.customId;
			var cityId = options.cityId;
			var createUserId = options.createUserId;
			$(".J_market_custom_detail[customid = '"+id+"']").data("func",{callback:options.backFunc});
			var url = $.ctx + "/aibi_ci/core/pages/customGroup/shareCustomerForm.jsp?id=" + id + "&name=" + name+"&cityId="+ cityId+"&msg="+msg+"&createUserId="+createUserId;
			var dlgObj = dialogUtil.create_dialog("shareCustomerDialog", {
				"title" :  "用户群共享",
				"height": "auto",
				"width" : 660,
				"frameSrc" : url,
				"frameHeight":180,
				"position" : ['center','center'] 
			});
			$(".tishi").each(function(){
				var toffset=$(this).parent("td").offset();
				var td_height=$(this).parent("td").height();
				$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
			});
		}, 
		/**
		 * 初始化用户群共享重名弹出框页面信息
		 */
		initCustomerShare : function(){
			var id = $("#customGroupId_").val();
			var name = decodeURIComponent($("#customGroupName_").val());
			var cityId = $("#createCityId_").val();
			var msg =  decodeURIComponent($("#msgShareForm").val());
			var createUserId = $("#createUserId_").val();
			$("#customGroupId").val(id);
			$("#customGroupName").val(name);
			$("#createCityId").val(cityId);
			$("#createUserId").val(createUserId);
			
			//显示用户群名称重名问题
			$("#cnameTip").empty();
			$("#cnameTip").show().append(msg);
			validater = $("#saveForm").validate({
				rules: {
					"ciTemplateInfo.customGroupName": {
						required: true
					}
				},
				messages: {
					"ciTemplateInfo.customGroupName": {
						required: "用户群名称不允许为空!"
					}
				},
				errorPlacement:function(error,element) {
					element.parent("div").next("div").hide();
					element.parent("div").next("div").after(error);
				},
				success:function(element){
					element.remove();
				},
				errorElement: "div",
				errorClass: "tishi error"
			});
			$("#customGroupName").focus();
		},
		/**
		 *提交 用户群共享重名弹出框 确认验证
		 */
		customerShareValidateForm : function(){
			var cname = $.trim($("#customGroupName").val());
			$("#customGroupName").val(cname);
			if(!validater.form()){
				return false;
			}
			return true;
		},
		/**
		 * 提交 用户群共享重名弹出框
		 */
		customerShareSubmitForm : function(target){
			$(target).COClive("click",function() {
				if(coreMain.customerShareValidateForm()){
					var actionUrl=$.ctx + "/core/customGroupOperateAction!shareCustom.ai2do"
					var customGroupName=$("#customGroupName").val();
					var customGroupId=$("#customGroupId").val();
					var createCityId=$("#createCityId").val();
					var createUserId=$("#createUserId").val();
					var para={
							"customGroupInfo.customGroupName":customGroupName,
							"customGroupInfo.customGroupId":customGroupId,
							"customGroupInfo.createCityId":createCityId,
							"customGroupInfo.createUserId":createUserId
					};
					$.post(actionUrl, para, function(result){
						if(result.success){
							var customGroupName = $("#customGroupName").val();
							$.fn.weakTip({"tipTxt":"用户群共享成功！","backFn":function(){
								var funcObj = parent.$(".J_market_custom_detail[customid = '"+customGroupId+"']").data("func");
								funcObj.callback();
								parent.$("#shareCustomerDialog").dialog("close");
							}});
						}else{
							var cmsg = result.msg;
							if(cmsg.indexOf("重名") >= 0){
								$("#cnameTip").empty();
								$("#cnameTip").show().append(cmsg);
							}else{
								parent.showAlert(cmsg,"failed");
							}
						}
					});
				}
			});
		},
		/**
		 * 鼠标悬浮在收藏图标上事件
		 */
		attentionBoxHover : function(){
			$(".collectStar").hover(function(){
				 var labelOrCustomId = $(this).attr("labelorcustomid");
				 var isAttention = $("#isAttention_"+labelOrCustomId).val();
				 if(isAttention == 'true'){//已关注的标签或用户群
					 $("#attentionImg_"+labelOrCustomId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite_gray.png");
				 }else{//未关注的标签或用户群
					 $("#attentionImg_"+labelOrCustomId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite.png");
				 }
			 },function(){
				 var labelOrCustomId = $(this).attr("labelorcustomid");
				 var isAttention = $("#isAttention_"+labelOrCustomId).val();
				 if(isAttention == 'true'){//已关注的标签或用户群
					 $("#attentionImg_"+labelOrCustomId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite.png");
				 }else{//未关注的标签或用户群
					 $("#attentionImg_"+labelOrCustomId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite_gray.png");
				 }
			 });
		},
		/**
		 * 标签收藏图标点击取消收藏事件
		 * @parameter labelId 当前点击标签的Id
		 */
		cancelAttentionLabel : function(){
			$(".J_label_cancel_attention").COClive("click", function(e) {
				var labelId = $(this).attr("labelid");
				var url = $.ctx + "/core/ciUserAttentionAction!delUserAttentionLabel.ai2do";
				$.ajax({
					type: "POST",
					url: url,
					data:{"labelId":labelId},
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.message });
				       		$("#isAttention_"+labelId).val("false");
							/*$("#attentionImg_"+labelId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite_gray.png");
							$("#attentionImg_"+labelId).attr("title",'点击添加标签收藏');
							$("#isAttentionShow_"+labelId).attr("style",'display: none');
							$("#attentionImg_"+labelId).attr("class","J_label_attention")*/;
							$("#isAttentionShow_"+labelId).attr("style",'display: none');
							$('#attentionImg_'+labelId).text('收藏');
				       		$("#attentionImg_"+labelId).attr("class","J_label_attention");
				       	}else{
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
		 * 标签收藏图标点击添加收藏事件
		 * @parameter labelId 当前点击标签的Id
		 */
		addAttentionLabel : function(){
			$(".J_label_attention").COClive("click", function(e) {
				var labelId = $(this).attr("labelid");
				var url = $.ctx + "/core/ciUserAttentionAction!addUserAttentionLabel.ai2do";
				$.ajax({
					type: "POST",
					url: url,
					data:{"labelId":labelId},
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.message });
				       		$("#isAttention_"+labelId).val("true");
							/*$("#attentionImg_"+labelId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite.png");
							$("#attentionImg_"+labelId).attr("title","点击取消标签收藏");
							$("#isAttentionShow_"+labelId).attr("style","display: block");
							$("#attentionImg_"+labelId).attr("class","J_label_cancel_attention");*/
				       		$("#isAttentionShow_"+labelId).attr("style","display: block");
				       		$('#attentionImg_'+labelId).text('取消收藏');
				       		$("#attentionImg_"+labelId).attr("class","J_label_cancel_attention");
						}else{
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
		 * @parameter customId 当前点击用户群的Id
		 */
		cancelAttentionCustom : function(){
			$(".J_custom_cancel_attention").COClive("click", function(e) {
				var customId = $(this).attr("customId");
				var url = $.ctx + "/core/ciUserAttentionAction!delUserAttentionCustom.ai2do";
				$.ajax({
					type: "POST",
					url: url,
					data:{'customGroupId':customId},
			      	success: function(result){
				       	if(result.success) {
			       			$.fn.weakTip({"tipTxt":result.message });
			       			$("#isAttention_"+customId).val("false");
							/*$("#attentionImg_"+customId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite_gray.png");
							$("#attentionImg_"+customId).attr("title",'点击添加用户群收藏');
							$("#isAttentionShow_"+customId).attr("style",'display: none');
							$("#attentionImg_"+customId).attr("class","J_custom_attention");*/
			       			$("#isAttentionShow_"+customId).attr("style",'display: none');
			       			$('#attentionImg_'+customId).text('收藏');
				       		$("#attentionImg_"+customId).attr("class","J_custom_attention");
							
						}else{
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
		 * 用户群收藏图标点击添加收藏事件
		 * @parameter customId 当前点击用户群的Id
		 */
		addAttentionCustom : function(){
			$(".J_custom_attention").COClive("click", function(e) {
				var customId = $(this).attr("customId");
				var url = $.ctx + "/core/ciUserAttentionAction!addUserAttentionCustom.ai2do";
				$.ajax({
					type: "POST",
						url: url,
						data:{'customGroupId':customId},
						success: function(result){
							if(result.success) {
								$.fn.weakTip({"tipTxt":result.message });
								$("#isAttention_"+customId).val("true");
								/*$("#attentionImg_"+customId).attr("src",$.ctx + "/aibi_ci/assets/themes/default/images/favorite.png");
								$("#attentionImg_"+customId).attr("title","点击取消用户群收藏");
								$("#isAttentionShow_"+customId).attr("style","display: block");
								$("#attentionImg_"+customId).attr("class","J_custom_cancel_attention");*/
								$("#isAttentionShow_"+customId).attr("style","display: block");
								$('#attentionImg_'+customId).text('取消收藏');
					       		$("#attentionImg_"+customId).attr("class","J_custom_cancel_attention");
							}else{
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
		 * 用户群统计失败重新生成
		 * @parameter customGroupId 用户群ID
		 * @parameter createType 用户群创建类型 
		 * @parameter dataDate 要重新生成失败清单的数据日期，不赋值则重新生成全部失败的清单
		 * @parameter backFunc 统计之后回调刷新列表
		 */
		statistFailRegenCustomer : function(options){
			$(options.target).COClive("click",function(e) {
				var customGroupId = $(this).attr("customId");
				var createType = $(this).attr("createTypeId");
				var dataDate = $(this).attr("dataDate");
				if(createType == 7){//文件创建的不支持
					return;
				}
				if(!dataDate){
					dataDate = '';
				}
				var url = $.ctx + "/core/customGroupOperateAction!reGenerateCustomer.ai2do";
				$.ajax({
					url: url,
					data:{
						"customGroupInfo.customGroupId":customGroupId,
						"customGroupInfo.dataDate":dataDate
					},
					success: function(result){
						if(result.success){
							$.fn.weakTip({"tipTxt":"已提交，请刷新查看","backFn":options.backFunc});
						}else{
							commonUtil.create_alert_dialog("confirmDialog", {
								"txt":result.msg,
								"type":"failed",
								"width":500,
								"height":200
							},function(){options.backFunc;});
						}
					}
				});
			});
		},
		/**
		 * 一次性规则创建用户群重新生成
		 */
		oneTimeRegenCustomer : function(options){
			$(options.target).COClive("click",function(e){
				$("#isEditCustom").val("false");
				var customId = $(this).attr("customId");
				var createTypeId = $(this).attr("createTypeId");
				if(createTypeId == 7){//文件创建的不支持
					return;
				}	
				var url = $.ctx + "/core/customGroupOperateAction!checkReGenerateCustomer.ai2do?customGroupInfo.customGroupId="+customId;
				$.ajax({
					url: url,
					success: function(result){
						if(result.success){
							commonUtil.create_alert_dialog("confirmDialog", {
								 "txt":"抱歉，该用户群正在被使用，无法重新生成，请稍后再试",
								 "type":"success",
								 "width":500,
								 "height":200
							}); 
							$("#console").append("用户群运算：" +result.msg+"<br/>");
						}else{
							coreMain.regenCustomer(customId,createTypeId,'',options);
						}
					}
				});
			});
		},
		/**
		 * 用户群重新生成
		 */
		regenCustomer : function(customGroupId,createTypeId,listTableName,options){
			$("#isEditCustom").val("false");
			if(createTypeId == 7){//文件创建的不支持
				return;
			}
			var url = $.ctx + "/core/customGroupOperateAction!reGenerateCustomer.ai2do";
			if (!listTableName) {
				listTableName = "";
			}
			$.ajax({
				url: url,
				data:{
					"customGroupInfo.customGroupId":customGroupId,
					"listTableName":listTableName
				},
				success: function(result){
					if(result.success){
						commonUtil.create_alert_dialog("confirmDialog", {
							 "txt":"已提交，请刷新查看",
							 "type":"success",
							 "width":500,
							 "height":200
						},options.backFunc);
						$("#console").append("用户群运算：" +result.msg+"<br/>");
					}else{
						commonUtil.create_alert_dialog("confirmDialog", {
							 "txt":result.msg,
							 "type":"failed",
							 "width":500,
							 "height":200
						},options.backFunc);
						$("#console").append("用户群运算：" +result.msg+"<br/>");
					}
				}
			});
		},
		/**
		 * 清单预览
		 */
		queryGroupList : function(target){
			$(target).COClive("click",function(e){ 
				var customId = $(this).attr("customId");
				var dataDate = $(this).attr("dataDate");
				var url = $.ctx + "/core/customGroupOperateAction!findGroupList.ai2do?customGroupInfo.customGroupId="+customId+"&customGroupInfo.dataDate="+dataDate;
				var dlgObj = dialogUtil.create_dialog("queryListDialog", {
					"title" :  "用户群清单预览",
					"height": "auto",
					"width" : 700,
					"frameSrc" : url,
					"frameHeight":328,
					"position" : ['center','center'] 
				}); 
			});
		},
		/**
		 * 推送设置
		 */
		pushCustomerGroupSingle : function(target){
			$(target).COClive("click",function(e){
				var customGroupId = $(this).attr("customGroupId");
				var updateCycle = $(this).attr("updateCycle");
				var isPush = $(this).attr("isPush");
				var duplicateNum = $(this).attr("duplicateNum");
				if(duplicateNum) {
					if(duplicateNum != 0) {
						$.fn.weakTip({"tipTxt":"用户群包含重复记录不能进行推送设置！"});
						return;
					}
				}
				$("#isEditCustom").val("false");
				var pushFromSign = true;
				
				var ifmUrl = $.ctx +"/core/customGroupOperateAction!prePushCustomerSingle.ai2do?customGroupInfo.customGroupId=" 
					+ customGroupId + "&updateCycle=" + updateCycle + "&isPush=" + isPush;
				var dlgObj = dialogUtil.create_dialog("dialog_push_single", {
					"title" :  "用户群推送设置",
					"height": "auto",
					"width" : 730,
					"frameSrc" : ifmUrl,
					"frameHeight":335,
					"position" : ['center','center'] 
				});
			});
		},
		/**
		 * 用户群推送平台及周期显示
		 */
	    showCustomPushCycle : function(target){
				var isPush = $("#isPush").val();
				var pushCycle = $("#pushCycle").val();
				$('.cycle_push_platform').hide();
				$('.day_cycle_push_platform').hide();
				$('.disposable_push_platform').show();
				if(isPush == 1) {
					$('.disposable_push_platform').hide();
					if(pushCycle == 2) {
						$('.cycle_push_platform').show();
					} else {
						$('.day_cycle_push_platform').show();
					}
					
					var cId = $('#customGroupIdHidden').val();
					var getPushedCycleUrl = $.ctx +'/core/customGroupOperateAction!findPushedCycle.ai2do';
					$.post(getPushedCycleUrl, 'customGroupInfo.customGroupId=' + cId, function(result) {
						if(result.success){
							coreMain.sysIds = result.msg.split(',');
							for(var i=0; i<sysIds.length; i++) {
								if(pushCycle == 2) {
									$('.cycle_push_platform input[type=checkbox]').each(function() {
										if(coreMain.sysIds[i] == this.value) {
											this.checked = true;
										}
									});
								} else {
									$('.day_cycle_push_platform input[type=checkbox]').each(function() {
										if(coreMain.sysIds[i] == this.value) {
											this.checked = true;
										}
									});
								}
							}
						}else{
							var msg = result.msg;
							commonUtil.create_alert_dialog('showDetailFailed_Type', {
								txt 	: msg,
								type 	: 'failed', 
								width 	: 500,
								height 	: 200,
								param	: false
							});
						}
					});
				}
		},
		/**
		 * 推送平台单选radio
		 */
		changePushRadio : function(target){
			$(target).COClive("click",function(e){
				var checkedVal = $('.push_type_radio:checked').val();
				$('.platform_checkbox').attr('checked', false);
				if(checkedVal == 1) {
					$('.cycle_push_platform').hide();
					$('.day_cycle_push_platform').hide();
					$('.disposable_push_platform').show();
				} else if(checkedVal == 2) {
					$('.disposable_push_platform').hide();
					$('.day_cycle_push_platform').hide();
					$('.cycle_push_platform').show();
				} else if(checkedVal == 3) {
					$('.disposable_push_platform').hide();
					$('.cycle_push_platform').hide();
					$('.day_cycle_push_platform').show();
				}
				for(var i=0; i<coreMain.sysIds.length; i++) {
					if(pushCycle == 2){
						$('.cycle_push_platform input[type=checkbox]').each(function() {
							if(coreMain.sysIds[i] == this.value) {
								this.checked = true;
							}
						});
					}else if(pushCycle == 3){
						$('.day_cycle_push_platform input[type=checkbox]').each(function() {
							if(coreMain.sysIds[i] == this.value) {
								this.checked = true;
							}
						});
					}
				}
			});
		},
		/**
		 * 弹出推送确认框
		 */
		pushCustomerGroupConfirm : function(target){
			$(target).COClive("click",function(e){
				if(coreMain.validateIsNull()) {
					var dlgObj = commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":"确认推送该用户群？",
						 "type":"confirm",
						 "width":500,
						 "height":200
					},"coreMain.push"); 
				}
		    })
		},
		/**
		 * 校验推送设置
		 */
		validateIsNull : function(){
			var currentPushCycle = $('.push_type_radio[name="pushCycle"]:checked').val();
			var disposablePlateform = $('.disposable_push_platform input[type="checkbox"]:checked').val();
			var monthPlateform = $('.cycle_push_platform input[type="checkbox"]:checked').val();
			var dayPlateform = $('.day_cycle_push_platform input[type="checkbox"]:checked').val();
			if(currentPushCycle == 1 && !disposablePlateform) {
				commonUtil.create_alert_dialog('showDetailFailed_Type', {
					txt 	: '请选择推送平台！',
					type 	: 'failed', 
					width 	: 500,
					height 	: 200,
					param	: false
				});
				return false;
			} else if(currentPushCycle == 2 && isPush != 1 && !monthPlateform) {
				commonUtil.create_alert_dialog('showDetailFailed_Type', {
					txt 	: '请选择推送平台！',
					type 	: 'failed', 
					width 	: 500,
					height 	: 200,
					param	: false
				});
				return false;
			}else if(currentPushCycle == 3 && isPush != 1 && !dayPlateform) {
				commonUtil.create_alert_dialog('showDetailFailed_Type', {
					txt 	: '请选择推送平台！',
					type 	: 'failed', 
					width 	: 500,
					height 	: 200,
					param	: false
				});
				return false;
			} else {
				return true;
			}
		},
		/**
		 * 推送用户群
		 */
		push : function(){
			var url = $.ctx +'/core/customGroupOperateAction!pushCustomerAfterSave.ai2do';
			coreMain.submitForm(url);
		},
		/**
		 * 提交推送用户群 
		 */
		submitForm : function(url){
			 var coc_province = $("#province").val();
			 if(coc_province == "zhejiang"){
				//浙江营销管理校验
				var customGroupId = $("#customGroupIdHidden").val();
				var sysId = "";
				var result=0;
				
				var _updateCycle = $('.push_type_radio[name="pushCycle"]:checked').val();
				var pushPlatformEl = '';
				
				if(_updateCycle == 1) {
					pushPlatformEl = 'disposable_push_platform';
				} else if(_updateCycle == 2) {
					pushPlatformEl = 'cycle_push_platform';
				} else if(_updateCycle == 3) {
					pushPlatformEl = 'day_cycle_push_platform';
				}
				
				var num = 0;
				$('.' + pushPlatformEl + ' input[type=checkbox]').each(function() {
					if($(this).attr("checked")=="checked"){
						sysId=$(this).val();
						if(sysId.split('_')[1] === 'MARKET'){
							++num;
							if(num>1){
								return false;
							}
						}
					}
				});
				if(num>1){
					commonUtil.create_alert_dialog('showDetailFailed_Type', {
						txt 	: '推送营销管理平台不允许选择多个平台渠道',
						type 	: 'failed', 
						width 	: 500,
						height 	: 200,
						param	: false
					});
					return;
				}
				$('.' + pushPlatformEl + ' input[type=checkbox]').each(function() {
					if($(this).attr("checked")=="checked"){
						sysId=$(this).val();
						result = coreMain.checkSysId(sysId,customGroupId);
						if(result>0){
							return false;
						}
					}
				});
				if(result>0){
					parent.showAlert("推送设置失败<br/>营销管理平台不存在该用户群创建者的账号","failed");
					commonUtil.create_alert_dialog('showDetailFailed_Type', {
						txt 	: '推送设置失败营销管理平台不存在该用户群创建者的账号',
						type 	: 'failed', 
						width 	: 500,
						height 	: 200,
						param	: false
					});
					return;
				}
				
			}
			var actionUrl = $.ctx + "/core/customGroupOperateAction!pushCustomerGroupSingle.ai2do?isPush=" + isPush;
			if(url) {
				actionUrl = url;
			}
			
			//提交推送的时候取消其他选中的信息，防止重复提交
			var currentPushCycle = $('.push_type_radio[name="pushCycle"]:checked').val();
			if(currentPushCycle == 1) {//一次性
				$('.day_cycle_push_platform input[type="checkbox"]').attr('checked', false); 
				$('.cycle_push_platform input[type="checkbox"]').attr('checked', false); 
			} else if(currentPushCycle == 2) {//月
				$('.disposable_push_platform input[type="checkbox"]').attr('checked', false); 
			} else if(currentPushCycle == 3) {//日
				$('.disposable_push_platform input[type="checkbox"]').attr('checked', false); 
			}
			var customGroupInfoId = $('#customGroupIdHidden').val();
			$.ajax({
				url		: $.ctx + '/core/customGroupOperateAction!findCustomer.ai2do?customGroupInfo.customGroupId=' + customGroupInfoId,
		        type	: 'post',
		        async	: false,//同步
		        data	: $("#labelForm").serialize(),
		        success	: function (data) {
		        	var duplicateNum = data.customGroupInfo.duplicateNum;
		        	if(duplicateNum != 0 && duplicateNum != null) {
		        		commonUtil.create_alert_dialog('showDetailFailed_Type', {
							txt 	: '用户群包含重复记录不能进行推送设置！',
							type 	: 'failed', 
							width 	: 500,
							height 	: 200,
							param	: false
						});
		        		if(url) {
		        			window.setTimeout(function() {
		            			var url = $.ctx + "/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1";
		    					window.parent.location.href = url;
		            		}, 1500);
		        		}
		        		return;
		        	}
		        	$.ajax({
		        		url		: actionUrl,
		                type	: 'post',
		                async	: false,//同步
		                data	: $("#pushForm").serialize(),
		                success	: function (result) {
			        		if(result.success){
			        			if(parent.$("#isPushCustom").val() == "true"){
			        				parent.showAlert(result.msg, "success" , function() {
			           					var url = $.ctx + "/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1";
			           					window.parent.location.href = url;
			            			}, null, 500, 200, -1, $.ctx + '/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4', function() {
			            				window.parent.location.href = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1";
			            			});
			        			} else {
			        				commonUtil.create_alert_dialog('showDetailFailed_Type', {
										txt 	: result.msg,
										type 	: 'success', 
										width 	: 500,
										height 	: 200,
										param	: false
									},function(){
										parent.$('#dialog_push_single').dialog('close');
									});
			        			}
			        			
			        		}else{
			        			var msg = result.msg;
			        			//parent.showAlert(msg,"failed");
			        			commonUtil.create_alert_dialog('showDetailFailed_Type', {
									txt 	: msg,
									type 	: 'failed', 
									width 	: 500,
									height 	: 200,
									param	: false
								});
			        		}
		        		}
		        	});
		        }
			});
		},
		/**
		 * 浙江检查sysId
		 */
		checkSysId : function(sysId,customGroupId){
			if(sysId.split('_')[1] === 'MARKET'){
		        if(!checkUserExistOnYXGLPlatform(customGroupId)){
		            return 1;
		        }else{
		        	return 0;
		        }
		    }else{
		    	return 0;
		    }
		},
		/**
		 *浙江验证营销管理是否有用户
		 */
		checkUserExistOnYXGLPlatform : function(customGroupId){
			var exist = false;
		    $.ajax({
		        url : $.ctx + "/ci/zjDownloadApproveAction!checkUserExist.ai2do",
		        type: 'post',
		        data: {customGroupId: customGroupId},
		        dataType: 'json',
		        async: false,
		        success: function(result){
		            exist = result.success;
		        }
		    });
		    return exist;
		},
		
		/**
		 * 用户群规则展开
		 */
		expandCustomRules : function(target){
			var divId = "tag_dropable";
			var top = $(target).offset().top-100;
			$("#"+divId).COCdialog({
				backFun: resizeConditionDiv,//修改标签宽度
				top:top,
				buttons:[
							{
							   type:"cancel",
							   text: "取消", 
							   click: function() {
								   $("#"+divId+"_dialog").animate({"width":0},500,function(){
									   $(this).hide();
								   });
								   $("#tag_dropable").hide();//隐藏层
								   $(".dialog-widget-overlay").remove();
							   } 
							},
							{
								type:"ok",
								text: "确定",
								click: function() {
									coreMain.submitToShoppingCart();
									$("#"+divId+"_dialog").animate({"width":0},500,function(){
										$(this).hide();
									});
									$("#tag_dropable").hide();//隐藏层
									$(".dialog-widget-overlay").remove(); 
								}
							}
						]
			});
		},
		
		/**
		 * 标签取反
		 * @param ths 点击的元素this
		 */
		reverseLabel : function(ths) {
			var tar=$(ths).parent();
			if(tar.hasClass("conditionCT_active")){
				tar.attr('labelFlag', 1).removeClass("conditionCT_active");
			}else{
				tar.addClass("conditionCT_active").attr('labelFlag', 0);
			}
		},
		
		/**
		 * 标签提示
		 * @param event 事件
		 * @param ths 点击的元素this
		 */
		labelTip : function(eevent, ths) {
			var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			
			var labelId = $(ths).attr('labelId');
			//TODO action迁移
			var url = $.ctx + '/ci/ciIndexAction!findLabelTipInfo.ai2do';
			
			var isHiddenOpt = false;
			var labelOrCustomerId;
			if(labelOrCustomerId == labelId && $('#labelTip').is(":visible")) {
				$('#labelTip').hide();
				isHiddenOpt = true;
			}
			
			//隐藏用户群提示
			$('#customTip').hide();
			
			labelOrCustomerId = labelId;
			
			if(isHiddenOpt) return;
			
			var posx = $(ths).offset().left - 165;
			var posy = $(ths).offset().top + $(ths).height() - 6;
			
			$.post(url, {labelId : labelId}, function(response) {
				
				var labelInfo = response.labelInfo;
				var detailInfo= response.detailInfo;
				var cycleStr = '日周期';
				var customNumDisplay = '';
				if(labelInfo.updateCycle == 2) {
					cycleStr = '月周期';
				}
				if(labelInfo.labelTypeId == 1 && labelInfo.customerNum != -1) {
					customNumDisplay = '<th>用户数：</th>' + 
		                '<td>' + labelInfo.customerNum + '</td>';
				}
				var desc = labelInfo.busiCaliber;
				if(desc == null) {desc = ''}
				
				var sDate = labelInfo.effecTimeStr;
				var eDate = labelInfo.failTimeStr;
				if(sDate == null) {sDate = '--'}
				if(eDate == null) {eDate = '--'}
				var html = '<div class="labelTipTop">&nbsp;</div>'+
				   '<div class="labelTipInner">'+
					'<div class="maintext">' + desc + '</div>'+
				 '<table>' + 
					'<tr>' + 
				     '<th width="85">更新周期：</th>' + 
				     '<td>' + cycleStr + '</td>' + 
				     '<th width="85">创建时间：</th>' + 
				     '<td>' + labelInfo.createTimeStr + '</td>' + 
				 '</tr>' + 
				 '<tr>' +
				     '<th>最新数据时间：</th>' + 
				     '<td>' + labelInfo.newDataDate + '</td>' + 
				     customNumDisplay + 
				 '</tr>' + 
					'</table>' + 
					'<div class="marketTipBottomBox">'+
		        '<div class="fleft">';
		        
				if(detailInfo.isSysRecom != null && detailInfo.isSysRecom == 1){
			    	html += '<a href="javascript:void(0);" class="organ"  title="系统推荐标签"/>';
			    }
		        if(detailInfo.isHot == 'true'){
		        	html += '<a href="javascript:void(0);" class="hot" title="系统热门标签"/>';
		        }
		        if(detailInfo.isAttention == 'true'){
		        	html += '<a href="javascript:void(0);" class="hideFont" title="我收藏的标签"/>';
		        }
		        
		        if(detailInfo.isHot == 'true' || detailInfo.isAttention == 'true' 
					|| (detailInfo.isSysRecom != null && detailInfo.isSysRecom == 1)){
					html += '<span class="storeSegLine"></span>';
			    }
		        
		        if(detailInfo.isAttention == 'false'){
		        	html += '<input type="hidden" id="isAttentionLabel_'+labelId+'" value="' + detailInfo.isAttention + '" />';
		        	html += '<a href="javascript:void(0);"  onclick="coreMain.attentionLabelOper(this)" labelId="'+labelId+'" class="store" >收藏</a>';
		        }
		        if(detailInfo.isAttention == 'true'){
		        	html += '<input type="hidden" id="isAttentionLabel_'+labelId+'" value="'+ detailInfo.isAttention+'" />';
		        	html += '<a href="javascript:void(0);"  onclick="coreMain.attentionLabelOper(this)" labelId="'+labelId+'" class="store" >取消收藏</a>';
		        }
		        html += '</div>'+
					'<div class="fright">'+
					   '<label>有效期：</label>'+
		            '<span>' + sDate + '</span>'+
					   '<span>至</span>'+
					   '<span>' + eDate + '</span>'+
					'</div>'+
			    '</div>'+
				'</div>';
			
				$('#labelTip').empty();
				$('#labelTip').html(html);
				$('#labelTip').css({'left' : posx + "px", 'top' : posy + "px"}).show();
			});
		},
		
		/**
		 * 用户群提示框
		 * @param event 事件
		 * @param ths 点击的元素this
		 */
		customTip : function(event, ths) {
			var e = e || window.event;
			e.stopPropagation ? e.stopPropagation() : e.cancelBubble=true;

			var customGroupId = $(ths).attr('customId');
			//TODO action迁移
			var url = $.ctx + '/ci/ciIndexAction!findCustomTipInfo.ai2do';
			
			var isHiddenOpt = false;
			var labelOrCustomerId;
			if(labelOrCustomerId == customGroupId && $('#customTip').is(":visible")) {
				$('#customTip').hide();
				isHiddenOpt = true;
			}
			
			//隐藏标签提示
			$('#labelTip').hide();
			
			labelOrCustomerId = customGroupId;
			
			if(isHiddenOpt) return;
			
			var posx = $(ths).offset().left - 165;
			var posy = $(ths).offset().top + $(ths).height() - 6;

			$.post(url, {'ciCustomGroupInfo.customGroupId' : customGroupId}, function(response) {
				
				var customGroupInfo = response.customGroupInfo;
				
				var productDate		= customGroupInfo.productDate;
				var desc 			= customGroupInfo.customGroupDesc;
				if(desc == null) {desc = ''}
				var sDate = customGroupInfo.startDateStr;
				var eDate = customGroupInfo.endDateStr;
				var customNum = customGroupInfo.customNum;
				if(sDate == null) {sDate = '--'}
				if(eDate == null) {eDate = '--'}
				if(customNum == null) {customNum = '-'}
				if(productDate == null) {productDate = '--'}
				var html = '<div class="labelTipTop">&nbsp;</div>'+
				   '<div class="labelTipInner">'+
					'<div class="maintext">' + desc + '</div>'+
					 '<table>' + 
						'<tr>' + 
				         '<th width="85">更新周期：</th>';
				         if(customGroupInfo.updateCycle == 1) {
					         html += '<td>一次性</td>';
				         }else if(customGroupInfo.updateCycle == 2){
					         html += '<td>月周期</td>';
				         } else if(customGroupInfo.updateCycle == 3) {
				        	 html += '<td>日周期</td>';
				         }else if(customGroupInfo.updateCycle == 4) {
				        	 html += '<td>无</td>';
				         }
				         html += '<th width="85">创建时间：</th>' + 
				         '<td>' + customGroupInfo.createTimeView + '</td>' + 
				     '</tr>' + 
				     '<tr>' +
				         '<th>用户数：</th>' + 
				         '<td>' + customNum + '</td>' + 
				         '<th>最新数据时间：</th>' + 
				         '<td>' + productDate + '</td>' + 
				     '</tr>' + 
				 	'</table>' + 
					'<div class="marketTipBottomBox">'+
		        '<div class="fleft">';
		        
		        
		        if(customGroupInfo.isSysRecom != null && customGroupInfo.isSysRecom == 1){
			    	html += '<a href="javascript:void(0);" class="organ"  title="系统推荐标签"/>';
			    }
		        if(customGroupInfo.isHotCustom == 'true'){
		        	html += '<a href="javascript:void(0);" class="hot" title="系统热门标签"/>';
		        }
		        if(customGroupInfo.isAttention == 'true'){
		        	html += '<a href="javascript:void(0);" class="hideFont" title="我收藏的标签"/>';
		        }
		        
		        if(customGroupInfo.isHotCustom == 'true' || customGroupInfo.isAttention == 'true' 
					|| (customGroupInfo.isSysRecom != null && customGroupInfo.isSysRecom == 1)){
					html += '<span class="storeSegLine"></span>';
			    }
		        
		        
		        if(customGroupInfo.isAttention == 'false'){
		        	html += '<input type="hidden" id="isAttentionCustom_'+customGroupId+'" value="'+customGroupInfo.isAttention+'" />';
		        	html += '<a href="javascript:void(0);" customGroupId='+customGroupId+'  onclick="coreMain.attentionCustomOper(this)" class="store" >收藏</a>';
		        }
		        if(customGroupInfo.isAttention == 'true'){
		        	html += '<input type="hidden" id="isAttentionCustom_'+customGroupId+'" value="'+customGroupInfo.isAttention+'" />';
		        	html += '<a href="javascript:void(0);" customGroupId='+customGroupId+'  onclick="coreMain.attentionCustomOper(this)" class="store" >取消收藏</a>';
		        }
		        html += '</div>';
		        if(customGroupInfo.updateCycle == 2){
		        	html += '<div class="fright">'+
					   '<label>有效期：</label>'+
		               '<span>' + sDate + '</span>'+
					   '<span>至</span>'+
					   '<span>' + eDate + '</span>'+
					'</div>';
		        }
		        
		        html += '</div></div>';
				$('#customTip').empty();
				$('#customTip').html(html);
				$('#customTip').css({'left' : posx, 'top' : posy}).show();
			});
		},
		
		/**
		 * 标签关注图标点击事件
		 * @param ths 点击的dom对象
		 */
		attentionLabelOper : function(ths){
			var labelId = $(ths).attr("labelId");
			var isAttention = $("#isAttentionLabel_"+labelId).val();
			if(isAttention == 'true'){
				//已经关注，点击进行取消关注动作
				var url=$.ctx+'/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
				$.ajax({
					type: "POST",
					url: url,
			      	data:{'labelId':labelId},
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.message});
						}else{
							window.parent.showAlert(result.message,"failed");
						}
					}
				});
			}else{
				//没有关注，点击进行关注动作触发
				var url=$.ctx+'/ci/ciLabelAnalysisAction!addUserAttentionLabel.ai2do';
				$.ajax({
					type: "POST",
						url: url,
						data:{'labelId':labelId},
						success: function(result){
						if(result.success) {
							$.fn.weakTip({"tipTxt":result.message});
						}else{
							window.parent.showAlert(result.message,"failed");
						}
					}
		        });
			}
			
		},
		
		/**
		 * 用户群关注图标点击事件
		 * @param ths 点击的dom对象
		 */
		attentionCustomOper : function(ths){
			var customId = $(ths).attr("customGroupId");
			var isAttention = $("#isAttentionCustom_"+customId).val();
			if(isAttention == 'true'){
				//已经关注，点击进行取消关注动作
				//alert('已经关注，点击进行取消关注动作');
				var url=$.ctx+'/ci/customersManagerAction!delUserAttentionCustom.ai2do';
				$.ajax({
					type: "POST",
					url: url,
					data:{'customGroupId':customId},
			      	success: function(result){
				       	if(result.success) {
				       		$.fn.weakTip({"tipTxt":result.message});
						}else{
							window.parent.showAlert(result.message,"failed");
						}
					}
				});
			}else{
				//没有关注，点击进行关注动作触发
				//alert('没有关注，点击进行关注动作触发');
				var url=$.ctx+'/ci/customersManagerAction!addUserAttentionCustom.ai2do';
				$.ajax({
					type: "POST",
					url: url,
					data:{'customGroupId':customId},
					success: function(result){
						if(result.success) {
							$.fn.weakTip({"tipTxt":result.message});
						}else{
							window.parent.showAlert(result.message,"failed");
						}
					}
		        });
			}
		},
		
		/**
		 * 提交用户群规则到购物车
		 */
		submitToShoppingCart : function(){
			var custSortNum = $(".J_custSortNum").val();
			var target = $(".J_setting[sort='"+ custSortNum + "']");
			target.attr("elementType", 6);
			var custParentId = target.attr("calcuElement");
			//拼接用户群规则到对应的用户群下
			var html = "";
			$("#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis").each(function(i, element){
				var calcuElement = "", //括号、操作符号、标签ID或者用户群清单表名
				minVal		 = "", //标识标签 0 1属性
				maxVal 		 = "", 
				elementType	 = "", //元素类型:1-运算符;2-标签ID;3-括号;4-产品ID;5-清单表名。
				labelFlag	 = ""; //标签是否取反
			
				var ele = $(element);
				if (ele.hasClass("conditionCT")) {//处理标签和用户群
					elementType				= ele.attr("elementType");
					var customOrLabelName 	= ele.attr("customOrLabelName");
					
					if (elementType == 2) {//标签
						labelFlag			= ele.attr("labelFlag");
						calcuElement 		= ele.attr("calcuElement");
						minVal				= ele.attr("minVal");
						maxVal				= ele.attr("maxVal");
						var effectDate		= ele.attr("effectDate");
						var labelTypeId 	= ele.attr("labelTypeId");
						var attrVal 		= ele.attr("attrVal");
						var startTime 		= ele.attr("startTime");
						var endTime 		= ele.attr("endTime");
						var contiueMinVal 	= ele.attr("contiueMinVal");
						var contiueMaxVal 	= ele.attr("contiueMaxVal");
						var leftZoneSign 	= ele.attr("leftZoneSign");
						var rightZoneSign 	= ele.attr("rightZoneSign");
						var exactValue 		= ele.attr("exactValue");
						var darkValue 		= ele.attr("darkValue");
						var attrName		= ele.attr("attrName");
						var queryWay		= ele.attr("queryWay");
						var unit			= ele.attr("unit");
						var customCreateTypeId = ele.attr("customCreateTypeId");
						var dataDate        = ele.attr("dataDate");
						var updateCycle     = ele.attr("updateCycle");
						var isNeedOffset	= ele.attr("isNeedOffset");
						if (!$.isNotBlank(isNeedOffset)){
							isNeedOffset = "";
						}
						
						html += "<div class='child' elementType='" + elementType + "' labelFlag='" + labelFlag
								+ "' labelTypeId='" + labelTypeId + "' parentId='" + custParentId
								+ "' calcuElement='" + calcuElement + "' customId='" + customId + "' effectDate='" + effectDate 
								+ "' attrVal='" + attrVal + "' startTime='" + startTime + "' unit='" + unit
								+ "' endTime='" + endTime + "' contiueMinVal='" + contiueMinVal + "' contiueMaxVal='" + contiueMaxVal
								+ "' rightZoneSign='" + rightZoneSign + "' leftZoneSign='" + leftZoneSign
								+ "' exactValue='" + exactValue + "' darkValue='" + darkValue + "' columnCnName='" + columnCnName
								+ "' customOrLabelName='" + customOrLabelName + "' attrName='" + attrName
								+ "' queryWay='" + queryWay + "' maxVal='" + maxVal + "' minVal='" + minVal
								+ "' dataDate='" + dataDate  + "' updateCycle='" + updateCycle  + "' isNeedOffset='" + isNeedOffset + "'>"
						if (labelTypeId == 8) {
							var childs = ele.children(".child");
							for(var index=0; index<childs.length; index++) {
								var el				= $(childs[index]);
								var labelFlag		= el.attr("labelFlag");
								var calcE		 	= el.attr("calcuElement");
								var minVal			= el.attr("minVal");
								var maxVal			= el.attr("maxVal");
								var effectDate		= el.attr("effectDate");
								var lti 			= el.attr("labelTypeId");
								var attrVal 		= el.attr("attrVal");
								var startTime 		= el.attr("startTime");
								var endTime 		= el.attr("endTime");
								var contiueMinVal 	= el.attr("contiueMinVal");
								var contiueMaxVal 	= el.attr("contiueMaxVal");
								var leftZoneSign 	= el.attr("leftZoneSign");
								var rightZoneSign 	= el.attr("rightZoneSign");
								var exactValue 		= el.attr("exactValue");
								var darkValue 		= el.attr("darkValue");
								var attrName		= el.attr("attrName");
								var queryWay		= el.attr("queryWay");
								var columnCnName	= el.attr("columnCnName");
								var childUnit		= el.attr("unit");
								var dataDate        = el.attr("dataDate");
								var updateCycle     = el.attr("updateCycle");
								var isNeedOffset	= el.attr("isNeedOffset");
								var parentId		= el.attr("parentId");
								if (!$.isNotBlank(isNeedOffset)){
									isNeedOffset = "";
								}
								
								html += "<div class='grandson' elementType='" + elementType + "' labelFlag='" + labelFlag
										+ "' labelTypeId='" + lti + "' parentId='" + parentId
										+ "' calcuElement='" + calcE + "' customId='" + customId + "' effectDate='" + effectDate
										+ "' attrVal='" + attrVal + "' startTime='" + startTime + "' unit='" + childUnit
										+ "' endTime='" + endTime + "' contiueMinVal='" + contiueMinVal + "' contiueMaxVal='" + contiueMaxVal
										+ "' rightZoneSign='" + rightZoneSign + "' leftZoneSign='" + leftZoneSign
										+ "' exactValue='" + exactValue + "' darkValue='" + darkValue + "' columnCnName='" + columnCnName
										+ "' attrName='" + attrName
										+ "' queryWay='" + queryWay + "' maxVal='" + maxVal + "' minVal='" + minVal
										+ "' dataDate='" + dataDate  + "' updateCycle='" + updateCycle  + "' isNeedOffset='" + isNeedOffset + "'></div>";
							}
						}
						html += "</div>";
					} else if (elementType == 5) {//用户群
						var attrVal 		= ele.attr("attrVal");
						var customId 		= ele.attr("customId");
						var dataCycle       = ele.attr("dataCycle");
						calcuElement 		= ele.attr("calcuElement");
						var isNeedOffset	= ele.attr("isNeedOffset");
						if (!$.isNotBlank(isNeedOffset)){
							isNeedOffset = "";
						}
						
						html += "<div class='child' elementType='" + elementType + "' parentId='" + custParentId
								+ "' calcuElement='" + calcuElement + "' customId='" + customId
								+ "' attrVal='" + attrVal + "' customOrLabelName='" + customOrLabelName
								+ "' labelFlag='" + dataCycle  + "' isNeedOffset='" + isNeedOffset + "'></div>";
					}
				} else if (ele.hasClass("chaining")) {//处理操作符
					elementType = 1;
					var name = ele.find("span").text();
					if(name == "且") {
						calcuElement = "and";
					}else if(name == "或") {
						calcuElement = "or";
					}else if(name == "剔除") {
						calcuElement = "-";
					}
					html += "<div class='child' elementType='" + elementType + "' parentId='" + custParentId
							+ "' calcuElement='" + calcuElement + "'></div>";
				} else if (ele.hasClass("parenthesis")) {//处理括号
					var parenthesisEl = $("#parenthesis");
					if(!ele.hasClass("waitClose")) {
						elementType = 3;
						calcuElement = ele.attr("calcuElement");
						
						var creat = ele.attr("creat");
						
						html += "<div class='child' elementType='" + elementType + "' parentId='" + custParentId
								+ "' calcuElement='" + calcuElement + "' createBrackets='" + creat + "'></div>";
					} else {
						return;
					}
				}
			});
			target.html(html);
			coreMain.submitShopCartSession(custSortNum);//提交session
		},
		
		/**
		 * 系统正在建设中
		 */
		systemBuiding : function(){
			var url = $.ctx + "/aibi_ci/systemBuilding.jsp";
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		},
		/**
		 * 直接跳转保存用户群页面
		 */
		forwardSaveCustomPage : function() {
			//判断选择的计算元素个数是否超过限制
			if(parent.coreMain.isOverLength($._maxLabelNum)) {
				coreMain.alertFailed('isOverLength', '计算元素不能超过' + $._maxLabelNum + '个！');
				return;
			}
			//有计算有元素没有设置属性
			if(!parent.coreMain.attrCompleted()) {
				coreMain.alertFailed('attrCompleted', '计算元素没有设置属性，无法保存用户群！');
				return;
			} 
			//判断标签是否有数据
			if(!parent.coreMain.haveNewestData()) {
				coreMain.alertFailed('haveNoDataDate', '标签数据对应最新数据日期没有数据，无法保存用户群！');
				return;
			}
			//验证sql
			var validateSqlUrl = $.ctx + '/core/ciMainAction!validateSql.ai2do';
			var sign = true;
			$.ajax({
				url		: validateSqlUrl,
				type	: 'POST',
				async	: false,//同步
				data	: parent.$('#shopCartForm').serialize(), 
				success	: function(result){
					flag = result.success;
					if(!flag){
						sign = false;
						coreMain.alertFailed('validateSql', result.msg);
					}
				}
			});
			if(!sign) {return};
			var url = $.ctx + "/ci/customersManagerAction!saveCustomByRulesInit.ai2do";
			window.parent.location.href = url;
		}
	}
	
	return coreMain;
}();