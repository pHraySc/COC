$(function(){
	$(document).click(function(e){
		var e=e||window.event;
		e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
		$("#cia-bubble-dialog").remove();
		$(".tag-box-list-hover").removeClass("tag-box-list-hover").addClass("tag-box-list");
	})
	/********
   	 查询头部multiselect
   	********/
   	jQueryUIUtils.multiselect("#group-list",{
   	   multiple: false,
   	   header:false,
   	   noneSelectedText: "请选择清单",
   	   selectedList: 1 ,
   	   click:function(event, ui){
   		   var customNum = $("#group-list").find("[value = '" + ui.value + "']").attr("data-customNum");
   		   $("#customNum").html(customNum);
   		   //加载清单属性
   		   if(ui.value != ""){
   			   mainPage.getCustomAttr(ui.value);
   		   }   		   
   	   }
   	});
	/**
	 * 查看清单详情
	 */
	$("#queryGroupListInfo").click(function(){		 
		var customId = "KHQ99900000957";
		var dataDate = "20141203";
		var url = $.ctx + "/ci/customersManagerAction!findGroupList.ai2do?ciCustomGroupInfo.customGroupId="+customId+"&ciCustomGroupInfo.dataDate="+dataDate;
		var dlgObj = dialogUtil.create_dialog("queryListDialog", {
			"title" :  "客户群清单预览",
			"height": "auto",
			"width" : 700,
			"frameSrc" : url,
			"frameHeight":328,
			"position" : ['center','center'] 
		}); 
	
	});
	/********
	 dialog弹窗
	********/
	$("#createTable").click(function(){ 
		jQueryUIUtils.dialog("#cia-dialog",{ 
			content:"请添加内容",
			title:"弹框标题", 
			height: "auto",
			width: 750, 
		});
	});

	/**************
	智能分析首页菜单展开收起操作
	***************/
	$(".menu-groups-header").click(function(){
		var header = $(this).find(".menu-groups-open");
		if(header.hasClass("close")){
			header.removeClass("close").parent().next().show();
		}else{
			header.addClass("close").parent().next().hide();
		} 
	});
	/**************
	添加新工作表
	***************/
	$("#createWorkSheet").click(function(){
		$.ajax({
			url:$.ctx+"/aibi_ci/ia/pages/main/main_right.jsp",
			type:"GET",
			dataType:"html",
			success:function(data){
				var uuid = Math.uuid();
				var $newSheet = $(data).attr("id","sheet_" + uuid).attr("sheetId",uuid);
				$("#ciaWorkBook").append($newSheet);				
				$newSheet.find(".J_chartContainer").attr("id","chart_" + uuid);
				
				//设置表格id
				$newSheet.find(".J_jqGrid").attr("id","jqGrid_"+uuid);
				$newSheet.find(".J_jqGridPager").attr("id","jqGridPager_"+uuid);
				$newSheet.find(".J_switch").attr("uuid",uuid);
				
				mainPage.bindDroppable($newSheet.find(".J_droppable"));
				mainPage.bindChartBtn($newSheet.find(".cia-show-button"));
				mainPage.bindDelBox($newSheet.find(".tag-del-box"));
								
				var sheetNo = ++mainPage.sheetNo;
				var $li = $('<li class="fleft cia-tab-item cursor J_sheetFooter"><div class="fleft tab-left-text tc">工作表-'+sheetNo+'</div><div class="fleft tab-left-bg"></div></li>');
				if(sheetNo == 1){
					$li = $('<li class="fleft cia-tab-item cia-tab-item-first cursor J_sheetFooter"><div class="fleft tab-left-text tab-left-text-first tc">工作表-'+sheetNo+'</div><div class="fleft tab-left-bg"></div></li>');
				}					 		
				$li.attr("id",uuid);
				
				$("#createWorkSheet").before($li);
				$li.click();
			}
		});
	});
	
	/**************
	智能分析首页table和charts切换
	***************/
	$(".J_switch").live("click",function(){
		$(".cia-active-tab").find(".J_switch.active").removeClass("active").find("> i").removeClass("active");
		$(this).addClass("active").find("> i").addClass("active");
		if($(this).hasClass("cia-charts")){
			$(".cia-active-tab").find(".J_chartContainer").show();
			$(".cia-active-tab").find(".J_tableContainer").hide();
		}else if($(this).hasClass("cia-table")){
			$(".cia-active-tab").find(".J_chartContainer").hide();
			$(".cia-active-tab").find(".J_tableContainer").show();
		}
		
	});
	
	/*********************
		底部tab切换
	********************/
	$(".cia-tab-item").live("mouseover",function(){
		$(".tab-left-bg-prev-hover").removeClass("tab-left-bg-prev-hover");
		$(".tab-left-text-hover").removeClass("tab-left-text-hover");
		$(".tab-left-bg-hover").removeClass("tab-left-bg-hover");
		$(".tab-left-bg-last-hover").removeClass("tab-left-bg-last-hover");
		$(this).prev().find("> div").last().addClass("tab-left-bg-prev-hover");
		$(this).find("> div").first().addClass("tab-left-text-hover");
		$(this).find("> div").last().addClass("tab-left-bg-hover");
		if($(this).next().length == 0){
			$(this).find("> div").last().addClass("tab-left-bg-last-hover");
		} 
	});
	$(".cia-tab-item").live("mouseout",function(){ 
		$(".tab-left-bg-prev-hover").removeClass("tab-left-bg-prev-hover");
		$(".tab-left-text-hover").removeClass("tab-left-text-hover");
		$(".tab-left-bg-hover").removeClass("tab-left-bg-hover");
		$(".tab-left-bg-last-hover").removeClass("tab-left-bg-last-hover");
	
	});
	/*********************
		底部tab选中
	********************/
	$(".J_sheetFooter").live("click",function(){
		$this = $(this);
		var id = $this.attr("id");
		$(".cia-active-tab").removeClass("cia-active-tab").hide();
		$("#sheet_"+id).addClass("cia-active-tab").show();
		$(".J_footerActive").removeClass("J_footerActive");
		$this.addClass("J_footerActive");
		
		$(".tab-left-bg-prev-active").removeClass("tab-left-bg-prev-active");
		$(".tab-left-text-active").removeClass("tab-left-text-active");
		$(".tab-left-bg-active").removeClass("tab-left-bg-active");
		$this.prev().find("> div").last().addClass("tab-left-bg-prev-active");
		$this.find("> div").first().addClass("tab-left-text-active");
		$this.find("> div").last().addClass("tab-left-bg-active");	
	});
	/*********************
		底部tab右键菜单
	********************/
	$(".J_sheetFooter").rightContextMenu({
		click:function(){
			var id = $(this).attr("id");			
			var sheetId = $(this).attr("attrId");
			var $this=$(this);
			//重命名
			if(id=="rename"){
				var $div = $("#" + sheetId).find("div:first");
				var text = $div.text();
				$input = $("<input></input>").val(text).blur(function(e){
					if($(this).val() != ""){
						text =  $(this).val();
					}
					$div.text(text);
					$input.remove();		
				}).bind("keypress",function(e){
					if(e.keyCode == "13"){
						if($(this).val() != ""){
							text =  $(this).val();
						}
						$div.text(text);
						$input.remove();	
					}
				});
				$div.empty().append($input);
				$input.select();
			}else if(id == "delete"){
				$li = $("#" + sheetId);
				$sheet = $("#sheet_" + sheetId);

				//至少存在一个工作表,不能全删除
				if($(".J_sheetFooter").length==1){
					commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "不能删除!至少要存在一张工作表",
						 height	: "auto"
					});
					return false;
				}
				
				//如果删除活动工作表，删除后需要激活其他工作表
				if($li.hasClass("J_footerActive")){
					if($li.next(".J_sheetFooter").length != 0){
						$li.next(".J_sheetFooter").click();
					}else{
						$li.prev(".J_sheetFooter").click();
					}
				}
				
				$li.remove();
				$sheet.remove();
			}			
		},
		menuList:[
			[{
				name:"删除",
				id:"delete" 
			},{
				name:"重命名",
				id:"rename" 
			} ]
		] 
	});
	
	$( ".menu-groups-ul li,.J_droppable li.tag-box-list" ).draggable({
		appendTo: "body",
		helper: "clone",
		cursor: "move" ,
		start: function( event, ui ) {
			if($(this).hasClass("tag-box-list") || $(this).hasClass("tag-box-list-hover")){ 
				$( ".tag-del-box" ).fadeIn(1000);
			}
		},
		stop:function(){
			$("body").css({"cursor": "default" });
			if($(this).length > 0){
				$( ".tag-del-box" ).fadeOut(1000);
			}
		}
		
	});
	
	//右侧属性右键菜单
	$(".J_droppable > li").rightContextMenu({
		click:function(){
			var id = $(this).attr("id");
			var page=$(this).attr("url");
			
			var attrId = $(this).attr("attrId");
			var eventData={type:page,attrId:attrId,clickSource:"right"};//点击事件传递的数据
			var $this=$(this);
			//重命名
			if(id == "setRenaming"){				
				var attrId = $this.attr("attrId");
				var $li = $("#" + attrId);
				var attrModel = $li.getAttrModel();
				$input = $("<input></input>").val(attrModel.attrName).blur(function(e){
					var text = $(this).val();
					if(text != ""){
						attrModel.attrName = text;
					}
					$li.text(attrModel.attrName);
					$input.remove();		
				}).bind("keypress",function(e){
					if(e.keyCode == "13"){
						var text = $(this).val();
						if(text != ""){
							attrModel.attrName = text;
						}
						$li.text(attrModel.attrName);
						$input.remove();	
					}
				});
				$li.empty().append($input);
				$input.select();
			}else if(id=="setExclude"){
				var _labelTypeId=$("#" + attrId).data("attrModel").labelTypeId;
				var _attrName=$("#" + attrId).data("attrModel").attrName;
				_labelTypeId=4;
				//var page="";
				var width=0;
		      	var height=0;
				if(_labelTypeId==6){		//日期
					page="date";
					width=570;
			      	height=290;
				}else if(_labelTypeId==4){ 	//kpi 数值
					page="number";
					width=530;
			      	height=270;
				}else if(_labelTypeId==7){	//模糊值
					page="darkValue";
					width=570;
			      	height=328;
				}
				eventData={type:page,attrId:attrId,clickSource:"right"};//点击事件传递的数据
				$.ajax({
					url:$.ctx+"/aibi_ci/ia/pages/main/filter_"+page+".jsp",
					type:"GET",
					dataType:"html",
					success:function(data){
						jQueryUIUtils.dialog("dialog_filter",{
							title:_attrName+" -- "+$this.text(),
					      	buttons:{},
					      	width:width,
					      	height:height,
					      	content:data
					 	});
						
						filterSetting.initValue(page,attrId);//打开窗口后，初始化值
						
						//取消按纽 绑定窗口关闭事件
						$("#but_cancel").live("click",function(){
							$("#dialog_filter").dialog("close")
						});
						//确定按纽 绑定事件
						//$("#but_ensure").unbind("click");
						$("#but_ensure").bind("click", function(){
							filterSetting.buttonEnsure(eventData);
						});
					}
				});
				
			}else{
				var _attrName=$("#" + attrId).data("attrModel").attrName;
				$.ajax({
					url:$.ctx+"/aibi_ci/ia/pages/main/dialog_"+page+".jsp",
					type:"GET",
					dataType:"html",
					success:function(data){
						jQueryUIUtils.dialog("dialog_setting",{
							title:_attrName+" -- "+$this.text(),
					      	buttons:{},
					      	content:data
					 	});
						
						dialogSetting.initValue(page,attrId);//打开窗口后，初始化值
						
						//取消按纽 绑定窗口关闭事件
						$("#but_cancel").live("click",function(){
							$("#dialog_setting").dialog("close")
						});
						//确定按纽 绑定事件
						//$("#but_ensure").unbind("click");
						$("#but_ensure").bind("click", function(){
							dialogSetting.buttonEnsure(eventData);
						});
					}
				});
			}			
		},
		menuList:[
			[{
				name:"排序",
				id:"setSequence" 
			},{
				name:"重命名",
				id:"setRenaming" 
			} ],
			[{
				name:"创建数据分档",
				id:"createDataFile",
				url:"grade"
			}],
			[{
				name:"设置数据格式",
				id:"setDataFormat",
				url:"pattern"
			},{
				name:"设置计算字段",
				id:"setCalculatedField", 
				url:"customSystemAttr"
			},{
				name:"设置聚合方式",
				id:"setAggregation",
				url:"aggregation"
			},{
				name:"仅排除",
				id:"setExclude",
				url:"filter"
			},{
				name:"仅包含",
				id:"setInclude",
				url:"filter"
			}]
		] 
	});
	/////////////////////////弹出窗口 绑定事件//////////////////////////////
	//格式设置窗口，单位下拉框事件
	$("#unit").live("change",function(){
		dialogSetting.pattern_unitChange($(this));
	});
	
	//创建分档窗口，绑定事件
	$("#addNewGrade").live("click",function(){
		dialogSetting.grade_addNewGrade(1);
	});
	$("#delGrade").live("click",function(){
		dialogSetting.grade_delGrade();
	});
	
	//自定义属性窗口 操作符 绑定事件
	$("#clearBut").live("click",function(){
		if(confirm("是否确定重置计算表达式？")){
			dialogSetting.customSystemAttr_clear_expression();
		}
	});
	
	$("#icon-jiahao").live("click",function(){
		dialogSetting.customSystemAttr_expression_operate("+");
	});
	$("#icon-jianhao").live("click",function(){
		dialogSetting.customSystemAttr_expression_operate("-");
	});
	$("#icon-cheng").live("click",function(){
		dialogSetting.customSystemAttr_expression_operate("*");
	});
	$("#icon-chufa").live("click",function(){
		dialogSetting.customSystemAttr_expression_operate("/");
	});
	$("#icon-zuo").live("click",function(){
		dialogSetting.customSystemAttr_expression_operate("(");
	});
	$("#icon-you").live("click",function(){
		dialogSetting.customSystemAttr_expression_operate(")");
	});
	
	//自定义属性窗口 常量值  回车事件
	$("#constantVal").live("keydown",function(e){
		if(e.keyCode==13){
			dialogSetting.customSystemAttr_expression_operate($(this).val());
			$(this).val("");
		}
	});
	//属性绑定双击事件
	$(".operation-right-li > li").live("dblclick",function(){
		var id=$(this).attr("id");
		var name=$("#" + id).data("attrModel").attrName;
		var attrId=$("#" + id).data("attrModel").attrId;
		dialogSetting.customSystemAttr_expression_attribute(name,attrId);
	});

	//左侧属性右键菜单
	$(".menu-groups-item > span").rightContextMenu({
		click:function(){
			var attrId = $(this).attr("attrId");
			var page=$(this).attr("url");
			var eventData={type:page,attrId:attrId,clickSource:"left"};//点击事件传递的数据
			var $this=$(this);
			var target = $(this).attr("target");
			if(target){//添加到行、列、标记、筛选器
				var attrObj = $("#" + attrId);				
				var $ul = $('.cia-active-tab').find('ul[target='+ target +']');
				$ul.find(".placeholder").remove();
				//数据校验
				if(mainPage.isCanAddAttr($ul, attrId)){
					var newId = Math.uuid();
					var text = attrObj.find("span").last().text();
					var $li = $( '<li class="fleft tag-box-list"></li>').text(text).attr("id",newId).attr("attrId",attrId);
					//克隆数据
					var newData = $.extend(true, {}, attrObj.getAttrModel()); 
					$li.setAttrModel(newData);
					$li.appendTo($ul).draggable({
						appendTo: "body",
						helper: "clone",
						cursor: "move" ,
						start: function( event, ui ) {
							if($(this).hasClass("tag-box-list") || $(this).hasClass("tag-box-list-hover")){
								$( ".tag-del-box" ).fadeIn(1000);
							}
						} ,
						stop:function(){
							$("body").css({"cursor": "default" });
							if($(this).length > 0){
								$( ".tag-del-box" ).fadeOut(1000);
							}
						}
					});
				}
			}else{
				var _attrName=$("#" + attrId).data("attrModel").attrName;
				$.ajax({
					url:$.ctx+"/aibi_ci/ia/pages/main/dialog_"+page+".jsp",
					type:'GET',
					dataType:"html",
					success:function(data){
						jQueryUIUtils.dialog("dialog_setting",{
							title:_attrName+" -- "+$this.text(),
					      	buttons:{},
					      	content:data
					 	});
						
						dialogSetting.initValue(page,attrId);//打开窗口后，初始化值
						
						//取消按纽 绑定窗口关闭事件
						$("#but_cancel").live("click",function(){
							$("#dialog_setting").dialog("close");
						});
						//确定按纽 绑定事件
						//$("#but_ensure").unbind("click");
						$("#but_ensure").bind("click",function(){
							 dialogSetting.buttonEnsure(eventData);
						});
						
					}
				});
			}						
		},
		menuList:[
			[{
				name:"添加到行",
				id:"addRow",
				target:"row"
			},{
				name:"添加到列",
				id:"addSeries",
				target:"column"
			},{
				name:"添加到标记",
				id:"addMark",
				target:"symbol"
			},{
				name:"添加到筛选器(仅排除)",
				id:"addExFilter",
				target:"filter-exclude"
			},{
				name:"添加到筛选器(仅保留)",
				id:"addInFilter",
				target:"filter-include"
			}],
			[{
				name:"创建数据分档",
				id:"createDataFile",
				url:"grade"
			}],
			[{
				name:"设置数据格式",
				id:"setDataFormat",
				url:"pattern"
			},{
				name:"设置计算字段",
				id:"setCalculatedField",
				url:"customSystemAttr"
			},{
				name:"设置聚合方式",
				id:"setAggregation",
				url:"aggregation"
			}]
		] 
	});	
	
	/*********************
		图形类型选择
	********************/
	$(".J_chartType").live("click",function(){
		$this = $(this);
		if(!$this.hasClass("active")){
			$this.parent().parent().find(".J_chartType.active").removeClass("active");
			$this.addClass("active");
		}
	});	
	
	//加载第一个工作表
	$("#createWorkSheet").click();
	//获取清单列表 
	mainPage.getCustomerListData("KHQx00001073");
	//查询公共属性
	mainPage.getPublicAttr(); 
});
