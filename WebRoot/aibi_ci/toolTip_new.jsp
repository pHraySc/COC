<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript"  >
//30秒会操作:关闭演示弹出层
function closeProcessIntro(){   $("#processIframe").hide();}
$(document).load(function(){
       //30秒会操作:演示弹出层宽和高动态设置
        //演示弹出层宽和高动态设置
	$("#processIframe").height($(document).height());
       //30秒会操作:获取是否跳过演示标识值
   	var cookieNums = getCookieByName("CALCULATE_NUMS_COOKIE_${userId}");
   	/* if(cookieNums<=1){
   		//是否显示30秒会操作 1 显示; 0 不显示
   		if('${isShow30}' != '0'){
	   		canOperation30();
   		}
   	} */
   	//30秒会操作:检测F11按键动态改变高度
   	$(document).keyup(function (ev) {
   		var event = ev|| event;
   		if(event.keyCode == "122" && !processFlag){
   			$("#processIframe").height($(document).height());
   			processIframe.window.changeBodyHeightWidth();
   		}
   	});
   	//30秒会操作:关闭弹出框
   	$(".dialogClose ,.btnActiveBox input").click(function(){ $(this).parent().parent().dialog("close");})
   	
    //工具栏定位
	var bodyWidth = $("body").width();
	var toolTipRight= parseInt((bodyWidth-1005)/2,10)-32 ;
	toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
	$("#toolTip").css("right",toolTipRight);
});

//我的关注 ：1 ;我的使用 ：2
//在计算中心点击我的关注
function showUserAttentionLabel2(){
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=5";
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//在计算中心点击我的使用
function showUserUsedLabel2(){
	var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?userOperType=2";
	window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//三十秒会操作
function canOperation30(){
	$("#processIframe").show();
}
</script>
	<!-- 提示框-->
    <div class="toolTip" id="toolTip">
	  <ol style="line-height:100%;">
		<li><a href="javascript:void(0);" class="myfocus" onclick="showUserAttentionLabel2()"><span>我的收藏</span></a></li>
		<!-- <li><a href="javascript:void(0);" class="feedback"><span>意见反馈</span></a></li>
		<li><a href="javascript:void(0);" class="history"><span>浏览历史</span></a></li> -->
		<!-- <li><a href="javascript:void(0);" class="history" ><span>我的使用</span></a></li> -->
		<li><a href="javascript:void(0);" class="action" onclick="canOperation30()"><span>30s操作</span></a></li>
		<li><a href="javascript:void(0);" class="scrollTop"  id="scrollTop" ><span>回到顶部</span></a></li>
	  </ol>
	</div>
	<iframe style="display:none;" id="processIframe"  class="processIframe" src="${ctx}/aibi_ci/calculate30.jsp" framespacing="0" frameborder="0" width="100%" height="100%" scrolling="no" > </iframe>
	