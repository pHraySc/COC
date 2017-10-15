<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入客户群</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<c:set var="scenePublicId" value=""/>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/dialog.table.js"></script>
<style>
.ui-tooltip {
	padding: 5px 5px;
	background:#f1f2f7; color:#333;
	border:1px solid #bbb;
	font-size:12px; font-weight:normal;
}
.ui-tooltip ul{list-style:none; padding:0; margin:0;}
.ui-tooltip ul li{height:16px; line-height:16px;}
select.sceneWidth {width:310px;}
.greenTip {margin-top:2px;}
.importCustermersTip li{text-align:left; text-indent:5px;}
</style>
<script type="text/javascript">
var importWay = 'fileImportBox';
$(function(){
	
	//表导入滚动条
	$("#tableImportBox").mCustomScrollbar({
		theme:"minimal-dark", //滚动条样式
		scrollbarPosition:"outside", //滚动条位置
		scrollInertia:500,
		mouseWheel:{
			preventDefault:true //阻止冒泡事件
		}
	});
	
	var scenePublicId="<%=ServiceConstants.ALL_SCENE_ID%>";
	//切换选择方式
	$("#switchImportWayBox div").click(function(){
	    $("#switchImportWayBox div").removeClass("current");
		$(this).addClass("current");
        $(".importWayBox").hide();
		var showDiv= $(this).attr("importWay");
		
		//切换重置form表单
		if(showDiv != importWay) {
			resetForm(showDiv);
		}
		
		importWay = showDiv;
		$("#"+showDiv).show();
		
	});
	//统一浏览器对FILE 文件获取上传路径的差异 
	$("#customGroupFile").change(function(){
		$("#customGroupFileText").val($(this).val());
	  	var file = $(this).val();
		var fileName = file.substr(file.lastIndexOf("."));   
		fileName = fileName.toUpperCase();
		if(fileName != ".TXT" && fileName != ".CSV"  && fileName != ".XLS"){
	    	$("#fnameTip").empty();
			$("#fnameTip").show().append("请选择合法格式文件");
		}else{
			$("#fnameTip").empty();
			$("#fnameTip").hide();
		}
	});
	
	//数据日期
	$("#createPeriodBox div").bind('click', changeDataDateType);
	$('#dataDateInput').val(commonUtil.dateFormat(new Date(), 'yyyy-MM-dd'));
	$('#dataDateInput').bind('click', changeDateSelectType('yyyy-MM-dd', '%y-%M-%d'));
	
	//生成周期
	$("#createPeriodBox4Import div").bind('click', changeCycle);
	
	//是否系统推荐时间
	$(".sysRecommendTime").click(function(){
		$(".sysRecommendTime").show();
	   	$(this).hide();
	   	if($(this).hasClass("isSysRecommendTime")){
			var createPeriod =$("#createPeriod4Import").val();
			if(createPeriod == '2'){
				$("#monthTimeDiv").show();
			}
			if(createPeriod == '3'){
				$("#dayTimeDiv").show();
			}
			$("#isSysRecommendTime").val("0");
		}else{
			$("#dayTimeDiv").hide();
			$("#monthTimeDiv").hide();
			$("#isSysRecommendTime").val("1");
		}
	});
});
 
function changeDataDateType() {
	$('#createPeriodBox div').removeClass();
	$(this).addClass('current');
	
	var dataType = $(this).attr('dataType');
	var dateFmt = 'yyyy-MM-dd';
	var maxDate = '%y-%M-%d';
	
	if(dataType == 2) {
		dateFmt = 'yyyy-MM';
		maxDate = '%y-%M';
	}
	changeDateSelectType(dateFmt, maxDate);
}
function changeDateSelectType(dateFmt, maxDate) {
	$('#dataDateInput').val(commonUtil.dateFormat(new Date(), dateFmt));
	$('#dataDateInput').unbind('click').bind('click', function() {
		WdatePicker({dateFmt : dateFmt, maxDate : maxDate})
	});
}

function resetForm(showDiv) {
	if(showDiv == 'fileImportBox') {
		$('#saveForm')[0].reset();
		$('#sceneIds1').val('');
		$('#scenesTxt').text('');
		$('#is_private').val('1');
		
		$(".isPrivateClass").children("input")[0].checked=false;
        $(".isPrivateClass").hide();
        $("#isPrivateClass_default").show();
        $("#isPrivateClass_default").find("input")[0].checked=true;
		
        $('.isHaveAttrTitle').children('input')[0].checked=false;
        $('.isHaveAttrTitle').hide();
        $('#isHaveAttrTitle_default').show();
        $('#isHaveAttrTitle_default').find('input')[0].checked=true;
        $('#isHaveAttrTitleInput').val('0');
        $('#attributeDiv dd input').attr('disabled', false);
        
        $($("#createPeriodBox div")[0]).click();
        
      	$('#attributeDiv dd a').unbind('click').bind('click', function(){delAttribute(this)});
      	$('#addAttributeBtn').removeClass('tag_btn_dis').unbind('click').bind('click', addAttribute);
        
		$('#selListBox').find('input[type="checkbox"]').each(function(index, item) {
			item.checked = false;	
		});
	} else if(showDiv == 'tableImportBox') {
		$('#importByTableForm')[0].reset();
		$('#sceneIds2').val('');
		$('#scenesTxt2').text('');
		$('#is_private').val('1');
		
		$(".isPrivateClass2").children("input")[0].checked=false;
        $(".isPrivateClass2").hide();
        $("#isPrivateClass_default2").show();
        $("#isPrivateClass_default2").find("input")[0].checked=true;
        
		$('#selListBox2').find('input[type="checkbox"]').each(function(index, item) {
			item.checked = false;	
		});
	}
}

