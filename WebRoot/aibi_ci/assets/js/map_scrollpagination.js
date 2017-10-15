/*
**	Anderson Ferminiano
**	contato@andersonferminiano.com -- feel free to contact me for bugs or new implementations.
**	jQuery ScrollPagination
**	28th/March/2011
**	http://andersonferminiano.com/jqueryscrollpagination/
**	You may use this script for free, but keep my credits.
**	Thank you.
*/

(function( $ ){

	$.fn.scrollPagination = function(options) {
		var opts = $.extend({},$.fn.scrollPagination.defaults, options);
		var target = opts.scrollTarget;
		if (target == null) {
			target = obj;
		}
		opts.scrollTarget = target;

		return this.each(function() {
			$.fn.scrollPagination.init($(this), opts);
		});

	};

	$.fn.stopScrollPagination = function() {
		return this.each(function() {
			$(this).attr('scrollPagination', 'disabled');
		});

	};

	$.fn.scrollPagination.loadContent = function(obj, opts) {
		var target = opts.scrollTarget;
		var mayLoadContent = $(target).scrollTop() + opts.heightOffset >= $(
				document).height()
				- $(target).height();
		if ((mayLoadContent && opts.loadStatus == 1) || (opts.currentPage == 1 && opts.loadStatus == 1)) {
			opts.loadStatus = 0;
			if (opts.beforeLoad != null) {
				opts.beforeLoad();
			}
			$(obj).children().attr('rel', 'loaded');
			$.ajax({
				type : 'POST',
				url : opts.contentPage,
				data : $.extend({},opts.contentData,{"currentPage" : opts.currentPage}),
				success : function(data) {//alert(data.toString());
					var tempct=$("<div></div>");
					tempct.append(data);
					tempct.find(".tagList dd").not(".regroup_done").each(function(){
						var nodata=$(this).find("td.needCrush");
						if(nodata.length>0){
							var newrow=$('<tr class="last" width="100%"><td colspan="2"><ul class="tagList_ul"></ul></td></tr>');
							var _table=$(this).find(".commonTable");
							nodata.each(function(){
								var li_string=$(this).prev().html().replace(/span/ig,"li");
								var _li=$(li_string);
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
								
								newrow.find("ul").addClass("regroup_ul").append(_li);
								/*if(_li.hasClass("isCanDrag")){
									_li.draggable({ revert: "invalid", helper: "clone" });
									
								}*/
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
							newrow.find("li:first").css("width","180px");
							_table.append(newrow);
							var len=_table.find("tr").length;
							_table.find("tr").eq(0).find("th.floatTitle").attr("rowspan",len);
						}
						
					})
					tempct.find(".tagList").each(function(){
						var regroupDD=$('<dd><table class="commonTable" cellSpacing="0" cellPadding="0"><tr class="last" width="100%"><td><ul class="tagList_ul regroup_ul"></ul></td></tr></table></dd>');
						$(this).find("dd").each(function(){
							if($(this).find("tr").length==1&&$(this).find(".nodata").length>=2){
								var li_string=$(this).find(".floatTitle").html().replace(/span/ig,"li");
								var _li=$(li_string);
								regroupDD.find("ul").append(_li);
								$(this).remove();
							}
						})
						if(regroupDD.find("li").length>=1){
							$(this).append(regroupDD);
						}
					})
					$(obj).append(tempct.html());

					opts.loadStatus = 1;
					var objectsRendered = $(obj).children('[rel!=loaded]');
					opts.currentPage = opts.currentPage + 1;
					if (opts.currentPage > opts.totalPage) {
						$(obj).attr('scrollPagination', 'disabled');
					}
					if (opts.afterLoad != null) {
						opts.afterLoad(objectsRendered);
					}
				},
				dataType : 'html'
			});
		}

	};

	$.fn.scrollPagination.init = function(obj, opts) {
		var target = opts.scrollTarget;
		$(obj).attr('scrollPagination', 'enabled');
        $(target).unbind("scroll");
		$(target).scroll(function(event) {
			if ($(obj).attr('scrollPagination') == 'enabled') {
				$.fn.scrollPagination.loadContent(obj, opts);
			} else {
				event.stopPropagation();
			}
		});

		$.fn.scrollPagination.loadContent(obj, opts);

	};

	$.fn.scrollPagination.defaults = {
		'contentPage' : null,
		'contentData' : {},
		'beforeLoad' : null,
		'afterLoad' : null,
		'scrollTarget' : null,
		'heightOffset' : 0,
		'currentPage' : 1,
		'totalPage' : null,
		'loadStatus' : 1
	};
	
	//code for fade in element by element
	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity : 1 }, 200);
			delay += 100;
		});
	};
	
})(jQuery);