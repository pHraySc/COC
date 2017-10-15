package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiExploreSqlAllHDao;
import com.ailk.biapp.ci.dao.base.HibernateBaseDao;
import com.ailk.biapp.ci.entity.CiExploreSqlAll;
import org.springframework.stereotype.Repository;

@Repository
public class CiExploreSqlAllHDaoImpl extends HibernateBaseDao<CiExploreSqlAll, Integer> implements ICiExploreSqlAllHDao {
    public CiExploreSqlAllHDaoImpl() {
    }

    public void insertCiExploreSqlAll(CiExploreSqlAll ciExploreSqlAll) {
        this.save(ciExploreSqlAll);
    }
}
