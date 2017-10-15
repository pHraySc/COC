<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
%>
<html>
	<head>
		<%@ include file="../../html_include.jsp"%>
		<title></title>
		<%
			//这里放css和js脚本的引用。引用在assets.jsp里面定义的变量地址
		%>
		<style type="text/css">
			#secenId {width:310px;}
		</style>
		<script type="text/javascript" src="<%=request.getContextPath()%>/aibi_chart/chartswf/FusionCharts.js"></script>
		<script type="text/javascript" language="javascript">
		var noData = '${noDataToDisplay}';
	
		var chartParam = "?ChartNoDataText="+noData+"&InvalidXMLText="+noData+"&LoadDataErrorText="+noData;
		$(document).ready(function(){
			window.parent.scroll(0,0);
			$("#isAutoMatch").prop("checked",false);
			saveUserRelLabel();
			$("#createDialog").dialog({
				autoOpen: false,
				resizable:false,
				height: 355,
				width: 660,
				title: "保存客户群",
				modal: true,
				position: [350,180],
				close:function(){
					//$("#createFrame").attr("src", "");
				}
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

		//获取关联分析结果图
		function saveUserRelLabel(){
		    var mainLabelId = $('#mainLabelId').val();
	    	var dataDate = $('#dataDate').val();
	    	var mainLabelUserNum = $('#mainLabelUserNum').val();
	    	var relLabelIds = $('#relLabelIds').val();
	    	var overlapUserNumStr = $('#overlapUserNumStr').val();
			var param = 'ciLabelRelModel.mainLabelId='+mainLabelId;
				param += '&ciLabelRelModel.dataDate='+dataDate;
				param += '&ciLabelRelModel.mainLabelUserNum='+mainLabelUserNum;
				param += '&ciLabelRelModel.relLabelIds='+relLabelIds;
				param += '&ciLabelRelModel.overlapUserNumStr='+overlapUserNumStr;

			var chart_url = escape($.ctx+'/ci/ciLabelRelAnalysisAction!saveUserRelLabel.ai2do?'+param);

		    var bar = new FusionCharts($.ctx+"/aibi_ci/assets/flash/DragNode.swf"+chartParam, "",$('#labelRelDiv').width(), $('#labelRelDiv').height(), "0", "0");
		    bar.addParam("wmode","Opaque");
		    bar.setDataURL(chart_url);
		    bar.render("labelRelDiv");		   
		}

		//将主标签与关联标签的关系保存为客户群
		function toSaveCustom(mainLabelId,relLabelId,overlapUserNum) {
			$('#customGroupName').val('');
			$('#customGroupDesc').text('');
			$('#mainLabelId').val(mainLabelId);
			$('#relLabelId').val(relLabelId);
			$('#overlapUserNum').val(overlapUserNum);
			openCreate();
		}

		function saveCustom() {
			
			var sname = $('#secenId').val();
			if($.trim(sname) == "") {
				$("#snameTip").empty();
				$("#snameTip").show().append("请选择客户群分类");
				return false;
			}
			
			var customGroupName = $('#customGroupName').val();
			var dataDate = $('#dataDate').val();
			var mainLabelId = $('#mainLabelId').val();
			var relLabelId = $('#relLabelId').val();
			var overlapUserNum = $('#overlapUserNum').val();
			var customGroupDesc = $('#customGroupDesc').text();
			var productAutoMacthFlag = 0;
			if($("#isAutoMatch").prop("checked")==true) {
				productAutoMacthFlag = 1;
			}
			if(customGroupName.Trim() == '' || customGroupName.Trim() == "请输入客户群名称") {
				$('#tishi_name').html("客户群名称不能为空！");
				$('#tishi_name').show();
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
                        	
    						url:  $.ctx+"/ci/ciLabelRelAnalysisAction!saveRelLabelAsCustomGroup.ai2do",	
    						type: "POST",
    						data:{
    							"ciLabelRelModel.mainLabelId":mainLabelId,
    							"ciLabelRelModel.dataDate":dataDate,
    							"ciLabelRelModel.relLabelId":relLabelId,
    							"ciCustomGroupInfo.dataDate":dataDate,
    							"ciCustomGroupInfo.customGroupName":customGroupName,
    							"ciCustomGroupInfo.customGroupDesc":customGroupDesc,
    							"ciCustomGroupInfo.productAutoMacthFlag":productAutoMacthFlag,
    							"ciCustomGroupInfo.sceneId":sname,
    							"ciCustomGroupInfo.customNum":overlapUserNum
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
    							$("#createDialog").dialog("close");
    							
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

		function editLabelRel() {
		    var mainLabelId = $('#mainLabelId').val();
	    	var dataDate = $('#dataDate').val();
	    	var mainLabelUserNum = $('#mainLabelUserNum').val();
	    	var relLabelIds = $('#relLabelIds').val();
	    	var overlapUserNumStr = $('#overlapUserNumStr').val();
	    	
			var param = "?ciLabelRelModel.mainLabelId=" + mainLabelId;
			param = param + "&ciLabelRelModel.mainLabelUserNum=" + mainLabelUserNum;
			param = param + "&ciLabelRelModel.dataDate=" + dataDate;
			param = param + "&ciLabelRelModel.relLabelIds=" + relLabelIds;	
			param = param + "&ciLabelRelModel.overlapUserNumStr=" + overlapUserNumStr;
			var url = $.ctx+'/ci/ciLabelRelAnalysisAction!toLabelRelAnalysis.ai2do'+param;
			$(window.parent.document).find("iframe").each(function(index){
				if(index == 0) {
					$(this).attr("src",url);
				}
			});
		}


		//保存客户群弹出层
		function openCreate(){
			$("#createDialog").dialog("close");

			$("#createDialog").dialog("open");
		}
		//关闭创建客户群弹出层
		function closeCreate(){
			$("#createDialog").dialog("close");
		}

		//打开保存成功弹出层
		function openSuccess(customerName, templateName){
			customerName = encodeURIComponent(encodeURIComponent(customerName));
			templateName = encodeURIComponent(encodeURIComponent(templateName));
			$("#successDialog").dialog("close");
			var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="+customerName+"&templateName="+templateName;
			$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
		}

		function shortThis(obj){
			var oldValue = $(obj).val();
			if(oldValue.length > 170 ){
				oldValue = oldValue.substr(0,170);
				$(obj).val(oldValue);
			}
		}
		$(function(){
			var myHeight = $("#labelRelDiv").height();
			$(window.parent.document).find("#Frame0").height(myHeight+40);
		})
		</script>
		<style>
		
		</style>
	</head>
	<body>
		<input id="relLabelId" type="hidden" value=""/>
		<input id="mainLabelId" type="hidden" value="${ciLabelRelModel.mainLabelId}"/>
		<input id="dataDate" type="hidden" value="${ciLabelRelModel.dataDate}"/>
		<input id="mainLabelUserNum" type="hidden" value="${ciLabelRelModel.mainLabelUserNum}"/>
		<input id="relLabelIds" type="hidden" value="${ciLabelRelModel.relLabelIds}"/>
		<input id="overlapUserNumStr" type="hidden" value="${ciLabelRelModel.overlapUserNumStr}"/>
		<input id="overlapUserNum" type="hidden"/>
		<div></div>
		<div class="main_con_box">
			<!--标题行开始 -->
			<div align="right" style="padding-top: 5px;">
				数据时间：${dateFmt:dateFormat(ciLabelRelModel.dataDate)} &nbsp;&nbsp;
				<a href="javascript:void(0);" onClick="editLabelRel();" style="margin-right: 10px">配置关联图谱</a>
			</div>
			<div id="labelRelDiv" style="height:800px;">
				
			</div>
			<div id="createDialog" style="display:none;">
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
			<div id="successDialog" style="display:none;">
				<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:265px"></iframe>
			</div>
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