<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>保存营销策略</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		$(window.parent).scrollTop(0);
		//表格斑马线
		aibiTableChangeColor(".showTable");
		//标题名称提示
		$("#select1 input").click(function(){
			if($(this).val()=="请输入策略名称"){
				$(this).val("");
			}
		});
		
		$(".add_policyDiv .new_tag_input").each(function(){
			$(this).width($(this).parents("li").width()-10);
		});
		$(".add_policyDiv .new_tag_textarea").each(function(){
			$(this).width($(this).parents("li").width()-4);
		});
		function loadpolicy(){
			$(".showMove_div .add_policyDiv").removeClass("add_policyDiv_border");
			var length=$(".showMove_div .add_policyDiv").length;
			if((length%2==0)&&(length>2)){
				$(".showMove_div .add_policyDiv:not(:last):not(:eq("+(length-2)+"))").addClass("add_policyDiv_border");
			}else if((length%2==1)&&(length>2)){
				$(".showMove_div .add_policyDiv:not(:last)").addClass("add_policyDiv_border");
			}
		};
		loadpolicy();
		
		$(".showMove_div .add_policyDiv .analyse_dt_con").click(function(e){
			openConfirm($(this));
		});
		
	});

	//打开营销策略页的confirm
	function openConfirm(_t){
		var tar=$("#confirm_div");
		tar.dialog({
			autoOpen: false,
			width: 500,
			height:185,
			title:"系统提示",
			modal: true,
			resizable:false
		});
		tar.dialog( "open" );
		tar.find("#confirm_true" ).unbind("click").bind("click",function(){
			_t.parents(".add_policyDiv").remove();
			tar.dialog( "close" );
		});
		tar.find("#confirm_false" ).click(function(){
			tar.dialog( "close" );
		})
	}

	
	
	//全量营销
	function marketing(){
		var actionUrl = "${ctx}/ci/marketingStrategyAction!initSaveMarketing.ai2do";
		$('#precisionMarketing').val("false");
		$('#saveForm').attr("action",actionUrl);
		$('#saveForm').submit();
	}
	//同步输入策略名称
	function insertNameRow(){
		var tacticName = $('#tacticName').val();
		$('#nameTip').empty();
		$('.add_policyDiv').each(function(){
			$(this).find('#tnameTip').empty();
			var productName = $(this).find('#productName').val();
			$(this).find('.new_tag_input').val(tacticName + '_' + productName);
		});
		if(tacticName == ""){
			$('.add_policyDiv').each(function(){
				$(this).find('.new_tag_input').val("");
			});
		}
	}
	//同步输入策略描述
	function insertDescRow(){
		var tacticDesc = $('#tacticDesc').val();
		$('.add_policyDiv').each(function(){
			$(this).find('.new_tag_textarea').val(tacticDesc);
		});
		if(tacticDesc == ""){
			$('.add_policyDiv').each(function(){
				$(this).find('.new_tag_textarea').val("");
			});
		}
	}
	
	//保存
	function submitList(){
		var tacticNames = "";
		var productsIdsStr = "";
		var compareTacticName = "";
		var existCount = 0;
		var istNameExist = true;
		var iscNameExist = true;
		var isNameLong = true;
		$('.add_policyDiv').each(function(){
			var isTacticNameExist = true;
			var isCustomNameExist = true;
		 	var tacticName = $(this).find('.new_tag_input').val();
		 	if(tacticName == ""){
		 		$(this).find('#tnameTip').empty();
		 		$(this).find('#tnameTip').show().append("请输入策略名称");
		 		return false;
		 	}
		 	if(tacticName.length >35){
		 		$(this).find('#tnameTip').empty();
		 		$(this).find('#tnameTip').show().append("策略名称长度不能超过35个字符");
		 		isNameLong = false;
		 	}

		 	//判断页面上的策略名称互相是否重名
		 	$('.add_policyDiv').each(function(){
		 		var eachTacticName = $(this).find('.new_tag_input').val();
		 		if(compareTacticName == eachTacticName){
		 			existCount ++;
		 		}
		 	});
		 	
		 	if(existCount > 1){
		 		$(this).find('#tnameTip').empty();
		 		$(this).find('#tnameTip').show().append("策略名称重名");
				return false;
			}
			
		 	existCount = 0;
		 	
		 	var productId = $(this).find('#productId').val();
		 	tacticNames = tacticNames + tacticName + ",";
		 	productsIdsStr = productsIdsStr + productId + ",";
		 	//判断页面上的策略名称是否重名
		 	var actionUrl = '$.ctx/ci/marketingStrategyAction!isNameExit.ai2do';
			$.ajax({
				url: actionUrl,
				type: "POST",
				data:{
					"marketTactics.tacticName":tacticName
				},
				async: false,
				success: function(result){
				if(!result){
					isTacticNameExist = false;
					istNameExist = false;
				  }
				}
			});
			if(!isTacticNameExist){
				$(this).find('#tnameTip').empty();
		 		$(this).find('#tnameTip').show().append("策略名称重名");
			}

			//判断页面上的策略名称跟客户群是否重名
			var isNameExitUrl = $.ctx+'/ci/customersManagerAction!isNameExist.ai2do';
			$.ajax({
				url: isNameExitUrl,
				type: "POST",
				data:{
					"ciCustomGroupInfo.customGroupName":tacticName
				},
				async: false,
				success: function(result){
				if(!result){
					isCustomNameExist = false;
					iscNameExist = false;
				  }
				}
			});
			if(!isCustomNameExist){
				$(this).find('#tnameTip').empty();
		 		$(this).find('#tnameTip').show().append("策略名称跟客户群名称有重复");
			}
			
		});

		if(!isNameLong){
			window.parent.showAlert("策略名称长度不能超过35个字符","failed");
			return false;
		}
		
		if(!istNameExist){
			window.parent.showAlert("策略名称重名","failed");
			return false;
		}

		if(!iscNameExist){
			window.parent.showAlert("策略名称跟客户群名称重复","failed");
			return false;
		}

		if(tacticNames == ""){
			window.parent.showAlert("请返回上一步选择产品","failed");
			return false;
		}
		tacticNames = tacticNames.substring(0,tacticNames.length);
		$("#tacticNames").val(tacticNames);
		var tacticNames = encodeURIComponent(encodeURIComponent($("#tacticNames").val()));
		var tacticDesc = encodeURIComponent(encodeURIComponent($("#tacticDesc").val()));;
		var listTableName = $("#listTableName").val();
		var productsStr = $("#productsStr").val();
		var dataDate = $("#dataDate").val();
		var productDate = $("#productDate").val();
		var customGroupId = $("#customGroupId").val();
		var customGroupName = $("#customGroupName").val();
		var customMatch = $("#customMatch").val();
		var precisionMarketing = $("#precisionMarketing").val();

        var param = "marketTactics.tacticNames="+tacticNames;
        param = param+"&customGroup.listTableName="+listTableName;
        param = param+"&customGroup.productsStr="+productsStr;
        param = param+"&customGroup.productsIdsStr="+productsIdsStr;
        param = param+"&customGroup.dataDate="+dataDate;
        param = param+"&customGroup.customGroupId="+customGroupId;
        param = param+"&customGroup.productDate="+productDate;
        param = param+"&customGroup.customMatch="+customMatch;
        param = param+"&customGroup.precisionMarketing="+precisionMarketing;
		window.parent.marketingSave(param);

	}
	function returnPage(){
		var actionUrl = "";
		if($("#returnPage").val() == "customProductView"){
		   actionUrl = '$.ctx/ci/marketingStrategyAction!customProductView.ai2do';
		}else{
		   actionUrl = '$.ctx/ci/marketingStrategyAction!marketingStrategy.ai2do';
		}
		$("#saveForm").attr("action",actionUrl);
		$("#saveForm").submit();
	}
		
