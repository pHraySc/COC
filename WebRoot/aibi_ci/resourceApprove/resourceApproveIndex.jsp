<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#_label").addClass("menu_on");
		$("#approveLabelTitle").addClass("top_menu_on");
 		$("#createdLabelTitle").removeClass("top_menu_on");
		$("#labelAnalysisTitle").removeClass("top_menu_on");
		$(".tishi").each(function(){
			var toffset=$(this).parent("td").offset();
			var td_height=$(this).parent("td").height();
			$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
		});
		//对class为on的做回显至x-form-field,和模拟select框的功能对应
		$(".x-form-field ul li[class=on]").each(function(){
			$(this).parent("ul").parent("div").prev("input").prev("input").val($(this).html());
			$(this).parent("ul").parent("div").prev("input").val($(this).attr("id")); 
		});
		//end
		aibiTableChangeColor(".mainTable");

		$("#simpleSearchName").focusblur(); 

        $("#searchResourceButton").bind("click", function () {
            simpleSearch();
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
        search();

	});

function multiApproveOk(){
	var statusIds = [];
	$("input[name='statusIds']:checked").each(function(){
		statusIds.push($(this).val());
	});
	if(statusIds.length > 0 ){
		approveOk(statusIds);
	}	
}
function approveOk(statusId){
	var ifmUrl = "${ctx}/aibi_ci/resourceApprove/approve_ok.jsp?statusIds="+statusId;
	var dlgObj = dialogUtil.create_dialog("dialog_approve_ok", {
		"title" :  "资源审批通过",
		"height": "auto",
		"width" : 730,
		"frameSrc" : ifmUrl,
		"frameHeight":300,
		"position" : ['center','center']  
		
	});  
}
function multiApproveNo(){
	var statusIds = [];
	$("input[name='statusIds']:checked").each(function(){
		statusIds.push($(this).val());
	});
	if(statusIds.length > 0 ){
		approveNo(statusIds);
	}	
}
function approveNo(statusId){
	var ifmUrl = "${ctx}/aibi_ci/resourceApprove/approve_no.jsp?statusIds="+statusId;
	var dlgObj = dialogUtil.create_dialog("dialog_approve_no", {
		"title" :  "资源审批不通过",
		"height": "auto",
		"width" : 730,
		"frameSrc" : ifmUrl,
		"frameHeight":300,
		"position" : ['center','center']  
		
	});  
	
}
//做全选checkBox操作时调用的js方法，做全选后调用方法设置删除是否可见
function checkBoxAll(obj){
	$(".checkBox").each(function(){
		if($(this).prop("disabled") != true){
			$(this).prop("checked", obj.checked);
		}
	  });
	checkDelete();
}
//设置批量操作是否可见
function checkDelete(){
	if($(".checkBox:checked").size()>0){
		$(".icon_button_ok").removeClass("disable").removeAttr("title");	
		$(".icon_button_no").removeClass("disable").removeAttr("title");
	}else{
		$(".icon_button_ok").addClass("disable").attr("title","请至少选择一条记录！");
		$(".icon_button_no").addClass("disable").attr("title","请至少选择一条记录！");
	}
}

//打开展示资源窗口
function showLabelDetail(resourceId, resourceDetailLink) {
	 var ifmUrl = "${ctx}" + resourceDetailLink+resourceId;
 	var dlgObj = dialogUtil.create_dialog("show_label_div", {
		"title" :  "资源详情",
		"height": "auto",
		"width" : 730,
		"frameSrc" : ifmUrl,
		"frameHeight":400,
		"position" : ['center','center']  
		
	});  
}
//关闭展示资源窗口，由showLabel.jsp调用
function closeShowLabelDialog(){
	$( "#show_label_div" ).dialog( "close" );
}

function simpleSearch() {
    var serachStr = "";
    if ("false" == $("#simpleSearchName").attr("data-val")) {
    	serachStr = "";
    }else {
        serachStr = $.trim($("#simpleSearchName").val());
    }
    $("#resourceName").val(serachStr);
    search();
}

