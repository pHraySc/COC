<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/aibi_ci/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<%@ include file="/aibi_ci/html_include.jsp"%>
		<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/main.js"></script>
		<script type="text/javascript" src="${ctx}/aibi_ci/core/assets/scripts/customGroup/recomCustomOrLabelEntrance.js"></script>
	</head>
	<body>
		<form id="saveForm" action="" method="post">
			<div class="clientMsgBox" >
				<ol class="clearfix">
					<li>			
						<label class="star fleft labFmt110">一级营销任务：</label>
						<div class="fleft formQuerySelBox recom_dialog J_first_level_task_flag" style="width: 150px;">
							<div class="selTxt firstLevelTask" style="width: 110px;" >请选择一级营销任务</div>
							<a href="javascript:void(0);" class="selBtn firstLevelTask"></a>
							<div class="querySelListBox zIndex" style="z-index: 1118px;" onclick="">
								<input name="firstLevelTask" type="hidden" id="firstLevelTask" value=""/>
								<dl>
									<dd>
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
						<div class="fleft formQuerySelBox recom_dialog J_second_level_task_flag"  style="width: 150px;">
							<div class="selTxt secondLevelTask J_second_level_task_show"  style="width: 110px;">请选择二级营销任务</div>
							<a href="javascript:void(0);" class="selBtn secondLevelTask"></a>
							<div class="querySelListBox zIndex">
								<input name="customLabelSceneRel.marketTaskId" type="hidden" id="secondLevelTask" value=""/>
								<dl>
									<dd id="secondLevelTaskList">
									</dd>
								</dl>
							</div>
						</div>
						<div id="secondLevelTaskTip" class="tishi-page error recom_tishi" style="display:none;"></div>
					</li>
					<li>
						<label class="star fleft labFmt110">场景分类：</label>
						<div class="fleft formQuerySelBox recom_dialog" style="width: 150px;">
							<div class="selTxt firstLevelScene J_second_first_level_scene_show" style="width: 110px;">请选择场景分类</div>
							<a href="javascript:void(0);" class="selBtn firstLevelScene"></a>
							<div class="querySelListBox zIndex">
								<input name="customLabelSceneRel.sceneId" type="hidden" id="firstLevelScene"/>
								<dl>
									<dd>
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
				<input type="hidden" name="customLabelSceneRel.customGroupId" id="recomCustomId" value="${customLabelSceneRel.customGroupId }" />
				<input type="hidden" name="customLabelSceneRel.labelId" id="recomLabelId" value="${customLabelSceneRel.labelId }" />
				<input type="hidden" name="customLabelSceneRel.useTimes" value="${customLabelSceneRel.useTimes }" />
				<input type="hidden" name="customLabelSceneRel.showType" id="showType" value="${customLabelSceneRel.showType }" />
			</div>
		</form>
	</body>
</html>