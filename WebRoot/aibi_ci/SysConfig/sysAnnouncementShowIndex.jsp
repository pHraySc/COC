<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/themes/zhejiang/dialog_overload.css" rel="stylesheet"  />
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript">
        $(document).ready(function () {
        	//关闭确认窗口，并处理关闭确认之后的业务逻辑 1:删除操作  2:标记已读操作
        	var optionADelOrUpdate = '';//设置closeConfirmDialog回调
            search($("#noticeIdInput").val());
            $("#confirmDialog").dialog({
    			autoOpen: false,
    			width: 500,
    			height:185,
    			title: "系统提示",
    			modal: true,
    			resizable:false
    		});
        });

        function pagetable() {
        	$(window).scrollTop(0);
            var actionUrl = '${ctx}/ci/ciSysAnnouncementAction!showQuery.ai2do?' + $("#pageForm").serialize()+"&sysAnnouncement.readStatus="+$("#readStatus").val();
            $("#sysAnnouncementTable").page({url: actionUrl, objId: 'sysAnnouncementTable', callback: pagetable });
        }

        function search(noticeId) {
        	$("#noticeIdInput").val("");
        	$("#readStatus").val("");
        	if(noticeId==undefined)
                noticeId = "";
            var actionUrl = '${ctx}/ci/ciSysAnnouncementAction!showQuery.ai2do?' + $("#pageForm").serialize()+'&noticeId='+noticeId;
            $.ajax({
                url:actionUrl,
                type:"POST",
                dataType: "html",
                success:function(html){
                    $("#sysAnnouncementTable").html("");
                    $("#sysAnnouncementTable").html(html);
                    pagetable();
                    $("input[name='sysAnnouncementCheckBox']").each(function(){
                        if($.trim($(this).val()) == noticeId){
                        	 $(this).parent().addClass("checkBoxOn");
                        	 $(this).attr("checked", true);
                            }
            		});
                    var _sysAnnouncementTitle=$.trim($("#_sysAnnouncementTitle").html());
                    if(_sysAnnouncementTitle=="" || _sysAnnouncementTitle == "未读的系统公告"){
                    	$("#_sysAnnouncementTitle").html("系统公告");
        			}
                    var notReadCount = $("#sysNotReadCount").val();
        			var totalCount = $("#sysTotal").val();
                    $("#notReadCount").html(notReadCount);
                    $("#totalCount").html(totalCount);
                    
                	$("#sys_notReadSpan").hide();
                    $("#sys_notRead").show();
                	$("#sys_allRead").hide();
                	$("#sys_allReadSpan").show();
                }
            });
        }
        //获取未读列表
        function notReadSearch(){
    		var noticeId = $("#noticeIdInput").val();
        	if(noticeId==undefined)
                noticeId = "";
            var readStatus = 0;
            $("#readStatus").val(readStatus);
            var para={
                    "sysAnnouncement.readStatus":readStatus
                    };
            var actionUrl = '${ctx}/ci/ciSysAnnouncementAction!showQuery.ai2do?' + $("#pageForm").serialize()+'&noticeId='+noticeId;
            $.ajax({
                url:actionUrl,
                type:"POST",
                dataType: "html",
                data:para,
                success:function(html){
                    $("#sysAnnouncementTable").html("");
                    $("#sysAnnouncementTable").html(html);
                    pagetable();
                    var _sysAnnouncementTitle=$.trim($("#_sysAnnouncementTitle").html());
                    if(_sysAnnouncementTitle=="" || _sysAnnouncementTitle == "系统公告"){
                    	$("#_sysAnnouncementTitle").html("未读的系统公告");
        			}
        			var notReadCount = $("#sysNotReadCount").val();
        			var totalCount = $("#sysTotal").val();
                    $("#notReadCount").html(notReadCount);
                    $("#totalCount").html(totalCount);
                    
                    $("#sys_notRead").hide();
                	$("#sys_notReadSpan").show();
                	$("#sys_allReadSpan").hide();
                	$("#sys_allRead").show();
                }
            });
        }
        //点击“全部”
        function searchR(){
			search("");
        }
        //标记系统公告为已读状态
        function readedSysAnnouncement(obj) {
                if($(obj).attr("readStatus")!="1"){
                	var actionUrl = 'ciSysAnnouncementAction!updateReadStatus.ai2do?sysAnnouncement.announcementId=' + $(obj).attr("announcementId");
                    $.ajax({
                        url:actionUrl,
                        type:"POST",
                        success:function(result){
                    	    flag = result.success;
                    	    if(flag){
                    	    	$(obj).parent().parent().removeClass("checkBoxChecked");
                    	    	$("#notReadCount").html(result.readStatusNot);
                    	    	var readStatus = $("#readStatus").val();
	           					if(readStatus == '0'){
	           						notReadSearch();
	           					}else{
	       	    					search($("#noticeIdInput").val());
	           					}
                        	}
                        }
                    })
                }
        }
        function updateReadStatusYesAll(){
        	var announcementIds = "";
        	optionADelOrUpdate = '2';
    		$("input[name='sysAnnouncementCheckBox']").each(function(){
    			var checked = this.checked;
    			if(checked){
    				announcementIds += (announcementIds == "" ? "" :',') + $(this).val();
    			}
    		});
    		if(announcementIds != ""){
    			openConfirmUpdateReadStatus("确认将所选公告标记为已读？",announcementIds);
    		}
        }
    	//打开确认弹出层,由于confirmDialog中调用了父页面的closeConfirmDialog和“#confirmDialog”
    	//所以在父页面中必须要有这两个内容
    	function openConfirmUpdateReadStatus(confirmMsg, id){
    		confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
    		var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
    		$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
    	}
    	//标记未读标签为已读标签的具体业务逻辑，支持批量标记已读，其中 $(".num_on").click 实现了页码保持：在哪个页码里做的标记已读，标记已读后还是在哪页
    	function updateReadStatus(announcementId){
    		var actionUrl = $.ctx + "/ci/ciSysAnnouncementAction!updateAllReadStatus.ai2do";
    		$.ajax({
    			type: "POST",
    			url: actionUrl,
    			data: "pojo.announcementIds="+announcementId,
    			success: function(result){
    				if(result.success){
   					 //弱提示
                    	$.fn.weakTip({"tipTxt":result.msg});
    					$("input[name='pager.totalSize']").val(0);
    					//先刷新页面，然后点击到对应的页码
    					 var num_on=$(".num_on");
    					 var pageNum=num_on.text();
    					 var readStatus = $("#readStatus").val();
    					 if(readStatus == '0'){
    						 notReadSearch();
    					 }else{
	    					 search($("#noticeIdInput").val());
    					 }
    					 $(".num").each(function () {
    				            if ($(this).text() == pageNum) {
        				            $(this).addClass("num_on");
    				                $(this).click();
    				            }
    				        });
    					 $("#_sysAnnouncementTitle").attr("_checked",false);
    				}else{
    					showAlert("标记已读失败：" +result.msg,"failed");
    				}
    				
    			}
    		});	
    	}
        //点击批量删除时所调用的js方法用来删除选中的标签
    	function delAll(){
    		optionADelOrUpdate = '1';
    		var announcementIds = "";
    		$("input[name='sysAnnouncementCheckBox']").each(function(){
    			var checked = this.checked;
    			if(checked){
    				announcementIds += (announcementIds == "" ? "" :',') + $(this).val();
    			}
    		});
    		if(announcementIds != ""){
    			openConfirm("确认删除所选公告？",announcementIds);
    		}
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
    			if(optionADelOrUpdate == '1'){
	    			del(id);
    			}else if(optionADelOrUpdate == '2'){
    				updateReadStatus(id);
    			}
    		}
    	}
    	//删除标签的具体业务逻辑，支持批量删除，其中 $(".num_on").click 实现了页码保持：在哪个页码里做的删除，删除后还是在哪页
    	function del(announcementId){
    		var actionUrl = $.ctx + "/ci/ciSysAnnouncementAction!del.ai2do";
    		$.ajax({
    			type: "POST",
    			url: actionUrl,
    			data: "pojo.announcementIds="+announcementId,
    			success: function(result){
    				if(result.success){
    					 //弱提示
                    	$.fn.weakTip({"tipTxt":result.msg});
    					$("input[name='pager.totalSize']").val(0);
    					//先刷新页面，然后点击到对应的页码
    					 var num_on=$(".num_on");
    					 var pageNum=num_on.text();
    					 var readStatus = $("#readStatus").val();
    					 if(readStatus == '1'){
    						 notReadSearch();
    					 }else{
	    					 search($("#noticeIdInput").val());
    					 }
    					 $(".num").each(function () {
    				            if ($(this).text() == pageNum) {
        				            $(this).addClass("num_on");
    				                $(this).click();
    				            }
    				        });
    					 $("#_sysAnnouncementTitle").attr("_checked",false);
    				}else{
    					showAlert("删除失败：" +result.msg,"failed");
    				}
    			}
    		});	
    	}

    	//做全选checkBox操作时调用的js方法
    	function checkBoxAll(obj){
    		var _checked = $(obj).attr("_checked"); 
    		var _checkedFlag = true;
    		if(_checked == "false"){
    			$(obj).attr("_checked",true);
    			_checkedFlag = true;
        	}else{
        		$(obj).attr("_checked",false);
        		_checkedFlag = false;
            }
    		$("input[name='sysAnnouncementCheckBox']").each(function(){
    			if($(this).prop("disabled") != true){
    				if(_checkedFlag){
						if($(this).parent().hasClass("checkBoxOn")){
        				}else{	
        					$(this).parent().addClass("checkBoxOn");
            			}
        			}else{
						if($(this).parent().hasClass("checkBoxOn")){
							$(this).parent().removeClass("checkBoxOn");
						}
            		}
					$(this).attr("checked", _checkedFlag);
    			}
    		  });
    	}
    	function ucheckAll(){
    		$("#_sysAnnouncementTitle").attr("_checked",false);
        }
