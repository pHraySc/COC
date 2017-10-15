package com.ailk.biapp.ci.ia.service;

import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.ia.model.WorkSheetModel;
import java.util.List;
import java.util.Map;

public interface IToushiAnalysisService {
    List<CiCustomListInfo> getCustomGroupList(String var1);

    Map<String, Object> generateChart(WorkSheetModel var1);
}
