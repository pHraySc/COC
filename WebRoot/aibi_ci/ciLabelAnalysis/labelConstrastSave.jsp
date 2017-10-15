<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<style type="text/css">
.com_box .slideDown dl dd table.per_help{*margin-left:-40px;}
#secenId {width:310px;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	
	window.parent.scroll(0,0);
	$("#isAutoMatch").prop("checked",false);
	loadtag();
	//对比标签选中
	$(".com_lable .tag tr .bg_c,.com_lable .tag tr .bg_r").click(function(e){
		var ele = $(this).parent();
		if(ele.hasClass("on")){
			return;
		}
		ele.addClass("on").siblings().removeClass("on");
		var mainLabelId = $("#mainLabelId").val();
		var contrastLabelId=ele.attr("contrastLabelId");
		var contrastLabelName=ele.attr("contrastLabelName");
		//从父页面获取
		//var dataDate = $("#dataDate",parent.document).val();
		var dataDate = $("#dataDate").val();
		var url = $.ctx+'/ci/ciLabelConstrastAnalysisAction!findLabelContrastDetail.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data: "dataDate="+dataDate+'&contrastLabelId='+contrastLabelId+'&mainLabelId='+mainLabelId,
			success: function(result){
				if(result.success) {
					$('#curContrastLabelId').val(result.contrastDetailInfo.contrastLabelId);
					$('#contrastLabelName').text(contrastLabelName);
					$('#intersectionNum').text(result.contrastDetailInfo.intersectionNum.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
					$('#mainDifferenceNum').text(result.contrastDetailInfo.mainDifferenceNum.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
					$('#contrastDifferenceNum').text(result.contrastDetailInfo.contrastDifferenceNum.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
					$('#intersectionRate').text(result.contrastDetailInfo.intersectionRate);
					$('#mainDifferenceRate').text(result.contrastDetailInfo.mainDifferenceRate);
					$('#contrastDifferenceRate').text(result.contrastDetailInfo.contrastDifferenceRate);
				}else{
					showAlert(result.message,"failed");
				}
			}
        });
	});

	//对比标签删除
	$(".com_lable .tag tr .bg_l").click(function(e){
	     openConfirm($(this));
	});

	//百分比说明
	$(".info_table .per_help_button").click(function(e){
		$(this).parents("p").siblings("div.slideDown").slideDown("fast");
	});
	$(".slideDown dl dt .close").click(function(){
		$(this).parents(".slideDown").hide();
	});
		
	$("#successDialog").dialog({
		autoOpen: false,
		width: 550,
		title: "系统提示",
		modal: true,
		position: [350,180],
		resizable:false
	});
});

function shortThis(obj){
	var oldValue = $(obj).val();
	if(oldValue.length > 170 ){
		oldValue = oldValue.substr(0,170);
		$(obj).val(oldValue);
	}
}

//是否删除的confirm
function openConfirm(_t){
	var tar=$("#confirm_div");
	tar.dialog({
		autoOpen: false,
		width: 500,
		height:185,
		title:"系统提示",
		modal: true,
		resizable:false
	});
	tar.dialog( "open" );
	tar.find("#confirm_true" ).unbind("click").bind("click",function(){
		deleteLabel(_t);
		tar.dialog( "close" );
	});
	tar.find("#confirm_false" ).click(function(){
		tar.dialog( "close" );
	})
}

//对比标签加载
function loadtag(del){
	if($(".com_lable .tag tr").length == 1){
		$(".com_lable .tag tr").addClass("only_tr");
	}
	if($(".com_lable .tag tr").length > 1){
		$(".com_lable .tag tr:first").addClass("first_tr");
		$(".com_lable .tag tr:last").addClass("last_tr");
	}
	if(del == 'del'){
		$(".com_lable .tag tr:first .bg_c").click();
	}
}

function deleteLabel(_t){
	var mainLabelId = $("#mainLabelId").val();
	var contrastLabelId=$(_t).parent("tr").attr("contrastLabelId");
	var url = $.ctx+'/ci/ciLabelConstrastAnalysisAction!delUserLabelContrast.ai2do';
	$.ajax({
		type: "POST",
		url: url,
		data:'&contrastLabelId='+contrastLabelId+'&mainLabelId='+mainLabelId,
		success: function(result){
			if(result.success) {
				showAlert(result.message,"success",callBack);
				$(_t).parent("tr").remove();
			}else{
				showAlert(result.message,"failed");
	   		}
		}
	});
}

function callBack(){
	if($(".com_lable .tag tr").length == 0){
		$("#chars_overlay").show();
		editLabelContrast();
	}else{
		var del = '';
		$(".com_lable .tag tr").each(function(i){
			if(!$(this).hasClass("on")){
				del = 'del';
			}
		});
		loadtag(del);
	}
}
	
