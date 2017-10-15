﻿$(function(){
	//首页顶部position fix的div在页面缩小于1004px以后右侧显示不全
	$(window).resize(function(){
	   /*	var tar=$(".loginfInner");
		var winW=$(window).width();
		winW<1004?tar.width(winW):tar.width(1004);*/
		toolPosition();
	});
	$(document).click(function(e){
		 e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		 var opList =$(".resultItemActiveOption");
		 opList.find(".resultItemActiveList").hide();
		 opList.removeClass("resultItemActiveOption");
		 $(".searchPanel .classify .slideDown").hide().prev().find(".arr_down").removeClass("arr_up");
		 $(".ui-autocomplete").hide();
		 $(".searchHistoryBox").hide();
		 $("#searchText_history_Box").hide();
	});
	 //点击页面其他地方隐藏操作列及恢复操作按钮样式
//	 $("body").COClive("click",function(e){
//		 e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
//		 var opList =$(".resultItemActiveOption");
//		 opList.find(".resultItemActiveList").hide();
//		 opList.removeClass("resultItemActiveOption");
//		 $(".searchPanel .classify .slideDown").hide().prev().find(".arr_down").removeClass("arr_up");
//		$(".ui-autocomplete").hide();
//		$(".searchHistoryBox").hide();
//		$("#searchText_history_Box").hide();
//	 });
	//---------------分析页面选择下拉框，用于切换不同页面 start------------------
	//选择框
	$( '.tag_box_manage #select_box' ).click(function( e ){
		e.preventDefault();
		var div = $( this ).parents( '.tag_box_manage' ).siblings( '.form-field' ).show();
		var dom = div.get( 0 );
		dom.out = setTimeout(function(){
			div.hide();
		},6000);
	}).parents( '.tag_box_manage' ).siblings( '.form-field' ).bind( 'mouseleave', function( e ){
		clearTimeout( this.out );
		this.out = setTimeout(function(){
			$( e.currentTarget ).hide();
		},200);
	}).bind( 'mouseenter',function( e ){
		clearTimeout( this.out );
	});
	$('.form-field li a').click(function( e ){
		var a = $( this );
		a.parents( 'ul' ).find( 'li.on' ).removeClass( 'on' );
		a.parent( 'li' ).addClass( 'on' );
		$( '.tag_box_manage #select_box .like_button_middle:first span' ).html(a.html());
		a.parents('.form-field').hide();
	});
	//---------------分析页面选择下拉框，用于切换不同页面 end------------------
	
	if('undefined' == typeof(document.body.style.maxHeight)){
		$(".menulist").addClass("ie6_menulist");
		$(".date_line").addClass("date_line_ie6");
	}
	//左侧菜单
	$(".menulist a").focus(function(){
		$(this).blur();
	});
	$(".menulist li").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		var _index=$(this).parent().find("li").index($(this)[0]);
		var submenu=$("body > .subMenu");
		if($(this).hasClass("menu_on")){
			//submenu.hide().find("dl.on").removeClass("on");
		}else{
			$(this).siblings(".menu_on").removeClass("menu_on");
			$(this).addClass("menu_on");
			submenu.find("dl").eq(_index).addClass("on").siblings("dl.on").removeClass("on");
			submenu.show().width(0).animate({width:162},250);
		}
	});
	$(document).on("mouseover", ".mainTable tr", function(){
		if(!$(this).hasClass("nohover")&&!$(this).parents("table").hasClass("nohover")){
			$(this).addClass("hover");
		}
	}).on("mouseout", ".mainTable tr", function(){
		$(this).removeClass("hover");
	});
	//个人消息下拉
	$(".slideDown_menu > .slideTitle").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		$(this).blur();
		$(this).siblings(".slideDown").slideDown("fast");
	})
	$(".slideDown_menu dl dt .close").click(function(){
		$(this).parents(".slideDown").hide();
	})
	
	//帮助修改
	$(".slideDown_menu .icon_help").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		$(this).blur();
		$(this).siblings(".slideDown").slideDown("fast");
	})
	//通知
	$(".main_notice dl dd .close").click(function(){
		$(this).parents("dl").hide();
	})
	
	//首页模糊搜索
	$("#searchTab li a").click(function(){
		$(this).blur();
		var _p=$(this).parent();
		if(_p.hasClass("active")) return;
		_p.addClass("active").siblings(".active").removeClass("active");
	});
	
	$(".top_menu li").click(function(){
		$(this).siblings().removeClass("top_menu_on");
		$(this).addClass("top_menu_on");
	});
	$(".top_menu li").not(".top_menu_on").mouseover(function(){
		$(this).addClass("top_menu_hover");
	}).mouseout(function(){
		$( this ).removeClass("top_menu_hover");	
	});
	
	//搜索区域下拉
	$(".searchPanel .classify .droplist").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		var _icon=$(this).find(".arr_down");
		if(_icon.hasClass("arr_up")){
			_icon.removeClass("arr_up");
			$(this).siblings("ul").hide();
		}else{
			_icon.addClass("arr_up");
			$(this).siblings("ul").slideDown("fast");
		}
	});
	$(".searchPanel .classify .slideDown li").click(function(){
		var txt=$(this).find("a").html();
		var classId = $(this).find("input").val();
		$(this).parent().prev().find("span").html(txt);
		$("#classId").val(classId);
	});

	
	//搜索区域高级查询
	$(".aibi_search ul li.more a").click(function(){
			if($(this).html()=="高级查询"){
				$(".aibi_search ul.search_line1").hide();
				$(".aibi_search ul.search_line2").show();
			}else{
				$(".aibi_search ul.search_line2").hide();
				$(".aibi_search ul.search_line1").show();
			}
		});
	
	//更多信息展示隐藏
	$(".showMore_box .showMove_control").click(function(){
		if($(this).children("span").html() == "展示更多信息"){
			$(this).siblings(".showMore").children(".showMove_div").show();
			$(this).children("span").html("隐藏更多信息");
			$(this).children("span").addClass("up_arr");
		}else{
			$(this).siblings(".showMore").children(".showMove_div").hide();
			$(this).children("span").html("展示更多信息");
			$(this).children("span").removeClass("up_arr");
		};
	});
	
	//工具栏定位
    toolPosition();
	
	//2014-04-17 页面元素切换效果 --开始*************************
	//客户群标签视图切换效果
