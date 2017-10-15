package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiTaskServerInfo;
import java.util.List;

public interface ICiTaskServerInfoHDao {
    CiTaskServerInfo selectById(String var1);

    List<CiTaskServerInfo> select();

    List<CiTaskServerInfo> selectByIsExeTask(int var1);
}
