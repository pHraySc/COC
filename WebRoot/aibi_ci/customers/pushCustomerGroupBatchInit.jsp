<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量客户群推送</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>

</head>
<body>
<form id="pushForm" method="post" action="" enctype="multipart/form-data">
				<div class="dialog_table">
					<table width="100%" class="showTable  new_tag_table" cellpadding="0" cellspacing="0">
						<tr>
							<th width="110px">推送方式：</th>
							<td>
								<c:forEach items="${ciCustomGroupInfoList}" var="po" varStatus="st">
									<input type="hidden" name="ciCustomGroupInfoList[${st.index}].customGroupId" value="${po.customGroupId}" />
								</c:forEach>
								<div class="new_tag_selectBox">
									<span id="selectCycle">
										<input type="radio" id="dayCreate" class="push_type_radio valign_middle" name="pushCycle"  value="1" checked />&nbsp;&nbsp;一次性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<c:if test="${isAllDisposable == 0}">
											<input type="radio" id="monthCreate" class="push_type_radio valign_middle" name="pushCycle"  value="2" />&nbsp;&nbsp;周期性&nbsp;
										</c:if>
									</span>
								</div>
							</td>
							<td></td>
						</tr>
						<tr>
							<th>推送平台：</th>
							<td>
								<div  class="disposable_push_platform">
									<c:forEach items="${ciSysInfoList}" var="po" varStatus="st">
										<input type="checkbox" id="dayCreate" class="valign_middle" name="sysIds"  value=<c:out value="${po.sysId}"></c:out> />&nbsp;&nbsp;<c:out value="${po.sysName}"></c:out>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</c:forEach>
								</div>
								<div class="cycle_push_platform">
									<c:if test="${isAllDisposable == 0}">
										<c:forEach items="${sysInfoCycleList}" var="po" varStatus="st">
											<input type="checkbox" id="dayCreate" class="valign_middle" name="sysIds"  value=<c:out value="${po.sysId}"></c:out> />&nbsp;&nbsp;<c:out value="${po.sysName}"></c:out>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</c:forEach>
									</c:if>
								</div>								
							</td>
						</tr>
					</table>
				</div>
				<!-- 按钮区 -->
				<div class="dialog_btn_div">
					<input id="saveBtn" name="" type="button" value="确定" class="tag_btn"/>
				</div>
				
				
		 </form>
<script type="text/javascript">
$('.cycle_push_platform').hide();
$('.disposable_push_platform').show();
$("#saveBtn").bind("click", function(){
	submitForm();
});
$('.push_type_radio').bind('click', function() {
	var checkedVal = $('.push_type_radio:checked').val();
	if(checkedVal == 1) {
		$('.cycle_push_platform').hide();
		$('.disposable_push_platform').show();
	} else {
		$('.disposable_push_platform').hide();
		$('.cycle_push_platform').show();
	}
});
//提交推送
function submitForm(){
	var actionUrl = "${ctx}/ci/customersManagerAction!pushCustomerGroupBatch.ai2do";
	$.post(actionUrl, $("#pushForm").serialize(), function(result){
		if(result.success){
			parent.showAlert(result.msg,"success");
			parent.closePushBatch();
		}else{
			var msg = result.msg;
			parent.showAlert(msg,"failed");
		}
	});
}


function hideTip(){
	$("#cnameTip").hide();;
}


</script>
</body>
</html>