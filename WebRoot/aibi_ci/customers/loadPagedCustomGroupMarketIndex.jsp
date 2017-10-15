<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
%>
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="isNotContainLocalList" value="<%=isNotContainLocalList %>"></c:set>
<script type="text/javascript">
$(document).ready( function() {
	$("#myCreateCustom").click(function(ev){
		if($("#myCreateCustom input").attr("checked")){
			$("#myCreateCustom input").attr("checked",false);
		}else{
			$("#myCreateCustom input").attr("checked",true);
		}
		multiCustomSearch();
		var e = ev || event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	
	$("#myCreateCustom input").click(function(ev){
		multiCustomSearch();
		var e = ev || event ;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	$("#viewNavBox").hoverForIE6({current:"viewNavBoxHover",delay:200});
	$("#viewNavBox .viewNavItem").hoverForIE6({current:"viewNavItemHover",delay:200});
	//工具栏定位
	var bodyWidth = $("body").width();
	var toolTipRight= parseInt((bodyWidth-1005)/2,10)-32 ;
	toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
	$("#toolTip").css("right",toolTipRight);
	$("#shopCart").css("right",parseInt((bodyWidth-1005)/2,10));
	
	//导入加提示框
  /*var cookieNums = getCookieByName("INDEX_NUMS_COOKIE_${userId}");
	if(cookieNums>1){
		$("#importClientBtnTip").hide();	
	}else{
		$("#importClientBtnTip").show();
	} */
		
	//输入框获得焦点显示按钮，失去焦点隐藏按钮
 	$("#startDate").bind({
		focus: function() {
			$("#dateSelf").show();
		},
		blur: function() {
			if($("#startDate").val() == "" && $("#endDate").val() == ""){
				$("#dateSelf").css({display:"none"}); 
			}
		}
	});
 	$("#endDate").bind({
		focus: function() {
			$("#dateSelf").show();
		},
		blur: function() {
			if($("#startDate").val() == "" && $("#endDate").val() == ""){
				$("#dateSelf").css({display:"none"}); 
			}
		}
	});
 	$("#minCustomNum_").bind({
		focus: function() {
			$("#numSelf").show();
		},
		blur: function() {
			if($("#minCustomNum_").val() == "" && $("#maxCustomNum_").val == ""){
				$("#numSelf").css({display:"none"}); 
			}
		}
	});
 	$("#maxCustomNum_").bind({
		focus: function() {
			$("#numSelf").show();
		},
		blur: function() {
			if($("#minCustomNum_").val() == "" && $("#maxCustomNum_").val == ""){
				$("#numSelf").css({display:"none"}); 
			}
		}
	});
 	$(".customNumDiv").bind({
		focus: function() {
			if($("#minCustomNum_").val() != "" || $("#maxCustomNum_").val() != ""){
				$("#numSelf").show();
			}
		},
		blur: function() {
			if($("#minCustomNum_").val() == "" && $("#maxCustomNum_").val == ""){
				$("#numSelf").css({display:"none"}); 
			}
		}
	});
 	$(".dateSelfDiv").bind({
		focus: function() {
			if($("#startDate").val() != "" || $("#endDate").val() != ""){
				$("#dateSelf").show();
			}
		},
		blur: function() {
			if($("#startDate").val() == "" && $("#endDate").val() == ""){
				$("#dateSelf").css({display:"none"}); 
			}
		}
	});
 	
	//默认排序样式切换
    $(".defaultSort").click(function(){
	   $(this).toggleClass('defaultSortActive');
	})
	
 	$("#nameOrder").hover(function(){
	    $(this).addClass("sortItemHover");
	},function(){
	    $(this).removeClass("sortItemHover");
	}).click(function(){
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).attr("title","点击按客户群名称正序排序");
		}else{
			if($(this).hasClass("desc")){
				$(this).attr("title","点击按客户群名称倒序排序");
			}else{
				$(this).attr("title","点击按客户群名称正序排序");
			}
		}
		$("#sortList").find("li").each(function(i,v){
			if($(this).find("a").attr("id") != 'nameOrder'){
				$(this).find("a").removeClass("desc");
				$(this).find("a").removeClass("asc");
			}
		});
		$("#sortList").find("li").find("a").removeClass("sortItemActive");
 		$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).addClass("sortItemActiveDown");
		}else{
			if($(this).hasClass("desc")){
				$(this).addClass("sortItemActive");
	           $(this).removeClass("sortItemActiveDown");
			}else{
			   $(this).addClass("sortItemActiveDown");
	           $(this).removeClass("sortItemActive");
			}
		}
		sortByName('CUSTOM_GROUP_NAME',this);
	});
	
 	$("#timeOrder").hover(function(){
	    $(this).addClass("sortItemHover");
	},function(){
	    $(this).removeClass("sortItemHover");
	}).click(function(){
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).attr("title","点击按最新修改时间从远到近排序");
		}else{
			if($(this).hasClass("desc")){
				$(this).attr("title","点击按最新修改时间从近到远排序");
			}else{
				$(this).attr("title","点击按最新修改时间从远到近排序");
			}
		}
		$("#sortList").find("li").each(function(i,v){
			if($(this).find("a").attr("id") != 'timeOrder'){
				$(this).find("a").removeClass("desc");
				$(this).find("a").removeClass("asc");
			}
		});
		$("#sortList").find("li").find("a").removeClass("sortItemActive");
 		$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).addClass("sortItemActiveDown");
		}else{
			if($(this).hasClass("desc")){
				$(this).addClass("sortItemActive");
	           $(this).removeClass("sortItemActiveDown");
			}else{
			   $(this).addClass("sortItemActiveDown");
	           $(this).removeClass("sortItemActive");
			}
		}
		sortByName('NEW_MODIFY_TIME',this);
	});
 	
 	$("#numOrder").hover(function(){
	    $(this).addClass("sortItemHover");
	},function(){
	    $(this).removeClass("sortItemHover");
	}).click(function(){
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).attr("title","点击按客户群规模从小到大排序");
		}else{
			if($(this).hasClass("desc")){
				$(this).attr("title","点击按客户群规模从大到小排序");
			}else{
				$(this).attr("title","点击按客户群规模从小到大排序");
			}
		}
		$("#sortList").find("li").each(function(i,v){
			if($(this).find("a").attr("id") != 'numOrder'){
				$(this).find("a").removeClass("desc");
				$(this).find("a").removeClass("asc");
			}
		});
		$("#sortList").find("li").find("a").removeClass("sortItemActive");
 		$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).addClass("sortItemActiveDown");
		}else{
			if($(this).hasClass("desc")){
				$(this).addClass("sortItemActive");
	           $(this).removeClass("sortItemActiveDown");
			}else{
			   $(this).addClass("sortItemActiveDown");
	           $(this).removeClass("sortItemActive");
			}
		}
		sortByName('CUSTOM_NUM',this);
	});
 	
	$("#useNumOrder").hover(function(){
	    $(this).addClass("sortItemHover");
	},function(){
	    $(this).removeClass("sortItemHover");
	}).click(function(){
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).attr("title","点击按人气从小到大排序");
		}else{
			if($(this).hasClass("desc")){
				$(this).attr("title","点击按人气从大到小排序");
			}else{
				$(this).attr("title","点击按人气从小到大排序");
			}
		}
		$("#sortList").find("li").each(function(i,v){
			if($(this).find("a").attr("id") != 'useNumOrder'){
				$(this).find("a").removeClass("desc");
				$(this).find("a").removeClass("asc");
			}
		});
		$("#sortList").find("li").find("a").removeClass("sortItemActive");
 		$("#sortList").find("li").find("a").removeClass("sortItemActiveDown");
		if(!$(this).hasClass("desc") && !$(this).hasClass("asc")){
			$(this).addClass("sortItemActiveDown");
		}else{
			if($(this).hasClass("desc")){
				$(this).addClass("sortItemActive");
	           $(this).removeClass("sortItemActiveDown");
			}else{
			   $(this).addClass("sortItemActiveDown");
	           $(this).removeClass("sortItemActive");
			}
		}
		sortByName('USECOUNT',this);
	});
	//添加你选项的查询条件
	$(".selectOptionValue a").click(function(){
		if($(this).parents("div").attr("id") != 'sceneName'){
			$(this).parent("li").parent("ul").children("li").each(function(){
	  			var elementE = $(this).children("a").attr("selectedElement");
	  			$(this).children("a").removeClass("selectOptionValueHover");
			});
		}else{
			if($(this).attr("selectedElement") == "all"){
				$(this).parent("li").parent("ul").children("li").each(function(){
		  			var elementE = $(this).children("a").attr("selectedElement");
		  			$(this).children("a").removeClass("selectOptionValueHover");
				});
			}else{
				$("#sceneAll").removeClass("selectOptionValueHover");
			}
		}
		var ths = $(this);
		$(this).addClass("selectOptionValueHover");
		var closeLi = $(this).parent("li").parent("ul");
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
	    huChi($selectedItemBox,idStr,$(this),flag,value);
		
		//绑定单击事件
		  $selectedItemClose.click(function(){
			  if(idStr.indexOf("sceneName") >= 0){
				  $(this).parent().remove();
				  ths.removeClass("selectOptionValueHover");
				  var f = false;
				  closeLi.children("li").each(function(){
					  f = $(this).children("a").hasClass("selectOptionValueHover");
					  if(f){return false;}
				  });
				  if(!f){
					  closeLi.children("li").each(function(){
				    	 if($(this).children("a").attr("selectedElement") == "all"){
				    		 $(this).children("a").addClass("selectOptionValueHover");
				    	 }
					  });
				  }
			  }else{
			     $(this).parent().remove();
			     closeLi.children("li").each(function(){
			    	 if($(this).children("a").attr("selectedElement") == "all"){
			    		 $(this).children("a").addClass("selectOptionValueHover");
			    	 }else{
			    		 $(this).children("a").removeClass("selectOptionValueHover");
			    	 }
				  });
			  }
			  multiCustomSearch();
		  });
	 	multiCustomSearch();
	});
	//更多查询条件
	$("#moreOptionActive").click(function(){
    	if($("#moreSelectOptionList").is(":visible")){
	   		$("#moreSelectOptionList").slideUp(500);
	   		$(this).val("更多条件");
	   		$(this).removeClass("up");
		}else{
	   		$("#moreSelectOptionList").slideDown(500);
	   		$(this).addClass("up");
	   		$(this).val("收起");
		}
	});
	$( "#dialog_edit" ).dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 730,
		title:"新增客户群预警",
		modal: true,
		resizable:false
	});
	//系统自动匹配提示
	$("#dialog-match").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 550,
		resizable:false,
		modal: true,
		close: function() {
		$( "#sysMatchTitle" ).html("");
		$( "#sysMatchTitle" ).html("您未进行系统自动匹配！");
		$("#sysMatch").removeClass("tag_btn_disable").removeAttr("title").unbind("click").bind("click",function(){
			sysMatch();
		});
		}
	});
	//提取模板弹出框
	$("#createTemplateDialog").dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: false,
		width: 660,
		title: "保存模板",
		modal: true,
		resizable:false
	});
	
	//浙江审批
    $( "#dialog_download_approve" ).dialog({
    	dialogClass:"ui-dialogFixed",
        autoOpen: false,
        width: 480,
        resizable:false,
        title:"下载审批",
        modal: true,
        close:function(){
            $("#dialog_download_approve_detail").attr("src", "");
        }
    });
    
    //浙江审批
    $( "#dialog_commit_approve" ).dialog({
    	dialogClass:"ui-dialogFixed",
        autoOpen: false,
        width: 480,
        resizable:false,
        title:"提交审批",
        modal: true,
        close:function(){
            $("#dialog_commit_approve_detail").attr("src", "");
        }
    });

});
//客户群默认排序
function sortDefault(){
	$("#sortList").find("li").find("a").removeClass();
	$("#useNumOrder").attr("title","点击按人气从大到小排序");
	$("#timeOrder").attr("title","点击按最新修改时间从近到远排序");
	$("#nameOrder").attr("title","点击按客户群名称倒序排序");
	$("#numOrder").attr("title","点击按客户群规模从大到小排序");
	$("#order").val("");
	$("#orderBy").val("");
	multiCustomSearch();
}
//客户群单排序
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

