<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>保存营销策略</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(window.parent).scrollTop(0);
	$("#saveBtn").bind("click", function(){
		submitList();
	});
	$("#precisionMarke").click(function(){
		var actionUrl = "${ctx}/ci/marketingStrategyAction!initSaveMarketing.ai2do";
		$('#precisionMarketing').val("true");
		$('#saveForm').attr("action",actionUrl);
		$('#saveForm').submit();
	});
});

function hideTip(){
	$("#mnameTip").hide();;
}

function vForm(){
	if($.trim($('#tacticName').val()) == ""){
		$("#mnameTip").empty();
		$("#mnameTip").show().append("请输入策略名称");
		return false;
	}
	return true;
}

//保存
function submitList(){
	if(vForm()){
		var tacticName = encodeURIComponent(encodeURIComponent($("#tacticName").val()));
		var tacticDesc = encodeURIComponent(encodeURIComponent($("#tacticDesc").val()));;
		var listTableName = $("#listTableName").val();
		var productsIdsStr = $("#productsIdsStr").val();
		var productsStr = $("#productsStr").val();
		var dataDate = $("#dataDate").val();
		var productDate = $("#productDate").val();
		var customGroupId = $("#customGroupId").val();
		var customGroupName = $("#customGroupName").val();
		var customNum = $("#customNum").val();
		var customMatch = $("#customMatch").val();
		var precisionMarketing = $("#precisionMarketing").val();

        var param = "marketTactics.tacticName="+tacticName;
        param = param+"&customGroup.listTableName="+listTableName;
        param = param+"&customGroup.productsStr="+productsStr;
        param = param+"&customGroup.productsIdsStr="+productsIdsStr;
        param = param+"&customGroup.dataDate="+dataDate;
        param = param+"&customGroup.customGroupId="+customGroupId;
        param = param+"&customGroup.productDate="+productDate;
        param = param+"&customGroup.customNum="+customNum;
        param = param+"&customGroup.customMatch="+customMatch;
        param = param+"&customGroup.precisionMarketing="+precisionMarketing;
		
		
		var actionUrl = '$.ctx/ci/marketingStrategyAction!isNameExit.ai2do?marketTactics.tacticName='+tacticName;
		$.post(actionUrl, function(result){
			if(result){
				window.parent.marketingSave(param);
			}else{
				$("#mnameTip").empty();
				$("#mnameTip").show().append("策略名称重名");
			}
		});
	}
}

function returnPage(){
	var actionUrl = "";
	if($("#returnPage").val() == "customProductView"){
	   actionUrl = '$.ctx/ci/marketingStrategyAction!customProductView.ai2do';
	}else{
	   actionUrl = '$.ctx/ci/marketingStrategyAction!marketingStrategy.ai2do';
	}
	$("#saveForm").attr("action",actionUrl);
	$("#saveForm").submit();
}

</script>
</head>
<body>
<div class="submenu">
	<div class="top_menu">
		<ul>
			<li class="top_menu_on">
				<div class="bgleft">&nbsp;</div>
				<a class="no14"><span></span><p>全量推荐</p></a>
				<div class="bgright">&nbsp;</div>
			</li>
			<li>
				<div class="bgleft">&nbsp;</div>
				<a id="precisionMarke" class="no15"><span></span><p>精准营销</p></a>
				<div class="bgright">&nbsp;</div>
			</li>
			<div class="clear"></div>
		</ul>
	</div>
</div>
<!-- 全量推荐begin -->
<form action="${ctx}/ci/marketingStrategyAction!saveMarketingStategy.ai2do" method="post" id="saveForm">
<div class="dialog_table" style="padding-top:0px">
    <span class="analyse_dt_con" style="float: right;margin-top: 10px;margin-right: 10px"><a href="javascript:returnPage()" class="link_back" style="color:#5c9ad5; text-decoration:none"  >返回配置</a></span>

	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<th width="110px"><span>*</span>策略名称：</th>
			<td>
				<div id="select1" class="new_tag_selectBox">
					<input name="marketTactics.tacticName" onkeyup="hideTip()" type="text" id="tacticName" value="" class="new_tag_input" style="cursor:text"/>
				</div>
				<div id="mnameTip" class="tishi error"  style="display:none;"></div><!-- 用于重名验证 -->
			</td>
		</tr>
		<tr class="even">
			<th>策略描述：</th>
			<td>
				<textarea name="marketTactics.tacticDesc" id="tacticDesc" class="new_tag_textarea" style="height:50px;"></textarea>
			</td>
		</tr>
	</table>
</div>
<!-- 审批流程 -->
<div class="showMore_box">
	<div class="showMore">
		<div class="showMove_div">
			<div class="title">
				<div class="title_l">${customGroup.customGroupName} (${customGroup.customNum}人)</div><div class="title_r">统计时间:${dateFmt:dateFormat(customGroup.dataDate)}</div><div class="clear"></div>
			</div>
			<div class="con">
				<dl>
					<dt>产品清单</dt>
					<c:forEach items="${productInfoList}" var="productInfo">
					       <dd>产品名称：${productInfo.productName}，适用品牌：${productInfo.brandNames}</dd>
					</c:forEach>
				</dl>
			</div>
			<div class="bottom"><div class="bottom_l"></div><div class="bottom_r"></div><div class="clear"></div></div>
		</div>
	</div>
	<div class="showMove_control"><span>隐藏更多信息</span></div>
</div>
<!-- 全量推荐end -->
<input type="hidden" name="customGroup.returnPage" id="returnPage" value="${customGroup.returnPage}"/>
<input type="hidden" name="customGroup.productsIdsStr" id="productsIdsStr" value="${customGroup.productsIdsStr}"/>
<input type="hidden" name="customGroup.productsStr" id="productsStr" value="${customGroup.productsStr}"/>
<input type="hidden" name="customGroup.listTableName" id="listTableName" value="${customGroup.listTableName}"/>
<input type="hidden" name="customGroup.dataDate" id="dataDate" value="${customGroup.dataDate}"/>
<input type="hidden" name="customGroup.productDate" id="productDate" value="${customGroup.productDate}"/>
<input type="hidden" name="customGroup.customGroupId" id="customGroupId" value="${customGroup.customGroupId}"/>
<input type="hidden" name="customGroup.customGroupName" id="customGroupName" value="${customGroup.customGroupName}"/>
<input type="hidden" name="customGroup.customNum" value="${customGroup.customNum}" />
<input type="hidden" name="customGroup.customMatch" value="${customGroup.customMatch}"/>
<input type="hidden" name="customGroup.precisionMarketing" id="precisionMarketing" value="${customGroup.precisionMarketing}"/>
<div class="dialog_btn_div like_normal_btndiv">
	<input name="" type="reset" value=" 重置 " class="tag_btn "/>
	<input name="" type="button" onclick="submitList()" value=" 保 存 " class="tag_btn"/>
</div>
</form>
</body>
</html>