//改变客户群生成周期
function changeCycle(){
	var oldValue = $("#createPeriodBox4Import").find(".current").attr("value");
	
    $("#createPeriodBox4Import div").removeClass();
	$(this).addClass("current");
	var value = $(this).attr("value");
	
	if (value == oldValue) {
		return;
	}
	
	$("#monthCycleTimeTip,#dayCycleTimeTip").hide();
	
	$("#createPeriod4Import").val(value);
	
	if (value == "1") {//一次性
		$("#sysRecommendTimeLi").hide();
		
		var isSysRecommendTime = $("#isSysRecommendTime").val();
		//否的情况下
		if(isSysRecommendTime == '0'){
			$("#sysRecommendTimeYes").show();
			$("#sysRecommendTimeNot").hide();
			$("#isSysRecommendTime").val("1");
			$("#monthTimeDiv").hide();
			$("#dayTimeDiv").hide();
		}
	} else if (value == "2") {//月
		$("#sysRecommendTimeLi").show();
	
		var isSysRecommendTime = $("#isSysRecommendTime").val();
		//否的情况下
		if(isSysRecommendTime == '0'){
			$("#sysRecommendTimeYes").show();
			$("#sysRecommendTimeNot").hide();
			$("#isSysRecommendTime").val("1");
			$("#monthTimeDiv").hide();
			$("#dayTimeDiv").hide();
		}
	} else if (value == "3") {//日
		$("#sysRecommendTimeLi").show();
		
		var isSysRecommendTime = $("#isSysRecommendTime").val();
		//否的情况下
		if(isSysRecommendTime == '0'){
			$("#sysRecommendTimeYes").show();
			$("#sysRecommendTimeNot").hide();
			$("#isSysRecommendTime").val("1");
			$("#monthTimeDiv").hide();
			$("#dayTimeDiv").hide();
		}
	}
}

