 /*
 * Title: jquery.extend.num.format.js. 
 * Description: 

 * Aucthor:luyan
 * Create Date:2013-06-06
 * Call Method: $.formatNumber(2000000010, 3, ',');      //result:   2,000,000,010  
 */
(function($) {
	$.extend({
		formatNumber : function(str, step, splitor) {
			//创建$.format()的jquery全局函数，带有三个参数
			str = str.toString();    //转换成字符串数据类型
			var len = str.length;    //获取字符串长度
			if(len > step) {
				var l1 = len%step,   //l1为余数部分
				l2 = parseInt(len/step),  //l2为整数部分
				arr = [],  //创建空数组
				first = str.substr(0, l1); //返回一个从指定位置开始的指定长度的子字符串。
				if(first != '') {
					arr.push(first);  //将新元素添加到一个数组中，并返回数组的新长度值。
					//参数step为str字符串每step个字符为一组子字符串，first为先处理str中不满step个字符的部分
				}
				for(var i=0; i<l2 ; i++) {
					arr.push(str.substr(l1 + i*step, step));  //把子字符串放入数组arr中
				}
				str = arr.join(splitor);//返回字符串值，包含连接一起的数组所有元素，元素由指定分隔符分隔开来
			}
			return str;
		}
	});
})(jQuery); 
