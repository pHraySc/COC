/*
 * Title: datattable
 * Description: 给datatable表格增加样式。
 * Aucthor:tiansuqing
 * Email: tiansuqing@gmail.com
 * Create Date:2009-07-21
 * edit by chengjia 2009-12-10
 */
( function($) {
	$( function() {
		
		$(".tablesorter").datatable();
	});

	//下钻表格手动填充tr背景色
	$.fn.datatable = function() {
		return this.each( function() {
			//$("tbody tr:odd", this).removeClass("odd");
			//$("tbody tr:even", this).removeClass("even");
			$("tbody tr", this).removeClass("aibi_tr_changecolor");
			$("tbody tr", this).removeClass("odd");
			$("tbody tr:odd", this).addClass("odd");
			$("tbody tr:even", this).addClass("aibi_tr_changecolor");
			//鼠标滑动
			$("tbody tr", this).hover( function() {
					$(this).addClass("hover");
				}, function() {
					$(this).removeClass("hover");
				});
			});
	};
})(jQuery);
