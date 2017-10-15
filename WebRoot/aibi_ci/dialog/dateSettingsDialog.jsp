<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<%@ include file="/aibi_ci/html_include.jsp"%>
	<script type="text/javascript">
	$(function(){
		var startTime =  '${param["startTime"]}';
		var endTime =  '${param["endTime"]}';
		var dateLeftClosed =  '${param["leftZoneSign"]}';
		var dateRightClosed =  '${param["rightZoneSign"]}';
		var queryWay = '${param["queryWay"]}';//区分是日期范围还是精确日期
		var exactValueDate = '${param["exactValue"]}'; //精确日期 yyyy,mm,dd 没有选的也填充为-1
		var isNeedOffset = '${param["isNeedOffset"]}';
		if(isNeedOffset == 1) {
			$('#dynamicUpdate').attr('checked', 'checked');
		}
		
		if(!queryWay){
			queryWay="1";
		}
		if(queryWay){
			$(".methodSel").each(function(){
				if($(this).attr("value") == queryWay){
					$(this).addClass("methodSelCurrent");
				}else{
					$(this).removeClass("methodSelCurrent");
				}
			});
			if(queryWay == "1"){
				$("input[name^='exactValueDate']").attr("disabled","disabled");
				$("#startTime").removeAttr("disabled");
				$("#endTime").removeAttr("disabled");
				$("#dateLeftClosed").removeAttr("disabled");
				$("#dateRightClosed").removeAttr("disabled");
			}else{
				$("#startTime").attr("disabled","disabled");
				$("#endTime").attr("disabled","disabled");
				$("#dynamicUpdate").attr("disabled","disabled");
				$("#dateLeftClosed").attr("disabled","disabled");
				$("#dateRightClosed").attr("disabled","disabled");
				$("input[name^='exactValueDate']").removeAttr("disabled");
			}
		}
		
		if(startTime){
			$("#startTime").val(startTime);
		}
		if(endTime){
			$("#endTime").val(endTime);
		}
		if(dateLeftClosed.indexOf("=")>=0){
			$("#dateLeftClosed").attr("checked" ,true);
		}
		if(dateRightClosed.indexOf("=")>=0){
			$("#dateRightClosed").attr("checked" ,true);
		}
		//精确值
		if(exactValueDate){
			var itemValueArr = exactValueDate.split(",");
			if(itemValueArr.length == 3){
				var exactValueDateYear = itemValueArr[0];
				var exactValueDateMonth = itemValueArr[1];
				var exactValueDateDay =itemValueArr[2];
				if(exactValueDateYear && exactValueDateYear != "-1"){
					$("#exactValueDateYear").val(exactValueDateYear);
				}
				if(exactValueDateMonth && exactValueDateMonth != "-1"){
					$("#exactValueDateMonth").val(exactValueDateMonth);
				}
				if(exactValueDateDay && exactValueDateDay != "-1"){
					$("#exactValueDateDay").val(exactValueDateDay);
				}
			}
		}
		//日期单选按钮选项
		$(".methodSel").click(function(){
			$(".methodSel").removeClass("methodSelCurrent");
			$(this).addClass("methodSelCurrent");
			var queryWay = $(this).attr("value");
			if(queryWay == "1"){
				$("input[name^='exactValueDate']").attr("disabled","disabled");
				$("#startTime").removeAttr("disabled");
				$("#endTime").removeAttr("disabled");
				$("#dynamicUpdate").removeAttr("disabled");
				$("#dateLeftClosed").removeAttr("disabled");
				$("#dateRightClosed").removeAttr("disabled");
			}else{
				$("#startTime").attr("disabled","disabled");
				$("#endTime").attr("disabled","disabled");
				$("#dynamicUpdate").attr("disabled","disabled");
				$("#dateLeftClosed").attr("disabled","disabled");
				$("#dateRightClosed").attr("disabled","disabled");
				$("input[name^='exactValueDate']").removeAttr("disabled");
			}
			$("#snameTip").hide();
		})
		$(".methodSel").hover(function(){
			$(this).addClass("methodSelHover");
		},function(){
			$(this).removeClass("methodSelHover");
		});
		
	});
	function validateForm(){
		var queryWay="";
		$(".methodSel").each(function(){
			if($(this).hasClass("methodSelCurrent")){
				queryWay= $(this).attr("value")
			}
		});
		if(queryWay == "1"){
			var startTime = $.trim($("#startTime").val());
			var endTime= $.trim($("#endTime").val());
			var timeStr=startTime+endTime;
			if($.trim(timeStr) == ""){
				$("#snameTip").empty();
				$("#snameTip").show().append("时间段至少一个不能为空");
				return false;
			}
		}else{
			var exactValueDateYear = $.trim($("#exactValueDateYear").val());
			var exactValueDateMonth = $.trim($("#exactValueDateMonth").val());
			var exactValueDateDay = $.trim($("#exactValueDateDay").val());
			var exactValueDate = exactValueDateYear+exactValueDateMonth+exactValueDateDay;
			
			if(exactValueDate == ""){
				$("#snameTip").empty();
				$("#snameTip").show().append("精确日期至少一个不能为空！");
				return false;
			}
			//年
			if(exactValueDateYear !=""){
				if(isNaN(exactValueDateYear)){
					$("#snameTip").empty();
					$("#snameTip").show().append("年份格式不符合要求！");
					return false;
				}
			}else{
				exactValueDateYear = 1990;//给定年固定值
			}
			//月
			if(exactValueDateMonth !=""){
				if(isNaN(exactValueDateMonth)){
					$("#snameTip").empty();
					$("#snameTip").show().append("月份格式不符合要求！");
					return false;
				}
			}else{
				exactValueDateMonth = 7;//给定月固定值
			}
			//日
			if(exactValueDateDay !=""){
				if(isNaN(exactValueDateDay)){
					$("#snameTip").empty();
					$("#snameTip").show().append("日格式不符合要求！");
					return false;
				}
			} else {
				exactValueDateDay = 20;//给定日固定值
			}
			var year = parseInt(exactValueDateYear);   
			var month = parseInt(exactValueDateMonth, 10)-1;              
			var day = parseInt(exactValueDateDay, 10); 
			var date = new Date(year, month, day);       
			var y = date.getFullYear();              
			var m = date.getMonth();              
			var d = date.getDate();       
			if ((y != year) || (m != month) || (d != day)) {                  
				$("#snameTip").empty();
				$("#snameTip").show().append("请输入合法的日期！");
				return false;
			}  
		}
			return true;
	}
	 	
		function submitSetDate(){
			if(validateForm()){
				var queryWay ="";
				$(".methodSel").each(function(){
					if($(this).hasClass("methodSelCurrent")){
						queryWay= $(this).attr("value")
					}
				});
				var startTime = "";
				var endTime= "";
				var dateLeftChecked= "";
				var leftZoneSign="";
				var rightZoneSign="";
				var exactValueDate="";
				var temp = "";
				var isNeedOffset = 0;
				if(queryWay == "1"){
					var isNeedOffsetVal = $('#dynamicUpdate').attr('checked');
					if(isNeedOffsetVal) {
						isNeedOffset = 1;	
					}
					
					startTime = $("#startTime").val();
					endTime= $("#endTime").val();
					dateLeftChecked=$("#dateLeftClosed").attr("checked");
					if(dateLeftChecked){
						leftZoneSign=">=";
						if(startTime !=""){
							temp += "大于等于"+startTime;
						}
					}else{
						leftZoneSign=">";
						if(startTime !=""){
							temp += "大于"+startTime;
						}
					}
					var dateRightChecked=$("#dateRightClosed").attr("checked");
					if(dateRightChecked){
						rightZoneSign="<=";
						if(endTime != ""){
							temp += "小于等于"+endTime;
						}
					}else{
						rightZoneSign="<";
						if(endTime != ""){
							temp += "小于"+endTime;
						}
					}
					if(isNeedOffsetVal) {
						temp += "（按此日期动态偏移1天/1月）";
					}
			}else{
				var exactValueDateYear = $.trim($("#exactValueDateYear").val());
				var exactValueDateMonth = $.trim($("#exactValueDateMonth").val());
				var exactValueDateDay = $.trim($("#exactValueDateDay").val());
				if(exactValueDateYear && exactValueDateYear !=""){
					temp += exactValueDateYear+"年";
					exactValueDate=exactValueDateYear;
				}else{
					exactValueDate="-1";
				}
				if(exactValueDateMonth && exactValueDateMonth !=""){
					temp += exactValueDateMonth+"月";
					exactValueDate += ","+exactValueDateMonth;
				}else{
					exactValueDate += ",-1";
				}
				if(exactValueDateDay && exactValueDateDay !=""){
					temp += exactValueDateDay+"日";
					exactValueDate += ","+exactValueDateDay;
				}else{
					exactValueDate += ",-1";
				}
			}
			
			parent.$("._idClass").parent().parent().attr("startTime",startTime);
			parent.$("._idClass").parent().parent().attr("endTime",endTime);
			parent.$("._idClass").parent().parent().attr("leftZoneSign",leftZoneSign);
			parent.$("._idClass").parent().parent().attr("rightZoneSign",rightZoneSign);
			parent.$("._idClass").parent().parent().attr("exactValue",exactValueDate);
			parent.$("._idClass").parent().parent().attr("queryWay",queryWay);
			parent.$("._idClass").parent().parent().attr("isNeedOffset",isNeedOffset);

			var $conditionMore =  parent.$("._idClass").parent().siblings(".conditionMore");
			var p = $("<p><label>已选择条件：</label><span>"+temp+"</span></p>");
			var title_temp ="已选择条件：" + temp;
			$conditionMore.html('').append(p);
			$conditionMore.attr("title",title_temp);
			
			parent.$("._idClass").removeClass("_idClass");
			parent.submitRules();
			parent.iframeCloseDialog("#dateSettings");
		}
	}
	</script>
