
/**
 * 	2014-12-30
 *  jQuery 扩展  右键菜单
 */
(function(){
	$.fn.extend({  
		/*hoverShowMenu:function(option){
			var defaults = {
				click:'',
				menuList:[
					[{
						name:"促进本网销售",
						id:"addRow"
					},{
						name:"引导终端使用体验",
						id:"addSeries"
					}] 
				],
				left:"",
				top:"" 	
			};
			var dataClick = {
					name:"",
					id:"",
					param:"",
					click:function(){}
			}
			var options = $.extend(defaults,option);
			var $this=$(this); 
			
			var html = '<div class="cia-bubble-dialog of" id="cia-bubble-dialog"><div class="cia-bubble-dialog-top"></div>'
					+'<div class="cia-bubble-dialog-center" data-style="height"><div class="cia-bubble-dialog-content">'
				    +'<ul class="clearfix of" id="cia-menu-list"></ul></div></div><div class="cia-bubble-dialog-bottom"></div></div>';
			$this.hover(function(e){ 
				var e=e||window.event;
				e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
				if($this.hasClass("tag-box-list") || $this.hasClass("tag-box-list-hover")){
					$(".tag-box-list-hover").removeClass("tag-box-list-hover").addClass("tag-box-list");
					$(this).removeClass("tag-box-list").addClass("tag-box-list-hover");
				} 
				$("#cia-bubble-dialog").remove(); 
				var left = options.left || $(this).offset().left + $(this).width();
				var top = options.top || $(this).offset().top + $(this).height()/2 - 22; 
				var $menuBox = $(html).css({"left":left,"top":top});
				var $menus = $menuBox.find("#cia-menu-list");
				$("body").append($menuBox);
				var list = options.menuList;
				for(var i in list){
					for(var j in list[i]){// group-line  
						var dataList = $.extend(dataClick,list[i][j]);
						var $li = $('<li class="of cia-menu-item tl hand J_menu_child_event" parentIcon="' + dataList.parentIcon 
								+ '" id="'+dataList.id+'">'+dataList.name+'</li>').bind("click",dataList.click);
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
				return false;
			});
		}*/
	  });
})($);