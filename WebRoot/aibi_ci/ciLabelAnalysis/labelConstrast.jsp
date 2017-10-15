<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<%@ include file="/aibi_ci/html_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数据探索</title>

<script type="text/javascript">
var zNodes;
//对比标签数组
var contrastLabels = new Array();
var closeZindex=500;
var dropCount = 0;

var iconHelpHtml1 ='<li class="icon_help" onclick="showcontrast_analyseTip(this)">';
iconHelpHtml1+='<div class="slideDown" style="width:235px;">';
iconHelpHtml1+='<dl>';
iconHelpHtml1+='<dt><span class="close" onclick="hidecontrast_analyseTip(this,event)"></span></dt>';
iconHelpHtml1+='<dd>';
iconHelpHtml1+='<table cellpadding="0" cellspacing="0" class="per_help">';
iconHelpHtml1+='<tr>';
iconHelpHtml1+='<td>';
iconHelpHtml1+='<div>主标签 ∩ 对比标签</div>';
iconHelpHtml1+='<div class="line"></div>';
iconHelpHtml1+='<div>主标签 U 对比标签</div>';
iconHelpHtml1+='</td>';
iconHelpHtml1+='<td width="45" class="align_left per">×100%</td>';
iconHelpHtml1+='</tr>';
iconHelpHtml1+='</table>';
iconHelpHtml1+='</dd>';
iconHelpHtml1+='</dl>';
iconHelpHtml1+='</div>';
iconHelpHtml1+='</li>';

var iconHelpHtml2 ='<li class="icon_help" onclick="showcontrast_analyseTip(this)">';
iconHelpHtml2+='<div class="slideDown" style="width:235px;">';
iconHelpHtml2+='<dl>';
iconHelpHtml2+='<dt><span class="close" onclick="hidecontrast_analyseTip(this,event)"></span></dt>';
iconHelpHtml2+='<dd>';
iconHelpHtml2+='<table cellpadding="0" cellspacing="0" class="per_help">';
iconHelpHtml2+='<tr>';
iconHelpHtml2+='<td>';
iconHelpHtml2+='<div>主标签  - (主标签 ∩ 对比标签 )</div>';
iconHelpHtml2+='<div class="line"></div>';
iconHelpHtml2+='<div>主标签 U 对比标签</div>';
iconHelpHtml2+='</td>';
iconHelpHtml2+='<td width="45" class="align_left per">×100%</td>';
iconHelpHtml2+='</tr>';
iconHelpHtml2+='</table>';
iconHelpHtml2+='</dd>';
iconHelpHtml2+='</dl>';
iconHelpHtml2+='</div>';
iconHelpHtml2+='</li>';

var iconHelpHtml3 ='<li class="icon_help" onclick="showcontrast_analyseTip(this)">';
iconHelpHtml3+='<div class="slideDown" style="width:235px">';
iconHelpHtml3+='<dl>';
iconHelpHtml3+='<dt><span class="close" onclick="hidecontrast_analyseTip(this,event)"></span></dt>';
iconHelpHtml3+='<dd>';
iconHelpHtml3+='<table cellpadding="0" cellspacing="0" class="per_help">';
iconHelpHtml3+='<tr>';
iconHelpHtml3+='<td>';
iconHelpHtml3+='<div>对比标签 -(主标签 ∩对比标签)</div>';
iconHelpHtml3+='<div class="line"></div>';
iconHelpHtml3+='<div>主标签  U 对比标签</div>';
iconHelpHtml3+='</td>';
iconHelpHtml3+='<td width="45" class="align_left per">×100%</td>';
iconHelpHtml3+='</tr>';
iconHelpHtml3+='</table>';
iconHelpHtml3+='</dd>';
iconHelpHtml3+='</dl>';
iconHelpHtml3+='</div>';
iconHelpHtml3+='</li>';




//获取屏幕滚动高度，定位alert位置
function getPageScroll_y(){
	var _y=$(window.parent).scrollTop();
	return _y;
}

$(document).ready(function(){
	//初始化时间轴
	initDate();
	//格式化数字
	format();
	
	showLabelTree("ct");
	//初始化对比展示区域
	initContrastArea();
	
});



