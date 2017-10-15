package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiEnumCategoryInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface ICiEnumCategoryInfoJDao {
    int selectEnumCategoryInfoTotalCountByColumnId(String var1, String var2, Integer var3) throws Exception;

    List<CiEnumCategoryInfo> selectEnumCategoryInfoPagerListByColumnId(Pager var1, String var2, String var3, Integer var4) throws Exception;

    List<CiEnumCategoryInfo> queryEnumCategoryInfoListByCityIdAndColumnId(String var1, Integer var2, String var3);
}
