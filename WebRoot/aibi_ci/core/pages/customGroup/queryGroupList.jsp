<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.ailk.biapp.ci.entity.CiGroupAttrRel"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户群清单预览</title>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/default/easyui.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/icon.css"/>
		<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryEasyUI/easyloader.js"></script>
		
		<script type="text/javascript">
			$(function(){
				$(document).bind("contextmenu",function(e){  
		            return false;  
		        });
				
				var _isSuccess = '${resultMap["success"]}';
				var _msg = '${resultMap["msg"]}';
				if(_isSuccess == 'false'){
					//parent.showAlert(_msg,"failed",parent.closeDialog,"#queryListDialog");
					commonUtil.create_alert_dialog("showAlertDialog", {
	                    "txt":_msg,
	                    "type":"failed",   
	                    "width":500,
	                    "height":200,
	                    "param":"#queryListDialog"         
					},"parent.closeDialog"); 
					parent.multiCustomSearch();
				}else{
					//没有错误信息再加载
					easyloader.load('datagrid', function(){ //加载指定模块 
						$("#groupGridList").datagrid({
							height:328,
						    remoteSort:true,
						    fitColumns:false,
						    rownumbers:true,
						    nowrap:true,
						    striped:true,
						    pagination:true,
						    onRowContextMenu:function(e, rowIndex, rowData){
						    	e= e||event;
						    	//e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
						    	e.preventDefault();
						    	return false;
						    },
						    loadMsg:'正在加载数据，请稍候',
						    url:'${ctx}/core/customGroupOperateAction!findGroupInfoList.ai2do?customGroupInfo.customGroupId=${customGroupInfo.customGroupId}&'
						    		+'customGroupInfo.dataDate=${customGroupInfo.dataDate}',
						    columns:[[
					  			<%
					  			List<CiGroupAttrRel> groupAttrRelList = 
					  				(List<CiGroupAttrRel>)request.getAttribute("groupAttrRelList");
					  			String title = Configure.getInstance().getProperty("RELATED_COLUMN_CN_NAME");
					  			if (null != title && "".equals(title)) {
					  				title = "手机号码";
					  			}
					  			out.print("{field:'" + ServiceConstants.MAINTABLE_KEYCOLUMN +
						  				   "',title:\"" + title + "\",width:110,resizable:true}");
					  			if(groupAttrRelList != null && groupAttrRelList.size() > 0){
					  				 out.print(",");
					  				 for(int i=0;i<groupAttrRelList.size();i++){
						  				 CiGroupAttrRel attrRel = groupAttrRelList.get(i);
						  				 String str = "{field:'"+attrRel.getId().getAttrCol().toUpperCase()+ 
						  					 "',title:'"+attrRel.getAttrColName().trim()+"',width:100},";
						  				 if(i == groupAttrRelList.size()-1){
						  					str = str.substring(0,str.length()-1);
						  				 }
						  				 out.print(str);
									}
					  			}
					  			%>
						  	]],
							rowStyler:function(index,row){
								return "height:20px;";
							},
					        onLoadSuccess:function(data){
						    	$(".datagrid-view1,.datagrid-view1 > div,.datagrid-view1 table").width(45);
						    	$(".datagrid-view2").width(613);
					        },
					        onLoadError:function(XMLHttpRequest, textStatus, errorThrown){
					        	parent.showAlert("表格加载错误！", "failed");
					        },
					        onClickRow: function (rowIndex, rowData) {
	                            $(this).datagrid('unselectRow', rowIndex);
	                        }
						});
						$(".datagrid-header-rownumber").html("<span class='tableHead' >序号</span>");
						$(".datagrid-header-row span").addClass("tableHead");
						var p = $('#groupGridList').datagrid('getPager');
						$(p).pagination({  
							pageSize: 10,//每页显示的记录条数，默认为10  
							pageList: [5,10],//可以设置每页记录条数的列表  
							beforePageText: '第',//页数文本框前显示的汉字  
							afterPageText: '页    共 {pages} 页',  
							displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
						});  
					});
				}
			});
		</script>
	</head>
	<style>
		.datagrid-row .datagrid-header-rownumber,.datagrid-header-row .datagrid-header-rownumber,.datagrid-row .datagrid-cell-rownumber{width:auto;}
		.datagrid .pagination .pagination-num{width:3.5em;}
	</style>
	<body>
		<table id="groupGridList" ></table>
	</body>
</html>

