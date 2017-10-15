<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
    <c:forEach items="${productDetailList}" var="product">
                <div class="customerBase_rows" style="width:99.5%;">
                	<table width="100%">
	                	<tr>
	                	   <td>
	                	   	 <dl>
			                    <dt>${product.productName}</dt>
			                    <dd>
			                        <table class="commonTable">
			                        	<tr>
			                                <th>适用品牌：</th>
			                                <td colspan="3">
			                                  ${product.brandNames}
			                                </td>
			                            </tr>
			                            <tr>
			                                <th>有效期：</th>
			                                <td colspan="3">
			                                  <fmt:formatDate value="${product.effecTime}" pattern="yyyy-MM-dd"/>
												至
			                                  <fmt:formatDate value="${product.failTime}" pattern="yyyy-MM-dd"/>
			                                </td>
			                            </tr>
			                            <tr>
			                                <th>分类：</th>
			                                <td colspan="3">
			                                  ${product.ancestorNames}
			                                </td>
			                            </tr>
			                            <tr>
			                                <th>推荐理由：</th>
			                                <td colspan="3">
			                                  ${product.descTxt}
			                                </td>
			                            </tr>
			                        	<tr>
			                                <th>产品涵义：</th>
			                                <td colspan="3">
			                                  ${product.productMean}
			                                </td>
			                            </tr>
										<!--
			                            <tr>
			                                <th>累计使用次数：</th>
			                                <td colspan="3">
			                                   
			                                </td>
			                            </tr>
			                            -->
			                        </table>
			                    </dd>
			                    <dd class="info_panel">
			                    	<ul>
			                          <li class="icon01" title="规则校验报告" onclick='openVerifyPage("${product.productId}")'><a href="javascript:void(0)" onmouseover='verify(this,"${product.productId}")' onmouseout=verifyhide(this)>规则校验报告</a></li>              	
			                        </ul>                                    
			                    </dd>
			                </dl>
	                	   </td>
	                	   <td width="39" style="background:#f7f6f6;">
	                	   		<div class="addCustomer" id="${product.productId}" labelName="${product.productName}" element="${product.productId}" 
			                	min="0" max="1" type="" elementType="4"
			                	effectDate="${product.effectDate}"><a href="javascript:addLabel('${product.productId}');">添加</a></div>
	                	   </td>
	                	</tr>
                	</table>
               </div>
     </c:forEach>      
<!--row end -->
