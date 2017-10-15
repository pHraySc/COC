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

		/*$("#selectOption").multiselect({
			multiple: false,
			height: "auto",
			header: false,
			selectedList: 1
		});*/

		$( "#dialog_approve_ok" ).dialog({
			autoOpen: false,
			width: 730,
			title:"标签审核通过",
			modal: true,
			resizable:false,
			draggable: true
		});

		$( "#dialog_approve_no" ).dialog({
			autoOpen: false,
			width: 730,
			title:"标签审核不通过",
			modal: true,
			resizable:false,
			draggable: true
		});

		$( "#show_label_div" ).dialog({
			autoOpen: false,
			width: 730,
			title:"查看",
			modal: true,
			resizable:false,
			draggable: true
		});

		$("#successDialog").dialog({
			autoOpen: false,
			width: 550,
			title: "系统提示",
			modal: true,
			resizable:false,
			draggable: true
		});

		$("#simpleSearchName").focusblur({ 
			"focusColor":"#222232" ,
		    "blurColor":"#757575"  
		});

        $("#searchLabelButton").bind("click", function () {
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
	var labelIds = [];
	$("input[name='labelIds']:checked").each(function(){
		labelIds.push($(this).val());
	});
	if(labelIds.length > 0 ){
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/approve_ok.jsp?labelIds="+labelIds;
		$("#approve_ok").attr("src", ifmUrl).load(function(){$("#dialog_approve_ok").dialog("open");});
	}	
}
function approveOk(labelId){
	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/approve_ok.jsp?labelIds="+labelId;
	$("#approve_ok").attr("src", ifmUrl).load(function(){$("#dialog_approve_ok").dialog("open");});
}
function multiApproveNo(){
	var labelIds = [];
	$("input[name='labelIds']:checked").each(function(){
		labelIds.push($(this).val());
	});
	if(labelIds.length > 0 ){
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/approve_no.jsp?labelIds="+labelIds;
		$("#approve_no").attr("src", ifmUrl).load(function(){$("#dialog_approve_no").dialog("open");});
	}	
}
function approveNo(labelId){
	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/approve_no.jsp?labelIds="+labelId;
	$("#approve_no").attr("src", ifmUrl).load(function(){$("#dialog_approve_no").dialog("open");});
	
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
//设置删除是否可见
function checkDelete(){
	if($(".checkBox:checked").size()>0){
		$(".icon_button_ok").removeClass("disable").removeAttr("title");	
		$(".icon_button_no").removeClass("disable").removeAttr("title");
	}else{
		$(".icon_button_ok").addClass("disable").attr("title","请至少选择一条记录！");
		$(".icon_button_no").addClass("disable").attr("title","请至少选择一条记录！");
	}
}

//打开展示标签窗口
function showLabelDetail(labelId) {
	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/showLabel.jsp?labelId="+labelId+"&showWhichContent=both";;
	$("#show_label_iframe").attr("src", ifmUrl);
	$( "#show_label_div" ).dialog( "open" );
}
//关闭展示标签窗口，由showLabel.jsp调用
function closeShowLabelDialog(){
	$( "#show_label_div" ).dialog( "close" );
}
function simpleSearch() {
    var serachStr = "";
    if ("模糊查询" == $.trim($("#simpleSearchName").val())) {
        serachStr = "";
    } else {
        serachStr = $.trim($("#simpleSearchName").val());
    }
    $("#labelName").val(serachStr);
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
    var labelName = $("#simpleSearchName").val();
    if (labelName == "模糊查询") {
    	labelName = "";
    }
    $("#labelName").val(labelName);
    var actionUrl = "";
    if($("#selectOption").val() == 2)
	{
		$("#approveButtons").css("display","none");
		actionUrl = '${ctx}/ci/ciLabelInfoApproveAction!queryApproved.ai2do?' + $("#pageForm").serialize();
		//$("#labelList").load($.ctx + "/ci/ciLabelInfoApproveAction!queryApproved.ai2do?" + $("#pageForm").serialize()+'&'+$("#queryForm").serialize());
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
               }
         })
	}else if($("#selectOption").val() == 1) {
		$("#approveButtons").css("display","block");
		actionUrl = '${ctx}/ci/ciLabelInfoApproveAction!query.ai2do?' + $("#pageForm").serialize();
		//$("#labelList").load($.ctx + "/ci/ciLabelInfoApproveAction!query.ai2do?" + $("#pageForm").serialize()+'&'+$("#queryForm").serialize());	
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
	var actionUrl = '${ctx}/ci/ciLabelInfoApproveAction!query.ai2do?' + $("#pageForm").serialize();
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
	var actionUrl = '${ctx}/ci/ciLabelInfoApproveAction!queryApproved.ai2do?' + $("#pageForm").serialize();
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
    var actionUrl = '${ctx}/ci/ciLabelInfoApproveAction!query.ai2do?' + $("#pageForm").serialize();
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: $("#queryForm").serialize(),
        success:function(html){
            $("#labelList").html("");
            $("#labelList").html(html);
            pagetable();
            checkDelete();
            datatable();
        }
    })
	//$("#labelList").load($.ctx + "/ci/ciLabelInfoAction!query.ai2do?" + $("#pageForm").serialize()+'&'+$("#queryForm").serialize());
}