</script>
</head>

<body>
<div class="submenu">
	<div class="top_menu">
		<ul>
			<li><div class="bgleft">&nbsp;</div><a onclick="javascript:marketing()" class="no14"><span></span><p>全量推荐</p></a><div class="bgright">&nbsp;</div></li>
			<li class="top_menu_on"><div class="bgleft">&nbsp;</div><a class="no15"><span></span><p>精准营销</p></a><div class="bgright">&nbsp;</div></li>
			<div class="clear"></div>
		</ul>
	</div>
</div>
<form action="${ctx}/ci/marketingStrategyAction!saveMarketingStategy.ai2do" method="post" id="saveForm">
<div class="dialog_table" style="padding-top:0px">
<span class="analyse_dt_con" style="float: right;margin-top: 10px;margin-right: 10px"><a href="javascript:returnPage()" class="link_back" style="color:#5c9ad5; text-decoration:none"  >返回配置</a></span>
	<table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="2" style="padding-left:50px">
				统一添加策略名称或编辑单个策略信息
			</td>
		</tr> 
		<tr>
			<th width="110px"><span>*</span>策略名称：</th>
			<td>
				<div id="select1" class="new_tag_selectBox">
					<input name="" onkeyup="insertNameRow()" type="text" value="${customGroup.customGroupName}" id="tacticName" class="new_tag_input" style="cursor:text"/>
				</div>
				<!-- 提示1 -->
				<div class="tishi error" id="nameTip" style="display:none;">此项不能为空！</div>
			</td>
		</tr>
		<tr>
			<th>策略描述：</th>
			<td>
				<textarea name=""  id="tacticDesc" onkeyup="insertDescRow()" class="new_tag_textarea" style="height:50px;"></textarea>
			</td>
		</tr>
	</table>