//格式化数字
function format(){
	num = accounting.formatNumber('${labelDetailInfo.customNum}');
	$("#customNumLab").text(num);
}

//时间轴初始化
function initDate(){
	//时间轴
	var month = "${newLabelMonth}";
	if(month == ""){
		$.ajax({
			url: "${ctx}/ci/customersManagerAction!findNewestMonth.ai2do",
			type: "POST",
			success: function(result){
				month = result;
				creatDateline($("#dateline_ct"),month,dataDateClick);
			}
		});
	}else{
		creatDateline($("#dateline_ct"),month,dataDateClick);
	}
	
	//获取传入的时间值
	var curDataDate = $('#dataDate').val();
	
	if(curDataDate != month){
		//取消默认的日期选择
		$("li[_value='"+month+"']").removeClass("onchoose");
		//设置传入的日期为选择状态
		$("li[_value='"+curDataDate+"']").addClass("onchoose");
	}
	
}

//切换月份对比区域等待
function contrastDataWait(){
	if(contrastLabels.length>0){
	var mainLabelId = $('#mainLabelId').val();
	var contrastLabelstr = contrastLabels.join(',');
	var dataDate = $('#dataDate').val()
	var actionUrl=$.ctx + "/ci/ciLabelConstrastAnalysisAction!findLabelContrastTempList.ai2do"
	$.ajax({
			type: "POST",
			url: actionUrl,
			async: false,
			dataType: "json",
			data:'&mainLabelId='+mainLabelId+'&dataDate='+dataDate+'&contrastLabelstr='+contrastLabelstr,
			success: function(result){
				if(result.success){
					if(result.contrastLabelList > 0){
						$("#tag_dropable").empty();
						$.each(result.contrastLabelList, function(i,item){  
							var html='<div class="conditionCT block_conditionCT" labelId="'+item.contrastLabelId+'">';
							html+='<div class="contrast_analyse_tagct">';
							html+='<div class="left_bg"><a href="javascript:void(0)" onclick=""></a></div>';
							html+='<div class="midCT">'+item.contrastLabelName+'</div>';
							html+='<div class="right_bg"></div>';
							html+='</div>';
							html+='<ul class="persent"><li class="persent01"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml1+'<li class="persent03"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml2+'<li class="persent02"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml3+'</ul>';
		                    html+='</div>';
		                    var obj=$(html);
							$("#tag_dropable").append(obj);
							var tag_width=obj.find(".contrast_analyse_tagct").width();
							obj.find(".icon_help").css("z-index",closeZindex);
							closeZindex--;
							var old_width=$("#tag_dropable").attr("tag_width");
							if(tag_width>old_width){
								$("#tag_dropable").attr("tag_width",tag_width);
							}
							$("#tag_dropable").find(".contrast_analyse_tagct").width($("#tag_dropable").attr("tag_width"));
							
	                    });
					}
				}else{
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
					//showAlert("主标签在当前时间没有数据","failed");
				}
			}
	});
	}
}