/* 	if(orderBy == null || orderBy == ""){ 多排序
		order = sort;
		orderBy = columnName;
	}else{
		var orders = order.split(",");
		var orderBys = orderBy.split(",");
		var isNew = 0;
		for(var i=0;i<orders.length;i++){
			if(orderBys[i] == columnName){
				orders[i] = sort;
				isNew = 1;
				break;
			}
		}
		order = orders.join(",");
		orderBy = orderBys.join(",");
		if(isNew!=1){
			order += "," + sort;
			orderBy += "," + columnName;
		}
	} */
	$("#order").val(sort);
	$("#orderBy").val(columnName);
	multiCustomSearch();
}
//同一种条件是互斥添加
function huChi(selectedItemBox,selectedDivId,selectedObj,flag,value){
	selectedItemBox.hover(function(){
		$(this).addClass("selectedItemBoxHover");
	},function(){
		$(this).removeClass("selectedItemBoxHover");
	});
	var isScenceTypeArr = $.trim(selectedDivId).split("_");
	value = $.trim(value);
	if(isScenceTypeArr[0] == 'sceneName'){//客户群分类 不互斥筛选
		var selectedE = "";
		if(selectedObj != null){
			selectedE = selectedObj.attr("selectedElement");
		}
	    if($(".selectedItemDetail").size() >= 1){
			if(selectedE != "all"){
				$(".selectedItemDetail").each(function(){
					var sceneName = $.trim($(this).html());
		          	if(value == sceneName){
		        	  	flag = false;
		          		return false;
		          	}else{
		          		flag = true;
		          	}
				});
	    	}else{
				$(".selectedItemDetail").each(function(){
					var idArr = $.trim($(this).attr("id")).split("_");
		          	if(idArr[0] == 'sceneName'){
		          		$(this).parent().remove();
		          	}
				});
				return false;
	    	}
			selectedItemBox.hover(function(){
				$(this).addClass("selectedItemBoxHover");
			},function(){
				$(this).removeClass("selectedItemBoxHover");
			});
			if(flag) $("#selectedItemListBox").append(selectedItemBox);
	    }else{
			if(selectedE != "all"){
				$("#selectedItemListBox").append(selectedItemBox);
			}
	    }
	}else{//互斥筛选
		var selectedE = "";
		if(selectedObj != null){
			selectedE = selectedObj.attr("selectedElement");
		}
	    if($(".selectedItemDetail").size() >= 1){
			$(".selectedItemDetail").each(function(){
	        	var idArr = $.trim($(this).attr("id")).split("_");
	        	var selectedDivIdArr = $.trim(selectedDivId).split("_");
	          	if(idArr[0] == selectedDivIdArr[0]){
	          		if(selectedE == "all"){
	          			$(this).parent().remove();
	          		}else{
		        	  	$(this).html(value);
		        	  	$(this).attr("id",selectedDivId);
	          		}
	        	  	flag = false;
	          		return false;
	          	}else{
	          		if(selectedE == "all"){
	          			flag = false;
	          		}else{
		          		flag = true;
	          		}
	          	}
			});
			if(flag) $("#selectedItemListBox").append(selectedItemBox);
	    }else{
			if(selectedE != "all"){
				$("#selectedItemListBox").append(selectedItemBox);
			}
	    }
	}
}
//点击规模筛选按钮
function shaiXuanNum(obj){
	obj.parent("div").parent("li").parent("ul").children("li").each(function(){
			  $(this).children("a").removeClass("selectOptionValueHover");
    });
	var minNum = $.trim($("#minCustomNum_").val());
    var maxNum = $.trim($("#maxCustomNum_").val());
    if(maxNum != "" && !/^\d+$/.test(maxNum)){
   		commonUtil.create_alert_dialog("confirmDialog", {
   			 "txt":"请输入合法字符！",
   			 "type":"failed",
   			 "width":500,
   			 "height":200
   		});
    	return;
    }
    if(minNum != "" && !/^\d+$/.test(minNum)){
    	commonUtil.create_alert_dialog("confirmDialog", {
  			 "txt":"请输入合法字符！",
  			 "type":"failed",
  			 "width":500,
  			 "height":200
  		});
    	return;
    }
    if(maxNum != "" && parseInt(minNum,10) >= parseInt(maxNum,10)){
	   	commonUtil.create_alert_dialog("confirmDialog", {
  			 "txt":"最大值小于最小值！",
  			 "type":"failed",
  			 "width":500,
  			 "height":200
  		});
	   	return;
    }
    if("" != minNum && "" != maxNum){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"customNum\" >"+minNum+"-"+maxNum+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"customNum",null,true,minNum+"-"+maxNum);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
		   $(this).parent().remove();
		   multiCustomSearch();
		});
	    multiCustomSearch();
    }else if("" != minNum && "" == maxNum){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"customNum\" >大于"+minNum+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"customNum",null,true,"大于"+minNum);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
		   $(this).parent().remove();
		   multiCustomSearch();
		});
	    multiCustomSearch();
    }else if("" == minNum && "" != maxNum){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"customNum\" >小于"+maxNum+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"customNum",null,true,"小于"+maxNum);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
		   $(this).parent().remove();
		   multiCustomSearch();
		});
	    multiCustomSearch();
    }
}
//点击时间筛选按钮
function shaiXuanDate(obj){
	obj.parent("div").parent("li").parent("ul").children("li").each(function(){
		  $(this).children("a").removeClass("selectOptionValueHover");
	});
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if("" != startDate && "" != endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >"+startDate+"至"+endDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType",null,true,startDate+"至"+endDate);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
			$(this).parent().remove();
			$("#dateType li").each(function(){
				$("#dateSelf").css({display:"none"}); 
			  	 if($(this).children("a").attr("selectedElement") == "all"){
			  		 $(this).children("a").addClass("selectOptionValueHover");
			  	 }else{
			  		 $(this).children("a").removeClass("selectOptionValueHover");
			  	 }
			});
			multiCustomSearch();
		});
		multiCustomSearch();
	}else if("" != startDate && "" == endDate){
		var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >大于"+startDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType",null,true,"大于"+startDate);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
		     $(this).parent().remove();
		     $("#dateType li").each(function(){
		    	 $("#dateSelf").css({display:"none"}); 
			  	 if($(this).children("a").attr("selectedElement") == "all"){
			  		 $(this).children("a").addClass("selectOptionValueHover");
			  	 }else{
			  		 $(this).children("a").removeClass("selectOptionValueHover");
			  	 }
			});
		     multiCustomSearch();
		});
		multiCustomSearch();
    }else if("" == startDate && "" != endDate){
    	var $selectedItemBox = $("<div class=\"selectedItemBox\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\" id=\"dateType\" >小于"+endDate+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
	    $selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
	    huChi($selectedItemBox,"dateType",null,true,"小于"+endDate);
	  	//绑定单击事件
		$selectedItemClose.click(function(){
		     $(this).parent().remove();
		     $("#dateType li").each(function(){
		    	 $("#dateSelf").css({display:"none"}); 
			  	 if($(this).children("a").attr("selectedElement") == "all"){
			  		 $(this).children("a").addClass("selectOptionValueHover");
			  	 }else{
			  		 $(this).children("a").removeClass("selectOptionValueHover");
			  	 }
			});
		    multiCustomSearch();
		});
		multiCustomSearch();
    }
}

