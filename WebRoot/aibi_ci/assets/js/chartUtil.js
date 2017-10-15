var _chartDebug=false;
var exceChart1 = "StackedColumn2D";
var exceChart2 = "Pie2D";
var exceChart3 = "Column2D";
/**
 * 
 * @param chartId         chart对象的domID
 * @param isHideBig       是否隐藏最大值
 * @param isShowValues    是否显示数据
 * @param hideLabelArr    需要隐藏的legen标签数组
 * @return   
 */
function reConfigChart(chartId,isHideBig,isShowValues,hideLabelArr){
	try{
		var chartObj = eval("c_" + chartId);
		var myChart = getChart(chartObj);
		var newDataXml = myChart.data;
		var type = myChart.type;
		
		
		var sTmp = newDataXml;
		var reg = /%26#x000A;/g; // 创建正则表达式
		var srt = sTmp.replace(reg,""); // 用 “” 替换 “%26#x000A;”
		newDataXml = srt;
		
			
		if(isHideBig)
			newDataXml=hideBigest(chartId,newDataXml,type);
		if(isShowValues)
			newDataXml=showValues(newDataXml);
		if(hideLabelArr)
			newDataXml=hideDataByLabel(newDataXml,hideLabelArr);
		
		chartObj.setDataXML(newDataXml);
	}catch(e){
		if(window.console&&window.console.log&&_chartDebug)
			 console.log(e.description);
	}
}
function hideBigest(chartId,newDataXml,type){
	
	var f1 = newDataXml.indexOf("<categories>");
	var fragment1 = newDataXml.substring(0,f1);
	//alert(fragment1);
	var f2 = newDataXml.lastIndexOf("</dataset>");
	var fragment2 = newDataXml.substring(f2+10);
	//alert(fragment2);
	
	var cates = getCategories(newDataXml);
	var datasets = getDataSets(newDataXml);
	
	var dataSetArr = new Array();
	for(var i =0;i<datasets.length;i++){
		var sets = getSets(datasets[i]);
		var setArr = new Array();
		for(var j =0;j<sets.length;j++){
			
			var u1 = sets[j].indexOf("value");
			var u2 = sets[j].lastIndexOf("'");
			var v = sets[j].substring(u1+7,u2);
			//alert(!isNaN(Number(v)));
			setArr.push(Number(v));
		}
		dataSetArr.push(setArr);		
	}
	
	
	/*********** 求最大值 算法 开始 *************************/
	var x=0,y=0,c; // x 指-指标，y 表示-维度，c 是-最大值
	var markArr = new Array(); // 作为各个指标的标示
	for(var i=0;i<dataSetArr.length;i++){
		
		var iMaxArr = dataSetArr[i];
		var ij = iMaxArr[0];
		c = ij;
		for(var j=0;j<iMaxArr.length;j++){
			if(ij>iMaxArr[j]){
				//continue;
			}else{
				ij = iMaxArr[j];
				x = i;
				y = j;
				c = ij;
			}
		}
		var obj = new Object();
		obj.x = x;
		obj.y = y;
		obj.c = c;
		markArr.push(obj);
	}
	
	var maxO = markArr[0];
	
	for(var i=0;i<markArr.length;i++){
		var o = markArr[i];
		if(maxO.c>o.c){
			x = maxO.x;
			y = maxO.y;
			c = maxO.c;
		}else{
			maxO = o;
			x = o.x;
			y = o.y;
			c = o.c;
		}
		
	}
	/*********** 求最大值 算法 结束 *************************/
	//alert("x="+x+", y="+y+", c="+c); // 求得结果
	
	/*******  对于特殊图形的  最大指标的算法 开始   StackedColumn2D ******************************************/
	if( type == exceChart1){
		var tempArr = new Array() ;
		var fg = true;
		for(var i=0;i<dataSetArr.length;i++){
			var iMaxArr = dataSetArr[i];
			
			if(fg){
				for(var j=0;j<iMaxArr.length;j++){
					tempArr.push(0);
				}
				fg = false;
			}
			
			for(var j=0;j<iMaxArr.length;j++){
				if(!isNaN(iMaxArr[j])){
					tempArr[j] += Math.abs(iMaxArr[j]);
				}
			}
		}
		
		var idx = 0,m = tempArr[0];
		for(var i=0;i<tempArr.length;i++){
			if(m<tempArr[i]){
				idx = i;
				m = tempArr[i];
			}
		}
		y = idx;
		//alert("idx="+idx+", m="+m); // 求得结果
	}
	
	/*******  对于特殊图形的  最大指标的算法 结束   StackedColumn2D ******************************************/
	
	/*******  对于特殊图形的  最大指标的算法 开始   Pie2D ******************************************/
	//////// 特殊是因为没有 dataset 标签， 只有 set 标签
	if(type == exceChart2 || type == exceChart3 ){
		var sets = getSets(newDataXml);
		var stArr = new Array();
		for(var j =0;j<sets.length;j++){
			
			var u1 = sets[j].indexOf("value");
			var u2 = sets[j].lastIndexOf("'");
			var v = sets[j].substring(u1+7,u2);
			//alert(!isNaN(Number(v)));
			stArr.push(Number(v));
		}
		
		var idx = 0,m = stArr[0];
		for(var i=0;i<stArr.length;i++){
			if(m<stArr[i]){
				idx = i;
				m = stArr[i];
			}
		}
		y = idx;
		
		var sts = "";
		for(var j=0;j<sets.length;j++){
			if(j!=y){
				sts+=sets[j];
			}
		}
		// alert("----"+sts);
		
		///// 时间仓促，就这样了
		var sIndex1 = newDataXml.indexOf("<set");
		var spied1 = newDataXml.substring(0,sIndex1);
		//alert("spied1="+spied1);
		var sIndex2 = newDataXml.lastIndexOf("</set>");
		var spied2 = newDataXml.substring(sIndex2+6);
		//alert("spied2="+spied2);
		
		return (spied1 + sts + spied2);
		
	}
	
	/*******  对于特殊图形的  最大指标的算法 结束   Pie2D ******************************************/
	
	
	var cts = "";
	for(var i=0;i<cates.length;i++){
		if(i!=y){
			cts+=cates[i];
		}
	}
	var fragmentcates = wrapCategoriesTag(cts);
	//alert(fragmentcates);
	var dts = "";
	for(var i=0;i<datasets.length;i++){
		//alert(datasets[i]);
		var datasetTagTmp = datasets[i].substring(0,datasets[i].indexOf(">")+1);
		//alert(datasetTagTmp);
		var sets = getSets(datasets[i]);
		//alert("sets="+sets);
		
		var sts = "";
		for(var j=0;j<sets.length;j++){
			if(j!=y){
				sts+=sets[j];
			}
		}
		dts += wrapDatasetTag(datasetTagTmp,sts);
	}
	
	var fragmentcdts = fragmentcates+dts;
	
	var rtDataXml = fragment1+fragmentcdts+fragment2;
	
	return rtDataXml;
	
	
	
	
	/*
	//alert(dataSetArr.length);
	
		var chatObj = eval("c_" + chartId);
		var pdata = eval("pdata_" + chartId);
		var sdata;
		try{
		sdata=eval("sdata_"+chartId);
		}catch(e){}
		var dimArr = eval("dim_" + chartId);
		var bigIndex = 0, bigValue = null, thisValue=null, bigDimValue = "";
		if (typeof pdata=='object'&&pdata instanceof Array&&pdata.length > 0) {
			var pdataProxy=pdata[0];
			var doubleDim=false;
			if(typeof pdataProxy!='object' &&!(pdataProxy instanceof Array))
				pdataProxy=pdata;
			else
				doubleDim=true;   //二维数组
				var flag=true;
			//判断是否全部为空
			for ( var value in pdataProxy) {
				if(!isNaN(Number(value))){ 
					if(pdataProxy[value]==null || pdataProxy[value]==""){
						flag=false;
					}else{
						flag=true;
						break;
					}
				}
			}
			if(!flag){
				return newDataXml;
			}
			//如果是二维数组,需要把同一维度的数据相加后，再比较大小
			if(doubleDim==true){
				var tempArr=new Array();
				var dimLen=pdata[0].length;
				var subArrLen=pdata.length;
				for(var i=0;i<dimLen;i++){
					var tempVal=0;
					for(var j=0;j<subArrLen;j++){
						if(!isNaN(Number(pdata[j][i])))
							tempVal=tempVal+pdata[j][i];
					}
					tempArr[i]=tempVal;
				}
				pdataProxy=tempArr;
			}
			
			for ( var value in pdataProxy) {
				thisValue = pdataProxy[value];
				
				if(!isNaN(thisValue)){
					if (bigValue == null) {
						bigValue = thisValue;
						bigIndex = value;
						bigDimValue = dimArr[value];
						continue;
					}
					if (thisValue > bigValue) {
						bigIndex = value;
						bigValue = thisValue;
						bigDimValue = dimArr[value];
					}
				}
			}
			
			if(window.console&&window.console.log&&_chartDebug)
			 console.log("bigValue : "+bigValue+"     bigIndex : "+bigIndex);
				var categoryIndex = 0, categoryEndIndex, category = '<category', categoryEnd = '</category>';
	            //找到lable
				for ( var index = 0; index <= bigIndex; index++) {
					if(categoryIndex!=0)
						categoryIndex=categoryIndex+category.length;
					categoryIndex = newDataXml.indexOf(category, categoryIndex);
					if(categoryIndex==-1)
						break;
				}
				if(categoryIndex!=-1){
					categoryEndIndex = newDataXml.indexOf(categoryEnd, categoryIndex);
					
					newDataXml = newDataXml.substring(0, categoryIndex)
								+ newDataXml.substr(categoryEndIndex+ categoryEnd.length);
					if(window.console&&window.console.log&&_chartDebug)
						console.log("categoryIndex : "+categoryIndex+"    categoryEndIndex : "+categoryEndIndex+newDataXml);
				}
				
			var setIndex=0,setEndIndex,set='<set',setEnd='</set>';
			for(var outIndex=0;outIndex<pdata.length&&doubleDim==true;outIndex++){
				//有多个dataset的时候，很有用
				//找到set
				setIndex=newDataXml.indexOf("<dataset",setIndex)+8;
				for(var index = 0; index <= bigIndex; index++){
					if(setIndex!=0)
						setIndex=setIndex+set.length;
					setIndex=newDataXml.indexOf(set,setIndex);
					if(setIndex==-1)
						break;
				}
				if(setIndex!=-1){
					setEndIndex=newDataXml.indexOf(setEnd,setIndex);
					newDataXml=newDataXml.substring(0,setIndex)+newDataXml.substr(setEndIndex+setEnd.length);
					if(window.console&&window.console.log&&_chartDebug)
						console.log("categoryIndex : "+categoryIndex+"    categoryEndIndex : "+categoryEndIndex+newDataXml);
				
				}
			}
			
			if(doubleDim==false){
				for(var index = 0; index <= bigIndex; index++){
					if(setIndex!=0)
						setIndex=setIndex+set.length;
					setIndex=newDataXml.indexOf(set,setIndex);
					if(setIndex==-1)
						break;
				}
				if(setIndex!=-1){
				setEndIndex=newDataXml.indexOf(setEnd,setIndex);
				newDataXml=newDataXml.substring(0,setIndex)+newDataXml.substr(setEndIndex+setEnd.length);
				}
			}
			//console.log("----------"+newDataXml);
			if(typeof sdata =='object'&& sdata instanceof Array &&sdata.length>0){
				//如果有右 Y轴   这里需要修改，放到以后需要的时候
				for(var outIndex=0;outIndex<sdata.length;outIndex++){
					setIndex=newDataXml.indexOf("<dataset",setIndex);
					for(var index = 0; index <= bigIndex; index++){
						setIndex=setIndex+set.length;
						setIndex=newDataXml.indexOf(set,setIndex);
						if(setIndex==-1)
							break;
					}
					if(setIndex!=-1){
					setEndIndex=newDataXml.indexOf(setEnd,setIndex);
					newDataXml=newDataXml.substring(0,setIndex)+newDataXml.substr(setEndIndex+setEnd.length);
					}
				}
			}
		}
		if(categoryIndex == -1 ){
			setIndex=newDataXml.indexOf("<set",bigIndex);
			setEndIndex=newDataXml.indexOf("</set>",bigIndex);
			var set1= newDataXml.substring(0,setIndex);
			var set2= newDataXml.substring(setIndex);
			set2= set2.substring(set2.indexOf("/>")+2);
			newDataXml=set1+set2;
		}
		if(window.console&&window.console.log&&_chartDebug)
		console.log(newDataXml);
		
		
		return newDataXml;
		
		*/
}
function showValues(newDataXml){
	var value='1';
	var attFinder = "showValues='";
	var dataIndex = newDataXml.indexOf(attFinder) + attFinder.length;
	newDataXml = newDataXml.substring(0, dataIndex) + value
			+ newDataXml.substr(dataIndex + 1);
	return newDataXml;
}
function hideDataByLabel(newDataXml,labelArr){
	
	var seriesNameFinder="",finderIndex=0,finderEndIndex;
	var dataSetEnd="</dataset>",dataSet="<dataset";
	for(var outIndex=0;outIndex<labelArr.length;outIndex++){
		seriesNameFinder="seriesName='"+labelArr[outIndex]+"'";
		finderIndex=newDataXml.indexOf(seriesNameFinder);
		if(finderIndex==-1)
			continue;
		finderEndIndex=newDataXml.indexOf(dataSetEnd,finderIndex);
		var temp=newDataXml.substr(0,finderIndex);
		finderIndex=temp.lastIndexOf(dataSet);
		newDataXml=temp.substr(0,finderIndex)+newDataXml.substr(finderEndIndex+dataSetEnd.length);
	}
	return newDataXml;
}
function getChartData(chartObj) {
	var dataFinder = "&dataXML=";
	var dataEndFinder = "/></object>";
	var newDataXml = "";
	var swfHtml = chartObj.getSWFHTML();
	alert("html="+swfHtml);
	var typeFinder = "movie\"";
	var ctype =	swfHtml.substring(swfHtml.indexOf(typeFinder)+6);
	alert(ctype.substring(ctype.indexOf("value=\"/")+8,ctype.indexOf("?")));
	// console.log(swfHtml);
	var dataIndex = swfHtml.indexOf(dataFinder);
	var dataEndIndex = swfHtml.indexOf(dataEndFinder, dataIndex);
	// console.log("index : "+dataIndex+" endIndex: "+dataEndIndex+" len :
	// "+swfHtml.length);
	newDataXml = "\"<"+ swfHtml.substring(dataIndex + dataFinder.length + 1, dataEndIndex);
	if(window.console&&window.console.log&&_chartDebug)
	 console.log(newDataXml);
	return newDataXml;
}

