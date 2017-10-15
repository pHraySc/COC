package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaFilter;
import java.util.List;

public interface ICiIaFilterHDao {
    List<CiIaFilter> selectBySheetChartRelId(int var1);

    void deleteBySheetChartRelId(int var1);

    void insert(CiIaFilter var1);

    void insert(List<CiIaFilter> var1);
}
