<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript">
$(document).ready(function () {
	
	$("#simpleSearchName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	$("#busiName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	
	   //模拟下拉框
    $(".selBtn ,.selTxt").click(function(){
        if($(this).nextAll(".querySelListBox").is(":hidden")){
        	$(this).nextAll(".querySelListBox").slideDown();
        	if($(this).hasClass("selBtn")){
                $(this).addClass("selBtnActive");
            }else{
            	$(this).next(".selBtn").addClass("selBtnActive");
            }
       }else{
            	 $(this).nextAll(".querySelListBox").slideUp();
                 $(this).removeClass("selBtnActive");

            }
    	return false;
    })

    $(".querySelListBox a").click(function(){
    	var selVal = $(this).attr("value");
    	var selHtml = $(this).html();
    	 $(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
    	 $(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
    	 $(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
    	//向隐藏域中传递值
       $(this).parents(".querySelListBox").slideUp();
         return false;
    })
	
	$("#alarm_index").addClass("menu_on");

    $("#searchBtn").bind("click", function () {
        multiSearch();
    });

    $("#simpleSearchBtn").bind("click", function () {
        simpleSearch();
    });


	$("#dialog-new").click(function (event) {
        editThreshold(null);
    });

    $("#dialog_edit").dialog({
        autoOpen: false,
        width: 730,
        modal: true,
		resizable:false,
		close:function(event, ui){
			$("#dialog_edit_frame").attr("src", "");
		}
    });

   /*  $("#columnId").multiselect({
        multiple: false,
        header: false,
        height: "auto",
        selectedList: 1
    }); */
    $( "#show_customer_div").dialog({
		autoOpen: false,
		width: 730,
		title:"查看",
		modal: true,
		resizable:false
	});
	search();
});

function checkBoxAll(obj){
	$("input[name='alarmInfo.thresholdIdList']").prop("checked", obj.checked);
 	checkDelete();
}

function pagetable() {
    var formId = "simpleSearchForm";
    if ($(".search_line1").css("display") == "none") {
        formId = "multiSearchForm";
    }
    var actionUrl = 'ciCustomersAlarmAction!queryThreshold.ai2do?' + $("#pageForm").serialize();
    $("#alarmTable").page({
		url: actionUrl, 
		param: $("#" + formId).serialize(), 
		objId: 'alarmTable',
		callback: function() {
			pagetable();
		}
    });
    /*if($("#alarmTable_sort").find("tr").size()>1){
	$("#alarmTable_sort").tablesorter({
		sortList: [[1,0]],
		headers: { 6: { sorter: false},0: { sorter: false} }
	});
	}*/
	//datatable();
}

function search() {
    var formId = "simpleSearchForm";
    if ($(".search_line1").css("display") == "none") {
        formId = "multiSearchForm";
        var busiName = $("#simpleSearchName").val();
        if (busiName == "模糊查询") {
            busiName = "";
        }
        $("#busiName").val(busiName);
        $("#columnId").val($("#columnId_").val());
    } else {
    	 if ("false" == $.trim($("#simpleSearchName").attr("data-val"))) {
             busiName = "";
    	 }else{
    	    var busiName = $("#simpleSearchName").val();
    	 }
        $("#simpleBusiName").val(busiName);
    }
    var actionUrl = 'ciCustomersAlarmAction!queryThreshold.ai2do?' + $("#pageForm").serialize();
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: $("#" + formId).serialize(),
        success:function(html){
        	$(window).scrollTop(0);
            $("#alarmTable").html("");
            $("#alarmTable").html(html);
            pagetable();
            checkDelete();
            //datatable();
        }
    });
}
/* function datatable() {
    $(".commonTable mainTable").datatable();
} */

function simpleSearch() {
    var serachStr = "";
    if ("false" == $.trim($("#simpleSearchName").attr("data-val"))) {
        serachStr = "";
    } else {
        serachStr = $.trim($("#simpleSearchName").val());
    }
    $("#busiName").val(serachStr);
    search();
}

function multiSearch() {
    var serachStr = $.trim($("#busiName").val());
    if ("" == serachStr) {
        $("#simpleSearchName").val("");
    } else {
        $("#simpleSearchName").val(serachStr);
    }
    search();
}

//批量删除阈值
function deleteMultiThreshold() {
    openConfirm("确定要删除选择的预警设置么？","multiRecords");
}

function checkDelete(){
    if($(".checkbox:checked").size()>0){
        $(".icon_button_delete").removeClass("disable");
        $("#btn-batch-del").bind("click", deleteMultiThreshold);
    }else{
        $(".icon_button_delete").addClass("disable");
        $("#btn-batch-del").unbind("click");
    }
}

//删除单个阈值
function deleteThreshold(thresholdId) {
    openConfirm("确定要删除选择的预警设置么？","alarmInfo.thresholdIdList=" + thresholdId);
}

//删除阈值
function deleteThresholdByParam(param) {
	 if(param == "multiRecords"){
         param = $("input[name='alarmInfo.thresholdIdList']:checked").serialize();
     }
    var actionUrl = $.ctx + '/ci/ciCustomersAlarmAction!deleteThreshold.ai2do?' + param;
    $.ajax({
        type: "POST",
        url: actionUrl,
        success: function (result) {
        	if(result.success){
				$.fn.weakTip({"tipTxt":"删除成功！"});
					refreshList();
            } else{
            	showAlert("删除失败：" +result.msg,"failed");
            }
        }
    });
}


function editThreshold(thresholdId) {
    var url = 'ciCustomersAlarmAction!toEdit.ai2do';
    var title = "新增客户群预警";
    if (thresholdId != null) {
        url = url + "?alarmThreshold.thresholdId=" + thresholdId;
        title = "修改客户群预警";
    }
    //alert(0);
	
	$("#dialog_edit").dialog("open");
	$("#dialog_edit").dialog({"title":title});
	$("#dialog_edit_frame").attr("src", url);	
	/* .load(function(){
	//$("#dialog_edit").dialog("close");
	}); */
    $(".tishi").each(function(){
        var toffset=$(this).parent("td").offset();
        var td_height=$(this).parent("td").height();
        $(this).css({top:toffset.top + td_height + 13, left: toffset.left});
    });
}

//打开确认弹出层
function openConfirm(confirmMsg, id){
    confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
    var dlgObj =  commonUtil.create_alert_dialog("showAlertDialog", {
		 txt:"确定删除所选预警吗？",
		 type:"confirm",
		 width:500,
		 height:200,
		 param:id
	},deleteThresholdByParam) ; 
	dlgObj.dialog({
	  close: function( event, ui ) {
		  simpleSearch();
	  }
	});
}
//操作成功后刷新列表
function refreshList(){
	$("#alarmTable").html("");
 	search();
}
   
function callBack(){
	closeDialog("#dialog_edit");
	refreshList();
} 
   
//关闭确认弹出层
function closeDialog(dialogId){
	$(dialogId).dialog("close");
}
   
function closeConfirmDialog(dialogId, flag,param){
	closeDialog(dialogId);
    if(flag){
        if(param == "multiRecords"){
            param = $("input[name='alarmInfo.thresholdIdList']:checked").serialize();
        }
        deleteThresholdByParam(param);
    }
}
//打开展示标签窗口
function showCustomerDetail(busiId) {
	var ifmUrl = "${ctx}/aibi_ci/customers/showCustomer.jsp?customerId="+busiId;
	$("#show_customer_iframe").attr("src", ifmUrl);
	$("#show_customer_div").dialog("open");
}
//关闭展示标签窗口，由showLabel.jsp调用
function closeShowLabelDialog(){
	$("#show_customer_div").dialog("close");
}
</script>
</head>
<body>
<div class="aibi_search">
     <!-- 简单查询 -->
    <form id="simpleSearchForm">
    <input type="hidden" name="formId" value="simpleSearchForm" />
        <ul class="search_line1 clearfix">
            <li>
                <input type="text" id="simpleSearchName" class="search_input fleft" data-val="false" value="模糊查询"/>
                <input type="hidden" name="alarmThreshold.busiName" id="simpleBusiName"/>
            </li>
            <li><a href="javascript:void(0);" id="simpleSearchBtn" class="queryBtn-bak">查询</a></li>
            <li class="more"><a href="javascript:void(0);">高级查询</a></li>
        </ul>
    </form>
    <!-- 高级查询 -->
    <form id="multiSearchForm" action="">
        <ul class="search_line2" style="display:none">
            <li>
					<li><label  class="fleft">客户群关键字：</label>
                <input id="busiName" name="alarmThreshold.busiName" type="text" class="search_input fleft" data-val="false" value="模糊查询"/>
            </li>
            <li>
					<div class="fleft formQuerySelBox">
			      	  <div class="selTxt">全部预警类型</div>
			      	  <a href="javascript:void(0);" class="selBtn"></a>
			      	  <div class="querySelListBox">
			      	     <input type="hidden"  id="columnId_"  value=""/>
			      	  	 <dl>
			      	  	 	<dd>
			      	  	 		<a href="javascript:void(0);"  value="">全部预警类型</a>
			      	  	 		<a href="javascript:void(0);" value="0">基础</a>
			      	  	 		<a href="javascript:void(0);" value="1">环比</a>
			      	  	 	</dd>
			      	  	 </dl>
			      	  </div>
					</div>
					<input name="alarmThreshold.columnId" id="columnId" type="hidden"/>
			</li>
            <li>
            <a href="javascript:void(0);" id="searchBtn" class="queryBtn-bak"  >查询</a>
                <input id="reset" name="" type="reset" style="display:none;"/>
            </li>
            <li class="more"><a href="javascript:void(0);" id="simpleSearch">简单查询</a></li>
        </ul>
    </form>
    <div class="clear"></div>
</div>

<div class="aibi_button_title">
    <ul>
        <li><a href="javascript:void(0);" id="dialog-new" class="icon_button_add"><span>增加</span></a></li>
        <li class="noborder"><a id="btn-batch-del" href="javascript:void(0);" class="icon_button_delete disable"><span>删除</span></a>
        </li>
    </ul>
</div>

<!-- 增加or删除div end -->
<div class="aibi_area_data">
    <div class="content">
        <jsp:include page="customersAlarmThresholdList.jsp"/>
    </div>
</div>

<!-- dialog -->
 <div id="dialog_edit" style="overflow:hidden;"> 
     <iframe name="dialog_edit" scrolling="no" src="" id="dialog_edit_frame" framespacing="0" border="0" frameborder="0" style="width:100%;height:400px"></iframe> 
 </div> 
<!-- <div id="confirmDialog">
    <iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div> -->
<!-- </div>mainCT end -->

<!-- </div>sync_height end -->
 <div id="show_customer_div"> 
	<iframe id="show_customer_iframe" name="show_customer_iframe" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:421px"></iframe> 
 </div> 
<!-- </body> -->
