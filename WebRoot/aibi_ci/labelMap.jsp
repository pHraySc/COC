<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String labelNumber = Configure.getInstance().getProperty("LABEL_NUMBER");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>标签地图</title>
<script type="text/javaScript" src="${ctx}/aibi_ci/assets/js/map_scrollpagination.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var actionUrl = $.ctx + "/ci/ciIndexAction!getFirstLevelLabelInfoList.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		data: {},
		success: function(result){
			if(result.length == 0){
				$("#noResults").show();
				$("#loading").hide();
			}
			var html = '<ul> ';
			var number = 0;
			$.each(result, function(i, record) {
				number = number + record.labelNum + 1;	
				var content;
				var className;
				if(record.labelName == "基础标签"){
					className = "no1";
				}else if(record.labelName == "电信标签"){
					className = "no5";
				}else if(record.labelName == "生活标签"){
					className = "no6";
				}
				if(i == 0) {
					//if(record.labelName.length > 4){
						//content = "<li class=\"big top_menu_on top_menu_bon\" style=\"width:180px\"><a href=\"javascript:void(0)\" onclick=\"showChildrenOfFirstL('"+record.labelId+"',this)\" class=\""+className+"\" style=\"width:180px\"><span></span><p style=\"width:140px\">"+record.labelName+"</p></a></li>";
					//}else{
						content = "<li class=\"top_menu_on\"><div class='bgleft'>&nbsp;</div><a href=\"javascript:void(0)\" onclick=\"showChildrenOfFirstL('"+record.labelId+"',this)\" class=\""+className+"\"><span></span><p>"+record.labelName+ "<%if("true".equals(labelNumber)){ %>";
						content += "(" + record.labelNum + ")" +  "<%}%>" + "</p></a><div class='bgright'>&nbsp;</div></li>";
					//}
					var id = record.labelId;
					showChildrenOfFirstL(id);
				} else {
					//if(record.labelName.length > 4){
						//content = "<li class=\"big\" style=\"width:180px\"><a href=\"javascript:void(0)\" onclick=\"showChildrenOfFirstL('"+record.labelId+"',this)\" class=\""+className+"\" style=\"width:180px\"><span></span><p style=\"width:140px\">"+record.labelName+"</p></a></li>";
					//}else{
						content = "<li><div class='bgleft'>&nbsp;</div><a href=\"javascript:void(0)\" onclick=\"showChildrenOfFirstL('"+record.labelId+"',this)\" class=\""+className+"\"><span></span><p>"+record.labelName + "<%if("true".equals(labelNumber)){ %>";
						content += "(" + record.labelNum + ")" + "<%}%>" + "</p></a><div class='bgright'>&nbsp;</div></li>";
					//}
				}
				html += content;
			});
			$("#number",parent.document).html(number);
			html += '<div class="clear"></div> </ul>';
			$("#top_menu").append(html);
		}
	});
	
	//标签鼠标滑过加背景色
	$(document).on("mouseover", ".tag_List .isCanDrag", function(){
		$(this).addClass("hover");
	}).on("mouseout", ".tag_List .isCanDrag", function(){
		$(this).removeClass("hover")
	});

});

