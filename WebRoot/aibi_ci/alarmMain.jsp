<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<script type="text/javascript">
$(function(){
	if('${showType}' == 1){
		custAlarm();
	}
	if('${showType}' != 1){
		alarmRecord();
	}
	if('${fromPageFlag}' ==1){
		alarmRecord();
	}
});
function custAlarm(){
	$("#custAlarm").parent().find("li").removeClass("current");
	$("#custAlarm").addClass("current");
	var _html = $("#custAlarm").find("a").html();
	$("#pathCustomText").html(_html);
	$("#alarmManagerList").empty();
	$("#totalSizeNum").parent().show();
	$("#alarmManagerList").load("${ctx}/ci/ciCustomersAlarmAction!init.ai2do");
}
function alarmRecord(){
	$("#alarmRecord").parent().find("li").removeClass("current");
	$("#alarmRecord").addClass("current");
	var _html = $("#alarmRecord").find("a").html();
	$("#pathCustomText").html(_html);
	$("#alarmManagerList").empty();
	$("#totalSizeNum").parent().show();
	$("#alarmManagerList").load("${ctx}/ci/ciCustomersAlarmAction!initRecord.ai2do");
}
</script>
<!-- <style>
  .newMsgCenterBox{padding-top:0px;}
  .queryResultCharDate{padding-left:0px;}
</style> -->
</head>
<body>
<div class="navBox comWidth" id="navBox">
	<div class="fleft navListBox">
	    <ol>
	    	<li id="custAlarm" class="current"><a href="javascript:void(0);" onclick="custAlarm();">预警设置</a></li>
	    	<li id="alarmRecord"><a href="javascript:void(0);" onclick="alarmRecord();">预警结果</a></li>
		</ol>
	</div>
</div>
<div class="comWidth pathNavBox pathNavTagBox">
	<span class="pathNavHome"  style="cursor:default;" onclick="custAlarm();">预警监控</span>
    <span class="pathNavArrowRight" id="pathNavArrowRightMyCustom"></span>
    <span class="pathNavNext" id="pathCustomText"></span>
  	<span class="amountItem"> 共<span class="amountNum" id="totalSizeNum"></span>条预警</span>
</div>
<div id="alarmManagerList"></div>
