package com.ailk.biapp.ci.dao;

public interface ILoginJDao {
    void saveOrUpdateLoginHistory(String var1, String var2, String var3, String var4, String var5, String var6) throws Exception;

    String checkBrowse(String var1);
}
