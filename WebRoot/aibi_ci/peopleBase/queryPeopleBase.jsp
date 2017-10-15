<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ taglib uri="http://bi.asiainfo.com/biframe/privilege" prefix="pri"%>
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
	
	var phone = $("#keyword").val();
	var actionUrl = '${ctx}/ci/ciIndexAction!queryPeopleLife.ai2do?keyWord='
		+ phone;
	$("#life").hide();
	<c:if test="${operRight == true}">
	$("#life").load(actionUrl);
	</c:if>
});

function changeToLife(){
	$("#base").hide();
	$("#life").show();
}

</script>

	<div class="left_title">
        <div class="title_txt"><font>${keyWord}</font></div>
        <ul id="rwb_iconlist" class="rwb_iconlist">
            <li class="onhover"><span class="icon07">&nbsp;</span><p>电信类</p></li>
            <c:if test="${operRight == true}">
            	<li id="lifeLi" onclick="changeToLife();"><span id="lifeBtn" class="icon08">&nbsp;</span><p>生活类</p></li>
            </c:if>
        </ul>
    </div>
    
    <div class="rwb_left_ct">
    
    	<c:forEach items="${peopleBaseList}" var="configs">
    		
    		<dl class="rwb_left_rows">
    			<dt>
    			<c:choose>
    				<c:when test="${configs.typeName=='消费特征'}"><span class="icon_xftz">${configs.typeName}</span></c:when>
    				<c:when test="${configs.typeName=='服务特征'}"><span class="icon_clock">${configs.typeName}</span></c:when>
    				<c:when test="${configs.typeName=='捆绑续约'}"><span class="icon_kbxy">${configs.typeName}</span></c:when>
    				<c:when test="${configs.typeName=='流量特征'}"><span class="icon_lltx">${configs.typeName}</span></c:when>
    				<c:when test="${configs.typeName=='终端特征'}"><span class="icon_zdtx">${configs.typeName}</span></c:when>
    				<c:when test="${configs.typeName=='生命周期'}"><span class="icon_smzq">${configs.typeName}</span></c:when>
    			</c:choose>
				</dt>
				<dd> 
                	<ul class="rwb_ul">
						<c:forEach items="${configs.displayLabelOrIndexList}" var="labelsAndIndexes">
							<li>
								<font title="${labelsAndIndexes.displayName}">${labelsAndIndexes.displayName}</font>
								<!--向下的箭头，暂时没有其他信息展示 <span>&nbsp;</span> -->
							</li>
						</c:forEach>
					</ul>
				</dd>
				
            	<dd>
		 	</dl> 					
		</c:forEach>	
    </div><!--rwb_left_ct end -->

