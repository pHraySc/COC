<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<%@ include file="/aibi_ci/html_include.jsp"%>
<c:set var="DIM_LABEL" value="<%=CommonConstants.ALL_EFFECTIVE_LABEL_MAP%>"></c:set>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>标签关联分析</title>
<script type="text/javascript">
var relLabelIds = '';
var dropCount = 0;

//获取屏幕滚动高度，定位alert位置
function getPageScroll_y(){
	var _y=$(window.parent).scrollTop();
	return _y;
}

$(function(){
	initDate();
	var myHeight = document.body.scrollHeight;
	$(window.parent.document).find("#Frame0").height(myHeight+40);
	showLabelTree("ct");
	initRelArea();

});

//时间轴初始化
function initDate(){
	//时间轴
	var month = "${newLabelMonth}";
	if(month == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
			type: "POST",
			success: function(result){
				month = result;
				creatDateline($("#dateline_ct"),month,dataDateClick);
			}
		});
	}else{
		creatDateline($("#dateline_ct"),month,dataDateClick);
	}
	
	//获取传入的时间值
	var curDataDate = $('#dataDate').val();
	if(curDataDate != "" && curDataDate != month){
		//取消默认的日期选择
		$("li[_value='"+month+"']").removeClass("onchoose");
		//设置传入的日期为选择状态
		$("li[_value='"+curDataDate+"']").addClass("onchoose");
	} else {
		$('#dataDate').val(month);
	}
	
}

//点击时间轴触发函数
function dataDateClick(){
	//设置时间值
	$('#dataDate').val($(".date_line").attr("_value"));
	
	var mainLabelId = $('#mainLabelId').val();
	var dataDate = $('#dataDate').val();
	
	var param = "?ciLabelRelModel.mainLabelId=" + mainLabelId;
		param = param + "&ciLabelRelModel.dataDate=" + dataDate;
	var actionUrl=$.ctx + "/ci/ciLabelRelAnalysisAction!findMainLabelUserNum.ai2do"+param
	relLabelIds = "";
	$.ajax({
			type: "POST",
			url: actionUrl,
			dataType: "json",
			async: false,
			success: function(result){
				if(result.success){
					var mainLabelUserNumShow = result.ciLabelRelModel.mainLabelUserNumShow;
					var mainLabelUserNum = result.ciLabelRelModel.mainLabelUserNum;
					$('#mainUserNumShow').html(mainLabelUserNumShow);
					if(mainLabelUserNum > 0) {
						$('#tag_dropable').find(".conditionCT").each(function() {
							labelId = $(this).attr("labelId");
							relLabelIds = relLabelIds + labelId + ",";
							$('#persent_'+labelId).html('<img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif">');
							$('#relLabel_' + labelId).unbind();
						});
						setTimeout('setRelLabelProportion()',100); 
					} else {
						$('#tag_dropable').find(".conditionCT").each(function() {
							labelId = $(this).attr("labelId");
							relLabelIds = relLabelIds + labelId + ",";
							$('#persent_'+labelId).html('0%');
						});
						parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
					}
					
				}else{
					$('#tag_dropable').find(".conditionCT").each(function() {
						labelId = $(this).attr("labelId");
						relLabelIds = relLabelIds + labelId + ",";
						$('#persent_'+labelId).html('0%');
					});
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
				}
			}
	});

}