//点击时间轴触发函数
function dataDateClick(){
	//设置时间值
	$('#dataDate').val($(".date_line").attr("_value"));
	
	//更新标签关联用户数
	updateUserNum();
	
	if(contrastLabels.length>0){
		
	contrastDataWait()
	//对比区域有选择的标签
	var mainLabelId = $('#mainLabelId').val();
	var contrastLabelstr = contrastLabels.join(',');
	var dataDate = $('#dataDate').val()
	var actionUrl=$.ctx + "/ci/ciLabelConstrastAnalysisAction!findLabelContrastTemp.ai2do"
	$.ajax({
			type: "POST",
			url: actionUrl,
			dataType: "json",
			data:'&mainLabelId='+mainLabelId+'&dataDate='+dataDate+'&contrastLabelstr='+contrastLabelstr,
			success: function(result){
				if(result.success){
					$("#tag_dropable").empty();
					if(result.contrastLabelList){
						$.each(result.contrastLabelList, function(i,item){  
							var html='<div class="conditionCT block_conditionCT" labelId="'+item.contrastLabelId+'">';
							html+='<div class="contrast_analyse_tagct">';
							html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this,'+item.contrastLabelId+')"></a></div>';
							html+='<div class="midCT">'+item.contrastLabelName+'</div>';
							html+='<div class="right_bg"></div>';
							html+='</div>';							
							html+='<ul class="persent"><li class="persent01">'+item.intersectionRate+'</li>'+iconHelpHtml1+'<li class="persent03">'+item.mainDifferenceRate+'</li>'+iconHelpHtml2+'<li class="persent02">'+item.contrastDifferenceRate+'</li>'+iconHelpHtml3+'</ul>';
		                    html+='</div>';
		                    var obj=$(html);
							$("#tag_dropable").append(obj);
							var tag_width=obj.find(".contrast_analyse_tagct").width();
							obj.find(".icon_help").css("z-index",closeZindex);
							closeZindex--;
							var old_width=$("#tag_dropable").attr("tag_width");
							if(tag_width>old_width){
								$("#tag_dropable").attr("tag_width",tag_width);
							}
							$("#tag_dropable").find(".contrast_analyse_tagct").width($("#tag_dropable").attr("tag_width"));
	                    });
					}
				}else{
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y())
					//showAlert("主标签在当前时间没有数据","failed");
				}
			}
	});
	}
	
}

//更新标签关联用户数
function updateUserNum(){
	var mainLabelId = $('#mainLabelId').val();
	var dataDate = $('#dataDate').val();
	var actionUrl = $.ctx + "/ci/ciLabelAnalysisAction!findLabelBrandUserNum.ai2do?labelId="+mainLabelId+"&dataDate="+dataDate;
	$.ajax({
		url: actionUrl,
	    type: "POST",
		success: function(result){
			flag = result.success;
			if(flag){
				var num = accounting.formatNumber(result.userNum);
				$("#customNumLab").text(num);
				if(result.userNum == '0' || result.userNum == '-' ){
					parent.showAlert("主标签在当前时间没有数据","failed",null,null,500,200,getPageScroll_y());
				}
			}else{
				parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
			}
		}
	});
}

function showLabelTree(who) {
	$('#searchLabelName').val('');
	var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
	$('#labelType').val(who);
	if('ct' == who) {
		$('#ct_tree').addClass('on');
		$('#my_tree').removeClass('on');
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTree.ai2do";
	} else if('my' == who) {
		$('#my_tree').addClass('on');
		$('#ct_tree').removeClass('on');
		//对比标签ID
		var mainLabelId = $('#mainLabelId').val();
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTree.ai2do?type=contrast&mainLabelId="+mainLabelId;
		//actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeHaveTemplate.ai2do";
	} 
	initLabelTree(actionUrl);
}

function searchLabelByName() {
	var labelName = $('#searchLabelName').val();
	labelName = $.trim(labelName);
	var labelType = $('#labelType').val();
	var actionUrl = "";
	if('ct' == labelType) {
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findLabelTreeByName.ai2do";
	} else if('my' == labelType) {
		//对比标签ID
		var mainLabelId = $('#mainLabelId').val();
		actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeByName.ai2do?type=contrast&mainLabelId="+mainLabelId;
		//actionUrl = $.ctx + "/ci/ciLabelInfoAction!findUserLabelTreeHaveTemplateByName.ai2do";
	}
	initLabelTreeByName(actionUrl,labelName);
}

function initLabelTree(actionUrl) {
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.treeList;
			}else{
				parent.showAlert(result.msg,"failed",null,null,500,200,getPageScroll_y());
			}
			//$("#content").append(result.labelName);
		}
	});

	initTree();
}

function initLabelTreeByName(actionUrl,labelName) {
	$.ajax({
		type: "POST",
		url: actionUrl,
		async: false,
		data: ({"labelName" : labelName}),
		dataType: "json",
		success: function(result){
			if(result.success){
				zNodes = result.treeList;
			}else{
				parent.showAlert(result.msg,"failed",null,null,500,200,getPageScroll_y());
			}
			//$("#content").append(result.labelName);
		}
	});
	
	initTree();
}

