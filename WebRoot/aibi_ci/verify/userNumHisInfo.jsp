<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<chart caption='潜在用户数'  yAxisName='用户数︵人︶'  palette="2" baseFontSize="12" shownames="1" showvalues="0" useRoundEdges="1" legendBorderAlpha="0" bgColor="ffffff" yAxisNameWidth="16" 
		showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei" rotateYAxisName="0"
		canvasBorderColor="#cccccc" formatNumberScale='0' showShadow="0" anchorBgColor="#6BCF39"  anchorBorderColor="F8F8F8" anchorRadius="5" anchorBorderThickness="3" lineThickness='2' labelDisplay='ROTATE' slantLabels='1' lineColor="#6BCF39">

<set/>
<c:forEach items="${ciVerifyMmList}" var="verifyMm">
<set label="${dateFmt:dateFormat(verifyMm.dataDate)}"  value='${verifyMm.potentialUserNum}' />
</c:forEach>
<set/>
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


