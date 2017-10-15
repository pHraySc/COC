<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
	String columnId = request.getParameter("columnId");
%>
<c:set var="columnId" value="<%=columnId%>"></c:set>
<script type="text/javascript">
jQuery.fn.page = function(options) {
	var settings = {
		url :'',
		param:'',
		objId:'',
		callback:false
	};
	var opts = jQuery.extend(settings, options);
	var postfix = '<%=columnId%>';
	if (opts.url == "")
		alert("你必须提供url参数。");
	$(this).find(".pageButton").click(function(){
		var pageflag = $(this).attr("pageFlag");
	    var pageNum = $("#pageNum" + postfix).val();
		if(opts.url.indexOf("pager.pageNum") == -1){//防止点击上一页或者下一页过快传递重复参数
	    	opts.url += "&pager.pageNum="+pageNum;
	    }
	    if(opts.url.indexOf("pager.pageFlag") == -1){
	    	opts.url += "&pager.pageFlag="+pageflag;
	    }
	    if(opts.url.indexOf("pager.pageSizeSel") == -1){
	    	opts.url +="&pager.pageSizeSel="+$("#pageSizeSel").val();
	    }
        $.post( opts.url,
                opts.param,
                function (data) {
                    $("#" + opts.objId).html(data);
                    if (opts.callback) opts.callback();
                },
                "html"
        );
	});
	//输入分页数跳页
	$(this).find("#jumpPage").click(function(){
		opts.url +="&pager.pageNum="+$("#jump" + postfix).val();
		opts.url +="&pager.pageFlag=G";
        $.post(opts.url,
                opts.param,
                function (data) {
                    $("#" + opts.objId).html(data);
                    if (opts.callback) opts.callback();
                },
                "html"  
        );
	});
	
	//输入框回车事件
	$("#jump" + postfix).keydown(function (ev) {
		var event= ev||event;
		if (event.keyCode == "13") {//keyCode=13回车键
			opts.url +="&pager.pageNum="+$(this).val();
			opts.url +="&pager.pageFlag=G";
	        $.post(opts.url,
	                opts.param,
	                function (data) {
	                    $("#" + opts.objId).html(data);
	                    if (opts.callback) opts.callback();
	                },
	                "html"  
	        );
		}
		
	 });

	//点击分页数跳页
	$(this).find(".num").click(function(){
		var num = $(this).html();
		if(!regJump(num)){
			num = 1;
		}
		if(opts.url.indexOf("pager.pageNum") == -1){
			opts.url +="&pager.pageNum="+num;
	    }
		if(opts.url.indexOf("pager.pageFlag") == -1){
			opts.url +="&pager.pageFlag=G";
	    }
        $.post(opts.url,
        	    opts.param,
                function (data) {
                    $("#" + opts.objId).html(data);
                    if (opts.callback) opts.callback();
                },
                "html"
        );
	});
	//分页输入数校验
	function regJump(num){
		var patrn=/^[0-9]+$/; 
		if (!patrn.exec(num)){
		   return false;
		   }else{
		   return true;
		}
	}
}
</script>

<form id="pageForm${columnId }">
<input type="hidden" name="pager.totalSize" value="${pager.totalSize }">
<input type="hidden" name="pager.totalPage" value="${pager.totalPage }">
</form>
<input type="hidden" id="pageNum${columnId }" name="pager.pageNum" value="${pager.pageNum}">
	<div class="newPagerBox">
		<ol>
			<li>
			<a href="javascript:;" class="pageFirst num" id="num_1"></a>
			</li>
			<li>
				<a href="javascript:;" class="pageUp pageButton" pageFlag="P"> </a>
			</li>
                 
			<li>
				<span class="fleft">第</span>
				<input type="text"  id="jump${columnId }" class="fleft pageInput" value="${pager.pageNum }"/>
				<span class="fleft" >页</span>
			</li>
			<li>
			<p><span class="fleft">共</span> <strong class="fleft">${pager.totalPage }</strong><span class="fleft">页</span></p>
			</li>
			<li>
				<a href="javascript:;" class="pageNext pageButton" pageFlag="N"></a>
			</li>
			<li>
				<a href="javascript:;" class="pageLast num"  id="num_${pager.totalPage}">${pager.totalPage}</a>
			</li>
		</ol>
	</div>
