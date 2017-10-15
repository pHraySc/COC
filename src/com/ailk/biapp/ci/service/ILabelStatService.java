package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.DimCocLabelStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ILabelStatService {
    void statLabelCustomNum(String var1, int var2) throws CIServiceException;

    void statLabelRingNum(String var1, int var2) throws CIServiceException;

    void statLabelProportion(String var1, int var2) throws CIServiceException;

    void updateLabelTotalCustomNum(String var1, int var2) throws CIServiceException;

    int dataDateExist(String var1) throws CIServiceException;

    void updateDwNullDataTo0ForMonth(String var1) throws CIServiceException;

    void updateDwNullDataTo0ForDay(String var1) throws CIServiceException;

    void updateLabelStatDateStatus(int var1) throws CIServiceException;

    int updateCiLabelInfoDataDate(List<DimCocLabelStatus> var1, String var2);
}
