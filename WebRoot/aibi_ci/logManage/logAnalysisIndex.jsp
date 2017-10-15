<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<%@ include file="/aibi_ci/html_include.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志统计</title>

<script type="text/javascript">
var zTree;

$(function(){
	$("#_log").addClass("menu_on");
	needNext();
	loadTitle()//加载曲线图		
	createTree()//创建下拉框部门树
	loadLogTable();	//加载表格数据
	$("#deptName").bind("click",{id:"deptName"},showMenu);
})

//生成部门树
function createTree(){
	var curMenu = null, zTree_Menu = null;
    var setting = {	
    	check: {          
    	   enable: true        
    	}, 
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
        	onCheck: zTreeOnClick
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
                zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            } else {
            	showAlert(result.msg,"failed");
            }
        }
    });
}

function zTreeOnClick(event, treeId, treeNode) {
	if(treeNode.pId != null) {
		var parent = treeNode.getParentNode();
		var flag = true;
		var child = parent.children;
		if(child !=null) {
			for(var j = 0, k = child.length; j<k; j++) {
				var node = child[j];
				if(!node.checked) {
					flag = false;
					break;
				}
			}
			if(!flag) {
				parent.checked = false;
			} else {
				parent.checked = true;
			}
		}
	}
	setModelIds(); 
	loadLogTable(true);
}

function setModelIds() {

	var secondDeptId = "";
	var thirdDeptId = "";
	var checkedNodes = zTree.getCheckedNodes(true);
	
	for (var i=0, l=checkedNodes.length; i<l; i++) {
		if(checkedNodes[i].pId != null) {//如果是三级部门节点
			thirdDeptId += "'" + checkedNodes[i].id +"'"+ ",";
		} else if(checkedNodes[i].pId == null) {//如果是二级部门节点
		    secondDeptId += "'" + checkedNodes[i].id  + "'" + ",";
		}
	}
	
	$("#secondDeptId").val(secondDeptId.substr(0,secondDeptId.length - 1));
	$("#thirdDeptId").val(thirdDeptId.substr(0,thirdDeptId.length - 1));
	
}

