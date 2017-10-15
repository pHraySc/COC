<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String contactPhone = Configure.getInstance().getProperty("ERROR_CONTACT_PHONE"); 
String contactMobilePhone = Configure.getInstance().getProperty("ERROR_CONTACT_MOBILE_PHONE"); 
String contactEmail= Configure.getInstance().getProperty("ERROR_CONTACT_EMAIL"); 
%>
<html>
	<head>
		<title>错误页面</title>
		<link href="${ctx }/aibi_ci/assets/themes/common.css" rel="stylesheet"  />
		<link href="${ctx }/aibi_ci/assets/themes/default/main.css" rel="stylesheet"  />
		<script type="text/javascript" src="${ctx }/aibi_ci/assets/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/scrollbar/jquery.mCustomScrollbar.concat.min.js" ></script>
		<script type="text/javascript" src="${ctx }/aibi_ci/assets/js/main.js"></script>
		<SCRIPT type="text/javascript">
		$(function(){
			var _w=$(window).width(),_h=$(window).height();
			var tar=$(".page404main");
			if(_w<1017){
				tar.css("left",0).css("zoom",0.8);
				tar.find(".address").css("margin-top",356*0.8)
			}
			if(_h<626){
				tar.css("top",0).css("zoom",0.8);
			}
			
			//判断不是iframe引入
			if(!tar.parent().hasClass("page404body")){
				tar.css("position","static");
			}
			//如果联系人配置都为空，就不展示联系人
			<% if (StringUtils.isEmpty(contactPhone) 
				&& StringUtils.isEmpty(contactMobilePhone)
				&& StringUtils.isEmpty(contactEmail)) { %>
				$(".page404main").addClass("page404mainOther");
			<% } %>
		})
		</script>
	</head>
	<body class="page404body">
		<div class="page404main">
			<div class="page404main_inner">
		    	<ul class="address">
		    	<%=(StringUtils.isEmpty(contactPhone)?"":"<li>手机：" + contactPhone + "</li>")%>
		    	<%=(StringUtils.isEmpty(contactMobilePhone)?"":"<li>座机：" + contactMobilePhone + "</li>")%>
		    	<%=(StringUtils.isEmpty(contactEmail)?"":"<li>邮箱：" + contactEmail + "</li>")%>
		        </ul>
		        <div class="gotoHomepage">
		        	<a target="_top" href="${ctx }/ci/ciIndexAction!labelIndex.ai2do">&nbsp;</a>
		        </div>
		    </div>
		</div>
		<div id="errorMessage" style="display:none;">
			<table>
			<tr>
				<td>
					${exception.message}
				</td>
			</tr>
			<tr>
				<td><p>
					<%
					    if (request.getAttribute("exception") != null) {
							Exception e = (Exception)request.getAttribute("exception");
							StackTraceElement[] s = e.getStackTrace();
							String msg = "";
							out.println("class:" + e.getClass());
							
							//System.out.println("class:" + e.getClass());
							//System.out.println("cause:" + e.getCause());
							
							out.println("cause:" + e.getCause());
							for (int i = 0; i < s.length; i++) {
								out.println("\tat " + s[i] + "<br/>");
								//System.out.println("-"+s[i]);
							}
					    }
					%>
					</p>
				</td>
			</tr>
			</table>
		</div>
	</body>
</html>

