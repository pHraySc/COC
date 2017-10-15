<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<script type="text/javascript">
$(document).ready( function() {
	  //设置选中页的效果
	  $(".num").each(function(){
		if($(this).text() == "${pager.pageNum}"){
			$(this).addClass("num_on");
		}
	 });
});
</script>	
 <div id="kpiList">
    <table width="100%" id="sortTable"  class="tableStyle" cellpadding="0" cellspacing="0">
      <thead>
        <tr>
        <th width="60px"><input type="checkbox" name="checkBox" id="checkBoxAll" onclick="checkBoxAll(this);" /></th>
        <th dataType='text'>指标编码</th>
        <th dataType='text'>字段名称</th>
        <th dataType='text'>数据源表名</th>
        </tr>
       </thead>
       <tbody>
        <c:forEach items="${pager.result}" var="po" varStatus="st">
        	<tr>
              <td><input type="checkbox" name="checkBox" class="checkBox" id="checkBox${po.indexCode}" value="${po.indexCode}" onclick="setLabelAttrListText(this);"/></td>              
              <td>${po.indexCode}</td>
              <td>${po.dataSrcColName}</td>
              <td>${po.dataSrcName}</td>
             </tr>
        </c:forEach>
        </tbody>
    </table>
     <div class="pagenum" id="pager"><jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" /></div>
  </div>
