/*
 * Title: panel
 * Description: 给panel增加收缩功能。
 * Aucthor:tiansuqing
 * Email: tiansuqing@gmail.com
 * Create Date:2009-07-21
 */
(function($){	
	$(function(){
		$(".panel-end-notoggle,.panel2-title-notoggle").prepend('<img class="panel-toggle" status="open"  src="'+$.ctx+'/images/blank.gif" />');		
		$(".panel-end,.panel2-title").prepend('<img class="panel-toggle" status="open"  src="'+$.ctx+'/images/panel/arrow-down-icon.gif" />');		
		$(".panel2-title-close").prepend('<img class="panel-toggle" status="close"  src="'+$.ctx+'/images/panel/arrow-up-icon.gif" />');		

		$(".panel-toggle").each(function(){
			$(this).click(function(){
				var status =$(this).attr("status");
				if(status=='open'){
					$(this).attr("src",$.ctx+'/images/panel/arrow-up-icon.gif'); 
					$(this).attr("status","close");
				}else{
					$(this).attr("src",$.ctx+'/images/panel/arrow-down-icon.gif'); 
					$(this).attr("status","open");
				}
				$(this).parent().next().toggle();
			});
			$(".panel2-title-close").next().hide();
		});
	});
})(jQuery);	

