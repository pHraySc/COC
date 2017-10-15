package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaChartIndexRule;
import java.util.List;

public interface ICiIaChartIndexRuleHDao {
    List<CiIaChartIndexRule> selectBySheetChartRelId(int var1);

    void deleteBySheetChartRelId(int var1);

    void insert(CiIaChartIndexRule var1);

    void inset(List<CiIaChartIndexRule> var1);
}
