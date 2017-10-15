package com.ailk.biapp.ci.market.dao;

import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import java.util.List;

public interface ICiCustomLabelSceneRelHDao {
    void deleteCustomLabelRel(CiCustomLabelSceneRel var1);

    void insertCustomLabelRel(CiCustomLabelSceneRel var1);

    List<CiCustomLabelSceneRel> queryCustomLabelSceneRels(CiCustomLabelSceneRel var1);

    List<CiCustomLabelSceneRel> queryCustomLabelSceneRelsByType(String var1, Integer var2);

    List<CiMarketScene> selectMarketScene();

    List<CiMarketTask> queryCustomLabelSceneRelsById(CiCustomLabelSceneRel var1);
}