//将主标签与对比标签的关系保存为客户群
function toSaveCustom(type) {
	$('#saveCustomDiv').show();
	$('#type').val(type);
	var title = '保存客户群';
	$("#saveCustomDialog").dialog({
		modal:true,
		title:title,
		width:600,
		height:350,
		resizable:false,
		draggable:false,
		position: [350,180],
  		zIndex:9999,
		close: function(event, ui) {
			$(this).dialog("destroy"); // 关闭时销毁
		}
	});
}  

//保存客户群
function saveCustom(){
	var customGroupName = $('#customGroupName').val();
	var dataDate = $('#dataDate').val();
	var mainLabelId = $('#mainLabelId').val();
	var contrastLabelId = $('#curContrastLabelId').val();
	var customGroupDesc = $('#customGroupDesc').text();
	var type = $('#type').val();
	var productAutoMacthFlag = 0;
	if($("#isAutoMatch").prop("checked")==true) {
		productAutoMacthFlag = 1;
	}
	
	if(customGroupName.Trim() == '' || customGroupName.Trim() == "请输入客户群名称"){
		$('#tishi_name').html("客户群名称不能为空！");
		$('#tishi_name').show();
		return false;
	}

	var sname = $('#secenId').val();
	if($.trim(sname) == "") {
		$("#snameTip").empty();
		$("#snameTip").show().append("请选择客户群分类");
		return false;
	}
	
	$.ajax({
		url: $.ctx + "/ci/customersManagerAction!isNameExist.ai2do",
		type: "POST",
		data:{
			"ciCustomGroupInfo.customGroupName":$('#customGroupName').val()
		},
		dataType:"text",             
		success: function(data) {
			if("false"==data) {
				$('#tishi_name').html("客户群名称已经存在!");
				$('#tishi_name').show();
        		return false;
        	} else {
        		$.ajax({
						url: $.ctx+'/ci/ciLabelConstrastAnalysisAction!saveLabelContrastCustomGroup.ai2do',	
						type: "POST",
						data:{
							"contrastDetailInfo.mainLabelId":mainLabelId,
							"contrastDetailInfo.contrastLabelId":contrastLabelId,
							"ciCustomGroupInfo.customGroupName":customGroupName,
							"ciCustomGroupInfo.dataDate":dataDate,
							"ciCustomGroupInfo.customGroupDesc":customGroupDesc,
							"ciCustomGroupInfo.productAutoMacthFlag":productAutoMacthFlag,
							"ciCustomGroupInfo.sceneId":sname,
							"type":type
						},
						success: function(result) {
							if(result.success){
								var customerName = encodeURIComponent(encodeURIComponent(result.customerName));
								var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName;
								$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
							}else {
								var cmsg = result.cmsg;
								parent.showAlert(cmsg,"failed");
							}
							$('#customGroupName').attr("value","");
							$('#customGroupDesc').attr("value","");
							$("#saveCustomDialog").dialog("close");
							
						}
        		});
        	}
		}       
	});
}

function focusName(id) {
	if($('#'+id).val().Trim() == "请输入客户群名称") {
		$('#'+id).val('');
	}
}

function blurName(id) {
	if($('#'+id).val().Trim() == "") {
		$('#'+id).val('请输入客户群名称');
	}
}

function hideSaveCustom() {
	$("#saveCustomDialog").dialog('close')
}
		
//切换到对比关系维护页面
function editLabelContrast() {
    //重新加载树
	var labelId = $("#mainLabelId").val();
	var dataDate =$("#dataDate").val();
	var param = "mainLabelId="+labelId+"&dataDate="+dataDate;
	var url = $.ctx + "/ci/ciLabelConstrastAnalysisAction!initLabelConstrast.ai2do?" + param;
	window.location.href = url;
}
		
</script>

</head>