function showChildrenOfFirstL(firstLevId,obj) {
	
		$(obj).parent().parent("ul").find("li").removeClass("top_menu_on");
	
	if($(obj).parent().hasClass("big")){
		$(obj).parent().addClass("top_menu_on");
	}else{
		$(obj).parent().addClass("top_menu_on");
	}
	var secondLLabelCount;
	var getChildrenUrl = $.ctx + "/ci/ciIndexAction!getChildrenLabelById.ai2do?labelId="+firstLevId;
	$.ajax({
		type: "POST",
		url: getChildrenUrl,
		data: {},
		success: function(result){
			var html = '';
			$.each(result, function(i, record) {
				var content;
				if(i == 0) {
					content = "<div class=\"tag_padding\"></div>";
					content += "<div class=\"tag\" onclick=showChildrenOfSecondL("+record.labelId+",this)>";
			        content +=     "<div class=\"bg_t\"></div>";
			        content +=     "<p>"+record.labelName+"</p>";
			        content +=     "<div class=\"bg_b\"></div>";
			        content += "</div>";
			        //showChildrenOfSecondL(record.labelId);		
					}
				else {
					content = "<div class=\"tag_padding\"></div>";
					content += "<div class=\"tag\" onclick=showChildrenOfSecondL("+record.labelId+",this)>";
			        content +=     "<div class=\"bg_t\"></div>";
			        content +=     "<p>"+record.labelName+"</p>";
			        content +=     "<div class=\"bg_b\"></div>";
			        content += "</div>";
					}
				html += content;
			});
			$("#tag_curtain_CT").empty();
			$("#tag_curtain_CT").append(html);
			//标签拉绳效果
			setTagPos();
			$(window).bind("resize",setTagPos);
		}
	});
	$("#aibi_area_data").empty();
	showAllEffictiveLabel(firstLevId);
}
//当用户在标签地图中点击某个二级标签，获取该二级标签及其所有后代标签
function showChildrenOfSecondL(secondLevId,obj) {
	var _p=$(obj).prev(".tag_padding");
	if($(obj).hasClass("click_active")) return;
	_p.addClass("tag_padding_active").siblings(".tag_padding_active").removeClass("tag_padding_active");
	var _this=$(obj);
	
	var _beginHeight=_p.height();
	_p.animate({height: (_beginHeight+20)+"px"}, 180,function(){
		_p.animate({height: _beginHeight+"px"}, 180);
	});
	$(obj).animate({top: (_beginHeight+20)+"px"}, 180,function(){
		_this.addClass("tag_active click_active").siblings(".tag_active,.click_active").removeClass("tag_active click_active");
		$(obj).animate({top: _beginHeight+"px"}, 180,function(){
			$("#aibi_area_data").empty();
			$("#noMoreResults").hide();
			$("#loading").fadeIn();
			//参考下面 showAllEffictiveLabel方法中的注释
			$(window).unbind("scroll");
		    var actionUrl = $.ctx + "/ci/ciIndexAction!getSecondLLabelTree.ai2do?labelId="+secondLevId;
			$.ajax({
				type : 'POST',
				url : actionUrl,
				data : {},
				success : function(data) {	
					//1-实现标签重组
					var aibi_area_data=$("#aibi_area_data");
					var tempct=$("<div></div>");
					tempct.append(data);
					tempct.find(".tag_List dd").not(".regroup_done").each(function(){
						var nodata=$(this).find("td.needCrush");
						if(nodata.length>0){
							var newrow=$('<tr class="last" width="100%"><td colspan="2" width="100%"><ul class="tag_List_ul"></ul></td></tr>');
							var _table=$(this).find(".commonTable");
							nodata.each(function(){
								var li_string=$(this).prev().html().replace(/span/ig,"li");
								var _li=$(li_string);
								/*
								//找到用css的解决方法，暂注释掉
								if(userBrowser()=="IE"){
									//ie7下有的标签超长
									if(parseInt(userBrowserVer())==7){
										_table.find("th").not(".floatTitle").each(function(){
											var _td=$(this).next("td");
											var w_thtd=$("#aibi_area_data").width()-16;
											_td.attr("width",w_thtd-164);
											$(this).width("auto").attr("width",164)
										})
									}
								}
								*/
								
								newrow.find("ul").addClass("regroup_ul").append(_li);
								
								if($(this).siblings(".floatTitle").length>0){
									$(this).prev().remove();
									$(this).remove()
								}else{
									$(this).parent("tr").remove();
								}
								
							});
							$(this).addClass("regroup_done");
							_table.find("tr:last").removeClass("last");
							_table.append(newrow);
							var len=_table.find("tr").length;
							_table.find("tr").eq(0).find("th.floatTitle").attr("rowspan",len);
						}
					})
					aibi_area_data.append(tempct.html());
					
					//2-标签拖动
					$(".isCanDrag").not(".draggable").draggable({ revert: "invalid", helper: "clone" });
					
					$("#loading").fadeOut("normal", function(){						
						$("#noMoreResults").fadeIn();
					});

					var dls=aibi_area_data.find("dl");
					dls.each(function(){
						$(this).find("> dd").each(function(){
							$(this).find("> table tr ").last().addClass("last");
						});
						$(this).find("> dd").last().addClass("last");
					});
					//$("#aibi_area_data").stopScrollPagination();
					$(".isCanDrag").first().find("a").attr("id","firstLable_tooltip");
				},
				dataType : 'html'
			});
		});
		
	});
	
	
}
//展示三、四、五级标签地图

