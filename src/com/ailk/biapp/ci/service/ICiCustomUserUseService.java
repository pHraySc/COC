package com.ailk.biapp.ci.service;

public interface ICiCustomUserUseService {
    void addCustomUserUseLog(String var1, int var2, int var3);

    void insertCiCustomMonthUseStat(String var1);

    long getCustomUseTimesByID(String var1, int var2);
}
