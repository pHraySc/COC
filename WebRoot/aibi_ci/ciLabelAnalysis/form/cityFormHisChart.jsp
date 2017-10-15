<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<chart caption="" showPercentageValues='1' showShadow="0" showPlotBorder="0" canvasBorderColor="CCCCCC" plotFillRatio="100" 
baseFontSize="12" yAxisNameWidth="16" yAxisName='' rotateYAxisName="0" formatNumberScale='0'
shownames="1" showvalues="0"  useRoundEdges="0" legendBorderAlpha="0" bgColor="ffffff" showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei">

  <categories>
  	<c:if test="${!empty labelFormTrendList}">
		<c:forEach items="${labelFormTrendList}" var="cityForm" begin="0" end="0">
			<c:forEach items="${cityForm.labelFormList}" var="cityFormTrend">
				<category label="${dateFmt:dateFormat(cityFormTrend.dataDate)}" />
			</c:forEach>
		</c:forEach>
	</c:if>
  </categories>
  	<c:if test="${!empty labelFormTrendList}">
		<c:forEach items="${labelFormTrendList}" var="cityForm">
		   <dataset seriesName='${cityForm.cityName}' color="${cityForm.color}">
				<c:forEach items="${cityForm.labelFormList}" var="cityFormTrend">
					<set value='${cityFormTrend.customNum }' />
				</c:forEach>
		   </dataset>
		</c:forEach>
	</c:if>
<styles>
      <definition>
          <style name='CaptionFont' type='font' font='Microsoft YaHei' size='14' color='666666' bold='1' />
          <style name='AxisNameFont' type='font' font='Microsoft YaHei' size='12' color='666666' italic='0'/>
          <style name='AxisNameFont' type='font' font='Microsoft YaHei' size='14' color='666666' italic='0'/>
      </definition>
      <application>
          <apply toObject='Caption' styles='CaptionFont' />
          <apply toObject='yAxisName' styles='AxisNameFont' />
          <apply toObject='XAxisName' styles='AxisNameFont' />
      </application>
</styles>
</chart>