function delOne(customGroupId){
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认删除该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":customGroupId
	},"del") ;
}
function shareOne(cid){
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认共享该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":cid
	},"shareCustomer");
}
function cancelShareOne(cid){
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确认取消共享该客户群？",
		 "type":"confirm",
		 "width":500,
		 "height":200,
		 "param":cid
	},"cancelShareCustomer");
}

//删除
function del(customGroupId){
	var actionUrl = $.ctx + "/ci/customersManagerAction!delete.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: "ciCustomGroupInfo.customGroupId="+customGroupId,
		success: function(result){
			if(result.success){
				$.fn.weakTip({"tipTxt":"" +result.msg,"backFn":function(){
					multiCustomSearch();
				 }});
			}else{
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":"删除失败：" +result.msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				});
			}
		}
	});
}

//推送提交
function push(){
	$("#push_single_alarm_iframe")[0].contentWindow.submitForm();
}

//修改客户群
function editCustomer(typeId, cycle, cid){
//	if(typeId == 1 && cycle != 1){
//		var url = $.ctx + "/ci/customersManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+cid;
//		window.open(url, "_blank", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
//	}else{
		var url = $.ctx + "/ci/customersManagerAction!preEditCustomer.ai2do?ciCustomGroupInfo.customGroupId="+cid;
		openCreate(url);
//	}
}
//客户群共享弹出层
function openCustomerShare(url){
	var dlgObj = dialogUtil.create_dialog("shareCustomerDialog", {
		"title" :  "客户群共享",
		"height": "auto",
		"width" : 660,
		"frameSrc" : url,
		"frameHeight":180,
		"position" : ['center','center'] 
	});
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//客户群共享
function customerShare(id,name,cityId,createUserId,msg){
	msg=encodeURIComponent(encodeURIComponent(msg));
	name=encodeURIComponent(encodeURIComponent(name));
	var url = "${ctx}/aibi_ci/customers/shareCustomerForm.jsp?id=" + id + "&name=" + name+"&cityId="+ cityId+"&msg="+msg+"&createUserId="+createUserId;
	openCustomerShare(url);
}
function shareCustomer(cid){
	var cname=$("#share_"+cid).attr("c_name");
	var cityId=$("#share_"+cid).attr("c_cityId");
	var createUserId=$("#share_"+cid).attr("c_userId");
	var actionUrl=$.ctx + "/ci/customersManagerAction!shareCustom.ai2do"
	var para={
			"ciCustomGroupInfo.customGroupId":cid,
		 	"ciCustomGroupInfo.customGroupName":cname,
		 	"ciCustomGroupInfo.createCityId":cityId,
		 	"ciCustomGroupInfo.createUserId":createUserId
		};
	$.post(actionUrl, para, function(result){
			if(result.success){
				//showAlert("客户群共享成功！","success",multiCustomSearch);
				$.fn.weakTip({"tipTxt":"客户群共享成功！","backFn":function(){
					multiCustomSearch();
				 }});
			}else{
				var msg = result.msg;
				if(msg.indexOf("重名") >= 0){
					customerShare(cid,cname,cityId,createUserId,msg);
				}else{
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					}); 
				}
			}
		});
}
function cancelShareCustomer(cid){
	var actionUrl=$.ctx + "/ci/customersManagerAction!cancelShareCustom.ai2do"
	var para={
			"ciCustomGroupInfo.customGroupId":cid
		};
	$.post(actionUrl, para, function(result){
			if(result.success){
				$.fn.weakTip({"tipTxt":"取消客户群共享成功！","backFn":function(){
					multiCustomSearch();
				 }});
			}else{
				var msg = result.msg;
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				}); 
			}
	});
}
//关闭推送弹出层
function closePush(){
	$("#dialog_push_single").dialog("close");
}
//关闭批量推送弹出层
function closePushBatch(){
	$("#dialog_push_batch").dialog("close");
}
//打开共享成功弹出层
function openShareSuccess(customerName,templateName){
	templateName=encodeURIComponent(encodeURIComponent(templateName));
	customerName = encodeURIComponent(encodeURIComponent(customerName));
	var ifmUrl = "${ctx}/aibi_ci/customers/shareSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
	var dlgObj = dialogUtil.create_dialog("successShareDialog", {
		"title" :  "系统提示",
		"height": "auto",
		"width" : 550,
		"frameSrc" : ifmUrl,
		"frameHeight":265,
		"position" : ['center','center']
	},"simpleSearch");
}
//配置客户群预警
function addAlarmThreshold(customGroupId,customGroupName){
	var customGroupName = encodeURIComponent(encodeURIComponent(customGroupName));
	var ifmUrl = "${ctx}/aibi_ci/customersAlarm/customersAlarmThresholdEdit.jsp?customGroupId="+customGroupId+"&customGroupName="+customGroupName;
	ifmUrl += "&fromPageFlag=1";
	$("#add_alarm_iframe").attr("src", ifmUrl);
	$("#dialog_edit").dialog( "open" );
	$(".tishi").each(function(){
        var toffset=$(this).parent("td").offset();
        var td_height=$(this).parent("td").height();
        $(this).css({top:toffset.top + td_height + 13, left: toffset.left});
    });
}
var pushFromSign = false;
//单个推送客户群
function pushCustomerGroupSingle(customerGroupId, updateCycle, isPush, duplicateNum) {
	if(duplicateNum) {
		if(duplicateNum != 0) {
			$.fn.weakTip({"tipTxt":"客户群包含重复记录不能进行推送设置！"});
			return;
		}
	}
	pushFromSign = true;
	var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId=" 
		+ customerGroupId + "&updateCycle=" + updateCycle + "&isPush=" + isPush;
	var dlgObj = dialogUtil.create_dialog("dialog_push_single", {
		"title" :  "客户群推送设置",
		"height": "auto",
		"width" : 730,
		"frameSrc" : ifmUrl,
		"frameHeight":335,
		"position" : ['center','center'] 
	});
}
function callBack(){
    closeDialog("#dialog_edit");
}

