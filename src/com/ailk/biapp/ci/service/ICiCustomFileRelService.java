package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiCustomFileRelService {
    void saveCustomFileRel(CiCustomFileRel var1) throws CIServiceException;

    CiCustomFileRel queryCustomFileByCustomGroupId(String var1) throws CIServiceException;

    CiCustomFileRel queryCustomFile(String var1) throws CIServiceException;

    String createTable(String var1, String var2, String var3, List<CiGroupAttrRel> var4) throws CIServiceException;

    void deleteAllData(String var1) throws CIServiceException;

    void batchUpdateMobileList(List<String[]> var1, String var2, String var3) throws CIServiceException;

    int batchInsert2CustListTab(String var1, String var2, String var3) throws CIServiceException;

    List<String> addAttr2TmpTable(String var1, List<CiGroupAttrRel> var2) throws CIServiceException;
}
