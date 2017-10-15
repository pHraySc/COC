<%@page import="java.net.URLDecoder"%>
<%@page import="com.ailk.biapp.ci.localization.zhejiang.webservice.*,com.ailk.biapp.ci.entity.*,com.asiainfo.biframe.utils.spring.SystemServiceLocator"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title>模拟触发客户群审批下载</title>
<%
IZjYxglGroupInfoClient service  = null;
try {
    service = (IZjYxglGroupInfoClient) SystemServiceLocator.getInstance().getService("zjYxglGroupInfoClientImpl");
    CiCustomGroupInfo ciCustomGroupInfo = new CiCustomGroupInfo();
	ciCustomGroupInfo.setCustomGroupId("3h7VS39XhfvrLBt_WLsU7h");
	ciCustomGroupInfo.setCustomGroupName("1");
	ciCustomGroupInfo.setCustomGroupDesc("1");
	ciCustomGroupInfo.setCreateUserId("admin");
	ciCustomGroupInfo.setCreateUserName("admin");
	CiCustomListInfo ciCustomListInfo = new CiCustomListInfo();
	ciCustomListInfo.setCustomGroupId("3h7VS39XhfvrLBt_WLsU7h");
	ciCustomListInfo.setListTableName("CI_CUSER_20140312153612847");
	service.createRequirementInfo(ciCustomGroupInfo, ciCustomListInfo);
} catch (Exception e) {
    e.printStackTrace();
}
%>
</head>
<body>
</body>
</html>
