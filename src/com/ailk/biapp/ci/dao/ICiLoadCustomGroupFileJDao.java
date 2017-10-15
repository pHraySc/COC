package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import java.util.List;

public interface ICiLoadCustomGroupFileJDao {
    String createTable(String var1, String var2, String var3);

    void batchUpdateMobileList(List<String[]> var1, String var2, String var3);

    void deleteAllData(String var1);

    boolean tableExists(String var1);

    int batchInsert2CustListTab(String var1, String var2, String var3) throws Exception;

    List<String> addAttr2TmpTable(String var1, List<CiGroupAttrRel> var2);
}
