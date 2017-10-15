<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<?xml version="1.0" encoding="gbk"?>
<chart caption="${dim:toName(DIM_OP_LOG_TYPE,ciLogStatAnalysisModel.opTypeId) }"  yAxisName="次数︵次︶" palette="2" baseFontSize="12"  showValues="0" 
		useRoundEdges="0" legendBorderAlpha="0" bgColor="ffffff" yAxisNameWidth="16" 
		showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei" rotateYAxisName="0"
		canvasBorderColor="#cccccc" formatNumberScale="0" showShadow="0"   plotGradientColor=""
		anchorBorderColor="F8F8F8" anchorRadius="5" anchorBorderThickness="3" lineThickness="2" 
		 slantLabels="1" lineColor="#6BCF39" showPlotBorder="0">
<categories >
<category label="" />
<c:forEach items="${multDeptOptypesChartList}" var="verifyMm">
<category label="${verifyMm.secondDeptName}" />
</c:forEach>
<category label="" />
</categories>

<dataset seriesName="${dim:toName(DIM_OP_LOG_TYPE,ciLogStatAnalysisModel.parentId) }">
	<set />
		<c:forEach items="${multDeptOptypesChartList}" var="verifyMm">
			<set value="${verifyMm.opTimes1}" label="${verifyMm.secondDeptName}" link="JavaScript:showData('${verifyMm.secondDeptId }','${verifyMm.opTypeId }','${verifyMm.parentId }','0')"/>
		</c:forEach>
	<set />
</dataset>
<c:choose>
	<c:when test="${ciLogStatAnalysisModel.parentId==ciLogStatAnalysisModel.opTypeId}">
	</c:when>
	<c:otherwise>
		<dataset seriesName="${dim:toName(DIM_OP_LOG_TYPE,ciLogStatAnalysisModel.opTypeId) }">
		<set />
		<c:forEach items="${multDeptOptypesChartList}" var="verifyMm">
		<set value="${verifyMm.opTimes}" label="${verifyMm.secondDeptName}"  link="JavaScript:showData('${verifyMm.secondDeptId }','${verifyMm.opTypeId }','${verifyMm.parentId }','0')"/>
		</c:forEach>
		<set />
		</dataset>
	
	</c:otherwise>
</c:choose>
	<styles>                
		<definition>
			<style name="CaptionFont" type="font" size="12"/>
		</definition>
		<application>
			<apply toObject="CAPTION" styles="CaptionFont" />
			<apply toObject="SUBCAPTION" styles="CaptionFont" />
		</application>
	</styles>
</chart>


