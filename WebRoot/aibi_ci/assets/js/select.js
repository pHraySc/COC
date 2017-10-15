$(document).ready(function(){
	//动画速度 
	var speed = 200;
	$(".likeSelect").click(function (event) { 
		event.stopPropagation(); 
		//设置弹出层位置 
		var offset = $(this).offset(); 
		
		//依据 input 设定下拉框的宽度
		$(this).nextAll(".x-form-field").width($(this).width() + 32); 
		$(".x-form-field li").width($(this).width() + 22);
		//鼠标移开隐藏弹出层 
		$(document).click(function (event){ $(".x-form-field").hide(speed) }); 
		
		//设定下拉选单显示的位置 
		$(this).nextAll(".x-form-field").css({ top: offset.top + 23, left: offset.left }); 
		//切换弹出层的显示状态 
		$(this).nextAll(".x-form-field").toggle(speed); 
		
		$(".x-form-field ul li:odd").addClass("odd");	
		if($(".x-form-field").height() > "120"){
			$(".x-form-field").height("120");
		}
		$(".x-form-field ul li").click(function(){
			$(".x-form-field ul li").removeClass("on");
			$(this).addClass("on");
			$(this).parent("ul").parent("div").prev("input").prev("input").val($(this).html()); 
			$(this).parent("ul").parent("div").prev("input").val($(this).attr("id"));
			$(this).parent("ul").parent("div").hide(speed)
		});
		
	});

});




function aibiSelect(btn,divBox){
	//动画速度 
	var speed = 200;
	var width = $(btn).children("input").width() + $(btn + " .btn_img").width(); 
	$(btn).width(width+15);
	$(btn).click(function (event) { 
		//取消事件冒泡 
		event.stopPropagation(); 
		//设置弹出层位置 
		var offset = $(btn).offset(); 
		var width = $(btn).children("input").width() + $(btn + " .btn_img").width(); 
		
		//依据 input 跟 button宽度设定下拉框的宽度
		$(divBox).width(width + 8); 
		//鼠标移开隐藏弹出层 
		$(document).click(function (event) { $(divBox).hide(speed) }); 
		
		//设定下拉选单显示的位置 
		$(divBox).css({ top: offset.top + 23, left: offset.left }); 
		//切换弹出层的显示状态 
		$(divBox).toggle(speed); 
	});
	$(divBox + " ul li:odd").addClass("odd");
	if($(divBox).height() > "120"){
		$(divBox).height("120");
	}
	$(divBox + " ul li").click(function(){
		$(divBox + " ul li").removeClass("on");
		$(this).addClass("on");
		$(btn).children("input").val($(this).html()); 

	});
};
function aibiSelectSearch(btn,divBox){
	//动画速度 
	var speed = 200;
	var width = $(btn).prev("input").width() + $(btn).width(); 
	$(btn).parent("div").width(width+15);
	$(btn).click(function (event) { 
		//取消事件冒泡 
		event.stopPropagation(); 
		//设置弹出层位置 
		var offset = $(btn).parent("div").offset(); 
		
		//依据 input 跟 button宽度设定下拉框的宽度
		$(divBox).width($(btn).prev("input").width() + 8); 
		//单击空白区域隐藏弹出层 
		$(document).click(function (event) { $(divBox).hide(speed) }); 
		//设定下拉选单显示的位置 
		$(divBox).css({ top: offset.top + 23, left: offset.left }); 
		//切换弹出层的显示状态 
		$(divBox).toggle(speed); 
	});
	$(divBox + " ul li:odd").addClass("odd");
	if($(divBox).height() > "120"){
		$(divBox).height("120");
	}
	$(divBox + " ul li").click(function(){
		$(divBox + " ul li").removeClass("on");
		$(this).addClass("on");
		$(btn).prev("input").val($(this).html()); 
	});
};