<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>标签配置-新增标签</title>
<%
	String labelSubmit = Configure.getInstance().getProperty("LABEL_SUBMIT"); 
%>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$(document).click(function(e){
		    var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
			$("#selListBox").slideUp().removeAttr("open");
			$(".selBtn").removeClass("selBtnActive");
		});
		var labelId = '${param["labelId"]}';
		//控制页面下拉之后navigation.jsp不显示浮动搜索域
		$("#isCreateCustomPage").val("true");
		//更新周期
 		$("#createPeriodBox div").bind('click', changeCycle);
 		if(getCookieByName("${version_flag}") == "true"){
 			$("#sceneListLi").hide();
 	 	} else {
 	 		$("#sceneListLi").show();
 	 	}
		//标签修改
		if(labelId != null && labelId != "") {
			var actionUrl = $.ctx + "/ci/ciLabelInfoAction!getLabelInfo.ai2do";
			$('.customer-title').html("修改标签");
			document.title = "标签配置-修改标签"; 
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: "labelId=" + labelId,
				success: function(result){
					$("#labelId").val(result.ciLabelInfo.labelId);
					$("#labelName").val(result.ciLabelInfo.labelName);
					$("#labelBusiCaliber").val(result.ciLabelInfo.busiCaliber);
					$("#labelApplySuggest").val(result.ciLabelInfo.applySuggest);
					$("#labelBusiLegend").val(result.ciLabelInfo.busiLegend);
					$("#labelCreateDesc").val(result.ciLabelInfo.createDesc);
					//$("#dataStatusId").val(result.ciLabelInfo.dataStatusId);
				}
			});
			
			//分类参数回显start
		    var selBakArr = $("#sceneIds").val().split(",");
		    var firstItemVal = $("#firstItem_active").val();
		    var selListInput = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
		    var titleArr = [];
		    for(var i = 0;i< selBakArr.length ;i++){
		        //公共场景
		        if(selBakArr[i] == firstItemVal){
		            titleArr.push($("#firstItem_active").attr("_sceneName"));
		            $("#firstItem_active").attr("checked",true);
		            break;
		        }else{
		            for(var j=0;j<selListInput.length ;j++){
		                if($(selListInput[j]).val() == selBakArr[i]){
		                   titleArr.push($(selListInput[j]).attr("_sceneName"));
		                   $(selListInput[j]).attr("checked",true);
		                   break ; 
		                }
		            }
		        }
		    }
		    $("#scenesTxt").html(titleArr.join(",")).attr("title",titleArr.join(","));
		}//标签修改初始化页面结束

		//标签名称默认值提示     和      标签名称为空提示
		var labeNameDefaultVal = $("#labelName").val();
		$("#labelName").focusblur();
		$("#labelName").blur(function () {
			var labelName = $.trim($(this).val());
			$("#sameNameTip").empty();
			$("#sameNameTip").hide();
	 		if(labelName == "" || labelName == labeNameDefaultVal) {
	 			$("#labelNameTip").next("div").hide();//屏蔽掉验证框架提示
	 			$("#labelNameTip").empty();
	 			$("#labelNameTip").show().append("标签名称不允许为空！");
	 			$(document).scrollTop(20);
	 			return false;
	 		} else {
	 			$("#labelNameTip").empty();
	 			$("#labelNameTip").hide();
	 		}
 		});//标签名称默认值提示     和      标签名称为空提示
 		//业务口径为空提示
		$("#labelBusiCaliber").blur(function () {
			var labelBusiCaliber = $.trim($(this).val());
	 		if(labelBusiCaliber == "") {
	 			$("#labelBusiCaliberTip").next("div").hide();//屏蔽掉验证框架提示
	 			$("#labelBusiCaliberTip").empty();
	 			$("#labelBusiCaliberTip").show().append("业务口径不允许为空！");
	 			$(document).scrollTop(20);
	 			return false;
	 		} else {
	 			$("#labelBusiCaliberTip").empty();
	 			$("#labelBusiCaliberTip").hide();
	 		}
 		});//业务口径为空提示
		
		$("#saveBtn").bind("click", function(){
			var actionUrl = $.ctx + "/ci/ciLabelInfoConfigAction!save.ai2do";
			submitList(actionUrl, "save");
		});
		$("#savaAndConfig").bind("click", function(){
			var actionUrl = $.ctx + "/ci/ciLabelInfoConfigAction!save.ai2do";
			submitList(actionUrl,"saveAndConfig");
		});

		$("#updateCycle").multiselect({
			multiple: false,
			minWidth: 317,
			height: "auto",
			header: false,
			selectedList: 1
		});

		$("#successDialog").dialog({
			autoOpen: false,
			width: 620,
			title: "系统提示",
			modal: true,
			resizable:false
		});
		
		aibiSelectSearch2("#search_img","#divPop1");
		
		$("#selListBox li a").click(function(ev){
			if($(this).find("input").attr("checked")){
				$(this).find("input").attr("checked",false)
			}else{
				$(this).find("input").attr("checked",true)
			}
			selItemFn($(this).find("input"),'sceneIds');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
		$("#selListBox li a input").click(function(ev){
			selItemFn($(this),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
		$("#scenesTxt").click(function(){
	    	var open = $("#selListBox").attr("open");
	    	if(!open){
	    		$("#snameTip").hide();
	        	$("#selListBox").slideDown().attr("open",true);
	        	$("#selBtn").addClass("selBtnActive");
	    	}else{
	    	    $("#selListBox").slideUp().removeAttr("open");
	            $("#selBtn").removeClass("selBtnActive");
	    	}
	    	return false;
	    });
		
		//模拟下拉框--打开方式 
	    $("#selBtn").click(function(){
	    	var open = $("#selListBox").attr("open");
	    	if(!open){
	    		$("#snameTip").hide();
	    	    $("#selListBox").slideDown().attr("open",true);
	    	    $(this).addClass("selBtnActive");
	    	}else{
				$(this).next("#selListBox").slideUp().removeAttr("open");
				$(this).removeClass("selBtnActive");
	    	}
	    	return false;
	    });
	    $(".selAllBox").click(function(ev){
			if($("#selBox_all").attr("checked")){
				$("#selBox_all").attr("checked",false)
			}else{
				$("#selBox_all").attr("checked",true)
			}
			selItemAllFn($("#selBox_all"),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		$(".selAllBox input").click(function(ev){
			selItemAllFn($("#selBox_all"),'sceneIds');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
	  	//阻止事件冒泡
		$(".firstItem").click(function(ev){
			firstItemFn($("#firstItem_active"), 'sceneIds');
			var e = ev||event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		})
		
	});
	
	//多选下拉框全选
	function selItemAllFn(obj,selId){
		$("#firstItem_active").attr("checked",false);
		var $obj =$(obj);
		var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
		var _id =obj.attr("id");
		var flag = document.getElementById(_id).checked;
		var valArr = [];
		var textArr = [];
		$.each($inputArr,function(index ,item){
			$(item).attr("checked",flag);
			valArr.push($(item).val());
			var $labelText = $.trim($(item).attr("_sceneName"));
			textArr.push($labelText);
		});
		if(flag){
			$("#" +selId ).val(valArr.join(","));
			$("#scenesTxt").html(textArr.join(","));
			$("#scenesTxt").attr("title",textArr.join(","));
		}else{
			$("#" +selId ).val("");
			$("#scenesTxt").html("");
			$("#scenesTxt").attr("title","");
		}
	}
	//第一个参数
	function firstItemFn(obj,selId){
		//var $inputArr = $(obj).parent().next("ol").find("input");
		var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
		$.each($inputArr,function(index ,item){
			$(item).attr("checked",false);
		})
		$("#selBox_all").attr("checked",false);
		if($(obj).attr("checked")){
			var itemVal =  $(obj).val();
			var optionArr =$("#"+selId).val(itemVal);
			var $labelText = $.trim($(obj).attr("_sceneName"));
			$("#scenesTxt").html($labelText);
			$("#scenesTxt").attr("title",$labelText);
		}else{
			$("#"+selId).val("");
			$("#scenesTxt").html("");
			$("#scenesTxt").attr("title","");
		}
	}
	function selItemFn(obj,selId){
		//全选和第一复选框不选中
		$("#firstItem_active").attr("checked",false);
		$("#selBox_all").attr("checked",false);
		//去掉第一个参数 
		//var $inputArr  = $(obj).parents("ol").find("input");
		var $inputArr = $("input[type='checkbox'][name='_sceneBoxItemCheckList']");
		var valArr = []; 
		var textArr = [];
		$.each($inputArr,function(index ,item){
			if($(item).attr("checked")){
				valArr.push($(item).val());
				var $labelText = $.trim($(item).attr("_sceneName"));
				textArr.push($labelText);
			}
		})
		$("#"+selId).val(valArr.join(","));
		$("#scenesTxt").html(textArr.join(","));
		$("#scenesTxt").attr("title",textArr.join(","));
	}
	//切换更新周期
	function changeCycle() {
		var dateFmt = 'yyyy-MM';
		var minDate = '%y-{%M+1}';
		var _this = $(this);
		var cycle = _this.attr('cycle');
		
		$("#createPeriodBox div").removeClass();
		_this.addClass("current");
		var defaultFailDate = '2099-12-01';
		
		if(cycle == 1) {//日周期
			$('#labelUpdateCycle').val(1);
			dateFmt = 'yyyy-MM-dd';
			minDate = '%y-%M-{%d+1}';
		} else if(cycle == 2) {//月周期
			$('#labelUpdateCycle').val(2);
			dateFmt = 'yyyy-MM';
			minDate = '%y-{%M+1}';
			defaultFailDate = '2099-12';
		}
		
		$("#failTimeShow").val(defaultFailDate);
		$("#failTime").val(defaultFailDate);
		$("#failTimeShow").unbind("click");
		$("#failTimeShow").click(function(){
			 WdatePicker({onpicked:function(){setFailTime();}, dateFmt:dateFmt, minDate:minDate})
	    });
	}
	
	//查询标签，以做标签复制
	function aibiSelectSearch2(btn,divBox){
		//动画速度 
		var speed = 200;
		var width = $(btn).prev("input").width() + $(btn).width(); 
		$(btn).parent("div").width(width+15);
		$(btn).click(function (event) {
			var actionUrl = $.ctx + "/ci/ciLabelInfoAction!findSimilarityLabelNameList.ai2do";
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: $("#saveForm").serialize(),
				success: function(result){
					$(divBox+" ul").empty();
					$(divBox).css({height : 200});
					var html = '';
					$.each(result, function(i, record) {
						html="<li title='"+record.labelName
						+"' busiCaliber='"+(record.busiCaliber == null ? '' : record.busiCaliber)
						+"' failTime='"+record.failTime
						+"' updateCycle='"+record.updateCycle
						+"' applySuggest='"+ (record.applySuggest == null ? '' : record.applySuggest)
						+"' busiLegend='"+ (record.busiLegend == null ? '' : record.busiLegend)
						+"' createDesc='"+ (record.createDesc == null ? '' : record.createDesc)
						+"'>"+record.labelName+"</li>";
						//$(divBox+" ul").empty();
						$(divBox+" ul").append(html);
					});
					//取消事件冒泡 
					event.stopPropagation(); 
					//设置弹出层位置 
					var offset = $("#labelNameP").offset();  
					//单击空白区域隐藏弹出层 
					$(document).click(function (event) { $(divBox).hide(speed) });  
					//切换弹出层的显示状态 
					if($(divBox).is(":hidden")){
						$(divBox).css({ "top": 30, "left": 120,"width":$("#labelName").width() + 4}).show(speed); 
					}else{
						$(divBox).hide(speed); 
					} 
					$(divBox + " ul li:odd").addClass("odd");
					$(divBox + " ul li").click(function(){
						$(divBox + " ul li").removeClass("on");
						$(this).addClass("on");
						$("#labelName").val($(this).text()); 
						
						$("#labelBusiCaliber").val($(this).attr("busiCaliber"));
						
						$("#labelApplySuggest").val($(this).attr("applySuggest"));
						$("#labelBusiLegend").val($(this).attr("busiLegend"));
						$("#labelCreateDesc").val($(this).attr("createDesc"));
					});
				}
			});
			
		});
		
	};
	
	function submitList(actionUrl,operation){
		if(validateForm()){
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: $("#saveForm").serialize(),
				success: function(result){
					if(result.msgType == "sameLabel") {
						$("#sameNameTip").empty();	
						$("#sameNameTip").show().append(result.msgContent);		
					} else if(result.msgType == "successSave") {
						$("#sameNameTip").empty();
						$("#sameNameTip").hide();
						if(operation == "saveAndConfig") {
							configLabel(result.labelId);
						} else {
							showAlert('标签保存成功！', 'success', function() {
								showLabelConfig(4);
							});
						}
					}
				}
			});
		}
	}

	function validateForm(){
		var result = true;
		var name = $.trim($("#labelName").val());
		if(name == "请输入标签名称"){
			$("#labelName").val("");
		}else{
			$("#labelName").val(name);
		}

		if($.trim($("#labelName").val()) == "") {
			$("#labelNameTip").empty();
			$("#labelNameTip").show().append("标签名称不允许为空！");
			result = false;
		}
		if($.trim($("#labelBusiCaliber").val()) == "") {
			$("#labelBusiCaliberTip").empty();
			$("#labelBusiCaliberTip").show().append("业务口径不允许为空！");
			result = false;
		}
		
		return result;
	}
	//打开成功弹出层，保存成功窗口需要自定义
	function openSaveSuccess(operation,successMsg,labelName,labelId){
		successMsg = encodeURIComponent(encodeURIComponent(successMsg));
		labelName = encodeURIComponent(encodeURIComponent(labelName));
		labelId = encodeURIComponent(encodeURIComponent(labelId));
		var ifmUrl;
		if(operation == "save"){
			ifmUrl= "${ctx}/aibi_ci/ciLabelInfo/saveLabelSuccDialog.jsp?successMsg="+successMsg+"&labelName="+labelName+"&labelId="+labelId;
		}else{
			ifmUrl= "${ctx}/aibi_ci/ciLabelInfo/saveAndSubLabelSuccDialog.jsp?successMsg="+successMsg+"&labelName="+labelName+"&labelId="+labelId;
		}
		$("#successFrame").attr("src", ifmUrl).load(function(){$("#successDialog").dialog("open");});
	}
	//通用的关闭弹出层：根据dialogId直接关闭dialog
	//success 和 error的提示页面都调用该js来关闭提示框
	//由于是在新增标签页中关闭提示，所以一并需要把新增标签框都关闭
	function closeDialog(dialogId){
		$(dialogId).dialog("close");
		window.location.href = "${ctx}/ci/ciLabelInfoAction!labelManageIndex.ai2do?showType=4";
	}
	function setFailTime(){
		if($("#failTimeShow").val().length == 7){
			$("#labelFailTime").val($("#failTimeShow").val()+"-01");
		}else{
			$("#labelFailTime").val($("#failTimeShow").val());
		}
	}

	//进入标签配置页面
	function configLabel(labelId) {
		var url = $.ctx + "/ci/ciLabelInfoConfigAction!configLabelIndex.ai2do?configLabelInfo.labelId="+labelId;
		window.location = url;
	}

</script>
</head>

<body>
<jsp:include page="/aibi_ci/navigation.jsp" ></jsp:include>
<form id="saveForm" method="post">
	<input type="hidden" id="labelId" name="createLabelInfo.labelId" />
	<input type="hidden" id="dataStatusId" name="createLabelInfo.dataStatusId"/>
	<div class="container">
		<div class="save-customer-header comWidth clearfix">
			<div class="logoBox clearfix">
				<div class="index_logo fleft" onclick="showHomePage();return false;"></div>
				<div class="secTitle fleft">标签配置</div>
			</div>
		</div>
		<div class="comWidth clearfix save-customer-inner">
			<div class="clearfix titleBox">
				<div class="starIcon fleft"></div>
				<div class="customer-title fleft">新增标签</div> 
			</div>
			<div class="clientBaseList clearfix">
				<ul>
					<li class="clearfix name-box">
						<p class="fleft fmt150">
							<span class="star fleft"></span>
							<label>标签名称：</label>
						</p>
						<p id="labelNameP" class="fleft inputBox">
							<input type="text" name="createLabelInfo.labelName" value="请输入标签名称" id="labelName" 
								maxlength="40"/>
						</p>
						<div id="divPop1" class="x-form-field"><ul></ul></div>
						<p id="search_img" class="fleft btn_img_addPage search_img_addPage"></p>
						<div id="labelNameTip" class="tishi-page error" style="display:none;"></div>
						<div id="sameNameTip" class="tishi-page error" style="display:none;"></div><!-- 用于重名验证 -->
					</li>
					<li class="clearfix" id="sceneListLi">
						<div class="fleft fmt150">
							<label>场景分类：</label>
						</div>
					      <div class="fleft selBox">
			             	  <div class="selTxt" id="scenesTxt" style="overflow:hidden;">请选择场景分类</div>
			             	  <a href="javascript:void(0);" class="selBtn" id="selBtn"></a>
			             	  <div class="selListBox" id="selListBox">
			             	     <div class="firstItem">
			             	     	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
										<c:if test="${dimScenes.sortNum == 0}">
											<input type="checkbox" value="${dimScenes.sceneId}"  id="firstItem_active" name="_sceneBoxItemCheckShare" _sceneName="${dimScenes.sceneName}"/>
											<label for="firstItem_active">${dimScenes.sceneName}</label>
										</c:if>
									</c:forEach>
			             	  	 </div>
			             	  	 <ol>
			             	  	 	<c:forEach var="dimScenes" items="${dimScenes}" varStatus="st">
			             	  	 		<c:if test="${dimScenes.sortNum > 0}">
				             	  	 	 <li>
				             	  	 	 	<a href="javascript:void(0);" >
				             	  	 	 		<input type="checkbox" id="selBoxItem0${st.index}" name="_sceneBoxItemCheckList"  
				             	  	 	 			value="${dimScenes.sceneId}" _sceneName="${dimScenes.sceneName}"/>
				             	  	 	 		${dimScenes.sceneName}
				             	  	 	 	</a>
				             	  	 	 </li>
				             	  	 	 </c:if>
			             	  	 	</c:forEach>
			             	  	 </ol>
			             	  	 <div class="selAllBox">
			             	  	 	 <input type="checkbox" id="selBox_all">全选</input>
			             	  	 </div>
			             	  </div>
			             </div>
						<div id="snameTip" class="tishi-page error"  style="display:none;"></div><!-- 非空验证提示 -->
						<input type="hidden" name="sceneIds" id="sceneIds" value="${sceneIds}" />
					</li>
					<li class="clearfix">
						<p class="fleft fmt150">
							<span class="star fleft"></span>
							<label>业务口径：</label>
						</p>
						<p class="fleft textareaBoxShort">
							<textarea id="labelBusiCaliber" name="createLabelInfo.busiCaliber" 
								onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}"
								 onblur="this.value = this.value.slice(0,341)"></textarea>
						</p>
						<div id="labelBusiCaliberTip" class="tishi-page error" style="display:none;"></div>
					</li>
					<li class="clearfix">
						<p class="fleft fmt150">
							<label>应用建议：</label>
						</p>
						<p class="fleft textareaBox">
							<textarea id="labelApplySuggest" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" 
								onblur="this.value = this.value.slice(0,170)" name="createLabelInfo.applySuggest" ></textarea>
						</p> 
					</li>
					<li class="clearfix">
						<p class="fleft fmt150">
							<label>业务说明：</label>
						</p>
						<p class="fleft textareaBox">
							<textarea id="labelBusiLegend" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" 
								onblur="this.value = this.value.slice(0,170)" name="createLabelInfo.busiLegend" ></textarea>
						</p> 
					</li>
					<li class="clearfix">
						<p class="fleft fmt150">
							<label>备注：</label>
						</p>
						<p class="fleft textareaBox">
							<textarea id="labelCreateDesc" onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}" 
								onblur="this.value = this.value.slice(0,170)" name="createLabelInfo.createDesc" ></textarea>
						</p> 
					</li>
				</ul>
			</div>
			<div class="customer-bottom-content clearfix">
				<div class="tagSaveBtnBox">
					<input id="savaAndConfig" name="savaAndConfig" type="button" value="保存并发布" />
					<input id="saveBtn" name="saveBtn" type="button" value="保存" />
					<a href="javascript:void(0);" onclick="showLabelConfig(4);return false;" class="prevBtn fright" id="prevCalculateBtn">返回标签配置</a>
				</div>
			</div>
		</div>
	</div>
</form>	

<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>

</body>
</html>
