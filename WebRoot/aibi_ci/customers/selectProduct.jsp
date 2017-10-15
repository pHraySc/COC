<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设置规则</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/js/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//时间轴
	var month = "${newLabelMonth}";
	if(month == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
			type: "POST",
			success: function(result){
				month = result;
				creatDateline($("#dateline_ct"),month);
			}
		});
	}else{
		creatDateline($("#dateline_ct"),month);
	}
	
	showProductTree("ct");//左侧标签树
	
	$("#saveCustomerBtn").bind("click", function(){
		toSave();
	});
	
	<c:if test='${ciCustomGroupInfo.customGroupId == null || ciCustomGroupInfo.customGroupId == ""}'>
	var title = "创建客户群";
	</c:if>
	<c:if test='${ciCustomGroupInfo.customGroupId != null && ciCustomGroupInfo.customGroupId != ""}'>
	var title = "客户群修改";
	</c:if>
	
	$("#createDialog").dialog({
		autoOpen: false,
		width: 660,
		title: title,
		modal: true,
		resizable:false
	});
	
	$("#successDialog").dialog({
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		resizable:false
	});
});
//保存客户群
function toSave(){
	createForm();
	if(validateSql()){
		openCreate();
	}
}
//保存客户群弹出层
function openCreate(){
	$("#createDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/createCustomer.jsp";
	$("#createFrame").attr("src", ifmUrl).load(function(){resetForm();$("#createDialog").dialog("open");});
	
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#createDialog").dialog("close");
	clearCondition();
}
//打开保存成功弹出层
function openSuccess(customerName, templateName){
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	templateName = encodeURIComponent(encodeURIComponent(templateName));
	$("#successDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
}
//清空数据探索条件
function clearCondition(){
	$("#parenthesis").removeClass("opened");
	$("#inputList").empty();
	$("#tag_dropable").empty();
}
//还原iframe中的表单
function resetForm(){
	var ifm = $("#createFrame").contents();
	//编辑时填充表单
	var customGroupId = $("#customGroupId").val();
	if(customGroupId != null && customGroupId != ""){
		ifm.find("#custom_id").val(customGroupId);
		ifm.find("#custom_name").val($("#customGroupName").val());
		ifm.find("#custom_desc").val($("#customGroupDesc").val());
		//ifm.find("#updateCycle").val($("#updateCycle").val());
		ifm.find("#startDate").val($("#startDate").val());
		ifm.find("#endDate").val($("#endDate").val());
	}
}
//整合条件，拼接表单
function createForm(){
	$("#dataDate").val($(".date_line").attr("_value"));
	
	$("#inputList").empty();
	var labelOptRule = "";
	var html = "";
	$("#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis").each(function(i, element){
		var ele = $(element);
		var calcuElement = "", minVal="", maxVal="", elementType="", labelFlag="", nameStr="";
		if(ele.hasClass("conditionCT")){//处理标签
			calcuElement = ele.attr("element");
			minVal = ele.attr("min");
			maxVal = ele.attr("max");
			elementType = 2;
			labelFlag = ele.attr("flag");
			nameStr = ele.children(".midCT").text();
			if(labelFlag != null && labelFlag != ""){
				if(labelFlag == 0){
					nameStr = "非 " + nameStr + " ";
				}else if(labelFlag == 1){
					nameStr = nameStr + " ";
				}
			}else{
				nameStr = minVal + " <= " + nameStr + " <= " + maxVal + " ";
			}
		}else if(ele.hasClass("chaining")){//处理逻辑运算
			var name = ele.find("a").text();
			name = $.trim(name);
			if(name == "且"){
				calcuElement = "and";
			}else if(name == "或"){
				calcuElement = "or";
			}else if(name == "剔除"){
				calcuElement = "-";
			}
			nameStr += name + " ";
			elementType = 1;
		}else if(ele.hasClass("parenthesis")){//处理括号
			calcuElement = ele.children("input").val();
			elementType = 3;
			nameStr = calcuElement + " ";
		}
		labelOptRule += nameStr;
		html += '<input type="hidden" name="ciLabelRuleList[' + i + '].calcuElement" value="' + calcuElement + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].minVal" value="' + minVal + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].maxVal" value="' + maxVal + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].sortNum" value="' + (i+1) + '"/>' +
			'<input type="hidden" name="ciLabelRuleList[' + i + '].elementType" value="' + elementType + '"/>';
		if(elementType == 2){
			html += '<input type="hidden" name="ciLabelRuleList[' + i + '].labelFlag" value="' + labelFlag + '"/>';
		}
	});
	$("#labelOptRuleShow").val(labelOptRule);
	$("#inputList").append(html);
}
//SQL验证
function validateSql(){
	var flag = true;
	var len = $("#tag_dropable > .conditionCT").size();
	if(len == 0){
		showAlert("请拖拽至少一个标签！","failed");
		return false;
	}
	if($(".waitClose").length>0){
		showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
		return false;
	}
	var actionUrl = $.ctx + "/ci/customersManagerAction!validateSql.ai2do";
	$.ajax({
		url: actionUrl,
	    type: "POST",
	    async: false,//同步
		data: $("#exploreForm").serialize(), 
		success: function(result){
			flag = result.success;
			if(!flag){
				showAlert(result.msg,"failed");
			}
		}
	});
	return flag;
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
				alert(result.msg);
			}
			//$("#content").append(result.labelName);
		}
	});

	initTree();
}

