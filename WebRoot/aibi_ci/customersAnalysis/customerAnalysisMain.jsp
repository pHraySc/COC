<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${customGroup.customGroupName }</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		//保存营销策略提示框
		$("#successDialog").dialog({
			autoOpen: false,
			width: 550,
			title: "系统提示",
			modal: true,
			resizable:false,
			close: function() {
				toProductMatch();
			}
		});

		$.ajaxSetup ({ 
		      cache: false //关闭AJAX相应的缓存 
		  });
		
	});
	
	//客户群关联分析
	function toCustomRel() {
		var customGroupId = $('#customId').val();
		var listTableName = $('#listName').val();
		var dataDate = $('#date').val();
		var param = "ciCustomRelModel.customGroupId="+customGroupId+"&ciCustomRelModel.listTableName="+listTableName+"&ciCustomRelModel.dataDate="+dataDate
			+ "&fromPageFlag=${fromPageFlag }";
		var url = $.ctx+"/ci/customersRelAnalysisAction!customGroupRelAnalysis.ai2do?" + param;
		var frameObj = $('#rel').find("iframe");
		if(frameObj.attr("src")==""){
			frameObj.attr("src",url);
		}
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	} 

	//客户群对比分析
	function toCustomContrast() {
		var customGroupId = $('#customId').val();
		var listTableName = $('#listName').val();
		var dataDate = $('#date').val();
		var param = "customGroupContrast.customGroupId="+customGroupId+"&customGroupContrast.listTableName="+listTableName+"&dataDate="+dataDate
			+ "&fromPageFlag=${fromPageFlag }";
		var url = $.ctx + "/ci/customersCompareAnalysisAction!customGroupConstrast.ai2do?" + param;
		var frameObj = $('#ant').find("iframe");
		if(frameObj.attr("src")==""){
			frameObj.attr("src",url);
		}
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	} 

	//产品营销策略匹配
	function toProductMatch(){
		var customGroupId = $('#customId').val();
		var listTableName = $('#listName').val();
		var customMatch = $('#customMatch').val();
		var param = "customGroup.listTableName="+listTableName;
		param = param + "&customGroup.customGroupId="+customGroupId + "&fromPageFlag=${fromPageFlag }";
		var url = $.ctx + "/ci/marketingStrategyAction!marketingStrategy.ai2do?"+param;
		var frameObj = $('#prod').find("iframe");
		frameObj.attr("src",url);
		frameObj.load();
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	}
	//标签微分
	function toLabelDiff(){
		var url = $.ctx +"/ci/customersManagerAction!customerDifferential.ai2do?ciCustomListInfo.customGroupId=${customGroup.customGroupId}";
		var dataDate = $('#date').val();
		url += "&ciCustomListInfo.dataDate="+dataDate + "&fromPageFlag=${fromPageFlag }";
		var frameObj = $('#tag').find("iframe");
		if(frameObj.attr("src")==""){
			frameObj.attr("src",url);
		}
		frameObj.parent().parent().find("div").hide();
		frameObj.parent().show();
	}
	
	//指标微分
	function toSa(){
		var url = "${ctx}/ci/customersManagerAction!attrDiffer.ai2do";
		var dataDate = $('#date').val();
		var data = "ciCustomListInfo.customGroupId="+encodeURI('${customGroup.customGroupId}')+
			"&ciCustomListInfo.dataDate="+dataDate + "&fromPageFlag=${fromPageFlag }";
		var frameObj = $('#sa').find("iframe");
		if(frameObj.attr("src")!=""){
			frameObj.parent().parent().find("div").hide();
			frameObj.parent().show();
			return;
		}
		$.ajax({
			url: url,
			type:"post",
			data:data,
			success: function(result){
				if(result.success){
					if(frameObj.attr("src")==""){
						frameObj.attr("src",result.msg);
					}
					frameObj.parent().parent().find("div").hide();
					frameObj.parent().show();
					var w=document.documentElement.clientHeight;
					frameObj.height(eval(eval(w)-208-20));
				}else{
					showAlert(result.msg,"failed");
					//$("#console").append("发布：" +result.msg+"<br/>");
				}
			}
		});
	}
	
	function marketingSave(param){
		var url = "${ctx}/ci/marketingStrategyAction!saveMarketingStategy.ai2do";
		$.post(url, param, function(result){
			if(result.success){
				openSuccess();
			}else{
				var msg = result.msg;
				showAlert(cmsg,"failed");
			}
		});
	}

	//打开保存成功弹出层
	function openSuccess(){
		$("#successDialog").dialog("close");
		var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp";
		$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
	}
</script>
</head>
<body>
<div class="newHeader">
	<div class="header_left" style="background:url(${ctx}/aibi_ci/assets/themes/default/images/header_left_<%=teleOperator %>.png) no-repeat;">
        <img src="${ctx }/aibi_ci/assets/themes/default/images/title_<%=coc_province %>.png" /></div>
	<div class="clear"></div>
