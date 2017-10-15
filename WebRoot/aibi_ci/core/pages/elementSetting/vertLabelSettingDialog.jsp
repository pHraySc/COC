<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/aibi_ci/html_include.jsp"%>
<style>
/*隐藏分页总记录数 和 跳转*/
.dialogPageBox .total,.dialogPageBox ul li.jump,.dialogPageBox ul li.num_jump_box
	{
	display: none;
}
.unitColor{padding:0px 5px;color:#ff0000;}
.date-setting-box .right-box li{margin:0px ;padding:0px;}
.tootip-table {border-collapse:collapse;border-spacing:0;border-left:1px solid #d4d4d4;border-top:1px solid #4c4c4c;background:#FFF;}
.tootip-table th,.tootip-table td{border-right:1px solid #d4d4d4;border-bottom:1px solid #d4d4d4;padding:5px 15px;white-space: nowrap;}
.tootip-table th {background-color:#f1f1f1;height:30px;border:1px solid #d4d4d4;font-weight:bold;}
</style>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/jquery.uploadify.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/sortTag.js"></script>

<script type="text/javascript">
	$(function() {
		//默认显示的标签
		var labelTypeId = $('#firstLabelType').val();
		var columnId = $('#firstLabelColumnId').val();
		$('#labelElement' + labelTypeId + '' + columnId).show();
		accordingCurrentSize(labelTypeId);
		//导航切换选择方式
		$('#switchImportWayBox div').click(function() {
			$('#switchImportWayBox div').removeClass('current');
			$(this).addClass('current');
			$('.importWayBox').hide();
			hideEnumPage();
			var labelTypeId = $(this).attr('labelTypeId');
			var columnId = $(this).attr('childLabelId');
			accordingCurrentSize(labelTypeId);
			$('#labelElement' + labelTypeId + '' + columnId).show();
		})
		$(document).click(function() {
			$('#enumTip').hide(500);
		})
		$('#switchImportWayBox div').each(function(index, el) {
			var labelTypeId 		= $(el).attr('labelTypeId');
			var childCalcuElementId = $(el).attr('childLabelId');
			var unit 				= $(el).attr('unit');
			var isExist 			= isExistsChildLabel(childCalcuElementId);
			var childCalcEl			= $(getChildLabel(childCalcuElementId));
			if(labelTypeId == 4) {
				var queryWay = 1;
				//$('#unit' + childCalcuElementId).html(unit);
				$(".unitColor").html(unit);
				//$('#unitExact' + childCalcuElementId).html(unit);
				if(isExist) {
					queryWay = childCalcEl.attr('queryWay');
					if(queryWay == 1) {
						var contiueMinVal	= childCalcEl.attr('contiueMinVal');
						var contiueMaxVal	= childCalcEl.attr('contiueMaxVal');
						var leftClosed		= childCalcEl.attr('leftZoneSign');
						var rightClosed		= childCalcEl.attr('rightZoneSign');
						if(contiueMinVal){
							$('#contiueMinVal' + childCalcuElementId).val(contiueMinVal);
						}
						if(contiueMaxVal){
							$('#contiueMaxVal' + childCalcuElementId).val(contiueMaxVal);
						}
						if(leftClosed){
							if(leftClosed.indexOf("=")>=0){
								$('#leftClosed'  + childCalcuElementId).attr("checked" ,true);
							}
						}
						if(rightClosed){
							if(rightClosed.indexOf("=")>=0){
								$('#rightClosed' + childCalcuElementId).attr("checked" ,true);
							}
						} 
					} else {
						var exactValue = childCalcEl.attr('exactValue');
						if(exactValue){
							$('#exactValue' + childCalcuElementId).val(exactValue);
						}
					}
				}
				
				$('.queryMethodControl' + childCalcuElementId).each(function(){
					if($(this).attr('value') == queryWay){
						$(this).addClass('current');
					}else{
						$(this).removeClass('current');
					}
				});
				if(queryWay == 1){
					$('#exactValue' + childCalcuElementId).attr('disabled','disabled');
					$('#contiueMinVal' + childCalcuElementId).removeAttr('disabled');
					$('#contiueMaxVal' + childCalcuElementId).removeAttr('disabled');
					$('#leftClosed' + childCalcuElementId).removeAttr('disabled');
					$('#rightClosed' + childCalcuElementId).removeAttr('disabled');
				}else{
					$('#contiueMinVal' + childCalcuElementId).attr('disabled','disabled');
					$('#contiueMaxVal' + childCalcuElementId).attr('disabled','disabled');
					$('#leftClosed' + childCalcuElementId).attr('disabled','disabled');
					$('#rightClosed' + childCalcuElementId).attr('disabled','disabled');
					$('#exactValue' + childCalcuElementId).removeAttr('disabled');
				}
				$('.queryMethodControl' + childCalcuElementId).click(function() {
					$('.queryMethod').removeClass('current');
					$(this).addClass('current');
					$('.queryMethod').parent().siblings('dd').find('input').attr('disabled', 'disabled');
					$('.current').parent().siblings('dd').find('input').removeAttr('disabled');
				});
				$(".queryMethod").hover(function(){
					$(this).addClass("hover");
				},function(){
					$(this).removeClass("hover");
				});

				var cmin = $.trim($('#contiueMinVal' + childCalcuElementId).val());
				if (cmin == '输入下限') {
					$.focusblur('#contiueMinVal' + childCalcuElementId);
				}
				var cmax = $.trim($('#contiueMaxVal' + childCalcuElementId).val());
				if (cmax == '输入上限') {
					$.focusblur('#contiueMaxVal' + childCalcuElementId);
				}
			} else if(labelTypeId == 5) {
				if(isExist) {
					var attrVal 	= childCalcEl.attr('attrVal');
					var attrName 	= childCalcEl.attr('attrName');
					var itemKeyArr 	= [];
					var itemNameArr = [];
					if(attrVal) {
						itemKeyArr = attrVal.split(',');
						itemNameArr = attrName.split(',');
						$('#addItemDetailBox' + childCalcuElementId).empty();
						for ( var i = 0, len; i < itemKeyArr.length; i++) {
							var $liTemp = $('<li><a href="javascript:void(0);" ondblclick="delEnumItemByDbClick(\'' 
									+ childCalcuElementId + '\',this);" id="'
									+ itemKeyArr[i] + '" data="' + itemKeyArr[i] + '">'
									+ itemNameArr[i] + '</a></li>');
							$('#addItemDetailBox' + childCalcuElementId).append($liTemp);
						}
					}
					$('#selectedItemSize' + childCalcuElementId).html(attrVal.split(',').length);
				} else {
					$('#selectedItemSize' + childCalcuElementId).html(0);
				}
				
				var calcuElement = $(this).attr('childLabelId');
				$('#calcuElement' + childCalcuElementId).val(calcuElement);
				// 添加和删除操作
				$('#addItemAction' + childCalcuElementId).click(function() {
					addEnumItem(childCalcuElementId);
				});
				$('#removeItemAction' + childCalcuElementId).click(function() {
					$('#addItemDetailBox' + childCalcuElementId).find('.itemLiNextAHover').parent().remove();
					calcEnumChooseItemNum(childCalcuElementId);
				});
				// 添加和删除操作
				$('#addAllItemAction' + childCalcuElementId).click(function() {
					$("#_loading"+childCalcuElementId).show();
					var dimName = $("#dimName" + childCalcuElementId).val();
					var calcuElement = $("#calcuElement" + childCalcuElementId).val();
					var enumCategoryId=$("#enumCategoryId"+childCalcuElementId).val();
					var para = {
						"columnId" : calcuElement,
						"dimName" : dimName,
						"enumCategoryId" : enumCategoryId
					};
					var actionUrl = '${ctx}/ci/ciLabelInfoAction!findAllLabelDimValueByColumnId.ai2do';
					$.ajax({
						url:actionUrl,
						type:"POST",
						dataType: "json",
						data: para,
						success:function(result){
							if(result.success){
								var dimValueList = result.dimValueList;
								var $liTemp = '';
								$.each(dimValueList, function (n, value) {  
									$('#addItemDetailBox' + childCalcuElementId).find('a[id="'+value.V_KEY+'"]').parent().remove();
									$liTemp += '<li><a href="javascript:void(0);" ondblclick="delEnumItemByDbClick(\'' 
											+ childCalcuElementId + '\',this);" ' 
											+ 'onclick="aAddOrRemoveClass(this);" id="'
											+ value.V_KEY
											+ '" data="'
											+ value.V_KEY
											+ '" >'
											+ value.V_NAME + '</a></li>';
					          	}); 
								$('#addItemDetailBox' + childCalcuElementId).append($liTemp);
								calcEnumChooseItemNum(childCalcuElementId);
								$("#_loading"+childCalcuElementId).hide();
							}else{
								$("#_loading"+childCalcuElementId).hide();
								$.fn.weakTip({"tipTxt":result.msg});
							}
						},
						error:function(){
							$("#_loading"+childCalcuElementId).hide();
							$.fn.weakTip({"tipTxt":'获取所有枚举类型标签的维值失败!'});
						}
					});
				});
				$('#removeAllItemAction' + childCalcuElementId).click(function() {
					$('#addItemDetailBox' + childCalcuElementId).empty();
					$('#selectedItemSize' + childCalcuElementId).html(0);
				});
				//添加选中效果
				$('#addItemDetailBox' + childCalcuElementId + ' a, #itemChooseDetailBox' + childCalcuElementId + ' a').click(function() {
					if ($(this).hasClass('itemLiNextAHover')) {
						$(this).removeClass('itemLiNextAHover');
					} else {
						$(this).addClass('itemLiNextAHover');
					}
				});
				serchItemList(childCalcuElementId);
				
				initEnumCategorys(childCalcuElementId);
				
				//导入按钮事件
				$('#file_upload' + childCalcuElementId).uploadify({
					buttonClass	: 'importBtn',
					btnBoxClass : 'fright',
					buttonText	: '导入',
					width		: 60,
					height		: 24,
					fileObjName	: 'file',
					auto		: true,//为true当选择文件后就直接上传了，为false需要点击上传按钮才上传
					multi		: true,
					itemTemplate: true,
					fileTypeExts: '*.txt;*.csv',
					fileTypeDesc: '导入文件格式只能是txt或者csv格式！',
			        swf			: '${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.swf',
			        uploader	: '${ctx}/ci/ciIndexAction!findEnumByImport.ai2do?columnId=' + childCalcuElementId,
			        onUploadSuccess	: function(file, data, response) {
			        	var success = $.parseJSON(data).success;
			        	if(!success) {
			        		showAlert($.parseJSON(data).msg, 'failed');
			        		return;
			        	}
			        	var selectedIds = [];
			        	var selectedItems = $('#addItemDetailBox' + childCalcuElementId).html();
			        	
			        	$(selectedItems).each(function(index, item) {
			        		selectedIds.push($.trim($(item).children('a').attr('id')));
			        	});
			        	var html = '';
			        	var dataObj = $.parseJSON(data).data;
						for(var i=0; i<dataObj.length; i++) {
							var item = dataObj[i];
							if(!arrayContain(selectedIds, $.trim(item.V_KEY))) {
								html += '<li><a href="javascript:void(0);" ondblclick="delEnumItemByDbClick(\'' 
				        			+ childCalcuElementId + '\', this);" onclick="aAddOrRemoveClass(this);" ' 
				        			+ 'id="' + item.V_KEY + '" data="' + item.V_KEY + '" title="' + item.V_NAME + '">'
				        			+ item.V_NAME + '</a></li>';
							}
						}
						$('#addItemDetailBox' + childCalcuElementId).append(html);
						calcEnumChooseItemNum(childCalcuElementId);
						removeHover(childCalcuElementId);
			        }
			    });
				
				var tableChild = '';
				var items = $('#itemChooseDetailBox'  + childCalcuElementId).children('li');
				if(items) {
					for(var i=0; i<items.length; i++) {
						var item = $(items[i]);
						tableChild += '<tr><td>' + item.children('a').attr('data') + '</td><td>' + item.text() + '</td></tr>';
					}
				}
				$('#importTip'  + childCalcuElementId).click(function() {
					var e=e||window.event;
					e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
					
					if($('#enumTip').is(":visible")) {
						$('#enumTip').hide(500);
						return;
					}
					
					var displayEl = '<table class="tootip-table"><tr><th>ID(导入的值)</th><th>显示值</th></tr>';
					displayEl += tableChild;
					displayEl += '</table>';
					$('#enumTip').html(displayEl).show(500);
					var posy = $(this).offset().top + $(this).height()+4;
					var posx = $(this).offset().left - $(".tootip-table").width()+22;
					$('#enumTip').css({'left' : posx + "px", 'top' : posy + "px","zIndex":"999"});
				});
			} else if(labelTypeId == 6) {
				var startTime		= childCalcEl.attr('startTime');
				var endTime			= childCalcEl.attr('endTime');
				var dateLeftClosed 	= childCalcEl.attr('leftZoneSign');
				var dateRightClosed = childCalcEl.attr('rightZoneSign');
				var queryWay 		= childCalcEl.attr('queryWay');//区分是日期范围还是精确日期
				var exactValueDate 	= childCalcEl.attr('exactValue'); //精确日期 yyyy,mm,dd 没有选的也填充为-1
				var isNeedOffset	= childCalcEl.attr('isNeedOffset');
				
				if(isNeedOffset == 1) {
					$('#dynamicUpdate'+ childCalcuElementId).attr('checked', 'checked');
				}
				
				if(!queryWay){
					queryWay="1";
				}
				if(queryWay){
					$(".methodSetControl" + childCalcuElementId).each(function(){
						if($(this).attr("value") == queryWay){
							$(this).addClass("methodSelCurrent");
						}else{
							$(this).removeClass("methodSelCurrent");
						}
					});
					if(queryWay == "1"){
						
						$("#exactValueDateYear"+ childCalcuElementId).attr("disabled","disabled");
						$("#exactValueDateMonth"+ childCalcuElementId).attr("disabled","disabled");
						$("#exactValueDateDay"+ childCalcuElementId).attr("disabled","disabled");
						
						$("#startTime"+ childCalcuElementId).removeAttr("disabled");
						$("#endTime"+ childCalcuElementId).removeAttr("disabled");
						$("#dynamicUpdate"+ childCalcuElementId).removeAttr("disabled");
						$("#dateLeftClosed"+ childCalcuElementId).removeAttr("disabled");
						$("#dateRightClosed"+ childCalcuElementId).removeAttr("disabled");
					}else{
						$("#startTime"+ childCalcuElementId).attr("disabled","disabled");
						$("#endTime"+ childCalcuElementId).attr("disabled","disabled");
						$("#dynamicUpdate"+ childCalcuElementId).attr("disabled","disabled");
						$("#dateLeftClosed"+ childCalcuElementId).attr("disabled","disabled");
						$("#dateRightClosed"+ childCalcuElementId).attr("disabled","disabled");
						
						$("#exactValueDateYear"+ childCalcuElementId).removeAttr("disabled");
						$("#exactValueDateMonth"+ childCalcuElementId).removeAttr("disabled");
						$("#exactValueDateDay"+ childCalcuElementId).removeAttr("disabled");
					}
				}
				if(isExist) {
					if(queryWay == "1"){
						if(startTime){
							$('#startTime' + childCalcuElementId).val(startTime);
						}
						if(endTime){
							$('#endTime' + childCalcuElementId).val(endTime);
						}
						if(dateLeftClosed.indexOf('=')>=0){
							$('#dateLeftClosed' + childCalcuElementId).attr('checked' ,true);
						}
						if(dateRightClosed.indexOf('=')>=0){
							$('#dateRightClosed' + childCalcuElementId).attr('checked' ,true);
						}
					}else{
						//精确值
						if(exactValueDate){
							var itemValueArr = exactValueDate.split(",");
							if(itemValueArr.length == 3){
								var exactValueDateYear = itemValueArr[0];
								var exactValueDateMonth = itemValueArr[1];
								var exactValueDateDay =itemValueArr[2];
								if(exactValueDateYear && exactValueDateYear != "-1"){
									$("#exactValueDateYear"+ childCalcuElementId).val(exactValueDateYear);
								}
								if(exactValueDateMonth && exactValueDateMonth != "-1"){
									$("#exactValueDateMonth"+ childCalcuElementId).val(exactValueDateMonth);
								}
								if(exactValueDateDay && exactValueDateDay != "-1"){
									$("#exactValueDateDay"+ childCalcuElementId).val(exactValueDateDay);
								}
							}
						}
					}
				}
				//日期单选按钮选项${item.id.columnId}
				$(".methodSetControl" + childCalcuElementId).click(function(){
					$(".methodSetControl" + childCalcuElementId).removeClass("methodSelCurrent");
					$(this).addClass("methodSelCurrent");
					var queryWay = $(this).attr("value");
					if(queryWay == "1"){
						$("#exactValueDateYear"+ childCalcuElementId).attr("disabled","disabled");
						$("#exactValueDateMonth"+ childCalcuElementId).attr("disabled","disabled");
						$("#exactValueDateDay"+ childCalcuElementId).attr("disabled","disabled");
						
						$("#startTime"+ childCalcuElementId).removeAttr("disabled");
						$("#dynamicUpdate"+ childCalcuElementId).removeAttr("disabled");
						$("#endTime"+ childCalcuElementId).removeAttr("disabled");
						$("#dateLeftClosed"+ childCalcuElementId).removeAttr("disabled");
						$("#dateRightClosed"+ childCalcuElementId).removeAttr("disabled");
					}else{
						$("#startTime"+ childCalcuElementId).attr("disabled","disabled");
						$("#endTime"+ childCalcuElementId).attr("disabled","disabled");
						$("#dynamicUpdate"+ childCalcuElementId).attr("disabled","disabled");
						$("#dateLeftClosed"+ childCalcuElementId).attr("disabled","disabled");
						$("#dateRightClosed"+ childCalcuElementId).attr("disabled","disabled");

						$("#exactValueDateYear"+ childCalcuElementId).removeAttr("disabled");
						$("#exactValueDateMonth"+ childCalcuElementId).removeAttr("disabled");
						$("#exactValueDateDay"+ childCalcuElementId).removeAttr("disabled");
					}
					$("#dateSnameTip" + childCalcuElementId).hide();
				})
				$(".methodSel").hover(function(){
					$(this).addClass("methodSelHover");
				},function(){
					$(this).removeClass("methodSelHover");
				});
			} else if(labelTypeId == 7) {
				var darkValue = childCalcEl.attr('darkValue');
				var queryWay = childCalcEl.attr('queryWay');//区分是模糊值还是精确值
				var exactValue = childCalcEl.attr('exactValue'); //精确值

				if(!queryWay){
					queryWay="1";
				}
				if(queryWay){
					$(".dimMatchMethodControl" + childCalcuElementId).each(function(){
						if($(this).attr("value") == queryWay){
							$(this).addClass("current");
						}else{
							$(this).removeClass("current");
						}
					});
				}
				if(queryWay == "1"){
					$("#exactValue" + childCalcuElementId).attr("disabled","disabled");
					$("#darkValue" + childCalcuElementId).removeAttr("disabled");
					$("#importFontStyleBox" + childCalcuElementId).html("输入您想查询的任意内容");
					
					if(darkValue){
						$("#darkValue" + childCalcuElementId).val(darkValue);
					}
				}else{
					$("#darkValue" + childCalcuElementId).attr("disabled","disabled");
					$("#exactValue" + childCalcuElementId).removeAttr("disabled");
					$("#importFontStyleBox" + childCalcuElementId).html("多条件用','分割,且不能用','结尾");
	 				
					if(exactValue){
						$("#exactValue" + childCalcuElementId).val(exactValue);
					}
				}
				
				//导入按钮事件
				$("#file_upload_dark" + childCalcuElementId).uploadify({
					buttonClass	: "importBtn",
					btnBoxClass : "fright tMargin5",
					buttonText	: "导入",
					width		: 60,
					height		: 24,
					fileObjName	: "file",
					auto		: true,//为true当选择文件后就直接上传了，为false需要点击上传按钮才上传
					multi		: true,
					progressData: "speed",
					itemTemplate: true,
					fileTypeExts: "*.txt;*.csv",
					fileTypeDesc: "导入文件格式只能是txt或者csv格式！",
			        swf			: "${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.swf",
			        uploader	: "${ctx}/ci/ciIndexAction!findLikeLabelByImport.ai2do",
			        onUploadSuccess	: function(file, data, response) {
			        	var success = $.parseJSON(data).success;
			        	if(!success) {
			        		showAlert($.parseJSON(data).msg, 'failed');
			        		return;
			        	}
			        	var dataObj = $.parseJSON(data).data;
			        	var dataResult = "";
			        	/*if(dataObj.length>$._maxDarkLabelExactValue){
			        		var _index = dataObj.lastIndexOf(",",$._maxDarkLabelExactValue);
			        		dataResult = dataObj.substring(0,_index);
			        		$("#newTip"+ childCalcuElementId).html("导入文本过长，已被截取");
			        		$("#newTip"+ childCalcuElementId).show();
				        }else{*/
				        	dataResult = dataObj;
				        	$("#newTip"+ childCalcuElementId).empty();
				        //}
				        $("#exactValue" + childCalcuElementId).val(dataResult);
				        $("#exactValue" + childCalcuElementId).attr("title",dataResult);
			        },
			        onSWFReady:onSWFReady
			    });
				function onSWFReady(){
					if(queryWay){
						if(queryWay == "1"){
			 				$("#file_upload_dark" + childCalcuElementId).uploadify('disable', true);
						}else{
			 				$("#file_upload_dark" + childCalcuElementId).uploadify('disable', false);
						}
					}
				 }
				
				$(".dimMatchMethodControl" + childCalcuElementId).click(function(){
					$(".dimMatchMethod").removeClass("current");
					$(this).addClass("current");
					var queryWayTemp = $(this).attr("value");
					$(".dimMatchMethod").parent().siblings("dd").find("input").attr("disabled","disabled");
					$(".current").parent().siblings("dd").find("input").removeAttr("disabled");
					$("#darkSnameTip" + childCalcuElementId).hide();
					$("#enameTip" + childCalcuElementId).hide();
					$("#newTip"+ childCalcuElementId).hide();
					if(queryWayTemp == "1"){
						$("#file_upload_dark"+ childCalcuElementId).uploadify('disable', true);
						$("#importFontStyleBox"+ childCalcuElementId).html("输入您想查询的任意内容");
					}else{
						$("#file_upload_dark"+ childCalcuElementId).uploadify('disable', false);
						$("#importFontStyleBox"+ childCalcuElementId).html("多条件用','分割,且不能用','结尾");
					}
				});
				$(".dimMatchMethod").hover(function(){
					$(this).addClass("hover");
				},function(){
					$(this).removeClass("hover");
				});
				
			}
		});
		
		//枚举搜索回车查询
		$('.enumItemSearchInput').keyup(function(e) {
    		if(e.keyCode == 13) {
    			$(this).next('.searchBtn').click();
    		}
    	});
		
		$('.enumItemSearchBtn').click(function(e) {
			var columnId = $(this).attr('columnId');
			serchItemList(columnId);
    	});
		
		//已选枚举搜索回车查询
		$('.choosedEnumItemSearchInput').keyup(function(e) {
    		if(e.keyCode == 13) {
    			$(this).next('.searchBtn').click();
    		}
    	});
		
		$('.choosedEnumItemSearchBtn').click(function(e) {
			var columnId = $(this).attr('columnId');
			searchAddItemList(columnId);
    	});
	});
	
	function arrayContain(arr, item) {
		var flag = false;
		for(var i=0; i<arr.length; i++) {
			var arrayItem = $.trim(arr[i]);
			if(arrayItem == item) {
				flag = true;
				break;
			} else {
				continue;
			}
		}
		return flag;
	}
	
	//枚举类型点击添加，添加枚举值
	function addEnumItem(childCalcuElementId) {
		var selectItemList = $('#itemChooseDetailBox' + childCalcuElementId)
				.find('.itemLiNextAHover');
		for ( var i = 0, len = selectItemList.length; i < len; i++) {
			var selectedItem = $(selectItemList[i]);
			$('#addItemDetailBox' + childCalcuElementId).find('a[id="'+selectedItem.attr('id')+'"]').parent()
					.remove();
			var $liTemp = $('<li><a href="javascript:void(0);" ondblclick="delEnumItemByDbClick(\'' 
					+ childCalcuElementId + '\',this);" ' 
					+ 'onclick="aAddOrRemoveClass(this);" id="'
					+ selectedItem.attr('id')
					+ '" data="'
					+ selectedItem.attr('data')
					+ '" >'
					+ selectedItem.html() + '</a></li>');
			$('#addItemDetailBox' + childCalcuElementId).append($liTemp);
		}
		calcEnumChooseItemNum(childCalcuElementId);
		//添加完成后，移除选中效果
		removeHover(childCalcuElementId);
	}
	//双击添加枚举值
	function addEnumItemByDbClick(childCalcuElementId, ths) {
		
		var selectedItem = $(ths);
		$('#addItemDetailBox' + childCalcuElementId).find('a[id="'+selectedItem.attr('id')+'"]').parent()
				.remove();
		var $liTemp = '<li><a href="javascript:void(0);" ondblclick="delEnumItemByDbClick(\'' 
				+ childCalcuElementId + '\',this);" '
				+ 'onclick="aAddOrRemoveClass(this);" id="'
				+ selectedItem.attr('id')
				+ '" data="'
				+ selectedItem.attr('data')
				+ '" >'
				+ selectedItem.html() + '</a></li>';
		$('#addItemDetailBox' + childCalcuElementId).append($liTemp);
		calcEnumChooseItemNum(childCalcuElementId);
		removeHover(childCalcuElementId);
	}
	
	//枚举值移除选中效果
	function removeHover(childCalcuElementId) {
		//添加完成后，移除选中效果
		$('#itemChooseDetailBox' + childCalcuElementId).find('.itemLiNextAHover')
				.addClass('_disabled');
		$('#itemChooseDetailBox' + childCalcuElementId).find('.itemLiNextAHover')
				.removeClass('itemLiNextAHover');
	}
	
	//双击删除枚举值
	function delEnumItemByDbClick(childCalcuElementId, ths) {
		$(ths).parent().remove();
		calcEnumChooseItemNum(childCalcuElementId);
	}
	
	//计算被选中的枚举值的数量
	function calcEnumChooseItemNum(childCalcuElementId) {
		var num = $('#addItemDetailBox' + childCalcuElementId).children().length;
		$('#selectedItemSize' + childCalcuElementId).html(num);
	}
	
	//因为样式原因要单独写一个隐藏枚举类型页面的方法
	function hideEnumPage() {
		$('.enum').hide();
	}
	
	//切换tab页的时候动态调整tab窗体大小
	function accordingCurrentSize(labelTypeId) {
		var iframe = parent.$('#vertLabelSettingFrame');
		var dialog = parent.$('#vertLabelSetting').parent();
		if (labelTypeId == 4) {
			iframe.height(318);
			dialog.width(610);
		} else if (labelTypeId == 5) {
			iframe.height(488);
			dialog.width(720);
		} else if (labelTypeId == 6) {
			iframe.height(338);
			dialog.width(610);
		} else if (labelTypeId == 7) {
			iframe.height(338);
			dialog.width(610);
		}
	}

	//全选和 取消选择操作 
	function setItemStatus(flag, divBoxId) {
		if(flag) {
			$("#" + divBoxId).find("a").addClass("itemLiNextAHover");
		} else {
			$("#" + divBoxId).find("a").removeClass("itemLiNextAHover");
		}
	}

	function pagetable(columnId) {
		var dimName = $("#dimName" + columnId).val();
		var calcuElement = $("#calcuElement" + columnId).val();
		var enumCategoryId=$("#enumCategoryId"+columnId).val();
		var para = {
			"columnId" : calcuElement,
			"dimName" : dimName,
			"enumCategoryId" : enumCategoryId
		};
		var actionUrl = '${ctx}/ci/ciLabelInfoAction!findLabelDimValueByColumnId.ai2do?'
				+ $("#pageForm" + calcuElement).serialize();
		$("#itemChooseListDiv" + calcuElement).page({
			url : actionUrl,
			param : para,
			objId : 'itemChooseListDiv' + calcuElement,
			callback : function() {
				pagetable(calcuElement);
			}
		});
	}

	//模糊查询选中的属性值
	function searchAddItemList(columnId){
		var addDimNameTmp=$("#addDimName" + columnId).val();
		var addDimName = addDimNameTmp.toLowerCase();
		var $itemObjArr= $("#addItemDetailBox" + columnId).find("a");
		for(var i = 0 ,len = $itemObjArr.length ; i< len ; i++){
			var $itemObj = $($itemObjArr[i]);
			var nameVarTmp=$itemObj.html();
			var nameVar = nameVarTmp.toLowerCase();
			if(nameVar.indexOf(addDimName)>=0){
				if(!$itemObj.hasClass("itemLiNextAHover")){
					$itemObj.addClass("itemLiNextAHover");
				}
			}else {
				if($itemObj.hasClass("itemLiNextAHover")){
					$itemObj.removeClass("itemLiNextAHover");
				}
			}
		}
	}
	
	jQuery.focusblur = function(focusid) {
		var focusblurid = $(focusid);
		var defval = focusblurid.val();
		focusblurid.focus(function() {
			var thisval = $(this).val();
			if (thisval == defval) {
				$(this).val("");
			}
		});
		focusblurid.blur(function() {
			var thisval = $(this).val();
			if (thisval == "") {
				$(this).val(defval);
			}
		});
	};

	//查询枚举列表	
	function serchItemList(columnId){
		$("#loading").show();
		var dimName=$("#dimName"+columnId).val();
		var calcuElement=$("#calcuElement"+columnId).val();
		var enumCategoryId=$("#enumCategoryId"+columnId).val();
		var actionUrl = '${ctx}/ci/ciLabelInfoAction!findLabelDimValueByColumnId.ai2do?' + $("#pageForm" + columnId).serialize();
		var para={
			"columnId"		: calcuElement,
			"dimName"		: dimName,
			"enumCategoryId":enumCategoryId
		};
		$.ajax({
			url		: actionUrl,
			type	: "POST",
			async	: false,
			dataType: "html",
			data	: para,
			success	:function(html){
				$("#itemChooseListDiv" + columnId).html("");
	            $("#itemChooseListDiv" + columnId).html(html);
	            pagetable(columnId);
				var totalSize = $('#totalSize' + columnId).val();
	            if(totalSize){
		            $('#totalSizeNum' + columnId).html(totalSize);
		        }
	            $("#loading").hide();
	            
			},
			error:function(){
				$("#loading").hide();
			}
		});
	}
	//点击a标签添加移除选中效果
	function aAddOrRemoveClass(obj){
		if($(obj).hasClass("itemLiNextAHover")){
			$(obj).removeClass("itemLiNextAHover");
		}else{
			$(obj).addClass("itemLiNextAHover");
		}
	}
	function setData() {
		var flag = true;
		var displayContext  = '';
		var displayTitle	= '';
		
		$('#switchImportWayBox div').each(function(index, el) {
			var labelTypeId 	= $(el).attr('labelTypeId');
			var childLabelId 	= $(el).attr('childLabelId');
			var isMustColumn 	= $(el).attr('isMustColumn');
			var parentLabelId 	= $(el).attr('parentLabelId');
			var columnCnName	= $(el).attr('columnCnName');
			var isExist			= isExistsChildLabel(childLabelId);
				
			if(labelTypeId == 4) {
				if(!validateNumValueForm(isMustColumn, childLabelId)) {
					toWarningTag(4, childLabelId);
					flag = false;
					return flag;
				}
				var numContext = setNumberValue(childLabelId, parentLabelId, isExist, columnCnName);
				if(numContext) {
					displayContext	+=  '<label>' + columnCnName + '：</label><span>' + numContext + '</span>；';
					displayTitle	+= columnCnName + '：' + numContext + ';';
				}
			} else if(labelTypeId == 5) {
				if(!validateEnumValueForm(isMustColumn, childLabelId, columnCnName)) {
					toWarningTag(5, childLabelId);
					flag = false;
					return flag;
				}
				var enumContext = setEnumValue(childLabelId, parentLabelId, isExist, columnCnName);
				if(enumContext) {
					displayContext	+=  '<label>' + columnCnName + '：</label><span>' + enumContext + '</span>；';
					displayTitle	+= columnCnName + '：' + enumContext + ';';
				}
			} else if(labelTypeId == 6) {
				if(!validateDateValueForm(isMustColumn, childLabelId)) {
					toWarningTag(6, childLabelId);
					flag = false;
					return flag;
				}
				var dateContext = setDateValue(childLabelId, parentLabelId, isExist, columnCnName);
				if(dateContext) {
					displayContext	+=  '<label>' + columnCnName + '：</label><span>' + dateContext + '</span>；';
					displayTitle	+= columnCnName + '：' + dateContext + ';';
				}
			} else if(labelTypeId == 7) {
				if(!validateDarkValueForm(isMustColumn, childLabelId)) {
					toWarningTag(7, childLabelId);
					flag = false;
					return flag;
				}
				var darkValue = setDarkValue(childLabelId, parentLabelId, isExist, columnCnName);
				if(darkValue) {
					displayContext	+=  '<label>' + columnCnName + '：</label><span>' + darkValue + '</span>；';
					displayTitle	+= columnCnName + '：' + darkValue + ';';
				}
			}
		});
		if(!flag) return;
		var source = $('#source').val();
		if (source == 1) {
			var sort = parent.$('._idClass').attr('sort');
			parent.coreMain.submitShopCartSession(sort);//提交session
		} else if(source == 2) {
			var conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
			if(displayContext) {
				conditionMore.html('').append('<p>' + displayContext + '</p>');
				conditionMore.attr('title', displayTitle);
			}
		}
		
		parent.ShoppingCart.iframeCloseDialog("#vertLabelSetting");
	}
	
	function toWarningTag(typeId, columnId) {
		$('.importWayBox').hide();
		hideEnumPage();
		accordingCurrentSize(typeId);
		$('#labelElement' + typeId + '' + columnId).show();
		$('#switchImportWayBox div').removeClass('current');
		$('#switchImportWayBox div').each(function() {
			if($(this).attr('childLabelId') == columnId) {
				$(this).addClass('current');
			}
		})
	}
	
	//判断是否已经存在对应的子标签，如果存在则对div属性进行重新修改，如果不存在则新加div
	function isExistsChildLabel(childLabelId) {
		var flag = false;
		
		var nextElList = parent.$('._idClass').children();
		
		var source = $("#source").val();
		if (source == 2) {
			nextElList = parent.$("._idClass").parent().parent().children();
		}
		for(var i=0; i<nextElList.length; i++) {
			var el = nextElList[i];
			if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	function getChildLabel(childLabelId) {
		var result;
		var nextElList = parent.$('._idClass').children();
		
		var source = $("#source").val();
		if (source == 2) {
			nextElList = parent.$("._idClass").parent().parent().children();
		}
		
		for(var i=0; i<nextElList.length; i++) {
			var el = nextElList[i];
			if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
				result = el;
				break;
			}
		}
		return result;
	}
	
	//表单验证
	function validateNumValueForm(isMustColumn, columnId){
		var queryWay="";
		$(".queryMethodControl" + columnId).each(function(){
			if($(this).hasClass("current")){
				queryWay= $(this).attr("value")
			}
		});
		
		var contiueMinVal	= $.trim($("#contiueMinVal" + columnId).val());
		var contiueMaxVal	= $.trim($("#contiueMaxVal" + columnId).val());
		if(contiueMinVal == "输入下限"){
			contiueMinVal = "";
		}
		if(contiueMaxVal == "输入上限") {
			contiueMaxVal = "";
		}
		var exactValue = $.trim($("#exactValue" + columnId).val());
		//判空
		if(isMustColumn == 1) {
			if(queryWay == 1) {
				var contiueVal = contiueMinVal + contiueMaxVal;
				if($.trim(contiueVal) == ""){
					$("#numCnameTip" + columnId).empty();
					$("#numCnameTip" + columnId).show().append("请输入数值！");
					return false;
				}
			} else {
				if(exactValue == ""){
					$("#numSnameTip" + columnId).empty();
					$("#numSnameTip" + columnId).show().append("精确值不能为空！");
					return false;
				}
			}
		}
		
		if(queryWay == 1){
			var reg = /^(-?([1-9]\d{0,11})|0)(\.\d{1,4})?$/
			if(contiueMinVal !=""){
				if(!contiueMinVal.match(reg)){
					$("#numCnameTip" + columnId).empty();
					$("#numCnameTip" + columnId).show().append("请输入合法的数值！");
					return false;
				}
			}
			if(contiueMaxVal !=""){
				if(!contiueMaxVal.match(reg)){
					$("#numCnameTip" + columnId).empty();
					$("#numCnameTip" + columnId).show().append("请输入数值！");
					return false;
				}
			}
			if(contiueMinVal !="" && contiueMaxVal !=""){
				//var maxVal=parseInt($("#contiueMaxVal").val());
				//var minVal=parseInt($("#contiueMinVal").val());
				var minVal	= parseInt($("#contiueMinVal" + columnId).val());
				var maxVal	= parseInt($("#contiueMaxVal" + columnId).val());
				if(maxVal < minVal){
					$("#numCnameTip" + columnId).empty();
					$("#numCnameTip" + columnId).show().append("数值范围上限不能小于下限！");
					return false;
				}
			}
		} else {
			var reg = /^(-?([1-9]\d{0,11})|0)(\.\d{1,4})?(\,(-?([1-9]\d{0,11})|0)(\.\d{1,4})?)*$/;
			if(exactValue !=""){
				if(!exactValue.match(reg)){
					$("#numSnameTip" + columnId).empty();
					$("#numSnameTip" + columnId).show().append("请输入合法的数值！");
					return false;
				}
			}
		}
		return true;
	}
	
	
	function numIsNotNull(queryWay, contiueMinVal, contiueMaxVal, exactValue) {
		var flag = true;
		if(queryWay == 1) {
			var contiueVal = contiueMinVal + contiueMaxVal;
			if($.trim(contiueVal) == ""){
				flag = false;
			}
		} else {
			if(exactValue == ""){
				flag = false;
			}
		}
		return flag;
	}
	
	function setNumberValue(childLabelId, parentLabelId, isExist, columnCnName){
		var queryWay		= '';
		var contiueMinVal 	= '';
		var contiueMaxVal 	= '';
		var exactValue 		= '';
		var leftZoneSign	= '';
		var rightZoneSign	= '';
		var unit			= '';
		var temp			= '';
		
		$(".queryMethodControl" + childLabelId).each(function() {
			if($(this).hasClass('current')) {
				queryWay = $(this).attr('value')
			}
		});
		if(queryWay == 1) {
			contiueMinVal = $('#contiueMinVal' + childLabelId).val();
			contiueMaxVal = $('#contiueMaxVal' + childLabelId).val();
			//unit = $('#unit' + childLabelId).html();
			unit = $(".unitColor").html();
			if(contiueMinVal == '输入下限') {
				contiueMinVal = '';
			}
			if(contiueMaxVal == '输入上限') {
				contiueMaxVal = '';
			}
			
			var leftChecked = $('#leftClosed' + childLabelId).attr('checked');
			if(leftChecked) {
				leftZoneSign = '>=';
				if(contiueMinVal !=""){
					temp  +="大于等于" +contiueMinVal ;
				}
			} else {
				leftZoneSign = '>';
				if(contiueMinVal !=""){
					temp +="大于" +contiueMinVal;
				}
			}
			
			var rightChecked=$('#rightClosed' + childLabelId).attr('checked');
			if(rightChecked) {
				rightZoneSign = '<=';
				if(contiueMaxVal !=""){
					temp +="小于等于" +contiueMaxVal ;
				}
			} else {
				rightZoneSign = '<';
				if(contiueMaxVal !=""){
					temp +="小于" +contiueMaxVal;
				}
			}
		}
		if(queryWay == 2){
			exactValue = $('#exactValue' + childLabelId).val();
			//unit = $('#unitExact' + childLabelId).html();
			unit = $(".unitColor").html();
			temp += exactValue;
		}
		if(unit && temp) {
			temp += '(' + unit + ')';
		}
		
		var source = $("#source").val();
		var nextElList = parent.$('._idClass');
		if (source == 2) {
			nextElList = parent.$("._idClass").parent().parent();
		}
		
		if(isExist) {
			nextElList.children().each(function (index, el) {
				if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
					if(numIsNotNull(queryWay, contiueMinVal, contiueMaxVal, exactValue)) {
						$(el).attr('parentId', 		parentLabelId);
						$(el).attr('calcuElement', 	childLabelId);
						$(el).attr('contiueMinVal', contiueMinVal);
						$(el).attr('contiueMaxVal', contiueMaxVal);
						$(el).attr('rightZoneSign', rightZoneSign);
						$(el).attr('leftZoneSign', 	leftZoneSign);
						$(el).attr('exactValue', 	exactValue);
						$(el).attr('queryWay', 		queryWay);
						$(el).attr('unit', 			unit);
						$(el).attr('columnCnName',	columnCnName);
						return false;
					} else {
						$(el).remove();
					}
				}
			});
		} else {
			if(numIsNotNull(queryWay, contiueMinVal, contiueMaxVal, exactValue)) {
				var childRule = '<div  class="child" elementType="2" labelFlag=1 ' +
					'labelTypeId="4" parentId="' + parentLabelId + '" calcuElement="' + childLabelId + 
					'" customId="" effectDate="" unit="' + unit + '"' + 
					'attrVal="" startTime="" endTime="" contiueMinVal="' + contiueMinVal + 
					'" contiueMaxVal="' + contiueMaxVal + '" columnCnName="' + columnCnName + '" ' + 
					'rightZoneSign="' + rightZoneSign + '" leftZoneSign="' + leftZoneSign + 
					'" exactValue="' + exactValue + '" darkValue="" ' + 
					'customOrLabelName="" attrName="" queryWay="' + queryWay + 
					'" maxVal="" minVal=""></div>';
					nextElList.append(childRule);
			}
		}
		return temp;
	}	
	
	function validateEnumValueForm(isMustColumn, columnId, columnCnName){
		var selectItemList= $("#addItemDetailBox" + columnId).find("a");
		if(isMustColumn == 1 && selectItemList.length <= 0){
			showAlert(columnCnName + "的值不能为空，请选择","failed");
			return false;
		}
		/*if(selectItemList.length>$._maxItemNum){
			showAlert("最多选择" + $._maxItemNum +"个指标值，请重新选择","failed");
			return false;
		}*/
		return true;
	}
	
	function enumIsNotNull(columnId) {
		var selectItemList= $("#addItemDetailBox" + columnId).find("a");
		if(selectItemList.length <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	function setEnumValue(childLabelId, parentLabelId, isExist, columnCnName){
		var $itemObjArr	= $("#addItemDetailBox" + childLabelId).find("a");
		var itemKeyArr 	= [];
		var itemNameArr = []; 
		var temp 		= '';
		
		for(var i = 0 ,len = $itemObjArr.length ; i< len ; i++){
			var $itemObj = $($itemObjArr[i]);
			itemKeyArr.push($itemObj.attr("data"));
			itemNameArr.push($itemObj.html());
		}
		temp = itemNameArr.join(',');
		
		var source = $("#source").val();
		var nextElList = parent.$('._idClass');
		if (source == 2) {
			nextElList = parent.$("._idClass").parent().parent();
		}
		
		if(isExist) {
			nextElList.children().each(function (index, el) {
				if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
					if(enumIsNotNull(childLabelId)) {
						$(el).attr('parentId',		parentLabelId);
						$(el).attr('calcuElement', 	childLabelId);
						$(el).attr('attrVal', 		itemKeyArr.join(","));
						$(el).attr('attrName', 		itemNameArr.join(","));
						$(el).attr('columnCnName',	columnCnName);
						return false;
					} else {
						$(el).remove();
					}
				}
			});
		} else {
			if(enumIsNotNull(childLabelId)) {
				var childRule = '<div class="child" elementType="2" labelFlag=1 ' +
					'labelTypeId="5" parentId="' + parentLabelId + '" calcuElement="' + childLabelId + 
					'" attrVal="' + itemKeyArr.join(",") + '" attrName="' + itemNameArr.join(",") + '" ' + 
					'startTime="" endTime="" contiueMinVal="" contiueMaxVal="" ' + 
					'" customId="" effectDate="" columnCnName="' + columnCnName + '" ' + 
					'rightZoneSign="" leftZoneSign="" exactValue="" darkValue="" ' + 
					'customOrLabelName="" queryWay="" maxVal="" minVal="" ></div>';
				nextElList.append(childRule);
			}
		}
		return temp;
	}
	
	function validateDateValueForm(isMustColumn, columnId){
		var queryWay="";
		$(".methodSetControl" + columnId).each(function(){
			if($(this).hasClass("methodSelCurrent")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			var startTime = $.trim($("#startTime" + columnId).val());
			var endTime= $.trim($("#endTime" + columnId).val());
			var timeStr=startTime+endTime;
			if(isMustColumn == 1 && $.trim(timeStr) == ""){
				
				$("#dateSnameTip" + columnId).empty();
				$("#dateSnameTip" + columnId).show().append("时间段至少一个不能为空");
				return false;
			}
		}else{
			var exactValueDateYear = $.trim($("#exactValueDateYear" + columnId).val());
			var exactValueDateMonth = $.trim($("#exactValueDateMonth" + columnId).val());
			var exactValueDateDay = $.trim($("#exactValueDateDay" + columnId).val());
			var exactValueDate = exactValueDateYear+exactValueDateMonth+exactValueDateDay;
			
			if(exactValueDate == ""){
				$("#dateSnameTip" + columnId).empty();
				$("#dateSnameTip" + columnId).show().append("精确日期至少一个不能为空！");
				return false;
			}
			//年
			if(exactValueDateYear !=""){
				if(isNaN(exactValueDateYear)){
					$("#dateSnameTip" + columnId).empty();
					$("#dateSnameTip" + columnId).show().append("年份格式不符合要求！");
					return false;
				}
			}else{
				exactValueDateYear = 1990;//给定年固定值
			}
			//月
			if(exactValueDateMonth !=""){
				if(isNaN(exactValueDateMonth)){
					$("#dateSnameTip" + columnId).empty();
					$("#dateSnameTip" + columnId).show().append("月份格式不符合要求！");
					return false;
				}
			}else{
				exactValueDateMonth = 7;//给定月固定值
			}
			//日
			if(exactValueDateDay !=""){
				if(isNaN(exactValueDateDay)){
					$("#dateSnameTip" + columnId).empty();
					$("#dateSnameTip" + columnId).show().append("日格式不符合要求！");
					return false;
				}
			}else{
				exactValueDateDay = 20;//给定日固定值
			}
			var year = parseInt(exactValueDateYear, 10);   
			var month = parseInt(exactValueDateMonth, 10)-1;              
			var day = parseInt(exactValueDateDay, 10);              
			var date = new Date(year, month, day);              
			var y = date.getFullYear();              
			var m = date.getMonth();              
			var d = date.getDate();              
			if ((y != year) || (m != month) || (d != day)) {                  
				$("#dateSnameTip" + columnId).empty();
				$("#dateSnameTip" + columnId).show().append("请输入合法的日期！");
				return false;
			}  
		}
		
		return true;
	}
	
	function dateIsNotNull(columnId) {
		var queryWay="";
		$(".methodSetControl" + columnId).each(function(){
			if($(this).hasClass("methodSelCurrent")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			var startTime	= $.trim($("#startTime" + columnId).val());
			var endTime		= $.trim($("#endTime" + columnId).val());
			var timeStr		= startTime + endTime;
			if($.trim(timeStr) == ""){
				return false;
			}
		}else{
			var exactValueDate="";//存年月日
			var exactValueDateYear = $.trim($("#exactValueDateYear" + columnId).val());//年
			var exactValueDateMonth = $.trim($("#exactValueDateMonth" + columnId).val());//月
			var exactValueDateDay = $.trim($("#exactValueDateDay" + columnId).val());//日
			exactValueDate = exactValueDateYear + exactValueDateMonth + exactValueDateDay;
			if(exactValueDate == ""){
				return false;
			}
		}
		return true;
	}
	 	
	function setDateValue(childLabelId, parentLabelId, isExist, columnCnName){
		var queryWay ="";
		$(".methodSetControl" + childLabelId).each(function(){
			if($(this).hasClass("methodSelCurrent")){
				queryWay= $(this).attr("value")
			}
		});
		var startTime 		= "";
		var endTime			= "";
		var leftZoneSign	= "";
		var rightZoneSign	= "";
		var isNeedOffset	= 0;
		
		var exactValueDate  = "";//存年月日
		var temp			= "";

		if(queryWay == "1"){
			
			startTime 		= $('#startTime' + childLabelId).val();
			endTime			= $('#endTime' + childLabelId).val();
			var dateLeftChecked	= $('#dateLeftClosed' + childLabelId).attr('checked');
			
			if(dateLeftChecked){
				leftZoneSign=">=";
				if(startTime !=""){
					temp += "大于等于"+startTime;
				}
			}else{
				leftZoneSign=">";
				if(startTime !=""){
					temp += "大于"+startTime;
				}
			}
			
			var dateRightChecked=$("#dateRightClosed" + childLabelId).attr("checked");
			if(dateRightChecked){
				rightZoneSign="<=";
				if(endTime != ""){
					temp += "小于等于"+endTime;
				}
			}else{
				rightZoneSign="<";
				if(endTime != ""){
					temp += "小于"+endTime;
				}
			}
			var isNeedOffsetVal = $('#dynamicUpdate' + childLabelId).attr('checked');
			if(startTime || endTime) {
				if(isNeedOffsetVal) {
					isNeedOffset = 1;
					temp += "（按此日期动态偏移1天/1月）";
				}
			}
			
		}else{
			var exactValueDateYear = $.trim($("#exactValueDateYear" + childLabelId).val());//年
			var exactValueDateMonth = $.trim($("#exactValueDateMonth" + childLabelId).val());//月
			var exactValueDateDay = $.trim($("#exactValueDateDay" + childLabelId).val());//日
			
			if(exactValueDateYear && exactValueDateYear !=""){
				temp += exactValueDateYear+"年";
				exactValueDate=exactValueDateYear;
			}else{
				exactValueDate="-1";
			}
			if(exactValueDateMonth && exactValueDateMonth !=""){
				temp += exactValueDateMonth+"月";
				exactValueDate += ","+exactValueDateMonth;
			}else{
				exactValueDate += ",-1";
			}
			if(exactValueDateDay && exactValueDateDay !=""){
				temp += exactValueDateDay+"日";
				exactValueDate += ","+exactValueDateDay;
			}else{
				exactValueDate += ",-1";
			}
		} 
		
		var source = $("#source").val();
		var nextElList = parent.$('._idClass');
		if (source == 2) {
			nextElList = parent.$("._idClass").parent().parent();
		}
		
		if(isExist) {
			nextElList.children().each(function (index, el) {
				if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
					if(dateIsNotNull(childLabelId)) {
						$(el).attr('parentId', 		parentLabelId);
						$(el).attr('calcuElement', 	childLabelId);
						$(el).attr('startTime', 	startTime);
						$(el).attr('endTime', 		endTime);
						$(el).attr('rightZoneSign', rightZoneSign);
						$(el).attr('leftZoneSign', 	leftZoneSign);
						$(el).attr('queryWay',	    queryWay);
						$(el).attr('exactValue',	exactValueDate);
						$(el).attr('columnCnName',	columnCnName);
						$(el).attr('isNeedOffset',	isNeedOffset);
						return false;
					} else {
						$(el).remove();
					}
				}
			});
		} else {
			if(dateIsNotNull(childLabelId)) {
				var childRule = '<div class="child" elementType="2" labelFlag=1 ' +
					'labelTypeId="6" parentId="' + parentLabelId + '" calcuElement="' + childLabelId + 
					'" attrVal="" attrName="" columnCnName="' + columnCnName + '" ' + 
					'startTime="' + startTime + '" endTime="' + endTime + '" contiueMinVal="" contiueMaxVal="" ' + 
					'" customId="" effectDate="" ' + 
					'rightZoneSign="' + rightZoneSign + '" leftZoneSign="' + leftZoneSign + '" exactValue="' + exactValueDate + '" darkValue="" ' + 
					'customOrLabelName="" queryWay="' + queryWay + '" maxVal="" minVal="" isNeedOffset="' + isNeedOffset + '"></div>';
				nextElList.append(childRule);
			}
		}
		return temp;
	}
	
	function validateDarkValueForm(isMustColumn, columnId){
		
		var queryWay="";
		$(".dimMatchMethodControl" + columnId).each(function(){
			if($(this).hasClass("current")){
				queryWay= $(this).attr("value")
			}
		});
		
		if(queryWay == "1"){
			var darkValue = $.trim($("#darkValue" + columnId).val());
			if(isMustColumn == 1 && darkValue == ""){
				$("#darkSnameTip" + columnId).empty();
				$("#darkSnameTip" + columnId).show().append("模糊值不能为空！");
				return false;
			}
		}else {
			var exactValue = $.trim($("#exactValue" + columnId).val());
			if(exactValue == ""){
				$("#enameTip" + columnId).empty();
				$("#enameTip" + columnId).show().append("精确值不能为空！");
				return false;
			}
			if(exactValue.indexOf(",") == 0 ||  exactValue.lastIndexOf(",") == exactValue.length-1 || exactValue.indexOf(",,") !=-1){
				$("#enameTip" + columnId).empty();
				$("#enameTip" + columnId).show().append("请输入合法的值！");
				return false;
			}
		}
		return true;
	}
	
	function darkIsNotNull(columnId) {
		var queryWay="";
		$(".dimMatchMethodControl" + columnId).each(function(){
			if($(this).hasClass("current")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			var darkValue = $.trim($("#darkValue" + columnId).val());
			if(darkValue == ""){
				return false;
			}
		}else{
			var exactValue = $.trim($("#exactValue" + columnId).val());
			if(exactValue == ""){
				return false;
			}
		}
		return true;
	}
	
	function setDarkValue(childLabelId, parentLabelId, isExist, columnCnName) {
		var darkValue = '';
		var exactValue = "";
		var queryWay="";
		var temp ="";

		$(".dimMatchMethodControl" + childLabelId).each(function(){
			if($(this).hasClass("current")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			darkValue=$("#darkValue" + childLabelId).val();
			temp = darkValue;
		}else{
			exactValue=$("#exactValue" + childLabelId).val();
			temp = exactValue;
		}
		
		var source = $("#source").val();
		var nextElList = parent.$('._idClass');
		if (source == 2) {
			nextElList = parent.$("._idClass").parent().parent();
		}
		
		if(isExist) {
			nextElList.children().each(function (index, el) {
				if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
					if(darkIsNotNull(childLabelId)) {
						$(el).attr('parentId', 		parentLabelId);
						$(el).attr('calcuElement', 	childLabelId);
						$(el).attr('darkValue', 	darkValue);
						$(el).attr('columnCnName',	columnCnName);
						$(el).attr('exactValue', 	exactValue);
						$(el).attr('queryWay', 	    queryWay);
						return false;
					} else {
						$(el).remove();
					}
				}
			});
		} else {
			if(darkIsNotNull(childLabelId)) {
				var childRule = '<div class="child" elementType="2" labelFlag=1 ' +
					'labelTypeId="7" parentId="' + parentLabelId + '" calcuElement="' + childLabelId + 
					'" attrVal="" attrName="" columnCnName="' + columnCnName + '" ' + 
					'startTime="" endTime="" contiueMinVal="" contiueMaxVal="" ' + 
					'" customId="" effectDate="" ' + 
					'rightZoneSign="" leftZoneSign="" exactValue="' + exactValue + '" darkValue="' + darkValue + '" ' + 
					'customOrLabelName="" queryWay="' + queryWay +'" maxVal="" minVal="" ></div>';
				nextElList.append(childRule);		
			}
		}
		return temp;
	}
	function cancel() {
		parent.iframeCloseDialog("#verticalLabelSetDialog");
	}
	
	//初始化枚举分类列表
	function initEnumCategorys(columnId){
		var calcuElement=$("#calcuElement"+columnId).val();
		var para={"columnId":columnId};
		var actionUrl='${ctx}/ci/ciLabelInfoAction!initEnumCategorys.ai2do';
		$.ajax({
			url:actionUrl,
			type:"POST",
			dataType: "json",
			//async	: false,
			data: para,
			success:function(result){
				if(result.success){
					var cnum = result.categoryNum;
					if(cnum==0){
						$("#textTipSpan"+columnId).show();
					}else if(cnum>0){
						$(".selBtn ,.selTxt").unbind("click");
						$(".selBtn ,.selTxt").removeClass("selBtnActive");
						var html = $('<a href="javascript:void(0);" value="">所有分类</a>').bind("click",function(){
							selListBox(this,columnId);
						});
						var enumCategoryDivChild = $("#enumCategoryDiv"+columnId+" dd");
						enumCategoryDivChild.empty().append(html);
						$.each(result.enumCategory, function(i,categorys){
							var $aTag = $('<a href="javascript:void(0);" value="' + categorys.enumCategoryId + '">' + categorys.enumCategoryName + '</a>').bind("click",function(){
								selListBox(this,columnId);
							}); 
							enumCategoryDivChild.append($aTag);
						});
			            $("#enumCategoryDiv"+columnId).show();
			            
			          	//模拟下拉框
			            $(".selBtn ,.selTxt").click(function(){
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
					}
				}else{
					$.fn.weakTip({"tipTxt":result.msg});
				}
			},
			error:function(){
				$.fn.weakTip({"tipTxt":'获取枚举分类失败!'});
			}
		});
	}
	//下拉框点击事件
	function selListBox(ths,columnId){
		var selVal = $(ths).attr("value");
    	var selHtml = $(ths).html();
    	$(ths).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
    	$(ths).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
    	$(ths).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
   		//向隐藏域中传递值
        $(ths).parents(".querySelListBox").slideUp();
        var enumCategoryId=$("#enumCategoryId"+columnId).val();
		if(enumCategoryId!=null&&enumCategoryId!=""){
			$("#addAllItemAction"+columnId).show();
			$("#removeAllItemAction"+columnId).show();
		}else{
			$("#addAllItemAction"+columnId).hide();
			$("#removeAllItemAction"+columnId).hide();
		}
   		serchItemList(columnId);
	}
</script>
</head>
<body >
<div class="clearfix labelContent">
	<div class="switchImportWayBox" id="switchImportWayBox">
		<ol>
			<c:forEach var="item" items="${ciLabelVerticalColumnRelList}" varStatus="status">
				<li>
					<c:if test="${status.index == 0}">
					<div labelTypeId="${item.labelTypeId}" childLabelId="${item.id.columnId}" parentLabelId="${item.id.labelId}"
						isMustColumn="${item.isMustColumn}" unit="${item.unit}" columnCnName="${item.columnCnName}" class="current">${item.columnCnName}<c:if test="${item.isMustColumn == 1}">(必填)</c:if></div>
					<input id="firstLabelType" type="hidden" value="${item.labelTypeId}"></input>
					<input id="firstLabelColumnId" type="hidden" value="${item.id.columnId}"></input>
					</c:if>
					<c:if test="${status.index != 0}">
					<div labelTypeId="${item.labelTypeId}" childLabelId="${item.id.columnId}" parentLabelId="${item.id.labelId}"
						isMustColumn="${item.isMustColumn}" unit="${item.unit}" columnCnName="${item.columnCnName}">${item.columnCnName}<c:if test="${item.isMustColumn == 1}">(必填)</c:if></div>
					</c:if>
				</li>
			</c:forEach>
		</ol>
	</div>
	<div id="enumTip" style="display: none;position: absolute;"></div>
	<c:forEach var="item" items="${ciLabelVerticalColumnRelList}">
	
	<!-- 指标类标签HTML START -->
	<c:if test="${item.labelTypeId == 4}">
		<div id="labelElement4${item.id.columnId}" class="importWayBox hidden">
			<div class="number-value-setting-box">
				<dl>
					<dt>
		            <div class="queryMethod queryMethodControl${item.id.columnId}"  value="1">数值范围</div>
		            </dt>
		            <dd>
		                  <ol class="fleft numberBoxUl">
		                    <li>
		                         <input type="text" class="fleft inputFmt100" id="contiueMinVal${item.id.columnId}"  value="输入下限" onkeyup="$('#numCnameTip${item.id.columnId}').hide();"/>
		                         <span class="fleft unitColor"></span>
		                         <p class="fleft slectionBox">
	                                <input type="checkbox"  id="leftClosed${item.id.columnId}" value="1" class="fleft" />
	                                <label for="leftClosed" class="fleft">包含当前值</label>
		                         </p>
		                    </li>
		                    <li> <div id="numCnameTip${item.id.columnId}" class="tishi error hidden"  ></div></li>
		                    <li>
		                    	 <input type="text" class="fleft inputFmt100" id="contiueMaxVal${item.id.columnId}" value="输入上限" onkeyup="$('#cnameTip${item.id.columnId}').hide();"/>
		                         <span class="fleft unitColor"></span>
	                             <p class="fleft slectionBox">
	                                <input type="checkbox"   class="fleft" id="rightClosed${item.id.columnId}"  value="1" />
	                                <label for="rightClosed" class="fleft">包含当前值</label>
	                             </p>
		                    </li>
		                  </ol>
		            </dd>
				</dl>
				<dl class="moddel">
					<dt>
		            	<div class="queryMethod queryMethodControl${item.id.columnId}"  value="2">精确查询</div>
		            </dt>
		            <dd>
						<div >
						   <input type="text" class="exactQueryInput fleft"  id="exactValue${item.id.columnId}" value="" onkeyup="$('#numSnameTip${item.id.columnId}').hide();"/>
						   <span id="unitExact${item.id.columnId}" class="fleft unitColor"></span>
						   <div id="numSnameTip${item.id.columnId}" class="tishi error" style="display:none;"></div>
						</div>
						<div class="fleft useTip">多条件用","分割且不能用","结尾</div>
		            </dd>
				</dl>
			</div>
		</div>
	</c:if>
	<!-- 指标类标签HTML END -->

	<!-- 枚举类标签 HTML START -->
	<c:if test="${item.labelTypeId == 5}">
		<div id="labelElement5${item.id.columnId}" class="hidden enum">
			<div class="itemChooseTopBox">
				<div id="enumCategoryDiv${item.id.columnId}" class="fleft formQuerySelBox" style="display:none;margin-top:5px;width:200px;">
					<div class="selTxt" style="width:166px;">所有分类</div>
					<a href="javascript:void(0);" class="selBtn"></a>
					<div class="querySelListBox">
					<input type="hidden" value="" id="enumCategoryId${item.id.columnId}"/>
					<dl>
					<dd>
					</dd>
					</dl>
					</div>
				</div>
		        <span id="textTipSpan${item.id.columnId}" style="display:none;"> 请选择，可多选</span>
				<a id="importTip${item.id.columnId}" class="fright fileUploadHelp" title=""></a>
				<input type="file" name="file_upload${item.id.columnId}" class="fright" id="file_upload${item.id.columnId}" />
			</div>
			<div class="itemChooseActionBox clearfix">
				<div class="itemChooseBox fleft" id="itemChooseBoxPage">
					<div class="itemActionTopBox ">
						<ol>
							<li><strong>共&nbsp;<span id="totalSizeNum${item.id.columnId}"></span>&nbsp;个</strong></li>
							<li><a href="javascript:void(0);"
								onclick="setItemStatus(true,'itemChooseDetailBox${item.id.columnId}');">全选</a></li>
							<li><a href="javascript:void(0);"
								onclick="setItemStatus(false,'itemChooseDetailBox${item.id.columnId}');">取消</a></li>
						</ol>
	
						<div class="itemSearchBox fright">
							<input type="text" class="fleft searchInput enumItemSearchInput" id="dimName${item.id.columnId}" /> <input
								type="button" class="fleft searchBtn enumItemSearchBtn" columnId="${item.id.columnId}" />
						</div>
					</div>
					<jsp:include page="/aibi_ci/core/pages/elementSetting/vertLabelEnumSettingList.jsp">
						<jsp:param value="${item.id.columnId}" name="columnId"/>
					</jsp:include>
				</div>
				<div class="itemChooseCenterBox">
					<!--              <a href="javascript:void(0);" id="addItemAction"> -->
             <div class="addActionBox" id="addItemAction${item.id.columnId}">
               		添加
            </div>
<!--             </a> -->
<!--             <a href="javascript:void(0);" id="removeItemAction"> -->
            <div class="removeActionBox" id="removeItemAction${item.id.columnId}">
                	删除
            </div>
<!--             </a> -->
<!--              <a href="javascript:void(0);" id="addAllItemAction"> -->
             <div class="addActionBox allAddActionBox" id="addAllItemAction${item.id.columnId}" style="display:none;">
               		全部添加
            </div>
<!--             </a> -->
<!--             <a href="javascript:void(0);" id="removeAllItemAction"> -->
            <div class="removeActionBox allRmoveActionBox" id="removeAllItemAction${item.id.columnId}" style="display:none;">
                	全部删除
            </div>
<!--             </a> -->
				</div>
	
				<div class="itemChooseBox fright">
					<div class="itemActionTopBox">
						<ol>
							<li><strong>已选&nbsp;<span id="selectedItemSize${item.id.columnId}"></span>&nbsp;个</strong></li>
							<li><a href="javascript:void(0);"
								onclick="setItemStatus(true,'addItemDetailBox${item.id.columnId}');">全选</a></li>
							<li><a href="javascript:void(0);"
								onclick="setItemStatus(false,'addItemDetailBox${item.id.columnId}');">取消</a></li>
						</ol>
						<div class="itemSearchBox fright">
							<input type="text" class="fleft searchInput choosedEnumItemSearchInput" id="addDimName${item.id.columnId}" /> <input
								type="button" class="fleft searchBtn choosedEnumItemSearchBtn" columnId="${item.id.columnId}" />
						</div>
					</div>
					<div class="itemChooseContentBox">
						<div class="_loading" id="_loading${item.id.columnId}" style="display:none;">正在加载，请稍候 ...</div>
						<ol class="clearfix" id="addItemDetailBox${item.id.columnId}">
						</ol>
					</div>
				</div>
			</div>
			<input type="hidden" name="calcuElement" id="calcuElement${item.id.columnId}" />
		</div>
	</c:if>
	<!-- 枚举类标签HTML END -->

	<!-- 日期类标签HTML END -->
	<c:if test="${item.labelTypeId == 6}">
		<div id="labelElement6${item.id.columnId}" class="importWayBox hidden">
			<div class="date-setting-box"   >
				<dl>
					<dt>
						<div class="methodSel methodSelCurrent methodSetControl${item.id.columnId}" value="1">日期范围</div>
					</dt>
					<dd>
						<div>
							<span class="fleft">开始时间&nbsp;&nbsp;&nbsp;</span>
							<input type="text" id="startTime${item.id.columnId}"
								class="fleft dateInputFmt80" readonly="readonly"
								onclick="if($('#endTime${item.id.columnId}').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime${item.id.columnId}\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"
								value="${pojo.startTime}" />
							<p class="fright rPadding100Margin3">
								 <input type="checkbox" id="dateLeftClosed${item.id.columnId}" class="fleft" value="1"/>
			                     <label for="dateLeftClosed${item.id.columnId}" class="fleft">包含当前值</label>
							</p>
		            	</div>
		            </dd>
		        </dl>
				<dl>
					<dt>
						&nbsp;
					</dt>
					<dd>
						<span class="fleft">结束时间&nbsp;&nbsp;&nbsp;</span>
						<input type="text" name="pojo.endTime" id="endTime${item.id.columnId}"
							readonly="readonly" class="fleft dateInputFmt80"
							value="${pojo.endTime}"
							onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime${item.id.columnId}\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" />
						<p class="fright rPadding100Margin3">
							<input type="checkbox" class="fleft" id="dateRightClosed${item.id.columnId}" value="1"/>
		                    <label for="dateRightClosed${item.id.columnId}" class="fleft">包含当前值</label>
		               </p>
		            </dd>
		        </dl>
		        <dl>
					<dt>
						&nbsp;
					</dt>
					<dd>
		               <p class="fleft">
							<input type="checkbox" class="fleft" id="dynamicUpdate${item.id.columnId}" value="1"/>
		                	<label for="dynamicUpdate${item.id.columnId}" class="fleft">按此日期动态偏移1天/1月</label>
		            	</p>
		            </dd>
		        </dl>
		        <dl>
				<dt>
					<div class="methodSel methodSetControl${item.id.columnId}" value="2">数值范围</div>
				</dt>
				<dd>
					<input type="text" value="" maxlength="4" id="exactValueDateYear${item.id.columnId}"  name="exactValueDateYear${item.id.columnId}" class="fleft inputFmt60"                     />
					<span class="fleft padding10">年</span>
					<input type="text" value="" maxlength="2" id="exactValueDateMonth${item.id.columnId}" name="exactValueDateMonth${item.id.columnId}" class="fleft inputFmt60"                     />
					<span class="fleft padding10">月</span>
					<input type="text" value="" maxlength="2" id="exactValueDateDay${item.id.columnId}" name="exactValueDateDay${item.id.columnId}" class="fleft inputFmt60"                     />
					<span class="fleft padding10">日</span>
				</dd>
		        </dl>
		        <dl>
					<dd>
						<div id="dateSnameTip${item.id.columnId}" class="tishi error" style="display:none;"></div>
					</dd>
		        </dl>
				<!--<div class="fleft left-box"></div>
				<div class="fleft right-box">
					<ol >
						<li><label class="fleft">请选择时间段</label></li>
						<li><input type="text" id="startTime${item.id.columnId}"
							class="fleft dateInputFmt325" readonly="readonly"
							onclick="if($('#endTime').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime${item.id.columnId}\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"
							value="${pojo.startTime}" /></li>
						<li ><span>至</span></li>
						<li><input type="text" name="pojo.endTime" id="endTime${item.id.columnId}"
							readonly="readonly" class="fleft dateInputFmt325"
							value="${pojo.endTime}"
							onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime${item.id.columnId}\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" />
						</li>
						<li>
							 提示 
							<div id="dateSnameTip${item.id.columnId}" class="tishi error" style="display: none;"></div>
							 非空验证提示 
						</li>
						<li>
							<p class="fright">
								<input type="checkbox" class="fleft" id="dateRightClosed${item.id.columnId}"
									value="1" /> <label for="rightClosed" class="fleft">右闭区间</label>
							</p>
							<p class="fright">
								<input type="checkbox" id="dateLeftClosed${item.id.columnId}" class="fleft"
									value="1" /> <label for="leftClosed" class="fleft">左闭区间</label>
							</p>
						</li>
					</ol>
				</div>
			--></div>
			<!-- 按钮操作-->
			<input type="hidden" name="pojo.valueId" id="valueId${item.id.columnId}" /> <input
				type="hidden" name="pojo.sortNum" id="sortNum${item.id.columnId}" />
		</div>
	</c:if>
	<!-- 日期类标签HTML END -->

	<!-- 模糊类标签HTML END -->
	<c:if test="${item.labelTypeId == 7}">
		<div id="labelElement7${item.id.columnId}" class="importWayBox hidden">
			<div class="dim-match-box clearfix">
				<div class="fleft  dim-match-left-box"></div>
				<dl>
					<dt>
						<div class="dimMatchMethod dimMatchMethodControl${item.id.columnId} current" value="1">模糊值</div>
					</dt>
					<dd class="dim-match-dd">
						<div class="fright dim-match-right-box">
							<!--<h6> 请输入模糊匹配内容：</h6>
							--><input type="text" class="dimMatchInput" id="darkValue${item.id.columnId}" onkeyup="$('#darkSnameTip${item.id.columnId}').hide();"/>
							<div id="darkSnameTip${item.id.columnId}" class="tishi error" style="display: none;"></div> 
						</div>
					</dd>
				</dl>
				<dl>
					<dt>
						<div class="dimMatchMethod dimMatchMethodControl${item.id.columnId}"  value="2">精确值</div>
					</dt>
					<dd  class="dim-match-dd">
						<div class="fright dim-match-right-box">
							<input type="text"  class="dimMatchInput"   id="exactValue${item.id.columnId}" onkeyup="$('#enameTip${item.id.columnId}').hide();$('#newTip${item.id.columnId}').hide();" onkeydown="if(event.keyCode==32) return false"/>
							<div class="itemChooseTopBox uploadify_div">
		       				<input type="file" name="file_upload_dark" id="file_upload_dark${item.id.columnId}" />
		  		 			</div>
							<div id="enameTip${item.id.columnId}" class="tishi error" style="display:none;"></div><!-- 非空验证提示 -->
						</div>
						<div class="fright newTip newTip-dim-match" id="newTip${item.id.columnId}"></div>
					</dd>
				</dl>
			</div>
			<div class="importFontStyleBox" id="importFontStyleBox${item.id.columnId}"></div>
		</div>
	</c:if>
	<!-- 模糊类标签HTML END -->
	</c:forEach>

	<!-- 按钮操作-->
	<div class="btnActiveBox">
		<input type="button" value="确定" class="ensureBtn" id="verticalLabelSaveBtn" onclick="setData();" />
	</div>
</div>
<input type="hidden" id="source" name="source" value="${source}"/>
</body>
</html>