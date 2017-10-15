package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiEnumConditionInfo;
import java.util.List;

public interface ICiEnumConditionInfoHDao {
    void insertCiEnumConditionInfo(CiEnumConditionInfo var1);

    List<CiEnumConditionInfo> selectCiEnumConditionInfoList(String var1);

    CiEnumConditionInfo selectById(String var1);

    void deleteCiEnumConditionInfoListByenumCategoryId(String var1);
}
