<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-content-inner cia-active-tab">
	<div class="fleft of tag-del-box margin-r-30 cia-icons cursor hidden"></div>
	<ul class="main-content-inner">
		<li class="main-content-item clearfix">
			<div class="fleft cia-left-tag">
				<span class="fleft cia-left-tag-text tc">行</span> <span
					class="fleft cia-left-tag-arrow cia-icons"></span>
			</div>
			<div class="fleft cia-left-dropzone">
				<ul class="clearfix of J_droppable" target="row">
					<li class="fleft placeholder">请拖拽至此</li> 
				</ul>
			</div>
		</li>
		<li class="main-content-item clearfix">
			<div class="fleft cia-left-tag">
				<span class="fleft cia-left-tag-text tc">列</span> <span
					class="fleft cia-left-tag-arrow cia-icons"></span>
			</div>
			<div class="fleft cia-left-dropzone">
				<ul class="clearfix of J_droppable" target="column">
					<li class="fleft placeholder">请拖拽至此</li> 
				</ul>
			</div>
		</li>
		<li class="main-content-item clearfix">
			<div class="fleft cia-left-tag">
				<span class="fleft cia-left-tag-text tc">标记</span> <span
					class="fleft cia-left-tag-arrow cia-icons"></span>
			</div>
			<div class="fleft cia-left-dropzone">
				<ul class="clearfix of J_droppable" target="symbol">
					<li class="fleft placeholder">请拖拽至此</li> 
				</ul>
			</div>
		</li>
		<li class="main-content-item clearfix">
			<div class="fleft cia-left-tag">
				<span class="fleft cia-left-tag-text tag-text-height tc">筛选</span> <span
					class="fleft cia-left-tag-arrow tag-text-height cia-icons"></span>
			</div>
			<div class="fleft of b-right">
				<p class="cia-left-tag-sec b-bottom">仅排除</p>
				<p class="cia-left-tag-sec">仅保留</p>
			</div>
			<div class="clearfix of">
				<div class="fleft cia-left-dropzone b-bottom width980">
					<ul class="clearfix of J_droppable" target="filter-exclude">
						<li class="fleft placeholder">请拖拽至此</li> 
					</ul>
				</div>
			</div>
			<div class="clearfix of">
				<div class="fleft cia-left-dropzone width980">
					<ul class="clearfix of J_droppable" target="filter-include">
						<li class="fleft placeholder">请拖拽至此</li> 
					</ul>
				</div>
			</div>
		</li>
	</ul>
	<div class="of clearfix">
		<div class="charts-control-box of b-bottom cb">
			<div class="fleft cursor">
				<span class="fleft">
				 	<i class="cia-icon table-icon-one active J_chartType" title="折线图" chartType="line"></i>
				</span> 
				<span class="fleft">
				 	<i class="cia-icon table-icon-two J_chartType" title="饼图" chartType="pie"></i>
				</span> 
				<span class="fleft">
				 	<i class="cia-icon table-icon-three J_chartType" title="柱状图" chartType="column"></i>
				</span> 
				<span class="fleft">
				 	<i class="cia-icon table-icon-four J_chartType" title="条形图" chartType="bar"></i>
				</span> 
				<span class="fleft">
				 	<i class="cia-icon table-icon-five J_chartType" title="组合图" chartType="combin"></i>
				</span> 
				<span class="fleft">
				 	<i class="cia-icon table-icon-six J_chartType" title="散点图" chartType="scatter"></i>
				</span> 
				<span class="fleft">
				 	<i class="cia-icon table-icon-seven J_chartType" title="汽泡图" chartType="bubble"></i>
				</span> 
				<span class="fleft">
					 <i class="cia-icon table-icon-eight J_chartType" title="堆积图" chartType="stackColumn"></i>
				</span>
			</div>
			<div class="of fright">
				<div class="fleft of margin-r-30">
					<label> <input type="checkbox"
						class="fleft excluding-checkbox" /> <span class="fleft tl">是否按电话号码剔除</span>
					</label>
				</div>
				<button type="button" class="cia-show-button cursor fright">生成图表</button>
			</div>

		</div>
		<div class="echarts-box of b-bottom cb no-charts">
			<div class="echarts-box of b-bottom cb J_chartContainer"></div>
			
			<div id="tableDiv" class="echarts-box of b-bottom cb J_tableContainer" >
				<table border="0" class="cia-table-box J_jqGrid"></table>
				<div class="J_jqGridPager"></div>
			</div>	
			
			<div class="switch-views cursor">
				<span class="fleft cia-charts active J_switch"> 
					<i class="cia-icons charts-icon-switch active"></i>
				</span> 
				<span class="fleft cia-table J_switch"> 
					<i class="cia-icons table-icon-switch"></i>
				</span>
			</div>			
		</div>		
	</div>
</div>