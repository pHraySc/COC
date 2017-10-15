<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<c:set var="DIM_SCENE" value="<%=CommonConstants.TABLE_DIM_SCENE%>"></c:set>
<%
	String productMenu = Configure.getInstance().getProperty(
			"PRODUCT_MENU");
	String marketingMenu = Configure.getInstance().getProperty(
			"MARKETING_MENU");
	String personMenu = Configure.getInstance().getProperty(
			"PERSON_MENU");
	String alarmMenu = Configure.getInstance().getProperty(
			"AlARM_MENU");
	String customersFileDown = Configure.getInstance().getProperty("CUSTOMERS_FILE_DOWN");
	String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
	String indexDifferentialMenu = Configure.getInstance().getProperty("INDEX_DIFFERENTIAL");
	String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
	String root = Configure.getInstance().getProperty("CENTER_CITYID");
	int isNotContainLocalList = ServiceConstants.IS_NOT_CONTAIN_LOCAL_LIST;
	String isCustomVertAttr = Configure.getInstance().getProperty("IS_CUSTOM_VERT_ATTR");
%>
<c:set var="isCustomVertAttr" value="<%=isCustomVertAttr %>"></c:set>
<script type="text/javascript">
$(document).ready( function() {
	$("#totalSizeNum").html('${pager.totalSize}');
	if($.trim($("#customGroupName").val()) == "" || $.trim($("#customGroupName").val()) == "模糊查询"){
		$("#pathCustomText").html("");
		$("#pathNavArrowRightMyCustom").hide(); 
	}else{
		$("#pathNavArrowRightMyCustom").show(); 
		if($("#isSimpleSearch").val() == "true" || $("#isSimpleSearch").val() == ""){
			$("#pathCustomText").html($.trim($("#customGroupName").val()));
		}else{
			$("#pathCustomText").html($.trim($("#customGroupName").val()));
		}
	}
	if(!$("#deleteButton").hasClass("disable")){
		$("#deleteButton").addClass("disable");
	}
	$(".num").each(function(){
		if($(this).text() == ${pager.pageNum}){
			$(this).addClass("num_on");
		}
	});
	
	var tarTable=$("#sortTable");
	var tableCT=tarTable.parent();
	var startWidth=tableCT.width();
	tarTable.find("th").click(sortTable);
	
	$(window).resize(function(){
		var thHeight=tarTable.find("th").eq(0).height();
		//alert(thHeight);
		if(thHeight>26){
			tarTable.width(startWidth);
		}
	})
});

