package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiOperLogTempHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiOperLogTemp;
import org.springframework.stereotype.Repository;

@Repository
public class CiOperLogTempHDaoImpl extends HibernateBaseDao<CiOperLogTemp, String> implements ICiOperLogTempHDao {
    public CiOperLogTempHDaoImpl() {
    }

    public void save(CiOperLogTemp entity) {
        super.save(entity);
    }
}
