package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiListExeSqlAll;
import java.util.List;

public interface ICiListExeSqlAllHDao {
    void insertCiListExeSqlAll(CiListExeSqlAll var1);

    List<CiListExeSqlAll> selectCiListExeSqlAllByExeInfoId(String var1);
}
