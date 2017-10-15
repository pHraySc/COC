$(function(){
	resizeDropct();
	$(window).resize(resizeDropct);
	//标签拖动
	//$(".canDrag").draggable({ revert: "invalid", helper: "clone" });
	//释放标签
	dropTag();
	//拖动括号
	dragParenthesis();
	//点击切换“且”、“或”时，屏蔽右键菜单
	$("#tag_dropable").bind("contextmenu",function(e){  
		return false;  
	});
});
function firsttime_tip_remove(ts){
	$(ts).parents(".firsttime_tip").remove();
}
function resizeDropct(){
	var win_width=$(window).width();
	if('undefined' == typeof(document.body.style.maxHeight) && $(window.parent.document).find("iframe#main").length>0){
		var win_width=$(window).width()-92;
	}
	$(".tag_operation").width(win_width-8);
	var tag_dropable_minwidth=win_width-47-$(".tag_operation .btns").width()-16-90;
	if($(".tag_operation_hided").length>0){
		$(".tag_operation_hided .tag_dropable").attr("minwidth",tag_dropable_minwidth).width(tag_dropable_minwidth);
	}else{
		$(".tag_operation .tag_dropable").width("100%");
	}
	
}


function expandTagperation(t){
	var parent=$(t).parents(".tag_operation");
	if(parent.hasClass("tag_operation_hided")){
		parent.removeClass("tag_operation_hided");
		parent.find(".tag_dropable").width("auto").css("width","100%");
		//parent.find("> .tag_dropCT").addClass("tag_dropCT_min");
		$(t).addClass("icon_expanded");
		$(t).html("收起");
		$("body").addClass("tag_operation_body_high");
	}else{
		parent.addClass("tag_operation_hided");
		var dropable=parent.find(".tag_dropable");
		dropable.width(dropable.attr("minwidth"));
		$(t).removeClass("icon_expanded");
		$(t).html("展开");
		$("body").removeClass("tag_operation_body_high");
	}
}

function sync_html(p,c){
	var html=p.html();
	c.html("").append(html);
}

//标签拉绳效果
function setTagPos(){
	var tag_first_x=0;
	var _ct=$("#tag_curtain_CT");
	var tags=_ct.find(".tag");
	var container_width=_ct.width();
	if('undefined' == typeof(document.body.style.maxHeight)){
		container_width=_ct.width()-92;
	}
	var distance=(container_width-65)/tags.length;
	distance>119?distance=120:"";
	tags.each(function(i){
		var _padding=$(this).prev(".tag_padding");
		var padding_h=80;
		//var flag=parseInt(Math.random()*22+1);
		if(i%2!=0){
			padding_h=20//+flag;
		}else{
			padding_h=60//+flag;
		}
		_padding.height(padding_h).css("left",tag_first_x+54);
		$(this).css({"left":tag_first_x,"top":padding_h});
		tag_first_x+=distance;
	});
	tags.mouseenter(function(){
		var _zindex=$(this).css("z-index");
		$(this).attr("zindex",_zindex).css({"z-index":200});
		$(this).prev(".tag_padding").attr("zindex",_zindex).css({"z-index":200});
		if(!$(this).hasClass("tag_active")){
			$(this).addClass("tag_active mouseover_active").prev().addClass("tag_padding_active");
		}
	}).mouseleave(function(){
		var _zindex=$(this).attr("zindex");
		$(this).attr("zindex","").css({"z-index":_zindex});
		$(this).prev(".tag_padding").attr("zindex","").css({"z-index":_zindex});
		if(!$(this).hasClass("click_active")){
			$(this).removeClass("tag_active").prev().removeClass("tag_padding_active");
		}
	})/*.click(function(){
		
		var _p=$(this).prev(".tag_padding");
		if($(this).hasClass("click_active")) return;
		_p.addClass("tag_padding_active").siblings(".tag_padding_active").removeClass("tag_padding_active");
		var _this=$(this);
		
		var _beginHeight=_p.height();
		_p.animate({height: (_beginHeight+20)+"px"}, 180,function(){
			_p.animate({height: _beginHeight+"px"}, 180);
		});
		$(this).animate({top: (_beginHeight+20)+"px"}, 180,function(){
			$(this).animate({top: _beginHeight+"px"}, 180);
			_this.addClass("tag_active click_active").siblings(".tag_active,.click_active").removeClass("tag_active click_active");
		});
		
	})*/
}


