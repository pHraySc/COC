<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
<title>客户标签视图</title>
<link href="${ctx}/aibi_ci/assets/themes/common.css" type="text/css" rel="stylesheet"></link>
<link href="${ctx}/aibi_ci/assets/themes/zhejiang/process.css" type="text/css" rel="stylesheet"></link>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.cookie.js"></script>
<script type="text/javascript">
var STEP_CONSTANT ={
     "1":{"bg":"","tree":{"id01":"#processStep01"}},
     "2":{"bg":"","tree":{"id01":"#processStep021","id02":"#processStep022"}},
	 "3":{"bg":"","tree":{"id01":"#processStep031","id02":"#processStep032"}},
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
    $.cookie("processFlag","true");
}
//跳过引导
function skipGuide(){
    nextStep(1);
    //1.获取cookie中值或者session中的值
    $.cookie("processFlag","true");
	//关闭演示操作流程介绍
    parent.closeProcessIntro();
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
<body>
   <!-- 背景遮罩 -->
   <div class="shadeLayout" id="shadeLayout" > </div>
   <div class="processBox"  id="processBox" >
   <!--************ 操作第八步开始*****************-->
    <div class="process processStep081  " id="processStep081">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-081.png"  width="580" height="215"  usemap="#stepmap08" /> 
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
	  <area shape="rect" coords="135,115,155,130" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="160,115,180,130" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="185,115,205,130" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="209,115,229,130" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="233,115,253,130" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="256,115,276,130" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="281,115,301,130" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="307,115,327,130" href ="javascript:void(0);"  />
	</map>


    <!--************ 操作第七步开始*****************-->
    <div class="process processStep071  " id="processStep071">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-071.png"  width="640" height="231"  usemap="#stepmap07" /> 
	</div>
	<div class="process processStep072  " id="processStep072">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-072.png"  width="1012" height="46" /> 
	</div>
	<map name="stepmap07" id="stepmap07">
	  <!-- 下一步 -->
	  <area shape="rect" coords="455,120,535,148"  href ="javascript:nextStep(8);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="523,20,543,45" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="175,126,195,141" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="200,126,220,141" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="225,126,245,141" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="248,126,268,141" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="273,126,293,141" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="297,126,317,141" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="321,126,341,141" href ="javascript:void(0);"  />
	  <area shape="rect" coords="346,126,366,141" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第六步开始*****************-->
    <div class="process processStep06  " id="processStep06">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-061.png"  width="622" height="225"  usemap="#stepmap06" /> 
	</div>
	<map name="stepmap06" id="stepmap06">
	  <!-- 下一步 -->
	  <area shape="rect" coords="528,185,608,213"  href ="javascript:nextStep(7);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="597,133,617,158" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="288,191,308,206" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="313,191,333,206" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="338,191,358,206" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="362,191,382,206" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="387,191,407,206" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="412,191,432,206" href ="javascript:void(0);"  />
	  <area shape="rect" coords="436,191,456,206" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="460,191,480,206" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第五步开始*****************-->
    <div class="process processStep05  " id="processStep05">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-051.png"  width="580" height="220"  usemap="#stepmap05" /> 
	</div>
	<map name="stepmap05" id="stepmap05">
	  <!-- 下一步 -->
	  <area shape="rect" coords="485,177,565,205"  href ="javascript:nextStep(6);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="555,90,575,115" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="246,182,266,197" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="271,182,291,197" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="296,182,316,197" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="319,182,339,197" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="344,182,364,197" href ="javascript:void(0);"  />
	  <area shape="rect" coords="369,182,389,197" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="394,182,414,197" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="417,182,437,197" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第四步开始*****************-->
    <div class="process processStep04  " id="processStep04">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-041.png"  width="672" height="210"  usemap="#stepmap04" /> 
	</div>
	<map name="stepmap04" id="stepmap04">
	  <!-- 下一步 -->
	  <area shape="rect" coords="710,170,790,195"  href ="javascript:nextStep(5);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="648,85,668,110" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="339,175,359,190" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="364,175,384,190" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="389,175,409,190" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="413,175,433,190" href ="javascript:void(0);"  />
	  <area shape="rect" coords="437,175,457,190" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="461,175,481,190" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="485,175,505,190" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="510,175,530,190" href ="javascript:nextStep(8);"  />
	</map>
   <!--************ 操作第三步开始*****************-->
	<div class="process processStep031" id="processStep031">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-031.png"  width="500" height="220"  usemap="#stepmap03"/> 
	</div>
	<div class="process processStep032 " id="processStep032">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-032.png"  width="215" height="245"   /> 
	</div>
	<map name="stepmap03" id="stepmap03">
	  <!-- 下一步 -->
	  <area shape="rect" coords="403,110,483,138"  href ="javascript:nextStep(4);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="473,24,493,49" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="165,115,185,130" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="190,115,210,130" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="215,115,235,130" href ="javascript:void(0);"  />
	  <area shape="rect" coords="239,115,259,130" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="263,115,283,130" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="287,115,307,130" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="312,115,332,130" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="335,115,355,130" href ="javascript:nextStep(8);"  />
	</map>
    <!--************ 操作第二步开始*****************-->
	<div class="process processStep021" id="processStep021">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-021.png"  width="636" height="138"  /> 
	</div>
	<div class="process processStep022 " id="processStep022">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-022.png"  width="800" height="196"  usemap="#stepmap02" /> 
	</div>
	<map name="stepmap02" id="stepmap02">
	  <!-- 下一步 -->
	  <area shape="rect" coords="424,70,505,98"  href ="javascript:nextStep(3);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="495,24,515,49" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="125,75,145,90" href ="javascript:nextStep(1);"  />
	  <area shape="rect" coords="150,75,170,90" href ="javascript:void(0);"  />
	  <area shape="rect" coords="175,75,195,90" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="199,75,219,90" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="224,75,244,90" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="248,75,268,90" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="272,75,292,90" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="296,75,316,90" href ="javascript:nextStep(8);"  />
	</map>
	<!--************ 操作第一步开始*****************-->
    <div class="process processStep01  " id="processStep01">
	   <img src="${ctx}/aibi_ci/assets/themes/zhejiang/images/process/tag-process-step-01.png"  width="660" height="245"  usemap="#stepmap01" /> 
	</div>
	<map name="stepmap01" id="stepmap01">
	  <!-- 下一步 -->
	  <area shape="rect" coords="503,200,585,235"  href ="javascript:nextStep(2);" />
	  <!-- 关闭跳过 -->
	  <area shape="rect" coords="570,120,595,140" href ="javascript:skipGuide();"  />
	  <!-- 点操作步骤 -->
	  <area shape="rect" coords="265,211,285,226" href ="javascript:void(0);"  />
	  <area shape="rect" coords="289,211,309,226" href ="javascript:nextStep(2);"  />
	  <area shape="rect" coords="314,211,334,226" href ="javascript:nextStep(3);"  />
	  <area shape="rect" coords="338,211,358,226" href ="javascript:nextStep(4);"  />
	  <area shape="rect" coords="362,211,382,226" href ="javascript:nextStep(5);"  />
	  <area shape="rect" coords="385,211,405,226" href ="javascript:nextStep(6);"  />
	  <area shape="rect" coords="410,211,430,226" href ="javascript:nextStep(7);"  />
	  <area shape="rect" coords="435,211,455,226" href ="javascript:nextStep(8);"  />
	</map>
 </div>
</body>
</html>
