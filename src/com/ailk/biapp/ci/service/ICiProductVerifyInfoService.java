package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiVerifyFilterProcess;
import com.ailk.biapp.ci.entity.CiVerifyMm;
import com.ailk.biapp.ci.entity.CiVerifyRuleInfo;
import com.ailk.biapp.ci.entity.CiVerifyRuleRel;
import com.ailk.biapp.ci.entity.DimVerifyIndexShow;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiProductVerifyInfoService {
    String queryRuleDesc(String var1) throws CIServiceException;

    List<CiVerifyRuleRel> queryVerifyRuleRelList(String var1) throws Exception;

    List<CiVerifyMm> queryVerifyMmList(String var1) throws CIServiceException;

    List<CiVerifyMm> queryVerifyMmResultList(String var1) throws CIServiceException;

    List<CiVerifyFilterProcess> queryLabelProcess(String var1) throws CIServiceException;

    List<CiVerifyFilterProcess> queryLabelProcessLevel(String var1, String var2) throws CIServiceException;

    List<CiVerifyRuleRel> queryFinalPotentialUserRule(String var1) throws CIServiceException;

    List<CiVerifyFilterProcess> queryIndexProcess(String var1) throws CIServiceException;

    List<CiVerifyFilterProcess> queryIndexProcessLevel(String var1, String var2) throws CIServiceException;

    List<CiVerifyRuleInfo> queryIndexRuleDescribe(String var1) throws CIServiceException;

    List<CiVerifyRuleInfo> queryLabelRule(String var1) throws CIServiceException;

    List<DimVerifyIndexShow> queryIndexList() throws CIServiceException;
}
