 /*
 * Title: jquery.tablesorter.remote.js 
 * Description: 表格远程排序控件
 * Aucthor:chengjia
 * Create Date:2011-01-18
 * Call Method:
 */
jQuery.fn.remotesorter = function(options) {

	$(this).find("th").filter(".sorter").click(function(){
		//排序之前,先去请求后台数据
		var url = options.url;
		url+="&orderBy="+$(this).attr("orderBy");
		var ascOrDesc = $(this).attr("ascOrDesc");
		if(ascOrDesc==0){
			//降序排列
			url+="&ascOrDesc=1";
			$(this).removeClass("header");
			$(this).removeClass("headerSortDown");
			$(this).addClass("headerSortUp");
		}else{
			//升序排列
			url+="&ascOrDesc=0";
			$(this).removeClass("header");
			$(this).removeClass("headerSortDown");
			$(this).addClass("headerSortUp");
		}
		options.obj.load(url);
	 });
	
	$(this).find("th").filter(".sorter").hover(
			function(){
				if($(this).hasClass('headerSortUp')){
					$(this).addClass('headerSortDown-hover');
				}else if($(this).hasClass('headerSortDown')){
					$(this).addClass('headerSortUp-hover');
				}else{
					$(this).addClass('headerSortDown-hover');
				}
			},
			function(){
				$(this).removeClass('headerSortDown-hover');
				$(this).removeClass('headerSortUp-hover');
			}
		)
}