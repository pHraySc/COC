package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiMarketTactics;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiMarketTacticsService {
    boolean isNameExit(String var1) throws CIServiceException;

    List<CiMarketTactics> queryCiMarketTacticsName(String var1, String var2, String var3) throws Exception;

    long queryCiMarketTacticsCount(CiMarketTactics var1) throws Exception;

    List<CiMarketTactics> queryCiMarketTacticsList(int var1, int var2, CiMarketTactics var3) throws Exception;
}
