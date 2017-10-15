<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
	<script type="text/javascript">
	
	$(function(){
		//pagetable();
		$.focusblur("#simpleSearchName");
		$("#searchKpiInfoButton").bind("click", function(){
			serchKpiInfoList();
		});
		$("#saveKpiInfoButton").bind("click", function(){
			setKpiInfo();
		});
		$("#cacelButton").bind("click", function(){
			parent.closeAddLabelDialog();
		});
		$("#labelListBox").mCustomScrollbar({
			theme:"minimal-dark", //滚动条样式
			scrollbarPosition:"inside", //滚动条位置
			scrollInertia:500,
			axis:"y",
			mouseWheel:{
				preventDefault:true, //阻止冒泡事件 
			}
		});
		$(".clientSelectItemClose").live("click",function(){
			$(this).parent().parent().remove();
			var checkedboxId = $(this).parent().attr("checkedboxId");
			$("#" + checkedboxId).attr("checked",false);
			//if($("#labelListBox li").length==0){
				//$("#labelListBox").css("height","auto");
			//}
		});
		serchKpiInfoList();
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

	function pagetable(){
		var simpleSearchName=$("#simpleSearchName").val();
		var indexName = "";
		if(simpleSearchName != "模糊查询"){
			indexName = simpleSearchName;
		}
		$("#indexName").val(indexName);
		var para="pojo.indexName="+indexName;
		var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!queryIndexCodeList.ai2do?'+$("#pageForm").serialize();
		$("#kpiList").page({url:actionUrl,param:para,objId:'kpiList',callback:pagetable});
		$("#labelListBox li div").each(function(index ,item){
			var checkedboxId = $(item).attr("checkedboxId");
			$("#kpiList").find("input[id='"+checkedboxId+"']").attr("checked",true);
		});
	}
	
	//查询指标信息列表	
	function serchKpiInfoList(){
		var simpleSearchName=$("#simpleSearchName").val();
		var indexName = "";
		if(simpleSearchName != "模糊查询"){
			indexName = simpleSearchName;
		}
		$("#indexName").val(indexName);
		var actionUrl = '${ctx}/ci/ciLabelInfoConfigAction!queryIndexCodeList.ai2do?' +$("#pageForm").serialize();
		var para={
				"pojo.indexName": indexName
			};
		$.ajax({
			url:actionUrl,
			type:"POST",
			dataType: "html",
			async	: false,
			data: para,
			success:function(html){
				$("#kpiList").html("");
	            $("#kpiList").html(html);
	            pagetable();
			}
		});
	}

	//做全选checkBox操作时调用的js方法
	function checkBoxAll(obj){
		$(".checkBox").each(function(){
			$(this).prop("checked", obj.checked);
			setLabelAttrListText($(this))
		});
	}
	
	function setLabelAttrListText(ths){
		//选择客户群列表请选项
		var checked = $(ths).attr("checked");
		var selfId = $(ths).attr("id");
		var labelTxt = $(ths).attr("value");
	
		if(checked){//勾选复选框
			//$("#labelListBox").show().css("height","80px");
			$("#labelListBox").show();
			var $li = '<li><div checkedboxId =' + selfId + ' value =' +  labelTxt +
				'><p class=\"fleft\" title=\"'+labelTxt+'\">' + labelTxt + 
				'</p><p class="clientSelectItemClose fleft"></p></div></li>'
			$("#labelListBox ul").append($li);
		}else{
			$("#labelListBox div").each(function(index ,item){
				var checkedboxId = $(item).attr("checkedboxId");
				if(selfId == checkedboxId){
					$(item).parent("li").remove();
				}
			});
			//if($("#labelListBox li").length == 0){
				//$("#labelListBox").css("height","auto");
			//}
		}
	}
	function setKpiInfo(){
		var indexCodeArr = [];
		$("#labelListBox li div").each(function(index ,item){
			var _value = $(item).attr("value");
			indexCodeArr.push(_value);
		});
		parent.$("#dependIndex").val(indexCodeArr.join(","));;
		parent.$("#dependIndex").attr("title",indexCodeArr.join(","));;
		parent.closeAddLabelDialog();
	}
	</script>
</head>
	<body>
	<div class="clearfix customer-title-item">
		<div class="customer-title fleft">已选择的指标编码：</div>
	</div>
	<div class="clientSelectListBox" id="labelListBox" style="height:80px;">
		<ul class="sysTagList clearfix">
		</ul>
	</div>
	<div class="aibi_search">
	<ul>
		<li>
			<input type="text" id="simpleSearchName" class="search_input fleft" value="模糊查询"/>
	        <!--模糊查询 关键字匹配  -->
			<input type="text" id="indexName" name="pojo.indexName" style="display:none;"></input>
			<input type="text" id="indexcodes" name="pojo.indexcodes" style="display:none;"></input>
		</li>
		<li>
			<a href="javascript:void(0);" id="searchKpiInfoButton" class="queryBtn-bak">查询</a>
		</li>
	</ul>
	<div class="clear"></div>
	</div>
	<div>
		<jsp:include page="/aibi_ci/ciLabelInfo/kpiSearchList.jsp"></jsp:include>
	</div>
	<div class="customer-bottom-content customer-buttonBox">
		<div class="btnActiveBox">
        	<input type="button" value="取消"  id="cacelButton" class="cancelBtn fright"/>   
			<input type="button" value="确定"  id="saveKpiInfoButton" class="ensureBtn fright"/>
		</div>
	</div>
	</body>
</html>