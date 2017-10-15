<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
<head>
<title>分析与设定过程</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	aibiTableChangeColor(".mainTable");
	resizeParentIframe();
	
});

function resizeParentIframe(){
	var _h=$("body").height();
	$(window.parent.document).find("#Frame").height(_h+20);
}

function showMoreRslt(ts){
	var tar=$(ts).parents(".verify_main_con_box").next(".moreRslt");
	var _t=$(ts).parent();
	_t.hasClass("conclusionMore_showed")?tar.slideUp("fast",function(){
		_t.removeClass("conclusionMore_showed");
		resizeParentIframe();
	}):tar.slideDown("fast",function(){
		_t.addClass("conclusionMore_showed");
		resizeParentIframe();
	});
}
function hideMoreRslt(ts){
	$(ts).parents(".moreRslt").slideUp("fast",function(){
		$(this).prev(".verify_main_con_box").find(".conclusionMore_showed").removeClass("conclusionMore_showed");
		resizeParentIframe();
	})
}
</script>
</head>

<body>
<div class="main_con_box verify_main_con_box">
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>分析方法概述</span></div>
	</div><!--main_con_top end -->
    
    <dl class="even_odd_dl">
    	<dd>产品潜在用户挖掘算法是 “主成分分析方法” 和   “因子分析方法” 为基础，经过一定的数据处理和模型加工而来，其主要特点是符合电信行业营销业务场景，预测产品的潜在用户。本算法由标签规则与指标规则组合构成。</dd>
        <dd>（1）标签规则——标签规则主要是从定性的角度来解释产品属性与用户需求之间的一致性；通过一致性特征预测未来具有相同需求的潜在用户。</dd>
        <dd>（2）指标规则——指标规则是从定量角度（如ARPU、主叫计费时长等通用指标）来对产品属性与客户需求相一致的用户群体进行进一步细分。</dd>
    </dl>
</div>

<div class="main_con_box verify_main_con_box">
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>特征标签选取结果</span>下表根据产品订购前后的用户在客户需求标签的分布情况进行对比分析，获取的与产品潜在用户强相关的特征标签。详细内容请点击该表格右下角“更多分析过程”</div>
	</div><!--main_con_top end -->
    <div class="aibi_table_div" id="table1">
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>

				<th width="70">基准月</th>
				<th width="85">新增订购用户数</th>
				<th width="330">标签</th>
                <th width="125">基准月新增订购用户在基准月上一月的标签分布用户数</th>
                <th width="145">基准月新增订购用户在基准月上一月的标签分布用户数百分比</th>
                <th width="105">基准月未订购用户在基准月上一月的标签分布用户数</th>
                <th width="135">基准月未订购用户在基准月上一月的标签分布用户数百分比</th>
				<th width="70" style="border-right:none;">标签特征值</th>

			</tr>
            <tr>
            	<td rowspan="${fn:length(labelProcess)+1}">${labelProcess[0].datumMonth}</td>
            </tr>
            <c:forEach items="${labelProcess}" var="labProcess">
	            <tr>
					<td class="align_right"><fmt:formatNumber value="${labProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${labProcess.labelIndexName}</td>
					<c:choose>
						<c:when test="${labProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${labProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${labProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
            <tr class="conclusion last">
            	<td>结论</td>
                <td colspan="7" class="align_left" style="border-right:none;">
                	<div id="moreLabelSel" class="conclusionMore"><a href="javascript:void(0)" onclick="showMoreRslt(this)">更多分析过程</a></div>
                    <div class="conclusionInput">${labelProcess[0].labelSelectConclusion}</div>
                    
                </td>
            </tr>
		</table>
	</div>
</div><!--main_con_box end -->


