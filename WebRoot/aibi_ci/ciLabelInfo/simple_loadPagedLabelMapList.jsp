<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>

<% 
	/**从配置文件获取配置的加载首页标签地图标签个数**/
	//二三级标签，每个二级标签下要加载的三级标签个数(目前页面每行展示7个标签，默认设置为3行)
	String secth_th = Configure.getInstance().getProperty("SECTH_TH_LABELSIZE"); 
	//二三四级标签，每个二级标签下要加载的三级标签个数
	String secthfourth_th = Configure.getInstance().getProperty("SECTHFourth_TH_LABELSIZE"); 
	//二三四级标签，每个三级级标签下要加载的四级标签个数(默认设置为1行,即6个，最后一级展示凑满一行)
	String secthfourth_fourth = Configure.getInstance().getProperty("SECTHFourth_FOURTH_LABELSIZE");
	//二三四五级标签，每个二级标签下要加载的三级标签个数 
	String secthfourthfiv_th = Configure.getInstance().getProperty("SECTHFourthFiv_TH_LABELSIZE");
	//二三四五级标签，每个三级级标签下要加载的四级标签个数
	String secthfourthfiv_four = Configure.getInstance().getProperty("SECTHFourthFiv_FOURTH_LABELSIZE");
	//二三四五级标签，每个四级级标签下要加载的五级标签个数(默认设置为1行)
	String secthfourthfiv_fiv = Configure.getInstance().getProperty("SECTHFourthFiv_FIV_LABELSIZE");

	//如果配置文件中没有设置，在此将上面六个控制标签个数的变量设置默认值
	secth_th = "".equals(secth_th) ? "21" : secth_th;
	secthfourth_th = "".equals(secthfourth_th) ? "3" : secthfourth_th;
	secthfourth_fourth = "".equals(secthfourth_fourth) ? "6" : secthfourth_fourth;
	secthfourthfiv_th = "".equals(secthfourthfiv_th) ? "3" : secthfourthfiv_th;
	secthfourthfiv_four = "".equals(secthfourthfiv_four) ? "3" : secthfourthfiv_four;
	secthfourthfiv_fiv = "".equals(secthfourthfiv_fiv) ? "5" : secthfourthfiv_fiv;
%>

<%--二三级标签，每个二级标签下要加载的三级标签个数(目前页面每行展示7个标签，默认设置为3行)--%> 
<c:set var="secth_th" value="<%=secth_th%>"></c:set>
<%--二三四级标签，每个二级标签下要加载的三级标签个数 --%>
<c:set var="secthfourth_th" value="<%=secthfourth_th%>"></c:set>
<%--二三四级标签，每个三级级标签下要加载的四级标签个数(默认设置为1行,即6个，最后一级展示凑满一行) --%>
<c:set var="secthfourth_fourth" value="<%=secthfourth_fourth%>"></c:set>
<%--二三四五级标签，每个二级标签下要加载的三级标签个数 --%>
<c:set var="secthfourthfiv_th" value="<%=secthfourthfiv_th%>"></c:set>
<%--二三四五级标签，每个三级级标签下要加载的四级标签个数 --%>
<c:set var="secthfourthfiv_four" value="<%=secthfourthfiv_four%>"></c:set>
<%--二三四五级标签，每个四级级标签下要加载的五级标签个数(默认设置为1行) --%>
<c:set var="secthfourthfiv_fiv" value="<%=secthfourthfiv_fiv%>"></c:set>

