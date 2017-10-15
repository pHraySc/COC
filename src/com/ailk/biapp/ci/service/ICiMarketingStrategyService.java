package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomProductTacticsRel;
import com.ailk.biapp.ci.entity.CiMarketTactics;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.util.GroupCalcSqlPaser;
import java.util.List;

public interface ICiMarketingStrategyService {
    boolean isCustomProductMatch(CiCustomGroupInfo var1) throws CIServiceException;

    List<CustomProductMatch> querySysProductMatch(CiCustomGroupInfo var1) throws CIServiceException;

    CustomProductMatch getProductMatch(CiCustomGroupInfo var1, String var2, GroupCalcSqlPaser var3, Integer var4) throws CIServiceException;

    void saveCustomProductRel(String var1, String[] var2, String var3) throws CIServiceException;

    void saveCustomProductTacticsRel(CiMarketTactics var1, List<CiCustomProductTacticsRel> var2) throws CIServiceException;

    void removeCustomProductMatchByListTableName(String var1);

    void saveCustomProductMatch(CustomProductMatch var1);
}
