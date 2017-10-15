<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!-- ******************新的页面结构显示开始**********************  -->
<c:forEach var="firstTree" items="${secondLTreeList}" varStatus="firstIndex">
     <c:choose>
           <%--一共有四级标签的展示，depth=3表示展示：2、3、4 这三级的标签 --%>
	      <c:when test="${firstTree.depth == 3}">
		      <dl class="tagMapListBox">
		           <%--为2级标签的名称，不可能为空，不用做空考虑. --%> 
				   <dt><c:out value="${firstTree.ciLabelInfo.labelName}"></c:out></dt>
				   <%--标签子级详细内容 --%>
				   <dd>
		    	       <table>
		    	         <tbody>
		    	            <c:set var="firstNullFlag" value="false"></c:set>
		    	            <%--由于depth为3，所以肯定有三级标签 --%>
		    	            <c:forEach var="secondTree" items="${firstTree.ciLabelInfoTree}" varStatus="secondIndex">
		    	               <!-- 多遍历一边控制判断该级下是否有子元素  -->
							   <c:choose>
							      <c:when test="${fn:length(secondTree.ciLabelInfoTree) != 0}">
							       <tr>
									 <%--显示三级标签的名称 --%>
					            	<td class="firstTd">
					            	      <%--下面的几个div的作用是让选中标签后，标签的背景为圆角且背景大小根据标签名称的长度自适应 --%>
					            		  <a class="label_map_show"
					            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">id="isAttention_${secondTree.ciLabelInfo.labelId}"
					            		 	class="canDrag isCanDrag"  ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
					            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
					            		 	class="canDrag cannotDrag" </c:if>
					            		 	element="${secondTree.ciLabelInfo.labelId}" elementType="2" min="${secondTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
					            			max="${secondTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${secondTree.ciLabelInfo.labelTypeId}"
					            			labelName="${secondTree.ciLabelInfo.labelName}" effectDate="${secondTree.ciLabelInfo.effectDate}" 
					            			attrVal="${secondTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
					            			updateCycle="${secondTree.ciLabelInfo.updateCycle}"
					            			labelSceneNames="${secondTree.ciLabelInfo.labelSceneNames}"