<body>
<div class="save_box">
	<dl class="analyse_right_dl marginTop_0">
		<dt><span>数据时间：${dateFmt:dateFormat(mainLabelInfo.dataDate)}</span><p align="right"> <a href="javascript:void(0)" onclick="editLabelContrast();" style="text-align:left">配置对比图谱 </a></p></dt>
	</dl>
	<dd class="save_box_dd">
		<div class="aibi_table_div" id="table1">
			<div class="com_box">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<!--对比标签开始 -->
						<table width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td width="105px;"></td>
								<td><div class="t">对比标签</div></td>
							</tr>
							<tr>
								<td class="main_lable" width="107px;" style="padding-bottom:47px;">
									<!--主标签 -->
                                    <input type="hidden" id="mainLabelId" value='${mainLabelInfo.labelId}' />
                                    <input type="hidden" id="dataDate" value='${mainLabelInfo.dataDate}' />
                                    <div class="t" style="width:107px">主标签</div>
									<div class="tag">
										<div class="bg_l"></div>
										<div class="bg_c">
											<p title=${mainLabelInfo.labelName}>${mainLabelInfo.labelName}</p>
											<p title=${mainLabelInfo.customNum}><fmt:formatNumber type="number" value="${mainLabelInfo.customNum}"  pattern="###,###,###" /></p>
										</div>
										<div class="bg_r"></div>
									</div>
									<div class="clear"></div>
								</td>
								<td class="com_lable">
									<!--对比标签列表开始 -->
									<table width="100%" cellpadding="0" cellspacing="0" class="tag">
                                        <c:forEach items="${contrastLabelInfos}" var="contrastLabel" varStatus="st">
                                        <c:choose>
                                        <c:when test="${st.index == 0}">
                                        <tr contrastLabelId='${contrastLabel.labelId}' contrastLabelName='${contrastLabel.labelName}' class="on">
                                        </c:when>
                                        <c:otherwise>
                                        <tr contrastLabelId='${contrastLabel.labelId}' contrastLabelName='${contrastLabel.labelName}' >
                                        </c:otherwise>
                                        </c:choose>
											<td class="bg_line"></td>
											<td class="bg_l"></td>
											<td class="bg_c"><p title=${contrastLabel.labelName}>${contrastLabel.labelName}</p></td>
											<td class="bg_c"><div title=${contrastLabel.customNum}><fmt:formatNumber type="number" value="${contrastLabel.customNum}"  pattern="###,###,###" /></div></td>
											<td class="bg_r"></td>
										</tr>
                                        </c:forEach>
									</table>
									<!--对比标签列表结束 -->
								</td>
							</tr>
						</table>
						<!--对比标签结束 -->
						</td>
						<td style="padding-left:10px;padding-top:23px;">
							<!--对比详情开始 -->
							<!--对比详情开始 -->
						<div class="info_box">
                            <input type="hidden" id="curContrastLabelId" value='${contrastDetailInfo.contrastLabelId}' />
							<div class="info_title"><span class="dot"></span><span><label id="contrastLabelName" >${contrastDetailInfo.contrastLabelName}</label></span><span class="dot"></span></div>
							<div class="info_show"><span class="dot"></span><span>图例所示区域的用户数及占比</span></div>
							<div class="clear"></div>
							<table width="100%" cellpadding="0" cellspacing="0" class="info_table">
								<tr class="on">
									<th>
										<img src="${ctx}/aibi_ci/assets/themes/default/images/img1.png" />
									</th>
									<td><p><label id="intersectionNum" ><fmt:formatNumber type="number" value="${contrastDetailInfo.intersectionNum}"  pattern="###,###,###" /></label></p>交集用户数</td>
									<td class="per">
										<div class="per_box">
											<p class="blue"><span><label id="intersectionRate" >${contrastDetailInfo.intersectionRate}</label></span><span class="per_help_button"><img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" /></span></p>占总用户数的
											 <!--弹出帮助开始 -->
											 <div class="slideDown" style="width:235px;right:0px;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>主标签 ∩ 对比标签</div>
																	<div class="line"></div>
																	<div>主标签 U 对比标签</div>
																</td>
																<td width="45px" class="align_left per">×100%</td>
															</tr>
														</table>
													</dd>
												</dl>
											</div>
											<!--弹出帮助结束 -->
										</div>
									</td>
                                    <td class="rborder"><a href="javascript:void(0)" onclick="toSaveCustom('intersection')" class="btn_save">保存客户群</a></td>
								</tr>
								<tr>
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr class="on">
									<th><img src="${ctx}/aibi_ci/assets/themes/default/images/img2.png" /></th>
									<td><p><label id="mainDifferenceNum" ><fmt:formatNumber type="number" value="${contrastDetailInfo.mainDifferenceNum}"  pattern="###,###,###" /></label></p>主标签差集用户数</td>
									<td class="per">
										<div class="per_box">
											<p class="blue"><span><label id="mainDifferenceRate" >${contrastDetailInfo.mainDifferenceRate}</label></span><span class="per_help_button"><img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" /></span></p>占总用户数的
											<div class="slideDown" style="width:235px; right:0;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>主标签 - (主标签 ∩ 对比标签)</div>
																	<div class="line"></div>
																	<div>主标签 U 对比标签</div>
																</td>
																<td width="45px" class="align_left per">×100%</td>
															</tr>
														</table>
													</dd>
												</dl>
											</div>
										</div>
									</td>
                                    <td class="rborder"><a href="javascript:void(0)" onclick="toSaveCustom('mainDifference')" class="btn_save">保存客户群</a></td>
								</tr>
								<tr>
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr class="on">
									<th><img src="${ctx}/aibi_ci/assets/themes/default/images/img3.png" /></th>
									<td><p><label id="contrastDifferenceNum" ><fmt:formatNumber type="number" value="${contrastDetailInfo.contrastDifferenceNum}"  pattern="###,###,###" /></label></p>对比标签差集用户数</td>
									<td class="per">
										<div class="per_box">
											<p class="blue"><span><label id="contrastDifferenceRate" >${contrastDetailInfo.contrastDifferenceRate}</label></span><span class="per_help_button"><img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" /></span></p>占总用户数的
											<div class="slideDown" style="width:235px;right:16px;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>对比标签 - (主标签 ∩ 对比标签)</div>
																	<div class="line"></div>
																	<div>主标签 U 对比标签</div>
																</td>
																<td width="45px" class="align_left per">×100%</td>
															</tr>
														</table>
													</dd>
												</dl>
											</div>
										</div>
									</td>
                                    <td class="rborder"><a href="javascript:void(0)" onclick="toSaveCustom('contrastDifference')" class="btn_save">保存客户群</a></td>
								</tr>
                                
							</table>
						</div>
						<!--对比详情结束 -->
						</td>
					</tr>
                    <!--
					<tr><td colspan="2" style="text-align:right;padding-top:8px;">数据时间：${mainLabelInfo.dataDate}</td></tr>
                    -->
				</table>
			</div>		
		</div>
	</dd>
    <div id="confirm_div" style="display:none;">
    <div class="dialog_table">
		<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
					<div class="confirm showtxt">确定删除此项？</div>
				</td>
			</tr>
		</table>
	</div>
    <!-- 按钮区 -->
    <div class="dialog_btn_div">
        <input id="confirm_true" name="" type="button" value=" 确 定 " class="tag_btn"/>
        <input id="confirm_false" name="" type="button" value=" 取 消 " class="tag_btn"/>
    </div>
