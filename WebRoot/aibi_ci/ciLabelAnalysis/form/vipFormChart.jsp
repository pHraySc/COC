<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>	
<chart showLabels="1" showLegend="1" caption="" useHoverColor="0" pieRadius="120" doughnutRadius="90" showZeroPies="1" slicingDistance="0" enableRotation="1" showPercentageValues='1' palette="5" use3DLighting="0" showPlotBorder="1" showShadow="0" baseFontSize="12" 
shownames="1" showvalues="1" animation="1" legendBorderAlpha="0" borderColor="FFFFFF" plotBorderColor="FFFFFF" bgColor="ffffff" showBorder="0" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei">

   <category label='总用户数<fmt:formatNumber type="number" value="${ciLabelFormModel.customNum}"  pattern="###,###,###" />' Color='FFFFFF' link='javascript:getVipLevelFormHisChart();' hoverText="查看趋势">
		<c:forEach items="${labelFormList}" var="vipForm">
			<c:if test="${vipForm.customNum != 0 && ciLabelFormModel.customNum != 0}">
				<category color="${vipForm.color}" label='<fmt:formatNumber type="number" value="${(vipForm.customNum/ciLabelFormModel.customNum)*100}"  pattern="###,###,###.##" />%' value="${vipForm.customNum}"  link="javascript:getVipTrendChartByVipLevelId('${vipForm.vipLevelId}');" hoverText='点击查看${vipForm.vipLevelName} <fmt:formatNumber type="number" value="${(vipForm.customNum/ciLabelFormModel.customNum)*100}"  pattern="###,###,###.##" />% 近六个月趋势'/>
			</c:if>
			<c:if test="${vipForm.customNum == 0 || ciLabelFormModel.customNum == 0}">
				<category label='' value="${vipForm.customNum}"  link="javascript:getVipTrendChartByVipLevelId('${vipForm.vipLevelId}');" hoverText='点击查看${vipForm.vipLevelName} <fmt:formatNumber type="number" value="${(vipForm.customNum/ciLabelFormModel.customNum)*100}"  pattern="###,###,###.##" />% 近六个月趋势'/>
			</c:if>
		</c:forEach>
    </category>
	
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