<%-- 					            			dataDateStr="<fmt:formatDate value="${secondTree.ciLabelInfo.dataDateStr }"  type="date" dateStyle="default"/>" --%>
					            			dataDateStr="${secondTree.ciLabelInfo.dataDateStr}"
					            			customNum="${secondTree.ciLabelInfo.ciLabelExtInfo.customNum}"
					            			     href="javascript:void(0)"  title="${secondTree.ciLabelInfo.labelName}">
						            				<c:out value="${secondTree.ciLabelInfo.labelName}"></c:out>
						            			</a>
					            	</td>
					            	<td >
			    		                <ul class="tagMapList">
					                		<c:forEach var="thirdTree" items="${secondTree.ciLabelInfoTree}" varStatus="thirdIndex">
						                    	<li>
						                    		<a class="label_map_show" 
						                    		<c:if test="${thirdTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}"> id="isAttention_${thirdTree.ciLabelInfo.labelId}"
							            		 	class="isCanDrag" ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)"  cusNumFlag="0"</c:if>
						                    		element="${thirdTree.ciLabelInfo.labelId}" elementType="2" min="${thirdTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
						                    		max="${thirdTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdTree.ciLabelInfo.labelTypeId}"
						                    		labelName="${thirdTree.ciLabelInfo.labelName}" effectDate="${thirdTree.ciLabelInfo.effectDate}" 
						                    		attrVal="${thirdTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
						                    		updateCycle="${thirdTree.ciLabelInfo.updateCycle}"
						                    		labelSceneNames="${thirdTree.ciLabelInfo.labelSceneNames}"
						                    		dataDateStr="${thirdTree.ciLabelInfo.dataDateStr}"
						                    		customNum="${thirdTree.ciLabelInfo.ciLabelExtInfo.customNum}" href="javascript:void(0)" title="${thirdTree.ciLabelInfo.labelName}">
								                    	 <c:out value="${thirdTree.ciLabelInfo.labelName}"></c:out>
								                    </a>
						                    	</li>
					                        </c:forEach>
					                    </ul>
					                 </td>
					                </tr>
					                </c:when> 
					                <c:otherwise>
					                   <c:set var="firstNullFlag" value="true"></c:set>
					                </c:otherwise>
		                       </c:choose>
		    	            </c:forEach>
		    	            <c:if test="${firstNullFlag}"> 
		    	              <tr class="noChildItem lastId">
								 <%--显示三级标签的名称 --%>
						         <td colspan="2"  >
						           <ul class="tagMapList">
					    	            <%--由于depth为3，所以肯定有三级标签 --%>
					    	            <c:forEach var="secondTree" items="${firstTree.ciLabelInfoTree}" varStatus="secondIndex">
					    	               <!-- 多遍历一边控制判断该级下是否有子元素  -->
										   <c:choose>
										      <c:when test="${fn:length(secondTree.ciLabelInfoTree) == 0}">
										                <li>
									            	      <%--下面的几个div的作用是让选中标签后，标签的背景为圆角且背景大小根据标签名称的长度自适应 --%>
									            		  <a  class="label_map_show"
									            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}"> id="isAttention_${secondTree.ciLabelInfo.labelId}"
									            		 	class="canDrag isCanDrag"  ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
									            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
									            		 	class="canDrag cannotDrag" </c:if>
									            		 	element="${secondTree.ciLabelInfo.labelId}" elementType="2" min="${secondTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
									            			max="${secondTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${secondTree.ciLabelInfo.labelTypeId}"
									            			labelName="${secondTree.ciLabelInfo.labelName}" effectDate="${secondTree.ciLabelInfo.effectDate}" 
									            			attrVal="${secondTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
									            			updateCycle="${secondTree.ciLabelInfo.updateCycle}"
									            			labelSceneNames="${secondTree.ciLabelInfo.labelSceneNames}"
									            			customNum="${secondTree.ciLabelInfo.ciLabelExtInfo.customNum}"
									            			dataDateStr="${secondTree.ciLabelInfo.dataDateStr}"
									            			     href="javascript:void(0)"  title="${secondTree.ciLabelInfo.labelName}">
										            				<c:out value="${secondTree.ciLabelInfo.labelName}"></c:out>
										            			</a>
									                        </li>
								              </c:when> 
					                       </c:choose>
					    	              </c:forEach>
				    	            </ul>
		    	            	</td>
					           </tr>
		    	           </c:if>
		    	         </tbody>
		    	      </table>
		    	    </dd>
		      </dl>
		  </c:when>
		  <%--一共有三级标签的展示，depth=2表示展示：2、3这两级的标签，所以比较简单，不用做详细注释 --%>
	      <c:when test="${firstTree.depth == 2}">  
	          <dl class="tagMapListBox">
		            <%--为2级标签的名称，不可能为空，不用做空考虑. --%> 
				   <dt><c:out value="${firstTree.ciLabelInfo.labelName}"></c:out></dt>
				    <%--标签子级详细内容 --%>
				   <dd>
		    	       <table>
		    	         <tbody>
		    	            <tr class="lastId">
		    	              <td >
		    	              <ul class="tagMapList">
			    	            <%--由于depth为3，所以肯定有三级标签 --%>
			    	            <c:forEach var="secondTree" items="${firstTree.ciLabelInfoTree}" varStatus="secondIndex">
									<li>
		                    			<a  class="label_map_show"
		                    			<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}"> id="isAttention_${secondTree.ciLabelInfo.labelId}"
						            	class="isCanDrag" ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)"   cusNumFlag="0"</c:if>
		                    			element="${secondTree.ciLabelInfo.labelId}" elementType="2" min="${secondTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
		                    			max="${secondTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${secondTree.ciLabelInfo.labelTypeId}"
		                    			labelName="${secondTree.ciLabelInfo.labelName}" effectDate="${secondTree.ciLabelInfo.effectDate}" 
		                    			attrVal="${secondTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
		                    			updateCycle="${secondTree.ciLabelInfo.updateCycle}"
		                    			labelSceneNames="${secondTree.ciLabelInfo.labelSceneNames}"
		                    			dataDateStr="${secondTree.ciLabelInfo.dataDateStr}"
		                    			customNum="${secondTree.ciLabelInfo.ciLabelExtInfo.customNum}" href="javascript:void(0)" title="${secondTree.ciLabelInfo.labelName}">
				                    			<c:out value="${secondTree.ciLabelInfo.labelName}"></c:out>
				                    	</a>
		                    		</li>
			    	            </c:forEach>
		    	               </ul>
		    	               </td>
		    	              </tr>
		    	         </tbody>
		    	      </table>
		    	    </dd>
		      </dl>
	       </c:when> 
	       <%--一共有五级标签的展示，depth=4表示展示：2、3、4、5这四级的标签，比较麻烦，需要做详细注释 --%>
	       <c:when test="${firstTree.depth == 4}">
	           <dl class="tagMapListBox">
		            <%--为2级标签的名称，不可能为空，不用做空考虑. --%> 
				   <dt><c:out value="${firstTree.ciLabelInfo.labelName}"></c:out></dt>
				   <%--标签子级详细内容 --%>
				   <dd>
		    	       <table>
		    	         <tbody>
		    	            <c:set var="secondNullFlag" value="false"></c:set>
		    	            <%--由于depth为3，所以肯定有三级标签 --%>
		    	            <c:forEach var="secondTree" items="${firstTree.ciLabelInfoTree}" varStatus="secondIndex">
		    	               <c:choose>
							     <c:when test="${fn:length(secondTree.ciLabelInfoTree) != 0}">
							         <tr>
							           <td class="firstTd treeTitle">
							                 <a  class="label_map_show"
									               <c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">id="isAttention_${secondTree.ciLabelInfo.labelId}"
									            		 	class="canDrag isCanDrag"  ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
									            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
									            		 	class="canDrag cannotDrag" </c:if>
									            		 	element="${secondTree.ciLabelInfo.labelId}" elementType="2" min="${secondTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
									            			max="${secondTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${secondTree.ciLabelInfo.labelTypeId}"
									            			labelName="${secondTree.ciLabelInfo.labelName}" effectDate="${secondTree.ciLabelInfo.effectDate}" 
									            			attrVal="${secondTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
									            			updateCycle="${secondTree.ciLabelInfo.updateCycle}"
									            			labelSceneNames="${secondTree.ciLabelInfo.labelSceneNames}"
									            			dataDateStr="${secondTree.ciLabelInfo.dataDateStr}"
									            			customNum="${secondTree.ciLabelInfo.ciLabelExtInfo.customNum}"
									            			     href="javascript:void(0)"  title="${secondTree.ciLabelInfo.labelName}">
										            				<c:out value="${secondTree.ciLabelInfo.labelName}"></c:out>
										          	</a>
							           </td>
							           <td>
							                <table>
		    	                               <tbody>
		    	                                <c:set var="thirdNullFlag" value="false"></c:set>
								                <c:forEach  var="thirdTree" items="${secondTree.ciLabelInfoTree}" varStatus="thirdIndex">
								                   <c:choose>
							                           <c:when test="${fn:length(thirdTree.ciLabelInfoTree) != 0}">
							                                <tr>
							                                  <td class="firstTd">
							                                        <a  class="label_map_show"
												            		 	<c:if test="${thirdTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">id="isAttention_${thirdTree.ciLabelInfo.labelId}"
												            		 	class="canDrag isCanDrag"  ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
												            		 	<c:if test="${thirdTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
												            		 	class="canDrag cannotDrag" </c:if>
												            		 	element="${thirdTree.ciLabelInfo.labelId}" elementType="2" min="${thirdTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
												            			max="${thirdTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdTree.ciLabelInfo.labelTypeId}"
												            			labelName="${thirdTree.ciLabelInfo.labelName}" effectDate="${thirdTree.ciLabelInfo.effectDate}" 
												            			attrVal="${thirdTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
												            			updateCycle="${thirdTree.ciLabelInfo.updateCycle}"
												            			labelSceneNames="${thirdTree.ciLabelInfo.labelSceneNames}"
												            			dataDateStr="${thirdTree.ciLabelInfo.dataDateStr}"
												            			customNum="${thirdTree.ciLabelInfo.ciLabelExtInfo.customNum}"
												            			     href="javascript:void(0)"  title="${thirdTree.ciLabelInfo.labelName}">
													            				<c:out value="${thirdTree.ciLabelInfo.labelName}"></c:out>
													            			</a>
							                                  </td>
							                                  <td>
							                                   <ul class="tagMapList">
										                		<c:forEach var="fourthTree" items="${thirdTree.ciLabelInfoTree}" varStatus="fourthIndex">
										                    		<li>
										                    			<a class="label_map_show"
										                    			<c:if test="${fourthTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">id="isAttention_${fourthTree.ciLabelInfo.labelId}"
											            		 		class="isCanDrag" ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)"  cusNumFlag="0"</c:if>
										                    			element="${fourthTree.ciLabelInfo.labelId}" elementType="2" min="${fourthTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
										                    			max="${fourthTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${fourthTree.ciLabelInfo.labelTypeId}"
										                    			labelName="${fourthTree.ciLabelInfo.labelName}" effectDate="${fourthTree.ciLabelInfo.effectDate}" 
										                    			attrVal="${fourthTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
										                    			updateCycle="${fourthTree.ciLabelInfo.updateCycle}"
										                    			labelSceneNames="${fourthTree.ciLabelInfo.labelSceneNames}"
										                    			dataDateStr="${fourthTree.ciLabelInfo.dataDateStr}"
										                    			customNum="${fourthTree.ciLabelInfo.ciLabelExtInfo.customNum}" href="javascript:void(0)" title="${fourthTree.ciLabelInfo.labelName}">
											                    				<c:out value="${fourthTree.ciLabelInfo.labelName}"></c:out>
											                    			</a>
										                    		</li>
										                        </c:forEach>
										                  </ul>
							                                  </td>
							                                  
							                                </tr>
							                           </c:when>
							                           <c:otherwise>
										                   <c:set var="thirdNullFlag" value="true"></c:set>
										             </c:otherwise>
							                           </c:choose>
								                </c:forEach>
								                    <c:if test="${thirdNullFlag}"> 
								    	              <tr class="noChildItem lastId">
														 <%--显示三级标签的名称 --%>
												         <td colspan="2"  >
												           <ul class="tagMapList">
											    	            <%--由于depth为3，所以肯定有三级标签 --%>
											    	            <c:forEach var="thirdTree" items="${secondTree.ciLabelInfoTree}" varStatus="thirdIndex">
											    	               <!-- 多遍历一边控制判断该级下是否有子元素  -->
																   <c:choose>
																      <c:when test="${fn:length(thirdTree.ciLabelInfoTree) == 0}">
																                <li>
															            	      <%--下面的几个div的作用是让选中标签后，标签的背景为圆角且背景大小根据标签名称的长度自适应 --%>
															            		  <a  class="label_map_show"
															            		 	<c:if test="${thirdTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">id="isAttention_${thirdTree.ciLabelInfo.labelId}"
															            		 	class="canDrag isCanDrag"  ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
															            		 	<c:if test="${thirdTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
															            		 	class="canDrag cannotDrag" </c:if>
															            		 	element="${thirdTree.ciLabelInfo.labelId}" elementType="2" min="${thirdTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
															            			max="${thirdTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdTree.ciLabelInfo.labelTypeId}"
															            			labelName="${thirdTree.ciLabelInfo.labelName}" effectDate="${thirdTree.ciLabelInfo.effectDate}" 
															            			attrVal="${thirdTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
															            			updateCycle="${thirdTree.ciLabelInfo.updateCycle}"
															            			labelSceneNames="${thirdTree.ciLabelInfo.labelSceneNames}"
															            			dataDateStr="${thirdTree.ciLabelInfo.dataDateStr}"
															            			customNum="${thirdTree.ciLabelInfo.ciLabelExtInfo.customNum}"
															            			     href="javascript:void(0)"  title="${thirdTree.ciLabelInfo.labelName}">
																            				<c:out value="${thirdTree.ciLabelInfo.labelName}"></c:out>
																            			</a>
															                        </li>
														              </c:when> 
											                       </c:choose>
											    	              </c:forEach>
										    	            </ul>
								    	            	</td>
											           </tr>
								    	           </c:if>
							                   </tbody>
							                </table>
							            </td>
							         </tr>
							         
							     </c:when>
					        	 <c:otherwise>
					                   <c:set var="secondNullFlag" value="true"></c:set>
					             </c:otherwise>
								</c:choose>
		    	            </c:forEach>
		    	            <c:if test="${secondNullFlag}"> 
		    	              <tr class="noChildItem lastId">
								 <%--显示三级标签的名称 --%>
						         <td colspan="2"  >
						           <ul class="tagMapList">
					    	            <%--由于depth为3，所以肯定有三级标签 --%>
					    	            <c:forEach var="secondTree" items="${firstTree.ciLabelInfoTree}" varStatus="secondIndex">
					    	               <!-- 多遍历一边控制判断该级下是否有子元素  -->
										   <c:choose>
										      <c:when test="${fn:length(secondTree.ciLabelInfoTree) == 0}">
										                <li>
									            	      <%--下面的几个div的作用是让选中标签后，标签的背景为圆角且背景大小根据标签名称的长度自适应 --%>
									            		  <a  class="label_map_show"
									            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">id="isAttention_${secondTree.ciLabelInfo.labelId}"
									            		 	class="canDrag isCanDrag"  ondblclick="addToShoppingCar(event,this,1);" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
									            		 	<c:if test="${secondTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
									            		 	class="canDrag cannotDrag" </c:if>
									            		 	element="${secondTree.ciLabelInfo.labelId}" elementType="2" min="${secondTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
									            			max="${secondTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${secondTree.ciLabelInfo.labelTypeId}"
									            			labelName="${secondTree.ciLabelInfo.labelName}" effectDate="${secondTree.ciLabelInfo.effectDate}" 
									            			attrVal="${secondTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
									            			updateCycle="${secondTree.ciLabelInfo.updateCycle}"
									            			labelSceneNames="${secondTree.ciLabelInfo.labelSceneNames}"
									            			dataDateStr="${secondTree.ciLabelInfo.dataDateStr}"
									            			customNum="${secondTree.ciLabelInfo.ciLabelExtInfo.customNum}"
									            			     href="javascript:void(0)"  title="${secondTree.ciLabelInfo.labelName}">
										            				<c:out value="${secondTree.ciLabelInfo.labelName}"></c:out>
										            			</a>
									                        </li>
								              </c:when> 
					                       </c:choose>
					    	              </c:forEach>
				    	            </ul>
		    	            	</td>
					           </tr>
		    	           </c:if>
		    	         </tbody>
		    	      </table>
		    	    </dd>
		       </dl>
	       </c:when>
	 </c:choose>
</c:forEach>
<!-- *************************新的页面结构显示结束 ************************** -->
<script type="text/javascript">
$(function(){
	$(document).click(function(){
		$("#tag_tips .onshow").hide().removeClass("onshow");
		$("#tag_tips").hide();
		$(".tagChoosed").removeClass("tagChoosed");
	});
});
</script>	
