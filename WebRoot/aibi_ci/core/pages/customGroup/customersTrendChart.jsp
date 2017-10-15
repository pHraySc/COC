<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ include file="/aibi_ci/include.jsp"%>

<?xml version="1.0" encoding="gbk"?>
<chart caption="用户群用户数趋势分析" palette="2" baseFontSize="12" 
		yAxisName='用户数︵人︶' shownames="1" showvalues="0" useRoundEdges="1" legendBorderAlpha="0" bgColor="ffffff" yAxisNameWidth="16" 
		showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei" rotateYAxisName="0"
		canvasBorderColor="#cccccc" formatNumberScale='0' showShadow="0" anchorBgColor="#6BCF39"  anchorBorderColor="F8F8F8" anchorRadius="5" anchorBorderThickness="3" lineThickness='2' labelDisplay='ROTATE' slantLabels='1' lineColor="#6BCF39">
    <set/>  
	<c:forEach items="${trendAnalysisList}" var="vipForm">
		<set label="${dateFmt:dateFormat(vipForm.dataDate)}" value="${vipForm.value}" />
	</c:forEach>
	<set/>
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