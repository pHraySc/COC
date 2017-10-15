<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_SCENE" value="<%=CommonConstants.TABLE_DIM_SCENE%>"></c:set>
<c:forEach var="item" items="${ciTemplateInfoHotList}" varStatus="status">
			  <div class="hotMasterPlateItemBox">
			     <div class="hotMasterPlateIntro">
				<c:if test="${item.sceneId == 1}">
			      <h5 class="title1" ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"><c:out value="${item.templateName}"></c:out></h5>
			     </c:if>
		        <c:if test="${item.sceneId == 2}">
			      <h5 class="title4" ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"><c:out value="${item.templateName}"></c:out></h5>
			     </c:if>
		        <c:if test="${item.sceneId == 3}">
			      <h5 class="title2" ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"><c:out value="${item.templateName}"></c:out></h5>
			     </c:if>
		        <c:if test="${item.sceneId == 4}">
			      <h5 class="title3" ondblclick="addToShoppingCar(event,this,3);" element="${item.templateId}"><c:out value="${item.templateName}"></c:out></h5>
			    </c:if>
			    </div>
				<div class="queryResultCharDate">
					  <ol>
					   <li><label>创建时间：</label><span><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
					    <li><label>模板分类：</label><span><c:out value="${dim:toName(DIM_SCENE,item.sceneId) }"></c:out></span></li>
					  </ol>
			    </div>
			   <div class="queryResultCharDesc">
				  <p>创建规则：<c:out value="${item.labelOptRuleShow}"></c:out></p>
				  <p>描述说明：<c:out value="${item.templateDesc}"></c:out></p>
			   </div>
			  </div>
			</c:forEach>