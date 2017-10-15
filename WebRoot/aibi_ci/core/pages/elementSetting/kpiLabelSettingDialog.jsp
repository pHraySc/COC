<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<style>
.unitColor{padding:0px 5px;color:#ff0000;}
</style>
<script type="text/javascript">
var unit = decodeURIComponent('${param["ciLabelRule.unit"]}');
$(function(){
	var queryWay = '${param["ciLabelRule.queryWay"]}';
	var contiueMinVal = '${param["ciLabelRule.contiueMinVal"]}';
	var contiueMaxVal = '${param["ciLabelRule.contiueMaxVal"]}';
	var leftClosed = '${param["ciLabelRule.leftZoneSign"]}';
	var rightClosed = '${param["ciLabelRule.rightZoneSign"]}';
	var exactValue = '${param["ciLabelRule.exactValue"]}'; 
	
	$(".unitColor").html(unit);
	// var isSaveModel = '${param["isSaveModel"]}';
	  		if(queryWay == "" || queryWay=="undefined"){
		queryWay="1";
	}
	if(queryWay){
		$(".queryMethod").each(function(){
			if($(this).attr("value") == queryWay){
				$(this).addClass("current");
			}else{
				$(this).removeClass("current");
			}
		});
		if(queryWay == "1"){
			$("#exactValue").attr("disabled","disabled");
			$("#contiueMinVal").removeAttr("disabled");
			$("#contiueMaxVal").removeAttr("disabled");
			$("#leftClosed").removeAttr("disabled");
			$("#rightClosed").removeAttr("disabled");
		}else{
			$("#contiueMinVal").attr("disabled","disabled");
			$("#contiueMaxVal").attr("disabled","disabled");
			$("#leftClosed").attr("disabled","disabled");
			$("#rightClosed").attr("disabled","disabled");
			$("#exactValue").removeAttr("disabled");
		}
	}
	//上线和下线  区间
	if(contiueMinVal){
		$("#contiueMinVal").val(contiueMinVal);
	}
	if(contiueMaxVal){
		$("#contiueMaxVal").val(contiueMaxVal);
	}
	if(leftClosed){
		if(leftClosed.indexOf("=")>=0){
			$("#leftClosed").attr("checked" ,true);
		}
	}
	if(rightClosed){
		if(rightClosed.indexOf("=")>=0){
			$("#rightClosed").attr("checked" ,true);
		}
	} 
	if(exactValue){
		$("#exactValue").val(exactValue);
	}
	//数值查询选项
	$(".queryMethod").click(function(){
		$(".queryMethod").removeClass("current");
		$(this).addClass("current");
		$(".queryMethod").parent().siblings("dd").find("input").attr("disabled","disabled");
		$(".current").parent().siblings("dd").find("input").removeAttr("disabled");
		$("#cnameTip").hide();
		$("#snameTip").hide();
	});
	$(".queryMethod").hover(function(){
		$(this).addClass("hover");
	},function(){
		$(this).removeClass("hover");
	});
	var cmin= $.trim($("#contiueMinVal").val());
	if(cmin == "输入下限"){
		$.focusblur("#contiueMinVal");
	}
	var cmax= $.trim($("#contiueMaxVal").val());
	if(cmax == "输入上限"){
		$.focusblur("#contiueMaxVal");
	}		 
});
//TODO
jQuery.focusblur = function(focusid){
	var focusblurid = $(focusid);
	var defval = focusblurid.val();
	focusblurid.focus(function(){
		var thisval = $(this).val();
		if(thisval==defval){
			$(this).val("");
		}
	});
	focusblurid.blur(function(){
		var thisval = $(this).val();
		if(thisval==""){
			$(this).val(defval);
		}
	});
};
//表单验证
function validateForm(){
	var queryWay="";
	$(".queryMethod").each(function(){
		if($(this).hasClass("current")){
			queryWay= $(this).attr("value")
		}
	});
	if(queryWay == "1"){
		var contiueMinVal=$.trim($("#contiueMinVal").val());
		var contiueMaxVal=$.trim($("#contiueMaxVal").val());
		if(contiueMinVal == "输入下限"){
			contiueMinVal="";
		}
		if(contiueMaxVal == "输入上限") {
			contiueMaxVal="";
		}
		var contiueVal=contiueMinVal+contiueMaxVal;
		if($.trim(contiueVal) == ""){
			$("#cnameTip").empty();
			$("#cnameTip").show().append("请输入数值！");
			return false;
		}
		//var reg = /^(-?([1-9]\d*)|0)(\.\d+)?$/;
		var reg = /^(-?([1-9]\d{0,11})|0)(\.\d{1,4})?$/
		if(contiueMinVal !=""){
			if(!contiueMinVal.match(reg)){
				$("#cnameTip").empty();
				$("#cnameTip").show().append("请输入合法的数值！");
				return false;
			}
		}
		if(contiueMaxVal !=""){
			if(!contiueMaxVal.match(reg)){
				$("#cnameTip").empty();
				$("#cnameTip").show().append("请输入合法的数值！");
				return false;
			}
		}
		if(contiueMinVal !="" && contiueMaxVal !=""){
			var maxVal=parseInt($("#contiueMaxVal").val());
			var minVal=parseInt($("#contiueMinVal").val());
			if(maxVal < minVal){
				$("#cnameTip").empty();
				$("#cnameTip").show().append("数值范围上限不能小于下限！");
				return false;
			}
		}
	}else {
		var exactValue = $.trim($("#exactValue").val());
		if(exactValue == ""){
			$("#snameTip").empty();
			$("#snameTip").show().append("精确值不能为空！");
			return false;
		}
		//var reg = /^(-?([1-9]\d*)|0)(\.\d+)?(\,(-?([1-9]\d*)|0)(\.\d+)?)*$/;
		var reg = /^(-?([1-9]\d{0,11})|0)(\.\d{1,4})?(\,(-?([1-9]\d{0,11})|0)(\.\d{1,4})?)*$/;
		if(exactValue !=""){
			if(!exactValue.match(reg)){
				$("#snameTip").empty();
				$("#snameTip").show().append("请输入合法的数值！");
				return false;
			}
		}
	}
	
	return true;
}

function setNumberValue(){
	if(validateForm()){
		var queryWay="";
		var contiueMinVal = "";
		var contiueMaxVal = "";
		var exactValue = "";
		var leftZoneSign="";
		var rightZoneSign="";
		//var isSaveModel = "";
		var temp  = "";
		$(".queryMethod").each(function(){
			if($(this).hasClass("current")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			contiueMinVal=$("#contiueMinVal").val();
			contiueMaxVal=$("#contiueMaxVal").val();
			if(contiueMinVal == "输入下限"){
				contiueMinVal="";
			}
			if(contiueMaxVal == "输入上限") {
				contiueMaxVal="";
			}
			var leftChecked=$("#leftClosed").attr("checked");
			if(leftChecked){
				leftZoneSign=">=";
				if(contiueMinVal !=""){
					temp  +="大于等于" +contiueMinVal ;
				}
			}else{
				leftZoneSign=">";
				if(contiueMinVal !=""){
					temp +="大于" +contiueMinVal;
				}
			}
			var rightChecked=$("#rightClosed").attr("checked");
			if(rightChecked){
				rightZoneSign="<=";
				if(contiueMaxVal !=""){
					temp +="小于等于" +contiueMaxVal ;
				}
			}else{
				rightZoneSign="<";
				if(contiueMaxVal !=""){
					temp +="小于" +contiueMaxVal;
				}
			}
		}
		if(queryWay == "2"){
			exactValue=$("#exactValue").val();
			temp += exactValue;
		}
		
		var source = $("#source").val();
		var target = null;
		if (source == 1) {
			target = parent.$("._idClass");
		} else if (source == 2) {
			target = parent.$("._idClass").parent().parent();
		}
		
		target.attr("contiueMinVal",contiueMinVal);
		target.attr("contiueMaxVal",contiueMaxVal);
		target.attr("leftZoneSign",leftZoneSign);
		target.attr("rightZoneSign",rightZoneSign);
		target.attr("exactValue",exactValue);
		//target.attr("isSaveModel",isSaveModel);
		target.attr("queryWay",queryWay);
		
		if (source == 2) {
			var $conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
			if($.trim(unit) != "" && $.trim(unit) != null){
				temp += "("+unit+")";
			}
			var p = $("<p><label>数值范围：</label><span>"+temp+ "</span></p>");
			var title_temp ="数值范围：" + temp;
			$conditionMore.html('').append(p);
			$conditionMore.attr("title",title_temp);
		} else if (source == 1) {
			var sort = target.attr('sort');
			parent.coreMain.submitShopCartSession(sort);//提交session
		}
		target.removeClass("_idClass");
		parent.ShoppingCart.iframeCloseDialog("#kpiLabelSetting");
	}
}	
</script>
</head>
<body>	 
	<div class="number-value-setting-box">
		<dl>
			<dt>
				<div class="queryMethod" value="1">数值范围</div>
			</dt>
		 	<dd>
		    	<ol class="fleft numberBoxUl">
					<li>
						<input type="text" class="fleft inputFmt100" id="contiueMinVal"  value="输入下限" onkeyup="$('#cnameTip').hide();"/>
						<span class="fleft unitColor"></span>
						<p class="fright slectionBox">
							<input type="checkbox"  id="leftClosed" value="1" class="fleft" />
							<label for=fleft class="fleft">包含当前值</label>
		                </p>
					</li>
					<li><div id="cnameTip" class="tishi error" style="display:none;"></div></li>
		            <li>
						<input type="text" class="fleft inputFmt100" id="contiueMaxVal" value="输入上限" onkeyup="$('#cnameTip').hide();"/>
						<span class="fleft unitColor"></span>
						<p class="fright slectionBox">
							<input type="checkbox"   class="fleft" id="rightClosed"  value="1" />
							<label for="fleft" class="fleft">包含当前值</label>
						</p>
					</li>
				</ol>
			</dd>
		</dl>
		<dl class="moddel">
			<dt>
				<div class="queryMethod" value="2">精确查询</div>
			</dt>
			<dd>
				<div>
					<input type="text" class="exactQueryInput fleft"  id="exactValue" value="" onkeyup="$('#snameTip').hide();"/>
					<span class="fleft unitColor"></span>
					<div id="snameTip" class="tishi error" style="display:none;"></div>
				</div>
				<div class="fleft useTip">多条件用","分割且不能用","结尾</div>
			</dd>
		</dl>
	</div>
	<!-- 按钮操作-->
	<div class="btnActiveBox">
		<input type="button" value="确定" class="ensureBtn"  id="numberValueSetEnsureBtn" onclick="setNumberValue();"/>
	</div>
	<input type="hidden" id="source" name="source" value="${source}"/>
</body>
</html>