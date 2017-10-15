<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>数据探索</title>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<style>
	.dialog_table .showTable th,.dialog_table .showTable td{padding-top:4px;padding-bottom:4px;line-height:24px;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		//审批流程展示隐藏
		$(".approve_box .approve_control").click(function(){
			
			if($(this).children("span").html() == "审批流程展示"){
				$(this).siblings(".approve").children(".approve_step").show();
				$(this).children("span").html("审批流程隐藏");
				$(this).children("span").addClass("up_arr");
			}else{
				$(this).siblings(".approve").children(".approve_step").hide();
				$(this).children("span").html("审批流程展示");
				$(this).children("span").removeClass("up_arr");
			};
			if($("#approve_step_show").height()>280){
				$("#approve_step_show").height(280);
			};
			$("#approve_step_show").mCustomScrollbar({
				theme:"minimal-dark", //滚动条样式
				scrollbarPosition:"outside", //滚动条位置
				scrollInertia:500,
				mouseWheel:{
					preventDefault:true //阻止冒泡事件
				}
			});
		});
		
		$("#tag_line_top").hover(function() {
			$(this).attr("title","以下为审批历史记录");
		});
		
		//表格斑马线
		aibiTableChangeColor(".showTable");
		
		var labelId = '${param["labelId"]}';
		var showWhichContent = '${param["showWhichContent"]}';

		if(labelId != null && labelId != "") {
	    //获取标签详细信息及流程图展示信息
		var actionUrl = $.ctx + "/ci/ciLabelInfoAction!getLabelDetailInfo.ai2do?showWhichContent="+showWhichContent;
		$.ajax({
			type: "POST",
			url: actionUrl,
			data: "labelId="+labelId,
			success: function(result){
			    //设置标签详细信息
				$("#labelName").text(result.ciLabelInfo.labelName);
				$("#busiCaliber").text(result.ciLabelInfo.busiCaliber);
				$("#effecFailTime").text(result.ciLabelInfo.failTime);

				if(result.ciLabelInfo.updateCycle == 2){
					$("#updateCycle").text("月");
				}else if(result.ciLabelInfo.updateCycle == 1){
					$("#updateCycle").text("日");
				}
				if(result.ciLabelInfo.createDesc == null){
					$("#createDesc").text("");
				}else{
					$("#createDesc").text(result.ciLabelInfo.createDesc);
				}
				if(result.ciLabelInfo.busiLegend == null){
					$("#busiLegend").text("");
				}else{
					$("#busiLegend").text(result.ciLabelInfo.busiLegend);
				}
				if(result.ciLabelInfo.applySuggest == null){
					$("#applySuggest").text("");
				}else{
					$("#applySuggest").text(result.ciLabelInfo.applySuggest);
				}
							

				//设置标签流程信息
				//需要根据标签当前审批状态来判断当前是哪个环节
				if(result.currApproveStatusId == "101" || result.currApproveStatusId == "103" || result.currApproveStatusId == "105") {
					$("#processFlow").addClass("step1");
				}else if(result.currApproveStatusId == "102") {
					$("#processFlow").addClass("step2");
				}else if(result.currApproveStatusId == "104") {
					$("#processFlow").addClass("step3");
				}else if(result.currApproveStatusId == "106") {
					$("#processFlow").addClass("step4");
				}else if(result.currApproveStatusId == "107") {
					$("#processFlow").addClass("step5");
				}
				if(result.ciApproveHistroList.length == 0&&result.nextOperName==""){
					$("#tag_line_top").hide();
				}
				var html = "";
				if(result.ciApproveHistroList.length==0&&result.nextOperName!=""){
					var content="";
					content = "<div class='tag_line'>";
					content += "<div class='tag_date'></div>";
					content += "<div class='tag_padding'></div>";
					content += "<div class='tag'>";
					content += 		"<div class='bg_l'></div>";
					content += 		"<div class='bg_c'>";
					content += "<p class='inline_left'>下一操作人:"+result.nextOperName+"</p>";
					content +=      "</div>";
				    content +=    "</div>";
				    content +=    "<div class='clear'></div>";
				    content += "</div>";
					
					html += content;
				}
				$.each(result.ciApproveHistroList, function(i, record) {
					var content="";
					content = "<div class='tag_line'>";
					content += "<div class='tag_date'>"+record.approveTime+"</div>";
					content += "<div class='tag_padding'></div>";
					content += "<div class='tag'>";
					content += 		"<div class='bg_l'></div>";
					content += 		"<div class='bg_c'>";
					content += 				"<p class='inline_left'>操作人:"+record.approveUserName+"</p>";
					content += 				"<p>操作:"+record.approveResult+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					if(record.approveOpinion!=" "){
						content += "审批意见：" +record.approveOpinion
					}
					content += 				"</p>"
					if(result.nextOperName!=""&&i == result.ciApproveHistroList.length-1){
						content += "<p class='inline_left'>下一操作人:"+result.nextOperName+"</p>";
					}
					content +=      "</div>";
				    content +=    "</div>";
				    content +=    "<div class='clear'></div>";
				    content += "</div>";
					
					html += content;
				});
				$("#approveHistory").append(html);
				$(".approve_step_show .tag_line:last").addClass("tag_line_last");
				if($(".approve_step_show").height()>200){
					$(".approve_step_show").height(200);
				};
				/*
				$(".showTable .textarea_look").each(function(){
					if($(this).height()>48){
						$(this).height(48);
					};
					
				});*/
			}
		});	
		
		}
	});
	function closeShowLabelDialog(){
		parent.closeShowLabelDialog();
	}
