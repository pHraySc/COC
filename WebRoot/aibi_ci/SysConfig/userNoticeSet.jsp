<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<%@ page import="com.asiainfo.biframe.utils.config.Configure"%>
<%
	String smsSysFlag = Configure.getInstance().getProperty(
			"SYSANNOUNCEMENT_SMS_FLAG");
	String emailSysFlag = Configure.getInstance().getProperty(
			"SYSANNOUNCEMENT_EMAIL_FLAG");
	String sendModeId = (String) request.getAttribute("sendModeId");
%>
<script type="text/javascript">
$(function(){
	//切换效果 
	$(".msgStatusSelOn").click(function(){
     	if($(this).hasClass("msgStatusSelOff")){
		   	$(this).removeClass("msgStatusSelOff");
			$(this).find("input").attr("checked", false);
		}else{
			$(this).addClass("msgStatusSelOff");
			$(this).find("input").attr("checked", true);
		}
     	userNoticeSet($(this).find("input"));
   	});
	
	//设置批量处理按钮状态
	checkUserNoticeSet("1","0",sendModeId);
	checkUserNoticeSet("1","1",sendModeId);
	checkUserNoticeSet("2","0",sendModeId);
	checkUserNoticeSet("2","1",sendModeId);
	
	var sysAllSetId_html= $.trim($("#sysAllSetId").html());
	var personAllSetId_html = $.trim($("#personAllSetId").html());
	if(sysAllSetId_html == ""){
		$("#sysAllSetId").html("全部打开");
	}
	if(personAllSetId_html == ""){
		$("#personAllSetId").html("全部打开");
	}
});
</script>
<!--提示结束-->
<div class="newMsgContentWrap" >
	<!--大类开始-->
	<%
		if (("true".equalsIgnoreCase(smsSysFlag) && sendModeId
				.equals(ServiceConstants.NOTICE_PERSON_SEND_SMS))
				|| ("true".equalsIgnoreCase(emailSysFlag) && sendModeId
						.equals(ServiceConstants.NOTICE_PERSON_SEND_MAIL))
				|| sendModeId
						.equals(ServiceConstants.NOTICE_PERSON_SEND_SYS)) {
	%>
	<dl noticeType='1'>
		<!--<dt><input id="buttonOne" name="" type="button" value="" class="btn_open"/><span class="open">公告类</span></dt>
		-->
		<dt><h5 class="fleft">系统公告</h5> <a href="javascirpt:void(0);" id="sysAllSetId" class="allOpen" onclick="openAll('isModel');return false;"></a></dt>
		<!--小类开始-->
		<c:forEach items="${noticeSets}" var="po" varStatus="st">
			<c:choose>
			<c:when test="${po.id.noticeType == '1'}">
			<dd>
				<h6>${po.typeName}</h6>
				 <c:choose>
                 <c:when test="${po.isReceive=='1'}">
					<div class="msgStatusSelOn" >
						<input type="radio" name="isModel" noticeId='${po.id.noticeId}' noticeType='${po.id.noticeType}' isSuccess='${po.id.isSuccess}' class="hidden" value="1"/>
					</div>
                 </c:when>
                </c:choose>
                <c:choose>
                <c:when test="${po.isReceive=='0'}">
                	<div class="msgStatusSelOn msgStatusSelOff" >
						<input type="radio" name="isModel" noticeId='${po.id.noticeId}' noticeType='${po.id.noticeType}' isSuccess='${po.id.isSuccess}' class="hidden" value="1" checked="checked">
					</div>
                </c:when>
                </c:choose>
				<p  class="fleft">
					${po.typeDesc}
				</p>
				</dd>
           </c:when>
           </c:choose>
		</c:forEach>
		<!--小类结束-->
	</dl>
	<%
		}
	%>
<!--大类结束-->
<!--大类开始-->
	<dl noticeType='2'>
		<!--<dt><input id="buttonTwo" name="" type="button" value="" class="btn_close"/><span class="open">个人通知类</span></dt>
		-->
		<dt><h5 class="fleft">个人通知</h5> <a href="javascirpt:void(0);" id="personAllSetId" class="allOpen"   onclick="openAll('isSaveModel');return false;"></a></dt>
		<!--小类开始-->
           <c:forEach items="${noticeSets}" var="po" varStatus="st">
           <c:choose>
           <c:when test="${po.id.noticeType == '2'}">
			<dd>
		 		<h6>${po.typeName}</h6>
		 		<c:choose>
                 <c:when test="${po.isReceive=='1'}">
					 <div class="msgStatusSelOn" >
					   <input type="radio" name="isSaveModel" noticeId='${po.id.noticeId}' noticeType='${po.id.noticeType}' isSuccess='${po.id.isSuccess}'  class="hidden" value="1"/>
					 </div>
                 </c:when>
                 </c:choose>
                 <c:choose>
                	<c:when test="${po.isReceive=='0'}">
                		<div class="msgStatusSelOn msgStatusSelOff" >
					   		<input type="radio" name="isSaveModel" noticeId='${po.id.noticeId}' noticeType='${po.id.noticeType}' isSuccess='${po.id.isSuccess}'  class="hidden" value="1"/>
					 	</div>
                	</c:when>
                </c:choose>
					 <p  class="fleft">
					   ${po.typeDesc}
					 </p>
			</dd>
           </c:when>
           </c:choose>
           </c:forEach>
		<!--小类结束-->
	</dl>
<!--大类结束-->
   </div>
<!-- mainCT end -->
<div class="clear"></div>

