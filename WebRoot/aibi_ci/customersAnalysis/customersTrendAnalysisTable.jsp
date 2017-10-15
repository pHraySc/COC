<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<%
	String alarmMenu = Configure.getInstance().getProperty(
			"AlARM_MENU");
%>
<script type="text/javascript">
$(document).ready( function() {
	$(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	 });
	 $("#customersTrendTable .mainTable a.push_link").click(function(){
			var toffset=$(this).offset();
			$(this).siblings(".form-field").css({top:toffset.top+18,left:toffset.left-32});
			$(this).siblings(".form-field").show();
			$(this).addClass("push_link_on");
			$(document).click(function (event) {$(".form-field").hide();$("a.push_link").removeClass("push_link_on");}); 
			return false;
	});

	//点击告警详情关闭
		$(".tag_main_icon .slideDown .close").click(function(){
			$(this).parents(".slideDown").hide();
			if($(this).parents(".slideDown").hasClass("alarm_detail")){
				$(".tag_main_icon .relative_help").hide();
				$(".tag_main_icon .basis_help").hide();
			};
		});
		$(".tag_main_icon .icon_relative_help").click(function(){
			var offset=$(this).offset();
			$(".tag_main_icon .relative_help").css({top:offset.top + 18,left:offset.left-240});
			$(".tag_main_icon .relative_help").show();

		});
		$(".tag_main_icon .icon_basis_help").click(function(){
			var offset=$(this).offset();
			$(".tag_main_icon .basis_help").css({top:offset.top + 18,left:offset.left-240});
			$(".tag_main_icon .basis_help").show();
		});
});

function showWaringInfo(){
	var customGroupId = $('#customGroupId', parent.document).val();
	var dataDate = $('#dataDate', parent.document).val();
    var actionUrl = $.ctx + "/ci/ciCustomersAlarmAction!findCustomersAlarmRecord.ai2do";
    $.ajax({
        url: actionUrl,
        type: "POST",
        data: {
            "customRelModel.customGroupId" : customGroupId,
            "customRelModel.dataDate" : dataDate
        },
        success: function(result){
            flag = result.success;
            if(flag){
                var levelTr = '<td><strong>告警</strong></td>';
                var thresholdTr = '<td><strong>告警阈值</strong></td>';
                var realValueTr = '<td><strong>实际值</strong></td>';
                for(i=0;i<2;i++){
                    var levelStr = result.result[i].level;
                    var levelTd = "";
                    if(levelStr == "未设置"){
                        levelTd = '<td>未设置</td>';
                    }else if(levelStr=="正常"){
                        levelTd = '<td class="green">正常</td>';
                    }else{
                        levelTd = '<td class="red">'+levelStr+'</td>';
                    }
                    levelTr = levelTr + levelTd;

                    thresholdTr = thresholdTr + '<td>'+ result.result[i].threshold + '</td>';
                    realValueTr = realValueTr + '<td  class="align-right">'+result.result[i].realValue +'</td>';
                }
                $("#level").empty();
                $("#level").append(levelTr);
                $("#threshold").empty();
                $("#threshold").append(thresholdTr);
                $("#realValue").empty();
                $("#realValue").append(realValueTr);
                /* var num = "";
                 if((result.msg != null && result.msg != "") || result.msg == 0){
                 num = $.formatNumber(result.msg, 3, ',');
                 } */

                $(".tag_main_icon .alarm_detail").show();
            }else{
                window.parent.showAlert(result.msg,"failed");
            }
        }
    });

}
	

</script>
</head>

<body>
	<div id="customersTrendTable" >
		<table width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0">
			<tr>
				<th class="header">时间</th>
				<%if("true".equals(alarmMenu)){ %>
				<th>预警</th>
				<%} %>
				<th class="header headerSortUp">用户数</th>
			   
				<!-- <th class="header headerSortDown" style="border-right:none;">使用次数</span></th> -->
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
            	
				<td class="align_right">${dateFmt:dateFormat(po.dataDate)}</td>
				<%if("true".equals(alarmMenu)){ %>
				<td>
				<c:if test="${po.alarm == '告警'}">
					<a href="javascript:;" onclick="showWaringInfo()"><font color="red">${po.alarm}</font></a>
					</c:if>
					<c:if test="${po.alarm == '正常'}">
					${po.alarm}
					</c:if>
				</td>
				<%} %>
				<td class="align_right"><fmt:formatNumber type="number" value="${po.value}"  pattern="###,###,###" /></td>
				
				
			</tr>
            </c:forEach>
            
		</table>
		<!--icon 根据实际情况显示 -->
						<div class="tag_main_icon">
		
                            <!-- 告警弹出层start -->
                            <div class="slideDown alarm_detail">
                                <dl>
                                    <dt><span class="close"></span>告警详情</dt>
                                    <dd>
                                        <table width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0" id="alarmDetailTable">
                                            <tr class="even">
                                                <th width="78px">&nbsp;</th>
                                                <th width="110px">基础</th>
                                                <th width="110px">环比<img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" class="icon_relative_help"/></th>
                                            </tr>
                                            <tr id="level" class="odd"></tr>
                                            <tr id="threshold" class="even"></tr>
                                            <tr id="realValue" class="odd"></tr>

                                        </table>
                                    </dd>
                                </dl>
                            </div>
                            <div class="slideDown relative_help">
                                <dl>
                                    <dt><span class="close"></span></dt>
                                    <dd>
                                        环比预警：将客户群当前统计周期的用户数与上一周期用户数进行比较，针对差值进行预警<br/>
                                        <span>环比增长量 = 客户群当前统计周期用户数 - 客户群上一周期用户数</span>
                                    </dd>
                                </dl>
                            </div>
                            <!-- 告警弹出层end -->
						</div>
		<div class="pagenum" id="pager">
        <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
        </div>
     </div>
     
</body>
</html>