function datePicked(){
 	var d = $("#monthCycleTime").val();
 	if(d != null && d!= ""){
 		$("#listCreateTime").val(d);
	}
}
</script>
</head>
<body>
<div id="importClientDialog">
	<div class="dialogHeaderTitle">
		<p class="dialogTitleText"> 导入客户群</p>
		<a href="javascript:void(0);"  class="dialogClose" ></a>
	</div>
	<div class="switchImportWayBox" id="switchImportWayBox">
		<ol>
			<li> 
				<div  id="selectFileImport" class="current" importWay="fileImportBox">文件方式导入 </div>
			</li>
			<%if(coc_province.equals("zhejiang")){ %>
			<!-- 浙江客户群表接口方式导入 -->
				<c:if test="${isAdmin}">
					<li> 
					    <div  id="selectTableImport" importWay="tableImportBox">表接口方式导入 </div>
					</li> 
				</c:if>
			<%}%>
		</ol>
	</div>
	<!-- 文件方式导入表单 -->
	<div id="fileImportBox"  class="importWayBox">
	<form id="saveForm" method="post" action="${ctx}/ci/customersFileAction!uploadCustomGroupFile.ai2do" enctype="multipart/form-data">
	    <ol class="clearfix">
		    <li> 
				<label class="fleft labFmt110 star">客户群名称：</label>
			  	<input type="text" name="ciCustomGroupInfo.customGroupName" id="customGroupName" class="fleft inputFmt305" value="" maxlength="35" />
			  	<!-- 提示 -->
			  	<div id="cnameTip" class="tishi error"  style="display:none;"></div><!-- 用于重名验证 -->
			</li> 
			<li> 
				<label  class="fleft labFmt110 star">客户群分类：</label>
			 	<div class="fleft selBox">
            	  	<div class="selTxt" id="scenesTxt" style="overflow:hidden;"></div>
            	  	<a href="javascript:void(0);" class="selBtn" id="selBtn"></a>
            	  	<div class="selListBox" id="selListBox">
	            	    <div class="firstItem" id="firstItem">
	            	  	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
								<c:if test="${dimScenes.sortNum == 0}">
									<input type="checkbox" value="${dimScenes.sceneId}"  id="firstItem_active" name="_sceneBoxItemCheckShare" _sceneName="${dimScenes.sceneName}"/>
									<label for="firstItem_active">${dimScenes.sceneName}</label>
								</c:if>
							</c:forEach>
	            	  	</div>
	            	  	<ol>
	            	  		<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
	            	  	 		<c:if test="${dimScenes.sortNum > 0}">
	             	  	 		<li>
		             	  	 	 	<a href="javascript:void(0);" >
		             	  	 	 		<input type="checkbox" id="selBoxItem0${st.index}" name="sceneBoxItemCheckList" value="${dimScenes.sceneId}" _sceneName="${dimScenes.sceneName}"/>
		             	  	 	 		${dimScenes.sceneName}<!--<label for="selBoxItem0${st.index}">${dimScenes.sceneName}</label>-->
		             	  	 	 	</a>
	             	  	 	 	</li>
	             	  	 	 	</c:if>
	            	  	 	</c:forEach>
	            	  	</ol>
	            	  	<div class="selAllBox selBox_all">
	            	  	 	 <input type="checkbox" id="selBox_all">全选</input>
						</div>
					</div>
           		</div>
				<!-- 提示 -->
			  	<div id="snameTip" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
			</li>
			<li>
			    <label class="fleft labFmt110 star">数据日期：</label>
				<div class="fleft createPeriodBox" id="createPeriodBox">
				   <input type="hidden" id="createPeriod" />
				   <div dataType="1" class="current">日</div> 
				   <div dataType="2">月</div>
				</div>
				<div>
					<input name="ciCustomGroupInfo.dataDate" type="text" 
						id="dataDateInput" readonly class="new_tag_input tag_date_input" />
				</div>
		  	</li>
			<li>
				<label class="fleft labFmt110">是否共享：</label>
				<div class="nullItemBox hidden isPrivateClass">
					<input type="radio" name="isPrivateTemp" class="hidden" value="0" />
					<p class="fleft nullItemText">是</p>
					<div class="fleft nullItemOption">&nbsp;</div>
				</div>
				<div id="isPrivateClass_default" class="nullItemBox isPrivateClass">
					<input type="radio" name="isPrivateTemp" class="hidden" value="1" checked="checked" />
					<div class="fleft nullItemOption">&nbsp;</div>
					<p class="fleft nullItemText">否</p>
				</div>
	            <input type="hidden" id="is_private" name="ciCustomGroupInfo.isPrivate" value="1" />
	        </li> 
			<li>  
			     <label class="fleft labFmt110">客户群描述：</label>
				 <textarea class="fleft textarea478" id="custom_desc" name="ciCustomGroupInfo.customGroupDesc"  ></textarea>
			</li> 
			<li>
				<label  class="fleft labFmt110">是否带表头： </label>
				<div class="nullItemBox hidden isHaveAttrTitle">
					<input type="radio" name="isHaveAttrTitleTmp" class="hidden" value="1" />
					<p class="fleft nullItemText">是</p>
					<div class="fleft nullItemOption">&nbsp;</div>
				</div>
				<div id="isHaveAttrTitle_default" class="nullItemBox isHaveAttrTitle">
					<input type="radio" name="isHaveAttrTitleTmp" class="hidden" value="0" checked="checked" />
					<div class="fleft nullItemOption">&nbsp;</div>
					<p class="fleft nullItemText">否</p>
				</div>
	            <input type="hidden" id="isHaveAttrTitleInput" name="isHaveAttrTitle" value="0" />
	            <!-- <span>如果导入文件自带属性列名，则不需要添写属性。</span> -->
			</li>
		    <li> 
			    <label  class="fleft labFmt110 star">导入文件： </label>
			    <div class="fleft" >
			       <input  type="text"  class="fleft inputFmt230" name="customGroupFileText" id="customGroupFileText"  readonly="readonly"/>
				   <input  type="button"  class="fleft importFileBtn" value="浏览"  />
			       <input onkeyup="$('#fnameTip').hide();"  type="file"  class="fleft customGroupFile" id="customGroupFile" name="file" />
			       <a id="importTip" class="fleft downAndHelp " href="${ctx}/ci/customersFileAction!downImportTempletFile.ai2do" title="">示例</a>
			       <div id="fnameTip" class="tishi error"  style="display:none;"></div>
			    </div>
			</li> 
			<li style="display:none;">
			    <label  class="fleft labFmt110">&nbsp; </label>
			    <div class="fleft">
			       <a  class="fleft importFileModelBtn " href="${ctx}/ci/customersFileAction!downImportTempletFile.ai2do" title="">下载导入文件模板</a>
				   <span id="importTip1" class="greenTip inline_block">&nbsp;</span>
			    </div>
			</li>
			<li> 
			    <label  class="fleft labFmt110">添加属性： </label>
			    <dl class="addAttributeBox" id="attributeDiv">
			    	<dd>
						<input type="text" name="attrList[0]" class="fleft inputFmt305" />
					</dd>
					<dd>
						<input type="text" name="attrList[1]" class="fleft inputFmt305" />
					</dd>
					<dd>
						<input type="text" name="attrList[2]" class="fleft inputFmt305"/>
					</dd>
				</dl>
				<div id="anameTip" class="tishi error"  style="display:none;"></div>
			</li> 
			<li> 
			    <label  class="fleft labFmt110">&nbsp; </label>
			    <input type="button" id="addAttributeBtn" value="添加属性" class="fleft addFileImportBtn"/>
			</li> 
			
		 	</ol>
		 	<input type="hidden" name="sceneIds" id="sceneIds1" value=""/>
		</form>
	</div>

	<!-- 表接口方式导入 -->
	<div id="tableImportBox" class="importWayBox hidden importBox">
		<form id="importByTableForm" method="post" action="${ctx}/ci/customersManagerAction!createCustomByImportTable.ai2do" enctype="multipart/form-data">
	
	    <ol class="clearfix">
		    <li> 
			  	<label class="fleft labFmt110 star">客户群名称： </label>
			  	<input type="text" name="ciCustomGroupInfo.customGroupName"  onkeyup="$('#cnameTip2').hide();" 
			  	id="customGroupName2" class="fleft inputFmt305"  value="" maxlength="35" />
			   	<!-- 提示 -->
			  	<div id="cnameTip2" class="tishi error"  style="display:none;"></div><!-- 用于重名验证 -->
			</li> 
			<li>
			  	<label class="fleft labFmt110 star">客户群分类：</label>
			  	<div class="fleft selBox">
	           	  	<div class="selTxt" id="scenesTxt2" style="overflow:hidden;"></div>
	           	  	<a href="javascript:void(0);" class="selBtn" id="selBtn2"></a>
	           	  	<div class="selListBox" id="selListBox2">
	           	     	<div class="firstItem" id="firstItem2">
	           	  	 	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
								<c:if test="${dimScenes.sortNum == 0}">
									<input type="checkbox" value="${dimScenes.sceneId}"  id="firstItem_active2" name="_sceneBoxItemCheckShare" _sceneName="${dimScenes.sceneName}"/>
									<label for="firstItem_active2">${dimScenes.sceneName}</label>
								</c:if>
							</c:forEach>
	           	  	 	</div>
	           	  	 	<ol>
		          	  	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
		          	  	 	<c:if test="${dimScenes.sortNum > 0}">
		           	  	 		<li>
		           	  	 	 	<a href="javascript:void(0);" >
		           	  	 	 		<input type="checkbox" id="selBoxItem0${st.index}" name="sceneBoxItemCheckList2" value="${dimScenes.sceneId}" _sceneName="${dimScenes.sceneName}"/>
		           	  	 	 		${dimScenes.sceneName}<!--<label for="selBoxItem0${st.index}">${dimScenes.sceneName}</label>-->
		           	  	 	 	</a>
		           	  	 	 	</li>
		           	  	 	</c:if>
		          	  	 	</c:forEach>
	           	  	 	</ol>
	           	  	 	<div class="selAllBox selBox_all2">
	           	  	 	 <input type="checkbox" id="selBox_all2">全选</input>
	           	  	 	</div>
	           	  	</div>
	           	</div>
				<div id="snameTip2" class="tishi error" style="display:none;"></div><!-- 非空验证提示 -->
			</li>
			<li>
				<label class="fleft labFmt110">是否共享：</label>
				<div class="nullItemBox hidden isPrivateClass2">
					<input type="radio" name="isPrivateTemp2" class="hidden" value="0" />
					<p class="fleft nullItemText">是</p>
					<div class="fleft nullItemOption">&nbsp;</div>
				</div>
				<div id="isPrivateClass_default2" class="nullItemBox isPrivateClass2">
					<input type="radio" name="isPrivateTemp2" class="hidden" value="1" checked="checked" />
					<div class="fleft nullItemOption">&nbsp;</div>
					<p class="fleft nullItemText">否</p>
				</div>
	            <input type="hidden" id="is_private2" name="ciCustomGroupInfo.isPrivate" value="1" />
	        </li> 
			<li>  
			     <label class="fleft labFmt110">客户群描述：</label>
				 <textarea class="fleft textarea478 height30" id="custom_desc2" onkeyup="$('#descTip2').hide();" name="ciCustomGroupInfo.customGroupDesc"></textarea>
				 <!-- 提示 -->
				 <div id="descTip" class="tishi error"  style="display:none;"></div>
			</li> 
			<li> 
			  	<div class="fleft">
			     	<label class="fleft labFmt110 star">失效时间：</label>
				 	<input id="customEndDate" readonly type="text" class="new_tag_input tag_date_input" name="ciCustomGroupInfo.endDate"
				 		onclick="WdatePicker({minDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"
				 		onblur="validateNull('customEndDate');"/>
			  	</div>
			  	<div id="customEndDateTip" class="tishi error" style="display:none;"></div><!-- 非空验证提示 -->
			</li>
			<li>
			    <label class="fleft labFmt110">生成周期：</label>
				<div class="fleft createPeriodBox" id="createPeriodBox4Import">
					<input type="hidden" id="createPeriod4Import" name="ciCustomGroupInfo.updateCycle" value="1"/>
				   	<div value="3">日</div>
				   	<p class="line-left fleft"></p>
				   	<div value="2">月</div>
				   	<p class="line-left fleft"></p>
				   	<div value="1" class="current">一次性</div>
				</div>
		  	</li>
			<li id="sysRecommendTimeLi" class="hidden">
	        	<label  class="fleft labFmt110">系统推荐时间：</label>
				<div class="nullItemBox sysRecommendTime isSysRecommendTime" id="sysRecommendTimeYes">
					<p class="fleft nullItemText">是</p>
					<div class="fleft nullItemOption">&nbsp;</div>
				</div>
				<div class="nullItemBox sysRecommendTime hidden" id="sysRecommendTimeNot">
					<div class="fleft nullItemOption">&nbsp;</div>
					<p class="fleft nullItemText">否</p>
				</div>
				<input name="ciCustomGroupInfo.isSysRecommendTime" type="hidden" id="isSysRecommendTime" value="1" />
				<input type="hidden" name="ciCustomGroupInfo.listCreateTime" id="listCreateTime"/>
				<div class="fleft hidden" id="dayTimeDiv">
					<label class="fleft labFmt76">每日</label>
					<input type="text" id="dayCycleTime" class="new_tag_input tag_date_input" onblur="validateNull('dayCycleTime');"
						onclick="WdatePicker({vel:'listCreateTime',dateFmt:'HH:mm:00',disabledDates:['23\:..\:..']})"/>
				</div>
				<div id="dayCycleTimeTip" class="tishi error" style="display:none;"></div>
				<div class="fleft hidden" id="monthTimeDiv">
					<label class="fleft labFmt76">每月</label>
					<input type="text" id="monthCycleTime" class="new_tag_input tag_date_input" onblur="validateNull('monthCycleTime');"
						onclick="WdatePicker({onpicked:datePicked,dateFmt:'dd HH:mm:ss',disabledDates:['....-..-.. 23\:..\:..','....-..-.. 23\:..\:..']})"/>
				</div>
				<div id="monthCycleTimeTip" class="tishi error" style="display:none;"></div>
	        </li>
			<li> 
			    <label class="fleft labFmt110 star">表名： </label>
			    <input onkeyup="$('#tabelTip').hide();"  name="ciCustomGroupInfo.tabelName" id="customTabelName" type="text"class="fleft inputFmt305" value=""/>
			    <div id="tabelTip" class="tishi error"  style="display:none;"></div>
			    <p class="userTipDesc">
			      	如果表名带有时间后缀，请使用<span>大写的$YYYYMMDD或$YYYYMM</span>通配符，比如日更新TABLE_$YYYYMMDD，月更新TABLE_$YYYYMM
			    </p>
			</li>
			<li> 
			    <label class="fleft labFmt110 star">字段名： </label>
			    <input onkeyup="$('#colTip').hide();" name="columnName" id="columnName" type="text" class="fleft inputFmt305" value=""/>
			    <div id="colTip" class="tishi error"  style="display:none;"></div>
			    <p class="userTipDesc">
			    	多个字段名之间请用<span>英文逗号</span>分隔，且第一列必须是<span >PRODUCT_NO</span>
			    </p>
			</li> 
			<li>  
			    <label class="fleft labFmt110 star">字段中文名： </label>
			    <input onkeyup="$('#colCnTip').hide();" name="columnCnName" id="columnCnName" type="text" class="fleft inputFmt305" value=""/>
			    <div id="colCnTip" class="tishi error" style="display:none;"></div>
			    <p class="userTipDesc">多个字段中文名之间请用<span>英文逗号</span>分隔，与字段名一一对应</p>
			</li>
			<li>
			    <label class="fleft labFmt110">过滤条件： </label>
			    <input name="ciCustomGroupInfo.whereSql" id="whereSql" type="text" class="fleft inputFmt305" value=""/>
				<p style="padding:5px 20px 5px 5px">可输入SQL语句中的<span style="color:red">WHERE</span>之后的内容</p>
			</li>
		</ol>
		<input type="hidden" name="sceneIds" id="sceneIds2" value=""/>
		</form>
	</div>
	
	<!-- 按钮操作-->
	<div class="btnActiveBox">
		<a href="javascript:void(0);" id="saveBtn" onclick="importInit();" class="ensureBtnLong">保存客户群</a>
	</div>
