//导航切换效果插件
(function($){
	$.fn.hoverForIE6=function(option){
		var s=$.extend({current:"hover",delay:10 ,thirdNavBox :"secondNavListBox" },option||{}); //thirdNavBox 三级菜单外层DIV
		$.each(this,function(){
			var timer1=null,timer2=null,flag=false;
			$(this).bind("mouseover",function(){
				if (flag){
					clearTimeout(timer2);
				}else{
					var _this=$(this);
					timer1=setTimeout(function(){
						_this.addClass(s.current);
						flag=true;
					},s.delay);
				}
				//判断二级导航栏高度问题:
                if(s.thirdNavBox){
				   var documentHeight = $(document.body).height();
				   var thirdNavHeight = $(this).find("."+s.thirdNavBox).height()+  $(".viewNavListBox").offset().top + 10;
				   if(documentHeight < thirdNavHeight){
				      $(document.body).height(thirdNavHeight);
				   }
				}
                var viewNavListBoxHeight = $(this).parents(".viewNavListBox").height() - 2;
                $(this).find(".secondNavListBox").css("min-height",viewNavListBoxHeight+"px");

			}).bind("mouseout",function(){
				if (flag){
					var _this=$(this);timer2=setTimeout(function(){
						_this.removeClass(s.current);
						flag=false;
					},s.delay);
				}else{
					clearTimeout(timer1);
				}
			})
		})
	}
})(jQuery);