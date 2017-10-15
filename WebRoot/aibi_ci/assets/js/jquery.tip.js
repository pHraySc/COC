 /*
 * Title: tip 
 * Description: 增强系统默认的提示功能。搜索所有的title属性，然后重新显示。
 * Aucthor:tiansuqing
 * Email: tiansuqing@gmail.com
 * Create Date:2009-07-21
 */
(function($){	
	$(function(){
		if($.distooltip) return;
		var tooltip=$("#divTooltip");
		if(tooltip.length == 0){	
			$('body').append("<div id='divTooltip' class='tooltip'></div>");
			tooltip=$("#divTooltip");
			tooltip.hide();			
		}
		
		$().find("[title]").each(function() {
			//save the title text and remove it from title to avoid showing the default tooltip
			var image="<img style='float:left' src='"+$.ctx+"/images/info-icon.gif' />";
			var content=image+$(this).attr("title");
			$(this).attr("title","");
			
			$(this).mouseover(function(e){
				winw = $(window).width();
				w = tooltip.width();
				
				//right priority
				if(e.clientX+150 < winw)
				  tooltip.css("left", $(document).scrollLeft() + e.clientX+10);
				else {
				  tooltip.css("left", $(document).scrollLeft() + e.clientX-160);
				}
				
				winh = $(window).height();
				h = tooltip.height();
				
				//top position priority
				if(h+e.clientY < winh)
				  tooltip.css("top", $(document).scrollTop() + e.clientY);
				else 
				  tooltip.css("top", $(document).scrollTop() + e.clientY-h);
	
				tooltip.append(content);
				tooltip.show();
			});
			
			$(this).mouseout(function(e){
			    tooltip.empty();
				tooltip.hide();
			});
		});
	});
})(jQuery);	