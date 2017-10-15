<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>客户群管理</title>
<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/icon.css"/>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryEasyUI/easyloader.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/customManage.js"></script>
<script type="text/javascript">
var sysId = '${param["sysId"]}';//用于判断是否是其他系统内嵌的
$(function(){
	easyloader.load('datagrid', function(){ //加载指定模块 
		$("#groupGrid").datagrid({
			height:420,
		    remoteSort:true,
		    fitColumns:false,
		    striped:true,
		    pagination:true,
		    loadMsg:'正在加载数据，请稍后',
		    url:'${ctx}/ci/customersManagerAction!searchCustomGroupList.ai2do',
		    columns:[[
	  			{field:'customGroupName',title:"客户群名称",width:200,halign:'center',
	  	    		formatter:function(value,rowData,rowIndex){
		  	    		var str="<a href='javascript:showGroupInfo(\""+rowData.id+"\")'>"+value+"</a>";
		  	    		return str;
		  	    	}
	  			},
	  			{field:'createUserName',title:"创建人",width:90,halign:'center'},
	  	    	{field:'customNum',title:"客户数",width:90,halign:'center',align:'right',
	  				formatter:function(value,rowData,rowIndex){
	  					var num = "";
		  	    		if(value != null && value != "" || value == 0){
		  	    			num = $.formatNumber(value, 3, ',');
		  	    		}else{
		  	    			num = "-";
		  	    		}
		  	    		return num;
	  				}
	  	    	},
	  	    	{field:'updateCycle',title:"生成周期",width:70,halign:'center',
	  				formatter:function(value,rowData,rowIndex){
	  					var str = "";
		  	    		if(value != null && value != "" || value == 0){
		  	    			if(value==<%= ServiceConstants.CUSTOM_CYCLE_TYPE_ONE%>){
		  	    				str = "<%= ServiceConstants.CUSTOM_CYCLE_ONE%>";
		  	    			}else if(value==<%= ServiceConstants.CUSTOM_CYCLE_TYPE_M%>){
		  	    				str = "<%= ServiceConstants.CUSTOM_CYCLE_M%>";
		  	    			}else if(value==<%= ServiceConstants.CUSTOM_CYCLE_TYPE_D%>){
		  	    				str = "<%= ServiceConstants.CUSTOM_CYCLE_D%>";
		  	    			}
		  	    		}else{
		  	    			str = "-";
		  	    		}
		  	    		return str;
	  				}
	  	    	},
	  	    	{field:'dataStatus',title:"数据状态",width:70,halign:'center',
	  				formatter:function(value,rowData,rowIndex){
	  					var str = "";
		  	    		if(value != null && value != "" || value == 0){
		  	    			if(value==<%= ServiceConstants.CUSTOM_LIST_STATUS_WAIT%>){
		  	    				str = "待创建";
		  	    			}else if(value==<%= ServiceConstants.CUSTOM_LIST_STATUS_CREATING%>){
		  	    				str = "创建中";
		  	    			}else if(value==<%= ServiceConstants.CUSTOM_LIST_STATUS_SUCCESS%>){
		  	    				str = "创建成功";
		  	    			}else if(value==<%= ServiceConstants.CUSTOM_LIST_STATUS_FAILED%>){
		  	    				str = "创建失败";
		  	    			}
		  	    		}else{
		  	    			str = "-";
		  	    		}
		  	    		return str;
	  				}
	  	    	},
	  	    	{field:'createTimeView',title:"创建时间",width:125,halign:'center',align:'right'},
	  	    	{field:'createType',title:"创建方式",width:125,halign:'center',align:'right'},
	  	    	{field:'opt',title:"操作",width:125,halign:'center',align:'left',
	  	    		formatter:function(value,rowData,rowIndex){
		  	    		var str="";
		  	    		str+="<a class='icon_edit' href='javascript:setUserGroup(\""+rowData.customGroupId+"\",\""+rowData.customGroupName+"\",\""+rowData.customNum+"\")'>号码导入</a>";
		  	    		return str;
		  	    	}
	  	    	}
		  	]],
		  	singleSelect: false,
			rowStyler:function(index,row){
		    	return "height:35px;";
			},
	        onLoadSuccess:function(){
				$("input[name='disable']").each(function(index,el){
					 $("input[name='ck']").each(function(ind,e2){
						 if(el.value == ind){
							 $(e2).attr("disabled",true);
						 }
					 });
				 });
				
				var datagrid_table=$("#datagridCT");
				datagrid_table.find(".datagrid-cell-c1-createUserName").each(function(){
					if($(this).text=""){
						$(this).text("--").css("text-align","center");
					}
				})
				
				$(window).resize(function(){
					var sortTable=$("#datagridCT");
					var tableCT=sortTable.parent();
					var w=$("body").width();
					$("#groupGrid").datagrid("resize",{width:w});
					tableCT.width(w);
				});
	        },
	        onClickRow:function(index,row){
	        	$("input[name='ck']").each(function(ind,e){
					 if(index == ind){
						 $(e).attr("checked",false); 
					 }
				 });
			},
	        onLoadError:function(XMLHttpRequest, textStatus, errorThrown){
				if(sysId == null || sysId == ""){
					parent.showAlert("表格加载错误！", "failed");
				}else{
					showAlert("表格加载错误！", "failed");
				}
	        }
		});
		$(".datagrid-header-check input").click(function(){
			$("input[name='disable']").each(function(index,el){
				 $("input[name='ck']").each(function(ind,e2){
					 if(el.value == ind){
						 $(e2).attr("checked",false); 
					 }
				 });
			 });
		});
		$(".datagrid-header-row span").addClass("tableHead");
		var p = $('#groupGrid').datagrid('getPager');
		$(p).pagination({  
			pageSize: 10,//每页显示的记录条数，默认为10  
			pageList: [5,10,15],//可以设置每页记录条数的列表  
			beforePageText: '第',//页数文本框前显示的汉字  
			afterPageText: '页    共 {pages} 页',  
			displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
		//计算页面高度
		if(sysId == null || sysId == ""){
			var h = $("body").height();
			window.parent.$("#main").height(h);
		}
	});
});

//客户群高级查询
function query(){
	$("#groupGrid").data("datagrid").options.queryParams = {
		"ciCustomGroupInfo.customGroupName":$('#customGroupName').val(),
		"ciCustomGroupInfo.startDate":$('#startTime').val(),
		"ciCustomGroupInfo.endDate":$('#endTime').val(),
		"ciCustomGroupInfo.customStatusId":$('#customStatusId').val(),
		"ciCustomGroupInfo.updateCycle":$('#updateCycle').val()
	};
	$("#groupGrid").datagrid("load");
}
//客户群简单查询
function simpleQuery(){
	alert($('#simpleSearchName').val());
	$("#groupGrid").data("datagrid").options.queryParams = {
		"ciCustomGroupInfo.customGroupName":$('#simpleSearchName').val()
	};
	$("#groupGrid").datagrid("load");
}
//重新加载
function reloadGrid(){
	$("#groupGrid").datagrid("reload");
}
function setUserGroup(groupCode,groupName,customNum){
	var groupName = encodeURIComponent(encodeURIComponent(groupName));
	var ifmUrl = "http://${ipAdd}/DemoProject/userGroupCheck.jsp?groupCode=" + groupCode +"&groupName="+groupName+"&customNum="+customNum;
	$("#kyFrame").attr("src", ifmUrl).load();
}
</script>
<style>
#datagridCT .datagrid{width:100%;overflow:auto;}
</style>
</head>
<body>
	<div class="aibi_search">
		<!-- 简单查询 -->
		<ul class="search_line1">
			<li><input name="simpleSearchName" type="text" id="simpleSearchName" class="aibi_input" style="width:220px"/></li>
			<li><input name="" type="button" id="simpleSearchBtn" onclick="simpleQuery()"  class="aibi_search_btn"/></li>
	        <li class="more"><a href="javascript:void(0);">高级查询</a></li>
		</ul>
		<!-- 高级查询 -->
		<form id="queryForm" action="" method="post">
			<ul class="search_line2" style="display:none;">
				<li>
					<span class="line2_span">客户群名称：</span>
					<input name="ciCustomGroupInfo.customGroupName" id="customGroupName" type="text" class="aibi_input" style="width:200px"/>
				</li>
				<li>
                    <span>创建时间</span>
                    <input type="text" class="aibi_input aibi_date" id="startTime"
                           onfocus="if($('#endTime').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate: new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"
                           name="ciCustomGroupInfo.startDate"/>
                </li>
                <li>
                    <span>--</span>
                    <input type="text" class="aibi_input aibi_date" id="endTime"
                           onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate: new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"
                           name="ciCustomGroupInfo.endDate"/>
                </li>
				<li>
					<span class="line2_span">生成周期：</span><select name="groupInfo.updateCycle" id="updateCycle">
						<option value="">全部生成周期</option>
						<option value="1">一次性</option>
						<option value="2">月周期</option>
						<!-- <option value="3">日周期</option> -->
					</select>
				</li>
				<li>
					<span class="line2_span">数据状态：</span><select name="ciCustomGroupInfo.customStatusId" id="customStatusId">
						<option value="">全部</option>
						<option value="1">待创建</option>
						<option value="2">创建中</option>
						<option value="3">创建成功</option>
						<option value="4">创建失败</option>
					</select>
				</li>
				<li style="margin-left:10px;width:90px">
					<input name="searchBtn" id="searchBtn" type="button" onclick="query()"  class="aibi_search_btn"/>
				</li>
		        <li class="more"><a href="javascript:void(0);">简单查询</a></li>
			</ul>
		</form>
		<div class="clear"></div>
	</div>
	<!-- <div class="aibi_area_data"> -->
	<div id="datagridCT" class="content"  style="height:420px">
	   <table id="groupGrid" ></table>
	</div>
	<!-- </div> -->
<div id="kyDiv" style="display: none;">
	<iframe id="kyFrame" name="kyFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:300px"></iframe>
</div>
</body>
</html>