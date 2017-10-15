<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title></title>
    <%@ include file="/aibi_ci/html_include.jsp" %>

    <script type="text/javascript">
    	var _submitFlag=true;
        function submitApprove(){
        	_submitFlag=false;
            if($("#approveLeaderId").val() == null || $("#approveLeaderId").val() == ''){
                parent.showAlert("请选择审批领导", "failed");
                return;
            }
            //等待提示
            showLoading('正在提交审批，请耐心等候...');
            
            var actionUrl = "${ctx}/ci/zjDownloadApproveAction!commitApprove.ai2do";
            $.post(actionUrl, $("#approveForm").serialize(), function(result){
                if (result.success){
                    var actionUrl = "${ctx}/ci/customersFileAction!createCustomersFile.ai2do?listTableName=" + '${listTableName}';
                    $.ajax({
                        type: "POST",
                        url: actionUrl,
                        async: false,
                        success: function(result){
                        	closeLoading();
                            if (result.success) {
                            	$.fn.weakTip({"tipTxt":result.msg,"backFn":function(){
                            		parent.closeCommitApprove();
            		       	    }});
                            } else {
                                parent.showAlert(result.msg, "failed");
                            }
                        }
                    });
                }else{
                	closeLoading();
                    parent.showAlert(result.msg, "failed");
                }
            });
            _submitFlag=true;
        }

        function submitApproveInit(){
        	if(_submitFlag){
        		submitApprove();
        	}
        }
        function switchLeader() {
            $("#approveLeaderName").val($("#approveLeaderId").find("option:selected").text());
        }

        function createCustomersFile() {
            if ($("#exportStatus").html() == "生成中") {
                return false;
            }
            var actionUrl = "${ctx}/ci/customersFileAction!createCustomersFile.ai2do?listTableName=" + '${listTableName}';
            $.ajax({
                type: "POST",
                url: actionUrl,
                success: function (result) {
                    if (!result.success) {
                        parent.showAlert(result.msg, "failed");
                    }
                }
            });
        }

    </script>
</head>
<body>
<form id="approveForm" method="post" >
    <input type="hidden" name="customGroupId" value="${customGroupId}"/>
    <input type="hidden" id="approveLeaderName" name="approveLeaderName" value="${approveLeaders[0].STAFF_NAME}"/>
    <input type="hidden" name="listTableName" value="${listTableName}"/>

    <div class="dialog_table">
        <table width="100%" class="showTable new_tag_table" cellpadding="0" cellspacing="0">
            <tr>
                <th width="110px"><span> * </span>审批领导：</th>
                <td>
                    <div class="new_tag_selectBox">
                        <select name="approveLeaderId" id="approveLeaderId" style="width: 347px"
                                onchange="switchLeader()">
                            <c:forEach items="${approveLeaders}" var="leader">
                                <option value="${leader.STAFF_ID}">${leader.STAFF_NAME}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <!-- 提示 -->
                    <div id="snameTip" class="tishi error" style="display:none;"></div>
                    <!-- 非空验证提示 -->
                </td>
            </tr>
            <tr>
                <th>备注信息：</th>
                <td>
                    <div>
                        <textarea id="custom_desc"
                                  onkeyup="if(this.value.length>170){this.value = this.value.slice(0,170)}"
                                  onblur="this.value = this.value.slice(0,170)" name="remark"
                                  class="new_tag_textarea"></textarea>
                    </div>
                    <!-- 提示 -->
                    <div class="tishi error" style="display:none;"></div>
                </td>
            </tr>
        </table>
    </div>
    <div class="dialog_btn_div">
        <input id="saveBtn" name="" type="button" value="提交" class="tag_btn" onclick="submitApproveInit()"/>
    </div>
</form>
</body>
</html>