function dragParenthesis(){
	var parenthesis=$("#parenthesis > div");
	parenthesis.draggable({
		revert: "invalid",
		scope: "parenthesis",
		cursor:'pointer',
		cursorAt:{left:-4,top:-10},
		helper:function(){
			var cls=$(this).parent().hasClass("opened")?"dragingRightPar":"dragingLeftPar";
			var n=$("<span class='"+cls+"'></span>");
			n.appendTo('body');
			return n;
		},
		start:function(){
			parenthesis_possibility_position($(this));
		},
		drag:function(){},
		stop:function(source){
			$("#tag_dropable .dragTempCT").remove();
		}
	});
}
function parenthesis_possibility_position(_t){
	//若果没有单独的左边括号
	if(!_t.parent().hasClass("opened")){
		var tarAry=$("#tag_dropable .conditionCT,#tag_dropable .leftParenthesis");
		tarAry.each(function(){
			var dragTempCT=$('<div class="dragTempCT"></div>');
			$(this).before(dragTempCT);
			dragTempCT.droppable({
				greedy:true,
				scope: "parenthesis",
				activeClass: "ui-state-active",
				hoverClass: "dragEnter",
				drop: function( event, ui ) {
					var uuid = newGuid();
					var span=$(getCurlyBraceHTML('(',1,uuid));
					$(this).replaceWith(span);
					_t.parent().addClass("opened");
					$("#tag_dropable .dragTempCT").remove();
				}
			})
		})
	}//endof 若果没有单独的左边括号
	else{
		var wait=$("#tag_dropable .waitClose");
		var totalAry=[];
		if(wait.prev().hasClass("leftParenthesis")){
			var creat=wait.prev().attr("creat");
			var stopAry=wait.nextAll(".conditionCT,.parenthesis");
			for(var k=0;k<stopAry.length;k++){
				if($(stopAry[k]).attr("creat")==creat) break;
				totalAry.push(stopAry[k]);
			}
		}
		else{
			var stopAry=wait.nextAll(".conditionCT,.parenthesis");
			for(var k=0;k<stopAry.length;k++){
				if(!$(stopAry[k]).hasClass("conditionCT") && stopAry.filter("[creat='"+$(stopAry[k]).attr("creat")+"']").length<2) break;
				totalAry.push(stopAry[k]);
			}
		}
		var tarAry=[];
		$(totalAry).each(function(){
			var ary=[];
			var ary2=$(this).prevAll(".conditionCT,.parenthesis").andSelf();
			for(var i=ary2.length-1;i>=0;i--){	
				if($(ary2[i]).hasClass("waitClose")) break;
				else{
					ary.push($(ary2[i]));
				}
			}
			var left=[]
			for(var i=ary.length-1;i>=0;i--){
				//$("#easyButton").append('<div style="color:green">'+$(ary[i]).attr("class")+'</div>');
				if($(ary[i]).hasClass("leftParenthesis")){
					left.push($(ary[i]));
				}
				else if($(ary[i]).hasClass("rightParenthesis")){
					//right.push($(ary[i]));
					for(var j=0;j<left.length;j++){
						if( $(left[j]).attr("creat")==$(ary[i]).attr("creat") ){
							left=left.slice(0,j).concat(left.slice(j+1,left.length));
							break;
						}
					}
				}
				else{}
			}
		
			if(left.length==0 &&( $(this).hasClass("conditionCT")||$(this).hasClass("rightParenthesis")) ){
				tarAry.push($(this));
			}
		})
		$(tarAry).each(function(){
			$(this).after('<div class="dragTempCT"></div>');
			$(this).next().droppable({
				greedy:true,
				scope: "parenthesis",
				activeClass: "ui-state-active",
				hoverClass: "dragEnter",
				drop: function( event, ui ) {
					//$(this).replaceWith('<span class="parenthesis rightParenthesis" creat="'+$("#easyConDropDisplay .waitClose").attr("creat")+'"<span class="delCondition" onclick="deletePar(this,e)"></span></span>');
					var span=$(getCurlyBraceHTML(')',1,$("#tag_dropable .waitClose").attr("creat")),true);
					$(this).replaceWith(span);
					$("#tag_dropable .waitClose").removeClass("waitClose");
					_t.parent().removeClass("opened");
					$("#tag_dropable .dragTempCT").remove();
				}
			})
		})
	}//endof 有单独的左括号
}




