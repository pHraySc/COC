package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelHistoryInfoHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiLabelHistoryInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelHistoryInfoHDaoImpl extends HibernateBaseDao<CiLabelHistoryInfo, Integer> implements ICiLabelHistoryInfoHDao {
    public CiLabelHistoryInfoHDaoImpl() {
    }

    public void insertCiLabelHistoryInfo(CiLabelHistoryInfo ciLabelHistoryInfo) {
        this.save(ciLabelHistoryInfo);
    }
}
