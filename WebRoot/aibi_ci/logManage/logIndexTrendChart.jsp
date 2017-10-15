<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<chart palette="2" scrollPadding="8" baseFontSize="12" 
		shownames="1" showvalues="0" useRoundEdges="1" legendBorderAlpha="0" bgColor="ffffff" yAxisNameWidth="16" 
		showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei" rotateYAxisName="0"
		canvasBorderColor="#bbbbbb" formatNumberScale='0' showShadow="0" anchorBgColor="#6BCF39"  anchorBorderColor="F8F8F8" anchorRadius="5" 
		anchorBorderThickness="3" lineThickness='1' labelDisplay='ROTATE' slantLabels='1' lineColor="#6BCF39" >
	<categories>
		<c:if test="${fn:length(resultListChar) > 4}">
			<category></category>
		</c:if>
		
		<c:forEach items="${resultListChar}" var="map1">
				<c:if test="${map1.week == true}">
					<category label="${dateFmt:dateFormat(map1.dataDate)}(周末)"/>
				</c:if>
				<c:if test="${map1.week == false}">
					<category label="${dateFmt:dateFormat(map1.dataDate)}" />
				</c:if>
		</c:forEach>	
	</categories>
	<dataset seriesName="">
		<c:if test="${fn:length(resultListChar) > 4}">
			<set></set>
		</c:if>
		
		<c:forEach items="${resultListChar}" var="map2">
				<c:if test="${map2.week == true}">
					<set value="${map2.opTimes }"  toolText="${typeName },${dateFmt:dateFormat(map2.dataDate)}(节假日),${map2.opTimes}" anchorRadius='6' anchorBgColor='3DEE11'/>			
				</c:if>
				<c:if test="${map2.week == false}">			
					<set value="${map2.opTimes }" />	
				</c:if>		
		</c:forEach>
	</dataset>
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