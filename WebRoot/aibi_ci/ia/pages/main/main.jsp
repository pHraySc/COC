<%@page import="com.ibm.db2.jcc.am.s"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.asiainfo.biframe.utils.config.Configure" %>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="customGroupId" value="${customGroupId}"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智能分析</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<link href="${ctx}/aibi_ci/ia/assets/styles/main_cia.css" rel="stylesheet" />  
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/common.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/main/mainPage.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/main/dialogSetting.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/main/filterSetting.js"></script> 
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/jQeryUIPlug-in.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/ciaScriptEntrance.js"></script> 
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/jQuery.contextMenu.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/echarts/echarts-all.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/echarts/echarts-theme.js"></script>
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/ciacharts.js"></script> 
										   
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/jqGrid/jquery.jqGrid.min.js"></script> 
<script type="text/javascript" src="${ctx}/aibi_ci/ia/assets/scripts/jqGrid/grid.locale-cn.js"></script> 
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/aibi_ci/ia/assets/scripts/jqGrid/ui.jqgrid.css" />
</head>
<body> 
 <jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
    <div class="mainBody">
        <!-- search -->
        <jsp:include page="/aibi_ci/index_top.jsp" ></jsp:include>
        <div id="mainContent"></div>
    </div>
    <div class="clear"></div>
	<div class="contentLayout">
		<div class="contentInner">
			<div class="cia-location ">
				<ul class="clearfix cb location-ul">
					<li class="fleft location-item">
						<h4 class="location-title location-item"> 4G飞享套餐推荐用户</h4>   
					</li>
					<li class="fleft location-item">
						<i class="cia-icons fleft cia-dividing-line"></i>
					</li>
					<li class="fleft location-item">
						<div class="location-item clearfix cb">
							<div class="fleft location-item cia-item-text">请选择清单：</div>
							<div class="fleft cia-item-select" id="">
								<select name="groupList" id ="group-list" class="hidden" >
									<option value="">请选择清单</option>  
								</select>
							</div>
						</div>
					</li>
					<li class="fleft location-item">
						<div class="fleft location-item cia-item-text">用户数：</div>
						<div class="fleft location-item cia-item-num"><span class="red-num" id="customNum"></span></div>
					</li>	
					<li class="fright location-item">
						<button type="button" class="cia-show-detail cursor" id="queryGroupListInfo">查看详情</button> 
					</li>
				</ul>
			</div>
			<div class="content-cia clearfix cb">
				<div class="left-menus fleft">
					<div class="left-menu-header tl">分析属性</div>
					<div class="left-menu-content">
						<div class="menu-groups">
							<h3 class="menu-groups-header tl cursor">
								<span class="fleft menu-groups-open cia-icons"></span>
								<span class="fleft">客户群自带属性</span>
							</h3>
							<ul class="menu-groups-ul" id="customAttr">
					
							</ul>
						</div>
						<div class="menu-groups">
							<h3 class="menu-groups-header tl cursor">
								<span class="fleft menu-groups-open cia-icons"></span>
								<span class="fleft">通用属性</span>
							</h3>
							<ul class="menu-groups-ul"	id="publicAttr">
								
							</ul>
						</div>
					
					</div>
				
				</div>
				<div class="main-cia fleft">
					<div class="main-cia-inner">
						<div class="main-cia-content" id="ciaWorkBook">
							
						</div>
						<div class="main-cia-footer">
							<ul class="of clearfix">
	
								<li class="fleft cia-tab-item cursor" id="createWorkSheet">
									<div class="fleft tab-left-text tl">
										<i class="fleft add-job-table cia-icons"></i>
										创建工作表 
										</div>
									<div class="fleft tab-left-bg"></div>
								</li>  	
								<li class="fleft cia-tab-item cursor">
									<div class="fleft tab-left-text tc">
										<i class="fleft add-report-table cia-icons"></i>
										创建报告
									</div>
									<div class="fleft tab-left-bg-last"></div>
								</li>  
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
