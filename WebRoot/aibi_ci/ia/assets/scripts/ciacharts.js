/**
 * 类定义
 * var chart = new CIACharts("domId");
 */
var CIACharts = (function() {   
	return function() {
		this._initialize.apply(this, arguments);
	}
})();

/**
 * 图形类型
 */
CIACharts.ChartType = {
	PIE: 'pie',	//饼图
	LINE: 'line',	//折线图
	BAR: 'bar',	//条形图
	COLUMN: 'column',	//柱状图
	STACKCOLUMN: 'stackColumn',	//堆积柱状图
	SCATTER: 'scatter',		//散点图
	BUBBLE: 'bubble',	//气泡图
	COMBIN: 'combin'	//组合图
};

/**
*图形模版
*公共静态方法
*/
CIACharts.ChartTemplates = {
	    //饼图
	    Pie: function (option) {
	      var defaultOption = {
	    	calculable:true,
	      	title: {
	      		text: '饼图',
	      		subtext: '',
	      		x: 'center'
	      	},
	        tooltip: {
	          trigger: 'item',
	          formatter: '{b} : {c} ({d}/%)',
	          show: true
	        },
	        toolbox: {
				show: true, //是否显示工具栏
				orient:'vertical',//布局方式，可选为：'horizontal' | 'vertical' 
				feature: {
					mark: {show: true},  //辅助线
					dataView: {
						show:true,
						readOnly: false
					}, //数据预览
					restore: {show: true}, //复原
					saveAsImage: {show: true} //是否保存图片
				}
	        },
	        legend: {
	          orient: 'vertical',
	          x: 'left'
	        },
	        series: [
	          {
	            type: 'pie',
	            radius: '65%',
	            center: [
	              '50%',
	              '60%'
	            ],
	            itemStyle : {
	                normal : {
	                    label : {
	                      show:true,
	                       //position : 'outer',
	                      formatter : function (a,b,c,d) {return b+" "+ (d - 0).toFixed(0) + '%'}
	                    },
	                    labelLine : {
	                        show : true
	                    }
	                },
	                emphasis : {
	                    label : {
	                        show : true,
	                        position:'inner',
	                        formatter : "{b}\n{d}%"
	                    }
	                }
	                
	            }
	          }
	        ]
	      };
	      return $.extend(true, {}, defaultOption, option);
	    },
	    //折线图、柱图
	    LineOrBar: function(option){
	    	var defaultOption = {
	    		title: {
	    			x: 'center'
	    		},
	    		tooltip: {
					trigger: 'item' //tooltip触发方式:axis以X轴线触发,item以每一个数据项触发
				},
				toolbox: {
					show: true, //是否显示工具栏
					orient:'vertical',//布局方式，可选为：'horizontal' | 'vertical' 
					feature: {
						mark: {show: true},  //辅助线
						dataView: {
							show:true,
							readOnly: true
						}, //数据预览
						magicType : {show: true, type: ['line', 'bar']},	//图形切换
						restore: {show: true}, //复原
						saveAsImage: {show: true} //是否保存图片
					}
				},
	    		legend: {
	    			y: 'bottom'
	    		}
	    	};
	    	//柱子最大宽度
	    	$.each(option.series,function(i,item){
	    		item.barMaxWidth = 25;
	    	});
	    	return $.extend(true, {}, defaultOption, option);
	    },
	    //散点、汽泡图
	    Scatter: function(option){
	    	var defaultOption = {
	    		title: {
	    			x: 'center'
	    		},
	    		tooltip: {
					trigger: 'item', //tooltip触发方式:axis以X轴线触发,item以每一个数据项触发
					axisPointer: {
						type: 'cross',
						lineStyle: {
							type: 'dashed',
							width: 1
						}
					}
				},
				toolbox: {
					show: true, //是否显示工具栏
					orient:'vertical',//布局方式，可选为：'horizontal' | 'vertical' 
					feature: {
						mark: {show: true},  //辅助线
						dataZoom: {show: true},
						dataView: {
							show:true,
							readOnly: true
						}, //数据预览
						restore: {show: true}, //复原
						saveAsImage: {show: true} //是否保存图片
					}
				}
	    	};
	    	//显示最大值、最小值、平均值
	    	$.each(option.series,function(i,item){
	    		item.markPoint = {
	    			data: [
	    			   {type: 'max',name:'最大值'},
	    			   {type: 'min',name:'最小值'}
	    			]
	    		};
	    		item.markLine = {
	    			data: [
	    			   {type: 'average',name:'平均值'}
	    			]
	    		};
	    	});
	    	return $.extend(true, {}, defaultOption, option);	    	
	    }
};

/**
 * 实例方法	
 */
CIACharts.prototype = {
	_initialize: function(id,theme){
		if(!theme && typeof(macarons_theme) != 'undefined'){
			theme = macarons_theme;
		}
		this.chartObj = echarts.init(document.getElementById(id),theme);
		this.chartObj.showLoading({
			text: '正在努力的读取数据中...'    //loading
		});
	},
	renderChart: function (option,type) {
		switch (type) {
			case CIACharts.ChartType.PIE:
				option = CIACharts.ChartTemplates.Pie(option);
			break;
			case CIACharts.ChartType.LINE:
			case CIACharts.ChartType.COLUMN:
			case CIACharts.ChartType.STACKCOLUMN:
			case CIACharts.ChartType.BAR:
				option = CIACharts.ChartTemplates.LineOrBar(option);
			break;
			case CIACharts.ChartType.SCATTER:
			case CIACharts.ChartType.BUBBLE:
				option = CIACharts.ChartTemplates.Scatter(option);
			break;
			default:
				// default statements
		}	
		
		this.chartObj.hideLoading();
		this.chartObj.setOption(option, true);
	}
};