//关闭确认弹出层
function closeDialog(dialogId){
	$(dialogId).dialog("close");
}

function importInit(){
	//调用iframe中的内容
	$("#importFile")[0].contentWindow.submitList();
}
//查看客户群清单表
function queryGroupList(id,dataDate){
	var url = $.ctx + "/ci/customersManagerAction!findGroupList.ai2do?ciCustomGroupInfo.customGroupId="+id+"&ciCustomGroupInfo.dataDate="+dataDate;
	openQueryList(url);
}

//查询客户群清单表弹出层
function openQueryList(url){
	var dlgObj = dialogUtil.create_dialog("queryListDialog", {
		"title" :  "客户群清单预览",
		"height": "auto",
		"width" : 700,
		"frameSrc" : url,
		"frameHeight":328,
		"position" : ['center','center'] 
	}); 
}
//客户群趋势分析明细页面加载方法
function showCustomerTrend(customGroupId) {
    var ifmUrl = "${ctx}/ci/customersTrendAnalysisAction!initCustomersTrendDetail.ai2do?customGroupId="+customGroupId;
    var dlgObj = dialogUtil.create_dialog("dialog_look", {
		"title" :  "客户群清单下载",
		"height": "auto",
		"width" : 980,
		"frameSrc" : ifmUrl,
		"frameHeight":376,
		"position" : ['center','center'] 
	});
}
function notShowCustomNum(){
	$.fn.weakTip({"tipTxt":"抱歉不可操作！非本人创建客户群。" });
}

