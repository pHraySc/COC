package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.exception.CIServiceException;

public interface ICustomGroupStatJDao {
    void statCustomGroupCustomNum(String var1, int var2) throws CIServiceException;

    void statCustomGroupRingNum(String var1, int var2) throws CIServiceException;

    void statCustomGroupRingNumById(String var1, String var2, String var3) throws CIServiceException;

    void statCustomGroupCustomNumById(String var1, String var2, String var3) throws CIServiceException;

    int dataDateExist(String var1) throws CIServiceException;

    void insertCustomGroupNotExistData(String var1, String var2) throws CIServiceException;

    void deleteCustomGroupData(String var1, String var2) throws CIServiceException;
}