// ---------------------------------------

/**
 * 
 * @param chartObj
 * @return
 */
function getChart(chartObj) {
	var chart = new Object();
	
	var dataFinder = "&dataXML=";
	var dataEndFinder = "/></object>";
	
	var typeFinder = "\"movie\"";
	var typeMiddleFinder1 = "value=\"";
	var typeMiddleFinder2 = "/";
	var typeEndFinder = "?";
	
	var newDataXml = "";
	var type = "";
	
	var swfHtml = chartObj.getSWFHTML();
	
	var typeTmp1 = swfHtml.substring(swfHtml.indexOf(typeFinder)+6);
	var tmpIndex = typeTmp1.indexOf(typeMiddleFinder1);
	var tmpEndIndex = typeTmp1.indexOf("?")
	var typeTmp2 = typeTmp1.substring(tmpIndex+8,tmpEndIndex);
	var type = typeTmp2.substring(typeTmp2.lastIndexOf("/")+1,typeTmp2.lastIndexOf("."));
	
	var dataIndex = swfHtml.indexOf(dataFinder);
	var dataEndIndex = swfHtml.indexOf(dataEndFinder, dataIndex);
	newDataXml = "\"<"+ swfHtml.substring(dataIndex + dataFinder.length + 1, dataEndIndex);
	
	chart.data = newDataXml;
	chart.type = type;
	return chart;
}

