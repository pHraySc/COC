<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%
String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU"); 
String marketingMenu = Configure.getInstance().getProperty("MARKETING_MENU"); 
String personMenu = Configure.getInstance().getProperty("PERSON_MENU"); 
String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
String alarmMenu = Configure.getInstance().getProperty("AlARM_MENU");
String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
String root = Configure.getInstance().getProperty("CENTER_CITYID");
int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
String isCustomVertAttr = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
%>
<c:set var="isCustomVertAttr" value="<%=isCustomVertAttr %>"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>客户群详情</title>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/default/easyui.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/aibi_ci/assets/js/jqueryEasyUI/themes/icon.css"/>
		<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/jqueryEasyUI/easyloader.js"></script>
		<script type="text/javascript">
			$(function(){
				var groupId = "${ciCustomGroupInfo.customGroupId}";
				easyloader.load('datagrid', function(){ //加载指定模块 
					$("#groupGridList").datagrid({ 
						height:328, 
					    remoteSort:true,
					    fitColumns:false,
					    rownumbers:true,
					    nowrap:true,
					    striped:true,
					    pagination:true,
					    loadMsg:'正在加载数据，请稍候',
					    url:'${ctx}/ci/customersManagerAction!findGroupInfoLists.ai2do?ciCustomGroupInfo.customGroupId='+groupId,
					    columns:[[
							{field:'listTableName',title: '清单表名',align: 'left',width: 170},  
							{field:'dataDate',title: '统计周期',align: 'right',width: 65},  
							{field:'customNum',title: '客户群规模',align: 'right',width: 80,formatter:function(value,rec){
								var customNum = rec.customNum;
								if(customNum == null || $.trim(customNum).length == 0){
									return "-";
								}
								return customNum;
							}},
							{field:'duplicateNum',title: '重复数',align: 'right',width: 70,formatter:function(value,rec){
								var duplicateNum = rec.duplicateNum;
								if(duplicateNum == null || '' == duplicateNum){
									return "-";
								}
								return duplicateNum;
							}}, 
							{field:'dataStatusStr',title: '数据状态',align: 'left',width: 69,
								formatter:function(value,rec){
									var html="";
									var dataStatusVar = rec.dataStatus;
									if(rec.createTypeId == 1 && dataStatusVar == '<%=ServiceConstants.CUSTOM_DATA_STATUS_FAILED%>'){
										/* html="<p class='query-item-total-txt-my-custom'>"+rec.dataStatusStr
											+"</p><a title='重新生成' href='javascript:void(0);' onclick='parent.regenCustomer(\"${ciCustomGroupInfo.customGroupId}\",\""+rec.createTypeId+"\")' class='query-item-total-icon'/>";
										*/
										html="<p class='query-item-total-txt-my-custom'>"+rec.dataStatusStr
											+"</p><a title='重新生成' href='javascript:void(0);' onclick='parent.regenCustomer(\""+groupId+"\",\""+rec.createTypeId+"\",\""+rec.listTableName+"\")' class='query-item-total-icon'/>";
									}else{
										html=rec.dataStatusStr;
									}
									return html;
								}
							},  
							{field:'dayLabelDate',title: '日标签数据时间',align: 'right',width: 95},  
							{field:'monthLabelDate',title: '月标签数据时间',align: 'right',width: 90},  
							{field:'dataTimeStr',title: '清单创建时间',align: 'right',width: 125},  
							{field:'opt',title:'操作',width:85,align:'center',
								formatter:function(value,rec){  
								var btn = '<a class="editcls" style="text-decoration: underline;" onclick="parent.showSqlInfo(\''+rec.listTableName+'\')" href="javascript:void(0)">查看执行详情</a>';  
								return btn;  
								}  
							}  
					  	]],
						rowStyler:function(index,row){
							return 'height:20px;';
						},
				        onLoadSuccess:function(data){
					    	$(".datagrid-view1,.datagrid-view1 > div,.datagrid-view1 table").width(45);
					    	$(".datagrid-view2").width(613);
					    	$('.editcls').linkbutton({text:'查看执行详情',plain:true});  
					    	if('${isCustomVertAttr}' != "true"){
						    	$(this).datagrid("hideColumn", 'duplicateNum');
						    	 //datagrid主体 table 的第一个td 的tr们，即列的集合             
						    	 var bodyTds = $(".datagrid-body table td:last-child").children();  
						    	 var headerTds = $(".datagrid-header-inner table tr:first-child").children();
						    	 //设置表头最后一列的宽度
							     $(headerTds.get(headerTds.length-1)).children("div").width(150);
						    	 //循环设置宽度               
						    	 bodyTds.each(function (i, obj) {  
						    		 if(bodyTds.length/2 <= i){
							    		 var bodyTd = $(bodyTds.get(i)).width(148);    
						    		 }
						    		 if(bodyTds.length == i){
							    		 headerTd.width(148);    
						    		 }
						    	 });
					    	}
				        },
				        onLoadError:function(XMLHttpRequest, textStatus, errorThrown){
				        	parent.showAlert("表格加载错误！", "failed");
				        },
				        onClickRow: function (rowIndex, rowData) {
                            $(this).datagrid('unselectRow', rowIndex);
                        }
					});
					$(".datagrid-header-rownumber").html("<span class='tableHead' >序号</span>");
					$(".datagrid-header-row span").addClass("tableHead");
					var p = $('#groupGridList').datagrid('getPager');
					$(p).pagination({  
						pageSize: 10,//每页显示的记录条数，默认为10  
						pageList: [5,10],//可以设置每页记录条数的列表  
						beforePageText: '第',//页数文本框前显示的汉字  
						afterPageText: '页    共 {pages} 页',  
						displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
					});  
				});
				 //告警弹出定位
				
			    var toffset=$(".tag_main_icon").parent("td").offset();
			    var noffset=$(".tag_main_icon .main_icon_alarm").offset();
			    if(toffset != null){
			    var alarmLeft=noffset.left-toffset.left+1;
			    $(".tag_main_icon .alarm_detail").css({top:toffset.top + 28,left:toffset.left+5});
			    $(".tag_main_icon .alarm_detail").css("background-position",""+alarmLeft+" 0px"); 
			   
			    //客户群告警帮助提示
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
			    }
			});
			
			//显示告警信息
			function showWaringInfo(){
			    //TODO
			    var customGroupId = "${ciCustomGroupInfo.customGroupId}"
			    var actionUrl = $.ctx + "/ci/ciCustomersAlarmAction!findCustomersAlarmRecord.ai2do";
			    $.ajax({
			        url: actionUrl,
			        type: "POST",
			        data: {
			            "customRelModel.customGroupId" : customGroupId
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
			                        levelTd = '<td class="normal">未设置</td>';
			                    }else if(levelStr=="正常"){
			                        levelTd = '<td class="green normal">正常</td>';
			                    }else{
			                        levelTd = '<td class="red normal">'+levelStr+'</td>';
			                    }
			                    levelTr = levelTr + levelTd;

			                    thresholdTr = thresholdTr + '<td class="normal">'+ result.result[i].threshold + '</td>';
			                    realValueTr = realValueTr + '<td  class="align-right normal" style="border-bottom:none">'+result.result[i].realValue +'</td>';
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
	<style>
		.datagrid-row .datagrid-header-rownumber,.datagrid-header-row .datagrid-header-rownumber,.datagrid-row .datagrid-cell-rownumber{width:auto;}
		.datagrid .pagination .pagination-num{width:3.5em;}
	</style>
	<body>
	<div>
	 <div class="tableDottedWrap">
	                        <input  type="hidden" id="dataDate" value="${customGroup.dataDate }" />
            	<table>
                    	<tbody>
                    	<tr class="firstTr">
                        	<td colspan="4" align="left" title="客户群名称" >&nbsp;${ciCustomGroupInfo.customGroupName}
                        	   <c:if test= "${ciCustomGroupInfo.isAlarm == true}">
                        <!--icon 根据实际情况显示 -->
						<div class="tag_main_icon">
							<span><img style="cursor:pointer;width: 15px;"  title="客户群告警" src="${ctx}/aibi_ci/assets/themes/default/images/warning.png" class="main_icon_alarm"/></span>
                            <!-- 告警弹出层start -->
                            <div class="slideDown alarm_detail">
                                <dl>
                                    <dt><span class="close"></span>告警详情</dt>
                                    <dd>
                                        <table width="100%" class="commonTable mainTable" cellpadding="0" cellspacing="0" id="alarmDetailTable">
                                            <tr class="even">
                                                <th width="78px">&nbsp;</th>
                                                <th width="110px">基础</th>
                                                <th width="110px">环比<img src="${ctx}/aibi_ci/assets/themes/default/images/ico	n_help.png" class="icon_relative_help"/></th>
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
                                                                                                           环比增长量 = 客户群当前统计周期用户数 - 客户群上一周期用户数
                                    </dd>
                                </dl>
                            </div>
                            <!-- 告警弹出层end -->
						</div>
                        </c:if>	
                        	</td>
                    	</tr>
                    	<tr>
                    	  <th width="25%" title="编号">${ciCustomGroupInfo.customGroupId}</th>
                    	  <th width="25%" title="最新修改时间">
						   <c:choose>
				       	    <c:when test="${ciCustomGroupInfo.newModifyTime == null}">
				       	   		暂无内容
				       	   	</c:when>
				       	   	<c:otherwise>
				       	   		<fmt:formatDate value="${ciCustomGroupInfo.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				       	   	</c:otherwise>
				       	   </c:choose>
				       	  </th>
                    	  <th width="25%" title="创建人">${ciCustomGroupInfo.createUserName}</th>
                          <th width="25%" title="创建方式">${ciCustomGroupInfo.createType}
                        </tr>
                    	<tr>
                    	  <th width="25%" title="生成周期">
						    <c:if test="${ciCustomGroupInfo.updateCycle == 1}">一次性</c:if>
				            <c:if test="${ciCustomGroupInfo.updateCycle == 2}">月周期</c:if>
				            <c:if test="${ciCustomGroupInfo.updateCycle == 3}">日周期</c:if>
				            <c:if test="${ciCustomGroupInfo.updateCycle == 4}">无</c:if>
            			  </th>
                    	  <th width="25%" title="客户群分类"><c:if test="${empty ciCustomGroupInfo.customSceneNames}">-</c:if>${ciCustomGroupInfo.customSceneNames }</th>
                    	  <th width="25%" title="是否共享">
							<c:if test="${ciCustomGroupInfo.isPrivate == 0}">共享</c:if>
							<c:if test="${ciCustomGroupInfo.isPrivate == 1}">非共享</c:if>
							<c:if test="${empty ciCustomGroupInfo.isPrivate }">-</c:if>
						  </th>
                          <th width="25%" title="地市" >${ciCustomGroupInfo.createCityName }</th></tr>
                    	<tr class="solid">
                    	  <c:if test="${ciCustomGroupInfo.isAlarm == false}">
                        	<td colspan="4"  align="left">&nbsp;描述：${ciCustomGroupInfo.customGroupDesc}
                        	</td>
                          </c:if>
                        	<c:if test="${ciCustomGroupInfo.isAlarm == true}">
                        	<td colspan="3"  align="left">&nbsp;描述：${ciCustomGroupInfo.customGroupDesc}</td>
                        	<td align="center">
                        	
                        	</td>
                        	</c:if>
                    	</tr>
                    	<tr>
                        	<td colspan="4" align="left">
                        	<div title="${ciCustomGroupInfo.prodOptRuleShow}${ciCustomGroupInfo.labelOptRuleShow}${ciCustomGroupInfo.customOptRuleShow}${ciCustomGroupInfo.kpiDiffRule}" class="strLenLimit width860" >&nbsp;规则：${ciCustomGroupInfo.prodOptRuleShow}${ciCustomGroupInfo.labelOptRuleShow}${ciCustomGroupInfo.customOptRuleShow}${ciCustomGroupInfo.kpiDiffRule}
                        	</div>
                        	</td>
                    	</tr>
                    	<tr >
                        	<td colspan="4" title="标签统计日期" align="left">
							<c:if test="${ciCustomGroupInfo.monthLabelDate != null && ciCustomGroupInfo.monthLabelDate != ''}">
							<label> &nbsp;数据月：</label>${ciCustomGroupInfo.monthLabelDate}&nbsp;&nbsp;
							</c:if>
						    <c:if test="${ciCustomGroupInfo.dayLabelDate != null && ciCustomGroupInfo.dayLabelDate != ''}">
							<label> &nbsp;数据日：</label>${ciCustomGroupInfo.dayLabelDate}
							</c:if>
							<c:if test="${(ciCustomGroupInfo.dayLabelDate == null || ciCustomGroupInfo.dayLabelDate == '')  && (ciCustomGroupInfo.monthLabelDate == null || ciCustomGroupInfo.monthLabelDate == '')}">
							<label> &nbsp;数据日期：</label>${ciCustomGroupInfo.dataDateStr}
							</c:if>
                        	</td>
                    	</tr>
                	
            	        </tbody>
            	</table>
    	</div>
		<div id="customGroupList">
		<table id="groupGridList"></table>
		</div>
	</div>
	
	</body>
</html>