//树节点初始化
function initTree(){
	//初始化第一级node的样式
	$.fn.zTree.init($("#analyse_tree"), setting, zNodes);
	zTree_Menu = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodes_0=zTree_Menu.getNodes()[0];
	curMenu = nodes_0;
	if(!curMenu){
		return;
	}
	while(curMenu.children&&curMenu.children.length > 0){
		curMenu = curMenu.children[0];
	}
	
	if(curMenu.click != false){
		zTree_Menu.selectNode(curMenu);
	}else{
		$("#" + nodes_0.tId + "_a").click();
	}
	$("#" + curMenu.tId + "_a").click();
	var a = $("#" + nodes_0.tId + "_a");
	a.addClass("cur");
	var par=a.parent();
	par.addClass("cur_parent");
	par.parent().find("> li.level0").each(function(){
		$(this).find("> a.level0").prepend("<div class='slideIcon'></div>");
	});
	
	//标签拖动
	$("#analyse_tree").mouseover(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		e=e||window.event;
		var tar=$(e.target||e.srcElement);
		var canDrag = 0;
		if(!tar.hasClass("treenode") || tar.hasClass("ui-draggable") || tar.hasClass("level0")) return;
		var tagId = parseInt(tar[0].id.replace(/analyse_tree_/,"").replace(/_a/,""));
		var nodeObj = zTree_Menu.getNodeByTId("analyse_tree_"+tagId);
		if(nodeObj.param.isCanDrag != null || nodeObj.param.isCanDrag != undefined){
			canDrag = nodeObj.param.isCanDrag;
		}
		if(canDrag == 0){
			$("#"+tar[0].id).css({"cursor":"default"});
			$("#"+tar[0].id).find(".button").css({"cursor":"default"});
			return;
		}
		var dropTar=$("#tag_dropable").parent();
		tar.draggable({ 
			revert: "invalid", 
			helper: "clone" ,
			start: function() {
				var startY=dropTar.offset().top;
				var fix=$(window.parent).scrollTop()-startY;
				var posy=$(this).offset().top;
				var margin=posy-startY-dropTar.height()/2;
				fix>0?dropTar.css("margin-top",margin):"";
			},
			stop:function(){
				dropTar.css("margin-top",8)
			}
		});
	})
	
	//$("#xxxxxxxxxxx").draggable({ revert: "invalid", helper: "clone" });
	//释放标签
	dropTag();
	autoHeight();
}

//等待查询结果返回页面
function contrastAreaWait(){
	var mainLabelId = $('#mainLabelId').val();
	var dataDate = $('#dataDate').val();
	var actionUrl=$.ctx + "/ci/ciLabelConstrastAnalysisAction!findLabelContrastList.ai2do";
	
	$.ajax({
			type: "POST",
			url: actionUrl,
			dataType: "json",
			async: false,
			data:'&mainLabelId='+mainLabelId+'&dataDate='+dataDate,
			success: function(result){
				if(result.success){
					var contrastLabelArray = eval(result.contrastLabelInfos);
					if(contrastLabelArray&&contrastLabelArray.length>0){
						$("#tag_dropable").empty();
						$("#drag_tip").hide();
					}
					jQuery.each(result.contrastLabelInfos, function(i,item){  
						var html='<div class="conditionCT block_conditionCT" labelId="'+item.labelId+'">';
						html+='<div class="contrast_analyse_tagct">';
						html+='<div class="left_bg"><a href="javascript:void(0)" onclick=""></a></div>';
						html+='<div class="midCT">'+item.labelName+'</div>';
						html+='<div class="right_bg"></div>';
						html+='</div>';
						html+='<ul class="persent"><li class="persent01"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml1+'<li class="persent03"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml2+'<li class="persent02"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml3+'</ul>';
	                    html+='</div>';
	                    var obj=$(html);
						$("#tag_dropable").append(obj);
						var tag_width=obj.find(".contrast_analyse_tagct").width();						
						obj.find(".icon_help").css("z-index",closeZindex);
						closeZindex--;						
						var old_width=$("#tag_dropable").attr("tag_width");
						if(tag_width>old_width){
							$("#tag_dropable").attr("tag_width",tag_width);
						}
						$("#tag_dropable").find(".contrast_analyse_tagct").width($("#tag_dropable").attr("tag_width"));					
                    });  
					
				}else{
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());

				}
			}
	   });
	
}


