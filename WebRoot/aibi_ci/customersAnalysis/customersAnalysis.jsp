<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>客户群分析</title>
<script type="text/javascript">
//var loadFlag = true;//全局变量。跟加载顺序有关。避免其他iframe重复加载
$(document).ready(function(){
	showAlarm();
	initDataDate();
	loadCustomersTrend();
	loadCustomGroupForm();
    loadCustomersRel();
	aibiTableChangeColor(".showTable");

	//增加标签分析等按钮的鼠标移入和移出特效效果(由于标签是通过滚动动态添加的，所以可以在此处为新加载的页面添加事件)
	$("#slideIcons a").on("mouseenter",function(){
		$(this).find("span").css("display","block").width(0).show().animate({width:50},250);
	}).on("mouseleave",function(){
		$(this).find("span").width(0).hide();
	})

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
	var dataDate = $("#dataDate").val();
	var updateCycle = $("#updateCycle").val();
       var dataDateMax = dataDate;
	$("#dataDateMax").val(dataDateMax);

	//月标签
	var dataStr = dataDate.substr(0,4)+'-'+dataDate.substr(4,2);
    if(updateCycle == 1){
    	var param={startDate:dataStr,endDate:dataStr,downISAdd:"false"};
    	window.FlipDate.createDate(dataStr,"dateDayChangeEvent",param);
	} else{
		var startDateStr = $("#startDate").val();
		startDateStr = startDateStr.substr(0,4)+'-'+startDateStr.substr(4,2);
		var endDateStr = $("#endDate").val();
		endDateStr = endDateStr.substr(0,4)+'-'+endDateStr.substr(4,2);
		var param={startDate:startDateStr,endDate:endDateStr,downISAdd:"false"};
    	window.FlipDate.createDate(dataStr,"dateMonthChangeEvent",param);
	}
	
}

function dateDayChangeEvent(valueStr){
	var year=valueStr.substr(0,4);
	var month=valueStr.substr(5,2);
	var day=valueStr.substr(7,2);
	$('#dataDate').val(year+month+day);
	 //重新加载页面
    loadCustomersTrend();
    loadCustomGroupForm();
	showAlarm();
}

function dateMonthChangeEvent(valueStr){
	var year=valueStr.substr(0,4);
	var month=valueStr.substr(5,2);
	var dateDate = year+month
	var dataDatemax = $("#dataDateMax").val();
	
	if(dateDate > dataDatemax){
		window.parent.showAlert("日期选择不能大于最迟统计日期 "+dataDatemax,"failed");
		var dataDate = $('#dataDate').val();
		var dataStr = dataDate.substr(0,4)+'-'+dataDate.substr(4,2)+'-'+dataDate.substr(6,2);
		window.FlipDate.createDate(dataStr,"dateMonthChangeEvent");
	}else{
		$('#dataDate').val(dateDate);
		updateUserNum();
		//重新加载页面
		loadCustomersTrend();
		loadCustomGroupForm();
		loadCustomersRel();
		showAlarm();
	}
}

//判断是否显示告警
function showAlarm(){
	var customGroupId = $('#customGroupId').val();
	var dataDate = $('#dataDate').val();
	var actionUrl = $.ctx + "/ci/customersCompareAnalysisAction!isShowAlarm.ai2do?customGroupContrast.customGroupId="+customGroupId+"&customGroupContrast.dataDate="+dataDate;
    $.ajax({
        url: actionUrl,
        type: "POST",
        success: function (result) {
            flag = result.success;
            //alert(flag);
            if (flag) {
               
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
	
	//更新客户群用户数
	function updateUserNum(){
		var customGroupId = $('#customGroupId').val();
		var dataDate = $('#dataDate').val();
		var actionUrl = $.ctx + "/ci/customersCompareAnalysisAction!updateUserNum.ai2do?customGroupContrast.customGroupId="+customGroupId+"&customGroupContrast.dataDate="+dataDate;
		$.ajax({
			url: actionUrl,
		    type: "POST",
			success: function(result){
				flag = result.success;
				if(flag){
					$("#customNumLab").text('('+result.customNum+')');
					$("#customNum").val(result.customNum);
					$("#listTableName").val(result.listTableName);
					$("#customOpt").show();
					if(result.listTableName == ""){
						$("#customOpt").hide();
					}
				}else{
					$("#customNumLab").text('(-)');
					$("#customOpt").hide();
					$("#customNum").val("");
					$("#listTableName").val("");
				}
			}
		});
	}

//图形区域收缩
function showTable() {
	if ($("#dog").is(":visible")) {
		$("#dog").hide();
		$("#shrink").hide();
		$("#expend").show();
		window.parent.$("#analysisFrame").height(window.parent.$("#analysisFrame").height()-160);
	} else {
		$("#dog").show();
		$("#shrink").show();
		$("#expend").hide();
		window.parent.$("#analysisFrame").height(window.parent.$("#analysisFrame").height()+160);
	}
};
//客户群趋势分析区域 IFrame 动态加载，第一个加载
function loadCustomersTrend(){
	$("#customGroupFormFrame").hide().prev().show();
	$("#customGroupRelFrame").hide().prev().show();
	var customGroupId = $("#customGroupId").val();
	var dataDate = $("#dataDate").val();
	var customersTrendAnalysisURL =$.ctx+'/ci/customersTrendAnalysisAction!initCustomersTrendAnalysis.ai2do?customGroupInfo.customGroupId='+customGroupId+'&dataDate='+dataDate;
	$('#customersTrendFrame').attr("src",customersTrendAnalysisURL);
	//$('#customersTrendFrame').load();
}
//客户群构成分析IFrame 动态加载，第二个加载
function loadCustomGroupForm() {
	var customGroupId = $("#customGroupId").val();
	var dataDate = $("#dataDate").val();
	var updateCycle = $("#updateCycle").val();
	var labelFormURL = $.ctx+'/ci/customersFormAnalysisAction!customersForm.ai2do?ciCustomGroupForm.customGroupId='+customGroupId+'&ciCustomGroupForm.dataDate='+dataDate+'&ciCustomGroupForm.updateCycle='+updateCycle;
	$('#customGroupFormFrame').attr("src",labelFormURL).show();
	//$('#customGroupFormFrame').load();
}
//标签对比区域 IFrame 动态加载
function loadCustomersContrast(){
	$('#customGroupRelFrame').height("550px");
	var customGroupId = $("#customGroupId").val();
	var dataDate = $("#dataDate").val();
	var customersContrastAnalysisURL = $.ctx+'/ci/customersCompareAnalysisAction!customersCompareAnalysis.ai2do?customGroupContrast.customGroupId='+customGroupId+'&customGroupContrast.dataDate='+dataDate;
	$('#customGroupRelFrame').attr("src",customersContrastAnalysisURL);
	$('#customGroupRelFrame').load();
}
//标签关联分析 IFrame 动态加载，第三个加载
function loadCustomersRel() {
	//$('#customGroupRelFrame').height("550px");
	var customGroupId = $("#customGroupId").val();
	var dataDate = $("#dataDate").val();
	var customRelURL = $.ctx+'/ci/customersRelAnalysisAction!customGroupRel.ai2do?ciCustomRelModel.customGroupId='+customGroupId+'&ciCustomRelModel.dataDate='+dataDate;
	$('#customGroupRelFrame').attr("src",customRelURL).show();
	//$('#customGroupRelFrame').load();
}

//对比分析连接
function openCustomContrast(){
	if($('#customNum').val() == ""){
		window.parent.showAlert("客户群清单未创建成功","failed");
		return;
	}
	var customGroupId = $("#customGroupId").val();
	var listTableName = $("#listTableName").val();
	var customNum = $("#customNum").val();
	var dataDate = $("#dataDate").val();
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.listTableName="+listTableName;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&dataDate="+dataDate;
		param = param + "&tab=ant";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//关联分析连接
function openCustomRel() {
	if($('#customNum').val() == ""){
		window.parent.showAlert("客户群清单未创建成功","failed");
		return;
	}
	var customGroupId = $("#customGroupId").val();
	var listTableName = $("#listTableName").val();
	var customNum = $("#customNum").val();
	var dataDate = $("#dataDate").val();
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.listTableName="+listTableName;
	    param = param + "&customGroup.customNum="+customNum;
		param = param + "&dataDate="+dataDate;
		param = param + "&tab=rel";
	var url = $.ctx+"/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}
//标签微分
function openlabelDeff() {
	var customGroupId = $("#customGroupId").val();
	var customNum = $("#customNum").val();
	var dataDate = $("#dataDate").val();
	var listTableName = $("#listTableName").val();
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
	    param = param + "&customGroup.listTableName="+listTableName;
	    param = param + "&dataDate="+dataDate;
		param = param + "&tab=tag";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}

//指标微分
function openIndexDeff() {
	var customGroupId = $("#customGroupId").val();
	var customNum = $("#customNum").val();
	var dataDate = $("#dataDate").val();
	var listTableName = $("#listTableName").val();
	var param = "customGroup.customGroupId="+customGroupId;
	    param = param + "&customGroup.customNum="+customNum;
	    param = param + "&customGroup.listTableName="+listTableName;
		param = param + "&dataDate="+dataDate;
		param = param + "&tab=sa";
	var url = "${ctx }/ci/customersAnalysisAction!customerAnalysisMain.ai2do?"+param;
	window.open(url, "", "height="+$(window).height()+",width="+$(window).width()+",top=10,left=10,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
}

function addToPanel(obj,dataDate){
	parent.addToPanel(null,$("#dataDate").val(),'${customGroup.json}');
}

//显示告警信息
function showWaringInfo(){
    //TODO
    var customGroupId = $("#customGroupId").val();
    var customGroupName = $("#customGroupName").val();
    var dataDate = $('#dataDate').val();
    var actionUrl = $.ctx + "/ci/ciCustomersAlarmAction!findCustomersAlarmRecord.ai2do";
    $.ajax({
        url: actionUrl,
        type: "POST",
        data: {
            "customRelModel.customGroupId" : customGroupId,
            "customRelModel.customGroupName" : customGroupName,
            "customRelModel.dataDate" : dataDate
        },
        success: function(result){
            flag = result.success;
            if(flag){
                var levelTr = '<td><strong>告警</strong></td>';
                var thresholdTr = '<td><strong>告警阈值</strong></td>';
                var realValueTr = '<td><strong>实际值</strong></td>';
                for(i=0;i<2;i++){
                    var levelStr = result.result[i].level;
                    var levelTd = "";
                    if(levelStr == "未设置"){
                        levelTd = '<td>未设置</td>';
                    }else if(levelStr=="正常"){
                        levelTd = '<td class="green">正常</td>';
                    }else{
                        levelTd = '<td class="red">'+levelStr+'</td>';
                    }
                    levelTr = levelTr + levelTd;

                    thresholdTr = thresholdTr + '<td>'+ result.result[i].threshold + '</td>';
                    realValueTr = realValueTr + '<td  class="align-right" style="border-bottom:none">'+result.result[i].realValue +'</td>';
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

</script>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="l_d v_top">
	<!--标签口径及日期 -->
		<div class="top_box">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="40" class="v_top" style="padding-left:5px;">
						<!--客户群名称 -->
						<div class="tag_main_title">${customGroup.customGroupName}
						<c:if test="${customGroup.customNum != null}">
							   <label id="customNumLab" >(<fmt:formatNumber value="${customGroup.customNum}" type="number" pattern="###,###,###"/>)</label>
							</c:if>
						</div>
						<!--icon 根据实际情况显示 -->
						<div class="tag_main_icon">
							<span><img style="cursor:pointer;" title="客户群告警" src="${ctx}/aibi_ci/assets/themes/default/images/warning.png" class="main_icon_alarm"/></span>
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
                                        环比预警：将客户群当前统计周期的用户数与上一周期用户数进行比较，针对差值进行预警<br/>
                                        <span>环比增长量 = 客户群当前统计周期用户数 - 客户群上一周期用户数</span>
                                    </dd>
                                </dl>
                            </div>
                            <!-- 告警弹出层end -->
						</div>

					</td>
					<td width="230px" rowspan="2" class="v_top">
						<div class="wdate_box">
                       <input  type="hidden" id="dataDate" value="${customGroup.dataDate }" />
                       <input  type="hidden" id="startDate" value="${customGroup.startDate }" />
                       <input  type="hidden" id="endDate" value="${customGroup.endDate }" />
                       <input id="dataDateMax"  type="hidden" value="" />
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
					<td class="v_top" style="padding-bottom:10px;">
						<!--口径说明 -->
						<div class="tag_main_text">
							<strong>规则展示：</strong>${customGroup.prodOptRuleShow}
							${customGroup.labelOptRuleShow}${customGroup.customOptRuleShow}
							${customGroup.kpiDiffRule}
						</div>
					</td>
				</tr>
			</table>
			<!--口径的详细信息，默认隐藏，点击按钮可以展开显示 -->
			<div id="dog" style="display:none;">
				<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
					<tr>
					    <th>生成周期：</th>
						<td>${customGroup.updateCycleStr}</td>
                        <input type="hidden" id="updateCycle" value="${customGroup.updateCycle}" />
						<th width="20%">创建方式：</th>
						<td width="30%">${customGroup.createType}</td>
					</tr>
					<tr>
						<th>数据生成时间：</th>
						<td><fmt:formatDate value="${customGroup.dataTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<th>创建时间：</th>
						<td><fmt:formatDate value="${customGroup.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
					<tr>
						<th>统计日期：</th>
						<td>${dateFmt:dateFormat(customGroup.dataDate)}</td>
						<th>客户群描述：</th>
						<td>${customGroup.customGroupDesc == '' ? '-' : customGroup.customGroupDesc}</td>
					</tr>
					<tr>
						<th>导入源文件名称：</th>
						<td>${customFileRel.fileName == null ? '-' : customFileRel.fileName}</td>
						<th>导入开始/结束时间：</th>
						<td>
							<c:choose>
								<c:when test="${customFileRel.fileStartTime != null }">
									<fmt:formatDate value="${customFileRel.fileStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						    			至
						   			<fmt:formatDate value="${customFileRel.fileEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<c:if test="${isAdmin}">
					<tr>
						<th>创建人：</th>
						<td colspan="3">${customGroup.createUserName}</td>
					</tr>
					</c:if>
				</table>
			</div>
		</div>
		<div class="main_button_title">
			<div style="width:80px;float:left;">
				<a href="javascript:void(0);" id="expend" onclick="showTable()" title='点击显示详细客户群信息' class="icon_hide">展开</a>
				<a href="javascript:void(0);" id="shrink" style="display: none;" onclick="showTable()" title='点击隐藏详细客户群信息' class="icon_show">收起</a>
			</div>
			<ul id="slideIcons" class="slideIcons" style="">
				<li><a href="javascript:;" onclick="addToPanel(this);" class="icon_add"><span class="hidden_text">添加</span></a></li>
				<c:if test="${customGroup.listTableName != null}">
				<% if("true".equals(indexDifferentialMenu)){ %>
				<li><a href="javascript:void(0);" onclick="openIndexDeff();" class="icon_quota"><span>指标微分</span></a></li>
				<%};if("true".equals(customerAnalysisMenu)){%>
				<li><a href="javascript:void(0);" onclick="openlabelDeff();" class="icon_wtag"><span>标签微分</span></a></li>
				<li><a href="javascript:void(0);" onclick="openCustomRel();" class="icon_rel"><span>关联分析</span></a></li>
				<li><a href="javascript:void(0);" onclick="openCustomContrast();" class="icon_ant"><span>对比分析</span></a></li>
				<%
				  };if("true".equals(marketingMenu)){
                %>
				<li><a href="javascript:void(0);" onclick="window.parent.customSysMatch('${customGroup.customGroupId}',$('#dataDate').val(),'${customGroup.listTableName}',$('#customNum').val());" class="icon_marketing"><span class="hidden_text">营销策略匹配</span></a></li>
				<%
                   };
				%>
				</c:if>
			</ul>
			<div class="clear"></div>
		</div>
		<div class="main_bottom"></div>
		<div class="clear"></div>
		<!--趋势分析开始 -->
		<input type="hidden" id="customGroupId" value="${customGroup.customGroupId}"/>
		<input type="hidden" id="customGroupName" value="${customGroup.customGroupName}"/>
		<input type="hidden" id="listTableName" value="${customGroup.listTableName}"/>
		<input type="hidden" id="customNum" value="${customGroup.customNum}"/>
		<div class="main_box">
			<iframe name="" id="customersTrendFrame" scrolling="no"  allowtransparency="true" id=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:315px"></iframe>
		</div>
		<!--趋势分析结束 -->
		<!--构成分析开始 -->
		<div class="main_box" style="position: relative; height: 350px;">
			<!-- <div id="chars_overlay" class="chars_overlay" style="height:320px;width:100%; position:absolute; left:0; top:0;">
                <div class="loadingIcon"></div>
            </div> -->
			<iframe name="" id="customGroupFormFrame" scrolling="no"  allowtransparency="true" id=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:350px"></iframe>
		</div>
		<!--构成分析结束-->
		<!--关联对比开始 -->
		<div class="main_box" style="position: relative; height: 650px;">
			<!-- <div id="chars_overlay" class="chars_overlay" style="height:650px;width:100%; position:absolute; left:0; top:0;">
                <div class="loadingIcon"></div>
            </div> -->
			<iframe name="" id="customGroupRelFrame" scrolling="no"  allowtransparency="true" id=""  framespacing="0" border="0" frameborder="0" style="width:100%;height:625px"></iframe>
		</div>
		<!--关联对比结束-->
		<!-- 分析页不被浮动层遮住 -->
        <div style="height:40px;"></div>      
	</td>
  </tr>
</table>
</body>
</html>