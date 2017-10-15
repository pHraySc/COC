<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.ailk.biapp.ci.model.CiShopSessionModel"%>
<%@ include file="/aibi_ci/include.jsp"%>
<%@ page import="java.util.Iterator"%>
<%String num = (String)session.getAttribute("calcElementNum");%>
<c:set value="<%=num %>" var="shopCartListNum"/>
<!-- 购物车-->
<div class="shopCartBox J_show_cart_box" id="shopCartBox">
	<div class="shopCartLetter" id="shopCartLetter">
		<div class="shopCartPointer"></div>
		<div class="shopCartEmptyTip" id="shopCartEmptyTip"> 我的篮子是空的!</div>
		<div id="shopCartGoodsListBox"  class="shopCartGoodsListBox">
			<div class="shopCartGoodsListTitle">
			    <a href="javascript:void(0);" onclick="clearLabelsBox()" class="fleft  emptyShopCart"   id="emptyShopCart" >清空</a>
				<a href="javascript:void(0);" onclick="openCalculateCenter()" class="fright skipTagCalc"  id="skipTagCalc" >创建客户群</a>
			</div>
			<div id="shopCartGoodsList" class="shopCartGoodsList">
				<ol></ol>
			</div>
		</div>
	</div>
	
	<div class="shopCartInfoBox">
	    <div class="fleft shopCartInfo" id="shopCartInfo" >
		    <div class="shopCartIcon fleft"></div>
				<% if (num==null) { %>
					<p class="fleft basket" id="shopBasket">
						收纳篮 (<em id="shopNumBasket">0</em>)
					</p>
					<p class="fleft hidden" id="shopBasketSelected">
						<label> 已选</label>
				   		<strong  id="shopNum">0 </strong>
				   		<label>个</label>
					</p>
				<% } else{ %>
					<p class="fleft basket" id="shopBasket">
						收纳篮 (<em id="shopNumBasket">${shopCartListNum }</em>)
					</p>
					<p class="fleft hidden" id="shopBasketSelected">
						<label> 已选</label>
				   		<strong id="shopNum">${shopCartListNum }</strong>
				   		<label>个</label>
					</p>
				<% } %>
		</div>
		<div class="fleft shopCartActive" >
		    <a href="javascript:openCalculateCenter();">创建客户群</a>
		</div>
	</div>
</div>
<script type="text/javascript">

$(document).ready(function() {
	//购物车显示详情
	shopCarList();
})

$(document).load(function(){
	//购物车定位
	var bodyWidth = $("body").width();
	$("#shopCartBox").css("right",parseInt((bodyWidth-1005)/2,10));
	//购物车中商品删除
	$(".shopCartGoodsActive").click(function(){
		$(this).parents("li").remove();
	});
});

//清空所有计算元素
function clearLabelsBox(){
	var innerHtml = $("#shopNum").html();
	if(innerHtml == 0){
		return;
	}
	commonUtil.create_alert_dialog("confirmDialog", {
		 "txt":"确定要清空吗？",
		 "type":"confirm",
		 "width":500,
		 "height":200
	},"clearCalElesInCart");
}
//清空收纳篮
function clearCalElesInCart(){
	var clearCarShopItemUrl = $.ctx + '/ci/ciIndexAction!delAllCarShopSession.ai2do';
	$.post(clearCarShopItemUrl, {}, function(result) {
		$('#shopCartGoodsList ol').empty();
		$("#shopNum").html(0);
		$("#shopNumBasket").html(0);
		$("#shopTopNum").html(0);
		
		$("#shopCartGoodsListBox").hide();
		$("#shopCartEmptyTip").show();
		
		$("#shopBasket").show(); //购物篮隐藏
		$("#shopBasketSelected").hide(); //已选购物篮显示
	});
}
</script>