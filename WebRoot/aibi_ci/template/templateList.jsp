<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_SCENE" value="<%=CommonConstants.TABLE_DIM_SCENE%>"></c:set>
<script type="text/javascript">
$(document).ready(function(){
	var isLatestTemplateFlag='${param["isLatestTemplateFlag"]}';
	var isSysRecommendTemplateFlag='${param["isSysRecommendTemplateFlag"]}';
	if(isLatestTemplateFlag == 'true'){
		$(".pathNavHome").html("最新发布模板");
	}
	if(isSysRecommendTemplateFlag == 'true'){
		$(".pathNavHome").html("系统热门模板");
	}
	$("#isLatestTemplateFlag").val(isLatestTemplateFlag);
	$("#isSysRecommendTemplateFlag").val(isSysRecommendTemplateFlag);
	 //查询条件筛选条件删除
	  $(".selectedItemClose").click(function(){
	     var selectedDivId = $(this).parent().children().first(); 
	     $(this).parent().remove();
	     multiSearch(selectedDivId);
	  });
	    //输入框获得焦点显示按钮，失去焦点隐藏按钮
  	$("#startDate").bind({
	        focus: function() {
	        	$("#dateSelf").show();
	        },
	        blur: function() {
	        	$("#dateSelf").css({display:"none"}); 
	        }
		});
  	$("#endDate").bind({
	        focus: function() {
	        	$("#dateSelf").show();
	        },
	        blur: function() {
	        	$("#dateSelf").css({display:"none"}); 
	        }
		});

  	$(".dateSelfDiv").bind({
	        focus: function() {
	        	$("#dateSelf").show();
	        },
	        blur: function() {
	        	$("#dateSelf").css({display:"none"}); 
	        }
		});
  	$("#timeOrder").bind("click", function(){
  		sortByName('NEW_MODIFY_TIME',this);
	});
  	$("#useNumOrder").bind("click", function(){
		sortByName('USECOUNT',this);
	});
  	$("#nameOrder").bind("click", function(){
		sortByName('TEMPLATE_NAME',this);
	});
	  //添加你选项的查询条件
	  $(".selectOptionValue a").click(function(){
		  var selectedA = $(this);
		  var selectedDivId = $(this).parents("div");
		  var selectedLiId = $(this).parent().attr("id");
		  var selectedLiIdStr = "";
		  if(selectedLiId != null && selectedLiId != ""){
			  selectedLiId = $.trim(selectedLiId).split("_");
			  selectedLiIdStr = selectedLiId[1];
		  }
		  var idStr = selectedDivId.attr("id")+"_"+selectedLiIdStr;
		  var value = $(this).html();
		  var flag = true;
	      var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		  var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\""+idStr+"\" >"+value+"</div>");
		  var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	      $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
        //同一种条件是互斥添加
	      huChi($selectedItemBox,idStr,$(this).attr("class"),flag,value);
		
		  //绑定单击事件
		  $selectedItemClose.click(function(){
		     $(this).parent().remove();
		     multiTemplateSearch();
		  })
		  multiTemplateSearch();
	  });
	
	  //更多查询条件
	  $("#moreOptionActive").click(function(){
	      if($("#moreSelectOptionList").is(":visible")){
		     $("#moreSelectOptionList").slideUp(500);
		  }else{
		     $("#moreSelectOptionList").slideDown(500);
		  }
	  });
	$("#successSaveDialog").dialog({
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		resizable:false
	});
	
	$("#confirmDialog").dialog({
		autoOpen: false,
		width: 500,
		height:185,
		title: "系统提示",
		modal: true,
		resizable:false
	});

	$("#shareDialog").dialog({
		autoOpen: false,
		width: 660,
		title: "模板共享",
		modal: true,
		resizable:false
	});

	$("#successShareDialog").dialog({
		autoOpen: false,
		width: 450,
		title: "系统提示",
		modal: true,
		resizable:false
	});
});
function loadCustomsList(){
	$("#noMoreTemplateResults").hide();
	$("#noMoreResults").hide();
	$("#noResults").hide();
	$("#noResultsTemplate").hide();
	$("#masterPlateResultTBox").empty();
	var keyword = $.trim($("#keyword").val());
	if(keyword == "请输入关键字"){
		keyword="";
	}
	$("#templateName").val(keyword);
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/templateManageAction!findTemplateTotalNum.ai2do";
	$.post(getTotalUrl, $("#multiSearchForm").serialize(), function(result) {
		if (result.success) {
			$(".pathNavNext").html(keyword);
			$(".amountNum").html(result.totalSize);
			var formArray = $("#multiSearchForm").serializeArray();
			var data = convertArray(formArray);
			var totalPage = result.totalPage;
			var actionUrl = $.ctx + "/ci/templateManageAction!findTemplateList.ai2do?pager.totalPage=" + totalPage;
			var i = totalPage;
			if(i != 0){
				$("#queryResultListBox").scrollPagination({
					"contentPage" : actionUrl, // the url you are fetching the results
					"contentData" : data, // these are the variables you can pass to the request, for example: children().size() to know which page you are
					"scrollTarget" : $(window),// who gonna scroll? in this example, the full window
					"heightOffset" : 10,
					"currentPage" : 1,
					"totalPage" : totalPage,
					"beforeLoad" : function() { // before load function, you can display a preloader div
					$("#loading").fadeIn();
					},
					"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements
						$(elementsLoaded).fadeInWithDelay();
						i--;
						if(i == 0){
							$("#loading").fadeOut("normal", function(){
								if($("#queryResultListBox").attr("scrollPagination") == "disabled"){
									$("#noMoreTemplateResults").fadeIn();
								}
							});
						}else{
							$("#loading").fadeOut("normal", function(){});
						}
					}
				});
			}else{
				$("#noResultsTemplate").show();
				$("#loading").hide();
			}
		} else {
			showAlert(result.msg,"failed");
		}
	});
}
//同一种条件是互斥添加
function huChi(selectedItemBox,selectedDivId,selectedA,flag,value){
    if($(".selectedItemDetail").size() >= 1){
          $(".selectedItemDetail").each(function(){
        	var idArr = $.trim($(this).attr("id")).split("_");
        	var selectedDivIdArr = $.trim(selectedDivId).split("_");
          	if(idArr[0] == selectedDivIdArr[0]){
          		if(selectedA == "all"){
          			$(this).parent().remove();
          		}else{
	        	  	$(this).html(value);
	        	  	$(this).attr("id",selectedDivId);
          		}
        	  	flag = false;
          		return false;
          	}else{
          		flag = true;
          	}
          });
          if(flag) $("#selectedItemListBox").append(selectedItemBox);
    }else{
  	  if(selectedA != "all"){
          	$("#selectedItemListBox").append(selectedItemBox);
  	  }
    }
}
//客户群模板单排序
function sortByName(columnName,ele){
	var sort = "";
	if((!$(ele).hasClass("desc")) && (!$(ele).hasClass("asc"))){
		sort = "desc";
		$(ele).addClass("desc");
	}else{
		if($(ele).hasClass("desc")){
			$(ele).removeClass("desc");
			sort = "asc";
			$(ele).addClass("asc");
		}else{
			$(ele).removeClass("asc");
			sort = "desc";
			$(ele).addClass("desc");
		}
	}
	$("#order").val("");
	$("#orderBy").val("");
	$("#order").val(sort);
	$("#orderBy").val(columnName);
	multiTemplateSearch();
}
//删除一个
function del(id){
	var actionUrl = $.ctx + "/ci/templateManageAction!deleteTemplate.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "ciTemplateInfo.templateId="+id,
		success: function(result){
			if(result.success){
				showAlert(result.msg,"success");
				showTemplateIndex();
			}
		}
	});
}
//要去删除一条记录
function toDel(id){
	openConfirm("确定要删除吗？", id);
}
//要共享客户群
function toShare(templateId){
	showAlert("确认共享该客户群模板?共享之后将不能恢复操作!","confirm",templateShare,templateId);
}
//删除多个
function delSome(ids){
	var actionUrl = $.ctx + "/ci/templateManageAction!deleteSomeTemplates.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "templateIds="+ids,
		success: function(result){
			if(result.success){
				var array = result.delIds;
				for(var i=0; i<array.length; i++){
					$("#"+array[i]).remove();
				}
				$("#delSomeBtn").addClass("disable");
				$("#delSomeBtn").unbind("click");
				showAlert(result.msg,"success");
			}else{
				showAlert(result.msg,"failed");
			}
		}
	});
}
//要去删除多个
function toDelSome(){
	var tid = $(".select");
	var ids = "";
	var i = 0;
	$(tid).each(function(){
		ids += $(this).attr("id") + ",";
		i ++;
	});
	if(i>0){
		ids = ids.substring(0, ids.length-1);
		openConfirm("确定要删除吗？", "");
	}else{
		showAlert("至少选中一条记录！","failed");
	}
}
//打开共享成功弹出层
function openShareSuccess(customerName,templateName){
	$("#successShareDialog").dialog({
		close: function( event, ui ) {
			showTemplateIndex();
		}
	});
	templateName=encodeURIComponent(encodeURIComponent(templateName))
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	$("#successShareDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/shareSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successShareFrame").attr("src", ifmUrl).load(function(){$("#successShareDialog").dialog("open");});
}
//打开保存成功弹出层
function openSaveSuccess(customerName, templateName){
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	templateName = encodeURIComponent(encodeURIComponent(templateName));
	$("#successSaveDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	$("#successFrame").attr("src", ifmUrl).load(function(){$("#successSaveDialog").dialog("open");});
}
//打开确认弹出层
function openConfirm(confirmMsg, id){
	confirmMsg = encodeURIComponent(encodeURIComponent(confirmMsg));
	$("#confirmDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/confirmDialog.jsp?confirmMsg="+confirmMsg+"&id="+id;
	$("#confirmFrame").attr("src", ifmUrl).load(function(){$("#confirmDialog").dialog("open");});
}
//关闭确认弹出层
function closeConfirmDialog(dialogId, isSome, id){
	$(dialogId).dialog("close");
	if(isSome){
		if(id == ""){
			var tid = $(".select");
			var ids = "";
			var i = 0;
			$(tid).each(function(){
				ids += $(this).attr("id") + ",";
				i ++;
			});
			if(i>0){
				ids = ids.substring(0, ids.length-1);
				delSome(ids);
			}else{
				return false;
			}
		}else{
			del(id);
		}
	}else{
		return false;
	}
}
//打开编辑页面
function preEdit(id){
	var url = $.ctx + "/ci/templateManageAction!preEditTemplate.ai2do?ciTemplateInfo.templateId="+id;
	//window.open(url, "_blank", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	openEditTemplist(url);
}
//修改模板弹出层
function openEditTemplist(url){
	$("#createDialog").dialog({
		title: "编辑模板",
		close: function( event, ui ) {
			$("#createFrame").attr("src", "");
		}
	});
	var ifmUrl = url;
	$("#createFrame").attr("src", ifmUrl).load(function(){});
	$("#createDialog").dialog("open");
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//模板共享弹出层
function openShare(url){
	$("#shareDialog").dialog({
		close: function( event, ui ) {
			$("#shareFrame").attr("src", "");
		}
	});
	var ifmUrl = url;
	$("#shareFrame").attr("src", ifmUrl).load(function(){});
	$("#shareDialog").dialog("open");
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//模板共享
function templateShare(id){
	var name = $("#share_"+id).attr("t_name");
	var actionUrl = $.ctx + "/ci/templateManageAction!saveShareTemplate.ai2do";
	var para={
			"ciTemplateInfo.templateId":id,
		 	"ciTemplateInfo.templateName":name
		};
	$.post(actionUrl, para, function(result){
		if(result.success){
			showTemplateIndex();
			showAlert("客户群模板共享成功！","success");
		}else{
			var cmsg = result.cmsg;
			if(cmsg.indexOf("重名") >= 0){
				msg=encodeURIComponent(encodeURIComponent(cmsg));
				name=encodeURIComponent(encodeURIComponent(name));
				var url = "${ctx}/aibi_ci/template/shareTemplateForm.jsp?id=" + id + "&name=" + name+"&msg="+msg;
				openShare(url);
			}else{
				showAlert(cmsg,"failed");
			}
		}
		});
}

//保存客户群弹出层
function openCreate(url){
	$("#createDialog").dialog({
		close: function( event, ui ) {
			$("#createFrame").attr("src", "");
		}
	});
	var ifmUrl = url;
	$("#createFrame").attr("src", ifmUrl).load(function(){});
	$("#createDialog").dialog("open");
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//根据模板创建客户群
function createCustomer(id,rule){
	rule = encodeURIComponent(encodeURIComponent(rule));
	var month;
	$.ajax({
		url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
		type: "POST",
		success: function(result){
			var y = result.substring(0,4);
			var m = result.substring(4,6);
			if(m.indexOf(0)=="0")
				m = m.substring(1,2);
			month = y + "-" + m;
			var url = "${ctx}/aibi_ci/template/customerForm.jsp?id=" + id + "&rule=" + rule + "&month=" + month;
			openCreate(url);
		}
	});
}

jQuery.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});
};
//点击时间筛选按钮
function shaiXuanDateT(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if("" != startDate && "" != endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >"+startDate+"至"+endDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType","",true,startDate+"至"+endDate);
	  //绑定单击事件
		  $selectedItemClose.click(function(){
		     $(this).parent().remove();
		     multiTemplateSearch();
		  })
		multiTemplateSearch();
	}else if("" != startDate && "" == endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >大于"+startDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType","",true,"大于"+startDate);
	  //绑定单击事件
		  $selectedItemClose.click(function(){
		     $(this).parent().remove();
		     multiTemplateSearch();
		  })
		multiTemplateSearch();
    }else if("" == startDate && "" != endDate){
    	var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >小于"+endDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType","",true,"小于"+endDate);
	  //绑定单击事件
		  $selectedItemClose.click(function(){
		     $(this).parent().remove();
		     multiTemplateSearch();
		  })
		multiTemplateSearch();
    }
}
//显示客户群模板操作列表
function showOperList(e,ths,templateId){
	var selfIndex = $(".resultItemActive").index($(ths));
	$.each($(".resultItemActive"),function(index,item){
		if(index != selfIndex){
		 var $resultItemActiveList= $(item).find(".resultItemActiveList");
		     $resultItemActiveList.hide();
		     $(item).removeClass("resultItemActiveOption");
		}
	});
	if($('#'+templateId).children("ol").children("li").length > 0){
		$('#'+templateId).children("ol").children("li:last-child").addClass("end");
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		var _t=$(ths);
		var $resultItemActiveList= $(ths).children(".resultItemActiveList");
		if($resultItemActiveList.is(":visible")){
				$resultItemActiveList.hide();
				_t.removeClass("resultItemActiveOption");
		}else{
			var resultListHeight = $resultItemActiveList.height();
			var _tOffsetHeight= _t.offset().top + _t.height();
			var documentHeight = $(document.body).height();
			var resultHeight = resultListHeight + _tOffsetHeight;
			if(documentHeight<resultHeight){
				$(document.body).height(resultHeight + 20);
			}
		    $resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveOption")});
		}
	}
}
</script>
<form id="multiSearchForm" >
<input type="hidden" id="isLatestTemplateFlag" name="isLatestTemplateFlag" />
<input type="hidden" id="isSysRecommendTemplateFlag" name="isSysRecommendTemplateFlag" />
<div class="comWidth pathNavBox">
      <span class="pathNavHome"> 最新客户群模板</span>
      <span class="pathNavArrowRight"></span>
      <span class="pathNavNext"></span>
   <span class="amountItem"> 共<span class="amountNum"></span>个客户群模板 </span>
</div>
<HR width="100%" color="#ccccc" noShade> 
<br>
<div class="comWidth queryResultBox">
	<div class="comWidth queryResultListBox" id="queryResultListBox">
	</div>
	<div id="noMoreTemplateResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
	<div id="noResultsTemplate" class="noResults" style="display:none;">没有模板信息...</div>
</div>
<!-- 展示切换 -->
<map name="planetmap" id="planetmap">
	  <area shape="rect" coords="1,1,30,25" href ="###"    />
	  <area shape="rect" coords="34,1,62,25" href ="###"  />
</map>
</form>