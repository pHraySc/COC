<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/rankingList/sysHotCustom.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/rankingList/sysHotCustomEntrance.js"></script>
<div class="main-list-content fright">
	<div class="main-list-content-inner">
		<div class="content-COC-list">
			<div class="market-filter-content">
				<div class="COC-list-header">
					<ul class="clearfix">
						<li class="fleft list-header-item sys-hot-custom-datescope active"  dateScope="4">
							<p class="fleft header-adress-text">近一周排行</p>
						</li>	
						<li class="fleft list-header-item sys-hot-custom-datescope"  dateScope="2">
							<p class="fleft header-adress-text">近一个月排行</p>
						</li>
						<li class="fleft list-header-item sys-hot-custom-datescope"  dateScope="3">
							<p class="fleft header-adress-text">近三个月排行</p>
						</li>
					</ul>
				</div>
				<div class="market-customer-list">
					<div class="market-customer-list-inner">
						<div class="customer-list-search-box">
							<span class="fleft">当前位置：</span>
							<span class="fleft">系统热门用户群</span>
						</div>
						<div class="market-customer-list-inner" id="sysHotCustomRankingList">
						</div> 
					</div>
				</div>
			</div> 
		</div> 
	</div>
</div>
