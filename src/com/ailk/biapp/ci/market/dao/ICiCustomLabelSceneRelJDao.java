package com.ailk.biapp.ci.market.dao;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import java.util.List;

public interface ICiCustomLabelSceneRelJDao {
    void modifyLabelSceneRel(List<CiLabelInfo> var1) throws Exception;

    void modifyCustomSceneRel(List<CiCustomGroupInfo> var1) throws Exception;

    List<CiCustomLabelSceneRel> selectCiCustomLabelSceneRel(int var1, int var2, CiCustomLabelSceneRel var3) throws Exception;

    int selectCiCustomLabelSceneRelCount(CiCustomLabelSceneRel var1);

    List<CiMarketTask> selectFistLevelMarketTask();

    List<CiMarketTask> selectMarketTaskByParentId(String var1);
}
