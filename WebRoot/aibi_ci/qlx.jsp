<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>群乐享</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/scrollpagination.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dragdropTag.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<script type="text/javascript">
$(function() {
	$(".searchSubmit").bind("click", function(){
		loadCustom('',$("#classId").val(),$("#keyword").val());
		$(".query_condition li").removeClass("on");
	});
	function formatItem(row) {
		return " <p>"+row[0] +" </p>";
	}
	function formatResult(row) {
		return row[0].replace(/(<.+?>)/gi, '');
	}
	function selectItem(li) {
		//makeSearchUrl(document.formkeyword);
	}
	$("#dataStatusId").multiselect({
		multiple: false,
		height: "auto",
		header: false,
		selectedList: 1
	});
	$("#keyword").autocomplete({
		 source: function( request, response ) {
			$.ajax({
			 url: "<%=request.getContextPath() %>/ci/ciIndexAction!indexQueryCustomGroupName.ai2do",
			 dataType: "jsonp",
			 type: "POST",
			 data: {
				keyWord: request.term,
				classId:$("#classId").val()
			 },
			 success: function( data ) {
				response( $.map( data.geonames, function( item ) {
				 return {
					label: item.customGroupName,
					value: item.customGroupName
				 }
				}));
			 }
			});
		 },
		 minLength: 1,
		 autoFocus:true,
		 select: function( event, ui ) {
		 },
		 open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
		 },
		 close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		 }
		});
	checkCanExplor();
	
	$("#successDialog").dialog({
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	$("#confirmDialog").dialog({
		autoOpen: false,
		width: 500,
		height:185,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	$("#createTemplateDialog").dialog({
		autoOpen: false,
		width: 660,
		title: "保存模板",
		modal: true,
		resizable:false
	});
	
	//客户群趋势明细信息查看窗口
	$( "#dialog_look" ).dialog({
	  	autoOpen: false,
	  	width: 980,
		resizable:false,
	  	title:"查看",
	  	modal: true,
	 	close:function(){
			$("#dialog_detail").attr("src", "");
	  	}
	});

	$("#sysMatch").click(function(e){
		sysMatch();
	});

});
//客户群趋势分析明细页面加载方法
function showCustomerTrend(customGroupId) {
	    //alert(customGroupId);
	    var ifmUrl = "${ctx}/ci/customersTrendAnalysisAction!initCustomersTrendDetail.ai2do?customGroupId="+customGroupId;
	    $("#dialog_detail").attr("src", ifmUrl);
	    $("#dialog_look" ).dialog("open" );
}

function regenCustomer(customGroupId,carateType){
	if(carateType == 7){//文件创建的不支持
		return;
	}
	var url = "${ctx}/ci/customersManagerAction!reGenerateCustomer.ai2do?ciCustomGroupInfo.customGroupId="+customGroupId;;
	$.ajax({
		url: url,
		success: function(result){
			if(result.success){
				showAlert("已提交，请刷新查看","success");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				showAlert(result.msg,"failed");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}
		}
	});
}
function changeNum(month){
	$("input[name='ciCustomGroupInfo.dataDate']").val(month);
	$("#tag_dropable").find(".midCT").each(function(){
		var data = $(this).attr("data");
		if(data && data.length > 0){
			var jsondata =$.parseJSON(decodeURIComponent(data));
			if(jsondata.updateCycle == 1){//非周期
			}else if(jsondata.updateCycle == 2){//月周期
				var hasList  = false;
				for(var i=0;i< jsondata.successList.length;i++){
					var listinfo  = jsondata.successList[i];
					if(listinfo.dataDate == month){
						hasList = true;
						break;
					}
				}
				if(!hasList){
					$(this).parent().addClass("outOfDate").removeAttr("title").attr("title","周期性客户群在当前日期没有数据,系统将使用有数据的最新日期");
				}else{
					$(this).parent().removeClass("outOfDate").removeAttr("title");
				}
			}
		}
	});
}
//释放正在拖动的标签
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_dropable").droppable({
		accept: ".tag_List_ul li,.canDrag",
		greedy:true,
		drop: function( event, ui ) {
			var onDragTag=ui.draggable;
			tagOnDrop(onDragTag,$(this));
		}
	});
}
//关闭弹出层
function closeDialog(dialogId){
	$(dialogId).dialog("close");
	$(dialogId+"2").dialog("close");
}
//打开保存成功弹出层
function openSuccess(customerName, templateName){
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	templateName = encodeURIComponent(encodeURIComponent(templateName));
	$("#successDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successFrame").attr("src", ifmUrl).load(function(){});
	$("#successDialog").dialog("open");
}
function tagOnDrop(dragtag,ct,dataDate,jsonStr){
	if($(".tag_operation_overlay:visible").size() > 0){
		return;
	}
	if($(".conditionCT").length>=$._maxLabelNum){
		showAlert("最多拖动" + $._maxLabelNum + "个客户群！","failed");
		return false;
	}
	var jsondata;
	if(jsonStr){
		jsondata =$.parseJSON(decodeURIComponent(jsonStr));
	}else{
		jsondata =$.parseJSON(decodeURIComponent(dragtag.attr("data")));
	}
	if(jsondata.successList.length ==0 ){
		showAlert("该客户群没有可用的清单","failed");
		return;
	}
	var calcuElement ;
	var dataStr = "数据日期:";
	if(dataDate){
		for(var i=0;i< jsondata.successList.length;i++){
			var listinfo  = jsondata.successList[i];
			if(listinfo.dataDate == dataDate){
				calcuElement = listinfo.listTableName;
				dataStr += dataDate;
				break;
			}
		}
	}else{
		dataStr += jsondata.dataDate;
		calcuElement = jsondata.customGroupId;
	}
	if(jsondata.updateCycle == 1){
		dataStr = "";
	}
	$("input[name='ciCustomGroupInfo.dataDate']").val(jsondata.dataDate);
	if(!calcuElement){
		showAlert("该客户群没有可用的清单"+dataDate,"failed");
		return;
	}
	
	var _chaining='<div class="chaining">';
	_chaining+='<div class="left_bg"></div>';
	_chaining+='<div class="midCT" >';
	_chaining+='<input type=hidden name="calcuElement" value="and" />';
	//kongly
//	_chaining+='<input type=hidden name="elementType" value="0" />';
	_chaining+='<input type=hidden name="elementType" value="1" />';
	_chaining+='<span onmousedown="switchConnector_turn(this,event)">且</span><a onclick="switchConnector(this,event)" href="javascript:void(0)" class="icon_rightArr">&nbsp;</a>';
	_chaining+='</div>';
	_chaining+='<div class="right_bg"></div>';
	_chaining+='</div>';
	var html='<div class="conditionCT">';
	html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this)"></a></div>';
	html+='<div class="midCT" data="'+dragtag.attr("data")+'" title="'+dataStr+'">'+jsondata.customGroupName;
	html+='<input type=hidden name="calcuElement" value="'+calcuElement+'" />';
	//kongly
