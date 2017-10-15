/**
 * 购物车js
 * 功能：购物车的删除、清空、客户群规模查询、展开规则、设置规则等
 */
ShoppingCart = function() {
	var ShoppingCart = {
		elementObjTypeMap : {
			"1" : "signLabelSetting", 	// 标签类型，1=标志型，用0、1来作为值的；
			"4" : "kpiLabelSetting", 	// 标签类型，4=指标型，存具体的指标值；
			"5" : "enumLabelSetting", 	// 标签类型，5=枚举型，列的值有对应的维表，下拉展示；
			"6" : "dateLabelSetting", 	// 标签类型，6=日期型，字符串类型的日期值；
			"7" : "textLabelSetting", 	// 标签类型，7=模糊型，存字符串，like查询；
			"8" : "vertLabelSetting", 	// 标签类型，8=组合型，对应多个列，数据是纵表存储。
			"10" : "customerSetting" 	// 客户群设置
		},
		/**
		 * 设置规则弹出层
		 * @param obj 点击的对象(设置按钮)
		 * @param elementObjType 元素类型
		 * @param name 元素名称(标签名称或客户群名称)
		 * @param source 来源于购物车或者客户群规则展开：1、购物车；2、客户群规则展开
		 */
		setElementObjAttr : function(obj, elementObjType, name, source) {
			if (!$.isNotBlank(elementObjType)) {
				elementObjType = 10;
			}
			var dialogId = this.elementObjTypeMap[elementObjType];
			var mainObj = null;
			var sort = "";
			if (source == 1) {
				//删除被点击过的样式
				$(".market-cart-of-ul .J_setting").each(function(){
					$(this).removeClass("_idClass");
				});
				mainObj = $(obj);//根据点击DOM节点获得其jQuery对象
				mainObj.addClass("_idClass");
				sort = mainObj.attr("sort");
			} else if (source == 2) {
				$('#tag_dropable .conditionCT').each(function(){
					$(this).find("a").removeClass("_idClass");
				});
				mainObj = $(obj).parents('.conditionCT');
				mainObj.children('.left_bg').children('a').addClass("_idClass");
			}
			
			var elementType = mainObj.attr("elementType");
			/*if (elementObjType == 10) {
				elementType = 5;
			}*/
			
			if (elementObjType == "1") { // 标识型，即0或者1
				var labelFlag = mainObj.attr("labelFlag");
				var para = "?ciLabelRule.labelFlag=" + labelFlag
							+ "&ciLabelRule.labelTypeId=" + elementObjType 
							+ "&elementType=" + elementType + "&source=" + source;
				
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do" + para;
				
				dialogUtil.create_dialog(dialogId, {
					"title" 		: name + "-条件设置",
					"height"		: "auto",
					"width" 		: 530,
					"frameSrc" 		: ifmUrl,
					"frameHeight"	: 140,
					"position" 		: ['center','center'] 
				});
			} else if (elementObjType == "4") { // 指标型，存具体的指标值；
				var queryWay = mainObj.attr("queryWay");
				var contiueMinVal = mainObj.attr("contiueMinVal");
				var contiueMaxVal = mainObj.attr("contiueMaxVal");
				var leftZoneSign = mainObj.attr("leftZoneSign");
				var rightZoneSign = mainObj.attr("rightZoneSign");
				var exactValue = mainObj.attr("exactValue"); 
				var unit = mainObj.attr("unit");
				unit = encodeURIComponent(encodeURIComponent(unit));
				
				var para = "?ciLabelRule.queryWay=" + queryWay + "&ciLabelRule.contiueMinVal=" + contiueMinVal
							+ "&ciLabelRule.contiueMaxVal=" + contiueMaxVal + "&ciLabelRule.leftZoneSign=" + leftZoneSign
							+ "&ciLabelRule.rightZoneSign=" + rightZoneSign + "&ciLabelRule.exactValue=" + exactValue
							+ "&ciLabelRule.unit=" + unit 
							+ "&ciLabelRule.labelTypeId=" + elementObjType 
							+ "&elementType=" + elementType + "&source=" + source;
				
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do" + para;
				dialogUtil.create_dialog(dialogId, {
					"title" 		: name + "-条件设置",
					"height"		: "auto",
					"width" 		: 530,
					"frameSrc" 		: ifmUrl,
					"frameHeight"	: 241,
					"position" 		: ['center','center'] 
				});
				/*
				var actionUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do"
				$.ajax({
			        url:actionUrl,
			        type:"POST",
			        dataType: "html",
			        data: {
			        	"elementType":elementType,
			        	"ciLabelRule.labelTypeId":elementObjType,
			        	"ciLabelRule.queryWay":queryWay,
			        	"ciLabelRule.contiueMinVal":contiueMinVal,
						"ciLabelRule.contiueMaxVal":contiueMaxVal,
						"ciLabelRule.leftZoneSign":leftZoneSign,
						"ciLabelRule.rightZoneSign":rightZoneSign,
						"ciLabelRule.exactValue":exactValue,
						"ciLabelRule.unit":unit
			        },
			        success:function(html){
			        	$("body").append("<div id=" + dialogId + " style='display:none;'></div>");
			        	$("#" + dialogId).html(html).dialog({
			        		resizable 	: false,
			        		modal 		: true,
			        		height 		: 270,
			        		width 		: 530,
			        		title 		: name + "-条件设置",
			        		autoOpen 	: false,
			        		close: function(event, ui){
			        			$("#" + dialogId).remove();
			        		}
			        	}).dialog("open");
			        }
				});
				*/
			} else if(elementObjType == "5") {  //条件选择
				var attrVal = mainObj.attr("attrVal");
				var attrName = mainObj.attr("attrName");
				var calcuElement = mainObj.attr("calcuElement");
				$("#" + dialogId).dialog("option", "title", name+"-条件设置");
				
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do";
				var form = '<form id="postData_form" action="' + ifmUrl + '" method="post" target="_self">' + 
					'<input name="ciLabelRule.attrVal" type="hidden" value="' + attrVal + '"/>' +
					'<input name="ciLabelRule.attrName" type="hidden" value="' + attrName + '"/>' +
					'<input name="ciLabelRule.calcuElement" type="hidden" value="' + calcuElement + '"/>' +
					'<input name="ciLabelRule.labelTypeId" type="hidden" value="' + elementObjType + '"/>' +
					'<input name="elementType" type="hidden" value="' + elementType + '"/>' +
					'<input name="source" type="hidden" value="' + source + '"/>' +
					'</form>';
				document.getElementById('enumLabelSettingFrame').contentWindow.document.write(form);
				document.getElementById('enumLabelSettingFrame').contentWindow.document.getElementById("postData_form").submit();
				$("#" + dialogId).dialog("open");
				/*
				var actionUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do"
				$.ajax({
			        url:actionUrl,
			        type:"POST",
			        dataType: "html",
			        data: {
			        	"elementType":elementType,
			        	"ciLabelRule.labelTypeId":elementObjType,
			        	"ciLabelRule.attrVal":attrVal,
			        	"ciLabelRule.attrName":attrName,
						"ciLabelRule.calcuElement":calcuElement
			        },
			        success:function(html){
			        	$("body").append("<div id=" + dialogId + " style='display:none;'></div>");
			        	$("#" + dialogId).html(html).dialog({
			        		resizable 	: false,
			        		modal 		: true,
			        		height 		: 490,//500
			        		width 		: 710,//710
			        		title 		: name + "-条件设置",
			        		autoOpen 	: true,
			        		close: function(event, ui){
			        			$("#" + dialogId).remove();
			        		}
			        	});//.dialog("open");
			        }
				});*/
				
				
			} else if(elementObjType == "6") {//日期类型标签
				var startTime = "" ;
				var endTime ="" ;
				var leftZoneSign ="" ;
				var rightZoneSign = "" ;
				var isNeedOffset = mainObj.attr('isNeedOffset');
				startTime = mainObj.attr("startTime");
				endTime = mainObj.attr("endTime");
				leftZoneSign = mainObj.attr("leftZoneSign");
				rightZoneSign = mainObj.attr("rightZoneSign");
				queryWay = mainObj.attr("queryWay");
				exactValue = mainObj.attr("exactValue");
				
				var para = "?ciLabelRule.startTime=" + startTime + "&ciLabelRule.endTime=" + endTime + "&ciLabelRule.leftZoneSign=" + leftZoneSign
							+ "&ciLabelRule.rightZoneSign=" + rightZoneSign + "&ciLabelRule.queryWay=" + queryWay
							+ "&ciLabelRule.exactValue=" + exactValue + "&ciLabelRule.isNeedOffset=" + isNeedOffset
							+ "&ciLabelRule.labelTypeId=" + elementObjType 
							+ "&elementType=" + elementType + "&source=" + source;
				
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do" + para;
				dialogUtil.create_dialog(dialogId, {
					"title" 		: name + "-条件设置",
					"height"		: "auto",
					"width" 		: 570,
					"frameSrc" 		: ifmUrl,
					"frameHeight"	: 290,
					"position" 		: ['center','center'] 
				});			
			} else if(elementObjType == "7") {
				var darkValue = "" ;
				darkValue = mainObj.attr("darkValue");
				darkValue = encodeURIComponent(encodeURIComponent(darkValue));

				var queryWay = mainObj.attr("queryWay"); 
				var exactValue = mainObj.attr("exactValue"); 
				exactValue = encodeURIComponent(encodeURIComponent(exactValue));
				
				var para = "?ciLabelRule.darkValue=" + darkValue + "&ciLabelRule.exactValue=" + exactValue
							+ "&ciLabelRule.queryWay=" + queryWay
							+ "&ciLabelRule.labelTypeId=" + elementObjType 
							+ "&elementType=" + elementType + "&source=" + source;
				
				/* dialogUtil.create_dialog("darkValueSet", {
					"title" 		: name + "-条件设置",
					"height"		: "auto",
					"width" 		: 570,
					"frameSrc" 		: ifmUrl,
					"frameHeight"	: 290,
					"position" 		: ['center','center'] 
				}); */
				$("#" + dialogId).dialog("option", "title", name+"-条件设置");
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do" + para;
				$("#textLabelSettingFrame").attr("src", ifmUrl).load();
				$("#" + dialogId).dialog("open");
			} else if(elementObjType == '8') {//组合标签
				var calcuElement = mainObj.attr("calcuElement");
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do?ciLabelRule.calcuElement=" + calcuElement
								+ "&ciLabelRule.labelTypeId=" + elementObjType 
								+ "&elementType=" + elementType + "&source=" + source;
				/* dialogUtil.create_dialog("verticalLabelSetDialog", {
					"title" 		: name + "-条件设置",
					"height"		: "auto",
					"width" 		: 720,
					"frameSrc" 		: ifmUrl,
					"frameHeight"	: 480,
					"position" 		: ['center','center'] 
				}); */
				$("#" + dialogId).dialog("option", "title", name+"-条件设置");
				$("#vertLabelSettingFrame").attr("src", ifmUrl).load();
				$("#" + dialogId).dialog("open");
			} else if(elementObjType == "10") {
				//var expandType = 0;//0、使用清单；1、使用规则
				//var detailedListDate;//清单日期
				var valueId = mainObj.attr("customId");
				var dataDate = mainObj.attr("attrVal");
				var isHasList = mainObj.attr('isHasList');
				
				var para="?ciLabelRule.customId="+valueId+"&ciLabelRule.dataDate="+dataDate+"&sort="+sort+"&ciLabelRule.isHasList="+isHasList;
				var ifmUrl = $.ctx + "/core/ciMainAction!openElementSettingDialog.ai2do" + para
					+ "&ciLabelRule.labelTypeId=" + elementObjType 
					+ "&elementType=" + elementType + "&source=" + source;
				if(isHasList == 1) {
					dialogUtil.create_dialog(dialogId, {
						"title" 		: name + "-条件设置",
						"height"		: "auto",
						"width" 		: 530,
						"frameSrc" 		: ifmUrl,
						"frameHeight"	: 275,
						"position" 		: ['center','center'] 
					});
				} else {
					if(source == 1) {
						$.ajax({
							url		: ifmUrl,
							type	: "POST",
							success	: function(data){
								$("#tag_dropable").show();
								var target = $(".J_setting[sort='"+sort+"']");
								if (source == 1) {
									$("#tag_dropable").html(data);
									//删除括号上的‘x’
									$(".delCondition").remove();
									coreMain.expandCustomRules(target);
								}
							}
						});
					} else {
						dialogUtil.create_dialog(dialogId, {
							"title" 		: name + "-条件设置",
							"height"		: "auto",
							"width" 		: 530,
							"frameSrc" 		: ifmUrl,
							"frameHeight"	: 275,
							"position" 		: ['center','center'] 
						});
					}
				}
				
			} else {
				showAlert("计算元素类型错误！", "failed");
			}
		},
		/**
		 * iframe 方式 子调用父方法 关闭弹出框
		 * @param obj 要关闭的dialog的id
		 */
		iframeCloseDialog : function(obj) {
			$("._idClass").removeClass("_idClass");
			$(obj).dialog("close");
		}
	}
	return ShoppingCart;
}();