</div>
<input type="hidden" id="customId" value="${customGroup.customGroupId }" />
<input type="hidden" id="customNum" value="${customGroup.customNum }" />
<input type="hidden" id="listName" value="${customGroup.listTableName }" />
<input type="hidden" id="date" value="${customGroup.dataDate }" />
<input type="hidden" id="customMatch" value="${customGroup.customMatch }" />
<div class="customerTop">
	<dl>
		<dt>${customGroup.customGroupName }</dt>
		<dt>用户数:<fmt:formatNumber value="${customGroup.customNum}" type="number" pattern="###,###,###"/></dt>
		<dd title="客户群规则">${customGroup.prodOptRuleShow} ${customGroup.labelOptRuleShow} ${customGroup.customOptRuleShow}</dd>
	</dl>
</div>
<div class="submenu">
	<div class="top_menu">
		<ul>
		    <% if("true".equals(customerAnalysisMenu)){ %>
			<li id="toCustomRel"><div class="bgleft">&nbsp;</div><a href="javascript:;" onclick="toCustomRel()" target="Frame" class="no7"><span></span><p>关联分析</p></a><div class="bgright">&nbsp;</div></li>
			<li id="toCustomContrast"><div class="bgleft">&nbsp;</div><a href="javascript:;" onclick="toCustomContrast()" target="Frame"  class="no8"><span></span><p>对比分析</p></a><div class="bgright">&nbsp;</div></li>
			<li id="toLabelDiff"><div class="bgleft">&nbsp;</div><a href="javascript:;" onclick="toLabelDiff()" target="Frame" class="no4"><span></span><p>标签微分</p></a><div class="bgright">&nbsp;</div></li>
			<%}; if("true".equals(indexDifferentialMenu)){ %>
			<li id="toSa"><div class="bgleft">&nbsp;</div><a href="javascript:;" onclick="toSa()" target="Frame"  class="no10"><span></span><p>指标微分</p></a><div class="bgright">&nbsp;</div></li>
			<%
			  }; if("true".equals(marketingMenu)){
            %>
			<li id="toProductMatch"><div class="bgleft" >&nbsp;</div><a href="javascript:;" onclick="toProductMatch()" target="Frame"  class="no11"><span></span><p>营销策略匹配</p></a><div class="bgright">&nbsp;</div></li>
			<%
                };
			%>
			<div class="clear"></div>
		</ul>
	</div>
</div>
<div>
	<div id="rel" style="display:none;">
		<iframe name="Frame" scrolling="no"  src="" id="Frame0"  framespacing="0" border="0" frameborder="0" style="width:100%;height:900px"></iframe>
	</div>
	<div id="ant" style="display:none;">
		<iframe name="Frame" scrolling="no"  src="" id="Frame1"  framespacing="0" border="0" frameborder="0" style="width:100%;height:900px"></iframe>
	</div>
	<div id="tag" style="display:none;">
		<iframe name="Frame" scrolling="no"  src="" id="Frame2"  framespacing="0" border="0" frameborder="0" style="width:100%;height:900px"></iframe>
	</div>
	<div id="prod" style="display:none;">
		<iframe name="Frame" scrolling="no"  src="" id="Frame3"  framespacing="0" border="0" frameborder="0" style="width:100%;height:1200px"></iframe>
	</div>
	<div id="sa" style="display:none;">
		<iframe name="Frame" scrolling="auto"  src="" id="Frame4"  framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
	</div>
</div> 
<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>

<div id="confirm_div" style="display:none;">
    <div class="dialog_table">
        <table width="100%" class="" cellpadding="0" cellspacing="0">
            <tr>
                <td class="app_td" align="center">
                    <div class="ok ok_nobg">确定删除此项？</div>
                </td>
            </tr>
        </table>
    </div>
    <!-- 按钮区 -->
    <div class="dialog_btn_div">
        <input id="confirm_true" name="" type="button" value=" 确 定 " class="tag_btn"/>
        <input id="confirm_false" name="" type="button" value=" 取 消 " class="tag_btn"/>
    </div>
</div>
<script>
	if('${param["tab"]}'=='tag'){
		$("#toLabelDiff").removeClass("top_menu_on").addClass("top_menu_on").find("a").click();
	}else if('${param["tab"]}'=='rel'){
		$("#toCustomRel").removeClass("top_menu_on").addClass("top_menu_on").find("a").click();
	}else  if('${param["tab"]}'=='ant'){
		$("#toCustomContrast").removeClass("top_menu_on").addClass("top_menu_on").find("a").click();
	}else  if('${param["tab"]}'=='match'){
		$("#toProductMatch").removeClass("top_menu_on").addClass("top_menu_on").find("a").click();
	}else  if('${param["tab"]}'=='sa'){
		$("#toSa").removeClass("top_menu_on").addClass("top_menu_on").find("a").click();
	}
</script>
</body>
</html>