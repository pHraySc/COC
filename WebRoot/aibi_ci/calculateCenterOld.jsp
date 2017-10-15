<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/aibi_ci/include.jsp" %>
<%
    String templateMenu = Configure.getInstance().getProperty("TEMPLATE_MENU");
    String labelUpdateInBatche = Configure.getInstance().getProperty("LABEL_UPDATE_IN_BATCHES");
    String customSaveTactics = Configure.getInstance().getProperty("CUSTOM_SAVE_TACTICS");
%>
<script type="text/javascript" src="${ctx}/aibi_ci/assets/js/sortTagOld.js"></script>
<script type="text/javascript">
    var _EnumCount = 100;//枚举类型最多选的个数
    var redBorderFlag = true;//导致标签红框的原因：true此标签对应所选标签数据最新日期没有数据；false标签生效日期大于所选标签最新数据日期
    var updateCycleD = "<%=ServiceConstants.LABEL_CYCLE_TYPE_D%>";
    var updateCycleM = "<%=ServiceConstants.LABEL_CYCLE_TYPE_M%>";
    var tacticsOne = "<%=ServiceConstants.LIST_TABLE_TACTICS_ID_ONE%>";
    var tacticsThree = "<%=ServiceConstants.LIST_TABLE_TACTICS_ID_THREE%>";

    //修改客户群回写客户群 策略
    window.onload = function () {
        callBackData();
    }
    window.onload = function () {
        callBackData();
    }

    //iframe 方式 子调用父方法 关闭弹出框
    function iframeCloseDialog(obj) {
        $("._idClass").removeClass("_idClass");
        $(obj).dialog("close");
    }

    $(function () {
        //计算规则展示的位置
        var marginLeft = -($('#tagSaveActionBox').width()) / 2;
        $("#tagSaveActionBox").css("margin-left", marginLeft);

        //初始化规则到form表单中
        var ruleStr = '';

        $('#tag_dropable .conditionCT,#tag_dropable .chaining,#tag_dropable .parenthesis').each(function (i, element) {

            var calcuElement = '', //括号、操作符号、标签ID或者客户群清单表名
                elementType = '', //元素类型:1-运算符;2-标签ID;3-括号;4-产品ID;5-清单表名。
                labelFlag = ''; //标签是否取反

            var ele = $(element);

            if (ele.hasClass('conditionCT')) {//处理标签和客户群
                elementType = ele.attr('elementType');
                var customOrLabelName = ele.attr('customOrLabelName');

                if (elementType == 2) {//标签
                    labelFlag = ele.attr('labelFlag');
                    calcuElement = ele.attr('calcuElement');
                    var effectDate = ele.attr('effectDate');
                    var labelTypeId = ele.attr('labelTypeId');
                    var attrVal = ele.attr('attrVal');
                    var startTime = ele.attr('startTime');
                    var endTime = ele.attr('endTime');
                    var contiueMinVal = ele.attr('contiueMinVal');
                    var contiueMaxVal = ele.attr('contiueMaxVal');
                    var leftZoneSign = ele.attr('leftZoneSign');
                    var rightZoneSign = ele.attr('rightZoneSign');
                    var exactValue = ele.attr('exactValue');
                    var darkValue = ele.attr('darkValue');
                    var attrName = ele.attr('attrName');
                    var queryWay = ele.attr('queryWay');
                    var dataDate = ele.attr('dataDate');
                    var updateCycle = ele.attr('updateCycle');

                    if (labelFlag != 1) {
                        ruleStr += '(非)' + customOrLabelName;
                    } else {
                        ruleStr += customOrLabelName;
                    }

                    if (labelTypeId == 4) {
                        if (queryWay == 1) {
                            ruleStr += '[数值范围：';
                            if (contiueMinVal) {
                                if (leftZoneSign == '>=') {
                                    ruleStr += '大于等于' + contiueMinVal;
                                } else {
                                    ruleStr += '大于' + contiueMinVal;
                                }
                            }
                            if (contiueMaxVal) {
                                if (rightZoneSign == '<=') {
                                    ruleStr += '小于等于' + contiueMaxVal;
                                } else {
                                    ruleStr += '小于' + contiueMaxVal;
                                }
                            }
                            ruleStr += ']';
                        } else if (queryWay == 2) {
                            ruleStr += "[数值范围：" + exactValue + ']';
                        }

                    } else if (labelTypeId == 5) {
                        if (attrVal) {
                            ruleStr += '[已选择条件：' + attrName + ']';
                        }
                    } else if (labelTypeId == 6) {
                        var tempStr6 = '';
                        if (queryWay == 1) {

                            if (startTime != '') {
                                if (leftZoneSign == '>=') {
                                    tempStr6 += '大于等于' + startTime;
                                } else if (leftZoneSign == '>') {
                                    tempStr6 += '大于' + startTime;
                                }
                            }

                            if (endTime != '') {
                                if (rightZoneSign == '<=') {
                                    tempStr6 += '小于等于' + endTime;
                                } else if (rightZoneSign == '<') {
                                    tempStr6 += '小于' + endTime;
                                }
                            }
                        } else if (queryWay == 2) {
                            var itemValueArr = exactValue.split(",");
                            if (itemValueArr.length == 3) {

                                var exactValueDateYear = itemValueArr[0];
                                var exactValueDateMonth = itemValueArr[1];
                                var exactValueDateDay = itemValueArr[2];

                                if (exactValueDateYear && exactValueDateYear != "-1") {
                                    tempStr6 += exactValueDateYear + "年";
                                }
                                if (exactValueDateMonth && exactValueDateMonth != "-1") {
                                    tempStr6 += exactValueDateMonth + "月";
                                }
                                if (exactValueDateDay && exactValueDateDay != "-1") {
                                    tempStr6 += exactValueDateDay + "日";
                                }
                            }
                        }

                        if (tempStr6 != '') {
                            ruleStr += '[已选择条件：' + tempStr6 + ']';
                        }
                    } else if (labelTypeId == 7) {
                        if (queryWay == 1) {
                            if (darkValue) {
                                ruleStr += '[模糊值：' + darkValue + ']';
                            }
                        } else if (queryWay == 2) {
                            if (exactValue) {
                                ruleStr += '[精确值：' + exactValue + ']';
                            }
                        }
                    }
                    ruleStr += ' ';
                } else if (elementType == 5) {//客户群
                    var attrVal = ele.attr('attrVal');
                    ruleStr += '客户群：' + customOrLabelName;
                    if (attrVal) {
                        ruleStr += '[已选清单：' + attrVal + ']';
                    }
                    ruleStr += ' ';
                }

            } else if (ele.hasClass('chaining')) {//处理操作符

                var name = ele.find("span").text();
                ruleStr += name + ' ';

            } else if (ele.hasClass('parenthesis')) {//处理括号
                calcuElement = ele.attr('calcuElement');
                ruleStr += calcuElement;

                if (calcuElement == ')') {
                    ruleStr += ' ';
                }
            }

        });

        $('#labelOptRuleShow').val(ruleStr);

        adaptSize();

        //关闭弹出框
        $(".dialogClose ,.btnActiveBox input").click(function () {
            $(this).parent().parent().dialog("close");
        })

        //拖括号
        dragParenthesis();

        //计算规则收缩
        $("#tagCalcDetail").click(function () {
            var tar = $("#labelsCTBtm");
            if (tar.is(":visible")) {
                tar.stop().animate({height: 0}, 50, function () {
                    tar.hide();
                    $("#calcCenterBody").css("padding-bottom", $("#tagSaveActionBox").height() - 20);
                });
                $(this).addClass("arrowDown").html("展开");
            } else {
                tar.css("visibility", "visible").height("auto");
                var shopChartHeight = tar.height();
                tar.height(0).show();

                tar.stop().animate({height: shopChartHeight}, 500, function () {
                    $("#calcCenterBody").css("padding-bottom", $("#tagSaveActionBox").height() - 20);
                });
                $(this).removeClass("arrowDown").html("折叠");

            }
        })

        $('#tagCalcSaveBtn').click(saveCustom);
        $('#tagCalcExplore').click(explore);
        $('#tagCalcSaveTemplateBtn').click(saveTemplate);

        $("#itemChoose").dialog({
            width: 710,
            autoOpen: false,
            modal: true,
            title: "条件设置",
            dialogClass: "ui-dialogFixed",
            resizable: false,
            draggable: true,
            close: function (event, ui) {
                $("#itemChooseFrame").attr("src", "");
            }
        });
        $("#darkValueSet").dialog({
            width: 570,
            autoOpen: false,
            modal: true,
            title: "条件设置",
            dialogClass: "ui-dialogFixed",
            resizable: false,
            draggable: true,
            close: function (event, ui) {
                $("#darkValueSetFrame").attr("src", "");
            }
        });
        $("#verticalLabelSetDialog").dialog({
            width: 720,
            dialogClass: "ui-dialogFixed",
            autoOpen: false,
            modal: true,
            title: "纵表标签条件设置",
            resizable: false,
            draggable: true,
            close: function (event, ui) {
                $("#verticalLabelSetFrame").attr("src", "");
            }
        });

        $("#saveCustomerDialog").dialog({
            width: 720,
            autoOpen: false,
            modal: true,
            dialogClass: "ui-dialogFixed",
            title: "保存客户群",
            resizable: false,
            draggable: true,
            zIndex: 99999,
            position: [10, 10],
            close: function (event, ui) {
                $("#saveCustomerFrame").attr("src", "");

            }
        });

        $("#successDialog").dialog({
            width: 640,
            autoOpen: false,
            dialogClass: "ui-dialogFixed",
            modal: true,
            title: "系统提示",
            position: ["center", "center"],
            resizable: false,
            draggable: true,
            close: function (event, ui) {
                $("#successFrame").attr("src", "");
            }
        });
        $("#editDialog").dialog({
            autoOpen: false,
            width: 660,
            dialogClass: "ui-dialogFixed",
            title: "保存模板",
            modal: true,
            resizable: false
        });
        $("#labelAttrValidateDialog").dialog({
            width: 510,
            height: 331,
            autoOpen: false,
            dialogClass: "ui-dialogFixed",
            modal: true,
            title: "请设置属性",
            resizable: false,
            draggable: true,
            close: function (event, ui) {
                $("#labelAttrValidateFrame").attr("src", "");
            }
        });
        $('#dialog_push_single').dialog({
            dialogClass: "ui-dialogFixed",
            autoOpen: false,
            width: 730,
            title: '单个客户群推送',
            modal: true,
            resizable: false
        });
        //模拟下拉框
        $(".selBtn ,.selTxt").click(function () {
            $("#tacticsTip").hide();
            if ($(this).nextAll(".querySelListBox").is(":hidden")) {
                $(this).nextAll(".querySelListBox").slideDown();
                if ($(this).hasClass("selBtn")) {
                    $(this).addClass("selBtnActive");
                } else {
                    $(this).next(".selBtn").addClass("selBtnActive");
                }
            } else {
                $(this).nextAll(".querySelListBox").slideUp();
                $(this).removeClass("selBtnActive");
            }
            return false;
        })

        $(".querySelListBox a").click(function () {
            var selVal = $(this).attr("value");
            var selHtml = $(this).html();
            $(this).parents(".querySelListBox").prevAll(".selTxt").html(selHtml);
            $(this).parents(".querySelListBox").prevAll(".selBtn").removeClass("selBtnActive");
            $("#dimListTactics").val(selVal);
            //向隐藏域中传递值
            $(this).parents(".querySelListBox").slideUp();
            <% if (labelUpdateInBatche.equalsIgnoreCase("true")) { %>
            //校验日标签
            checkDayDate($("#dayDataDateHidden").val());
            checkDate($("#monthLabelDateHidden").val());
            <%}%>
            checkEffectDate();
            return false;
        })
        $(document).click(function (ev) {
            var e = ev || event;
            e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
            $(".querySelListBox").slideUp();
            $(".selBtn").removeClass("selBtnActive");
        })
        datePicked();
        dayDatePicked();
        saveButtonsDisplay();
        //dayLabelDate给默认值，最新数据日期
        var dayDataDateHidden = $("#dayDataDateHidden").val();
        var newLabelDay = $("#newLabelDay").val();
        if (dayDataDateHidden == "") {
            if (newLabelDay != null && newLabelDay != "") {
                var _year = newLabelDay.substring(0, 4);
                var _month = newLabelDay.substring(5, 7);
                var _day = newLabelDay.substring(8, 10);
                var dayDateTemp = _year + _month + _day;
                $("#dayDataDateHidden").val(dayDateTemp);
            }
        }
    });

    //统计日期选择后的操作
    function datePicked() {
        var d = $("#dataDate").val();
        if (d != null && d != "") {
            var _year = d.substring(0, 4);
            var _month = d.substring(5, 7);
            $("#startDateHidden").val(d + "-01");
            $("#startDateByMonth").val(d);
            var dateTemp = _year + _month;
            var customGroupId = $("#customGroupId").val();
            if (customGroupId == null || customGroupId == "") {
                $("#dataDateHidden").val(dateTemp);
            }
            $("#monthLabelDateHidden").val(dateTemp);
            <% if (labelUpdateInBatche.equalsIgnoreCase("true")) { %>
            checkDate(dateTemp);
            <%}%>
            checkEffectDate();
        }
    }
    //统计日期选择后的操作
    function dayDatePicked() {
        var d = $("#dayDataDate").val();

        if (d != null && d != "") {
            var _year = d.substring(0, 4);
            var _month = d.substring(5, 7);
            var _day = d.substring(8, 10);
            var dayDateTemp = _year + _month + _day;
            $("#dayDataDateHidden").val(dayDateTemp);
            <% if (labelUpdateInBatche.equalsIgnoreCase("true")) { %>
            checkDayDate(dayDateTemp);
            <%}%>
            checkEffectDate();
        }
    }
    //标签关注图标点击事件
    function attentionLabelOper(ths) {
        var labelId = $(ths).attr("labelId");
        var isAttention = $("#isAttentionLabel_" + labelId).val();
        if (isAttention == 'true') {
            //已经关注，点击进行取消关注动作
            var url = $.ctx + '/ci/ciLabelAnalysisAction!delUserAttentionLabel.ai2do';
            $.ajax({
                type: "POST",
                url: url,
                data: {'labelId': labelId},
                success: function (result) {
                    if (result.success) {
                        $.fn.weakTip({"tipTxt": result.message});
                    } else {
                        window.parent.showAlert(result.message, "failed");
                    }
                }
            });
        } else {
            //没有关注，点击进行关注动作触发
            var url = $.ctx + '/ci/ciLabelAnalysisAction!addUserAttentionLabel.ai2do';
            $.ajax({
                type: "POST",
                url: url,
                data: {'labelId': labelId},
                success: function (result) {
                    if (result.success) {
                        $.fn.weakTip({"tipTxt": result.message});
                    } else {
                        window.parent.showAlert(result.message, "failed");
                    }
                }
            });
        }

    }

    //客户群关注图标点击事件
    function attentionCustomOper(ths) {
        var customId = $(ths).attr("customGroupId");
        var isAttention = $("#isAttentionCustom_" + customId).val();
        if (isAttention == 'true') {
            //已经关注，点击进行取消关注动作
            //alert('已经关注，点击进行取消关注动作');
            var url = $.ctx + '/ci/customersManagerAction!delUserAttentionCustom.ai2do';
            $.ajax({
                type: "POST",
                url: url,
                data: {'customGroupId': customId},
                success: function (result) {
                    if (result.success) {
                        $.fn.weakTip({"tipTxt": result.message});
                    } else {
                        window.parent.showAlert(result.message, "failed");
                    }
                }
            });
        } else {
            //没有关注，点击进行关注动作触发
            //alert('没有关注，点击进行关注动作触发');
            var url = $.ctx + '/ci/customersManagerAction!addUserAttentionCustom.ai2do';
            $.ajax({
                type: "POST",
                url: url,
                data: {'customGroupId': customId},
                success: function (result) {
                    if (result.success) {
                        $.fn.weakTip({"tipTxt": result.message});
                    } else {
                        window.parent.showAlert(result.message, "failed");
                    }
                }
            });
        }
    }

    var labelOrCustomerId;

    //策略提示
    function tacticsTip(e, ths) {
        var e = e || window.event;
        e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

        var isHiddenOpt = false;
        if ($('#tacticsTip').is(":visible")) {
            $('#tacticsTip').hide();
            isHiddenOpt = true;
        }

        //隐藏客户群提示和标签提示
        $('#labelTip').hide();
        $('#customTip').hide();

        if (isHiddenOpt) return;

        var posx = $(ths).offset().left - 165;
        var posy = $(ths).offset().top + $(ths).height() - 6;

        var desc = '当使用的标签没有更新到最新日期时：' +
            '<br/>1、采用"保守数据策略"，将使用已有数据日期生成清单' +
            '<br/>2、采用"预约数据策略"，将等待标签更新后再生成清单';

        var html = '<div class="labelTipTop">&nbsp;</div>' +
            '<div class="labelTipInner">' +
            '<div class="maintext">' + desc + '</div>' +
            '</div>';

        $('#tacticsTip').html(html);
        $('#tacticsTip').css({'left': posx + "px", 'top': posy + "px"}).show();
    }

    //标签提示
    function labelTip(e, ths) {

        var e = e || window.event;
        e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

        var labelId = $(ths).attr('labelId');
        var url = $.ctx + '/ci/ciIndexAction!findLabelTipInfo.ai2do';

        var isHiddenOpt = false;
        if (labelOrCustomerId == labelId && $('#labelTip').is(":visible")) {
            $('#labelTip').hide();
            isHiddenOpt = true;
        }

        //隐藏客户群提示和策略提示
        $('#tacticsTip').hide();
        $('#customTip').hide();

        labelOrCustomerId = labelId;

        if (isHiddenOpt) return;

        var posx = $(ths).offset().left - 165;
        var posy = $(ths).offset().top + $(ths).height() - 6;

        $.post(url, {labelId: labelId}, function (response) {

            var labelInfo = response.labelInfo;
            var detailInfo = response.detailInfo;
            var cycleStr = '日周期';
            var customNumDisplay = '';
            if (labelInfo.updateCycle == 2) {
                cycleStr = '月周期';
            }
            if (labelInfo.labelTypeId == 1 && labelInfo.customerNum != -1) {
                customNumDisplay = '<th>用户规模数：</th>' +
                    '<td>' + labelInfo.customerNum + '</td>';
            }
            var desc = labelInfo.busiCaliber;
            if (desc == null) {
                desc = ''
            }

            var sDate = labelInfo.effecTimeStr;
            var eDate = labelInfo.failTimeStr;
            if (sDate == null) {
                sDate = '--'
            }
            if (eDate == null) {
                eDate = '--'
            }
            var html = '<div class="labelTipTop">&nbsp;</div>' +
                '<div class="labelTipInner">' +
                '<div class="maintext">' + desc + '</div>' +
                '<table>' +
                '<tr>' +
                '<th width="85">更新周期：</th>' +
                '<td>' + cycleStr + '</td>' +
                '<th width="85">创建时间：</th>' +
                '<td>' + labelInfo.createTimeStr + '</td>' +
                '</tr>' +
                '<tr>' +
                '<th>最新数据时间：</th>' +
                '<td>' + labelInfo.newDataDate + '</td>' +
                customNumDisplay +
                '</tr>' +
                '</table>' +
                '<div class="marketTipBottomBox">' +
                '<div class="fleft">';

            if (detailInfo.isSysRecom != null && detailInfo.isSysRecom == 1) {
                html += '<a href="javascript:void(0);" class="organ"  title="系统推荐标签"/>';
            }
            if (detailInfo.isHot == 'true') {
                html += '<a href="javascript:void(0);" class="hot" title="系统热门标签"/>';
            }
            if (detailInfo.isAttention == 'true') {
                html += '<a href="javascript:void(0);" class="hideFont" title="我收藏的标签"/>';
            }

            if (detailInfo.isHot == 'true' || detailInfo.isAttention == 'true'
                || (detailInfo.isSysRecom != null && detailInfo.isSysRecom == 1)) {
                html += '<span class="storeSegLine"></span>';
            }

            if (detailInfo.isAttention == 'false') {
                html += '<input type="hidden" id="isAttentionLabel_' + labelId + '" value="' + detailInfo.isAttention + '" />';
                html += '<a href="javascript:void(0);"  onclick="attentionLabelOper(this)" labelId="' + labelId + '" class="store" >收藏</a>';
            }
            if (detailInfo.isAttention == 'true') {
                html += '<input type="hidden" id="isAttentionLabel_' + labelId + '" value="' + detailInfo.isAttention + '" />';
                html += '<a href="javascript:void(0);"  onclick="attentionLabelOper(this)" labelId="' + labelId + '" class="store" >取消收藏</a>';
            }
            html += '</div>' +
                '<div class="fright">' +
                '<label>有效期：</label>' +
                '<span>' + sDate + '</span>' +
                '<span>至</span>' +
                '<span>' + eDate + '</span>' +
                '</div>' +
                '</div>' +
                '</div>';

            $('#labelTip').empty();
            $('#labelTip').html(html);
            $('#labelTip').css({'left': posx + "px", 'top': posy + "px"}).show();
        });
    }
    //客户群提示框
    function customTip(event, ths) {
        var e = e || window.event;
        e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

        var customGroupId = $(ths).attr('customId');
        var url = $.ctx + '/ci/ciIndexAction!findCustomTipInfo.ai2do';

        var isHiddenOpt = false;
        if (labelOrCustomerId == customGroupId && $('#customTip').is(":visible")) {
            $('#customTip').hide();
            isHiddenOpt = true;
        }

        //隐藏标签提示和策略提示
        $('#labelTip').hide();
        $('#tacticsTip').hide();

        labelOrCustomerId = customGroupId;

        if (isHiddenOpt) return;

        var posx = $(ths).offset().left - 165;
        var posy = $(ths).offset().top + $(ths).height() - 6;

        $.post(url, {'ciCustomGroupInfo.customGroupId': customGroupId}, function (response) {

            var customGroupInfo = response.customGroupInfo;

            var productDate = customGroupInfo.productDate;
            var desc = customGroupInfo.customGroupDesc;
            if (desc == null) {
                desc = ''
            }
            var sDate = customGroupInfo.startDateStr;
            var eDate = customGroupInfo.endDateStr;
            var customNum = customGroupInfo.customNum;
            if (sDate == null) {
                sDate = '--'
            }
            if (eDate == null) {
                eDate = '--'
            }
            if (customNum == null) {
                customNum = '-'
            }
            if (productDate == null) {
                productDate = '--'
            }
            var html = '<div class="labelTipTop">&nbsp;</div>' +
                '<div class="labelTipInner">' +
                '<div class="maintext">' + desc + '</div>' +
                '<table>' +
                '<tr>' +
                '<th width="85">更新周期：</th>';
            if (customGroupInfo.updateCycle == 1) {
                html += '<td>一次性</td>';
            } else if (customGroupInfo.updateCycle == 2) {
                html += '<td>月周期</td>';
            } else if (customGroupInfo.updateCycle == 3) {
                html += '<td>日周期</td>';
            } else if (customGroupInfo.updateCycle == 4) {
                html += '<td>无</td>';
            }
            html += '<th width="85">创建时间：</th>' +
                '<td>' + customGroupInfo.createTimeView + '</td>' +
                '</tr>' +
                '<tr>' +
                '<th>用户规模数：</th>' +
                '<td>' + customNum + '</td>' +
                '<th>最新数据时间：</th>' +
                '<td>' + productDate + '</td>' +
                '</tr>' +
                '</table>' +
                '<div class="marketTipBottomBox">' +
                '<div class="fleft">';


            if (customGroupInfo.isSysRecom != null && customGroupInfo.isSysRecom == 1) {
                html += '<a href="javascript:void(0);" class="organ"  title="系统推荐标签"/>';
            }
            if (customGroupInfo.isHotCustom == 'true') {
                html += '<a href="javascript:void(0);" class="hot" title="系统热门标签"/>';
            }
            if (customGroupInfo.isAttention == 'true') {
                html += '<a href="javascript:void(0);" class="hideFont" title="我收藏的标签"/>';
            }

            if (customGroupInfo.isHotCustom == 'true' || customGroupInfo.isAttention == 'true'
                || (customGroupInfo.isSysRecom != null && customGroupInfo.isSysRecom == 1)) {
                html += '<span class="storeSegLine"></span>';
            }


            if (customGroupInfo.isAttention == 'false') {
                html += '<input type="hidden" id="isAttentionCustom_' + customGroupId + '" value="' + customGroupInfo.isAttention + '" />';
                html += '<a href="javascript:void(0);" customGroupId=' + customGroupId + '  onclick="attentionCustomOper(this)" class="store" >收藏</a>';
            }
            if (customGroupInfo.isAttention == 'true') {
                html += '<input type="hidden" id="isAttentionCustom_' + customGroupId + '" value="' + customGroupInfo.isAttention + '" />';
                html += '<a href="javascript:void(0);" customGroupId=' + customGroupId + '  onclick="attentionCustomOper(this)" class="store" >取消收藏</a>';
            }
            html += '</div>';
            if (customGroupInfo.updateCycle == 2) {
                html += '<div class="fright">' +
                    '<label>有效期：</label>' +
                    '<span>' + sDate + '</span>' +
                    '<span>至</span>' +
                    '<span>' + eDate + '</span>' +
                    '</div>';
            }

            html += '</div></div>';
            $('#customTip').empty();
            $('#customTip').html(html);
            $('#customTip').css({'left': posx, 'top': posy}).show();
        });
    }

    var tagConfig = {
        "4": "#numberValueSet", // 指标型，存具体的指标值；
        "5": "#itemChoose",   //枚举型，列的值有对应的维表，下拉展示；
        "6": "#dateSettings",   //日期型，字符串类型的日期值。
        "7": "#darkValueSet",   //模糊型，存字符串，like查询
        "8": "#verticalLabelSetDialog",   //纵表型，对应多个列，数据是纵表存储。 */
        "10": "#customerSetDialog",    //客户群设置
        "11": "#saveCustomerDialog" //保存客户群

    }

    //标签弹出层处理
    function setLabelAttr(obj, labelType, name) {
        $('#tag_dropable .conditionCT').each(function () {
            $(this).find("a").removeClass("_idClass");
        });
        var dialogId = tagConfig[labelType];
        var dataJson = {};
        var dataJsonStr = '';

        var mainObj = $(obj).parents('.conditionCT');

        $(mainObj).children('.left_bg').children('a').addClass("_idClass");

        if (labelType == "4") { // 指标型，存具体的指标值；

            var queryWay = mainObj.attr("queryWay");
            var contiueMinVal = mainObj.attr("contiueMinVal");
            var contiueMaxVal = mainObj.attr("contiueMaxVal");
            var leftZoneSign = mainObj.attr("leftZoneSign");
            var rightZoneSign = mainObj.attr("rightZoneSign");
            var exactValue = mainObj.attr("exactValue");
            var unit = mainObj.attr("unit");
            unit = encodeURIComponent(encodeURIComponent(unit));

            var para = "?queryWay=" + queryWay + "&contiueMinVal=" + contiueMinVal
                + "&contiueMaxVal=" + contiueMaxVal + "&leftZoneSign=" + leftZoneSign
                + "&rightZoneSign=" + rightZoneSign + "&exactValue=" + exactValue
                + "&unit=" + unit;
            var ifmUrl = "${ctx}/aibi_ci/dialog/numberValueSetDialog.jsp" + para;
            dialogUtil.create_dialog("numberValueSet", {
                "title": name + "-条件设置",
                "height": "auto",
                "width": 530,
                "frameSrc": ifmUrl,
                "frameHeight": 241,
                "position": ['center', 'center']
            });
        } else if (labelType == "5") {  //条件选择
            var attrVal = mainObj.attr("attrVal");
            var attrName = mainObj.attr("attrName");
            var calcuElement = mainObj.attr("calcuElement");
            var para = "?attrVal=" + attrVal + "&attrName=" + attrName + "&calcuElement=" + calcuElement;
            $(dialogId).dialog("option", "title", name + "-条件设置");

            var ifmUrl = '${ctx}/aibi_ci/dialog/itemChooseDialog.jsp';
            var form = '<form id="postData_form" action="' + ifmUrl + '" method="post" target="_self">' +
                '<input name="attrVal" type="hidden" value="' + attrVal + '"/>' +
                '<input name="attrName" type="hidden" value="' + attrName + '"/>' +
                '<input name="calcuElement" type="hidden" value="' + calcuElement + '"/>' +
                '</form>';
            document.getElementById('itemChooseFrame').contentWindow.document.write(form);
            document.getElementById('itemChooseFrame').contentWindow.document.getElementById('postData_form').submit();
            $(dialogId).dialog("open");
        } else if (labelType == "6") {//日期类型标签
            var startTime = "";
            var endTime = "";
            var leftZoneSign = "";
            var rightZoneSign = "";
            var isNeedOffset = mainObj.attr('isNeedOffset');
            startTime = mainObj.attr("startTime");
            endTime = mainObj.attr("endTime");
            leftZoneSign = mainObj.attr("leftZoneSign");
            rightZoneSign = mainObj.attr("rightZoneSign");
            queryWay = mainObj.attr("queryWay");
            exactValue = mainObj.attr("exactValue");

            var para = "?startTime=" + startTime + "&endTime=" + endTime + "&leftZoneSign=" + leftZoneSign
                + "&rightZoneSign=" + rightZoneSign + "&queryWay=" + queryWay
                + "&exactValue=" + exactValue + "&isNeedOffset=" + isNeedOffset;
            var ifmUrl = "${ctx}/aibi_ci/dialog/dateSettingsDialog.jsp" + para;
            dialogUtil.create_dialog("dateSettings", {
                "title": name + "-条件设置",
                "height": "auto",
                "width": 570,
                "frameSrc": ifmUrl,
                "frameHeight": 290,
                "position": ['center', 'center']
            });
        } else if (labelType == "7") {
            var darkValue = "";
            darkValue = mainObj.attr("darkValue");
            darkValue = encodeURIComponent(encodeURIComponent(darkValue));

            var queryWay = mainObj.attr("queryWay");
            var exactValue = mainObj.attr("exactValue");
            exactValue = encodeURIComponent(encodeURIComponent(exactValue));

            var para = "?darkValue=" + darkValue + "&exactValue=" + exactValue + "&queryWay=" + queryWay;
            var ifmUrl = "${ctx}/aibi_ci/dialog/darkValueSetDialog.jsp" + para;

            /* dialogUtil.create_dialog("darkValueSet", {
             "title" 		: name + "-条件设置",
             "height"		: "auto",
             "width" 		: 570,
             "frameSrc" 		: ifmUrl,
             "frameHeight"	: 290,
             "position" 		: ['center','center']
             }); */
            $(dialogId).dialog("option", "title", name + "-条件设置");
            var ifmUrl = "${ctx}/aibi_ci/dialog/darkValueSetDialog.jsp" + para;
            $("#darkValueSetFrame").attr("src", ifmUrl).load(function () {
            });
            $(dialogId).dialog("open");

        } else if (labelType == '8') {//纵表标签
            var calcuElement = mainObj.attr("calcuElement")
            var ifmUrl = "${ctx}/ci/ciIndexAction!findVerticalLabel.ai2do?labelId=" + calcuElement;
            /* dialogUtil.create_dialog("verticalLabelSetDialog", {
             "title" 		: name + "-条件设置",
             "height"		: "auto",
             "width" 		: 720,
             "frameSrc" 		: ifmUrl,
             "frameHeight"	: 480,
             "position" 		: ['center','center']
             }); */
            $(dialogId).dialog("option", "title", name + "-条件设置");
            $("#verticalLabelSetFrame").attr("src", ifmUrl).load(function () {
            });
            $(dialogId).dialog("open");
        } else if (labelType == "10") {
            var expandType = 0;//0、使用清单；1、使用规则
            var detailedListDate;//清单日期
            var valueId = mainObj.attr("customId");
            var dataDate = mainObj.attr("attrVal");
            var para = "?pojo.valueId=" + valueId + "&pojo.dataDate=" + dataDate;

            var ifmUrl = $.ctx + "/ci/ciIndexAction!findCustomListAndRule.ai2do" + para;

            dialogUtil.create_dialog("customerSetDialog", {
                "title": name + "-条件设置",
                "height": "auto",
                "width": 530,
                "frameSrc": ifmUrl,
                "frameHeight": 275,
                "position": ['center', 'center']
            });
        } else {
            showAlert("计算元素类型错误！", "failed");
        }
    }

    function changeEndDate() {
        $("#endDateByMonth").val("");
        $("#endDateHidden").val("");
    }
    //获得指定年月最后一天日期
    function getLastDay(year, month) {
        var new_year = year;    //取当前的年份
        var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）
        if (month > 12) {            //如果当前大于12月，则年份转到下一年
            new_month -= 12;        //月份减
            new_year++;            //年份增
        }
        var new_date = new Date(new_year, new_month, 1);                //取当年当月中的第一天
        return (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();//获取当月最后一天日期
    }

    //数据探索
    function explore() {
        //控制按钮 todo

        $("#customerNum").empty().append("请稍候...");

        if (validateSql2()) {
            showLoading('正在进行数据探索，请耐心等候...');
            submitForExplore();
        } else {
            $("#customerNum").empty().append("");
            //恢复按钮样式 todo
            closeLoading();
        }
    }

    function submitForExplore() {

        var actionUrl = "";
        if (isExistGroupOrVerticalLabel()) {
            actionUrl = $.ctx + "/ci/customersManagerAction!countOfCustomerCalc.ai2do";
        } else {
            actionUrl = $.ctx + "/ci/customersManagerAction!findCustomerNumForExplore.ai2do";
        }

        $.post(actionUrl, $("#labelForm").serialize(), function (result) {
            if (result.success) {
                var num = "";
                if ((result.msg != null && result.msg != "") || result.msg == 0) {
                    num = $.formatNumber(result.msg, 3, ',');
                }
                $("#customerNum").empty().append("<label>客户数:</label><span>" + num + "</span>");
            } else {
                showAlert(result.msg, "failed");
            }
            //恢复按钮样式 todo
            closeLoading();
        });
    }

    //还原iframe中的表单
    function resetForm(isOpen) {
        if (!isOpen) {
            return;
        }
        var ifm = $("#saveCustomerFrame").contents();
        //编辑时填充表单
        var customGroupId = $("#customGroupId").val();
        if (customGroupId != null && customGroupId != "") {//修改客户群
            ifm.find("#custom_id").val(customGroupId);
            ifm.find("#data_status").val($("#dataStatus").val());
            ifm.find("#custom_name").val($("#customGroupName").val());
            ifm.find("#custom_desc").val($("#customGroupDesc").val());
            ifm.find("#oldIsHasList").val($("#isHasList").val());
            ifm.find("#oldEndDate").val($("#endDate").val());


            //新添加是否私有与创建地市字段
            ifm.find("#sceneIds").val($("#sceneId").val());
            ifm.find("#create_city_id").val($("#createCityId").val());

            //分类参数回显start
            var selBakArr = ifm.find("#sceneIds").val().split(",");
            var firstItemVal = ifm.find("#firstItem_active").val();
            var selListInput = ifm.find("input[type='checkbox'][name='_sceneBoxItemCheckList']");
            var titleArr = [];
            for (var i = 0; i < selBakArr.length; i++) {
                //公共场景
                if (selBakArr[i] == firstItemVal) {
                    titleArr.push(ifm.find("#firstItem_active").attr("_sceneName"));
                    ifm.find("#firstItem_active").attr("checked", true);
                    break;
                } else {
                    for (var j = 0; j < selListInput.length; j++) {
                        if ($(selListInput[j]).val() == selBakArr[i]) {
                            titleArr.push(ifm.find(selListInput[j]).attr("_sceneName"));
                            ifm.find(selListInput[j]).attr("checked", true);
                            break;
                        }
                    }
                }
            }
            ifm.find("#scenesTxt").html(titleArr.join(",")).attr("title", titleArr.join(","));
            //分类参数回显end

            //是否共享
            ifm.find("#is_private").val($("#isPrivate").val());
            ifm.find("#is_private_source").val($("#isPrivate").val());
            if ($("#isPrivate").val() == 1) {
                ifm.find(".is_private").children("input")[0].checked = false;
                ifm.find(".is_private").show();
                ifm.find(".isShared").hide();
                ifm.find(".notShared").children("input")[0].checked = true;
            } else {
                ifm.find(".is_private").children("input")[0].checked = false;
                ifm.find(".is_private").show();
                ifm.find(".notShared").hide();
                ifm.find(".isShared").children("input")[0].checked = true;
            }

            //是否包含清单
            ifm.find("#is_hasList").val($("#isHasList").val());
            ifm.find("#is_hasList_source").val($("#isHasList").val());
            if ($("#isHasList").val() == 1) {
                ifm.find(".is_hasList").children("input")[0].checked = false;
                ifm.find(".is_hasList").show();
                ifm.find(".notHasList").hide();
                ifm.find(".isHasList").children("input")[0].checked = true;
            } else {
                ifm.find(".is_hasList").children("input")[0].checked = false;
                ifm.find(".is_hasList").show();
                ifm.find(".isHasList").hide();
                ifm.find(".notHasList").children("input")[0].checked = true;
                ifm.find("#attrList_a").hide();
                ifm.find("#createPeriodBox_li").hide();
                ifm.find("#period_monthly").hide();
                ifm.find("#period_daily").hide();
                ifm.find("#sysRecommendTimeLi").hide();
                $("#saveCustomerFrame").height(window.parent.$("#saveCustomerFrame").height() - 318);
                var left = window.screen.width / 2 - window.parent.$("#saveCustomerFrame").width() / 2;
                var top = 135;
                $("#saveCustomerDialog").parent().css({'left': left, 'top': top});
                return;
            }

            //周期性设置
            var updateCycle = $.trim($('#updateCycle').val());
            ifm.find('#createPeriod').val(updateCycle);
            ifm.find("#createPeriodBox div").removeClass('current');
            var flagConstraint = $(ifm.find("#flagConstraint")[0]).val();
            if (updateCycle == 2) {//月周期
                var num = 1;
                if ('false' == flagConstraint) {
                    num = num + 1;
                }
                $(ifm.find("#createPeriodBox div")[num]).addClass('current');
                ifm.find('#period_monthly').show();
                if ($("#saveCustomerFrame").height() < 574) {
                    $("#saveCustomerFrame").height($("#saveCustomerFrame").height() + 35);
                }
                var startDate = $("#startDate").val();
                var sYear = startDate.substring(0, 4);
                var sMonth = startDate.substring(5, 7);
                ifm.find("#startDateHidden").val(startDate);
                ifm.find("#startDateByMonth").val(sYear + "-" + sMonth);

                var endDate = $("#endDate").val();
                var eYear = endDate.substring(0, 4);
                var eMonth = endDate.substring(5, 7);
                ifm.find("#endDateHidden").val(endDate);
                ifm.find("#endDateByMonth").val(eYear + "-" + eMonth);

            } else if (updateCycle == 3) {//日周期
                $(ifm.find("#createPeriodBox div")[0]).addClass('current');
                ifm.find('#period_daily').show();
                if ($("#saveCustomerFrame").height() < 574) {
                    $("#saveCustomerFrame").height($("#saveCustomerFrame").height() + 35);
                }
                ifm.find("#startDateHidden").val($("#startDate").val());
                ifm.find("#startDateByDay").val($("#startDate").val());
                ifm.find("#endDateHidden").val($("#endDate").val());
                ifm.find("#endDateByDay").val($("#endDate").val());
            }

            //带出是否预约时间生成清单
            var listCreateTime = $.trim($('#listCreateTime').val());
            ifm.find("#sysRecommendTimeLi").show();
            ifm.find("#listCreateTime").val(listCreateTime);
            if (listCreateTime) {
                ifm.find("#sysRecommendTimeYes").hide();
                ifm.find("#sysRecommendTimeNot").show();
                ifm.find("#isSysRecommendTime").val("0");
                if (updateCycle == 2) {
                    ifm.find("#monthTimeDiv").show();
                    ifm.find("#monthCycleTime").val(listCreateTime)
                } else if (updateCycle == 3) {
                    ifm.find("#dayTimeDiv").show();
                    ifm.find("#dayCycleTime").val(listCreateTime)
                }
            } else {
                ifm.find("#sysRecommendTimeYes").show();
                ifm.find("#sysRecommendTimeNot").hide();
                ifm.find("#isSysRecommendTime").val("1");
                ifm.find("#monthTimeDiv").hide();
                ifm.find("#dayTimeDiv").hide();
            }

            //带出客户群的属性
            var selfAttrRel = $.trim($('#selectedCustomGroupAttrRel').html());
            if (selfAttrRel.length > 0) {
                if (ifm.find("#foldAction").is(":hidden")) {
                    ifm.find("#labelListBox").height(65);
                } else {
                    ifm.find("#labelListBox").height(100);
                }
                ifm.find('#labelListBox ol').html(selfAttrRel).show();

                //绑定删除单击事件
                ifm.find('#labelListBox').find(".clientSelectItemClose").click(function () {
                    var checkedboxId = $(this).parent().attr("checkedboxId");
                    ifm.find("#" + checkedboxId).attr("checked", false);
                    ifm.find("#two_" + checkedboxId).attr("checked", false);
                    $(this).parents("li").remove();
                    if (ifm.find("#labelListBox li").length == 0) {
                        ifm.find("#labelListBox").css("height", "auto");
                    }
                })

                //选中展示框里的属性
                $('#selectedCustomGroupAttrRel li').each(function (index, item) {
                    var div = $(this).find('div');
                    var attrSource = div.attr('attrSource');
                    var checkedboxId = div.attr('checkedboxId');
                    var labelOrCustomColumn = div.attr('labelOrCustomColumn');
                    if (attrSource == 2) {
                        ifm.find('#labelListTd ol li input[id="' + checkedboxId + '"]').attr('checked', true);
                        ifm.find('#labelListTdTwo li input[id="two_' + checkedboxId + '"]').attr('checked', true);
                    } else if (attrSource == 3) {
                        ifm.find('#customAttrListTd ol li input[id="'
                            + checkedboxId + '"]').attr('checked', true);
                        ifm.find('#customAttrListTdTwo li input[id="two_'
                            + checkedboxId + '"]').attr('checked', true);
                    }
                });
            }
        }
    }
    //保存客户群弹出层
    function openCreate() {
        var ifmUrl = "${ctx}/ci/customersManagerAction!saveCustomByRulesInit.ai2do";
        $("#labelForm").submit();
    }

    function validateForm(isEdit, isExplore) {
        if ($(".waitClose").length > 0) {
            showAlert("左右括号不匹配，请确认括号是否正确使用！", "failed");
            return false;
        }
        if (isExplore && $(".redBorder").length > 0) {
            commonUtil.create_alert_dialog("redBorderTip", {
                txt: "有红框的标签，当前选择的日期数据未生成！",
                type: "failed",
                width: 500,
                height: 200
            });
            //showAlert("有红框的标签，当前选择的日期数据未生成！", "failed");
            return;
        }
        if (!isEdit && $(".redBorder").length > 0) {
            //showAlert("有红框的标签，当前选择的日期数据未生成！","failed");
            if (redBorderFlag) {
                var dlgObj = dialogUtil.create_dialog("showBSCLDialog", {
                    "title": "提示",
                    "height": 475,
                    "width": 600,
                    "frameSrc": "${ctx}/aibi_ci/dialog/showTactics.jsp",
                    "frameHeight": 480,
                    "position": ['center', 'center']
                });
            } else {
                //showAlert("标签生效日期晚于所选标签数据日期！", "failed");
                commonUtil.create_alert_dialog("effectDateTip", {
                    txt: "标签生效日期晚于所选标签数据日期！",
                    type: "failed",
                    width: 500,
                    height: 200
                });
            }

            return false;
        }

        if ($(".conditionCT").length > $._maxLabelNum) {
            showAlert("最多拖动" + $._maxLabelNum + "个标签！", "failed");
            return false;
        }
        var dataDateHidden = $.trim($("#dataDateHidden").val());
        if (dataDateHidden == "") {
            showAlert("没有选择日期，请选择日期", "failed");
            return false;
        }
        if (!isSetProperties()) {
            var iframeUrl = "${ctx}/aibi_ci/dialog/labelAttrValidateDialog.jsp"
            $("#labelAttrValidateFrame").attr("src", iframeUrl).load(function () {
            });
            $("#labelAttrValidateDialog").dialog("open");
            return false;
        }
        return true;
    }
    //SQL验证
    function validateSql(isEdit) {
        var flag = true;
        if (validateForm(isEdit)) {
            submitRules();//校验sql之前先把计算中心的内容提交到session中，防止在购物车页面删除计算元素，导致验证sql报错
            var actionUrl = "";
            if (isExistGroupOrVerticalLabel()) {
                actionUrl = $.ctx + "/ci/customersManagerAction!validateCreateCustomGroupSql.ai2do";
            } else {
                actionUrl = $.ctx + "/ci/customersManagerAction!validateSql.ai2do";
            }
            $.ajax({
                url: actionUrl,
                type: "POST",
                async: false,//同步
                data: $("#labelForm").serialize(),
                success: function (result) {
                    flag = result.success;
                    if (!flag) {
                        commonUtil.create_alert_dialog("validateSqlFailed", {
                            txt: result.msg,
                            type: "failed",
                            width: 500,
                            height: 200
                        });
                        //showAlert(result.msg,"failed");
                        var idArray = result.ids;
                        if (idArray != null) {
                            var html = '<div class="blackOverlay">&nbsp;</div><div class="blackOverlayCT">'
                                + '<a class="delOverlay" href="javascript:void(0);">&nbsp;</a>'
                                + '<div class="overlayTxt">该客户群已失效</div></div>';
                            var i = 0;
                            $.each(idArray, function (key, val) {
                                $(".conditionCT[calcuElement=" + val + "]").append(html);
                                i = key + 1;
                            });
                            if (i > 0) {
                                $("#tag_dropable").unbind("click").on("click", ".delOverlay", function () {
                                    $(this).parent().siblings(".right_bg").find(".delLabel").click();
                                });
                            }
                        }
                    }
                }
            });
        } else {
            flag = false;
        }
        return flag;
    }

    function validateSql2() {
        var flag = true;
        if (validateEnumCount() == false) {
            showAlert("配置的条件中值超过" + _EnumCount + "个，不支持数据探索，可以保存客户群后查看客户数！", "failed");
            flag = false;
            return flag;
        }
        if (validateForm(false, true)) {
            if (checkDataDate()) {
                submitRules();//校验sql之前先把计算中心的内容提交到session中，防止在购物车页面删除计算元素，导致验证sql报错
                var actionUrl = "";
                if (isExistGroupOrVerticalLabel()) {
                    actionUrl = $.ctx + "/ci/customersManagerAction!validateCreateCustomGroupSql.ai2do";
                } else {
                    actionUrl = $.ctx + "/ci/customersManagerAction!validateSql.ai2do";
                }
                $.ajax({
                    url: actionUrl,
                    type: "POST",
                    async: false,//同步
                    data: $("#labelForm").serialize(),
                    success: function (result) {
                        flag = result.success;
                        if (!flag) {
                            commonUtil.create_alert_dialog("validateSqlFailed2", {
                                txt: result.msg,
                                type: "failed",
                                width: 500,
                                height: 200
                            });
                            //showAlert(result.msg,"failed");
                            var idArray = result.ids;
                            if (idArray != null) {
                                var html = '<div class="blackOverlay">&nbsp;</div><div class="blackOverlayCT">'
                                    + '<a class="delOverlay" href="javascript:void(0);">&nbsp;</a>'
                                    + '<div class="overlayTxt">该客户群已失效</div></div>';
                                var i = 0;
                                $.each(idArray, function (key, val) {
                                    $(".conditionCT[calcuElement=" + val + "]").append(html);
                                    i = key + 1;
                                });
                                if (i > 0) {
                                    $("#tag_dropable").unbind("click").on("click", ".delOverlay", function () {
                                        $(this).parent().siblings(".right_bg").find(".delLabel").click();
                                    });
                                }
                            }
                        }
                    }
                });
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    //保存客户群
    function saveCustom() {


        var dimListTactics = $("#dimListTactics").val();
        var tacticsOne = "<%=ServiceConstants.LIST_TABLE_TACTICS_ID_ONE%>";

        //修改客户群不验证数据是否存在
        var customGroupId = $.trim($("#customGroupId").val());
        var isEdit = false;
        if (customGroupId) {
            isEdit = true;
        }
        //验证权限
        if (validatePower()) {
            //策略一，sql验证通不过也可以保存客户群
            if (dimListTactics == tacticsOne) {
                if (validateForm(isEdit)) {
                    openCreate();
                }
            } else {
                if (validateSql(isEdit)) {
                    openCreate();
                }
            }
        }
    }

    //权限验证
    function validatePower() {
        var Flag = true;
        var actionUrl = $.ctx + "/ci/customersManagerAction!validatePower.ai2do";
        submitRules();
        $.ajax({
            url: actionUrl,
            type: "POST",
            async: false,
            data: $("#labelForm").serialize(),
            success: function (result) {
                flag = result.success;
                if (!flag) {
                    commonUtil.create_alert_dialog("validatePowerFailed", {
                        txt: result.msg,
                        type: "failed",
                        width: 500,
                        height: 200
                    })
                    Flag = false;
                }
            }
        })
        return Flag;
    }


    //校验被计算的客户群是否有被删除的客户群
    function validateDelCustom() {
        var url = $.ctx + "/ci/ciIndexAction!findDelCustom.ai2do"
        $.ajax({
            url: url,
            async: false,
            success: function (response) {
                var result = response.result;
                var name = '';
                if (result && result.length > 0) {
                    for (var i = 0; i < result.length; i++) {
                        var item = result[i];
                        name += item.customGroupName;
                        name += '、';
                    }
                    name = name.substring(0, name.length - 1);
                    showAlert("客户群【" + name + "】已经被删除，不能参与运算！", "failed");
                }
            },
            error: function (response) {
                showAlert("查询被删除的客户群异常！", "failed");
            }
        });
    }

    function isSetProperties() {
        var flag = true;
        $(".conditionMore").each(function () {
            if ($.trim($(this).html()).indexOf('emptyGroup') > 0) {
                flag = false;
            }
        });
        return flag;
    }
    function isExistGroupOrVerticalLabel() {
        var flag = false;
        $(".conditionCT").each(function () {
            if ($(this).attr("labelTypeId") == 8 || $(this).attr("elementType") == 5) {//纵表标签和客户群
                flag = true;
                return false;
            }
        });
        return flag;
    }
    //打开保存成功弹出层
    function openSuccess(customerId, customerName, templateName, isHasList) {
        customerName = encodeURIComponent(encodeURIComponent(customerName));
        templateName = encodeURIComponent(encodeURIComponent(templateName));
        //$("#successDialog").dialog("close");
        var ifmUrl = "${ctx}/aibi_ci/customers/saveSuccessDialog.jsp?customerName="
            + customerName + "&templateName=" + templateName + "&customerId=" + customerId + "&isHasList=" + isHasList;
        $("#successFrame").attr("src", ifmUrl);
        $("#successDialog").dialog("open");
        clearCalEles();//保存成功后清空计算中心
    }

    //打开编辑对话框
    function openEdit() {
        $("#editDialog").dialog({
            close: function (event, ui) {
                $("#editFrame").attr("src", "");
            }
        });
        $("#editDialog").dialog("close");
        var ifmUrl = "${ctx}/aibi_ci/dialog/saveTemplateDialog.jsp";
        $("#editFrame").attr("src", ifmUrl).load(function () {
            resetTemplateForm();
        });
        $("#editDialog").dialog("open");

        $(".tishi").each(function () {
            var toffset = $(this).parent("td").offset();
            var td_height = $(this).parent("td").height();
            $(this).css({top: toffset.top + td_height + 13, left: toffset.left});
        });
    }
    function resetTemplateForm() {
        var ifm = $("#editFrame").contents();
        ifm.find("#template_labelOptRuleShow").val($("#labelOptRuleShow").val());
    }
    //保存模板
    function saveTemplate() {
        if (validateSql()) {
            openEdit();
        }
    }

    //推送设置
    function pushCustomGroup(customerGroupId) {
        var ifmUrl = "${ctx}/ci/customersManagerAction!prePushCustomerSingle.ai2do?ciCustomGroupInfo.customGroupId="
            + customerGroupId;
        $("#successDialog").dialog("close");
        $('#push_single_alarm_iframe').attr('src', ifmUrl);
        $('#dialog_push_single').dialog('open');
    }
    function pushCustomerGroupConfirm() {
        if ($("#push_single_alarm_iframe")[0].contentWindow.validateIsNull()) {
            commonUtil.create_alert_dialog("clearCalculateCenterOld", {
                txt: "确认推送该客户群？",
                type: "failed",
                width: 500,
                height: 200
            }, push);
            //showAlert("确认推送该客户群？","confirm",push);
        }
    }
    //推送提交
    function push() {
        var url = '${ctx}/ci/customersManagerAction!pushCustomerAfterSave.ai2do';
        $('#dialog_push_single').dialog('close');
        $("#push_single_alarm_iframe")[0].contentWindow.submitForm(url);
    }
    //关闭推送弹出层
    function closePush() {
        $("#dialog_push_single").dialog("close");
    }

    function checkDate(choiceDate) {
        var dimListTactics = $("#dimListTactics").val();
        $(".conditionCT").each(function () {
            var updateCycle = $(this).attr("updateCycle");
            //数据最新日期
            var dataDate = $(this).attr("dataDate");
            //生效日期
            var effectDate = $(this).attr("effectDate");
            if (updateCycle && updateCycle == updateCycleM) {
                //策略一只校验生效日期--最新日，只校验生效日期
                if (dimListTactics == tacticsThree) {//往前推三天，校验生效日期与最新数据日期
                    if (choiceDate > dataDate) {
                        if (!$(this).hasClass("redBorder")) {
                            //alert('checkDate');
                            $(this).addClass("redBorder");
                        }
                        redBorderFlag = true;
                    } else {
                        if ($(this).hasClass("redBorder")) {
                            $(this).removeClass("redBorder");
                        }
                    }
                } else if (dimListTactics == tacticsOne) {
                    $(this).removeClass("redBorder");
                }
            }
        });
    }
    //校验日标签,校验最新数据日期
    function checkDayDate(choiceDate) {
        var d = $("#newLabelDay").val();
        var dimListTactics = $("#dimListTactics").val();
        if (d != null && d != "") {
            $(".conditionCT").each(function () {
                var updateCycle = $(this).attr("updateCycle");
                //生效日期
                var effectDate = $(this).attr("effectDate");
                var dataDate = $(this).attr("dataDate");
                if (updateCycle && updateCycle == updateCycleD) {
                    //策略一只校验生效日期--最新日，只校验生效日期
                    if (dimListTactics == tacticsThree) {//往前推三天，校验生效日期与最新数据日期
                        if (choiceDate > dataDate) {
                            if (!$(this).hasClass("redBorder")) {
                                //alert('checkDayDate');
                                $(this).addClass("redBorder");
                            }
                            redBorderFlag = true;
                        } else {
                            if ($(this).hasClass("redBorder")) {
                                $(this).removeClass("redBorder");
                            }
                        }
                    } else if (dimListTactics == tacticsOne) {
                        $(this).removeClass("redBorder");
                    }
                }
            });
        }
    }
    function checkDataDate() {
        var dimListTactics = $("#dimListTactics").val();
        var choiceDateD = $("#dayDataDateHidden").val();
        var choiceDateM = $("#monthLabelDateHidden").val();
        var flag = true;
        $(".conditionCT").each(function () {
            var updateCycle = $(this).attr("updateCycle");
            if (updateCycle) {
                var dataDate = $(this).attr("dataDate");
                if (updateCycle == updateCycleD) {
                    if (choiceDateD > dataDate) {
                        flag = false;
                        commonUtil.create_alert_dialog("effectDateTip1", {
                            txt: "当前选择的日期数据未生成！",
                            type: "failed",
                            width: 500,
                            height: 200
                        });
                        //showAlert("当前选择的日期数据未生成！","failed");
                        return false;
                    }
                }
                if (updateCycle == updateCycleM) {
                    if (choiceDateM > dataDate) {
                        flag = false;
                        commonUtil.create_alert_dialog("effectDateTip1", {
                            txt: "当前选择的日期数据未生成！",
                            type: "failed",
                            width: 500,
                            height: 200
                        });
                        //showAlert("当前选择的日期数据未生成！","failed");
                        return false;
                    }
                }
            }

        });
        return flag
    }
    //校验生效日期
    function checkEffectDate() {
        $(".conditionCT").each(function () {
            var updateCycle = $(this).attr("updateCycle");
            //生效日期
            var effectDate = $(this).attr("effectDate");
            if (updateCycle && updateCycle == updateCycleD) {
                var choiceDate = $("#dayDataDateHidden").val();
                //策略一只校验生效日期--最新日，只校验生效日期
                if (choiceDate && choiceDate != "") {
                    if (choiceDate < effectDate) {
                        if (!$(this).hasClass("redBorder")) {
                            $(this).addClass("redBorder");
                        }
                        redBorderFlag = false;
                    }
                }
            }
            if (updateCycle && updateCycle == updateCycleM) {
                var choiceDate = $("#monthLabelDateHidden").val();
                //策略一只校验生效日期--最新日，只校验生效日期
                if (choiceDate && choiceDate != "") {
                    //alert(choiceDate + "-" + effectDate);
                    if (choiceDate < effectDate) {
                        if (!$(this).hasClass("redBorder")) {
                            $(this).addClass("redBorder");
                        }
                        redBorderFlag = false;
                    }
                }
            }
        });
    }
    function negationTip_remove() {
        $("#negationTip").remove();
    }

    function negationtipF() {
        var firstObj = $("div[_statusIndex^='labelIndex_'][labelTypeId!=8]").first();
        if (firstObj.length > 0) {
            var firstOffset = $(firstObj).offset();
            var firstTipHeight = parseInt(firstOffset.top, 10) - $("#negationTip").height();
            $("#negationTip").css({left: firstOffset.left - 10 + "px", top: firstTipHeight + "px"}).show();
        } else {
            $("#negationTip").hide();
        }
    }
    //修改客户群回写客户群  策略
    function callBackData() {
        var tacticsId = '${ciCustomGroupInfo.tacticsId }';
        $("#tacticsId_" + tacticsId).click();
    }
    //枚举或者文本类精确匹配时需要验证个数不能大于100个（oracle数据库超过1000个报错，而且过多影响数据探索，没有实际意义）
    function validateEnumCount() {
        var num = 0;
        var b = true;
        $("#tag_dropable .conditionCT[labelTypeId='5'],#tag_dropable .conditionCT[labelTypeId='7'],#tag_dropable .conditionCT>div[labelTypeId='5'],#tag_dropable .conditionCT>div[labelTypeId='7']").each(function (i, element) {
            var ele = $(element);
            var str = "";
            if (ele.attr('labelTypeId') == 5) {
                str = ele.attr('attrVal');
            } else {
                str = ele.attr('exactValue');
            }
            if (str != null && str != "" && str.split(",").length > _EnumCount) {
                b = false;
            }
            if (!b) {
                return b;
            }
        });
        if (!b) {
            return false;
        }
    }
    //iframe 方式 子调用父方法 关闭弹出框
    function closeDialog(obj) {
        $(obj).dialog("close");
    }

</script>
<div class="mainBody" id="calcCenterBody">
    <div class="labelComputCtrls">
        <div class="fleft labelDate">
            &nbsp;&nbsp;月<input name="dataDate" type="text" id="dataDate" value="${newLabelMonthFormat}"
                                readonly class="datepicker_input"
                                onclick="WdatePicker({onpicked:datePicked, ychanged:changeEndDate,Mchanged:changeEndDate, dateFmt:'yyyy-MM',maxDate:'${newLabelMonthFormat}',minDate:'#F{$dp.$DV(\'${newLabelMonthFormat}\',{y:-1});}'})"/>
        </div>
        <% if (labelUpdateInBatche.equalsIgnoreCase("true")) { %>
        <% if (customSaveTactics.equalsIgnoreCase("true")) { %>
        <div class="fleft labelDate">
            &nbsp;&nbsp;日<input name="dayDataDate" type="text" id="dayDataDate" value="${newLabelDayFormat}"
                                readonly class="datepicker_input"
                                onclick="WdatePicker({onpicked:dayDatePicked, ychanged:changeEndDate,Mchanged:changeEndDate, dateFmt:'yyyy-MM-dd',maxDate:'${newLabelDayFormat}',minDate:'#F{$dp.$DV(\'${newLabelDayFormat}\',{d:-6});}'})"/>
        </div>
        <% } else {%>
        <div class="fleft labelDataDate">
            <p class="fleft">&nbsp;&nbsp;日&nbsp;&nbsp;</p>
            <p class="fleft">${newLabelDayFormat}</p>
        </div>
        <% } %>
        <% if (customSaveTactics.equalsIgnoreCase("true")) { %>
        <div class="fleft separate">&nbsp;</div>
        <div class="fleft">
            <label class="fleft labFmt30">策略:</label>
            <div class="fleft formQuerySelBox">
                <div class="selTxt">保守数据策略</div>
                <a href="javascript:void(0);" class="selBtn"></a>
                <div class="querySelListBox">
                    <input type="hidden" name="dimListTactics" id="dimListTactics2" value=""/>
                    <dl>
                        <dd>
                            <c:forEach items="${dimListTacticsList}" var="dimListTactics" varStatus="st">
                                <a href="javascript:void(0);" value="${dimListTactics.tacticsId }"
                                   id="tacticsId_${dimListTactics.tacticsId }">${dimListTactics.tacticsName }</a>
                            </c:forEach>
                        </dd>
                    </dl>
                </div>
            </div>
            <span class="conditionTipIcon labelConditionTipIcon calculateCenterTip" onclick="tacticsTip(event, this);">&nbsp;</span>
        </div>
        <% } %>
        <% } %>
        <div class="fleft separate">&nbsp;</div>
        <div id="parenthesis" class="fleft parenthesis">
            <div class="leftParenthesis"></div>
            <div class="rightParenthesis"></div>
        </div>
        <div class="fleft separate">&nbsp;</div>
        <ul class="fleft controlAll">
            <li><input class="hiddenCheckbox" type="checkbox"/>
                <div class="queryWay">全部(且)</div>
            </li>
            <li><input class="hiddenCheckbox" type="checkbox"/>
                <div class="queryWay">全部(或)</div>
            </li>
            <li><input class="hiddenCheckbox" type="checkbox"/>
                <div class="queryWay">全部(剔除)</div>
            </li>
        </ul>

    </div><!--labelComputCtrls end -->
    <div id="calculateContain" class="labelComput">
        <jsp:include page="calculateBodyOld.jsp"/>
    </div><!--labelComput end -->
</div>
<!--mainBody end -->

<!-- 点击标签上的叹号，下拉内容： -->
<!--labelTip-->
<div id="labelTip" class="labelTip">
</div>

<!--customTip-->
<div id="customTip" class="labelTip">
</div>

<!--tacticsTip-->
<div id="tacticsTip" class="labelTip">
</div>

<div id="tagSaveActionBox" class="mainBody tagSaveActionBox">
    <div class="arrowBox">
        <div id="tagCalcDetail" class="arrowUp">折叠</div>
    </div>
    <div class="labelsCTBtm maxHeight" id="labelsCTBtm">
        <c:forEach var="result" items="${sessionModelList}">
            <c:if test="${result.elementType == 3}">
                <c:if test="${result.calcuElement == '(' }">
                    <c:out value="("></c:out>
                </c:if>
                <c:if test="${result.calcuElement == ')' }">
                    <c:out value=") "></c:out>
                </c:if>
            </c:if>
            <c:if test="${result.elementType == 1}">
                <c:if test="${result.calcuElement == 'and' }">
                    <c:out value="且 "></c:out>
                </c:if>
                <c:if test="${result.calcuElement == 'or' }">
                    <c:out value="或 "></c:out>
                </c:if>
                <c:if test="${result.calcuElement == '-' }">
                    <c:out value="剔除 "></c:out>
                </c:if>
            </c:if>
            <c:if test="${result.elementType == 2}">
                <c:if test="${result.labelFlag != 1}">
                    <c:out value="(非)"></c:out></c:if><c:out value="${result.customOrLabelName}"></c:out><c:if
                    test="${result.labelTypeId == 4}"><c:if test="${result.queryWay == 1}"><c:out
                    value="[数值范围："></c:out><c:if test="${not empty result.contiueMinVal }"><c:if
                    test="${result.leftZoneSign eq '>=' }">大于等于${result.contiueMinVal}</c:if><c:if
                    test="${result.leftZoneSign eq '>' }">大于${result.contiueMinVal}</c:if></c:if><c:if
                    test="${not empty result.contiueMaxVal }"><c:if
                    test="${result.rightZoneSign eq '<='}">小于等于${result.contiueMaxVal}</c:if><c:if
                    test="${result.rightZoneSign eq '<'}">小于${result.contiueMaxVal}</c:if></c:if><c:if
                    test="${not empty result.unit}">(${result.unit})</c:if><c:out value="]"></c:out></c:if><c:if
                    test="${result.queryWay == 2}"><c:if
                    test="${not empty result.exactValue}">[数值范围：${result.exactValue}<c:if
                    test="${result.unit}">(${result.unit})</c:if>]</c:if></c:if></c:if><c:if
                    test="${result.labelTypeId == 5}"><c:if
                    test="${not empty result.attrVal}">[已选择条件：${result.attrName}]</c:if></c:if><c:if
                    test="${result.labelTypeId == 6}"><c:if test="${result.queryWay == 1}"><c:if
                    test="${not empty result.startTime || not empty result.endTime}"><c:out
                    value="[已选择条件："></c:out><c:if test="${not empty result.startTime }"><c:if
                    test="${result.leftZoneSign eq '>=' }">大于等于${result.startTime}</c:if><c:if
                    test="${result.leftZoneSign eq '>' }">大于${result.startTime}</c:if></c:if><c:if
                    test="${not empty result.endTime }"><c:if
                    test="${result.rightZoneSign eq '<='}">小于等于${result.endTime}</c:if><c:if
                    test="${result.rightZoneSign eq '<'}">小于${result.endTime}</c:if></c:if><c:if
                    test="${result.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if
                    test="${result.queryWay == 2}"><c:if test="${not empty result.exactValue }"><c:out
                    value="[已选择条件："></c:out><c:if
                    test="${not empty fn:split(result.exactValue,',')[0] && fn:split(result.exactValue,',')[0]!='-1'}">${fn:split(result.exactValue,',')[0]}年</c:if><c:if
                    test="${not empty fn:split(result.exactValue,',')[1] && fn:split(result.exactValue,',')[1]!='-1'}">${fn:split(result.exactValue,',')[1]}月</c:if><c:if
                    test="${not empty fn:split(result.exactValue,',')[2] && fn:split(result.exactValue,',')[2]!='-1'}">${fn:split(result.exactValue,',')[2]}日</c:if><c:out
                    value="]"></c:out></c:if></c:if></c:if><c:if test="${result.labelTypeId == 7}"><c:if
                    test="${result.queryWay == 1}"><c:if
                    test="${not empty result.darkValue}">[模糊值：${result.darkValue}]</c:if></c:if><c:if
                    test="${result.queryWay == 2}"><c:if
                    test="${not empty result.exactValue}">[精确值：${result.exactValue}]</c:if></c:if></c:if><c:if
                    test="${result.labelTypeId == 8}"><c:forEach var="item" items="${result.childCiLabelRuleList}"><c:if
                    test="${item.labelTypeId == 4}"><c:if test="${item.queryWay == 1}">[${item.columnCnName}：<c:if
                    test="${not empty item.contiueMinVal }"><c:if
                    test="${item.leftZoneSign eq '>=' }">大于等于${item.contiueMinVal}</c:if><c:if
                    test="${item.leftZoneSign eq '>' }">大于${item.contiueMinVal}</c:if></c:if><c:if
                    test="${not empty item.contiueMaxVal }"><c:if
                    test="${item.rightZoneSign eq '<='}">小于等于${item.contiueMaxVal}</c:if><c:if
                    test="${item.rightZoneSign eq '<'}">小于${item.contiueMaxVal}</c:if></c:if><c:if
                    test="${not empty item.unit}">(${item.unit})</c:if>]</c:if><c:if test="${item.queryWay == 2}"><c:if
                    test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}<c:if
                    test="${not empty item.unit}">(${item.unit})</c:if>]</c:if></c:if></c:if><c:if
                    test="${item.labelTypeId == 5}"><c:if
                    test="${not empty item.attrVal}">[${item.columnCnName}：${item.attrName}]</c:if></c:if><c:if
                    test="${item.labelTypeId == 6}"><c:if test="${item.queryWay == 1}"><c:if
                    test="${not empty item.startTime || not empty item.endTime}">[${item.columnCnName}：<c:if
                    test="${not empty item.startTime }"><c:if
                    test="${item.leftZoneSign eq '>=' }">大于等于${item.startTime}</c:if><c:if
                    test="${item.leftZoneSign eq '>' }">大于${item.startTime}</c:if></c:if><c:if
                    test="${not empty item.endTime }"><c:if
                    test="${item.rightZoneSign eq '<='}">小于等于${item.endTime}</c:if><c:if
                    test="${item.rightZoneSign eq '<'}">小于${item.endTime}</c:if></c:if><c:if
                    test="${item.isNeedOffset == 1}">（动态偏移更新）</c:if><c:out value="]"></c:out></c:if></c:if><c:if
                    test="${item.queryWay == 2}"><c:if test="${not empty item.exactValue }">[${item.columnCnName}：<c:if
                    test="${not empty fn:split(item.exactValue,',')[0] && fn:split(item.exactValue,',')[0]!='-1'}">${fn:split(item.exactValue,',')[0]}年</c:if><c:if
                    test="${not empty fn:split(item.exactValue,',')[1] && fn:split(item.exactValue,',')[1]!='-1'}">${fn:split(item.exactValue,',')[1]}月</c:if><c:if
                    test="${not empty fn:split(item.exactValue,',')[2] && fn:split(item.exactValue,',')[2]!='-1'}">${fn:split(item.exactValue,',')[2]}日</c:if><c:out
                    value="]"></c:out></c:if></c:if></c:if><c:if test="${item.labelTypeId == 7}"><c:if
                    test="${item.queryWay == 1}"><c:if
                    test="${not empty item.darkValue}">[${item.columnCnName}：${item.darkValue}]</c:if></c:if><c:if
                    test="${item.queryWay == 2}"><c:if
                    test="${not empty item.exactValue}">[${item.columnCnName}：${item.exactValue}]</c:if>
            </c:if>
            </c:if>
            </c:forEach>
            </c:if>
            </c:if>
            <c:if test="${result.elementType == 5}">
                <c:out value="客户群：${result.customOrLabelName}"></c:out><c:if test="${not empty result.attrVal}"><c:out
                    value="[已选清单：${result.attrVal}]"></c:out>
            </c:if>
            </c:if>
        </c:forEach>
    </div>
    <div class="tagSaveBtnBox">
        <input type="button" value="下一步" id="tagCalcSaveBtn"/>
        <% if ("true".equalsIgnoreCase(templateMenu)) { %>
        <input type="button" value="保存模板" id="tagCalcSaveTemplateBtn"/>
        <% } %>
        <input type="button" value="客户群规模" id="tagCalcExplore"/>
        <input type="button" value="上一步" id="calculateAddLable"/>
        <p class="fleft" id="customerNum"></p>
    </div>

</div>

<ol id="selectedCustomGroupAttrRel" style="display:none;">
    <c:forEach var="ciGroupAttrRel" items="${ciGroupAttrRelList}" varStatus="st">
        <li>
            <div class="clientSelectItem"
                    <c:if test="${ciGroupAttrRel.attrSource == 2}">
                        <c:if test="${ not empty ciGroupAttrRel.labelOrCustomColumn}">checkedboxId="${ciGroupAttrRel.labelOrCustomId}_${ciGroupAttrRel.labelOrCustomColumn}"</c:if>
                        <c:if test="${ empty ciGroupAttrRel.labelOrCustomColumn}">checkedboxId="${ciGroupAttrRel.labelOrCustomId}"</c:if>
                    </c:if>
                 <c:if test="${ciGroupAttrRel.attrSource == 3}">checkedboxId="${ciGroupAttrRel.customId}_${ciGroupAttrRel.labelOrCustomColumn}"</c:if>
                 attrSource="${ciGroupAttrRel.attrSource}" labelOrCustomColumn="${ciGroupAttrRel.labelOrCustomColumn}">
				<span class="clientSelectItemText">
				<c:if test="${ciGroupAttrRel.attrSource == 3}">${ciGroupAttrRel.customName}-</c:if>
                        ${ciGroupAttrRel.attrColName}</span>
                <a href="javascript:void(0);" class="clientSelectItemClose"></a>
            </div>
        </li>
    </c:forEach>
</ol>

<form id="labelForm" method="post" action="${ctx}/ci/customersManagerAction!saveCustomByRulesInit.ai2do" target="_self">
    <div id="inputList" style="display:none;"></div>
    <input id="dataDateHidden" type="hidden" name="ciCustomGroupInfo.dataDate" value="${ciCustomGroupInfo.dataDate }"/>
    <input id="createTypeId" type="hidden" name="ciCustomGroupInfo.createTypeId" value="1"/>
    <c:if test='${ciCustomGroupInfo.createUserId != null && ciCustomGroupInfo.createUserId != ""}'>
        <input id="createUserId" type="hidden" name="ciCustomGroupInfo.createUserId"
               value="${ciCustomGroupInfo.createUserId}"/>
    </c:if>
    <c:if test='${ciCustomGroupInfo.createTime != null && ciCustomGroupInfo.createTime != ""}'>
        <input id="createTime" type="hidden" name="ciCustomGroupInfo.createTime"
               value='<fmt:formatDate value="${ciCustomGroupInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
    </c:if>
    <input type="hidden" name="saveCustomersFlag" value="5"/>
    <input type="hidden" name="exploreFromModuleFlag" value="1"/>
    <input type="hidden" name="ciCustomGroupInfo.dayLabelDate" id="dayDataDateHidden" value=""/>
    <input type="hidden" name="ciCustomGroupInfo.monthLabelDate" id="monthLabelDateHidden" value=""/>
    <input type="hidden" name="ciCustomGroupInfo.tacticsId" id="dimListTactics"
           value="<%=ServiceConstants.LIST_TABLE_TACTICS_ID_THREE %>"></input>
    <input id="labelOptRuleShow" type="hidden" name="ciCustomGroupInfo.labelOptRuleShow" value=""/>
    <!-- 页面传值 -->
    <input id="customGroupId" type="hidden" name="ciCustomGroupInfo.customGroupId"
           value="${ciCustomGroupInfo.customGroupId}"/>
    <input id="customGroupName" type="hidden" name="ciCustomGroupInfo.customGroupName"
           value="${ciCustomGroupInfo.customGroupName}"/>
    <input id="customGroupDesc" type="hidden" name="ciCustomGroupInfo.customGroupDesc"
           value="${ciCustomGroupInfo.customGroupDesc}"/>
    <input id="updateCycle" type="hidden" name="ciCustomGroupInfo.updateCycle"
           value="${ciCustomGroupInfo.updateCycle}"/>
    <input id="dataStatus" type="hidden" name="ciCustomGroupInfo.dataStatus" value="${ciCustomGroupInfo.dataStatus}"/>
    <input id="listCreateTime" type="hidden" name="ciCustomGroupInfo.listCreateTime"
           value="${ciCustomGroupInfo.listCreateTime}"/>
    <input id="startDate" type="hidden" name="ciCustomGroupInfo.startDate"
           value='<fmt:formatDate value="${ciCustomGroupInfo.startDate}" pattern="yyyy-MM-dd"/>'/>
    <input id="endDate" type="hidden" name="ciCustomGroupInfo.endDate"
           value='<fmt:formatDate value="${ciCustomGroupInfo.endDate}" pattern="yyyy-MM-dd"/>'/>
    <!-- 添加是否私有与创建地市字段 -->
    <input id="sceneId" type="hidden" name="ciCustomGroupInfo.sceneId" value="${ciCustomGroupInfo.sceneId }"/>
    <input id="isPrivate" type="hidden" name="ciCustomGroupInfo.isPrivate" value="${ciCustomGroupInfo.isPrivate }"/>
    <input id="isHasList" type="hidden" name="ciCustomGroupInfo.isHasList" value="${ciCustomGroupInfo.isHasList}"/>
    <input id="createCityId" type="hidden" name="ciCustomGroupInfo.createCityId"
           value="${ciCustomGroupInfo.createCityId }"/>
    <input id="listMaxNum" type="hidden" name="ciCustomGroupInfo.listMaxNum" value="${ciCustomGroupInfo.listMaxNum}"/>

    <input id="newLabelDay" type="hidden" name="newLabelDay" value="${newLabelDayFormat}"/>
</form>

<div id="negationTip" class="dragFirstTagTip">
    <p class="tipTxt">鼠标单击标签可取反!
        <a href="javascript:void(0);" onclick="negationTip_remove()"></a>
    </p>
    <div class="tipArrowDown"></div>
</div>
<div id="saveCustomerDialog" style="overflow:hidden; display:none;">
    <iframe id="saveCustomerFrame" name="saveCustomerFrame" src="" framespacing="0" border="0" frameborder="0"
            style="width:100%;height:569px;" scrolling="no"></iframe>
</div>
<div id="itemChoose" style="overflow:hidden;">
    <iframe id="itemChooseFrame" name="itemChooseFrame" src="" framespacing="0" border="0" frameborder="0"
            style="width:100%;height:453px;" scrolling="no"></iframe>
</div>
<div id="darkValueSet" style="overflow:hidden;">
    <iframe id="darkValueSetFrame" name="darkValueSetFrame" src="" framespacing="0" border="0" frameborder="0"
            style="width:100%;height:290px;" scrolling="no"></iframe>
</div>
<div id="verticalLabelSetDialog">
    <iframe id="verticalLabelSetFrame" name="verticalLabelSetFrame" scrolling="no" allowtransparency="true" src=""
            framespacing="0" border="0" frameborder="0" style="width:100%;height:480px"></iframe>
</div>
<div id="successDialog" style="overflow:hidden;">
    <iframe id="successFrame" name="successFrame" src="" framespacing="0" border="0" frameborder="0"
            style="width:100%;height:430px;" scrolling="no"></iframe>
</div>
<div id="editDialog" style="overflow:hidden;">
    <iframe id="editFrame" name="editFrame" scrolling="no" allowtransparency="true" src="" framespacing="0" border="0"
            frameborder="0" style="width:100%;height:310px"></iframe>
</div>

<div id="labelAttrValidateDialog" style="overflow:hidden;">
    <iframe id="labelAttrValidateFrame" name="labelAttrValidateFrame" scrolling="no" allowtransparency="true" src=""
            framespacing="0" border="0" frameborder="0" style="width:100%;height:300px"></iframe>
</div>
<!-- 推送对应标签 -->
<div id="dialog_push_single" style="overflow:hidden;">
    <iframe id="push_single_alarm_iframe" name="push_single_alarm_iframe" scrolling="no" allowtransparency="true" src=""
            framespacing="0" border="0" frameborder="0" style="width:100%;height:335px"></iframe>
</div>
