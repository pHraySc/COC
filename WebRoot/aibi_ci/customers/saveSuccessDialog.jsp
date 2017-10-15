<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>成功提示</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/dialog_overload.css" rel="stylesheet" />
<script type="text/javascript">
var customerId = decodeURIComponent('${param["customerId"]}');
var isHasList = '${param["isHasList"]}';
if(customerId == null || customerId==""){
	customerId = "${ciCustomGroupInfo.customGroupId}";
}
var userId = '${param["userId"]}';
$(document).ready(function(){
	if (getCookieByName("${version_flag}") == "false") {
		$(".J_search_custom_showname").html("客户群集市");
	}else{
		$(".J_search_custom_showname").html("用户群集市");
	}
	
	var templateName = decodeURIComponent('${param["templateName"]}');
	var customerName = decodeURIComponent('${param["customerName"]}');
	
	if(null == customerName || customerName == "") {
		customerName = '${customerName}';
	}
	if(templateName != null && templateName != ""&& customerName != "" && customerName != null ){
		$("#templateName").empty();
		$("#templateName").append(templateName);
		$("#customerName").append(customerName);
		$("#systemTip").show();
	}
	if(templateName != null && templateName != ""&& (customerName == "" || customerName == null )){
		$("#templateName_only").empty();
		$("#templateName_only").append(templateName);
		$("#successTip").show();
	}else{
		$("#notice").hide();
	}
	if(customerName != null && customerName != ""&&(templateName == null || templateName == "")){
		$("#customerName_only").empty();
		$("#customerName_only").append(customerName);
		$("#clientSuccessTip").show();
	}
	
	if(isHasList != 1) {
		$('.pushSetTipBox').hide();
	}
	//设置弹出窗口内的滚动条
	/*setTimeout(function(){
		var _autoheight = $(document).height();
		var contentFrame = $("iframe:visible",window.parent.document);
		var frameHeiht = parseInt(contentFrame.css("height"));
		if(_autoheight > frameHeiht){
			$(".dialog_table").height(frameHeiht+"px");
			$(".dialog_table").css("overflow","scroll");
		}
	},500);*/
});
//跳转到模板管理页面
function toTemplateList(){
	var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=3";
	//var url = $.ctx + "/aibi_ci/template/templateList.jsp";
	closeWindowAndOpenUrl(url);
}
//跳转到客户群管理页面
function toCustomList(){
	var url = "";
	if(getCookieByName("${version_flag}") == "true"){
		url = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?indexLinkType=2";
	}else{
		url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=2&isDefaultLoad=1";
	}
	closeWindowAndOpenUrl(url);
}
//如果父页面存则在关闭当前页面，在父页面跳转到新的url，否则在本页跳转到新的url
function closeWindowAndOpenUrl(url){
	top.location = url;
	/*var pw = parent.window.opener;
	var ppw = parent.parent.window.opener;
    if(pw!=null && typeof(pw) != "undefined" && !pw.closed){
		pw.top.location = url;
		parent.self.close();
	}else if(ppw!=null && typeof(ppw) != "undefined" && !ppw.closed){
		ppw.top.location = url;
		parent.parent.self.close();
	}else{
		top.location = url;
	}*/
}

//关闭当前提示框
function closeDialog() {
	parent.$("#successDialog").dialog("close");//计算中心保存关闭
	parent.$("#dialog-form").dialog("close");//导入 关闭
	if(parent.$("#isPushCustom").val() != "true"){
		openCalculateCenter();
	}
}
//初始化推送页面
function pushCustomGroupInit() {
	parent.$("#isPushCustom").val("true");
	parent.pushCustomGroup(customerId);
}
</script>
<style>body,html{overflow:hidden;}</style>
</head>
<body>


