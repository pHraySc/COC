<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryUploadify/jquery.uploadify.js"></script>
	<script type="text/javascript">
	$(function(){
		var darkValue =  decodeURIComponent('${param["darkValue"]}');
		var queryWay = '${param["queryWay"]}';
		var exactValue = decodeURIComponent('${param["exactValue"]}');
		//导入按钮事件
		$("#file_upload_dark").uploadify({
			buttonClass	: "importBtn",
			btnBoxClass : "fright tMargin5",
			buttonText	: "导入",
			width		: 60,
			height		: 24,
			fileObjName	: "file",
			auto		: true,//为true当选择文件后就直接上传了，为false需要点击上传按钮才上传
			multi		: true,
			progressData: "speed",
			itemTemplate: true,
			fileTypeExts: "*.txt;*.csv",
			fileTypeDesc: "导入文件格式只能是txt或者csv格式！",
	        swf			: "${ctx}/aibi_ci/assets/js/jqueryUploadify/uploadify.swf",
	        uploader	: "${ctx}/ci/ciIndexAction!findLikeLabelByImport.ai2do",
	        onUploadSuccess	: function(file, data, response) {
	        	var success = $.parseJSON(data).success;
	        	if(!success) {
	        		showAlert($.parseJSON(data).msg, 'failed');
	        		return;
	        	}
	        	var dataObj = $.parseJSON(data).data;
	        	var dataResult = "";
	        	/*if(dataObj.length>$._maxDarkLabelExactValue){
	        		var _index = dataObj.lastIndexOf(",",$._maxDarkLabelExactValue);
	        		dataResult = dataObj.substring(0,_index);
	        		$("#newTip").html("导入文本过长，已被截取");
	        		$("#newTip").show();
		        }else{*/
		        	dataResult = dataObj;
		        	$("#newTip").empty();
		        //}
		        $("#exactValue").val(dataResult);
		        $("#exactValue").attr("title",dataResult);
	        },
	        onSWFReady:onSWFReady
	    });

		$(".dimMatchMethod").click(function(){
			$(".dimMatchMethod").removeClass("current");
			$(this).addClass("current");
			var queryWayTemp = $(this).attr("value");
			$(".dimMatchMethod").parent().siblings("dd").find("input").attr("disabled","disabled");
			$(".current").parent().siblings("dd").find("input").removeAttr("disabled");
			$("#snameTip").hide();
			$("#enameTip").hide();
			$("#newTip").hide();
			if(queryWayTemp == "1"){
				$("#file_upload_dark").uploadify('disable', true);
				$("#importFontStyleBox").html("输入您想查询的任意内容");
			}else{
				$("#file_upload_dark").uploadify('disable', false);
				$("#importFontStyleBox").html("多条件用','分割,且不能用','结尾");
			}
		});
		$(".dimMatchMethod").hover(function(){
			$(this).addClass("hover");
		},function(){
			$(this).removeClass("hover");
		});
		
		function onSWFReady(){
		       // debugger
	     	if(!queryWay){
	 			queryWay="1";
	 		}
	 		if(queryWay){
	 			$(".dimMatchMethod").each(function(){
	 				if($(this).attr("value") == queryWay){
	 					$(this).addClass("current");
	 				}else{
	 					$(this).removeClass("current");
	 				}
	 			});
	 			if(queryWay == "1"){
	 				$("#exactValue").attr("disabled","disabled");
	 				$("#darkValue").removeAttr("disabled");
	 				$("#importFontStyleBox").html("输入您想查询的任意内容");
	 				$("#file_upload_dark").uploadify('disable', true);
	 				if(darkValue){
	 					$("#darkValue").val(darkValue);
	 				}
	 			}else{
	 				$("#darkValue").attr("disabled","disabled");
	 				$("#exactValue").removeAttr("disabled");
	 				$("#importFontStyleBox").html("多条件用','分割,且不能用','结尾");
	 				$("#file_upload_dark").uploadify('disable', false);
	 				
	 				if(exactValue){
	 					$("#exactValue").val(exactValue);
	 				}
	 			}
	 		}
		 }	
	});
	  
	//表单验证
	function validateForm(){
		var queryWay="";
		$(".dimMatchMethod").each(function(){
			if($(this).hasClass("current")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			var darkValue = $.trim($("#darkValue").val());
			if(darkValue == ""){
				$("#snameTip").empty();
				$("#snameTip").show().append("模糊值不能为空！");
				return false;
			}
			
		}else {
			var exactValue = $.trim($("#exactValue").val());
			if(exactValue == ""){
				$("#enameTip").empty();
				$("#enameTip").show().append("精确值不能为空！");
				return false;
			}
			/*if(exactValue.length>$._maxDarkLabelExactValue){
				$("#enameTip").empty();
				$("#enameTip").show().append("精确值长度过长，请重新输入！");
				return false;
			}*/
			if(exactValue.indexOf(",") == 0 || exactValue.lastIndexOf(",") == exactValue.length-1 || exactValue.indexOf(",,") !=-1){
				$("#enameTip").empty();
				$("#enameTip").show().append("请输入合法的值！");
				return false;
			}
			/*var reg = /^([\w\u4e00-\u9fa5]+)?(\,([\w\u4e00-\u9fa5]+))*$/;
			if(!exactValue.match(reg)){
				$("#enameTip").empty();
				$("#enameTip").show().append("请输入合法的值！");
				return false;
			}*/
		}
		return true;
	}
	
	function setDarkValue(){
		if(validateForm()){
			var darkValue = "";
			var exactValue = "";
			var queryWay="";

			$(".dimMatchMethod").each(function(){
				if($(this).hasClass("current")){
					queryWay= $(this).attr("value")
				}
			});
			document.getElementById('uploadify_div');
			var temp  = "";
			var title_temp ="";
			var p ="";
			if(queryWay == "1"){
				darkValue=$("#darkValue").val();
				temp +=  darkValue;
				title_temp += "模糊值：" + temp;
				p = $("<p><label>模糊值：</label><span>"+temp+ "</span></p>");
			}else{
				exactValue=$("#exactValue").val();
				temp +=  exactValue;
				title_temp += "精确值：" + temp;
				p = $("<p><label>精确值：</label><span>"+temp+ "</span></p>");
			}
			parent.$("._idClass").parent().parent().attr("darkValue",darkValue);
			parent.$("._idClass").parent().parent().attr("exactValue",exactValue);
			parent.$("._idClass").parent().parent().attr("queryWay",queryWay);
			
			var $conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
			$conditionMore.html('').append(p);
			$conditionMore.attr("title",title_temp);
			parent.$("._idClass").removeClass("_idClass");
			parent.submitRules();
			parent.iframeCloseDialog("#darkValueSet");
		}
	}
	
	</script>
</head>
<body>
	<div class="dim-match-box clearfix">
		<div class="fleft  dim-match-left-box minWidth"></div>
		<dl>
			<dt>
				<div class="dimMatchMethod current" value="1">模糊值</div>
			</dt>
			<dd class="dim-match-dd">
				<div class="fright dim-match-right-box">
					<!--<h6> 请输入模糊匹配内容：</h6>
					--><input type="text"  class="dimMatchInput"   id="darkValue" onkeyup="$('#snameTip').hide();"/>
					<div id="snameTip" class="tishi error" style="display:none;"></div> 
				</div>
			</dd>
		</dl>
		<dl>
			<dt>
				<div class="dimMatchMethod"  value="2">精确值</div>
			</dt>
			<dd  class="dim-match-dd">
				<div class="fright dim-match-right-box">
					<input type="text"  class="dimMatchInput"   id="exactValue" onkeyup="$('#enameTip').hide();$('#newTip').hide();" onkeydown="if(event.keyCode==32) return false"/>
					<div class="itemChooseTopBox uploadify_div" id="uploadify_div">
		       			<input type="file" name="file_upload_dark" id="file_upload_dark" />
		  		 	</div>
					<div id="enameTip" class="tishi error" style="display:none;"></div><!-- 非空验证提示 -->
				</div>
				<div class="fright newTip newTip-dim-match" id="newTip"></div>
			</dd>
		</dl>
    </div>
	<div class="importFontStyleBox" id="importFontStyleBox"></div>
    <!-- 按钮操作-->
    <div class="btnActiveBox">
		<input type="button" value="确定" class="ensureBtn" id="darkValueSetEnsureBtn" onclick="setDarkValue();"/>
    </div>
</body>
</html>