//封装括号HTML
var createdLeftPar=new Array();
function getCurlyBraceHTML(flag,count,uuidObj,needDelete){
	var tmp="";
	var htmlText = "";
	if(flag=="("){
		tmp = "<span class='parenthesis leftParenthesis waitClose' creat='@' selfid='curly_brace_block'><input type='hidden' value='" + flag + "' /></span>";
	}
	else{

		if(needDelete == true || needDelete==null) {
			tmp = "<span class='parenthesis rightParenthesis' creat='@' selfid='curly_brace_block'><input type='hidden' value='" + flag + "' /><span class='delCondition' onclick='deletePar(this,event)'></span></span>";
		} else {
			tmp = "<span class='parenthesis rightParenthesis' creat='@' selfid='curly_brace_block'><input type='hidden' value='" + flag + "' /></span>";
		}
	}
	
	var rslt="";
	if(uuidObj){
		rslt=tmp.replace('@',uuidObj);
	}
	else{
		for(var i=0;i<count;i++){
			if(flag=="("){
				var uuid = newGuid();
				var replaced=tmp.replace('@',uuid);
				rslt+=replaced.replace('waitClose','');
				createdLeftPar.push(uuid);
			}
			else{
				rslt+=tmp.replace('@',createdLeftPar.pop());
			}
		}	
	}
	
	var tmpCT=$("<div></div>").append(rslt);
	var str=$(tmpCT).html();
	$(tmpCT).remove();
	htmlText = htmlText + str;
	return htmlText;
}
//删除条件 同时删除关联的连接符和括号
function deleteThis(obj){
	//删除匹配的括号【与条件直接关联的括号】
	deleteCurlyBraces(obj);
	//删除关联的连接符
	deleteConnectFlags(obj);
	//删除自身
	$(obj).parent().parent().remove();

}
//删除连接符
function deleteConnectFlags(obj){
	var thisConditionCT=$(obj).parents(".conditionCT");
	var pre = thisConditionCT.prev();
	var nex = thisConditionCT.next();
	if(pre.hasClass("waitClose")){
		pre.prev().remove();
		pre.remove();
		$("#parenthesis").removeClass("opened");
		if(thisConditionCT.prevAll(".conditionCT").length == 0){
			thisConditionCT.next(".chaining").remove();
		}
	}
	else{
		if(pre.hasClass("chaining")){
			pre.remove();
		}else if(nex.hasClass("chaining")){
			nex.remove();
		}
	}
}
//删除1个或多个连续的相匹配的{或}
function deleteCurlyBraces(obj){
	var pre = $(obj).parent().prev();
	var nex = 	$(obj).parent().next();
	if(pre.attr('selfid') == 'curly_brace_block' && nex.attr('selfid') == 'curly_brace_block'){
		pre.remove();
		nex.remove();
		deleteCurlyBraces(obj);//递归删除
	}else{
		return;
	}
}
//括号删除按钮触发事件处理函数
function deletePar(t,evt){
	var e=evt||window.event;
	e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
	$(".onDelPar").removeClass("onDelPar");
	$(t).addClass("onDelPar");
	var posX=$(t).offset().left+9;
	var posY=$(t).offset().top+7;
	if($("body > #delPar").length==0){
		var _ul=$('<ul id="delPar"><li><a href="javascript:void(0)">删除括号</a></li><li><a href="javascript:void(0)">删除括号与内容</a></li></ul>');
		_ul.appendTo("body");
		$(document).click(function(){$("#delPar").hide()});
		$("#delPar li").eq(0).bind("click",delThisPars);
		$("#delPar li").eq(1).bind("click",delThisParsAndCT);
	}
	var tar=$("#delPar"),winW=$(window).width(),tarW=tar.width();
	if(posX+tarW>winW) posX=winW-tarW-6;
	tar.css({"left":posX+"px","top":posY+"px"}).slideDown("fast");
}
//删除括号的处理函数
function delThisPars(){
	var creat=$("body .onDelPar").parent().attr("creat");
	if($(".onDelPar").parent().prev().attr("creat") && $(".onDelPar").parent().prev().attr("creat") == creat){
		//删除前面或后面的连接符
		deleteConnectFlag($(".onDelPar").parent());
	}
	$(".onDelPar").parent().siblings("[creat='"+creat+"']").remove();
	$(".onDelPar").parent().remove();
	$("#delPar").hide();
}
//仅删除括号的处理函数
function onlyDelThisPars(){
	var creat=$("body .onDelPar").parent().attr("creat");	
	$(".onDelPar").parent().siblings("[creat='"+creat+"']").remove();
	$(".onDelPar").parent().remove();
	$("#delPar").hide();
}
//删除括号及整个块的处理函数
function delThisParsAndCT(){
	var creat=$("body .onDelPar").parent().attr("creat");
	//遍历删除括号内部所有的块
	var ary=$(".onDelPar").parent().siblings("[creat='"+creat+"']").nextAll();
	for(var i=0;i<ary.length;i++){
		if(ary.eq(i).attr('creat') == creat ){
			break;
		}
		else{
			ary.eq(i).remove();
		}
	}
	var nextObj = $(".onDelPar").parent().next();
	//删除前面或后面的连接符
	deleteConnectFlag($(".onDelPar").parent());
	//调用删除括号的方法
	onlyDelThisPars();

	//处理剩余空括号
	if( nextObj.attr('creat') && nextObj.attr('creat') == nextObj.prev().attr('creat')){
		delEmptyParsAndCF(nextObj);
	}
}
//循环删除空括号和前面的连接符
function delEmptyParsAndCF(obj){
	//删除前面或后面的连接符
	deleteConnectFlag(obj);
	
	var nextObj = obj.next();
	//删除自己和与自己配对的括号
	obj.prev().remove();
	obj.remove();
	
	//判断如果后面是括号，则循环执行删除
	if(nextObj.attr('creat') && nextObj.attr('creat') == nextObj.prev().attr('creat')){
		delEmptyParsAndCF(nextObj);
	}else{
		return;
	}
}
//删除连接符号
function deleteConnectFlag(obj){
	if(obj.prev().prev().hasClass("chaining")){
		obj.prev().prev().remove();
	}
	if(obj.prevAll(".conditionCT").length == 0 || obj.prev().prev().attr('selfid')=='curly_brace_block'){
		obj.next(".chaining").remove();
	}
}
//生成唯一标识
function newGuid(){ 
	var guid = ""; 
	for (var i = 1; i <= 32; i++){ 
		var n = Math.floor(Math.random()*16.0).toString(16); 
		guid += n; 
		if((i==8)||(i==12)||(i==16)||(i==20)) 
			guid += "-"; 
	} 
	return guid; 
} 
function changeColorWhenDrag(){
	var tar=$("body > .tag_operation");
	tar.addClass("tag_operation_tipbg");
	setTimeout(function(){
		tar.removeClass("tag_operation_tipbg");
	},200)
}