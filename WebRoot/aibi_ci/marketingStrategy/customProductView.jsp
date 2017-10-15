<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/aibi_ci/html_include.jsp"%>
<title>客户群与产品匹配展示</title>
<script type="text/javascript">
	$(document).ready(function(){
		$(window.parent).scrollTop(0);
		//对比标签加载
		function loadtag(){
			if($(".scom_llable .tag tr").length == 1){
				$(".scom_llable .tag tr").addClass("only_tr");
			};
			if($(".scom_llable .tag tr").length > 1){
				$(".scom_llable .tag tr:first").addClass("first_tr");
				$(".scom_llable .tag tr:last").addClass("last_tr");
			};
			if($(".scom_rlable .tag tr").length == 1){
				$(".scom_rlable .tag tr").addClass("only_tr");
			};
			if($(".scom_rlable .tag tr").length > 1){
				$(".scom_rlable .tag tr:first").addClass("first_tr");
				$(".scom_rlable .tag tr:last").addClass("last_tr");
			};
		}
		loadtag();
		//标签选中
		$(".scom_lable .tag tr").click(function(e){
			var productsIdsStr = "";
			if($(this).hasClass("on")){
				$(this).removeClass("on");
				$(".warnBox").hide();
			}else{
				if($(".scom_lable .tag tr.on").length < 5){
					$(this).addClass("on");
					$(".scom_lable .tag tr.on").each(function(){
						productsIdsStr = productsIdsStr + $(this).find(".productIds").val()+',';
					});
					$('#productsIdsStr').val(productsIdsStr);
				}else{
					$(".warnBox").show();
					var toffset=$(this).offset();
					$(".warnBox").css({top:toffset.top + 65, left: toffset.left+80}); 
					$(".warnBox .close").click(function(){
						$(".warnBox").hide();
					});
				}
			}
		});
		
		$("#initSaveMatchdialog").click(function(){
			var productsIdsStr = "";
			$(".scom_lable .tag tr.on").each(function(){
				productsIdsStr = productsIdsStr + $(this).find(".productIds").val()+',';
			});
			$('#productsIdsStr').val(productsIdsStr);
			if(productsIdsStr == ""){
				window.parent.showAlert("请选择产品","failed");
				return false;
			}
			var actionUrl = "${ctx}/ci/marketingStrategyAction!initSaveMarketing.ai2do";
			$("#returnForm").attr("action", actionUrl);
			$('#returnForm').submit();
		});

	});
	function marketingStrategy(){
		$('#returnForm').submit();
	}
</script>
</head>
<body>
<div class="save_box">
	<dl class="analyse_right_dl marginTop_0">
		<dt><span>数据时间：${dateFmt:dateFormat(customGroup.dataDate)}&nbsp;&nbsp;&nbsp;&nbsp;<font class="red">注：最多可选5个产品</font></span>
		 <span class="analyse_dt_con"><a href="javascript:marketingStrategy()" class="link_back">返回配置</a></span></dt>
	</dl>
	<dd class="save_box_dd">
		<div class="aibi_table_div">
			<div class="com_box">
				<!--对比标签开始 -->
				<table width="970px" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td><div class="t">系统推荐产品</div></td>
						<td width="234px;"></td>
						<td><div class="t">手工配置产品</div></td>
					</tr>
					<tr>
						<td class="scom_lable scom_llable">
							<!--系统推荐产品开始 -->
							<table width="100%" cellpadding="0" cellspacing="0" class="tag">
							  <c:forEach items="${sysProductMatchList}" var="sysProduct">
								<tr>
									<td class="bg_l">${sysProduct.matchPropotion}%</td>
									<td class="bg_c"><p title="${sysProduct.productName }" class="title">${sysProduct.productName }</p><p>用户数：${sysProduct.matchCustomNum }</p></td>
									<td class="bg_r"></td>
									<td class="bg_line"></td>
									<input type="hidden" class="productIds" value="${sysProduct.productId}" />
								</tr>
							  </c:forEach>
							</table>
							<!--系统推荐产品结束 -->
						</td>
						<td width="234px;">
							<!--客户群名称 -->
							<div class="smain_lable">
								<div class="tag_padding"></div>
								<div class="tag">
									<div class="bg_t"></div>
									<div class="bg_c">
										<table width="100%" cellpadding="0" cellspacing="0">
											<tr><td class="title"><p title="${customGroup.customGroupName }">${customGroup.customGroupName }</p></td></tr>
											<tr><td><p title="${customGroup.customNum }">${customGroup.customNum }</p></td></tr>
										</table>
									</div>
									<div class="bg_b"></div>
								</div>
								<div class="tag_padding"></div>
							</div>
							<div class="clear"></div>
							<div class="t">客户群名称</div>
						</td>
						<td class="scom_lable scom_rlable">
							<!--手工配置产品开始 -->
							<table width="100%" cellpadding="0" cellspacing="0" class="tag">
							  <c:forEach items="${productMatchList }" var="productMatch">
								<tr >
									<td class="bg_line"></td>
									<td class="bg_l">${productMatch.matchPropotion}%</td>
									<td class="bg_c"><p title="${productMatch.productName }" class="title">${productMatch.productName }</p><p>用户数：${productMatch.matchCustomNum }</p></td>
									<td class="bg_r"></td>
									<input type="hidden" class="productIds" value="${productMatch.productId}" />
								</tr>
							  </c:forEach>
							</table>
							<!--手工配置产品结束 -->
						</td>
					</tr>
				</table>
				<!--超出提示 -->
				<div class="warnBox" >
					<dl>
						<dt><span class="close"></span><span class="clear"></span></dt>
						<dd>您最多可选 5 个产品！</dd>
					</dl>
				</div>
			</div>		
		</div>
	</dd>
	<div class="analyse_right_btns">
	<input type="button" id="initSaveMatchdialog" class="tag_btn" value="保存营销策略" />
    </div>
    <form id="returnForm" action="<%=request.getContextPath()%>/ci/marketingStrategyAction!marketingStrategy.ai2do" method="post">
        <input type="hidden" name="customGroup.returnPage" value="${customGroup.returnPage}"/>
        <input type="hidden" name="customGroup.productsIdsStr" id="productsIdsStr" value="${customGroup.productsIdsStr}"/>
        <input type="hidden" name="customGroup.productsStr" id="productsStr" value="${customGroup.productsStr}"/>
        <input type="hidden" name="customGroup.listTableName" id="listTableName" value="${customGroup.listTableName}"/>
        <input type="hidden" name="customGroup.dataDate" id="dataDate" value="${customGroup.dataDate}"/>
        <input type="hidden" name="customGroup.productDate" id="productDate" value="${customGroup.productDate}"/>
        <input type="hidden" name="customGroup.customGroupId" id="customGroupId" value="${customGroup.customGroupId}"/>
        <input type="hidden" name="customGroup.customGroupName" id="customGroupName" value="${customGroup.customGroupName}"/>
        <input type="hidden" name="customGroup.customNum" value="${customGroup.customNum}" />
        <input type="hidden" name="customGroup.customMatch" value="${customGroup.customMatch}"/>
     </form>
</div>
</body>
</html>