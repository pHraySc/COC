/********
 *  2015-1-27
 *	智能分析 透视分析 过滤器 
 */
filterSetting=function() { 
	var filterSetting={
			initValue:function(page,attrId){
				if(page=="date"){
					filterSetting._date_initValue(page,attrId);
				}else if(page=="darkValue"){
					filterSetting._darkValue_initValue(page,attrId);
				}else if(page=="number"){
					filterSetting._number_initValue(page,attrId);
				}
			},
			//确定按纽 绑定事件
			buttonEnsure:function(eventData){
				var type=eventData.type;
				var attrId=eventData.attrId;
				var clickSource=eventData.clickSource;
				if(type=="date"){
					filterSetting._data_buttonEnsure(type,attrId,clickSource);
				}else if(type=="darkValue"){
					filterSetting._darkValue_buttonEnsure(type,attrId,clickSource);
				}else if(type=="number"){
					filterSetting._number_buttonEnsure(type,attrId,clickSource);
				}
				
			},
			//日期过滤窗口 确定按纽 事件
			_data_buttonEnsure:function(page,attrId,clickSource){
				if(filterSetting._date_validateForm()){
					var queryWay ="";
					$(".methodSel").each(function(){
						if($(this).hasClass("methodSelCurrent")){
							queryWay= $(this).attr("value")
						}
					});
					var startTime = "";
					var endTime= "";
					var dateLeftChecked= "";
					var leftZoneSign=">=";
					var rightZoneSign="<";
					var exactValueDate="";
					var temp = "";
					var isNeedOffset = 0;
					if(queryWay == "1"){
						startTime = $("#startTime").val();
						endTime= $("#endTime").val();
					}else{
						var exactValueDateYear = $.trim($("#exactValueDateYear").val());
						var exactValueDateMonth = $.trim($("#exactValueDateMonth").val());
						var exactValueDateDay = $.trim($("#exactValueDateDay").val());
						if(exactValueDateYear && exactValueDateYear !=""){
							temp += exactValueDateYear+"年";
							exactValueDate=exactValueDateYear;
						}else{
							exactValueDate="-1";
						}
						if(exactValueDateMonth && exactValueDateMonth !=""){
							temp += exactValueDateMonth+"月";
							exactValueDate += ","+exactValueDateMonth;
						}else{
							exactValueDate += ",-1";
						}
						if(exactValueDateDay && exactValueDateDay !=""){
							temp += exactValueDateDay+"日";
							exactValueDate += ","+exactValueDateDay;
						}else{
							exactValueDate += ",-1";
						}
					}
				
					var _attrModel=$("#" + attrId).data("attrModel");
					var _exclude=_attrModel.exclude;
					if(_exclude){
						for(var i=0;i<_exclude.length;i++){
							_filter=_exclude[i];
							if(_filter.attrId==attrId){
								_filter.startTime=startTime;
								_filter.endTime=endTime;
							}
						}
					}else{
						_exclude=new Array();
						var _filter={
								attrId:attrId,
								startTime:startTime,
								endTime:endTime
						}
						_exclude.push(_filter);
						$("#" + attrId).data("attrModel").exclude=_exclude;
					}
					//关闭窗口
					$("#dialog_filter").dialog("close");
				}
			},
			//模糊值，精确值  界面  确定按纽事件
			_darkValue_buttonEnsure:function(type,attrId,clickSource){
				if(filterSetting._darkValue_validateForm()){
					var darkValue = "";
					var exactValue = "";
					var queryWay="";

					$(".dimMatchMethod").each(function(){
						if($(this).hasClass("current")){
							queryWay= $(this).attr("value")
						}
					});
					if(queryWay == "1"){
						darkValue=$("#darkValue").val();
					}else{
						exactValue=$("#exactValue").val();
					}
					
					var _attrModel=$("#" + attrId).data("attrModel");
					var _exclude=_attrModel.exclude;
					if(_exclude){
						for(var i=0;i<_exclude.length;i++){
							_filter=_exclude[i];
							if(_filter.attrId==attrId){
								_filter.exactValue=exactValue;
								_filter.darkValue=darkValue;
							}
						}
					}else{
						_exclude=new Array();
						var _filter={
								attrId:attrId,
								exactValue:exactValue,
								darkValue:darkValue
						}
						_exclude.push(_filter);
						$("#" + attrId).data("attrModel").exclude=_exclude;
					}
					//关闭窗口
					$("#dialog_filter").dialog("close");
				}
			},
			_number_buttonEnsure:function(type,attrId,clickSource){
				if(filterSetting._number_validateForm()){
					var queryWay="";
					var contiueMinVal = "";
					var contiueMaxVal = "";
					var exactValue = "";
					$(".queryMethod").each(function(){
						if($(this).hasClass("current")){
							queryWay= $(this).attr("value")
						}
					});
					if(queryWay == "1"){
						contiueMinVal=$("#contiueMinVal").val();
						contiueMaxVal=$("#contiueMaxVal").val();
						if(contiueMinVal == "输入下限"){
							contiueMinVal="";
						}
						if(contiueMaxVal == "输入上限") {
							contiueMaxVal="";
						}
					}
					if(queryWay == "2"){
						exactValue=$("#exactValue").val();
					}
					var _attrModel=$("#" + attrId).data("attrModel");
					var _exclude=_attrModel.exclude;
					if(_exclude){
						for(var i=0;i<_exclude.length;i++){
							_filter=_exclude[i];
							if(_filter.attrId==attrId){
								_filter.contiueMinVal=contiueMinVal;
								_filter.contiueMaxVal=contiueMaxVal;
								_filter.exactValue=exactValue;
							}
						}
					}else{
						_exclude=new Array();
						var _filter={
								attrId:attrId,
								contiueMinVal:contiueMinVal,
								contiueMaxVal:contiueMaxVal,
								exactValue:exactValue
						}
						_exclude.push(_filter);
						$("#" + attrId).data("attrModel").exclude=_exclude;
					}
					//关闭窗口
					$("#dialog_filter").dialog("close");
				}
			},
			//日期过滤器窗口  初始化
			_date_initValue:function(page,attrId){
				//日期单选按钮选项
				$(".methodSel").click(function(){
					$(".methodSel").removeClass("methodSelCurrent");
					$(this).addClass("methodSelCurrent");
					var queryWay = $(this).attr("value");
					if(queryWay == "1"){
						$("#startTime").removeAttr("disabled");
						$("#endTime").removeAttr("disabled");
					}else{
						$("#startTime").attr("disabled","disabled");
						$("#endTime").attr("disabled","disabled");
					}
					$("#snameTip").hide();
				})
				$(".methodSel").hover(function(){
					$(this).addClass("methodSelHover");
				},function(){
					$(this).removeClass("methodSelHover");
				});
				
				var _attrModel=$("#" + attrId).data("attrModel");
				var _exclude=_attrModel.exclude;
				if(_exclude){
					for(var i=0;i<_exclude.length;i++){
						_filter=_exclude[i];
						if(_filter.attrId==attrId){
							if(_filter.startTime){
								$("#startTime").val(_filter.startTime);
							}
							if(_filter.endTime){
								$("#endTime").val(_filter.endTime);
							}
						}
					}
				}
			},
			//模糊值，精确值  界面 初始化
			_darkValue_initValue:function(page,attrId){
				var queryWay;
				if(!queryWay){
		 			queryWay="1";
		 		}
				
				$(".dimMatchMethod").click(function(){
					$(".dimMatchMethod").removeClass("current");
					$(this).addClass("current");
					var queryWayTemp = $(this).attr("value");
					$(".dimMatchMethod").parent().siblings("dd").find("input").attr("disabled","disabled");
					$(".current").parent().siblings("dd").find("input").removeAttr("disabled");
					$("#snameTip").hide();
					$("#enameTip").hide();
					$("#newTip").hide();
					if(queryWayTemp == "1"){
						$("#importFontStyleBox").html("输入您想查询的任意内容");
					}else{
						$("#importFontStyleBox").html("多条件用','分割,且不能用','结尾");
					}
				});
				$(".dimMatchMethod").hover(function(){
					$(this).addClass("hover");
				},function(){
					$(this).removeClass("hover");
				});
				
				var _attrModel=$("#" + attrId).data("attrModel");
				var _exclude=_attrModel.exclude;
				if(_exclude){
					for(var i=0;i<_exclude.length;i++){
						_filter=_exclude[i];
						if(_filter.attrId==attrId){
							if(_filter.exactValue){
								$("#exactValue").val(_filter.exactValue);
								queryWay=2;
							}
							if(_filter.darkValue){
								$("#darkValue").val(_filter.darkValue);
								queryWay=1;
							}
						}
					}
				}
				
				if(queryWay){
		 			$(".dimMatchMethod").each(function(){
		 				if($(this).attr("value") == queryWay){
		 					$(this).addClass("current");
		 				}else{
		 					$(this).removeClass("current");
		 				}
		 			});
		 			if(queryWay == "1"){
		 				$("#exactValue").attr("disabled","disabled");
		 				$("#darkValue").removeAttr("disabled");
		 				$("#importFontStyleBox").html("输入您想查询的任意内容");
		 				
		 			}else{
		 				$("#darkValue").attr("disabled","disabled");
		 				$("#exactValue").removeAttr("disabled");
		 				$("#importFontStyleBox").html("多条件用','分割,且不能用','结尾");
		 			}
		 		}
			},
			_number_initValue:function(page,attrId){
				var queryWay = '';
				var contiueMinVal = '';
				var contiueMaxVal = '';
				var exactValue = ''; 
				
				var _attrModel=$("#" + attrId).data("attrModel");
				var _exclude=_attrModel.exclude;
				if(_exclude){
					for(var i=0;i<_exclude.length;i++){
						_filter=_exclude[i];
						if(_filter.attrId==attrId){
							if(_filter.exactValue){
								exactValue=_filter.exactValue;
								queryWay=2;
							}
							if(_filter.contiueMinVal||_filter.contiueMaxVal){
								contiueMinVal=_filter.contiueMinVal;
								contiueMaxVal=_filter.contiueMaxVal;
								queryWay=1;
							}
						}
					}
				}
				
				$(".unitColor").html("");
				if(queryWay == "" || queryWay=="undefined"){
					queryWay="1";
				}
				if(queryWay){
					$(".queryMethod").each(function(){
						if($(this).attr("value") == queryWay){
							$(this).addClass("current");
						}else{
							$(this).removeClass("current");
						}
					});
					if(queryWay == "1"){
						$("#exactValue").attr("disabled","disabled");
						$("#contiueMinVal").removeAttr("disabled");
						$("#contiueMaxVal").removeAttr("disabled");
					}else{
						$("#contiueMinVal").attr("disabled","disabled");
						$("#contiueMaxVal").attr("disabled","disabled");
						$("#leftClosed").attr("disabled","disabled");
					}
				}
				//上线和下线  区间
				if(contiueMinVal){
					$("#contiueMinVal").val(contiueMinVal);
				}
				if(contiueMaxVal){
					$("#contiueMaxVal").val(contiueMaxVal);
				}
				if(exactValue){
					$("#exactValue").val(exactValue);
				}
				//数值查询选项
				$(".queryMethod").click(function(){
					$(".queryMethod").removeClass("current");
					$(this).addClass("current");
					$(".queryMethod").parent().siblings("dd").find("input").attr("disabled","disabled");
					$(".current").parent().siblings("dd").find("input").removeAttr("disabled");
					$("#cnameTip").hide();
					$("#snameTip").hide();
				});
				$(".queryMethod").hover(function(){
					$(this).addClass("hover");
				},function(){
					$(this).removeClass("hover");
				});
				
				var cmin= $.trim($("#contiueMinVal").val());
				if(cmin == "输入下限"){
					$.focusblur("#contiueMinVal");
				}
				var cmax= $.trim($("#contiueMaxVal").val());
				if(cmax == "输入上限"){
					$.focusblur("#contiueMaxVal");
				}
			},
			 //日期设置界面 验证表单
			_date_validateForm:function(){
				var queryWay="";
				$(".methodSel").each(function(){
					if($(this).hasClass("methodSelCurrent")){
						queryWay= $(this).attr("value");
					}
				});
				if(queryWay == "1"){
					var startTime = $.trim($("#startTime").val());
					var endTime= $.trim($("#endTime").val());
					var timeStr=startTime+endTime;
					if($.trim(timeStr) == ""){
						$("#snameTip").empty();
						$("#snameTip").show().append("时间段至少一个不能为空");
						return false;
					}
				}else{
					  
				}
				return true;
			},
			//模糊值，精确值  界面 表单验证
			_darkValue_validateForm:function(){
				var queryWay="";
				$(".dimMatchMethod").each(function(){
					if($(this).hasClass("current")){
						queryWay= $(this).attr("value")
					}
				});
				if(queryWay == "1"){
					var darkValue = $.trim($("#darkValue").val());
					if(darkValue == ""){
						$("#snameTip").empty();
						$("#snameTip").show().append("模糊值不能为空！");
						return false;
					}
				}else {
					var exactValue = $.trim($("#exactValue").val());
					if(exactValue == ""){
						$("#enameTip").empty();
						$("#enameTip").show().append("精确值不能为空！");
						return false;
					}
					if(exactValue.indexOf(",") == 0 || exactValue.lastIndexOf(",") == exactValue.length-1 || exactValue.indexOf(",,") !=-1){
						$("#enameTip").empty();
						$("#enameTip").show().append("请输入合法的值！");
						return false;
					}
				}
				return true;
			},
			//表单验证
			_number_validateForm:function(){
				var queryWay="";
				$(".queryMethod").each(function(){
					if($(this).hasClass("current")){
						queryWay= $(this).attr("value")
					}
				});
				if(queryWay == "1"){
					var contiueMinVal=$.trim($("#contiueMinVal").val());
					var contiueMaxVal=$.trim($("#contiueMaxVal").val());
					if(contiueMinVal == "输入下限"){
						contiueMinVal="";
					}
					if(contiueMaxVal == "输入上限") {
						contiueMaxVal="";
					}
					var contiueVal=contiueMinVal+contiueMaxVal;
					if($.trim(contiueVal) == ""){
						$("#cnameTip").empty();
						$("#cnameTip").show().append("请输入数值！");
						return false;
					}
					var reg = /^(-?([1-9]\d{0,11})|0)(\.\d{1,4})?$/
					if(contiueMinVal !=""){
						if(!contiueMinVal.match(reg)){
							$("#cnameTip").empty();
							$("#cnameTip").show().append("请输入合法的数值！");
							return false;
						}
					}
					if(contiueMaxVal !=""){
						if(!contiueMaxVal.match(reg)){
							$("#cnameTip").empty();
							$("#cnameTip").show().append("请输入合法的数值！");
							return false;
						}
					}
					if(contiueMinVal !="" && contiueMaxVal !=""){
						var maxVal=parseInt($("#contiueMaxVal").val());
						var minVal=parseInt($("#contiueMinVal").val());
						if(maxVal < minVal){
							$("#cnameTip").empty();
							$("#cnameTip").show().append("数值范围上限不能小于下限！");
							return false;
						}
					}
				}else {
					var exactValue = $.trim($("#exactValue").val());
					if(exactValue == ""){
						$("#snameTip").empty();
						$("#snameTip").show().append("精确值不能为空！");
						return false;
					}
					var reg = /^(-?([1-9]\d{0,11})|0)(\.\d{1,4})?(\,(-?([1-9]\d{0,11})|0)(\.\d{1,4})?)*$/;
					if(exactValue !=""){
						if(!exactValue.match(reg)){
							$("#snameTip").empty();
							$("#snameTip").show().append("请输入合法的数值！");
							return false;
						}
					}
				}
				
				return true;
			}
			
	};
	return filterSetting;
}();