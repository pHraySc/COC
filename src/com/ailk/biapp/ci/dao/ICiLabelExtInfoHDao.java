package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelExtInfo;

public interface ICiLabelExtInfoHDao {
    void insertCiLabelExtInfo(CiLabelExtInfo var1);

    void updateCiLabelExtInfo(CiLabelExtInfo var1);

    CiLabelExtInfo getById(Integer var1);

    void updateCiLabelExtInfoByLabelIds(String var1, Integer var2);
}
