<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready(function () {
    $("#sys_config_index").addClass("menu_on");

    $("#searchBtn").bind("click", function () {
        multiSearch();
    });

    $("#simpleSearchName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});

    $("#simpleSearchBtn").bind("click", function () {
        simpleSearch();
    });

    $("#dialog-new").click(function (event) {
        editSysAnnouncement(null)
    });

    $("#dialog_edit").dialog({
        autoOpen: false,
        width: 700,
        modal: true,
		resizable:false,
		zIndex:9999
    });

    $("#confirmDialog").dialog({
        autoOpen: false,
        width: 500,
		height:185,
        title: "系统提示",
        modal: true,
		resizable:false
    });

    $("select").multiselect({
        multiple: false,
        header: false,
        height: "auto",
        selectedList: 1
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
      $(document).click(function(ev){
         var e = ev||event ;
         e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
         $(".querySelListBox").slideUp();
         $(".selBtn").removeClass("selBtnActive");
    })
    
    //$("#btn-batch-del").bind("click", deleteMultiSysAnnouncement);
    search();
  //搜索区域高级查询
	$(".aibi_search ul li.more a").click(function(){
			if($(this).html()=="高级查询"){
				$(".aibi_search ul.search_line1").hide();
				$(".aibi_search ul.search_line2").show();
			}else{
				$(".aibi_search ul.search_line2").hide();
				$(".aibi_search ul.search_line1").show();
			}
		});
});

function pagetable() {
	$(window).scrollTop(0);
    var formId = "simpleSearchForm";
    if ($(".search_line1").css("display") == "none") {
        formId = "multiSearchForm";
    }
    var actionUrl = 'ciSysAnnouncementAction!query.ai2do?' + $("#pageForm").serialize();
    $("#sysAnnouncementTableList2").page(
		{
			url: actionUrl, 
			param: $("#" + formId).serialize(),
			objId: 'sysAnnouncementTableList2', 
			callback: function(){
				pagetable();
			} 
		}
    );
    /*if($("#alarmTable_sort").find("tr").size()>1){
		$("#alarmTable_sort").tablesorter({
			sortList: [[1,0]],
			headers: { 9: { sorter: false},0: { sorter: false} }
		});
	}*/
	datatable();
}

function search() {
    var formId = "simpleSearchForm";
    if ($(".search_line1").css("display") == "none") {
        formId = "multiSearchForm";
    } else {
        var announcementName = $("#simpleSearchName").val();
        if (announcementName == "模糊查询") {
            announcementName = "";
        }
        $("#simpleBusiName").val(announcementName);
    }


    var actionUrl = 'ciSysAnnouncementAction!query.ai2do?' + $("#syspageForm").serialize();
    var sysType = "";
    if($("#typeId").val()!=""){
    	sysType = $("#typeId").parents(".querySelListBox").prevAll(".selTxt").html();
    }
    var sysPriority = "";
    if($("#priorityId").val()!=""){
    	sysPriority = $("#priorityId").parents(".querySelListBox").prevAll(".selTxt").html();
    }
    var sysStatus = "";
    if($("#status").val()!=""){
    	sysStatus = $("#status").parents(".querySelListBox").prevAll(".selTxt").html();
    }
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: $("#" + formId).serialize()+"&sysType="+sysType+"&sysPriority="+sysPriority,
        success:function(html){
            $("#sysAnnouncementTableList2").html("");
            $("#sysAnnouncementTableList2").html(html);
            pagetable();
            checkDelete();
            datatable();
        }
    });
}
function datatable() {
    $(".commonTable mainTable").datatable();
}

function simpleSearch() {
	var serachStr = "";
    if ("模糊查询" == $.trim($("#simpleSearchName").val())) {
        serachStr = "";
    } else {
        serachStr = $.trim($("#simpleSearchName").val());
    }
    $("#announcementName").val(serachStr);
    search();
}

function multiSearch() {
    var serachStr = $.trim($("#announcementName").val());
    if ("" == serachStr) {
        $("#simpleSearchName").val("模糊查询");
    } else {
        $("#simpleSearchName").val(serachStr);
    }
    search();
}

jQuery.focusblur = function (focusid) {
    var focusblurid = $(focusid);
    var defval = focusblurid.val();
    focusblurid.focus(function () {
        var thisval = $(this).val();
        if (thisval == defval) {
            $(this).val("");
        }
    });
    focusblurid.blur(function () {
        var thisval = $(this).val();
        if (thisval == "") {
            $(this).val(defval);
        }
    });
};

//批量删除阈值
function deleteMultiSysAnnouncement() {
    openConfirm("确定要删除选择的系统公告么？","multiRecords");
}

//删除单个阈值
function deleteSysAnnouncement(thresholdId) {
    openConfirm("确定要删除选择的系统公告么？","sysAnnouncement.announcementIdList=" + thresholdId);
}

//删除阈值
function deleteSysAnnouncementByParam(param) {
    var actionUrl = $.ctx + '/ci/ciSysAnnouncementAction!delete.ai2do?' + param;
    $.ajax({
        type: "POST",
        url: actionUrl,
        success: function (result) {
            if (result.success) {
            	//弱提示
            	$.fn.weakTip({"tipTxt":"删除成功！"});
            	refreshList_reName();
            }else{
            	showAlert("删除失败：" +result.msg,"failed");
            }
        }
    });
}

function editSysAnnouncement(announcementId) {
    var url = "ci/ciSysAnnouncementAction!toEdit.ai2do?";
    var title = "新增系统公告";
    if (announcementId != null) {
        url = url + "sysAnnouncement.announcementId=" + announcementId;
        title = "修改系统公告";
    }
    $("#dialog_edit").dialog("close");
    $("#dialog_edit_frame").attr("src", url).load(function(){
        $("#dialog_edit").dialog({ "title":title});
        $("#dialog_edit").dialog("open");});
    $(".tishi").each(function(){
        var toffset=$(this).parent("td").offset();
        var td_height=$(this).parent("td").height();
        $(this).css({top:toffset.top + td_height + 13, left: toffset.left});
    });
}

function checkDelete(){
    if($(".checkbox:checked").size()>0){
        $(".icon_button_delete").removeClass("disable");
        $("#btn-batch-del").bind("click", deleteMultiSysAnnouncement);
    }else{
        $(".icon_button_delete").addClass("disable");
        $("#btn-batch-del").unbind("click");
    }
}

//打开确认弹出层
function openConfirm(confirmMsg, id){
    confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
    $("#confirmDialog").dialog("close");
    var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
    $("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
//操作成功后刷新列表
function refreshList_reName(){
	$("#sysAnnouncementTableList2").html("");
	search();
}
function closeConfirmDialog(dialogId, flag,param){
    $(dialogId).dialog("close");
    if(flag){
        if(param == "multiRecords"){
            param = $("input[name='sysAnnouncement.announcementIdList']:checked").serialize();
        }
        deleteSysAnnouncementByParam(param);
    }
}
</script>
<div class="sync_height">
    <div class="mainBody">
        <!-- search -->
        <div id="mainContent">
            <div class="aibi_search">
	        <input type="hidden" name="formId" value="simpleSearchForm">
	        <!-- 简单查询 -->
	        <form id="simpleSearchForm">
	            <ul class="search_line1 clearfix">
	                <li>
   	  	  	 	 	<input type="text"  id="simpleSearchName" class="search_input fleft" value="模糊查询"/>
	                <input type="hidden" name="sysAnnouncement.announcementName" id="simpleBusiName"/>
	                </li>
	                <li>
	                	<a href="javascript:void(0);" id="simpleSearchBtn" class="queryBtn-bak">查询</a>
	                </li>
	                <li class="more"><a href="javascript:void(0);">高级查询</a></li>
	            </ul>
	        </form>
	        <!-- 高级查询 -->
	        <form id="multiSearchForm" action="">
	            <ul class="search_line2 clearfix" style="display:none">
	                <li>
	                    <span>公告标题：</span>
	                    <input id="announcementName" name="sysAnnouncement.announcementName" type="text" class="aibi_input" value=""/>
	                </li>
	                <li>
	                <span class="fleft" >公告类型：</span>
					<div class="fleft formQuerySelBox">
	             	  <div class="selTxt">全部公告类型</div>
	             	  <a href="javascript:void(0);" class="selBtn"></a>
	             	  <div class="querySelListBox">
	             	     <input type="hidden"  name="sysAnnouncement.typeId" id="typeId" value=""/>
	             	  	 <dl>
	             	  	 	<dd>
							   	<a href="javascript:void(0);"  value="">全部公告类型</a>
							   	<c:forEach var="dimAnnouncementType" items="${dimAnnouncementTypeList}">
							   		<a href="javascript:void(0);" value="${dimAnnouncementType.typeId }">${dimAnnouncementType.typeName }</a>
							   	</c:forEach>
	             	  	 	</dd>
	             	  	 </dl>
	             	  </div>
					</div>
	                </li>
	                <li>
	                    <span class="fleft">公告优先级：</span>
	                    <div class="fleft formQuerySelBox">
	             	  <div class="selTxt">全部公告优先级</div>
	             	  <a href="javascript:void(0);" class="selBtn"></a>
	             	  <div class="querySelListBox">
	             	     <input type="hidden"  name="sysAnnouncement.priorityId" id="priorityId" value=""/>
	             	  	 <dl>
	             	  	 	<dd>
	             	  	 		<a href="javascript:void(0);"  value="">全部公告优先级</a>
	             	  	 		<a href="javascript:void(0);"  value="0">重要</a>
	             	  	 		<a href="javascript:void(0);" value="1">普通</a>
	             	  	 	</dd>
	             	  	 </dl>
	             	  </div>
					</div>
	                </li>
	                <li>
	                    <span class="fleft">公告状态：</span>
	                     <div class="fleft formQuerySelBox">
	             	  <div class="selTxt">全部公告状态</div>
	             	  <a href="javascript:void(0);" class="selBtn"></a>
	             	  <div class="querySelListBox">
	             	     <input type="hidden"  name="sysAnnouncement.status" id="status" value=""/>
	             	  	 <dl>
	             	  	 	<dd>
	             	  	 		<a href="javascript:void(0);"  value="">全部公告状态</a>
	             	  	 		<a href="javascript:void(0);"  value="1">有效</a>
	             	  	 		<a href="javascript:void(0);" value="2">失效</a>
	             	  	 	</dd>
	             	  	 </dl>
	             	  </div>
					</div>
					</li>
	                <li>
	                    <!--<input id="searchBtn" name="searchBtn" type="button" class="aibi_search_btn"/>
	                    --><a href="javascript:void(0);" id="searchBtn" class="queryBtn-bak">查询</a>
	                    <input id="reset" name="" type="reset" style="display:none;"/>
	                </li>
	                <li class="more"><a href="javascript:void(0);" id="simpleSearch">简单查询</a></li>
	            </ul>
	        </form>
	    </div>

	    <div class="aibi_button_title">
	        <ul>
	            <li><a href="javascript:void(0);" id="dialog-new" class="icon_button_add"><span>增加</span></a></li>
	            <li class="noborder"><a id="btn-batch-del" href="#" class="icon_button_delete disable"><span>删除</span></a>
	            </li>
	        </ul>
	    </div>
	    <!-- 增加or删除div end -->
	    <div class="aibi_area_data">
	        <div class="content">
	            <jsp:include page="sysAnnouncementList.jsp"/>
	        </div>
	    </div>
	    <div id="dialog_edit"  style="overflow:hidden;">
	        <iframe name="dialog_edit" id="dialog_edit_frame"src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:325px;" scrolling="no" ></iframe>
	    </div>
	    <div id="confirmDialog">
	        <iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
	    </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
