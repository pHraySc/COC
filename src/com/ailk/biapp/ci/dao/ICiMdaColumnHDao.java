package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;

public interface ICiMdaColumnHDao {
    void insertColumnInfo(CiMdaSysTableColumn var1) throws Exception;

    CiMdaSysTableColumn selectCiMdaSysTableColumnByColumnId(Integer var1);

    void deleteCiMdaSysTableColumnByColumnId(Integer var1);
}
