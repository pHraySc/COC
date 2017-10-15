<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>标签分析</title>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jquery.extend.num.format.js"></script>
<style type="text/css">
     <%--设置标签微分等超链接只显示图片，不显示名称--%>
	.hidden_a{display:none; overflow:hidden;}
</style>
<script type="text/javascript">

	//var loadFlag = true;//全局变量。跟加载顺序有关。避免其他iframe重复加载
	$(document).ready(function(){
		$("#slideIcons a").on("mouseenter",function(){
			$(this).find("span").css("display","block").width(0).show().animate({width:50},250);
		}).on("mouseleave",function(){
			$(this).find("span").width(0).hide();
		})	
		showAlarm();
		//用户数字显示格式化
		format();
		//初始化日期
		initDataDate();
		//标签分析区域 IFrame 动态加载
		loadLabelTrend(); //标签趋势分析
		loadLabelForm(); //标签构成分析
		loadLabelRel(); //标签关联分析
		aibiTableChangeColor(".showTable");
		
		//标签关注图标点击事件
		$('#attentionImg').click(function(e){
			var isAttention = $("#isAttention").val();
			var labelId=$('#labelId').val();
			
			if(isAttention == 'true'){
				//已经关注，点击进行取消关注动作
				//alert('已经关注，点击进行取消关注动作');
				var url=$.ctx+'/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
				$.ajax({
					type: "POST",
					url: url,
					data:{'labelId':labelId},
			      	success: function(result){
				       	if(result.success) {
				       		window.parent.showAlert(result.message,"success");
							$("#isAttention").val('false');
							$("#attentionImg").attr("src",'${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png');
							$("#attentionImg").attr("title",'点击添加标签关注');
						}else{
							window.parent.showAlert(result.message,"failed");
						}
					}
				});
			}else{
				//没有关注，点击进行关注动作触发
				//alert('没有关注，点击进行关注动作触发');
				var url=$.ctx+'/ci/ciLabelAnalysisAction!addUserAttentionLabel.ai2do';
				$.ajax({
					type: "POST",
   					url: url,
   					data:{'labelId':labelId},
   					success: function(result){
    					if(result.success) {
    						window.parent.showAlert(result.message,"success");
 							$("#isAttention").val('true');
							$("#attentionImg").attr("src",'${ctx}/aibi_ci/assets/themes/default/images/favorite.png');
 							$("#attentionImg").attr("title",'点击取消标签关注');
 						}else{
 							window.parent.showAlert(result.message,"failed");
 						}
      				}
	            });
			}
		});
		
		//告警弹出定位
		var toffset=$(".tag_main_icon").parent("td").offset();
		var noffset=$(".tag_main_icon .main_icon_alarm").offset();
		var alarmLeft=noffset.left-toffset.left+1;
		$(".tag_main_icon .alarm_detail").css({top:toffset.top + 28,left:toffset.left+5});
		$(".tag_main_icon .alarm_detail").css("background-position",""+alarmLeft+" 0px");
		
		//标签告警帮助提示
		$(".tag_main_icon .icon_relative_help").click(function(){
			var offset=$(this).offset();
			$(".tag_main_icon .relative_help").css({top:offset.top + 18,left:offset.left-240});
			$(".tag_main_icon .relative_help").show();
		});
		$(".tag_main_icon .icon_basis_help").click(function(){
			var offset=$(this).offset();
			$(".tag_main_icon .basis_help").css({top:offset.top + 18,left:offset.left-240});
			$(".tag_main_icon .basis_help").show();
		});
		
		//点击弹出告警详情
		$(".tag_main_icon .main_icon_alarm").click(function(){
			showWaringInfo();
		});
		
		//点击告警详情关闭
		$(".tag_main_icon .slideDown .close").click(function(){
			$(this).parents(".slideDown").hide();
			if($(this).parents(".slideDown").hasClass("alarm_detail")){
				$(".tag_main_icon .relative_help").hide();
				$(".tag_main_icon .basis_help").hide();
			};
		});
	});
	
	//初始化日期选择
	function initDataDate(){
		var updateCycle = $("#updateCycle").val();
		var dataDate = $("#dataDate").val();
		var publishTime = $("#publishTime").val();
		var failTime = $("#failTime").val();
		var dataDateMax = dataDate;
		$("#dataDateMax").val(dataDateMax);
		if(updateCycle == '1'){
			//日标签
			var dataStr = dataDate.substr(0,4)+'-'+dataDate.substr(4,2)+'-'+dataDate.substr(6,2);
			publishTime = publishTime.substr(0,10);
			var param={startDate:publishTime,endDate:failTime,downISAdd:"false"};
			window.FlipDate.createDate(dataStr,"dateDayChangeEvent");
		}
		if(updateCycle == '2'){
			//月标签
			var dataStr = dataDate.substr(0,4)+'-'+dataDate.substr(4,2);
			publishTime = publishTime.substr(0,4)+'-'+publishTime.substr(4,2);
			failTime = failTime.substr(0,4)+'-'+failTime.substr(4,2);
			var param={startDate:publishTime,endDate:failTime,downISAdd:"false"};
			window.FlipDate.createDate(dataStr,"dateMonthChangeEvent",param);
		}
		
	}
	
