<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_SCENE" value="<%=CommonConstants.TABLE_DIM_SCENE%>"></c:set>
<c:forEach var="item" items="${ciTemplateInfoList}" varStatus="status">
		        <div class="masterPlateIntro">
			     <c:if test="${item.sceneId == 1}">
			      <h5 class="title1" <c:if test="${item.status != 2}">ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"</c:if> > <c:out value="${item.templateName}"></c:out></h5>
			     </c:if>
		        <c:if test="${item.sceneId == 2}">
			      <h5 class="title4" <c:if test="${item.status != 2}"> ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"</c:if>> <c:out value="${item.templateName}"></c:out></h5>
			     </c:if>
		        <c:if test="${item.sceneId == 3}">
			      <h5 class="title2" <c:if test="${item.status != 2}"> ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"</c:if>> <c:out value="${item.templateName}"></c:out></h5>
			     </c:if>
		        <c:if test="${item.sceneId == 4}">
			      <h5 class="title3" <c:if test="${item.status != 2}"> ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"</c:if>> <c:out value="${item.templateName}"></c:out></h5>
			    </c:if>
			      <div class="fright resultItemActive" onclick="showOperList(event,this,'${item.templateId}');"> <span>操作</span>
				   <div class="resultItemActiveList" id="${item.templateId}">
					  <ol>
					  <c:if test="${item.status == 1}">
							<c:if test="${isAdmin || item.userId == userId}">
								<!-- <li><a href="javascript:addShopCart('${item.templateId}','3','0');"> 修改</a></li> -->
								<li><a href="javascript:preEdit('${item.templateId}');"> 修改</a></li>
								<li><a href="javascript:toDel('${item.templateId}');"> 删除</a></li>
								<c:if test="${item.isPrivate !=0}">
								<li id="share_${item.templateId}" t_name="${item.templateName}"><a href="javascript:toShare('${item.templateId}');"> 共享</a></li>
								</c:if>
							 </c:if>
							 <c:if test="${isAdmin || item.userId == userId || item.isPrivate == 0}">
								<li><a href="javascript:addShopCart('${item.templateId}','3','0');"> 添加</a></li>
							 </c:if>
					  </c:if>
					  <c:if test="${item.status == 2}">
					  <c:if test="${isAdmin || item.userId == userId}">
					  <li class="end"><a href="javascript:toDel('${item.templateId}');"> 删除</a></li>
					  </c:if>
					  </c:if>
					  </ol>
				   </div>
				</div>
			  </div>
			  <div class="queryResultCharDate">
					  <ol>
					   <li><label>创建时间：</label><span><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
					   <li><label>创建人：</label><span><c:out value="${item.userName}"></c:out></span></li>
					   <li><label>模板分类：</label><span>
					    <c:if test="${item.sceneId != null && item.sceneId!='' }">
		               <c:out value="${dim:toName(DIM_SCENE,item.sceneId) }"></c:out>
		               </c:if>
					   </span></li>
					   <li><label>模板状态：</label><span>
					   <c:if test="${item.status == 1}">有效</c:if>
				       <c:if test="${item.status == 2}">失效</c:if>
					   </span></li>
					   <li><label>是否共享：</label><span><c:if test="${item.isPrivate == 0}">公有</c:if>
			                     <c:if test="${item.isPrivate == 1}">私有</c:if>
			            </span></li>
					  </ol>
				</div>
			   <div class="masterPlateSegLine"></div>
			   <div class="queryResultCharDesc">
				  <p>创建规则：<c:out value="${item.labelOptRuleShow}"></c:out></p>
				  <p>描述说明：<c:out value="${item.templateDesc}"></c:out></p>
			   </div>
</c:forEach> 