//初始化展示区域
function initContrastArea(){
	var mainLabelId = $('#mainLabelId').val();
	var dataDate = $('#dataDate').val();
	//updateUserNum();
	//alert(mainLabelId);
	var actionUrl=$.ctx + "/ci/ciLabelConstrastAnalysisAction!findLabelContrast.ai2do";
	
	//展示等待区域
	contrastAreaWait();
	
	$.ajax({
			type: "POST",
			url: actionUrl,
			dataType: "json",
			data:'&mainLabelId='+mainLabelId+'&dataDate='+dataDate,
			success: function(result){
				if(result.success){
					var contrastLabelArray = eval(result.contrastLabelList);
					if(contrastLabelArray&&contrastLabelArray.length>0){
						$("#tag_dropable").empty();
						$("#drag_tip").hide();
					}
					if(result.contrastLabelList){
						$.each(result.contrastLabelList, function(i,item){  
							contrastLabels.push(item.contrastLabelId);
							var html='<div class="conditionCT block_conditionCT" labelId="'+item.contrastLabelId+'">';
							html+='<div class="contrast_analyse_tagct">';
							html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this,'+item.contrastLabelId+')"></a></div>';
							html+='<div class="midCT">'+item.contrastLabelName+'</div>';
							html+='<div class="right_bg"></div>';
							html+='</div>';
							html+='<ul class="persent"><li class="persent01">'+item.intersectionRate+'</li>'+iconHelpHtml1+'<li class="persent03">'+item.mainDifferenceRate+'</li>'+iconHelpHtml2+'<li class="persent02">'+item.contrastDifferenceRate+'</li>'+iconHelpHtml3+'</ul>';
		                    html+='</div>';
		                    var obj=$(html);
							$("#tag_dropable").append(obj);
							var tag_width=obj.find(".contrast_analyse_tagct").width();
							obj.find(".icon_help").css("z-index",closeZindex);
							closeZindex--;
							var old_width=$("#tag_dropable").attr("tag_width");
							if(tag_width>old_width){
								$("#tag_dropable").attr("tag_width",tag_width);
							}
							$("#tag_dropable").find(".contrast_analyse_tagct").width($("#tag_dropable").attr("tag_width"));
							
							
	                    });
						$("#saveConstrastButton").unbind("click").bind("click",function(){saveContrastLabel();});
					}
				}else{
					parent.showAlert(result.message,"failed",null,null,500,200,getPageScroll_y());
				}
			}
	});
}

//释放正在拖动的标签
function dropTag(){
	//伸展开以后的条件区域
	$("#tag_dropable").droppable({
		accept: "#analyse_tree a",
		greedy:true,
		drop: function( event, ui ) {
			$(this).find(".drag_tip").hide();
			var onDragTag=ui.draggable;
			tagOnDrop(onDragTag,$(this));
		}
	});
}

