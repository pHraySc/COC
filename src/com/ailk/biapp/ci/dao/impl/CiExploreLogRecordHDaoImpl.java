package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiExploreLogRecordHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiExploreLogRecord;
import org.springframework.stereotype.Repository;

@Repository
public class CiExploreLogRecordHDaoImpl extends HibernateBaseDao<CiExploreLogRecord, Integer> implements ICiExploreLogRecordHDao {
    public CiExploreLogRecordHDaoImpl() {
    }

    public void insertCiExploreLogRecord(CiExploreLogRecord bean) {
        this.save(bean);
    }
}
