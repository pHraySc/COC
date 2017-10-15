package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiCustomersFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import java.util.List;

public interface ICiCustomGroupFormJDao {
    List<CiCustomersFormTrendModel> queryBrandFormChartData(CiCustomGroupForm var1) throws Exception;

    List<CiCustomersFormTrendModel> selectBrandTrendChartData(CiCustomGroupForm var1) throws Exception;

    List<CiCustomGroupForm> selectSubBrandFormChartData(CiCustomGroupForm var1) throws Exception;

    int selectBrandHistoryDataCount(CiCustomGroupForm var1) throws Exception;

    int selectCityHistoryDataCount(CiCustomGroupForm var1) throws Exception;

    int selectVipHistoryDataCount(CiCustomGroupForm var1) throws Exception;

    List<CiBrandHisModel> selectBrandHistoryData(int var1, int var2, List<String> var3, CiCustomGroupForm var4) throws Exception;

    List<CiCustomersFormTrendModel> selectCityFormChartData(CiCustomGroupForm var1) throws Exception;

    List<CiCustomersFormTrendModel> selectCityTrendChartData(CiCustomGroupForm var1) throws Exception;

    List<CiCustomGroupForm> selectCityTrendChartDataByCityId(CiCustomGroupForm var1) throws Exception;

    List<CiCityHisModel> selectCityHistoryData(List<String> var1, CiCustomGroupForm var2) throws Exception;

    List<CiCustomersFormTrendModel> selectVipFormChartData(CiCustomGroupForm var1) throws Exception;

    List<CiCustomersFormTrendModel> selectVipTrendChartData(CiCustomGroupForm var1) throws Exception;

    int selectCustomersTotalByTrendAnalysisCount(String var1, String var2, int var3) throws Exception;

    int selectCustomersTotalByCustomerListCount(String var1, String var2, int var3) throws Exception;

    List<LabelTrendInfo> selectCustomersTotalByCustomerListPage(String var1, String var2, int var3, String var4, Integer var5, Integer var6) throws Exception;

    List<LabelTrendInfo> selectCustomersTotalByCustomerList(String var1, String var2, int var3, String var4) throws Exception;

    List<LabelTrendInfo> selectCustomersTotalByTrendAnalysis(String var1, String var2, int var3) throws Exception;

    List<LabelTrendInfo> selectCustomersTotalByTrendAnalysis(String var1, String var2, int var3, int var4, int var5) throws Exception;

    List<CiVipHisModel> selectVipHistoryData(List<String> var1, CiCustomGroupForm var2) throws Exception;

    List<CiCustomGroupForm> selectVipTrendChartDataByVipLevelId(CiCustomGroupForm var1) throws Exception;

    List<CiCustomGroupForm> selectMoreCityTrendChartDataByCityId(CiCustomGroupForm var1) throws Exception;

    void deleteByListTableName(String var1);

    void deleteByCustomGroupIdAndDataDate(String var1, String var2);
}