//一次性客户群重新生成
function oneTimeRegenCustomer(customGroupId,carateType){
	if(carateType == 7){//文件创建的不支持
		return;
	}
	var url = "${ctx}/ci/customersManagerAction!checkReGenerateCustomer.ai2do?ciCustomGroupInfo.customGroupId="+customGroupId;;
	$.ajax({
		url: url,
		success: function(result){
			if(result.success){
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":"抱歉，该客户群正在被使用，无法重新生成，请稍后再试",
					 "type":"success",
					 "width":500,
					 "height":200
				}); 
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				regenCustomer(customGroupId,carateType);
			}
		}
	});
	
}
//客户群重新生成dataDate--要重新生成失败清单的数据日期，不赋值则重新生成全部失败的清单
function regenCustomer(customGroupId,carateType,dataDate){
	if(carateType == 7){//文件创建的不支持
		return;
	}
	if(!dataDate){
		dataDate = '';
	}
	var url = "${ctx}/ci/customersManagerAction!reGenerateCustomer.ai2do";
	$.ajax({
		url: url,
		data:{
			"ciCustomGroupInfo.customGroupId":customGroupId,
			"ciCustomGroupInfo.dataDate":dataDate
		},
		success: function(result){
			if(result.success){
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":"已提交，请刷新查看",
					 "type":"success",
					 "width":500,
					 "height":200
				}); 
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}else{
				commonUtil.create_alert_dialog("confirmDialog", {
					 "txt":result.msg,
					 "type":"failed",
					 "width":500,
					 "height":200
				}); 
				$("#console").append("客户群运算：" +result.msg+"<br/>");
			}
			multiCustomSearch();
		}
	});
}
//客户群设置系统推荐与取消系统推荐
var recomFlag = true;  
function changeRecomCustom(ths){
	if(recomFlag){
		recomFlag = false;
		var customId = $(ths).attr("customId");
		var actionUrl = $.ctx + "/ci/customersManagerAction!changeRecommendCustom.ai2do";
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: {"ciCustomGroupInfo.customGroupId" : customId},
			success: function(result){
				if(result.success){
					$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
						multiCustomSearch();
						recomFlag = true;
					 }});
				}else{
					recomFlag = true;
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":result.msg,
						 "type":"failed",
						 "width":500,
						 "height":200
					}); 
				}
			}
		});
	}else{
		return false;
	}
}

//客户群提取模板，打开编辑对话框
function createTemplate(customId,customName,customSceneId){
	customName = encodeURIComponent(encodeURIComponent(customName));
	$("#createTemplateDialog").dialog({
		close: function( event, ui ) {
			$("#createTemplateFrame").attr("src", "");
		}
	});
	$("#createTemplateDialog").dialog("close");
	var ifmUrl = "${ctx}/aibi_ci/customers/createTemplate.jsp?customId="+customId+"&customName="+customName+"&sceneId="+customSceneId;
	$("#createTemplateFrame").attr("src", ifmUrl).load(function(){//resetForm();
	});
	$("#createTemplateDialog").dialog("open");
	
	$(".tishi").each(function(){
		var toffset=$(this).parent("td").offset();
		var td_height=$(this).parent("td").height();
		$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
	});
}
//产品手工匹配
function customMatch(){
	var listTableName = $("#matchListTableName").val();
	var customGroupId = $("#matchCustomGroupId").val();
	var dataDate = $("#matchDataDate").val();
	var customNum = $("#matchCustomNum").val();
	var param = "customGroup.listTableName="+listTableName;
	param = param + "&customGroup.customGroupId="+customGroupId;
	param = param + "&customGroup.customNum="+customNum;
	param = param + "&dataDate="+dataDate;
	param = param + "&fromPageFlag=1";
	param = param + "&tab=match";
	var actionUrl = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(actionUrl, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	$( "#dialog-match" ).dialog( "close" );
}
//产品系统匹配
function sysMatch(){
	var customGroupId = $("#matchCustomGroupId").val();
	var listTableName = $("#matchListTableName").val();
	var data = "customGroup.listTableName="+listTableName;
	data = data +"&customGroup.customGroupId="+customGroupId;
	var actionUrl = $.ctx+"/ci/marketingStrategyAction!sysMatch.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: data,
		success: function(result){
			if(result.success){
				$( "#sysMatchTitle" ).html("");
				$( "#sysMatchTitle" ).html("已经开始进行系统匹配");
				$("#sysMatch").addClass("tag_btn_disable").unbind("click").removeAttr("title").attr("title","已经进行系统匹配！");
			}else{
				$( "#sysMatchTitle" ).hide();
				$( "#failSysMatchTitle" ).show();
			}
		}
	});
}
//判断客户群是否进行过系统自动匹配
function customSysMatch(customGroupId,listTableName,customNum,dataDate,failedListCount){
	if(failedListCount >= 1){
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"客户群清单未创建成功",
			 "type":"failed",
			 "width":500,
			 "height":200
		}); 
		return;
	}
	var actionUrl = $.ctx + "/ci/marketingStrategyAction!isCustomProductMatch.ai2do?customGroup.listTableName="+listTableName+"&customGroup.customGroupId="+customGroupId;
	$.ajax({
		type: "POST",
		url: actionUrl,
		success: function(result){
			if(result.success){
				var param = "customGroup.listTableName="+listTableName;
				param = param + "&customGroup.customGroupId="+customGroupId;
				param = param + "&customGroup.customNum="+customNum;
				param = param + "&dataDate="+dataDate;
				param = param + "&fromPageFlag=1";
				param = param + "&tab=match";
				var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
				window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
				$( "#dialog-match" ).dialog( "close" );
			}else{
				if(result.matchIng){
					$( "#sysMatchTitle" ).html("");
					$( "#sysMatchTitle" ).html("已经开始进行系统匹配");
					$("#sysMatch").addClass("tag_btn_disable").unbind("click").removeAttr("title").attr("title","已经进行系统匹配！");
				}
				$("#matchCustomGroupId").val(customGroupId);
				$("#matchListTableName").val(listTableName);
				$("#matchCustomNum").val(customNum);
				$("#matchDataDate").val(dataDate);
				$("#dialog-match").dialog("open");
			}
		}
	});
}

