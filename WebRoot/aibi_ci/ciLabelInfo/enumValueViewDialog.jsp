<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script  type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	var columnId = '${param["columnId"]}';
	var labelTypeId = '${param["labelTypeId"]}';
	var enumCategoryId = '${param["enumCategoryId"]}';
	var labelId = '${param["labelId"]}';
	
	$("#columnId").val(columnId);
	$("#labelTypeId").val(labelTypeId);
	$("#enumCategoryId").val(enumCategoryId);
	$("#labelId").val(labelId);
	
	$.focusblur("#dimNameTemp");

	$("#enumCategoryInfoButton").bind("click", function () {
		enumCategoryInfoListView();
	});
	
	enumCategoryInfoListView();
	
});
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
function enumCategoryInfoListView(){
	var columnId = $("#columnId").val();
	var labelTypeId = $("#labelTypeId").val();
	var enumCategoryId = $("#enumCategoryId").val();
	var labelId = $("#labelId").val();
	
	var serachStr = "";
    if ("模糊查询" == $.trim($("#dimNameTemp").val())) {
        serachStr = "";
    } else {
        serachStr = $.trim($("#dimNameTemp").val());
    }
    $("#dimName").val(serachStr);

    var dimName = $("#dimName").val();
    
	var url = $.ctx + "/ci/ciLabelInfoAction!findAllLabelDimValueCategory.ai2do?" + $("#pageForm").serialize();
	var para = "columnId="+columnId+"&labelTypeId="+labelTypeId+"&enumCategoryId="+enumCategoryId+"&dimName="+dimName+"&labelId="+labelId;
	$.ajax({
	    url:url,
	    type:"POST",
	    dataType: "html",
	    data: para,
	    success:function(html){
	        $("#enumValueListTable").html("");
	        $("#enumValueListTable").html(html);
	        pagetable2();
	    }
	});
}
function pagetable2(){
	var columnId = $("#columnId").val();
	var labelTypeId = $("#labelTypeId").val();
	var enumCategoryId = $("#enumCategoryId").val();
	var labelId = $("#labelId").val();
	
	var serachStr = "";
    if ("模糊查询" == $.trim($("#dimNameTemp").val())) {
        serachStr = "";
    } else {
        serachStr = $.trim($("#dimNameTemp").val());
    }
    $("#dimName").val(serachStr);

    var dimName = $("#dimName").val();
    
	var para = "columnId="+columnId+"&labelTypeId="+labelTypeId+"&enumCategoryId="+enumCategoryId+"&dimName="+dimName+"&labelId="+labelId;
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findAllLabelDimValueCategory.ai2do?" + $("#pageForm").serialize();
   	$("#enumValueListTable").page( {url:actionUrl,param:para,objId:'enumValueListTable',callback: pagetable2 });
}
</script>	
</head>
<body>
<div class="aibi_search">
	<form id="queryEnumCategoryInfoForm" action="" method="post">
	<ul class="clearfix">
		<li>
			<input type="text"  id="dimNameTemp" class="search_input fleft"   value="模糊查询"/>
			<input type="text" id="dimName" name="dimName" style="display:none;"></input>
		</li>
		<li>
		<a href="javascript:void(0);" id="enumCategoryInfoButton" class="queryBtn-bak">查询</a>
		</li>
	</ul>
	<div class="clear"></div>
	</form>
</div>
<div>
	<jsp:include page="/aibi_ci/ciLabelInfo/enumValueListView.jsp" />
</div>
<input type="hidden" name="columnId" id="columnId"  />
<input type="hidden" name="labelTypeId" id="labelTypeId"  />
<input type="hidden" name="enumCategoryId" id="enumCategoryId"  />
<input type="hidden" name="labelId" id="labelId"  />
</body>
</html>