</script>
</head>
<body>
<c:if test='${param["showWhichContent"] == "labelDetail" || param["showWhichContent"] == "both"}'>
    <c:choose>
    	<c:when test='${param["showWhichContent"] == "labelDetail"}'>
    		<div id="labelDetailDiv" class="dialog_table" style="padding-top:10px;">
    	</c:when>
    	<c:otherwise>
    		<div id="labelDetailDiv" class="dialog_table">
    	</c:otherwise>
    </c:choose>
		<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
			<tr>
				<th width="110px">标签名称：</th>
				<td id="labelName">&nbsp;</td>
			</tr>
			<tr>
				<th>业务口径：</th>
				<td><div class="textarea_look" id="busiCaliber">&nbsp;</div></td>
			</tr>
			<tr>
				<th>失效日期：</th>
				<td id="effecFailTime">&nbsp;</td>
			</tr>
			<tr>
				<th>更新周期：</th>
				<td id="updateCycle">&nbsp;</td>
			</tr>
			<tr>
				<th>应用建议：</th>
				<td><div class="textarea_look" id="applySuggest">&nbsp;</div></td>
			</tr>
			<tr>
				<th>业务说明：</th>
				<td><div class="textarea_look" id="busiLegend">&nbsp;</div></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><div class="textarea_look" id="createDesc">&nbsp;</div></td>
			</tr>
	
		</table>
	</div>
	<div class="dialog_btn_div">
		<input name="" type="button" value=" 确 定 " onclick="closeShowLabelDialog(); " class="tag_btn"/>
	</div>
</c:if>


	
<!-- 审批流程 -->
<c:if test='${param["showWhichContent"] == "labelFlow" || param["showWhichContent"] == "both"}'>
<div id="approve_box_div" class="approve_box">
	<div class="approve">
	    <c:choose>
	    	<c:when test='${param["showWhichContent"] == "labelFlow"}'>
	    		<div class="approve_step">
	    	</c:when>
	    	<c:when test='${param["showWhichContent"] == "both"}'>
	    		<div class="approve_step" style="display:none;">
	    	</c:when>
	    </c:choose>		
			<div id="processFlow" class="step">
				<ul>
					<li>创建标签</li>
					<li>部门领导审批</li>
					<li>信息部审核</li>
					<li>数据处理</li>
					<li>创建成功</li>
				</ul>
			</div>
			<div class="approve_step_show" id="approve_step_show">
				<div id="tag_line_top" class="tag_line_top"></div>
				<div id="approveHistory">
				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
    <c:if test='${param["showWhichContent"] == "both"}'>
    	<div class="approve_control"><span>审批流程展示</span></div>
    </c:if>
</div>
</c:if>
</body>
</html>
