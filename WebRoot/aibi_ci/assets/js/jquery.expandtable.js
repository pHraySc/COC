jQuery.fn.expandtable = function(options) {
	var settings = {
		url :'',
		iframeid:'',
		afterOpen:false,
		afterClose:false
	};
	var opts = jQuery.extend(settings, options);
	if (opts.url == "")
		alert("你必须提供url参数。");
	// 增加加减号图标
	var today = new Date();
	var id = today.getTime() + parseInt(Math.random() * 100000);
	
	if(opts.iframeid == "") opts.iframeid="ifame"+id;
	var img=$("<img style='padding: 0 5px 0 5px;' id='ep'"+id+" src='"
			+ $.ctx + "/images/plus.gif'/>");
	$(this).prepend(img);
    
	var td = $(this);
	td.one("click", function() {
		var _tr=$("<tr></tr>").attr({"id":"trid"+id});
		var _td=$("<td style='padding:2px' colspan='"+td.parent().children().size()+"'></td>");
		
		var _iframe=$("<iframe frameborder=0 class='expandifame'></iframe>").attr( {
				"id":opts.iframeid,
				"src" :opts.url
			}).css({
				"width" :"98.8%",
				"height" :"100px"
			}).load( function() {
				var iframe = $(this);
				iframe.height(iframe.contents().find("body").height()+5);
			});
		_iframe.appendTo(_td);
		_td.appendTo(_tr)
		td.parent().after(_tr);
			
		img.attr('src', $.ctx + "/images/minus.gif");
		if(opts.afterOpen)opts.afterOpen();
		
		img.toggle( function() {
			img.attr('src', $.ctx + "/images/plus.gif");
			$("#trid" + id).hide();
			if(opts.afterClose)opts.afterClose();
		}, function() {
			img.attr('src', $.ctx + "/images/minus.gif");
			$("#trid" + id).show();
			if(opts.afterOpen)opts.afterOpen();
		});
	});


}