package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiListFailureInfo;
import java.util.Map;

public interface ICustomersListInfoService {
    void removeCustomerListBeforeMonth(int var1);

    CiCustomListInfo queryCustomerListInfoByListTableName(String var1);

    void updateCustomerListInfo(CiCustomListInfo var1);

    void deleteCustomerListInfo(CiCustomListInfo var1);

    void saveCiListFailureInfo(CiListFailureInfo var1);

    void updateCustomerListInfo(String var1, String var2, Integer var3, String var4, String var5, Map<String, String[]> var6);

    void updateCustomerListInfo(String var1, String var2, Integer var3);

    void addListTableNameColumn(String var1, Map<String, String> var2) throws Exception;
}