</script>
<style type="text/css">
</style>
	<table width="100%" id="sortTable" class="tableStyle" >
		<thead>
			<tr>
				<th width="25" style="cursor: pointer;">
					<input type="checkbox" name="checkBox" id="checkBoxAll"
						onclick="checkBoxAll(this)" />
				</th>
				<th width="105" class="header" dataType='text'>
					编号<span class="sort">&nbsp;</span>
				</th>
				<th width="155" class="header" dataType='text'>
					客户群名称<span class="sort">&nbsp;</span>
				</th>
				<!-- <th width="100" class="header" dataType='text'>
					客户群分类<span class="sort">&nbsp;</span>
				</th> -->
				<th width="80" class="header" dataType='text'>
					生成周期<span class="sort">&nbsp;</span>
				</th>
				<th width="65" class="header" dataType='num'>
					用户数<span class="sort">&nbsp;</span>
				</th>
				<c:if test="${isCustomVertAttr == 'true' }">
				<th width="90" class="header" dataType='num'>
					重复用户数<span class="sort">&nbsp;</span>
				</th>
				</c:if>
				<th width="80" class="header" dataType='date'>
					修改时间<span class="sort">&nbsp;</span>
				</th>
				<!-- <th width="120" class="header" dataType='date'>
					创建时间<span class="sort">&nbsp;</span>
				</th> -->
				<th width="75" class="header" dataType='text' style="white-space:nowrap;">
					创建方式<span class="sort">&nbsp;</span>
				</th>
				<th width="65" class="header" dataType='date'>
					数据月<span class="sort">&nbsp;</span>
				</th>
				<th width="75" class="header" dataType='date'>
					数据日<span class="sort">&nbsp;</span>
				</th>
				<%
					if ("true".equals(marketingMenu)) {
				%>
				<th width="70" class="header" dataType='text'>
					系统匹配<span class="sort">&nbsp;</span>
				</th>
				<%
					}
				%>
				<th width="45" class="header" dataType='text'>
					状态<span class="sort">&nbsp;</span>
				</th>
				<th width="70" class="header" dataType='text'>
					是否共享<span class="sort">&nbsp;</span>
				</th>
				<!-- <th width="70" class="header" dataType='text'>
					是否下线<span class="sort">&nbsp;</span>
				</th> -->
				<th class="noborder">
					操作
				</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pager.result}" var="po" varStatus="st">
			<c:choose>
				<c:when test="${st.index % 2 == 0 }">
					<tr class="even">
				</c:when>
				<c:otherwise>
					<tr class="odd">
				</c:otherwise>
			</c:choose>
			<td style="cursor: pointer;">
			<c:choose>
		        <c:when test="${po.dataStatus != 1 && po.dataStatus != 2 && po.dataStatus != 4 && (isAdmin || po.createUserId == userId)}">
				<input type="checkbox" name="checkBox" class="checkBox" id="checkBox${st.index+1}" value="${po.customGroupId}" onclick="checkDelete();checkPush();" />
		        </c:when>
		        <c:otherwise>
				<input type="checkbox" name="checkBox" class="checkBox" id="checkBox${st.index+1}" value=""  disabled/>
		        </c:otherwise>
		    </c:choose>
			</td>
			<td class="align_left textAuto">
				${po.customGroupId }
			</td>
			<td class="align_left textAuto" onclick="queryGroupInfo('${po.customGroupId}','${po.customGroupName}')" style="cursor: pointer;">
				<span style="color:#0081CC">${po.customGroupName}</span>
			</td>
			<%-- <td class="align_left">
			<c:set value="${fn:length(po.customSceneNames) }" var="strLen"></c:set>
			<div title="${po.customSceneNames} " class="strLenLimit width100">
			${po.customSceneNames }
			   <c:choose>
			      <c:when test="${strLen > 30 }">
			           ${fn:substring(fn:replace(po.customSceneNames, "&nbsp;", " "), 0, 15)}
			         ...
			      </c:when>
			      <c:otherwise>
			         ${po.customSceneNames} 
			      </c:otherwise>
			   </c:choose>
			</div>
			</td> --%>
			<td class="align_center">
				<c:if test="${po.updateCycle == 1}">一次性</c:if>
				<c:if test="${po.updateCycle == 2}">月周期</c:if>
				<c:if test="${po.updateCycle == 3}">日周期</c:if>
				<c:if test="${po.updateCycle == 4}">-</c:if>
			</td>
			<td class="align_right" style="white-space:nowrap">
				<c:choose>
					<c:when test="${po.customNum == null}">-</c:when>
					<c:otherwise>
						<fmt:formatNumber value="${po.customNum}" type="number" pattern="###,###,###"/>
					</c:otherwise>
				</c:choose>
			</td>
			<c:if test="${isCustomVertAttr == 'true' }">
			<td class="align_right" style="white-space:nowrap">
				<c:choose>
					<c:when test="${po.duplicateNum == null || po.duplicateNum == 0}">-</c:when>
					<c:otherwise>
						<fmt:formatNumber value="${po.duplicateNum}" type="number" pattern="###,###,###"/>
					</c:otherwise>
				</c:choose>
			</td>
			</c:if>
			<%-- <td class="align_right">
				<div>
				<fmt:formatDate value="${po.createTime}"
					pattern="yyyy-MM-dd HH:mm:ss" />
				</div>
			</td> --%>
			<td class="align_right">
				<div>
				<c:choose>
					<c:when test="${po.newModifyTime == null}">
                    	-
                    </c:when>
                    <c:otherwise>
                        <fmt:formatDate value="${po.newModifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </c:otherwise>
                </c:choose>
				</div>
			</td>
			<td class="align_center" style="white-space:nowrap;">
				${po.createType}
			</td>
			<td class="align_right">
			<c:if test="${po.monthLabelDate == null || po.monthLabelDate == ''}">-&nbsp;
			</c:if>
			<c:if test="${po.monthLabelDate != null && po.monthLabelDate != ''}">
				${dateFmt:dateFormat(po.monthLabelDate)}&nbsp;
			</c:if>
			</td>
			<td class="align_right">
			<c:if test="${po.dayLabelDate == null || po.dayLabelDate == ''}">-&nbsp;
			</c:if>
			<c:if test="${po.dayLabelDate != null && po.dayLabelDate != ''}">
				${dateFmt:dateFormat(po.dayLabelDate)}&nbsp;
			</c:if>
			</td>
			<%
				if ("true".equals(marketingMenu)) {
			%>
			<td class="align_center">
				<c:if test="${po.sysMatchStatus == 1}">未匹配</c:if>
				<c:if test="${po.sysMatchStatus == 2}">匹配中</c:if>
				<c:if test="${po.sysMatchStatus == 3}">已匹配</c:if>
			</td>
			<%
				}
			%>
			<td class="align_center">
				${po.dataStatusStr}
			</td>
			<td class="align_center">
				<c:if test="${po.isPrivate == 0}">共享</c:if>
				<c:if test="${po.isPrivate == 1}">非共享</c:if>
				<c:if test="${empty po.isPrivate }">-</c:if>
			</td>
			<!-- <td class="align_center">
				<c:if test="${po.isLabelOffline == 0}">未下线</c:if>
				<c:if test="${po.isLabelOffline == 1}">下线</c:if>
				<c:if test="${empty po.isLabelOffline }">未下线</c:if>
			</td> -->
			<td class="align_left">
				<div class="resultItemActive marginAuto" onclick="showOperCustomList(event,this,'${po.customGroupId}')" isPrivate="${po.isPrivate }" createUserId="${po.createUserId }" createCityId="${po.createCityId }" isContainLocalList="${po.isContainLocalList }"><span class="dsts">操作</span>
			    <c:if test="${isAdmin||(po.createCityId==root) || ( po.createCityId == cityId) || (po.createUserId == userId) || po.isPrivate == 0}">
			   	<div class="resultItemActiveList" id="${po.customGroupId }">
					<ol>
						<!-- 不含清单且含有下线标签的客户群 有删除操作无其他操作权限 -->
						<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
						<c:if test="${po.dataStatus == 0 || po.dataStatus == 3 }">
							<c:if test="${po.dataStatus == 3 && po.isHasList == 1}">
							<c:if test="${cityId == root || po.createCityId == root || po.createCityId == cityId }">
								<li><a href="javascript:queryGroupList('${po.customGroupId}','${po.dataDate}');" >清单预览</a></li>
							</c:if>
							<c:if test="${po.listTableName != null}">
						    <% if("true".equals(indexDifferentialMenu)){ %>
		                         	<li style="display:none"><a href="javascript:openIndexDeff('${po.customGroupId}','${po.listTableName}','${po.customNum}','${po.dataDate}')">指标微分</a></li>
						    <%};if("true".equals(customerAnalysisMenu)){
		                             %>
									<!-- <li style="display:none"><a href="javascript:openlabelDeff('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}')">标签微分</a></li>
		                         	<li style="display:none"><a href="javascript:openCustomRel('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">关联分析</a></li>
		                         	<li style="display:none"> <a href="javascript:openCustomContrast('${custom.customGroupId}','${custom.listTableName}','${custom.customNum}','${custom.dataDate}','${custom.failedListCount}')">对比分析</a></li> -->
		                         	<%
		                                 };if("true".equals(marketingMenu)){
		                             %>
		                         	<li><a href="javascript:customSysMatch('${po.customGroupId}','${po.listTableName}','${po.customNum}','${po.dataDate}','${po.failedListCount}')">营销策略匹配</a></li>
		                         	<%}; %>
				            </c:if>
					        </c:if>
					      </c:if>
					      <!-- 创建中、待创建、成功含清单的客户群，可以进行清单下载： 客户数不为null-->
					      <c:if test="${po.dataStatus != 0 && po.isHasList == 1 && po.customNum != null}">
					        <c:if test="${po.createUserId == userId }">
							<li><a href="javascript:showCustomerTrend('${po.customGroupId}');" >清单下载</a></li>
							</c:if>
						  </c:if>
						  <c:if test="${po.dataStatus == 0 || po.dataStatus == 3 }">
					        <c:if test="${havePushFun == 1}">
							<c:if test="${po.dataStatus == 3 && po.isHasList == 1}">
	                        	<c:if test="${po.createUserId == userId}">
		                      	   	<li><a href="javascript:pushCustomerGroupSingle('${po.customGroupId}','${po.updateCycle}', '${po.isPush}', '${po.duplicateNum}');">推送设置</a></li>
	                        	</c:if>
							</c:if>
							</c:if>
							<c:if test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0) && po.isHasList == 1}">
							<c:if test="${po.failedListCount >= 1  && po.createUserId == userId && po.createTypeId == 1}">
							<li><a href="javascript:regenCustomer('${po.customGroupId}','${po.createTypeId}');" >统计失败重新生成</a></li>
							</c:if>
							
							<c:if test="${po.failedListCount < 1  && po.createTypeId==1 && po.updateCycle==1 && po.isPrivate != 0 && po.createUserId == userId}">
								<li><a href="javascript:oneTimeRegenCustomer('${po.customGroupId}','${po.createTypeId}');" >重新生成</a></li>
							</c:if>
							</c:if>
							<c:if test="${po.isPrivate != 0}">
								<c:if test="${po.dataStatus == 3 && po.createUserId == userId}">
								<li id="share_${po.customGroupId}" c_name="${po.customGroupName}" c_cityId="${po.createCityId}" c_userId="${po.createUserId}">
									<a href="javascript:shareOne('${po.customGroupId}');">共享</a>
								</li>
								</c:if>
							</c:if>
							
							<c:if test="${po.isPrivate == 0 && po.isSysRecom == 0}">
							<li id="share_${po.customGroupId}">
								<a href="javascript:cancelShareOne('${po.customGroupId}');">取消共享</a>
							</li>
							</c:if>
							<c:if test="${po.dataStatus == 3}">
								<c:if test="${(root == cityId) || (root != cityId && po.createCityId == cityId)}">
									<c:if test="${isAdmin && po.isPrivate == 0 && (empty po.isSysRecom || po.isSysRecom == 0)}">
									<li><a href="javascript:void(0);" customId="${po.customGroupId}" onclick="changeRecomCustom(this)">推荐</a></li>
									</c:if>
								</c:if>
							</c:if>
							<c:if test="${(root == cityId) || (root != cityId && po.createCityId == cityId)}">
								<c:if test="${isAdmin && po.isPrivate == 0 && (!empty po.isSysRecom && po.isSysRecom == 1)}">
								<li><a href="javascript:void(0);" customId="${po.customGroupId}" onclick="changeRecomCustom(this)">取消推荐</a></li>
								</c:if>
							</c:if>
							<%
					       if("true".equals(alarmMenu)){
			                  %>
				           <c:if test="${po.dataStatus == 3 && po.updateCycle == 2 }">
					
					       <li><a href="javascript:addAlarmThreshold('${po.customGroupId}','${po.customGroupName}')">设置预警</a></li>
				           </c:if>
				           <c:if test="${po.dataStatus == 3 && po.updateCycle == 3 }">
					
					       <li><a href="javascript:addAlarmThreshold('${po.customGroupId}','${po.customGroupName}')">设置预警</a></li>
				           </c:if>
				             <%} %>
							<c:if test="${po.isLabelOffline != 1}">
							<c:if test="${isAdmin || po.createUserId == userId}">
								<c:choose>
									<c:when test="${(po.isLabelOffline == null ||  po.isLabelOffline == 0) && (po.dataStatus == 0 || po.dataStatus == 3) && po.createTypeId==1 && (po.updateCycle == 2 || po.updateCycle == 3|| po.updateCycle == 4) }">
						               <li><a href="javascript:editCustomerOpenCalculate('${po.customGroupId}');">修改</a></li>
									</c:when>
									<c:otherwise>
						               <li><a href="javascript:editCustomer(${po.createTypeId},${po.updateCycle},'${po.customGroupId}');">修改</a></li>
									</c:otherwise>
								</c:choose>
							</c:if>
							</c:if>
						</c:if>
						</c:if>
						<c:if test="${po.dataStatus == 3 || po.dataStatus == 0}">
						<c:if test="${isAdmin || po.createUserId == userId}">
							<li <c:if test="${po.isLabelOffline == 1 && po.updateCycle == 4}">title='此客户群不含清单且含有下线标签'</c:if>><a href="javascript:delOne('${po.customGroupId}');">删除</a></li>
						</c:if>
						</c:if>
						<!-- 不含清单且含有下线标签的客户群 有删除操作无其他操作权限 -->
						<c:if test="${!(po.isLabelOffline == 1 && po.updateCycle == 4)}">
							<c:if test="${po.dataStatus == 0 || po.dataStatus == 3 }">
								<% if ("true".equalsIgnoreCase(templateMenu)) { %>
								<c:if test="${po.createTypeId==1}">
									<li><a href="javascript:createTemplate('${po.customGroupId}','${po.customGroupName}','${po.sceneId}');" >提取模板</a></li>
								</c:if>
								<% } %>
							</c:if>
							<!-- 包含下线或者停用标签的客户群不能加入购物车 -->
							<c:if test="${po.isPrivate == 0}">
								<c:if test="${po.createCityId == root || po.createCityId == cityId || po.isContainLocalList == isNotContainLocalList }">
									<!-- 包含清单客户群：1、客户群数为空  && 2、数据状态为失败 不能加入购物车 -->
							        <!-- 模板客户群可以加入购物车 -->
							        <c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
										<li><a href="javascript:customValidate2('${po.customGroupId}','2','0');">添加到收纳篮</a></li>
							        </c:if>
								</c:if>
							</c:if>
							<c:if test="${po.isPrivate == 1}">
								<c:if test="${po.createUserId == userId }">
									<!-- 包含清单客户群：1、客户群数为空  && 2、数据状态为失败 不能加入购物车 -->
							        <!-- 模板客户群可以加入购物车 -->
							        <c:if test="${po.updateCycle == 4 ||(po.createTypeId == 7  && po.dataStatus == 3)||(po.createTypeId == 12 && po.dataStatus == 3) || (po.createTypeId != 7 && po.createTypeId != 12 && (po.customNum == null && po.dataStatus == 0) == false)}">
										<li><a href="javascript:customValidate2('${po.customGroupId}','2','0');">添加到收纳篮</a></li>
							        </c:if>
								</c:if>
							</c:if>
						 </c:if>
					  	</ol>
				   	</div>
					</c:if>
				</div>
			</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<form id="edit_custom_form" method="post" action="${ctx}/ci/ciIndexAction!forwardIndex.ai2do?forwardType=1">
		<input id="edit_custom_input" type="hidden" name="ciCustomGroupInfo.customGroupId" />
	</form>
	<div class="pagenum" id="pager">
		<jsp:include page="/aibi_ci/page_new_html.jsp" flush="true" />
	</div>
