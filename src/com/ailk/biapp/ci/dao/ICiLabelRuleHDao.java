package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelRule;
import java.util.List;

public interface ICiLabelRuleHDao {
    void insertCiLabelRule(CiLabelRule var1);

    void deleteCiLabelRuleListByCustomerOrTemplateId(String var1, Integer var2);

    List<CiLabelRule> selectCiLabelRuleList(String var1, Integer var2);

    List<CiLabelRule> selectCiLabelRuleListByLabelId(String var1, Integer var2);
}
