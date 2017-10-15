package com.ailk.biapp.ci.market.dao;

import com.ailk.biapp.ci.market.entity.CiMarketTask;
import java.util.List;

public interface ICiMarketTaskHDao {
    List<CiMarketTask> getFirstLevelTasks();

    List<CiMarketTask> querySecondTasksByTaskId(String var1);

    CiMarketTask selectMarketTaskById(String var1);
}