jQuery.focusblur = function (focusid) {
    var focusblurid = $(focusid);
    var defval = focusblurid.val();
    focusblurid.focus(function () {
        var thisval = $(this).val();
        $(this).css("color","#323232");
        if (thisval == defval) {
            $(this).val("");
        }
    });
    focusblurid.blur(function () {
        var thisval = $(this).val();
        if (thisval == "") {
        	$(this).css("color","#8e8e8e");
            $(this).val(defval);
        }
    });
};

function search() {
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(startDate != null && startDate != "" && endDate != null && endDate != "" && startDate>endDate){
		showAlert("开始时间不能大于结束时间","failed");
		return false;
	}
    var resourceName = $("#simpleSearchName").val();
    if ("false" == $("#simpleSearchName").attr("data-val")) {
    	resourceName = "";
    }
    $("#resourceName").val(resourceName);
    var actionUrl = "";
    if($("#selectOption").val() == 2)
	{
    	$(".aibi_button_title").hide();
		actionUrl = '${ctx}/ci/ciApproveAction!queryApproved.ai2do?' + $("#pageForm").serialize();
		$.ajax({
	        url:actionUrl,
	        type:"POST",
	        dataType: "html",
	        data: $("#queryForm").serialize(),
	        success:function(html){
	            $("#labelList").html("");
	            $("#labelList").html(html);
	            var totalSize = $("#totalSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
	            pagetableProcessed();
	            checkDelete();
	            datatable();
	            $(window).scrollTop(0);
               }
         })
	}else if($("#selectOption").val() == 1) {
		$(".aibi_button_title").show();
		actionUrl = '${ctx}/ci/ciApproveAction!query.ai2do?' + $("#pageForm").serialize();
		$.ajax({
	        url:actionUrl,
	        type:"POST",
	        dataType: "html",
	        data: $("#queryForm").serialize(),
	        success:function(html){
	            $("#labelList").html("");
	            $("#labelList").html(html);
	            var totalSize = $("#totalSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
	            pagetable();
	            checkDelete();
	            datatable();
	            $(window).scrollTop(0);
               }
         })			
	}
}

function pagetable(){
	/*
	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
	*/
	var actionUrl = '${ctx}/ci/ciApproveAction!query.ai2do?' + $("#pageForm").serialize();
	$("#labelList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'labelList',callback: pagetable });
	/*
	//过去的表格排序功能；注释掉
	if($("#labelList").find("tr").size()>1){
		$("#labelList table").tablesorter({
			sortList: [[1,0]]
		});
	}
	*/
	datatable();
	//每刷新一次查询结果，都需要判断是否显示批量删除按钮
	checkDelete();
}

function pagetableProcessed(){
	/*
	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
	*/
	var actionUrl = '${ctx}/ci/ciApproveAction!queryApproved.ai2do?' + $("#pageForm").serialize();
	$("#labelList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'labelList',callback: pagetableProcessed });

	/*if($("#labelList").find("tr").size()>1){
		$("#labelList table").tablesorter({
			sortList: [[0,0]]
		});
	}*/
	datatable();
	//每刷新一次查询结果，都需要判断是否显示批量删除按钮
	checkDelete();
}

function datatable(){
	$(".commonTable mainTable").datatable();	
}

//关闭审批通过窗口
function closeApproveOkDialog(){
	$("#dialog_approve_ok").dialog("close");
	simpleSearch();
}

function closeApproveNoDialog(){
	$("#dialog_approve_no").dialog("close");
	simpleSearch();
}

//打开审批成功弹出层
function openSuccessForNo(successMsg){
	var dlgObj = commonUtil.create_alert_dialog("successForOk", {
		 "txt":successMsg,
		 "type":"success",
		 "width":500,
		 "height":200
	}); 
}
//打开审批成功弹出层
function openSuccessForOk(successMsg){
	var dlgObj = commonUtil.create_alert_dialog("successForOk", {
		 "txt":successMsg,
		 "type":"success",
		 "width":500,
		 "height":200
	}); 
}
//点击单选框
function addRowClass(obj){
	if($(obj).parent().parent("tr").hasClass("hover")) {
		$(obj).parent().parent("tr").removeClass("hover");
	}else{
		$(obj).parent().parent("tr").addClass("hover");
	}
}
</script>
<div class="comWidth pathNavBox pathNavTagBox">
	<span class="pathNavHome"  style="cursor:default;">资源审批</span>
    <span class="pathNavArrowRight"></span>
    <span class="pathNavNext" id="pathResourceText"></span>
  	<span class="amountItem"> 共<span class="amountNum" id="totalSizeNum"></span>个资源</span>
</div>
<div class="aibi_search">
	<form id="queryForm" action="" method="post">
	<ul class="clearfix">
		<li>
			<input type="text"  id="simpleSearchName" class="search_input fleft"   value="模糊查询" data-val="false"/>
			<input id="resourceName" name="searchApproveStatus.resourceName" type="hidden"></input>
		</li>
		<li>
		<div class="fleft formQuerySelBox">
	      	  <div class="selTxt">待审批资源</div>
	      	  <a href="javascript:void(0);" class="selBtn"></a>
	      	  <div class="querySelListBox">
	      	     <input type="hidden"   id="selectOption" value="1"/>
	      	  	 <dl>
	      	  	 	<dd>
	      	  	 		<a href="javascript:void(0);"  value="1">待审批资源</a>
	      	  	 		<a href="javascript:void(0);" value="2">已处理资源</a>
	      	  	 	</dd>
	      	  	 </dl>
	      	  </div>
		</div>
		</li>
		<li>
			<div class="fleft formQuerySelBox">
	      	 <div class="selTxt">全部资源类型</div>
	      	 <a href="javascript:void(0);" class="selBtn"></a>
	      	  <div class="querySelListBox">
	      	     <input type="hidden" name="searchApproveStatus.resourceTypeId" id="resourceTypeId" value=""/>
	      	  	 <dl>
	      	  	 	<dd>
	      	  	 		<a href="javascript:void(0);"  value="">全部资源类型</a>
		      	  	 	<c:forEach items="${resourceTypeList}" var="resourceType" varStatus="st">
		      	  	 		<a href="javascript:void(0);" value="${resourceType.resourceTypeId }">${resourceType.resourceTypeName }</a>
		      	  	 	</c:forEach>
	      	  	 	</dd>
	      	  	 </dl> 
	      	  </div> 
			</div>
		</li>
	    <li>
		     <span class="line2_span">审批时间：</span>
		     <input style="cursor: pointer;" name="searchApproveStatus.startDate" id="startDate" readonly type="text" class="defineDataInput datepickerBG"  
		     onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"/> -- 
			 <input style="cursor: pointer;" name="searchApproveStatus.endDate" id="endDate" readonly type="text" class="defineDataInput datepickerBG"
			 onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"/>
		</li>
		<li>
			<a href="javascript:void(0);" id="searchResourceButton" class="queryBtn-bak">查询</a>
		</li>
	</ul>
	<div class="clear"></div>
	</form>
</div>
<div class="aibi_button_title">
	<ul id="approveButtons">
		<li><a href="javascript:void(0);" onclick="multiApproveOk();" id="multiApproveOk" class="icon_button_ok disable" title="请至少选择一条记录！"><span>审批通过</span></a></li>
		<li class="noborder" id="dialog-no"><a href="javascript:void(0);" onclick="multiApproveNo();" id="multiApproveNO" class="icon_button_no disable" title="请至少选择一条记录！"><span>审批不通过</span></a></li>
	</ul>
</div>
<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/resourceApprove/resourceApproveList.jsp" />
</div>
<div class="clear"></div>