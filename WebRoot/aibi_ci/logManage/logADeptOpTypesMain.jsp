<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="../include.jsp"%>
<%@page import="com.ailk.biapp.ci.constant.CommonConstants"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<c:set var="DIM_OP_LOG_TYPE" value="<%=CommonConstants.TABLE_DIM_OP_LOG_TYPE%>"></c:set>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/assets/themes/default/main.css" rel="stylesheet" />
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
var param ;
$(function(){
	$("#deptId").bind("click",{id:"deptId"},showMenu);
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
	});
	$("input[name='opTypeId']").click(function(){
     	this.blur();  
     	this.focus();
      });
      
     $("input[name='opTypeId']").change(reloadData);
         
    $("input[name='opTypeId']").attr("checked",false);
	$("input[name='opTypeId']:first").attr("checked",true);
	param = "ciLogStatAnalysisModel.beginDate="+$("input[name='ciLogStatAnalysisModel.beginDate']").val()
		+"&ciLogStatAnalysisModel.endDate="+$("input[name='ciLogStatAnalysisModel.endDate']").val()
		+"&ciLogStatAnalysisModel.secondDeptId="+$("input[name='ciLogStatAnalysisModel.secondDeptId']").val()
		+"&ciLogStatAnalysisModel.thirdDeptId="+$("input[name='ciLogStatAnalysisModel.thirdDeptId']").val()
	 	+ "&ciLogStatAnalysisModel.opTypeId=" + $("input[name='opTypeId']:first").val();
	changeTabParam = param;
	loadChart();
	loadTable();
	createTree();
});
function reloadData(){
	var opTypeId = "";
    $("input[name='opTypeId']:checked").each(function(){
    	 opTypeId += $(this).val() + ",";
    });
    opTypeId = opTypeId.substring(0, opTypeId.length-1);
    param = "ciLogStatAnalysisModel.beginDate="+$("input[name='ciLogStatAnalysisModel.beginDate']").val()
	+"&ciLogStatAnalysisModel.endDate="+$("input[name='ciLogStatAnalysisModel.endDate']").val()
	+"&ciLogStatAnalysisModel.secondDeptId="+$("input[name='ciLogStatAnalysisModel.secondDeptId']").val()
	+"&ciLogStatAnalysisModel.thirdDeptId="+$("input[name='ciLogStatAnalysisModel.thirdDeptId']").val()
    + "&ciLogStatAnalysisModel.opTypeId=" + opTypeId;
    changeTabParam = param;
    $("#exportTable").attr("href","ciLogStatAnalysisAction!export.ai2do?" + param);
    loadTable();
    loadChart();
}
function zTreeOnClick(event, treeId, treeNode) {
    $("#deptId").attr("deptValue",treeNode.id);
    $("#deptId").attr("value",treeNode.name);
    if(treeNode.pId != null){
    	$("input[name='ciLogStatAnalysisModel.thirdDeptId']").attr("value",treeNode.id);
    	$("input[name='ciLogStatAnalysisModel.secondDeptId']").attr("value","");
    }else{
    	$("input[name='ciLogStatAnalysisModel.thirdDeptId']").attr("value","");
    	$("input[name='ciLogStatAnalysisModel.secondDeptId']").attr("value",treeNode.id);
    }
    
    //document.title = treeNode.name;
    hideMenu();
    reloadData();
}
function createTree(){
    	var curMenu = null, zTree_Menu = null;
        var setting = {
            view: {
                showLine: true,
                selectedMulti: false,
                dblClickExpand: false
            },
            data: {
            	key: {
        			title: "tip"
        		},
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: zTreeOnClick
            }
        };
        var zNodes = "";
        var actionUrl = $.ctx + "/ci/ciLogStatAnalysisAction!initDeptTree.ai2do";
        $.ajax({
            type: "POST",
            url: actionUrl,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    zNodes = result.treeList;
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                    $.each(zNodes,function(i,obj){
                		if(obj.id == ${ciLogStatAnalysisModel.secondDeptId}){
                			$("#deptId").attr("value",obj.name);
                			$("#deptId").attr("deptValue",obj.id);
                			return;
                    	}
                    });
                    $("#exportTable").attr("href","ciLogStatAnalysisAction!export.ai2do?" + param);
                } else {
                	showAlert(result.msg,"failed");
                }
            }
        });
}
function showMenu(event) {
    var id = event.data.id;
    var cityObj = $("#" + id);
    var cityOffset = $("#" + id).offset();
    $("#menuContent").css({left: cityOffset.left + "px", top: cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
    $(document).bind("mousedown", onBodyDown);
}

function onBodyDown(event) {
    if (!(event.target.id == "deptId" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $(document).unbind("mousedown", onBodyDown);
}
function loadChart(){
	var url=escape("${ctx}/ci/ciLogStatAnalysisAction!findOneDeptOpLogTrend.ai2do?"+param);
	var noData = "该部门暂无数据";
    var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
	var bar = new FusionCharts("${ctx}/aibi_ci/assets/flash/ScrollLine2D.swf"+chartParam, "",$("#areaChart").width(), $("#areaChart").height(), "0", "0");
	bar.addParam("wmode","Opaque");
	bar.setDataURL(url);
    bar.render("areaChart");
}

function loadTable(){
	var url = "${ctx}/ci/ciLogStatAnalysisAction!findOneDeptOpLogUserDetail.ai2do?" + param;
	$(".aibi_area_data .content").load(url,pagetable);
}

function pagetable(){
	var actionUrl = "${ctx}/ci/ciLogStatAnalysisAction!findOneDeptOpLogUserDetail.ai2do?" + $("#pageForm").serialize();
	$("#contentDiv").page( {url:actionUrl,param:param,objId:'contentDiv',callback: pagetable });
	$(".commonTable mainTable").datatable();
}
function toExport(){
	var actionUrl = "${ctx}/ci/ciLogStatAnalysisAction!export.ai2do?" + param;
	$.ajax({
        type: "POST",
        url: actionUrl,
        dataType: "html",
        success: function (result) {
        }
    });
}
function toMultOpTypesMain(obj){
	var beginDate = $("input[name='ciLogStatAnalysisModel.beginDate']").val() ;
	var endDate = $("input[name='ciLogStatAnalysisModel.endDate']").val();
	var parentId = $(obj).attr('parentId');
	var param = "ciLogStatAnalysisModel.beginDate=" + beginDate
        + "&ciLogStatAnalysisModel.endDate=" + endDate
        + "&ciLogStatAnalysisModel.parentId=" + parentId;
	var url = $.ctx+"/ci/ciLogStatAnalysisAction!logMultDeptCompareInit.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}

</script>
<style type="text/css">
.ui-dialog .ui-dialog-content {
	overflow: hidden
}
/*第一层节点切换图标被main.css隐藏,显示出该图标*/
.ztree li span.button.switch.level0 {display:inline-block; *display:inline; zoom:1;}
.ztree li.level0 span.level0 {display:inline-block; *display:inline; zoom:1;}
.x-form-field li:hover, .x-form-field li.odd:hover {
	background: white;
}
#queryForm{
	font-size:18px;
	padding: 5px 0px;
	line-height:24px;
}
#queryForm input[type="text"]{
	padding: 0px;
	margin: 0px;
	font-size:12px;
	background-color: transparent;
	width: 59px;
	vertical-align: center;
}
#queryForm span{
	padding: 0px;
	margin: 0px;
	font-size:12px;
	background-color: transparent;
}
</style>
</head>

<body>
<div class="rztj_thead">
	<div class="fleft">
	<form id="queryForm">
		<input type="hidden" name="ciLogStatAnalysisModel.secondDeptId" value="${ciLogStatAnalysisModel.secondDeptId }"/>
		<input type="hidden" name="ciLogStatAnalysisModel.thirdDeptId" value="${ciLogStatAnalysisModel.thirdDeptId }"/>
		<div class="selectCheckboxCT"><input id="deptId" name="deptName" type="text" class="selectCheckbox" style="width:200px;cursor:text"  readonly="readonly"/>
		&nbsp;&nbsp;</div>
		<input type="text" name="ciLogStatAnalysisModel.beginDate" readonly="readonly" value="${dateFmt:dateFormat(ciLogStatAnalysisModel.beginDate) }">
		<span>至 </span>
		<input type="text" name="ciLogStatAnalysisModel.endDate" readonly="readonly" value="${dateFmt:dateFormat(ciLogStatAnalysisModel.endDate) }">
	</form>
    </div>
</div>

<div class="log_tabct">
	<div class="box box_on">
        <ul class="checkboxList">
        	<c:forEach items="${headList}" var="headModel">
        	<li><input type="checkbox" name='opTypeId' value="${headModel.opTypeId }"/>${dim:toName(DIM_OP_LOG_TYPE,headModel.opTypeId) }</li>&nbsp;&nbsp;
        	</c:forEach>
        </ul>
        <div id="areaChart" style="height: 280px; width:100%;" ></div>
    </div>
</div>
<div class="blueborder">&nbsp;</div>
<div class="approve_control log_approve_control"><span class="up_arr">收起</span></div>

<div class="aibi_area_data">
	<div class="fright" style="padding-bottom: 10px;">
		<a id="exportTable" class="tag_btn" style="color: #fff;height: 27px;width: 45px;text-align: center;" href="ciLogStatAnalysisAction!export.ai2do">下载</a>&nbsp;&nbsp;&nbsp;
	</div>
    <div class="content">
		<jsp:include page="/aibi_ci/logManage/logADeptOpTypesList.jsp"></jsp:include>
	</div>
</div>
<div id="menuContent" class="x-form-field menuContent" style="position: absolute;display:none;width:200px">
    <ul id="treeDemo" class="ztree" style="margin-top:0;  height: 240px;overflow-y:scroll;"></ul>
</div>
</body>
</html>
