//显示最近搜索tip      kongly
function showSearchTip(userId) {
	var searchTipStr = getCookieByName('searchTip_'+userId);
	if(searchTipStr!=null){
		searchTipStr = unescape(searchTipStr);
		var searchTipArray = searchTipStr.split('|');
		    searchTipArray = searchTipArray.reverse();
		    $("#searchHistoryList").empty();
		 for(var i=0;i<searchTipArray.length;i++){
			var $li = $("<li></li>");
			var $a = $("<a  href=\"javascript:void(0);\">"+ searchTipArray[i] + "</a>");
			$a.click(function(){
				var keyStr = escape2Html($(this).html());
				 $("#keyword").val(keyStr);
				 $("#keyword").attr("data-val","true");//给输入框赋值时 要把data-val属性赋值为true
				 $("#searchHistoryList").parent().slideUp(500);
			})
			$li.append($a);
			$("#searchHistoryList").append($li);
		  }
		  //最近搜索的样式要套
		 $("#searchHistoryBox").show();
	  }
	return false;
}


//更新最近搜索tip的方法，在点击搜索按钮时调用         kongly
function updateSearchTip(searchKeyWord,userId) {
	var searchTipArray = new Array(10);
	if(searchKeyWord!=null && $.trim(searchKeyWord).length>0 ){
		searchKeyWord = $.trim(searchKeyWord);
		if(searchKeyWord == "请输入关键字"){
			return;
		}
	}else{
		return;
	}
	var searchTipStr = getCookieByName('searchTip_'+userId);
	if(searchTipStr!=null){
		searchTipStr = unescape(searchTipStr);
		searchTipArray = searchTipStr.split('|');
		//1.删除重复的keyword
		var deleteIndex;
		for(var i=0;i<searchTipArray.length;i++){
			if(searchTipArray[i] == searchKeyWord){
				deleteIndex = i;
				break;
			}
		}
		if(deleteIndex>-1){
			searchTipArray.splice(deleteIndex,1);
		}
		
		//2.将新传入的keyword加入到数组里并取得现在的数组长度
		var nowLength = searchTipArray.push(searchKeyWord);
		
		//3.如果长度超过10，将最早的元素删除
		if(nowLength>10){
			searchTipArray.splice(0,1);
		}
		
		//4.将最新的数组转成字符串放入cookie
		addCookie('searchTip_'+userId,searchTipArray.join("|"));
	}else{
		addCookie('searchTip_'+userId,searchKeyWord);
	}
}

function getCurrentUserId()
{	
	var currentUserId;
	var actionUrl = $.ctx + "/ci/customersManagerAction!findCurrentUserId.ai2do";
	$.ajax({
		type: "POST",
		url: actionUrl,
		async:false,
		success: function(result){
			currentUserId = result;
		}
	});
	return currentUserId;
}

//读取cookies kongly
function getCookieByName(name){
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg)){
        return (arr[2]);
    }else{
    	return null;
    }
}

//写入cookie   kongly
//expiresHours过期时间，单位是小时
function addCookie(name,value){ 
	var Days = 30;
	var exp = new Date(); 
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	var cookieString=name+"="+escape(value); 
	document.cookie=cookieString + ' ;expires=' + exp.toGMTString()+ ' ;path=/';
}
//显示最近搜索tip      
function showSearchTipInfo(userId,keyId,divId,divParentId) {
	var searchTipStr = getCookieByName('searchTip_'+userId);
	if(searchTipStr!=null){
		searchTipStr = unescape(searchTipStr);
		var searchTipArray = searchTipStr.split('|');
		    searchTipArray = searchTipArray.reverse();
		    $("#"+divId).empty();
		 for(var i=0;i<searchTipArray.length;i++){
			var $li = $("<li></li>");
			var $a = $("<a  href=\"javascript:void(0);\">"+ searchTipArray[i] + "</a>");
			$a.click(function(){
				 $("#"+keyId).val($(this).html());
				 $("#"+keyId).attr("data-val","true");
				 $("#"+divId).parent().slideUp(500);
			})
			$li.append($a);
			$("#"+divId).append($li);
		  }
		  //最近搜索的样式要套
		 $("#"+divParentId).show();
	  }
	return false;
}

function escape2Html(str) {
	 var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
	 return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
}