//	html+='<input type=hidden name="elementType" value="1" />';
	html+='<input type=hidden name="elementType" value="5" />';
	html+='</div><div class="right_bg"></div>';
	html+='</div>';
	if(ct.find(".conditionCT").length==0){
		ct.append(html);
	}else{
		ct.append(_chaining+html);
	}
	checkCanExplor();
}
//删除条件 同时删除关联的连接符和括号
function deleteThis(obj){
	//删除匹配的括号【与条件直接关联的括号】
	deleteCurlyBraces(obj);
	//删除关联的连接符
	deleteConnectFlags(obj);
	//删除自身
	$(obj).parent().parent().remove();
	checkCanExplor();
}
function checkCanExplor(){
	if($("#tag_dropable").find(".midCT").size()>0){
		$(".btn_explore").removeClass("btn_explore_dis").unbind("click").bind("click",function(){
			queryCount(this);
		}).removeAttr("title");
		$(".btn_save").removeClass("btn_save_dis").unbind("click").bind("click",function(){
			if($(".waitClose").length>0){
				showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
				return;
			}
			$( "#dialog-form" ).dialog( "open" );
		}).removeAttr("title");
	}else{
		$(".customerNum").hide().find("p").empty();
		$(".btn_explore").addClass("btn_explore_dis").unbind("click").removeAttr("title").attr("title","请先添加客户群！");
		$(".btn_save").addClass("btn_save_dis").unbind("click").removeAttr("title").attr("title","请先添加客户群！");
	}
}
function addToPanel(obj,dataDate,jsonStr){
	tagOnDrop($(obj),$("#tag_dropable"),dataDate,jsonStr);
}
function queryCount(obj){
	var data = getParams();
	data += "&ciCustomGroupInfo.dataDate=" + $("input[name='ciCustomGroupInfo.dataDate']").val();
	showCount(data);
}
function controlAll(obj,text){
	if(obj.checked){
		$(obj).parent().parent().find("input:checked").not(obj).prop("checked",false);
		$("#tag_dropable").find(".chaining").find("span").text(text);
	}
}
function getParams(){
	var data = "";
	var sortNum = 0; 
	var dataCN = "";
	$("#tag_dropable").find(".chaining").find("span").each(function(){
		if($(this).text() == "且"){
			$(this).parent().find('input[name="calcuElement"]').val("and");
		}
		if($(this).text() == "或"){
			$(this).parent().find('input[name="calcuElement"]').val("or");
		}
		if($(this).text() == "剔除"){
			$(this).parent().find('input[name="calcuElement"]').val("-");
		}
	});
	$("#tag_dropable").find(".parenthesis,.midCT").each(function(){
		var calcuElement ;
		var elementType;
		if ($(this).hasClass("midCT")) {
			dataCN += $.trim($(this).text())+" ";
		}
		$(this).find("input:hidden").each(function(){
			if(this.name=='calcuElement'){
				calcuElement = this.value;
			}
			if(this.name=='elementType'){
				elementType = this.value;
			}
			if(this.value =='(' || this.value==')'){
				calcuElement = this.value;
				//kongly
//				elementType = 0;
				elementType = 3;
				dataCN += this.value+" ";
			}
		});
		//kongly
//		data += "&ciCustomSourceRelList["+sortNum+"].calcuElement="+calcuElement;
//		data += "&ciCustomSourceRelList["+sortNum+"].elementType="+elementType;
//		data += "&ciCustomSourceRelList["+sortNum+"].sortNum="+sortNum;
		
		data += "&ciLabelRuleList["+sortNum+"].calcuElement="+calcuElement;
		data += "&ciLabelRuleList["+sortNum+"].elementType="+elementType;
		data += "&ciLabelRuleList["+sortNum+"].sortNum="+sortNum;
		sortNum++;
	});
	$("input[name='ciCustomGroupInfo.customOptRuleShow']").val(dataCN);
	return data;
}
var showCount = function(data){
	if(data.length==0){
		return;
	}
	if($(".waitClose").length>0){
		showAlert("左右括号不匹配，请确认括号是否正确使用！","failed");
		return;
	}
	data += "&customerCalcFromPage=1";
	$(".btn_explore").addClass("btn_explore_dis");
	$(".customerNum").show().empty().append("请稍候…");
	var actionUrl = "<%=request.getContextPath() %>/ci/customersManagerAction!countOfCustomerCalc.ai2do";
	show_overlay();
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: data,
		success: function(result){
			if(result.success){
				$(".customerNum").show().empty().append("用户数<p>"+$.formatNumber(result.count, 3, ',')+"</p>");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				showAlert(result.msg,"failed");
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}
			hide_overlay();
			$(".btn_explore").removeClass("btn_explore_dis");
		}
	});
}
//点击探索按钮时候显示遮罩
function show_overlay(){
	$("body > .tag_operation .tag_operation_overlay").show();
	$("#tag_dropable").droppable("disable");
}
function hide_overlay(){
	$("body > .tag_operation .tag_operation_overlay").hide();
	$("#tag_dropable").droppable("enable");
}

