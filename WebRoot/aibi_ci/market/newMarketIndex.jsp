<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link rel="stylesheet" href="${ctx}/aibi_ci/assets/js/jqueryUI/slider/themes/default/default.css" type="text/css" media="screen" /> 
<link rel="stylesheet" href="${ctx}/aibi_ci/assets/js/jqueryUI/slider/nivo-slider.css" type="text/css" media="screen" /> 
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUI/slider/jquery.nivo.slider.js"></script> 
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/main.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/mainEntrance.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/webtrends/webtrends.load.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/webtrends/webtrends.hm.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/webtrends/webtrends.min.js"></script>
<title>首页</title>
<script type="text/javascript"> 
	$(function(){
		$(document).click(function(e){
			var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=false;  
		});
		$("#slider").nivoSlider({
			animSpeed: 300,
			pauseTime: 6000
		});
		$('.J_child_market_task').COClive('click', function() {
			var taskId = $(this).attr('taskId');
			var url = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?marketTaskId=" + taskId;
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()
					+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
			//轨迹跟踪  首页选择营销二级任务 
			try{
				if(window.WebTrends){
					    Webtrends.multiTrack('WT.busi','YXDH','WT.oper','MKT_TASK_LEVEL2_IDX','WT.obj',taskId);
				}
			}catch(e){
			    alert(e);
			}
		});
		
		$('.J_parent_market_task').COClive('click', function() {
			var taskId = $(this).attr('taskId');
			var url = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?marketTaskId=" + taskId;
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()
					+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		});
		
		$('.J_other_index_link').COClive('click', function() {
			var indexLinkType = $(this).attr('indexLinkType');
			var url = $.ctx + "/core/ciMainAction!findMarketMain.ai2do?indexLinkType=" + indexLinkType;
			window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");

			//轨迹跟踪 indexLinkType : 1 首页快捷方式进入我的用户群,2 首页快捷方式进入全景用户群,3首页快捷方式进入全景标签
			try{
				if(window.WebTrends){
					var busiId = "";
					var oper = "";
					var objId = "";
					if(indexLinkType == 1){
						busiId = "WDKHQ";
						oper = "MY_CUSTOM_IDX";
						objId = "WDKHQ"
					}else if(indexLinkType == 2){
						busiId = "QJKHQ";
						oper = "ALL_CUSTOM_IDX";
						objId = "QJKHQ"
					}else if(indexLinkType == 3){
						busiId = "QJBQ";
						oper = "ALL_LABEL_IDX";
						objId = "QJBQ"
					}
					Webtrends.multiTrack('WT.busi',busiId,'WT.oper',oper,'WT.obj',objId);
				}
			}catch(e){
			    alert(e);
			}
		});
		
	});
</script> 
</head>
<body>
    <jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
	<div class="container">
		<div class="coc-banner">
			<div class="slider-wrapper theme-default">
	            <div id="slider" class="nivoSlider">
	                <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/market/images/coc_index1.png" data-thumb="${ctx}/aibi_ci/assets/themes/zhejiang/images/market/images/coc_index1.png" alt="" data-transition="slideInLeft" /> 
	                <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/market/images/coc_index2.png" data-thumb="${ctx}/aibi_ci/assets/themes/zhejiang/images/market/images/coc_index2.png" alt="" data-transition="slideInLeft" /> 
	                <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/market/images/coc_index3.png" data-thumb="${ctx}/aibi_ci/assets/themes/zhejiang/images/market/images/coc_index3.png" alt="" data-transition="slideInLeft" /> 
	            </div>
	        </div>
		</div>
		<div class="index-content">
			<div class="index-bg-box">
				<div class="index-menu-line"></div>
				<div class="index-menu-line"></div>
				<div class="index-menu-line"></div>
			</div>
		
			<div class="clearfix ">
			    <c:forEach items="${marketTaskTree}" var="marketTaskTree" varStatus="st">
					<div class="index-menu-group fleft">
						<h3 class="index-menu-header">
							<span style="background:url('${ctx}/aibi_ci/market/assets/images/${marketTaskTree.iconName}') no-repeat center center;" class="index-menu-icon market-navigation-icon fleft" ></span>
							<a class="index-menu-text fleft hand J_parent_market_task" taskId="${marketTaskTree.marketTaskId}">${marketTaskTree.marketTaskName}</a>
						</h3>
						<ul class="index-menu-list">
						    <c:forEach items="${marketTaskTree.childCiMarketTaskList}" var="childCiMarketTask">
						        <li class="index-menu-item">
								    <a class="fleft J_child_market_task" taskId="${childCiMarketTask.marketTaskId}">${childCiMarketTask.marketTaskName}</a>
							    </li>
						    </c:forEach>
						</ul>
					</div>
			    </c:forEach>
			    
			    <div class="index-menu-group fleft">
					<h3 class="index-menu-header">
						<span class="index-menu-icon-eight market-navigation-icon fleft"></span>
						<span class="index-menu-text fleft">快捷通道</span>
					</h3>
					<ul class="index-menu-list">
						<li class="index-menu-item">
							<a class="fleft J_other_index_link" indexLinkType="1">我的用户群</a>
						</li>
						<li class="index-menu-item">
							<a class="fleft J_other_index_link" indexLinkType="2">用户群集市</a>
						</li>
                           <li class="index-menu-item">
							<a class="fleft J_other_index_link" indexLinkType="3">标签集市</a>
						</li>
					</ul>
			    </div>
			</div> 
		</div>
		<div class="index-footer">
        	<ul>
            	<li><a href="javascript:void(0);" class="J_buiding">产品功能</a></li>
                <li><a href="javascript:void(0);" class="J_buiding">操作指南</a></li>
                <li><a href="javascript:void(0);" class="J_buiding">经验技巧</a></li>
                <li><a href="javascript:void(0);" class="J_buiding">优秀案例</a></li>
            </ul>
        </div>
	</div>	 
</body>
</html>