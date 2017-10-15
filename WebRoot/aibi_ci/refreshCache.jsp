<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<%@ include file="/aibi_ci/html_include.jsp" %>

<%
    String searchByDB = Configure.getInstance().getProperty("SEARCH_BY_DB_FLAG");
%>
<html>
<head>
    <title>管理</title>
    <style type="text/css">
        .optionLi {
            width: 700px;
            float: left;
            margin-left: 50px;
            margin-top: 20px;
            text-align: left;
        }

        .optionLiNonePoint {
            float: left;
            margin-left: 40px;
            margin-top: 20px;
            list-style-type: none;
            text-align: left;
            line-height: 30px;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            showTab('refreshUl');

            $("#refreshBtn").click(function () {
                toRefresh();
            });
            <% if ("false".equalsIgnoreCase(searchByDB)) { %>
            $("#rebuildBtn").click(function () {
                toRebuild();
            });
            <% } %>
            $("#initLabelIdPathBtn").click(function () {
                initLabelIdPath();
            });

            $("#inner").height($(document).height() - 180);

        });
        //更新缓存
        function toRefresh() {

            $.ajax({
                url: "${ctx}/ci/ciIndexAction!refreshCache.ai2do",
                type: "POST",
                success: function (result) {
                    if (result.success) {
                        showAlert(result.msg, "success");
                    } else {
                        showAlert(result.msg, "failed");
                    }
                }
            });
        }
        //更新缓存
        function toRebuild() {
            $.blockUI({message: "<div class='aibi_progress'><p><br/>正在建立索引...</p></div>"});
            $.ajax({
                url: "${ctx}/ci/ciIndexAction!rebuildLuceneIndex.ai2do",
                type: "POST",
                //timeout : 10,
                success: function (result) {
                    $.unblockUI();
                    if (result.success) {
                        showAlert(result.msg, "success");
                    } else {
                        showAlert(result.msg, "failed");
                    }
                }
            });
        }
        //构建标签路径
        function initLabelIdPath() {
            $.blockUI({message: "<div class='aibi_progress'><p><br/>正在构建标签路径...</p></div>"});
            $.ajax({
                url: "${ctx}/ci/ciIndexAction!initLabelIdPath.ai2do",
                type: "POST",
                //timeout : 10,
                success: function (result) {
                    $.unblockUI();
                    if (result.success) {
                        showAlert(result.msg, "success");
                    } else {
                        showAlert(result.msg, "failed");
                    }
                }
            });
        }
        function showTab(id) {
            $("#" + id).parent().find("ul").hide();
            $("#" + id).show().find("ul").show();
            var liId = id.replace("Ul", "Li");
            addClassForChoice(liId);

            if ( id == 'dataUl'){
                $("#ci_show_data").empty();
                console.info("$.ctx" + $.ctx);
                <%--console.info("ctx" + ${ctx});--%>
                $("#ci_show_data").load($.ctx+"/ci/ciDataSourceAction!getDataSource.ai2do");

            }else if (id == 'labelUl'){

                $("#ci_show_label").empty();
                $("#ci_show_label").load($.ctx+"/ci/ciLabelCountAction!getLabelCount.ai2do");
            }

        }

        function runJob(obj) {
            var data = $(obj).parent().serializeArray();
            $.ajax({
                type: "POST",
                url: "${ctx}/ci/ciIndexAction!runJob.ai2do",
                data: data,
                success: function (result) {
                    if (result.success) {
                        showAlert(result.msg, "success");
                    } else {
                        showAlert(result.msg, "failed");
                    }
                }
            });
        }
        function runTransferLog(obj) {
            var data = $(obj).parent().serializeArray();
            $.ajax({
                type: "POST",
                url: "${ctx}/ci/ciIndexAction!transferLog.ai2do",
                data: data,
                success: function (result) {
                    if (result.success) {
                        showAlert(result.msg, "success");
                    } else {
                        showAlert(result.msg, "failed");
                    }
                }
            });
        }
        function addClassForChoice(id) {
            $("#childMenu").find("li").removeClass("current");
            $("#" + id).addClass("current");
            var titleId = id.replace("li", "title");
            $(".customer-title").hide();
            $("#" + titleId).show();
        }


    </script>
</head>
<body>
<jsp:include page="/aibi_ci/navigation.jsp"></jsp:include>
<div class="mainBody">
    <div class="no-search-header-without-line comWidth clearfix">
        <div class="logoBox clearfix">
            <div class="index_logo fleft" onclick="showHomePage();return false;"></div>
            <div class="secTitle fleft">管理</div>
        </div>
    </div>
</div>
<div class="navBox comWidth" id="navBox">
    <div class="fleft navListBox">
        <ol id="childMenu">
            <li id="refreshLi" class="current"><a href="javascript:void(0);" onclick="showTab('refreshUl')">更新缓存</a>
            </li>
            <li id="runLi"><a href="javascript:void(0);" onclick="showTab('runUl')">定时任务</a></li>
            <li id="logLi"><a href="javascript:void(0);" onclick="showTab('logUl')">日志统计</a></li>
            <li id="dataLi"><a href="javascript:void(0);" onclick="showTab('dataUl')">数据源</a></li>
            <li id="labelLi"><a href="javascript:void(0);" onclick="showTab('labelUl')">标签统计</a></li>
        </ol>
    </div>