function tagOnDrop(dragtag,ct){
	var tagId = parseInt(dragtag[0].id.replace(/analyse_tree_/,""));
	var treeObj = $.fn.zTree.getZTreeObj("analyse_tree");
	var nodeObj = treeObj.getNodeByTId("analyse_tree_"+tagId);
	/*
	//运算符拼接，对比分析关联分析不需要
	var _chaining='<div class="chaining">';
	_chaining+='<div class="left_bg"></div>';
	_chaining+='<div class="midCT">';
	_chaining+='<a class="icon_rightArr" href="javascript:void(0)" onclick="switchConnector(this,event)">且</a>';
	_chaining+='</div>';
	_chaining+='<div class="right_bg"></div>';
	_chaining+='</div>';
	*/
	
	//最多 10 个标签对比
	if(contrastLabels.length>=5){
		parent.showAlert("最多只能与5个标签进行对比分析","failed",null,null,500,200,getPageScroll_y());	
		  return false;		
	}
	
	var mainLabelId = $('#mainLabelId').val();
	if(nodeObj.id == mainLabelId){
		parent.showAlert("对比标签不能为主标签！","failed",null,null,500,200,getPageScroll_y());
		return false;
	}
	
	var isRepeat = false;
	//判断是否已经进行过对比分析
	$.each(contrastLabels,function(key,value){
		   if(value == nodeObj.id){
			   parent.showAlert('标签 '+nodeObj.name+' 不能重复对比！',"failed",null,null,500,200,getPageScroll_y());
			   isRepeat = true;
			   return false;
		   }
	});
	
	if(isRepeat){
		//该标签已经对比分析
		return;
	}else{
		contrastLabels.push(nodeObj.id);
	}
	
	var _txt=dragtag.find("span").eq(1).html();
	var html='<div class="conditionCT block_conditionCT" labelId="'+nodeObj.id+'">';
	html+='<div class="contrast_analyse_tagct">';
	html+='<div class="left_bg"><a href="javascript:void(0)" onclick="deleteThis(this,'+nodeObj.id+')"></a></div>';
	html+='<div class="midCT">'+_txt+'</div>';
	html+='<div class="right_bg"></div>';
	html+='</div>';	
	html+='<ul id="persent_'+nodeObj.id+'" class="persent"><li class="persent01"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml1+'<li class="persent03"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml2+'<li class="persent02"><img alt="" src="${ctx}/aibi_ci/assets/images/ajax-loader.gif"></li>'+iconHelpHtml3+'</ul>';
	html+='</div>';	
	var obj=$(html);
	ct.append(obj);
	var tag_width=obj.find(".contrast_analyse_tagct").width();
	
	//var old_width=ct.attr("tag_width")||0;
	var showed_maxWidth=0;
	ct.find(".contrast_analyse_tagct").each(function(){
		$(this).width("auto");
		if($(this).width()>showed_maxWidth) showed_maxWidth=$(this).width();
	})
	ct.find(".contrast_analyse_tagct").width(showed_maxWidth);


	dropCount++;
	$("#saveConstrastButton").addClass("tag_btn_disable").removeAttr("title").attr("title","对比分析结果产生后才可以保存");
	$("#saveConstrastButton").unbind("click");	
	var mainLabelId = $('#mainLabelId').val();
	var contrastLabelId = nodeObj.id;
	var dataDate = $('#dataDate').val();
		var param = "?mainLabelId=" + mainLabelId;
			param = param + "&contrastLabelId=" + contrastLabelId;
			param = param + "&dataDate=" + dataDate;
		var actionUrl = $.ctx + "/ci/ciLabelConstrastAnalysisAction!findLabelContrastDetail.ai2do" + param;
		
		var intersectionRate = '0%';
		var mainDifferenceRate = '0%';
		var contrastDifferenceRate = '0%';
		
		$.ajax({
			type: "POST",
			url: actionUrl,
			async: true,
			dataType: "json",
			success: function(result){
				if(result.success){
					intersectionRate = result.contrastDetailInfo.intersectionRate;
					mainDifferenceRate = result.contrastDetailInfo.mainDifferenceRate;
					contrastDifferenceRate = result.contrastDetailInfo.contrastDifferenceRate;
					$('#persent_'+contrastLabelId).html('<li class="persent01">'+intersectionRate+'</li>' +iconHelpHtml1+'<li class="persent03">'+mainDifferenceRate+'</li>'+iconHelpHtml2+'<li class="persent02">'+contrastDifferenceRate+'</li>'+iconHelpHtml3);
					obj.find(".icon_help").css("z-index",closeZindex);
					closeZindex--;
				}else{
					parent.showAlert("主标签与对比标签对比失败！","failed",null,null,500,200,getPageScroll_y());
				}
				
				dropCount--;
				if(dropCount == 0){
					$("#saveConstrastButton").removeClass("tag_btn_disable").removeAttr("title");
					$("#saveConstrastButton").unbind("click").bind("click",function(){saveContrastLabel();});
				}
				//$("#content").append(result.labelName);
			}
		});
	
	//html+='<ul class="persent"><li class="persent01">'+intersectionRate+'</li><li class="persent03">'+mainDifferenceRate+'</li><li class="persent02">'+contrastDifferenceRate+'</li></ul>';
	//html+='</div>';	
	
	//if(ct.find(".conditionCT").length==0){
	//ct.append(html);
	//}else{
		//ct.append(_chaining+html);
	//}
}

