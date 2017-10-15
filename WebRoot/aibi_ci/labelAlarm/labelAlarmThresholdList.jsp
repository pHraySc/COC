<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/main.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	  $('#sortTable th').click(sortTable);
});
</script>
<div id="alarmTable">
    <table id="sortTable" width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <th><input type="checkbox" id="checkAll" onclick="checkDelete()"/></th>
            <th class="header" dataType="num">序号<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">标签名称<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">预警类型<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">阈值范围<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">告警级别<span class="sort">&nbsp;</span></th>
            <th class="noborder">操作</th>
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

            <td class="align_center"><input type="checkbox" class="checkbox" name="alarmInfo.thresholdIdList" value="${po.thresholdId}" onclick="checkDelete()"/></td>
            <td class="align_center">${st.index+1}</td>
            <td class="align_left"><a href="javascript:showLabelDetail('${po.busiId}');" id="link-look" >${po.busiName}</a></td>
            <td class="align_center">${po.columnName}</td>
            <td class="align_left">
                <c:choose>
                    <c:when test="${po.thresholdType == 'more'}">
                        大于上限值<fmt:formatNumber value="${po.maxValue}" type="number" pattern="##0.##"/>
                    </c:when>
                    <c:when test="${po.thresholdType == 'less'}">
                        小于下限值<fmt:formatNumber value="${po.minValue}" type="number" pattern="##0.##"/>
                    </c:when>
                    <c:when test="${po.thresholdType == 'in'}">
                        在范围[<fmt:formatNumber value="${po.minValue}" type="number" pattern="##0.##"/>,<fmt:formatNumber value="${po.maxValue}" type="number" pattern="##0.##"/>]之间
                    </c:when>
                    <c:when test="${po.thresholdType == 'out'}">
                        在范围[<fmt:formatNumber value="${po.minValue}" type="number" pattern="##0.##"/>,<fmt:formatNumber value="${po.maxValue}" type="number" pattern="##0.##"/>]之外
                    </c:when>
                </c:choose>
            </td>
            <td class="align_center">
                <c:if test="${po.levelId == 1 }">高</c:if>
                <c:if test="${po.levelId == 2 }">中</c:if>
                <c:if test="${po.levelId == 3 }">低</c:if>
            </td>
            <td><a class="icon_edit" href="#" thresholdId="${po.thresholdId}" onclick="editThreshold('${po.thresholdId}')">修改</a>
                <a class="icon_del"  href="#" onclick="deleteThreshold('${po.thresholdId}')"><spn>删除</spn></a></td>
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

        $("#checkAll").bind("click",function () {
            var checked = $(this).prop("checked");
            $("input[name='alarmInfo.thresholdIdList']").each(function () {
                $(this).prop("checked", checked);
            })
            checkDelete();
        });
    })
</script>