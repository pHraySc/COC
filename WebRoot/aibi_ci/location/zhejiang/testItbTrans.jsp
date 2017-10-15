<%@page import="java.net.URLDecoder"%>
<%@page import="com.ailk.biapp.ci.localization.zhejiang.service.*,java.util.*,com.ailk.biapp.ci.entity.*,com.asiainfo.biframe.utils.spring.SystemServiceLocator"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title>异常</title>
<%
IZjKhqItbService service  = null;
try {
    service = (IZjKhqItbService) SystemServiceLocator.getInstance().getService("zjKhqItbServiceImpl");
   	Map<String,Object> map = new HashMap<String,Object>();
   	map.put("KHQ_CODE", "KHQLVJX22");
   	map.put("KHQ_DESC", "KHQLVJX11111222_DESC");
   	map.put("TABLE_NAME", "LVJX_TEST_TABLE20140408");
   	map.put("DB_NO", "AAA");
   	map.put("END_TIME", "20150101");
   	map.put("CYCLE", "1");
   	map.put("COL_NAME", "AAA,BBB,CCC");
   	service.startKhqTrans(map);
} catch (Exception e) {
    e.printStackTrace();
}
%>
</head>
<body>
</body>
</html>
