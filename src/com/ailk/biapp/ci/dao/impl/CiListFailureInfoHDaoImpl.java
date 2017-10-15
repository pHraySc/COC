package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiListFailureInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiListFailureInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CiListFailureInfoHDaoImpl extends HibernateBaseDao<CiListFailureInfo, Integer> implements ICiListFailureInfoHDao {
    public CiListFailureInfoHDaoImpl() {
    }

    public void saveCiListFailureInfo(CiListFailureInfo ciListFailureInfo) {
        this.save(ciListFailureInfo);
    }
}
