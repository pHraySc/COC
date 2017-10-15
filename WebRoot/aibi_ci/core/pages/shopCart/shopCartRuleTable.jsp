<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
	<head>
		<title>用户群完整规则</title>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<script type="text/javascript">
			$(function(){
				//规则滚动条
				$("#ruleBox").mCustomScrollbar({
					theme:"minimal-dark", //滚动条样式
					scrollbarPosition:"outside", //滚动条位置
					scrollInertia:500,
					mouseWheel:{
						preventDefault:true //阻止冒泡事件
					}
				});
				if(parent.$(".J_calculate_tactic_val").length > 0){
					$(".J_show_rule_tactic").html(parent.$(".J_calculate_tactic_val").html());
					$(".J_show_rule_month_date").html(parent.$("#dataDate").val());
					$(".J_show_rule_day_date").html(parent.$("#dayDataDate").val());
				}
				$("#cancelShowShopCartRule").COClive('click', function(e) {
					e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
					parent.$("#queryGroupRuleDialog").dialog("close");
				});
				$("#showRuleToCalculate").COClive('click', function() {
					if(parent.$(".J_is_calcucenter_to_createpage").val() == "false"){
						parent.coreMain.forwardSaveCustomPage();
					}else{
						parent.saveCustom();
					}
				});
			});
		</script>
	</head>
	<body>
		<div class="tableDottedWrap_">
	       	<table>
	          	<tbody>
		          	<tr>
		              	<td align="left" width="420">
		              	<span style="font-weight: bold;">数据时间：</span>月周期&nbsp;&nbsp;<span class="J_show_rule_month_date">${newLabelMonthFormat}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日周期&nbsp;&nbsp;<span class="J_show_rule_day_date">${newLabelDayFormat}</span>
		              	</td>
		              	<td align="left">
		              	<span style="font-weight: bold;">策略：</span><span class="J_show_rule_tactic">保守数据策略</span>
		              	</td>
		          	</tr>
	  	        </tbody>
	       	</table>
    	</div>
		<div class="ruleBox" id="ruleBox">
			<div class="ruleBoxInner ruleBoxTable">
				<table width="95%" id="sortTable" class="tableStyle" >
					<tr>
						<th width="100px;" style="text-align: center;">
						运算
						</th>
						<th width="200px;" style="text-align: center;" class="align_center">
						标签/用户群名称
						</th>
						<th style="text-align: center;" class="align_center">
						属性值
						</th>
					</tr>
					<c:forEach items="${shopCartRuleList}" var="po" varStatus="st">
					<c:choose>
						<c:when test="${st.index % 2 == 0 }">
							<c:choose>
							<c:when test="${po.isSetValue == false }">
							<tr class="even" style="color: #ff0000;">
							</c:when>
							<c:otherwise>
							<tr class="even">
							</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
							<c:when test="${po.isSetValue == false }">
								<tr class="odd" style="color: #ff0000;">
							</c:when>
							<c:otherwise>
							<tr class="odd">
							</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
						<td width="100px;" style="text-align: center;">${po.operatorElement }</td>
						<td width="150px;" style="text-align: left;">${po.lableOrCustomName }</td>
						<td style="text-align: left;">${po.attrValue }</td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="clearfix show-cart-rule-table">
			<div class="fleft show-cart-rule-table-text">注意：红色表示这个标签还未设置属性值</div>
			<div class="fright coc-cart-content show-cart-rule-table-button">
				<button class="fright market-cart-button show-cart-rule-table-buttons" type="button" id="showRuleToCalculate">保存客户群</button>
				<button class="fright market-cart-button show-cart-rule-table-buttons" type="button" id="cancelShowShopCartRule">取&nbsp;&nbsp;消</button>
			</div>
		</div>
	</body>
</html>

