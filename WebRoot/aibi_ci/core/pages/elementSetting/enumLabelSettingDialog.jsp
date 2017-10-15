<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/jquery.uploadify.js"></script>
<style>
.ui-tooltip {padding: 5px 5px; background:#f1f2f7; color:#333; border:1px solid #bbb; font-size:12px; font-weight:normal;}
.ui-tooltip ul{list-style:none; padding:0; margin:0;}
.ui-tooltip ul li{height:16px; line-height:16px;}
select.sceneWidth {width:310px;}
.greenTip {margin-top:2px;}
.importCustermersTip li{text-align:left; text-indent:5px;}
.tootip-table {border-collapse:collapse;border-spacing:0;border-left:1px solid #d4d4d4;border-top:1px solid #4c4c4c;background:#FFF;}
.tootip-table th,.tootip-table td{border-right:1px solid #d4d4d4;border-bottom:1px solid #d4d4d4;padding:5px 15px;white-space: nowrap;}
.tootip-table th {background-color:#f1f1f1;height:30px;border:1px solid #d4d4d4;font-weight:bold;}
</style>
<script type="text/javascript">
$(function(){
	$(document).click(function() {
		$('#enumTip').hide(500);
	});
	var attrVal = '${param["ciLabelRule.attrVal"]}';//alert(attrVal);
	var attrName = '${param["ciLabelRule.attrName"]}';//alert(attrName);
	var calcuElement = '${param["ciLabelRule.calcuElement"]}';//alert(calcuElement);
	$("#calcuElement").val(calcuElement);
	var itemKeyArr = [];
	var itemNameArr = [];
	if(attrVal){
		itemKeyArr = attrVal.split(",");
		itemNameArr = attrName.split(",");
		$("#addItemDetailBox").empty();
		for(var i=0,len ;i< itemKeyArr.length ;i++){
			var $liTemp = $("<li><a href=\"javascript:void(0);\" ondblclick=\"delEnumItemByDbClick(this);\" onclick=\"aAddOrRemoveClass(this);\" id='"+itemKeyArr[i]+"'  data='"+itemKeyArr[i]+"' >"+itemNameArr[i]+"</a></li>");
			$("#addItemDetailBox").append($liTemp);
		}
		$("#selectedItemSize").html(itemKeyArr.length);
	}
	
	//初始化枚举值分类
	initEnumCategorys();
	
	var enumCategoryId=$("#enumCategoryId").val();
	if(enumCategoryId==null||enumCategoryId==""){
		$("#addAllItemAction").hide();
		$("#removeAllItemAction").hide();
	}
	//列表事件绑定
	//pagetable();
	// 添加和删除操作
	$("#addItemAction").click(function(){
		var selectItemList= $("#itemChooseDetailBox").find(".itemLiNextAHover");
		for(var i = 0 ,len=selectItemList.length ; i< len ;i++){
			var selectedItem = $(selectItemList[i]);
			//$("#addItemDetailBox").find("#"+selectedItem.attr("id")).parent().remove();
			$("#addItemDetailBox").find("a[id='"+selectedItem.attr("id")+"']").parent().remove();
			var $liTemp = $("<li><a href=\"javascript:void(0);\" ondblclick=\"delEnumItemByDbClick(this);\" onclick=\"aAddOrRemoveClass(this);\"  id='"+selectedItem.attr("id")+"' data='"+selectedItem.attr("data")+"' title='"+selectedItem.attr("title")+"' >"+selectedItem.html()+"</a></li>");
			$("#addItemDetailBox").append($liTemp);
		}
		//已选数量
		calcEnumChooseItemNum();
		//添加完成后，移除选中效果
		removeSelectedClass();
	});
	$("#removeItemAction").click(function(){
		$("#addItemDetailBox").find(".itemLiNextAHover").parent().remove();
		//已选数量
		calcEnumChooseItemNum();
	});
	// 全部添加和全部删除操作
	$("#addAllItemAction").click(function(){
		$("#_loading").show();
		var dimName=$("#dimName").val();
		var calcuElement=$("#calcuElement").val();
		var enumCategoryId=$("#enumCategoryId").val();
		var para={
			"labelId": calcuElement,
			"dimName":dimName,
			"enumCategoryId":enumCategoryId
		};
		var actionUrl = '${ctx}/ci/ciLabelInfoAction!findAllLabelDimValue.ai2do';
		$.ajax({
			url:actionUrl,
			type:"POST",
			dataType: "json",
			data: para,
			success:function(result){
				if(result.success){
					var dimValueList = result.dimValueList;
					var $liTemp = "";
					$.each(dimValueList, function (n, value) {
						var child = $("#addItemDetailBox").find("a[id='"+value.V_KEY+"']");
						var parent = child.parent();
						parent.remove();
		            	$liTemp += "<li><a href=\"javascript:void(0);\" ondblclick=\"delEnumItemByDbClick(this);\" onclick=\"aAddOrRemoveClass(this);\"  id='"+value.V_KEY+"' data='"+value.V_KEY+"' title='"+value.V_NAME+"' >"+value.V_NAME+"</a></li>";
		          	});
					$("#addItemDetailBox").append($liTemp);
					calcEnumChooseItemNum();
					$("#_loading").hide();
				}else{
					$("#_loading").hide();
					$.fn.weakTip({"tipTxt":result.msg});
				}
			},
			error:function(){
				$("#_loading").hide();
				$.fn.weakTip({"tipTxt":'获取枚举类型标签的维值失败!'});
			}
		});
	});
	$("#removeAllItemAction").click(function(){
		$("#addItemDetailBox").empty();
		$("#selectedItemSize").html(0);
	});
	//查询枚举列表
	searchItemList();
	if($.trim($("#selectedItemSize").html()) =="" ){
		$("#selectedItemSize").html(0);
	}
	
	$("#enumListOperateDiv").find("#file_upload").remove();
	$("#enumListOperateDiv").append('<input type="file" name="file_upload" id="file_upload" class="fright" />');
	
	//导入按钮事件
	$('#file_upload').uploadify({
		buttonClass	: 'importBtn',
		btnBoxClass : 'fright tMargin5',
		buttonText	: '导入',
		width		: 60,
		height		: 24,
		fileObjName	: 'file',
		auto		: true,//为true当选择文件后就直接上传了，为false需要点击上传按钮才上传
		multi		: true,
		progressData: 'speed',
		itemTemplate: true,
		fileTypeExts: '*.txt;*.csv',
		fileTypeDesc: '导入文件格式只能是txt或者csv格式！',
        swf			: '${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.swf',
        uploader	: '${ctx}/ci/ciIndexAction!findEnumByImport.ai2do?columnId=' + calcuElement + '&flag=true',
        onUploadSuccess	: function(file, data, response) {
        	var success = $.parseJSON(data).success;
        	if(!success) {
        		showAlert($.parseJSON(data).msg, 'failed');
        		return;
        	}
        	var selectedIds = [];
        	var selectedItems = $('#addItemDetailBox').html();
        	$(selectedItems).each(function(index, item) {
        		selectedIds.push($.trim($(item).children('a').attr('id')));
        	});
        	var html = '';
        	var dataObj = $.parseJSON(data).data;
			for(var i=0; i<dataObj.length; i++) {
				var item = dataObj[i];
				if(!arrayContain(selectedIds, $.trim(item.V_KEY))) {
					html += '<li><a href="javascript:void(0);" '
						+ 'ondblclick="delEnumItemByDbClick(this);" onclick="aAddOrRemoveClass(this);" ' 
	        			+ 'id="' + item.V_KEY + '" data="' + item.V_KEY + '" title="' + item.V_NAME + '">'
	        			+ item.V_NAME + '</a></li>';
				}
			}
			$('#addItemDetailBox').append(html);
			calcEnumChooseItemNum();
			removeSelectedClass();
        }
    });
	var tableChild = '';
	var items = $('#itemChooseDetailBox').children('li');
	if(items) {
		for(var i=0; i<items.length; i++) {
			var item = $(items[i]);
			tableChild += '<tr><td>' + item.children('a').attr('data') + '</td><td>' + item.text() + '</td></tr>';
		}
	}
	$('#importTip').click(function() {
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
	
});

	//判断是否存在于数据元素中
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
	
	//全选和 取消选择操作 
	function setItemStatus(flag , divBoxId){
		if(flag){
			$("#" + divBoxId ).find("a").addClass("itemLiNextAHover");
		}else{
			$("#" + divBoxId ).find("a").removeClass("itemLiNextAHover");
		}
	}

	//列表事件绑定
	function pagetable(){
		var dimName=$("#dimName").val();
		var calcuElement=$("#calcuElement").val();
		var enumCategoryId=$("#enumCategoryId").val();
		var para={
			"ciLabelRule.calcuElement": calcuElement,
			"dimName":dimName,
			"enumCategoryId":enumCategoryId
		};
		var actionUrl = $.ctx + "/core/ciMainAction!findEnumLabelDimValue.ai2do?" + $("#pageForm").serialize();
		$("#enumLabelSettingList").page( {url:actionUrl,param:para,objId:"enumLabelSettingList",callback:pagetable});
	}
	
	//初始化枚举分类列表
	function initEnumCategorys(){
		var calcuElement=$("#calcuElement").val();
		var para={"labelId": calcuElement};
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
						$("#textTipSpan").show();
					}else if(cnum>0){
						$(".selBtn ,.selTxt").unbind("click");
						$(".querySelListBox").find("a").unbind("click");
						var html = $('<a href="javascript:void(0);" value="">所有分类</a>').bind("click",function(){
							selListBox(this);
						}); 
						var enumCategoryDivChild = $("#enumCategoryDiv dd");
						enumCategoryDivChild.empty().append(html);
						$.each(result.enumCategory, function(i,categorys){
							var $aTag = $('<a href="javascript:void(0);" value="' + categorys.enumCategoryId + '">' + categorys.enumCategoryName + '</a>').bind("click",function(){
								selListBox(this);
							}); 
							enumCategoryDivChild.append($aTag);
						});
			            $("#enumCategoryDiv").show();
			            
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
	
	function selListBox(ths){
		var selVal = $(ths).attr("value");
    	var selHtml = $(ths).html();
    	$(ths).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
    	$(ths).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
    	$(ths).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
   		//向隐藏域中传递值
        $(ths).parents(".querySelListBox").slideUp();
        var enumCategoryId=$("#enumCategoryId").val();
		if(enumCategoryId!=null&&enumCategoryId!=""){
			$("#addAllItemAction").show();
			$("#removeAllItemAction").show();
		}else{
			$("#addAllItemAction").hide();
			$("#removeAllItemAction").hide();
		}
   		searchItemList();
	}
	
	//查询枚举列表	
	function searchItemList(){
		//<div id="enumListLoading" class="_loading" style="display:none;">正在加载，请稍候 ...</div>
		//$("#enumListLoading").show();
		var dimName=$("#dimName").val();
		var calcuElement=$("#calcuElement").val();
		var enumCategoryId=$("#enumCategoryId").val();
		var actionUrl = $.ctx + "/core/ciMainAction!findEnumLabelDimValue.ai2do?" + $("#pageForm").serialize();
		var para={
				"ciLabelRule.calcuElement": calcuElement,
				"dimName":dimName,
				"enumCategoryId":enumCategoryId
			};
		$.ajax({
			url : actionUrl,
			type : "POST",
			dataType: "html",
			async : false,
			data : para,
			success : function(html){
				$("#enumLabelSettingList").html(html);
	            var totalSize = $("#totalSize").val();
	            if(totalSize){
		            $("#enumTotalSizeNum").html(totalSize);
		        }
				pagetable();
				//$("#enumListLoading").hide();
			},
			error : function(){
				//$("#enumListLoading").hide();
			}
		});
	}
	//模糊查询选中的属性值
	function searchAddItemList(){
		var addDimNameTmp=$.trim($("#addDimName").val());
		var addDimName = addDimNameTmp.toLowerCase();
		var $itemObjArr= $("#addItemDetailBox").find("a");
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
	//点击a标签添加移除选中效果
	function aAddOrRemoveClass(obj){
		if($(obj).hasClass("itemLiNextAHover")){
			$(obj).removeClass("itemLiNextAHover");
		}else{
			$(obj).addClass("itemLiNextAHover");
		}
	}
	//双击添加枚举值
	function addEnumItemByDbClick(ths) {
		var selectedItem = $(ths);
		$('#addItemDetailBox').find('a[id="'+selectedItem.attr('id')+'"]').parent()
				.remove();
		var $liTemp = $("<li><a href=\"javascript:void(0);\" ondblclick=\"delEnumItemByDbClick(this);\" onclick=\"aAddOrRemoveClass(this);\"  id='"+selectedItem.attr("id")+"' data='"+selectedItem.attr("data")+"' title='"+selectedItem.attr("title")+"' >"+selectedItem.html()+"</a></li>");
		$('#addItemDetailBox').append($liTemp);
		calcEnumChooseItemNum();
		//添加完成后，移除选中效果
		removeSelectedClass();
	}
	//计算已选数量
	function  calcEnumChooseItemNum(){
		var selectItemInfo= $("#addItemDetailBox").find("a");
		if(selectItemInfo){
			$("#selectedItemSize").html(selectItemInfo.length);
		}else{
			$("#selectedItemSize").html(0);
		}
	}
	//添加完成后，移除选中效果
	function removeSelectedClass(){
		$("#itemChooseDetailBox").find(".itemLiNextAHover").addClass("_disabled");
		$("#itemChooseDetailBox").find(".itemLiNextAHover").removeClass("itemLiNextAHover");
	}
	//双击删除枚举值
	function delEnumItemByDbClick(ths) {
		$(ths).parent().remove();
		calcEnumChooseItemNum();
	}
	function validateForm(){
		var selectItemList= $("#addItemDetailBox").find("a");
		if(selectItemList.length <= 0){
			showAlert("指标值不能为空，请重新选择","failed");
			return false;
		}
		/*if(selectItemList.length>$._maxItemNum){
			showAlert("最多选择" + $._maxItemNum +"个指标值，请重新选择","failed");
			return false;
		}*/
		return true;
	}
	function setItems(){
		if(validateForm()){
			var $itemObjArr= $("#addItemDetailBox").find("a");
			var temp = "" ;
			var title_temp="";
			var itemKeyArr = [];
			var itemNameArr = []; 
			for(var i = 0 ,len = $itemObjArr.length ; i< len ; i++){
				var $itemObj = $($itemObjArr[i]);
				itemKeyArr.push($itemObj.attr("data"));
				itemNameArr.push($itemObj.html());
			}
			var data=itemKeyArr.join(",") + ";" +itemNameArr.join(",");
			temp = "<span>"+itemNameArr.join(",")+ "</span>";
			title_temp = itemNameArr.join(",");
			
			var source = $("#source").val();
			var target = null;
			if (source == 1) {
				target = parent.$("._idClass");
			} else if (source == 2) {
				target = parent.$("._idClass").parent().parent();
			}
			
			target.attr("attrVal",itemKeyArr.join(","));
			target.attr("attrName",itemNameArr.join(","));
			
			if (source == 2) {
				var $conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
				var p = "";
				var title_temp_p ="";
				if($itemObjArr.length != 0){
					p = $("<p><label>已选择条件：</label>"+temp+ "</p>");
					title_temp_p = "已选择条件："+ title_temp;
				}
				$conditionMore.html('').append(p);
				$conditionMore.attr("title",title_temp_p);
			} else if (source == 1) {
				var sort = target.attr('sort');
				parent.coreMain.submitShopCartSession(sort);//提交session
			}
			
			target.removeClass("_idClass");
			parent.ShoppingCart.iframeCloseDialog("#enumLabelSetting");
		}
	}	
</script>
</head>
<body>
	<div id="enumTip" style="display: none;position: absolute;"></div>
	<div class="itemChooseTopBox" id="enumListOperateDiv">
        <div id="enumCategoryDiv" class="fleft formQuerySelBox" style="display:none;margin-top:5px;width:200px;">
			<div class="selTxt" style="width:166px;">所有分类</div>
			<a href="javascript:void(0);" class="selBtn"></a>
			<div class="querySelListBox">
			<input type="hidden" value="" id="enumCategoryId"/>
			<dl>
			<dd>
			</dd>
			</dl>
			</div>
		</div>
        <span id="textTipSpan" style="display:none;"> 请选择，可多选</span>
        <a id="importTip" class="fright fileUploadHelp tMargin5" title=""></a>
        
    </div>
    <div class="itemChooseActionBox clearfix">
        <div class="itemChooseBox fleft" id="itemChooseBoxPage">
            <div class="itemActionTopBox ">
                <ol>
                    <li><strong>共&nbsp;<span id="enumTotalSizeNum"></span>&nbsp;个</strong></li>
                    <li><a href="javascript:void(0);" onclick="setItemStatus(true,'itemChooseDetailBox');"> 全选</a></li>
                    <li><a href="javascript:void(0);" onclick="setItemStatus(false,'itemChooseDetailBox');"> 取消</a></li>
                </ol>

                <div class="itemSearchBox fright">
                    <input type="text" class="fleft searchInput" id="dimName" onkeydown="if(event.keyCode == 13){searchItemList();}"/>
                    <input type="button" class="fleft searchBtn" onclick="searchItemList();"/>
                </div>
            </div>
            <div id="enumLabelSettingList"></div>
        </div>
        <div class="itemChooseCenterBox">
             <div class="addActionBox" id="addItemAction">
               		添加
            </div>
            <div class="removeActionBox" id="removeItemAction">
                	删除
            </div>
             <div class="addActionBox allAddActionBox" id="addAllItemAction">
               		全部添加
            </div>
            <div class="removeActionBox allRmoveActionBox" id="removeAllItemAction">
                	全部删除
            </div>
        </div>

        <div class="itemChooseBox fright" >
            <div class="itemActionTopBox">
                <ol>
                    <li><strong>已选&nbsp;<span id="selectedItemSize"></span>&nbsp;个</strong></li>
                    <li><a href="javascript:void(0);" onclick="setItemStatus(true,'addItemDetailBox');"> 全选</a></li>
                    <li><a href="javascript:void(0);" onclick="setItemStatus(false,'addItemDetailBox');"> 取消</a></li>
                </ol>

                <div class="itemSearchBox fright">
                    <input type="text" class="fleft searchInput" id="addDimName" onkeydown="if(event.keyCode == 13){searchAddItemList();}"/>
                    <input type="button" class="fleft searchBtn" onclick="searchAddItemList();"/>
                </div>
            </div>

            <div class="itemChooseContentBox">
            	<div class="_loading" id="_loading" style="display:none;">正在加载，请稍候 ...</div>
                <ol class="clearfix" id="addItemDetailBox">
                </ol>
            </div>
        </div>
    </div>
    <!-- 按钮操作-->
    <div class="btnActiveBox">
        <input type="button" value="确定" class="ensureBtn" onclick="setItems();"/>
    </div>
    <input name ="calcuElement" id="calcuElement" type="hidden" />
    <input type="hidden" id="source" name="source" value="${source}"/>
</body>
</html>