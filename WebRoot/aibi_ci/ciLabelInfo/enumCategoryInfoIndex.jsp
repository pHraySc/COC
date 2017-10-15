<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(function(){
	$(".secMenu").live("click",function(){
		$(".secMenu.current").removeClass("current");
		$(this).addClass("current");
		searchCategoryInfoList($(this).attr("value"));
	});

	$("#saveEnumCateoryDialog").dialog({
		width:700,
		dialogClass:"ui-dialogFixed",
		autoOpen: false, 
		modal: true, 
		title:"新建分类",
		resizable:false, 
		draggable: true
	});
	
	$("#uploadDataDialog").dialog({
		width:500,
		dialogClass:"ui-dialogFixed",
		autoOpen: false, 
		modal: true, 
		title:"上传分类数据",
		resizable:false, 
		draggable: true
	});
	
	$("#confirmDialog").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 500,
		height:185,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	searchCategoryInfoList($(".secMenu.current").attr("value"));
});

//打开保存弹出框
function openEnumCateoryDialog(){
	var columnId = $(".menuBox .current").attr("value");
	$("#saveEnumCateoryDialog").dialog({
		close: function( event, ui ) {
			$("#saveEnumCateoryFrame").removeAttr("src");
		}
	});
	var para = "?columnId="+columnId;
	var ifmUrl ="${ctx}/aibi_ci/ciLabelInfo/createEnumCateoryDialog.jsp"+para;
	$("#saveEnumCateoryFrame").attr("src", ifmUrl).load(function(){});
	$("#saveEnumCateoryDialog").dialog("open");
}

//打开上传弹出框
function openUploadDataDialog(enumCategoryId){
	var columnId = $(".menuBox .current").attr("value");
	$("#uploadDataDialog").dialog({
		close: function( event, ui ) {
			$("#uploadDataFrame").removeAttr("src");
		}
	});
	var para = "?columnId="+columnId + "&enumCategoryId=" + enumCategoryId;
	var ifmUrl ="${ctx}/aibi_ci/ciLabelInfo/uploadDataDialog.jsp"+para;
	$("#uploadDataFrame").attr("src", ifmUrl).load(function(){});
	$("#uploadDataDialog").dialog("open");
}

//关闭弹出框
function iframeCloseDialog(obj){
	$(obj).dialog("close");
}

function delAll(){
	var enumCategoryIds = "";
	$("input[name='enumCategoryCheckBox']").each(function(){
		var checked = this.checked;
		if(checked){
			enumCategoryIds += (enumCategoryIds == "" ? "" :',') + $(this).val();
		}
	});
	if(enumCategoryIds != ""){
		showAlert("确认删除所选分类？","confirm",deleteEnumCateoryInfo,enumCategoryIds);
	}
}

function delOne(enumCategoryId){
	if(enumCategoryId != ""){
		showAlert("确认删除所选分类？","confirm",deleteEnumCateoryInfo,enumCategoryId);
	}
}

//删除分类
function deleteEnumCateoryInfo(enumCategoryId){
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!delCategoryInfo.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "enumCategoryIds="+enumCategoryId,
		success: function(result){
			if(result.success){
				 //弱提示
               	$.fn.weakTip({"tipTxt":result.msg});
               	searchCategoryInfoList($(".secMenu.current").attr("value"));
			}else{
				showAlert("删除失败：" +result.msg,"failed");
			}
		}
	});
}

//查询分类列表
function searchCategoryInfoList(columnId) {
	var para = "columnId="+columnId;
	var actionUrl = '${ctx}/ci/ciLabelInfoAction!findEnumCategoryInfoList.ai2do?' + $("#pageForm").serialize();
	$.ajax({
	    url:actionUrl,
	    type:"POST",
	    dataType: "html",
	    data: para,
	    success:function(html){
	        $("#categoryInfoListTable").html("");
	        $("#categoryInfoListTable").html(html);
	        pagetable();
	    }
	});
}

function pagetable(){
	var columnId = $(".menuBox .current").attr("value");
	var para = "columnId="+columnId;
   	var actionUrl = '${ctx}/ci/ciLabelInfoAction!findEnumCategoryInfoList.ai2do?' + $("#pageForm").serialize();
   	$("#categoryInfoListTable").page( {url:actionUrl,param:para,objId:'categoryInfoListTable',callback: pagetable });
}

