<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#labelTempName").focusblur({ 
		"focusColor":"#222232" ,
	    "blurColor":"#757575"  
	});
	$("#searchEnumCategoryButton").bind("click", function () {
		search();
	});
	search();
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
function search(){
	//关键字查询  标签名称、业务口径、应用建议
	var serachStr = "";
    if ("模糊查询" == $.trim($("#labelTempName").val())) {
        serachStr = "";
    } else {
        serachStr = $.trim($("#labelTempName").val());
    }
    $("#labelName").val(serachStr);
    
    var actionUrl = '${ctx}/ci/ciLabelInfoAction!findVertAndEnumLabelList.ai2do?' + $("#pageForm").serialize();
    $.ajax({
        url:actionUrl,
        type:"POST",
        dataType: "html",
        data: $("#queryEnumInfoForm").serialize(),
        success:function(html){
            $("#enumCategoryList").html("");
            $("#enumCategoryList").html(html);
            var totalSize = $("#enumCategoryTotalSize").val();
         	if(totalSize){
         	    console.info($("#totalSizeNum").html());
                console.info(totalSize);
          		$("#totalSizeNum").html(totalSize);
                console.info($("#totalSizeNum").html());
			}
            pagetable();
        }
    });
}

function pagetable(){
	/*
	$("#pageForm")中有totalSize 和  totalPage这两个属性，在点击换页时，这两个是不变的
	因此可以在url中带过去，而在page方法中再去拼接：pageNum pageFlag pageSizeSel。
	因为这三个值在换页时显然是动态的，不如直接跳转到某页、点击下一页或上一页、设置一页的大小
	*/
	var actionUrl = '${ctx}/ci/ciLabelInfoAction!findVertAndEnumLabelList.ai2do?' + $("#pageForm").serialize();
	$("#enumCategoryList").page( {url:actionUrl,param:$("#queryEnumInfoForm").serialize(),objId:'enumCategoryList',callback: pagetable });
}

function setCategoryInfo(labelId){
   	//关键字查询  标签名称、业务口径、应用建议
	var param = "labelId="+labelId;
	var actionUrl = '${ctx}/ci/ciLabelInfoAction!findEnumCategoryInfoIndex.ai2do?' + param;
	$.ajax({
	    url		: actionUrl,
	    type	: "POST",
	    dataType: "html",
	    success	: function(html){
	    	$(window).scrollTop(0);
			$("#vertAndEnumLabelInfoDiv").html(html);
			$("#totalSizeNum").parent().hide();
	    }
	});
}
</script>
<div id="vertAndEnumLabelInfoDiv">
<div class="aibi_search">
	<form id="queryEnumInfoForm" action="" method="post">
	<ul class="clearfix">
		<li>
			<input type="text"  id="labelTempName" class="search_input fleft"   value="模糊查询"/>
			<input type="text" id="labelName" name="searchConditionInfo.labelName" style="display:none;" >
		</li>
		<li>
		<a href="javascript:void(0);" id="searchEnumCategoryButton" class="queryBtn-bak">查询</a>
		</li>
	</ul>
	<div class="clear"></div>
	</form>
</div>
<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/ciLabelInfo/vertAndEnumLabelList.jsp" />
</div>
<div class="clear"></div>
</div>
