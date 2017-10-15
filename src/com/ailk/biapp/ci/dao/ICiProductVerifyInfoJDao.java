package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiVerifyFilterProcess;
import com.ailk.biapp.ci.entity.CiVerifyMm;
import com.ailk.biapp.ci.entity.CiVerifyRuleInfo;
import com.ailk.biapp.ci.entity.CiVerifyRuleRel;
import com.ailk.biapp.ci.entity.DimVerifyIndexShow;
import java.util.List;

public interface ICiProductVerifyInfoJDao {
    String selectRuleDesc(String var1) throws Exception;

    List<CiVerifyRuleRel> selectVerifyRuleRelList(String var1) throws Exception;

    List<CiVerifyMm> selectVerifyMmList(String var1) throws Exception;

    List<CiVerifyFilterProcess> selectLabelProcess(String var1) throws Exception;

    List<CiVerifyFilterProcess> selectLabelProcessLevel(String var1, String var2) throws Exception;

    List<CiVerifyRuleInfo> selectLabelRule(String var1) throws Exception;

    List<CiVerifyFilterProcess> selectIndexProcess(String var1) throws Exception;

    List<CiVerifyFilterProcess> selectIndexProcessLevel(String var1, String var2) throws Exception;

    List<CiVerifyRuleRel> selectFinalPotentialUserRule(String var1) throws Exception;

    List<CiVerifyRuleInfo> selectIndexRuleDescribe(String var1) throws Exception;

    List<DimVerifyIndexShow> selectIndexList() throws Exception;
}