function setRelLabelProportion() {

	//关联区域有选择的标签
	var mainLabelId = $('#mainLabelId').val();
	var mainLabelUserNum = $('#mainLabelUserNum').val();
	var dataDate = $('#dataDate').val();
	if(relLabelIds != "") {
		//当在计算关联度时不能保存，需等到关联度计算完成之后才可以保存
		dropCount++;
		$("#saveRelButton").addClass("tag_btn_disable").removeAttr("title").attr("title","关联分析结果产生后才可以保存");
		$("#saveRelButton").unbind("click");
		var param = "?ciLabelRelModel.mainLabelId=" + mainLabelId;
		param = param + "&ciLabelRelModel.mainLabelUserNum=" + mainLabelUserNum;
		param = param + "&ciLabelRelModel.dataDate=" + dataDate;
		param = param + "&ciLabelRelModel.relLabelIds=" + relLabelIds;
		var actionUrl=$.ctx + "/ci/ciLabelRelAnalysisAction!findRelLabelByDate.ai2do"+param

		$.ajax({
			type: "POST",
			url: actionUrl,
			dataType: "json",
			async: true,
			success: function(result){
				if(result.success){
					jQuery.each(result.relLabelList, function(i,item){
						$('#persent_'+item.relLabelId).html(item.proportion+'%');
						$('#relLabel_'+item.relLabelId).bind('click',function(){
							deleteThis(this);
						});
						$('#persent_'+item.relLabelId).attr('userNum',item.overlapUserNum);
                    });  
				}else{
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
				}

				//当在计算关联度时不能保存，需等到关联度计算完成之后才可以保存
				dropCount--;
				if(dropCount == 0){
					$("#saveRelButton").removeClass("tag_btn_disable").removeAttr("title");
					$("#saveRelButton").unbind("click").bind("click",function(){saveRelLabel();});
				}
				
			}
		});
	}		
}

function initLabelTree(actionUrl) {
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.treeList;
			}else{
				parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
			}
			//$("#content").append(result.labelName);
		}
	});

	initTree();
}

function initTree() {
	var labelType = $('#labelType').val();

	//初始化第一级node的样式
	$.fn.zTree.init($("#analyse_tree"), setting, zNodes);
	zTree_Menu = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodes_0=zTree_Menu.getNodes()[0];
	curMenu = nodes_0;
	if(!curMenu){
		return;
	}
	while(curMenu.children&&curMenu.children.length > 0){
		curMenu = curMenu.children[0];
	}

	if(curMenu.click != false){
		zTree_Menu.selectNode(curMenu);
	}else{
		$("#" + nodes_0.tId + "_a").click();
	}
	$("#" + curMenu.tId + "_a").click();
	var a = $("#" + nodes_0.tId + "_a");
	a.addClass("cur");
	var par=a.parent();
	par.addClass("cur_parent");
	par.parent().find("> li.level0").each(function(){
		$(this).find("> a.level0").prepend("<div class='slideIcon'></div>");
	});
	
	//标签拖动
	$("#analyse_tree").mouseover(function(e){		
		
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		e=e||window.event;
		var tar=$(e.target||e.srcElement);
		var canDrag = 0;
		if(!tar.hasClass("treenode") || tar.hasClass("ui-draggable") || tar.hasClass("level0")) return;
		var tagId = parseInt(tar[0].id.replace(/analyse_tree_/,"").replace(/_a/,""));
		var nodeObj = zTree_Menu.getNodeByTId("analyse_tree_"+tagId);
		if(nodeObj.param.isCanDrag != null || nodeObj.param.isCanDrag != undefined){
			canDrag = nodeObj.param.isCanDrag;
		}
		if(canDrag == 0){
			$("#"+tar[0].id).css({"cursor":"default"});
			$("#"+tar[0].id).find(".button").css({"cursor":"default"});
			return;
		}
		var dropTar=$("#tag_dropable").parent();
		tar.draggable({ 
			revert: "invalid", 
			helper: "clone" ,
			start: function() {
				var startY=dropTar.offset().top;
				var fix=$(window.parent).scrollTop()-startY;
				var posy=$(this).offset().top;
				var margin=posy-startY-dropTar.height()/2;
				fix>0?dropTar.css("margin-top",margin):"";
			},
			stop:function(){
				dropTar.css("margin-top",8)
			}
		});
	})
	
	//$("#xxxxxxxxxxx").draggable({ revert: "invalid", helper: "clone" });
	//释放标签
	dropTag();
	autoHeight();
}
//自动高度
function autoHeight(){
	var _autoheight = $(".analyse_left").height();
	var _autoheight2 = $(".analyse_right").height();
	if(_autoheight2 > _autoheight){
		_autoheight = _autoheight2;
	}
	var contentFrame = $("iframe:visible",window.parent.document);
	contentFrame.height(_autoheight+50);
}