<!-- 保存成功 -->
<div id="systemTip"  class="hidden"  >
    <div class="systemAlterTipBox" >
        数据处理完毕后，会在消息中心中提示您！
    </div>
    <div class="systemTipNextStepBox">
         <div class="fleft systemTipNextStepLeft">
		      <div class="tipNextStepImg"> </div>
			  <h4  id="templateName"></h4>
			  <p>
			   	 保存成功，你可以通过<a href="javascript:toTemplateList();" class="active">客户群模板</a>
			    	 查看本次操作，并进行相应的模板管理.
			  </p>
		   </div>
		   <div class="fright systemTipNextStepRight">
		      <div class="tipNextStepImg2"> </div>
			  <h4 id="customerName"> </h4>
			  <p>保存成功，你可以通过<a href="javascript:toCustomList();" class="active">客户群集市</a>
			     	查看本次操作，并进行相应的客户群管理.
			  </p>
		   </div>
    </div>
    <div class="systemTipDescBox">
        <p> 保存成功!</p>
    </div>
</div>	
	
<!-- 客户模板 -->	
<div id="successTip" class="hidden">
     <div class="systemTipTextBox"  >
        	保存成功!
    </div>
    <div class="successTipBox">
        <div class="fleft successTipLeftBox"></div>
        <div class="fright successTipRightBox">
                 <h4  id="templateName_only"></h4>
				  <p>
					保存成功，你可以通过<a href="javascript:toTemplateList();" class="active">客户群模板</a>
					 查看本次操作，并进行相应的模板管理.
				  </p>
        </div>
    </div>
</div>
<!-- 客户群保存成功 -->	
<div id="clientSuccessTip" class="hidden" style="overflow:hidden;">
    <div class="systemAlterTipBox">
        数据处理完毕后，会在消息中心中提示您！
    </div>
    <div class="successTipBox">
        <div class="fleft clientSuccessTipLeftBox"></div>
        <div class="fright successTipRightBox">
                 <h4  id="customerName_only"></h4>
				  <p>
					保存成功，你可以通过<a class="J_search_custom_showname" href="javascript:toCustomList();" class="active">客户群集市</a>
					 查看本次操作，并进行相应的客户群管理.
				  </p>
				  <p class="pushSetTipBox">
		             是否进行推送设置 <a href="javascript:pushCustomGroupInit();" class="pushSetTip">推送设置</a>
		             <a href="javascript:closeDialog();" class="pushSetTip">跳过设置</a>
		          </p>
        </div>
    </div>
    <div class="systemTipDescBox">
        <p>保存成功! </p>
    </div>
</div>
</body>
</html>

<!--  
 <div id="successTip">
		<div class="systemTipTextBox">
		    保存成功!
		</div>
		<div class="successTipBox"  id="template"  style="display:none;">
		       <div class="fleft successTipLeftBox"></div>
			   <div class="fright  successTipRightBox">
				  <h4  id="templateName"></h4>
				  <p>
					保存成功，你可以通过<a href="javascript:toTemplateList();">模板管理</a>
					 查看本次操作，并进行相应的模板管理.
				  </p>
			   </div>
		</div>
		<div class="successTipBox"  id="customer"  style="display:none;">
		       <div class="fleft successTipLeftBox"></div>
			   <div class="fright  successTipRightBox">
				  <h4  id="customerName"></h4>
				  <p>
					保存成功，你可以通过<a href="javascript:toCustomList();">客户群管理</a>
					 查看本次操作，并进行相应的客户群管理.
				  </p>
			   </div>
		</div>
		<div class="systemTipNextStepBox" id="customerAndTemplate" style="display:none;">
		   <div class="fleft systemTipNextStepLeft">
		      <div class="tipNextStepImg"> </div>
			  <h4 id="customerName"> </h4>
			  <p>保存成功，你可以通过<a href="javascript:toCustomList();">客户群管理</a>
			     	查看本次操作，并进行相应的客户群管理.
			  </p>
		   </div>
		   <div class="fright systemTipNextStepRight">
		      <div class="tipNextStepImg2"> </div>
			  <h4  id="templateName"></h4>
			  <p>
			   	 保存成功，你可以通过<a href="javascript:toTemplateList();">模版管理</a>
			    	 查看本次操作，并进行相应的模版管理.
			  </p>
		   </div>
		</div>
		<div class="systemTipDescBox" id="notice">
		    <p> 数据处理完毕后，会在<strong>系统通知中心</strong>中提示您！感谢您的使用</p>
		</div>
	</div>
-->
