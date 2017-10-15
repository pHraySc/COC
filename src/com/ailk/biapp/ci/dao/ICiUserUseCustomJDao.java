package com.ailk.biapp.ci.dao;

public interface ICiUserUseCustomJDao {
    Long getCustomUseTimesById(String var1, int var2);

    void insertCiCustomUseStat(String var1);

    void deleteCiCustomUseStat(String var1);
}