function dateDayChangeEvent(valueStr) {
    var year = valueStr.substr(0, 4);
    var month = valueStr.substr(5, 2);
    var day = valueStr.substr(7, 2);
    //alert(year+month+day);
    var dateDate = year + month + day;
    var dataDatemax = $("#dataDateMax").val();

    if (dateDate > dataDatemax) {
        window.parent.showAlert('日期选择不能大于最迟统计日期 ' + dataDatemax,"failed");
        var dataDate = $('#dataDate').val();
        var dataStr = dataDate.substr(0, 4) + '-' + dataDate.substr(4, 2) + '-' + dataDate.substr(6, 2);
        window.FlipDate.createDate(dataStr, "dateDayChangeEvent");
    } else {
        $('#dataDate').val(dateDate);
        //alert($('#dataDate').val());
        loadLabelTrend();
        loadLabelForm();

        updateUserNum();
		showAlarm();
    }
}
	
function dateMonthChangeEvent(valueStr) {
    var year = valueStr.substr(0, 4);
    var month = valueStr.substr(5, 2);
    //alert(year+month);
    var dateDate = year + month;
    var dataDatemax = $("#dataDateMax").val();
    //alert(dataDatemax);

    if (dateDate > dataDatemax) {
        window.parent.showAlert('月份选择不能大于最迟统计月份 ' + dataDatemax,"failed");
        var dataDate = $('#dataDate').val();
        var dataStr = dataDate.substr(0, 4) + '-' + dataDate.substr(4, 2);
        window.FlipDate.createDate(dataStr, "dateMonthChangeEvent");
    } else {
        $('#dataDate').val(dateDate);
        //alert($('#dataDate').val());
        loadLabelTrend();
        loadLabelForm();
        loadLabelRel();

        updateUserNum();
		showAlarm();
    }
}

//判断是否显示告警
function showAlarm(){
	var labelId = $("#labelId").val();
    var dataDate = $('#dataDate').val();
	var actionUrl = $.ctx + "/ci/ciLabelAnalysisAction!isShowAlarm.ai2do?labelId=" + labelId + "&dataDate=" + dataDate;
    $.ajax({
        url: actionUrl,
        type: "POST",
        success: function (result) {
            flag = result.success;
            if (flag) {
				//alert(result.isAlarm);
                if(result.isAlarm == 'true'){
					$(".tag_main_icon .main_icon_alarm").show();
				}else{
					$(".tag_main_icon .main_icon_alarm").hide();
				}
            } else {
                window.parent.showAlert(result.message,"failed");
            }
        }
    });
}
	
