package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import java.util.List;

public interface ICiCustomListExeInfoHDao {
    CiCustomListExeInfo selectCiCustomListExeInfoById(String var1);

    void insertCiCustomListExeInfo(CiCustomListExeInfo var1);

    void deleteByListTableName(String var1);

    List<CiCustomListExeInfo> queryListExeInfosByTableName(String var1);
}
