<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="LABEL_TYPE_SIGN" value="<%=ServiceConstants.LABEL_TYPE_SIGN%>"></c:set>
<script type="text/javascript">
//查询以选择条件 
 $(".selectedItemBox").hover(function(){
	 $(this).addClass("selectedItemBoxHover");
 },function(){
	 $(this).removeClass("selectedItemBoxHover");
 })
 $(".collectStar").hover(function(){
	 var labelId = $(this).attr("labelId");
	 var isAttention = $("#isAttention_"+labelId).val();
	 if(isAttention == 'true'){//已关注的标签
		 $("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
	 }else{//未关注的标签
		 $("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
	 }
 },function(){
	 var labelId = $(this).attr("labelId");
	 var isAttention = $("#isAttention_"+labelId).val();
	 if(isAttention == 'true'){//已关注的标签
		 $("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
	 }else{//未关注的标签
		 $("#attentionImg_"+labelId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
	 }
 });
 </script>
<c:forEach items="${labelDetailList}" var="label">
	<div class="queryResultItemBox"> 
		<div class="queryResultCharTop queryTagIntro">
			<ol class="hotState">
			<c:if test="${label.isSysRecom!=null && label.isSysRecom==1 }"><li><span class="organ"/></li></c:if>
			<c:if test="${label.isHot=='true' }"><li><span class="hot"/></li></c:if>
			<li <c:if test="${label.isAttention=='true' }">style="display: block"</c:if><c:if test="${label.isAttention=='false' }">style="display: none"</c:if> id="isAttentionShow_${label.labelId}"><span  class="hideFont"/></li>
			</ol>
			<h4 style="cursor: pointer;" ondblclick="addToShoppingCar(event,this,1);" element="${label.labelId}"> 
	        ${label.labelName}</h4>
	        <input type="hidden" id="isAttention_${label.labelId}" value="${label.isAttention}" />
			<c:if  test="${label.isAttention == 'false'}">
				<span  class="collectStar" isAttention="${label.isAttention}" labelId="${label.labelId}"><img style="cursor:pointer" title="点击增加标签收藏" onclick="attentionLabelOper(${label.labelId})" id="attentionImg_${label.labelId }" src="${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png" /></span>
			</c:if>
          	<c:if  test="${label.isAttention == 'true'}">
		   		<span  class="collectStar" isAttention="${label.isAttention}" labelId="${label.labelId}"><img style="cursor:pointer" title="点击取消标签收藏" onclick="attentionLabelOper(${label.labelId})") id="attentionImg_${label.labelId }" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png" /></span>
          	</c:if>
			<div class="fright resultItemActive"  onclick="showOperLabelList(event,this,'${label.labelId}')"> <span>操作</span>
		   	<div class="resultItemActiveList" id="${label.labelId}">
			  	<ol>
			  	<c:choose>
                  	<c:when test="${label.isInPrilabel== 1}">
              		<ul>
                    	<li style="display:none"><a onclick="openLabelAnalysis(${label.labelId})" href="javascript:void(0);">标签分析</a></li>
                    	<li style="display:none"><a onclick="openLabelDifferential(${label.labelId});" href="javascript:void(0);">标签微分</a></li>
              	  		<c:if test="${label.updateCycle== 2}">
                       	<li style="display:none"><a href="javascript:void(0);" onclick="openLabelRel(${label.labelId});">关联分析</a></li>
                       	<li style="display:none"><a href="javascript:void(0);" onclick="openLabelContrast(${label.labelId});">对比分析</a></li>
           				</c:if>
           				<c:if test="${isAdmin && (empty label.isSysRecom || label.isSysRecom == 0)}">
						<li><a href="javascript:void(0);" labelId="${label.labelId}" onclick="changeRecomLabel(this)" >推荐</a></li>
						</c:if>
						<c:if test="${isAdmin && (!empty label.isSysRecom && label.isSysRecom == 1)}">
						<li><a href="javascript:void(0);" labelId="${label.labelId}" onclick="changeRecomLabel(this)">取消推荐</a></li>
						</c:if>
           	  			<li><a href="javascript:addShopCart('${label.labelId}','1','0');">添加到收纳篮</a></li>
                  		</ul>
					</c:when>
              	</c:choose>
				</ol>
			</div>
			</div>
		</div>
	   <div class="queryResultCharDate">
			  <ol>
				<c:if test="${label.labelSceneNames != null &&  label.labelSceneNames != ''}">
					<li><span title="标签分类" style="cursor: pointer;">${label.labelSceneNames }</span></li><li class="separate">&nbsp;</li>
				</c:if>
				<li><span title="更新周期" style="cursor: pointer;">
				<c:if test="${label.updateCycle == 1}">日周期</c:if>
				<c:if test="${label.updateCycle == 2}">月周期</c:if>
				</span></li>
				<c:if test="${label.labelTypeId == LABEL_TYPE_SIGN  && label.customNum != null &&  label.customNum != ''}">
				<li class="separate">&nbsp;</li><li><span title="用户数" style="cursor: pointer;"><fmt:formatNumber  type="number" value="${label.customNum }" pattern="###,###,###"></fmt:formatNumber></span></li>
				</c:if>
				<c:if test="${label.dataDate != null &&  label.dataDate != ''}">
				<li class="separate">&nbsp;</li><li><span title="数据日期" style="cursor: pointer;">${dateFmt:dateFormat(label.dataDate)}
				</span></li>
				</c:if>
			  </ol>
		</div>
	   <div class="tagResultListSegLine"></div>
	   <div class="queryResultCharDesc">
		  <p>业务口径：${label.busiCaliber}</p>
		  <p>标签路径：${label.currentLabelPath}</p>
	   </div>
	   <div class="queryResultSegLine"></div>
	</div>
</c:forEach>    

