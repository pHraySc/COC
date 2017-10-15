<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script type="text/javascript">
$().ready(function () 
{
    $( "#dialog_look" ).dialog(
    {
        autoOpen : false, width : 980, height : 456, title : "查看", modal : true,
        close : function ()
        {
            $("#dialog_detail").attr("src", "");
        },
        resizable:false
    });
});
//取消用户对标签的关注
function delUserAttentionLabel(event, labelId){
    var evt = event || window.event;
    var element = evt.srcElement || evt.target;
    var url = $.ctx + '/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
    $.ajax({
        type : "POST", url : url, data : 'labelId=' + labelId + '&delAttentionLabelFromPage=1',
        success : function(result){
            if(result.success){
                //隐藏关注图标展示
                element.style.display = 'none';
                //element.src = '${ctx}/aibi_ci/assets/themes/default/images/favorite_gray.png';
                //element.title = '';
                //element.onclick='';
                window.parent.showAlert(result.message,"success");
                loadLabelList();
            }else {
                window.parent.showAlert(result.message,"failed");
            }
        }
    });
}
//点击累计使用次数，分页显示使用过的用户
function showLabelTrend(labelId) { 
	    var ifmUrl = $.ctx+"/ci/ciLabelInfoAction!useTimesDetail.ai2do?labelId="+labelId;
	    $("#dialog_detail").attr("src", ifmUrl);
	    $("#dialog_look" ).dialog("open");
}	
</script>
<%@ include file="/aibi_ci/include.jsp"%>
    <c:forEach items="${labelDetailList}" var="label">
                <div class="customerBase_rows" style="width:99.5%;">
                
                <table width="100%">
                <tr>
                <td>
                <dl>
                    <dt>
                    <div style="float:left;font-size:14px">
                    <c:choose>
	                    <c:when test="${label.hasAlarm == true}">
	                    	<span style="float:left;background:none;padding-left:0px;margin-top:8px;">${label.labelName}</span>
                    		<span style="float:left;margin-top:8px;">用户数为&nbsp<fmt:formatNumber  type="number" value="${label.customNum == null ? 0 : label.customNum}" pattern="###,###,###"></fmt:formatNumber>&nbsp人</span>
	                    </c:when>
	                    <c:otherwise>
	                    	<span style="float:left;background:none;padding-left:0px;margin-top:8px;">${label.labelName}</span>
                    		<span style="float:left;margin-top:8px;background:none">用户数为&nbsp<fmt:formatNumber  type="number" value="${label.customNum == null ? 0 : label.customNum}" pattern="###,###,###"></fmt:formatNumber>&nbsp人</span>
	                    </c:otherwise>
                    </c:choose>
                    </div> 	
                    <c:choose>
                    <c:when test="${searchType == 'userAttentionSearch'}">
                    <img  style="float:left;padding-top:7px;padding-left:5px;cursor:pointer;" title="点击取消标签关注" id="attentionImg" src="${ctx}/aibi_ci/assets/themes/default//images/favorite.png" onclick="delUserAttentionLabel(event,${label.labelId})" />
                    </c:when>
                    </c:choose>
                     <span class="clear"></span>
                    </dt>
                    <dd>
                        <table class="commonTable">
                            <tr>
                                <th>业务口径：</th>
                                <td colspan="3">
                                ${label.busiCaliber}
                                </td>
                            </tr>
                               <tr>
                                <th>标签路径：</th>
                                <td colspan="3">
                                ${label.currentLabelPath}
                                </td>
                            </tr>
                            <tr>
                                <th>累计使用次数：</th>
                                <td colspan="3">
                                    <a href="javascript:showLabelTrend('${label.labelId}');"><fmt:formatNumber  type="number" value="${label.useTimes}" pattern="###,###,###"></fmt:formatNumber></a>
                                </td>
                            </tr>
                        </table>
                    </dd>
                    <dd class="info_panel">
                        <c:choose>
	                        <c:when test="${label.isInPrilabel== 1}">
		                    	<ul>
		                          <li class="icon_tag" title="标签分析" onclick="openLabelAnalysis(${label.labelId})"><a href="javascript:void(0)">标签分析</a></li>
		                          <li class="icon_wtag" title="标签微分" onclick="openLabelDifferential(${label.labelId});"><a href="javascript:void(0)">标签微分</a></li>
		                    	  <c:if test="${label.updateCycle== 2}">
			                            <li class="icon04" title="关联分析" onclick="openLabelRel(${label.labelId});"><a href="javascript:void(0)">关联分析</a></li>
			                            <li class="icon05" title="对比分析" onclick="openLabelContrast(${label.labelId});"><a href="javascript:void(0)">对比分析</a></li>
		                    	  </c:if>                 	
		                        </ul>
	                      	</c:when>
                        </c:choose> 
                        <c:choose>
	                        <c:when test="${label.updateCycle== 1}">
	                        	用户数统计于 ${dateFmt:dateFormat(newLabelDay)}
	                        </c:when>
	                        <c:when test="${label.updateCycle== 2}">
	                        	用户数统计于 ${dateFmt:dateFormat(newLabelMonth)}
	                        </c:when>
                        </c:choose>            
                                                              
                    </dd>
                </dl>
                </td>
                <c:choose>
	            <c:when test="${label.isInPrilabel== 1}">
                <td style="width: 39px; background: #f7f6f6;"><div class="addCustomer" id="${label.labelId}" labelName="${label.labelName}" element="${label.labelId}" attrVal="${label.attrVal}" 
                	min="${label.minVal}" max="${label.maxVal}" type="${label.labelTypeId}" elementType="2" updateCycle="${label.updateCycle}"
                	effectDate="${label.effectDate}"><a href="javascript:addLabel('${label.labelId}');">添加</a></div></td>
                </c:when>
                <c:when test="${label.isInPrilabel== 0}">
                <td style="width: 39px; background: #f7f6f6;"><div class="addCustomer dis_addCustomer" id="${label.labelId}" labelName="${label.labelName}" element="${label.labelId}" attrVal="${label.attrVal}" 
                	min="${label.minVal}" max="${label.maxVal}" type="${label.labelTypeId}" elementType="2" updateCycle="${label.updateCycle}"
                	effectDate="${label.effectDate}"><a href="javascript:addLabel('${label.labelId}');">添加</a></div></td>
                </c:when>
                </c:choose>
                </tr>
                </table>
               </div>
     </c:forEach>    
<!--点击累计使用次数-->
<div id="dialog_look">
	<iframe name="dialog_detail" scrolling="no" allowtransparency="true" src="" id="dialog_detail"  framespacing="0" border="0" frameborder="0" style="width:100%;height:100%"></iframe>
</div>  
<!--row end -->