<c:forEach var="secondLTree" items="${secondLTreeList}" varStatus="status1">
<c:choose>
    <%--一共有四级标签的展示，depth=3表示展示：2、3、4 这三级的标签 --%>
	<c:when test="${secondLTree.depth == 3}">
		<div id="labelId_${secondLTree.ciLabelInfo.labelId}">
		<dl class="tag_List">
		    <%--为2级标签的名称，不可能为空，不用做空考虑. --%> 
			<dt><span><c:out value="${secondLTree.ciLabelInfo.labelName}"></c:out></span>
				 <%--在dt中判断是否显示"展示全部"，当三级标签大于secthfourth_th(默认值3)个或者某个三级标签下四级标签大于secthfourth_fourth(默认值6)个的情况 --%>
	        	<c:choose>
	        		<c:when test="${fn:length(secondLTree.ciLabelInfoTree) > secthfourth_th}"> 
	        			<a class="showAllLabel" href="javascript:void(0);" onclick="expandChildrenOfSecondL('${secondLTree.ciLabelInfo.labelId}');">展示全部</a>
	        		</c:when>
	        		<c:otherwise>
	        			<c:set var="expandThFourFlag" value="0"></c:set>
	        			<c:forEach var="thirdLTree" items="${secondLTree.ciLabelInfoTree}" varStatus="status2" >
	        				<c:forEach var="forthLTree" items="${thirdLTree.ciLabelInfoTree}" varStatus="status3" >
	        				<c:if test="${status3.index+1 > secthfourth_fourth}">
	        				 	<c:set var="expandThFourFlag" value="1"></c:set>
	        				</c:if>
	        				</c:forEach>
	        			</c:forEach>
	        			<c:if test="${expandThFourFlag == 1 }">
	        				<a class="showAllLabel" href="javascript:void(0);" onclick="expandChildrenOfSecondL('${secondLTree.ciLabelInfo.labelId}');">展示全部</a>
	        			</c:if>
	        		</c:otherwise>
	        	</c:choose>
			</dt>
		    <dd>
		    	<table class="commonTable" cellpadding="0" cellspacing="0">
		    	    <%--由于depth为3，所以肯定有三级标签 --%>
		    	    <%--如果某个二级标签下三级标签过多，只显示secthfourth_th(默认3)个三级标签 --%>
		    	    <c:forEach var="thirdLTree" items="${secondLTree.ciLabelInfoTree}" varStatus="status2">
			        	<c:choose>
			        		<c:when test="${status2.index < secthfourth_th}"> 
			        			<c:choose>
					        	    <%--第secthfourth_th(默认3)个三级标签或者最后一个三级标签tr是不要下划线的，所以增加了个class--%>
									<c:when test="${status2.index+1 == secthfourth_th || (status2.index+1 == fn:length(secondLTree.ciLabelInfoTree))}">
										<tr class="last">
									</c:when>  
					    		    <c:otherwise>
										<tr>
					   				</c:otherwise>
								</c:choose>
								<%--显示三级标签的名称 --%>
				            	<th>
				            		<span 
				            		 	<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
				            		 	class="canDrag isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
				            		 	<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
				            		 	class="canDrag cannotDrag" </c:if>
				            		 	element="${thirdLTree.ciLabelInfo.labelId}" elementType="2" min="${thirdLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
				            			max="${thirdLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdLTree.ciLabelInfo.labelTypeId}"
				            			labelName="${thirdLTree.ciLabelInfo.labelName}" effectDate="${thirdLTree.ciLabelInfo.effectDate}" 
				            			attrVal="${thirdLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
				            			updateCycle="${thirdLTree.ciLabelInfo.updateCycle}"
				            			customNum="${thirdLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
				            			<%--下面的几个div的作用是让选中标签后，标签的背景为圆角且背景大小根据标签名称的长度自适应 --%>
				            			<div class="tag_List_inner">
					            			<div class="bg_l"></div>
					            			<a href="javascript:void(0)" title="${thirdLTree.ciLabelInfo.labelName}">
					            				<font><c:out value="${thirdLTree.ciLabelInfo.labelName}"></c:out></font>
					            			</a>
					            			<div class="bg_r"></div>
				            			</div>	
				            		</span>
				            	</th>
			        		</c:when>
			        		<c:otherwise></c:otherwise>
			        	</c:choose>
		        	    
		            	<%--显示四级标签的名称，当然有可能没有四级标签 --%>
		                    <c:choose>
		                       <%--当四级标签有数据时，当然显示四级标签 --%>
		                       <%--如果某个二级标签下三级标签过多，只显示secthfourth_th(默认3)个三级标签 --%> 
		                       <c:when test="${status2.index < secthfourth_th}">
			                        <c:choose>
			                       <%--由于IE6和IE7当td为空时，样式不起作用，所以当没有四级标签时需要特殊处理 --%>
				                       <c:when test="${fn:length(thirdLTree.ciLabelInfoTree) == 0}">
				                           <td class="nodata needCrush">
				                       		&nbsp;
				                         	</td>
				                       </c:when>
				                       <%--当四级标签有数据时，当然显示四级标签 --%>
				                       <c:otherwise>
		                         <td>
		    		                <ul class="tag_List_ul">
				                		<c:forEach var="forthLTree" items="${thirdLTree.ciLabelInfoTree}" varStatus="status3">
					                    	<c:choose>
					                    	<%-- 每个三级级标签下要加载的四级标签个数(默认设置为1行,即6个，最后一级展示凑满一行) --%>
			        						<c:when test="${status3.index < secthfourth_fourth}"> 
					                    	<li 
					                    		<c:if test="${forthLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
						            		 	class="isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
					                    		element="${forthLTree.ciLabelInfo.labelId}" elementType="2" min="${forthLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
					                    		max="${forthLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${forthLTree.ciLabelInfo.labelTypeId}"
					                    		labelName="${forthLTree.ciLabelInfo.labelName}" effectDate="${forthLTree.ciLabelInfo.effectDate}" 
					                    		attrVal="${forthLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
					                    		updateCycle="${forthLTree.ciLabelInfo.updateCycle}"
					                    		customNum="${forthLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
					                    		<div class="tag_List_inner">
					                    			<div class="bg_l"></div>
							                    		<a href="javascript:void(0)" title="${forthLTree.ciLabelInfo.labelName}">
							                    			<font><c:out value="${forthLTree.ciLabelInfo.labelName}"></c:out></font>
							                    		</a>
							                    	<div class="bg_r"></div>
							                    </div>
					                    		<span></span>
					                    	</li>
					                    	</c:when>
					                    	</c:choose>
				                        </c:forEach>
				                    </ul>
				                   </td>
				                   </c:otherwise>
				                   </c:choose>
		                       </c:when>
		                    </c:choose>
		            </tr>
		            </c:forEach>
		        </table>
		    </dd>
		</dl></div><!--tag_list -->
	</c:when>
	
	<%--一共有三级标签的展示，depth=2表示展示：2、3这两级的标签，所以比较简单，不用做详细注释 --%>
	<c:when test="${secondLTree.depth == 2}">

		<div id="labelId_${secondLTree.ciLabelInfo.labelId}">
		<dl class="tag_List">
			<dt><span><c:out value="${secondLTree.ciLabelInfo.labelName}"></c:out></span>
				<c:choose>
					<c:when test="${fn:length(secondLTree.ciLabelInfoTree) > secth_th}"> 
		        		<a class="showAllLabel" href="javascript:void(0);" onclick="expandChildrenOfSecondL('${secondLTree.ciLabelInfo.labelId}');">展示全部</a>
		        	</c:when>
	        	</c:choose>
			</dt>
		    <dd>
		    	<table class="commonTable" cellpadding="0" cellspacing="0">
						<tr class="last">
		                <td>
		                	<ul class="tag_List_ul">
		                		<c:forEach var="thirdLTree" items="${secondLTree.ciLabelInfoTree}" varStatus="status2">
			                    	<c:choose>
			                    		<c:when test="${status2.index < secth_th}">
			                    		<li 
			                    			<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
							            	class="isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
			                    			element="${thirdLTree.ciLabelInfo.labelId}" elementType="2" min="${thirdLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
			                    			max="${thirdLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdLTree.ciLabelInfo.labelTypeId}"
			                    			labelName="${thirdLTree.ciLabelInfo.labelName}" effectDate="${thirdLTree.ciLabelInfo.effectDate}" 
			                    			attrVal="${thirdLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
			                    			updateCycle="${thirdLTree.ciLabelInfo.updateCycle}"
			                    			customNum="${thirdLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
			                    			<div class="tag_List_inner">
				                    			<div class="bg_l"></div>
					                    		<a href="javascript:void(0)" title="${thirdLTree.ciLabelInfo.labelName}">
					                    			<font><c:out value="${thirdLTree.ciLabelInfo.labelName}"></c:out></font>
					                    		</a>
					                    		<div class="bg_r"></div>
				                    		</div>
				                    		<span></span>
			                    		</li>
			                    		</c:when>	
			                    	</c:choose>
		                        </c:forEach>
		                    </ul>
		                </td>
		            </tr>
		        </table>
		    </dd>
		</dl></div><!--tag_list -->
	</c:when>
	
	<%--一共有五级标签的展示，depth=4表示展示：2、3、4、5这四级的标签，比较麻烦，需要做详细注释 --%>
	<c:when test="${secondLTree.depth == 4}">
		<div id="labelId_${secondLTree.ciLabelInfo.labelId}">
		<dl class="tag_List">
		    <%--展示二级标签的名称 --%>
			<dt><span><c:out value="${secondLTree.ciLabelInfo.labelName}"></c:out></span>
			<%--在dt中判断是否显示"展示全部"，当三级标签大于secthfourthfiv_th(默认3)个或者某个三级标签下四级标签
			大于secthfourthfiv_four(默认3)或者某个四级标签下五级标签大于secthfourthfiv_fiv(默认5)个的情况 --%>
	        	<c:choose>
	        		<c:when test="${fn:length(secondLTree.ciLabelInfoTree) > secthfourthfiv_th}"> 
	        			<a class="showAllLabel" href="javascript:void(0);" onclick="expandChildrenOfSecondL('${secondLTree.ciLabelInfo.labelId}');">展示全部</a>
	        		</c:when>
	        		<c:otherwise>
	        			<%--expandThFourFivFlag代表标签是否全部展示，0代表标签已经全部展示；1代表标签没有全部展示 --%>
	        			<c:set var="expandThFourFivFlag" value="0"></c:set>
	        			<c:forEach var="thirdLTree" items="${secondLTree.ciLabelInfoTree}" varStatus="status2" >
	        				<c:forEach var="forthLTree" items="${thirdLTree.ciLabelInfoTree}" varStatus="status3" >
		        				<c:if test="${status3.index+1 > secthfourthfiv_four}">
		        				 	<c:set var="expandThFourFivFlag" value="1"></c:set>
		        				</c:if>
	        				</c:forEach>
	        				<c:choose>
	        					<%--四级标签个数大于secthfourthfiv_four则不进行五级标签循环 --%>
		        				<c:when test="${expandThFourFivFlag == 1 }">
		        					
		        				</c:when>
		        				<%--进行五级标签循环 --%>
		        				<c:otherwise>
		        					<c:forEach var="forthLTree" items="${thirdLTree.ciLabelInfoTree}" varStatus="status3" >
			        					<c:forEach var="fifthLTree" items="${forthLTree.ciLabelInfoTree}" varStatus="status5" >
					        				<c:if test="${status5.index+1 > secthfourthfiv_fiv}">
					        				 	<c:set var="expandThFourFivFlag" value="1"></c:set>
					        				</c:if>
					        			</c:forEach>
		        					</c:forEach>
		        				</c:otherwise>
	        				</c:choose>
	        			</c:forEach>
	        			<c:if test="${expandThFourFivFlag == 1 }">
	        				<a class="showAllLabel" href="javascript:void(0);" onclick="expandChildrenOfSecondL('${secondLTree.ciLabelInfo.labelId}');">展示全部</a>
	        			</c:if>	
	        		</c:otherwise>
	        	</c:choose>
			</dt>
			<%--将每个三级标签及其下面的四级标签和五级标签做为一个table，有多少个三级标签就有多少个table，
			三级标签的list肯定不为空 --%>
			<c:forEach var="thirdLTree" items="${secondLTree.ciLabelInfoTree}" varStatus="status2">
			    <c:choose>
			    	<c:when test="${status2.index < secthfourthfiv_th}">
			    		<c:choose>
		    				<c:when test="${status2.index+1 == secthfourthfiv_th || (status2.index+1 == fn:length(secondLTree.ciLabelInfoTree))}">
		    					<dd class="last">
		    				</c:when>
		    				<c:otherwise>
		    					<dd>
		    				</c:otherwise>
		    			</c:choose>
			    		
			    		<table class="commonTable" cellpadding="0" cellspacing="0">
			    	    <c:choose>
			    	   		<%--三级标签下当没有四级标签，但是仍然需要显示这个三级标签的名称 --%>
			    	   		
			    	    	<c:when test="${fn:length(thirdLTree.ciLabelInfoTree) == 0}">
			    	    	     <%--还需要判断该一级产品是否为最后一个一级产品，如果是的话，就要增加样式：last --%>
			    	    	     <c:choose>
				    				<c:when test="${status2.index+1 == secthfourthfiv_th || (status2.index+1 == fn:length(secondLTree.ciLabelInfoTree))}">
				    					<tr class="last">
				    				</c:when>
				    				<c:otherwise>
				    					<tr>
				    				</c:otherwise>
			    			    </c:choose>
			    			    	
			    	    	     		<th class="floatTitle">
											<span 
												<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
						            		 	class="canDrag isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
						            		 	<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
						            		 	class="canDrag" </c:if>
												element="${thirdLTree.ciLabelInfo.labelId}" elementType="2" min="${thirdLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
												max="${thirdLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdLTree.ciLabelInfo.labelTypeId}"
												labelName="${thirdLTree.ciLabelInfo.labelName}" effectDate="${thirdLTree.ciLabelInfo.effectDate}" 
												attrVal="${thirdLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
												updateCycle="${thirdLTree.ciLabelInfo.updateCycle}"
												customNum="${thirdLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
												<div class="tag_List_inner">
													<div class="bg_l"></div>
					            					<a href="javascript:void(0)" title="${thirdLTree.ciLabelInfo.labelName}">
														<font><c:out value="${thirdLTree.ciLabelInfo.labelName}"></c:out></font>
													</a>
													<div class="bg_r"></div>
												</div>
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
			    	    	
			    	    	<%--当三级标签下有四级标签时 --%>
			    	    	<c:otherwise>
			    	    		<c:forEach var="forthLTree" items="${thirdLTree.ciLabelInfoTree}" varStatus="status3">
						        	<c:choose>
						        		<c:when test="${status3.index < secthfourthfiv_four}">
						        			<c:choose>
								        	    <%--找到最后一个三级标签(或者某个二级标签的第secthfourthfiv_th个三级标签)的最后一个四级标签(或者某个三级标签的第secthfourthfiv_four个四级标签)，设置样式为last --%>
												<c:when test="${((status3.index+1 == fn:length(thirdLTree.ciLabelInfoTree)) || status3.index+1 == secthfourthfiv_four) && ((status2.index+1 == fn:length(secondLTree.ciLabelInfoTree))|| status2.index+1 == secthfourthfiv_th)}">
													<tr class="last">
												</c:when>  
								    		    <c:otherwise>
													<tr>
								   				</c:otherwise>
											</c:choose>
						        		
						        	
									    <c:choose>
									    <%--有四级标签时，三级标签所占的列数要根据该三级标签下包含的四级标签个数确定--%>
								    	<%--显示第三级标签的名称 --%>
										<c:when test="${status3.index == 0}">
											<th rowspan="${fn:length(thirdLTree.ciLabelInfoTree)}" class="floatTitle">
												<span 
													<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
							            		 	class="canDrag isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
							            		 	<c:if test="${thirdLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
							            		 	class="canDrag" </c:if>
													element="${thirdLTree.ciLabelInfo.labelId}" elementType="2" min="${thirdLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
													max="${thirdLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${thirdLTree.ciLabelInfo.labelTypeId}"
													labelName="${thirdLTree.ciLabelInfo.labelName}" effectDate="${thirdLTree.ciLabelInfo.effectDate}" 
													attrVal="${thirdLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
													updateCycle="${thirdLTree.ciLabelInfo.updateCycle}"
													customNum="${thirdLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
													<div class="tag_List_inner">
														<div class="bg_l"></div>
						            					<a href="javascript:void(0)" title="${thirdLTree.ciLabelInfo.labelName}">
															<font><c:out value="${thirdLTree.ciLabelInfo.labelName}"></c:out></font>
														</a>
														<div class="bg_r"></div>
													</div>
												</span>			
											</th>
										</c:when>  
						    		    <c:otherwise>
											
						   				</c:otherwise>
									    </c:choose>
										<%--显示第四级标签的名称 --%>
						            	<th>
						            		<span 
						            			<c:if test="${forthLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
						            		 	class="canDrag isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
						            		 	<c:if test="${forthLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum != 1}">
						            		 	class="canDrag" </c:if>
						            			element="${forthLTree.ciLabelInfo.labelId}" elementType="2" min="${forthLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
						            			max="${forthLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${forthLTree.ciLabelInfo.labelTypeId}"
						            			labelName="${forthLTree.ciLabelInfo.labelName}" effectDate="${forthLTree.ciLabelInfo.effectDate}" 
						            			attrVal="${forthLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
						            			updateCycle="${forthLTree.ciLabelInfo.updateCycle}"
						            			customNum="${forthLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
						            			<div class="tag_List_inner">
							            			<div class="bg_l"></div>
						            				<a href="javascript:void(0)" title="${forthLTree.ciLabelInfo.labelName}">
							            				<font><c:out value="${forthLTree.ciLabelInfo.labelName}"></c:out></font>
							            			</a>
							            			<div class="bg_r"></div>
						            			</div>
						            		</span>		
						            	</th>
						            	<%--显示第五级标签的名称 --%>
						                
						                
							                <c:choose>
							                       <%--由于IE6和IE7当td为空时，样式不起作用，所以当没有五级标签时需要特殊处理 --%>
							                       <c:when test="${fn:length(forthLTree.ciLabelInfoTree) == 0}">
							                         <td class="nodata needCrush">
							                       		&nbsp;
							                       	  </td>
							                       </c:when>
							                       <%--当五级标签有数据时，当然显示五级标签 --%>
					                       		   <c:otherwise>
					                       		      <td>
									                	<ul class="tag_List_ul">
										                		<c:forEach var="fifthLTree" items="${forthLTree.ciLabelInfoTree}" varStatus="status5">
										                    		<c:choose>
										                    			<c:when test="${status5.index < secthfourthfiv_fiv }">
										                    				<li 
												                    			<c:if test="${fifthLTree.ciLabelInfo.ciLabelExtInfo.isStatUserNum == 1}">
													            		 		class="isCanDrag" onclick="showTip(event,this)" cusNumFlag="0"</c:if>
												                    			element="${fifthLTree.ciLabelInfo.labelId}" elementType="2" min="${fifthLTree.ciLabelInfo.ciLabelExtInfo.minVal}" 
												                    			max="${fifthLTree.ciLabelInfo.ciLabelExtInfo.maxVal}" type="${fifthLTree.ciLabelInfo.labelTypeId}"
												                    			labelName="${fifthLTree.ciLabelInfo.labelName}" effectDate="${fifthLTree.ciLabelInfo.effectDate}" 
												                    			attrVal="${fifthLTree.ciLabelInfo.ciLabelExtInfo.attrVal}" 
												                    			updateCycle="${fifthLTree.ciLabelInfo.updateCycle}"
												                    			customNum="${fifthLTree.ciLabelInfo.ciLabelExtInfo.customNum}">
												                    			<div class="tag_List_inner">
													                    			<div class="bg_l"></div>
													                    			<a href="javascript:void(0)" title="${fifthLTree.ciLabelInfo.labelName}">
													                    				<font><c:out value="${fifthLTree.ciLabelInfo.labelName}"></c:out></font>
													                    			</a>
													                    			<div class="bg_r"></div>
												                    			</div>
												                    			<span></span>
												                    		</li>
										                    			</c:when>
										                    			<c:otherwise></c:otherwise>
										                    		</c:choose>
										                        </c:forEach>
										                  </ul>
										                 </td>
					                       		   </c:otherwise>
				                               </c:choose>
						                
						            </tr>
						            </c:when>
						        	<c:otherwise></c:otherwise>
						        	</c:choose>
						            </c:forEach>
			    	    	</c:otherwise>
			    	    </c:choose>
			        </table>
			    </dd>
			    </c:when>
			    <c:otherwise></c:otherwise>
			    </c:choose>
		    </c:forEach>
		</dl></div><!--tag_list -->
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