<div class="moreRslt">
	<div class="main_con_top">
			<div class="tag_box_title tag_box_title_qsfx"><span>根据标签特征值对产品特征标签进行选取</span>
				根据产品新增订购用户与未订购用户在标签分布上的差异，找到与产品强相关的标签。
				基准月新增订购用户在基准月上一月的标签分布用户数：产品在基准月的新增订购用户作为一个用户集，并展示其在基准月上一月即此用户集订购前的标签分布数据情况
				基准月新增订购用户在基准月上一月的标签分布用户数百分比：基准月新增订购用户在基准月上一月的标签分布用户数 / 基准月产品新增订购用户数
				基准月未订购用户在基准月上一月的标签分布用户数：基准月全网用户减去截止基准月已经订购此产品的用户，形成未订购用户集，分析其在基准月上一月的标签分布数据情况
				基准月未订购用户在基准月上一月的标签分布用户数百分比：基准月未订购用户在基准月上一月的标签分布用户数 / 基准月未订购用户数
				标签特征值：基准月新增订购用户在基准月上一月的标签分布用户数百分比 — 基准月未订购用户在基准月上一月的标签分布用户数百分比，它表示此标签与产品订购之间强相关性。
			</div>
	</div><!--main_con_top end -->
      <div class="moreRslt_inner">	    
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th width="100">新增订购用户数</th>
				<th width="260">标签</th>
                <th>基准月新增订购用户在基准月上一月的标签分布用户数</th>
                <th>基准月新增订购用户在基准月上一月的标签分布用户数百分比</th>
                <th>基准月未订购用户在基准月上一月的标签分布用户数</th>
                <th>基准月未订购用户在基准月上一月的标签分布用户数百分比</th>
				<th width="80" style="border-right:none;">标签特征值</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(labelProcessL1)+1}">${labelProcessL1[0].datumMonth}</td>
            </tr>
            <c:forEach items="${labelProcessL1}" var="labProcess">
	            <tr>
					<td class="align_right"><fmt:formatNumber value="${labProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${labProcess.labelIndexName}</td>
	                <c:choose>
						<c:when test="${labProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${labProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${labProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
		</table>
		</div>
		
		<div class="main_con_top">
            <div class="tag_box_title tag_box_title_qsfx"><span>根据订购前标签分布用户数来选择产品特征标签</span>
			           下表是根据新增订购用户集自身的特征与产品订购之间的相关性进一步来筛选特征标签。根据“基准月新增订购用户在基准月上一月的标签分布用户数”列进行从大到小的排序，选择其中的标签。
            </div>
        </div><!--main_con_top end -->
        <div class="moreRslt_inner">
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th width="100">新增订购用户数</th>
				<th width="260">标签</th>
                <th>基准月新增订购用户在基准月上一月的标签分布用户数</th>
                <th>基准月新增订购用户在基准月上一月的标签分布用户数百分比</th>
                <th>基准月未订购用户在基准月上一月的标签分布用户数</th>
                <th>基准月未订购用户在基准月上一月的标签分布用户数百分比</th>
				<th width="80" style="border-right:none;">标签特征值</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(labelProcessL2)+1}">${labelProcessL2[0].datumMonth}</td>
            </tr>
            <c:forEach items="${labelProcessL2}" var="labProcess">
				<tr>
					<td class="align_right"><fmt:formatNumber value="${labProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${labProcess.labelIndexName}</td>
					<c:choose>
						<c:when test="${labProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${labProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${labProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
		</table>
	</div>
	
	<div class="main_con_top">
			<div class="tag_box_title tag_box_title_qsfx"><span>特征标签选取结果</span>
				将上述选择的产品特征标签进行汇总得到以下表格信息。
			</div>
	</div><!--main_con_top end -->
	<div class="moreRslt_inner">
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th width="100">新增订购用户数</th>
				<th width="260">标签</th>
                <th>基准月新增订购用户在基准月上一月的标签分布用户数</th>
                <th>基准月新增订购用户在基准月上一月的标签分布用户数百分比</th>
                <th>基准月未订购用户在基准月上一月的标签分布用户数</th>
                <th>基准月未订购用户在基准月上一月的标签分布用户数百分比</th>
				<th width="80" style="border-right:none;">标签特征值</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(labelProcessL3)+1}">${labelProcessL3[0].datumMonth}</td>
            </tr>
            <c:forEach items="${labelProcessL3}" var="labProcess">
				<tr>
					<td class="align_right"><fmt:formatNumber value="${labProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${labProcess.labelIndexName}</td>
					<c:choose>
						<c:when test="${labProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${labProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${labProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${labProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${labProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
		</table>
	</div>
	 <div class="conclusionMore_ct">
        <div class="conclusionMore conclusionMore_showed"><a href="#moreLabelSel" onclick="hideMoreRslt(this)">收起更多分析过程</a></div>
    </div>
</div><!--moreRslt end -->


<div class="main_con_box verify_main_con_box">
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>产品标签规则</span>根据分析得到的需求标签，设计标签的组合规则进行验证，根据数据验证结果的查全率和提升倍数综合评价，选择得出最优标签规则，橙色标注为最优标签规则。
</div>
	</div>
    
    <div class="aibi_table_div" id="table1">
		<table width="100%" class="mainTable verifyTable noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th>产品名称</th>
				<th>规则编码</th>
                <th width=50>查全率</th>
				<th width=70>提升倍数</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(labelRule)+1}">${labelRule[0].datumMonth}</td>
            	<td rowspan="${fn:length(labelRule)+1}">${labelRule[0].productName}</td>
            </tr>
            <c:forEach items="${labelRule}" var="label">
                <c:if test="${label.optimalFlag == 1}"><tr class="orange" ></c:if>
                <c:if test="${label.optimalFlag != 1}"><tr></c:if>
				<td class="align_left">${label.ruleCondition}</td>
				<td class="align_right"><fmt:formatNumber value="${label.datumRecall}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
				<td class="align_right"><fmt:formatNumber value="${label.datumUpgradeMultiple}" pattern="0.##"></fmt:formatNumber></td>
			   </tr>
            </c:forEach>
            <tr class="conclusion last">
            	<td>结论</td>
                <td colspan="4" class="align_left" style="border-right:none;">${labelRule[0].labelRuleConclusion}</td>
            </tr>
		</table>
	</div>
