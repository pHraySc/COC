<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>人为本</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
//查询相关js
$().ready(function() {
	$(".bg05").addClass("active");
	$("#_index").addClass("menu_on");
	$(".searchSubmit").bind("click", function(){
		search();
	});
});

function search() {
	showoverlay();
	var phone = $("#keyword").val();
	var partten = /^1[3,4,5,8]\d{9}$/g || /^15[8,9]\d{8}$/g;
	var partten2 = /^(-?\d+[.]?\d+)$/g;
	if (phone == "") {
		removeoverlay();
		showAlert("请输入手机号码！","failed");
	} else if (phone.length != 11 && partten2.test(phone)) {
		removeoverlay();
		showAlert("号码位数不正确！","failed");
	} else {
		if (partten.test(phone)) {
			var actionUrl = '${ctx}/ci/ciIndexAction!checkPeoplePhone.ai2do';
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: "keyWord=" + phone,
				success : function(result) {
					if(result.success){
						if(result.isBeijing == 'beijing') {//只有北京展示营销活动
							var actionUrl1 = '${ctx}/ci/ciIndexAction!queryPeopleCampaign.ai2do?keyWord='
								+ phone;
							$(".rwb_right").load(actionUrl1);
						} else {
							$(".rwb_left").css("margin-right", "5px"); 
							$(".rwb_right").hide();
						}
						
						var actionUrl2 = '${ctx}/ci/ciIndexAction!queryPeopleBase.ai2do?keyWord='
							+ phone;
						$("#base").load(actionUrl2);
						$("#base").show();
						$("#content").remove();
						removeoverlay();
					}else{
						removeoverlay();
						showAlert(result.msg,"failed");
					}
				}
			});
		} else {
			removeoverlay();
			showAlert("输入格式错误！","failed");
		}
	}
}
function ie6_noscroll(){
	$(window).scrollTop(0)
}
//点击探索按钮时候显示遮罩
function showoverlay(){
	if('undefined' == typeof(document.body.style.maxHeight)){
		$(window).bind("scroll",ie6_noscroll)
	}else{
		$("body,html").css("overflow","hidden");
	}
	var obj=$(window);
	var w=obj.width();
	var h=obj.height();
	var _left=obj.scrollLeft();
	var _top=obj.scrollTop();
	var html=$('<div class="overlay"><div class="loadding"></div></div>');
	html.css({"left":_left,"top":_top,"width":w,"height":h});
	$("body").append(html);
}
function removeoverlay(){
	$("body").find("> .overlay").remove();
	if('undefined' == typeof(document.body.style.maxHeight)){
		$(window).unbind("scroll",ie6_noscroll)
	}else{
		$("body,html").css("overflow","auto");
	}
}
</script>
</head>
<body class="body_bg">
<div class="sync_height" style="background: #f2f2f3">
	<jsp:include page="/aibi_ci/navigation.jsp?index=1"></jsp:include>
	<div class="mainCT" style="background: #f2f2f3">
		<jsp:include page="/aibi_ci/index_top.jsp?index=1"></jsp:include>
		<div id="content">
			<jsp:include page="/aibi_ci/peopleBase/analyseWelcom.jsp"></jsp:include>
		</div>
		<div class="rwb_right"></div><!--rwb_right end-->
		<div class="rwb_left"><div id="base"></div><div id="life"></div></div><!--rwb_left end -->
	</div>
</div>
</body>
</html>