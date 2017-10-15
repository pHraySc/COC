 <%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/main.js"></script>
<script type="text/javascript">
$(document).ready( function() {
});
$("#totalSizeNum").html('${pager.totalSize}');
</script>
<div id="alarmTable" class="aibi_area_data">
    <table id="sortTable" width="100%" class="tableStyle" >
        <thead>
	        <tr>
	            <th width="85" class="header"  dataType="num">序号&nbsp;</th>
	            <th width="205" class="header" dataType="text">客户群名称&nbsp;</th>
	            <th width="105" class="header" dataType="text">预警类型&nbsp;</th>
	            <th width="105" class="header"  dataType="num">实际值&nbsp;</th>
	            <th width="160" class="header" dataType="text">预警内容&nbsp;</th>
	            <th width="105" class="header" dataType="date">数据日期&nbsp;</th>
	        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pager.result}" var="po" varStatus="st">
            <c:choose>
                <c:when test="${st.index % 2 == 0 }">
                    <tr class="even">
                </c:when>
                <c:otherwise>
                    <tr class="odd">
                </c:otherwise>
            </c:choose>

            <td class="align_center">${st.index+1}</td>
            <td class="align_left"><a href="javascript:showCustomerDetail('${po.busiId}');" id="link-look" >${po.busiName}</a></td>
            <td class="align_center">${po.columnName}</td>
            <c:if test="${po.columnId == 2}">
                <td class="align_center"><fmt:formatNumber value="${po.resultValue}" type="number" pattern="0.00"/></td>
            </c:if>
            <c:if test="${po.columnId != 2}">
                <td class="align_center"><fmt:formatNumber value="${po.resultValue}" type="number" pattern="###,###,###"/></td>
            </c:if>
            <td class="align_left">${po.alarmCase}</td>
            <td class="align_center"><fmt:formatDate value="${po.calculateDate}" type="date" pattern="yyyy-MM-dd"/></td>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagenum" id="pager">
        <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true"/>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        $(".num").each(function () {
            if ($(this).text() == "${pager.pageNum}") {
                $(this).addClass("num_on");
            }
        });
    })
</script>