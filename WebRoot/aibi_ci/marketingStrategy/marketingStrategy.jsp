<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>数据探索</title>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" />
<script type="text/javascript">
var zNodes;
//对比标签数组
$(document).ready(function(){
	$("#initSaveMatchdialog").addClass("tag_btn_disable").attr('disabled',true);
	showProductTree();
	$(".recommend").click(function(){
		$(".warnBox").hide();
		if($(this).hasClass("recommend_active")) 
			$(this).removeClass("recommend_active");
		else 
			$(this).addClass("recommend_active");
		if(($(".recommend.recommend_active").length+$(".conditionCT.conditionCT_orange").length) != 0){
			//$("#saveMatchRelation").addClass("tag_btn_disable").attr('disabled',true);
			$("#initSaveMatchdialog").removeClass("tag_btn_disable").attr('disabled',false);
		}else{
			$("#initSaveMatchdialog").addClass("tag_btn_disable").attr('disabled',true);
			
			$("#saveMatchRelation").removeClass("tag_btn_disable").attr('disabled',false).bind("click",function(){
				nextSaveRelation();
			});
		}
		if(($(".recommend.recommend_active").length+$(".conditionCT.conditionCT_orange").length) > 5){
			$(this).removeClass("recommend_active");
			$(".warnBox").show();
			var toffset=$(this).offset();
			$(".warnBox").css({top:toffset.top + 65, left: toffset.left+80}); 
			$(".warnBox .close").click(function(){
				$(".warnBox").hide();
			});
		}
	})
	
	$(".conditionCT").click(function(){
		var productStr = "";
		$(".warnBox").hide();
		if($(this).hasClass("conditionCT_orange")) $(this).removeClass("conditionCT_orange");
		else $(this).addClass("conditionCT_orange");

		if(($(".recommend.recommend_active").length+$(".conditionCT.conditionCT_orange").length) != 0){
			//$("#saveMatchRelation").addClass("tag_btn_disable").attr('disabled',true);
			$("#initSaveMatchdialog").removeClass("tag_btn_disable").attr('disabled',false);
		}else{
			$("#initSaveMatchdialog").addClass("tag_btn_disable").attr('disabled',true);
			$("#saveMatchRelation").removeClass("tag_btn_disable").unbind("click").bind("click",function(){
				saveMatchRelation();
			});
		}
		
		if(($(".conditionCT_orange").length+$(".recommend_active").length) > 5){
			$(this).removeClass("conditionCT_orange");
			$(".warnBox").show();
			var toffset=$(this).offset();
			$(".warnBox").css({top:toffset.top + 65, left: toffset.left+80}); 
			$(".warnBox .close").click(function(){
				$(".warnBox").hide();
			});
		}else{
			$(".conditionCT_orange").each(function(){
				productStr = productStr + $(this).find(".productIds").val()+',';
			});
			if(productStr.length >0){
				productStr = productStr.substring(0,productStr.length-1);
			}
			$('#productIds').val(productStr);
		}
	});
	
	
	//保存营销策略
	$("#initSaveMatchdialog").click(function(){
	    if($("#customNum").val() == ""){
	    	window.parent.showAlert("没有清单表","failed");
            return false;
        }   

        var outOfDate = false;

		$(".conditionCT").each(function(){
			if($(this).hasClass("outOfDate")){
				outOfDate = true;
				return false;
			}
		});

		if(outOfDate){
			window.parent.showAlert("产品没有数据","failed");
			return false;
		}

		var productsIdsStr = "";
		//选择的手动拖动的产品
		$(".conditionCT.conditionCT_orange").each(function(){
			if($(this).find(".productIds").length != ""){
				productsIdsStr = productsIdsStr + $(this).find(".productIds").val()+',';
			}
		});

		//选择的系统匹配的产品
		$(".recommend.recommend_active").each(function(){
			if($(this).find(".sysProductIds").length != ""){
				productsIdsStr = productsIdsStr + $(this).find(".sysProductIds").val()+',';
			}
		});

		$('#productsIdsStr').val(productsIdsStr);

			
		if($('#productsIdsStr').val() == ""){
			window.parent.showAlert("请选择产品","failed");
			return false;
		}

		var productsStr = "";
		$(".productIds").each(function() {
			productsStr = productsStr + $(this).val() +",";
		});
		
		$('#productsStr').val(productsStr);
		
		var actionUrl = "${ctx}/ci/marketingStrategyAction!initSaveMarketing.ai2do";
		$("#matchForm").attr("action", actionUrl);
		$('#matchForm').submit();
	});
	
});