</script>
<input id="noticeIdInput" type="hidden" name="pojo.noticeId" value="${pojo.noticeId }"/>
<div class="" id="sysAnnouncementTable2">
	<div class="newMsgContentWrap">
		<div class="clearfix title-box-tip">
			<a class="fleft big-title" onclick="checkBoxAll(this);" _checked="false" id="_sysAnnouncementTitle" title="点击勾选所有系统公告！">系统公告</a>
			<p class="fleft">(<a href="javascript:void(0);" id="sys_allRead" onclick="searchR();" class="notReadSpan">全部</a><span id="sys_allReadSpan" style="display:none;" >全部</span>&nbsp;<span id="totalCount"></span>条 <span>其中 </span><a href="javascript:void(0);" id="sys_notRead" onclick="notReadSearch();" class="notReadSpan">未读公告 </a><span id="sys_notReadSpan" style="display:none;" >未读消息</span><span id="notReadCount"></span> 条)
			<input type="button" value="删除" onclick="delAll()" title="请至少选择一条记录！" class="msgDelBtn disable" />
			<input type="button" value="标记已读11" onclick="updateReadStatusYesAll()" title="请至少选择一条记录！" class="msgDelBtn disable" />
			</p>
		</div>
	  	<div id="sysAnnouncementTable" style="margin-top:20px;">
	  	</div>
	</div>
</div>
<div id="confirmDialog">
	<iframe id="confirmFrame" name="confirmFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;"></iframe>
</div>
<input type="hidden" name="sysAnnouncement.readStatus" id="readStatus" value="${sysAnnouncement.readStatus }"></input>