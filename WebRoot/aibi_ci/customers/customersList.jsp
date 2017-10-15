<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
%>
<c:forEach items="${ciCustomGroupInfoList}" var="custom">
	
	<div class="customerBase_rows" style="width:99.5%;">
	
                
                <table width="100%">
                <tr>
                <td>
		
		<dl>
			<dt>${custom.customGroupName}
			<c:if test="${custom.isAlarm == false}">			
				<span style="background:none;padding-left:15px;">
			</c:if>
			<c:if test="${custom.isAlarm == true}">
				<span>
			</c:if>
				<c:if test="${custom.customNum >= 0  }">
					用户数为<a href="javascript:showCustomerTrend('${custom.customGroupId}');" style="font-size: 13px;"><fmt:formatNumber type="number" value="${custom.customNum}"  pattern="###,###,###" />人</a>
					<%if(("sichuan").equals(coc_province)){ %>
						<a href="javascript:showCustomerTrend('${custom.customGroupId}');" style="font-size: 13px;background: none;">清单查看及下载</a>
					<%}%>
				</c:if>
				
				<c:if test="${custom.failedListCount >= 1  }">
					统计失败<a class="failed" href="javascript:regenCustomer('${custom.customGroupId}','${custom.createTypeId}');" >重新生成</a>
				</c:if>
				
				<c:if test="${custom.failedListCount < 1  && custom.createTypeId==1 && custom.updateCycle==1}">
					<a class="failed" href="javascript:regenCustomer('${custom.customGroupId}','${custom.createTypeId}');" >一次性客户群重新生成</a>
				</c:if>
				
				<c:if test="${custom.createTypeId==1}">
					<a class="failed" href="javascript:createTemplate('${custom.customGroupId}','${custom.customGroupName}','${custom.sceneId}');" >提取模板</a>
				</c:if>
			</span>
		</dt>
				<dd>
					<table class="commonTable">
                           <tr>
                               <th>创建规则：</th>
                               <td colspan="3" title="${custom.prodOptRuleShow}${custom.labelOptRuleShow}${custom.customOptRuleShow}${custom.kpiDiffRule}">
                               <c:choose>
                               	   	<c:when test="${custom.prodOptRuleShow == null && custom.labelOptRuleShow == null && custom.customOptRuleShow == null && custom.kpiDiffRule == null}">
                               	   		暂无内容
                               	   	</c:when>
                               	   	<c:otherwise>
                               	   		<div style="height: 20px;overflow: hidden;">
	                               	   		${custom.prodOptRuleShow}
											${custom.labelOptRuleShow}${custom.customOptRuleShow}
											${custom.kpiDiffRule}
                               	   		</div>
                               	   	</c:otherwise>
                               	   </c:choose>
                               </td>
                           </tr>
                           <tr>
                               <th>生成周期：</th>
                               <td>
                                   <c:if test="${custom.updateCycle == 1}">一次性</c:if>
                                   <c:if test="${custom.updateCycle == 2}">月周期</c:if>
                                   <c:if test="${custom.updateCycle == 3}">日周期</c:if>
                               </td>
                               <th style="padding-left: 80px">数据生成时间：</th>
                               <td>
                               	   <c:choose>
                               	   	<c:when test="${custom.dataTime == null}">
                               	   		暂无内容
                               	   	</c:when>
                               	   	<c:otherwise>
                               	   		<fmt:formatDate value="${custom.dataTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                               	   	</c:otherwise>
                               	   </c:choose>
                               </td>
                           </tr>
                           <tr>
                               <th>创建时间：</th>
                               <td><fmt:formatDate value="${custom.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                               <th>创建方式：</th>
                               <td>
                                  ${custom.createType}
                               </td>
                           </tr>
                           <c:if test="${custom.updateCycle != 1}">
                           <tr>
                               <th>有效期：</th>
                               <td><fmt:formatDate value="${custom.startDate}" pattern="yyyy-MM-dd"/>&nbsp至&nbsp<fmt:formatDate value="${custom.endDate}" pattern="yyyy-MM-dd"/></td>
                           </tr>
                           </c:if>
                       </table>
                   </dd>
                   <dd class="info_panel">
                   		<ul>
							<c:if test="${custom.listTableName != null}">
							    <% if("true".equals(indexDifferentialMenu)){ %>
	                           	<li class="icon_quota"><a href="javascript:openIndexDeff('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}')">指标微分</a></li>
							    <%
							       };if("true".equals(customerAnalysisMenu)){
                                %>
								<li class="icon_tag"><a href="javascript:openlabelDeff('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}')">标签微分</a></li>
	                           	<li class="icon_rel"><a href="javascript:openCustomRel('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">关联分析</a></li>
	                           	<li class="icon_ant"> <a href="javascript:openCustomContrast('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">对比分析</a></li>
	                           	<%
                                    };if("true".equals(marketingMenu)){
                                %>
	                           	<li class="icon_marketing"><a href="javascript:customSysMatch('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">营销策略匹配</a></li>
	                           	<%}; %>
		                    </c:if>
                   		</ul>
                   </dd>
	</dl>
    </td>
                <td  style="width: 39px;background: #f7f6f6"> <div class="addCustomer"><a onclick="addToPanel(this);" data="${custom.json}" >添加</a></div></td>
                </tr>
                </table>
   
	</div><!--row end -->
</c:forEach>      