//保存匹配关系
function nextSaveRelation(){

	if($("#customNum").val() == ""){
		window.parent.showAlert("没有清单表","failed");
        return false;
    }

    var outOfDate = false;

	$(".conditionCT").each(function(){
		if($(this).hasClass("outOfDate")){
			outOfDate = true;
			return false;
		}
	});

	if(outOfDate){
		window.parent.showAlert("产品没有数据","failed");
		return false;
	}

	
	var productsStr = "";
	$(".productIds").each(function() {
		productsStr = productsStr + $(this).val() +",";
	});
	
	if($(".sysProductIds").length < 1){
		if(productsStr == ""){
			window.parent.showAlert("请选择产品","failed");
			return false;
		}
	}
	productsStr = productsStr.substring(0,productsStr.length -1)
	$('#productsStr').val(productsStr);
	$('#matchForm').submit();
}

function showProductTree() {
		$('#searchProductName').val('');
		$('#ct_tree').addClass('on');
		var actionUrl = "<%=request.getContextPath() %>/ci/productManagerAction!findProductTree.ai2do";
		initProductTree(actionUrl);
}

function searchProductByName() {
		var productName = $('#searchProductName').val();
		var actionUrl = "<%=request.getContextPath() %>/ci/productManagerAction!findProductTree.ai2do";
		initProductTreeByName(actionUrl,productName);
}

function initProductTree(actionUrl) {
		$.ajax({
			type: "POST",
			url: actionUrl,
			async: false,
			dataType: "json",
			success: function(result){
				if(result.success){
					zNodes = result.treeList;
				}else{
					parent.showAlert(result.msg,"failed");
				}
			}
		});

		initTree();
}

function initProductTreeByName(actionUrl,productName) {
		$.ajax({
			type: "POST",
			url: actionUrl,
			async: false,
			data: ({"productName" : productName}),
			dataType: "json",
			success: function(result){
				if(result.success){
					zNodes = result.treeList;
				}else{
					parent.showAlert(result.msg,"failed");
				}
			}
		});
		
		initTree();
}

//树节点初始化
function initTree(){
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
	
	
	//var tagId = parseInt(dragtag[0].id.replace(/analyse_tree_/,""));
	//var nodeObj = zTree_Menu.getNodeByTId("analyse_tree_"+tagId);
	
	
	//标签拖动
	$("#analyse_tree").mouseover(function(e){
		e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
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
		tar.draggable({ revert: "invalid", helper: "clone" });
	})
	
	//释放标签
	dropTag();
}

