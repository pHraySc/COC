<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>评定结果</title>
<script type="text/javascript">
	$(document).ready(function(){
		initProductHisInfo();
		initUserNumHisInfo();
		aibiTableChangeColor(".mainTable");
		var _h=$("body").height();
		$(window.parent.document).find("#Frame").height(_h+20);
		
		$( document ).tooltip({
			items: "#tooltip01,#tooltip02,#tooltip03,#tooltip04",
			content: function() {
				var element = $( this );
				if ( element.is( "#tooltip01" ) ) {
					return element.attr( "title" );
				}
				if ( element.is( "#tooltip02" ) ) {
					return element.attr( "title" );
				}
                if ( element.is( "#tooltip03" ) ) {
                    return element.attr( "title" );
                }
				if ( element.is( "#tooltip04" ) ) {
					return $("#raise_tipct").html()
				}
			}
		});
		
	});

	//加载产品提升倍数和查全率的趋势图
	function initProductHisInfo() {
		var offerId = $("#offerId").val();
		$('#productHisInfoFrame').height("280px");
		var url = $.ctx+'/ci/ciProductVerifyInfoAction!initProductHisInfo.ai2do?ciVerifyRuleRel.offerId='+offerId;
		$('#productHisInfoFrame').attr("src",url);
		$('#productHisInfoFrame').load();
	}

	//加载产品提潜在用户的趋势图
	function initUserNumHisInfo() {
		var offerId = $("#offerId").val();
		$('#userNumHisInfoFrame').height("280px");
		var url = $.ctx+'/ci/ciProductVerifyInfoAction!initUserNumHisInfo.ai2do?ciVerifyRuleRel.offerId='+offerId;
		$('#userNumHisInfoFrame').attr("src",url);
		$('#userNumHisInfoFrame').load();
	}
</script>
<style>
.ui-tooltip {
	max-width: 510px;
}
#raise_tipct{display:none;}
</style>
</head>
<body>
<div id="raise_tipct">
    <table class="raise_tiptable commonTable" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <th width="46" nowrap="nowrap"><p align="center"><strong>星级 </strong></p></th>
        <th width="119" nowrap="nowrap"><p align="center"><strong>查全率 </strong></p></th>
        <th width="125" nowrap="nowrap"><p align="center"><strong>提升倍数 </strong></p></th>
        <th width="195" nowrap="nowrap"><p align="center"><strong>适用营销方式 </strong></p></th>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">5</p></td>
        <td width="119" nowrap="nowrap"><p>60%-100%（包含60%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以上（包含10倍） </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>热线外呼、短信主动营销 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">4.5</p></td>
        <td width="119" nowrap="nowrap"><p>60%-100%（包含60%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以下 </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>短信主动营销 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">4</p></td>
        <td width="119" nowrap="nowrap"><p>50%-60%（包含50%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以上（包含10倍） </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>营业厅、电子渠道、热线呼入营销 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">3.5</p></td>
        <td width="119" nowrap="nowrap"><p>50%-60%（包含50%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以下 </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>营业厅、电子渠道、热线呼入营销 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">3</p></td>
        <td width="119" nowrap="nowrap"><p>40%-50%（包含40%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以上（包含10倍） </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>营业厅、电子渠道、热线呼入营销 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">2.5</p></td>
        <td width="119" nowrap="nowrap"><p>40%-50%（包含40%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以下 </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>营业厅、电子渠道、热线呼入营销 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">2</p></td>
        <td width="119" nowrap="nowrap"><p>30%-40%（包含30%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以上（包含10倍） </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>需要进一步优化规则 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">1.5</p></td>
        <td width="119" nowrap="nowrap"><p>30%-40%（包含30%） </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以下 </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>需要进一步优化规则 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">1</p></td>
        <td width="119" nowrap="nowrap"><p>30%以下 </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以上（包含10倍） </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>需要进一步优化规则 </p></td>
      </tr>
      <tr>
        <td width="46" nowrap="nowrap"><p align="center">0.5</p></td>
        <td width="119" nowrap="nowrap"><p>30%以下 </p></td>
        <td width="125" nowrap="nowrap"><p>10倍以下 </p></td>
        <td width="195" nowrap="nowrap" valign="bottom"><p>需要进一步优化规则 </p></td>
      </tr>
    </table>
</div>

<div class="main_con_box verify_main_con_box">
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>历史规则变化信息</span></div>
	</div><!--main_con_top end -->
    
    <div style="width:98%; margin:0 auto;">
    	<iframe name="" id="productHisInfoFrame" scrolling="no"  allowtransparency="true"  framespacing="0" border="0" frameborder="0" style="width:540px;height:280px;margin-left:40px;"></iframe>
    	<iframe name="" id="userNumHisInfoFrame" scrolling="no"  allowtransparency="true"  framespacing="0" border="0" frameborder="0" style="width:580px;height:280px"></iframe>
	</div>
</div>

<div class="main_con_box verify_main_con_box">
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>历史规则变化信息表格</span></div>
	</div><!--main_con_top end -->
    
    <div class="aibi_table_div" id="table1">
		<table width="100%" class="mainTable verifyTable noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="15%">月份</th>
				<th width="15%">查全率<span id="tooltip02" class="greenTip inline_block" title="利用基准月新增订购用户数据通过发掘获取产品潜在用户规则，通过计算某月新增订购用户符合此规则的用户数占比，即此月的查全率。">&nbsp;</span></th>
				<th width="15%">提升倍数<span id="tooltip03" class="greenTip inline_block" title="通过规则获取产品潜在用户集，则全量未订购产品用户数与产品潜在用户集用户数的比值，即产品潜在用户规则提升倍数。">&nbsp;</span></th>
                <th width="15%">潜在用户数</th>
				<th width="40%" style="border-right:none;">星级评定<span id="tooltip04" class="greenTip inline_block">&nbsp;</span></th>
			</tr>
			<c:forEach items="${ciVerifyRuleRelList}" var="verifyRuleRel">
			    <tr><td colspan="5" class="align_left">
			           <div class="main_con_top">
		<div class="tag_box_title" style="background:none;">产品规则:${verifyRuleRel.ruleDesc}。</div>
	</div><!--main_con_top end -->
			          </td></tr>
			    <c:forEach items="${verifyRuleRel.verifyMmList}" var="verifyMm">
			          <tr>
			              <td class="align_right">${verifyMm.dataDate}</td>
                          <td class="align_right"><fmt:formatNumber value="${verifyMm.recallInt}" type="number" pattern="0.##"/>%</td>
				          <td class="align_right"><fmt:formatNumber value="${verifyMm.upgradeMultiple }" type="number" pattern="0.##"/></td>
				          <td class="align_right"><fmt:formatNumber value="${verifyMm.potentialUserNum}" type="number" pattern="###,###,###"/></td>
				          <td style="border-right:none;">
                                  <div class="topstars">
                                      <font>${verifyMm.starsId/2}</font>
                                     ${verifyMm.starsHtml}
                                  </div>
                         </td>
			          </tr>
			    </c:forEach>
			</c:forEach>
		</table>
	</div>
</div><!--main_con_box end -->
<input type="hidden" id="offerId" value="${ciVerifyRuleRel.offerId}" />
</body>
</html>
