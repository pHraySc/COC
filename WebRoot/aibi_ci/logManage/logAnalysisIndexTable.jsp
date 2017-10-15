<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<c:set var="intervalDays" value="15"/><!-- 趋势预览显示天数，默认15天 -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('#sortTable th').click(sortTable);
	$("#table",document.parent).height($("#sortTable").height());
	/* $(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	}); */
});
	function showDetail(obj){
		var endDate = $("#test2").val();
		var beginDate = getBeginDate(endDate.replace("-","").replace("-",""),'${intervalDays }' ) ;
		var secondDeptId = $(obj).attr('secondDeptId');
		var secondDeptName = $(obj).attr('secondDeptName');
		var title = secondDeptName + "(" + beginDate.substring(0,4)+"-"+beginDate.substring(4,6)+"-"+beginDate.substring(6,8) + "至" + endDate + ")";
		$("#trendDialog").dialog({
			autoOpen: false,
			resizable:false,
			height: 480,
			width: 850,
			title:  title,
			modal: true,
			position: [150,90],
			close:function(){$(this).dialog("destroy");}
		});
		$("#trendDialog").dialog("open");
		var url=$.ctx+"/ci/ciLogStatAnalysisAction!findAnaOpTypeJson.ai2do";
		$.ajax({
			type: "POST",
			url: url,
	      	data:"",
	      	success: function(result){
	       		var list = result.opTypeModelList;
	       		var html = "";
	       		for(var i=0; i<list.length; i++){
	       			var opTypeId = list[i].opTypeId;
	       			var opTypeName = list[i].opTypeName;
	       			html += "<input type='checkbox' name='opTypeId' value='" + opTypeId + "'  style='cursor:pointer;vertical-align:middle;'/>" + opTypeName + "&nbsp;&nbsp;&nbsp;";
	       		}
	       		$("#dialog_btn_div").html(html);
	            
	            $("input[name='opTypeId']").click(function(){
	            	this.blur();  
	            	this.focus();
	             });
	             
	            $("input[name='opTypeId']").change(function(){
		              var opTypeId = "";
		              $("input[name='opTypeId']:checked").each(function(){
		              	 opTypeId += $(this).val() + ",";
		              });
		              opTypeId = opTypeId.substring(0, opTypeId.length-1);
		              var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
			               + "&ciLogStatAnalysisModel.endDate=" + endDate
			               + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId
			               + "&ciLogStatAnalysisModel.opTypeId=" + opTypeId;
		              loadDigChart(param);
	             });
	            
				$("input[name='opTypeId']:first").attr("checked",true);
				var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
		               + "&ciLogStatAnalysisModel.endDate=" + endDate
		               + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId
		               + "&ciLogStatAnalysisModel.opTypeId=" + $("input[name='opTypeId']:first").val();
	            loadDigChart(param);
			}
		});
	}
	function loadDigChart(param){
		var url=escape("${ctx}/ci/ciLogStatAnalysisAction!findOneDeptOpLogTrend.ai2do?"+param);
		var noData = "该部门暂无数据";
        var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		var bar = new FusionCharts("${ctx}/aibi_ci/assets/flash/ScrollLine2D.swf"+chartParam, "",$("#trendChart").width(), $("#trendChart").height(), "0", "0");
		bar.addParam("wmode","Opaque");
		bar.setDataURL(url);
	    bar.render("trendChart");
	}
	
	function toADeptOpTypesMain(obj){
		var beginDate = $("#test1").val() ;
		var endDate = $("#test2").val();
		var secondDeptId = $(obj).attr('secondDeptId');
		var secondDeptName = $(obj).html();
		var parentIdIndex = $(obj).attr('parentId');
		var parentId=$("#opTypeId"+parentIdIndex).val();
		var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
	        + "&ciLogStatAnalysisModel.endDate=" + endDate
	        + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId
	        + "&ciLogStatAnalysisModel.parentId=" + parentId
	        + "&thirdFlag=1"
	        +"&tab=one";
		//var url = $.ctx+"/ci/ciLogStatAnalysisAction!findOneDeptOpLogMain.ai2do?"+param;
		var url = "${ctx}/aibi_ci/logManage/logDeptOpIndex.jsp?"+param;
		
		window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		
	}
	
	function getBeginDate(endDate, intervalDays){
		var endDateTemp;
		if( endDate.length >= 8 ){
			endDateTemp = new Date(endDate.substring(0,4),endDate.substring(4,6)-1,endDate.substring(6,8));
		}
		var beginDateTemp = new Date(endDateTemp);
		beginDateTemp.setDate(endDateTemp.getDate()-intervalDays+1);
		beginDate = $.datepicker.formatDate("yymmdd",beginDateTemp);
		return beginDate;
	}

	function toMultOpTypesMain(obj){
		var beginDate = $("#test1").val() ;
		var endDate = $("#test2").val();
		var secondDeptId = $(obj).attr('secondDeptId');
		var secondDeptName = $(obj).html();
		var parentIdIndex = $(obj).attr('parentId');
		var parentId=$("#opTypeId"+parentIdIndex).val();
		var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
	        + "&ciLogStatAnalysisModel.endDate=" + endDate
	        + "&ciLogStatAnalysisModel.secondDeptId=" + secondDeptId
	        + "&ciLogStatAnalysisModel.parentId=" + parentId
	        + "&thirdFlag=0" 
	        +"&tab=mul";
		//var url = $.ctx+"/ci/ciLogStatAnalysisAction!logMultDeptCompareInit.ai2do?"+param;
		var url = "${ctx}/aibi_ci/logManage/logDeptOpIndex.jsp?"+param;
		window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
		
	}
	
