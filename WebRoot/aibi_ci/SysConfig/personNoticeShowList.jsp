<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(document).ready( function() {
	$(".num").each(function () {
		if ($(this).text() == "${pager.pageNum}") {
			$(this).addClass("num_on");
		}
	});
	//选中状态 
	$(".checkBoxOff").click(function(){
        if($(this).hasClass("checkBoxOn")){
			$(this).removeClass("checkBoxOn");
			$(this).find("input").attr("checked", false);
		}else{
			$(this).addClass("checkBoxOn");
			$(this).find("input").attr("checked", true);
  	  	}
	});
});
</script>
<div>
	<input id='recNum' type="hidden" value='${currentRecordNum}'/>
	<c:forEach items="${pager.result}" var="po" varStatus="st">
		<ol class="clearfix">
	   	<c:choose>
       	<c:when test="${po.readStatus == 1 }">
       		<c:choose>
				<c:when test="${po.isSuccess == 1}">
					<li class="msgTitle checkBoxChecked" style="font-weight:bold;">
						<a href="javascript:void(0);" class="checkBoxOff" onclick="checkDelete()">
							<input type="checkbox" name="personNoticeCheckBox"  id="checkBox${st.index+1}" value="${po.noticeId}" />
						</a>
						<h5><span onclick="datatable(this);" noticeName="${po.noticeName }" noticeId="${po.noticeId }" readStatus="${po.readStatus }" noticeDetail="${po.noticeDetail }" noticeSendTime="${po.noticeSendTime }">${po.noticeName}-成功</span></h5>
					</li>
				</c:when>
				<c:when test="${po.isSuccess == 0}">
					 <li class="msgTitle createFail checkBoxChecked" style="font-weight:bold;" >
						<a href="javascript:void(0);" class="checkBoxOff" onclick="checkDelete()">
							<input type="checkbox" name="personNoticeCheckBox"  id="checkBox${st.index+1}" value="${po.noticeId}" />
						</a>
						<h5><span onclick="datatable(this);" noticeName="${po.noticeName }" noticeId="${po.noticeId }" readStatus="${po.readStatus }" noticeDetail="${po.noticeDetail }" noticeSendTime="${po.noticeSendTime }">${po.noticeName}-失败</span></h5>
					</li>
				</c:when>
			</c:choose>
       </c:when>
       <c:otherwise>
       		<c:choose>
				<c:when test="${po.isSuccess == 1}">
			  		<li  class="msgTitle " >
						<a href="javascript:void(0);" class="checkBoxOff" onclick="checkDelete()">
							<input type="checkbox" name="personNoticeCheckBox"  id="checkBox${st.index+1}" value="${po.noticeId}" />
						</a>
						<h5><span onclick="datatable(this);"  noticeName="${po.noticeName }" noticeId="${po.noticeId }" readStatus="${po.readStatus }" noticeDetail="${po.noticeDetail }" noticeSendTime="${po.noticeSendTime }">${po.noticeName}-成功</span></h5>
					</li>
				</c:when>
				<c:when test="${po.isSuccess == 0}">
					<li  class="msgTitle  createFail" >
						<a href="javascript:void(0);" class="checkBoxOff" onclick="checkDelete()">
							<input type="checkbox" name="personNoticeCheckBox"  id="checkBox${st.index+1}" value="${po.noticeId}" />
						</a>
						<h5><span onclick="datatable(this);" noticeName="${po.noticeName }" noticeId="${po.noticeId }" readStatus="${po.readStatus }" noticeDetail="${po.noticeDetail }" noticeSendTime="${po.noticeSendTime }">${po.noticeName}-失败</span></h5>
					</li>
				</c:when>
			</c:choose>
       	</c:otherwise>
		</c:choose>
			<li class="msgContent">
				<span>${po.noticeDetail}</span>
			</li>
			<li class="msgDate">
				<fmt:formatDate value="${po.noticeSendTime}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/><span>来自</span><span style="color:#0194e9;">系统消息</span>
			</li>
	  	</ol>
	  	<div class="msgContentSegLine"></div>
	</c:forEach>
	<input type="hidden" name="personTotal" value="${pojo.totalCount }" id="personTotal"/>
	<input type="hidden" name="personNotReadCount" value="${pojo.readStatusNot }" id="personNotReadCount"/>
</div>
<div class="pagenum" id="pager">
	<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true"/>
</div>