<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/market/assets/scripts/marketIndex.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/market/assets/scripts/marketIndexEntrance.js"></script>
<input id="marketTaskIdFromMainInput" value="${customLabelSceneRel.marketTaskId}" type="hidden" />
<input id="parentMarketTaskIconInput" value="${parentMarketTask.iconName}" type="hidden" />
<input id="parentMarketIdIconInput" value="${parentMarketTask.marketTaskId}" type="hidden" />
<input id="childMarketTaskNameInput" value="${childMarketTask.marketTaskName}" type="hidden" />
<div class="main-list-content-inner">
	<div class="content-COC-list">
		<div class="COC-list-header">
			<ul class="clearfix">
				<c:forEach var="marketScene" items="${firstLevelScenes}" varStatus="status">
					<li class="fleft list-header-item <c:if test="${status.index == 0}">active</c:if> market-main-scene" 
						style="cursor: pointer;" sceneId="${marketScene.sceneId}" defaultOp="${marketScene.defaultOp}">
						<div class="list-header-item-text">
							<p class="fleft ${marketScene.iconCls} market-navigation-icon"></p>
							<p class="fleft header-adress-text">${marketScene.sceneName}</p>
						</div>
						<div class="selectedLine"></div>
					</li>
					<c:if test="${status.index < fn:length(firstLevelScenes) - 1}">
						<li class="fleft list-header-item">
							<p class="fleft header-arrow-icon market-navigation-icon"></p> 
						</li>
					</c:if>
				</c:forEach>
			</ul>
			
		</div>
		<div class="market-customer-list">
			<div class="market-customer-list-inner">
				<div class="customer-list-search-box">
					<span class="fleft">当前位置：</span>
					<c:if test="${parentMarketTask.marketTaskId ne childMarketTask.marketTaskId }">
						<span class="fleft text-blue hand J_parent_marketTask_path" taskId="${parentMarketTask.marketTaskId}">${parentMarketTask.marketTaskName}</span>
						<span class="fleft adress-next-arrow">&gt;</span>
						<span class="fleft text-blue hand">${childMarketTask.marketTaskName}</span>
					</c:if>
					<c:if test="${parentMarketTask.marketTaskId eq childMarketTask.marketTaskId }">
						<span class="fleft text-blue hand J_parent_marketTask_path" taskId="${parentMarketTask.marketTaskId}">${parentMarketTask.marketTaskName}</span>
					</c:if>
					<p id="queryBtn" class="fright market-navigation-icon customer-search-box">
						<input type="text" name="customLabelSceneRel.labelCustomName" class="customer-search-box-input" value="查询" id="search-customer"/>
						<i class="cus-search-icon"></i>
					</p>
				</div>
				<div id="customLabelList" class="market-customer-list-inner"></div>
			</div>
		</div>
		<!--加入购物车提示-->
		<div class="add-cart-tip market-navigation-icon hidden J_add-cart-tip">
			<div class="close-icon market-navigation-icon hand" id="closeAddShopCartTip"></div>
		</div>
		<form id="queryForm" action="" method="post">
			<input type="hidden" name="customLabelSceneRel.marketTaskId" value="${customLabelSceneRel.marketTaskId}" />
			<input type="hidden" name="customLabelSceneRel.labelCustomName" value="" id="labelCustomName"/>
			<input type="hidden" name="customLabelSceneRel.sceneId" value="" id="customLabelSceneId"/>
			<input type="hidden" name="customLabelSceneRel.defaultOp" value="" id="defaultOp"/>
		</form>
	</div> 
</div> 