function initLabelTreeByName(actionUrl,labelName) {
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		data: ({"labelName" : labelName}),
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.treeList;
			}else{
				parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
			}
			//$("#content").append(result.labelName);
		}
	});
	
	initTree();
}

//释放正在拖动的系统标签
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_dropable").droppable({
		accept: "#analyse_tree a",
		greedy:true,
		drop: function( event, ui ) {
			$(this).find(".drag_tip").hide();
			var onDragTag=ui.draggable;
			tagOnDrop(onDragTag,$(this));
		}
	});
}

function tagOnDrop(dragtag,ct){
	var tagId = parseInt(dragtag[0].id.replace(/analyse_tree_/,""));
	var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
	var relLabelId = nodeObj.id;
	var nodeExist = 0;
	var mainLabelId = $('#mainLabelId').val();
	var relLabelNum = 1;

	$('#tag_dropable').find(".conditionCT").each(function() {
		var labelId = $(this).attr("labelId");
		if(nodeObj.id == labelId) {
			nodeExist = 1;
		}
		relLabelNum = relLabelNum + 1;
	});

	var _txt=dragtag.find("span").eq(1).html();
	var html='<div class="conditionCT" labelId="'+nodeObj.id+'">';
	html+='<div class="left_bg"><a id="relLabel_'+nodeObj.id+'" href="javascript:void(0)"></a></div>';
	html+='<div class="midCT">'+_txt+'</div>';
	html+='<div class="right_bg"></div>';
	html+='<ul class="persent"><li class="onlyNum" id="persent_'+nodeObj.id+'" userNum=""><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li></ul>';
	html+='</div>';	

	if(mainLabelId == nodeObj.id) {
		parent.showAlert('不能与主标签重复!',"failed",null,null,500,200,getPageScroll_y());
		return false;
	}
	
	if(nodeExist == 1) {
		parent.showAlert('不能选择重复的标签做关联!',"failed",null,null,500,200,getPageScroll_y());
		return false;
	}

	if(relLabelNum > 10) {
		parent.showAlert('最多只能选择十个标签做关联分析!',"failed",null,null,500,200,getPageScroll_y());
		return false;
	}

	ct.append(html);
	//setTimeout('getRelLabelData('+relLabelId+')',10000);
	getRelLabelData(relLabelId);
}
function deleteThis(obj){
	if($(obj).parent().parent().siblings().length == 0){
		$("#saveRelButton").addClass("tag_btn_disable").removeAttr("title").attr("title","关联分析结果产生后才可以保存");
		$("#saveRelButton").unbind("click");
	}
	$(obj).parent().parent().remove();
}

