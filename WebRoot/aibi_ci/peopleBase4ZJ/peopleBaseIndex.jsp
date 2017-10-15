<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人为本</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
	$(function(){
    	$('#keyWordInput').focus(function() {
    		var val = $(this).val();
    		if(val == '请输入关键字') {
    			$(this).val('');
    		}
    	});
    	$('#keyWordInput').focusout(function() {
    		var val = $(this).val();
    		if(val == '') {
    			$(this).val('请输入关键字');
    		}
    	});
    	$('#keyWordInput').keyup(function(e) {
    		if(e.keyCode == 13) {
    			$('#searcheSubmitBtn').click();
    		}
    	});
    	
    	$('#searcheSubmitBtn').click(function() {
    		
    		var val = $('#keyWordInput').val();
    		if(val == '' || val == '请输入关键字') return;
    		var partten = /^1[3,4,5,8]\d{9}$/g || /^15[8,9]\d{8}$/g;
			var partten2 = /^(-?\d+[.]?\d+)$/g;
			
			if (val.length != 11 && partten2.test(val)) {
				showAlert("号码位数不正确！","failed");
				return;
			} else {
				if (!partten.test(val)) {
					showAlert("输入格式错误！","failed");
					return;
				} else {
					showWaitBox();
					$.post('${ctx}/ci/peopleBaseAction!findPhoneNum.ai2do', {phoneNum:val}, function(response) {
						if(response.count > 0) {
							$.post('${ctx}/ci/peopleBaseAction!queryProductNoTag.ai2do', {phoneNum:val}, function(data) {
								$('#welcomeDiv').hide();
								$('#tagListDiv').html(data).show();
								hideWaitBox();
							})
						} else {
							showAlert("输入手机号不存在！","failed");
							return;
						}
					});
				}
			}
    	});
    	
    	//查询显示遮罩
    	function showWaitBox() {
    		$("#waitLoading").css({width:$(document).width()+"px",height:$(document).height()+"px"}).show();
    		$("#waitBox").show();
    	}
    	function hideWaitBox(){
    		$("#waitLoading").css({width:$(document).width()+"px",height:$(document).height()+"px"}).hide();
    		$("#waitBox").hide();
    	}
	});
	//点击logo返回到首页
	function showHomePage(){
		var url = "${ctx}/ci/ciIndexAction!labelIndex.ai2do?seachTypeId=4";
		window.open(url, "_self", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	
</script>

</head>
<body>
	<!--登录信息-->
	<jsp:include page="/aibi_ci/navigation.jsp" />
    <!--登录信息-->
    
    <!--首页搜索区域 -->
    <div class="peoleBasicSearchBox">
		<div class="peoleBasicLogo" onclick="showHomePage();return false;"></div>
        <div class="pb_searchBox">
			<input id="keyWordInput" type="text" class="fleft" value="请输入关键字" />
			<button id="searcheSubmitBtn" class="fleft">搜索 </button>
		</div>
    </div>
	<!-- 首页搜索区域 -->
    
	<!-- 人为本区域开始 -->
	<div id="welcomeDiv" class="comWidth peopleBasicWelBox"  >
		<div class="peopleBasicWelTitle"><h5>号码查询</h5><p>你需要输入号码查询相关内容</p></div>
	</div>
	
	<div id="tagListDiv" class="comWidth peopleBasicBox hidden">
	</div>
	
	<div class="waitBox" id="waitBox">    正在搜索，请耐心等候... </div>
</body>
</html>
