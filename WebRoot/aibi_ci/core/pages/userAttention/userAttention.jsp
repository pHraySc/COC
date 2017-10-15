<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/userAttention/userAttention.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/userAttention/userAttentionEntrance.js"></script>
<div class="main-list-content fright">
	<div class="main-list-content-inner">
		<div class="content-COC-list">
			<div class="market-filter-content">
				<div class="COC-list-header">
					<ul class="clearfix">
						<li class="fleft list-header-item user-attention-type active" userAttentionType="1">
							<p class="fleft header-adress-text">标签收藏</p>
						</li>	
						<li class="fleft list-header-item user-attention-type" userAttentionType="2">
							<p class="fleft header-adress-text">用户群收藏</p>
						</li>
					</ul>
				</div>
				<div class="market-customer-list">
					<div class="market-customer-list-inner">
						<div class="customer-list-search-box">
							<span class="fleft">当前位置：</span>
							<span class="fleft" id="userAttrntionTypeTitle">我的收藏</span>
						</div>
						<div class="market-customer-list-inner" id="userAttentionLabelList"></div>
						<div class="market-customer-list-inner" id="userAttentionCustomList"></div>  
					</div>
				</div>
			</div> 
		</div> 
	</div>
</div>