function getRelLabelData(relLabelId) {

	//当在计算关联度时不能保存，需等到关联度计算完成之后才可以保存
	dropCount++;
	$("#saveRelButton").addClass("tag_btn_disable").removeAttr("title").attr("title","关联分析结果产生后才可以保存");
	$("#saveRelButton").unbind("click");
	var mainLabelId = $('#mainLabelId').val();
	var mainLabelUserNum = $('#mainLabelUserNum').val();
	var dataDate = $('#dataDate').val();
	var param = "?ciLabelRelModel.mainLabelId=" + mainLabelId;
		param = param + "&ciLabelRelModel.mainLabelUserNum=" + mainLabelUserNum;
		param = param + "&ciLabelRelModel.dataDate=" + dataDate;
		param = param + "&ciLabelRelModel.relLabelId=" + relLabelId;
	var actionUrl = $.ctx + "/ci/ciLabelRelAnalysisAction!findRelLabelData.ai2do" + param;
	var overlapUserNum = 0;
	var proportion = '';
	
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: true,
		dataType: "json",
		success: function(result){
			if(result.success){
				//计算出关联度后，使保存按钮可点击
				
				overlapUserNum = result.ciLabelRelModel.overlapUserNum;
				proportion = result.ciLabelRelModel.proportion;
				if(proportion == 0.00) {
					proportion = "0.00%";
				} else {
					proportion = result.ciLabelRelModel.proportion + "%";
				}
				$('#persent_'+relLabelId).html(proportion);
				$('#relLabel_'+relLabelId).bind('click',function(){
					deleteThis(this);
				});
				$('#persent_'+relLabelId).attr('userNum',overlapUserNum);
			}else{
				parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
			}
			//$("#content").append(result.labelName);
			//当在计算关联度时不能保存，需等到关联度计算完成之后才可以保存
			dropCount--;
			if(dropCount == 0){
				$("#saveRelButton").removeClass("tag_btn_disable").removeAttr("title");
				$("#saveRelButton").unbind("click").bind("click",function(){saveRelLabel();});
			}
		}
	});
}

//ztree
var curMenu = null, zTree_Menu = null;
var setting = {
	view: {
		showLine: true,
		selectedMulti: false,
		dblClickExpand: false
	},
	data: {
		key: {
			title: "tip"
		},
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: this.beforeClick,
		onClick: this.onClick
	}
};

//点击节点前判断是否第一级,不同展示方式
function beforeClick(treeId, node) {
	if(node.click != false){
		//恢复原有样式
		var icon = $(".curSelectedNode").find(".button");
		if(icon.css("background")){
			var bg = icon.css("background").replace("_on","");
			icon.css("background", bg);
		}
	}
	
	if (node.isParent) {
		if (node.level === 0) {
			var pNode = curMenu;
			while (pNode && pNode.level !==0) {
				pNode = pNode.getParentNode();
			}
			if (pNode !== node) {
				var a = $("#" + pNode.tId + "_a");
				a.removeClass("cur");
				zTree_Menu.expandNode(pNode, false,true,true,true);
			}
			a = $("#" + node.tId + "_a");
			a.addClass("cur");
			var cur_parent=a.parent();
			var open_close=true;
			if(cur_parent.hasClass("cur_parent")){
				cur_parent.removeClass("cur_parent");
				open_close=false;
			}else{
				cur_parent.addClass("cur_parent").siblings(".cur_parent").removeClass("cur_parent");
			}
			var isOpen = false;
			if(node.children != null)
				for (var i=0,l=node.children.length; i<l; i++) {
					if(node.children[i].open) {
						isOpen = true;
						break;
					}
				}
			if (isOpen) {
				zTree_Menu.expandNode(node, open_close);
				curMenu = node;
			} else {
				zTree_Menu.expandNode(node.children[0].isParent?node.children[0]:node, open_close,true,true,true);
				curMenu = node.children[0];
			}
			
		} else {
			//非叶子节点的点击展开功能屏蔽，否则和点击事件同时调用，展开节点很慢，可以通过点击非叶子节点前的加号展开下一级节点
			//zTree_Menu.expandNode(node,true,true,true,true);
		}
	}
	//如果返回return !node.isParent，那么就会有false的情况，根据ztree的api当返回false时，onClick事件就会失效
	//return true;
	return (node.click != false);
}

//节点点击事件
function onClick(e, treeId, node) {
	if ((node.isParent && node.level !== 0) || !node.isParent) {
		initClick();
	}
}

//初始化节点选中事件
function initClick() {
	var icon = $(".curSelectedNode").find(".button");
	var bg = icon.css("background");
	var last = bg.lastIndexOf("/");
	var right = bg.lastIndexOf(")");
	var begin = bg.substring(0, last+1);
	var middle = bg.substring(last+1, right);
	var iconExt = middle.substring(middle.indexOf("."), middle.length);
	var iconName = middle.substring(0, middle.indexOf(".")) + "_on" + iconExt;
	var end = bg.substring(right, bg.length);
	var newBg = begin + iconName + end;
	icon.css("background", newBg);
}

