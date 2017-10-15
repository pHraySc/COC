jQueryUIUtils = function() {

	var jQueryUIUtils = {
		multiselect:function(id,option){ 
			var defaults = { 
					multiple: false,
					header: false,
					noneSelectedText: "请选择",
					selectedList: 4,
					minWidth:225,
					checkAllText:"全选",
					uncheckAllText:"全不选",
					selectedText:"选中了#个", 
					show:['blind', 500], 
					hide:['blind', 500],
					autoOpen:false,
					beforeopen:function(event,ui){ 
						$(".ui-multiselect-checkboxes").mCustomScrollbar({
							theme:"minimal-dark", //滚动条样式
							scrollbarPosition:"inside", //滚动条位置
							scrollInertia:500,
							axis:"y",
							mouseWheel:{
								preventDefault:true, //阻止冒泡事件
								axis:"y"
							}
						});
					},
					click:function(event, ui){}
				};
			var options = $.extend(defaults,option);
			var multiObj = $(id).multiselect({
				multiple: options.multiple,
				header: options.header,
				noneSelectedText: options.noneSelectedText,
				selectedList: options.selectedList,
				checkAllText:options.checkAllText,
				uncheckAllText:options.uncheckAllText,
				selectedText:options.selectedText, 
				show:options.show, 
				hide:options.hide,
				minWidth:options.minWidth,
				autoOpen:options.autoOpen,
				beforeopen:options.beforeopen(),
				click: function(event, ui){
					options.click(event, ui);
				 }
			});
			return multiObj;
		},
		dialog:function(id,option){
			var defaults = { 
				close:function(){
					$(this).remove();
				},
				open:function(){},
				content:"请添加内容",
				title:"弹框标题", 
				height: "auto",
				width: 750,
				buttons: {
					"确定": function() {
					  $this.dialog( "close" );
					},
					"取消": function() {
					  $this.dialog( "close" );
					}
				}, 
				position:["center","center"] 
			};
			var options = $.extend(defaults,option);
			var $this =$(id).remove();
			if($this.length == 0){
				$this = $('<div id="'+id.replace("#","")+'">'+options.content+'</div>');
				$(document.body).append($this); 
			}
			$this = $this.dialog({ 
				close:options.close,
				open:options.open, 
				title:options.title,
				autoOpen:true,
				height: options.height,
				width: options.width,
				buttons: options.buttons,
				draggable: true,
				position:options.position,
				modal:true,
				resizable:false,
				show: { effect: "blind", duration: 800 },
				hide: { effect: "blind", duration: 800 }
			});
			return $this;
		}
		
	};
	return jQueryUIUtils;
}();