//	$("#viewNavBox").hoverForIE6({current:"viewNavBoxHover",delay:200});
//	$("#viewNavBox .viewNavItem").hoverForIE6({current:"viewNavItemHover",delay:200});

	//购物车位置定位
	//$("#shopCartBox").css("right",parseInt((bodyWidth-1005)/2,10));
	//第三级菜单点击时去除当前层
//	$("#viewNavBox  a").click(function(){
//		$("#viewNavBox").removeClass("viewNavBoxHover");
//	});
	//查询条件筛选条件删除
	$(".selectedItemClose").click(function(){
		$(this).parent().remove();
	});
	//添加你选项的查询条件
	$("#priceFilter .defineDataInput").focus(function(){
		$(this).siblings(".btn_normal").show();
	}).eq(1).blur(function(){
		$(this).siblings(".btn_normal").hide();
	});
	$("#dateFilter .defineDataInput").focus(function(){
		$(this).siblings(".btn_normal").show();
	}).eq(1).blur(function(){
		$(this).siblings(".btn_normal").hide();
	});
	$(".selectOptionValue a").click(function(){
		$(this).addClass("selectOptionValueHover");
		var targetCT=$("#selectedItemListBox");
		//判断group互斥
		var groupID=$(this).attr("group");
		if(groupID!=""&&targetCT.find("div[group='"+groupID+"']")>0) return;
		//判断单个条件重复
		if(targetCT.find("div[queryname='"+$(this).attr("queryname")+"']").length>0) return;
		
		var $selectedItemBox = $("<div class=\"selectedItemBox\" group=\""+groupID+"\" queryname=\""+$(this).attr("queryname")+"\"></div>");
		var $selectedItemDetail = $("<div class=\"selectedItemDetail\">"+$(this).html()+"</div>");
		var $selectedItemClose = $("<div class=\"selectedItemClose\"></div>");
		$selectedItemBox.append($selectedItemDetail).append($selectedItemClose);
		targetCT.append($selectedItemBox);
		$selectedItemBox.hover(function(){
		   $(this).addClass("selectedItemBoxHover");
		},function(){
			$(this).removeClass("selectedItemBoxHover");
		})
	  	//绑定单击事件
		$selectedItemClose.click(function(){
			$(this).parent().remove();
		});
	});
	//更多查询条件
	$("#moreOptionActive").click(function(){
	    if($("#moreSelectOptionList").is(":visible")){
			$("#moreSelectOptionList").slideUp(500);
			$(this).removeClass("moreOptionUp");
		}else{
   			$("#moreSelectOptionList").slideDown(500);
			$(this).addClass("moreOptionUp");
	 	}
	});
	 
			
	//购物车中商品删除
	$(".shopCartGoodsActiveBox a").click(function(){
	   	$(this).parents("li").remove();
		   var numCt=$("#shopCartInfo strong");
			var oldNum=parseInt(numCt.html());
				oldNum-=1;
				numCt.html(oldNum);
				$("#shopBasket em").html(oldNum);
				//判断如果购物车中商品为空的时的切换显示情况
				if(oldNum == 0){
					 $("#shopCartEmptyTip").show(); //购物篮隐藏
					 $("#shopCartGoodsListBox").hide(); //已选购物篮显示
				}else{
				    oldNum-=1;
					numCt.html(oldNum);
					$("#shopBasket em").html(oldNum);
				}
		return false;
	});
	//购物车显示详情
	$("#shopCartGoodsListBox").click(function(e){
		e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	})
	$("#shopCartInfo").hover(
		function(){
		  if($("#shopCartGoodsListBox").is(":visible")){
			  $("#shopCartLetter").removeClass("shopCartLetterHover");
			  $("#shopCartEmptyTip").hide();//显示购物车中没有物品
		  }else{
			//判断购物车中是否有物品
			var shopNum = parseInt($("#shopBasket em").html(),10);
			if(shopNum == 0){ //如果没有商品
			   $("#shopCartEmptyTip").show();
			}else{
			   $("#shopCartLetter").addClass("shopCartLetterHover");
			}
		  }
		  
	    },function(){
	       $("#shopCartLetter").removeClass("shopCartLetterHover");
		   $("#shopCartEmptyTip").hide();
	 }).click(function(){
		 showShopCartList();
	})
    //购物车清空
