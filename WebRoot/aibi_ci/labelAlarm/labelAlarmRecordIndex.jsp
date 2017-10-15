<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@ include file="/aibi_ci/html_include.jsp" %>
    <title>标签预警结果</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
    <script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        	$("#alarm_index").addClass("menu_on");
            $(".no19").parent("li").siblings("li").removeClass("top_menu_on");
            $(".no19").parent("li").addClass("top_menu_on");

            $("#searchBtn").bind("click", function () {
                multiSearch();
            });

            $.focusblur("#simpleSearchName");

            $("#simpleSearchBtn").bind("click", function () {
                simpleSearch();
            });

            $("#columnId").multiselect({
                multiple: false,
                header: false,
                height: "auto",
                selectedList: 1
            });
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
            var actionUrl = 'ciLabelAlarmAction!queryRecord.ai2do?' + $("#pageForm").serialize();
            $("#alarmTable").page({url: actionUrl, param: $("#" + formId).serialize(), objId: 'alarmTable', callback: pagetable }
            );
			/*if($("#alarmTable").find("tr").size()>1){
				$("#alarmTable_sort").tablesorter({
					sortList: [[0,0],[1,2],[2,2],[3,2],[4,2]]
				});
			}*/
        }
        function search() {
            var formId = "simpleSearchForm";
            if ($(".search_line1").css("display") == "none") {
                formId = "multiSearchForm";
            }else {
                var simpleBusiName = $("#simpleSearchName").val();
                if (simpleBusiName == "模糊查询") {
                    simpleBusiName = "";
                }
                $("#simpleBusiName").val(simpleBusiName);
            }
            var actionUrl = 'ciLabelAlarmAction!queryRecord.ai2do?' + $("#pageForm").serialize();
            $.ajax({
                url:actionUrl,
                type:"POST",
                dataType: "html",
                data: $("#" + formId).serialize(),
                success:function(html){
                    $("#alarmTable").html("");
                    $("#alarmTable").html(html);
                    pagetable();
                    datatable();
                }
            })
        }
        function datatable() {
            $(".commonTable mainTable").datatable();
        }

        function simpleSearch() {
            if ("模糊查询" == $.trim($("#simpleSearchName").val())) {
                serachStr = "";
            } else {
                serachStr = $.trim($("#simpleSearchName").val());
            }
            $("#busiName").val(serachStr);
            search();
        }

        function multiSearch() {
            var serachStr = $.trim($("#simpleSearchName").val());
            if ("模糊查询" == $.trim($("#simpleSearchName").val())) {
                serachStr = "";
            } else {
                serachStr = $.trim($("#simpleSearchName").val());
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
<style type="text/css">
body,html{height:100%;}
</style>
</head>
<body class="body_bg" style="height:610px">
<div class="sync_height">
<jsp:include page="/aibi_ci/navigation.jsp"/>

<div class="mainCT">
	<jsp:include page="/aibi_ci/alarmMain.jsp"/>
    <div class="aibi_search" style="height: 35px;">
        <input type="hidden" name="formId" value="simpleSearchForm"/>
        <!-- 简单查询 -->
        <form id="simpleSearchForm">
            <ul class="search_line1 clearfix">
                <li>
                    <input type="text" id="simpleSearchName" class="aibi_input"
                           style="width:220px" value="模糊查询"/>
                    <input type="hidden" name="alarmInfo.busiName" id="simpleBusiName"/>
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
                    <input id="busiName" name="alarmInfo.busiName" type="text" class="aibi_input" value=""/>
                </li>
                <li>
                    <span>预警类型：</span>
                    <select name="alarmInfo.columnId" id="columnId">
                        <option value="">全部预警类型</option>
                        <option value="0">基础</option>
                        <option value="1">环比</option>
                        <option value="2">占比</option>
                    </select>
                </li>
                <li>
                    <span>数据日期</span>
                    <input type="text" class="aibi_input aibi_date" id="startTime"
                           onfocus="if($('#endTime').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"
                           name="alarmInfo.startTime"/>
                </li>
                <li>
                    <span>--</span>
                    <input type="text" class="aibi_input aibi_date" id="endTime"
                           onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"
                           name="alarmInfo.endTime"/>
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

    <div class="aibi_area_data">
        <div class="content">
            <jsp:include page="labelAlarmRecordList.jsp"/>
        </div>
    </div>
</div><!-- mainCT end -->
    <div class="clear"></div>
</div><!-- sync_height end -->
</body>
</html>