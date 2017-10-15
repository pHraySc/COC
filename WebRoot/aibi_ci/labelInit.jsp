<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="include.jsp"%>
<html>
	<head>
		<%@ include file="html_include.jsp"%>
	<script type="text/javascript">

	var month;
	var day;

	function getDataDate() {
		month = $('#month').val();
		day = $('#day').val();
	}
	
	//标签数据初始化
	function initLabelData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initLabelData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算月标签统计数据
	function initMonthLabelStatData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initMonthLabelStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算月标签环比
	function initMonthLabelRingData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initMonthLabelRingData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算日标签环比
	function initDayLabelRingData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initDayLabelRingData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算日标签统计数据
	function initDayLabelStatData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initDayLabelStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算月客户群统计数据
	function initMonthCustomGroupStatData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initMonthCustomGroupStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算月客户群环比增长量
	function initMonthCustomGroupRingData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initMonthCustomGroupRingData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算日客户群统计数据
	function initDayCustomGroupStatData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initDayCustomGroupStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//将月统计数据各维度为null的数据更新为0
	function updateDwNullDataTo0ForMonth(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!updateDwNullDataTo0ForMonth.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//将日统计数据各维度为null的数据更新为0
	function updateDwNullDataTo0ForDay(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!updateDwNullDataTo0ForDay.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算月标签占比数据
	function initMonthLabelProportionData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initMonthLabelProportionData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算日标签占比数据
	function initDayLabelProportionData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initDayLabelProportionData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//更新标签最新用户数
	function initLabelTotalCustomNum(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initLabelTotalCustomNum.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//增量添加标签
	function increaseLabelData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!increaseLabelData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算月标签使用次数数据
	function initMonthLabelUseTimesStatData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initMonthLabelUseTimesStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//计算日标签使用次数数据
	function initDayLabelUseTimesStatData(){
		getDataDate();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initDayLabelUseTimesStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//初始化客户群不存在的数据
	function initCustomGroupNotExistStatData(){
		getDataDate();
		var customGroupId = $('#customGroupId').val();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!initCustomGroupNotExistStatData.ai2do",
			type: "POST",
			dataType: "json",
			data:'&month='+month+'&day='+day+'&customGroupId='+customGroupId,
			success: function(result){
				alert(result.msg);
			}
		});

	}

	//测试新增标签
	function addNewLabelTest(){
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!addNewLabelTest.ai2do",
			type: "POST",
			dataType: "json",
			success: function(result){
				alert(result.msg);
			}
		});
	}

	function toEncrypt() {
		var desInput = $('#desInput').val();
		$.ajax({
			url: "${ctx}/ci/sysDataInitAction!toEncrypt.ai2do",
			type: "POST",
			dataType: "json",
			data:{"desInput":desInput},
			success: function(result){
				$('#desResult').html(result.desResult);
			}
		});
	}
		
	</script>

</head>

<body class="body_bg">
<br>
<br>
<br>
<br>
    <div>
    	月份：<input id="month" value="201404"/>
    	日期：<input id="day" value="20140430"/>
    	<br>
    	<!-- 
    	客户群ID：<input id="customGroupId" value=""/>
    	 -->
    </div>
    <br>
    <div class="analyse_right_btns">
    	<input type="button" class="tag_btn" value="初始化标签数据" onClick="initLabelData();" />
    	<input type="button" class="tag_btn" value="增量添加标签数据" onClick="increaseLabelData();" />
    	<input type="button" class="tag_btn" value="更新标签最新用户数" onClick="initLabelTotalCustomNum();"/>
    	<!-- 
    	<input type="button" class="tag_btn" value="更新月数据null字段为0" onClick="updateDwNullDataTo0ForMonth();"/>
    	<input type="button" class="tag_btn" value="更新日数据null字段为0" onClick="updateDwNullDataTo0ForDay();"/>
        
        <br>
        <br>
        <input type="button" class="tag_btn" value="计算月标签统计数据" onClick="initMonthLabelStatData();"/>
        <input type="button" class="tag_btn" value="计算日标签统计数据" onClick="initDayLabelStatData();"/>
        <input type="button" class="tag_btn" value="计算月标签占比数据" onClick="initMonthLabelProportionData();"/>
        <input type="button" class="tag_btn" value="计算日标签占比数据" onClick="initDayLabelProportionData();"/>
        <br>
        <br>
        <input type="button" class="tag_btn" value="计算月标签环比数据" onClick="initMonthLabelRingData();"/>
        <input type="button" class="tag_btn" value="计算日标签环比数据" onClick="initDayLabelRingData();"/>
        <input type="button" class="tag_btn" value="计算月客户群统计数据" onClick="initMonthCustomGroupStatData();"/>
        <br>
        <br>
        <input type="button" class="tag_btn" value="计算日客户群统计数据" onClick="initDayCustomGroupStatData();"/>
        <input type="button" class="tag_btn" value="计算月客户群环比增长量" onClick="initMonthCustomGroupRingData();"/>
        <input type="button" class="tag_btn" value="计算月标签使用次数数据" onClick="initMonthLabelUseTimesStatData();"/>
        <input type="button" class="tag_btn" value="计算日标签使用次数数据" onClick="initDayLabelUseTimesStatData();"/>
        <br>
        <br>
        <input type="button" class="tag_btn" value="初始化客户群不存在的数据" onClick="initDayLabelUseTimesStatData();"/>
        <input type="button" class="tag_btn" value="测试用sequence新增标签" onClick="addNewLabelTest();"/>
       -->
        <br>
        <br>
        
         <br>
        输入要加密的字符串：
        <input type="text" style="border-color: black;border-bottom-width: 2px;width:200px;" id="desInput" />
        &nbsp;&nbsp;&nbsp;
        <input type="button" class="tag_btn" value="加密" onClick="toEncrypt();"/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <br>
        <br>
        	<span style="float: right;margin-right:400px;">结果:</span>
        	<br>
		<div id="desResult" style="float: right;margin-right:50px;"></div>
        
    </div>

</body>
</html>
