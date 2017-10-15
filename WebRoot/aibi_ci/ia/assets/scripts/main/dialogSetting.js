/********
 *  2014-12-19
 *	智能 弹出窗口 
 */
dialogSetting=function() { 
	var dialogSetting={
			/**
			 * 初始化值
			 */
			initValue:function(page,attrId){
				if(page=="pattern"){
					dialogSetting._pattern_initValue(page,attrId);
				}else if(page=="grade"){
					dialogSetting._grade_initValue(page,attrId);
				}else if(page=="aggregation"){
					dialogSetting._aggregation_initValue(page,attrId);
				}else if(page=="customSystemAttr"){
					dialogSetting._customSystemAttr_initValue(page,attrId);
				}
			},	
			/**
			 * 确定按纽事件
			 * @param page
			 * @param attrId
			 * @returns
			 */
			 dialog_buttonEnsure:function(page,attrId,clickSource){
				if(page=="pattern"){
					dialogSetting._pattern_buttonEnsure(attrId);
				}else if(page=="grade"){
					dialogSetting._grade_buttonEnsure(attrId);
				}else if(page=="aggregation"){
					dialogSetting._aggregation_buttonEnsure(attrId);
				}else if(page=="customSystemAttr"){
					dialogSetting._customSystemAttr_buttonEnsure(attrId,clickSource);
				}
			},
			/**
			 * 格化设置窗口初始化值
			 */
			_pattern_initValue:function(page,attrId){
				var _attrModel=$("#" + attrId).data("attrModel");
				var _pattern=_attrModel.guidelineMask;
				if(_pattern){
					$("input:radio[value="+_pattern+"]").attr('checked','true');
				}
				var _title=_attrModel.guidelineTitle;
				if(_title){
					$("input:radio[value="+_title+"]").attr('checked','true');
				}
				var _unit=_attrModel.guidelineUnit;
				if(_unit!="元"&&_unit!="M"&&_unit!=""){
					$("#unit").val("9");
					$("#customUnit").css("display","block").val(_unit);
				}else{
					$("#unit").val(_unit);
				}
			},
			/**
			 * 分档窗口 初始化值 
			 */
			_grade_initValue:function(page,attrId){
				var _group=$("#" + attrId).data("attrModel").group;
				if(_group){
					var len=_group.length;
					if(len==1){
						var _grade=_group[0];
						if(_grade.isIsometry==1){
							$("#radio1").attr('checked','true');
							$("#gradeNum").val(_grade.gradeNum);
						}else{
							$("#radio0").attr('checked','true');
							$("#isIsometry-div input:eq(0)").val(_grade.groupName);
							$("#isIsometry-div input:eq(1)").val(_grade.dimBegin);
							$("#isIsometry-div input:eq(2)").val(_grade.dimEnd);
						}
					}else{
						$("#radio0").attr('checked','true');
						dialogSetting.grade_addNewGrade(_group.length-1);//添加新行
						for(var i=0;i<_group.length;i++){
							var _grade=_group[i];
							$(".J_isIsometry:eq("+i+") input:eq(0)").val(_grade.groupName);
							$(".J_isIsometry:eq("+i+") input:eq(1)").val(_grade.dimBegin);
							$(".J_isIsometry:eq("+i+") input:eq(2)").val(_grade.dimEnd);
						}
					}
				}
				
			},
			/**
			 * 聚合函数窗口 初始化值
			 */
			_aggregation_initValue:function(page,attrId){
				var _aggregationId=$("#" + attrId).data("attrModel").aggregationId;
				if(_aggregationId){
					$("input:radio[value='"+_aggregationId+"']").attr("checked",true);
				}
			},
			/**
			 * 自定义计算字段 初始化
			 */
			_customSystemAttr_initValue:function(page,attrId){
			
				var data=_GlobeObject["publicAttr"] ;	//公有属性
				var $ul=$(".operation-right-li");
				for(var i = 0; i < data.length; i++){
		   			var $li = $('<li class="li">'+data[i].attrName+'</li>').attr("id","attr_"+data[i].attrId);
		   			$ul.append($li);
				}	
				var _name=$("#" + attrId).data("attrModel").attrName;
				var _source=$("#" + attrId).data("attrModel").attrSource;
				
				if($("#" + attrId).data("attrModel").originalAttrName==null){
					$("#" + attrId).data("attrModel").originalAttrName=$("#" + attrId).data("attrModel").attrName;
					$("#" + attrId).data("attrModel").originalAttrId=$("#" + attrId).data("attrModel").attrId;
				}
				
				var _formulaText="";
				var _formulaValue="";
				if(_source=="3"||_source=="4"){	//自定义属性时
					$("#customAttrName").val(_name);
					_formulaValue=$("#" + attrId).data("attrModel").customSystemAttr;
					_formulaText=$("#" + attrId).data("attrModel").customSystemAttrText;
				}else{
					_formulaValue="^"+$("#" + attrId).data("attrModel").attrId+"^";
					_formulaText=_name;
					
				}
				
				$("#customSystemAttrHidden").val(_formulaValue).attr("defaultValue",_formulaValue);
				$("#customSystemAttr").val(_formulaText).attr("defaultValue",_formulaText);
				
				$("#customSystemAttr").attr("originalValue",$("#" + attrId).data("attrModel").originalAttrName);
				$("#customSystemAttrHidden").attr("originalValue","^"+$("#" + attrId).data("attrModel").originalAttrId+"^");
				
			},
			/**
			 * 自定义单位事件
			 * @param customerId-客户群ID
			 */ 
			pattern_unitChange:function(obj){
				var value=obj.val();
				if(value=="9"){
					$("#customUnit").css("display","block");
				}else{
					$("#customUnit").css("display","none");
				}
			},
			/**
			 * 设置格式,确定按纽事件
			 */
			_pattern_buttonEnsure:function(attrId){
				var _pattern=$('input:radio[name="pattern"]:checked').val();//格式
				var _title=$('input:radio[name="title"]:checked').val();	//指标title
				var _
				if(_pattern){
					$("#" + attrId).data("attrModel").guidelineMask=_pattern;
				}
				if(_title){
					$("#" + attrId).data("attrModel").guidelineTitle=_title;
				}
				var _unit=$("#unit").val();			//单位
				if(_unit=="9"){
					_unit=$("#customUnit").val();	//自定义单位
				}
				$("#" + attrId).data("attrModel").guidelineUnit=_unit;
		        
				//关闭窗口
				$("#dialog_setting").dialog("close");
			},
			/**
			 * 分档窗口 增加分档
			 * num 需要添加的行数
			 */
			grade_addNewGrade:function(num){
				for(var i=0;i<num;i++){
					$(".J_isIsometry:last i").remove();
					var _html="<div class=\"isIsometry-div J_isIsometry\">"
            			+"<p class=\"gradeName-box fleft first\">"
            			+"	<input type=\"text\" name=\"gradeName\" id=\"textfield\" class=\"gradeName\"/>"
            			+"</p>"
            			+"<p class=\"gradeName-box fleft\">"
            			+"  <input type=\"text\" name=\"gradeCustom\" id=\"textfield\" class=\"gradeName\"/>"
            			+"</p> "
            			+"<p class=\"gradeName-link fleft\">"
            			+"	<span>——</span>"
            			+"</p> "
            			+"<p class=\"gradeName-box fleft\">"
            			+"	<input type=\"text\" name=\"gradeCustom\" id=\"textfield\" class=\"gradeName\"/>"
            			+"</p> "
            			+"<i id=\"addNewGrade\" class=\"table-icon-off fleft cursor\"></i>"
            			+"<i id=\"delGrade\" class=\"table-icon-on fleft cursor\" ></i>"
            			+"</div>";
					$("#isIsometryLi").append(_html);
				}
			},
			//分档窗口 删除分档
			grade_delGrade:function(){
				var len=$(".J_isIsometry").length;
				if(len==1){
					return;
				}
				$(".J_isIsometry").last().remove();
				var _html="<i id=\"addNewGrade\" class=\"table-icon-off fleft cursor\"></i>"
					+"<i id=\"delGrade\" class=\"table-icon-on fleft cursor\" ></i>" ;
				$(".J_isIsometry:last").append(_html);
			},
			/**
			 * 自定义属性窗口 操作符、常量 表达式设值
			 */
			customSystemAttr_expression_operate:function(value){
				var _value=$("#customSystemAttr").val();
				_value+=value;
				$("#customSystemAttr").val(_value);
				
				var _valueHidden=$("#customSystemAttrHidden").val();
				_valueHidden+=value;
				$("#customSystemAttrHidden").val(_valueHidden);
			},
			/**
			 * 自定义属性窗口 属性 表达式设值
			 */
			customSystemAttr_expression_attribute:function(name,attrId){
				var _value=$("#customSystemAttr").val();
				_value+=name;
				$("#customSystemAttr").val(_value);
				
				var _valueHidden=$("#customSystemAttrHidden").val();
				attrId="^"+attrId+"^";
				_valueHidden+=attrId;
				$("#customSystemAttrHidden").val(_valueHidden);
			},
			/**
			 * 分档页面，确定按纽事件
			 */
			_grade_buttonEnsure:function(attrId){
				//判断 radio 值
				var _isIsometry=$('input:radio[name="isIsometry"]:checked').val();//是否等距分档
				if(!_isIsometry){
					commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "请选择分档类型！",
						 height	: "auto"
					});
					return;
				}
				
				if(_isIsometry==1){
					var _grade={
							isIsometry:1,
							gradeNum:$('#gradeNum').val()
					};
					var _group=new Array(1);
					_group[0]=_grade;
					$("#" + attrId).data("attrModel").group=_group;
					
				}else if(_isIsometry==0){
					var len=$(".J_isIsometry").length;
					var _group=new Array(len);
					for(var i=0;i<len;i++){
						var groupName=$(".J_isIsometry:eq("+i+") input:eq(0)").val()
						var inputL=$(".J_isIsometry:eq("+i+") input:eq(1)").val();
						var inputR=$(".J_isIsometry:eq("+i+") input:eq(2)").val();
						var _grade={
								isIsometry:0,
								groupName:groupName,
								dimBegin:inputL,
								dimEnd:inputR
						};
						_group[i]=_grade;
					}
					$("#" + attrId).data("attrModel").group=_group;
				}
				
				//关闭窗口
				$("#dialog_setting").dialog("close");
			},
			/**
			 * 聚合函数 窗口事件
			 */
			_aggregation_buttonEnsure:function(attrId){
				var _aggregation=$('input:radio[name="aggregation"]:checked').val();//聚合函数
				if(!_aggregation){
					commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "请选择聚合函数！",
						 height	: "auto"
					});
					return;
				}
				
				$("#" + attrId).data("attrModel").aggregationId=_aggregation;
				//关闭窗口
				$("#dialog_setting").dialog("close");
			},
			/**
			 * 清空表达式
			 */
			customSystemAttr_clear_expression:function(){
				$("#customSystemAttr").val($("#customSystemAttr").attr("originalValue"));
				$("#customSystemAttrHidden").val($("#customSystemAttrHidden").attr("originalValue"));
			},
			/**
			 * 自定义属性窗口 确定按纽事件
			 */
			_customSystemAttr_buttonEnsure:function(attrId,clickSource){
				var _originalAttrName=$("#" + attrId).data("attrModel").originalAttrName;	//原始属性名
				var _originalAttrId=$("#" + attrId).data("attrModel").originalAttrId;		//原始属性id
				var _source=$("#" + attrId).data("attrModel").attrSource;//属性来源
				var _attrName=$("#customAttrName").val();
				if(!_attrName||_attrName==""){
					commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "自定义属性名不能为空！",
						 height	: "auto"
					});
					return;
				}
				var _expression_text=$("#customSystemAttr").val();
				var _expression_value=$("#customSystemAttrHidden").val();
				if(clickSource=="left"){			//公共属性，清单自带属性时，判断左侧是否增加新的一项自定义属性
					var _id="c"+Math.uuid(14);
					
					$ul = $("#publicAttr");
					$li = $('<li class="clearfix menu-groups-item cb cursor"></li>').attr("id","attr_"+_id);
	   				$li.append('<span class="fleft red-block block tc">自</span>');
	   				$li.append('<span class="fleft tl">'+ _attrName +'</span>');
   				
   					if(_source!="3"){					//非自定义属性时，添加			
   	   					var attrObj={};
   	   	   				attrObj.attrId=_id;
   	   	   				attrObj.attrName=_attrName;
   	   	   				attrObj.originalAttrName=_originalAttrName;
   	   	   				attrObj.originalAttrId=_originalAttrId;
   	   	   				attrObj.attrSource="3";
   	   	   				attrObj.attrType="2";
   	   	   				attrObj.customSystemAttr=_expression_value;
   	   	   				attrObj.customSystemAttrText=_expression_text;
   	   	   				
   	   	   				$li.data("attrModel", attrObj);
   	   		   			$ul.append($li);
   	   		   			mainPage.bindAttrTag($li);		
   	   				}else if(_source=="3"){
   	   					$("#" + attrId).data("attrModel").customSystemAttrText=_expression_text;
   	   					$("#" + attrId).data("attrModel").customSystemAttr=_expression_value;
   	   					$("#" + attrId).data("attrModel").attrName=_attrName;
   	   					$("#" + attrId).data("attrModel").attrSource="3";   
   	   					$("#"+attrId+" span:last").text(_attrName);
   	   				}
   				}else if(clickSource=="right"){
   					$("#" + attrId).data("attrModel").customSystemAttrText=_expression_text;
	   				$("#" + attrId).data("attrModel").customSystemAttr=_expression_value;
	   				$("#" + attrId).data("attrModel").attrName=_attrName;
	   				$("#" + attrId).data("attrModel").attrSource="4";		//source=4时 表示 自定义属性来是操作右侧已经拖入的属性生成的
	   				$("#"+attrId).text(_attrName);
   				}
				
				//关闭窗口
				$("#dialog_setting").dialog("close");
			},
			/**
			 * 确定按纽事件
			 */
			buttonEnsure : function (eventData){
				var type=eventData.type;
				var attrId=eventData.attrId;
				var clickSource=eventData.clickSource;
				dialogSetting.dialog_buttonEnsure(type,attrId,clickSource);
			}
		
	};
	return dialogSetting;
}();
