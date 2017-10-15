<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<c:forEach items="${marketTacticsList}" var="marketTactics">
	<div class="customerBase_rows" style="width:99.5%;">
                
                <table width="100%">
                <tr>
                <td>
		
		<dl>
			<dt>${marketTactics.tacticName}</dt>
				<dd>
					<table class="commonTable">
                           <tr>
                               <th style="text-align:left;"><div style="text-align:right;">客户群名称：</div></th>
                               <td colspan="3">
                               <c:forEach items="${marketTactics.customGroupNames}" var="customGroupName">
                                 ${customGroupName}&nbsp;&nbsp;
                               </c:forEach>
                               </td>
                           </tr>
                           <tr>
                               <th>匹配产品：</th>
                               <td>
                                 <c:forEach items="${marketTactics.productNames}" var="productName">
                                 ${productName}&nbsp;&nbsp;
                               </c:forEach>
                               </td>
                           </tr>
                           <tr>
                               <th>创建时间：</th>
                               <td><fmt:formatDate value="${marketTactics.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                           </tr>
                       </table>
                   </dd>
	</dl>
    </td>
                </tr>
                </table>
   
	</div><!--row end -->
</c:forEach>      

