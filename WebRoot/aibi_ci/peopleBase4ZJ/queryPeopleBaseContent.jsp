<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<script type="text/javascript">
	$(function() {
		
	});
</script>
<div class="peopleBasicTitleBox">
      <h3>${phoneNum}</h3>
</div>
<div class="peoleBasicContentBox">
	<c:forEach items="${ciTagTypeModelList}" var="tagType">
	<dl>
		<dt class="${tagType.icon}">
		   	<h5>${tagType.typeName}</h5>
		</dt>
		<dd>
			<ul class="peoleBasicContentList clearfix">
				<c:forEach items="${tagType.ciTagInfoList}" var="item">
					<li title="${item.tagName}">${item.tagName}</li>
                </c:forEach>
			</ul>
		</dd>
	</dl>
	</c:forEach>
</div>