function saveRelLabel() {
	var overlapUserNumStr = '';
	var relLabelNum = 0;
	relLabelIds = "";
	var mainLabelId = $('#mainLabelId').val();
	var mainLabelUserNum = $('#mainLabelUserNum').val();
	var dataDate = $('#dataDate').val();
	$('#tag_dropable').find(".conditionCT").each(function() {
		labelId = $(this).attr("labelId");
		relLabelIds = relLabelIds + labelId + ",";
		userNum = $("#persent_"+labelId).attr("userNum");
		overlapUserNumStr = overlapUserNumStr + userNum + ",";
		relLabelNum = relLabelNum + 1;
	});

	$('#relLabelIds').val(relLabelIds);
	$('#overlapUserNumStr').val(overlapUserNumStr);

	var param = "?ciLabelRelModel.mainLabelId=" + mainLabelId;
	param = param + "&ciLabelRelModel.mainLabelUserNum=" + mainLabelUserNum;
	param = param + "&ciLabelRelModel.dataDate=" + dataDate;
	param = param + "&ciLabelRelModel.relLabelIds=" + relLabelIds;	
	param = param + "&ciLabelRelModel.overlapUserNumStr=" + overlapUserNumStr;
	var url = $.ctx+'/ci/ciLabelRelAnalysisAction!toLabelRelMain.ai2do'+param;
	$(window.parent.document).find("iframe").each(function(index){
		if(index == 0) {
			$(this).attr("src",url);
		}
	});
}

function showLabelTree(who) {
	$('#searchLabelName').val('');
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
	$('#labelType').val(who);
	if('ct' == who) {
		$('#ct_tree').addClass('on');
		$('#my_tree').removeClass('on');
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
	} else if('my' == who) {
		$('#my_tree').addClass('on');
		$('#ct_tree').removeClass('on');
		
		//对比标签ID
		var mainLabelId = $('#mainLabelId').val();
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTree.ai2do?type=rel&mainLabelId="+mainLabelId;
	} 
	initLabelTree(actionUrl);
}

function searchLabelByName() {
	var labelName = $('#searchLabelName').val();
	labelName = $.trim(labelName);
	var labelType = $('#labelType').val();
	var actionUrl = "";
	if('ct' == labelType) {
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTreeByName.ai2do";
	} else if('my' == labelType) {
		//actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeByName.ai2do";
		var mainLabelId = $('#mainLabelId').val();
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeByName.ai2do?type=rel&mainLabelId="+mainLabelId;
	}
	initLabelTreeByName(actionUrl,labelName);
}

function toLabelConstrast() {
	var mainLabelId = $('#mainLabelId').val();
	var dataDate = $('#dataDate').val();
	var param = "mainLabelId="+mainLabelId;
	param = param + "&dataDate="+dataDate;
	var url = $.ctx + "/ci/ciLabelConstrastAnalysisAction!initLabelConstrast.ai2do?" + param;
	window.location.href = url;
}