function deleteThis(obj,labelId){
	//if($(obj).parent().parent().siblings().length == 0){
		//$("#saveConstrastButton").addClass("tag_btn_disable").removeAttr("title").attr("title","对比分析结果产生后才可以保存");
	//$("#saveConstrastButton").unbind("click");
	//}
	$(obj).parent().parent().parent().remove();
	//全局对比标签数组处理
	for (var i = 0; i < contrastLabels.length; i++){
		if(contrastLabels[i] == labelId){
			contrastLabels.splice(i,1);
		}
	}
	//删除对比关系
}

//ztree
var curMenu = null, zTree_Menu = null;
var setting = {
	view: {
		showLine: true,
		selectedMulti: false,
		dblClickExpand: false
	},
	data: {
		key: {
			title: "tip"
		},
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: this.beforeClick,
		onClick: this.onClick
	}
};

//点击节点前判断是否第一级,不同展示方式
function beforeClick(treeId, node) {
	if(node.click != false){
		//恢复原有样式
		var icon = $(".curSelectedNode").find(".button");
		if(icon.css("background")){
			var bg = icon.css("background").replace("_on","");
			icon.css("background", bg);
		}
	}
	
	if (node.isParent) {
		if (node.level === 0) {
			var pNode = curMenu;
			while (pNode && pNode.level !==0) {
				pNode = pNode.getParentNode();
			}
			if (pNode !== node) {
				var a = $("#" + pNode.tId + "_a");
				a.removeClass("cur");
				zTree_Menu.expandNode(pNode, false,true,true,true);
			}
			a = $("#" + node.tId + "_a");
			a.addClass("cur");
			var cur_parent=a.parent();
			var open_close=true;
			if(cur_parent.hasClass("cur_parent")){
				cur_parent.removeClass("cur_parent");
				open_close=false;
			}else{
				cur_parent.addClass("cur_parent").siblings(".cur_parent").removeClass("cur_parent");
			}
			var isOpen = false;
			if(node.children){
				for (var i=0,l=node.children.length; i<l; i++) {
					if(node.children[i].open) {
						isOpen = true;
						break;
					}
				}	
			}else{
				isOpen = true;
			}
			if (isOpen) {
				zTree_Menu.expandNode(node, open_close);
				curMenu = node;
			} else {
				zTree_Menu.expandNode(node.children[0].isParent?node.children[0]:node, open_close,true,true,true);
				curMenu = node.children[0];
			}
			
		} else {
			//非叶子节点的点击展开功能屏蔽，否则和点击事件同时调用，展开节点很慢，可以通过点击非叶子节点前的加号展开下一级节点
			//zTree_Menu.expandNode(node,true,true,true,true);
		}
	}
	//如果返回return !node.isParent，那么就会有false的情况，根据ztree的api当返回false时，onClick事件就会失效
	//return true;
	return (node.click != false);
}

//节点点击事件
function onClick(e, treeId, node) {
	if ((node.isParent && node.level !== 0) || !node.isParent) {
		initClick();
	}
}

//初始化节点选中事件
function initClick() {
	var icon = $(".curSelectedNode").find(".button");
	var bg = icon.css("background");
	var last = bg.lastIndexOf("/");
	var right = bg.lastIndexOf(")");
	var begin = bg.substring(0, last+1);
	var middle = bg.substring(last+1, right);
	var iconExt = middle.substring(middle.indexOf("."), middle.length);
	var iconName = middle.substring(0, middle.indexOf(".")) + "_on" + iconExt;
	var end = bg.substring(right, bg.length);
	var newBg = begin + iconName + end;
	icon.css("background", newBg);
}

