<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
jQuery.fn.page = function(options) {
	var settings = {
		url :'',
		param:'',
		objId:'',
		callback:false
	};
	var opts = jQuery.extend(settings, options);
	if (opts.url == "")
		alert("你必须提供url参数。");
	$(this).find(".pageButton").click(function(){
		var pageflag = $(this).attr("pageFlag");
	    var pageNum = $("#syspageNum").val();
		if(opts.url.indexOf("syspager.pageNum") == -1){//防止点击上一页或者下一页过快传递重复参数
	    	opts.url += "&syspager.pageNum="+pageNum;
	    }
	    if(opts.url.indexOf("syspager.pageFlag") == -1){
	    	opts.url += "&syspager.pageFlag="+pageflag;
	    }
	    if(opts.url.indexOf("syspager.pageSizeSel") == -1){
	    	opts.url +="&syspager.pageSizeSel="+$("#pageSizeSel").val();
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
	$(this).find("#jumpSysPage").click(function(){
		opts.url +="&syspager.pageNum="+$("#jump_syspager").val();
		opts.url +="&syspager.pageFlag=G";
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
	$("#jump_syspager").keydown(function (ev) {
		var event= ev||event;
		if (event.keyCode == "13") {//keyCode=13回车键
			opts.url +="&syspager.pageNum="+$(this).val();
			opts.url +="&syspager.pageFlag=G";
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
		if(opts.url.indexOf("syspager.pageNum") == -1){
			opts.url +="&syspager.pageNum="+num;
	    }
		if(opts.url.indexOf("syspager.pageFlag") == -1){
			opts.url +="&syspager.pageFlag=G";
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
<div id="syspager" class="dialogPageBox clearfix">
<form id="syspageForm">
<input type="hidden" name="syspager.totalSize" value="${syspager.totalSize }">
<input type="hidden" name="syspager.totalPage" value="${syspager.totalPage }">
</form>
<input type="hidden" id="syspageNum" name="syspager.pageNum" value="${syspager.pageNum}">
    <div class="total">总记录<span>${syspager.totalSize}</span>条</div>
    <c:choose>
    <c:when test="${syspager.totalPage > 1}">
    <ul>
        <li>
        <c:choose>
	       <c:when test="${syspager.hasPrevPage}">
		      <a href="javascript:;" class="prev_next pageButton"  pageFlag="P">上一页</a>
		   </c:when>
		   <c:otherwise>
			  <a href="javascript:;" class="prev_next disabled"  pageFlag="P">上一页</a>
			</c:otherwise>
		</c:choose>
        </li>
        <c:forEach items="${syspager.pageButton}" var="buttonList" varStatus="st">
                  <li><a href="javascript:;" class="num" id="num_${st.index+1}">${st.index+1}</a></li>
        </c:forEach>
        <c:choose>
                 <c:when test="${syspager.totalPage >= 6}">
                    <!-- 如果是第一页加载1 2 页  -->
                    <c:choose>
                       <c:when test="${syspager.pageNum == 1}">
                           <li><a href="javascript:;" class="num" id="num_1">1</a></li>
                           <li><a href="javascript:;" class="num" id="num_2">2</a></li>
                       </c:when>
                    <c:otherwise>
                       <li><a href="javascript:;" class="num" id="num_1">1</a></li>
                    </c:otherwise>
                    </c:choose>
                    <!-- 从第三页开始前面加... -->
                    <c:choose>
                       <c:when test="${syspager.pageNum >= 3 }">
                          <li><a href="javascript:;">…</a></li>
                       </c:when>
                    </c:choose>
                    <c:choose>
                       <c:when test="${syspager.pageNum>1 && syspager.pageNum < syspager.totalPage}">
                       <!-- 如果不是第二页要加载前一页（第一页已经在前面添加） -->
                       <c:choose>
                           <c:when test="${syspager.pageNum>2}">
                              <li><a href="javascript:;" class="num" id="num_${syspager.pageNum-1}">${syspager.pageNum-1}</a></li>
                           </c:when>
                       </c:choose>
                       <li><a href="javascript:;" class="num">${syspager.pageNum}</a></li>
                       <!-- 如果不是最后第二页要加载后一页（最后一页已经在后面添加） -->
                       <c:choose>
                           <c:when test="${syspager.pageNum+1 < syspager.totalPage}">
                             <li><a href="javascript:;" class="num" id="num_${syspager.pageNum+1}">${syspager.pageNum+1}</a></li>
                           </c:when>
                       </c:choose>
                       </c:when>
                    </c:choose>
                    <!-- 不是后三页后面拼接... -->
                    <c:choose>
                       <c:when test="${syspager.totalPage - syspager.pageNum >=2}">
                           <li><a href="javascript:;">…</a></li>
                       </c:when>
                    </c:choose>
                    <!-- 最后一页加载时要同时加载前一页 -->
                    <c:choose>
                       <c:when test="${syspager.totalPage == syspager.pageNum}">
                           <li><a href="javascript:;" class="num" id="num_${syspager.pageNum-1}">${syspager.pageNum-1}</a></li>
                           <li><a href="javascript:;" class="num" id="num_${syspager.pageNum}">${syspager.pageNum}</a></li>
                       </c:when> 
                       <c:otherwise>
                           <li><a href="javascript:;" class="num" id="num_${syspager.totalPage}">${syspager.totalPage}</a></li>
                       </c:otherwise>
                    </c:choose>
                 </c:when>
        </c:choose>
        <li>
        <c:choose>
				<c:when test="${syspager.hasNextPage}">
					<a href="javascript:;" class="prev_next pageButton" pageFlag="N" >下一页</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:;" class="prev_next disabled" pageFlag="N" >下一页</a>
				</c:otherwise>
		</c:choose>
        </li>
        <li class="jump"><span>跳转</span><input id="jump_syspager" type="text" /><span>页</span></li>
        <li class="num_jump_box"><a href="javascript:;" class="num_jump" id="jumpSysPage">GO</a></li>
        </c:when>
       </c:choose>
    </ul>
</div>