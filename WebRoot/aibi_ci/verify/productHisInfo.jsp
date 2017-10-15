<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<chart caption='查全率与提升倍数'  yAxisName='倍数︵倍︶' palette="2" baseFontSize="12" shownames="1" showvalues="0" useRoundEdges="1" legendBorderAlpha="0" bgColor="ffffff" yAxisNameWidth="16" 
		showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei" rotateYAxisName="0"
		canvasBorderColor="#cccccc" formatNumberScale='0' showShadow="0" anchorBgColor="#6BCF39"  anchorBorderColor="F8F8F8" anchorRadius="5" anchorBorderThickness="3" lineThickness='2' labelDisplay='ROTATE' slantLabels='1' lineColor="#6BCF39">
<categories >
<category label='' />
<c:forEach items="${ciVerifyMmList}" var="verifyMm">
<category label="${dateFmt:dateFormat(verifyMm.dataDate)}" />
</c:forEach>
<category label='' />
</categories>
<dataset seriesName='提升倍数' >
<set />
<c:forEach items="${ciVerifyMmList}" var="verifyMm">
<set value='${verifyMm.upgradeMultiple}' />
</c:forEach>
<set />
</dataset>

<dataset seriesName='查全率' color='F1683C'  anchorBgColor='F1683C'>
<set />
<c:forEach items="${ciVerifyMmList}" var="verifyMm">
  <set value='${verifyMm.recall}' />
</c:forEach>
<set />
</dataset>
	<styles>                
		<definition>
			<style name='CaptionFont' type='font' size='12'/>
		</definition>
		<application>
			<apply toObject='CAPTION' styles='CaptionFont' />
			<apply toObject='SUBCAPTION' styles='CaptionFont' />
		</application>
	</styles>
</chart>


