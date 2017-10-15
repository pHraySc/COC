<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户群共享</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/main.js"></script>
		<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/shareCustomerFormEntrance.js"></script>
</head>
<body>
<form id="saveForm" method="post">
<input id="customGroupId" type="hidden" name="ciCustomGroupInfo.customGroupId" value="${ciCustomGroupInfo.customGroupId}"/>
<input id="createCityId" type="hidden" name="ciCustomGroupInfo.createCityId" />
<input id="createUserId" type="hidden" name="ciCustomGroupInfo.createUserId" />
<div class="clientMsgBox" >
	<ol class="clearfix">
		<li>
   			<label  class="fleft labFmt100 star">用户群名称：</label>
   			<input type="text"  class="fleft inputFmt235"   name="ciCustomGroupInfo.customGroupName" type="text" id="customGroupName" 
   					maxlength="35"  value="请输入用户群名称" data-val="false"/>
   			<div id="cnameTip" class="tishi error" style="display:none;"></div>
 		</li>
 	</ol>
 </div>
<!-- 按钮区 -->
<div class="dialog_btn_div">
	<input id="saveBtn" name="" type="button" value="确认" class="tag_btn"/>
</div>
</form>
<input id="customGroupId_" type="hidden" value="${param['id']}"/>
<input id="customGroupName_" type="hidden" value="${param['name']}"/>
<input id="createCityId_" type="hidden" value="${param['cityId']}"/>
<input id="createUserId_" type="hidden" value="${param['createUserId']}"/>
<input id="msgShareForm" type="hidden" value="${param['msg']}"/>
</body>
</html>