</div>
    <div id="saveCustomDialog" style="display:none;overflow-x:hidden ">
		<div id="saveCustomDiv" >
                   <input type="hidden" id="type" value='' />
			<div class="dialog_table">
				<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
					<tr>
						<th width="110px"><span>*</span>客户群名称：</th>
						<td>
							<div id="select1" class="new_tag_selectBox">
								<input name="" type="text" id="customGroupName" value="请输入客户群名称" maxlength="35" class="new_tag_input" onFocus="focusName(this.id);" onBlur="blurName(this.id);" style="width:302px;*width:306px;cursor:text"/>
							</div>
							<!-- 提示 -->
							<div class="tishi error" id="tishi_name" style="display:none;"></div>
						</td>
					</tr>
					<tr>
						<th width="110px"><span>*</span>客户群分类：</th>
						<td>
							<div class="new_tag_selectBox">
								<base:DCselect selectId="secenId" selectName="ciCustomGroupInfo.sceneId" 
									tableName="<%=CommonConstants.TABLE_DIM_SCENE%>" 
						        	nullName="请选择" nullValue="" selectValue="${ciCustomGroupInfo.sceneId}"/>
						    </div> 
							<!-- 提示 -->
							<div id="snameTip" class="tishi error"  style="display:none;"></div><!-- 非空验证提示 -->
						</td>
					</tr>
					<tr>
						<th>客户群描述：</th>
						<td><textarea name="" id="customGroupDesc" style="overflow:hidden;width:400px"  onblur="shortThis(this);" onKeyUp="shortThis(this);" class="new_tag_textarea"></textarea></td>
					</tr>
				</table>
			</div>
			<!-- 按钮区 -->
			<div class="dialog_btn_div">
			    <%
                        if("true".equals(marketingMenu)){
                      %>
                      <input id="isAutoMatch" type="checkbox" class="valign_middle"/> 系统自动匹配产品&nbsp;&nbsp;
                      <%} %>
				<input id="saveButton" onClick="saveCustom();" name="" type="button" value=" 保 存 " class="tag_btn"/>
			</div>
		</div>
	</div>

    
</div>

<div id="successDialog" style="display:none;">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
<div id="errorDialog">
	<iframe id="errorFrame" name="errorFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
</div>
<div id="chars_overlay" class="chars_overlay" style="display:none;height:100%;width:100%; position:absolute; left:0; top:0;">
    <div class="loadingIcon"></div>
</div>
<script type="text/javascript">
	$(function(){
		$("#isAutoMatch").click(function(e){
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
	});
	</script>

</body>
</html>
