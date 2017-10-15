<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<script type="text/javascript">
			$(document).ready(function(){
				//模拟下拉框
				$(".querySelListBox a").click(function(){
					var selVal = $(this).attr("value");
					var selHtml = $(this).html();
					$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
					$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
					$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
					//向隐藏域中传递值
					$(this).parents(".querySelListBox").slideUp();
						return false;
				});
				$(".selBtn ,.selTxt").click(function(){
					//下拉框互斥 start
					var selfIndex;
					if($(this).hasClass("selBtn")){
						selfIndex = $(".selBtn").index((this));
					}else{
						selfIndex = $(".selTxt").index((this));
					}
					$.each($(".selBtn"),function(index,item){
						if(index != selfIndex){
							$(this).next(".querySelListBox").slideUp();
						}
					});
					$.each($(".selTxt"),function(index,item){
						if(index != selfIndex){
							$(this).next(".querySelListBox").slideUp();
						}
				    });
				  	//下拉框互斥 end
				  	
					$("#firstLevelTaskTip").hide();
					$("#secondLevelTaskTip").hide();
					$("#firstLevelSceneTip").hide();
					//$("#secondLevelSceneTip").hide();
					//一级营销任务没有选择 则不能选择二级营销任务、一级场景分类、二级场景分类
					//二级营销任务没有选择 则不能选择一级场景分类、二级场景分类
					//一级场景分类没有选择 则不能选择二级场景分类
					var firstLevelTaskFlag = !!($("#firstLevelTask").val().length == 0);
					var secondLevelTaskFlag = !!($("#secondLevelTask").val().length == 0);
					var firstLevelSceneFlag = !!($("#firstLevelScene").val().length == 0);
					if($(this).hasClass("secondLevelTask")){
						if(firstLevelTaskFlag){
							commonUtil.create_alert_dialog("showAlertDialog", {
								"txt":"请先选择一级营销任务",
								"type":"failed",   
								"width":500,
								"height" :200
							}); 
							return;
						}else{
							var firstLevelTaskVal = $("#firstLevelTask").val();
							var actionUrl = $.ctx + "/ci/market/ciMarketAction!findSecondTasks.ai2do";
						    $.ajax({
						        url: actionUrl,
						        type: "POST",
						        data: {
						            "taskId" : firstLevelTaskVal
						        },
						        success: function(result){
						            flag = result.success;
						            if(flag){
						            	var secondLevelTasks = result.secondLevelTasks;
						            	$("#secondLevelTaskList").html("<a href='javascript:void(0);'  value=''>请选择二级营销任务</a>");
						            	for(var index=0; index<secondLevelTasks.length; index++) {
						            		var marketTaskId = secondLevelTasks[index].marketTaskId;
						            		var marketTaskName = secondLevelTasks[index].marketTaskName;
						            		//拼二级营销任务下拉框
						            		var secondLevelTask = "<a href='javascript:void(0);' value='"+marketTaskId+"'>"+marketTaskName+"</a>";
						            		$("#secondLevelTaskList").append(secondLevelTask)
						            	}
						            	$("#secondLevelTaskList a").click(function(){
											var selVal = $(this).attr("value");
											var selHtml = $(this).html();
											$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
											$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
											$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
											//向隐藏域中传递值
											$(this).parents(".querySelListBox").slideUp();
											return false;
										});
						            }else{
						            	commonUtil.create_alert_dialog("showAlertDialog", {
											"txt":"根据一级营销任务查询二级营销任务出错",
											"type":"failed",   
											"width":500,
											"height" :200
										}); 
						            }
						        }
						    });
						}
					}else if($(this).hasClass("firstLevelScene")){
						if(secondLevelTaskFlag){	
							commonUtil.create_alert_dialog("showAlertDialog", {
								"txt":"请先选择二级营销任务",
								"type":"failed",   
								"width":500,
								"height" :200
							}); 
							return;
						}
						
					}
					/* else if($(this).hasClass("secondLevelScene")){
						if(firstLevelSceneFlag){
							commonUtil.create_alert_dialog("showAlertDialog", {
								"txt":"请先选择一级场景分类",
								"type":"failed",   
								"width":500,
								"height" :200
							}); 
							return;
						}else{
							var firstLevelSceneVal = $("#firstLevelScene").val();
						    var actionUrl = $.ctx + "/core/ciMarketAction!findSecondScenes.ai2do";
						    $.ajax({
						        url: actionUrl,
						        type: "POST",
						        data: {
						            "sceneId" : firstLevelSceneVal
						        },
						        success: function(result){
						            flag = result.success;
						            if(flag){
						            	var secondLevelScenes = result.secondLevelScenes;
						            	$("#secondLevelSceneList").html("<a href='javascript:void(0);'  value=''>请选择二级场景分类</a>");
						            	for(var index=0; index<secondLevelScenes.length; index++) {
						            		var sceneId = secondLevelScenes[index].sceneId;
						            		var sceneName = secondLevelScenes[index].sceneName;
						            		//拼二级场景下拉框
						            		var secondLevelScene = "<a href='javascript:void(0);' value='"+sceneId+"'>"+sceneName+"</a>";
						            		$("#secondLevelSceneList").append(secondLevelScene)
						            	}
						            	$("#secondLevelSceneList a").click(function(){
											var selVal = $(this).attr("value");
											var selHtml = $(this).html();
											$(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
											$(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
											$(this).parents(".querySelListBox").find("input[type='hidden']").val(selVal);
											//向隐藏域中传递值
											$(this).parents(".querySelListBox").slideUp();
											return false;
										});
						            }else{
						            	commonUtil.create_alert_dialog("showAlertDialog", {
											"txt":"根据一级场景分类查询二级场景分类出错",
											"type":"failed",   
											"width":500,
											"height" :200
										}); 
						            }
						        }
						    });
						}
					} */
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
				});
				$("#saveBtn").bind("click", function(){
					submitForm();
				});
			});
			
			function submitForm(){
				if(validateForm()){
					var actionUrl = $.ctx + "/ci/market/ciMarketAction!saveCustomOrLabelSceneRel.ai2do";
					$.post(actionUrl, $("#saveForm").serialize(), function(result){
						if(result.success){
							$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
								var showType = $("#showType").val();
								if(showType == 1){
									parent.multiLabelSearch();//刷新列表
								}else{
									parent.backMultiCustom();
								}
								parent.closeCreate();
							}});
						}else{
							commonUtil.create_alert_dialog("showAlertDialog", {
								"txt":result.msg,
								"type":"failed",   
								"width":500,
								"height" :200
							}); 
						}
					});
				}
			}

			function validateForm(){
				var firstLevelTaskFlag = !!($("#firstLevelTask").val().length == 0);
				var secondLevelTaskFlag = !!($("#secondLevelTask").val().length == 0);
				var firstLevelSceneFlag = !!($("#firstLevelScene").val().length == 0);
				//var secondLevelSceneFlag = !!($("#secondLevelScene").val().length == 0);
				if(firstLevelTaskFlag){
					$("#firstLevelTaskTip").html("一级营销任务不允许为空！").show();
					return false;
				}
				if(secondLevelTaskFlag){
					$("#secondLevelTaskTip").html("二级营销任务不允许为空！").show();
					return false;
				}
				if(firstLevelSceneFlag){
					$("#firstLevelSceneTip").html("场景分类不允许为空！").show();
					return false;
				}
				/* if(secondLevelSceneFlag){
					$("#secondLevelSceneTip").html("二级场景分类不允许为空！").show();
					return false;
				} */
				return true;
			}
		</script>
	</head>
	<body>
		<form id="saveForm" action="" method="post">
			<div class="clientMsgBox" >
				<ol class="clearfix">
					<li>			
						<label class="star fleft labFmt110">一级营销任务：</label>
						<div class="fleft formQuerySelBox recom_dialog" style="width: 150px;">
							<div class="selTxt firstLevelTask" style="width: 110px;" >请选择一级营销任务</div>
							<a href="javascript:void(0);" class="selBtn firstLevelTask"></a>
							<div class="querySelListBox zIndex" style="z-index: 1118px;">
								<input name="firstLevelTask" type="hidden" id="firstLevelTask" value=""/>
								<dl>
									<dd>
										<a href="javascript:void(0);"  value="">请选择一级营销任务</a>
										<c:forEach items="${firstLevelTasks }" var="firstLevelTask">
											<a href="javascript:void(0);" value="${firstLevelTask.marketTaskId }">${firstLevelTask.marketTaskName }</a>
										</c:forEach>
									</dd>
								</dl>
							</div>
						</div>
						<div id="firstLevelTaskTip" class="tishi-page error recom_tishi" style="display:none;"></div>
					</li>
					<li>
						<label class="star fleft labFmt110">二级营销任务：</label>
						<div class="fleft formQuerySelBox recom_dialog"  style="width: 150px;">
							<div class="selTxt secondLevelTask"  style="width: 110px;">请选择二级营销任务</div>
							<a href="javascript:void(0);" class="selBtn secondLevelTask"></a>
							<div class="querySelListBox zIndex">
								<input name="customLabelSceneRel.marketTaskId" type="hidden" id="secondLevelTask" value=""/>
								<dl>
									<dd id="secondLevelTaskList">
										<a href="javascript:void(0);"  value="">请选择二级营销任务</a>
									</dd>
								</dl>
							</div>
						</div>
						<div id="secondLevelTaskTip" class="tishi-page error recom_tishi" style="display:none;"></div>
					</li>
					<li>
						<label class="star fleft labFmt110">场景分类：</label>
						<div class="fleft formQuerySelBox recom_dialog" style="width: 150px;">
							<div class="selTxt firstLevelScene" style="width: 110px;">请选择场景分类</div>
							<a href="javascript:void(0);" class="selBtn firstLevelScene"></a>
							<div class="querySelListBox zIndex">
								<input name="customLabelSceneRel.sceneId" type="hidden" id="firstLevelScene"/>
								<dl>
									<dd>
										<a href="javascript:void(0);"  value="">请选择场景分类</a>
										<c:forEach items="${firstLevelScenes }" var="firstLevelScene">
											<a href="javascript:void(0);" value="${firstLevelScene.sceneId }">${firstLevelScene.sceneName }</a>
										</c:forEach>
									</dd>
								</dl>
							</div>
						</div>
						<div id="firstLevelSceneTip" class="tishi-page error recom_tishi" style="display:none;"></div>
					</li>
