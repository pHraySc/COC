<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
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
		});

		$(".querySelListBox a").click(function(){
			var selVal = $(this).attr("value");
			var selHtml = $(this).html();
			$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
			$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
			$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
			//向隐藏域中传递值
			$(this).parents(".querySelListBox").slideUp();
				return false;
		});
		$(document).click(function(ev){
			var e = ev||event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			$(".querySelListBox").slideUp();
			$(".selBtn").removeClass("selBtnActive");
		});

		$("#simpleSearchName").focusblur({ 
			"focusColor":"#222232" ,
		    "blurColor":"#757575"  
		});

        $("#searchLabelButton").bind("click", function () {
            simpleSearch();
        });
        search();
		
        $("#importCommon").bind("click", function(){
			importDialog();
        });
        $("#importVert").bind("click", function(){
			importVertLabelDialog();
        });

	});
	//打开添加标签窗口
	function openAddLabel(){
		window.location = '${ctx}/ci/ciLabelInfoConfigAction!saveLabelInfoInit.ai2do';
	}
	
	//打开编辑标签窗口，窗口关闭时也调用的是：closeAddLabelDialog
	function editDialog(labelId){
		window.location = '${ctx}/ci/ciLabelInfoConfigAction!saveLabelInfoInit.ai2do?labelId=' + labelId;
	}

	//关闭确认弹出层
	function closeDialog(dialogId){
		$(dialogId).dialog("close");
	}

	//打开展示标签窗口
	function showLabelDetail(labelId) {
		showConfigLabel(labelId);
	}
	//在标签维护列表打开展示标签窗口
	function showConfigLabel(labelId) {
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/showConfigLabel.jsp?labelId="
			          +labelId+"&showWhichContent=both&showConfigContent=true";
		var dlgObj = dialogUtil.create_dialog("show_label_div3", {
			"title" :  "标签详情",
			"height": "auto",
			"width" : 730,
			"frameSrc" : ifmUrl,
			"frameHeight":580,
			"position" : ['center','center'] 
		});
	}

	//进入标签配置页面
	function configLabel(labelId) {
		var url = $.ctx + "/ci/ciLabelInfoConfigAction!configLabelIndex.ai2do?configLabelInfo.labelId="+labelId;
		window.location = url;
	}
	
	//关闭展示标签窗口，由showLabel.jsp调用
	function closeShowLabelDialog(){
		$( "#show_label_div3" ).dialog( "close" );
	}
		

	//打开确认弹出层,由于confirmDialog中调用了父页面的closeConfirmDialog和“#confirmDialog”
	//所以在父页面中必须要有这两个内容
	function openConfirm(confirmMsg, id){	
		confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
		var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
		var dlgObj = dialogUtil.create_dialog("confirmDialog", {
			"title" :  "系统提示",
			"height": "auto",
			"width" : 500,
			"frameSrc" : ifmUrl,
			"frameHeight":185,
			"position" : ['center','center'] 
			
		});
		//$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
		
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
					searchAfterDelete();
				}else{
					showAlert("删除失败：" +result.msg,"failed");
				}
			}
		});	
	}

	//点击删除一个标签时调用的方法
	function deleteLabel(labelId) { 
		openConfirm("确认删除该标签？",labelId);
	}

	//做全选checkBox操作时调用的js方法，做全选后调用方法设置删除是否可见
	function checkBoxAll(obj){
		$(".checkBox").each(function(){
			if($(this).prop("disabled") != true){
				$(this).prop("checked", obj.checked);
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
        $("#applySuggest").val(keyWord);
        
        $("#searchValue").val($("#queryForm").serialize());
        
        var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!query.ai2do?' + $("#pageForm").serialize();
        $.ajax({
            url:actionUrl,
            type:"POST",
            dataType: "html",
            data: $("#queryForm").serialize(),
            success:function(html){
            	$(window).scrollTop(0);
                $("#labelList").html("");
                $("#labelList").html(html);
                var totalSize = $("#totalConfigSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
                pagetable();
                datatable();
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
        $("#applySuggest").val(keyWord);
        
        var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!query.ai2do?' + $("#pageForm").serialize();
        $.ajax({
            url:actionUrl,
            type:"POST",
            dataType: "html",
            data:  $("#searchValue").val(),
            success:function(html){
        		$(window).scrollTop(0);
                $("#labelList").html("");
                $("#labelList").html(html);
                var totalSize = $("#totalConfigSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
                pagetable();
                datatable();
                checkDelete();
            }
        })
    }

    function openConfirmDialog(labelId,flag,confirmMsg){
    	$(".dialog_table .showTable tr td div").html(confirmMsg);
    	var tar=$("#confirm_div");
    	tar.dialog({
    		autoOpen: false,
    		width: 500,
    		height:200,
    		title:"系统提示",
    		modal: true,
    		resizable:false
    	});
    	if(flag == "stopUse"){
    		$(".dialog_table .showTable tr td div").css("line-height","25px");
    	}else{
    		$(".dialog_table .showTable tr td div").css("line-height","35px");
    	}
    	tar.dialog( "open" );
    	tar.find("#confirm_true" ).unbind("click").bind("click",function(){
    		if(flag == "stopUse"){
    			stopUse(labelId);
        	}
    		if(flag == "onLine"){
    			onLine(labelId);
        	}
    		if(flag == "offLine"){
    			offLine(labelId);
        	}
    		tar.dialog( "close" );
    	});
    	tar.find("#confirm_false" ).click(function(){
    		tar.dialog( "close" );
    	});
    }

    //标签停用
    function stopUseLabel(labelId){
		var msg = "<font size='3'>标签停用可能导致：使用该标签的周期性客户群以后每月跑客户群清单会失败，使用该标签的模板将会失效，确定要停用？</font>";
		openConfirmDialog(labelId,"stopUse",msg);
    }
  	//标签停用
    function stopUse(labelId){
    	showOverlay();
    	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!stopUseAndOffLineLabel.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId+"&searchType=1",
			success: function(result){
				hideOverlay();
				if(result.success){
					showAlert("" +result.msg,"success");
					searchAfterDelete();//刷新列表
				}else{
					showAlert("停用失败:" +result.msg,"failed");
				}
			}
		});
    } 

  	//标签启用
    function onLineLabel(labelId){
		var msg = "确定要启用该标签？";
		openConfirmDialog(labelId,"onLine",msg);
    }

  	//标签启用
    function onLine(labelId){
    	showOverlay();
    	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!onLineLabel.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId,
			success: function(result){
				hideOverlay();
				if(result.success){
					showAlert("" +result.msg,"success");
					searchAfterDelete();//刷新列表
				}else{
					showAlert("启用标签失败:" +result.msg,"failed");
				}
			}
		});
    } 

  	//标签下线 
    function offLineLabel(labelId){
		var msg = "确定将该标签下线？";
		openConfirmDialog(labelId,"offLine",msg);
    }

  	//标签下线
    function offLine(labelId){
    	showOverlay();
    	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!stopUseAndOffLineLabel.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId+"&searchType=2",
			success: function(result){
				hideOverlay();
				if(result.success){
					showAlert("" +result.msg,"success");
					searchAfterDelete();//刷新列表
				}else{
					showAlert("下线失败:" +result.msg,"failed");
				}
			}
		});
    } 


    function pagetable(){
    	/*
    	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
    	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
    	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
    	*/
    	var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!query.ai2do?' + $("#pageForm").serialize();
    	$("#labelList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'labelList',callback: pagetable });
    	
		datatable();
    	//每刷新一次查询结果，都需要判断是否显示批量删除按钮
    }
    function datatable(){
    	$(".commonTable mainTable").datatable();	
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

    function importDialog(){
    	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/importLabel.jsp";
    	var dlgObj = dialogUtil.create_dialog("label_import", {
			"title" :  "标签导入",
			"height": "auto",
			"width" : 680,
			"frameSrc" : ifmUrl,
			"frameHeight":160,
			"position" : ['center','center'] 
			
		},"simpleSearch");
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
	function importVertLabelDialog(){
    	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/importVertLabel.jsp";
		var dlgObj = dialogUtil.create_dialog("label_import", {
			"title" :  "标签导入",
			"height": "auto",
			"width" : 680,
			"frameSrc" : ifmUrl,
			"frameHeight":160,
			"position" : ['center','center'] 
			
		},"simpleSearch");
    }
	
	//标签设置系统推荐与取消系统推荐
	var recomFlag = true;  
	function changeRecomLabel(ths){
		if(recomFlag){
			recomFlag = false;
			var labelId = $(ths).attr("labelId");
			var actionUrl = $.ctx + "/ci/ciLabelInfoAction!changeRecommendLabel.ai2do";
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: {"labelId" : labelId},
				success: function(result){
					if(result.success){
						$.fn.weakTip({"tipTxt":"" +result.msg,"backFn":function(){
							searchAfterDelete();//刷新列表
							recomFlag = true;
						 }});
					}else{
						recomFlag = true;
						showAlert(result.msg,"failed");
					}
				}
			});
		}else{
			return false;
		}
	}
</script>
<div class="aibi_search" style="height:35px">
	<form id="queryForm" action="" method="post">
	<ul>
		<li>
			<input type="text" id="simpleSearchName" class="search_input fleft" value="模糊查询"/>
	        <!--模糊查询 关键字匹配  -->
			<input type="text" id="labelName" name="searchConditionInfo.labelName" style="display:none;"></input>
			<input id="busiCaliber" name="searchConditionInfo.busiCaliber" type="hidden"></input>
			<input id="applySuggest" name="searchConditionInfo.applySuggest" type="hidden"></input>
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
		</li>
	</ul>
	<div class="clear"></div>
	</form>
</div>
<div class="aibi_button_title">
	<ul>
		<li><a href="javascript:void(0);" id="add_label_button" onclick="openAddLabel();" class="icon_button_add"><span>增加</span></a></li>
		<li class="noborder"><a href="javascript:void(0);" class="icon_button_import" id="importCommon"><span>导入</span></a></li>
		<li class="noborder"><a href="javascript:void(0);" class="icon_button_import aibi_btn_long" id="importVert" ><span>导入组合标签</span></a></li>
		<li class="noborder"><a href="javascript:void(0);" class="icon_button_delete disable" onclick="delAll()" title="请至少选择一条未提交审批的记录！"><span>删除</span></a></li>
	</ul>
</div>
<!-- area_data start -->
<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/ciLabelInfo/ciLabelInfoConfigList.jsp" />
</div>
<!-- area_data end -->
<div class="clear"></div>
<!-- 存放最后一次查询的条件，用于删除时仍按此条件查询 -->
<input type="hidden" id="searchValue" value=""/>
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
    <div class="dialog_btn_div btnActiveBox ">
        <input id="confirm_false" name="" type="button" value=" 取 消 " class="cancelBtn"/>
        <input id="confirm_true" name="" type="button" value=" 确 定 " class="ensureBtn"/>
    </div>
</div>