//	$("#emptyShopCart").click(function(){
//	     //$(this).addClass("emptyShopCartActive");
//	     $("#shopCartEmptyTip").show(); 
//		 $("#shopCartGoodsListBox").hide();
//		 $("#shopBasket").show();
//		 $("#shopBasketSelected").hide();
//		 $("#shopCartInfo  strong").html(0);
//		 $("#shopBasket em").html(0);
//		 $("#shopCartGoodsList ol").empty();
//		 
//	}).mouseover(function(){
//	  // $(this).removeClass("emptyShopCartActive");
//	})
	//创建客户群
	$("#skipTagCalc").click(function(e){
	   $(this).addClass("skipTagCalcActive");
	}).mouseout(function(){
	   $(this).removeClass("skipTagCalcActive");
	})
	//回到顶部
	 $("#scrollTop").click(function(){
		 var scrollTop  = $(document).scrollTop();
		 var windowHeight =$(window).height();
		 var scrollTime = 1000;
		 var scale = parseInt(scrollTop/windowHeight);
		 //时间点变值
		$("body,html").stop().animate({scrollTop:0},scrollTime/scale);
		return false;
	 });


	 //客户群列表选择单击事件
	 /*$(".resultItemActive").click(function(e){
	     e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		 var _t=$(this);
		 var $resultItemActiveList= $(this).children(".resultItemActiveList");
	     if($resultItemActiveList.is(":visible")){
		     $resultItemActiveList.hide();
			 _t.removeClass("resultItemActiveHover");
		 }else{
		     $resultItemActiveList.slideDown('fast',function(){_t.addClass("resultItemActiveHover")});
		 }
	 }).hover(function(){
	    $(this).addClass("resultItemActiveOption");
		
	 },function(){
		 var $resultItemActiveList= $(this).children(".resultItemActiveList");
	     if(!$resultItemActiveList.is(":visible")){
		    $(this).removeClass("resultItemActiveOption");
		 }
	 })*/
	 

     
	 //关闭弹出框
	 //$(".dialogClose , .btnActiveBox input").click(function(){ $(this).parent().parent().dialog("close");})
	 //2014-04-17 页面元素切换效果 --结束*************************
	 $(window).load(function(){
		  //购物车滚动条
		   $("#shopCartGoodsList").mCustomScrollbar({
				theme:"minimal-dark", //滚动条样式
				scrollbarPosition:"inside", //滚动条位置
				scrollInertia:500,
				mouseWheel:{
					preventDefault:true //阻止冒泡事件
					
				}
			});
		 
	 })
	
});
//document.ready end
//工具栏定位
function toolPosition(){
	var bodyWidth = $("body").width();
	var toolTipRight= 0;
	    if(bodyWidth >1037){
		   toolTipRight= parseInt((bodyWidth-1005)/2,10)-32;
		}else{
		   toolTipRight = parseInt((bodyWidth-1005)/2,10);
		}
	    toolTipRight = toolTipRight<0 ? 0 : toolTipRight;
	$("#toolTip").css("right",toolTipRight);
}