//更新标签关联用户数
function updateUserNum() {
    var labelId = $("#labelId").val();
    var dataDate = $('#dataDate').val();
    var actionUrl = $.ctx + "/ci/ciLabelAnalysisAction!findLabelBrandUserNum.ai2do?labelId=" + labelId + "&dataDate=" + dataDate;
    $.ajax({
        url: actionUrl,
        type: "POST",
        success: function (result) {
            flag = result.success;
            if (flag) {
                var num = accounting.formatNumber(result.userNum);
                $("#customNumLab").text('(' + num + ')');
            } else {
                window.parent.showAlert(result.message,"failed");
            }
        }
    });
}
	
	//格式化数字
	function format(){
		num = accounting.formatNumber('${labelDetailInfo.customNum}');
		$("#customNumLab").text('('+num+')');
	}
	
	//图形区域收缩
	function showTable() {
		if ($("#dog").is(":visible")) {
			$("#dog").hide();
			$("#shrink").hide();
			$("#expend").show();
			window.parent.$("#analysisFrame").height(window.parent.$("#analysisFrame").height()-210);
		} else {
			$("#dog").show();
			$("#shrink").show();
			$("#expend").hide();
			window.parent.$("#analysisFrame").height(window.parent.$("#analysisFrame").height()+210);
			//记录查看标签详细信息的日志
			var labelId = $("#labelId").val();
			$.ajax({
				url		: $.ctx + '/ci/ciLabelAnalysisAction!saveLabelDetailLog.ai2do',
			    type	: 'POST',
				data	: {
					'ciLabelInfo.labelId' : labelId
				},
				success	: function(result){}
			});
		}
	};
	//标签分析区域 IFrame,动态加载,第一个加载
	function loadLabelTrend(){
		$("#labelFormFrame").hide().prev().show();
		$("#labelRelFrame").hide().prev().show();
		var labelId = $("#labelId").val();
		var labelTrendAnalysisURL =$.ctx+'/ci/ciLabelTrendAnalysisAction!initLabelTrendAnalysis.ai2do?labelId='+labelId
		$('#labelTrendFrame').attr("src",labelTrendAnalysisURL);
		//$('#labelTrendFrame').load();
	}
	//标签对比区域 IFrame 动态加载,
	function loadLabelContrast(){
		$('#labelRelFrame').height("550px");
		var labelId = $("#labelId").val();
		var dataDate = $("#dataDate").val();
		var labelContrastAnalysisURL = $.ctx+'/ci/ciLabelConstrastAnalysisAction!initLabelContrastAnalysis.ai2do?labelId='+labelId+'&dataDate='+dataDate;
		$('#labelRelFrame').attr("src",labelContrastAnalysisURL);
		$('#labelRelFrame').load();
	}

	//标签构成分析,第二个加载
	function loadLabelForm() {
		var labelId = $("#labelId").val();
		var dataDate = $("#dataDate").val();
		var updateCycle = $("#updateCycle").val();
		//var labelId = 1;
		//var dataDate = '201304';
		//var updateCycle = 2;
		var labelFormURL = $.ctx+'/ci/ciLabelFormAnalysisAction!showLabelForm.ai2do?ciLabelFormModel.labelId='+labelId+'&ciLabelFormModel.dataDate='+dataDate+'&ciLabelFormModel.updateCycle='+updateCycle;
		$('#labelFormFrame').attr("src",labelFormURL).show();
		//$('#labelFormFrame').load();
	}
	//标签关联分析,第三个加载
	function loadLabelRel() {
		//$('#labelRelFrame').height("550px");
		var labelId = $("#labelId").val();
		var dataDate = $("#dataDate").val();
		//var labelId = 1;
		//var dataDate = '201304';
		var labelRelURL = $.ctx+'/ci/ciLabelRelAnalysisAction!toLabelRel.ai2do?ciLabelRelModel.mainLabelId='+labelId+'&ciLabelRelModel.dataDate='+dataDate;
		$('#labelRelFrame').attr("src",labelRelURL).show();
		//$('#labelRelFrame').load();
	}

	function openLabelRel() {
		var labelId  = $("#labelId").val();
		var dataDate = $('#dataDate').val();
		var param = "ciLabelInfo.labelId="+labelId;
			param = param + "&dataDate="+dataDate;
			param = param + "&tab=rel";
		var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
		window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	
	//标签分析连接
	function openLabelContrast(){
		var labelId  = $("#labelId").val();
		var dataDate = $('#dataDate').val();
		var param = "ciLabelInfo.labelId="+labelId;
			param = param + "&dataDate="+dataDate;
			param = param + "&tab=ant";
		var url = $.ctx+"/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?"+param;
		window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}
	//标签微分
	function openlabelDeff() {
		var labelId = '${labelDetailInfo.labelId}';
		var dataDate = $("#dataDate").val();
		var param = "ciLabelInfo.labelId="+labelId;
			param = param + "&dataDate="+dataDate;
			param = param + "&tab=tag";
		var url = "${ctx }/ci/ciLabelAnalysisAction!labelAnalysisMain.ai2do?";
		url += param;
		window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
	}	
	//显示告警信息
	function showWaringInfo(){
		//TODO
		var labelId = $("#labelId").val();
		var dataDate = $('#dataDate').val();
		var actionUrl = $.ctx + "/ci/ciLabelAlarmAction!findLabelAlarmRecord.ai2do";
		$.ajax({
			url: actionUrl,
		    type: "POST",
			data: {
				"ciLabelInfo.labelId" : labelId,
				"ciLabelInfo.dataDate" : dataDate,
                "ciLabelInfo.updateCycle" : '${labelDetailInfo.updateCycle}'
			},
			success: function(result){
				flag = result.success;
				if(flag){
                    var levelTr = '<td><strong>告警</strong></td>';
                    var thresholdTr = '<td><strong>告警阈值</strong></td>';
                    var realValueTr = '<td><strong>实际值</strong></td>';
                    for(i=0;i<3;i++){
                        var levelStr = result.result[i].level;
                        var levelTd = "";
                        if(levelStr == "未设置"){
                            levelTd = '<td class="align-left">未设置</td>';
                        }else if(levelStr=="正常"){
                            levelTd = '<td class="green">正常</td>';
                        }else{
                            levelTd = '<td class="red">'+levelStr+'</td>';
                        }
                        levelTr = levelTr + levelTd;

                        thresholdTr = thresholdTr + '<td >'+ result.result[i].threshold + '</td>';
                        if(i!=2){
                            realValueTr = realValueTr + '<td >'+result.result[i].realValue.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,") +'</td>';
                        }else{
                            realValueTr = realValueTr + '<td>'+result.result[i].realValue +'</td>';
                        }
                    }
                    $("#level").empty();
                    $("#level").append(levelTr);
                    $("#threshold").empty();
                    $("#threshold").append(thresholdTr);
                    $("#realValue").empty();
                    $("#realValue").append(realValueTr);
					/* var num = "";
					if((result.msg != null && result.msg != "") || result.msg == 0){
						num = $.formatNumber(result.msg, 3, ',');
					} */
					
					$(".tag_main_icon .alarm_detail").show();
				}else{
					window.parent.showAlert(result.msg,"failed");
				}
			}
		});
		
	}

	function showLabelUsedLog(){
		parent.showLabelUsedLog($("#labelId").val());
	}

	function showLabelProcess(){
		parent.showLabelProcess($("#labelId").val());
	}
</script>
</head>
<body>
<input id="labelType" type="hidden" value="ct"/>
<input id="dataDateMax"  type="hidden" value="" />
<input id="publishTime"  type="hidden" value="${labelDetailInfo.publishTime}" />
<input id="failTime"  type="hidden" value="${labelDetailInfo.failTime}" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="l_d v_top">
		<!--标签口径及日期 -->
		<div class="top_box">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="40" class="v_top" style="padding-left:5px;">
						<!--如果是最新标签，显示下面div -->
                        <c:if  test="${labelDetailInfo.isNew == 'true'}">
						 <div title="新建标签" class="new"></div>
                        </c:if>
						<!--标签名称 -->
						<div class="tag_main_title">${labelDetailInfo.labelName}
							<c:if test="${labelDetailInfo.customNum != null}">
							   <label id="customNumLab" >${labelDetailInfo.customNum}</label>
							</c:if>
						</div>
                        <!--隐藏标签 labelId-->
                        <input type="hidden" id="labelId" value="${labelDetailInfo.labelId}" />
						<!--icon 根据实际情况显示 -->
						<div class="tag_main_icon">
                            <c:if  test="${labelDetailInfo.isHot == 'true'}">
							<span><img title="系统热门标签" src="${ctx}/aibi_ci/assets/themes/default/images/hot.png" /></span>
                            </c:if>
                            <input type="hidden" id="isAttention" value="${labelDetailInfo.isAttention}" />
                            <c:if  test="${labelDetailInfo.isAttention == 'false'}">
							   <span><img style="cursor:pointer" title="点击增加标签关注" id="attentionImg" src="${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png" /></span>
                            </c:if>
                            <c:if  test="${labelDetailInfo.isAttention == 'true'}">
							   <span><img style="cursor:pointer" title="点击取消标签关注" id="attentionImg" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png" /></span>
                            </c:if>
                            <span><img style="cursor:pointer" title="标签告警" src="${ctx}/aibi_ci/assets/themes/default/images/warning.png" class="main_icon_alarm"/></span>
							<!-- 告警弹出层start -->
							<div class="slideDown alarm_detail">
								<dl>
									<dt><span class="close"></span>告警详情</dt>
									<dd>
										<table width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0" id="alarmDetailTable">
											<tr class="even">
												<th width="78px">&nbsp;</th>
												<th width="110px">基础</th>
												<th width="110px">环比<img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" class="icon_relative_help"/></th>
												<th width="110px" style="border-right:none;">占比<img src="${ctx}/aibi_ci/assets/themes/default/images/icon_help.png" class="icon_basis_help"/></th>
											</tr>
                                            <tr id="level" class="odd"></tr>
                                            <tr id="threshold" class="even"></tr>
                                            <tr id="realValue" class="odd"></tr>

										</table>
									</dd>
								</dl>
							</div>
							<div class="slideDown relative_help">
								<dl>
									<dt><span class="close"></span></dt>
									<dd>
										环比预警：将标签当前统计周期的用户数与上一周期用户数进行比较，针对差值进行预警<br/>
										<span>环比增长量 = 标签当前统计周期用户数 - 标签上一周期用户数</span>
									</dd>
								</dl>
							</div>
							<div class="slideDown basis_help">
								<dl>
									<dt><span class="close"></span></dt>
									<dd>
										占比预警：将标签当前统计周期的用户数与父标签当前统计周期的用户数进行比较，针对占比值进行预警。没有父标签的标签，不能设置占比预警。<br/>
										<table cellpadding="0" cellspacing="0" align="center">
											<tr>
												<td>占比 = </td>
												<td class="gsh" class="align_right">
													<div>标签当前统计周期用户数</div>
													<div class="line"></div>
													<div>父标签当前统计周期用户数</div>
												</td>
											</tr>
										</table>
									</dd>
								</dl>
							</div>
							<!-- 告警弹出层end -->
						</div>
					</td>
					<td width="230px" rowspan="2" class="v_top">
                    <div class="wdate_box">
                       <input  type="hidden" id="dataDate" value="${labelDetailInfo.dataDate}" />
                       <div id="flashContent">
			<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="220" height="52" id="FlipDate" align="middle">
				<param name="movie" value="${ctx}/aibi_ci/assets/flash/FlipDate.swf" />
				<param name="quality" value="high" />
				<param name="bgcolor" value="#ffffff" />
				<param name="play" value="true" />
				<param name="loop" value="true" />
				<param name="wmode" value="transparent" />
				<param name="scale" value="showall" />
				<param name="menu" value="true" />
				<param name="devicefont" value="false" />
				<param name="salign" value="" />
				<param name="allowScriptAccess" value="always" />
				<!--[if !IE]>-->
				<object type="application/x-shockwave-flash" data="${ctx}/aibi_ci/assets/flash/FlipDate.swf" width="220" height="52">
					<param name="movie" value="${ctx}/aibi_ci/assets/flash/FlipDate.swf" />
					<param name="quality" value="high" />
					<param name="bgcolor" value="#ffffff" />
					<param name="play" value="true" />
					<param name="loop" value="true" />
					<param name="wmode" value="transparent" />
					<param name="scale" value="showall" />
					<param name="menu" value="true" />
					<param name="devicefont" value="false" />
					<param name="salign" value="" />
					<param name="allowScriptAccess" value="always" />
				<!--<![endif]-->
					
				<!--[if !IE]>-->
				</object>
				<!--<![endif]-->
			</object>
		</div>
        </div>
					</td>
				</tr>
				<tr>
					<td class="v_top" >
						<!--口径说明 -->	
						<div class="tag_main_text">
							<strong>业务口径：</strong> 
							<c:choose> 
							    <c:when test="${labelDetailInfo.busiCaliber == null}">
							   	   	暂无内容
							    </c:when>   
								<c:otherwise> 
									${labelDetailInfo.busiCaliber}
								</c:otherwise>
							</c:choose> 
						</div>
					</td>
				</tr>
			</table>
			<!--口径的详细信息，默认隐藏，点击按钮可以展开显示 -->
			<div id="dog" style="display:none;">
				<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
					<tr>
						<th width="20%">数据来源：</th>
						<td width="30%">${labelDetailInfo.dataSource}</td>
						<th width="20%">数据状态：</th>
						<td width="30%">${labelDetailInfo.dataStatus}</td>
					</tr>
					<tr>
						<th>生/失效时间：</th>
						<td>${labelDetailInfo.effecTime} 至 ${labelDetailInfo.failTime}</td>
						<th>更新周期：</th>
						<td>${labelDetailInfo.updateCycleName}</td>
                        <input type="hidden" id="updateCycle" value="${labelDetailInfo.updateCycle}" />
					</tr>
					<tr>
						<th>使用次数：</th>
						<td>${labelDetailInfo.useTimes} | <a href="javascript:showLabelUsedLog();">查看日志</a></td>
                        <!--
                        <td>${labelDetailInfo.useTimes}</td>
                        -->
						<th>审批流程：</th>
						<td>${labelDetailInfo.currApproveStatus} | <a href="javascript:showLabelProcess();">查看</a></td>
					</tr>
					<tr>
						<th>创建人：</th>
						<td>${labelDetailInfo.createUser}</td>
						<th>创建时间：</th>
						<td>${labelDetailInfo.createTime}</td>
					</tr>
                    <tr>
						<th>发布人：</th>
						<td>${labelDetailInfo.publishUser}</td>
						<th>发布时间：</th>
						<td>${labelDetailInfo.publishTime}</td>
					</tr>
                    <tr>
						<th style="vertical-align: top">业务说明：</th>
						<td colspan="3">${labelDetailInfo.busiLegend}</td>
					</tr>
                    <tr>
						<th style="vertical-align: top">应用建议：</th>
						<td colspan="3">${labelDetailInfo.applySuggest}</td>	
					</tr>
				</table>
			</div>
		</div>
		<div class="main_button_title">
			<div style="width:80px;float:left;">
				<a href="javascript:void(0);" id="expend" onclick="showTable()" title='点击显示详细标签信息' class="icon_hide">展开</a>
				<a href="javascript:void(0);" id="shrink" style="display: none;" onclick="showTable()" title='点击隐藏详细标签信息'  class="icon_show">收起</a>
			</div>
			
				<ul id="slideIcons" class="slideIcons" style="">
				<li><a href="javascript:void(0);" onclick="parent.addLabel('${labelDetailInfo.labelId}');" class="icon_add"><span>添加</span></a></li>
				<div id="${labelDetailInfo.labelId}" name="${labelDetailInfo.labelName}" element="${labelDetailInfo.labelId}" min="${labelDetailInfo.minVal}" 
		            max="${labelDetailInfo.maxVal}" type="${labelDetailInfo.labelTypeId}" effectDate="${labelDetailInfo.effectDate}" 
		            updateCycle="${labelDetailInfo.updateCycle}" attrVal="${labelDetailInfo.attrVal}" style="display:none;"></div>
				
                <c:if  test="${labelDetailInfo.updateCycle == '2'}">
                <li><a href="javascript:void(0);" onclick="openlabelDeff();" class="icon_wtag"><span>标签微分</span></a></li>
    			<li><a href="javascript:void(0);" onclick="openLabelRel();" class="icon_rel"><span>关联分析</span></a></li>
    			<li><a href="javascript:void(0);" onclick="openLabelContrast();" class="icon_ant"><span>对比分析</span></a></li>
                </c:if>
                <c:if  test="${labelDetailInfo.updateCycle == '1'}">
				<a href="javascript:void(0);" onclick="openlabelDeff();" class="icon_wtag"><span class="hidden_a">标签微分</span></a>
                </c:if>
                </ul>
		
			<div class="clear"></div>
		</div>
		<div class="main_bottom"></div>
		<div class="clear"></div>
		<!--趋势分析开始 -->
		<div class="main_box">
			<iframe name="labelTrendFrame" scrolling="no" src="" allowtransparency="true" id="labelTrendFrame"  framespacing="0" border="0" frameborder="0" style="width:100%;height:350px"></iframe>
		</div>
		<!--趋势分析结束 -->
		<!--构成分析开始 -->
		<div class="main_box" id="labelFormDiv" style="position:relative; height:350px;">
			<!-- <div id="chars_overlay" class="chars_overlay" style="height:350px;width:100%; position:absolute; left:0; top:0;">
                <div class="loadingIcon"></div>
            </div> -->
            <iframe name="labelFormFrame" scrolling="no" src="" allowtransparency="true" id="labelFormFrame"  framespacing="0" border="0" frameborder="0" style="width:100%;height:350px;"></iframe>
		</div>
		<!--构成分析结束-->
        <c:if  test="${labelDetailInfo.updateCycle == '1'}">
        <div class="main_box" id="labelRelDiv" style="display:none"> </div>
        </c:if>
        <c:if  test="${labelDetailInfo.updateCycle == '2'}">
		<!--关联对比开始 -->
		<div class="main_box" id="labelRelDiv" style="position:relative; height:650px;">
			<!-- <div id="chars_overlay" class="chars_overlay" style="height:650px;width:100%; position:absolute; left:0; top:0;">
                <div class="loadingIcon"></div>
            </div> -->
            <iframe name="labelRelFrame" scrolling="no" src="" allowtransparency="true" id="labelRelFrame"  framespacing="0" border="0" frameborder="0" style="width:100%;height:650px;"></iframe>
		</div>
		<!--关联对比结束-->
        </c:if>
        <!-- 分析页不被浮动层遮住 -->
        <div style="height:60px"></div>
	</td>
  </tr>
</table>
</body>
</html>
