<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript">
$(document).ready( function() {
	  //$('#sortTable th').click(sortTable);
	  //选中状态 
	$(".checkBoxOff").click(function(){
        if($(this).hasClass("checkBoxOn")){
  	     $(this).removeClass("checkBoxOn");
  		 //$(this).parent().removeClass("checkBoxChecked");
  		 $(this).find("input").attr("checked", false);
  	  }else{
  	      $(this).addClass("checkBoxOn");
  		  //$(this).parent().addClass("checkBoxChecked");
  		  $(this).find("input").attr("checked", true);
  	  }
     });
});
</script>
<div>
	  <c:forEach items="${pager.result}" var="po" varStatus="st">
	  <ol class="clearfix">
	   <c:choose>
       <c:when test="${po.readStatus == 0 }">
       		 <li  class="msgTitle checkBoxChecked" style="font-weight:bold;" >
			<a href="javascript:void(0);" class="checkBoxOff" onclick="ucheckAll();">
			<input type="checkbox" name="sysAnnouncementCheckBox"  id="checkBox${st.index+1}" value="${po.announcementId}" onclick="checkDelete()"/>
			</a>
			<h5>
				<span  onclick="readedSysAnnouncement(this);" announcementName="${po.announcementName }" announcementId="${po.announcementId }" readStatus="${po.readStatus }" announcementDetail="${po.announcementDetail }" releaseDate="${po.releaseDate }">${po.announcementName}</span>
				<c:if test="${po.priorityId == 0}">
				<em  title="重要">！</em>
				</c:if>
			</h5>
			</li>
       </c:when>
       <c:otherwise>
	  		<li  class="msgTitle " >
			<a href="javascript:void(0);" class="checkBoxOff" onclick="ucheckAll();">
			<input type="checkbox" name="sysAnnouncementCheckBox"  id="checkBox${st.index+1}" value="${po.announcementId}" />
			</a>
			<h5>
				<span  onclick="readedSysAnnouncement(this);" announcementName="${po.announcementName }" announcementId="${po.announcementId }" readStatus="${po.readStatus }" announcementDetail="${po.announcementDetail }" releaseDate="${po.releaseDate }">${po.announcementName}</span>
				<c:if test="${po.priorityId == 0}">
				<em title="重要">！</em>
				</c:if>
			</h5>
			</li>
       </c:otherwise>
   		</c:choose>
			<li class="msgContent">
			<!--<span class="fB">内容</span>：--><span>${po.announcementDetail}</span>
			</li>
			<!--<li class="msgContent">
				<span class="fB">类型</span>:<c:if test="${po.typeId == 1 }">标签发布</c:if>
                <c:if test="${po.typeId == 2 }">新功能上线</c:if>
                <c:if test="${po.typeId == 3 }">标签下线</c:if>
                <c:if test="${po.typeId == 4 }">其他</c:if>
                ;
				<span class="fB">状态</span>：
				<c:choose>
					<c:when test="${po.status == 1 }">有效</c:when>
					<c:otherwise>失效</c:otherwise>
				</c:choose>
                ;
                <span class="fB">优先级</span>：<c:if test="${po.priorityId == 0 }">重要</c:if>
                <c:if test="${po.priorityId == 1 }">普通</c:if>
				;<span class="fB">有效截止期</span>：<fmt:formatDate value="${po.effectiveTime }" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
			</li>
			--><li class="msgDate">
				   <fmt:formatDate value="${po.releaseDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/><span>来自</span><span style="color:#0194e9;">系统消息</span>
			</li>
	  </ol>
	  <div class="msgContentSegLine"> </div>
	  </c:forEach>
	  <input type="hidden" name="sysTotal" value="${pojo.totalCount }" id="sysTotal"/>
	  <input type="hidden" name="sysNotReadCount" value="${pojo.readStatusNot }" id="sysNotReadCount"/>
</div>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true"/>
</div>
<input id='recNum' type="hidden" value='${announce}'/>
<script type="text/javascript">
    $(document).ready(function(){
        $(".num").each(function () {
            if ($(this).text() == "${pager.pageNum}") {
                $(this).addClass("num_on");
            }
        });
    });
</script>