package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelElement;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;

public class AttrLabel extends LabelElement {
    private Logger log = Logger.getLogger(AttrLabel.class);

    public AttrLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        String attrVal = ciLabelRule.getAttrVal();
        if(StringUtil.isEmpty(attrVal)) {
            this.log.error("属性值传值为空");
            throw new CIServiceException("属性值传值为空");
        } else {
            if(1 == column.getColumnDataTypeId().intValue()) {
                if(0 == flag) {
                    wherelabel.append(" ").append(asName).append(" != ").append(attrVal);
                } else {
                    wherelabel.append(" ").append(asName).append(" = ").append(attrVal);
                }
            } else if(2 == column.getColumnDataTypeId().intValue()) {
                if(0 == flag) {
                    wherelabel.append(" ").append(asName).append(" != \'").append(attrVal).append("\'");
                } else {
                    wherelabel.append(" ").append(asName).append(" = \'").append(attrVal).append("\'");
                }
            }

            return wherelabel.toString();
        }
    }
}