<!-- 				<li class="clearfix">
						<label class="star fleft labFmt110">二级场景分类：</label>
						<div class="fleft formQuerySelBox recom_dialog" style="width: 150px;">
							<div class="selTxt secondLevelScene" style="width: 110px;">请选择二级场景分类</div>
							<a href="javascript:void(0);" class="selBtn secondLevelScene"></a>
							<div class="querySelListBox zIndex">
								<input name="customLabelSceneRel.sceneId" type="hidden" id="secondLevelScene" value=""/>
								<dl>
									<dd id="secondLevelSceneList">
										<a href="javascript:void(0);"  value="">请选择二级场景分类</a>
									</dd>
								</dl>
							</div>
						</div>
						<div id="secondLevelSceneTip" class="tishi-page error recom_tishi" style="display:none;"></div>
					</li>
 -->					<li class="recom_tip">
						<p>请依次选择一级营销任务、二级营销任务、场景分类。</p>
					</li>
				</ol>
				<!-- 按钮区 -->
				<div class="dialog_btn_div">
					<input id="saveBtn" type="button" value="设置" class="tag_btn"/>
				</div>
				<input type="hidden" name="customLabelSceneRel.customGroupId" value="${customLabelSceneRel.customGroupId }" />
				<input type="hidden" name="customLabelSceneRel.labelId" value="${customLabelSceneRel.labelId }" />
				<input type="hidden" name="customLabelSceneRel.useTimes" value="${customLabelSceneRel.useTimes }" />
				<input type="hidden" name="customLabelSceneRel.showType" id="showType" value="${customLabelSceneRel.showType }" />
			</div>
		</form>
	</body>
</html>