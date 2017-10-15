<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_SCENE" value="<%=CommonConstants.TABLE_DIM_SCENE%>"></c:set>
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
<c:set var="root" value="<%=root%>"></c:set>
<c:set var="isNotContainLocalList" value="<%=isNotContainLocalList %>"></c:set>
<c:set var="isCustomVertAttr" value="<%=isCustomVertAttr %>"></c:set>
<script type="text/javascript">
//查询以选择条件 
 $(".selectedItemBox").hover(function(){
	 $(this).addClass("selectedItemBoxHover");
 },function(){
	 $(this).removeClass("selectedItemBoxHover");
 })
 //收藏小盒子样式的改变
 $(".collectStar").hover(function(){
	 var customId = $(this).attr("customId");
	 var isAttention = $("#isAttention_"+customId).val();
	 if(isAttention == 'true'){//已关注的客户群
		 $("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
	 }else{//未关注的客户群
		 $("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
	 }
 },function(){
	 var customId = $(this).attr("customId");
	 var isAttention = $("#isAttention_"+customId).val();
	 if(isAttention == 'true'){//已关注的客户群
		 $("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite.png");
	 }else{//未关注的客户群
		 $("#attentionImg_"+customId).attr("src","${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png");
	 }
 });
function showChart(chartStr,customId){
	var allStr = chartStr;
    if(null != allStr || "" != allStr){
	    var array = allStr.split("|");
	    var categoriesVar = eval('(' + array[0] + ')');
	    if(categoriesVar.length > 6){
	    	categoriesVar.splice(0,categoriesVar.length-6);
	    }
	    var dataVar = eval('(' + array[1] + ')');
	    if(dataVar.length > 6){
	    	dataVar.splice(0,dataVar.length-6);
	    }
	    $('#chart'+customId).highcharts({
	            chart: {
	                type: 'areaspline',
	                spacingTop: 0,
	                spacingRight: -7,
	        		spacingBottom: 0,
	        		spacingLeft: -5,
	                plotBorderWidth: null,
	                plotBackgroundColor: null
	            },
	            title: {
	                text: ''
	            },
	            exporting: {
	                enabled: false  //设置导出按钮不可用        
	            },
	            legend: {
	                layout: 'vertical',
	                align: 'left',
	                verticalAlign: 'top',
	                x: 150,
	                y: 100,
	                floating: true,
	                borderWidth: 0,
	                backgroundColor: '#FFFFFF',
	                enabled: false
	            },
	            xAxis: {
	                categories: categoriesVar,
	                tickmarkPlacement: 'on',//between
	                tickLength: 0,
	                lineWidth: 0,
	                labels:{
	                    formatter: function() {//去掉X轴的刻度显示  
	                        return '';
	                    },
	                        enabled: false
	                     }
	            },
	            yAxis: {
	                title: {
	                    text: ''
	                },
	               
	                gridLineWidth: null,
	                labels:{
	                    formatter: function() {//去掉Y轴的刻度显示  
	                        return '';
	                    },
	                        enabled: false
	                     }
	            },
	            tooltip: {
	                enabled: false,
	                shared: true,
	                valueSuffix: ''
	            },
	            credits: {
	                enabled: false
	            },
	            plotOptions: {
	                areaspline: {
	                    fillOpacity: 1,
	                    dataLabels:{
	                        enabled: false  //在数据点上显示对应的数据值
	                    },
	                    enableMouseTracking: true //取消鼠标滑向触发提示框
	                },
	                series: {
	                   lineColor: '#7DB708'           
	                }
	            },
	            series: [{
	            	name: '用户数',
	                data: dataVar,
	                color:'#F4F8EA',
	                marker: {
	                    enabled: true,
	                    fillColor: '#7DB708',
	                    lineColor: '#FFFFFF',
	                    lineWidth: 2,
	                    radius: 4.5,
	                    symbol: null
	                 }
	            }]
	        });
    	}
}
</script>
<c:forEach items="${ciCustomGroupInfoList}" var="custom">
<div class="query-item-char-box">
	<div class="query-item-char-top"> 
		<ol class="hotState">
			<c:if test="${custom.isSysRecom!=null && custom.isSysRecom==1 }"><li><span class="organ"/></li></c:if>
			<c:if test="${custom.isHotCustom=='true' }"><li><span class="hot"/></li></c:if>
			<li <c:if test="${custom.isAttention=='true' }">style="display: block"</c:if><c:if test="${custom.isAttention=='false' }">style="display: none"</c:if> id="isAttentionShow_${custom.customGroupId}"><span  class="hideFont"/></li>
		</ol>                                      
		<c:if test="${custom.isPrivate == 0}">
		<h4 style="cursor: pointer;" <c:choose> <c:when test='${custom.createCityId == root || custom.createCityId == cityId || custom.isContainLocalList == isNotContainLocalList }'><c:choose><c:when test="${custom.updateCycle == 4 ||(custom.createTypeId == 7  && custom.dataStatus == 3)||(custom.createTypeId == 12 && custom.dataStatus == 3) || (custom.createTypeId != 7 && custom.createTypeId != 12 && (custom.customNum == null && custom.dataStatus == 0) == false)}">ondblclick="customValidate(event,this,2);" element="${custom.customGroupId}"</c:when><c:otherwise>
		isPrivate="${custom.isPrivate }" createUserId="${custom.createUserId }" createCityId="${custom.createCityId }" isContainLocalList="${custom.isContainLocalList }" ondblclick="failAddShoppingCar(this);"</c:otherwise></c:choose></c:when><c:otherwise>
		isPrivate="${custom.isPrivate }" createUserId="${custom.createUserId }" createCityId="${custom.createCityId }" isContainLocalList="${custom.isContainLocalList }" ondblclick="failAddShoppingCar(this);"</c:otherwise></c:choose>>
		${custom.customGroupName}</h4>
		</c:if>
		<c:if test="${custom.isPrivate == 1}">
		<h4 style="cursor: pointer;"  <c:choose><c:when test='${custom.createUserId == userId}'><c:choose><c:when test="${custom.updateCycle == 4 ||(custom.createTypeId == 7  && custom.dataStatus == 3)||(custom.createTypeId == 12 && custom.dataStatus == 3) || (custom.createTypeId != 7 && custom.createTypeId != 12 && (custom.customNum == null && custom.dataStatus == 0) == false)}">ondblclick="customValidate(event,this,2);" element="${custom.customGroupId}"</c:when><c:otherwise>
		isPrivate="${custom.isPrivate }" createUserId="${custom.createUserId }" createCityId="${custom.createCityId }" isContainLocalList="${custom.isContainLocalList }" ondblclick="failAddShoppingCar(this);"</c:otherwise></c:choose></c:when><c:otherwise>
		isPrivate="${custom.isPrivate }" createUserId="${custom.createUserId }" createCityId="${custom.createCityId }" isContainLocalList="${custom.isContainLocalList }" ondblclick="failAddShoppingCar(this);"</c:otherwise></c:choose>>
		${custom.customGroupName}</h4>
		</c:if>
		<input type="hidden" id="isAttention_${custom.customGroupId}" value="${custom.isAttention}" />
		<c:if  test="${custom.isAttention == 'false'}">
		   &nbsp;<span class="collectStar" isAttention="${custom.isAttention}" customId="${custom.customGroupId}"><img style="cursor:pointer" title="点击可收藏此客户群" onclick="attentionOperCustom('${custom.customGroupId}')" id="attentionImg_${custom.customGroupId }" src="${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png" /></span>
        </c:if>
        <c:if  test="${custom.isAttention == 'true'}">
		   &nbsp;<span class="collectStar" isAttention="${custom.isAttention}" customId="${custom.customGroupId}"><img style="cursor:pointer" title="点击可取消收藏此客户群" onclick="attentionOperCustom('${custom.customGroupId}')") id="attentionImg_${custom.customGroupId }" src="${ctx}/aibi_ci/assets/themes/default/images/favorite.png" /></span>
        </c:if>
        <c:if test="${custom.dataStatus != 0 && custom.isHasList == 1 && custom.customNum != null}">
        <c:if test="${custom.customNum >= 0}"><p>客户群为<a style="text-decoration: underline;" href="javascript:void(0)" <c:if test="${custom.createUserId != userId }">onclick="notShowCustomNum()"</c:if><c:if test="${custom.createUserId == userId }">onclick="showCustomerTrend('${custom.customGroupId}')"</c:if>><fmt:formatNumber type="number" value="${custom.customNum}"  pattern="###,###,###" /></a>人</p></c:if>
        </c:if>
        <a href="javascript:void(0)" class="query-item-total-state" >
			  <p style="cursor: pointer;" title="数据状态" class="query-item-total-txt">
				  ${custom.dataStatusStr}
			  </p>
			  <c:if test="${(custom.isLabelOffline == null ||  custom.isLabelOffline == 0) && custom.dataStatus == 0 && custom.createTypeId == 1 && custom.createUserId == userId}">
			  	<a title="重新生成" href="javascript:void(0);" onclick="regenCustomer('${custom.customGroupId}','${custom.createTypeId}','${custom.dataDate }')" class="query-item-total-icon ">
			  	</a>
			  </c:if>
		</a>
		<div class="fright resultItemActive" onclick="showOperCustomList(event,this,'${custom.customGroupId}')" isPrivate="${custom.isPrivate }" createUserId="${custom.createUserId }" createCityId="${custom.createCityId }" isContainLocalList="${custom.isContainLocalList }"><span class="dsts">操作</span>
		<!-- 不含清单且含有下线标签的客户群 无操作权限 -->
		<c:if test="${!(custom.isLabelOffline == 1 && custom.updateCycle == 4)}">
	    <c:if test="${isAdmin||(custom.createCityId==root) || ( custom.createCityId == cityId) || (custom.createUserId == userId) || custom.isPrivate == 0}">
	   	<div class="resultItemActiveList" id="${custom.customGroupId }">
			<ol>
			<c:if test="${custom.dataStatus == 0 || custom.dataStatus == 3}">
				<c:if test="${custom.dataStatus == 3 && custom.isHasList == 1}">
					<c:if test="${cityId == root || custom.createCityId == root || custom.createCityId == cityId }">
						<li><a href="javascript:queryGroupList('${custom.customGroupId}','${custom.dataDate}');" >清单预览</a></li>
					</c:if>
					<c:if test="${custom.listTableName != null}">
				    <% if("true".equals(indexDifferentialMenu)){ %>
                    	<li style="display:none"><a href="javascript:openIndexDeff('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}')">指标微分</a></li>
				    <% }
				   	   if("true".equals(customerAnalysisMenu)){ %>
					<!-- <li style="display:none"><a href="javascript:openlabelDeff('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}')">标签微分</a></li>
                         <li style="display:none"><a href="javascript:openCustomRel('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">关联分析</a></li>
                         <li style="display:none"> <a href="javascript:openCustomContrast('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">对比分析</a></li> -->
                    <% }
				   	   if("true".equals(marketingMenu)){ %>
                    	<li><a href="javascript:customSysMatch('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">营销策略匹配</a></li>
                    <% } %>
		            </c:if>
			  	</c:if>
			</c:if>
		    <!-- 创建中、待创建、成功含清单的客户群，可以进行清单下载： 客户数不为null-->
		    <c:if test="${custom.dataStatus != 0 && custom.isHasList == 1 && custom.customNum != null}">
				<c:if test="${custom.createUserId == userId }">
					<li><a href="javascript:showCustomerTrend('${custom.customGroupId}');" >清单下载</a></li>
				</c:if>
			</c:if>
			<c:if test="${custom.dataStatus == 0 || custom.dataStatus == 3}">
		        <c:if test="${havePushFun == 1}">
				<c:if test="${custom.dataStatus == 3 && custom.isHasList == 1}">
                      	<c:if test="${custom.createUserId == userId }">
                     	   	<li><a href="javascript:pushCustomerGroupSingle('${custom.customGroupId}','${custom.updateCycle}', '${custom.isPush}', '${custom.duplicateNum}');">推送设置</a></li>
                      	</c:if>
				</c:if>
				</c:if>
				<c:if test="${(custom.isLabelOffline == null ||  custom.isLabelOffline == 0)&& custom.isHasList == 1 }">
				<c:if test="${custom.failedListCount >= 1  && custom.createUserId == userId && custom.createTypeId == 1}">
				<li><a href="javascript:regenCustomer('${custom.customGroupId}','${custom.createTypeId}');" >统计失败重新生成</a></li>
				</c:if>
				
				<c:if test="${custom.failedListCount < 1  && custom.createTypeId == 1 && custom.updateCycle==1&&custom.isPrivate != 0 && custom.createUserId == userId}">
					<li><a href="javascript:oneTimeRegenCustomer('${custom.customGroupId}','${custom.createTypeId}');" >重新生成</a></li>
				</c:if>
				</c:if>
				<c:if test="${custom.isPrivate != 0}">
					<c:if test="${custom.dataStatus == 3 && custom.createUserId == userId}">
					<li id="share_${custom.customGroupId}" c_name="${custom.customGroupName}" c_cityId="${custom.createCityId}" c_userId="${custom.createUserId}">
						<a href="javascript:shareOne('${custom.customGroupId}');">共享</a>
					</li>
					</c:if>
				</c:if>
				<c:if test="${custom.isPrivate == 0}">
				<c:if test="${custom.createUserId == userId && custom.isSysRecom == 0}">
				<li id="share_${custom.customGroupId}">
					<a href="javascript:cancelShareOne('${custom.customGroupId}');">取消共享</a>
				</li>
				</c:if>
				</c:if>
				<c:if test="${custom.dataStatus == 3}">
					<c:if test="${(root == cityId) || (root != cityId && custom.createCityId == cityId)}">
						<c:if test="${isAdmin && custom.isPrivate == 0 && (empty custom.isSysRecom || custom.isSysRecom == 0)}">
						<li><a href="javascript:void(0);" customId="${custom.customGroupId}" onclick="changeRecomCustom(this)">推荐</a></li>
						</c:if>
					</c:if>
				</c:if>
				<c:if test="${(root == cityId) || (root != cityId && custom.createCityId == cityId)}">
					<c:if test="${isAdmin && custom.isPrivate == 0 && (!empty custom.isSysRecom && custom.isSysRecom == 1)}">
						<li><a href="javascript:void(0);" customId="${custom.customGroupId}" onclick="changeRecomCustom(this)">取消推荐</a></li>
					</c:if>
				</c:if>
				<!--  
				<c:if test="${custom.isPrivate != 0}">
				<c:if test="${isAdmin || custom.createUserId == userId}">
                      	<li><a href="javascript:editCustomer(${custom.createTypeId},${custom.updateCycle},'${custom.customGroupId}');">修改</a></li>
					<li><a href="javascript:delOne('${custom.customGroupId}');">删除</a></li>
				</c:if>
				</c:if> 
				-->
				<c:if test="${custom.dataStatus == 3}">
					<% if ("true".equalsIgnoreCase(templateMenu)) { %>
					<c:if test="${custom.createTypeId==1}">
						<li><a href="javascript:createTemplate('${custom.customGroupId}','${custom.customGroupName}','${custom.sceneId}');" >提取模板</a></li>
					</c:if>
					<% } %>
				</c:if>
			</c:if>
			<!-- 包含下线或者停用标签的客户群不能加入购物车 -->
				<c:if test="${custom.isPrivate == 0}">
					<c:if test="${custom.createCityId == root || custom.createCityId == cityId || custom.isContainLocalList == isNotContainLocalList }">
						<!-- 包含清单客户群：1、客户群数为空  && 2、数据状态为失败 不能加入购物车 -->
				        <!-- 模板客户群可以加入购物车 -->
				        <c:if test="${custom.updateCycle == 4 ||(custom.createTypeId == 7  && custom.dataStatus == 3)||(custom.createTypeId == 12 && custom.dataStatus == 3) || (custom.createTypeId != 7 && custom.createTypeId != 12 && (custom.customNum == null && custom.dataStatus == 0) == false)}">
							<li><a href="javascript:customValidate2('${custom.customGroupId}','2','0');">添加到收纳篮</a></li>
				        </c:if>
					</c:if>
				</c:if>
				<c:if test="${custom.isPrivate == 1}">
					<c:if test="${custom.createUserId == userId }">
						<!-- 包含清单客户群：1、客户群数为空  && 2、数据状态为失败 不能加入购物车 -->
				        <!-- 模板客户群可以加入购物车 -->
				        <c:if test="${custom.updateCycle == 4 ||(custom.createTypeId == 7  && custom.dataStatus == 3)||(custom.createTypeId == 12 && custom.dataStatus == 3) || (custom.createTypeId != 7 && custom.createTypeId != 12 && (custom.customNum == null && custom.dataStatus == 0) == false)}">
							<li><a href="javascript:customValidate2('${custom.customGroupId}','2','0');">添加到收纳篮</a></li>
				        </c:if>
					</c:if>
				</c:if>
			</ol>
		</div>
		</c:if>
		</c:if>
		</div>
   </div>
   <div class="query-item-char-date">
   <ol>
       	<li><label>编号：</label><span>${custom.customGroupId}</span></li><li class="item-seg-line"></li>
	   	<li><label>最新修改时间：</label>
	   	<span><c:choose>
		<c:when test="${custom.newModifyTime == null}">
        	暂无内容
        </c:when>
        <c:otherwise>
        	<fmt:formatDate value="${custom.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </c:otherwise>
		</c:choose></span>
		</li>
		<li class="item-seg-line"></li>
        <li><span title="创建人" style="cursor: default;">${custom.createUserName}</span></li>
        <li class="item-seg-line"></li>
	   	<li><span title="创建方式" style="cursor: default;">${custom.createType}</span></li>
	   	<li class="item-seg-line"></li>
	   		<c:if test="${custom.updateCycle != 4}">
	   	<li>
	   		<span title="生成周期" style="cursor: default;">
	   		<c:if test="${custom.updateCycle == 1}">一次性</c:if>
        	<c:if test="${custom.updateCycle == 2}">月周期</c:if>
        	<c:if test="${custom.updateCycle == 3}">日周期</c:if>
        	</span>
        </li>
        <li class="item-seg-line"></li></c:if>
        <li><span title="客户群分类" style="cursor: default;">${custom.customSceneNames}</span></li>
        <li class="item-seg-line"></li>
	   	<li>
	   		<span title="是否共享" style="cursor: default;">
	    	<c:if test="${custom.isPrivate == 0}">共享</c:if>
			<c:if test="${custom.isPrivate == 1}">非共享</c:if>
			<c:if test="${empty custom.isPrivate }">-</c:if>
	   		</span>
	   	</li>
	   	<li class="item-seg-line"></li>
	   	<li><span title="地市" style="cursor: default;">${custom.createCityName }</span></li>
	   	<c:if test="${isCustomVertAttr == 'true' && custom.duplicateNum > 0 }">
		   <li class="item-seg-line"></li>
		   <li><span title="重复用户数" style="cursor: default;">${custom.duplicateNum }</span></li>
	   	</c:if>
	   	<% if ("true".equals(marketingMenu)) { %>
	   	<li class="item-seg-line"></li>
	   	<li>
	   		<span title="系统匹配" style="cursor: default;">
	    	<c:if test="${custom.sysMatchStatus == 1}">未匹配</c:if>
			<c:if test="${custom.sysMatchStatus == 2}">匹配中</c:if>
			<c:if test="${custom.sysMatchStatus == 3}">已匹配</c:if>
	   		</span>
	   	</li>
		<% }
		   if("true".equals(alarmMenu)){
		%>
		<c:if test="${custom.isAlarm}">
		<li class="item-seg-line"></li>
	   	<li><span title="预警状态" style="cursor: default;">预警
	   	</span>
	   	</li>
	   	</c:if>
		<% } %>
	</ol>
   	</div>
	<div class="queryResultCharSegLine"></div>
   	<div class="query-item-char-desc">
		<p><label>描述：</label>
			${custom.customGroupDesc}
	 	</p>
     	<p class="itemRuleContent"><label>规则：</label>
			${custom.prodOptRuleShow}${custom.simpleRule}${custom.customOptRuleShow}
			<c:if test="${custom.isOverlength == true}">
			...
			</c:if>
			${custom.kpiDiffRule}
			<c:if test="${custom.isOverlength == true}">
			<a href="javascript:void(0);" onclick="showRule('${custom.customGroupId}');">更多</a>
			</c:if>
	 	</p>
	 	<p class="itemRuleDate">
			<c:if test="${custom.monthLabelDate != null && custom.monthLabelDate != ''}">
			<label>数据月：</label>${custom.monthLabelDate}&nbsp;&nbsp;
			</c:if>
			<c:if test="${custom.dayLabelDate != null && custom.dayLabelDate != ''}">
			<label>数据日：</label>${custom.dayLabelDate}
			</c:if>
			<c:if test="${(custom.dayLabelDate == null || custom.dayLabelDate == '')  && (custom.monthLabelDate == null || custom.monthLabelDate == '')}">
			<label>数据日期：</label>${custom.dataDateStr}
			</c:if>
		    <!-- 收起和展开操作 -->
			<a href="javascript:void(0)" class="itemRuleUp" >收起</a>
			<a href="javascript:void(0)" class="itemRuleDown" >展开</a>
	  	</p>
   	</div>
</div>
</c:forEach> 
<script type="text/javascript">
$(function(){
	//客户群规则显隐效果高度变化 
	$(".itemRuleDown").click(function(){
	    $(this).siblings(".itemRuleUp").show().end().hide();
	       //展开规则动画效果
	       var $itemRuleContent = $(this).parent().prev(".itemRuleContent");
	       $itemRuleContent.css("overflow","visible").css("maxHeight","100%");
		var itemRuleContentHeight = $itemRuleContent.height();
		$itemRuleContent.animate({maxHeight:itemRuleContentHeight},300)
	});
	$(".itemRuleUp").click(function(){
	    $(this).siblings(".itemRuleDown").show().end().hide();
	       //收起动画效果
		var itemRuleContent = $(this).parent().prev(".itemRuleContent");
		itemRuleContent.animate({maxHeight:80+"px"},300,function(){
			$(this).css("overflow","hidden");
		});
	});
	//控制显隐效果
	$(".itemRuleContent").each(function(index,item){
		$(item).css("overflow","visible").css("maxHeight","100%");
	    var $item= $(item);
		var selfHeight = $item.height(); 
		if( selfHeight <=80){
			$(this).next().find(".itemRuleUp").hide();
			$(this).next().find(".itemRuleDown").hide();
		}else{
	    	$(item).css("overflow","hidden").css("maxHeight","80px");
	    }	   
	});
});
</script>