</head>
<body>
    <div class="date-setting-box">
    <dl>
		<dt>
			<div class="methodSel methodSelCurrent" value="1">日期范围</div>
		</dt>
		<dd>
			<div>
				<p class="fleft">开始时间</p>
				<input type="text" id="startTime" class="fleft dateInputFmt80" readonly="readonly" 
						onclick="if($('#endTime').val()!=''){WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}else{WdatePicker({dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})}" 
						value="${pojo.startTime}"/>&nbsp;
				<p class="fright rPadding88Margin3">
				 	<input type="checkbox" id="dateLeftClosed" class="fleft" value="1"/>
                    <label for="dateLeftClosed" class="fleft">包含当前值</label>
				</p>
            </div>
        </dd>
    </dl>
	<dl>
		<dt>
			&nbsp;
		</dt>
			<dd>
			<div>
				<p class="fleft ">结束时间</p>
				<input type="text" name="pojo.endTime" id="endTime" readonly="readonly"  
                	class="fleft dateInputFmt80"  value="${pojo.endTime}"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',dateFmt:'yyyy-MM-dd',isShowToday:true,isShowClear:true,readOnly:true})" 
						/>
				<p class="fright rPadding88Margin3">
					<input type="checkbox" class="fleft" id="dateRightClosed" value="1"/>
                	<label for="dateRightClosed" class="fleft">包含当前值</label>
            	</p>
            </div>
        </dd>
       </dl>
       <dl>
		<dt>
			&nbsp;
		</dt>
		<dd>
            <p class="fleft">
				<input type="checkbox" class="fleft" id="dynamicUpdate" value="1"/>
               <label for="dynamicUpdate" class="fleft">按此日期动态偏移1天/1月</label>
             </p>
         </dd>
       </dl>
       <dl>
		<dt>
			<div class="methodSel"  value="2">数值范围</div>
		</dt>
		<dd>
			<input type="text" value="" maxlength="4" id="exactValueDateYear"  name="exactValueDateYear" class="fleft inputFmt60"                     />
			<span class="fleft padding10">年</span>
			<input type="text" value="" maxlength="2" id="exactValueDateMonth" name="exactValueDateMonth" class="fleft inputFmt60"                     />
			<span class="fleft padding10">月</span>
			<input type="text" value="" maxlength="2" id="exactValueDateDay" name="exactValueDateDay" class="fleft inputFmt60"                     />
			<span class="fleft padding10">日</span>
		</dd>
       </dl>
       <dl>
		<dd>
			<!--<p class="cred">错误信息显示</p>
			--><div id="snameTip" class="tishi error" style="display:none;"></div>
		</dd>
       </dl>
       </div>
    <!-- 按钮操作-->
    <div class="btnActiveBox">
        <input type="button" value="确定" class="ensureBtn" onclick="submitSetDate();"/>
    </div>
     <input type="hidden" name="pojo.valueId" id="valueId"/>
	 <input type="hidden" name="pojo.sortNum" id="sortNum"/>
</body>
</html>