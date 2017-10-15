package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.model.CiLabelFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import java.util.List;

public interface ICiLabelFormAnalysisService {
    List<CiLabelFormModel> queryBrandFormChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormModel> querySubBrandFormChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormModel> queryCityFormChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormModel> queryVipFormChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormTrendModel> queryBrandTrendChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormTrendModel> queryCityTrendChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormTrendModel> queryVipTrendChartData(CiLabelFormModel var1) throws CIServiceException;

    List<CiBrandHisModel> queryBrandHistoryData(int var1, int var2, List<String> var3, CiLabelFormModel var4) throws CIServiceException;

    List<CiVipHisModel> queryVipHistoryData(List<String> var1, CiLabelFormModel var2) throws CIServiceException;

    List<CiLabelFormModel> queryCityTrendChartDataByCityId(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormModel> queryVipTrendChartDataByVipLevelId(CiLabelFormModel var1) throws CIServiceException;

    List<CiCityHisModel> queryCityHistoryData(List<String> var1, CiLabelFormModel var2) throws CIServiceException;

    long queryBrandHistoryDataCount(CiLabelFormModel var1) throws CIServiceException;

    long queryVipHistoryDataCount(CiLabelFormModel var1) throws CIServiceException;

    long queryCityHistoryDataCount(CiLabelFormModel var1) throws CIServiceException;

    List<CiLabelFormModel> queryMoreCityTrendChartDataByCityId(CiLabelFormModel var1) throws CIServiceException;
}
