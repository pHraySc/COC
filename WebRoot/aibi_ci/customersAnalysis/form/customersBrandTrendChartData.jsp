<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<chart caption="" showPercentageValues='1' showShadow="0" plotBorderAlpha="0" plotFillAlpha="90" showPlotBorder="0" canvasBorderColor="CCCCCC" plotFillRatio="100"
 baseFontSize="12" yAxisNameWidth="16" yAxisName='' formatNumberScale='0' slantLabels="1"
shownames="1" showvalues="0" rotateYAxisName="0" useRoundEdges="0" legendBorderAlpha="80" bgColor="ffffff" showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei">



  <categories>
  	<c:if test="${!empty customGroupTrendList}">
		<c:forEach items="${customGroupTrendList}" var="brandForm" begin="1" end="1">
			<c:forEach items="${brandForm.customGroupFormList}" var="brandFormTrend">
				<category label="${dateFmt:dateFormat(brandFormTrend.dataDate)}" />
			</c:forEach>
		</c:forEach>
	</c:if>
  </categories>
  	<c:if test="${!empty customGroupTrendList}">
		<c:forEach items="${customGroupTrendList}" var="brandForm">
		   <dataset seriesName='${brandForm.brandName}' color="${brandForm.color}">
				<c:forEach items="${brandForm.customGroupFormList}" var="brandFormTrend">
					<set value="${brandFormTrend.customNum }" />
				</c:forEach>
		   </dataset>
		</c:forEach>
	</c:if>
<styles>
      <definition>
          <style name='CaptionFont' type='font' font='Microsoft YaHei' size='14' color='666666' bold='1' />
          <style name='AxisNameFont' type='font' font='Microsoft YaHei' size='12' color='666666' italic='0'/>
      </definition>
      <application>
          <apply toObject='Caption' styles='CaptionFont' />
          <apply toObject='yAxisName' styles='AxisNameFont' />
          <apply toObject='XAxisName' styles='AxisNameFont' />
      </application>
</styles>
</chart>