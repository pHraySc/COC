package com.ailk.biapp.ci.dao;

import java.util.List;
import java.util.Map;

public interface ICiLabelRuleJDao {
    int getIsUsingCustomCount(String var1);

    int getIsUsingCustomCount(String var1, String var2);

    void batchUpdateValueList(List<String> var1, String var2, String var3);

    List<Map<String, Object>> findValueListData(String var1);
}
