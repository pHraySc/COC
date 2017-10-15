package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.LabelElement;
import com.asiainfo.biframe.utils.string.StringUtil;

public class ScoreLabel extends LabelElement {
    public ScoreLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        if(!StringUtil.isEmpty(asName) && (!StringUtil.isNotEmpty(asName) || asName.contains("."))) {
            String[] str = asName.split(",");
            String alias = str[0];
            if(0 == flag) {
                wherelabel.append(" ").append(asName).append(" = ").append(flag);
            } else if(1 == flag) {
                wherelabel.append(" ").append(asName).append(" = ").append(flag);
            } else if(2 == flag) {
                wherelabel.append(" (").append(alias).append(".").append(column.getColumnName()).append("_SCORE").append(" < ").append(ciLabelRule.getMinVal()).append(" or ").append(alias).append(".").append(column.getColumnName()).append("_SCORE").append(" > ").append(ciLabelRule.getMaxVal()).append(")");
            } else if(3 == flag) {
                wherelabel.append(" (").append(alias).append(".").append(column.getColumnName()).append("_SCORE").append(" >= ").append(ciLabelRule.getMinVal()).append(" and ").append(alias).append(".").append(column.getColumnName()).append("_SCORE").append(" <= ").append(ciLabelRule.getMaxVal()).append(")");
            }

            return wherelabel.toString();
        } else {
            return wherelabel.toString();
        }
    }
}
