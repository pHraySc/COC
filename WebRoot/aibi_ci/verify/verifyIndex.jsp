<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>产品规则校验</title>
<script type="text/javascript">
$(function(){
	loadassessResult();
})
function show_clipboard_table(ts){
	var tar=$("#clipboard_table");
	$(ts).hasClass("showed")?tar.slideUp("fast",function(){$(ts).removeClass("showed")}):tar.slideDown("fast",function(){$(ts).addClass("showed")});
	$(ts).blur();
}
//标签关联分析
function loadassessResult() {
	var offerId = '${productInfo.offerId}'
	var url = $.ctx+'/ci/ciProductVerifyInfoAction!assessResult.ai2do?ciVerifyRuleRel.offerId='+offerId;
	$('#Frame').attr("src",url);
	$('#Frame').load();
}
</script>
</head>

<body>
<div class="newHeader">
	<div class="header_left" style="background:url(${ctx}/aibi_ci/assets/themes/default/images/header_left_<%=teleOperator %>.png) no-repeat;">
        <img src="${ctx}/aibi_ci/assets/themes/default/images/title_<%=coc_province %>.png" /></div>
	<div class="clear"></div>
</div>
<div class="customerTop">
    <dl class="clipboard">
		<dt>${productInfo.productName}<a onclick="show_clipboard_table(this)" href="javascript:void(0)">查看详情</a></dt>
		<dd title="${productInfo.ruleDesc}">${productInfo.ruleDesc}</dd>
	</dl>
    <div class="clear"></div>
    <div id="clipboard_table" class="clipboard_table" style="display:none;">
        <table width="100%" class="commonTable" cellpadding="0" cellspacing="0">
            <tr class="bg">
                <th width=100>适用品牌：</th>
                <td colspan="3" class="align_left">${productInfo.brandNames}</td>
            </tr>
            <tr >
                <th width=100>产品含义：</th>
                <td colspan="3" class="align_left">${productInfo.productMean}</td>
            </tr>
            <tr class="bg">
                <th width=100>互斥规则：</th>
                <td colspan="3" class="align_left">${productInfo.mutexRule == null ? '-' : productInfo.mutexRule}</td>
            </tr>
            <tr >
                <th width=100>所属部门：</th>
                <td class="align_left">${productInfo.deptId == null ? '-' : productInfo.deptId}</td>
            </tr>
            <tr class="bg">
                <th width=100>所属分类：</th>
                <td class="align_left">${productInfo.categoryName}</td>
            </tr>
            <tr class="bg">
                <th width=100>推荐理由：</th>
                <td colspan="3" class="align_left">${productInfo.descTxt}</td>
            </tr>
        </table>
    </div>
</div>
<div class="submenu">
	<div class="top_menu">
		<ul>
			<li class="top_menu_on"><div class="bgleft">&nbsp;</div><a onclick="loadassessResult()" class="no22"><span></span><p>评定结果</p></a><div class="bgright">&nbsp;</div></li>
			<li><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciProductVerifyInfoAction!getAnalysisAndDesignProcess.ai2do?productInfo.productId=${productInfo.productId}&productInfo.offerId=${productInfo.offerId}" target="Frame"  class="no4"><span></span><p>分析与设定过程</p></a><div class="bgright">&nbsp;</div></li>
			<!--<li><a href="xxxxx.html" target="Frame"  class="no23"><span></span><p>帮助</p></a></li>
			--><div class="clear"></div>
		</ul>
	</div>
</div>
<div>
	<iframe name="Frame" id="Frame" scrolling="no"  id="frame"  framespacing="0" border="0" frameborder="0" style="width:100%;height: 800px;"></iframe>
</div>
</body>
</html>
