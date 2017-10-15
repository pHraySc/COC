package com.ailk.biapp.ci.market.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.market.dao.ICiMarketSceneHDao;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiMarketSceneHDaoImpl extends HibernateBaseDao<CiMarketScene, String> implements ICiMarketSceneHDao {
    public CiMarketSceneHDaoImpl() {
    }

    public List<CiMarketScene> queryFirstLevelScenes() {
        String hql = "from CiMarketScene where parentId is null order by sortNum asc";
        return this.find(hql, new Object[0]);
    }

    public List<CiMarketScene> querySecondScenesBySceneId(String sceneId) {
        String hql = "from CiMarketScene where parentId = ? order by sortNum asc";
        return this.find(hql, new Object[]{sceneId});
    }
}