function initTree() {
	//恢复原有样式
	var icon = $(".curSelectedNode").find(".button");
	if(icon.css("background")){
		var bg = icon.css("background").replace("_on","");
		icon.css("background", bg);
	}
	
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
	zTree_Menu.selectNode(curMenu);
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
		if(!tar.hasClass("treenode") || tar.hasClass("ui-draggable") || tar.hasClass("level0")) return;
		tar.draggable({ revert: "invalid", helper: "clone" });
	})
	
	//释放标签
	dropTag();
	
	//点击标签取反
	$("#tag_dropable").click(function(e){
		e=e||window.event;
		var tar=$(e.target||e.srcElement);
		if(!tar.hasClass("midCT")) return;
		var _p=tar.parent();
		if(_p.hasClass("conditionCT_long")) return;
		if(_p.hasClass("conditionCT_active")){
			_p.removeClass("conditionCT_active");
			_p.attr("flag", 1);
		}else if(tar.parent().hasClass("conditionCT")){
			_p.addClass("conditionCT_active");
			_p.attr("flag", 0);
		}
	});
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
				alert(result.msg);
			}
			//$("#content").append(result.labelName);
		}
	});
	
	initTree();
}

//释放正在拖动的标签
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
	
	//生成运算符
	var _chaining='<div class="chaining">';
	_chaining+='<div class="left_bg"></div>';
	_chaining+='<div class="midCT">';
	_chaining+='<a class="icon_rightArr" href="javascript:void(0)" onclick="switchConnector(this,event)">且</a>';
	_chaining+='</div>';
	_chaining+='<div class="right_bg"></div>';
	_chaining+='</div>';
	var _txt=dragtag.find("span").eq(1).html();
	
	//标签内容
	var html='<div class="conditionCT" element="' + nodeObj.id + '" ';
	html+='min="0" max="1" ';
	html+='type="4" flag="1">';
	html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this)"></a></div>';
	html+='<div class="midCT">'+_txt+'</div>';
	html+='<div class="right_bg"></div>';
	html+='</div>';
	if(ct.find(".conditionCT").length==0){
		ct.append(html);
	}else{
		ct.append(_chaining+html);
	}
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
	return true;
}

//系统标签和我的标签切换显示
function showProductTree(who) {
	$('#searchProductName').val('');
	var actionUrl = $.ctx + "/ci/productManagerAction!findProductTree.ai2do";
	$('#labelType').val(who);
	if('ct' == who) {
		$('#ct_tree').addClass('on');
		$('#my_tree').removeClass('on');
		actionUrl = $.ctx + "/ci/productManagerAction!findProductTree.ai2do";
	} else if('my' == who) {
		$('#my_tree').addClass('on');
		$('#ct_tree').removeClass('on');
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTree.ai2do";
	} 
	initLabelTree(actionUrl);
}

//通过名称模糊查询标签树数据
function searchProductByName() {
	
	var productName = $('#searchProductName').val();
	productName = $.trim(productName);
	if(productName == null || productName == ""){
		return;
	}
	var labelType = $('#labelType').val();
	var actionUrl = "";
	if('ct' == labelType) {
		actionUrl = $.ctx + "/ci/productManagerAction!findProductTreeByName.ai2do";
	} else if('my' == labelType) {
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeByName.ai2do";
	}
	initProductTreeByName(actionUrl,productName);
}

</script>
<style type="text/css">

</style>
</head>

<body class="body_bg">
<div class="top_menu analyse_top_menu">
    <ul>
        <li class="top_menu_on"><a href="#" class="no2"><span></span><p>设置规则</p></a></li>
        <div class="clear"></div>
    </ul>
