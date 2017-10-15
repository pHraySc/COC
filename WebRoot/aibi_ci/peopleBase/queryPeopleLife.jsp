<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<script type="text/javascript">
$(function(){
	
	/* $("#rwb_iconlist li").hover(function(){
		$(this).addClass("onhover");
	},function(){
		$(this).removeClass("onhover");
	}); */
	
	$(".rwb_ul li").hover(function(){
		$(this).addClass("onhover");
	},function(){
		$(this).removeClass("onhover");
	});
	
});
function changeToBase(){
	$("#base").show();
	$("#life").hide();
}
</script>

	<div class="left_title">
        <div class="title_txt"><font>${keyWord}</font></div>
        <ul id="rwb_iconlist" class="rwb_iconlist">
            <li onclick="changeToBase();"><span id="baseBtn" class="icon07">&nbsp;</span><p>电信类</p></li>
            <li class="onhover"><span class="icon08">&nbsp;</span><p>生活类</p></li>
        </ul>
    </div>
    
    <div class="rwb_left_ct">
    
    	<c:forEach items="${peopleLifeList}" var="configs">
    		<c:if test="${configs.displayLabelOrIndexList != null && fn:length(configs.displayLabelOrIndexList) != 0}" >
    		<dl class="rwb_left_rows">
    			<dt>
    			<span style="display:block; float:left; padding-left:50px; color:#393939; background:url('${ctx}/aibi_ci/assets/themes/default/images/help/${configs.icon}') no-repeat 15px 5px; font-weight:bold; font-size:14px;">${configs.typeName}</span>
				</dt>
				<dd>
                	<ul class="rwb_ul">
						<c:forEach items="${configs.displayLabelOrIndexList}" var="labelsAndIndexes">
							<li>
								<!-- "&#10;"换行 -->
								<font title="${fn:trim(labelsAndIndexes.displayName)}&#10;业务口径：${labelsAndIndexes.busiCaliber}">${labelsAndIndexes.displayName}</font>
								<!--向下的箭头，暂时没有其他信息展示 <span>&nbsp;</span> -->
							</li>
						</c:forEach>
					</ul>
				</dd>
				
            	<dd>
		 	</dl>
		 	</c:if>
		</c:forEach>	
    </div><!--rwb_left_ct end -->
