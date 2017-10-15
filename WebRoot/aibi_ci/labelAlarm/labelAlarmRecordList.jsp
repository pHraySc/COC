<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="GB18030" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript">
$(document).ready( function() {
	  $('#sortTable th').click(sortTable);
});
</script>
<div id="alarmTable">
    <table id="sortTable" width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0">
        <thead>
        <tr>
            <th class="header" dataType="num">序号<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">标签名称<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">预警类型<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="num">实际值<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">预警内容<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="date">数据日期<span class="sort">&nbsp;</span></th>
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
            <td class="align_left"><a href="javascript:showLabelDetail('${po.busiId}');" id="link-look" >${po.busiName}</a></td>
            <td class="align_center">${po.columnName}</td>
            <c:if test="${po.columnId == 2}">
                <td class="align_right"><fmt:formatNumber value="${po.resultValue}" type="number" pattern="0.00"/></td>
            </c:if>
            <c:if test="${po.columnId != 2}">
                <td class="align_right"><fmt:formatNumber value="${po.resultValue}" type="number" pattern="###,###,###"/></td>
            </c:if>
            <td class="align_left">${po.alarmCase}</td>
            <td class="align_right"><fmt:formatDate value="${po.calculateDate}" type="date" pattern="yyyy-MM-dd"/></td>
            </tr>
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