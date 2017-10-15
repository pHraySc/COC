package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimCocLabelCountRules;

public interface ICiLabelCountRulesHDao {
    void insertDimCocLabelCountRule(DimCocLabelCountRules var1) throws Exception;

    DimCocLabelCountRules selectDimCocLabelCountRule(String var1) throws Exception;

    String selectMaxLabelCountRule() throws Exception;
}
