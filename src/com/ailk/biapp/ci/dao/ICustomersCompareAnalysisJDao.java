package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.LabelShortInfo;
import java.util.List;

public interface ICustomersCompareAnalysisJDao {
    List<LabelShortInfo> getCustomersCompareAnalysis(String var1, String var2, String var3) throws Exception;
}
