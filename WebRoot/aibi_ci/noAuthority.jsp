<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>

<head runat="server">
<title>错误页面1</title>
<style type="text/css">
body {
	margin: 0px;
	line-height: 140%;
	color: #000000;
	font: 12px "Verdana", "Tahoma", "sans-serif";
	background-color: #cdd6dd;
	text-align: center;
}

table {
	width: 460px;
	margin: 0 auto;
}

table tr td {
	background-color: #F5F5F5;
}

#title {
	font-size: 16px;
	font-family: '宋体';
}

#content td {
	font-size: 14px;
	text-align: left;
	padding: 10px;
}

a {
	color: #333399;
	text-decoration: underline;
}

a:hover {
	color: #CC0000;
	text-decoration: none;
}

#message {
	margin-top: 100px;
	background-color: #FFFFFF;
	text-align: center;
	width: 500px;
	padding: 20px;
	border: 1px dotted #386792;
	margin-right: auto;
	margin-left: auto;
}
</style>
	</head>
	<body style="table-layout: fixed; word-break: break-all">
		<form id="form1" runat="server">
			<div align="center" id="message">
				<table border="0" cellspacing=0 cellpadding=0>
					<tr id='title'>
						<td height="50">
							<b><font color="#0068CA">温馨提示</font>
							</b>
						</td>
					</tr>
					<tr id='content'>
						<td>
							<span style="color: #FF3300;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对不起！您没有相应的权限或登录已超时！</span>
						</td>
					</tr>
					<tr>
						<td height="50"></td>
					</tr>
				</table>
			</div>

		</form>
	</body>
</html>
