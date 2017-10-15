package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiEnumCategoryInfo;
import java.util.List;

public interface ICiEnumCategoryInfoHDao {
    void insertCiEnumCategoryInfo(CiEnumCategoryInfo var1);

    List<CiEnumCategoryInfo> selectCiEnumCategoryInfoList(String var1, Integer var2);

    CiEnumCategoryInfo selectById(String var1);
}
