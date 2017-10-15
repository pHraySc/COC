<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
    <script type="text/javascript"  >
	   $(document).load(function(){
          //工具栏定位
		  var bodyWidth = $("body").width();
		  var toolTipRight= parseInt((bodyWidth-1005)/2,10)-32 ;
		  toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
		  $("#toolTip").css("right",toolTipRight);
	   });
	   function showUserAttentionLabel(){
		   var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=5";
		   window.open(url, "_new", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	   }
	 </script>
	<!-- 提示框-->
    <div class="toolTip" id="toolTip">
	    <c:if test='${cookie[version_flag].value eq "false"}'>
		  <ol style="line-height:100%;">
			<li><a href="javascript:showUserAttentionLabel();" class="myfocus"><span>我的收藏</span></a></li>
			<!-- <li><a href="javascript:void(0);" class="feedback"><span>意见反馈</span></a></li>
			<li><a href="javascript:void(0);" class="history"><span>浏览历史</span></a></li> -->
			<!--  <li><a href="javascript:void(0);" class="history" ><span>我的使用</span></a></li>-->
			<li><a href="javascript:void(0);" class="action" onclick="canOperation30()"><span>30s操作</span></a></li>
			<li><a href="javascript:void(0);" class="scrollTop"  id="scrollTop" ><span>回到顶部</span></a></li>
		  </ol>
	    </c:if>
	</div>
	