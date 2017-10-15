package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.DimCocIndexInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.HashMap;
import java.util.List;

public interface IDimCocIndexInfoService {
    int queryDimCocIndexInfoCount(HashMap<String, Object> var1) throws CIServiceException;

    List<DimCocIndexInfo> queryDimCocIndexInfoList(int var1, int var2, HashMap<String, Object> var3) throws CIServiceException;
}
