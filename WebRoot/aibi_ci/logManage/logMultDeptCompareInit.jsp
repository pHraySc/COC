<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.ailk.biapp.ci.constant.CommonConstants"%>
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日志统计</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
    var para;
    var flag=1;
    var thirdFlag='${ciLogStatAnalysisModel.thirdDeptId}';
    var secondDeptId ='${ciLogStatAnalysisModel.secondDeptId}';
	$(document).ready(function(){
		
        //div收起展开
        $(".log_approve_control").click(function(){
    		var _span=$(this).find("span");
    		var _ct=$(this).prev().prev(".log_tabct");
    		if(_span.hasClass("up_arr")){
    			_span.removeClass("up_arr").html("展开");
    			_ct.slideUp("fast");
    		}else{
    			_span.addClass("up_arr").html("收起");
    			_ct.slideDown("fast");
    		}
    	})
    	
    	//单选框
    	$("input[name='opTypeName']").click(function(){
     	this.blur();  
     	this.focus();
      	});

        $("input[name='opTypeName']:first").attr("checked",true);
        //var opTypeId= $("input[name='opTypeName']:checked").val();      
 		var opTypeId='${ciLogStatAnalysisModel.opTypeId}';
        var beginDate='${ciLogStatAnalysisModel.beginDate }';
    	var endDate='${ciLogStatAnalysisModel.endDate }';
    	//单标签分析
    	var parentId='${ciLogStatAnalysisModel.parentId }';
    	//var secondDeptId ='${ciLogStatAnalysisModel.secondDeptId}';
    	
    	//var opTypeId="21";
    	para='ciLogStatAnalysisModel.beginDate='+beginDate
    	+'&ciLogStatAnalysisModel.endDate='+endDate
    	+'&ciLogStatAnalysisModel.parentId='+parentId
    	+'&ciLogStatAnalysisModel.opTypeId='+opTypeId;
		
		//给查询结果table设定斑马线效果
		aibiTableChangeColor(".mainTable");
		if(thirdFlag=='1'){
	        chartSearch(thirdFlag);
			}else{
				
				showData(secondDeptId,opTypeId,parentId,thirdFlag );
				}		

        $("#thirdChart").text("");

        $("input[name='labelInfoName']").change(function(){
        	parentId= $("input[name='labelInfoName']:checked").val();
        	
        	var text=$("input[name='labelInfoName']:checked + span").text();
            var secondDepName = $("input[name='ciLogStatAnalysisModel.secondDeptId']").text();
        	
        	para='ciLogStatAnalysisModel.beginDate='+beginDate
        	+'&ciLogStatAnalysisModel.endDate='+endDate
        	+'&ciLogStatAnalysisModel.parentId='+parentId
        	+'&ciLogStatAnalysisModel.opTypeId='+opTypeId;
        	
        	if(thirdFlag=='1'){
        		
    	        chartSearch(thirdFlag);
    			}else{
    				
    				showData(secondDeptId,opTypeId,parentId,thirdFlag );
    				}	
        	
        	//标签分析的名称
        	$("#labelTitleInfo").text(text);
        	 $("#labelMain").fadeOut("fast");
	    	 $(document).unbind("mousedown", onLableMouseDown);
        });

        $("input[name='labelInfoName']").each(function(){
            if(parentId==$(this).val()){
				this.checked=true;
                }
       	 	
       });

        
	});

	//柱状图
	
	function chartSearch(thirdFlagStr) {
			
			flag=1;
			thirdFlag=thirdFlagStr;
			var param=para+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId
			+'&ciLogStatAnalysisModel.thirdDeptId='+thirdFlag;
			
	        var url = $.ctx+'/ci/ciLogStatAnalysisAction!anaChart.ai2do';
	        $.ajax({
	            url:url,
	            type:"POST",
	            dataType: "html",
	            data: param,
	            success:function(html){
	                $("#chart1").html("");
	                $("#chart1").html(html);
	                tableList();
	                $("#thirdChart").text("");
	            }
	        });
	} 
	function tableList() {
			var param=para+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId	;
	    	var url = $.ctx+'/ci/ciLogStatAnalysisAction!findMultOpTypeAllLogViewData.ai2do?'+$("#pageForm").serialize();
	        $.ajax({
	            url:url,
	            type:"POST",
	            dataType: "html",
	            data: param,
	            success:function(html){
	                $("#multDeptOptyesList").html("");
	                $("#multDeptOptyesList").html(html);
	                pagetable();
	            }
	        });
	}

	function opUserTableList(param) {
				
	    	var url = $.ctx+'/ci/ciLogStatAnalysisAction!findOneOpLogTypeUserSpread.ai2do?'+$("#pageForm").serialize();
	        $.ajax({
	            url:url,
	            type:"POST",
	            dataType: "html",
	            data: param,
	            success:function(html){
	                $("#multDeptOptyesList").html("");
	                $("#multDeptOptyesList").html(html);
	                pageUsertable();
	            }
	        });
	}
	function pagetable(){
    	/*
    	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
    	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
    	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
    	*/
    	var actionUrl = '${ctx}/ci/ciLogStatAnalysisAction!findMultOpTypeAllLogViewData.ai2do?' + $("#pageForm").serialize();
    	var param=para+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId	;
    	$("#multDeptOptyesList").page( {url:actionUrl,param:param,objId:'multDeptOptyesList',callback: pagetable });
   
    }

	function pageUsertable(){
		
    	/*
    	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
    	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
    	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
    	*/
    	var actionUrl = '${ctx}/ci/ciLogStatAnalysisAction!findOneOpLogTypeUserSpread.ai2do?' + $("#pageForm").serialize();
    	var param=para+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId	;
    	$("#multDeptOptyesList").page( {url:actionUrl,param:param,objId:'multDeptOptyesList',callback: pageUsertable });
   
    }  

	//柱状图下钻
   function showData(secondDeptIdStr,opTypeId,parentId,thirdFlagStr ){
		
		flag=0;
		thirdFlag=thirdFlagStr;
		secondDeptId=secondDeptIdStr;
		
		var opTypeId=opTypeId;
		var parentId=parentId;
		var beginDate=$("#beginDate").val();
		var endDate=$("#endDate").val();
		var param='ciLogStatAnalysisModel.beginDate='+beginDate
		+'&ciLogStatAnalysisModel.endDate='+endDate
		+'&ciLogStatAnalysisModel.parentId='+parentId
		+'&ciLogStatAnalysisModel.opTypeId='+opTypeId
		+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId
		+'&ciLogStatAnalysisModel.thirdDeptId='+thirdFlag;
		
		var param2='ciLogStatAnalysisModel.beginDate='+beginDate
		+'&ciLogStatAnalysisModel.endDate='+endDate
		+'&ciLogStatAnalysisModel.parentId='+parentId
		+'&ciLogStatAnalysisModel.opTypeId='+opTypeId
		+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId
		
		 var url = $.ctx+'/ci/ciLogStatAnalysisAction!anaChart.ai2do?' + param;
		 
	        $.ajax({
	            url:url,
	            type:"POST",
	            dataType: "html",
	            success:function(html){
	                $("#chart1").html("");
	                $("#chart1").html(html);
	                $("#thirdChart").text("返回上一级");
	                
	                opUserTableList(param2);
	                
	            }
	        });
	   }

   function toExcel(){
	   	var param=para+'&ciLogStatAnalysisModel.secondDeptId='+secondDeptId;
		var excelUrl;
		if(flag==1){
			excelUrl= $.ctx+'/ci/ciLogStatAnalysisAction!toMultOpTyPesExcel.ai2do?' + param;
			}
		else{
			excelUrl=$.ctx+'/ci/ciLogStatAnalysisAction!toMultOpTyPesUserExcel.ai2do?' + param;
			}
		
		parent.location.href=excelUrl;
		//window.open(excelUrl);
		
		
	   }
 function selectLable(){
	
	 var cityObj = $("#changeLable");
	 var cityOffset = $("#changeLable").offset();
	 $("#labelMain").css({left: cityOffset.left+20 + "px", top: cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$(document).bind("mousedown", onLableMouseDown);
	
 }

 function onLableMouseDown(event) {
	 	
	    if (!(event.target.id == "labelMain" || $(event.target).parents("#labelMain").length > 0)) {
	    	 $("#labelMain").fadeOut("fast");
	    	 $(document).unbind("mousedown", onLableMouseDown);
	    }
	}


</script>
</head>
<body>
<div class="rztj_thead">
	<div class="fleft">
		<label id="labelTitleInfo">${dim:toName(DIM_OP_LOG_TYPE,ciLogStatAnalysisModel.parentId) }</label>
       	
        <a class="changeDept" id="changeLable" href="#" onclick="selectLable()">切换指标</a>
        <span class="data_date">
        &nbsp;&nbsp;${dateFmt:dateFormat(ciLogStatAnalysisModel.beginDate) }&nbsp;至 ${dateFmt:dateFormat(ciLogStatAnalysisModel.endDate) }
        </span>
    </div>
    <div class="fright">
    	<a href="javascript:void(0);" onclick="chartSearch('1')" id="thirdChart"></a>
    </div>
</div>
	<div class="log_tabct">
    	<div  id="chart1" style="height:300px;width:100%;"></div>
	</div>
	<div class="blueborder">&nbsp;</div>
<div class="approve_control log_approve_control"><span class="up_arr">收起</span></div>
<div class="fright" style="padding-bottom:8px;  overflow:visible; height:auto;">
	<a  class="tag_btn" id="download" style="color: #fff;height: 27px;width: 45px;text-align: center;" href="#" onclick="toExcel();">下载</a>
</div>
<div class="aibi_area_data">
    <div class="content">
    	<div id="multDeptOptyesList"></div>
    	</div>
</div>
<div id="labelMain" class="x-form-field menuContent" style="position: absolute;display:none;width:206px">
    <div class="box box_on">
    		<table>
    			<c:forEach items="${headList}" var="op" varStatus="st">
    			<tr>
    				<td>
	    				<input  type="radio" name="labelInfoName"  value="${op.opTypeId }" class="valign_middle" /><span>${op.opTypeName }</span>
    				</td>
    			</tr>
    			</c:forEach>
    		</table>
		</div>
</div>
<input type="hidden"  name="ciLogStatAnalysisModel.beginDate"  id="beginDate" value="${ciLogStatAnalysisModel.beginDate }" />
<input type="hidden"  name="ciLogStatAnalysisModel.endDate"  id="endDate" value="${ciLogStatAnalysisModel.endDate }"/>

</body>
</html>