//释放正在拖动的标签
function dropTag(){	
	//分析区域
	var tag_analyse_dropable=$("#tag_analyse_dropable");
	tag_analyse_dropable.droppable({
		accept: "#analyse_tree a",
		drop: function( e, ui ) {
			$(this).find(".drag_tip").hide();
			var onDragTag=ui.draggable;
			tag_analyseOnDrop(onDragTag,$(this));
		}
	});
	
	//浮动层区域
	$("#tag_dropable").droppable({
		accept: "#analyse_tree a",
		tolerance: 'touch' ,
		greedy:true,
		over:function(){
			tag_analyse_dropable.droppable( 'disable' ) 
		},
		out:function(){
			tag_analyse_dropable.droppable( 'enable' );
		},
		drop: function( event, ui ) {
			var onDragTag=ui.draggable;
			tag_analyse_dropable.droppable( 'enable' );
			tagOnDrop(onDragTag,$(this));
		}
	});
}
function tag_analyseOnDrop(dragtag,ct){
	var tagId = parseInt(dragtag[0].id.replace(/analyse_tree_/,""));
	var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
	
	//最多 20 个产品对比
	if($('.productIds').length>=20){
		window.parent.showAlert("最多只能与20个产品进行产品营销策略","failed");	
		  return false;	
	}
	
    var repate = true;
	
	$('.productIds').each(function(){
		if($(this).val() == nodeObj.id){
			window.parent.showAlert("产品 "+nodeObj.name+" 不能重复添加！","failed");
			repate = false;
			return false;
		}
	});
	//判断一下与系统匹配的重叠问题
	var sysRepate = true;
	$('.sysProductIds').each(function(){
		if($(this).val()==nodeObj.id){
			window.parent.showAlert("系统匹配中已包含产品 "+nodeObj.name+"！","failed");
			sysRepate = false;
			return false;
	    }
	});

	if(!repate){
		return repate;
	}
	if(!sysRepate){
		return sysRepate;
	}
	
	var _txt=dragtag.find("span").eq(1).html();
	var html='<div class="conditionCT';
	var choiceDate = $("#productDate").val();
    var effectDate = nodeObj.param.effectDate;
    if(choiceDate < effectDate){
		html+=' outOfDate"';
	}else{
		html+='"';
	}
	html+='effectDate="' + effectDate + '" >';
	html+='<div class="left_bg"><a href="javascript:void(0)" onclick="onlydeleteThis(this)"></a></div>';
	html+='<div class="midCT">'+_txt+'<span class="selected">&nbsp;</span></div>';
	html+='<div class="right_bg"></div>';
	html+='<input type="hidden" class="productIds" value="'+nodeObj.id+'"></input>';
	html+='</div>';	
	var obj=$(html);
	obj.click(function(){
		var productStr = "";
		$(".warnBox").hide();
		if($(this).hasClass("conditionCT_orange")) $(this).removeClass("conditionCT_orange");
		else $(this).addClass("conditionCT_orange");

		if(($(".recommend.recommend_active").length+$(".conditionCT.conditionCT_orange").length) != 0){
			//$("#saveMatchRelation").addClass("tag_btn_disable").attr('disabled',true);		
			$("#initSaveMatchdialog").removeClass("tag_btn_disable").attr('disabled',false);
		}else{
			$("#saveMatchRelation").removeClass("tag_btn_disable").attr('disabled',false).bind("click",function(){
				saveMatchRelation();
			});
			$("#initSaveMatchdialog").addClass("tag_btn_disable").attr('disabled',true);
		}
		
		if(($(".conditionCT_orange").length+$(".recommend_active").length) > 5){
			$(this).removeClass("conditionCT_orange");
			$(".warnBox").show();
			var toffset=$(this).offset();
			$(".warnBox").css({top:toffset.top + 65, left: toffset.left+80}); 
			$(".warnBox .close").click(function(){
				$(".warnBox").hide();
			});
		}else{
			$(".conditionCT_orange").each(function(){
				productStr = productStr + $(this).find(".productIds").val()+',';
			});
			if(productStr.length >0){
				productStr = productStr.substring(0,productStr.length-1);
			}
			$('#productIds').val(productStr);
		}
	})
	ct.append(obj);
}
function tagOnDrop(dragtag,ct){
	//运算符拼接，对比分析关联分析不需要
	var _chaining='<div class="chaining">';
	_chaining+='<div class="left_bg"></div>';
	_chaining+='<div class="midCT">';
	_chaining+='<a class="icon_rightArr" href="javascript:void(0)" onclick="switchConnector(this,event)">且</a>';
	_chaining+='</div>';
	_chaining+='<div class="right_bg"></div>';
	_chaining+='</div>';
	var _txt=dragtag.find("span").eq(1).html();
	var html='<div class="conditionCT">';
	html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this)"></a></div>';
	html+='<div class="midCT">'+_txt+'</div>';
	html+='<div class="right_bg"></div>';
	html+='</div>';
	if(ct.find(".conditionCT").length==0){
		ct.append(html);
	}else{
		ct.append(_chaining+html);
	}
	var tar=$("body > .tag_operation");
	if(!tar.hasClass("firstdragDone")){
		tar.addClass("firstdragDone").find(".firsttime_tip").show();
	}
}

