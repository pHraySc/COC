<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
String alarmMenu = Configure.getInstance().getProperty("AlARM_MENU");
String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>客户群清单预览</title>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/default/easyui.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/icon.css"/>
		<style type="text/css">
			.datagrid-row .datagrid-header-rownumber,.datagrid-header-row .datagrid-header-rownumber,.datagrid-row .datagrid-cell-rownumber{width:auto;}
			.datagrid-row .datagrid-cell-rownumber{height: 82px;}
			.datagrid .pagination .pagination-num{width:3.5em;}
		    .datagrid-body{overflow:visible;overflow-x:hidden;overflow-y:auto;}	
		</style>
		<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryEasyUI/jquery.easyui.min.js"></script>
<%-- 		<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryEasyUI/easyloader.js"></script> --%>
		<script type="text/javascript">
		$(function(){
			//easyloader.load('datagrid', function(){ //加载指定模块 
				$("#sqlTable").datagrid({
					height:400,
				    remoteSort:true,
				    fitColumns:false,
				    rownumbers:true,
				    nowrap:false,
				    fit:true,
				    striped:true,
				    autoRowHeight:false,
				    pagination:true,
				    scroll:false,
				    loadMsg:'正在加载数据，请稍候',
				    url:'${ctx}/ci/customersManagerAction!findSqlInfo.ai2do?ciCustomListInfo.listTableName=${ciCustomListInfo.listTableName}',
				    columns:[[
						{field:'startTimeStr',title: '开始时间',align: 'right',width: 125},  
						{field:'endTimeStr',title: '结束时间',align: 'right',width: 125},  
						{field:'runTime',title: '运行时间',align: 'right',width: 60},  
						{field:'expression',title: 'sql表达式',align: 'left',width: 280,
							styler:function(value,row,index){
								return 'height:82px;';
							}},  
						{field:'excpInfo',title: '异常信息',align: 'left',width: 240},  
				  	]],
					/* rowStyler:function(index,row){
						alert(index);
							return 'height:90px;overflow-x:hidden;overflow-y:auto;';
					}, */
			        onLoadSuccess:function(data){
				    	$(".datagrid-view1,.datagrid-view1 > div,.datagrid-view1 table").width(45);
				    	//$(".datagrid-view2").width(1000);
// 				    	$(".datagrid-row").find("td:last div").css({"padding-right":"20px","width":"212px"});
			        },
			        onLoadError:function(XMLHttpRequest, textStatus, errorThrown){
			        	commonUtil.create_alert_dialog("failedDialog", {
			   			 "txt":"表格加载错误！",
			   			 "type":"failed",
			   			 "width":500,
			   			 "height":200
			   			}); 
			        },
			        onClickRow: function (rowIndex, rowData) {
                        $(this).datagrid('unselectRow', rowIndex);
                    }
				});
			    $(".datagrid-header-rownumber").html("<span class='tableHead' >序号</span>");
				$(".datagrid-header-row span").addClass("tableHead");
				var p = $('#sqlTable').datagrid('getPager');
				$(p).pagination({  
					pageSize: 10,//每页显示的记录条数，默认为10  
					pageList: [1,3,5,10],//可以设置每页记录条数的列表  
					beforePageText: '第',//页数文本框前显示的汉字  
					afterPageText: '页    共 {pages} 页',  
					displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
				});   
			});
		//});
	</script>

	</head>
	<body>
		<table id="sqlTable"></table>
	</body>
</html>