function showAllEffictiveLabel(firstLevId) {
	//当第一次加载完成之后会显示该div，所以每次展示标签地图之前都需要初始化为不可显示。
	$("#noMoreResults").hide();
	//已经在scrollpagination.js 中调用绑定事件之前做了解除之前的绑定，但是在满足特定条件下，点击第一级标签时，
	//会首先去触发滚动事件（估计是后面的某块代码修改了页面中的内容，内容改变后有些情况下会自动触发滚动条事件），然后才
	//执行scrollPagination方法做新的绑定。但是这样不是我们想要的，所以就在触发scorll之前先解除之前的绑定。
	$(window).unbind("scroll");
	//设置标签地图页数为全局变量
	var totalPage;
	//需要获取总页数
	var getTotalUrl = $.ctx + "/ci/ciIndexAction!queryLabelMapTotalPage.ai2do?labelId="+firstLevId;
	$.ajax({
		type: "POST",
		url: getTotalUrl,
		data: {},
		success: function(result) {
			totalPage = result;
			var i = totalPage;
			var acUrl = $.ctx + "/ci/ciIndexAction!getAllEffictiveLabelByFirstL.ai2do?labelId="+firstLevId+"&totalPage="+totalPage;
			var aibi_area_data=$("#aibi_area_data");
			aibi_area_data.scrollPagination({
		        'contentPage': acUrl, // the url you are fetching the results
		        'contentData': {}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
		        'scrollTarget': $(window),// who gonna scroll? in this example, the full window
		        'heightOffset':10,	
		        'currentPage': 1,
		        'totalPage': totalPage,
		        "beforeLoad" : function() { // before load function, you can display a preloader div
					$("#loading").fadeIn();
				},
				"afterLoad" : function(elementsLoaded) { // after loading content, you can use this function to animate your new elements

					//整理空白标签到一行显示,已改为在scrollpagination.js执行
					//regroup();
					
					//标签详细信息
					var tipsWidth=($("#aibi_area_data").eq(0).width()+2)*0.985;
					$("#tag_tips").width(tipsWidth);
					
					//标签拖动
					$(".isCanDrag").not(".draggable").draggable({ revert: "invalid", helper: "clone" });
				
					//当由于加载的第一页内容太少而没有出现滚动条时，自动触发滚动事件，加载下一页。
					if ($(window).height() >= $(document).height()){					
						$(window).scroll();
					}
					var dls=aibi_area_data.find("dl");
					dls.each(function(){
						$(this).find("> dd").each(function(){
							$(this).find("> table tr ").last().addClass("last");
						});
						$(this).find("> dd").last().addClass("last");
					});
					$(elementsLoaded).fadeInWithDelay();
					i--;
					//i<=0表示的是最后一个页面，只有当加载完最后一个页面时才显示 noMoreResults	
					if(i<=0){
						$("#loading").fadeOut("normal", function(){						
							if($("#aibi_area_data").attr("scrollPagination") == "disabled"){
								$("#noMoreResults").fadeIn();
							}
						});
					}else{
						$("#loading").fadeOut("normal", function(){});
					}
					$(".isCanDrag").first().find("a").attr("id","firstLable_tooltip");
				}
			});
		}
	});
}

