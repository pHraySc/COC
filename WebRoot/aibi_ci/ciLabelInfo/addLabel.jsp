<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String labelSubmit = Configure.getInstance().getProperty("LABEL_SUBMIT"); 
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新建标签</title>
<%@ include file="/aibi_ci/html_include.jsp"%>
<script language="javaScript" src="${ctx}/aibi_ci/assets/js/jquery.validate.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		//审批流程展示隐藏
		$(".approve_box .approve_control").click(function(){
			if($(this).children("span").html() == "审批流程展示"){
				$(this).siblings(".approve").children(".approve_step").show();
				$(this).children("span").html("审批流程隐藏");
				$(this).children("span").addClass("up_arr");
			}else{
				$(this).siblings(".approve").children(".approve_step").hide();
				$(this).children("span").html("审批流程展示");
				$(this).children("span").removeClass("up_arr");
			};
		});
		if(getCookieByName("${version_flag}") == "true"){
 			$("#sceneListLi").hide();
 	 	} else {
 	 		$("#sceneListLi").show();
 	 	}
		var labelId = '${param["labelId"]}';
		//标签修改
		if(labelId != null && labelId != "") {
			var actionUrl = $.ctx + "/ci/ciLabelInfoAction!getLabelInfo.ai2do";
			$.ajax({
				type: "POST",
				url: actionUrl,
				data: "labelId="+labelId,
				success: function(result){
					$("#labelId").val(result.ciLabelInfo.labelId);
					$("#labelName").val(result.ciLabelInfo.labelName);
					$("#busiCaliber").val(result.ciLabelInfo.busiCaliber);
					$("#failTime").val(result.ciLabelInfo.failTime);
					$("#updateCycle").val(result.ciLabelInfo.updateCycle);
					if(result.ciLabelInfo.updateCycle == 2 && result.ciLabelInfo.failTime.length == 10){
						$("#failTimeShow").val(result.ciLabelInfo.failTime.substring(0,7));
					}else {
						$("#failTimeShow").val(result.ciLabelInfo.failTime);
					}
					if(result.ciLabelInfo.updateCycle == 2){
						$("#monthCreate").attr("checked",true);
						$("#failTimeShow").unbind("click");
						$("#failTimeShow").click(function(){
							 WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM',minDate:'%y-{%M+1}',maxDate:'2037-12'})
						 });
					}else if(result.ciLabelInfo.updateCycle == 1){
						$("#dayCreate").attr("checked",true);
						$("#failTimeShow").unbind("click");
						$("#failTimeShow").click(function(){
							WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d+1}',maxDate:'2037-12-31'})
						 });
					}
					$("#applySuggest").val(result.ciLabelInfo.applySuggest);
					$("#busiLegend").val(result.ciLabelInfo.busiLegend);
					$("#createDesc").val(result.ciLabelInfo.createDesc);
					$("#updateCycle").multiselect({
						multiple: false,
						minWidth: 317,
						height: "auto",
						header: false,
						selectedList: 1
					});
					$("#avgScore").val(result.ciLabelInfo.avgScore);
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
		}
		
		//标题名称提示
		//标签名称默认值提示     和      标签名称为空提示
		var labeNameDefaultVal = $("#labelName").val();
		$("#labelName").focusblur();
		
		aibiSelectSearch2("#search_img","#divPop1");
        
		$("#saveBtn").bind("click", function(){
			var actionUrl = $.ctx + "/ci/ciLabelInfoAction!save.ai2do";
			submitList(actionUrl,"save");
		});
		$("#savaAndSubmit").bind("click", function(){
			var actionUrl = $.ctx + "/ci/ciLabelInfoAction!saveAndSubmitApprove.ai2do";
			submitList(actionUrl,"saveAndSubmit");
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
		//新建标签默认为月周期，时间框默认需要选择月
		if(labelId == null || labelId == ""){
			$("#failTimeShow").click(function(){
				 WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM',minDate:'%y-{%M+1}',maxDate:'2037-12'})
			 });
		}	

		$("input[type='radio'][name='createLabelInfo.updateCycle']").change(function(){
			if($(this).val() == 1){
				$("#failTimeShow").val("");
				$("#failTime").val("");
				$("#failTimeShow").unbind("click");
				$("#failTimeShow").click(function(){
					 WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d+1}',maxDate:'2037-12-31'})
				});
			}else if($(this).val() == 2){
				$("#failTimeShow").val("");
				$("#failTime").val("");
				$("#failTimeShow").unbind("click");
				$("#failTimeShow").click(function(){
					 WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM',minDate:'%y-{%M+1}',maxDate:'2037-12'})
			    });
			}
		});
		
		$("#scenesListBox li a").click(function(ev){
			if($(this).find("input").attr("checked")){
				$(this).find("input").attr("checked",false)
			}else{
				$(this).find("input").attr("checked",true)
			}
			selItemFn($(this).find("input"),'sceneIds');
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
		
		$("#scenesListBox li a input").click(function(ev){
			selItemFn($(this),'sceneIds' );
			var e = ev || event ;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=true;
		});
		
		$("#scenesTxt").click(function(){
	    	var open = $("#scenesListBox").attr("open");
	    	if(!open){
	        	$("#scenesListBox").slideDown().attr("open",true);
	        	$("#scenesBtn").addClass("selBtnActive");
	    	}else{
	    	    $("#scenesListBox").slideUp().removeAttr("open");
	            $("#scenesBtn").removeClass("selBtnActive");
	    	}
	    	return false;
	    });
		//绑定隐藏提示层
		$(document).click(function(e){
		    var e=e||window.event;
			e.stopPropagation?e.stopPropagation():e.cancelBubble=false;
			$("#scenesListBox").slideUp().removeAttr("open");
			$(".selBtn").removeClass("selBtnActive");
		});
		
		//模拟下拉框--打开方式 
	    $("#scenesBtn").click(function(){
	    	var open = $("#scenesListBox").attr("open");
	    	if(!open){
	    	    $("#scenesListBox").slideDown().attr("open",true);
	    	    $(this).addClass("selBtnActive");
	    	}else{
				$(this).next("#scenesListBox").slideUp().removeAttr("open");
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
		});
		
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
		});

	});
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
					var html = '';
					$.each(result, function(i, record) {
						html="<li title='"+record.labelName
						+"' busiCaliber='"+record.busiCaliber
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
					
					//依据 input 跟 button宽度设定下拉框的宽度
					$(divBox).width($(btn).prev("input").width() + 8); 
					//单击空白区域隐藏弹出层 
					$(document).click(function (event) { $(divBox).hide(speed) }); 
					//设定下拉选单显示的位置 
					$(divBox).css({ "top":50, "left": 110,"width":$("#labelName").width() + 10}); 
					//切换弹出层的显示状态 
					$(divBox).toggle(speed); 

					$(divBox + " ul li:odd").addClass("odd");
					$(divBox + " ul li").click(function(){
						$(divBox + " ul li").removeClass("on");
						$(this).addClass("on");
						$("#labelName").val($(this).text());
						$("#busiCaliber").val($(this).attr("busiCaliber"));
						$("#failTime").val($(this).attr("failTime"));
						
						$("#updateCycle").val($(this).attr("updateCycle"));
						if($("#updateCycle").val() == 2 && $("#failTime").val().length == 10){
							$("#failTimeShow").val($("#failTime").val().substring(0,7));
						}else {
							$("#failTimeShow").val($("#failTime").val());
						}
						if($("#updateCycle").val() == 2){
							$("#failTimeShow").unbind("click");
							$("#failTimeShow").click(function(){
								 WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM',minDate:'%y-{%M+1}',maxDate:'2037-12'})
							 });
						}else if($("#updateCycle").val() == 1){
							$("#failTimeShow").unbind("click");
							$("#failTimeShow").click(function(){
								WdatePicker({onpicked:function(){setFailTime();},dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d+1}',maxDate:'2037-12-31'})
							 });
						}
						$("#applySuggest").val($(this).attr("applySuggest"));
						$("#busiLegend").val($(this).attr("busiLegend"));
						$("#createDesc").val($(this).attr("createDesc"));
						$("#updateCycle").multiselect({
							multiple: false,
							minWidth: 317,
							height: "auto",
							header: false,
							selectedList: 1
						});
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
					}else if(result.msgType == "successSave") {
						openSaveSuccess(operation,"" + result.msgContent,result.labelName,result.labelId);
					}
				}
			});
		}
	}

	function validateForm(){
		var name = $.trim($("#labelName").val());
		if(name == "请输入标签名称"){
			$("#labelName").val("");
		}else{
			$("#labelName").val(name);
		}

		var failTimeShow = $.trim($("#failTimeShow").val());
		if(failTimeShow == ""){
			$("#failTimeShow").val("");
		}else{
			$("#failTimeShow").val(failTimeShow);
		}
		
		var	validater = $("#saveForm").validate({
			rules: {
				"createLabelInfo.labelName": {
					required: true
				},
				"createLabelInfo.busiCaliber": {
					required: true
				},
				"failTimeShow": {
					required: true
				}
				
			},
			messages: {
				"createLabelInfo.labelName": {
					required: "标签名称不允许为空!"
				},
				"createLabelInfo.busiCaliber": {
					required: "业务口径不允许为空!"
				},
				"failTimeShow": {
					required: "标签失效日期不允许为空!"
				}
			},
			debug:true,
			errorPlacement:function(error,element) {
				element.next("div").hide();
				element.after(error);
			},
			success:function(element){
				element.remove();
			},
			errorElement: "div",
			errorClass: "tishi error"
		});
		if(!validater.form()){
			return false;
		}
		return true;
	}
	//打开成功弹出层，保存成功窗口需要自定义
	function openSaveSuccess(operation,successMsg,labelName,labelId){
		var msgType;
		if(successMsg.indexOf("修改")>=0){
			msgType = encodeURIComponent(encodeURIComponent("修改"));
		}else{
			msgType = encodeURIComponent(encodeURIComponent("保存"));
		}
		successMsg = encodeURIComponent(encodeURIComponent(successMsg));
		labelName = encodeURIComponent(encodeURIComponent(labelName));
		labelId = encodeURIComponent(encodeURIComponent(labelId));
		var ifmUrl;
		if(operation == "save"){
			ifmUrl= "${ctx}/aibi_ci/ciLabelInfo/saveLabelSuccDialog.jsp?successMsg="+successMsg+"&labelName="+labelName+"&labelId="+labelId+"&msgType="+msgType;
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
		parent.closeAddLabelDialog(); 
	}
	function setFailTime(){
		if($("#failTimeShow").val().length == 7){
			$("#failTime").val($("#failTimeShow").val()+"-01");
		}else{
			$("#failTime").val($("#failTimeShow").val());
		}
	}
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
</script>
</head>

<body>
<form id="saveForm" method="post">
<input type="hidden" id="labelId" name="createLabelInfo.labelId" />
<input type="hidden" id="avgScore" name="createLabelInfo.avgScore"/>
<div class="clientMsgBox pt30" >
	<ol class="clearfix">
		<li>
			<label  class="fleft labFmt100 star">标签名称：</label>
   			<input id="labelName" name="createLabelInfo.labelName" type="text"  class="fleft inputFmt305" 
   				 maxlength="40" value="请输入标签名称" />
   			<div id="divPop1" class="x-form-field"><ul></ul></div>
   			<span id="search_img" class="fleft btn_img_addPage search_img_addPage margin0"></span>
			<!-- 提示 --> 
			<div id="sameNameTip" class="tishi error" style="display:none;"></div><!-- 用于重名验证 -->
		</li>
		<li id="sceneListLi">
			<label  class="fleft labFmt100">场景分类：</label>
   			<div class="fleft selBox">
				<div class="selTxt" id="scenesTxt" style="overflow:hidden;">请选择场景分类</div>
				<a href="javascript:void(0);" class="selBtn" id="scenesBtn"></a>
				<div class="selListBox" id="scenesListBox">
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
			<input type="hidden" name="sceneIds" id="sceneIds" value="${sceneIds}" />
		</li>
		<li>
			<label  class="fleft labFmt100 star">业务口径：</label>
			<textarea class="fleft textarea456" name="createLabelInfo.busiCaliber" id="busiCaliber" onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}" 
				onblur="this.value = this.value.slice(0,341)" >${createLabelInfo.busiCaliber}</textarea>
            <div id="nameTip"  class="tishi error" style="display:none;"></div>
		</li>
		<li>
			<label  class="fleft labFmt100 star">更新周期：</label>
   			 <div class="fleft">
					<input type="radio" id="dayCreate"  name="createLabelInfo.updateCycle"  value="1"  />&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" id="monthCreate" name="createLabelInfo.updateCycle"  value="2" checked />&nbsp;月
			</div>
		</li>
		<li>
			<label  class="fleft labFmt100 star">失效日期：</label>
   			<div class="new_tag_selectBox">
					<input id="failTimeShow" name="failTimeShow" type="text" value="" readonly class="new_tag_input tag_date_input"/>
					<input id="failTime" name="createLabelInfo.failTime" type="hidden" value=""/>
				</div>
				<div class="tishi error" style="display:none;">请输入开始日期及结束日期</div>		
		</li>
		<li>
			<label  class="fleft labFmt100">应用建议：：</label>
			<textarea class="fleft textarea456" name="createLabelInfo.applySuggest" id="applySuggest" onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}" 
				onblur="this.value = this.value.slice(0,341)" >${createLabelInfo.applySuggest}</textarea>
		</li>
		<li>
			<label  class="fleft labFmt100">业务说明：</label>
			<textarea class="fleft textarea456" name="createLabelInfo.busiLegend" id="busiLegend" onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}" 
				onblur="this.value = this.value.slice(0,341)" >${createLabelInfo.busiLegend}</textarea>
		</li>
		<li>
			<label  class="fleft labFmt100">备注：</label>
			<textarea class="fleft textarea456" name="createLabelInfo.createDesc" id="createDesc" onkeyup="if(this.value.length>341){this.value = this.value.slice(0,341)}" 
				onblur="this.value = this.value.slice(0,341)" >${createLabelInfo.createDesc}</textarea>
		</li>
	</ol>
</div>
<!-- 按钮区 -->
<div class="btnActiveBox">
	<%
	if(!"false".equals(labelSubmit)){
	%>
	<a href="javascript:void(0);" id="savaAndSubmit" name="savaAndSubmit" class="ensureBtnLong">保存并提交审批</a>
	<%
	}
	%>
	<a href="javascript:void(0);" id="saveBtn" name="saveBtn"   class="ensureBtn">保存</a>
</div>
</form>	
<!-- 审批流程 -->
<div class="approve_box">
	<div class="approve">
		<div class="approve_step" style="display:none">
			<div class="step step1">
				<ul>
					<li>创建标签</li>
					<li>部门领导审批</li>
					<li>信息部审核</li>
					<li>数据处理</li>
					<li>创建成功</li>
				</ul>
			</div>
			
		</div>
	</div>
	<div class="approve_control"><span>审批流程展示</span></div>
</div>

<div id="successDialog">
	<iframe id="successFrame" name="successFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
</body>
</html>