function onlydeleteThis(obj){
	$(obj).parent().parent().remove();
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
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: this.beforeClick,
		onDrop: this.onDrop,
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
			if(node.children){
				for (var i=0,l=node.children.length; i<l; i++) {
					if(node.children[i].open) {
						isOpen = true;
						break;
					}
				}	
			}else{
				isOpen = true;
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

</script>
<style type="text/css">

</style>
</head>

<body class="body_bg tag_operation_body">
<div class="analyse_left">
	<ul class="analyse_tab">
    	<li class="on">产品库</li>
    </ul>
    
    <div class="analyse_tab_ct">
        <div class="analyse_date">
            <table class="commonTable">
                <tr>
                    <td>
                        <div class="analyse_search">
                            <input type="text" id="searchProductName" />
                            <span class="icon_search" onclick="searchProductByName()"></span>
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

<div class="analyse_right" style="margin-top:-7px">
    <dl class="analyse_right_dl">
    	<dt><span>产品匹配选择</span></dt>
        <dd id="tag_analyse_dropable" class="tag_analyse_ct tag_dropable">
            <div <c:if test="${productMatchList != null}"> style="display:none"</c:if> class="drag_tip">
            </div>
            <c:forEach items="${productMatchList}" var="productList">
                <div class="conditionCT">
                    <div class="left_bg"><a href="javascript:void(0)" onclick="onlydeleteThis(this)"></a></div>
                    <div class="midCT" >${productList.productName}<span class="selected">&nbsp;</span></div>
                    <div class="right_bg"></div>
                    <input type="hidden" class="productIds" value="${productList.productId}"></input>
                </div>
            </c:forEach>
        </dd>
    </dl>
    
    <dl class="analyse_right_dl">
    	<dt><span>系统匹配产品</span></dt>
        <dd class="recommend_ct">
          <c:forEach items="${sysProductMatchList}" var="productList">
            	<div class="recommend">
            	   <h5>${productList.productName}</h5>
            	   <div class="nums">${productList.matchPropotion}%</div>
            	   <input type="hidden" id="sysProductIds" class="sysProductIds" value="${productList.productId}"></input>
                 </div>
           </c:forEach>
        </dd>
    </dl>
    <form id="matchForm" action="<%=request.getContextPath()%>/ci/marketingStrategyAction!customProductView.ai2do" method="post">
    <div class="analyse_right_btns">
    	<input type="button" class="tag_btn" onclick="nextSaveRelation()" id="saveMatchRelation"  value="下一步" />
        <input type="button" class="tag_btn" id="initSaveMatchdialog" value="保存营销策略" />
        <input type="hidden" id="productIds"/>
        <input type="hidden" name="customGroup.returnPage" value="${customGroup.returnPage}"/>
        <input type="hidden" name="customGroup.productsStr" id="productsStr" value="${customGroup.productsStr}"/>
        <input type="hidden" name="customGroup.productsIdsStr" id="productsIdsStr" value="${customGroup.productsIdsStr}"/>
        <input type="hidden" name="customGroup.listTableName" id="listTableName" value="${customGroup.listTableName}"/>
        <input type="hidden" name="customGroup.dataDate" id="dataDate" value="${customGroup.dataDate}"/>
        <input type="hidden" name="customGroup.productDate" id="productDate" value="${customGroup.productDate}"/>
        <input type="hidden" name="customGroup.customGroupId" id="customGroupId" value="${customGroup.customGroupId}"/>
        <input type="hidden" name="customGroup.customGroupName" id="customGroupName" value="${customGroup.customGroupName}"/>
        <input type="hidden" id="customNum" name="customGroup.customNum" value="${customGroup.customNum}" />
        <input type="hidden" name="customGroup.customMatch" value="${customGroup.customMatch}"/>
        <input type="hidden" name="customGroup.precisionMarketing" id="precisionMarketing" value="false"/>
    </div>
    </form>
</div><!--analyse_right end -->
<!--超出提示 -->
				<div class="warnBox" id="fiveWarn">
					<dl>
						<dt><span class="close"></span><span class="clear"></span></dt>
						<dd>最多可选 5 个产品！</dd>
					</dl>
				</div>

<!--标签操作浮动层-->
<div class="tag_operation tag_operation_hided" style="display: none">
	<div class="tag_operation_inner">
        <div class="tag_dropCT">
            <div class="date_slider">
                date slider
            </div>
            <div id="tag_dropable" class="tag_dropable">
                 
            </div><!--#tag_dropable end -->
        </div><!--.tag_dropCT end -->
        <div class="icon_sjts">数据探索</div>
        <div id="parenthesis" class="parenthesis">
            <div class="leftParenthesis"></div>
            <div class="rightParenthesis"></div>
        </div>
        <ul class="controlAll">
            <li><input type="checkbox" />全部(且)</li>
            <li><input type="checkbox" />全部(或)</li>
            <li><input type="checkbox" />全部剔除</li>
        </ul>

        <ul class="btns">
            <li class="customerNum">用户数<p>123,123,456</p></li>
            <li><a href="javascript:void(0)" class="btn_explore">探索</a></li>
            <li><a href="javascript:void(0)" class="btn_save">保存客户群</a></li>
            <li><span class="icon_expand" onclick="expandTagperation(this)">展开</span></li>
        </ul>
        
        <div class="firsttime_tip">
            <div class="inner">
                <div class="close"><a href="javascript:void(0);" onclick="firsttime_tip_remove(this)">×</a></div>
                <p>鼠标单击标签可取反!</p>
            </div>
            <div class="arrow"></div>
        </div>
    </div>
</div><!--tag_operation end -->
</body>
</html>
