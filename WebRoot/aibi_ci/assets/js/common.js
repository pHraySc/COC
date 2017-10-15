
var commonUtil = {
	
	//ajax请求等待状态
	ajaxLoading:function(obj){
		var  loadingHtml = "<div class='loading' id='loading'><span class='f14'><i class='loadIcon'></i>正在加载…</span></div>"; 
		$("#loading").remove();
		$(obj).append(loadingHtml);
		$("#loading").ajaxStart(function(){
			$(this).show();
		}).ajaxComplete(function(event,request, settings){
			$(this).hide();
		}).ajaxError(function(event,request, settings){
		     $(this).append("加载失败:" + settings.url);
		});
	},
	//去除字符中所有空格
	trim : function(str,is_global){
         var result; 
         result = str.replace(/(^\s+)|(\s+$)/g,"");
         if(is_global && is_global.toLowerCase()=="g")
            result = result.replace(/\s/g,"");
         return result;
    },
	//将字符串转换成json对象
	strToJson:function(str){
		var json = '';
		try{
			json = eval('(' + str + ')');
		}catch(e){}
		
		return json;
	},
	//将json对象转换成字符串
	jsonToStr:function(json){
		if(commonUtil.isMap(json)){
			str = '{';
			var array = [];
			for(var name in json){
				if(json.hasOwnProperty(name)){
					var pair = '"' + name + '":' + commonUtil.jsonToStr(json[name]);
					array.push(pair);
				}
			}
			str += array.join(',') + '}';
			return str;
		}
		if(commonUtil.isArray(json)){
			var str = '[';
			var array = [];
			for(var i=0; i<json.length; i++){
				var obj = commonUtil.jsonToStr(json[i]);
				array.push(obj);
			}
			str += array.join(',') + ']';
			return str;
		}
		return json;
	},
	
	//判断obj元素是否为空，其中obj为jquery对象
	isEmpty:function(obj){
		if(obj && obj.size() > 0){
			return false;
		}
		return true;
	},
	
	//判断字符串是否为空
	isBlank:function(text){
		if(text && text.length > 0){
			return false;
		}
		return true;
	},
	isArray:function(obj){
		var str = Object.prototype.toString.call(obj);
		if(str == '[object Array]'){
			return true;
		}else{
			return false;
		}
	},
	//dialog
	create_dialog:function(id, title, contentUrl, options) { 
		options = options || {};
		var dlg = $('#' + id);
		if (dlg.length == 0) {
			$('body').append( '<div id="'+ id +'"></div>');
			dlg = $('#' + id);;
			
		}
		dlg.dialog( {
			modal : true,
			title : title,
			height:options.height?options.height:"auto",
			width:options.width?options.width:776,
			position:options.position?options.position:['center','center'],
			buttons : options.buttons ? options.buttons : {
				'确定' : function() {
					dlg.dialog('close');
				}
			}
		});
		AjaxUtils.sendAjax(contentUrl, {}, function(content) {
			dlg.dialog('option', 'buttons', options.buttons);
//			console.log(content);
			dlg.html(content);
			dlg.dialog('open');
		}); 
	},
	/**
	 * 系统提示
	 * @param option = {} 系统信息提示参数param的参数例子//{"test":"23","aaa":"asr"}或param
	 * @param callbackFn一定要以String方法传递
	 * 
	 */
	create_alert_dialog:function(id,option,callbackFn) {  
		var result = [];
		var defaults ={};
		option = $.extend(defaults,option); 
		if(typeof option.param == "object"){
		    for(var item in option.param){
		    	if(option.param[item] instanceof Function){
		    		option.param["backFunc"] =  option.param[item]; 
		    	}else{
		    		 result.push("'"+option.param[item]+"'");
		    	} 
		    }
		   option.param = option.isFunc? option.param : result.toString();
		} else {
			option.param = "'"+ option.param  +"'";  
		}
		var src ="";
		var _y = "center";
		var _x = "center"; 
		var buttonObj = {
				"确定" : function() {
					if(callbackFn){ 
						if(option.param){
							if(option.isFunc){
								callbackFn(option.param);
							}else{
								eval("("+callbackFn+"("+option.param+")"+")");
							}
						}else{
							eval("("+callbackFn+"()"+")");
						}
					}
					$(this).dialog('close');
				} 
			};
		var html = "";
		if(option.type == "success"){
			 html = '<div class="dialog_table" style="padding-top:23px;"><table width="100%" class="showTable" cellpadding="0" cellspacing="0">'
				    +'<tr><td class="app_td" style="padding-top:0px;padding-bottom:20px;"><div class="sucess showtxt" >'+option.txt+'</div></td></tr></table></div>';
		}else if(option.type == "failed"){
			 html = '<div class="dialog_table"><table width="100%" class="showTable" cellpadding="0" cellspacing="0"><tr>'
					+'<td class="app_td" style="padding-top:0px;padding-bottom:20px;"><div class="failed showtxt" >'+option.txt+'</div></td></tr></table></div>';
		}else if(option.type == "confirm"){
			 html = '<div class="dialog_table" style="padding-top:23px;"><table width="100%" class="showTable" cellpadding="0" cellspacing="0">'
				 	+'<tr><td class="app_td" style="padding-top:0px;padding-bottom:20px;"> <div class="confirm showtxt" >'+option.txt+'</div></td></tr></table></div>';
			 buttonObj = {
				"确定" : function() {
					if(callbackFn){
						if(option.param){
							if(option.isFunc){
								callbackFn(option.param);
							}else{
								eval("("+callbackFn+"("+option.param+")"+")");
							}
						}else{
							eval("("+callbackFn+"()"+")");
						}
					}
					$(this).dialog('close');
				},
				"取消": function() {
					$(this).dialog('close');
				}
			};
		} 
		if(option.pagescroll_y >= 0){
			_y = option.pagescroll_y+220;
			_x = $(window).width()/2-_width/2;
		} 
		
		var dlg = $('#' + id);
		if (dlg.length == 0) {
			$('body').append( '<div id="'+ id +'"></div>');
			dlg = $('#' + id);
			
		}
		 dlg.html(html).dialog({ 
			modal: true,
			autoOpen: true,
			width: option.width || 500,
			height:option.height || 200,
			title:"系统提示",
			position: [_x,_y],
			resizable:false,
			open : function(event,ui){
				var button = $(".ui-dialog-buttonset").find(".ui-button").eq(1);
				if(button.length>0){
					button.addClass("cancelBtn");
				}
			},
			close:function(event,ui){
				$(this).dialog("destroy");
				
			},
			buttons : buttonObj 
		});
		return dlg;
	},

	
	getAssembleUrl:function(href,text,onclick){
		var str = "<a href=" + href;
		if(onclick){
			str += " onclick=" + onclick;
		}
		str += ">" + text + "</a>";
		return str;
	},

			
	displayImage:function(orignal,dataURL){
		var img = document.createElement('img');
		img.src = dataURL;
		$(img).addClass('ims');
		orignal.replaceWith(img);				
	},
	
	stopPropagation:function(e){
		if(e.stopPropagation){
			e.stopPropagation();
		}
		if(e.cancelBubble){
			e.cancelBubble();			
		}
	},
	/***********
	 * 日期格式化
	 *  @param  date可是Date对象也可以是String
	 *  @param  fmt就是要转换的格式
	 *  @param  isDate是否是Date对象true OR false 非必填参数
	 */
	dateFormat : function(date, fmt,dateType) { 
		var ths = date;
		if(dateType){
			ths = this.toDate(date,dateType);
		}
		var o = {         
		    "M+" : ths.getMonth()+1, //月份         
		    "d+" : ths.getDate(), //日         
		    "h+" : ths.getHours()%12 == 0 ? 12 : ths.getHours()%12, //小时         
		    "H+" : ths.getHours(), //小时         
		    "m+" : ths.getMinutes(), //分         
		    "s+" : ths.getSeconds(), //秒         
		    "q+" : Math.floor((ths.getMonth()+3)/3), //季度         
		    "S" : ths.getMilliseconds() //毫秒         
		};         
	    var week = {         
		    "0" : "/u65e5",         
		    "1" : "/u4e00",         
		    "2" : "/u4e8c",         
		    "3" : "/u4e09",         
		    "4" : "/u56db",         
		    "5" : "/u4e94",         
		    "6" : "/u516d"        
	    };         
	    if(/(y+)/.test(fmt)){         
	        fmt=fmt.replace(RegExp.$1, (ths.getFullYear()+"").substr(4 - RegExp.$1.length));         
	    }         
	    if(/(E+)/.test(fmt)){         
	        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[ths.getDay()+""]);         
	    }         
	    for(var k in o){         
	        if(new RegExp("("+ k +")").test(fmt)){         
	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
	        }         
	    }         
	    return fmt;
	},
	/***********
	 * string to Date()
	 *  @param  str string 日期
	 *  @param  fmt是被转日期的格式，非正常日期格式例如yyyyMMdd
	 *  @param  notDate 非正常日期格式 true OR false 一般不要传值
	 */
	toDate:function(str,format) {
        pattern = format.replace("yyyy", "(\\~1{4})").replace("yy", "(\\~1{2})").replace("MM", "(\\~1{2})").replace("M", "(\\~1{1,2})").replace("dd", "(\\~1{2})").replace("d", "(\\~1{1,2})").replace(/~1/g, "d");
        var returnDate;
        if (new RegExp(pattern).test(str)) {
            var yPos = format.indexOf("yyyy");
            var mPos = format.indexOf("MM");
            var dPos = format.indexOf("dd");
            if (mPos == -1) mPos = format.indexOf("M");
            if (yPos == -1) yPos = format.indexOf("yy");
            if (dPos == -1) dPos = format.indexOf("d");
            var pos = new Array(yPos + "y", mPos + "m", dPos + "d");//.sort();
            var data = { y: 0, m: 0, d: 0 };
            var m = str.match(pattern);
            for (var i = 1; i < m.length; i++) {
 
                if (i == 0) return;
                var flag = pos[i - 1].split('')[1];
                data[flag] = m[i];
            };
 
            if (data.y.toString().length == 2) {
                data.y = parseInt("20" + data.y);
            }
            data.m = data.m - 1;
			if(data.d == 0){
				returnDate = new Date(data.y, data.m);
			}else{
				returnDate = new Date(data.y, data.m, data.d);
			}
            //returnDate = new Date(data.y, data.m, data.d);
        }
        if (returnDate == null || isNaN(returnDate)) returnDate = new Date();
        return returnDate;
    }
};