</div>

<script type="text/javascript">
	var attrNum = 3;
	$(document).ready(function(){
		$('#importTip').tooltip({
			content: function() {
				var html='<ul class="importCustermersTip" >' + 
					'<li>1.客户群数据文件必须是csv,txt格式文件;</li>' + 
					'<li>2.文件名中不能包含中文字符;</li>' + 
					'<li>3.每行一条记录;</li>' + 
					'<li>4.文件中多个属性用逗号分隔;</li>' + 
					'<li>5.添加属性的个数与顺序必须与文件中一致;</li>' + 
					'</ul>';
				return html;
			}
		});
	
		//添加属性
		$('#addAttributeBtn').unbind("click").bind('click', function() {
			addAttribute();
		});
		//模拟下拉框--打开方式 
	    $("#selBtn").toggle(function(){
	    	$("#snameTip").hide();
	    	$("#selListBox").slideDown();
	    	$(this).addClass("selBtnActive");
	    },function(){
	        $(this).next("#selListBox").slideUp();
	        $(this).removeClass("selBtnActive");
	    })
	    $("#scenesTxt").toggle(function(){
	    	$("#snameTip").hide();
	    	$("#selListBox").slideDown();
	    	$("#selBtn").addClass("selBtnActive");
	    },function(){
	        $(this).next("#selListBox").slideUp();
	        $("#selBtn").removeClass("selBtnActive");
	    })
	   
	    
	    $("#selBtn2").toggle(function(){
	    	$("#snameTip2").hide();
	    	$("#selListBox2").slideDown();
	    	$(this).addClass("selBtnActive");
	    },function(){
	        $(this).next("#selListBox2").slideUp();
	        $(this).removeClass("selBtnActive");
	    })
	    $("#scenesTxt2").toggle(function(){
	    	$("#snameTip2").hide();
	    	$("#selListBox2").slideDown();
	    	$("#selBtn2").addClass("selBtnActive");
	    },function(){
	        $(this).next("#selListBox2").slideUp();
	        $("#selBtn2").removeClass("selBtnActive");
	    })
	    
		//绑定隐藏提示层
		$(document).click(function(e){
		    var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
			$("#selListBox").slideUp();
			$("#selListBox2").slideUp();
			$(".selBtn").removeClass("selBtnActive");
		});
		
		//阻止事件冒泡
		$("#firstItem").click(function(ev){
			firstItemFn($("#firstItem_active"),'sceneIds1','sceneBoxItemCheckList','selBox_all','scenesTxt');
			var e = ev||event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		$("#firstItem2").click(function(ev){
			firstItemFn($("#firstItem_active2"),'sceneIds2','sceneBoxItemCheckList2','selBox_all2','scenesTxt2');
			var e = ev||event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
		$("#selListBox li a").click(function(ev){
			if($(this).find("input").attr("checked")){
				$(this).find("input").attr("checked",false)
			}else{
				$(this).find("input").attr("checked",true)
			}
			selItemFn($(this).find("input"),'sceneIds1','firstItem_active','selBox_all','sceneBoxItemCheckList','scenesTxt');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		$("#selListBox li a input").click(function(ev){
			selItemFn($(this),'sceneIds1','firstItem_active','selBox_all','sceneBoxItemCheckList','scenesTxt');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
		$("#selListBox2 li a").click(function(ev){
			if($(this).find("input").attr("checked")){
				$(this).find("input").attr("checked",false)
			}else{
				$(this).find("input").attr("checked",true)
			}
			selItemFn($(this).find("input"),'sceneIds2','firstItem_active2','selBox_all2','sceneBoxItemCheckList2','scenesTxt2');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		$("#selListBox2 li a input").click(function(ev){
			selItemFn($(this),'sceneIds2','firstItem_active2','selBox_all2','sceneBoxItemCheckList2','scenesTxt2');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})

		$(".selBox_all").click(function(ev){
			if($("#selBox_all").attr("checked")){
				$("#selBox_all").attr("checked",false)
			}else{
				$("#selBox_all").attr("checked",true)
			}
			selItemAllFn($("#selBox_all"),'sceneIds1','firstItem_active','sceneBoxItemCheckList','scenesTxt');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
		$(".selBox_all input").click(function(ev){
			selItemAllFn($("#selBox_all"),'sceneIds1','firstItem_active','sceneBoxItemCheckList','scenesTxt');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
		
		$(".selBox_all2").click(function(ev){
			if($("#selBox_all2").attr("checked")){
				$("#selBox_all2").attr("checked",false)
			}else{
				$("#selBox_all2").attr("checked",true)
			}
			selItemAllFn($("#selBox_all2"),'sceneIds2','firstItem_active2','sceneBoxItemCheckList2','scenesTxt2');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
		$(".selBox_all2 input").click(function(ev){
			selItemAllFn($("#selBox_all2"),'sceneIds2','firstItem_active2','sceneBoxItemCheckList2','scenesTxt2');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
		//是否共享 -文件导入
	    $(".isPrivateClass").click(function(){
			$(".isPrivateClass").children("input")[0].checked=false;
			$(".isPrivateClass").show();
			$(this).hide();
			$(".isPrivateClass:visible").find("input")[0].checked=true;
			var _valTemp = $.trim($(".isPrivateClass:visible").find("input").val());
	        $("#is_private").val(_valTemp);
		});
	    //是否共享 -表导入
	    $(".isPrivateClass2").click(function(){
	    	$(".isPrivateClass2").children("input")[0].checked=false;
	        $(".isPrivateClass2").show();
	        $(this).hide();
	        $(".isPrivateClass2:visible").find("input")[0].checked=true;
	        var _valTemp = $.trim($(".isPrivateClass2:visible").find("input").val());
	        $("#is_private2").val(_valTemp);
	    });
		//是否带表头-文件导入
		$('.isHaveAttrTitle').click(function() {
			$('.isHaveAttrTitle').children('input')[0].checked=false;
	        $('.isHaveAttrTitle').show();
	        $(this).hide();
	        $('.isHaveAttrTitle:visible').find('input')[0].checked=true;
	        var _valTemp = $.trim($('.isHaveAttrTitle:visible').find('input').val());
	        $('#isHaveAttrTitleInput').val(_valTemp);
	        if(_valTemp == 0) {
	        	$('#attributeDiv dd input').attr('disabled', false);
	        	$('#attributeDiv dd a').unbind('click').bind('click', function(){delAttribute(this)});
	        	$('#addAttributeBtn').removeClass('tag_btn_dis').bind('click', addAttribute);
	        } else if(_valTemp == 1) {
				$('#attributeDiv dd input').attr('disabled', 'disabled');
				$('#attributeDiv dd a').removeAttr('onclick');
				$('#addAttributeBtn').addClass('tag_btn_dis').unbind('click');
	        }
		});
	});
	
	//添加属性
	function addAttribute() {
		var attrHtml = 	'<dd><input type="text" name="attrList[' + attrNum + 
			']" class="fleft inputFmt305" /><a href="javascript:void(0);" class="addAttributeBtn" onclick="delAttribute(this)"></a></dd>';
		$('#attributeDiv').append(attrHtml);
		$("#attributeDiv").stop().animate({scrollTop: $("#attributeDiv")[0].scrollHeight}, 300); 
		attrNum ++;
		if(attrNum >= 20) { //属性最多只能有20个
			$('#addAttributeBtn').unbind("click").bind('click', function() {
				parent.parent.showAlert('一次导入最多只能添加20个属性！','failed');
			});
		}
	}
	
	//删除属性文本框
	function delAttribute(a) {
		$(a).parent().remove();
		attrNum --;
		if(attrNum < 6) { //属性最多只能有6个
			$('#addAttributeBtn').unbind("click").bind('click', function() {
				addAttribute();
			});
		}
	}
	
	//保存
	function submitList(){
		if(validateForm()){
			//加上遮罩层
			parent.showLoading('正在导入客户群文件，请耐心等候...');
			var actionUrl = $.ctx+'/ci/customersManagerAction!isNameExist.ai2do';
			$.post(actionUrl, $("#saveForm").serialize(), function(result){
				if(result){
					$('#saveForm').submit();
					parent.$('#dialog-form').height(428);
				}else{
					$('#cnameTip').empty();
					$('#cnameTip').show().append('客户群重名');
				}
				//去掉遮罩层
				parent.closeLoading();
			});
		}
	}
	
	function hideTip(){
		$("#cnameTip").hide();
	}
	
	
	//表单验证
	function validateForm(){
		var name = $.trim($("#customGroupName").val());
		if(name == "请输入客户群名称"){
			$("#customGroupName").val("");
		}else{
			$("#customGroupName").val(name);
		}
		
		var sname = $('#sceneIds1').val();
		if($.trim(sname) == "") {
			$("#snameTip").empty();
			$("#snameTip").show().append("客户群分类不能为空");
			return false;
		}
		
	    var	validater = $("#saveForm").validate({
			rules: {
				"ciCustomGroupInfo.customGroupName": {
					required: true
				}
			},
			messages: {
				"ciCustomGroupInfo.customGroupName": {
					required: "客户群名称不允许为空!"
				}
			},
			errorPlacement:function(error,element) {
				element.next("div").hide();
				element.after(error);
			},
			success:function(element){
				element.remove();
			},
			errorElement: "div",
			errorClass: "tishi error"
		});
		if(!validater.form()){
			return false;
		}
		
		if($("#isAutoMatch").prop("checked")==true) {
			$("#productAutoMacthFlag").val("1");
		}
		
		var file = $("#customGroupFile").val();
		var fileName = file.substr(file.lastIndexOf("."));   
		if(fileName == ""){
			$("#fnameTip").empty();
			$("#fnameTip").show().append("请选择文件");
			return false;
		}
		fileName = fileName.toUpperCase();
		if(fileName != ".TXT" && fileName != ".CSV"  && fileName != ".XLS"){
	    	$("#fnameTip").empty();
			$("#fnameTip").show().append("请选择合法格式文件");
			return false;
		}
		return customGroupAttValueRepeat();
	}
	//验证属性是否重复
	function customGroupAttValueRepeat() {
		var flag = true;
		var array = [];
	    var attrsInput = $('.fleft inputFmt460');
	    for(var i=0; i<attrsInput.length; i++) {
	    	var attrVal = $.trim($(attrsInput[i]).val());
	    	if($.inArray(attrVal, array) != -1) {
				flag = false;
			} else {
				array.push(attrVal);  
			}
	    }
	    if(!flag) {
	    	$("#anameTip").empty();
			$("#anameTip").show().append("客户群属性名称重复！");
	    } else {
	    	$("#anameTip").hide();
	    }
	    return flag;
	}
	
	//============================以下是表导入客户群用的：
	//保存
	function submitList2(){
		//校验是否有重名客户群
		if(validateForm2()){
			var actionUrl = $.ctx+'/ci/customersManagerAction!isNameExist.ai2do';
			$.post(actionUrl,$("#importByTableForm").serialize(), function(result){
				if(result){
					window.parent.$('.dialog_btn_div').hide();
					var submitUrl =  $.ctx+'/ci/customersManagerAction!createCustomByImportTable.ai2do';
					var  columnNames  =   $("#columnName").val().split(',');
					var columnCnNames  =   $("#columnCnName").val().split(',');
					var params = $("#importByTableForm").serialize();
					for(var i = 0;i<columnNames.length;i++){
						params = params +'&importColumnList['+i+'].columnName='+columnNames[i]+'&importColumnList['+i+'].columnCnName='+columnCnNames[i];
					}
					//alert(params);
					$.post(submitUrl,params, function(result){
						if(result.success){
							$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
								parent.simpleSearch();
								//成功后把弹出层关掉
								parent.closeDialog("#dialog-form");
							}});
						}else{alert("error");
							$.fn.weakTip({"tipTxt":result.cmsg});
						}
					});
				}else{
					$('#cnameTip2').empty();
					$('#cnameTip2').show().append('客户群重名');
				}
			});
		}
	}


	//表单验证
	function validateForm2(){
		var customGroupName = $.trim($("#customGroupName2").val());
		if(customGroupName == ""){
			$("#customGroupName2").val("");
			$("#cnameTip2").empty();
			$("#cnameTip2").show().append("请输入客户群名称");
			return false;
		}else{
			$("#customGroupName2").val(customGroupName);
		}
		var sname = $('#sceneIds2').val();
		if($.trim(sname) == "") {
			$("#snameTip2").empty();
			$("#snameTip2").show().append("客户群分类不能为空");
			return false;
		}
		
		var customEndDate = $('#customEndDate').val();
		if($.trim(customEndDate) == "") {
			$("#customEndDateTip").empty().show().append("失效时间不能为空");
			return false;
		}
		
		if($("#isSysRecommendTime").val() == "0"){
			var createPeriod = $("#createPeriod4Import").val();
			if(createPeriod == '2'){
				if($.trim($("#monthCycleTime").val()) == "") {
					$("#monthCycleTimeTip").empty();
					$("#monthCycleTimeTip").show().append("清单生成时间不能为空!");
					$(document).scrollTop(20);
					return false;
				}
			}
			if(createPeriod == '3'){
				if($.trim($("#dayCycleTime").val()) == "") {
					$("#dayCycleTimeTip").empty();
					$("#dayCycleTimeTip").show().append("清单生成时间不能为空!");
					$(document).scrollTop(20);
					return false;
				}
			}
		}
		
		var customTabelName = $.trim($("#customTabelName").val());
		if(customTabelName == ""){
			$("#customTabelName").val("");
			$("#tabelTip").empty();
			$("#tabelTip").show().append("请输入表名");
			return false;
		}else{
			$("#customTabelName").val(customTabelName);
		}
		var columnName = $.trim($("#columnName").val());
		if(columnName == ""){
			$("#columnName").val("");
			$("#colTip").empty();
			$("#colTip").show().append("请输入字段名");
			return false;
		}else{
			$("#columnName").val(columnName);
		}
		var columnCnName = $.trim($("#columnCnName").val());
		if(columnCnName == ""){
			$("#columnCnName").val("");
			$("#colCnTip").empty();
			$("#colCnTip").show().append("请输入字段中文名");
			return false;
		}else{
			$("#columnCnName").val(columnCnName);
		}
		return true;
	}
	
	function validateNull(id){
		var $input = $("#" + id);
		var val = $input.val();
		if($.trim(val) == "") {
			$input.parent().next().empty().hide();
			return false;
		}
	}
	
	function importInit(){
		//成功后把弹出层关掉
		if(importWay === 'fileImportBox'){
			submitList();
		}else if(importWay === 'tableImportBox'){
			submitList2();
		}else{
			
		}
	}
	//多选下拉框全选
	function selItemAllFn(obj,selId,firstItem_active,sceneBoxItemCheckList,scenesTxt){
		$("#"+firstItem_active).attr("checked",false);
		var $obj =$(obj);
		var $inputArr = $("input[type='checkbox'][name='"+sceneBoxItemCheckList+"']");
		var _id =obj.attr("id");
		var flag = document.getElementById(_id).checked;
		var valArr = [];
		var textArr = [];
		$.each($inputArr,function(index ,item){
			$(item).attr("checked",flag);
			valArr.push($(item).val());
			var $labelText = $.trim($(item).attr("_sceneName"));
			//alert($labelText+"*");
			textArr.push($labelText);
		});
		if(flag){
			$("#" +selId).val(valArr.join(","));
			$("#"+scenesTxt).html(textArr.join(","));
			$("#"+scenesTxt).attr("title",textArr.join(","));
		}else{
			$("#" +selId).val("");
			$("#"+scenesTxt).html("");
			$("#"+scenesTxt).attr("title","");
		}
	}
	//第一个参数
	function firstItemFn(obj,selId,sceneBoxItemCheckList,selBox_all,scenesTxt){
		var $inputArr = $("input[type='checkbox'][name='"+sceneBoxItemCheckList+"']");
		$.each($inputArr,function(index ,item){
			$(item).attr("checked",false);
		})
		$("#"+selBox_all).attr("checked",false);
		if($(obj).attr("checked")){
			var itemVal =  $(obj).val();
			var optionArr =$("#"+selId).val(itemVal);
			var $labelText = $.trim($(obj).attr("_sceneName"));
			$("#"+scenesTxt).html($labelText);
			$("#"+scenesTxt).attr("title",$labelText);
		}else{
			$("#"+selId).val("");
			$("#"+scenesTxt).html("");
			$("#"+scenesTxt).attr("title","");
		}
	}
	function selItemFn(obj,selId,firstItem_active,selBox_all,sceneBoxItemCheckList,scenesTxt){
		//全选和第一复选框不选中
		$("#"+firstItem_active).attr("checked",false);
		$("#"+selBox_all).attr("checked",false);
		//去掉第一个参数 
		var $inputArr = $("input[type='checkbox'][name='"+sceneBoxItemCheckList+"']");
		var valArr = []; 
		var textArr = [];
		$.each($inputArr,function(index ,item){
			if($(item).attr("checked")){
				valArr.push($(item).val());
				var $labelText = $.trim($(item).attr("_sceneName"));
				textArr.push($labelText);
			}
		})
		$("#"+selId).val(valArr.join(","));
		$("#"+scenesTxt).html(textArr.join(","));
		$("#"+scenesTxt).attr("title",textArr.join(","));
	}
</script>
</body>
</html>