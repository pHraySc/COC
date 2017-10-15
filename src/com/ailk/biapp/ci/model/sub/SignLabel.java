package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.LabelElement;

public class SignLabel extends LabelElement {
    public SignLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        if(0 == flag) {
            wherelabel.append(" ").append(asName).append(" = ").append(ciLabelRule.getMinVal().intValue());
        } else if(1 == flag) {
            wherelabel.append(" ").append(asName).append(" = ").append(ciLabelRule.getMaxVal().intValue());
        }

        return wherelabel.toString();
    }
}
