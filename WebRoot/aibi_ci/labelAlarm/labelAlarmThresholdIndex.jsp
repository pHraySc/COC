<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@ include file="/aibi_ci/html_include.jsp" %>
    <title>标签预警设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
    <script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        	$("#alarm_index").addClass("menu_on");
            $("#searchBtn").bind("click", function () {
                multiSearch();
            });

            $.focusblur("#simpleSearchName");

            $("#simpleSearchBtn").bind("click", function () {
                simpleSearch();
            });


            $("#dialog-new").click(function (event) {
                editThreshold(null)
            });

            $("#dialog_edit").dialog({
                autoOpen: false,
                width: 730,
                modal: true,
        		resizable:false
            });

            $("#confirmDialog").dialog({
                autoOpen: false,
                width: 500,
        		height:185,
                title: "系统提示",
                modal: true,
        		resizable:false
            });

            $("#columnId").multiselect({
                multiple: false,
                header: false,
                height: "auto",
                selectedList: 1
            });
            //$("#btn-batch-del").bind("click", deleteMultiThreshold);
            
            $( "#show_label_div" ).dialog({
				autoOpen: false,
				width: 730,
				title:"查看",
				modal: true,
				resizable:false
			});

            search();
        });

        function pagetable() {
            var formId = "simpleSearchForm";
            if ($(".search_line1").css("display") == "none") {
                formId = "multiSearchForm";
            }
            var actionUrl = 'ciLabelAlarmAction!queryThreshold.ai2do?' + $("#pageForm").serialize();
            $("#alarmTable").page({url: actionUrl, param: $("#" + formId).serialize(), objId: 'alarmTable', callback: pagetable }
            );
            /*if($("#alarmTable_sort").find("tr").size()>1){
				$("#alarmTable_sort").tablesorter({
					sortList: [[1,0]],
					headers: { 6: { sorter: false},0: { sorter: false} }
				});
			}*/
        }

        function search() {
            var formId = "simpleSearchForm";
            if ($(".search_line1").css("display") == "none") {
                formId = "multiSearchForm";
            } else {
                var busiName = $("#simpleSearchName").val();
                if (busiName == "模糊查询") {
                    busiName = "";
                }
                $("#simpleBusiName").val(busiName);
            }
            var actionUrl = 'ciLabelAlarmAction!queryThreshold.ai2do?' + $("#pageForm").serialize();
            $.ajax({
                url:actionUrl,
                type:"POST",
                dataType: "html",
                data: $("#" + formId).serialize(),
                success:function(html){
                    $("#alarmTable").html("");
                    $("#alarmTable").html(html);
                    pagetable();
                    checkDelete();
                    datatable();
                }
            })
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
            $("#busiName").val(serachStr);
            search();
        }

        function multiSearch() {
            var serachStr = $.trim($("#busiName").val());
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
        function deleteMultiThreshold() {
            openConfirm("确定要删除选择的预警设置么？","multiRecords");
            //deleteThresholdByParam($("input[name='alarmInfo.thresholdIdList']:checked").serialize());
        }

        //删除单个阈值
        function deleteThreshold(thresholdId) {
            openConfirm("确定要删除选择的预警设置么？","alarmInfo.thresholdIdList=" + thresholdId);
            //deleteThresholdByParam("alarmInfo.thresholdIdList=" + thresholdId)
        }

        //删除阈值
        function deleteThresholdByParam(param) {
            var actionUrl = $.ctx + '/ci/ciLabelAlarmAction!deleteThreshold.ai2do?' + param;
            $.ajax({
                type: "POST",
                url: actionUrl,
                success: function (result) {
                    if (result.success) {
                    	showAlert("删除成功！","success");
                    	refreshList();
                    }else{
                    	showAlert("删除失败：" +result.msg, "failed");
                    }
                }
            });
        }

        function editThreshold(thresholdId) {
            var url = "ci/ciLabelAlarmAction!toEdit.ai2do?";
            var title = "新增标签预警";
            if (thresholdId != null) {
                url = url + "alarmThreshold.thresholdId=" + thresholdId;
                title = "修改标签预警";
            }
            $("#dialog_edit").dialog("close");
            $("#dialog_edit_frame").attr("src", url).load(function(){
                $("#dialog_edit").dialog("option", "title", title);
                $("#dialog_edit").dialog("open");
            });
            $(".tishi").each(function(){
                var toffset=$(this).parent("td").offset();
                var td_height=$(this).parent("td").height();
                $(this).css({top:toffset.top + td_height + 13, left: toffset.left});
            });
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
        //打开确认弹出层
        function openConfirm(confirmMsg, id){
            confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
            $("#confirmDialog").dialog("close");
            var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
            $("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
        }
      	//操作成功后刷新列表
        function refreshList(){
	        $("#alarmTable").html("");
	        search();
        }
      	
        function callBack(){
        	refreshList();
            closeDialog("#dialog_edit");
        }
        
      	//关闭确认弹出层
        function closeDialog(dialogId){
        	$(dialogId).dialog("close");
        }
      	
        function closeConfirmDialog(dialogId, flag,param){
            $(dialogId).dialog("close");
            if(flag){
                if(param == "multiRecords"){
                    param = $("input[name='alarmInfo.thresholdIdList']:checked").serialize();
                }
                deleteThresholdByParam(param);
            }
        }
    	//打开展示标签窗口
    	function showLabelDetail(labelId) {
    		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/showLabel.jsp?labelId="+labelId+"&showWhichContent=labelDetail";
    		var dlgObj = dialogUtil.create_dialog("show_label_div", {
    			"title" :  "标签详情",
    			"height": "auto",
    			"width" : 730,
    			"frameSrc" : ifmUrl,
    			"frameHeight":400,
    			"position" : ['center','center']  
    			
    		});  
    	}
    	//关闭展示标签窗口，由showLabel.jsp调用
    	function closeShowLabelDialog(){
    		$( "#show_label_div" ).dialog( "close" );
    	}
    </script>
<style>
.dialog_btn_div{position:static;}
body,html{height:100%;}
</style>
</head>
<body class="body_bg">
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp"/>

<div class="mainCT">
	<jsp:include page="/aibi_ci/alarmMain.jsp"/>
    <div class="aibi_search" style="height:35px">
        <input type="hidden" name="formId" value="simpleSearchForm">
        <!-- 简单查询 -->
        <form id="simpleSearchForm">
            <ul class="search_line1 clearfix">
                <li>
                    <input type="text" id="simpleSearchName" class="aibi_input"
                           style="width:220px" value="模糊查询"/>
                    <input type="hidden" name="alarmThreshold.busiName" id="simpleBusiName"/>
                </li>
                <li><input name="simpleSearchBtn" id="simpleSearchBtn" type="button" class="aibi_search_btn"/></li>
                <li class="more"><a href="javascript:void(0);">高级查询</a></li>
            </ul>
        </form>
        <!-- 高级查询 -->
        <form id="multiSearchForm" action="">
            <ul class="search_line2" style="display:none">
                <li>
                    <span>标签名称：</span>
                    <input id="busiName" name="alarmThreshold.busiName" type="text" class="aibi_input" value=""/>
                </li>
                <li>
                    <span>预警类型：</span>
                    <select name="alarmThreshold.columnId" id="columnId" class="like_select">
                        <option value="">全部预警类型</option>
                        <option value="0">基础</option>
                        <option value="1">环比</option>
                        <option value="2">占比</option>
                    </select>
                </li>
                <li>
                    <input id="searchBtn" name="searchBtn" type="button" class="aibi_search_btn"/>
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
            <li class="noborder"><a id="btn-batch-del" href="#" class="icon_button_delete disable"><span>删除</span></a>
            </li>
        </ul>
    </div>
    <!-- 增加or删除div end -->
    <div class="aibi_area_data">
        <div class="content">
            <jsp:include page="labelAlarmThresholdList.jsp"/>
        </div>
    </div>


    <div id="dialog_edit">
        <iframe name="dialog_edit" scrolling="no" allowtransparency="true" src=""
                id="dialog_edit_frame" framespacing="0" border="0" frameborder="0"
                style="width:100%;height:355px"></iframe>
    </div>
    <div id="confirmDialog">
        <iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
    </div>
</div><!-- mainCT end -->
    <div class="clear"></div>
</div><!-- sync_height end -->
</body>
</html>