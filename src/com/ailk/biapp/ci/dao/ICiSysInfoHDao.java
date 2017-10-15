package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiSysInfo;
import java.util.List;

public interface ICiSysInfoHDao {
    CiSysInfo selectByName(String var1);

    CiSysInfo selectById(String var1);

    void insertCiSysInfo(CiSysInfo var1);

    List<CiSysInfo> selectByShowOrNOt(int var1);
}