//确认清空
function confirmClearAll(){
	var innerHtml = $("#tag_dropable").html();
	if(innerHtml == null || innerHtml ==""){
		return;
	}
	openConfirm("确定要清空吗？", "");
}
//打开确认弹出层
function openConfirm(confirmMsg, id){
	confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
	$("#confirmDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
	$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
//关闭确认弹出层
function closeConfirmDialog(dialogId, isSome, id){
	$(dialogId).dialog("close");
	if(isSome){
		clearCondition();	
	}
}

//客户群提取模板，打开编辑对话框
function createTemplate(customId,customName,customSceneId){
	customName = encodeURIComponent(encodeURIComponent(customName));
	$("#createTemplateDialog").dialog({
		close: function( event, ui ) {
			$("#createTemplateFrame").attr("src", "");
		}
	});
	$("#createTemplateDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/createTemplate.jsp?customId="+customId+"&customName="+customName+"&sceneId="+customSceneId;
	$("#createTemplateFrame").attr("src", ifmUrl).load(function(){//resetForm();
	});
	$("#createTemplateDialog").dialog("open");
	
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//关闭创建客户群弹出层
function closeCreate(){
	$("#createTemplateDialog").dialog("close");
}
</script>
<style type="text/css">
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
</head>
<body class="body_bg">
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp?index=1" ></jsp:include>
<div class="mainCT">
	<jsp:include page="/aibi_ci/index_top.jsp?index=1" ></jsp:include>	
	<div id="contentLoad">
		<form id="multiSearchForm" >
			<input id="createTypeId" name="ciCustomGroupInfo.createTypeId" type="hidden"/>
			<input id="dateType" name="ciCustomGroupInfo.dateType" type="hidden"/>
			<input id="customGroupName" name="ciCustomGroupInfo.customGroupName" type="hidden"/>
		</form>
		<div class="customerBase_querylist">
			<dl class="query_condition">
				<dt><span class="icon_slideLeft"></span>条件</dt>
				<dd>
					<ul class="condition_list">
						<li><a href="#" onclick="loadCustom('',null)" >全部时间</a></li>
						<li><a href="#" onclick="loadCustom(1,null,null)">一天内</a></li>
						<li><a href="#" onclick="loadCustom(2,null,null)">一个月内</a></li>
						<li onclick="loadCustom(3,null,null)"><a href="#">三个月内</a></li>
					</ul>
					<br><br>
					<%-- <ul class="condition_list" id="item">
						<li><a href="#" onclick="loadCustom(null,'')" >全部客户群属性</a></li>
						 <c:forEach items="${createTypeList}" var="createType">
                           <li><a href="#" onclick="loadCustom(null,'${createType.createTypeId}')">${createType.createTypeName}</a></li>
                        </c:forEach>
					</ul> --%>
					<br><br><br><br><br><br><br><br>
				</dd>
			</dl><!--query_condition end -->
			<div class="customerBase" id="customerList"></div><!--customerBase end -->
			<div class="loading" id="loading">正在加载，请稍候 ...</div>
			<div class="loading" id="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
			<div id="noResults" style="display:none;">没有查询到 ...</div>
		</div><!--customerBase_querylist end -->
	</div><!--contentLoad end -->
	<div id="dialog-form" title="保存客户群" style="display:none;"><!-- dialog-form -->
		 <form id="saveForm">
				<input type="hidden" name="ciCustomGroupInfo.customOptRuleShow" id="ciCustomGroupInfo.customOptRuleShow" value=""/>
				<!-- //TODO -->
				<input type="hidden" name="ciCustomGroupInfo.dataDate" value=""/>
				<!-- //TODO -->
				<input type="hidden" name="ciCustomGroupInfo.productAutoMacthFlag" value="0"/>
				<div class="dialog_table">
					<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
						<tr>
							<th width="110px"><span>*</span>客户群名称：</th>
							<td>
								<div class="new_tag_selectBox">
									<input name="ciCustomGroupInfo.customGroupName" id="ciCustomGroupInfo.customGroupName" type="text"  
											maxlength="35" class="new_tag_input" style="width:302px;*width:306px;cursor:text" value="请输入客户群名称"/>
									<input style="display:none" /><!-- 防止回车提交 -->
								</div>
								<!-- 提示 -->
								<div class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
							</td>
						</tr>
						<tr>
							<th>客户群描述：</th>
							<td>
								<div>
									<textarea style="overflow:hidden;width:400px" id="ciCustomGroupInfo.customGroupDesc" onblur="shortThis(this);" onkeyup="shortThis(this);" name="ciCustomGroupInfo.customGroupDesc" class="new_tag_textarea" cols="30"></textarea>
								</div>
								<!-- 提示 -->
							</td>
						</tr>
					</table>
				</div>
				<!-- 按钮区 -->
				<div class="dialog_btn_div" style="position:relative;">
				    <%
                      if("true".equals(marketingMenu)){
                    %>
					<input type="checkbox" id="isAutoMatch" class="valign_middle"/> 系统自动匹配产品&nbsp;&nbsp;
					<%} %>
					<input id="saveBtn" name="" type="button" value="保存" class="tag_btn"/>
				</div>
		 </form>
		</div><!-- dialog-form end-->
		
		<div id="dialog-match" title="系统自动匹配提示" style="display:none;"><!-- "dialog-match" -->
<div class="dialog_table">
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="app_td" colspan="2" style="padding-top:0px;padding-bottom:20px;">
				<div id="sysMatchTitle" class="no no_nobg">您未进行系统自动匹配！</div>
				<div id="failSysMatchTitle" class="no" style="display: none">系统自动匹配失败！</div>
			</td>
		</tr>
		<tr class="even">
			<td class="save_td save_text_td" style="text-align:left">
				<li>可选择”跳过“，直接进行手工匹配！</li>
				<li>可选择”系统匹配”，您的策略匹配中会出现<br/>
				如右图“系统推荐”的TAB页哦！</li>
			</td>
			<td width="283px" class="save_text_td" style="text-align:left">
				<img src="<%=request.getContextPath()%>/aibi_ci/assets/themes/default/images/img4.png" width="263" height="124" />
			</td>
		<tr class="even">
			<td class="save_td" colspan="2">
				<div class="show" >
					系统匹配功能，可能需要您等待几分钟哦！<br/>
					数据处理完毕后，会在<span>系统通知消息</span>中提示您！感谢您的使用！
				</div>
			</td>
		</tr>
		<input type="hidden" id="matchCustomGroupId" />
		<input type="hidden" id="matchListTableName" />
		<input type="hidden" id="matchCustomNum" />
		<input type="hidden" id="matchDataDate" />
	</table>
</div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input name="" type="button" id="sysMatch" value="系统自动匹配" class="tag_btn"/>
	<input name="" type="button" id="customMacth" onclick="customMatch();" value=" 跳 过 " class="tag_btn"/>
</div>		                
		</div><!-- "dialog-match" end-->
		
</div><!--mainCT end -->
</div>

<div class="tag_operation tag_operation_hided">
	<div class="tag_operation_inner">
		<div class="tag_operation_overlay">
        	<div class="loadding"></div>
        </div>
		<div class="tag_dropCT">
			<div id="tag_dropable" class="tag_dropable">
				 
			</div><!--#tag_dropable end -->
		</div><!--.tag_dropCT end -->
		<ul class="btns">
			<li class="customerNum" style="display:;"><p></p></li>
			<li><a href="javascript:void(0)" class="btn_explore">探索</a></li>
			<li><a href="javascript:void(0)" class="btn_save" >保存客户群</a></li>
			<li><span class="icon_expand" onclick="expandTagperation(this)">展开</span></li>
		</ul>
		<div class="icon_sjts"><span style="font-size: 10px">客户群运算</span></div>
		<div id="parenthesis" class="parenthesis">
			<div class="leftParenthesis"></div>
			<div class="rightParenthesis"></div>
		</div>
		<ul class="controlAll">
			<li><input type="checkbox" onclick='controlAll(this,"且")'/>全部(且)</li>
            <li><input type="checkbox" onclick='controlAll(this,"或")'/>全部(或)</li>
            <li><input type="checkbox" onclick='controlAll(this,"剔除")'/>全部(剔除)</li>
		</ul>
		<div class="clearAll"><a href="javascript:void(0)" onclick="confirmClearAll();">清空</a></div>
		
		
	</div>
</div><!--tag_operation end -->

<div id="successDialog" style="display:none;">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<div id="createTemplateDialog">
	<iframe id="createTemplateFrame" name="createTemplateFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:225px"></iframe>
</div>
<!-- 客户群趋势分析查看 -->
<div id="dialog_look">
	<iframe name="dialog_detail" scrolling="no" allowtransparency="true" src="" id="dialog_detail"  framespacing="0" border="0" frameborder="0" style="width:100%;height:375px"></iframe>
</div>
<script>
//查询相关js
$(".bg02").addClass("active");
$("#_index").addClass("menu_on");
search(null,null,null);
function search(dateType,createTypeId,customGroupName){
	//因为当总页数为0时，就不执行下面的.scrollPagination了。所以当前绑定的scroll事件就是旧的，不可以使用，否则当查询结果为空时依然可以滚动并查询到记录。
	//所以需要把之前绑定的事件解除绑定。在labelList.jsp中其实也可以加这句话，不过由于标签地图点击查询时是重新load的list界面，所以不太一样，就不必加这个了。
	$(window).unbind("scroll");
	$("#noResults").hide();
	$("#noMoreResults").hide();
	$("#customerList").empty();
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/customersManagerAction!findCustomTotalNum.ai2do";
	$.post(getTotalUrl, $("#multiSearchForm").serialize(), function(result) {
		if (result.success) {
			var totalPage = result.totalPage;
			var actionUrl = $.ctx + "/ci/customersManagerAction!customersList.ai2do?pager.totalPage=" + totalPage;
			var i = totalPage;
			if(i != 0){
				$("#customerList").scrollPagination({
					"contentPage" : actionUrl, // the url you are fetching the results
					"contentData" : {
					     "ciCustomGroupInfo.dateType" : dateType,
					     "ciCustomGroupInfo.customGroupName" : customGroupName,
					     "ciCustomGroupInfo.createTypeId" : createTypeId
			 	    }, // these are the variables you can pass to the request, for example: children().size() to know which page you are
					"scrollTarget" : $(window),// who gonna scroll? in this example, the full window
					"heightOffset" : 10,
					"currentPage" : 1,
					"totalPage" : totalPage,
					"beforeLoad" : function() { // before load function, you can display a preloader div
						$("#loading").fadeIn();
					},
					"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
						$(elementsLoaded).fadeInWithDelay();
						i--;
						if(i == 0){
							$("#loading").fadeOut("normal", function(){
								if($("#customerList").attr("scrollPagination") == "disabled"){
									$("#noMoreResults").fadeIn();
								}
							});
						}else{
							$("#loading").fadeOut("normal", function(){});
						}
						
						//增加标签分析等按钮的鼠标移入和移出特效效果(由于标签是通过滚动动态添加的，所以可以在此处为新加载的页面添加事件)
						$(".info_panel ul li").unbind("mouseenter").unbind("mouseleave");
						$(".info_panel ul li").on("mouseenter",function(){
							var _a=$(this).find("a");
							_a.css({"position":"absolute","left":"-3000px"});
							var _w=_a.width();
							_a.css({"position":"static","left":"0"});
							_a.width(0).show().animate({width:_w + 2},250);
						}).on("mouseleave",function(){
							var _a=$(this).find("a");
							_a.width(_a.width() - 2).hide();
						})
					}
				});
			}else{
				$("#noResults").show();
				$("#loading").hide();
			}
		} else {
			showAlert(result.msg,"failed");
		}
	});
	// code for fade in element by element
	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity : 1 }, 200);
			delay += 100;
		});
	};
	return false;
}