function wrapCategoriesTag(xml){
	return "<categories>"+xml+"</categories>";
}
function wrapDatasetTag(tag,xml){
	return tag+xml+"</dataset>";
}

/**
 * public
 * @param xml
 * @return Array
 */
function getCategories(xml){
	var sIndex = xml.indexOf("<categories>");
	if(sIndex<0){
		return null;
	}
	var eIndex = xml.indexOf("</categories>");
	var catesC = xml.substring(sIndex+12,eIndex);
	var cates = catesC.split("</category>");
	
	var arr = new Array();
	for(var i=0;i<cates.length;i++){
		if(cates[i]!=""){
			arr.push(cates[i]+"</category>");
		}
	}
	
	return arr;
}

/**
 * public 
 * @param xml
 * @returns Array
 */
function getDataSets(xml){
	var xmlDataSetArray = new Array();
	getDataSetFragment(xml,xmlDataSetArray);
	return xmlDataSetArray;
}
/**
 * private 
 * @param xml
 * @param arr
 * @returns {String}
 */
function getDataSetFragment(xml,arr){
	var sIndex = xml.indexOf("<dataset ");
	if(sIndex<0) return "";
	var dataSetC = xml.substring(sIndex+9);
	var eIndex = dataSetC.indexOf("</dataset>");
	var dataSet =  xml.substring(sIndex,sIndex+eIndex+19);
	arr.push(dataSet);
	getDataSetFragment(dataSetC,arr);

}


/**
 * public 
 * @param xml
 * @returns Array
 */
function getSets(xml){
	var xmlSetArray = new Array();
	getSetFragment2(xml,xmlSetArray);
	return xmlSetArray;
}
/**
 * private 
 * @param xml
 * @returns {String}
 */
function getSetFragment1(xml,arr){
	var sIndex = xml.indexOf("<set ");
	if(sIndex<0) return "";
	var setC = xml.substring(sIndex+5);
	var eIndex = setC.indexOf("<set ");
	if(eIndex<0){
		var end = setC.indexOf("/>");
		eIndex = end+2;
	}
	var set1 =  xml.substring(sIndex,sIndex+eIndex+5);
	arr.push(set1);
	getSetFragment1(setC,arr);
}
/**
 * private 
 */
function getSetFragment2(xml,arr){
	var sIndex = xml.indexOf("<set ");
	if(sIndex<0) return "";
	var setC = xml.substring(sIndex+5);
	var eIndex = setC.indexOf("</set>");
	var set =  xml.substring(sIndex,sIndex+eIndex+11);
	arr.push(set);
	getSetFragment2(setC,arr);
	
}