function showMenu(event) {
    var id = event.data.id;
    var cityObj = $("#" + id);
    var cityOffset = $("#" + id).offset();
    $("#menuContent").css({left: cityOffset.left + "px", top: cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

    $(document).bind("mousedown", onBodyDown);
}

function onBodyDown(event) {
    if (!(event.target.id == "busiName" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}

function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $(document).unbind("mousedown", onBodyDown);
}

/**
 * 加载表格
 */
function loadLogTable(isClick){
	var beginDate = $("#test1").val();
	var endDate = $("#test2").val();
	var url=$.ctx+'/ci/ciLogStatAnalysisAction!findAllLogViewData.ai2do?ciLogStatAnalysisModel.beginDate='+beginDate+'&ciLogStatAnalysisModel.endDate='+endDate+'&ciLogStatAnalysisModel.secondDeptId='+$("#secondDeptId").val()+'&ciLogStatAnalysisModel.thirdDeptId='+$("#thirdDeptId").val();
	if(isClick){
		url += "&isClick=" + isClick;
	}
	$('#table').load(url);
}
/**
 * 控制下一天按钮样式
 */

function needNext() {
	if(	$("#test2").val() ==  $("#test4").val()) {
      	$("#hou").addClass("btn_explore_dis");
  		$("#hou").attr("href","javaScript:void(0)");
      }
      if(	$("#test2").val() !=  $("#test4").val()) {
      	$("#hou").removeClass("btn_explore_dis");
  		$("#hou").attr("href","javaScript:next()");
      }
}
/**
 * 查询
 */
function search() {
	$("#test3").html($("#test2").val());
	needNext();
	loadTitle();
	loadLogTable();
}
/**
 * 后一天
 */
function next() {
	changeDate("next");
	if(test4 == test2) {
		$("#hou").addClass("btn_explore_dis");
	}
}
 

/**
 * 前一天
 */
function front() {
	
	changeDate("front");
	
}

function changeDate(frontOrNext) {
	var beginDate = $("#test1").val();
	var endDate = $("#test2").val();
	var actionUrl = $.ctx + "/ci/ciLogStatAnalysisAction!changeDate.ai2do?frontOrNext="+frontOrNext+"&ciLogStatAnalysisModel.beginDate="+beginDate+'&ciLogStatAnalysisModel.endDate='+endDate;
	$.ajax({
        type: "get",
        url: actionUrl,
        dataType: "json",
        success: function (result) {
            if (result.success) {
                if(beginDate == endDate) {
                	$("#test1").val(result.endDate);
                	$("#test2").val(result.endDate);
                } else {
                	$("#test2").val(result.endDate); 
                	
                	
                }        
            	needNext();
                $("#test3").html(result.endDate);
                search();                     
            }
        }
    });
}
 
/**
 * 加载曲线图
 */
function loadTitle() {
	var beginDate = $("#test1").val();
	var endDate = $("#test2").val();
	var opTypeId = $("#opTypeFlag").val();
	var url=$.ctx+'/ci/ciLogStatAnalysisAction!loadTitle.ai2do?ciLogStatAnalysisModel.beginDate='+beginDate+'&ciLogStatAnalysisModel.endDate='+endDate+'&ciLogStatAnalysisModel.opTypeId='+opTypeId;
	$('#tab_title').load(url);
}
/**
 * 下载表格
 */
function download() {
	var beginDate = $("#test1").val();
	var endDate = $("#test2").val();
	var secondDeptId = $("#secondDeptId").val();
	var thirdDeptId = $("#thirdDeptId").val();
	var path = "ciLogStatAnalysisAction!exportIndexTable.ai2do?ciLogStatAnalysisModel.beginDate="+beginDate+"&ciLogStatAnalysisModel.endDate="+endDate+"&ciLogStatAnalysisModel.secondDeptId="+secondDeptId+"&ciLogStatAnalysisModel.thirdDeptId="+thirdDeptId;
	$("#download").attr("href",path);
}
</script>
<style type="text/css">
.ui-dialog .ui-dialog-titlebar{line-height:auto!important;padding-bottom:0em!important;padding-top:0em!important;}
</style>
 
</head>

<body>
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp"/>
<div class="mainCT">
<div class="submenu">
	<div class="submenu_left" style="float: left;"/></div>
	<div class="top_menu">
	    <ul>
	        <li class="top_menu_on">
	            <div class="bgleft">&nbsp;</div>
	            <a href="#" class="no1"><span></span><p>日志统计</p></a>
	            <div class="bgright">&nbsp;</div>
	        </li>
	        
	       
	    </ul>
	</div>
</div>

<div class="rztj_thead">
	<input type="hidden" id="test4" value="${dateFmt:dateFormat(endDate)}"></input>
	<input type="hidden" id="test5" value="${dateFmt:dateFormat(minDate)}"></input>
	
    <div class="fright">
    <span class="dateTitle">选择时间：</span>
    <input id="test1" class="new_tag_input tag_date_input" type="text"  value="${dateFmt:dateFormat(beginDate)}" onFocus="var test2=$dp.$('test2');WdatePicker({onpicked:function(){test2.focus();},maxDate:'#F{$dp.$D(\'test2\')}'})"/>
	至
	<input id="test2" class="new_tag_input tag_date_input" type="text"  value="${dateFmt:dateFormat(endDate)}" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'test1\')}',maxDate:'#F{$dp.$D(\'test4\')}'})"/>
    
    
    <a class="btn_explore" href="javaScript:search()">查看</a>
    <a class="btn_explore" id="qian" href="javaScript:front()" >前一天</a>
    <a class="btn_explore" id="hou" href="javaScript:next()" >后一天</a>
    </div>
</div>
<input id="opTypeFlag" type="hidden"></input>
<input id="frontOrNext" type="hidden"></input>
<div class="area_title">整体概况</div>
<div id="tab_title">
	
</div>

<div class="main_con_box log_tabct">

	<div class="main_con_top">
		<div class="fright"><a  class="tag_btn" id="download" style="color: #fff;height: 27px;width: 45px;text-align: center;" href="#" onclick="download()">下载</a></div>
		<div class="tag_box_title tag_box_title_qsfx"><span>各部门系统使用情况总览</span></div>
        <div class="selectCheckboxCT"><input id="deptName" type="text" class="selectCheckbox" style="width:167px;cursor:text"  readonly="readonly" value="点击选择部门"/></div>
    </div>
<div id="testDivId" class="log_tabct_padding0">
	
	<div class="box box_on">
		
    	<div class="aibi_table_div" id="table"></div>
    </div>
</div>
</div>
</div>
<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
    <ul id="treeDemo" class="ztree selectCheckboxZtree"></ul>
</div>
<input id="secondDeptId" name="ciLogStatAnalysisModel.secondDeptId" type="hidden" >
<input id="thirdDeptId" name="ciLogStatAnalysisModel.thirdDeptId" type="hidden" >
</div>
</body>
</html>