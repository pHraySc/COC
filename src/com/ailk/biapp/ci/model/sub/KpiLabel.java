package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.LabelElement;
import com.asiainfo.biframe.utils.string.StringUtil;

public class KpiLabel extends LabelElement {
    public KpiLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        String minVal = ciLabelRule.getContiueMinVal();
        String maxVal = ciLabelRule.getContiueMaxVal();
        String leftZoneSign = ciLabelRule.getLeftZoneSign();
        String rightZoneSign = ciLabelRule.getRightZoneSign();
        String exactValsStr;
        String[] vals;
        if(0 == flag) {
            if(StringUtil.isNotEmpty(minVal) && StringUtil.isEmpty(maxVal)) {
                if(leftZoneSign.equals(">=")) {
                    wherelabel.append(" ").append(asName).append(" ").append("<").append(" ").append(minVal);
                } else if(leftZoneSign.equals(">")) {
                    wherelabel.append(" ").append(asName).append(" ").append("<=").append(" ").append(minVal);
                }
            } else if(StringUtil.isNotEmpty(maxVal) && StringUtil.isEmpty(minVal)) {
                if(rightZoneSign.equals("<=")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">").append(" ").append(maxVal);
                } else if(rightZoneSign.equals("<")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">=").append(" ").append(maxVal);
                }
            } else if(StringUtil.isNotEmpty(minVal) && StringUtil.isNotEmpty(maxVal)) {
                if(leftZoneSign.equals(">=")) {
                    wherelabel.append(" ( ").append(asName).append(" ").append("<").append(" ").append(minVal);
                } else if(leftZoneSign.equals(">")) {
                    wherelabel.append(" ( ").append(asName).append(" ").append("<=").append(" ").append(minVal);
                }

                wherelabel.append(" or");
                if(rightZoneSign.equals("<=")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">").append(" ").append(maxVal).append(" )");
                } else if(rightZoneSign.equals("<")) {
                    wherelabel.append(" ").append(asName).append(" ").append(">=").append(" ").append(maxVal).append(" )");
                }
            } else if(StringUtil.isEmpty(minVal) && StringUtil.isEmpty(maxVal)) {
                exactValsStr = ciLabelRule.getExactValue();
                vals = exactValsStr.split(",");
                if(vals.length == 1) {
                    wherelabel.append(" ").append(asName).append(" ").append("<>").append(" ").append(vals[0]);
                } else {
                    wherelabel.append(" (");

                    for(int j = 0; j < vals.length; ++j) {
                        wherelabel.append(" ").append(asName).append(" ").append("<>").append(" ").append(vals[j]);
                        if(j != vals.length - 1) {
                            wherelabel.append(" and");
                        }
                    }

                    wherelabel.append(" )");
                }
            }
        } else if(1 == flag) {
            if(StringUtil.isNotEmpty(minVal) && StringUtil.isEmpty(maxVal)) {
                wherelabel.append(" ").append(asName).append(" ").append(leftZoneSign).append(" ").append(minVal);
            } else if(StringUtil.isNotEmpty(maxVal) && StringUtil.isEmpty(minVal)) {
                wherelabel.append(" ").append(asName).append(" ").append(rightZoneSign).append(" ").append(maxVal);
            } else if(StringUtil.isNotEmpty(minVal) && StringUtil.isNotEmpty(maxVal)) {
                wherelabel.append(" ( ").append(asName).append(" ").append(leftZoneSign).append(" ").append(minVal);
                wherelabel.append(" and");
                wherelabel.append(" ").append(asName).append(" ").append(rightZoneSign).append(" ").append(maxVal).append(" )");
            } else if(StringUtil.isEmpty(minVal) && StringUtil.isEmpty(maxVal)) {
                exactValsStr = ciLabelRule.getExactValue();
                vals = exactValsStr.split(",");
                if(vals.length == 1) {
                    wherelabel.append(" ").append(asName).append(" = ").append(vals[0]);
                } else {
                    if(exactValsStr.length() == exactValsStr.lastIndexOf(",")) {
                        exactValsStr = exactValsStr.substring(0, exactValsStr.length() - 1);
                    }

                    wherelabel.append(" ").append(asName).append(" in (").append(exactValsStr).append(")");
                }
            }
        }

        return wherelabel.toString();
    }
}