</div>


<c:if test="${fn:length(indexProcess) != 0}">
	
<div class="main_con_box verify_main_con_box">
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>指标规则选择结果</span>
		下表根据产品新增订购用户与未订购用户在指标分档的分布情况进行对比分析，获取与产品潜在用户强相关的特征指标，利用这些特征指标进一步精细化产品潜在用户范围。筛选方法是按照指标特征值从大到小排序进行筛选的结果，详细内容请点击该表格右下角“更多分析过程”。
		</div>
	</div><!--main_con_top end -->
    
    <div class="aibi_table_div" id="table1">
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th width="100">新增订购用户数</th>
				<th width="200">指标名称</th>
				<th width="100">指标区间</th>
                <th>基准月新增订购用户在基准月上一月指标分档用户数</th>
                <th>基准月新增订购用户在基准月上一月指标分档用户数百分比</th>
                <th>未订购用户在基准月上一月指标分档用户数</th>
                <th>未订购用户在基准月上一月指标分档用户数百分比</th>
				<th width="80" style="border-right:none;">指标特征值</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(indexProcess)+1}">${indexProcess[0].datumMonth}</td>
            </tr>
            <c:forEach items="${indexProcess}" var="indxProcess">
				<tr>
					<td class="align_right"><fmt:formatNumber value="${indxProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${indxProcess.labelIndexName}</td>
					<td >${indxProcess.indexInterval}</td>
					<c:choose>
						<c:when test="${indxProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${indxProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${indxProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${indxProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${indxProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${indxProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${indxProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
            <tr class="conclusion last">
            	<td>结论</td>
                <td colspan="8" class="align_left" style="border-right:none;">
                	<div class="conclusionMore"><a href="javascript:void(0)" onclick="showMoreRslt(this)">更多分析过程</a></div>
                    <div class="conclusionInput">${indexProcess[0].indexSelectConclusion}</div>  
                </td>
            </tr>
		</table>
	</div>
</div><!--main_con_box end -->

