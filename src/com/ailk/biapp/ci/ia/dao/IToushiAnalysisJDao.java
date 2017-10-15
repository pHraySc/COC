package com.ailk.biapp.ci.ia.dao;

import java.util.List;
import java.util.Map;

public interface IToushiAnalysisJDao {
    List<Map<String, Object>> getData(String var1, Object[] var2);

    List<Map<String, Object>> getData(String var1);
}
