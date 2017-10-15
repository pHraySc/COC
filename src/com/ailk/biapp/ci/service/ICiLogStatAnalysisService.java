package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.DimOpLogType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiLogStatAnalysisModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.TreeNode;
import java.util.List;
import java.util.Map;

public interface ICiLogStatAnalysisService {
    List<CiLogStatAnalysisModel> findAnaOpTypeInThePeriod(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findtitle(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findAllLogViewDataInThePeriod(Pager var1, CiLogStatAnalysisModel var2) throws Exception;

    List<CiLogStatAnalysisModel> findOpTypeChar(CiLogStatAnalysisModel var1) throws Exception;

    List<Map<String, Object>> findOneDeptOpLogTrend(CiLogStatAnalysisModel var1) throws Exception;

    List<Map<String, Object>> findLogUserDetilList(Pager var1, CiLogStatAnalysisModel var2) throws Exception;

    int findLogUserDetilListCount(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findOneOpLogTypeSpread(CiLogStatAnalysisModel var1) throws Exception;

    List<DimOpLogType> findOpTypeListByParentId(String var1) throws Exception;

    List<TreeNode> queryDeptTree() throws CIServiceException;

    List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(Pager var1, CiLogStatAnalysisModel var2) throws Exception;

    int findAllSecondLogViewDataInThePeriodCount(CiLogStatAnalysisModel var1) throws Exception;

    List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(CiLogStatAnalysisModel var1) throws Exception;

    List<Map<String, Object>> findLogUserDetilList(CiLogStatAnalysisModel var1) throws Exception;
}