//做全选
function checkBoxAll(obj){
	var checkedVal = $(obj).attr("checked"); 
	$("input[name='enumCategoryCheckBox']").each(function(){
		if($(this).prop("disabled") != true){
			if(checkedVal == "checked" || checkedVal == true){
				$(this).attr("checked", true);
			} else {
				$(this).attr("checked", false);
			}
		}
	});
}

function downloadData(){
	var columnId = $(".menuBox .current").attr("value");
	window.location.href = '${ctx}/ci/ciLabelInfoAction!findAllEnumData.ai2do?columnId=' + columnId;
}
function openEnumCategoryInfoViewDialog(enumCategoryId,enumCategoryName){
	var columnId = $(".menuBox .current").attr("value");
	var labelId = $("#labelId").val();

	var labelTypeId = $("#labelTypeId").val();
	$("#enumCategoryInfoViewDialog").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 600,
		title: enumCategoryName+"分类维值表",
		modal: true,
		resizable:false,
		close: function( event, ui ) {
			$("#enumCategoryInfoViewFrame").attr("src", "");
		}
	});
	var para = "?columnId="+columnId+"&labelTypeId="+labelTypeId+"&enumCategoryId="+enumCategoryId+"&labelId="+labelId;
	var ifmUrl ="${ctx}/aibi_ci/ciLabelInfo/enumValueViewDialog.jsp"+para;
	$("#enumCategoryInfoViewFrame").attr("src", ifmUrl).load(function(){});
	$("#enumCategoryInfoViewDialog").dialog("open");
}
</script>
<div class="rightContainer fleft">
<div class="rightInner">
<div class="topContainer">
	<div class="fright back"><a href="javascript:void(0);" onclick="ciEnumCategoryInfo();">返回</a></div>
	<div class="fright leftTitle">
		<div class="titleInner">
			<div class="title">${ciLabelInfo.labelName }</div>
			<div class="introduction" title="<c:choose><c:when test="${ciLabelInfo.busiCaliber == null}">暂无内容</c:when><c:otherwise>${ciLabelInfo.busiCaliber}</c:otherwise></c:choose>">
			<c:choose> 
			    <c:when test="${ciLabelInfo.busiCaliber == null}">
			   	   	暂无内容
			    </c:when>   
				<c:otherwise> 
					${ciLabelInfo.busiCaliber}
				</c:otherwise>
			</c:choose>
			</div>
		</div>
	</div>
</div>
<div class="dateArraySwitchBox rightMenu">
<ol>
	<c:forEach items="${ciLabelInfoList}" var="columns" varStatus="st">
		<c:choose>
		<c:when test="${st.index == 0}">
		<li class="menuBox">
			<div class="current secMenu" id="${columns.columnId}" value="${columns.columnId }" title="${columns.columnCnName }" >${columns.columnCnName }</div>
		</li>
		</c:when>
		<c:otherwise>
		<li class="menuBox">
			<div class="secMenu" id="${columns.columnId }" value="${columns.columnId }" title="${columns.columnCnName }">${columns.columnCnName }</div>
		</li>
		</c:otherwise>
		</c:choose>
	</c:forEach>
</ol>
<div class="iconBox">
<div class="createIcon fleft" title="创建" onclick="openEnumCateoryDialog();"></div>
<div class="delIcon fleft" title="删除" onclick="delAll();"></div>
<div class="leftLine fleft"></div>
<div class="downIcon fleft" title="标签维值数据字典下载" onclick="downloadData();"></div>
</div>
</div>
<div >
	<jsp:include page="/aibi_ci/ciLabelInfo/enumCategoryInfoList.jsp" />
</div>
<div id="saveEnumCateoryDialog" style="overflow:hidden; display:none;">
  <iframe id="saveEnumCateoryFrame"  name="saveEnumCateoryFrame"  src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:318px;" scrolling="no"  ></iframe>
</div>
<div id="uploadDataDialog" style="overflow:hidden; display:none;">
	<iframe id="uploadDataFrame"  name="uploadDataFrame"  src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:120px;" scrolling="no"  ></iframe>
</div>
<div id="enumCategoryInfoViewDialog" style="overflow:hidden; display:none;">
	<iframe id="enumCategoryInfoViewFrame"  name="enumCategoryInfoViewFrame"  src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:520px;" scrolling="no"  ></iframe>
</div>
<input type="hidden" name="labelTypeId" id="labelTypeId" value="${ciLabelInfo.labelTypeId }">
<input type="hidden" name="labelId" id="labelId" value="${ciLabelInfo.labelId }">
</div>
</div>
