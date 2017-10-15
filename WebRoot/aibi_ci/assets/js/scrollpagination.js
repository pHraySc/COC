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
				success : function(data) {
					$(obj).append(data);
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