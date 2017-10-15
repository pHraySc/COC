/********
 *  2014-12-19
 *	智能分析首页
 */
mainPage = function() { 
	var mainPage = {
		//工作表计数器
		sheetNo : 0,
		
		/**
		 * 获取客户群清单列表
		 * @param customerId-客户群ID
		 */ 
		getCustomerListData : function(customerId){
//		    $.blockUI(); //添加遮罩层
			var postUrl = $.ctx + "/cia/toushiAnalysisAction!getCustomGroupList.ai2do";
			$.ajax({
				type: "POST",
				url: postUrl, 
			   	cache: false,
			   	data: {"groupId":customerId}, 
			   	dataType: "json",
			   	success: function(data){
			   		var $select = $("#group-list");
			   		for(var i=0; i<data.length; i++){
			   			if(i==0){
			   				//下拉框选中第一个清单
			   				$select.append('<option selected=true data-customNum="'+data[i].customNum+'" value="'+data[i].listTableName+'">'+data[i].dataDate+'</option> ');
			   				$("#customNum").html(data[i].customNum);
			   				//加载清单自带属性
			   				mainPage.getCustomAttr(data[i].listTableName);
			   			}else{
			   				$select.append('<option data-customNum="'+data[i].customNum+'" value="'+data[i].listTableName+'">'+data[i].dataDate+'</option> ');
			   			}			   			
			   		} 
			   		$select.multiselect("refresh");
			   	}, 
			   	error:function(XMLHttpRequest, textStatus, errorThrown){
			   		commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "失败啦！！",
						 height	: "auto"
					});
			   		
			   	},
			   	complete: function() {
//			   		$.unblockUI(); 
			   	}
			});					
		},
		/**
		 * 查询系统预置公共属性
		 */
		getPublicAttr : function(){
			var postUrl = $.ctx + "/cia/toushiAnalysisAction!queryPublicAttr.ai2do";
			$.ajax({
				type: "POST",
				url: postUrl, 
			   	cache: false,
			   	data: {}, 
			   	dataType: "json",
			   	success: function(data){
			   		//全局缓存公共属性
			   		_GlobeObject["publicAttr"] = data;
			   		$ul = $("#publicAttr");
			   		for(var i = 0; i < data.length; i++){
			   			$li = $('<li class="clearfix menu-groups-item cb cursor"></li>').attr("id","attr_"+data[i].attrId);
			   			if(data[i].attrType == 1){//指标
			   				$li.append('<span class="fleft red-block block tc">指</span>');
			   				$li.append('<span class="fleft tl">'+ data[i].attrName +'</span>');
			   			}else if(data[i].attrType == 2){//维度
			   				$li.append('<span class="fleft green-block block tc">维</span>');
			   				$li.append('<span class="fleft">'+ data[i].attrName +'</span>');
			   			}
			   			//绑定节点数据
			   			$li.data("attrModel", data[i]);
			   			$ul.append($li);
			   			mainPage.bindAttrTag($li);			   			
			   		}
			   	}, 
			   	error:function(XMLHttpRequest, textStatus, errorThrown){
			   		commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "失败啦！！",
						 height	: "auto"
					});
			   		
			   	}
			});	
		},
		/**
		 * 查询客户群自带属性
		 * @param listTableName-清单表名
		 */
		getCustomAttr : function(listTableName){
			var postUrl = $.ctx + "/cia/toushiAnalysisAction!queryCustomAttr.ai2do";
			$.ajax({
				type: "POST",
				url: postUrl, 
			   	cache: false,
			   	data: {"listTableName":listTableName}, 
			   	dataType: "json",
			   	success: function(data){
			   		$ul = $("#customAttr");
			   		$ul.empty();	//清除上一个清单自带属性
			   		for(var i = 0; i < data.length; i++){
			   			$li = $('<li class="clearfix menu-groups-item cb cursor"></li>').attr("id",data[i].attrId);
			   			if(data[i].attrType == 1){//指标
			   				$li.append('<span class="fleft red-block block tc">指</span>');
			   				$li.append('<span class="fleft tl">'+ data[i].attrName +'</span>');
			   			}else if(data[i].attrType == 2){//维度
			   				$li.append('<span class="fleft green-block block tc">维</span>');
			   				$li.append('<span class="fleft">'+ data[i].attrName +'</span>');
			   			}
			   			//绑定节点数据
			   			$li.data("attrModel", data[i]);
			   			$ul.append($li);
			   			mainPage.bindAttrTag($li);
			   		}
			   	}, 
			   	error:function(XMLHttpRequest, textStatus, errorThrown){
			   		commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "失败啦！！",
						 height	: "auto"
					});
			   		
			   	}
			});	
		},
		/**
		 * 绑定属性拖拽事件
		 * @param $obj-属性节点jquery对象
		 */
		bindAttrTag : function ($obj){	
			$obj.draggable({
				appendTo: "body",
				helper: "clone",
				cursor: "move" 	
			});
		},
		/**
		 * 绑定右侧编辑区域拖拽事件
		 * @param $obj-行、列、标记的jquery对象
		 */
		bindDroppable : function($obj){
			$obj.droppable({
				activeClass: "ui-state-default",
				hoverClass: "ui-state-hover",
				accept: ":not(.ui-sortable-helper)", 
				drop: function( event, ui ) { 
					if(!(ui.draggable.hasClass("tag-box-list") || ui.draggable.hasClass("tag-box-list-hover"))){
						$( this ).find( ".placeholder" ).remove();
						var newId = Math.uuid();
						var text = ui.draggable.find("span").last().text();
						var attrId = ui.draggable.attr("id");
						//数据校验
						if(!mainPage.isCanAddAttr($(this),attrId)){
							return;
						}
						var $li = $( '<li class="fleft tag-box-list"></li>').text( text ).attr("id",newId).attr("attrId",attrId);
						//克隆数据
						var newData = $.extend(true, {}, ui.draggable.getAttrModel()); 
						$li.setAttrModel(newData);
						$li.appendTo( this ).draggable({
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
						$(".main-content-item-hover").removeClass("main-content-item-hover");
					}
			    },
				over:function(event,ui){
					$(".main-content-item-hover").removeClass("main-content-item-hover");
					if(ui.draggable.hasClass("menu-groups-item")){
						var parentItem = $(this).parent().parent();
						if(parentItem.hasClass("main-content-item")){
							parentItem.addClass("main-content-item-hover")
						}
					}
					
				},
				out:function(event,ui){
					$(".main-content-item-hover").removeClass("main-content-item-hover"); 
				}
				
			});
		},
		/**
		 * 绑定垃圾桶删除事件
		 * @param $delBox-垃圾桶的jquery对象
		 */
		bindDelBox : function($delBox){
			$delBox.droppable({
				activeClass: "ui-state-default",
				hoverClass: "tag-del-box-hover",
				accept: ":not(.ui-sortable-helper)", 
				drop: function( event, ui ) {
					$( this ).find( ".placeholder" ).remove();   
					if(ui.draggable.hasClass("tag-box-list") || ui.draggable.hasClass("tag-box-list-hover")){ 
						var li = ui.draggable;
						var ul = li.parent();
						li.remove();
						$( ".tag-del-box" ).fadeOut(1000);
						if(ul.find("li").length == 0){
							ul.append('<li class="fleft placeholder">请拖拽至此</li>');
						}					
					}				 
				}
			});	
		},		
		/**
		 * 绑定生成图表事件
		 * @param $btn-按钮的jquery对象
		 */
		bindChartBtn : function($btn){
			$btn.click(function(){
				//数据校验，行、列上不能为空
				if(!mainPage.isCanSubmit()){
					return false;
				}
				
				var $sheet = $('.cia-active-tab');
				var sheetId = $sheet.attr("sheetId");
				var sheetName = $("#" + sheetId).children("div:first").text();
				var listTableName = $("#group-list").val();
				var dataDate = $("#group-list").find("option:selected").text();
				var chartId = $sheet.find(".J_chartContainer").attr("id");
				var chartType = $sheet.find(".J_chartType.active").attr("chartType");

				//图表实例,new后会显示正在加载效果
				var chart = new CIACharts(chartId);
				$sheet.find(".no-charts").removeClass("no-charts");
				
				var workSheetModel = {};
				workSheetModel["chartType"] = chartType;
				workSheetModel["dataDate"] = dataDate;
				workSheetModel["listTableName"] = listTableName;
				workSheetModel["workSheetId"] = sheetId;
				workSheetModel["workSheetName"] = sheetName;
				
				//行数据
				workSheetModel["row"] = [];
				$sheet.find('ul[target=row] > li').each(function(){
					var data = $(this).getAttrModel();
					workSheetModel["row"].push(data);
				});
				
				
				//列数据
				workSheetModel["column"] = [];
				$sheet.find('ul[target=column] > li').each(function(){
					var data = $(this).getAttrModel();
					workSheetModel["column"].push(data);
				});		
				
				//标记数据
				workSheetModel["symbol"] = [];
				$sheet.find('ul[target=symbol] > li').each(function(){
					var data = $(this).getAttrModel();
					//workSheetModel["symbol"].push(data);
				});	
				//筛选器数据 仅排除
				
				workSheetModel["filterExclude"] = [];
				$sheet.find('ul[target=filter-exclude] > li').each(function(){
					var data = $(this).getAttrModel();
					workSheetModel["filterExclude"].push(data);
				});		
				//筛选器数据 仅保留
				workSheetModel["filterInclude"] = [];
				$sheet.find('ul[target=filter-include] > li').each(function(){
					var data = $(this).getAttrModel();
					workSheetModel["filterInclude"].push(data);
				});	
				var workSheetString = JSON.stringify(workSheetModel);
				//提交请求
				$.ajax({
					url:$.ctx+"/cia/toushiAnalysisAction!generateChart.ai2do",
					type:'POST',
					data:'workSheetString=' + encodeURIComponent(workSheetString),
					dataType:"json",
					success:function(data){		
						var option = $.parseJSON(data.option);
						chart.renderChart(option,chartType);
						
						//生成表格 开始
						var gridData=data.data;
						var colModel=new Array();
						for(var i=0;i<data.columnInfo.length;i++){
							var _head={
									label:data.columnInfo[i],
									name:data.columnInfo[i],
									width:100
							}
							colModel[i]=_head;
						}
						$("#jqGrid_"+sheetId).jqGrid({
				               datatype: "local",
				               data: gridData,
				               height: 200,
				               width: 780,  //可动态获得宽度
				               colModel:colModel,
				               multiselect: true,
				               rowList:[10,20,100], 
				               viewrecords: true, // show the current page, data rang and total records on the toolbar
				               //caption: "数据",
				               rowNum: 10,
							   viewrecords: true,
				               pager: "#jqGridPager_"+sheetId
				           });
						//生成表格 结束
					}
				});
				
			});
		},
		/**
		 * 数据校验，行、列上不能为空
		 */
		isCanSubmit : function(){
			if($('.cia-active-tab').find('ul[target=row] .placeholder').length != 0){
				commonUtil.create_alert_dialog('showSortError1AlertDialog', {
					 type	: 'failed',//failed
					 width	: 500,
					 txt	: "行上没有添加属性!",
					 height	: "auto"
				});
				return false;
			}
			if($('.cia-active-tab').find('ul[target=column] .placeholder').length != 0){
				commonUtil.create_alert_dialog('showSortError1AlertDialog', {
					 type	: 'failed',//failed
					 width	: 500,
					 txt	: "列上没有添加属性!",
					 height	: "auto"
				});
				return false;
			}
			return true;
		},
		/**
		 * 数据校验
		 * 属性不能重复，行上最多两个,标记一个
		 * @param $ul-行、列、标记、筛选器
		 * @param attrId-属性ID
		 */
		isCanAddAttr : function($ul,attrId){
			var target = $ul.attr("target");
			if(target=="row"){
				if($ul.find("li").length >= 2){
					commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "行上最多只能添加2个属性!",
						 height	: "auto"
					});
					return false;
				}
			}
			if(target=="symbol"){
				if($ul.find("li").length >= 1){
					commonUtil.create_alert_dialog('showSortError1AlertDialog', {
						 type	: 'failed',//failed
						 width	: 500,
						 txt	: "标记只能有1个!",
						 height	: "auto"
					});
					return false;
				}
			}
			return true;
		}
		//****
	};
	return mainPage;
}();

