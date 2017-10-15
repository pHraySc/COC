<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
	<head>
		<title>客户群完整规则</title>
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
			});
		</script>
	</head>
	<body>
		<div class="clientMsgBox rule-title">
			<ol class="clearfix">
				<li>
		   			<label class="fleft" style="marggin-left:10px;"><b>客户群名称：</b>${ciCustomGroupInfo.customGroupName}</label>
		 		</li>
		 	</ol>
		</div>
		<div class="ruleBox" id="ruleBox">
			<div class="ruleBoxInner fleft">
		 		<b>规则：</b>${ciCustomGroupInfo.prodOptRuleShow}${ciCustomGroupInfo.labelOptRuleShow}${ciCustomGroupInfo.customOptRuleShow}${ciCustomGroupInfo.kpiDiffRule}
			</div>
	    </div>
	</body>
</html>

