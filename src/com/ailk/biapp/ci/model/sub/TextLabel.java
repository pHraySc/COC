package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.LabelElement;
import com.asiainfo.biframe.utils.string.StringUtil;

public class TextLabel extends LabelElement {
    public TextLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        String exactValsStr;
        if(StringUtil.isNotEmpty(ciLabelRule.getTableName())) {
            exactValsStr = ciLabelRule.getTableName();
            String vals = "VALUE_ID";
            String j = "select " + vals + " from " + exactValsStr;
            if(0 == flag) {
                wherelabel.append(" ").append(asName).append(" not in (").append(j).append(")");
            } else if(1 == flag) {
                wherelabel.append(" ").append(asName).append("  in (").append(j).append(")");
            }
        } else {
            exactValsStr = ciLabelRule.getExactValue();
            if(StringUtil.isEmpty(exactValsStr)) {
                if(0 == flag) {
                    wherelabel.append(" ").append(asName).append(" not like \'%").append(ciLabelRule.getDarkValue()).append("%\'");
                } else if(1 == flag) {
                    wherelabel.append(" ").append(asName).append(" like \'%").append(ciLabelRule.getDarkValue()).append("%\'");
                }
            } else {
                String[] var12 = exactValsStr.split(",");
                int var13;
                if(1 == flag) {
                    if(var12.length != 1 && !isValidate) {
                        wherelabel.append(" ").append(asName).append(" in (");

                        for(var13 = 0; var13 < var12.length; ++var13) {
                            wherelabel.append("\'").append(var12[var13]).append("\'");
                            if(var13 != var12.length - 1) {
                                wherelabel.append(",");
                            }
                        }

                        wherelabel.append(")");
                    } else {
                        wherelabel.append(" ").append(asName).append(" = \'").append(var12[0]).append("\'");
                    }
                } else if(0 == flag) {
                    if(var12.length != 1 && !isValidate) {
                        wherelabel.append(" ").append(asName).append(" not in (");

                        for(var13 = 0; var13 < var12.length; ++var13) {
                            wherelabel.append("\'").append(var12[var13]).append("\'");
                            if(var13 != var12.length - 1) {
                                wherelabel.append(",");
                            }
                        }

                        wherelabel.append(")");
                    } else {
                        wherelabel.append(" ").append(asName).append(" ").append("<>").append(" \'").append(var12[0]).append("\'");
                    }
                }
            }
        }

        return wherelabel.toString();
    }
}
