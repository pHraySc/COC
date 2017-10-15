package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelElement;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Date;
import org.apache.log4j.Logger;

public class DateLabel extends LabelElement {
    private Logger log = Logger.getLogger(DateLabel.class);

    public DateLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        String nullInput = "-1";
        String leftZoneSign = ciLabelRule.getLeftZoneSign();
        String rightZoneSign = ciLabelRule.getRightZoneSign();
        String startTime = ciLabelRule.getStartTime();
        Integer isNeedOffsetObj = ciLabelRule.getIsNeedOffset();
        int isNeedOffset = 0;
        if(null != isNeedOffsetObj) {
            isNeedOffset = ciLabelRule.getIsNeedOffset().intValue();
        }

        if(StringUtil.isNotEmpty(startTime)) {
            if(ServiceConstants.IS_NEED_OFFSET_YES == isNeedOffset && interval.intValue() != 0) {
                startTime = DateUtil.calculateOffsetDate(startTime, interval.intValue(), "yyyy-MM-dd", updateCycle.intValue());
            } else {
                Date endTime = DateUtil.string2Date(startTime, "yyyy-MM-dd");
                startTime = DateUtil.date2String(endTime, "yyyy-MM-dd");
            }

            if(leftZoneSign.equals(">=")) {
                startTime = startTime + " 00:00:00";
            } else if(leftZoneSign.equals(">")) {
                startTime = startTime + " 23:59:59";
            }
        }

        String endTime1 = ciLabelRule.getEndTime();
        if(StringUtil.isNotEmpty(endTime1)) {
            if(ServiceConstants.IS_NEED_OFFSET_YES == isNeedOffset && interval.intValue() != 0) {
                endTime1 = DateUtil.calculateOffsetDate(endTime1, interval.intValue(), "yyyy-MM-dd", updateCycle.intValue());
            } else {
                Date exactValsStr = DateUtil.string2Date(endTime1, "yyyy-MM-dd");
                endTime1 = DateUtil.date2String(exactValsStr, "yyyy-MM-dd");
            }

            if(rightZoneSign.equals("<=")) {
                endTime1 = endTime1 + " 23:59:59";
            } else if(rightZoneSign.equals("<")) {
                endTime1 = endTime1 + " 00:00:00";
            }
        }

