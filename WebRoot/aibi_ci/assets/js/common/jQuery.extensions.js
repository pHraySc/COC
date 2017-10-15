
/**
 * 普通下拉层
 * @param targetList 下拉层的jquery对象
 * @param slideSpeed 下拉速度(非必需)
 * @param focusblur input提示信息
 */
(function(){
	$.fn.extend({
		COClive:function(type,fn){
			return $(this).die(type||"").live(type||"",fn||function(){});
			
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
				position :"",
				absoluteObj:""
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
					//window.setTimeout(function(){
					var obj_width = (options.absoluteObj == null || options.absoluteObj == "") ? ($(options.targetObj).width()+2)/2 : ($(options.absoluteObj).width()+2)/2;
					$(options.targetObj).css({position:options.position,left:options.left+"px",top:options.top+"px",margin:"0 0 0 -"+obj_width+"px" }).show("scale",100);
					//$(options.targetObj).css({position:options.position,left:options.left+"px",top:options.top+"px" }).slideDown();
					// } , 500)
					   
				},function(){
					options.navTimer= setTimeout(function(){
						$(options.targetObj).hide("scale",100);
//						$(options.targetObj).slideUp();
						_this.removeClass(options.hoverClass);
					},options.waitTime);
				});
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
		
		  },
		  //input text 内容提示信息  option参数可有可无，但是如要改变字体颜色必须传参  根据data-val的true Or false来判断input value 是否为空
		  focusblur:function(option){
			  option = option || {};
			  var defval = $(this).val();
			  $(this).focus(function () {
	                var thisval = $(this).val();
	                if (thisval == defval) {
	                    $(this).val("").css("color",(option.focusColor || "")).attr("data-val","true");
	                    defval = option["searchVal"] ? option["searchVal"] : defval;
	                }
	          });
			  $(this).blur(function () {
	                var thisval = $(this).val();
	                if (thisval == "") {
	                	$(this).css("color",(option.blurColor || "")).attr("data-val","false").val(option["searchVal"]||defval);;
	                }
	            });
			},
			addCartAnimate:function(option){ 
				var $this = $(this);
				var defaults = {
						isClick:false,
						hideFuc:function(){}
				}; 
				var options = $.extend(defaults,option);  
				if(options.isClick){
					var e=e||window.event;
					e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
					var clone = $(this).clone().addClass("u-flyer");
//					var top = $(clone).offset().top+20;
					var cartUl = $(".J_cart_box  ul:first");
					var top=0,left=0;
					if(cartUl.length == 0){
						var current = $("#openCalculateCenterBtn").parent();
						left =  current.offset().left;
						top =  current.offset().top + current.height() + 1;
					}else{
						var firstLi = cartUl.find(">li:first");
						left = firstLi.offset().left;
						top = firstLi.offset().top;
					}
					var result = options.hideFuc(this);
					if(!result){return;}
					clone.fly({
						speed: 2,
						vertex_Rtop:($(clone).offset().top+150),
						start:{
							left: $(this).offset().left,
							top: $(this).offset().top
						},
						end:{
							left:left,
							top: top,
							width: "auto",
							height: "auto"
						}
					}).hide(1500); 
					return;
				}
				$this.COClive("dblclick",function(e){  
					var e=e||window.event;
					e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
					var clone = $(this).clone().addClass("u-flyer");
//					var top = $(clone).offset().top+20;
					var cartUl = $(".J_cart_box  ul:first");
					var firstLi = cartUl.find(">li:first");
					var result = options.hideFuc(this);
					if(!result){return;}
					clone.fly({
						speed: 2,
						vertex_Rtop:($(clone).offset().top+150),
						start:{
							left: $(this).offset().left,
							top: $(this).offset().top
						},
						end:{
							left: firstLi.offset().left,
							top: firstLi.offset().top,
							width: "auto",
							height: "auto"
						}
					}).hide(1500); 
				});
				
			},
			tagNavMap:function(optionKey,option){ 
				var defaultKey ={
					name:"name",
					id:"id"
				};
				var optionsKey = $.extend(defaultKey,optionKey);
				var nameKey = optionsKey.name;
				var idKey = optionsKey.id;
				var defaults = { 
					menuList:{data:[
						{
							nameKey:"社会属性",
							idKey:"attr",
							children:[
								{
									nameKey:"人口统计",
									idKey:"people"
								},
								{
									nameKey:"职业身份",
									idKey:"IT"
								}
							]
						},
						{
							nameKey:"用户属性",
							idKey:"user-attr",
							children:[
								{
									nameKey:"品牌星级",
									idKey:"star"
								},
								{
									nameKey:"地市归属",
									idKey:"city"
								},
								{
									nameKey:"生命周期",
									idKey:"date"
								},
								{
									nameKey:"位置特征",
									idKey:"adress"
								},
								{
									nameKey:"集团信息",
									idKey:"company"
								},
								{
									nameKey:"通信轨迹",
									idKey:"map"
								}
							]
						}
					]}, 
					left:"",
					top:"" 	,
					click:function(){}
				};
				 
				var options = $.extend(defaults,option);
				var $this=$(this);  
				$this.hover(function(e){  
					var e=e||window.event;
					e.stopPropagation?e.stopPropagation():e.cancelBubble=false; 
					var firstCategoryId = $(this).attr("id");
					//$(this).addClass("market-group-item-hover");
					$("#marketMapMenus").remove();
					var list = options.menuList.data; 
					if(list.length == 0){
						return;
					}
					var html =' <div class="market-menus" id="marketMapMenus"><div class="market-menus-header"></div><div class="market-menus-center"><div class="market-menus-center-inner"><ul class="market-menus-ul" id="tagNavMapContent"></ul></div></div><div class="market-menus-bottom"></div></div>';
					var left = options.left || $(this).offset().left + $(this).width()-20;
					var top = options.top || $(this).offset().top + $(this).height()/2 - 44;
					var $html = $(html).css({"left":left,"top":top});					
					$("body").append($html); 
					for(var i = 0,len = list.length;i < len;i++){
						var $li = $('<li class="market-menus-li" id="'+list[i][idKey]+'_li" ><div class="fleft sec-menu ellipsis-all "><a class="J_menu_click" id="'+list[i][idKey]+'" title="'+list[i][nameKey]+'">'+list[i][nameKey]+'</a></div><div class="fleft thr-menu"><div class="thr-menu-inner" id="'+list[i][idKey]+'_content"></div></div></li>');
						if(list[i].children && list[i].children.length == 0){
							$li = $('<li class="market-menus-li" id="'+list[i][idKey]+'_li" ><div class="fleft thr-menu-text sec-menu J_menu_click" id="'+list[i][idKey]+'" title="'+list[i][nameKey]+'">'+list[i][nameKey]+'</div><div class="fleft thr-menu"><div class="thr-menu-inner" id="'+list[i][idKey]+'_content"></div></div></li>');
						}
						if(i == list.length-1){
							$li.css({"border":0});
						}
						$("#tagNavMapContent").append($li); 
						if(list[i].children && list[i].children.length > 0){
							for(var j = 0,leng = list[i].children.length;j < leng;j++){ 
								var $a = $('<a href ="javascript:;" class="thr-menu-text fleft J_menu_click" id="'+list[i].children[j][idKey]+'">'+list[i].children[j][nameKey]+'</a>');
								$("#"+list[i][idKey]+"_content").append($a);
							}	 
						}
					}  
					var height = $("#tagNavMapContent").height(); 
					if(height < 40){
						$(".market-menus-center").height(height-20).find("> div").height(height); 
						$("#marketMapMenus").find("> div.market-menus-header").addClass("market-menus-header-s");	
						top = options.top || $(this).offset().top + $(this).height()/2 - 34; 
						$html.css({"left":left,"top":top});	
						$("#tagNavMapContent").parent().css("top","17px");
					}else if(height < 60){
						$("#marketMapMenus").find("> div.market-menus-header").addClass("market-menus-header-s");
						$(".market-menus-center").height(height-35).find("> div").height(height);  
						top = options.top || $(this).offset().top + $(this).height()/2 - 34; 
						$html.css({"left":left,"top":top});
						$("#tagNavMapContent").parent().css("top","7px");
					}else{
						$("#marketMapMenus").find("> div.market-menus-header").removeClass("market-menus-header-s");
						$(".market-menus-center").height(height-49).find("> div").height(height); 
						$("#tagNavMapContent").parent().css("top","7px");						
					} 
					$(".J_menu_click").bind("click",{firstCategoryId:firstCategoryId},options.click)
					return false;
				},
				function(){
					var $menuthis = $(this);
					$("#marketMapMenus").hover(function(){
						$("#marketMapMenus").show();
						$menuthis.addClass("market-group-item-hover");
					},function(){
						$("#marketMapMenus").hide();
					    $menuthis.removeClass("market-group-item-hover");
					});
					$("#marketMapMenus").hide();
				});
				
			},
			COCdialog:function(option){
				var $this = $(this).remove();
				var defaults = {
					buttons:[
						{
						   	type:"cancel",
						   	text: "取消", 
						   	click: function() {
							   $("#"+$this.attr("id")+"_dialog").animate({"width":0},500,function(){
								   $(this).hide();
							   });
							   $("#tag_dropable").hide();//隐藏层
							   $(".dialog-widget-overlay").remove(); 
							} 
						},
						{
							type:"ok",
							text: "确定",
							click: function() {
								$("#"+$this.attr("id")+"_dialog").animate({"width":0},500,function(){
									$(this).hide();
								});
								$(".dialog-widget-overlay").remove(); 
							} 
						}
					],
					skipUrl:"",
					show:"clip",
					hide:"drop",
					open:function(){alert("open")},
					modal:true,
					title:"设置", 
					top: $(".content-COC-list").offset().top,
					left:$(".content-COC-list").offset().left - 10
				};
				var width = $(".content-COC-list").width() + 26;//26为多出的那个arrow 
				var options = $.extend(defaults,option);
				$("#"+$this.attr("id")+"_dialog").remove();
				var html = '<div class="customer-group-dialog clearfix hidden" id="'+$this.attr("id")+'_dialog"><div class="fleft left-dialog-bg"></div><div class="fleft dialog-content">'+
						   '<div class="clearfix dialog-content-inner"><div class="fright right-dialog-bg"></div><div class="fright dialog-main">'+
						   '<div class="dialog-main-inner"><div class="customer-dialog-title"><span class="ellipsis-all fleft">'+options.title+'</span>'+
						   '<span class="fright share-iocn market-navigation-icon J_dialog_exit"></span></div><div class="customer-dialog-content" id="customerDialogScroll">'+
						   '<div class="customer-dialog-content-inner" id="'+$this.attr("id")+'_COCdialog"></div></div>'+
						   '<div class="customer-dialog-footer clearfix" id="'+$this.attr("id")+'_buttons"></div></div></div></div></div></div>';
				html = $(html).css({right:$("body").width()-(options.left + width),top:options.top,width:width}); 
				$("body").append(html.hide().width(0));
				$("#"+$this.attr("id")+"_COCdialog").append($this)
				html.show().animate({"width":width},500,function(){
					if (options.backFun && options.backFun instanceof Function) {
						options.backFun();
					}
				});
				$(window).resize(function(){ 
					var top = $(".content-COC-list").offset().top;
					var left = $(".content-COC-list").offset().left - 10;
					var width = $(".content-COC-list").width() + 26;//26为多出的那个arrow 
					html.css({left:left,top:top,width:width}); 
				});
				$(".J_dialog_exit").COClive("click", function() {
					$("#"+$this.attr("id")+"_dialog").animate({"width":0},500,function(){
						$(this).hide();
					});
					$("#tag_dropable").hide();//隐藏层
					$(".dialog-widget-overlay").remove();
				});
				if(options.modal){
					$(".dialog-widget-overlay").remove();
					$("body").append('<div class="dialog-widget-overlay"></div>');
				}else{
					$(".dialog-widget-overlay").remove();
				}  
				var btns = options.buttons;
				for(var i = 0,len=btns.length; i < len; i++){
					var button = $('<button type="button" class="fright '+btns[i].type+'-button">'+btns[i].text+'</button>').bind("click",btns[i].click);
					$("#"+$this.attr("id")+"_buttons").append(button);
				}
				$("#customerDialogScroll").mCustomScrollbar({
					theme:"minimal-dark", //滚动条样式
					scrollbarPosition:"inside", //滚动条位置
					scrollInertia:500,
					axis:"y",
					mouseWheel:{
						preventDefault:true, //阻止冒泡事件
						axis:"y"
					}
				});
				return html;
			},
			hoverShowMenu:function(option){
				var defaults = {
					click:function(){},
					menuList:[
						[{
							name:"创建数据文档",
							id:"createDataFile"
						}],
						[{
							name:"设置数据格式",
							id:"setDataFormat"
						},{
							name:"设置计算字段",
							id:"setCalculatedField"
						},{
							name:"设置聚合方式",
							id:"setAggregation"
						}]
					],
					left:"",
					top:"" 	
				};
				var dataClick = {
						name:"",
						id:"",
						click:function(){}
				}
				var options = $.extend(defaults,option);
				var $this=$(this); 
				
				var html = '<div class="cia-bubble-dialog of" id="cia-bubble-dialog"><div class="cia-bubble-dialog-top"></div>'
						+'<div class="cia-bubble-dialog-center" data-style="height"><div class="cia-bubble-dialog-content">'
						+'<ul class="clearfix of" id="cia-menu-list"></ul></div></div><div class="cia-bubble-dialog-bottom"></div></div>';
				$this.hover(function(e){ 
					//$(this).addClass("market-group-item-hover");
					var e=e||window.event;
					e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
					if($this.hasClass("tag-box-list") || $this.hasClass("tag-box-list-hover")){
						$(".tag-box-list-hover").removeClass("tag-box-list-hover").addClass("tag-box-list");
						$(this).removeClass("tag-box-list").addClass("tag-box-list-hover");
					} 
					$("#cia-bubble-dialog").remove(); 
					var left = options.left || $(this).offset().left + $(this).width()-20;
					var top = options.top || $(this).offset().top + $(this).height()/2 - 22; 
					var $menuBox = $(html).css({"left":left,"top":top});
					var $menus = $menuBox.find("#cia-menu-list");
					$("body").append($menuBox);
					var list = options.menuList;
					for(var i=0,len = list.length;i<len;i++){
						for(var j=0 ,length = list[i].length;j<length;j++){// group-line  
							var dataList = $.extend(dataClick,list[i][j]);
							var $li = $('<li class="of cia-menu-item tl hand J_menu_child_event" parentIcon="' + list[i][j].parentIcon 
									+ '" pId="' + list[i][j].pId + '" id="'
									+list[i][j].id+'">'+list[i][j].name+'</li>').bind("click",dataList.click);
							if(j == list[i].length - 1){
								$li.addClass("group-line"); 
							}
							$menus.append($li);  
						}
					}
					var $last_line_li= $menus.find("li.group-line").last();
					if($last_line_li.next().length == 0){
						$last_line_li.removeClass("group-line"); 
					}
					var height = ($menus.find("li").length-1)*26+$menus.find("li.group-line").length*3; 
					$("div[data-style='height']").height(height);
//					$(".J_menu_child_event").COClive("click",options.click);
					return false;
				},
				function(){
					var $menuthis = $(this);//.removeClass("market-group-item-hover");
					$("#cia-bubble-dialog").hover(function(){
						$("#cia-bubble-dialog").show();
						$menuthis.addClass("market-group-item-hover");
					},function(){
						$("#cia-bubble-dialog").hide();
						$menuthis.removeClass("market-group-item-hover");
					});
					$("#cia-bubble-dialog").hide();
					
				});
			}
	  });
})($);