package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomListInfo;
import java.util.List;

public interface ICiCustomListInfoHDao {
    List<CiCustomListInfo> selectByCustomListInfo(CiCustomListInfo var1);

    List<CiCustomListInfo> selectByCustomGroupId(String var1);

    List<CiCustomListInfo> selectSuccessByCustomGroupId(String var1);

    CiCustomListInfo selectByCustomGroupIdAndDataDate(String var1, String var2);

    List<CiCustomListInfo> selectByCustomGroupIdAndBeforeDataDate(String var1, String var2);

    void insertCiCustomListInfo(CiCustomListInfo var1);

    void updateCiCustomListInfo(CiCustomListInfo var1);

    CiCustomListInfo selectById(String var1);

    void deleteCiCustomListInfoByCustomerId(String var1);

    void delete(CiCustomListInfo var1);

    List<CiCustomListInfo> selectAll();

    List<CiCustomListInfo> select(String var1, Object... var2);
}