        String[] vals;
        String year;
        String month;
        String day;
        String exactValsStr1;
        if(0 == flag) {
            if(StringUtil.isNotEmpty(startTime) && StringUtil.isEmpty(endTime1)) {
                if(leftZoneSign.equals(">=")) {
                    wherelabel.append(" ").append(asName).append(" ").append("<").append(" \'").append(startTime).append("\'");
                } else if(leftZoneSign.equals(">")) {
                    wherelabel.append(" ").append(asName).append(" ").append("<=").append(" \'").append(startTime).append("\'");
                }
            } else if(StringUtil.isNotEmpty(endTime1) && StringUtil.isEmpty(startTime)) {
                if(rightZoneSign.equals("<=")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">").append(" \'").append(endTime1).append("\'");
                } else if(rightZoneSign.equals("<")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">=").append(" \'").append(endTime1).append("\'");
                }
            } else if(StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime1)) {
                if(leftZoneSign.equals(">=")) {
                    wherelabel.append(" ( ").append(asName).append(" ").append("<").append(" \'").append(startTime).append("\'");
                } else if(leftZoneSign.equals(">")) {
                    wherelabel.append(" ( ").append(asName).append(" ").append("<=").append(" \'").append(startTime).append("\'");
                }

                wherelabel.append(" or");
                if(rightZoneSign.equals("<=")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">").append(" \'").append(endTime1).append("\' )");
                } else if(rightZoneSign.equals("<")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">=").append(" \'").append(endTime1).append("\' )");
                }
            } else if(StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime1) && StringUtil.isNotEmpty(ciLabelRule.getExactValue())) {
                exactValsStr1 = ciLabelRule.getExactValue();
                vals = exactValsStr1.split(",");
                if(vals.length != 3) {
                    this.log.error("精确日期传值错误");
                    throw new CIServiceException("精确日期传值错误");
                }

                year = vals[0];
                month = vals[1];
                day = vals[2];
                if(!month.equals(nullInput) && month.length() == 1) {
                    month = "0" + month;
                }

                if(!day.equals(nullInput) && day.length() == 1) {
                    day = "0" + day;
                }

                if(year.equals(nullInput) && month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" not like \'%-").append(day).append(" %\'");
                } else if(year.equals(nullInput) && !month.equals(nullInput) && day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" not like \'%-").append(month).append("-%\'");
                } else if(!year.equals(nullInput) && month.equals(nullInput) && day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" not like \'").append(year).append("-%\'");
                } else if(!year.equals(nullInput) && !month.equals(nullInput) && day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" not like \'").append(year).append("-").append(month).append("-%\'");
                } else if(year.equals(nullInput) && !month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" not like \'%-").append(month).append("-").append(day).append("%\'");
                } else if(!year.equals(nullInput) && month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" (").append(asName).append(" not like \'").append(year).append("-%\'").append(" or ").append(asName).append(" not like \'%-").append(day).append(" %\')");
                } else if(!year.equals(nullInput) && !month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" not like \'").append(year).append("-").append(month).append("-").append(day).append("%\'");
                }
            }
        } else if(1 == flag) {
            if(StringUtil.isNotEmpty(startTime) && StringUtil.isEmpty(endTime1)) {
                wherelabel.append(" ").append(asName).append(" ").append(leftZoneSign).append(" \'").append(startTime).append("\'");
            } else if(StringUtil.isNotEmpty(endTime1) && StringUtil.isEmpty(startTime)) {
                wherelabel.append(" ").append(asName).append(" ").append(rightZoneSign).append(" \'").append(endTime1).append("\'");
            } else if(StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime1)) {
                wherelabel.append(" ( ").append(asName).append(" ").append(leftZoneSign).append(" \'").append(startTime).append("\'");
                wherelabel.append(" and");
                wherelabel.append(" ").append(asName).append(" ").append(rightZoneSign).append(" \'").append(endTime1).append("\' )");
            } else if(StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime1) && StringUtil.isNotEmpty(ciLabelRule.getExactValue())) {
                exactValsStr1 = ciLabelRule.getExactValue();
                vals = exactValsStr1.split(",");
                if(vals.length != 3) {
                    this.log.error("精确日期传值错误");
                    throw new CIServiceException("精确日期传值错误");
                }

                year = vals[0];
                month = vals[1];
                day = vals[2];
                if(!month.equals(nullInput) && month.length() == 1) {
                    month = "0" + month;
                }

                if(!day.equals(nullInput) && day.length() == 1) {
                    day = "0" + day;
                }

                if(year.equals(nullInput) && month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" like \'%-").append(day).append(" %\'");
                } else if(year.equals(nullInput) && !month.equals(nullInput) && day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" like \'%-").append(month).append("-%\'");
                } else if(!year.equals(nullInput) && month.equals(nullInput) && day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" like \'").append(year).append("-%\'");
                } else if(!year.equals(nullInput) && !month.equals(nullInput) && day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" like \'").append(year).append("-").append(month).append("-%\'");
                } else if(year.equals(nullInput) && !month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" like \'%-").append(month).append("-").append(day).append("%\'");
                } else if(!year.equals(nullInput) && month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" (").append(asName).append(" like \'").append(year).append("-%\'").append(" and ").append(asName).append(" like \'%-").append(day).append(" %\')");
                } else if(!year.equals(nullInput) && !month.equals(nullInput) && !day.equals(nullInput)) {
                    wherelabel.append(" ").append(asName).append(" like \'").append(year).append("-").append(month).append("-").append(day).append("%\'");
                }
            }
        }

        return wherelabel.toString();
    }
}
