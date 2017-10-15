<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" class="processBodyTag">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
<title>客户标签视图</title>
<link href="${ctx}/aibi_ci/assets/themes/common.css" type="text/css" rel="stylesheet"></link>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/process.css" type="text/css" rel="stylesheet"></link>
<style >.processStep04{display:block;}</style>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.cookie.js"></script>
<script type="text/javascript">
var STEP_CONSTANT ={
	 "4":{"bg":"processBodyTag","tree":{"id01":"#processStep04"}},
	 "5":{"bg":"processBodyTag","tree":{"id01":"#processStep05"}},
	 "6":{"bg":"processBodyTag","tree":{"id01":"#processStep06"}},
	 "7":{"bg":"processBodyTag","tree":{"id01":"#processStep071","id02":"#processStep072"}},
	 "8":{"bg":"processBodyTag","tree":{"id01":"#processStep081","id02":"#processStep082"}}
}
//下一步操作
function nextStep(index){
    $(".process").hide();
	var contant = STEP_CONSTANT[index]["tree"];
	$("body").removeClass().addClass(STEP_CONSTANT[index]["bg"]);
	for(var key in contant){
	     $(contant[key]).show();
	}
    $.cookie("tagProcessFlag","true");
}
//跳过引导
function skipGuide(){
    nextStep(4);
    //1.获取cookie中值或者session中的值
    $.cookie("tagProcessFlag","true");
	//关闭演示操作流程介绍
    top.closeProcessIntro();
}
$(window).load(function(){
    changeBodyHeightWidth()
})
function changeBodyHeightWidth(){
    var iframeHeight =$(parent.document.getElementById("processIframe")).height(); //父级iframe 高度
	var windowClientHeight = parent.document.documentElement.clientHeight;//浏览器可视高度
    $("#shadeLayout").height(iframeHeight);
    $("#processBox").height(windowClientHeight);
}
//关闭  20* 25   下一步 80*28    点 20*15 
</script>
</head>
<body class="processBodyTag">
   <!-- 背景遮罩 -->
   <div class="shadeLayout" id="shadeLayout"> </div>
   <div class="processBox"  id="processBox" >
   <!--************ 操作第八步开始*****************-->
    <div class="process processStep081  " id="processStep081">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/process-step-081.png"  width="580" height="215"  usemap="#stepmap08" /> 
	</div>
	<div class="process processStep082  " id="processStep082">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-082.png"  width="1012" height="46" /> 
	</div>
	<map name="stepmap08" id="stepmap08">
	  <!-- 下一步 -->
	  <area shape="rect" coords="370,110,472,138"  href ="javascript:skipGuide();" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="463,20,483,45" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  
	  <area shape="rect" coords="169,115,189,130" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="193,115,213,130" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="216,115,236,130" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="241,115,261,130" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="267,115,287,130" href ="javascript:void(0);"  />
	</map>


    <!--************ 操作第七步开始*****************-->
    <div class="process processStep071  " id="processStep071">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/process-step-071.png"  width="640" height="231"  usemap="#stepmap07" /> 
	</div>
	<div class="process processStep072  " id="processStep072">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/process-step-072.png"  width="1012" height="46" /> 
	</div>
	<map name="stepmap07" id="stepmap07">
	  <!-- 下一步 -->
	  <area shape="rect" coords="455,120,535,148"  href ="javascript:nextStep(8);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="523,20,543,45" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="220,125,240,140" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="244,125,264,140" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="268,125,288,140" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="292,125,312,140" href ="javascript:void(0);"  />
	  <area shape="rect" coords="317,125,337,140" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第六步开始*****************-->
    <div class="process processStep06  " id="processStep06">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/process-step-061.png"  width="622" height="225"  usemap="#stepmap06" /> 
	</div>
	<map name="stepmap06" id="stepmap06">
	  <!-- 下一步 -->
	  <area shape="rect" coords="528,185,608,213"  href ="javascript:nextStep(7);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="597,133,617,158" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="312,190,332,205" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="337,190,357,205" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="362,190,382,205" href ="javascript:void(0);"  />
	  <area shape="rect" coords="386,190,406,205" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="410,190,430,205" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第五步开始*****************-->
    <div class="process processStep05  " id="processStep05">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/process-step-051.png"  width="580" height="220"  usemap="#stepmap05" /> 
	</div>
	<map name="stepmap05" id="stepmap05">
	  <!-- 下一步 -->
	  <area shape="rect" coords="485,177,565,205"  href ="javascript:nextStep(6);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="555,90,575,115" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="299,183,319,198" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="324,183,344,198" href ="javascript:void(0);"  />
	  <area shape="rect" coords="349,183,369,198" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="374,183,394,198" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="397,183,417,198" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第四步开始*****************-->
    <div class="process processStep04  " id="processStep04">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/process-step-041.png"  width="672" height="210"  usemap="#stepmap04" /> 
	</div>
	<map name="stepmap04" id="stepmap04">
	  <!-- 下一步 -->
	  <area shape="rect" coords="578,170,658,195"  href ="javascript:nextStep(5);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="648,85,668,110" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="403,175,423,190" href ="javascript:void(0);"  />
	  <area shape="rect" coords="427,175,447,190" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="451,175,471,190" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="475,175,495,190" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="500,175,520,190" href ="javascript:nextStep(8);"  />
	</map>
  </div>
</body>
</html>