//表格隔行换色及选中样式
function aibiTableChangeColor(obj){
	$(obj+" tr:even").addClass("even");
	$(obj+" tr:odd").addClass("odd");
	
	if(!$(obj).hasClass("nohover")){
		$(obj+ " tr").hover(function(){
			$(this).addClass('hover');
		},function(){
			$(this).removeClass('hover');
		});
	}
	if(!$(obj).hasClass("noselect_tr")){
		$(obj+" tr").click(function(){
			if($(this).hasClass('aibi_tr_selected')){
				
			}
			else{
				$(obj+ " tr").removeClass('aibi_tr_selected');	
				$(this).addClass('aibi_tr_selected');
			}
		})
	}
}

//生成日期
function creatDateline(ct,date,clickDateFn){

    var _year;
    var _month;
    var isContainHyphen = false;
    date = new String(date);
    if(date.indexOf("-") > -1){
        _year=date.split("-")[0];
        _month=date.split("-")[1];
        isContainHyphen = true;
    }else{
        _year=date.substring(0,4);
        _month = date.substring(4,6);
    }
    if(_month.length == 2 && _month.substring(0,1)=='0'){
        _month = _month.substring(1);
    }
    ct.empty();
	//var _li='<font class="year">2012</font>年<font class="month">6</font>月';
    var outer=$('<div class="date_line" _value="'+getDateString(_year,_month,isContainHyphen)+'"><ul></ul></div>');
	for(var i=0;i<12;i++){
		if(i==0){
			var _li=$('<li class="onchoose"></li>');
		}else{
			var _li=$('<li></li>');
		}
		var cmonth='<font class=\"month\">'+_month+'</font><span>月</span>';
        _li.attr("_value",getDateString(_year,_month,isContainHyphen));
		_li.prepend(cmonth);
		_month-=1;
		if(_month==0){
			var cyear='<font class=\"year\">'+_year+'</font><span>年</span>';
			_li.prepend(cyear);
			_year-=1;
			_month=12;
		}
		outer.find("ul").prepend(_li);
	}
	ct.prepend(outer);
	outer.find("li").each(function(){
		$(this).bind("click",function(){
            $(".date_line").attr("_value",$(this).attr("_value"));
			$(this).addClass("onchoose").siblings(".onchoose").removeClass("onchoose");
			if(clickDateFn){
				clickDateFn($(this).attr("_value"));
			}
		});
		$(this).hover(function(){
			$(this).hasClass("onchoose")?"":$(this).addClass("hover_choose")
		},function(){
			$(this).removeClass("hover_choose")
		})
	})
	resize_dateline(outer,ct);
	$(window).resize(function(){
		resize_dateline(outer,ct);
	})
}
function resize_dateline(outer,ct){//alert(1);
	var totalW=0;
	outer.css({"position":"absolute","left":"-9999px","display":"block"});
	outer.find("li").each(function(){
		totalW+=$(this).innerWidth();
	})
	var _margin=(ct.width()-totalW-20)/24;
	outer.find("li").each(function(){
		$(this).css({"margin-left":_margin,"margin-right":_margin});
	});
	outer.css({"position":"static","left":"auto","display":""});
}

