<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>	
<chart caption="" showShadow="0" plotFillAlpha="90" showPlotBorder="0" canvasBorderColor="CCCCCC" plotFillRatio="100"
 baseFontSize="12" yAxisNameWidth="16" yAxisName='' formatNumberScale='0'
shownames="1" showvalues="0" rotateYAxisName="1" useRoundEdges="0" legendBorderAlpha="0" bgColor="ffffff" showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei">
	
	<c:forEach items="${labelFormList}" var="cityForm">
		<set label='${cityForm.cityName }' value='${cityForm.customNum }' link="javascript:getMoreCityTrendChart('${cityForm.cityId}');" />
	</c:forEach>

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