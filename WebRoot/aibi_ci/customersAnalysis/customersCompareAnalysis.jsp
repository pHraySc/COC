<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>客户群对比分析</title>
<script type="text/javascript">
$(document).ready(function(){
	loadtag();
	//对比标签选中
	$(".com_lable .tag tr .bg_c,.com_lable .tag tr .bg_r").click(function(e){
		if($(this).parent().hasClass("on")){
			return;
		}
		loadAnalysis($(this).parent());
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
		if(del == 'del'){
			loadAnalysis($(".com_lable .tag tr"));
		}
	};
	if($(".com_lable .tag tr").length > 1){
		$(".com_lable .tag tr:first").addClass("first_tr");
		$(".com_lable .tag tr:last").addClass("last_tr");
		if(del == 'del'){
			loadAnalysis($(".com_lable .tag tr:first"));
		}
	};
}

function loadAnalysis(obj){
	obj.addClass("on").siblings().removeClass("on");
	var customGroupId = $("#customGroupId").val();
	var listTableName = $("#listTableName").val();
	var contrastLabelId=obj.attr("contrastLabelId");
	var contrastLabelName=obj.attr("contrastLabelName");
	//从父页面获取
	var customNum = $("#customNum",parent.document).val();
	var dataDate = $("#dataDate",parent.document).val();
	var url = $.ctx+'/ci/customersCompareAnalysisAction!customersCompareAnalysisResult.ai2do';
	$.ajax({
		type: "POST",
	   	url: url,                    
	   	data: 'customGroupContrast.customGroupId='+customGroupId+'&customGroupContrast.dataDate='+dataDate+'&customGroupContrast.contrastLabelId='+contrastLabelId+'&customGroupContrast.listTableName='+listTableName+'&customGroupContrast.customGroupNum='+customNum,
	   	success: function(result){
			if(result.success) {
			  	$('#curContrastLabelId').val(result.customGroupContrast.contrastLabelId);
			  	$('#contrastLabelName').text(contrastLabelName);
			 	$('#intersectionNum').text($.formatNumber(result.customGroupContrast.intersectionNum, 3, ','));
			  	$('#mainDifferenceNum').text($.formatNumber(result.customGroupContrast.mainDifferenceNum, 3, ','));
			  	$('#contrastDifferenceNum').text($.formatNumber(result.customGroupContrast.contrastDifferenceNum, 3, ','));
			  	$('#intersectionRate').text(result.customGroupContrast.intersectionRate);
			  	$('#mainDifferenceRate').text(result.customGroupContrast.mainDifferenceRate);
			  	$('#contrastDifferenceRate').text(result.customGroupContrast.contrastDifferenceRate);
		 	}else{
			 	showAlert(result.message,"failed");
		 	}
       	}
	});
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
	var customGroupId = $("#customGroupId").val();
    var contrastLabelId=$(_t).parent("tr").attr("contrastLabelId");
	var url = $.ctx+'/ci/customersCompareAnalysisAction!delUserCustomContrast.ai2do';
	$.ajax({
		type: "POST",
      	url: url,
      	data:'&customGroupContrast.contrastLabelId='+contrastLabelId+'&customGroupContrast.customGroupId='+customGroupId,
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
		window.parent.loadCustomersContrast();
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

//对比分析连接
function openCustomContrast(){
	if($('#customNum').val() == ""){
		window.parent.showAlert("客户群清单未创建成功","failed");
		return;
	}
	var customGroupId = $("#customGroupId",parent.document).val();
	var listTableName = $("#listTableName",parent.document).val();
	var customNum = $("#customNum",parent.document).val();
	var dataDate = $("#dataDate",parent.document).val();
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.listTableName="+listTableName;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&dataDate="+dataDate;
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
</script>
</head>
<body>
<div class="main_con_box">
	<!--标题行开始 -->
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_dbfx"><span>对比分析</span>统计日期：${dateFmt:dateFormat(customGroupContrast.dataDate)}&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="openCustomContrast()">配置对比图谱</a></div>
		<div class="tag_box_manage" >
			<!--选择框 -->
			<div class="like_button_box" id="select_box" style="width:110px;float: right;">
				<div class="like_button_left"></div>
				<div class="like_button_middle" style="width:72px"><span>对比分析</span></div>
				<div class="like_button_hr"></div>
				<div class="like_button_middle" style="width:26px"><a href="#" class="icon_select"></a></div>
				<div class="like_button_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="clear"></div>
		<!--弹出层-->
		<div class="form-field" style="right:15px;_right:14px">
			<ul>
				<li class="on"><a href="javascript:void(0);">对比分析</a></li>
				<li><a  onclick="window.parent.loadCustomersRel();">关联分析</a></li>
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
									<!--客户群 -->
									<div class="t" style="width:107px">客户群</div>
									<div class="tag">
										<div class="bg_l"></div>
										<div class="bg_c">
											<p title="${customGroupContrast.customGroupName}">${customGroupContrast.customGroupName}</p>
											<p title="<c:if test="${customGroupContrast.customGroupNum != null}"><fmt:formatNumber type="number" value="${customGroupContrast.customGroupNum}" pattern="###,###,###"/></c:if><c:if test="${customGroupContrast.customGroupNum == null}">0</c:if>">
												<c:if test="${customGroupContrast.customGroupNum != null}">
												    <fmt:formatNumber type="number" value="${customGroupContrast.customGroupNum}" pattern="###,###,###"/>
												</c:if>
												<c:if test="${customGroupContrast.customGroupNum == null}">
												    0
												</c:if>
											</p>
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
											<td class="bg_c"><p title="${contrastLabel.labelName}">${contrastLabel.labelName}</p></td>
											<td class="bg_c"><div title='<fmt:formatNumber type="number" value="${contrastLabel.customNum}" pattern="###,###,###"/>'><fmt:formatNumber type="number" value="${contrastLabel.customNum}" pattern="###,###,###"/></div></td>
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
                            <input type="hidden" id="curContrastLabelId" value='${customGroupContrast.contrastLabelId}' />
							<div class="info_title"><span class="dot"></span><span><label id="contrastLabelName" >${customGroupContrast.contrastLabelName}</label></span><span class="dot"></span></div>
							<div class="info_show"><span class="dot"></span><span>图例所示区域的用户数及占比</span></div>
							<div class="clear"></div>
							<table width="100%" cellpadding="0" cellspacing="0" class="info_table">
								<tr class="on">
									<th>
										<img src="${ctx}/aibi_ci/assets/themes/default/images/img1.png" />
									</th>
									<td><p><label id="intersectionNum" ><fmt:formatNumber type="number" value="${customGroupContrast.intersectionNum}" pattern="###,###,###"/></label></p>交集用户数</td>
									<td class="per">
										<div class="per_box">
											<p class="blue"><span><label id="intersectionRate" >${customGroupContrast.intersectionRate}</label></span><span class="per_help_button"><img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" /></span></p>占总用户数的
											 <!--弹出帮助开始 -->
											 <div class="slideDown" style="width:235px">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>客户群 ∩ 对比标签</div>
																	<div class="line"></div>
																	<div>客户群 U 对比标签</div>
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
									<td><p><label id="mainDifferenceNum" ><fmt:formatNumber type="number" value="${customGroupContrast.mainDifferenceNum}" pattern="###,###,###"/></label></p>客户群差集用户数</td>
									<td class="per">
										<div class="per_box">
											<p class="blue"><span><label id="mainDifferenceRate" >${customGroupContrast.mainDifferenceRate}</label></span><span class="per_help_button"><img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" /></span></p>占总用户数的
											<div class="slideDown" style="width:235px;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>客户群  - (客户群 ∩ 对比标签)</div>
																	<div class="line"></div>
																	<div>客户群 U 对比标签</div>
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
									<td><p><label id="contrastDifferenceNum" ><fmt:formatNumber type="number" value="${customGroupContrast.contrastDifferenceNum}" pattern="###,###,###"/></label></p>对比标签差集用户数</td>
									<td class="per">
										<div class="per_box">
											<p class="blue"><span><label id="contrastDifferenceRate" >${customGroupContrast.contrastDifferenceRate}</label></span><span class="per_help_button"><img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" /></span></p>占总用户数的
											<div class="slideDown" style="width:235px;">
												<dl>
													<dt><span class="close"></span></dt>
													<dd>
														<table cellpadding="0" cellspacing="0" class="per_help">
															<tr>
																<td class="align_right">
																	<div>客户群  - (客户群 ∩ 对比标签)</div>
																	<div class="line"></div>
																	<div>客户群 U 对比标签</div>
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

<input type="hidden" id="listTableName" value='${customGroupContrast.listTableName}' />
<input type="hidden" id="customGroupId" value='${customGroupContrast.customGroupId}' />

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