function saveContrastLabel(){
	if(contrastLabels.length<=0){
		parent.showAlert("请选择对比标签！","failed",null,null,500,200,getPageScroll_y());
		return;
	}
	var contrastLabelstr = contrastLabels.join(',');
	var mainLabelId = $('#mainLabelId').val();
	var actionUrl=$.ctx + "/ci/ciLabelConstrastAnalysisAction!saveLabelContrast.ai2do"
	$.ajax({
			type: "POST",
			url: actionUrl,
			data:'&mainLabelId='+mainLabelId+'&contrastLabelstr='+contrastLabelstr,
			success: function(result){
				if(result.success){
					//隐藏标签对比区域
					$('#contrastDiv').hide();
					//展示标签对比分析区域
					var dataDate =  $('#dataDate').val();
					var url = $.ctx + "/ci/ciLabelConstrastAnalysisAction!initLabelContrastSave.ai2do?mainLabelId="+mainLabelId+"&dataDate="+dataDate;
					window.location.href = url;
				}else{
					parent.showAlert("标签对比分析保存失败","failed",null,null,500,200,getPageScroll_y());
				}
			}
	});
}

function toLabelRel() {
	var mainLabelId = $('#mainLabelId').val();
	var dataDate = $('#dataDate').val();
	
	var param = "ciLabelRelModel.mainLabelId="+mainLabelId;
	param = param + "&ciLabelRelModel.dataDate="+dataDate;
	var url = $.ctx+"/ci/ciLabelRelAnalysisAction!toLabelRelAnalysis.ai2do?"+param;
	window.location.href = url;
}
//自动高度
function autoHeight(){
	var _autoheight = $(".analyse_left").height();
	var _autoheight2 = $(".analyse_right").height();
	if(_autoheight2 > _autoheight){
		_autoheight = _autoheight2;
	}
	var contentFrame = $("iframe:visible",window.parent.document);
	contentFrame.height(_autoheight+50);
}

function hidecontrast_analyseTip(t,e){
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$(t).parents(".slideDown").hide();
}
function showcontrast_analyseTip(t){
	$(t).find("div.slideDown").slideDown("fast");
}

</script>
<style type="text/css">
</style>
</head>

<body class="body_bg">

<input id="dataDate" type="hidden" value=${labelDetailInfo.dataDate } />
<div id="contrastDiv">
<div class="analyse_left">
<ul class="analyse_tab">
	<input id="labelType" type="hidden" value="ct" />
	<li class="on" id="ct_tree" onClick="showLabelTree('ct');">标签库</li>
	<li class="noborder" id="my_tree" onClick="showLabelTree('my');">我的标签库</li>
</ul>

<div class="analyse_tab_ct" id="analyse_tab_ct">
<div class="analyse_date">
<table class="commonTable">
	<tr>
		<td>
		<div class="analyse_search"><input type="text"
			id="searchLabelName" /> <span class="icon_search"
			onClick="searchLabelByName();"></span></div>
		</td>
	</tr>
</table>
</div>

<div class="tree_area">
<ul id="analyse_tree" class="ztree"></ul>
</div>
<!--tree_area end --></div>
<!--analyse_tab_ct end --></div>
<!--analyse_left end -->

<div class="analyse_right" style="margin-top:0px">
<dl class="analyse_right_dl marginTop_0">
	<dt><span>日期选择</span></dt>
	<dd>
	<table class="commonTable mainTag_show">
		<tr>
			<td id="dateline_ct"></td>
			<td width="150"><font class="green_num bold"><label
				id="customNumLab"></label></font>
			<p>用户数</p>
			</td>
		</tr>
		<input type="hidden" id="mainLabelId" value=${labelDetailInfo.labelId } />
	</table>
	</dd>
</dl>

<dl class="analyse_right_dl">
	<dt class="tuli">
		<span class="title">主标签(<font style='font-weight:bold;'>${labelDetailInfo.labelName}</font>)的对比分析展示</span>
		<span>图例所示区域的用户占比</span>
		<span class="dot"></span></dt>
	<dd id="tag_dropable" class="tag_analyse_ct tag_dropable" >
	<div id="drag_tip" class="drag_tip"></div>
	</dd>

</dl>

<div class="analyse_right_btns">
	<input type="button" id="saveConstrastButton" class="tag_btn" value="保存"  />
</div>
</div>
<!--analyse_right end --></div>
<div id="contrastResultDiv" style="display: none;"></div>

</body>
</html>