</div>
<div class="comWidth clearfix save-customer-inner" id="inner">
    <div class="clearfix titleBox">
        <div class="starIcon fleft"></div>
        <div class="customer-title fleft" id="refreshTitle">刷新缓存手动设置</div>
        <div class="customer-title fleft" id="runTitle">定时任务手动设置</div>
        <div class="customer-title fleft" id="logTitle">日志统计手动设置</div>
        <div class="customer-title fleft" id="dataTitle">数据源手动设置</div>
        <div class="customer-title fleft" id="labelTitle">标签统计手动设置</div>
    </div>
    <div class="clientBaseList clearfix">
        <ul id="refreshUl">
            <li class="optionLi">
                需要更新缓存，请点击按钮： <input id="refreshBtn" name="" type="button" value="更新缓存" class="tag_btn"/>
            </li>
            <% if ("false".equalsIgnoreCase(searchByDB)) { %>
            <li class="optionLi">
                需要重建索引，请点击按钮： <input id="rebuildBtn" name="" type="button" value="重建索引" class="tag_btn"/>
            </li>
            <% } %>
            <li class="optionLi">
                构建标签路径，请点击按钮： <input id="initLabelIdPathBtn" name="" type="button" value="构建标签路径" class="tag_btn"/>
            </li>
        </ul>
        <ul id="runUl">
            <form>
                <li class="optionLi">日周期客户群任务，请选择数据日期：<input type="text" class="aibi_input aibi_date" name="dataDate"
                                                             title="不输入将使用系统获取的最新日期"
                                                             onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'})"
                                                             readonly="readonly"/>
                    <input type="hidden" name="jobName" value="dailyCustomersJob"/>
                    <input type="hidden" name="methodName" value="work"/>
                    <input id="runDayCustomJob" onclick="runJob(this)" name="" type="button" value="立即执行"
                           class="tag_btn"/>
                </li>
            </form>
            <form>
                <li class="optionLi">月周期客户群任务，请选择数据日期：<input type="text" class="aibi_input aibi_date" name="dataDate"
                                                             title="不输入将使用系统获取的最新日期"
                                                             onclick="WdatePicker({dateFmt:'yyyyMM',maxDate:'%y-%M'})"
                                                             readonly="readonly"/>
                    <input type="hidden" name="jobName" value="monthlyCustomersJob"/>
                    <input type="hidden" name="methodName" value="work"/>
                    <input id="runMonthCustomJob" onclick="runJob(this)" name="" type="button" value="立即执行"
                           class="tag_btn"/>
                </li>
            </form>
            <form>
                <li class="optionLi">日周期标签统计信息任务，请选择数据日期：<input type="text" class="aibi_input aibi_date" name="dataDate"
                                                                title="不输入将使用系统获取的最新日期"
                                                                onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'})"
                                                                readonly="readonly"/>
                    <input type="hidden" name="jobName" value="dailyLabelStatJob"/>
                    <input type="hidden" name="methodName" value="work"/>
                    <input id="rundailyLabelStatJob" onclick="runJob(this)" name="" type="button" value="立即执行"
                           class="tag_btn"/>
                </li>
            </form>
            <form>
                <li class="optionLi">月周期标签统计信息任务，请选择数据日期：<input type="text" class="aibi_input aibi_date" name="dataDate"
                                                                title="不输入将使用系统获取的最新日期"
                                                                onclick="WdatePicker({dateFmt:'yyyyMM',maxDate:'%y-%M'})"
                                                                readonly="readonly"/>
                    <input type="hidden" name="jobName" value="pageExeTaskMonthlyCacheRefreashJob"/>
                    <input type="hidden" name="methodName" value="work"/>
                    <input  onclick="runJob(this)" name="" type="button" value="立即执行"
                           class="tag_btn"/>
                </li>
            </form>
        </ul>
        <ul id="logUl">
            <form>
                <li class="optionLi">日志统计任务，请选择统计日期：<input type="text" class="aibi_input aibi_date" name="dataDate"
                                                           title="不输入将使用系统获取的最新日期"
                                                           onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'})"
                                                           readonly="readonly"/>
                    <input id="runDayCustomJob" onclick="runTransferLog(this)" name="" type="button" value="立即执行"
                           class="tag_btn"/>
                </li>
            </form>
        </ul>


        <ul id="dataUl">
            <div id="ci_show_data">

            </div>
        </ul>

        <ul id="labelUl">
            <div id="ci_show_label">

            </div>
        </ul>
    </div>


    <div class="tagList clearfix attrList" style="margin-top:30px;padding:0px 0px 20px 0px">
        <li class="optionLiNonePoint">当前线程池状态:</li>
        <br/>
        <div style="text-align:left;word-break:break-all;margin-left:40px;float:left"><%=com.ailk.biapp.ci.util.ThreadPool.getInstance().showStatus() %>
        </div>
    </div>
</div>
</body>
</html>
