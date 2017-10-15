<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
	String waterMark = Configure.getInstance().getProperty("WATER_MARK");
%>
<script  type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.page.js"></script>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/ciTableSorter.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	  //设置选中页的效果
	  $(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	 });
	  $('#sortTable th').click(sortTable);

	  //下面的两端js方法是用来处理我创建的标签列表页面中的操作按钮效果的
	  	$(".icon_slidemore").on("click",function(e){
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
			if(!$(this).hasClass("icon_slidemore_slided")){
				$(this).addClass("icon_slidemore_slided");
				var posx=$(this).offset().left;
				var posy=$(this).offset().top;
				var list=$(this).next(".iconslidect");
				list.css({"left":(posx-(79-38)),"top":posy+16}).show();
			}
		});
		$(document).click(function(){
			$(".iconslidect").hide();
			$(".icon_slidemore_slided").removeClass("icon_slidemore_slided");
		})

	//定义限制宽度单元格的默认宽度
		var tableWidth=$(".mainTable").parent("div").width();
		$(".mainTable td div.nowrap").width(tableWidth-900);
});
</script>	
<style type="text/css">
	.dialog_btn_div{*position:static;}
	<%if("true".equals(waterMark)){ %>
	#labelClassList{background:url(${ctx}/watermark?transparent=0.07&userId=${userId}) ;}
	<%} %>
</style>
<!-- area_operation end -->
<!-- area_data start -->
<div class="watermark_class"></div>
 <div id="labelClassList">
    <table width="100%" id="sortTable" class="commonTable mainTable" cellpadding="0" cellspacing="0">
      <thead>
        <tr>
        <th><input type="checkbox" name="checkBox" id="checkBoxAll" onclick="checkBoxAll(this)" /></th>
        <th  class="header" dataType='num'>序号<span class="sort">&nbsp;</span></th>
        <th  class="header" dataType='text'>标签名称<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='date'>创建时间<span class="sort">&nbsp;</span></th>
        <th class="header" dataType='text'>数据状态<span class="sort">&nbsp;</span></th>
        <th  class="noborder">操作</th>
        </tr>
       </thead>
       <tbody>
        <c:forEach items="${pager.result}" var="po" varStatus="st">
        
		    <c:choose>
		        <c:when test="${st.index % 2 == 0 }">
		          <tr class="even">
		        </c:when>
		        <c:otherwise>
		          <tr class="odd">
		        </c:otherwise>
		    </c:choose>
            <c:choose>
            	<c:when test="${labelOperManagerList[st.count-1].deleteFlag == 1}">
            		<td>
            			<input type="checkbox" name="checkBox" class="checkBox" id="checkBox${st.index+1}" value="${po.labelId}" onclick="checkDelete()"/>
            		</td>
            	</c:when>
            	<c:otherwise>
            		<td>
              			<input type="checkbox" name="checkBox" class="checkBox" id="checkBox${st.index+1}" value="" disabled onclick="checkUnDelete()"/>
            		</td>
            	</c:otherwise>
            </c:choose>
            <td>${st.index+1}</td>
            <td class="align_left"><div class="nowrap" align="left" title="${po.labelName }"><a href="javascript:showConfigLabel('${po.labelId}');" id="link-look" >${po.labelName}</a></div></td>
			<c:choose>
				<c:when test="${po.createTime==null}">
					<td class="align_center">-</td>
				</c:when>
				<c:otherwise>
					<td class="align_right"><fmt:formatDate value="${po.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
				</c:otherwise>
			</c:choose>
           <td>${po.dataStatusName}</td>
              <td class="main_icon">
              		<!--上线的条件：上级标签是已生效标签 -->
	              	<c:if test="${labelOperManagerList[st.count-1].onLineFlag == 1 }">
              			<a class="icon_onLine" href="javascript:onLineClassesLabel('${po.labelId}')">上线</a>
	              	</c:if>
	              	<!--下线的条件：1、已生效  2、没有已生效、未生效且已发布的子标签 -->
	              	<c:if test="${labelOperManagerList[st.count-1].offLineFlag == 1 }">
              			<a class="icon_offLine" href="javascript:offLineLabel('${po.labelId}')">下线</a>
	              	</c:if>
	              	<!--修改的条件：非已下线标签 -->
	              	<c:if test="${po.dataStatusId !=  5}">
	              		<a class="icon_edit" href="javascript:editLabelClassify('${po.labelId}')">修改标签分类</a>
	              	</c:if>
              		<!--删除的条件：没有已生效、未生效且已发布的子标签 -->
              		<c:if test="${labelOperManagerList[st.count-1].deleteFlag == 1}">
	              	<a class="icon_del" href="javascript:deleteLabel('${po.labelId}')">删除</a>
	              	</c:if>
              </td>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagenum" id="pager">
    <jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
    </div>
    <input type="hidden" name="totalSize" id="totalConfigSize" value="${pager.totalSize}">
  </div>
<!-- area_data start -->
