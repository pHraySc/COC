package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiCustomRelModel;
import com.ailk.biapp.ci.model.CiCustomersFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.model.CustomGroupContrastDetailInfo;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import java.util.List;

public interface ICustomersAnalysisService {
    int queryAssociationAnalysis(String var1, String var2) throws CIServiceException;

    void queryRelAnalysis(String var1, String var2, CiCustomRelModel var3) throws CIServiceException;

    void saveAssociationAnalysis(String var1, String var2, String var3, String var4) throws CIServiceException;

    void saveCompareAnalysis(String var1, String var2, String var3) throws CIServiceException;

    void delUserCustomContrast(String var1, String var2, String var3) throws CIServiceException;

    void updateCiUserCustomContrast(String var1, String var2) throws CIServiceException;

    List<LabelShortInfo> queryCustomersCompareAnalysis(String var1, String var2, String var3) throws CIServiceException;

    String saveCustomersAnalsisRelation(String var1, String var2, String var3) throws CIServiceException;

    List<CiCustomRelModel> queryRelCustom(String var1, CiCustomRelModel var2) throws CIServiceException;

    CustomGroupContrastDetailInfo queryCompareAnalysis(String var1, String var2, CustomGroupContrastDetailInfo var3) throws CIServiceException;

    List<LabelTrendInfo> queryTrendAnalysis(String var1, String var2, int var3) throws CIServiceException;

    int selectCustomersTotalByTrendAnalysisCount(String var1, String var2, int var3) throws CIServiceException;

    List<LabelTrendInfo> queryTrendAnalysis(String var1, String var2, int var3, int var4, int var5) throws CIServiceException;

    List<CiCustomGroupForm> queryBrandFormChartData(CiCustomGroupForm var1) throws CIServiceException;

    List<CiCustomersFormTrendModel> queryBrandTrendChartData(CiCustomGroupForm var1) throws CIServiceException;

    int queryBrandHistoryDataCount(CiCustomGroupForm var1) throws CIServiceException;

    int queryCityHistoryDataCount(CiCustomGroupForm var1) throws CIServiceException;

    List<CiCustomGroupForm> querySubBrandFormChartData(CiCustomGroupForm var1) throws CIServiceException;

    List<CiBrandHisModel> selectBrandHistoryData(int var1, int var2, List<String> var3, CiCustomGroupForm var4) throws CIServiceException;

    List<CiCustomGroupForm> queryCityFormChartData(CiCustomGroupForm var1) throws CIServiceException;

    List<CiCustomGroupForm> queryCityTrendChartDataByCityId(CiCustomGroupForm var1) throws CIServiceException;

    List<CiCityHisModel> queryCityHistoryData(List<String> var1, CiCustomGroupForm var2) throws CIServiceException;

    List<CiCustomersFormTrendModel> queryCityTrendChartData(CiCustomGroupForm var1) throws CIServiceException;

    List<CiCustomGroupForm> queryVipFormChartData(CiCustomGroupForm var1) throws CIServiceException;

    List<CiCustomersFormTrendModel> queryVipTrendChartData(CiCustomGroupForm var1) throws CIServiceException;

    int queryVipHistoryDataCount(CiCustomGroupForm var1) throws CIServiceException;

    List<CiVipHisModel> queryVipHistoryData(List<String> var1, CiCustomGroupForm var2) throws CIServiceException;

    List<CiCustomGroupForm> queryVipTrendChartDataByVipLevelId(CiCustomGroupForm var1) throws CIServiceException;

    String queryLabelBrandUserNumById(String var1, String var2) throws CIServiceException;

    List<CiCustomGroupForm> queryMoreCityTrendChartDataByCityId(CiCustomGroupForm var1) throws CIServiceException;

    String getCustomersTrendChartJson(String var1, String var2, int var3) throws CIServiceException;
}
