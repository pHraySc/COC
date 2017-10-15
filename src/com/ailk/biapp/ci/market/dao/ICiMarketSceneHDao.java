package com.ailk.biapp.ci.market.dao;

import com.ailk.biapp.ci.market.entity.CiMarketScene;
import java.util.List;

public interface ICiMarketSceneHDao {
    List<CiMarketScene> queryFirstLevelScenes();

    List<CiMarketScene> querySecondScenesBySceneId(String var1);
}