//对比分析连接
function openCustomContrast(customGroupId,listTableName,customNum,dataDate,failedListCount){
	if(failedListCount >= 1){
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"客户群清单未创建成功",
			 "type":"failed",
			 "width":500,
			 "height":200
		});
		return;
	}
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//关联分析连接
function openCustomRel(customGroupId,listTableName,customNum,dataDate,failedListCount) {
	if(failedListCount >= 1){
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"客户群清单未创建成功",
			 "type":"failed",
			 "width":500,
			 "height":200
		});
		return;
	}
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=rel";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//标签微分
function openlabelDeff(customGroupId,listTableName,customNum,dataDate){
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;		
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=tag";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//指标微分
function openIndexDeff(customGroupId,listTableName,customNum,dataDate){
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&customGroup.listTableName="+listTableName;		
		param = param + "&dataDate="+dataDate;
		param = param + "&fromPageFlag=1";
		param = param + "&tab=sa";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//显示客户群操作列表
function showOperCustomList(e,ths,customId){
	var selfIndex = $(".resultItemActive").index($(ths));
	$.each($(".resultItemActive"),function(index,item){
		if(index != selfIndex){
		 var $resultItemActiveList= $(item).find(".resultItemActiveList");
		     $resultItemActiveList.hide();
		     $(item).removeClass("resultItemActiveOption");
		}
	 })
	if($('#'+customId).children("ol").children("li").length == 0){
		var createCityId = $(ths).attr("createCityId");
		var createUserId = $(ths).attr("createUserId");
		var isPrivate = $(ths).attr("isPrivate");
		var isContainLocalList = $(ths).attr("isContainLocalList");
		var userId = "${userId}";
		var cityId = "${cityId}";
		var root = "${root}";
		if(isPrivate == 0){
			if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
				$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
			}else{
				$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，无操作！" });
			}
		}else{
			if(createUserId != userId){
				$.fn.weakTip({"tipTxt":"抱歉，您无权限操作！" });
			}else{
				$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，无操作！" });
			}
		}
	}else{
		$('#'+customId).children("ol").children("li:last-child").addClass("end");
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

//浙江审批初始化
function showDownloadApprove(listTableName){
    var ifmUrl = "${ctx}/ci/zjDownloadApproveAction!initDownloadApprove.ai2do?&listTableName=" + listTableName;
    $("#dialog_download_approve_detail").attr("src", ifmUrl);
    $("#dialog_download_approve").dialog("open");
}
//浙江审批提交
function showCommitApprove(listTableName, customGroupId) {
    var ifmUrl = "${ctx}/ci/zjDownloadApproveAction!initCommitApprove.ai2do?customGroupId="+ customGroupId + "&listTableName=" + listTableName;
    $("#dialog_commit_approve_detail").attr("src", ifmUrl);
    $("#dialog_commit_approve").dialog("open");
}
//浙江关闭审批提交
function closeCommitApprove(){
	$("#dialog_download_approve").dialog("close");
    $("#dialog_commit_approve").dialog("close");
}

function importClientBtnTip_remove(ts){
	$(ts).remove();
}

//客户群关注图标点击事件
function attentionOperCustom(customId){
	var isAttention = $("#isAttention_"+customId).val();
	if(isAttention == 'true'){
		//已经关注，点击进行取消关注动作
		//alert('已经关注，点击进行取消关注动作');
		var url=$.ctx+'/ci/customersManagerAction!delUserAttentionCustom.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data:{'customGroupId':customId},
	      	success: function(result){
		       	if(result.success) {
		       		$.fn.weakTip({"tipTxt":result.message });
		       		//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+customId).val('false');
					$("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
					$("#attentionImg_"+customId).attr("title",'点击收藏客户群');
					$("#isAttentionShow_"+customId).attr("style",'display: none');
					
				}else{
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":result.message,
						 "type":"failed",
						 "width":500,
						 "height":200
					});
				}
			}
		});
		
	}else{
		//没有关注，点击进行关注动作触发
		//alert('没有关注，点击进行关注动作触发');
		var url=$.ctx+'/ci/customersManagerAction!addUserAttentionCustom.ai2do';
		$.ajax({
			type: "POST",
				url: url,
				data:{'customGroupId':customId},
				success: function(result){
				if(result.success) {
					$.fn.weakTip({"tipTxt":result.message });
					//window.parent.showAlert(result.message,"success");
					$("#isAttention_"+customId).val('true');
					$("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
					$("#attentionImg_"+customId).attr("title",'点击取消客户群收藏');
					$("#isAttentionShow_"+customId).attr("style",'display: block');
				}else{
					commonUtil.create_alert_dialog("confirmDialog", {
						 "txt":result.message,
						 "type":"failed",
						 "width":500,
						 "height":200
					});
				}
			}
        });
	}
}
function failAddShoppingCar(ths){
	var createCityId = $(ths).attr("createCityId");
	var createUserId = $(ths).attr("createUserId");
	var isPrivate = $(ths).attr("isPrivate");
	var isContainLocalList = $(ths).attr("isContainLocalList");
	var userId = "${userId}";
	var cityId = "${cityId}";
	var root = "${root}";
	if(isPrivate == 0){
		if(createCityId != root && createCityId != cityId && isContainLocalList != isNotContainLocalList ){
			$.fn.weakTip({"tipTxt":"抱歉，您无权限添加到收纳篮！" });
		}else{
			$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！" });
		}
	}else{
		if(createUserId != userId){
			$.fn.weakTip({"tipTxt":"抱歉，您无权限添加到收纳篮!" });
		}else{
			$.fn.weakTip({"tipTxt":"抱歉，该客户群无规则、无清单可用，不能添加到收纳篮！" });
		}
	}
}

//推送设置
function pushCustomGroup(customerGroupId) {
	var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId=" 
		+ customerGroupId;
	$("#dialog-form").dialog("close");
	$('#push_single_alarm_iframe').attr('src', ifmUrl);
	$('#dialog_push_single').dialog('open');
}
function pushCustomerGroupConfirm() {
	if($("#dialog_push_single_iframe")[0].contentWindow.validateIsNull()) {
		commonUtil.create_alert_dialog("confirmDialog", {
			 "txt":"确认推送该客户群?",
			 "type":"confirm",
			 "width":500,
			 "height":200
		},"push");
	}
}
//推送提交
function push(){
	var url = '${ctx}/ci/customersManagerAction!pushCustomerAfterSave.ai2do';
	if(pushFromSign) {
		url = false;
	}
	$("#dialog_push_single_iframe")[0].contentWindow.submitForm(url);
	$('#dialog_push_single').dialog('close');
}
function customValidate(e,ths,typeId){
	var customId = $(ths).attr("element");
	var url = $.ctx + '/ci/ciIndexAction!findCusotmValidate.ai2do?ciCustomGroupInfo.customGroupId='+customId;
	$.ajax({
		url: url,
		type: "POST",
		async: false,
		success: function(result){
			if(result.success){
				addToShoppingCar(e,ths,typeId);
			}else{
				$.fn.weakTip({"tipTxt":result.msg });
			}
		}
	});
}
function customValidate2(customGroupId,typeId,isEditCustomFlag){
	var url = $.ctx + '/ci/ciIndexAction!findCusotmValidate.ai2do?ciCustomGroupInfo.customGroupId='+customGroupId;
	$.ajax({
		url: url,
		type: "POST",
		async: false,
		success: function(result){
			if(result.success){
				addShopCart(customGroupId,typeId,isEditCustomFlag);
			}else{
				$.fn.weakTip({"tipTxt":result.msg });
			}
		}
	});
}

//展示客户群规则
function showRule(id){
	var url = $.ctx + "/ci/customersManagerAction!showRule.ai2do?ciCustomGroupInfo.customGroupId="+id;
	var dlgObj = dialogUtil.create_dialog("queryGroupRuleDialog", {
		"title" :  "客户群的完整规则",
		"height": "auto",
		"width" : 800,
		"frameSrc" : url,
		"frameHeight":400,
		"position" : ['center','center'] 
	}).focus(); 
}
</script>
<!--  页面路径导航  -->
<form id="multiSearchForm" class="_groupManageInfo">
	<input type="hidden" id="order" name="pager.order" />
	<input type="hidden" id="orderBy" name="pager.orderBy" />
	<input id="sceneIdC" name="ciCustomGroupInfo.sceneId" type="hidden"/>
	<input id="customGroupName" name="ciCustomGroupInfo.customGroupName" type="hidden"/>
	<input id="minCustomNum" name="ciCustomGroupInfo.minCustomNum" type="hidden"/>
	<input id="maxCustomNum" name="ciCustomGroupInfo.maxCustomNum" type="hidden"/>
	<input id="dateType_" name="ciCustomGroupInfo.dateType" type="hidden"/>
	<input id="createTypeId" name="ciCustomGroupInfo.createTypeId" type="hidden"/>
	<input id="updateCycle" name="ciCustomGroupInfo.updateCycle" type="hidden"/>
	<input id="isPrivate" name="ciCustomGroupInfo.isPrivate" type="hidden"/>
	<input id="dataStatus" name="ciCustomGroupInfo.dataStatus" type="hidden"/>
	<input id="isCustomGroupMarket" name="isCustomGroupMarket" type="hidden" value="true"/>
	<input id="createCityId" name="ciCustomGroupInfo.cityId" type="hidden"/>
	<input id="isMyCustom" name="ciCustomGroupInfo.isMyCustom" type="hidden"/>

	<div class="comWidth pathNavBox">
		<span class="pathNavHome inline-block" onclick="forwardGustomGroupMarket()" style="cursor:default; "> 客户群集市</span>
		<span class="pathNavArrowRight inline-block"></span>
		<span class="pathNavNext ellipsis"></span>
		<span class="amountItem inline-block"> 共<span class="amountNum"></span>个客户群 </span>
	</div>
	<!-- 查询条件 -->
	<div class="comWidth queryItemBox">
		<!-- 选择结果 -->
		<div class="selectedItemList">
			<dl>
				<dt class="fleft">您的选择：</dt>
				<dd class="fleft"  id="selectedItemListBox"></dd>
			</dl>
		</div>
	   <!-- 选择条件列表 -->
		<div  class="comWidth selectOptionBox clearfix">
			<div class="selectOptionList">
			    <div class="selectOptionKey">
			      地市:
			    </div>
				<div class="selectOptionValue" id="city">
					<ul>
						<li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);" id="cityIdAll"> 全部</a></li>
						<c:forEach var="city" items="${cityMap}">   
						<li id="city_${city.key }"><a href="javascript:void(0);">${city.value }</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="selectOptionList ">
				<div class="selectOptionKey">
				   客户群分类:
				</div>
				<div class="selectOptionValue" id="sceneName">
					<ul>
						<li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);" id="sceneAll"> 全部</a></li>
						<c:forEach items="${dimScenes }" var="dimScens">
						<c:if test="${dimScens.sceneId > 0 }">
						<li id="sceneName1_${dimScens.sceneId }"><a href="javascript:void(0);"> ${dimScens.sceneName }</a></li>
						</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="selectOptionList">
				<div class="selectOptionKey">
				  创建方式:
				</div>
				<div class="selectOptionValue" id="createTypeName">
					<ul>
						<li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
						<c:forEach items="${customCreateTypes }" var="customCreateType">
						<li id="createTypeName_${customCreateType.createTypeId }"><a href="javascript:void(0);"> ${customCreateType.createTypeName }</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div id="moreSelectOptionList" class="comWidth clearfix hidden">
			<div class="comWidth selectOptionList ">
				<div class="selectOptionKey">
				   客户群规模:
				</div>
				<div class="selectOptionValue" id="customNum">
					<ul>
						<li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
						<li><a href="javascript:void(0);"> 1-1000</a></li>
						<li><a href="javascript:void(0);"> 1000-5000</a></li>
						<li><a href="javascript:void(0);"> 5000-25000</a></li>
						<div></div>
						<li>
							<div id="priceFilter" class="defineSection">
								<input type="text" class="defineDataInput" id="minCustomNum_" />
								<span>—</span>
								<input type="text" class="defineDataInput" id="maxCustomNum_" />
							</div>
						</li>
						<li><div class="customNumDiv" style="height: 20px; width: 60px; margin-left: 6px;"><input type="button" style="display:none;" class="btn_normal" id="numSelf" value="规模筛选" onclick="shaiXuanNum($(this))"/></div></li>
					</ul>
				</div>
			</div>
			<div class="selectOptionList ">
			    <div class="selectOptionKey">
		          	创建时间:
			    </div>
				<div class="selectOptionValue" id="dateType">
					<ul>
						<li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
						<li><a href="javascript:void(0);"> 一天</a></li>
						<li><a href="javascript:void(0);"> 一个月</a></li>
						<li><a href="javascript:void(0);"> 三个月</a></li>
						<li>
							<div id="dateFilter" class="defineSection">
								<input name="ciCustomGroupInfo.startDate" id="startDate" readonly type="text" class="defineDataInput datepickerBG"  
								onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}" 
								value="${ciCustomGroupInfo.startDate}"/> -- 
								<input name=ciCustomGroupInfo.endDate id="endDate" readonly type="text" class="defineDataInput datepickerBG"  
								onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" 
								value="${ciCustomGroupInfo.endDate}"/>
							</div>
						</li>
						<li><div class="dateSelfDiv" style="height: 20px; width: 60px; margin-left: 6px;"><input id="dateSelf" type="button" style="display:none;" class="btn_normal" onclick="shaiXuanDate($(this))" value="时间筛选"/></div></li>
					</ul>
				</div>
			</div>
			<div class="selectOptionList">
				<div class="selectOptionKey">
				  生成周期:
				</div>
				<div class="selectOptionValue" id="updateCycle">
					<ul>
					    <li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
					    <li><a href="javascript:void(0);">一次性</a></li>
						<li><a href="javascript:void(0);">月周期</a></li>
						<li><a href="javascript:void(0);">日周期</a></li>
					</ul>
				</div>
			</div>
			<div class="selectOptionList">
				<div class="selectOptionKey">
				     是否共享:
				</div>
				<div class="selectOptionValue" id="isPrivate">
					<ul>
					    <li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
					    <li><a href="javascript:void(0);">共享</a></li>
						<li><a href="javascript:void(0);">非共享</a></li>
					</ul>
				</div>
			</div>
			<div class="selectOptionList">
			    <div class="selectOptionKey">
			      数据状态:
			    </div>
				<div class="selectOptionValue" id="customDataStatus">
					<ul>
						<li><a class="selectOptionValueHover" selectedElement="all" href="javascript:void(0);"> 全部</a></li>
						<c:forEach items="${customDataStatus }" var="customDataStatus_">
							<li id="customDataStatus_${customDataStatus_.dataStatusId }"><a href="javascript:void(0);"> ${customDataStatus_.dataStatusName }</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div class="moreOptionBox">
			<input type="button"  value="更多条件" id="moreOptionActive" class="hand"/>
		</div>
	</div>
    <!-- 查询结果列表 -->
	<div class="comWidth queryResultBox">
		<div class="queryResultSort">
		     <ol class="fleft clearfix" id="sortList">
			   <li class="defaultSort">
			      <span style="cursor: pointer;" onclick="sortDefault()"> 默认排序</span>
			   </li>
			   <li>
			     <a href="javascript:void(0);" id="useNumOrder" title="点击按人气从大到小排序"> 人气<span></span></a>
			   </li>
			   <li>
			     <a href="javascript:void(0);" id="timeOrder" title="点击按最新修改时间从近到远排序"> 修改时间<span></span></a>
			   </li>
			   <li>
			     <a href="javascript:void(0);" id="nameOrder" title="点击按客户群名称倒序排序"> 客户群名称<span></span></a>
			   </li>
			   <li>
			     <a href="javascript:void(0);" id="numOrder" title="点击按客户群规模从大到小排序"> 客户群规模<span></span></a>
			   </li>
			   <li class="item-segLine"></li>
			   <li>
			   <div id="myCreateCustom" href="javascript:void(0);" style="color: #686868"><input style="margin-bottom: -3px;cursor:pointer;" type="checkbox"/>&nbsp;&nbsp;我创建的客户群</div>
			   </li>
			 </ol>
			<img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/icon_show_mode.png" width="63" height ="27" class=" fright" usemap="#planetmap"  style="display: none;"/>
		</div>
		<div class="comWidth query-list-box" id="queryResultListBox">
		</div>
		<div id="noMoreCustomGroupResults" class="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
		<div id="noResultsCustomGroup" class="noResults" style="display:none;">没有客户群信息...</div>
	</div>
	<!--“增加标签预警窗口”对应的dialog对应的div -->
	<div id="dialog_edit" style="overflow:hidden;">
		<iframe id="add_alarm_iframe" name="add_alarm_iframe" scrolling="no" allowtransparency="true" src=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
	</div>
	<div id="dialog-match" title="系统自动匹配提示" style="display:none;"><!-- "dialog-match" -->
		<div class="dialog_table">
			<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
				<tr>
					<td class="app_td" colspan="2" style="padding-top:0px;padding-bottom:20px;">
						<div id="sysMatchTitle" class="no no_nobg">您未进行系统自动匹配！</div>
						<div id="failSysMatchTitle" class="no" style="display: none">系统自动匹配失败！</div>
					</td>
				</tr>
				<tr class="even">
					<td class="save_td save_text_td" style="text-align:left">
						<li>可选择”跳过“，直接进行手工匹配！</li>
						<li>可选择”系统匹配”，您的策略匹配中会出现<br/>
						如右图“系统推荐”的TAB页哦！</li>
					</td>
					<td width="283px" class="save_text_td" style="text-align:left">
						<img src="${ctx}/aibi_ci/assets/themes/default/images/img4.png" width="263" height="124" />
					</td>
				<tr class="even">
					<td class="save_td" colspan="2">
						<div class="show" >
							系统匹配功能，可能需要您等待几分钟哦！<br/>
							数据处理完毕后，会在<span>系统通知消息</span>中提示您！感谢您的使用！
						</div>
					</td>
				</tr>
				<input type="hidden" id="matchCustomGroupId" />
				<input type="hidden" id="matchListTableName" />
				<input type="hidden" id="matchCustomNum" />
				<input type="hidden" id="matchDataDate" />
			</table>
		</div>
		<!-- 按钮区 -->
		<div class="dialog_btn_div">
			<input name="" type="button" id="sysMatch" value="系统自动匹配" class="tag_btn"/>
			<input name="" type="button" id="customMacth" onclick="customMatch();" value=" 跳 过 " class="tag_btn"/>
		</div>		                
	</div>
	<div id="createTemplateDialog" style="overflow-y:hidden;">
		<iframe id="createTemplateFrame" name="createTemplateFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:280px"></iframe>
	</div>
	<div id="dialog-form" title="导入客户群" style="display:none;overflow-y:hidden;"><!-- dialog-form -->
	  <iframe id="importFile" scrolling="yes" allowtransparency="true" src="" framespacing="0" border="0"	frameborder="0" style="width: 100%; height: 520px;"></iframe>
		<!-- 按钮区 
		<div class="btnActiveBox" style="position: relative;">
			<%
	              if("true".equals(marketingMenu)){
	              %>
			    <input id="isAutoMatch" type="checkbox" class="valign_middle"/> 系统自动匹配产品&nbsp;&nbsp;
			<%
	              }
			%>
			 <input type="button" id="saveBtn" onclick="importInit();" value="保存客户群"  class="ensureBtnLong" />
		</div>-->
	</div><!-- dialog-form end-->
	<!-- dialog-form end-->
</form>

<!-- zhejiang 审批初始页面 -->
<div id="dialog_download_approve" style="overflow:hidden;">
    <iframe name="dialog_download_approve_detail" scrolling="no" allowtransparency="true" src="" id="dialog_download_approve_detail"
            framespacing="0" border="0" frameborder="0" style="width:100%;height:200px"></iframe>
</div>

<!-- zhejiang 审批提交页面 -->
<div id="dialog_commit_approve" style="overflow:hidden;">
    <iframe name="dialog_commit_approve_detail" scrolling="no" allowtransparency="true" src="" id="dialog_commit_approve_detail"
            framespacing="0" border="0" frameborder="0" style="width:100%;height:275px"></iframe>
</div>