//标签详情
function showTip(e,ths){
	var e=e||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$("#tag_tips .onshow").hide().removeClass("onshow");
	var _p=$(ths);
	_p.find("a").blur();
	if(_p.hasClass("tagChoosed")){
		_p.removeClass("tagChoosed");
		return;
	}else{
		$(".tagChoosed").removeClass("tagChoosed");
	}
	_p.addClass("tagChoosed");
	var posy=_p.offset().top+_p.height();
	var posx=_p.parents("dd").offset().left-1;
	//减去左侧菜单的宽度
	var iconx=_p.offset().left+_p.find("a").width()/2-8-$(".menulist").width();
	var tipCT=$("#tag_tips");
	tipCT.css({"top":posy,"left":posx});
	var updateCycle = "";
	if($(ths).attr("updateCycle") == 1){
		updateCycle = "每日";
	}else if($(ths).attr("updateCycle") == 2){
		updateCycle = "每月";
	} 
	
	<%--cusNumFlag查看详情中用户数避免多次查询，0:第一次去后台查询 1：直接用第一次查询的结果--%>
	if($(ths).attr("cusNumFlag") == "0"){
		var url = $.ctx + '/ci/ciIndexAction!getLabelInfoCustomNum.ai2do';
		$.ajax({
			url: url,
			type: "POST",
			async: false,
			data:'&labelId=' + $(ths).attr("element") + '&customNum=' + $(ths).attr("customNum") ,
			success: function(result){
				if(result.success){
					privCustomNum =  result.retCustomNum;
					$(ths).attr("cusNumFlag","1");
					$(ths).attr("customNum",result.retCustomNum);
					if(result.busiCaliber){
						$(ths).attr("busiCaliber",result.busiCaliber);
					}else{
						$(ths).attr("busiCaliber","");
					}
					if(result.applySuggest){
						$(ths).attr("applySuggest",result.applySuggest);
					}else{
						$(ths).attr("applySuggest","");
					}
				}else{
					showAlert("获取首页标签地图showTip展示详细信息时出错","failed");
				}
			}
		});
	}
	var privCustomNum = $(ths).attr("customNum");
	var html = '<div class="tips">' + 
				'<div class="icon"></div>' + 
					'<div class="ct">' + 
						'<table class="commonTable">' + 
							'<tr>' + 
								'<th>标签名称：</th>' +
								'<td><font color="#0d69b8">' + $(ths).attr("labelName") + '</font></td>' +
								'<th>用户数：</th>' +
								'<td>' + $.formatNumber(privCustomNum, 3, ',') + '</td>' +
							'</tr>' +
							'<tr>' +
								'<th>业务口径：</th>' +
								'<td colspan=3>' + $(ths).attr("busiCaliber") + '</td>' +
							'</tr>' +
							'<tr>' +
								'<th>更新周期：</th>' +
								'<td colspan=3>' + updateCycle +'</td>' +
							'</tr>' +
							'<tr>' +
								'<th>应用建议：</th>' +
								'<td colspan=3>' + $(ths).attr("applySuggest") +'</td>' +
							'</tr>' +
						'</table>' +
						'<div class="info_panel">' +
							'<ul>' +
								'<li class="icon07" element="' + $(ths).attr("element") + 
								'" labelName="'+ $(ths).attr("labelName") +
								'" min="' + $(ths).attr("min") + 
								'" max="' + $(ths).attr("max") + 
								'" elementType="' + $(ths).attr("elementType") + 
								'" effectDate="' + $(ths).attr("effectDate") +
								'" updateCycle="' + $(ths).attr("updateCycle") +
								'" attrVal="' + $(ths).attr("attrVal") +
								'" type="' + $(ths).attr("type") + '" onclick="addTag(this);"><a href="javascript:void(0)">添加</a></li>';
								html +=
									   '<li class="icon_tag" title="标签分析" onclick="openLabelAnalysis('+ $(ths).attr("element") +');"><a href="javascript:void(0)">标签分析</a></li>' +
									   '<li class="icon_wtag" title="标签微分" onclick="openLabelDifferential('+ $(ths).attr("element") +');"><a href="javascript:void(0)">标签微分</a></li>';
								if($(ths).attr("updateCycle") == 2) {
									html +=
										'<li class="icon04" title="关联分析" onclick="openLabelRel('+ $(ths).attr("element") +');"><a href="javascript:void(0)">关联分析</a></li>' +
										'<li class="icon05" title="对比分析" onclick="openLabelContrast('+ $(ths).attr("element") +');"><a href="javascript:void(0)">对比分析</a></li>'
								}
                            html +=
							'</ul>' +
						'</div>' +
					'</div>' +
				'</div>';
	var _tip=$(html);
	$("#tag_tips .group").empty().append(_tip);
	_tip.find(".icon").css("margin-left",iconx+"px");
	_tip.addClass("onshow").slideDown("fast");
	_tip.click(function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	});


	//增加标签分析等按钮的鼠标移入和移出特效效果(由于这几个按钮是在调用showTip方法时动态加入的，所以需要在这个地方给这几个按钮加事件)
	$(".info_panel ul li").unbind("mouseenter").unbind("mouseleave");
	$(".info_panel ul li").on("mouseenter",function(){
		var _a=$(this).find("a");
		_a.css({"position":"absolute","left":"-3000px"});
		var _w=_a.width();
		_a.css({"position":"static","left":"0"});
		_a.width(0).show().animate({width:_w + 2},250);
	}).on("mouseleave",function(){
		var _a=$(this).find("a");
		_a.width(_a.width() - 2).hide();
	})
}

