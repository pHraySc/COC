package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiTaskServerCityRelHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiTaskServerCityRel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CiTaskServerCityRelHDaoImpl extends HibernateBaseDao<CiTaskServerCityRel, Integer> implements ICiTaskServerCityRelHDao {
    public CiTaskServerCityRelHDaoImpl() {
    }

    public List<CiTaskServerCityRel> selectListByServerId(String serverId) {
        List list = this.find("from CiTaskServerCityRel u where u.id.serverId = ?", new Object[]{serverId});
        return list;
    }

    public List<CiTaskServerCityRel> selectAllList() {
        List list = this.getAll();
        return list;
    }

    public CiTaskServerCityRel selectById(Integer cityId) {
        CiTaskServerCityRel ciTaskServerCityRel = (CiTaskServerCityRel)this.get(cityId);
        return ciTaskServerCityRel;
    }
}
