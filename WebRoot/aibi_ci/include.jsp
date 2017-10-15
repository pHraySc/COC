<%@ page  import="java.util.*" %>
<%@ page import="com.ailk.biapp.ci.util.*"%>
<%@ page import="com.ailk.biapp.ci.constant.*"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/cmp.tld" prefix="cmp" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/IdToName.tld" prefix="dim" %>
<%@ taglib uri="/WEB-INF/tlds/dateFormat.tld" prefix="dateFmt" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/devbase.tld" prefix="base"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure"%>
<%@ page import="com.asiainfo.biframe.privilege.IUserSession"%>
<%@ page isELIgnored="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<% 
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 

String coc_province = Configure.getInstance().getProperty("PROVINCE");
String teleOperator = Configure.getInstance().getProperty("TELE_OPERATOR");

String userid = (String)request.getAttribute("userId");
final IUserSession isession = (IUserSession) session.getAttribute(IUserSession.ASIA_SESSION_NAME);
if (isession != null) {
	if (userid == null || userid.equals("")) {
		if ("nj".equalsIgnoreCase(CommonConstants.base)) {
			userid = isession.getUserID().toLowerCase();
		} else {
			userid = isession.getUserID();
		}
	}
}
String version_flag = ServiceConstants.COOKIE_NAME_FOR_VERSION + "_" + userid;
%> 
<c:set var="version_flag" value="<%=version_flag %>"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


