<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<title>客户经营中心</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/jQuery.contextMenu.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/shoppingCart.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/main.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/mainEntrance.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/sortTag.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/webtrends/webtrends.load.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/webtrends/webtrends.hm.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/webtrends/webtrends.min.js"></script>
</head>
<body>
	<jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
    <div class="mainBody">
        <!-- search -->
        <jsp:include page="/aibi_ci/index_top.jsp" ></jsp:include>
        <div id="mainContent"></div>
    </div>
	<div class="container COC-bgcolor">
		<div class="container-inner clearfix">
			<div class="show-menu-icon clearfix"></div>
			<!-- 左侧菜单begin -->
			<div class="left-menu-list fleft J_left_menus">
				<div class="left-menu-inner">
					<div class="menu-top-icon market-navigation-icon menu-top-icon0" id="menu-top-icon"></div>
					<div class="market-menu-title" id="marketTaskTitle"></div>
					<div class="market-dividing-line market-navigation-icon"></div>
					<div class="market-type-group group-bottom-line">
						<h4 class="market-group-header ellipsis-all J_market_Menus">
							<span class="fleft market-group-icon-hover market-navigation-icon "></span>
							<span class="fleft">营销视图</span>
						</h4>
						<ul class="market-type-group J_market_group">
							<c:forEach var="marketTask" items="${marketTaskTree}">
								<li class="market-group-item ellipsis-all J_market-task-menu" taskId="${marketTask.marketTaskId}" id="parentMarketTask_${marketTask.marketTaskId}"
									icon="${marketTask.iconName}" data-attr='${marketTask.childrenJson}'>${marketTask.marketTaskName}</li>
							</c:forEach>
							<li class="last-bottom-line"></li>
						</ul>
					</div>
					<div class="market-type-group">
						<h4 class="market-group-header ellipsis-all J_market_Menus">
							<span class="fleft market-group-icon market-navigation-icon"></span>
							<span class="fleft">全景视图</span>
						</h4>
						<ul class="market-type-group hidden J_group_menus J_market_group">
							<li class="market-group-item ellipsis-all" id="labelInfoIndex">标签集市</li>
							<c:forEach items="${firstCategoryList}" var="labelInfo">
							<li class="market-group-item ellipsis-all market-group-item-no-dot firstLabelCategories" 
								id="firstCategory_${labelInfo.labelId}" label_value='${labelInfo.labelId }' 
								data-attr ='${labelInfo.labelCategories }' 
								title="${labelInfo.labelName }">${labelInfo.labelName }</li>
							</c:forEach>
							<li class="market-group-item ellipsis-all" id="customMarketLink">用户群集市</li>
						</ul>
					</div>
					<div class="market-type-group">
						<h4 class="market-group-header ellipsis-all J_market_Menus">
							<span class="fleft market-group-icon market-navigation-icon"></span>
							<span class="fleft">我的视图</span>
						</h4>
						<ul class="market-type-group hidden J_market_group">
							<li class="market-group-item ellipsis-all" id="myCustomGroup">我的用户群</li>
							<li class="market-group-item ellipsis-all" id="userAttention">我的收藏</li>
						</ul>
					</div>
					<div class="market-type-group">
						<h4 class="market-group-header ellipsis-all J_market_Menus">
							<span class="fleft market-group-icon market-navigation-icon"></span>
							<span class="fleft">热门推荐</span>
						</h4>
						<ul class="market-type-group hidden J_market_group">
							<li class="market-group-item ellipsis-all" id="sysHotLabelRankingListLink">系统热门标签</li>
							<li class="market-group-item ellipsis-all" id="lastPublishRankLabelLink">最新发布标签</li>
							<li class="market-group-item ellipsis-all" id="sysHotCustomRankingListLink">系统热门用户群</li>
							<li class="market-group-item ellipsis-all" id="lastUsedCuntomLink">最新发布用户群</li>
						</ul>
					</div>
				</div>
			</div>
			<!-- 左侧菜单end -->
			
			<div class="main-content fleft J_market_content">
				<div class="coc-main-inner">
					<!-- 购物车begin -->
					<div id="shopCartList" class="coc-cart-content fright"></div>
					<!-- 购物车end -->
					<!-- 中间页面begin -->
					<div id="marketMainList" class="main-list-content fright"></div>
					<!-- 中间页面end -->
				</div>
			</div>
		</div>
	</div>
	
	<!-- 点击标签上的叹号，下拉内容： -->
	<!--labelTip-->
	<div id="labelTip" class="labelTip">
	</div>
	
	<!--customTip-->
	<div id="customTip" class="labelTip">
	</div>
	
	<div id="enumLabelSetting" style="overflow:hidden;">
		<iframe id="enumLabelSettingFrame" name="enumLabelSettingFrame" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:453px;" scrolling="no"  ></iframe>
	</div>
	<div id="textLabelSetting" style="overflow:hidden;">
		<iframe id="textLabelSettingFrame" name="textLabelSettingFrame" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:290px;" scrolling="no"  ></iframe>
	</div>
	<div id="vertLabelSetting">
		<iframe id="vertLabelSettingFrame" name="vertLabelSettingFrame" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:480px" scrolling="no" allowtransparency="true"></iframe>
	</div>
	<input id="seachTypeId_" type="hidden" value="${seachTypeId }" />
	<input id="seachKeyWord_" type="hidden" value="${seachKeyWord}" />
	
	<!--用户群规则设置-->
	<div class="labelsCT J_mainPageCustomRuleDiv" id="tag_dropable" style="min-height:400px; text-align:left; display:none;">
	</div>
	<input id="marketTaskIdFromIndexInput" value="${marketTaskId}" type="hidden"/>
	<input id="indexLinkTypeInput" value="${indexLinkType}" type="hidden"/>
</body>
</html>