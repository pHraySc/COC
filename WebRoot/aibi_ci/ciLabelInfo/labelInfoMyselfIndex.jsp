<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		//将增加标签设置为dialog
		$( "#add_label_div" ).dialog({
			autoOpen: false,
			width: 730,
			title:"新建标签",
			modal: true,
			resizable:false,
			zIndex:99999
		});
		
		$( "#show_label_div" ).dialog({
			autoOpen: false,
			width: 730,
			title:"查看",
			modal: true,
			resizable:false,
			zIndex:99999
		});
		$("#confirmDialog").dialog({
			autoOpen: false,
			width: 500,
			height:185,
			title: "系统提示",
			modal: true,
			resizable:false
		});
		$("#explanatorySuccessDialog").dialog({
			autoOpen: false,
			width: 550,
			title: "系统提示",
			modal: true,
			resizable:false
		});

		$("#simpleSearchName").focusblur({ 
			"focusColor":"#323232" ,
		    "blurColor":"#8e8e8e"  
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
	//打开添加标签窗口
	function openAddLabelDialog(){
		var actionUrl = "${ctx}/ci/ciLabelInfoAction!saveLabelInfoInit.ai2do";
		$("#add_label_iframe").attr("src", actionUrl);
		$("#add_label_div").dialog("option", "title", "新建标签");
		$("#add_label_div").dialog( "open" );
	}
	//标签创建成功关闭创建窗口,在addLabel.jsp中调用
	function closeAddLabelDialog(){
		$("#add_label_div").dialog("close");
        var actionUrl = '${ctx}/ci/ciLabelInfoAction!query.ai2do?' + $("#pageForm").serialize();
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
            }
        });
	}
	
	//打开编辑标签窗口，窗口关闭时也调用的是：closeAddLabelDialog
	function editDialog(labelId){
		var ifmUrl = "${ctx}/ci/ciLabelInfoAction!saveLabelInfoInit.ai2do?labelId="+labelId;
		$("#add_label_iframe").attr("src", ifmUrl);
		$("#add_label_div").dialog("option", "title", "修改标签");
		$( "#add_label_div" ).dialog( "open" );
	}

	//关闭确认弹出层
	function closeDialog(dialogId){
		$(dialogId).dialog("close");
	}
	
	//打开展示标签窗口
	function showLabelDetail(labelId) {
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/showLabel.jsp?labelId="+labelId+"&showWhichContent=both";
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
		
	//点击批量删除时所调用的js方法用来删除选中的标签
	function delAll(){
		var labelIds = "";
		$(".checkBox").each(function(){
			var checked = this.checked;
			if(checked){
				labelIds += (labelIds == "" ? "" :',') + $(this).val();
			}
		});
		if(labelIds != ""){
			openConfirm("确认删除所选标签？",labelIds);
		}
	}
	//点击删除一个标签时调用的方法
	function deleteLabel(labelId) { 
		openConfirm("确认删除该标签？",labelId);
	}

	//打开确认弹出层,由于confirmDialog中调用了父页面的closeConfirmDialog和“#confirmDialog”
	//所以在父页面中必须要有这两个内容
	function openConfirm(confirmMsg, id){	
		confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
		var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
		$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
		
	}
	//关闭确认窗口，并处理关闭确认之后的业务逻辑，由confirmDialog.jsp调用
	function closeConfirmDialog(dialogId, flag,id){
		$(dialogId).dialog("close");
		if(flag){
			del(id);
		}
	}
	//删除标签的具体业务逻辑，支持批量删除，其中 $(".num_on").click 实现了页码保持：在哪个页码里做的删除，删除后还是在哪页
	function del(labelId){
		var actionUrl = $.ctx + "/ci/ciLabelInfoAction!del.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelIds="+labelId,
			success: function(result){
				if(result.success){
					showAlert("" +result.msg,"success");
					$("input[name='pager.totalSize']").val(0);
					searchAfterDelete();//刷新列表
				}else{
					showAlert("删除失败：" +result.msg,"failed");
				}
			}
		});	
	}
	//打开成功弹出层，提交审批的成功窗口需要自定义
	function openSubmitApproveSuccess(successMsg,labelName,labelId){
		successMsg = encodeURIComponent(encodeURIComponent(successMsg));
		labelName = encodeURIComponent(encodeURIComponent(labelName));
		labelId = encodeURIComponent(encodeURIComponent(labelId));
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/submitApprSuccDialog.jsp?successMsg="+successMsg+"&labelName="+labelName+"&labelId="+labelId;
		$("#explanatorySuccessFrame").attr("src", ifmUrl).load(function(){$("#explanatorySuccessDialog").dialog("open");});
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
			$(".icon_button_delete").removeClass("disable").removeAttr("title");	
		}else{
			$(".icon_button_delete").addClass("disable").attr("title","请至少选择一条未提交审批记录！");
		}
	}
	//提交标签审批业务逻辑
	function submitApprove(labelId,labelName) {
		var actionUrl = $.ctx + "/ci/ciLabelInfoAction!submitApprove.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId,
			success: function(result){
			if(result.success) {
				openSubmitApproveSuccess("" +result.msg,labelName,labelId);
				 //先刷新页面，然后点击到对应的页码
				var num_on=$(".num_on");
				var pageNum=num_on.text();
				searchAfterDelete();
		        $(".num").each(function () {
		            if ($(this).text() == pageNum) {
			            $(this).addClass("num_on");
		                $(this).click();
		            }
				});
				}
			}
		});	
	}

    function simpleSearch() {
        var serachStr = "";
        if ("模糊查询" == $.trim($("#simpleSearchName").val())) {
            serachStr = "";
        } else {
            serachStr = $.trim($("#simpleSearchName").val());
        }
        $("#labelName").val(serachStr);
        $("#busiCaliber").val(serachStr);
        $("#createDesc").val(serachStr);
        search();
    }

    function search() {
    	//关键字查询  标签名称、业务口径、应用建议
        var keyWord = $("#simpleSearchName").val();
        if (keyWord == "模糊查询") {
        	keyWord = "";
        }
        $("#labelName").val(keyWord);
        $("#busiCaliber").val(keyWord);
        $("#createDesc").val(keyWord);
        
        $("#searchValue").val($("#queryForm").serialize());
        
        var actionUrl = '${ctx}/ci/ciLabelInfoAction!query.ai2do?' + $("#pageForm").serialize();
        $.ajax({
            url:actionUrl,
            type:"POST",
            dataType: "html",
            data: $("#queryForm").serialize(),
            success:function(html){
        		$(window).scrollTop(0);
                $("#labelList").html("");
                $("#labelList").html(html);
                var totalSize = $("#totalSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
                pagetable();
                checkDelete();
            }
        })
    }

    //删除后查询
    function searchAfterDelete() {
        var keyWord = $("#simpleSearchName").val();
        if (keyWord == "模糊查询") {
        	keyWord = "";
        }
        $("#labelName").val(keyWord);
        $("#busiCaliber").val(keyWord);
        $("#createDesc").val(keyWord);
        
        var actionUrl = '${ctx}/ci/ciLabelInfoAction!query.ai2do?' + $("#pageForm").serialize();
        $.ajax({
            url:actionUrl,
            type:"POST",
            dataType: "html",
            data: $("#searchValue").val(),
            success:function(html){
        		$(window).scrollTop(0);
                $("#labelList").html("");
                $("#labelList").html(html);
                var totalSize = $("#totalSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
                pagetable();
                checkDelete();
            }
        })
    }

    function pagetable(){
		$(window).scrollTop(0);
    	/*
    	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
    	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
    	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
    	*/
    	var actionUrl = '${ctx}/ci/ciLabelInfoAction!query.ai2do?' + $("#pageForm").serialize();
    	$("#labelList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'labelList',callback: pagetable });
    	//每刷新一次查询结果，都需要判断是否显示批量删除按钮
    	checkDelete();
    }

  //标签停用、上线、下线时候显示遮罩
    function showOverlay(){
    	if('undefined' == typeof(document.body.style.maxHeight)){
    		$(window).bind("scroll",ie6_noscroll)
    	}else{
    		$("body,html").css("overflow","hidden");
    	}
    	var obj=$(window);
    	var w=obj.width();
    	var h=obj.height();
    	var _left=obj.scrollLeft();
    	var _top=obj.scrollTop();
    	var html=$('<div class="overlay"><div class="loadding"></div></div>');
    	html.css({"left":_left,"top":_top,"width":w,"height":h});
    	$("body").append(html);
    }
    function hideOverlay(){
    	$("body").find("> .overlay").remove();
    	if('undefined' == typeof(document.body.style.maxHeight)){
    		$(window).unbind("scroll",ie6_noscroll)
    	}else{
    		$("body,html").css("overflow","auto");
    	}
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
			<!--<input type="text" id="simpleSearchName" class="aibi_input" style="width:220px" value="模糊查询"/>
			--><input type="text" id="labelName" name="searchConditionInfo.labelName" style="display:none;"></input>
			<input id="busiCaliber" name="searchConditionInfo.busiCaliber" type="hidden"></input>
			<input id="createDesc" name="searchConditionInfo.createDesc" type="hidden"></input>
		</li>
		<li>
			<div class="fleft formQuerySelBox">
	      	  <div class="selTxt">全部数据状态</div>
	      	  <a href="javascript:void(0);" class="selBtn"></a>
	      	  <div class="querySelListBox">
	      	     <input type="hidden"  name="searchConditionInfo.dataStatusId" id="dataStatusId" value=""/>
	      	  	 <dl>
	      	  	 	<dd>
	      	  	 		<a href="javascript:void(0);"  value="">全部数据状态</a>
	      	  	 		<c:forEach items="${dimLabelDataStatusList}" var="dimLabelDataStatus" varStatus="st">
	      	  	 			<a href="javascript:void(0);" value="${dimLabelDataStatus.dataStatusId }">${dimLabelDataStatus.dataStatusName }</a>
	      	  	 		</c:forEach>
	      	  	 	</dd>
	      	  	 </dl>
	      	  </div>
			</div>
		  </li>
		<li>
			<div class="fleft formQuerySelBox">
	      	  <div class="selTxt">全部审批状态</div>
	      	  <a href="javascript:void(0);" class="selBtn"></a>
	      	  <div class="querySelListBox">
	      	     <input type="hidden"  name="searchConditionInfo.currApproveStatusId" id="currApproveStatusId" value=""/>
	      	  	 <dl>
	      	  	 	<dd>
	      	  	 		<a href="javascript:void(0);"  value="">全部审批状态</a>
		      	  	 	<c:forEach items="${dimApproveStatusList}" var="dimApproveStatus" varStatus="st">
		      	  	 		<a href="javascript:void(0);" value="${dimApproveStatus.approveStatusId }">${dimApproveStatus.approveStatusName }</a>
		      	  	 	</c:forEach>
	      	  	 	</dd>
	      	  	 </dl>
	      	  </div>
			</div>
			</li>
		<li>
		<a href="javascript:void(0);" id="searchLabelButton" class="queryBtn-bak">查询</a>
		<!-- <input id="searchLabelButton" name="" type="button" value="" class="aibi_search_btn"></input> -->
		</li>
	</ul>
	<div class="clear"></div>
	</form>
</div>
<div class="aibi_button_title">
	<ul>
		<li><a href="javascript:void(0);" id="add_label_button" onclick="openAddLabelDialog()" class="icon_button_add"><span>增加</span></a></li>
		<li class="noborder"><a href="#" class="icon_button_delete disable" onclick="delAll()" title="请至少选择一条未提交审批的记录！"><span>删除</span></a></li>
	</ul>
</div>
<!-- area_data start -->
<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/ciLabelInfo/ciLabelInfoList.jsp" />
</div>

<div class="clear"></div>
<!-- 存放最后一次查询的条件，用于删除时仍按此条件查询 -->
<input type="hidden" id="searchValue" value=""/>

<!-- ui-dialog -->
<div id="add_label_div">
	<iframe id="add_label_iframe" name="add_label_iframe" scrolling="no" allowtransparency="true" src=""   framespacing="0" border="0" frameborder="0" style="width:100%;height:600px"></iframe>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<div id="explanatorySuccessDialog">
	<iframe id="explanatorySuccessFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
<div id="confirm_div" style="display:none;">
    <div class="dialog_table">
		<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
					<div class="confirm showtxt"></div>
				</td>
			</tr>
		</table>
	</div>
    <!-- 按钮区 -->
   <div class="btnActiveBox">
		<a href="javascript:void(0);" id="confirm_true" name=""  class="ensureBtn">确定</a>
		<a href="javascript:void(0);" id="confirm_false" name=""  class="cancelBtn">取消</a>
	</div>
</div>