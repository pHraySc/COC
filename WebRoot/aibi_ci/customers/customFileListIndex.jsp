<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html>
	<head>
		<title>清单下载</title>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#keyWordGroupName").focusblur({ 
					"focusColor":"#222232" ,
				    "blurColor":"#757575"  
				});
				//设置子窗口中提示框相对于子窗口的位置，比较诡异：子窗口中的位置却要在父窗口中设定
				$(".tishi").each(function(){
					var toffset=$(this).parent("td").offset();
					var td_height=$(this).parent("td").height();
					$(this).css({top:toffset.top + td_height + 13, left: toffset.left}); 
				});
				search();
				$("#searchBtn").click(function(){
					search();
				});
			 //模拟下拉框
	        $(".selBtn ,.selTxt").click(function(){
	            if($(this).nextAll(".querySelListBox").is(":hidden")){
	            	$(this).nextAll(".querySelListBox").slideDown();
	            	if($(this).hasClass("selBtn")){
	                    $(this).addClass("selBtnActive");
	                }else{
	                	$(this).next(".selBtn").addClass("selBtnActive");
	                }
	           }else{
	                	 $(this).nextAll(".querySelListBox").slideUp();
	                     $(this).removeClass("selBtnActive");

	                }
	        	return false;
	        })
			
	        $(".querySelListBox a").click(function(){
        	var selVal = $(this).attr("value");
        	var selHtml = $(this).html();
        	 $(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
        	 $(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
        	 $(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
        	//向隐藏域中传递值
           $(this).parents(".querySelListBox").slideUp();
             return false;
        })
          $(document).click(function(ev){
             var e = ev||event ;
             e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
             $(".querySelListBox").slideUp();
             $(".selBtn").removeClass("selBtnActive");
        })
        
        var canManageDeclare = $("#canManageDeclare").val();
		if (canManageDeclare) {
			$("#add_feedback_div").hide();
		}			
	});

	function pagetable() {
    	var actionUrl = 'customersFileAction!customFileList.ai2do?' + $("#pageForm").serialize();
    	$("#declareList").page( {url:actionUrl,param:$("#queryForm").serialize(),objId:'declareList',callback: pagetable });
		datatable();
    }
    function datatable() {
    	$(".commonTable mainTable").datatable();	
    }
    
	jQuery.focusblur = function (focusid) {
		var focusblurid = $(focusid);
	    var defval = focusblurid.val();
	    focusblurid.focus(function (){
	    	var thisval = $(this).val();
	        if (thisval == defval){
	        	$(this).val("");
	        	$(this).attr("flag","true");
	        }
		});
	    focusblurid.blur(function () {
	        var thisval = $(this).val();
	        if (thisval == "") {
	            $(this).val(defval);
	            $(this).attr("flag","false");
	        }
	    });
	}
	
	//查询反馈
	function search(){
		$(window).scrollTop(0);
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate != null && startDate != "" && endDate != null && endDate != "" && startDate>endDate){
			showAlert("开始时间不能大于结束时间","failed");
			return false;
		}
		var keyWord = $("#keyWordGroupName").val();
        if ("false" == $("#keyWordGroupName").attr("data-val")) {
        	keyWord = "";
        }
        $("#customGroupName").val(keyWord);
		var actionUrl = '${ctx}/ci/customersFileAction!customFileList.ai2do?' + $("#pageForm").serialize();
		$.ajax({
	    	url:actionUrl,
	        type:"POST",
	        dataType: "html",
	        data: $("#queryForm").serialize(),
	        success:function(html){
	        	$("#declareList").html("");
	            $("#declareList").html(html);
	            pagetable();
	            datatable();
	    	}
        });
	}
	</script>
</head>
<body>
<div class="comWidth pathNavBox pathNavTagBox">
	     <span class="pathNavHome"  style="cursor:default;"> 清单下载</span>
	     <span class="pathNavArrowRight" id="pathNavArrowRightMyCustom"></span>
	     <span class="pathNavNext" id="pathCustomText"></span>
	  <span class="amountItem"> 共<span class="amountNum" id="totalSizeNum"></span>个清单 </span>
	</div>
<div class="aibi_search" style="height:35px">
	<form id="queryForm" action="" method="post">
		<ul>
			<li><label  class="fleft">客户群关键字：</label>
					<input  type="text" class="search_input fleft" id="keyWordGroupName" data-val="false" value="模糊查询" />
					<input  type="hidden" name="ciCustomListInfo.customGroupName" id="customGroupName" />
			</li>
			<li>
					<div class="fleft formQuerySelBox">
			      	  <div class="selTxt">全部生成周期</div>
			      	  <a href="javascript:void(0);" class="selBtn"></a>
			      	  <div class="querySelListBox">
			      	     <input type="hidden" value="" name="ciCustomListInfo.updateCycle" id="updateCycle_"/>
			      	  	 <dl>
			      	  	 	<dd>
			      	  	 		<a href="javascript:void(0);"  value="">全部生成周期</a>
			      	  	 		<a href="javascript:void(0);" value="1">一次性</a>
			      	  	 		<a href="javascript:void(0);" value="3">日周期</a>
			      	  	 		<a href="javascript:void(0);" value="2">月周期</a>
			      	  	 	</dd>
			      	  	 </dl>
			      	  </div>
					</div>
					<input name="ciCustomGroupInfo.updateCycle" id="updateCycle" type="hidden"/>
					</li>
			<li>
		     <span class="line2_span">清单生成时间：</span>
		     <input style="cursor: pointer;" name="ciCustomListInfo.startDate" id="startDate" readonly type="text" class="defineDataInput datepickerBG"  
		     onclick="if($('#endDate').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}"/> -- 
			 <input style="cursor: pointer;" name="ciCustomListInfo.endDate" id="endDate" readonly type="text" class="defineDataInput datepickerBG"
			 onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})"/>
			</li>
			<li><a href="javascript:void(0);" id="searchBtn" class="queryBtn-bak">查询</a></li>
		</ul>
		<div class="clear"></div>
		</form>
</div>

<div class="aibi_area_data">
    <jsp:include page="/aibi_ci/customers/customFileList.jsp" /> 
</div>
	</body>
</html>