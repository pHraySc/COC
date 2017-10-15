package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiMarketTactics;
import java.util.List;

public interface ICiMarketTacticsJDao {
    boolean isNameExit(String var1) throws Exception;

    List<CiMarketTactics> selectCiMarketTacticsName(String var1, String var2, String var3) throws Exception;

    int selectCiMarketTacticsCount(CiMarketTactics var1) throws Exception;

    List<CiMarketTactics> selectCiMarketTacticsList(int var1, int var2, CiMarketTactics var3) throws Exception;
}
