<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>

<script type="text/javascript">
$(function(){
	$("#log_tab li").mouseenter(function(){
			if(!$(this).hasClass("on")){
				$(this).addClass("hover_li");
			}
		}).mouseleave(function(){
			$(this).removeClass("hover_li");
		}).click(function(){
			if(!$(this).hasClass("on")){
				$(this).addClass("on").removeClass("hover_li").siblings(".on").removeClass("on");
				var _index=$(this).parent().find("li").index($(this)[0]);
				$(this).parent().next(".log_tabct").find(".box").eq(_index).addClass("box_on").siblings(".box_on").removeClass("box_on");
			}
			
		})
		init();
		
	})
function init() {
	var id = $("#log_tab .on").attr("id");
	if(id != undefined) {
		id  = id.substr(1,id.length);
		loadLogTrendChart(id);
	}	
}

function loadLogTrendChart(opTypeId) {
	var beginDate = $("#test1",parent.document).val();
	var endDate = $("#test2",parent.document).val();
	$("#opTypeFlag",parent.document).val(opTypeId);//记录点击哪个
	var noData = '无统计数据';
    var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
	var url=escape($.ctx+'/ci/ciLogStatAnalysisAction!findOpTypeChar.ai2do?ciLogStatAnalysisModel.beginDate='+beginDate+'&ciLogStatAnalysisModel.endDate='+endDate+'&ciLogStatAnalysisModel.opTypeId='+opTypeId);
	var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/ScrollLine2D.swf"+chartParam,"abc","100%","500", "0", "1");
	bar.addParam("wmode","Opaque");
	bar.setDataURL(url);	
	bar.render("d"+opTypeId);
}
</script>
</head>
<body>
	
	<ul id="log_tab" class="log_tab">
		<c:forEach items="${resultList}" var="model" varStatus="status">
			<c:choose>
				<c:when test="${opTypeId != ''}">
					<c:if test="${model.opTypeId == opTypeId}"><li class="on" id="a${model.opTypeId}" onclick="loadLogTrendChart('${model.opTypeId}')"></c:if>
					<c:if test="${model.opTypeId != opTypeId}"><li id="a${model.opTypeId}" onclick="loadLogTrendChart('${model.opTypeId}')"></c:if>
					${model.opTypeName}<p><fmt:formatNumber type="number" value="${model.opTimes}"  pattern="###,###,###" /></p></li>
				</c:when>
				<c:otherwise>
					<c:if test="${status.index == 0}"><li class="on" id="a${model.opTypeId}" onclick="loadLogTrendChart('${model.opTypeId}')"></c:if>
					<c:if test="${status.index != 0}"><li id="a${model.opTypeId}" onclick="loadLogTrendChart('${model.opTypeId}')"></c:if>
						${model.opTypeName}<p><fmt:formatNumber type="number" value="${model.opTimes}"  pattern="###,###,###" /></p></li>
				</c:otherwise>			
			</c:choose>
		</c:forEach>
	</ul>
	<div class="log_tabct">
		<c:forEach items="${resultList}" var="model" varStatus="status">
		
		<c:choose>
			<c:when test="${opTypeId != ''}">
				<c:if test="${model.opTypeId == opTypeId}"><div class="box box_on" id="d${model.opTypeId}" ></div></c:if>
				<c:if test="${model.opTypeId != opTypeId}"><div class="box" id="d${model.opTypeId}"></div></c:if>			
			</c:when>
			<c:otherwise>
				<c:if test="${status.index == 0}"><div class="box box_on" id="d${model.opTypeId}" ></div></c:if>
				<c:if test="${status.index != 0}"><div class="box" id="d${model.opTypeId}"></div></c:if>
			</c:otherwise>
		</c:choose>	
		</c:forEach>
	</div>	
</body>
</html>