function loadCustom(dateType,createTypeId,customGroupName){
	if(dateType != null){
		$("#dateType").attr("value",dateType);		
	} else {
		//$("#dateType").attr("value","");
	}
	if(createTypeId != null){
		$("#createTypeId").attr("value",createTypeId);		
	} else {
		//$("#createTypeId").attr("value","");
	}
	if(customGroupName != null){
		$("#customGroupName").attr("value",customGroupName);
	} else {
		//$("#customGroupName").attr("value","");
	}
	//从隐含域取到最新值
	dateType = $("#dateType").val();
	customGroupName = $("#customGroupName").val();
	createTypeId = $("#createTypeId").val();
	search(dateType,createTypeId,customGroupName);
}
</script>

<script type="text/javascript">
//保存表单
$(function(){
	//保存客户群的表单
	$("#dialog-form").dialog({
		autoOpen: false,
		width: 550,
		resizable:false,
		modal: true,
		close: function() {
			//清空表单
			$("#saveForm").find("input[type=text],textarea").each(function(){
				$(this).val("");
				$("div[for='"+this.name+"']").remove();
			});
			$(".tishi").not(".error").show();
			$("#isAutoMatch").prop("checked",false);
		}
	});
	//表格斑马线
	$("#saveBtn").bind("click", function(){
		submitList();
	});
	$("#cancelBtn").bind("click", function(){
		$( "#dialog-form" ).dialog( "close" );
	});
	$.focusblur($("input[name='ciCustomGroupInfo.customGroupName']:last"));
	//系统自动匹配提示
	$("#dialog-match").dialog({
		autoOpen: false,
		width: 550,
		resizable:false,
		modal: true,
		close: function() {
		$( "#sysMatchTitle" ).html("");
		$( "#sysMatchTitle" ).html("您未进行系统自动匹配！");
		$("#sysMatch").removeClass("tag_btn_disable").removeAttr("title").unbind("click").bind("click",function(){
			sysMatch();
		});
		}
	});
	$(".condition_list li").click(function(){
		if($(this).hasClass("on")) return;
		$(this).addClass("on").siblings(".on").removeClass("on");
	})
	$(".query_condition .icon_slideLeft").click(function(){
		var tar=$(this).parents(".query_condition");
		if(!tar.hasClass("query_condition_hided")){
			tar.addClass("query_condition_hided").animate({width:18},200);
			$(".customerBase").animate({"margin-left":28},200);
			$(this).addClass("icon_slideRight");
		}else{
			tar.removeClass("query_condition_hided").animate({width:190},200);
			$(".customerBase").animate({"margin-left":200},200);
			$(this).removeClass("icon_slideRight");
		}
	});
	$("#isAutoMatch").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		if(this.checked){
			$("input[name='ciCustomGroupInfo.productAutoMacthFlag']").val(1);
		}else{
			$("input[name='ciCustomGroupInfo.productAutoMacthFlag']").val(0);
		}
	});
});
function shortThis(obj){
	var oldValue = $(obj).val();
	if(oldValue.length > 170 ){
		oldValue = oldValue.substr(0,170);
		$(obj).val(oldValue);
	}
}
//产品手工匹配
function customMatch(){
	var listTableName = $("#matchListTableName").val();
	var customGroupId = $("#matchCustomGroupId").val();
	var dataDate = $("#matchDataDate").val();
	var customNum = $("#matchCustomNum").val();
	var param = "customGroup.listTableName="+listTableName;
	param = param + "&customGroup.customGroupId="+customGroupId;
	param = param + "&customGroup.customNum="+customNum;
	param = param + "&dataDate="+dataDate;
	param = param + "&fromPageFlag=1";
	param = param + "&tab=match";
	var actionUrl = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(actionUrl, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	$( "#dialog-match" ).dialog( "close" );
}
//产品系统匹配
function sysMatch(){
	var customGroupId = $("#matchCustomGroupId").val();
	var listTableName = $("#matchListTableName").val();
	var data = "customGroup.listTableName="+listTableName;
	data = data +"&customGroup.customGroupId="+customGroupId;
	var actionUrl = $.ctx+"/ci/marketingStrategyAction!sysMatch.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: data,
		success: function(result){
			if(result.success){
				$( "#sysMatchTitle" ).html("");
				$( "#sysMatchTitle" ).html("已经开始进行系统匹配");
				$("#sysMatch").addClass("tag_btn_disable").unbind("click").removeAttr("title").attr("title","已经进行系统匹配！");
			}else{
				$( "#sysMatchTitle" ).hide();
				$( "#failSysMatchTitle" ).show();
			}
		}
	});
}


