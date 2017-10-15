package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.DimOpLogType;
import com.ailk.biapp.ci.model.CiLogStatAnalysisModel;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;
import java.util.Map;

public interface ICiLogStatAnalysisJDao {
    List<CiLogStatAnalysisModel> findAnaOpTypeInThePeriod(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findtitle(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findAllLogViewDataInThePeriod(Pager var1, CiLogStatAnalysisModel var2) throws Exception;

    List<CiLogStatAnalysisModel> findOpTypeChar(CiLogStatAnalysisModel var1) throws Exception;

    List<Map<String, Object>> findOneDeptOpLogTrend(CiLogStatAnalysisModel var1) throws Exception;

    List<Map<String, Object>> findLogUserDetilList(Pager var1, CiLogStatAnalysisModel var2) throws Exception;

    int findLogUserDetilListCount(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findOneOpLogTypeSpread(CiLogStatAnalysisModel var1) throws Exception;

    List<DimOpLogType> findOpTypeListByParentId(String var1) throws Exception;

    List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(Pager var1, CiLogStatAnalysisModel var2) throws Exception;

    int findAllSecondLogViewDataInThePeriodCount(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(CiLogStatAnalysisModel var1) throws Exception;

    List<Map<String, Object>> findLogUserDetilList(CiLogStatAnalysisModel var1) throws Exception;
}
