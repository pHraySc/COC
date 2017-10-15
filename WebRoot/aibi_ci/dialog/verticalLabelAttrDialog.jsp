<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
			
			if(labelTypeId == 5) {
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
			} else if(labelTypeId == "otherAttrs") {
				//TODO
				
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
		
		//处理回显
		var attrVal1 = '${param["attrVal"]}';
		var attrName1 = '${param["attrName"]}';
		var columnId1 = '${param["columnId"]}';
		var otherAttr1 = '${param["otherAttrs"]}';
		var itemKeyArr1 = [];
		var itemNameArr1 = [];
		var otherAttrArr1 = [];
		if(attrVal1){
			itemKeyArr1 = attrVal1.split(",");
			itemNameArr1 = attrName1.split(",");
			$("#addItemDetailBox"+columnId1).empty();
			for(var i=0,len ;i< itemKeyArr1.length ;i++){
				var $liTemp = $("<li><a href=\"javascript:void(0);\" ondblclick=\"delEnumItemByDbClick(this);\" onclick=\"aAddOrRemoveClass(this);\" id='"+itemKeyArr1[i]+"'  data='"+itemKeyArr1[i]+"' >"+itemNameArr1[i]+"</a></li>");
				$("#addItemDetailBox"+columnId1).append($liTemp);
			}
			$("#selectedItemSize"+columnId1).html(itemKeyArr1.length);
			
			if(otherAttr1.length>0){
				var otherAttrArr1 = otherAttr1.split(",");
				for(var i=0 ;i< otherAttrArr1.length ;i++){
					$("#dateRightClosed"+otherAttrArr1[i]).attr('checked',true);
				}
			}
		}
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
		var iframe = parent.$('#verticalLabelSetFrame');
		var dialog = parent.$('#verticalLabelSetDialog').parent();
		if (labelTypeId == 5) {
			iframe.height(488);
			dialog.width(720);
		} else if (labelTypeId == "otherAttrs") {
			iframe.height(275);
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
		var vertLabelId = '';
		var attrVal = '';
		var attrName = '';
		var columnId = '';
		var tag = '';
		var count = parent.$('#vertLabelAttrListTd').find('ul').length;
		var arrJson =[];
		$('#switchImportWayBox div').each(function(index, el) {
			var labelTypeId 	= $(el).attr('labelTypeId');
			var labelName	= $(el).attr('labelName');
			var childLabelId 	= $(el).attr('childLabelId');
			var isMustColumn 	= $(el).attr('isMustColumn');
			var parentLabelId 	= $(el).attr('parentLabelId');
			var columnCnName	= $(el).attr('columnCnName');
			var isExist			= isExistsChildLabel(childLabelId);
			
			if(labelTypeId == 5) {
				if(!validateEnumValueForm(isMustColumn, childLabelId, columnCnName)) {
					toWarningTag(5, childLabelId);
					flag = false;
					return flag;
				}
				var enumContext = setEnumValue(childLabelId, parentLabelId, isExist, columnCnName);
				if(enumContext) {
					//displayContext	+=  '<label>' + columnCnName + '：</label><span>' + enumContext + '</span>；';
					displayContext	+=  '<label>' + columnCnName + '</label>';
					//displayTitle	+= columnCnName + '：' + enumContext + ';';
					displayTitle	+= columnCnName ;
					
					//TODO
					var $itemObjArr	= $("#addItemDetailBox" + childLabelId).find("a");
					var itemKeyArr 	= [];
					var itemNameArr = []; 
					var temp 		= '';
					
					for(var i = 0 ,len = $itemObjArr.length ; i< len ; i++){
						var $itemObj = $($itemObjArr[i]);
						itemKeyArr.push($itemObj.attr("data"));
						itemNameArr.push($itemObj.html());
					}
					
					vertLabelId = parentLabelId;
					columnId = childLabelId;
					attrVal = itemKeyArr.join(',');
					attrName = itemNameArr.join(','); 
					
					//var dataJson = {"vertLabelId":vertLabelId,"columnId":columnId,"attrVal":attrVal,"attrName":attrName};
					//arrJson.push(dataJson);
					count = getUniqueIndex(count);
					tag += '<ul count="'+count+'">';
					tag += '<input name="vertLabelAttrList['+count+'].labelId" ';
					tag += 'type="hidden" value="'+parentLabelId+'"  id="'+parentLabelId+'_'+childLabelId+'"/>';
					tag += '<input name="vertLabelAttrList['+count+'].columnId" type="hidden" value="'+childLabelId+'"/>';
					tag += '<input name="vertLabelAttrList['+count+'].columnCnName" type="hidden" value="'+columnCnName+'"/>';
					tag += '<input name="vertLabelAttrList['+count+'].attrVal" type="hidden" value="'+itemKeyArr.join(',')+'"/>';
					tag += '</ul>';
					count++;
				}
			}
		});
		//var form = parent.$("#saveForm"); 
		//form.data("arrJson",arrJson);
		//arrJson = new Array();
		//console.log(form.data("arrJson"));
		if(!flag) return;
		
		var conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
		if(displayContext) {
			conditionMore.html('').append('<p>' + displayContext + '</p>');
			conditionMore.attr('title', displayTitle);
		}
		
		var otherAttrArr =[];
		var childrenNotSelectedId = [];
		$('#otherAttrs input').each(function(index, item) {
			var checkedVal =$(item).attr("checked");
			if(checkedVal == "checked" || checkedVal == true){
				displayContext	+=  '<label>,' + $(item).attr("cnname") + '</label>';
				//displayTitle	+= columnCnName + '：' + enumContext + ';';
				displayTitle	+= ','+$(item).attr("cnname") ;
				//count = getUniqueIndex(count);
				tag += '<ul count="'+count+'">';
				tag += '<input name="vertLabelAttrList['+count+'].labelId" ';
				tag += 'type="hidden" value="'+$(item).attr("parentLabelId")+'"  id="'+$(item).attr("parentLabelId")+'_'+$(item).attr("childLabelId")+'" />';
				tag += '<input name="vertLabelAttrList['+count+'].columnId" type="hidden" value="'+$(item).attr("childLabelId")+'"/>';
				tag += '<input name="vertLabelAttrList['+count+'].columnCnName" type="hidden" value="'+$(item).attr("cnname")+'"/>';
				tag += '</ul>';
				
				otherAttrArr.push($(item).attr("childLabelId"));
				count++;
			} else {
				childrenNotSelectedId.push($(item).attr("childLabelId"));
			}
		});
		var otherAttr = otherAttrArr.join(',');
		if(parent.$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').length>0){
			parent.$('#vertLabelAttrListTd').find('li[vertLabelId="'+vertLabelId+'"]').remove();
		}
		
		tag ='<li vertLabelId=\"' + vertLabelId + 
			'\" attrVal=\"'+attrVal+'\" attrName=\"'+attrName+'\" columnId=\"'+columnId+'\" otherAttr=\"'+otherAttr+'\">'+tag+'</li>';
		parent.$('#vertLabelAttrListTd').append(tag);
		//向选中标签区域里设置值
		if(parent.$('#labelListBox').find('div[checkedboxId="'+vertLabelId+'"]').length>0){
			parent.$('#labelListBox').find('div[checkedboxId="'+vertLabelId+'"]').parent().remove();
		}
		
		var whichAction = 'popVertAttrSet';
		var $li = '<li><div attrSource="2" checkedboxId =' + vertLabelId + 
			' isVert=\"1\"><p class=\"fleft\" title=\"'+displayTitle+'\" vertLabelId=\"' + vertLabelId + 
			'\" onclick="'+whichAction+'('+vertLabelId+');" attrVal=\"'+attrVal+'\" attrName=\"'+attrName+'\" columnId=\"'+columnId+'\">' + displayContext + 
			'</p><p class="clientSelectItemClose fleft"></p></div></li>';
		
		//删除对应选中的排序属性
		for(var i=0; i<childrenNotSelectedId.length; i++) {
			var childId = childrenNotSelectedId[i];
			var selectedSortAttrs = parent.$('.sortAttrBox');
			selectedSortAttrs.each(function(index, item) {
				var labelId = parent.$(item).find('.sortAttrName').attr('columnId');
				if(labelId == childId) {
					parent.$(item).remove();
				}
			});
		}		
		parent.$("#labelListBox ul").append($li);
		parent.$("#labelListBox").show().css("height","65px");
		parent.iframeCloseDialog2("#verticalLabelSetDialog","ok");
		
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
	
	function getUniqueIndex(count) {
		if(parent.$('#vertLabelAttrListTd').find('ul[count="'+count+'"]').length>0){
			count++;
			return getUniqueIndex(count);
		}else{
			return count;
		}
	}
	
	//判断是否已经存在对应的子标签，如果存在则对div属性进行重新修改，如果不存在则新加div
	function isExistsChildLabel(childLabelId) {
		var flag = false;
		var nextElList = parent.$('._idClass').parent().parent().children();
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
		var nextElList = parent.$('._idClass').parent().parent().children();
		for(var i=0; i<nextElList.length; i++) {
			var el = nextElList[i];
			if($(el).hasClass('child') && $(el).attr('calcuElement') == childLabelId) {
				result = el;
				break;
			}
		}
		return result;
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
		if(isExist) {
			parent.$('._idClass').parent().parent().children().each(function (index, el) {
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
				parent.$('._idClass').parent().parent().append(childRule);
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
<div class="clearfix labelContent" id="labelContent">
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
				</li>
			</c:forEach>
			<li>
				<div labelTypeId="otherAttrs">其他属性(选填)</div>
			</li>
		</ol>
	</div>
	<div id="enumTip" style="display: none;position: absolute;"></div>
	<c:forEach var="item" items="${ciLabelVerticalColumnRelList}" varStatus="status">
	
	<!-- 枚举类标签 HTML START -->
	<c:if test="${status.index == 0}">
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
					<jsp:include page="/aibi_ci/dialog/itemChooseList4VerticalLabel.jsp">
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
	</c:forEach>
	
	<div class="clearfix labelBox-VerticalColumn" id="otherAttrs">
		<c:forEach var="item" items="${ciLabelVerticalColumnRelList}" varStatus="status">
			<c:if test="${status.index != 0}">
				<p class="fleft labelVerticalColumnRelBox">
					<label title="${item.columnCnName}">
						<input type="checkbox" id="dateRightClosed${item.id.columnId}" value="${item.id.columnId}" parentLabelId="${item.id.labelId}" childLabelId="${item.id.columnId}" cnname ="${item.columnCnName}"/>${item.columnCnName}
					</label>
				</p>
			</c:if>
		</c:forEach>
	</div>
	

	<!-- 按钮操作-->
	<div class="btnActiveBox">
		<input type="button" value="确定" class="ensureBtn" id="verticalLabelSaveBtn" onclick="setData();" />
	</div>
</div>
</body>
</html>