//初始化展示区域
function initRelArea(){
	var mainLabelId = $('#mainLabelId').val();
	var mainLabelUserNum = $('#mainLabelUserNum').val();
	var dataDate = $('#dataDate').val();
	var param = "?ciLabelRelModel.mainLabelId=" + mainLabelId;
		param = param + "&ciLabelRelModel.mainLabelUserNum=" + mainLabelUserNum;
		param = param + "&ciLabelRelModel.dataDate=" + dataDate;
	var actionUrl=$.ctx + "/ci/ciLabelRelAnalysisAction!findLabelRel.ai2do"+param;
	$.ajax({
			type: "POST",
			url: actionUrl,
			dataType: "json",
			async: true,
			success: function(result){
				if(result.success){					
					if(result.ciLabelRelModelList.length > 0){
						$("#tag_dropable").empty();
					}
					relLabelIds = "";
					jQuery.each(result.ciLabelRelModelList, function(i,item){    
						//var _txt=dragtag.find("span").eq(1).html();
						relLabelIds = relLabelIds + item.relLabelId + ",";
						var html='<div class="conditionCT" labelId="'+item.relLabelId+'">';
						html+='<div class="left_bg"><a id="relLabel_'+item.relLabelId+'" href="javascript:void(0)"></a></div>';
						html+='<div class="midCT">'+item.relLabelName+'</div>';
						html+='<div class="right_bg"></div>';
						html+='<ul class="persent"><li class="onlyNum" id="persent_'+item.relLabelId+'" userNum=""><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li></ul>';
						html+='</div>';	
						$("#tag_dropable").append(html);
                    });  
					setTimeout('setRelLabelProportion()',100);
				}else{
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
				}
			}
	});
}
</script>
<style type="text/css">

</style>
</head>

<body class="body_bg">
<input id="labelType" type="hidden" value="ct"/>
<input id="mainLabelId" type="hidden" value="${ciLabelRelModel.mainLabelId}"/>
<input id="mainLabelUserNum" type="hidden" value="${ciLabelRelModel.mainLabelUserNum}"/>
<input id="dataDate" type="hidden" value="${ciLabelRelModel.dataDate}"/>
<input id="relLabelIds" type="hidden" />
<input id="overlapUserNumStr" type="hidden"/>
<div id="analyseDiv">
<div class="analyse_left">
	<ul class="analyse_tab">
    	<li class="on" id="ct_tree" onClick="showLabelTree('ct');">标签库</li>
        <li class="noborder" id="my_tree" onClick="showLabelTree('my');">我的标签库</li>
    </ul>
    
    <div class="analyse_tab_ct" id="analyse_tab_ct">
        <div class="analyse_date">
            <table class="commonTable">
                <tr>
                    <td>
                        <div class="analyse_search">
                            <input type="text" id="searchLabelName"/>
                            <span class="icon_search" onClick="searchLabelByName();"></span>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        
        <div class="tree_area">
        	<ul id="analyse_tree" class="ztree"></ul>
        </div><!--tree_area end -->
        
    </div><!--analyse_tab_ct end -->
    
</div><!--analyse_left end -->

<div class="analyse_right" style="margin-top:0px">
    
	<dl class="analyse_right_dl marginTop_0">
        <dt><span>日期选择</span></dt>
        <dd>
        	<table class="commonTable mainTag_show">
            	<tr>
                	<td id="dateline_ct">
                        
                    </td>
                    <td width="150">
                    	<font id="mainUserNumShow" class="green_num bold">
							<fmt:formatNumber type="number" value="${ciLabelRelModel.mainLabelUserNum}" pattern="###,###,###" />
                    	</font>
                    	<p>用户数</p>
                    </td>
                </tr>
            </table>
        </dd>
    </dl>
    
    <dl class="analyse_right_dl">
    	<dt><span>与主标签(<font style='font-weight:bold;'>${ciLabelRelModel.mainLabelName}</font>)的关联度分析展示</span></dt>
        <dd id="tag_dropable" class="tag_analyse_ct tag_dropable">
            <div class="drag_tip"></div>
        </dd>
    </dl>
    
    <div class="analyse_right_btns">
    	<!--<input id="submit" type="button" class="tag_btn" value="保存" onClick="saveRelLabel();" />-->
    	<input id="saveRelButton" type="button" class="tag_btn tag_btn_disable" value="保存" title="关联分析结果产生后才可以保存" />
    	
    </div>
</div><!--analyse_right end -->
</div>
<div id="analyseResultDiv" style="display:none;">
	
</div>
</body>
</html>
