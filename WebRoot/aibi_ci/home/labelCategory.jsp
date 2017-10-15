<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String labelCategoryType = Configure.getInstance().getProperty("LABEL_CATEGORY_TYPE");
%>
<c:set var="labelCategoryType" value="<%=labelCategoryType%>"></c:set>
<c:if test="${labelCategoryType == 3 }">
	 <c:forEach var="c1TreeList" items="${categoryTreeList}" varStatus="statusL1">
         <div class="tagNavItem">
		      <h4 class="cascade0${statusL1.count % 10 }"><a c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' onclick='forwardShowAllLabelMap(this,1)' href='javascript:void(0);'><c:out value="${c1TreeList.ciLabelInfo.labelName}"></c:out></a></h4>
			  <div class="secondTagNavListBox">
                  <div class="fleft tagLeftBox">
                  <c:forEach var="c2TreeList_1" items="${c1TreeList.ciLabelInfoTree}" varStatus="statusL2_1">
                    	<c:if test="${statusL2_1.count % 2 == 1 }">
						<dl>
						<dt style="cursor: pointer;" c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_1.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_1.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,2)" ><c:out value="${c2TreeList_1.ciLabelInfo.labelName}"></c:out></dt>
						<dd>
							<c:forEach var="c3TreeList_1" items="${c2TreeList_1.ciLabelInfoTree}" varStatus="statusL3_1">
							<em><a c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_1.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_1.ciLabelInfo.labelId }' c3LabelName='${c3TreeList_1.ciLabelInfo.labelName }' c3LabelId='${c3TreeList_1.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,3)" href='javascript:void(0);'><c:out value="${c3TreeList_1.ciLabelInfo.labelName}"></c:out></a></em>
							</c:forEach>
						</dd>
						</dl>
					</c:if>
			     </c:forEach>
				</div>
				<div class="fleft ">
				<c:forEach var="c2TreeList_2" items="${c1TreeList.ciLabelInfoTree}" varStatus="statusL2_2">
				<c:if test="${statusL2_2.count % 2 == 0 }">
					<dl >
						<dt style="cursor: pointer;" c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_2.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_2.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,2)" ><c:out value="${c2TreeList_2.ciLabelInfo.labelName}"></c:out></dt>
						<dd>
							<c:forEach var="c3TreeList_2" items="${c2TreeList_2.ciLabelInfoTree}" varStatus="statusL3">
							<em><a c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_2.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_2.ciLabelInfo.labelId }' c3LabelName='${c3TreeList_2.ciLabelInfo.labelName }' c3LabelId='${c3TreeList_2.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,3)" href='javascript:void(0);'><c:out value="${c3TreeList_2.ciLabelInfo.labelName}"></c:out></a></em>
							</c:forEach>
						</dd>
					</dl>
				</c:if>
				</c:forEach>
				</div>
			  </div>
		 </div>
		 <c:if test="${!statusL1.last}" ><div class="tagNavItemSegLine "/></c:if>
	</c:forEach>
</c:if>
<c:if test="${labelCategoryType == 2 }">
   <div class="tagNavItem tagNavItemOther">
	  <c:forEach var="c1TreeList" items="${categoryTreeList}" varStatus="statusL1">
         <c:if test="${statusL1.count % 2 == 1 }">
         	<div class="secondTagNavListBox secondTagNavListBoxOther">
         </c:if>
         <c:if test="${statusL1.count % 2 == 1 }">
         	 <div class="fleft  tagLeftBox tagleftContent ${statusL1.last ? "none-border" : ""} }">
				<dl>
					<dt style="cursor: pointer;" c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_1.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_1.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,1)" ><c:out value="${c1TreeList.ciLabelInfo.labelName}"></c:out></dt>
					<dd>
						<c:forEach var="c2TreeList_1" items="${c1TreeList.ciLabelInfoTree}" varStatus="statusL2_1">
						<em><a c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_1.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_1.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,2)" href='javascript:void(0);'><c:out value="${c2TreeList_1.ciLabelInfo.labelName}"></c:out></a></em>
						</c:forEach>
					</dd>
				</dl>
			</div>
		</c:if>
		<c:if test="${statusL1.count % 2 == 0 }">
			<div class="fleft tagRightBox">
				<dl>
					<dt style="cursor: pointer;" c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_2.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_2.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,1)" ><c:out value="${c1TreeList.ciLabelInfo.labelName}"></c:out></dt>
					<dd>
						<c:forEach var="c2TreeList_2" items="${c1TreeList.ciLabelInfoTree}" varStatus="statusL2_2">
						<em><a c1LabelName='${c1TreeList.ciLabelInfo.labelName }' c1LabelId='${c1TreeList.ciLabelInfo.labelId }' c2LabelName='${c2TreeList_2.ciLabelInfo.labelName }' c2LabelId='${c2TreeList_2.ciLabelInfo.labelId }' onclick="forwardShowAllLabelMap(this,2)" href='javascript:void(0);'><c:out value="${c2TreeList_2.ciLabelInfo.labelName}"></c:out></a></em>
						</c:forEach>
					</dd>
				</dl>
			</div>
	  </c:if>   
	  <c:if test="${statusL1.count % 2 == 0 || statusL1.last}">
	  		</div>
		 	<c:if test="${!statusL1.last}" ><div class="tagNavItemSegLine "/></c:if> 
	  </c:if> 
	</c:forEach>
 </div>
</c:if>


         

