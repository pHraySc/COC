package com.ailk.biapp.ci.dao;

public interface ILabelStatDao {
    void statLabelCustomNum(String var1, int var2) throws Exception;

    void statLabelRingNum(String var1, int var2) throws Exception;

    void statLabelProportion(String var1, int var2) throws Exception;

    void updateLabelTotalCustomNum(String var1, int var2) throws Exception;

    int dataDateExist(String var1) throws Exception;

    void updateDwNullDataTo0ForMonth(String var1) throws Exception;

    void updateDwNullDataTo0ForDay(String var1) throws Exception;

    void updateLabelStatDateStatus(int var1) throws Exception;
}