//获取日期字符串
function getDateString(year,month,isContainHyphen){
    var hyphen = "";
    if(isContainHyphen){
        hyphen = "-";
    }
    var retDate = "";
    month = new String(month);
    if(month.length == 1){
        retDate = year + hyphen + "0"  + month;
    }else{
        retDate = year + hyphen  + month;
    }
    return retDate;
}



//下拉切换and or
function switchConnector(t,e){
	var e=e||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$(t).blur();
	$("#tag_dropable .onConnector").removeClass("onConnector");
	$(t).addClass("onConnector");
	var posX=$(t).offset().left-38;
	var posY=$(t).offset().top+39;
	if($("body > #connector").length==0){
		var _ul=$('<ul id="connector"><li><a href="javascript:void(0)">且</a></li><li><a href="javascript:void(0)">或</a></li><li><a href="javascript:void(0)">剔除</a></li></ul>');
		_ul.appendTo("body");
		$(document).click(function(){_ul.hide()});
		_ul.find("li").bind("click",function(){
			$("#tag_dropable .onConnector").prev().html($(this).find("a").html());
			$(this).parent().hide();
		});
		$(window).scroll(connectorPosFixed);
	}
	var tar=$("#connector"),winW=$(window).width(),tarW=tar.width(),winH=$(window).height()+$(window).scrollTop(),tarH=tar.height();
	if(posX+tarW>winW) posX=winW-tarW-14;
	if(posY+tarH>winH) posY=posY-tarH-40;
	tar.css({"left":posX+"px","top":posY+"px"}).show();
}
var delay_connectorPosFixed=false;
function connectorPosFixed(){
	var connector=$("#connector");
	if(connector.css("display")!="block") return;
	
	if(delay_connectorPosFixed!=false) clearTimeout(delay_connectorPosFixed);
	delay_connectorPosFixed=setTimeout(function(){
		var now_winscrollTop=$(window).scrollTop();
		var onConnectorCT=$("#tag_dropable .onConnector");
		var winW=$(window).width(),tarW=connector.width(),winH=$(window).height()+$(window).scrollTop(),tarH=connector.height();
		var posX=onConnectorCT.offset().left-38;
		var posY=onConnectorCT.offset().top+39;
		if(posX+tarW>winW) posX=winW-tarW-14;
		if(posY+tarH>winH) posY=posY-tarH-40;
		connector.stop().animate({top:posY,left:posX},200);
		delay_connectorPosFixed=false;
	},100);
}

var connectFlagArray=[ "且","或","剔除" ];
function switchConnector_turn(t,e){
	var e=e||window.event;
	var obj=$(t);
	var old=obj.html();
	var next="";
	if (/msie/.test(navigator.userAgent.toLowerCase()) && !$.support.leadingWhitespace){
		if(e.button==1) next=true;
		else if(e.button==2) next=false;
	}else{
		if(e.button==0) next=true;
		else if(e.button==2) next=false;
	}
	var _new="";
	for(var i=0;i<connectFlagArray.length;i++){
		if(connectFlagArray[i]==old){
			if(next==true){
				//左键
				if(i==connectFlagArray.length-1) _new=connectFlagArray[0];
				else _new=connectFlagArray[i+1];
			}else{
				//右键
				if(i==0) _new=connectFlagArray[connectFlagArray.length-1];
				else _new=connectFlagArray[i-1];
			}
		}
	}
	obj.html(_new);
	return false;
}

/**
 * 系统提示
 * @param _type 类型：1、success，2、failed，3、confirm
 * @param _width
 * @param _height
 * @param txt 提示信息
 */