</div>
<input id="labelType" type="hidden" value="ct"/>
<div class="analyse_left">
	<ul class="analyse_tab">
    	<li class="on" id="ct_tree" onclick="showProductTree('ct');">产品库</li>
        <li class="noborder" id="my_tree" onclick="showProductTree('my');">我的标签库</li>
    </ul>
    
    <div class="analyse_tab_ct">
        <div class="analyse_date">
            <table class="commonTable">
                <tr>
                    <td>
                        <div class="analyse_search">
                            <input type="text" id="searchProductName"/>
                            <span class="icon_search" onclick="searchProductByName();"></span>
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
    	<dt><span>时间轴</span></dt>
        <dd>
        	<table class="commonTable mainTag_show">
            	<tr>
                	<td id="dateline_ct">
                    </td>
                </tr>
            </table>
        </dd>
    </dl>
    
    <dl class="analyse_right_dl">
    	<dt>
            <div id="parenthesis" class="parenthesis">
                <div class="leftParenthesis"></div>
                <div class="rightParenthesis"></div>
            </div>
            <span>标签及规则设置</span>
        </dt>
        <dd id="tag_dropable" class="tag_analyse_ct tag_dropable">
            <c:if test='${ciCustomGroupInfo.customGroupId != null && ciCustomGroupInfo.customGroupId != ""}'>
            <div class="drag_tip" style="display:none;"></div>
				<c:forEach var="ciLabelRule" items="${ciLabelRuleList}" varStatus="status">
				<c:choose>
					<c:when test='${ciLabelRule.elementType == 2}'>
						<div class="conditionCT<c:if test="${ciLabelRule.labelFlag eq 0}"> conditionCT_active</c:if>" 
							flag="${ciLabelRule.labelFlag}" type="${ciLabelRule.elementType}" max="${ciLabelRule.maxVal}" 
							min="${ciLabelRule.minVal}" element="${ciLabelRule.calcuElement}">
							
							<div class="left_bg"><a onclick="deleteThis(this)" href="javascript:void(0)"/></a></div>
							<div class="midCT">${ciLabelRule.labelName}</div>
							<div class="right_bg"/></div>
						</div>
					</c:when>
					
					<c:when test='${ciLabelRule.elementType == 1}'>
						<div class="chaining">
							<div class="left_bg"></div>
							<div class="midCT">
								<a class="icon_rightArr" onclick="switchConnector(this,event)" href="javascript:void(0)">
									<c:choose>
										<c:when test='${ciLabelRule.calcuElement == "and"}'>
											且
										</c:when>
										<c:when test='${ciLabelRule.calcuElement == "or"}'>
											或
										</c:when>
										<c:when test='${ciLabelRule.calcuElement == "-"}'>
											剔除
										</c:when>
									</c:choose>
								</a>
							</div>
							<div class="right_bg"></div>
						</div>
					</c:when>
					
					<c:when test='${ciLabelRule.elementType == 3}'>
						<c:if test='${ciLabelRule.calcuElement == "("}'>
							<span class="parenthesis leftParenthesis" selfid="curly_brace_block">
								<input type="hidden" value="("/>
							</span>
						</c:if>
						<c:if test='${ciLabelRule.calcuElement == ")"}'>
							<span class="parenthesis rightParenthesis" selfid="curly_brace_block">
								<input type="hidden" value=")"/>
								<span class="delCondition" onclick="deletePar(this,event)"></span>
							</span>
						</c:if>
					</c:when>
				</c:choose>
				</c:forEach>
			</c:if>
			<c:if test='${ciCustomGroupInfo.customGroupId == null || ciCustomGroupInfo.customGroupId == ""}'>
			<div class="drag_tip"></div>
			</c:if>
        </dd>
    </dl>
    
    <div class="analyse_right_btns">
    	<input id="saveCustomerBtn" type="button" class="tag_btn" value="保存" />
        <input type="button" class="tag_btn tag_btn_disable" value="取消" onclick="window.close();"/>
    </div>
</div><!--analyse_right end -->

<!-- 页面传值 -->
<input id="customGroupId" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}" />
<input id="customGroupName" type="hidden" name="ciCustomGroupInfo.customGroupName" value="${ciCustomGroupInfo.customGroupName}" />
<input id="customGroupDesc" type="hidden" name="ciCustomGroupInfo.customGroupDesc" value="${ciCustomGroupInfo.customGroupDesc}" />
<input id="updateCycle" type="hidden" name="ciCustomGroupInfo.updateCycle" value="${ciCustomGroupInfo.updateCycle}" />
<input id="startDate" type="hidden" name="ciCustomGroupInfo.startDate" value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>' />
<input id="endDate" type="hidden" name="ciCustomGroupInfo.endDate" value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>' />

<!-- form -->
<form id="exploreForm">
	<input id="dataDate" type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>
	<input id="createTypeId" type="hidden" name="ciCustomGroupInfo.createTypeId" value="1" />
	<c:if test='${ciCustomGroupInfo.createUserId != null && ciCustomGroupInfo.createUserId != ""}'>
		<input id="createUserId" type="hidden" name="ciCustomGroupInfo.createUserId" value="${ciCustomGroupInfo.createUserId}" />
	</c:if>
	<c:if test='${ciCustomGroupInfo.createTime != null && ciCustomGroupInfo.createTime != ""}'>
		<input id="createTime" type="hidden" name="ciCustomGroupInfo.createTime" value='<fmt:formatDate value="${ciCustomGroupInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
	</c:if>
	<input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value=""/>
	<div id="inputList" style="display:none;"></div>
</form>
<div id="createDialog">
	<iframe id="createFrame" name="createFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:415px"></iframe>
</div>
<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
</body>
</html>
