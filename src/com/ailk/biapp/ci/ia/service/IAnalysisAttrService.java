package com.ailk.biapp.ci.ia.service;

import com.ailk.biapp.ci.ia.entity.CiIaAnalyseAttr;
import com.ailk.biapp.ci.ia.model.AttributeModel;
import java.util.List;

public interface IAnalysisAttrService {
    List<CiIaAnalyseAttr> selectEffectiveAttrBySource(int var1);

    List<AttributeModel> getPublicAttr();

    List<AttributeModel> getCustomAttr(String var1);
}
