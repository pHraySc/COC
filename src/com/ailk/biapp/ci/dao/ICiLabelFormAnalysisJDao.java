package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.model.CiLabelFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import java.util.List;

public interface ICiLabelFormAnalysisJDao {
    List<CiLabelFormModel> selectBrandFormChartData(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormModel> selectSubBrandFormChartData(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormModel> selectCityFormChartData(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormModel> selectVipFormChartData(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormTrendModel> selectBrandTrendChartData(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormTrendModel> selectCityTrendChartData(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormTrendModel> selectVipTrendChartData(CiLabelFormModel var1) throws Exception;

    List<CiBrandHisModel> selectBrandHistoryData(int var1, int var2, List<String> var3, CiLabelFormModel var4) throws Exception;

    List<CiVipHisModel> selectVipHistoryData(List<String> var1, CiLabelFormModel var2) throws Exception;

    List<CiLabelFormModel> selectCityTrendChartDataByCityId(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormModel> selectVipTrendChartDataByVipLevelId(CiLabelFormModel var1) throws Exception;

    List<CiCityHisModel> selectCityHistoryData(List<String> var1, CiLabelFormModel var2) throws Exception;

    long selectBrandHistoryDataCount(CiLabelFormModel var1) throws Exception;

    long selectVipHistoryDataCount(CiLabelFormModel var1) throws Exception;

    long selectCityHistoryDataCount(CiLabelFormModel var1) throws Exception;

    List<CiLabelFormModel> selectMoreCityTrendChartDataByCityId(CiLabelFormModel var1) throws Exception;
}
