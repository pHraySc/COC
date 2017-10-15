<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>柱状图</title> 
<script type="text/javascript">
	$(document).ready(function(){
		
		loadProductTrendChart();
		$("input[name='opTypeName']").change(function(){

			var beginDate=$("#beginDate").val();
			var endDate=$("#endDate").val();
		    var parentId=$("#parentId").val();
		    var opTypeId=$("#opTypeId").val();
		    var secondDeptId=$("#secondDeptId").val();
		    var thirdDeptId=$("#thirdDeptId").val();
            //重新赋值
        	opTypeId= $("input[name='opTypeName']:checked").val();
        	
        	var para='ciLogStatAnalysisModel.beginDate='+beginDate
        	+'&ciLogStatAnalysisModel.endDate='+endDate
        	+'&ciLogStatAnalysisModel.parentId='+parentId
        	+'&ciLogStatAnalysisModel.opTypeId='+opTypeId
        	+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId
        	+'&ciLogStatAnalysisModel.thirdDeptId='+thirdDeptId;
        	var url=escape($.ctx+'/ci/ciLogStatAnalysisAction!findOneOpLogTypeSpread.ai2do?'+para);
			
			var noData = '无统计数据';
            var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
			$("#chart").show();
			var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MSColumn2D.swf"+chartParam, "",$('#chart').width(), $('#chart').height(), "0", "0");
			bar.addParam("wmode","Opaque");
	    	bar.setDataURL(url);
	    	bar.render("chart");
        	
          });
	});
	//产品趋势图
	function loadProductTrendChart(){
		
		var beginDate=$("#beginDate").val();
		var endDate=$("#endDate").val();
	    var parentId=$("#parentId").val();
	    var opTypeId=$("#opTypeId").val();
	    var secondDeptId=$("#secondDeptId").val();
	    var thirdDeptId=$("#thirdDeptId").val();
	    
		var para='ciLogStatAnalysisModel.beginDate='+beginDate
		+'&ciLogStatAnalysisModel.endDate='+endDate
		+'&ciLogStatAnalysisModel.parentId='+parentId
		+'&ciLogStatAnalysisModel.opTypeId='+opTypeId
		+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId
		+'&ciLogStatAnalysisModel.thirdDeptId='+thirdDeptId;
		
			
		var url=escape($.ctx+'/ci/ciLogStatAnalysisAction!findOneOpLogTypeSpread.ai2do?'+para);
			
		var noData = '无统计数据';
                var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		$("#chart").show();
		var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/MSColumn2D.swf"+chartParam, "",$('#chart').width(), $('#chart').height(), "0", "0");
		bar.addParam("wmode","Opaque");
	    bar.setDataURL(url);
	    bar.render("chart");
	   
	}
</script>
</head>
<body>
<div>
    	<div class="aibi_chart_div" id="chart" style="width:100%;height:280px">
</div>
<div class="box box_on">
			<ul class="checkboxList">
			<c:forEach items="${opLogTypeList}" var="op" varStatus="st">
			
			 <c:choose>
			        <c:when test="${st.index==0 }">
			        	<li><input  type="radio" name="opTypeName"  value="${op.opTypeId }" class="valign_middle"  checked="checked"/>${op.opTypeName }</li>
			         </c:when>
			        <c:otherwise>
			        	<li><input type="radio" name="opTypeName"  value="${op.opTypeId }" class="valign_middle" >${op.opTypeName }</input></li>
			        </c:otherwise>
			       </c:choose>
			</c:forEach>
			</ul>
</div>
<input type="hidden"  name="ciLogStatAnalysisModel.beginDate"  id="beginDate" value="${ciLogStatAnalysisModel.beginDate }" />
<input type="hidden"  name="ciLogStatAnalysisModel.endDate"  id="endDate" value="${ciLogStatAnalysisModel.endDate }"/>
<input type="hidden"  name="ciLogStatAnalysisModel.parentId"  id="parentId" value="${ciLogStatAnalysisModel.parentId }" />
<input type="hidden"  name="ciLogStatAnalysisModel.opTypeId"  id="opTypeId" value="${ciLogStatAnalysisModel.opTypeId }"/>
<input type="hidden"  name="ciLogStatAnalysisModel.secondDeptId"  id="secondDeptId"  value="${ciLogStatAnalysisModel.secondDeptId }"/>
<input type="hidden"  name="ciLogStatAnalysisModel.thirdDeptId"  id="thirdDeptId"  value="${ciLogStatAnalysisModel.thirdDeptId }"/>

</div>
</body>
</html>