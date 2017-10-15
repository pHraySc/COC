package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiOperLoginTempHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiOperLoginTemp;
import org.springframework.stereotype.Repository;

@Repository
public class CiOperLoginTempHDaoImpl extends HibernateBaseDao<CiOperLoginTemp, String> implements ICiOperLoginTempHDao {
    public CiOperLoginTempHDaoImpl() {
    }

    public void save(CiOperLoginTemp entity) {
        super.save(entity);
    }
}
