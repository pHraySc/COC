package com.ailk.biapp.ci.market.dao.impl;

import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.market.dao.ICiMarketTaskHDao;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiMarketTaskHDaoImpl extends HibernateBaseDao<CiMarketTask, String> implements ICiMarketTaskHDao {
    public CiMarketTaskHDaoImpl() {
    }

    public List<CiMarketTask> getFirstLevelTasks() {
        String hql = "from CiMarketTask where parentId is null order by sortNum asc";
        return this.find(hql, new Object[0]);
    }

    public List<CiMarketTask> querySecondTasksByTaskId(String taskId) {
        String hql = "from CiMarketTask where parentId = ? order by sortNum asc";
        return this.find(hql, new Object[]{taskId});
    }

    public CiMarketTask selectMarketTaskById(String marketTaskId) {
        String hql = "from CiMarketTask where marketTaskId = ?";
        return (CiMarketTask)this.findUnique(hql, new Object[]{marketTaskId});
    }
}
