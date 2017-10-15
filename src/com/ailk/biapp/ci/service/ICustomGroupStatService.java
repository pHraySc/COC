package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.exception.CIServiceException;

public interface ICustomGroupStatService {
    void statCustomGroupCustomNum(String var1, int var2) throws CIServiceException;

    void statCustomGroupRingNum(String var1, int var2) throws CIServiceException;

    void statCustomGroupCustomNumById(String var1, String var2, String var3) throws CIServiceException;

    int dataDateExist(String var1) throws CIServiceException;

    void initCustomGroupNotExistStatData(String var1, String var2) throws CIServiceException;

    void deleteCustomGroupData(String var1, String var2);
}