<div class="moreRslt">

	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>指标选择</span>
				指标选择的原则符合以下两个特点：1、指标按分段分布的用户数量较为平均；
				2、这些指标在有限的分析范围内可以包含90%以上的全网用户。基于此标准，从经分系统的1000多个指标中选取了27个常用指标。
				指标规则信息的价值可进一步增大提升倍数，在尽量不降低查全率的前提下，缩小潜在用户范围，提升分析模型的精准度。
		</div>
	</div>
    <div class="moreRslt_inner">
        <table width="50%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
            <tr>
				<th width="%50">指标</th>
				<th width="20%">分布粒度</th>
				<th width="30%">分布范围</th>
			</tr>
			<c:forEach items="${verifyIndexShowList}" var="indexShow">
			<tr>
			     <td class="align_left">${indexShow.indexName }</td>
                 <td class="align_right">${indexShow.granularity}</td>
                 <td class="align_right">${indexShow.range}</td>
			</tr>
			</c:forEach>
        </table>
    </div>
    
    
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>指标分段分布数据</span>
			分档区间：显示此指标的分档信息
			基准月新增订购用户在基准月上一月的指标分档用户数：将此产品基准月新增订购用户作为一个用户集展示其基准月上一月即订购前的指标分档数据情况
			基准月新增订购用户在基准月上一月的指标分档用户数百分比：基准月新增订购用户在基准月上一月的指标分段用户数 / 基准月产品新增订购用户数
			未订购用户在基准月上一月指标分档用户数：基准月全网用户减去截止基准月已经订购此产品的用户，形成未订购用户集，分析其在基准月上一月的指标分档数据情况
			未订购用户在基准月上一月指标分档用户数百分比：基准月未订购用户在基准月上一月的指标分档用户数 / 基准月未订购用户数
			指标特征值是指：基准月新增订购用户在基准月上一月的指标分档用户数百分比 — 未订购用户在基准月上一月指标分档用户数百分比
		</div>
    </div>
    <div class="moreRslt_inner">
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th width="100">新增订购用户数</th>
				<th width="200">指标名称</th>
				<th width="100">指标区间</th>
                <th>基准月新增订购用户在基准月上一月指标分档用户数</th>
                <th>基准月新增订购用户在基准月上一月指标分档用户数百分比</th>
                <th>未订购用户在基准月上一月指标分档用户数</th>
                <th>未订购用户在基准月上一月指标分档用户数百分比</th>
				<th width="80" style="border-right:none;">指标特征值</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(indexProcessI1)+1}">${indexProcessI1[0].datumMonth}</td>
            </tr>
            <c:forEach items="${indexProcessI1}" var="indxProcess">
				<tr>
					<td class="align_right"><fmt:formatNumber value="${indxProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${indxProcess.labelIndexName}</td>
					<td >${indxProcess.indexInterval}</td>
					<c:choose>
						<c:when test="${indxProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${indxProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${indxProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${indxProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${indxProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${indxProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${indxProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
		</table>
	</div>
	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>指标及分档筛选结果</span>
			将上表的数据信息进行指标分档合并同类项后得到下表信息
		</div>
    </div>
	<div class="moreRslt_inner">
		<table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="70">基准月</th>
				<th width="100">新增订购用户数</th>
				<th width="200">指标名称</th>
				<th width="100">指标区间</th>
                <th>基准月新增订购用户在基准月上一月指标分档用户数</th>
                <th>基准月新增订购用户在基准月上一月指标分档用户数百分比</th>
                <th>未订购用户在基准月上一月指标分档用户数</th>
                <th>未订购用户在基准月上一月指标分档用户数百分比</th>
				<th width="80" style="border-right:none;">指标特征值</th>
			</tr>
            <tr>
            	<td rowspan="${fn:length(indexProcessI2)+1}">${indexProcessI2[0].datumMonth}</td>
            </tr>
            <c:forEach items="${indexProcessI2}" var="indxProcess">
				<tr>
					<td class="align_right"><fmt:formatNumber value="${indxProcess.datumNewUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
					<td class="align_left">${indxProcess.labelIndexName}</td>
					<td >${indxProcess.indexInterval}</td>
					<c:choose>
						<c:when test="${indxProcess.lastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${indxProcess.lastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${indxProcess.lastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
	                <c:choose>
						<c:when test="${indxProcess.notLastUserNum == null}">
							<td class="align_right">-</td>
						</c:when>
						<c:otherwise>
							<td class="align_right"><fmt:formatNumber value="${indxProcess.notLastUserNum}" pattern="###,###,###"></fmt:formatNumber></td>
						</c:otherwise>
					</c:choose>
	                <td class="align_right"><fmt:formatNumber value="${indxProcess.notLastPropotion}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
					<td class="align_right"><fmt:formatNumber value="${indxProcess.eigenvalue}" pattern="0.####"></fmt:formatNumber></td>
				</tr>
            </c:forEach>
		</table>
	</div>
	


	<div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>指标规则描述</span>
		
		</div>
    </div>
    <div class="moreRslt_inner">
        <table width="100%" class="mainTable verifyTable lightTHbg noselect_tr nohover" cellpadding="0" cellspacing="0">
            <tr>
				<th width="30%">产品名称</th>
				<th width="70%">规则描述</th>
			</tr>
			<tr>
			   <td class="align_left">${productInfo.productName }</td>
			   <td class="align_left">${indexRuleDescribe[0].ruleCondition }</td>
			</tr>
        </table>
    </div>

     <div class="conclusionMore_ct">
        <div class="conclusionMore conclusionMore_showed"><a href="javascript:void(0)" onclick="hideMoreRslt(this)">收起更多分析过程</a></div>
    </div>
</div><!--moreRslt end -->

</c:if>

<div class="main_con_box verify_main_con_box">   
    <div class="main_con_top">
		<div class="tag_box_title tag_box_title_qsfx"><span>形成最终潜在用户规则</span>
    </div>
</div><!--main_con_top end -->

    <div class="aibi_table_div" id="table1">
		<table width="100%" class="mainTable verifyTable noselect_tr nohover" cellpadding="0" cellspacing="0">
			<tr>
				<th width="10%">产品名称</th>
				<th width="70%">产品规则</th>
				<th>基准月查全率</th>
                <th>基准月提升倍数</th>
			</tr>
            <tr>
				<td class="align_left">${productInfo.productName }</td>
				<td class="align_left">${finalPotentialUserRule.ruleDesc}</td>
                <td class="align_right"><fmt:formatNumber value="${finalPotentialUserRule.datumRecall}" type="percent" maxFractionDigits="2"></fmt:formatNumber></td>
				<td class="align_right"><fmt:formatNumber value="${finalPotentialUserRule.datumUpgradeMultiple}" pattern="0.##"></fmt:formatNumber></td>
			</tr>
            <tr class="conclusion last">
            	<td>结论</td>
                <td colspan="3" class="align_left" > ${finalPotentialUserRule.finalRuleConclusion }</td>
            </tr>
		</table>
	</div>
</div>

</body>