function closeApproveNoDialog(){
	$("#dialog_approve_no").dialog("close");
    var actionUrl = '${ctx}/ci/ciLabelInfoApproveAction!query.ai2do?' + $("#pageForm").serialize();
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: $("#queryForm").serialize(),
        success:function(html){
            $("#labelList").html("");
            $("#labelList").html(html);
            pagetable();
            checkDelete();
            datatable();
        }
    })
}

//打开审批成功弹出层，保存成功窗口需要自定义
function openSuccessForNo(successMsg,labelIds){
	successMsg = encodeURIComponent(encodeURIComponent(successMsg));
	//labelName = encodeURIComponent(encodeURIComponent(labelName));
	//labelIds = encodeURIComponent(encodeURIComponent(labelIds));
	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/approveNOSuccDialog.jsp?successMsg="+successMsg+"&labelIds="+labelIds;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
}
//打开审批成功弹出层，保存成功窗口需要自定义
function openSuccessForOk(successMsg,labelIds){
	successMsg = encodeURIComponent(encodeURIComponent(successMsg));
	//labelName = encodeURIComponent(encodeURIComponent(labelName));
	//labelIds = encodeURIComponent(encodeURIComponent(labelIds));
	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/approveOKSuccDialog.jsp?successMsg="+successMsg+"&labelIds="+labelIds;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
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
<div class="aibi_search">
	<form id="queryForm" action="" method="post">
	<ul class="clearfix">
		<li>
			<input type="text"  id="simpleSearchName" class="search_input fleft"   value="模糊查询"/>
			<!--<input type="text" id="simpleSearchName" class="aibi_input"
	                           style="width:220px" value="模糊查询"/>
			--><input id="labelName" name="searchConditionInfo.labelName" type="hidden"></input>
		</li>
		<li>
		<div class="fleft formQuerySelBox">
	      	  <div class="selTxt">待审批标签</div>
	      	  <a href="javascript:void(0);" class="selBtn"></a>
	      	  <div class="querySelListBox">
	      	     <input type="hidden"   id="selectOption" value="1"/>
	      	  	 <dl>
	      	  	 	<dd>
	      	  	 		<a href="javascript:void(0);"  value="1">待审批标签</a>
	      	  	 		<a href="javascript:void(0);" value="2">已处理标签</a>
	      	  	 	</dd>
	      	  	 </dl>
	      	  </div>
			</div>
			</li>
	    <li>
		     <span class="line2_span">审批时间：</span>
		     <input style="cursor: pointer;" name="searchConditionInfo.startDate" id="startDate" readonly type="text" class="aibi_input aibi_date"  
		     onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"/> -- 
			 <input style="cursor: pointer;" name="searchConditionInfo.endDate" id="endDate" readonly type="text" class="aibi_input aibi_date"  
			 onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"/>
		</li>
		<li>
			<a href="javascript:void(0);" id="searchLabelButton" class="queryBtn-bak">查询</a>
		</li>
	</ul>
	<div class="clear"></div>
	</form>
</div>
<div class="aibi_button_title">
	<ul id="approveButtons">
		<li><a href="javascript:multiApproveOk();" id="multiApproveOk" class="icon_button_ok disable" title="请至少选择一条记录！"><span>审批通过</span></a></li>
		<li class="noborder" id="dialog-no"><a href="javascript:multiApproveNo();" class="icon_button_no disable" title="请至少选择一条记录！"><span>审批不通过</span></a></li>
	</ul>
</div>
<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/ciLabelInfo/ciLabelInfoApproveList.jsp" />
</div>
<div class="clear"></div>

<div id="dialog_approve_ok">
	<iframe name="approve_ok" scrolling="no" allowtransparency="true" src="" id="approve_ok"  framespacing="0" border="0" frameborder="0" style="width:100%;height:351px"></iframe>
</div>
<div id="dialog_approve_no">
	<iframe name="approve_no" scrolling="no" allowtransparency="true" src="" id="approve_no"  framespacing="0" border="0" frameborder="0" style="width:100%;height:351px"></iframe>
</div>
<div id="show_label_div">
	<iframe id="show_label_iframe" name="show_label_iframe" scrolling="yes" allowtransparency="true" src=""   framespacing="0" border="0" frameborder="0" style="width:100%;height:400px;"></iframe>
	<!-- 按钮区 -->
	<div class="dialog_btn_div">
		<input name="" type="button" value=" 确 定 " onclick="closeShowLabelDialog(); " class="tag_btn"/>
	</div>
</div>
<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:320px"></iframe>
</div>