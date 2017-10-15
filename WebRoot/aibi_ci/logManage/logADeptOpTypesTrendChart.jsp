<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<chart palette="2" baseFontSize="12" shownames="1" showvalues="0" useRoundEdges="1" legendBorderAlpha="0" bgColor="ffffff" yAxisNameWidth="16" 
		showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei" rotateYAxisName="0"
		canvasBorderColor="#bbbbbb" formatNumberScale='0' showShadow="0"  anchorRadius="4" 
		anchorBorderThickness="3" lineThickness='2' labelDisplay='ROTATE' slantLabels='1' scrollPadding="8">
	<categories>
	    <category label="" />
		<c:forEach items="${deptMultOpTypesChartList}" var="map">
		<category label="${dateFmt:dateFormat(map['DATA_DATE']) }" />
		</c:forEach>
    </categories>
   
   <c:forEach items="${headStrs}" var="headId" varStatus="status">
   		<c:set var="OP_TIMES" value="OP_TIMES_${status.count }"></c:set>
   		<dataset seriesName="${dim:toName(DIM_OP_LOG_TYPE,headId) }">
   		    <set value=""/>
   			<c:forEach items="${deptMultOpTypesChartList}" var="resultMap">
   			<set value="${resultMap[OP_TIMES] }"/>
   			</c:forEach>
   		</dataset>
   </c:forEach>

<styles>
      <definition>
          <style name="CaptionFont" type="font" font="Microsoft YaHei" size="14" color="666666" bold="1" />
          <style name="AxisNameFont" type="font" font="Microsoft YaHei" size="12" color="666666" italic="0"/>
      </definition>
      <application>
          <apply toObject="Caption" styles="CaptionFont" />
          <apply toObject="yAxisName" styles="AxisNameFont" />
          <apply toObject="XAxisName" styles="AxisNameFont" />
      </application>
</styles>
</chart>