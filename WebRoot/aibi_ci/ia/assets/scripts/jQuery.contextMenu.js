
/**
 *  jQuery 扩展  右键菜单
 */
(function(){
	$.fn.extend({  
		rightContextMenu:function(option){
			var defaults = {
				click:function(){
				},
				menuList:[
					[{
						name:"创建数据分档",
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
			$this.live("contextmenu",function(e){ 
				//判断右键菜单中的项目
				var _targer=$(this).parent().attr("target");
				
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
				
				for(var i =0 ,len = list.length;i<len;i++){
					for(var j = 0 ,length = list[i].length;j<length;j++){// group-line  
						if(_targer=="row"||_targer=="column"){
							if(list[i][j].name!="仅排除"&&list[i][j].name!="仅包含"){
								var $li = $('<li class="of cia-menu-item tl cursor J_contextMenu"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								if(!!list[i][j].url){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" url="'+list[i][j].url+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(!!list[i][j].target){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" target="'+list[i][j].target+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(j == list[i].length - 1){
									$li.addClass("group-line"); 
								}
								
								var attrId = $(this).parent("li").attr("id"); //左侧分析属性右键菜单
								if(attrId == undefined){
									attrId = $(this).attr("id");	//右侧已添加属性的右键菜单
								}
								$li.attr("attrId",attrId)
								$menus.append($li);  
							}
							
						}else if(_targer=="symbol"){
							if(list[i][j].name=="重命名"||list[i][j].name=="排序"||list[i][j].name=="设置聚合方式"||list[i][j].name=="设置计算字段"){
								var $li = $('<li class="of cia-menu-item tl cursor J_contextMenu"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								if(!!list[i][j].url){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" url="'+list[i][j].url+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(!!list[i][j].target){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" target="'+list[i][j].target+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(j == list[i].length - 1){
									$li.addClass("group-line"); 
								}
								
								var attrId = $(this).parent("li").attr("id"); //左侧分析属性右键菜单
								if(attrId == undefined){
									attrId = $(this).attr("id");	//右侧已添加属性的右键菜单
								}
								$li.attr("attrId",attrId)
								$menus.append($li);  
							}
						}else if(_targer=="filter-exclude"){
							if(list[i][j].name=="仅排除"){
								var $li = $('<li class="of cia-menu-item tl cursor J_contextMenu"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								if(!!list[i][j].url){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" url="'+list[i][j].url+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(!!list[i][j].target){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" target="'+list[i][j].target+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(j == list[i].length - 1){
									$li.addClass("group-line"); 
								}
								
								var attrId = $(this).parent("li").attr("id"); //左侧分析属性右键菜单
								if(attrId == undefined){
									attrId = $(this).attr("id");	//右侧已添加属性的右键菜单
								}
								$li.attr("attrId",attrId)
								$menus.append($li);  
							}
						}else if(_targer=="filter-include"){
							if(list[i][j].name=="仅包含"){
								var $li = $('<li class="of cia-menu-item tl cursor J_contextMenu"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								if(!!list[i][j].url){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" url="'+list[i][j].url+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(!!list[i][j].target){
									$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" target="'+list[i][j].target+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
								}
								if(j == list[i].length - 1){
									$li.addClass("group-line"); 
								}
								
								var attrId = $(this).parent("li").attr("id"); //左侧分析属性右键菜单
								if(attrId == undefined){
									attrId = $(this).attr("id");	//右侧已添加属性的右键菜单
								}
								$li.attr("attrId",attrId)
								$menus.append($li);  
							}
						}else{
							var $li = $('<li class="of cia-menu-item tl cursor J_contextMenu"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
							if(!!list[i][j].url){
								$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" url="'+list[i][j].url+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
							}
							if(!!list[i][j].target){
								$li = $('<li class="of cia-menu-item tl cursor J_contextMenu" target="'+list[i][j].target+'"  id="'+list[i][j].id+'">'+list[i][j].name+'</li>');
							}
							if(j == list[i].length - 1){
								$li.addClass("group-line"); 
							}
							
							var attrId = $(this).parent("li").attr("id"); //左侧分析属性右键菜单
							if(attrId == undefined){
								attrId = $(this).attr("id");	//右侧已添加属性的右键菜单
							}
							$li.attr("attrId",attrId)
							$menus.append($li);  
						}
					}
				}
				var $last_line_li= $menus.find("li.group-line").last();
				if($last_line_li.next().length == 0){
					$last_line_li.removeClass("group-line"); 
				}
				var height = ($menus.find("li").length-1)*26+$menus.find("li.group-line").length*3; 
				$("div[data-style='height']").height(height);
				$(".J_contextMenu").bind("click",options.click);
				return false;
			});
		},
	 });
		
})($);