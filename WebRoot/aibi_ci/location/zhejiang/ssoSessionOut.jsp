<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>异常</title>
<SCRIPT language=javascript>
	//setTimeout("go()",1000);
	
	var iTime = 10;
	function setShowText(){
		if(iTime > 0){
			document.getElementById('_SHOW_TEXT').innerText = (iTime--);
		}
		else{
			window.clearInterval(txtInter);
			window.location.href = "http://jyfxnew.zj.chinamobile.com/index2.jsp";
		}
	}
	var txtInter = window.setInterval("setShowText()", 1000)
	function login()
	{
		window.clearInterval(txtInter);
		window.location.href = "http://jyfxnew.zj.chinamobile.com/index2.jsp";
	}
</SCRIPT>
  </head>
  <body>
   <center><font style="color:red;font-size:16px;font-weight:bold;">
    <%
			String msg = (String) request.getSession().getAttribute("NO_JYFX_ACCOUNT");
    System.out.print(msg);
			if ("NO_JYFX_ACCOUNT".equals(msg)) {
    %>
			 对不起，	经分系统无法查询到该用户信息，请联系管理员创建经分账户。
    <% 
			}else{
    %>
    	         对不起，系统无法获取登录凭证，当前会话为空，请重新登录！
	<%
			}
	%>
   </font></center>
	<br>
	<center style="font-size:12px;"><a href="javascript:login();">登录</a>&nbsp;&nbsp;<font style="color:blue;"> （<span id="_SHOW_TEXT">10</span>秒后自动跳转到登录页面）</font></center>
  </body>
</html>
