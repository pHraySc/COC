<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<script type="text/javascript">
$(document).ready(function(){
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
		var dataDate = $("#dataDate",parent.document).val();
		var url = $.ctx+'/ci/ciLabelConstrastAnalysisAction!findLabelContrastDetail.ai2do';
		$.ajax({
			type: "POST",
			url: url,
			data: "dataDate="+dataDate+'&contrastLabelId='+contrastLabelId+'&mainLabelId='+mainLabelId,
			success: function(result){
				if(result.success) {
					$('#curContrastLabelId').val(result.contrastDetailInfo.contrastLabelId);
					$('#contrastLabelName').text(contrastLabelName);
					$('#intersectionNum').text($.formatNumber(result.contrastDetailInfo.intersectionNum, 3, ','));
					$('#mainDifferenceNum').text($.formatNumber(result.contrastDetailInfo.mainDifferenceNum, 3, ','));
					$('#contrastDifferenceNum').text($.formatNumber(result.contrastDetailInfo.contrastDifferenceNum, 3, ','));
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
	//判断分辨率
	if(document.documentElement.clientWidth < 800){
		$(".com_box .info_table .on p").css("font-size","15px");
	}
});

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
	});
}

//对比标签删除
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
		window.parent.loadLabelContrast();
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

function openLabelContrast(){
	var labelId  = $("#labelId",parent.document).val();
	//从父页面获取
	var dataDate = $("#dataDate",parent.document).val();
	var param = "ciLabelInfo.labelId="+labelId;
		param = param + "&dataDate="+dataDate;
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
</script>
</head>
<body>
<div class="main_con_box">
	<!--标题行开始 -->
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_dbfx"><span>关联对比</span>统计日期：${dateFmt:dateFormat(mainLabelInfo.dataDate)}&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="openLabelContrast()">配置对比图谱</a></div>
		<div class="tag_box_manage">
			<!--选择框 -->
			<div class="like_button_box" id="select_box" style="width:110px;float:right">
				<div class="like_button_left"></div>
				<div class="like_button_middle" style="width:72px"><span>对比分析</span></div>
				<div class="like_button_hr"></div>
				<div class="like_button_middle" style="width:26px"><a href="javascript:void(0);" class="icon_select"></a></div>
				<div class="like_button_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="clear"></div>
		<!--弹出层-->
		<div class="form-field" style="right:15px;_right:14px;float:right">
			<ul>
				<li class="on"><a href="javascript:void(0);">对比分析</a></li>
				<li><a href="javascript:void(0);" onclick="window.parent.loadLabelRel();">关联分析</a></li>
			</ul>
		</div> 
	</div>
	<!--标题行结束 -->
	<div class="aibi_chart_div" id="chart1" style="height:249px;display:none;">
		<img src="${ctx}/aibi_ci/assets/themes/default/images/show/img1.png" />
	</div>
    
    <c:choose>
    <c:when test="${hasContrastLabel == 'true'}">
    <div class="aibi_table_div" id="table1">
    </c:when>
    <c:otherwise>
    <div class="aibi_table_div" style="height:500px;text-align:center;line-height:500px">
           <div class="aibi_chart_nodata">
           <div class="nodata_txt"></div>
    </div>
    <div class="aibi_table_div" id="table1" style="display:none;">
    </c:otherwise>
	</c:choose>
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
											 <div class="slideDown" style="width:235px;">
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
											<div class="slideDown" style="width:235px;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>主标签 -(主标签 ∩ 对比标签)</div>
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
											<div class="slideDown" style="width:235px;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>对比标签 -(主标签 ∩ 对比标签)</div>
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
								</tr>
							</table>
						</div>
						<!--对比详情结束 -->
					</td>
				</tr>
			</table>
		</div>		
	</div>
</div>
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

<div id="chars_overlay" class="chars_overlay" style="display:none;height:550px;width:100%; position:absolute; left:0; top:0;">
    <div class="loadingIcon"></div>
</div>
</body>
</html>
