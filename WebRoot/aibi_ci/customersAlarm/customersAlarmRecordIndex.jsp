<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<script type="text/javascript">
$(document).ready(function () {
	$("#alarm_index").addClass("menu_on");
	$("#searchBtn").bind("click", function () {
    	multiSearch();
	});
   
	$("#simpleSearchName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	
	$("#busiName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	
	$("#simpleSearchBtn").bind("click", function () {
	    simpleSearch();
	});
	
	$(".num").each(function () {
	    if ($(this).text() == "${pager.pageNum}") {
	        $(this).addClass("num_on");
	    }
	});

	$( "#show_customer_div").dialog({
		autoOpen: false,
		width: 730,
		title:"查看",
		modal: true,
		resizable:false
	});

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
	    
	search();
});
function pagetable() {
     var formId = "simpleSearchForm";
     if ($(".search_line1").css("display") == "none") {
         formId = "multiSearchForm";
     }
     var actionUrl = 'ciCustomersAlarmAction!queryRecord.ai2do?' + $("#pageForm").serialize();
     $("#alarmTable").page({url: actionUrl, param: $("#" + formId).serialize(), objId: 'alarmTable', callback: pagetable }
     );
     /*if($("#alarmTable_sort").find("tr").size()>1){
     	$("#alarmTable_sort").tablesorter({
			sortList: [[0,0],[1,2],[2,2],[3,2],[4,2]]
		});
     }*/
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
    }else {
    	 if ("false" == $.trim($("#simpleSearchName").attr("data-val"))) {
             busiName = "";
    	 }else{
    	    var busiName = $("#simpleSearchName").val();
    	 }
        $("#simpleBusiName").val(busiName);
    }
    var actionUrl = 'ciCustomersAlarmAction!queryRecord.ai2do?' + $("#pageForm").serialize();
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
            //datatable();
        }
    })
}
       /* function datatable() {
           $(".commonTable mainTable").datatable();
       }
*/
function simpleSearch() {
	 var serachStr = "";
	    if ("false" == $.trim($("#simpleSearchName").attr("data-val"))) {
	        serachStr = "";
	    } else {
	        serachStr = $.trim($("#simpleSearchName").val());
	    }
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

<div class="aibi_search">
        <input type="hidden" name="formId" value="simpleSearchForm" />
        <!-- 简单查询 -->
        <form id="simpleSearchForm">
            <ul class="search_line1 clearfix">
                <li>
                    <input type="text" id="simpleSearchName" class="search_input fleft" data-val="false" value="模糊查询"/>
                    <input type="hidden" name="alarmInfo.busiName" id="simpleBusiName"/>
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
                    <input id="busiName" name="alarmInfo.busiName" type="text" class="search_input fleft" data-val="false" value="模糊查询"/>
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
					<input name="alarmInfo.columnId" id="columnId" type="hidden"/>
			</li>
                <li>
                    <span>数据日期</span>
                    <input type="text" class="defineDataInput datepickerBG" id="startTime"
                           onfocus="if($('#endTime').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"
                           name="alarmInfo.startTime"/>
                </li>
                <li>
                    <span>--</span>
                    <input type="text" class="defineDataInput datepickerBG" id="endTime"
                           onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"
                           name="alarmInfo.endTime"/>
                </li>
                <li>
                    <a href="javascript:void(0);" id="searchBtn" class="queryBtn-bak" >查询</a>
                    <input id="reset" name="" type="reset" style="display:none;"/>
                </li>
                <li class="more"><a href="javascript:void(0);" id="simpleSearch">简单查询</a></li>
            </ul>
        </form>
        <div class="clear"></div>
</div>
    <div class="aibi_area_data">
        <div class="content">
            <jsp:include page="customersAlarmRecordList.jsp"/>
        </div>
    </div>
<!-- mainCT end -->
<!-- </div>sync_height end -->
<div id="show_customer_div">
		<iframe id="show_customer_iframe" name="show_customer_iframe" scrolling="no" allowtransparency="true" src=""   framespacing="0" border="0" frameborder="0" style="width:100%;height:421px"></iframe>
</div>
