package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;

public abstract class LabelElement {
    public LabelElement() {
    }

    public abstract String getConditionSql(CiLabelRule var1, CiMdaSysTableColumn var2, String var3, Integer var4, Integer var5, boolean var6);
}
