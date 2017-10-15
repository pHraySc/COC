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
        searchClasses();

	});
	
	function callBack(){
	    closeDialog("#dialog_edit");
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
		var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/showClassesLabel.jsp?labelId="
			          +labelId+"&showWhichContent=both&showConfigContent=true";
		var dlgObj = dialogUtil.create_dialog("show_label_div2", {
			"title" :  "标签分类详情",
			"height": "auto",
			"width" : 630,
			"frameSrc" : ifmUrl,
			"frameHeight":215,
			"position" : ['center','center'] 
			
		});
	}
	
	//关闭展示标签窗口，由showLabel.jsp调用
	function closeShowLabelDialog(){
		$( "#show_label_div2" ).dialog( "close" );
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
	
    function simpleSearch() {
    	$(window).scrollTop(0);
        var serachStr = "";
        if ("模糊查询" == $.trim($("#simpleSearchName").val())) {
            serachStr = "";
        } else {
            serachStr = $.trim($("#simpleSearchName").val());
        }
        $("#labelName").val(serachStr);
        searchClasses();
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
    function searchClasses() {
    	//关键字查询  标签名称、业务口径、应用建议
        var keyWord = $("#simpleSearchName").val();
        if (keyWord == "模糊查询") {
        	keyWord = "";
        }
        $("#labelName").val(keyWord);
        $("#busiCaliber").val(keyWord);
        $("#applySuggest").val(keyWord);
        
        $("#searchValue").val($("#queryClassesForm").serialize());
        
        var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!queryClasses.ai2do?' + $("#pageForm").serialize();
        $.ajax({
            url:actionUrl,
            type:"POST",
            dataType: "html",
            data: $("#queryClassesForm").serialize(),
            success:function(html){
            	$(window).scrollTop(0);
                $("#labelClassList").html("");
                $("#labelClassList").html(html);
                var totalSize = $("#totalConfigSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
                pagetable();
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
        
        var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!queryClasses.ai2do?' + $("#pageForm").serialize();
        $.ajax({
            url:actionUrl,
            type:"POST",
            dataType: "html",
            data:  $("#searchValue").val(),
            success:function(html){
                $("#labelClassList").html("");
                $("#labelClassList").html(html);
                var totalSize = $("#totalConfigSize").val();
	            if(totalSize){
		            $("#totalSizeNum").html(totalSize);
				}
                pagetable();
                checkDelete();
            }
        })
    }

  	//标签下线 
    function offLineLabel(labelId){
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"确定将该标签下线？",
			 "type":"confirm",
			 "width":500,
			 "height":200,
			 "param":labelId
		},"offLine");
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
					commonUtil.create_alert_dialog("successDialog", {
						 "txt": result.msg,
						 "type":"success",
						 "width":500,
						 "height":200
					});
					searchAfterDelete();//刷新列表
				}else{
					commonUtil.create_alert_dialog("failedDialog", {
						 "txt": "下线失败:" +result.msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					});
				}
			}
		});
    } 
  	//标签上线 
    function onLineClassesLabel(labelId){
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt": "确定将该标签上线？",
			 "type":"confirm",
			 "width":500,
			 "height":200,
			 "param":labelId
		},"onLine") ;
    }
 	//标签上线
    function onLine(labelId){
    	showOverlay();
    	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!onLineClassesLabel.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId,
			success: function(result){
				hideOverlay();
				if(result.success){
					commonUtil.create_alert_dialog("successDialog", {
						 "txt": result.msg,
						 "type":"success",
						 "width":500,
						 "height":200
					});
					searchAfterDelete();//刷新列表
					parent.showLabelMarketCategory();//刷新标签分类地图
				}else{
					commonUtil.create_alert_dialog("failedDialog", {
						 "txt": "上线失败:" +result.msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					});
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
    	var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!queryClasses.ai2do?' + $("#pageForm").serialize();
    	$("#labelClassList").page( {url:actionUrl,param:$("#queryClassesForm").serialize(),objId:'labelClassList',callback: pagetable });
    	//每刷新一次查询结果，都需要判断是否显示批量删除按钮
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
    function openAddClassifyDialog(){
    	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/addLabelClassify.jsp";
    	var dlgObj = dialogUtil.create_dialog("label_classify_div", {
			"title" :  "增加",
			"height": "auto",
			"width" : 650,
			"frameSrc" : ifmUrl,
			"frameHeight":315,
			"position" : ['center','center'] 
			
		});
    }
    function openImportClassifyDialog(){
    	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/importLabelClasses.jsp";
    	var dlgObj = dialogUtil.create_dialog("label_classes_import", {
			"title" 	:  "导入标签分类",
			"height"	: "auto",
			"width" 	: 650,
			"frameSrc" 	: ifmUrl,
			"frameHeight":155,
			"position" 	: ['center','center'] 
		}, "searchAfterDelete");
    }
    function editLabelClassify(labelId){
    	var ifmUrl = "${ctx}/aibi_ci/ciLabelInfo/addLabelClassify.jsp?labelId="+labelId;
		var dlgObj = dialogUtil.create_dialog("label_classify_div", {
			"title" :  "修改",
			"height": "auto",
			"width" : 650,
			"frameSrc" : ifmUrl,
			"frameHeight":315,
			"position" : ['center','center'] 
		});
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
		if($("#checkBoxAll").prop("checked") == true){
			checkUnDelete();
		}
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
	function checkUnDelete(){
		$.fn.weakTip({"tipTxt":"未选中标签含有已生效或者已发布未生效或者冷冻期的子标签！"});
	}
</script>
</head>
<body>
<div class="aibi_search" style="height:35px">
	<form id="queryClassesForm" action="" method="post">
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
	      	  	 		<a href="javascript:void(0);" value="1">未生效</a>
	      	  	 		<a href="javascript:void(0);" value="2">已生效</a>
	      	  	 		<a href="javascript:void(0);" value="5">已下线</a>
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
		<li><a href="javascript:void(0);" id="add_label_button" onclick="openAddClassifyDialog();" class="icon_button_add"><span>增加</span></a></li>
		<li><a href="javascript:void(0);" id="import_label_button" onclick="openImportClassifyDialog();" class="icon_button_import"><span>导入</span></a></li>
		<li class="noborder"><a href="#" class="icon_button_delete disable" onclick="delAll()" title="请至少选择一条未提交审批的记录！"><span>删除</span></a></li>
	</ul>
</div>
<!-- area_data start -->
<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/ciLabelInfo/ciLabelInfoClassesList.jsp" />
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
    <div class="dialog_btn_div">
        <input id="confirm_true" name="" type="button" value=" 确 定 " class="tag_btn"/>
        <input id="confirm_false" name="" type="button" value=" 取 消 " class="tag_btn"/>
    </div>
</div>
</body>
</html>