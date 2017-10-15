package com.ailk.biapp.ci.ia.dao;

import com.ailk.biapp.ci.ia.entity.CiIaAnalyseAttr;
import java.util.List;

public interface ICiIaAnalyseAttrHDao {
    List<CiIaAnalyseAttr> selectEffectiveAttrBySource(int var1);

    void insert(CiIaAnalyseAttr var1);
}