$.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});
};

function submitList(){
	
	var nameObj = $("input[name='ciCustomGroupInfo.customGroupName']:last");
	if(nameObj.val()=="请输入客户群名称"){
		nameObj.val("");
	}
	if(validator.form()){
//kongly
//		var actionUrl = $.ctx + "/ci/customersManagerAction!saveCustomerByCustomerCalc.ai2do";
		var actionUrl = $.ctx + "/ci/customersManagerAction!saveCustomerByCustomerCalcOnly.ai2do";
		var data = getParams();
		if(data.length == 0 ){
			alert("请先拖拽客户群");
			return;
		}
		data += "&saveCustomersFlag=2&"+$("#saveForm").serialize();
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: data,
			success: function(result){
				if(result.success){
					openSuccess(nameObj.val(),"");
					$( "#dialog-form" ).dialog( "close" );
				}else{
					showAlert(result.msg,"failed");
				}
			}
		});
	}else{
		//validator.focusInvalid();
	}
}


var validator = $("#saveForm").validate({
	rules: {
		"ciCustomGroupInfo.customGroupName": {
			required: true,
			remote:{//验证用户名是否存在
					type:"POST",
					url:$.ctx + "/ci/customersManagerAction!isNameExist.ai2do", //
					data:{
						"ciCustomGroupInfo.customGroupName":function(){return $("#saveForm").find("input[name='ciCustomGroupInfo.customGroupName']").val();}
					}
				}
		}
	}, 
	messages: {
		"ciCustomGroupInfo.customGroupName": {
			required:"请输入客户群名称",
			remote:jQuery.format("名称已经存在")
		}
	},
	errorPlacement:function(error,element) {
		element.parent().parent().append(error);
	},
	success:function(element){
		element.parent().parent().find("div[class='tishi error']").remove();
	},
	errorElement: "div",
	errorClass: "tishi error"
}); 
//清空数据探索条件
function clearCondition(){
	$("#parenthesis").removeClass("opened");
	$("#tag_dropable").empty();
	$(".customerNum").hide().find("p").empty();
	checkCanExplor();
}
//判断客户群是否进行过系统自动匹配
function customSysMatch(customGroupId,listTableName,customNum,dataDate,failedListCount){
	if(failedListCount >= 1){
		showAlert("客户群清单未创建成功","failed");
		return;
	}
	var actionUrl = $.ctx + "/ci/marketingStrategyAction!isCustomProductMatch.ai2do?customGroup.listTableName="+listTableName+"&customGroup.customGroupId="+customGroupId;
	$.ajax({
		type: "POST",
		url: actionUrl,
		success: function(result){
			if(result.success){
				var param = "customGroup.listTableName="+listTableName;
				param = param + "&customGroup.customGroupId="+customGroupId;
				param = param + "&customGroup.customNum="+customNum;
				param = param + "&dataDate="+dataDate;
				param = param + "&fromPageFlag=1";
				param = param + "&tab=match";
				var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
				window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
				$( "#dialog-match" ).dialog( "close" );
			}else{
				if(result.matchIng){
					$( "#sysMatchTitle" ).html("");
					$( "#sysMatchTitle" ).html("已经开始进行系统匹配");
					$("#sysMatch").addClass("tag_btn_disable").unbind("click").removeAttr("title").attr("title","已经进行系统匹配！");
				}
				$("#matchCustomGroupId").val(customGroupId);
				$("#matchListTableName").val(listTableName);
				$("#matchCustomNum").val(customNum);
				$("#matchDataDate").val(dataDate);
				$( "#dialog-match" ).dialog( "open" );
			}
		}
	});
}


//对比分析连接
function openCustomContrast(customGroupId,listTableName,customNum,dataDate,failedListCount){
	if(failedListCount >= 1){
		showAlert("客户群清单未创建成功","failed");
		return;
	}
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//关联分析连接
function openCustomRel(customGroupId,listTableName,customNum,dataDate,failedListCount) {
	if(failedListCount >= 1){
		showAlert("客户群清单未创建成功","failed");
		return;
	}
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=rel";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//标签微分
function openlabelDeff(customGroupId,listTableName,customNum,dataDate){
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;		
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=tag";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//指标微分
function openIndexDeff(customGroupId,listTableName,customNum,dataDate){
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;		
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=sa";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
</script>
</body>
</html>