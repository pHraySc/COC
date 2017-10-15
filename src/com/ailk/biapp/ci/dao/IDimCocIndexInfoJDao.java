package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimCocIndexInfo;
import java.util.HashMap;
import java.util.List;

public interface IDimCocIndexInfoJDao {
    int getDimCocIndexInfoCount(HashMap<String, Object> var1) throws Exception;

    List<DimCocIndexInfo> getDimCocIndexInfoList(int var1, int var2, HashMap<String, Object> var3) throws Exception;
}
