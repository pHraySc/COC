package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiMdaSysTable;
import java.util.List;

public interface ICiMdaTableHDao {
    void insertTableInfo(CiMdaSysTable var1) throws Exception;

    List<CiMdaSysTable> selectAllCiMdaSysTable() throws Exception;

    CiMdaSysTable selectTableInfoByName(String var1) throws Exception;

    CiMdaSysTable selectTableInfoById(Integer var1) throws Exception;
}