function showAlert(txt,_type,callbackFn,param,_width,_height,pagescroll_y, goToHomePage, closeCallBackFn){
	if(_type=="success"){
		var tmpIframe=$('<iframe id="alertSucessIframe" class="alertTempIframe" showtxt="'+txt+'" scrolling="no" src="'+$.ctx+'/aibi_ci/successMsg.jsp" framespacing="0" border="0" frameborder="0" style="width:100%"></iframe>');
	}
	else if(_type=="failed"){
		var tmpIframe=$('<iframe id="alertFailedIframe" class="alertTempIframe" showtxt="'+txt+'" scrolling="no" src="'+$.ctx+'/aibi_ci/failedMsg.jsp" framespacing="0" border="0" frameborder="0" style="width:100%"></iframe>');
	}
	else if(_type=="confirm"){
		var tmpIframe=$('<iframe id="alertConfirmIframe" class="alertTempIframe" showtxt="'+txt+'" scrolling="no" src="'+$.ctx+'/aibi_ci/confirmMsg.jsp" framespacing="0" border="0" frameborder="0" style="width:100%"></iframe>');
	}
	tmpIframe.on("load",function(){
		$(this).css("width","100%");
		$(this).contents().find(".showtxt").html(txt);
		if(goToHomePage) {
			$(this).contents().find("#goToHomePage").show();
			$(this).contents().find("#goToHomePage").click(function() {
				window.parent.location.href = goToHomePage;
			});
		} else {
			$(this).contents().find("#goToHomePage").hide();
		}
		$(this).contents().find("#ok").on("click",function(){
			tmpIframe.dialog("destroy");
			$(this).remove();
			
			if(callbackFn){
				if(param){
					callbackFn(param);
				}else{
					callbackFn();
				}
			}
		});
		$(this).contents().find("#cancel").on("click",function(){
			tmpIframe.dialog("destroy");
			tmpIframe.remove();
		});
	})
	if(!_width){
		_width=500;
	}
	if(!_height){
		_height=200;
	}
	
	if(pagescroll_y>=0){
		var _y=pagescroll_y+220;
		var _x=$(window).width()/2-_width/2;
	}else{
		var _y="center";
		var _x="center";
	}
	tmpIframe.dialog({
		dialogClass:"ui-dialogFixed",
		autoOpen: true,
		width: _width,
		height:_height,
		title:"系统提示",
		modal: true,
		position: [_x,_y],
		resizable:false,
		close : function() {
			if(closeCallBackFn) {
				closeCallBackFn();
			}
		}
	});
}

/**
 * 打开Loading遮罩层
 */
function showLoading(tipText){
	var $waitDiv = "<div class='waitLoading' id='_waitLoading'></div>"
					+ "<div class='waitBox' id='_waitBox'>正在进行数据探索，请耐心等候...</div>";
	$(document.body).append($waitDiv);
	$("#_waitLoading").css({width:$(document).width() - 20,height:$(document).height(),"z-index":"99999"}).show();
	$("#_waitBox").css({"z-index":"99999"});
	if (tipText) {
		$("#_waitBox").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+tipText);
	}
	$("#_waitBox").show();
}

/**
 * 关闭Loading遮罩层
 */
function closeLoading(){
	$("#_waitLoading,#_waitBox").remove();
}

/* user browser */
function userBrowserVer(){
	var br=navigator.userAgent.toLowerCase();  
	var browserVer=(br.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [0, '0'])[1];
	return browserVer;
}
function userBrowser(){  
    var browserName=navigator.userAgent.toLowerCase();  
    if(/msie/i.test(browserName) && !/opera/.test(browserName)){  
        return("IE");   
    }else if(/firefox/i.test(browserName)){   
        return "Firefox";  
    }else if(/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName)){   
        return "Chrome";  
    }else if(/opera/i.test(browserName)){   
        return "Opera";  
    }else if(/webkit/i.test(browserName) &&!(/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName))){    
        return "Safari";  
    }else{  
        return "unKnow";  
    }  
} 

/**
 * 关闭dialog弹出层
 * @param dialogId 弹出dialog的Id
 */
function closeDialog(dialogId){
    $(dialogId).dialog("close");
}


/**
 * 普通下拉层
 * @param targetList 下拉层的jquery对象
 * @param slideSpeed 下拉速度(非必需)
 */
