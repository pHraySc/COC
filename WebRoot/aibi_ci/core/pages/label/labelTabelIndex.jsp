<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/label/label.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/label/labelEntrance.js"></script>
<div class="main-list-content fright">
	<div class="main-list-content-inner">
		<div class="content-COC-list">
			<div class="market-filter-content">
				<div class="market-filter-header">
					<div class="market-filter-header-left fleft">筛选条件：</div>
					<div class="market-filter-header-main fleft">
						<div class="market-filter-header-main-inner">
							<ul class="market-filter-header-main clearfix" id="labelFilterItemList"> 
							</ul>
						</div>
					</div>
				</div>
				<form  id="queryLabelForm">
				<div class="market-filter-list">
					<div class="market-filter-list-box">
						<div class="market-filter-box-left fleft">发布时间：</div>
						<div class="market-filter-header-main fleft">
							<div class="market-filter-header-main-inner" id="dateType1">
								<ul class="market-filter-row clearfix"> 
									<li class="fleft market-filter-row-item J_label_market_filter_item active" selectedElement="all">全部</li> 
									<li class="fleft market-filter-row-item J_label_market_filter_item" id="dateType1_1">一天</li>  
									<li class="fleft market-filter-row-item J_label_market_filter_item" id="dateType1_2">一个月</li>  
									<li class="fleft market-filter-row-item J_label_market_filter_item" id="dateType1_3">三个月</li>
									<li class="fleft market-filter-row-input">
										<p class="fleft">
											<input name="startDate" type="text" class="J_Label_date Wdate" id="wdateBrfore" 
											onFocus="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"/>
										</p>
										<p class="fleft">—</p>
										<p class="fleft">
											<input name="endDate" type="text" class="J_Label_date Wdate" id="wdateAfter" 
											 onFocus="WdatePicker({minDate:'#F{$dp.$D(\'wdateBrfore\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"/>
										</p>
										<p class="fleft">
											<button type="button" class="filter-button btn_normal J_label_date_button hidden">时间筛选</button>
										</p>
									</li>  
								</ul>
							</div> 
						</div> 
					</div> 
					<div class="market-filter-list-box">
						<div class="market-filter-box-left fleft">更新周期：</div>
						<div class="market-filter-header-main fleft">
							<div class="market-filter-header-main-inner" id="updateCycle1">
								<ul class="market-filter-row clearfix"> 
									<li class="fleft market-filter-row-item J_label_market_filter_item active" selectedElement="all">全部</li> 
									<li class="fleft market-filter-row-item J_label_market_filter_item" id="updateCycle1_1">日周期</li>  
									<li class="fleft market-filter-row-item J_label_market_filter_item" id="updateCycle1_2">月周期</li>
								</ul>
							</div> 
						</div> 
					</div> 
				</div>
				<input type="hidden" value="${ciLabelInfo.labelName }" name="ciLabelInfo.labelName" id="labelName"/>
				<!-- 标签分类参数 -->
				<input type="hidden" id="categoriesId" name="pojo.categoriesId" value="${pojo.categoriesId }" />
				<!-- 标签更新周期及发布时间参数 -->
				<input class="J_label_multi_filter" type="hidden" id="formDataScope" name="pojo.dataScope" value="all"></input>
				<input class="J_label_multi_filter" type="hidden" id="updateCycle" name="ciLabelInfo.updateCycle" value="${ciLabelInfo.updateCycle }"></input>
				<input class="J_label_multi_filter" type="hidden" id="orderBy" name="pager.orderBy" ></input>
				<input class="J_label_multi_filter" type="hidden" id="order" name="pager.order" ></input>
				<input class="J_label_multi_filter" type="hidden" id="startDate" name="ciLabelInfo.startDate" ></input>
				<input class="J_label_multi_filter" type="hidden" id="endDate" name="ciLabelInfo.endDate" ></input>
			</form>
			<div class="market-customer-list">
				<div class="market-customer-list-inner">
					<div class="customer-list-search-box">
						<span class="fleft">当前位置：</span>
						<span class="fleft text-blue hand J_labelCategories" _categoriesId="" >标签集市</span>
						<c:forEach items="${labelLevelPath}" var="categories">
							<span class="fleft adress-next-arrow J_labelCategoriesArrow">&gt;</span>
							<span class="fleft text-blue hand J_labelCategories" _categoriesId='${categories.labelId }' >${categories.labelName }</span>
						</c:forEach>
						<p class="fright market-navigation-icon customer-search-box">
							<input type="text" class="customer-search-box-input" value="查询" data-val="false" id="labelSearchName"/>
							<i class="cus-search-icon" id="searchLabelIcon"></i>
						</p>
					</div>
					<div class="market-customer-list-inner" id="labelTableList">
					</div>
				</div>
			</div>
		</div> 
	</div> 
</div>
</div>
