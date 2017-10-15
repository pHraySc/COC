<%@page import="com.asiainfo.biframe.privilege.ICity" %>
<%@ page import="com.asiainfo.biframe.privilege.IUserPrivilegeService" %>
<%@ page import="com.asiainfo.biframe.utils.spring.SystemServiceLocator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head></head>
<body>
<%

    IUserPrivilegeService service = null;
    try {
        service = (IUserPrivilegeService) SystemServiceLocator.getInstance().getService("userPrivilegeService");
    } catch (Exception e) {
        e.printStackTrace();
    }


    System.out.println("###getUser: ym(0)");
    System.out.println(service.getUser("ym").toString());
    System.out.println();

    System.out.println("###getUser: zhoulu(572)");
    System.out.println(service.getUser("zhoulu").toString());
    System.out.println();


    System.out.println("###getCityByCityId: 省公司 0");
    System.out.println(service.getCityByCityID("0").toString());
    System.out.println();

    System.out.println("###getCityByCityId: 571");
    System.out.println(service.getCityByCityID("571").toString());
    System.out.println();

    System.out.println("###getCityByCityId: 5701");
    System.out.println(service.getCityByCityID("5701").toString());
    System.out.println();

    System.out.println("###getSubCitysById: 0");
    for (ICity city : service.getSubCitysById("0")) {
        System.out.println(city);
    }
    System.out.println();

    System.out.println("###getSubCitysById: 574");
    for (ICity city : service.getSubCitysById("574")) {
        System.out.println(city);
    }
    System.out.println();

    System.out.println("###getCityByUser: ym");
    for (ICity city : service.getCityByUser("ym")) {
        System.out.println(city);
    }
    System.out.println();

    System.out.println("###getCityByUser: zhoulu");
    for (ICity city : service.getCityByUser("zhoulu")) {
        System.out.println(city);
    }
    System.out.println();

    System.out.println("###getCityByUser: jianyq");
    for (ICity city : service.getCityByUser("jianyq")) {
        System.out.println(city);
    }
    System.out.println();

    System.out.println("###isAdmin: zhoulu");
    System.out.println(service.isAdminUser("zhoulu"));
    System.out.println();

    System.out.println("###isAdmin: cenwd");
    System.out.println(service.isAdminUser("cenwd"));
    System.out.println();

    System.out.println("###getUserDept: zhoulu");
    System.out.println(service.getUserDept("zhoulu"));
    System.out.println();


%>

</body>
</html>