/*(function(){
	$.fn.extend({
		inputDefaultValue	: function(settings) {
			var defaults = {
				fColor	: "222232",
				bColor	: "8e8e8e"
			};
			
			var settings = $.extend(defaults, settings);
			var _this = $(this);
			var defaultVal = _this.val();

			_this.focus(function(){ 
				var currentVal = $(this).val(); 
				$(this).css("color", "#" + settings.fColor);
				if(currentVal == defaultVal){
					$(this).val("");
				}
			}).blur(function(){
				var currentVal = $(this).val();
				if(currentVal == ""){
					$(this).css("color", "#" + settings.bColor);
					$(this).val(defaultVal);
				}
			});
		},
		cocSlideDown:function(options){
			var defaults = {
				targetList:"",
				slideSpeed:"fast",
				posfixedParent:false,
				getData:''
			}
			var _this=$(this);
			var options = $.extend(defaults,options);
			
			var slideTargetList=function(e){
				e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
				if(options.targetList.hasClass("showed")){
					hideTargetList();
				}else{
					$(".slideDownCT").hide();
					var posx=_this.offset().left;
					var posy=_this.offset().top+_this.height();
					if(options.posfixedParent==true){
						posy-=$(window).scrollTop();
					}
					if(defaults.getData instanceof Function) {
						defaults.getData();
					}
					options.targetList.css({"left":posx,"top":posy,"z-index":1000}).slideDown(options.slideSpeed,function(){
						options.targetList.addClass("showed");
						_this.addClass("showed");
					});
				}
			} 
			var hideTargetList=function(){
				options.targetList.hide().removeClass("showed");
				_this.removeClass("showed");
			}
			return this.each(function(){
				_this.on("click",slideTargetList);
				$(document).on("click",hideTargetList);
				$(this).focus(function(){$(this).blur()});
				$(this).find("a").focus(function(){$(this).blur()})
			})
		},
	    //鼠标移入移除显示效果
        cocFanAnimate:function(option){
			var defaults = {
				targetObj:"",
				waitTime:300,
				navTimer: 0 ,
				left:"",
			    top :"",
			    getData:'',
				hoverClass:"",  //鼠标移动上的样式
				position :""

			}
			var _this=$(this);
			var options = $.extend(defaults,option);
			    
				_this.hover(function(){
					$(".searchText input").blur();
					if(options.getData instanceof Function) {
						options.getData();
					}
					if(options.hoverClass){
					  _this.addClass(options.hoverClass);
					}
				     clearTimeout(options.navTimer);
				     //颜色显示
					// window.setTimeout(function(){
						 $(options.targetObj).css({position:options.position,left:options.left+"px",top:options.top+"px" }).show("scale",100);
//						 $(options.targetObj).css({position:options.position,left:options.left+"px",top:options.top+"px" }).slideDown();
					  // } , 500)
					   
				   },function(){
					   options.navTimer= setTimeout(function(){
						  $(options.targetObj).hide("scale",100);
//						  $(options.targetObj).slideUp();
						  _this.removeClass(options.hoverClass);
					   },options.waitTime)
				   })
			   //子导航的操作
               $(options.targetObj).hover(function(){
				  clearTimeout(options.navTimer);
			   },function(){
				   options.navTimer= setTimeout(function(){
					  $(options.targetObj).hide("scale",100);
//					  $(options.targetObj).slideUp();
					  _this.removeClass(options.hoverClass);
				   },options.waitTime)
			   })
			  
			},
         //成功弱提示插件
		 weakTip:function(option){
			 var scrollTop = $(document).scrollTop();
			 var winWidth  = $(window).width();
			 var winHeight = $(window).height();
			 var left = parseInt((winWidth - 180)/2,10);
			 var top  = parseInt((winHeight -70)/2 + scrollTop ,10);
 			//动态设置默认的位置：
		    var defaults = {
					top:top,
					left:left,
					timer: 1500 , //等待时间
					tipTxt:"操作成功", //显示信息
					backFn:'' //回调函数
				}
			var options = $.extend(defaults,option);
			var $weakObj = $("<div class=\"weakTipBox\"><div class=\"weakTipLeft\"></div><div class=\"weakTipText\">"+options.tipTxt+"</div> <div class=\"weakTipRight\"></div></div>");
		   
			 $weakObj.css({"top":options.top}); 
			 if(options.tipTxt.length<21){
                  $weakObj.css("line-height","60px"); 
			 }else{
				  $weakObj.css("line-height","30px");  
			 }
			 $(document.body).append($weakObj);
			 //重新计算位置
			 left =  parseInt((winWidth - $weakObj.width())/2,10);
			 $weakObj.css("left",left).show();

			 window.setTimeout(function(){
				 $weakObj.fadeOut("normal").remove();
				 if(options.backFn instanceof Function) {
					 options.backFn();
				  }
			 },options.timer)
		
		  }
			
	})
})($);*/