</script>
<style type="text/css">
body,html{height:100%;}
.diaglog_checkbox_div{
	text-align:center; 
	background: #efefef; 
	padding-top:8px; 
	padding-bottom: 8px;
	width:100%;
	position: absolute;
	bottom: 0px;
	height: 30px;
}
.ui-dialog .ui-dialog-titlebar{line-height:auto;padding-bottom:0 em;padding-top:0 em;}
</style>
</head>
<body>
	<table   width="100%" id="sortTable" class="commonTable mainTable" cellpadding="0" cellspacing="0">
            <thead>
            <tr>
                    <th class="header" dataType="text">二级部门名称<span class="sort">&nbsp;</span></th>
                    <th class="header" dataType="text">三级部门名称<span class="sort">&nbsp;</span></th>
                    <c:forEach items="${headList}" var="model" varStatus="status">
                    	<input type="hidden"  id="opTypeId${status.index }" name="" value="${model.opTypeId }"></input>
                    	<th class="header" dataType="num">${model.opTypeName }<span class="sort">&nbsp;</span></th>                  	
                    </c:forEach>
                </tr>
            </thead>
                
            <tbody>
            	<c:forEach items="${resultList}" var="result" varStatus="status">
            		<c:choose>
            			<c:when test ="${fn:length(resultList)-1 == status.index}">
            				<c:choose>  
		       					<c:when test="${status.index % 2 == 0}">  
		            				<tr class="even" isSort="noSort">  
		        				</c:when>  
		       					<c:otherwise>  
		          			 		<tr class="odd" isSort="noSort">	
								</c:otherwise>  
			      			</c:choose>
							<td class="align_left">
								总计
							</td>
							<td class="align_left">
								-
							</td>
	                    	<c:if test="${!empty result.opTimes1}">	
	                    		 <td class="align_right"><c:if test="${empty result.opTimes1}">-</c:if>
	                    			<fmt:formatNumber type="number" value="${result.opTimes1}"  pattern="###,###,###" />
	                    		 </td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes2}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes2}">-</c:if>
	                    		<fmt:formatNumber type="number" value="${result.opTimes2}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes3}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes3}">-</c:if>
	                    		 <fmt:formatNumber type="number" value="${result.opTimes3}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes4}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes4}">-</c:if>
	                    		 <fmt:formatNumber type="number" value="${result.opTimes4}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes5}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes5}">-</c:if>
	                    		<fmt:formatNumber type="number" value="${result.opTimes5}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty  result.opTimes6}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes6}">-</c:if>
	                    		<fmt:formatNumber type="number" value="${result.opTimes6}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes7}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes7}">-</c:if>
	                    		 <fmt:formatNumber type="number" value="${result.opTimes7}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes8}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes8}">-</c:if>
	                    		<fmt:formatNumber type="number" value="${result.opTimes8}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes9}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes9}">-</c:if>
	                    		<fmt:formatNumber type="number" value="${result.opTimes9}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes10}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes10}">-</c:if>
	                    		 <fmt:formatNumber type="number" value="${result.opTimes10}"  pattern="###,###,###" /></td>
	                    	</c:if>
	                    </tr>
            			</c:when>
            			<c:otherwise>
            				  
		            		<c:choose>  
		       					<c:when test="${status.index % 2 == 0}">  
		            				<tr class="even">  
		        				</c:when>  
		       					<c:otherwise>  
		          			 		<tr class="odd">	
								</c:otherwise>  
			      			</c:choose>
							<td class="align_left">
								<c:if test="${empty result.secondDeptName}">-</c:if>
							<a href="javascript:void(0)"
								onclick="javascript: toADeptOpTypesMain(this)"
								secondDeptId="${result.secondDeptId}" parentId="0">${result.secondDeptName}</a>
								<img style="cursor: hand" onclick="javascript: showDetail(this)" id="showchartImg" secondDeptId="${result.secondDeptId}" secondDeptName="${result.secondDeptName}" alt="" src="${ctx}/aibi_ci/assets/themes/default/images/signal.png"/></td>
							<td class="align_left">
								<c:if test="${empty result.thirdDeptName}">-</c:if>
								${result.thirdDeptName}
							</td>
	                    	<c:if test="${!empty result.opTimes1}">
	      						
	                    		 <td class="align_right"><c:if test="${empty result.opTimes1}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="0" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes1}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes2}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes2}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="1" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes2}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes3}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes3}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="2" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes3}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes4}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes4}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="3" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes4}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes5}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes5}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="4" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes5}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty  result.opTimes6}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes6}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="5" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes6}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes7}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes7}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="6" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes7}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes8}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes8}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="7" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes8}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes9}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes9}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="8" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes9}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    	<c:if test="${!empty result.opTimes10}">
	                    		 <td class="align_right">
	                    		 <c:if test="${empty result.opTimes10}">-</c:if>
	                    		 <a href="javascript:void(0)"
								onclick="javascript: toMultOpTypesMain(this)"
								parentId="9" secondDeptId="${result.secondDeptId}"><fmt:formatNumber type="number" value="${result.opTimes10}"  pattern="###,###,###" /></a></td>
	                    	</c:if>
	                    </tr>
	                       </c:otherwise>
               			</c:choose>                     	
	               </c:forEach>	            
            </tbody>
        </table>
       	<%-- <div class="pagenum" id="pager">
			<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
		</div> --%>
        <!-- 单部门多操作类型操作趋势图 -->
<div id="trendDialog" class="ui-dialog ui-dialog-titlebar" style="display:none;padding-top: 0px;">
<table width="100%" class="showTable new_tag_table" cellpadding="0"
	cellspacing="0">
	<tr>
		<td style="text-line: 23px; color: #444; height: 30px; font: 14px;">最近${intervalDays}天趋势</td>
	</tr>
	<tr>
		<td>
		<div id="trendChart" style="width: 750px; height: 320px"></div>
		</td>
	</tr>
</table>
<!-- 按钮区 -->
<div id="dialog_btn_div" class="diaglog_checkbox_div"></div>
</div>
</body>
</html>
