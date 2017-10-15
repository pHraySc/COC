<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<script type="text/javascript">
$(function(){
	$("#top10_list li").hover(function(){
		$(this).addClass("onhover");
	},function(){
		$(this).removeClass("onhover");
	});
})
</script>


	<div class="top10">
    	<div class="img">&nbsp;</div>
        <p>近期营销活动推荐</p>
    </div>
    
    <ul id="top10_list" class="top10_list">
    <c:forEach items="${top10}" var="campaign" varStatus="campaignStatus">
    	<li>
        	<div class="bg"><span>${campaignStatus.count}</span><p>${campaign}</p></div>
        	<!-- 暂时没有“推荐理由”的数据
            <div class="txt">推荐理由：推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由推荐理由</div>
        	-->
        	</li>
    </c:forEach>
    </ul>
