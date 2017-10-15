<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<c:forEach var="secondCateAndProd" items="${secondCateAndProdList}" varStatus="status1">
<c:choose>
    <%--一共有两级标签的展示，depth=2表示展示：第二层的类别，以及1、2这两级产品 --%>
	<c:when test="${secondCateAndProd.productTreeDepth == 2}">
		<dl class="tag_List">
		    <%--为第二层的类别的名称，不可能为空，不用做空考虑. --%>
			<dt><span><c:out value="${secondCateAndProd.category.categoryName}"></c:out></span></dt>
		    <dd>
		    	<table class="commonTable" cellpadding="0" cellspacing="0">
		    	    <%--由于depth为2，所以肯定有第一级的产品 --%>
		    	    <c:forEach var="firstLTree" items="${secondCateAndProd.productTrees}" varStatus="status2">
		        	<c:choose>
		        		<%--最后一个一级产品的样式特殊，最后一个tr是不要下划线的，所以增加了个class--%>
						<c:when test="${status2.index+1 == fn:length(secondCateAndProd.productTrees)}">
							<tr class="last">
						</c:when>  
		    		    <c:otherwise>
							<tr>
		   				</c:otherwise>
					</c:choose>
		        	    <%--显示一级产品的名称 --%>
		            	<th>
		            		<span 
			            		<c:if test="${firstLTree.ciProductInfo.isProduct == 1}">
			            			class="canDrag"  onclick="showTip(event,this)"
			            		</c:if>		            		
		            			element="${firstLTree.ciProductInfo.productId}" elementType="4" min="0" 
		            			max="1" type="" labelName="${firstLTree.ciProductInfo.productName}" effectDate="${firstLTree.ciProductInfo.effectDate}"
		            			productMean="${firstLTree.ciProductInfo.productMean}" brandNames="${firstLTree.ciProductInfo.brandNames}" 
		            			descTxt="${firstLTree.ciProductInfo.descTxt}" busiCaliber="${firstLTree.ciProductInfo.busiCaliber}"
		            			effectPeriod="${firstLTree.ciProductInfo.effectPeriod}">
		            			<a href="javascript:void(0)" title="${firstLTree.ciProductInfo.productName}">
		            				<c:out value="${firstLTree.ciProductInfo.productName}"></c:out>
		            			</a>	
		            		</span>
		            	</th>
		            	<%--显示二级产品的名称，当然有可能没有二级产品 --%>
		                
			                <c:choose>
			                       <%--由于IE6和IE7当td为空时，样式不起作用，所以当没有二级产品时需要特殊处理 --%>
			                       <c:when test="${fn:length(firstLTree.ciProductInfoTree) == 0}">
			                       	<td class="nodata">
			                       		&nbsp;
			                       	</td>	
			                       </c:when>
			                       <%--当二级产品有数据时，当然显示二级产品 --%>
			                       <c:otherwise>
			                      	 <td>
					                	<ul class="tag_List_ul">
					                		<c:forEach var="secondLTree" items="${firstLTree.ciProductInfoTree}" varStatus="status2">
						                    	<li
						                    	<c:choose>
						                    		<c:when test="${secondLTree.ciProductInfo.isProduct == 1}">
						                    			class="canDrag" onclick="showTip(event,this)"
						                    		</c:when>
						                    	</c:choose>
						                    		element="${secondLTree.ciProductInfo.productId}" elementType="4" min="0" 
					            					max="1" type="" labelName="${secondLTree.ciProductInfo.productName}" effectDate="${secondLTree.ciProductInfo.effectDate}"
					            					productMean="${secondLTree.ciProductInfo.productMean}" brandNames="${secondLTree.ciProductInfo.brandNames}" 
		            								descTxt="${secondLTree.ciProductInfo.descTxt}" busiCaliber="${secondLTree.ciProductInfo.busiCaliber}"
		            								effectPeriod="${secondLTree.ciProductInfo.effectPeriod}">
						                    		<a href="javascript:void(0)" title="${secondLTree.ciProductInfo.productName}">
						                    			<c:out value="${secondLTree.ciProductInfo.productName}"></c:out>
						                    		</a>
						                    		<span></span>
						                    	</li>
					                        </c:forEach>
					                    </ul>
					                    </td>
					                </c:otherwise>
		                    </c:choose>
		            </tr>
		            </c:forEach>
		        </table>
		    </dd>
		</dl><!--tag_list -->
	</c:when>
	
	<%--一共有一级产品的展示，depth=1表示展示：第二级的类别和第一级产品，所以比较简单，不用做详细注释 --%>
	<c:when test="${secondCateAndProd.productTreeDepth == 1}">
		<dl class="tag_List">
			<dt><span><c:out value="${secondCateAndProd.category.categoryName}"></c:out></span></dt>
		    <dd>
		    	<table class="commonTable" cellpadding="0" cellspacing="0">
						<tr class="last">
		                <td>
		                	<ul class="tag_List_ul">
		                		<c:forEach var="firstLTree" items="${secondCateAndProd.productTrees}" varStatus="status2">
		                    		<li 
		                    		<c:choose>
			                    		<c:when test="${firstLTree.ciProductInfo.isProduct == 1}">
			                    			class="canDrag" onclick="showTip(event,this)"
			                    		</c:when>
			                    	</c:choose>
		                    			element="${firstLTree.ciProductInfo.productId}" elementType="4" min="0" 
		            					max="1" type="" labelName="${firstLTree.ciProductInfo.productName}" effectDate="${firstLTree.ciProductInfo.effectDate}"
		            					productMean="${firstLTree.ciProductInfo.productMean}" brandNames="${firstLTree.ciProductInfo.brandNames}" 
		            					descTxt="${firstLTree.ciProductInfo.descTxt}" busiCaliber="${firstLTree.ciProductInfo.busiCaliber}"
		            					effectPeriod="${firstLTree.ciProductInfo.effectPeriod}">
			                    		<a href="javascript:void(0)" title="${firstLTree.ciProductInfo.productName}">
			                    			<c:out value="${firstLTree.ciProductInfo.productName}"></c:out>
			                    		</a>
			                    		<span></span>
		                    		</li>
		                        </c:forEach>
		                    </ul>
		                </td>
		            </tr>
		        </table>
		    </dd>
		</dl><!--tag_list -->
	</c:when>
	
	<%--一共有三级产品的展示，depth=3表示展示：第二级别的类别和1、2、3级别的产品，比较麻烦，需要做详细注释 --%>
	<c:when test="${secondCateAndProd.productTreeDepth == 3}">
		<dl class="tag_List">
		    <%--展示第二类别的名称 --%>
			<dt><span><c:out value="${secondCateAndProd.category.categoryName}"></c:out></span></dt>
			<%--将每个第一级别产品及其下面的二级产品和三级产品做为一个table，有多少个第二级别的产品类别就有多少个table，
			第一级别产品的list肯定不为空 --%>
			<c:forEach var="firstLTree" items="${secondCateAndProd.productTrees}" varStatus="status2">
			    
			    <c:choose>
    				<c:when test="${status2.index+1 == fn:length(secondCateAndProd.productTrees)}">
    					<dd class="last">
    				</c:when>
    				<c:otherwise>
    					<dd>
    				</c:otherwise>
    			</c:choose>
			    
			    	<table class="commonTable" cellpadding="0" cellspacing="0">
			    	
			    	<c:choose>
			    		<%--一级产品下当没有二级产品，但是仍然需要显示这个一级产品的名称 --%>
			    		
			    		<c:when test="${fn:length(firstLTree.ciProductInfoTree) == 0}">
			    		    <%--还需要判断该一级产品是否为最后一个一级产品，如果是的话，就要增加样式：last --%>
			    			<c:choose>
			    				<c:when test="${status2.index+1 == fn:length(secondCateAndProd.productTrees)}">
			    					<tr class="last">
			    				</c:when>
			    				<c:otherwise>
			    					<tr>
			    				</c:otherwise>
			    			</c:choose>			    			
			    				<th class="floatTitle">
									<span 
									<c:if test="${firstLTree.ciProductInfo.isProduct == 1}">
				            			class="canDrag" onclick="showTip(event,this)"
				            		</c:if>	
										element="${firstLTree.ciProductInfo.productId}" elementType="4" min="0" 
		            					max="1" type="" labelName="${firstLTree.ciProductInfo.productName}" effectDate="${firstLTree.ciProductInfo.effectDate}"
		            					productMean="${firstLTree.ciProductInfo.productMean}" brandNames="${firstLTree.ciProductInfo.brandNames}" 
		            					descTxt="${firstLTree.ciProductInfo.descTxt}" busiCaliber="${firstLTree.ciProductInfo.busiCaliber}"
		            					effectPeriod="${firstLTree.ciProductInfo.effectPeriod}">
		            					<a href="javascript:void(0)" title="${firstLTree.ciProductInfo.productName}">
											<c:out value="${firstLTree.ciProductInfo.productName}"></c:out>
										</a>
									</span>			
								</th>
								<th class="nodata">
									&nbsp;
								</th>
								<td class="nodata">
									&nbsp;
								</td>
			    			</tr>
			    		</c:when>
			    		<%--当一级产品下有二级产品时 --%>
			    		<c:otherwise>
					    	<c:forEach var="secondLTree" items="${firstLTree.ciProductInfoTree}" varStatus="status3">
					        	<c:choose>
					        	    <%--找到最后一个一级产品和每个一级产品下的最后一个二级产品，设置样式为last --%>
									<c:when test="${(status3.index+1 == fn:length(firstLTree.ciProductInfoTree))}">
										<tr class="last">
									</c:when>
					    		    <c:otherwise>
										<tr>
					   				</c:otherwise>
								</c:choose>
								    <c:choose>
								    <%--有二级产品时，一级产品所占的列数要根据该一级产品下包含的二级产品个数确定--%>
								    <%--显示第一级产品的名称 --%>
									<c:when test="${status3.index == 0}">
										<th rowspan="${fn:length(firstLTree.ciProductInfoTree)}" class="floatTitle">
											<span 
											<c:if test="${firstLTree.ciProductInfo.isProduct == 1}">
						            			class="canDrag" onclick="showTip(event,this)"
						            		</c:if>	 
												element="${firstLTree.ciProductInfo.productId}" elementType="4" min="0" 
				            					max="1" type="" labelName="${firstLTree.ciProductInfo.productName}" effectDate="${firstLTree.ciProductInfo.effectDate}"
				            					productMean="${firstLTree.ciProductInfo.productMean}" brandNames="${firstLTree.ciProductInfo.brandNames}" 
		            							descTxt="${firstLTree.ciProductInfo.descTxt}" busiCaliber="${firstLTree.ciProductInfo.busiCaliber}"
		            							effectPeriod="${firstLTree.ciProductInfo.effectPeriod}">
				            					<a href="javascript:void(0)" title="${firstLTree.ciProductInfo.productName}">
													<c:out value="${firstLTree.ciProductInfo.productName}"></c:out>
												</a>
											</span>			
										</th>
									</c:when>  
					    		    <c:otherwise>
										
					   				</c:otherwise>
								    </c:choose>
									<%--显示第二级产品的名称 --%>
					            	<th>
					            		<span 
					            		<c:if test="${secondLTree.ciProductInfo.isProduct == 1}">
					            			class="canDrag" onclick="showTip(event,this)"
					            		</c:if>	
					            			element="${secondLTree.ciProductInfo.productId}" elementType="4" min="0" 
				            				max="1" type="" labelName="${secondLTree.ciProductInfo.productName}" effectDate="${secondLTree.ciProductInfo.effectDate}"
				            				productMean="${secondLTree.ciProductInfo.productMean}" brandNames="${secondLTree.ciProductInfo.brandNames}" 
		            						descTxt="${secondLTree.ciProductInfo.descTxt}" busiCaliber="${secondLTree.ciProductInfo.busiCaliber}"
		            						effectPeriod="${secondLTree.ciProductInfo.effectPeriod}">
				            				<a href="javascript:void(0)" title="${secondLTree.ciProductInfo.productName}">
					            				<c:out value="${secondLTree.ciProductInfo.productName}"></c:out>
					            			</a>
					            		</span>		
					            	</th>
					            	<%--显示第三级产品的名称 --%>
					                
					                   <c:choose>
					                       <%--由于IE6和IE7当td为空时，样式不起作用，所以当没有三级产品时需要特殊处理 --%>
					                       <c:when test="${fn:length(secondLTree.ciProductInfoTree) == 0}">
					                         <td class="nodata">
					                       		&nbsp;
					                       	 </td>
					                       </c:when>
					                       <%--当三级产品有数据时，当然显示三级产品 --%>
			                       		   <c:otherwise>
			                       		     <td>
				                       		   <ul class="tag_List_ul">
							                		<c:forEach var="thirdLTree" items="${secondLTree.ciProductInfoTree}" varStatus="status5">
							                    		<li
							                    		<c:choose>
								                    		<c:when test="${thirdLTree.ciProductInfo.isProduct == 1}">
								                    			class="canDrag" onclick="showTip(event,this)"
								                    		</c:when>
								                    	</c:choose>  
							                    			element="${thirdLTree.ciProductInfo.productId}" elementType="4" min="0"
						            						max="1" type="" labelName="${thirdLTree.ciProductInfo.productName}" effectDate="${thirdLTree.ciProductInfo.effectDate}"
						            						productMean="${thirdLTree.ciProductInfo.productMean}" brandNames="${thirdLTree.ciProductInfo.brandNames}" 
		            										descTxt="${thirdLTree.ciProductInfo.descTxt}" busiCaliber="${thirdLTree.ciProductInfo.busiCaliber}"
		            										effectPeriod="${thirdLTree.ciProductInfo.effectPeriod}">
							                    			<div class="tag_List_inner">
												                <div class="bg_l"></div>
								                    			<a href="javascript:void(0)" title="${thirdLTree.ciProductInfo.productName}">
								                    				<font><c:out value="${thirdLTree.ciProductInfo.productName}"></c:out></font>
								                    			</a>
								                    			<div class="bg_r"></div>
							                    			</div>
							                    			<span></span>
							                    		</li>
							                        </c:forEach>
							                    </ul>
							                   </td>
			                       		   </c:otherwise>
		                               </c:choose>					                	
					            </tr>
					            </c:forEach>
			    		</c:otherwise>
			    	</c:choose>
			        </table>
			    </dd>
		    </c:forEach>
		</dl><!--tag_list -->
	</c:when>
</c:choose>
</c:forEach>
<script type="text/javascript">
$(function(){
	$(document).click(function(){
		$("#tag_tips .onshow").hide().removeClass("onshow");
		$(".tagChoosed").removeClass("tagChoosed");
	});
});
</script>	
