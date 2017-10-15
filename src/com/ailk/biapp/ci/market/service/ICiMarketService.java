package com.ailk.biapp.ci.market.service;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import java.util.List;

public interface ICiMarketService {
    List<CiMarketTask> findFirstLevelTasks() throws CIServiceException;

    List<CiMarketScene> findFirstLevelScenes() throws CIServiceException;

    List<CiMarketTask> findSecondTasksByTaskId(String var1) throws CIServiceException;

    void updateLabelScoreAndTimesAndTime() throws CIServiceException;

    void updateCustomScoreAndTimesAndTime() throws CIServiceException;

    List<CiMarketScene> findSecondScenesBySceneId(String var1) throws CIServiceException;

    void saveCustomOrLabelSceneRel(CiCustomLabelSceneRel var1) throws CIServiceException;

    boolean isRepeatScene(CiCustomLabelSceneRel var1) throws CIServiceException;

    void deleteCustomOrLabelSceneRel(CiCustomLabelSceneRel var1) throws CIServiceException;

    boolean isExistOtherSceneRel(CiCustomLabelSceneRel var1) throws CIServiceException;

    List<CiCustomLabelSceneRel> queryMarketIndexList(int var1, int var2, CiCustomLabelSceneRel var3) throws CIServiceException;

    List<CiCustomLabelSceneRel> queryCustomLabelSceneRelsByType(String var1, Integer var2) throws CIServiceException;

    int queryMarketIndexListCount(CiCustomLabelSceneRel var1);

    List<CiMarketTask> queryFirstLevelMarketTask();

    List<CiMarketTask> queryMarketTaskByParentId(String var1);

    CiMarketTask queryMarketTaskById(String var1);

    List<CiMarketScene> queryMarketScene();

    String queryLabelDataDate(CiLabelInfo var1);

    String queryCustomDataDate(CiCustomGroupInfo var1);
}