/*function regroup(areaDataId){
	$(areaDataId+" > .tag_List dd").not(".regroup_done").each(function(){
		var nodata=$(this).find("td.needCrush");
		if(nodata.length>0){
			var newrow=$('<tr class="last"><td colspan="2"><ul class="tag_List_ul"></ul></td></tr>');
			var _table=$(this).find(".commonTable");
			nodata.each(function(){
				var li_string=$(this).prev().html().replace(/span/ig,"li");
				var _li=$(li_string);
				if(userBrowser()=="IE"){
					//ie7下有的标签超长
					if(parseInt(userBrowserVer())==7){
						_table.find("th").not(".floatTitle").each(function(){
							var _td=$(this).next("td");
							var w_thtd=$(this).width()+_td.width();
							_td.attr("width",w_thtd-164);
							$(this).width("auto").attr("width",164)
						})
					}
				}
				
				newrow.find("ul").addClass("regroup_ul").append(_li);
				if(_li.hasClass("isCanDrag")){
					_li.draggable({ revert: "invalid", helper: "clone" });
					
				}
				if($(this).siblings(".floatTitle").length>0){
					$(this).prev().remove();
					$(this).remove()
				}else{
					$(this).parent("tr").remove();
				}
				
			});
			//newrow.find(".regroup_ul li").mouseenter(function(){$(this).addClass("hover")}).mouseleave(function(){$(this).removeClass("hover")});
		
			$(this).addClass("regroup_done");
			_table.find("tr:last").removeClass("last");
			_table.append(newrow);
			var len=_table.find("tr").length;
			_table.find("tr").eq(0).find("th.floatTitle").attr("rowspan",len);
		}
	})
}*/


