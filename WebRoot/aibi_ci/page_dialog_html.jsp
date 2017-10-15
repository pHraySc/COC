<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!--  <div id="pager" class="dialogPageBox clearfix">-->
<form id="pageForm">
<input type="hidden" name="pager.totalSize" value="${pager.totalSize }">
<input type="hidden" name="pager.totalPage" value="${pager.totalPage }">
</form>
<input type="hidden" id="pageNum" name="pager.pageNum" value="${pager.pageNum}">
	<div class="newPagerBox">
		<ol>
			<li>
			<a href="javascript:;" class="pageFirst num" id="num_1"></a>
			</li>
			<li>
				<a href="javascript:;" class="pageUp pageButton" pageFlag="P"> </a>
			</li>
                 
			<li>
				<span class="fleft">第</span>
				<input type="text"  id="jump" class="fleft pageInput" value="${pager.pageNum }"/>
				<span class="fleft" >页</span>
			</li>
			<li>
			<p><span class="fleft">共</span> <strong class="fleft">${pager.totalPage }</strong><span class="fleft">页</span></p>
			</li>
			<li>
				<a href="javascript:;" class="pageNext pageButton" pageFlag="N"></a>
			</li>
			<li>
				<a href="javascript:;" class="pageLast num"  id="num_${pager.totalPage}">${pager.totalPage}</a>
			</li>
		</ol>
	</div>
