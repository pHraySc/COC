<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<?xml version="1.0" encoding="gbk"?>
<c:set var="DIM_LABEL" value="<%=CommonConstants.ALL_EFFECTIVE_LABEL_MAP%>"></c:set>
<chart caption=""  formatNumberScale='0' palette='2' xAxisMinValue='0' xAxisMaxValue='100' yAxisMinValue='0' yAxisMaxValue='100' is3D='0' viewMode='0'
 enableLink='1' showFormBtn='0' useRoundEdges="0" showToolTip="1" canvasBorderColor="FFFFFF" legendBorderAlpha="0" borderColor="FFFFFF" 
 bgColor="ffffff" showBorder="0" baseFontSize="14" outCnvBaseFontSize="14" baseFont="Microsoft YaHei" outCnvBaseFont="Microsoft YaHei">

   <dataset showPlotBorder="0">
      <set x='50' y='50' radius='50' shape='circle' name='${fn:replace(fn:replace(ciCustomRelModel.customGroupName,">","&gt;"),"<","&lt;")} <fmt:formatNumber type="number" value="${ciCustomRelModel.mainCustomUserNum}" pattern="###,###,###" />' color='6CA9DC' id='${ciCustomRelModel.customGroupId}'/>
   </dataset>
   <c:if test="${!empty _40PerRelList}">
	   <dataset showPlotBorder="1" plotBorderColor="FB9499" plotBorderThickness="4" >
	   		<c:forEach items="${_40PerRelList}" var="relCustomGroup">
	   			<set x='${relCustomGroup.x}' y='${relCustomGroup.y}' radius='${relCustomGroup.radius}' shape='circle' 
	   			 toolText=' 关联度为${relCustomGroup.proportion}% &#xA; 用户数为<fmt:formatNumber type="number" value="${relCustomGroup.overlapUserNum}" pattern="###,###,###" /> &#xA; 关联度   = 客户群和标签"${fn:replace(fn:replace(relCustomGroup.relLabelName,">","&gt;"),"<","&lt;")}"的交集用户数 / 主标签用户数 &#xA; 点击将交集客户保存为客户群'
	   			 name='${fn:replace(fn:replace(relCustomGroup.relLabelName,">","&gt;"),"<","&lt;")}&#xA;<fmt:formatNumber type="number" value="${relCustomGroup.overlapUserNum}" pattern="###,###,###" />&#xA;${relCustomGroup.proportion}%' color='FFFFFF' id='${relCustomGroup.relLabelId}' 
	   			 link="javascript:toSaveCustom('${ciCustomRelModel.customGroupId}','${relCustomGroup.relLabelId}',${relCustomGroup.overlapUserNum});"/>
	   		</c:forEach>
	   </dataset>
   </c:if>
   <c:if test="${!empty _70PerRelList}">
	   <dataset showPlotBorder="1" plotBorderColor="A3E184" plotBorderThickness="4">
	   		<c:forEach items="${_70PerRelList}" var="relCustomGroup">
	   			<set x='${relCustomGroup.x}' y='${relCustomGroup.y}' radius='${relCustomGroup.radius}' shape='circle' 
	   			 toolText=' 关联度为${relCustomGroup.proportion}% &#xA; 用户数为<fmt:formatNumber type="number" value="${relCustomGroup.overlapUserNum}" pattern="###,###,###" /> &#xA; 关联度   = 客户群和标签"${fn:replace(fn:replace(relCustomGroup.relLabelName,">","&gt;"),"<","&lt;")}"的交集用户数 / 主标签用户数 &#xA; 点击将交集客户保存为客户群'
	   			 name='${fn:replace(fn:replace(relCustomGroup.relLabelName,">","&gt;"),"<","&lt;")}&#xA;<fmt:formatNumber type="number" value="${relCustomGroup.overlapUserNum}" pattern="###,###,###" />&#xA;${relCustomGroup.proportion}%' color='FFFFFF' id='${relCustomGroup.relLabelId}'
	   			  link="javascript:toSaveCustom('${ciCustomRelModel.customGroupId}','${relCustomGroup.relLabelId}',${relCustomGroup.overlapUserNum});"/>
	   		</c:forEach>
	   </dataset>
   </c:if>
   <c:if test="${!empty _100PerRelList}" >
	   <dataset showPlotBorder="1" plotBorderColor="D2E6F6" plotBorderThickness="4" fontSize="">
	   		<c:forEach items="${_100PerRelList}" var="relCustomGroup">
	   			<set x='${relCustomGroup.x}' y='${relCustomGroup.y}' radius='${relCustomGroup.radius}' shape='circle' 
	   			 toolText=' 关联度为${relCustomGroup.proportion}% &#xA; 用户数为<fmt:formatNumber type="number" value="${relCustomGroup.overlapUserNum}" pattern="###,###,###" /> &#xA; 关联度   = 客户群和标签"${fn:replace(fn:replace(relCustomGroup.relLabelName,">","&gt;"),"<","&lt;")}"的交集用户数 / 主标签用户数 &#xA; 点击将交集客户保存为客户群'
	   			 name='${fn:replace(fn:replace(relCustomGroup.relLabelName,">","&gt;"),"<","&lt;")}&#xA;<fmt:formatNumber type="number" value="${relCustomGroup.overlapUserNum}" pattern="###,###,###" />&#xA;${relCustomGroup.proportion}%' color='FFFFFF' id='${relCustomGroup.relLabelId}'
	   			  link="javascript:toSaveCustom('${ciCustomRelModel.customGroupId}','${relCustomGroup.relLabelId}',${relCustomGroup.overlapUserNum});"/>
	   		</c:forEach>
	   </dataset>
   </c:if>
   <c:if test="${!empty customRelList}">
	   <connectors color='D7E9F8'>
	   		<c:forEach items='${customRelList}' var="relLabel">
	   			<connector label='<fmt:formatNumber type="number" value="${relLabel.proportion}" pattern="#,###.##" />%' from='${ciCustomRelModel.customGroupId}' to='${relLabel.relLabelId}' arrowAtStart="0" arrowAtEnd="0"/>
	   		</c:forEach>
	   </connectors>
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
          <apply toObject='DATALABELS' styles='AxisNameFont' />
      </application>
</styles>
</chart>