</div>
<!-- 审批流程 -->
<div class="showMore_box">
	<div class="showMore">
		<div class="showMove_div" style="margin-top:0px">
		  <c:forEach items="${productInfoList}" var="productInfo">
			<div class="add_policyDiv">
				<ul>
					<li>
						<dl class="analyse_right_dl marginTop_0">
							<dt><span>${productInfo.productName }</span><span class="analyse_dt_con"><img src="<%=request.getContextPath()%>/aibi_ci/assets/themes/default/images/close_blue.png" width="17px" height="17px" /></span></dt>
							<dd>
								适用品牌：${productInfo.brandNames}
							</dd>
						</dl>
					</li>
					<li>策略名称</li>
					<li>
						<div id="select1" class="new_tag_selectBox">
							<input name="" type="text" id="" value="${customGroup.customGroupName}_${productInfo.productName }" class="new_tag_input" style="cursor:text"/>
						</div>
						<div id="tnameTip" class="tishi error"  style="display:none;"></div><!-- 用于重名验证 -->
					</li>
					<li>策略描述</li>
					<li>
						<textarea name="" class="new_tag_textarea" style="height:40px;"></textarea>
					</li>
				</ul>
				<input type="hidden" id="productName" value="${productInfo.productName }"/>
				<input type="hidden" id="productId" value="${productInfo.productId}"/>
			</div>
			</c:forEach>
			<div class="clear"></div>	
		</div>
	</div>
	<div class="showMove_control"><span>隐藏更多信息</span></div>
</div>
<input type="hidden" name="marketTactics.tacticNames" id="tacticNames"/>
<input type="hidden" name="customGroup.returnPage" id="returnPage" value="${customGroup.returnPage}"/>
<input type="hidden" name="customGroup.productsIdsStr" id="productsIdsStr" value="${customGroup.productsIdsStr}"/>
<input type="hidden" name="customGroup.productsStr" id="productsStr" value="${customGroup.productsStr }"/>
<input type="hidden" name="customGroup.listTableName" id="listTableName" value="${customGroup.listTableName}"/>
<input type="hidden" name="customGroup.dataDate" id="dataDate" value="${customGroup.dataDate}"/>
<input type="hidden" name="customGroup.productDate" id="productDate" value="${customGroup.productDate}"/>
<input type="hidden" name="customGroup.customGroupId" id="customGroupId" value="${customGroup.customGroupId}"/>
<input type="hidden" name="customGroup.customGroupName" id="customGroupName" value="${customGroup.customGroupName}"/>
<input type="hidden" name="customGroup.customNum" value="${customGroup.customNum}" />
<input type="hidden" name="customGroup.customMatch" value="${customGroup.customMatch}"/>
<input type="hidden" name="customGroup.precisionMarketing" id="precisionMarketing" value="${customGroup.precisionMarketing}"/>
<div class="dialog_btn_div like_normal_btndiv">
	<input name="" type="reset" value=" 重置" class="tag_btn"/>
	<input name="" type="button" onclick="submitList()" value=" 保 存 " class="tag_btn"/>
</div>
</form>

<div id="confirm_div" style="display:none;">
    <div class="dialog_table">
		<table width="100%" class="showTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="app_td" style="padding-top:0px;padding-bottom:20px;">
					<div class="confirm showtxt">确定删除此项？</div>
				</td>
			</tr>
		</table>
	</div>
    <!-- 按钮区 -->
    <div class="dialog_btn_div">
        <input id="confirm_true" name="" type="button" value=" 确 定 " class="tag_btn"/>
        <input id="confirm_false" name="" type="button" value=" 取 消 " class="tag_btn"/>
    </div>
</div>
</body>
</html>
