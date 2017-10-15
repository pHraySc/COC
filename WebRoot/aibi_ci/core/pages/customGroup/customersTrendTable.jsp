<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%
String pushButton = Configure.getInstance().getProperty("PUSH_BUTTON"); 
String alarmMenu = Configure.getInstance().getProperty("AlARM_MENU");
String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>

<script type="text/javascript">
$(document).ready( function() {
	$(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	});
	pagetable();
});
	
function pagetable(){
	var customGroupId = '${param["customGroupId"]}';
	var dataDate = '${param["dataDate"]}'; 
	var actionUrl = $.ctx+'/ci/customersTrendAnalysisAction!findCustomerTrendTable.ai2do?customGroupId='+customGroupId+'&dataDate='+dataDate+$("#pageForm").serialize()+'';
	$("#labelTrendTable").page( {url:actionUrl,objId:'labelTrendTable',callback:pagetable});
	$(".commonTable mainTable").datatable();
}
//生成客户群文件
function createCustomersFile(listTableName, el){
	if($.trim($(el).html()) == "文件生成中"){
		return false;
	} 
	var actionUrl = $.ctx + "/ci/customersFileAction!createCustomersFile.ai2do?listTableName="+listTableName;
	$.ajax({
		type: "POST",
		url: actionUrl,
		success: function(result){
			if(result.success){
				$.fn.weakTip({"tipTxt":result.msg, timer:4000});
				$(el).html("文件生成中");
			}else{
				$.fn.weakTip({"tipTxt":result.msg, timer:4000});
			}
		}
	});
}
//客户群文件下载
function downCustomersFile(listTableName){
	var actionUrl = $.ctx + "/ci/customersFileAction!downCustomersFile.ai2do?listTableName="+listTableName;
	$.ajax({
		type: "POST",
		url: actionUrl,
		success: function(result){
		}
	});
}
</script>
</head>
<body>
	<div id="labelTrendTable" >
		<table width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0">
			<tr>
				<th class="header">时间</th>
				<th class="header headerSortUp">用户数</th>
				<%if("true".equals(alarmMenu)){ %>
				<th>告警</th>
				<%} %>
				<th class="header headerSortDown" style="border-right:none;">操作</th>
			</tr>
            <c:forEach items="${pager.result}" var="po" varStatus="st">
               <tr
            <c:choose>
            <c:when test="${st.index % 2 == 0 }">
                class="even"
            </c:when>
            <c:otherwise>
               class="odd"
            </c:otherwise>
            </c:choose>
                >
			<c:if test="${po.dataDate == null}">
					<td style="line-height:16px;">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<%if("true".equals(alarmMenu)){ %> 
					<td>&nbsp;</td>
					<%} %>
				</c:if>
				<c:if test="${po.dataDate != null}">
				<td>${dateFmt:dateFormat(po.dataDate)}</td>
				<c:choose>
						<c:when test="${po.value == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber type="number" value="${po.value}"  pattern="###,###,###" /></td>
						</c:otherwise>
					</c:choose>
				<%if("true".equals(alarmMenu)){ %>
				
				<c:choose>
					<c:when test="${po.alarm == null}">
						<td>&nbsp;</td>
					</c:when>
					<c:otherwise>
						<td>${po.alarm}&nbsp;</td>
					</c:otherwise>
				</c:choose>
				
				<%} %>
				<td>
					<%if(("true").equals(customersFileDown)){ %>
						<%if(coc_province.equals("zhejiang")){ %>
							<!-- 浙江客户群下载 -->
		                    <a class="icon_button_oper" id="downloadFile" style="line-height:16px; display: true" href="javascript:void(0);" onclick="parent.showDownloadApprove('${po.listTableName}')">下载</a>
						<%}else{%>
							<c:if test="${po.fileCreateStatus == null}">
							<a class="icon_button_oper" id="downFile" style="line-height:16px; display: true" href="javascript:void(0);" onclick="createCustomersFile('${po.listTableName}',this);">
							生成文件</a></c:if>
							<c:if test="${po.fileCreateStatus == 2}">
							<a class="icon_button_oper" style="line-height:16px; display: true" href="javascript:void(0);" onclick="createCustomersFile('${po.listTableName}',this);">
							文件生成中</a>
							</c:if>
							<c:if test="${po.fileCreateStatus == 3}">
							<a class="icon_button_oper" style="line-height:16px;" href="javascript:void(0);" onclick="downCustomersFile('${po.listTableName}')" id="downFile" style="line-height:16px;">
							下载</a></c:if>
						<%}%>
					<%}%>
				</td>
			</c:if>
			</tr>
            </c:forEach>
		</table>
		<div class="pagenum" id="pager">
        	<jsp:include page="/aibi_ci/page_dialog_html.jsp"/>
        </div>
     </div>
</body></html>