//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。 
function convertArray(o) { 
	var v = {};
	for (var i in o) { 
		if (typeof (v[o[i].name]) == 'undefined') v[o[i].name] = o[i].value;
		else v[o[i].name] += "," + o[i].value;
	}
	return v; 
}

/**
 * 打开标签计算中心
 */
function openCalculateCenter(){
	$("#shopCartGoodsListBox").hide();
	$("#shopBasketSelected").hide();
	$("#shopBasket").show();
	
	var url = $.ctx + "/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1";
	var  form = $("<form></form>")
    form.attr('action',url)
    form.attr('method','post')
    var input = $("<input type='hidden' name='url' />")
    var thisUrl = location.href;
    input.attr('value', thisUrl)
    
	form.append(input)
    form.appendTo("body")
    form.css('display','none')
    form.submit()
    
	/*window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+
			",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");*/
}

/**
 * 刷新购物车
 * @return
 */
function shopCarList(){
	$.ajax({
		url: $.ctx + "/ci/ciIndexAction!queryShopCarList.ai2do",
		type: "POST",
		success: function(data){
			$("#shopCartGoodsList ol").html(data);
			var len = $("#shopCartGoodsList ol li").length;
			$("#shopNum").html(len);
			$("#shopNumBasket").html(len);
		}
	});
}

function showShopCartList() {
	$("#shopCartLetter").removeClass("shopCartLetterHover");
	var tar=$("#shopCartGoodsList");
	var numValue = $.ajax({
		url		: $.ctx + '/ci/ciIndexAction!findCarShopNum.ai2do',
		async	: false
	}).responseText;
	var num = eval('(' + numValue + ')').num;
	if(!num) {
		num = 0;
	}
	$("#shopNum").empty();
	$("#shopNum").append(num);
	$("#shopNumBasket").empty();
	$("#shopNumBasket").append(num);
	$("#shopTopNum").empty();
	$("#shopTopNum").append(num);
	shopCarList();//刷新购物车
	
	if(tar.is(":visible")){
		tar.animate({height:0},300,function(){
			$("#shopCartGoodsListBox").hide();
			$("#shopBasketSelected").hide();
			$("#shopBasket").show();
		})
	} else {
		$("#shopCartEmptyTip").hide();//显示购物车中没有物品
		//判断购物车中是否有物品
		var shopNum = parseInt($("#shopBasket em").html(),10);
		if(shopNum == 0){
			$("#shopCartEmptyTip").show();//显示购物车中没有物品
		} else {
			/**及时同步 购物篮和已选商品数量  **/
			$("#shopBasket").hide(); //购物篮隐藏
			$("#shopBasketSelected").show(); //已选购物篮显示
			$("#shopCartGoodsListBox").show();
			tar.css("visibility","visible").height("auto");
			var shopChartHeight = tar.height();
			tar.height(0).show();
			tar.animate({height:shopChartHeight},300,function(){tar.height("auto")})
		}
	}
}
/**
 * 解决IE8不支持数组indexOf方法问题
 */
if (!Array.prototype.indexOf){
	Array.prototype.indexOf = function(elt /*, from*/){
	var len = this.length >>> 0;
	var from = Number(arguments[1]) || 0;
	from = (from < 0)
		 ? Math.ceil(from)
		 : Math.floor(from);
	if (from < 0)
	  from += len;
	for (; from < len; from++)
	{
	  if (from in this &&
		  this[from] === elt)
		return from;
	}
	return -1;
  };
}
/**
 * js判断浏览器是否安装Flash插件
 */
function detectFlash() {
    //navigator.mimeTypes是MIME类型，包含插件信息
	if(navigator.mimeTypes.length > 0){
		//application/x-shockwave-flash是flash插件的名字
	    var flashAct = navigator.mimeTypes["application/x-shockwave-flash"];
	    return flashAct != null ? flashAct.enabledPlugin != null : false;
	} else if(self.ActiveXObject) {
	    try {
	        new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
	        return true;
	    } catch (oError) {
	        return false;
	    }
	}
}
