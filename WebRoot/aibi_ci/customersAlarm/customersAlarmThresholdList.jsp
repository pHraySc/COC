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
    <table id="sortTable" width="100%" class="tableStyle">
       <thead>
        <tr>
            <th width="25" style="cursor: pointer;">
				<input type="checkbox" name="checkBox" id="checkAll" onclick="checkBoxAll(this)"/>
			</th>
            <th width="105" class="header"  dataType="num">序号</th>
            <th width="205" class="header" dataType="text">客户群名称</th>
            <th width="130" class="header" dataType="text">预警类型</th>
            <th width="200" class="header" dataType="text">阈值范围</th>
            <th width="130" class="header" dataType="text">告警级别</th>
            <th  class="noborder">操作</th>
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

            <td class="align_center" style="cursor: pointer;"><input type="checkbox" class="checkbox" name="alarmInfo.thresholdIdList" value="${po.thresholdId}" onclick="checkDelete()"/></td>
            <td class="align_center">${st.index+1}</td>
            <td class="align_left"><a href="javascript:showCustomerDetail('${po.busiId}');" id="link-look" >${po.busiName}</a></td>
            <td class="align_center">${po.columnName}</td>
            <td class="align_left">
                <c:choose>
                    <c:when test="${po.thresholdType == 'more'}">
                        大于等于上限值<fmt:formatNumber value="${po.maxValue}" type="number" pattern="##0.##"/>
                    </c:when>
                    <c:when test="${po.thresholdType == 'less'}">
                        小于等于下限值<fmt:formatNumber value="${po.minValue}" type="number" pattern="##0.##"/>
                    </c:when>
                    <c:when test="${po.thresholdType == 'in'}">
                       大于等于下限值<fmt:formatNumber value="${po.minValue}" type="number" pattern="##0.##"/>,小于等于上限值<fmt:formatNumber value="${po.maxValue}" type="number" pattern="##0.##"/>
                    </c:when>
                    <c:when test="${po.thresholdType == 'out'}">
                       小于等于下限值<fmt:formatNumber value="${po.minValue}" type="number" pattern="##0.##"/> 或 大于等于上限值<fmt:formatNumber value="${po.maxValue}" type="number" pattern="##0.##"/>
                    </c:when>
                </c:choose>
            </td>
            <td class="align_center">
                <c:if test="${po.levelId == 1 }">高</c:if>
                <c:if test="${po.levelId == 2 }">中</c:if>
                <c:if test="${po.levelId == 3 }">低</c:if>
            </td>
            <td >
            <a class="icon_edit" href="javascript:void(0);" thresholdId="${po.thresholdId}" onclick="editThreshold('${po.thresholdId}')">修改</a>
            <a class="icon_del"  href="javascript:void(0);" onclick="deleteThreshold('${po.thresholdId}')"><span>删除</span></a>
            </td>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagenum" id="pager">
        <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true"/>
    </div>
</div>