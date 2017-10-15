
var dialogUtil = {
		/**
		 * 弹框
		 * @param options = {} 参数对象param的参数例子//{"test":"23","aaa":"asr"}或param
		 * @param id dialog 的ID
		 * @param closeCallback一定要以String方法传递 非必填
		 * @param remove 是否移除 boolean 非必填
		 */
	create_dialog:function(id, options,closeCallback, remove) {
		if (remove || typeof remove == 'undefined') {
			$("#"+id).remove();
		} 
		var id = (id && id.length>0) ? id : "JQ_Dialog" + new Date().getTime();
		options = options || {};
		var dlg = $('<div id="'+id+'"><iframe id="' + id + '_iframe" name="' + id + '_iframe" scrolling="no" allowtransparency="true" src="'+ options.frameSrc +'"   framespacing="0" border="0" frameborder="0" style="width:100%;height:'+ options.frameHeight +'px"></iframe></div>')
		$(document).find("> div:last").after(dlg);
			 
		return dlg.dialog( {
			modal : true,
			title : options.title || "弹框标题",
			height : options.height || "auto",
			width : options.width || 776,
			autoOpen : true,
			resizable : false,
			position : options.position || ['center','center'],
			buttons : options.buttons || {
				/*'确定' : function() {
					$(this).dialog('close');
				}*/
			},
			close : function( event, ui ) {
				if (closeCallback) {
					var result = [];
					if(typeof options.param == "object"){
					    for(var item in options.param){
					        result.push("'"+options.param[item]+"'");
					    }
					    options.param = result.toString();
					} else {
						options.param = options.param ? "'" + options.param  +"'" : ""; 
					}
					var param = options.param ? options.param : "" ;
					
					eval("("+closeCallback+"("+param+")"+")");
				}
				$(this).dialog("destroy");
			 },
			 create: function( event, ui ) {
				 var width = options.width || 776;
				 var title = options.title || "弹框标题";
				 $(this).parent().find(".ui-dialog-title").width(width-50).attr("title",title);
			 }
		}); 
	}  
};




