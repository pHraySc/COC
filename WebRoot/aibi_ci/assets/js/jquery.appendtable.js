 /*
 * Title: jquery.appendtabel.js. 
 * Description: 给表格追加行。
 * Aucthor:tiansuqing
 * Email: tiansuqing@gmail.com
 * Create Date:2009-07-21
 */
( function($) {
	jQuery.fn.appendtable = function(options) {
		var settings = {
			trid:'',// 要在此tr后面追加tr 如果没有指定参数 寻找父元素追加
			type :'post',
			async :true,
			cache :false,
			data :'',
			url :'',
			beforeOpen:false,
			afterOpen:false,
			afterClose:false,
			error : function() {
				alert("Data Load Error!");
			}
		};
		var opts = jQuery.extend(settings, options);
		if (opts.url == "")
			alert("URL is needed!");

		var today = new Date();
		var time = today.getTime() + parseInt(Math.random() * 100000);

		if ($("#appenddiv").length == 0) {
			$('body').append("<div id='appenddiv'></div>");
			$("#appenddiv").hide();
		}

		var img = $("<img style='padding: 0 5px 0 5px;' id='appendtabledivimg_' src='"
				+ $.ctx + "/images/plus.gif'/>");
		var sessionAreaCode = opts.sessionAreaCode;
		if(sessionAreaCode.substring(2,4) == '00'){
			$(this).prepend(img);
		

		var td = $(this);
		td.one("click", function() {
			if(opts.beforeOpen)opts.beforeOpen(td);
			$.ajax( {
				type :'post',
				async :true,
				cache :false,
				url :opts.url,
				data :opts.data,
				cache :false,
				success : function(html) {
					var appenddiv = $("#appenddiv");
					appenddiv.append(html);
					appenddiv.find("tr").addClass("ad" + time);
					appenddiv.find("tr td:first-child").addClass("clickTd");
                    if(opts.trid=='')
                    	td.parent().after(appenddiv.html());
                    else
                    	$("$"+opts.trid).after(appenddiv.html())
					appenddiv.empty();
					img.attr('src', $.ctx + "/images/minus.gif");
					if(opts.afterOpen)opts.afterOpen(td);
					
					if(sessionAreaCode.substring(2,4) == '00'){
					
						img.toggle( function() {
							img.attr('src', $.ctx + "/images/plus.gif");
							$(".ad" + time).hide();
							if(opts.afterClose)opts.afterClose(td);
						}, function() {
							img.attr('src', $.ctx + "/images/minus.gif");
							$(".ad" + time).show();
							if(opts.afterOpen)opts.afterOpen(td);
						});
					}
				}
			});
		});}
	};
})(jQuery);




