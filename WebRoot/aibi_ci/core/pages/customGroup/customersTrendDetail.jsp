<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户群趋势分析明细</title>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/main.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/customListDownEntrance.js"></script>
</head>
<body>
<div class="dialog_table dialog_full_table">
	<div class="customerTop" style="background:#ecf4f9 url(${ctx}/aibi_ci/assets/themes/default/images/cu_top_bg.gif) repeat-x bottom; border-bottom:none;padding-bottom:10px;">
        <input type="hidden" id="customGroupId" value="${customGroupInfo.customGroupId}" />
        <input type="hidden" id="dataDate" value="${dataDate}" />
		<dl>
			<dt style="text-align:left;">${customGroupInfo.customGroupName}</dt>
			<dd style="cursor: default;text-align:left;" title="用户群规则 : ${customGroupInfo.prodOptRuleShow}${customGroupInfo.labelOptRuleShow}${customGroupInfo.customOptRuleShow}${customGroupInfo.kpiDiffRule}">${customGroupInfo.prodOptRuleShow}${customGroupInfo.labelOptRuleShow}${customGroupInfo.customOptRuleShow}${customGroupInfo.kpiDiffRule}
			</dd>
		</dl>
	</div>
	<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
		<tr>
			<td class="v_top" width="50%">
                <!-- 用户群趋势图展示区域 -->
				<div id='aibiChartDiv' class="aibi_chart_div" style="width:100%;height:290px;border:#ccc solid 1px; background:#fff">
				</div>
			</td>
			<td class="v_top" width="50%" style="padding-left:0px">
                <!-- 用户群趋势列表展示区域 -->
				<div id='aibiTableDiv' class="aibi_table_div" >
					<div class="loading" id="loading">正在加载，请稍候 ...</div>
				</div>
			</td>
		</tr>
	</table>
</div>
<input id="isSuccess" type="hidden" value="${resultMap['success']}"/>
<input id="msg" type="hidden" value="${resultMap['msg']}"/>
</body>
</html>
