<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title></title>
    <%@ include file="/aibi_ci/html_include.jsp" %>

    <script type="text/javascript">
        $(document).ready(function () {
            var exportStatus = '${listInfo.fileCreateStatus}';
            var approveStatus = '${listInfo.fileApproveStatus}';
            $("#exportStatus").html(exportStatusMap[exportStatus]);
            $("#approveStatus").html(approveStatusMap[approveStatus]);
            if (approveStatus === '2' || approveStatus === '3') {
                $("#exportBtn").attr('disabled', 'disabled').addClass("tag_btn_dis");
            }
            if (exportStatus != '3' || approveStatus != '3') {
                $("#downloadBtn").attr('disabled', 'disabled').addClass("tag_btn_dis");
            }
        });

        var approveStatusMap = {
            "": "未审批",
            "1": "未审批",
            "2": "审批中",
            "3": "审核通过",
            "4": "审核不通过"
        };

        var exportStatusMap = {
            "": "未创建",
            "1": "未创建",
            "2": "创建中",
            "3": "创建成功",
            "4": "创建失败"
        };

        function download() {
            window.open("${ctx}/ci/customersFileAction!downCustomersFile.ai2do?listTableName=" + '${listInfo.listTableName}');
        }

        function refresh() {
            var listTableName = '${listInfo.listTableName}';
            $.ajax("${ctx}/ci/zjDownloadApproveAction!refreshStatus.ai2do", {
                dataType: 'json',
                data: {listTableName: listTableName},
                success: function (listInfo) {
                    $("#exportStatus").html(exportStatusMap[listInfo.fileCreateStatus]);
                    $("#approveStatus").html(approveStatusMap[listInfo.fileApproveStatus]);
                    if (listInfo.fileApproveStatus === 2 || listInfo.fileApproveStatus === 3) {
                        $("#exportBtn").attr('disabled', 'disabled').addClass("tag_btn_dis");
                    } else {
                        $("#exportBtn").removeAttr('disabled').removeClass("tag_btn_dis");
                    }
                    if (listInfo.fileCreateStatus === 3 && listInfo.fileApproveStatus === 3) {
                        $("#downloadBtn").removeAttr('disabled').removeClass("tag_btn_dis");
                    } else {
                        $("#downloadBtn").attr('disabled', 'disabled').addClass("tag_btn_dis");
                    }
                }
            });
        }
    </script>
</head>

<body>
<div style="margin: 50px">
    <span style="margin-right: 40px">导出状态：<span id="exportStatus"></span> </span>
    <span>审批状态： <span id="approveStatus"></span></span>
</div>
<div>
    <input id="exportBtn" name="" type="button" value="提交审批" onclick="parent.showCommitApprove('${listInfo.listTableName}', '${listInfo.customGroupId}')" class="tag_btn"/>
    <input id="downloadBtn" name="" type="button" value="下载文件" onclick="download()" class="tag_btn"/>
    <%--<input id="refreshBtn" name="" type="button" value="刷新信息" onclick="refresh('${listInfo.listTableName}')" class="tag_btn"/>--%>
</div>
</body>
</html>
