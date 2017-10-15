<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript">
		//关闭此	
		function closeDialog() {
			parent.closeDialog("#showBSCLDialog");
		}
	</script>
</head>
<body>
	<div class="container">
		<div class="tip-div-text">
			<p class="tip-title-p">
				<span class="strong">带有红框的标签暂无最新周期数据，不能采用"保守数据策略"保存客户群</span>
			</p>
		</div>
		<div class="tip-div-center"></div>
		<div class="tip-div-text">
			<p class="tip-text-p">
				1、若需要最新周期数据， 请尝试切换<span class="strong">"预约数据策略"</span>保存客户群
			</p>
			<p class="tip-text-p">
				2、若需要立即产生客户群清单， 请尝试在日历框中选择上一周期保存客户群
			</p>
		</div>
		<div class="btnActiveBox bs-tip-btn">
		   <input class="ensureBtn" onclick="closeDialog();" type="button" value="确定" />
		</div>
	</div>
</body>
</html>