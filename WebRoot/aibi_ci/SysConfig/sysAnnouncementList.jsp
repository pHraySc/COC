<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript">
$(document).ready( function() {
	  $('#sortTable th').click(sortTable);
});
</script>
<div id="sysAnnouncementTableList2">
    <table id="sortTable" width="100%" class="tableStyle" cellpadding="0" cellspacing="0">
        <thead>
        <tr>
            <th style="cursor: pointer;width: 30px;"><input type="checkbox" id="checkAll" onclick="checkDelete()"/></th>
            <th style="width: 50px;" class="header" dataType="num">序号<span class="sort">&nbsp;</span></th>
            <th style="width: 135px;" class="header" dataType="text">标题<span class="sort">&nbsp;</span></th>
            <th class="header" dataType="text">内容<span class="sort">&nbsp;</span></th>
            <th style="width: 120px;" class="header" dataType="text">类型<span class="sort">&nbsp;</span></th>
            <th style="width: 80px;" class="header" dataType="date">发布日期<span class="sort">&nbsp;</span></th>
            <th style="width: 88px;" class="header" dataType="date">有效截止期<span class="sort">&nbsp;</span></th>
            <th style="width: 80px;" class="header" dataType="text">优先级<span class="sort">&nbsp;</span></th>
            <th style="width: 90px;" class="header" dataType="text">公告状态<span class="sort">&nbsp;</span></th>
            <th style="width: 120px;"class="noborder">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${syspager.result}" var="po" varStatus="st">
            <c:choose>
                <c:when test="${st.index % 2 == 0 }">
                    <tr class="even">
                </c:when>
                <c:otherwise>
                    <tr class="odd">
                </c:otherwise>
            </c:choose>

            <td class="align_center" style="cursor: pointer;"><input type="checkbox" class="checkbox" name="sysAnnouncement.announcementIdList" value="${po.announcementId}" onclick="checkDelete()"/></td>
            <td class="align_center">${st.index+1}</td>
            <td class="align_left">${po.announcementName}</td>
            <td class="align_left">${po.announcementDetail}</td>
            <td class="align_center">
                <c:if test="${po.typeId == 1 }">标签发布</c:if>
                <c:if test="${po.typeId == 2 }">新功能上线</c:if>
                <c:if test="${po.typeId == 3 }">标签下线</c:if>
                <c:if test="${po.typeId == 4 }">其他</c:if>
            </td>
            <td class="align_right"><fmt:formatDate value="${po.releaseDate}" type="date" pattern="yyyy-MM-dd"/></td>
            <td class="align_right"><fmt:formatDate value="${po.effectiveTime}" type="date" pattern="yyyy-MM-dd"/></td>
            <td class="align_center">
                <c:if test="${po.priorityId == 0 }">重要</c:if>
                <c:if test="${po.priorityId == 1 }">普通</c:if>
               
            </td>
            <td class="align_center">
                <c:if test="${po.status == 1 }">有效</c:if>
                <c:if test="${po.status == 2 }">失效</c:if>
            </td>
            <td>
            	<!--<a class="icon_edit" href="#" thresholdId="${po.announcementId}" onclick="editSysAnnouncement('${po.announcementId}')">修改</a>-->
            	<a class="icon_del"  href="#" onclick="deleteSysAnnouncement('${po.announcementId}')"><span>删除</span></a>
            </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagenum" id="syspager">
        <jsp:include page="/aibi_ci/page_sys_html.jsp" flush="true"/>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        $(".num").each(function () {
            if ($(this).text() == "${syspager.pageNum}") {
                $(this).addClass("num_on");
            }
        });

        $("#checkAll").bind("click",function () {
            var checked = $(this).prop("checked");
            $("input[name='sysAnnouncement.announcementIdList']").each(function () {
                $(this).prop("checked", checked);
            })
            checkDelete();
        });
    })
</script>