//首页标签地图点击"展示全部"显示所有二级标签及子标签
function expandChildrenOfSecondL(secondLevId) {
		showOverlay();
	    var actionUrl = $.ctx + "/ci/ciIndexAction!getSecondLLabelTree.ai2do?labelId="+secondLevId;
		$.ajax({
			type : 'POST',
			url : actionUrl,
			data : {},
			success : function(data) {	
				//1-实现标签重组
				var aibi_area_data=$("#labelId_"+secondLevId);
				aibi_area_data.empty();
				var tempct=$("<div></div>");
				tempct.append(data);
				tempct.find(".tag_List dd").not(".regroup_done").each(function(){
					var nodata=$(this).find("td.needCrush");
					if(nodata.length>0){
						var newrow=$('<tr class="last" width="100%"><td colspan="2" width="100%"><ul class="tag_List_ul"></ul></td></tr>');
						var _table=$(this).find(".commonTable");
						nodata.each(function(){
							var li_string=$(this).prev().html().replace(/span/ig,"li");
							var _li=$(li_string);
							/*
							//找到用css的解决方法，暂注释掉
							if(userBrowser()=="IE"){
								//ie7下有的标签超长
								if(parseInt(userBrowserVer())==7){
									_table.find("th").not(".floatTitle").each(function(){
										var _td=$(this).next("td");
										var w_thtd=$("#aibi_area_data").width()-16;
										_td.attr("width",w_thtd-164);
										$(this).width("auto").attr("width",164)
									})
								}
							}
							*/
							
							newrow.find("ul").addClass("regroup_ul").append(_li);
							
							if($(this).siblings(".floatTitle").length>0){
								$(this).prev().remove();
								$(this).remove()
							}else{
								$(this).parent("tr").remove();
							}
							
						});
						$(this).addClass("regroup_done");
						_table.find("tr:last").removeClass("last");
						_table.append(newrow);
						var len=_table.find("tr").length;
						_table.find("tr").eq(0).find("th.floatTitle").attr("rowspan",len);
					}
				})
				aibi_area_data.append(tempct.html());
				
				//2-标签拖动
				$(".isCanDrag").not(".draggable").draggable({ revert: "invalid", helper: "clone" });
				
				//$("#loading").fadeOut("normal", function(){						
				//	$("#noMoreResults").fadeIn();
				//});
				//$("#loading").fadeOut();
				 hideOverlay();

				var dls=aibi_area_data.find("dl");
				dls.each(function(){
					$(this).find("> dd").each(function(){
						$(this).find("> table tr ").last().addClass("last");
					});
					$(this).find("> dd").last().addClass("last");
				});
				//$("#aibi_area_data").stopScrollPagination();
			},
			dataType : 'html'
		});
}
function firsttimelabel_tip_remove(ts){
	$(ts).parents(".firsttimelabel_tip").remove();
}
</script>
<style type="text/css">
     <%--设置标签微分等超链接只显示图片，不显示名称--%>
	.info_panel ul li a{display:none; overflow:hidden;}
</style>
</head>
<body>
<div class="tag_curtain">
	<div id="top_menu" class="top_menu">
	</div>
    
    <div id="tag_curtain_CT" class="tag_curtain_CT">
    </div><!--tag_curtain_CT end -->
</div><!-- tag_curtain end-->

<div id="aibi_area_data" class="body_bg">
</div>
<div id="loading">正在加载，请稍候 ...</div>
<div id="noMoreResults" style="display:none;">没有更多的记录了 ...</div>
<div id="noResults" style="display:none;">没有标签信息...</div>

 <div id="firsttimelabel_tip" class="firsttimelabel_tip" style="position: absolute;display:none;">
 	 <div class="arrow"></div>
     <div class="inner">
         <div class="close"><a href="javascript:void(0);" onclick="firsttimelabel_tip_remove(this)">×</a></div>
         <p style="font-weight:bold;">拖动它，试试看！</p>
         <p>按住鼠标左键，拖到下方试试看！</p>
     </div>
 </div>
<!--  标签地图中标签详细信息弹出层div模板-->
<div id="tag_tips" class="tag_tips">
	<div class="group">
	</div>
</div>
</body>
</html>
