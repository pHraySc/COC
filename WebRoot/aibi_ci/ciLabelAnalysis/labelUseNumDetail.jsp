<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@ include file="/aibi_ci/html_include.jsp"%>



<script type="text/javascript">
$(document).ready( function() {
	  $(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	 });
	 pagetable();
	 $("#labelTrendTable .mainTable a.push_link").click(function(){
			var toffset=$(this).offset();
			$(this).siblings(".form-field").css({top:toffset.top+18,left:toffset.left-32});
			$(this).siblings(".form-field").show();
			$(this).addClass("push_link_on");
			$(document).click(function (event) {$(".form-field").hide();$("a.push_link").removeClass("push_link_on");}); 
			return false;
	});
});

function pagetable(){
    var labelId = '${param["labelId"]}';   
    var actionUrl = $.ctx+'/ci/ciLabelInfoAction!useTimesDetail.ai2do?labelId='+labelId+'&'+$("#pageForm").serialize()+'';
    $("#labelTrendTable").page( {url:actionUrl,param:'',objId:'labelTrendTable',callback:false});
    $(".commonTable mainTable").datatable();
}
</script>
</head>

<body>
	<div id="labelTrendTable" >
		<table width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0">
			<tr>
				<th>用户名</th>
				<th>部门名称</th>
				<th>使用类型</th>
				<th>使用时间</th>
			</tr>
            <c:forEach items="${pager.result}" var="po" varStatus="st">
            <c:choose>
            <c:when test="${st.index % 2 == 0 }">
            <tr class="even">
            </c:when>
            <c:otherwise>
            <tr class="odd">
            </c:otherwise>
            </c:choose>
				<td>${po.userName}</td>
				<td>${po.deptName}</td>
				<td>${po.labelUseTypeName}</td>
				<td class="align_right"><fmt:formatDate value="${po.useTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				
			</tr>
            </c:forEach>
		</table>
		<div class="pagenum" id="pager">
        <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
        </div>
     </div>
</body></html>
