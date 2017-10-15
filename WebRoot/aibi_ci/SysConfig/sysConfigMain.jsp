<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%String userNoticeSet = Configure.getInstance().getProperty("USER_NOTICE_SET");
%>
<div class="submenu">
	<div class="submenu_left" style="float:left"></div>
	<div class="top_menu">
		<ul>
		<c:if test="${isAdmin}">
		   <li style="white-space:nowrap;" class="top_menu_on"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciSysAnnouncementAction!init.ai2do" class="no12"><span></span><p>系统公告</p></a><div class="bgright">&nbsp;</div></li>
		</c:if>
		<%if("true".equals(userNoticeSet)){ %>
			<li style="white-space:nowrap;"><div class="bgleft">&nbsp;</div><a href="${ctx}/ci/ciUserNoticeSetAction!initUserNoticeSet.ai2do" class="no13"><span></span><p>个性化通知</p></a><div class="bgright">&nbsp;</div></li>
		<%} %>
			<div class="clear"></div>
		</ul>
	</div>
</div>
